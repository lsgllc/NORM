package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSecurityAccessRightsStatusChangeSEC008.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * B Arredondo	02/20/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * K Harrell	03/29/2004	Rename Frame to match standard
 *							modify main()
 *							defect 6973 Ver 5.2.0
 * Ray Rowehl	03/17/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7891 Ver 5.2.3
 * Min Wang 	04/06/2005	Remove arrays since they are causing 
 * 							initialization errors
 * 							delete carrBtn 
 * 							modify initialize(), keyPressed() 
 * 							defect 7891 Ver 5.2.3
 * Min Wang 	04/16/2005	remove unused method
 * 							delete setChkBxNum()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		09/01/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		12/12/2005  Uncomment code for arrow key function.
 * 							modify keyPressed(), initialize()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	01/19/2006	Screen manipulation for Tab Key movement
 * 							through checkboxes. 
 * 							defect 7891 Ver 5.2.3
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty(),
 * 							 isRegion()
 * 							delete cbVTRRgn 
 * 							modify disableBtnOnOfcId()
 * 							defect 9085 Ver Special Plates
 * B Woodson	01/11/2012	add getchkExport(), ivjchkExport, 
 * 							 TITLE_EXPORT  
 * 							modify actionPerformed(), disableBtnOnOfcId(), 
 * 							 getJDialogBoxContentPane(), setData(), 
 * 							 setValuesToDataObj()
 * 							defect 11228 Ver 6.10.0
 * B Woodson	02/10/2012	modified getchkExport() to hide checkbox
 * 							defect 11228 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for Status 
 * Change.  Data on this screen is managed through 
 * SecurityClientDataObject.
 *
 * @version Special Plates	02/10/2012
 * @author	Administrator
 * <br>Creation Date:		07/15/2001 13:11:28 
 */

public class FrmSecurityAccessRightsStatusChangeSEC008
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkBonded = null;
	private JCheckBox ivjchkCancelRegistration = null;
	private JCheckBox ivjchkLegalRestraint = null;
	private JCheckBox ivjchkMisc = null;
	private JCheckBox ivjchkRegisteredBy = null;
	private JCheckBox ivjchkRegistration = null;
	private JCheckBox ivjchkStatusChange = null;
	private JCheckBox ivjchkStolen = null;
	private JCheckBox ivjchkTitleRevoked = null;
	//defect 11228
	private JCheckBox ivjchkExport = null;
	//end defect 11228
	private JCheckBox ivjchkTitleSurrenered = null;
	private JCheckBox ivjchkVehicleJunked = null;
	private JCheckBox ivjchkMailRet = null;
	private JPanel ivjJDialogBoxContentPane = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjlblNameMiddle = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblStatusChange = null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private RTSButton[] carrBtn = new RTSButton[3];
	private JCheckBox[] carrBtnSec = new JCheckBox[12];

	// boolean  
	private boolean cbSelectAll = false;
	
	// defect 9085 
	// private boolean cbVTRRgn = false;
	// end defect9085 

	// int 
	private int ciEnableCount = 0;
	private int ciSelect = 0;
	private int ciSelectSec = 0;

	// Object 
	private SecurityClientDataObject caSecData = null;

	// Static 
	private static final String BOND_TITLE_CD = "Bonded Title Code";
	private static final String CANCEL_REG = "Cancel Registration";
	private static final String LEGAL_RESTRAINT_NO =
		"Legal Restraint No";
	private static final String MAIL_RETRN = "Mail Returned";
	private static final String MISC_REMARK = "Miscellaneous Remarks";
	private static final String REG_BY = "Registered by";
	private static final String REG_REFND_AMNT =
		"Registration Refund Amount";
	private static final String STATUS_CHANGE = "Status Change";
	private static final String STOLEN_SRS = "Stolen/SRS";
	private static final String TITLE_REVOK = "Title Revoked";
	//defect 11228
	private static final String TITLE_EXPORT = "Export";
	//end defect 11228
	private static final String TITLE_SURND = "Title Surrendered";
	private static final String VEHICLE_JUNK = "Vehicle Junked";
	private static final String Y = "Y";
	private static final String LABEL1 = "JLabel1";
	private static final String A = "A";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC008_FRAME_TITLE =
		"Security Access Rights Status Change   SEC008";

	private javax.swing.JPanel jPanel = null;

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessSEC008 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsStatusChangeSEC008(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
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
				caSecData.setSEC008(false);
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
				RTSHelp.displayHelp(RTSHelp.SEC008);
				return;
			}
			else if (aaAE.getSource() == getchkStatusChange())
			{
				resetSelectAllForEnabledSlctd();
				if (cbSelectAll)
				{
					cbSelectAll = false;
				}
				else
				{
					cbSelectAll = true;
				}
				selectChkButtons(cbSelectAll);
			}
			else if (aaAE.getSource() == getchkCancelRegistration())
			{
				// defect 9085 
				//if (cbVTRRgn)
				//
				if (getchkCancelRegistration().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
				//} 
				// end defect 9085
			}
			else if (aaAE.getSource() == getchkBonded())
			{
				if (getchkBonded().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkLegalRestraint())
			{
				if (getchkLegalRestraint().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkMisc())
			{
				if (getchkMisc().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkRegisteredBy())
			{
				if (getchkRegisteredBy().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkRegistration())
			{
				if (getchkRegistration().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkStolen())
			{
				if (getchkStolen().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkTitleRevoked())
			{
				if (getchkTitleRevoked().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkTitleSurrenered())
			{
				if (getchkTitleSurrenered().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkVehicleJunked())
			{
				if (getchkVehicleJunked().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkMailRet())
			{
				if (getchkMailRet().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			//defect 11228
			else if (aaAE.getSource() == getchkExport())
			{
				if (getchkExport().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			//end defect 11228
			checkCountZero();
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Check for EnableCount field for zero value and 
	 * set the StatusChange checkbox
	 */
	private void checkCountZero()
	{
		if (ciEnableCount == 0)
		{
			getchkStatusChange().setSelected(false);
		}
		else
		{
			getchkStatusChange().setSelected(true);
		}
	}

	/**
	 * Disable the various checkbox based on various conditions
	 */
	private void disableBtnOnOfcId()
	{
		getchkMisc().setEnabled(false);
		getchkMailRet().setEnabled(false);
		getchkRegisteredBy().setEnabled(false);
		if (!getchkMailRet().isSelected())
		{
			getchkMailRet().setSelected(true);
			ciEnableCount++;
		}
		if (!getchkRegisteredBy().isSelected())
		{
			getchkRegisteredBy().setSelected(true);
			ciEnableCount++;
		}
		if (!getchkMisc().isSelected())
		{
			getchkMisc().setSelected(true);
			ciEnableCount++;
		}
		// defect 9085 
		//if (caSecData.getWorkStationType()
		//	== LocalOptionConstant.COUNTY)
		if (SystemProperty.isCounty())
		{
			getchkStatusChange().requestFocus();
			getchkLegalRestraint().setEnabled(false);
			getchkBonded().setEnabled(false);
			getchkVehicleJunked().setEnabled(false);
			getchkTitleSurrenered().setEnabled(false);
			getchkStolen().setEnabled(false);
			getchkTitleRevoked().setEnabled(false);
			//defect 11228
			getchkExport().setEnabled(false);
			//defect 11228
		}
		else if (SystemProperty.isRegion())
			//caSecData.getWorkStationType()
			//	== LocalOptionConstant.REGION)
		{
			getchkStatusChange().requestFocus();
			getchkLegalRestraint().setEnabled(false);
			getchkBonded().setEnabled(false);
			//defect 11228
			getchkExport().setEnabled(false);
			//end defect 11228
		}
		//else if (
		//	caSecData.getWorkStationType() == LocalOptionConstant.VTR)
		//{
		// empty code block
		//}
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
				ivjButtonPanel1.setBounds(132, 345, 391, 82);
				// user code begin {1}
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the chkBonded property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkBonded()
	{
		if (ivjchkBonded == null)
		{
			try
			{
				ivjchkBonded = new javax.swing.JCheckBox();
				ivjchkBonded.setName("chkBonded");
				ivjchkBonded.setMnemonic('b');
				ivjchkBonded.setText(BOND_TITLE_CD);
				ivjchkBonded.setBounds(383, 210, 198, 22);
				// user code begin {1}
				ivjchkBonded.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkBonded;
	}

	/**
	 * Return the chkCancelRegistration property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCancelRegistration()
	{
		if (ivjchkCancelRegistration == null)
		{
			try
			{
				ivjchkCancelRegistration = new javax.swing.JCheckBox();
				ivjchkCancelRegistration.setBounds(60, 128, 198, 22);
				ivjchkCancelRegistration.setName(
					"chkCancelRegistration");
				ivjchkCancelRegistration.setMnemonic('n');
				ivjchkCancelRegistration.setText(CANCEL_REG);
				ivjchkCancelRegistration.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkCancelRegistration;
	}
	
	//defect 11228
	/**
	 * This method initializes ivjchkExport	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getchkExport()
	{
		if (ivjchkExport == null)
		{
			ivjchkExport = new JCheckBox();
			ivjchkExport.setBounds(383, 270, 167, 24);
			ivjchkExport.setMnemonic('E');
			ivjchkExport.setText(TITLE_EXPORT);
			ivjchkExport.setName("chkTitleRevoked");
			ivjchkExport.addActionListener(this);
			ivjchkExport.setVisible(false);
		}
		return ivjchkExport;
	}	
	//end defect 11228
	
	/**
	 * Return the chkLegalRestraint property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkLegalRestraint()
	{
		if (ivjchkLegalRestraint == null)
		{
			try
			{
				ivjchkLegalRestraint = new javax.swing.JCheckBox();
				ivjchkLegalRestraint.setName("chkLegalRestraint");
				ivjchkLegalRestraint.setMnemonic('l');
				ivjchkLegalRestraint.setText(LEGAL_RESTRAINT_NO);
				ivjchkLegalRestraint.setBounds(383, 180, 198, 22);
				// user code begin {1}
				ivjchkLegalRestraint.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkLegalRestraint;
	}

	/**
	 * Return the chkMailRet property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkMailRet()
	{
		if (ivjchkMailRet == null)
		{
			try
			{
				ivjchkMailRet = new JCheckBox();
				ivjchkMailRet.setBounds(60, 188, 198, 22);
				ivjchkMailRet.setName("chkMailRet");
				ivjchkMailRet.setMnemonic('u');
				ivjchkMailRet.setText(MAIL_RETRN);
				ivjchkMailRet.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkMailRet;
	}

	/**
	 * Return the chkMisc property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkMisc()
	{
		if (ivjchkMisc == null)
		{
			try
			{
				ivjchkMisc = new JCheckBox();
				ivjchkMisc.setName("chkMisc");
				ivjchkMisc.setMnemonic('m');
				ivjchkMisc.setText(MISC_REMARK);
				ivjchkMisc.setBounds(346, 150, 198, 22);
				// user code begin {1}
				ivjchkMisc.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkMisc;
	}

	/**
	 * Return the chkRegisteredBy property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkRegisteredBy()
	{
		if (ivjchkRegisteredBy == null)
		{
			try
			{
				ivjchkRegisteredBy = new JCheckBox();
				ivjchkRegisteredBy.setName("chkRegisteredBy");
				ivjchkRegisteredBy.setMnemonic('g');
				ivjchkRegisteredBy.setText(REG_BY);
				ivjchkRegisteredBy.setBounds(346, 120, 198, 22);
				// user code begin {1}
				ivjchkRegisteredBy.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkRegisteredBy;
	}

	/**
	 * Return the chkRegistration property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkRegistration()
	{
		if (ivjchkRegistration == null)
		{
			try
			{
				ivjchkRegistration = new JCheckBox();
				ivjchkRegistration.setBounds(60, 158, 198, 22);
				ivjchkRegistration.setName("chkRegistration");
				ivjchkRegistration.setMnemonic('r');
				ivjchkRegistration.setText(REG_REFND_AMNT);
				ivjchkRegistration.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkRegistration;
	}

	/**
	 * Return the chkStatusChange property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkStatusChange()
	{
		if (ivjchkStatusChange == null)
		{
			try
			{
				ivjchkStatusChange = new JCheckBox();
				ivjchkStatusChange.setBounds(60, 8, 198, 22);
				ivjchkStatusChange.setName("chkStatusChange");
				ivjchkStatusChange.setMnemonic('a');
				ivjchkStatusChange.setText(STATUS_CHANGE);
				ivjchkStatusChange.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkStatusChange;
	}

	/**
	 * Return the chkStolen property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkStolen()
	{
		if (ivjchkStolen == null)
		{
			try
			{
				ivjchkStolen = new JCheckBox();
				ivjchkStolen.setBounds(60, 98, 198, 22);
				ivjchkStolen.setName("chkStolen");
				ivjchkStolen.setMnemonic('o');
				ivjchkStolen.setText(STOLEN_SRS);
				ivjchkStolen.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkStolen;
	}

	/**
	 * Return the chkTitleRevoked property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkTitleRevoked()
	{
		if (ivjchkTitleRevoked == null)
		{
			try
			{
				ivjchkTitleRevoked = new JCheckBox();
				ivjchkTitleRevoked.setName("chkTitleRevoked");
				ivjchkTitleRevoked.setMnemonic('t');
				ivjchkTitleRevoked.setText(TITLE_REVOK);
				ivjchkTitleRevoked.setBounds(383, 240, 198, 22);
				// user code begin {1}
				ivjchkTitleRevoked.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkTitleRevoked;
	}

	/**
	 * Return the chkTitleSurrenered property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkTitleSurrenered()
	{
		if (ivjchkTitleSurrenered == null)
		{
			try
			{
				ivjchkTitleSurrenered = new JCheckBox();
				ivjchkTitleSurrenered.setBounds(60, 68, 198, 22);
				ivjchkTitleSurrenered.setName("chkTitleSurrenered");
				ivjchkTitleSurrenered.setMnemonic('i');
				ivjchkTitleSurrenered.setText(TITLE_SURND);
				ivjchkTitleSurrenered.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkTitleSurrenered;
	}

	/**
	 * Return the chkVehicleJunked property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkVehicleJunked()
	{
		if (ivjchkVehicleJunked == null)
		{
			try
			{
				ivjchkVehicleJunked = new JCheckBox();
				ivjchkVehicleJunked.setBounds(60, 38, 198, 22);
				ivjchkVehicleJunked.setName("chkVehicleJunked");
				ivjchkVehicleJunked.setMnemonic('v');
				ivjchkVehicleJunked.setText(VEHICLE_JUNK);
				ivjchkVehicleJunked.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkVehicleJunked;
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
	 * Return the JDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJDialogBoxContentPane()
	{
		if (ivjJDialogBoxContentPane == null)
		{
			try
			{
				ivjJDialogBoxContentPane = new JPanel();
				ivjJDialogBoxContentPane.setName(
					"JDialogBoxContentPane");
				ivjJDialogBoxContentPane.setLayout(null);
				getJDialogBoxContentPane().add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getJDialogBoxContentPane().add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getJDialogBoxContentPane().add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getJDialogBoxContentPane().add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getJDialogBoxContentPane().add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getJDialogBoxContentPane().add(
					getlblNameMiddle(),
					getlblNameMiddle().getName());
				getJDialogBoxContentPane().add(
					getstcLblStatusChange(),
					getstcLblStatusChange().getName());
				getJDialogBoxContentPane().add(
					getchkRegisteredBy(),
					getchkRegisteredBy().getName());
				getJDialogBoxContentPane().add(
					getchkMisc(),
					getchkMisc().getName());
				getJDialogBoxContentPane().add(
					getchkLegalRestraint(),
					getchkLegalRestraint().getName());
				getJDialogBoxContentPane().add(
					getchkBonded(),
					getchkBonded().getName());
				getJDialogBoxContentPane().add(
					getchkTitleRevoked(),
					getchkTitleRevoked().getName());
				//defect 11228
				getJDialogBoxContentPane().add(
					getchkExport(), getchkExport().getName());
				//end defect 11228
				getJDialogBoxContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getJDialogBoxContentPane().add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());
				// user code begin {1}
				ivjJDialogBoxContentPane.add(getJPanel(), null);
				// user code end
				
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJDialogBoxContentPane;
	}

	/**
	 * Return the lblNameFirst property value.
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
				ivjlblEmployeeFirstName.setText(Y);
				ivjlblEmployeeFirstName.setBounds(342, 40, 189, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
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
				ivjlblEmployeeId.setBounds(140, 18, 104, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblEmployeeId;
	}

	/**
	 * Return the lblNameLast property value.
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
				ivjlblEmployeeLastName.setBounds(139, 40, 188, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
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
				ivjlblEmployeeMName = new JLabel();
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(LABEL1);
				ivjlblEmployeeMName.setBounds(540, 40, 67, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblEmployeeMName;
	}

	/**
	 * Return the lblNameMiddle property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblNameMiddle()
	{
		if (ivjlblNameMiddle == null)
		{
			try
			{
				ivjlblNameMiddle = new JLabel();
				ivjlblNameMiddle.setName("lblNameMiddle");
				ivjlblNameMiddle.setText(A);
				ivjlblNameMiddle.setBounds(0, 0, 0, 0);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblNameMiddle;
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
				ivjstcLblEmployeeId.setComponentOrientation(
					java.awt.ComponentOrientation.LEFT_TO_RIGHT);
				ivjstcLblEmployeeId.setBounds(21, 17, 82, 15);
				ivjstcLblEmployeeId.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
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
				ivjstcLblEmployeeName.setComponentOrientation(
					java.awt.ComponentOrientation.LEFT_TO_RIGHT);
				ivjstcLblEmployeeName.setBounds(21, 39, 102, 15);
				ivjstcLblEmployeeName.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblEmployeeName;
	}

	/**
	 * Return the stcLblStatusChange property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblStatusChange()
	{
		if (ivjstcLblStatusChange == null)
		{
			try
			{
				ivjstcLblStatusChange = new JLabel();
				ivjstcLblStatusChange.setName("stcLblStatusChange");
				ivjstcLblStatusChange.setText(STATUS_CHANGE);
				ivjstcLblStatusChange.setBounds(215, 85, 188, 14);
				ivjstcLblStatusChange.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblStatusChange;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7891
		///* Uncomment the following lines to print uncaught exceptions
		// *  to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7891
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
			setName("FrmSecurityAccessSEC008");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(620, 440);
			setModal(true);
			setTitle(SEC008_FRAME_TITLE);
			setContentPane(getJDialogBoxContentPane());
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// defect 7891 
		carrBtnSec[0] = getchkStatusChange();
		carrBtnSec[1] = getchkVehicleJunked();
		carrBtnSec[2] = getchkTitleSurrenered();
		carrBtnSec[3] = getchkStolen();
		carrBtnSec[4] = getchkCancelRegistration();
		carrBtnSec[5] = getchkRegistration();
		carrBtnSec[6] = getchkMailRet();
		carrBtnSec[7] = getchkRegisteredBy();
		carrBtnSec[8] = getchkMisc();
		carrBtnSec[9] = getchkLegalRestraint();
		carrBtnSec[10] = getchkBonded();
		carrBtnSec[11] = getchkTitleRevoked();
		// end defect 7891
		// user code end
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		// defect 7891
		if (aaKE.getSource() instanceof JCheckBox)
		{
			ciSelectSec = 0;
			for (int i = 0; i < 12; i++)
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
					ciSelectSec = ciSelectSec + 1;
					if (ciSelectSec > 11)
					{
						ciSelectSec = 0;
					}
					if (ciSelectSec < 12
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
					ciSelectSec = ciSelectSec - 1;
					if (ciSelectSec < 0)
					{
						ciSelectSec = 11;
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
				if (carrBtn[i].hasFocus())
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
					if (ciSelect > 2)
					{
						ciSelect = 0;
					}
					if (ciSelect < 3 && carrBtn[ciSelect].isEnabled())
					{
						carrBtn[ciSelect].requestFocus();
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
					ciSelect = ciSelect - 1;
					if (ciSelect < 0)
					{
						ciSelect = 2;
					}
					if (ciSelect >= 0 && carrBtn[ciSelect].isEnabled())
					{
						carrBtn[ciSelect].requestFocus();
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
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args)
	{
		try
		{
			FrmSecurityAccessRightsStatusChangeSEC008 aFrmSecurityAccessRightsStatusChangeSEC008;
			aFrmSecurityAccessRightsStatusChangeSEC008 =
				new FrmSecurityAccessRightsStatusChangeSEC008();
			aFrmSecurityAccessRightsStatusChangeSEC008.setModal(true);
			aFrmSecurityAccessRightsStatusChangeSEC008
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			aFrmSecurityAccessRightsStatusChangeSEC008.show();
			java.awt.Insets insets =
				aFrmSecurityAccessRightsStatusChangeSEC008.getInsets();
			aFrmSecurityAccessRightsStatusChangeSEC008.setSize(
				aFrmSecurityAccessRightsStatusChangeSEC008.getWidth()
					+ insets.left
					+ insets.right,
				aFrmSecurityAccessRightsStatusChangeSEC008.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmSecurityAccessRightsStatusChangeSEC008.setVisibleRTS(
				true);
		}
		catch (Throwable leThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * Reset the buttons
	 */
	private void resetSelectAllForEnabledSlctd()
	{
		int liEnblCnt = 0;
		int liSlctCnt = 0;
		for (int i = 1; i < 12; i++)
		{
			if (carrBtnSec[i].isEnabled())
			{
				liEnblCnt = liEnblCnt + 1;
			}
			if (carrBtnSec[i].isEnabled()
				&& carrBtnSec[i].isSelected())
				liSlctCnt++;
		}
		//Means everything is selected
		if (liSlctCnt == 0 && liEnblCnt == 0)
		{
			cbSelectAll = false;
		}
		else if ((liSlctCnt == liEnblCnt) && cbSelectAll == false)
		{
			cbSelectAll = true;
		}
		else if (liSlctCnt == 0 && liEnblCnt == 2)
		{
			cbSelectAll = false;
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
		if (getchkBonded().isEnabled())
		{
			if (abVal && !getchkBonded().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkBonded().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkBonded().setSelected(abVal);
		}
		if (getchkCancelRegistration().isEnabled())
		{
			//if(bVTRRGN)
			{
				if (abVal && !getchkCancelRegistration().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else if (
					!abVal && getchkCancelRegistration().isSelected())
				{
					ciEnableCount = ciEnableCount - 1;
				}
				getchkCancelRegistration().setSelected(abVal);
			}
		}
		if (getchkLegalRestraint().isEnabled())
		{
			if (abVal && !getchkLegalRestraint().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkLegalRestraint().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkLegalRestraint().setSelected(abVal);
		}
		if (getchkMisc().isEnabled())
		{
			if (abVal && !getchkMisc().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkMisc().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkMisc().setSelected(abVal);
		}
		if (getchkRegisteredBy().isEnabled())
		{
			if (abVal && !getchkRegisteredBy().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkRegisteredBy().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkRegisteredBy().setSelected(abVal);
		}
		if (getchkRegistration().isEnabled())
		{
			if (abVal && !getchkRegistration().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkRegistration().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkRegistration().setSelected(abVal);
		}
		if (getchkStolen().isEnabled())
		{
			if (abVal && !getchkStolen().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkStolen().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkStolen().setSelected(abVal);
		}
		if (getchkTitleRevoked().isEnabled())
		{
			if (abVal && !getchkTitleRevoked().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkTitleRevoked().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkTitleRevoked().setSelected(abVal);
		}
		if (getchkTitleSurrenered().isEnabled())
		{
			if (abVal && !getchkTitleSurrenered().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkTitleSurrenered().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkTitleSurrenered().setSelected(abVal);
		}
		if (getchkVehicleJunked().isEnabled())
		{
			if (abVal && !getchkVehicleJunked().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkVehicleJunked().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkVehicleJunked().setSelected(abVal);
		}
		if (getchkMailRet().isEnabled())
		{
			if (abVal && !getchkMailRet().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkMailRet().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkMailRet().setSelected(abVal);
		}
	}
	// defect 7891 
	///**
	// * Set the check box by number
	// */
	//private void setChkBxNum()
	//{
	//	if (caSecData != null)
	//	{
	//		if (caSecData.getWorkStationType()
	//			== com
	//				.txdot
	//				.isd
	//				.rts
	//				.services
	//				.util
	//				.constants
	//				.LocalOptionConstant
	//				.VTR
	//			|| caSecData.getWorkStationType()
	//				== com
	//					.txdot
	//					.isd
	//					.rts
	//					.services
	//					.util
	//					.constants
	//					.LocalOptionConstant
	//					.REGION)
	//		{
	//			cbVTRRgn = true;
	//			NUM_CHKBOX = 11;
	//			// is this number right?
	//		}
	//	}
	//}
	// end defect 7891

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 */
	public void setData(Object aaData)
	{
		if (aaData != null)
		{
			caSecData = (SecurityClientDataObject) aaData;
			SecurityData laSecData =
				(SecurityData) caSecData.getSecData();
			getlblEmployeeId().setText(laSecData.getEmpId());
			getlblEmployeeLastName().setText(
				laSecData.getEmpLastName());
			getlblEmployeeFirstName().setText(
				laSecData.getEmpFirstName());
			getlblEmployeeMName().setText(laSecData.getEmpMI());
			if (laSecData.getStatusChngAccs() == 1)
			{
				getchkStatusChange().setSelected(true);
			}
			if (laSecData.getTtlSurrAccs() == 1)
			{
				getchkTitleSurrenered().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getStlnSRSAccs() == 1)
			{
				getchkStolen().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getCancRegAccs() == 1)
			{
				getchkCancelRegistration().setSelected(true);
				// defect 9085 
				//if (cbVTRRgn)
				//{
				ciEnableCount = ciEnableCount + 1;
				//}
				// end defect 9085 
			}
			if (laSecData.getRegRefAmtAccs() == 1)
			{
				getchkRegistration().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getMailRtrnAccs() == 1)
			{
				getchkMailRet().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getRgstrByAccs() == 1)
			{
				getchkRegisteredBy().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getMiscRemksAccs() == 1)
			{
				getchkMisc().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getLegalRestrntNoAccs() == 1)
			{
				getchkLegalRestraint().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getBndedTtlCdAccs() == 1)
			{
				getchkBonded().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getTtlRevkdAccs() == 1)
			{
				getchkTitleRevoked().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getJnkAccs() == 1)
			{
				getchkVehicleJunked().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			//defect 11228
			if (laSecData.getExportAccs() == 1)
			{
				getchkExport().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			//end defect 11228
		}
		//Enable disable check boxes
		disableBtnOnOfcId();
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Status change
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC008(true);
		//Take the new Values fromt the window and set to data object
		caSecData.getSecData().setBndedTtlCdAccs(
			getIntValue(getchkBonded().isSelected()));
		caSecData.getSecData().setCancRegAccs(
			getIntValue(getchkCancelRegistration().isSelected()));
		caSecData.getSecData().setLegalRestrntNoAccs(
			getIntValue(getchkLegalRestraint().isSelected()));
		caSecData.getSecData().setMailRtrnAccs(
			getIntValue(getchkMailRet().isSelected()));
		caSecData.getSecData().setMiscRemksAccs(
			getIntValue(getchkMisc().isSelected()));
		caSecData.getSecData().setRgstrByAccs(
			getIntValue(getchkRegisteredBy().isSelected()));
		caSecData.getSecData().setRegRefAmtAccs(
			getIntValue(getchkRegistration().isSelected()));
		caSecData.getSecData().setStatusChngAccs(
			getIntValue(getchkStatusChange().isSelected()));
		caSecData.getSecData().setStlnSRSAccs(
			getIntValue(getchkStolen().isSelected()));
		caSecData.getSecData().setTtlRevkdAccs(
			getIntValue(getchkTitleRevoked().isSelected()));
		caSecData.getSecData().setTtlSurrAccs(
			getIntValue(getchkTitleSurrenered().isSelected()));
		caSecData.getSecData().setJnkAccs(
			getIntValue(getchkVehicleJunked().isSelected()));
		//defect 11228
		caSecData.getSecData().setExportAccs(
				getIntValue(getchkExport().isSelected()));
		//end defect 11228
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
			jPanel.add(getchkStatusChange(), null);
			jPanel.add(getchkVehicleJunked(), null);
			jPanel.add(getchkTitleSurrenered(), null);
			jPanel.add(getchkStolen(), null);
			jPanel.add(getchkCancelRegistration(), null);
			jPanel.add(getchkRegistration(), null);
			jPanel.add(getchkMailRet(), null);
			jPanel.setBounds(20, 112, 282, 224);
		}
		return jPanel;
	}


}
