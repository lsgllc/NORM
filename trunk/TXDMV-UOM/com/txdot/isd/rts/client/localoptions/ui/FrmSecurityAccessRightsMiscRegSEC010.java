package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.services.data.SecurityClientDataObject;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSecurityAccessRightsMiscRegSEC010.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup.
 * 							Version 5.2.0	
 * Min Wang		03/16/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang		04/06/2005	Remove arrays because they are causing 
 * 							initialization errors.
 * 							delete carrRTSBtn, carrBtnSec
 * 							modify initialize(), keyPressed()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	remove unused method
 * 							delete disableBtnOnOfcId(), setController()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Min Wang		08/31/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		12/12/2005  Uncomment code for arrow key function.
 * 							modify keyPressed(), initialize()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	08/15/2006	Correct misspelling of "Additional" 
 * 							modify TEMP_ADD_WEIGHT
 * 							defect 8891 Ver 5.2.4 
 * K Harrell	10/27/2008	Add checkboxes, processing for new Disabled
 * 							Placard 
 * 							add ivjchkPlacardManagement, ivjchkReport
 * 							add ivjchkPlacardInquiry
 * 							add getchkPlacardManagement(), getchkReport(), 
 * 							  getchkPlacardInquiry() 
 * 							delete cbSelectAll, ivjchkDisabledParkingCard
 * 							delete getBuilderData() 
 * 							refactor ciEnableCount to ciSelectCount,
 * 							 ivjchkDisabledParkingCard to 
 * 							 ivjchkDisabledPlacard,getchkDisabledParkingCard()
 * 							  to getchkDisabledPlacard()
 * 							modify carrBtnSec[]
 * 							modify initialize(), keyPressed(), 
 * 							  checkCountZero(), selectChkbuttons(), 
 * 							  actionPerformed(), setData(),
 * 							  getFrmSecurityAccessRightsMiscRegSEC010ContentPane1(),
 *							  setValuesToDataObj()
 *							defect 9831 Ver Defect_POS_B
 * K Harrell	06/18/2011	Add checkbox, processing for TimedPermit Modify 
 * 							add ivjchkModifyTimedPermit, get method
 *							add MOD_TIMED_PRMT
 *							add caCheckBoxButtonGroup
 *							delete carrBtnSec, keyPressed(), ciSelect,
 *							  ciSelectSec 
 *							modify carrBtnSec
 *							modify actionPerformed(), initialize(), 
 *							 setChkButtons(), setData(), 
 *							 setValuesToDataObj(),
 *							 getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
 *							defect 10844 Ver 6.8.0 
 * K Harrell	01/11/2011	Add checkbox, processing for Reinstate Placard 
 * 							add ivjchkPlacardReinstate, get method
 *							add PLACARD_REINSTATE
 *							modify actionPerformed(), initialize(), 
 *							 setChkButtons(), setData(), 
 *							 setValuesToDataObj(),selectChkButtons(), 
 *							 getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
 *							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for
 * Miscellaneous Registration. Data on this screen is managed through
 * SecurityClientDataObject.
 * 
 * @version	6.10.0			01/11/2012
 * @author	Ashish Majajan
 * <br>Creation Date:		06/28/2001 12:50:18 
 */

public class FrmSecurityAccessRightsMiscRegSEC010
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkDisabledPlacard = null;
	private JCheckBox ivjchkMiscRegistration = null;
	private JCheckBox ivjchkNonResAgriPermit = null;
	private JCheckBox ivjchkPlacardInquiry = null;
	private JCheckBox ivjchkPlacardManagement = null;
	private JCheckBox ivjchkReport = null;
	private JCheckBox ivjchkTempAddWeight = null;
	private JCheckBox ivjchkTimedPermit = null;
	private JCheckBox ivjchkTowTruck = null;
	private JPanel ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1 =
		null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblMiscReg = null;

	// int 	
	private int ciSelectedCount = 0;

	// Object 
	private SecurityClientDataObject caSecData = null;

	// Constants 
	private static final String DISABLED_PLACARD = "Disabled Placard";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String LABEL1 = "JLabel1";
	private static final String LABEL2 = "JLabel2";
	private static final String MISC_REG = "Miscellaneous Registration";
	private static final String NON_RESIDENT_AGR_PERMIT =
		"Non-Resident Agriculture Permit";
	private static final String SEC010_FRAME_TITLE =
		"Security Access Rights Misc Registration   SEC010";
	private static final String TEMP_ADD_WEIGHT =
		"Temporary Additional Weight";
	private static final String TIMED_PERMIT = "Timed Permit";
	private static final String TOW_TRUCK = "Tow Truck";

	// defect 10844
	private RTSButtonGroup caCheckBoxButtonGroup; 
	private JCheckBox ivjchkModifyTimedPermit = null;
	private static final String MOD_TIMED_PRMT = "Permit Modify";
	// end defect 10844
	
	// defect 11214 
	private static final String PLACARD_REINSTATE = "Placard Reinstate";
	private JCheckBox ivjchkPlacardReinstate = null;
	// end defect 11214 

	/**
	 * This method initializes ivjchkPlacardReinstate	
	 * 	
	 * @return JCheckBox	
	 */
	private JCheckBox getchkPlacardReinstate()
	{
		if (ivjchkPlacardReinstate == null)
		{
			ivjchkPlacardReinstate = new JCheckBox();
			ivjchkPlacardReinstate.setBounds(new Rectangle(233, 416, 130, 21));
			ivjchkPlacardReinstate.setMnemonic(KeyEvent.VK_E);
			ivjchkPlacardReinstate.setText(PLACARD_REINSTATE);
			ivjchkPlacardReinstate.addActionListener(this);
		}
		return ivjchkPlacardReinstate;
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
			FrmSecurityAccessRightsMiscRegSEC010 laFrmSecurityAccessRightsMiscRegSEC010;
			laFrmSecurityAccessRightsMiscRegSEC010 =
				new FrmSecurityAccessRightsMiscRegSEC010();
			laFrmSecurityAccessRightsMiscRegSEC010.setModal(true);
			laFrmSecurityAccessRightsMiscRegSEC010
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSecurityAccessRightsMiscRegSEC010.show();
			Insets insets =
				laFrmSecurityAccessRightsMiscRegSEC010.getInsets();
			laFrmSecurityAccessRightsMiscRegSEC010.setSize(
				laFrmSecurityAccessRightsMiscRegSEC010.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSecurityAccessRightsMiscRegSEC010.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSecurityAccessRightsMiscRegSEC010.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeIVJEx.printStackTrace(System.out);
		}
	}
	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 */
	public FrmSecurityAccessRightsMiscRegSEC010()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsMiscRegSEC010(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 * 
	 * @param aaOwner java.awt.Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsMiscRegSEC010(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsMiscRegSEC010(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsMiscRegSEC010(
		Dialog aaOwner,
		String String,
		boolean abModal)
	{
		super(aaOwner, String, abModal);
	}

	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsMiscRegSEC010(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsMiscRegSEC010(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsMiscRegSEC010(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsMiscRegSEC010 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsMiscRegSEC010(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
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
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				caSecData.setSEC010(false);
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
				RTSHelp.displayHelp(RTSHelp.SEC010);
				return;
			}
			//defect 9381
			else if (aaAE.getSource() == getchkMiscRegistration())
			{
				selectChkButtons(getchkMiscRegistration().isSelected());
				// end defect 9381 
			}
			else if (aaAE.getSource() == getchkDisabledPlacard())
			{
				if (getchkDisabledPlacard().isSelected())
				{
					ciSelectedCount = ciSelectedCount + 4;
					getchkPlacardInquiry().setSelected(true);
					getchkPlacardManagement().setSelected(true);
					getchkReport().setSelected(true);
					// defect 11214
					getchkPlacardReinstate().setEnabled(SystemProperty.isHQ());
					// end defect 11214 
				}
				// getchkDisabledPlacard is deselected 
				// Either Placard Managment or Reports is deselected
				else
				{
					ciSelectedCount--;
					if (getchkPlacardInquiry().isSelected())
					{
						getchkPlacardInquiry().setSelected(false);
						ciSelectedCount--;
					}
					if (getchkPlacardManagement().isSelected())
					{
						getchkPlacardManagement().setSelected(false);
						ciSelectedCount--;
						// defect 11214 
						if (getchkPlacardReinstate().isSelected()) 
						{
							getchkPlacardReinstate().setSelected(false);
							ciSelectedCount--;
						}
						getchkPlacardReinstate().setEnabled(false); 
						// end defect 11214 
					}
					if (getchkReport().isSelected())
					{
						getchkReport().setSelected(false);
						ciSelectedCount--;
					}

				}
			}
			else if (aaAE.getSource() == getchkPlacardInquiry())
			{
				if (getchkPlacardInquiry().isSelected())
				{
					getchkPlacardInquiry().setSelected(true);
					ciSelectedCount++;
					if (!getchkDisabledPlacard().isSelected())
					{
						getchkDisabledPlacard().setSelected(true);
						ciSelectedCount++;
					}
				}
				else
				{
					getchkPlacardInquiry().setSelected(false);
					ciSelectedCount--;
					// If neither Report nor Management are selected
					//  deselect Disabled Placard 
					if (!getchkReport().isSelected()
						&& !getchkPlacardManagement().isSelected())
					{
						getchkDisabledPlacard().setSelected(false);
						ciSelectedCount--;
					}
				}
			}
			// defect 11214 
			else if (aaAE.getSource() == getchkPlacardReinstate())
			{
				if (getchkPlacardReinstate().isSelected())
				{
					getchkPlacardReinstate().setSelected(true);
					ciSelectedCount++;
				}
				else
				{
					getchkPlacardReinstate().setSelected(false);
					ciSelectedCount--;
				}
			}
			// end defect 11214 
			else if (aaAE.getSource() == getchkPlacardManagement())
			{
				if (getchkPlacardManagement().isSelected())
				{
					getchkPlacardManagement().setSelected(true);
					ciSelectedCount++;
					if (!getchkDisabledPlacard().isSelected())
					{
						getchkDisabledPlacard().setSelected(true);
						ciSelectedCount++;
					}
					// defect 11214 
					getchkPlacardReinstate().setEnabled(SystemProperty.isHQ());
					// end defect 11214 
				}
				else
				{
					getchkPlacardManagement().setSelected(false);
					ciSelectedCount--;
					// defect 11214
					if (getchkPlacardReinstate().isSelected())
					{
						getchkPlacardReinstate().setSelected(false);
						ciSelectedCount--; 
					}
					getchkPlacardReinstate().setEnabled(false); 
					// end defect 11214 
					
					if (!getchkPlacardInquiry().isSelected()
						&& !getchkReport().isSelected())
					{
						getchkDisabledPlacard().setSelected(false);
						ciSelectedCount--;
					}
				}
			}
			else if (aaAE.getSource() == getchkReport())
			{
				if (getchkReport().isSelected())
				{
					getchkReport().setSelected(true);
					ciSelectedCount++;
					if (!getchkDisabledPlacard().isSelected())
					{
						getchkDisabledPlacard().setSelected(true);
						ciSelectedCount++;
					}
				}
				else
				{
					getchkReport().setSelected(false);
					ciSelectedCount--;
					if (!getchkPlacardInquiry().isSelected()
						&& !getchkPlacardManagement().isSelected())
					{
						getchkDisabledPlacard().setSelected(false);
						ciSelectedCount--;
					}
				}
			}
			// end defect 9381 

			else if (aaAE.getSource() == getchkNonResAgriPermit())
			{
				if (getchkNonResAgriPermit().isSelected())
				{
					ciSelectedCount++;
				}
				else
				{
					ciSelectedCount--;
				}
			}
			else if (aaAE.getSource() == getchkTempAddWeight())
			{
				if (getchkTempAddWeight().isSelected())
				{
					ciSelectedCount++;
				}
				else
				{
					ciSelectedCount--;
				}
			}
			else if (aaAE.getSource() == getchkTimedPermit())
			{
				if (getchkTimedPermit().isSelected())
				{
					ciSelectedCount++;

					// defect 10844 
					getchkModifyTimedPermit().setEnabled(true);
					// end defect 10844 
				}
				else
				{
					ciSelectedCount--;

					// defect 10844 
					if (getchkModifyTimedPermit().isSelected())
					{
						ciSelectedCount--;
					}
					getchkModifyTimedPermit().setEnabled(false);
					getchkModifyTimedPermit().setSelected(false);
					// end defect 10844 
				}
			}
			// defect 10844 
			else if (aaAE.getSource() == getchkModifyTimedPermit())
			{
				if (getchkModifyTimedPermit().isSelected())
				{
					ciSelectedCount++;
				}
				else
				{
					ciSelectedCount--;
				}
			}
			// end defect 10844 
			else if (aaAE.getSource() == getchkTowTruck())
			{
				if (getchkTowTruck().isSelected())
				{
					ciSelectedCount++;
				}
				else
				{
					ciSelectedCount--;
				}
			}
			checkCountZero();
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Check for EnableCount field for zero value and 
	 * set the LocalOptions checkbox
	 */
	private void checkCountZero()
	{
		// defect 9831 
		getchkMiscRegistration().setSelected(ciSelectedCount != 0);
		// end defect 9831
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
				ivjButtonPanel1.setBounds(102, 487, 382, 36);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
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
	 * Return the chkDisabledPlacard property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDisabledPlacard()
	{
		if (ivjchkDisabledPlacard == null)
		{
			try
			{
				ivjchkDisabledPlacard = new JCheckBox();
				ivjchkDisabledPlacard.setSize(218, 22);
				ivjchkDisabledPlacard.setName("chkDisabledPlacard");
				ivjchkDisabledPlacard.setMnemonic('d');
				ivjchkDisabledPlacard.setText("Disabled Placard");
				ivjchkDisabledPlacard.setMaximumSize(
					new Dimension(151, 22));
				ivjchkDisabledPlacard.setActionCommand(
					DISABLED_PLACARD);
				ivjchkDisabledPlacard.setMinimumSize(
					new Dimension(151, 22));
				// user code begin {1}
				ivjchkDisabledPlacard.setLocation(168, 326);
				ivjchkDisabledPlacard.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDisabledPlacard;
	}

	/**
	 * Return the chkMiscRegistration property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkMiscRegistration()
	{
		if (ivjchkMiscRegistration == null)
		{
			try
			{
				ivjchkMiscRegistration = new JCheckBox();
				ivjchkMiscRegistration.setName("chkMiscRegistration");
				ivjchkMiscRegistration.setMnemonic('s');
				ivjchkMiscRegistration.setText(MISC_REG);
				ivjchkMiscRegistration.setMaximumSize(
					new Dimension(179, 22));
				ivjchkMiscRegistration.setActionCommand(MISC_REG);
				ivjchkMiscRegistration.setBounds(168, 128, 218, 22);
				ivjchkMiscRegistration.setMinimumSize(
					new Dimension(179, 22));
				// user code begin {1}
				ivjchkMiscRegistration.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkMiscRegistration;
	}

	/**
	 * This method initializes ivjchkModifyTimedPermit
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkModifyTimedPermit()
	{
		if (ivjchkModifyTimedPermit == null)
		{
			ivjchkModifyTimedPermit = new javax.swing.JCheckBox();
			ivjchkModifyTimedPermit.setSize(146, 21);
			ivjchkModifyTimedPermit.setText(MOD_TIMED_PRMT);
			ivjchkModifyTimedPermit.setLocation(196, 191);
			ivjchkModifyTimedPermit.setMnemonic(
				java.awt.event.KeyEvent.VK_O);
			ivjchkModifyTimedPermit.addActionListener(this);
		}
		return ivjchkModifyTimedPermit;
	}

	/**
	 * Return the chkNonResAgriPermit property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkNonResAgriPermit()
	{
		if (ivjchkNonResAgriPermit == null)
		{
			try
			{
				ivjchkNonResAgriPermit = new JCheckBox();
				ivjchkNonResAgriPermit.setSize(218, 22);
				ivjchkNonResAgriPermit.setName("chkNonResAgriPermit");
				ivjchkNonResAgriPermit.setMnemonic('n');
				ivjchkNonResAgriPermit.setText(NON_RESIDENT_AGR_PERMIT);
				ivjchkNonResAgriPermit.setMaximumSize(
					new Dimension(208, 22));
				ivjchkNonResAgriPermit.setActionCommand(
					NON_RESIDENT_AGR_PERMIT);
				ivjchkNonResAgriPermit.setMinimumSize(
					new Dimension(208, 22));
				// user code begin {1}
				ivjchkNonResAgriPermit.setLocation(168, 256);
				ivjchkNonResAgriPermit.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkNonResAgriPermit;
	}

	/**
	 * This method initializes ivjchkPlacardInquiry
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPlacardInquiry()
	{
		if (ivjchkPlacardInquiry == null)
		{
			ivjchkPlacardInquiry = new javax.swing.JCheckBox();
			ivjchkPlacardInquiry.setSize(184, 21);
			ivjchkPlacardInquiry.setText("Placard Inquiry");
			ivjchkPlacardInquiry.setMnemonic(
				java.awt.event.KeyEvent.VK_I);
			ivjchkPlacardInquiry.setLocation(196, 356);
			ivjchkPlacardInquiry.addActionListener(this);
		}
		return ivjchkPlacardInquiry;
	}

	/**
	 * This method initializes ivjchkPlacardManagement
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPlacardManagement()
	{
		if (ivjchkPlacardManagement == null)
		{
			ivjchkPlacardManagement = new JCheckBox();
			ivjchkPlacardManagement.setSize(200, 21);
			ivjchkPlacardManagement.setText("Placard Management");
			ivjchkPlacardManagement.setMnemonic(
				java.awt.event.KeyEvent.VK_P);
			ivjchkPlacardManagement.setLocation(196, 386);
			ivjchkPlacardManagement.addActionListener(this);
		}
		return ivjchkPlacardManagement;
	}

	/**
	 * This method initializes ivjchkReports
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkReport()
	{
		if (ivjchkReport == null)
		{
			ivjchkReport = new JCheckBox();
			ivjchkReport.setSize(150, 21);
			ivjchkReport.setText("Report");
			ivjchkReport.setMnemonic(java.awt.event.KeyEvent.VK_R);
			ivjchkReport.setLocation(195, 442);
			ivjchkReport.addActionListener(this);
		}
		return ivjchkReport;
	}
	/**
	 * Return the chkTempAddWeight property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkTempAddWeight()
	{
		if (ivjchkTempAddWeight == null)
		{
			try
			{
				ivjchkTempAddWeight = new JCheckBox();
				ivjchkTempAddWeight.setSize(218, 22);
				ivjchkTempAddWeight.setName("chkTempAddWeight");
				ivjchkTempAddWeight.setMnemonic('m');
				ivjchkTempAddWeight.setText(TEMP_ADD_WEIGHT);
				ivjchkTempAddWeight.setMaximumSize(
					new Dimension(186, 22));
				ivjchkTempAddWeight.setActionCommand(TEMP_ADD_WEIGHT);
				ivjchkTempAddWeight.setMinimumSize(
					new Dimension(186, 22));
				// user code begin {1}
				ivjchkTempAddWeight.setLocation(168, 221);
				ivjchkTempAddWeight.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkTempAddWeight;
	}

	/**
	 * Return the chkTimedPermit property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkTimedPermit()
	{
		if (ivjchkTimedPermit == null)
		{
			try
			{
				ivjchkTimedPermit = new JCheckBox();
				ivjchkTimedPermit.setSize(218, 22);
				ivjchkTimedPermit.setName("chkTimedPermit");
				ivjchkTimedPermit.setMnemonic('t');
				ivjchkTimedPermit.setText(TIMED_PERMIT);
				ivjchkTimedPermit.setMaximumSize(
					new Dimension(101, 22));
				ivjchkTimedPermit.setActionCommand(TIMED_PERMIT);
				ivjchkTimedPermit.setMinimumSize(
					new Dimension(101, 22));
				// user code begin {1}
				ivjchkTimedPermit.setLocation(168, 161);
				ivjchkTimedPermit.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkTimedPermit;
	}

	/**
	 * Return the chkTowTruck property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkTowTruck()
	{
		if (ivjchkTowTruck == null)
		{
			try
			{
				ivjchkTowTruck = new JCheckBox();
				ivjchkTowTruck.setSize(218, 22);
				ivjchkTowTruck.setName("chkTowTruck");
				ivjchkTowTruck.setMnemonic('w');
				ivjchkTowTruck.setText(TOW_TRUCK);
				ivjchkTowTruck.setMaximumSize(new Dimension(85, 22));
				ivjchkTowTruck.setActionCommand(TOW_TRUCK);
				ivjchkTowTruck.setMinimumSize(new Dimension(85, 22));
				// user code begin {1}
				ivjchkTowTruck.setLocation(168, 291);
				ivjchkTowTruck.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkTowTruck;
	}

	/**
	 * Return the FrmSecurityAccessRightsMiscRegSEC010ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1 =
					new JPanel();
				ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1
					.setName(
					"FrmSecurityAccessRightsMiscRegSEC010ContentPane1");
				ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1
					.setMaximumSize(
					new Dimension(482, 325));
				ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1
					.setMinimumSize(
					new Dimension(482, 325));
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getstcLblMiscReg(),
					getstcLblMiscReg().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkMiscRegistration(),
					getchkMiscRegistration().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkTimedPermit(),
					getchkTimedPermit().getName());

				// defect 10844 
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkModifyTimedPermit(),
					getchkModifyTimedPermit().getName());
				// end defect 10844 

				// defect 9831 
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkDisabledPlacard(),
					getchkDisabledPlacard().getName());
				// end defect 9831 
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkTempAddWeight(),
					getchkTempAddWeight().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkNonResAgriPermit(),
					getchkNonResAgriPermit().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkTowTruck(),
					getchkTowTruck().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());
				// defect 9831 
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkPlacardManagement(),
					null);
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1()
					.add(
					getchkReport(),
					null);
				// end defect 9831 
				ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1
					.add(
					getchkPlacardInquiry(),
					null);
				// defect 11214 
				ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1.add(getchkPlacardReinstate(), null);
				// end defect 11214 
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
		return ivjFrmSecurityAccessRightsMiscRegSEC010ContentPane1;
	}

	/**
	 * Get the int equivalent for the boolean parameter
	 * Returns 1 if parameter is true or returns 0.
	 * 
	 * @return int
	 * @param lbVal boolean
	 */
	private int getIntValue(boolean abVal)
	{
		// defect 9831 
		return abVal ? 1 : 0;
		// end defect 9831 
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
				ivjlblEmployeeFirstName.setText(LABEL1);
				ivjlblEmployeeFirstName.setBounds(342, 36, 183, 20);
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
		return ivjlblEmployeeFirstName;
	}

	/**
	 * Return the lblEmployeeId property value.
	 * 
	 * @return JLabel
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
				ivjlblEmployeeId.setText(LABEL1);
				ivjlblEmployeeId.setMaximumSize(new Dimension(45, 14));
				ivjlblEmployeeId.setMinimumSize(new Dimension(45, 14));
				ivjlblEmployeeId.setBounds(159, 12, 119, 20);
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
				ivjlblEmployeeLastName.setText(LABEL1);
				ivjlblEmployeeLastName.setMaximumSize(
					new Dimension(45, 14));
				ivjlblEmployeeLastName.setMinimumSize(
					new Dimension(45, 14));
				ivjlblEmployeeLastName.setBounds(159, 35, 173, 20);
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
				ivjlblEmployeeMName = new JLabel();
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(LABEL2);
				ivjlblEmployeeMName.setBounds(528, 36, 56, 20);
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
		return ivjlblEmployeeMName;
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
				ivjstcLblEmployeeId.setSize(83, 20);
				ivjstcLblEmployeeId.setName("stcLblEmployeeId");
				ivjstcLblEmployeeId.setText(EMP_ID);
				ivjstcLblEmployeeId.setMaximumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setMinimumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setLocation(33, 12);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
				ivjstcLblEmployeeName.setBounds(33, 35, 113, 20);
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
		return ivjstcLblEmployeeName;
	}

	/**
	 * Return the stcLblMiscReg property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMiscReg()
	{
		if (ivjstcLblMiscReg == null)
		{
			try
			{
				ivjstcLblMiscReg = new JLabel();
				ivjstcLblMiscReg.setSize(168, 20);
				ivjstcLblMiscReg.setName("stcLblMiscReg");
				ivjstcLblMiscReg.setAlignmentX(
					Component.CENTER_ALIGNMENT);
				ivjstcLblMiscReg.setText(MISC_REG);
				ivjstcLblMiscReg.setMaximumSize(new Dimension(154, 14));
				ivjstcLblMiscReg.setMinimumSize(new Dimension(154, 14));
				ivjstcLblMiscReg.setLocation(232, 78);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblMiscReg;
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
			setName("FrmSecurityAccessRightsMiscRegSEC010");
			setSize(600, 568);
			setTitle(SEC010_FRAME_TITLE);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setContentPane(
				getFrmSecurityAccessRightsMiscRegSEC010ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}

		// defect 10844 
		// Use RTSButtonGroup to manage Tab/Cursor Movement Keys 
		caCheckBoxButtonGroup = new RTSButtonGroup();
		caCheckBoxButtonGroup.add(ivjchkMiscRegistration);
		caCheckBoxButtonGroup.add(ivjchkTimedPermit);
		caCheckBoxButtonGroup.add(ivjchkModifyTimedPermit);
		caCheckBoxButtonGroup.add(ivjchkTempAddWeight);
		caCheckBoxButtonGroup.add(ivjchkNonResAgriPermit);
		caCheckBoxButtonGroup.add(ivjchkTowTruck);
		caCheckBoxButtonGroup.add(ivjchkDisabledPlacard);
		caCheckBoxButtonGroup.add(ivjchkPlacardInquiry);
		caCheckBoxButtonGroup.add(ivjchkPlacardManagement);
		// defect 11214 
		caCheckBoxButtonGroup.add(ivjchkPlacardReinstate);
		// end defect 11214 
		caCheckBoxButtonGroup.add(ivjchkReport);
		// end defect 10844  
		// user code end
	}

	/**
	 * Sets the various check boxes depending on whether 
	 * they are enabled and updates iEnableCount
	 * 
	 * @param abVal boolean
	 */
	private void selectChkButtons(boolean abVal)
	{
		// defect 9831 
		// Add Placard Mgmt and Report checkboxes
		//  (Disabled Parking Card renamed to Disabled Placard per VTR)
		// Streamline analysis 
		if (getchkDisabledPlacard().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount = ciSelectedCount + 4;
			}
			else if (getchkDisabledPlacard().isSelected())
			{
				ciSelectedCount--;
				if (getchkPlacardManagement().isSelected())
				{
					ciSelectedCount--;
					// defect 11214
					if (getchkPlacardReinstate().isSelected())
					{
						getchkPlacardReinstate().setSelected(false);
						ciSelectedCount--; 
					}
					// end defect 11214 
				}
				if (getchkReport().isSelected())
				{
					ciSelectedCount--;
				}
				if (getchkPlacardInquiry().isSelected())
				{
					ciSelectedCount--;
				}
			}
			getchkDisabledPlacard().setSelected(abVal);
			getchkPlacardInquiry().setSelected(abVal);
			getchkPlacardManagement().setSelected(abVal);
			// defect 11214 
			getchkPlacardReinstate().setEnabled(abVal && SystemProperty.isHQ());
			// end defect 11214
			getchkReport().setSelected(abVal);
		}
		if (getchkNonResAgriPermit().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkNonResAgriPermit().isSelected())
			{
				ciSelectedCount--;
			}
			getchkNonResAgriPermit().setSelected(abVal);
		}
		if (getchkTempAddWeight().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkTempAddWeight().isSelected())
			{
				ciSelectedCount--;
			}
			getchkTempAddWeight().setSelected(abVal);
		}
		if (getchkTimedPermit().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkTimedPermit().isSelected())
			{
				ciSelectedCount--;

				// defect 10844 
				if (getchkModifyTimedPermit().isSelected())
				{
					ciSelectedCount--;
				}
			}
			getchkModifyTimedPermit().setEnabled(abVal);
			getchkModifyTimedPermit().setSelected(false);
			// end defect 10844
			getchkTimedPermit().setSelected(abVal);
		}
		if (getchkTowTruck().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkTowTruck().isSelected())
			{
				ciSelectedCount--;
			}
			getchkTowTruck().setSelected(abVal);
		}
		// end defect 9831 
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view.
	 * 
	 * @param aaDataObject Object
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
			//disableBtnOnOfcId();
			if (laSecData.getMiscRegAccs() == 1)
			{
				getchkMiscRegistration().setSelected(true);
			}
			if (laSecData.getTimedPrmtAccs() == 1)
			{
				getchkTimedPermit().setSelected(true);
				ciSelectedCount++;

				// defect 10844				
				if (laSecData.getModfyTimedPrmtAccs() == 1)
				{
					getchkModifyTimedPermit().setSelected(true);
					ciSelectedCount++;
				}
			}
			else
			{
				getchkModifyTimedPermit().setSelected(false);
				getchkModifyTimedPermit().setEnabled(false);
			}
			// end defect 10844 

			// defect 9831 
			if (laSecData.getDsabldPersnAccs() == 1
				|| laSecData.getDsabldPlcrdRptAccs() == 1)
			{
				getchkDisabledPlacard().setSelected(true);
				ciSelectedCount++;
			}
			if (laSecData.getDsabldPersnAccs() == 1)
			{
				getchkPlacardManagement().setSelected(true);
				ciSelectedCount++;
				// defect 11214 
				if (SystemProperty.isHQ())
				{ 
					if (laSecData.getDsabldPlcrdReInstateAccs() == 1)

					{
						getchkPlacardReinstate().setSelected(true);
						ciSelectedCount++;
					}
				}
				else
				{
					getchkPlacardReinstate().setSelected(false);
					getchkPlacardReinstate().setEnabled(false);
				}
				
			}
			else
			{
				getchkPlacardReinstate().setSelected(false);
				getchkPlacardReinstate().setEnabled(false);
			}
			// end defect 11214
			if (laSecData.getDsabldPlcrdRptAccs() == 1)
			{
				getchkReport().setSelected(true);
				ciSelectedCount++;
			}
			if (laSecData.getDsabldPlcrdInqAccs() == 1)
			{
				getchkPlacardInquiry().setSelected(true);
				ciSelectedCount++;
			}

			// end defect 9831 
			if (laSecData.getTempAddlWtAccs() == 1)
			{
				getchkTempAddWeight().setSelected(true);
				ciSelectedCount++;
			}
			if (laSecData.getNonResPrmtAccs() == 1)
			{
				getchkNonResAgriPermit().setSelected(true);
				ciSelectedCount++;
			}
			if (laSecData.getTowTrkAccs() == 1)
			{
				getchkTowTruck().setSelected(true);
				ciSelectedCount++;
			}
		}
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Miscellaneous Registration.
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC010(true);

		// defect 9831 
		caSecData.getSecData().setDsabldPlcrdInqAccs(
			getIntValue(getchkPlacardInquiry().isSelected()));
		caSecData.getSecData().setDsabldPersnAccs(
			getIntValue(getchkPlacardManagement().isSelected()));
		caSecData.getSecData().setDsabldPlcrdRptAccs(
			getIntValue(getchkReport().isSelected()));
		// end defect 9831 
		
		// defect 11214 
		caSecData.getSecData().setDsabldPlcrdReInstateAccs(
				getIntValue(getchkPlacardReinstate().isSelected()));
		// end defect 11214 

		caSecData.getSecData().setMiscRegAccs(
			getIntValue(getchkMiscRegistration().isSelected()));
		caSecData.getSecData().setNonResPrmtAccs(
			getIntValue(getchkNonResAgriPermit().isSelected()));
		caSecData.getSecData().setTempAddlWtAccs(
			getIntValue(getchkTempAddWeight().isSelected()));
		caSecData.getSecData().setTimedPrmtAccs(
			getIntValue(getchkTimedPermit().isSelected()));

		// defect 10844 
		caSecData.getSecData().setModfyTimedPrmtAccs(
			getIntValue(getchkModifyTimedPermit().isSelected()));
		// end defect 10844 

		caSecData.getSecData().setTowTrkAccs(
			getIntValue(getchkTowTruck().isSelected()));
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
