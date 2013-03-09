package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
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
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmSecurityAccessRightsSpecialPltsSEC018.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Reyes		02/16/2007	Created;
 * 							defect 9124 Ver Special Plates
 * K Harrell	07/06/2007	Unacceptable set as a function of
 *  M Reyes  				chkReports
 * 							modify setValuesToDataObj()
 * 							defect 9085/9124 Ver Special Plates
 * K Harrell	09/17/2007	Add "Help is not available ... " msg
 * 							modify actionPerformed()
 * 							defect 9085/9214 Ver Special Plates 
 * B Hargrove	03/27/2009	Add help. 
 * 							modify actionPerformed() 
 * 							defect 10004 Ver Defect_POS_E 
 * --------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for
 * Special Plates.
 *
 * @version	Defect_POS_E	03/27/2009
 * @author	Mark Reyes
 * <br>Creation Date:		02/16/2007 15:37:00
 */
public class FrmSecurityAccessRightsSpecialPltsSEC018
	extends RTSDialogBox
	implements ActionListener
{
	private javax.swing.JCheckBox ivjchkApplication = null;
	private javax.swing.JCheckBox ivjchkDelete = null;
	private javax.swing.JCheckBox ivjchkRenewPlateOnly = null;
	private javax.swing.JCheckBox ivjchkReports = null;
	private javax.swing.JCheckBox ivjchkReserve = null;
	private javax.swing.JCheckBox ivjchkRevise = null;
	private javax.swing.JCheckBox ivjchkSpecialPlates = null;
	private javax.swing.JCheckBox ivjchkUnacceptable = null;
	private javax.swing.JLabel ivjlblEmployeeId = null;
	private javax.swing.JLabel ivjstcLblEmployeeId = null;
	private javax.swing.JLabel ivjstcLblEmployeeName = null;
	private javax.swing.JLabel ivjstcLblSplPlts = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSecurityAccessRightsSplPltsSEC018ContentPanel =
		null;
	private int ciEnableCount = 0;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private SecurityClientDataObject caSecData = null;
	private boolean cbSelectAll = false;

	private static final String APPL = "Application";
	private static final String DEL = "Delete";
	private static final String RENEW_PLATE_ONLY = "Renew Plate Only";
	private static final String RPTS = "Reports";
	private static final String RSVR = "Reserve";
	private static final String REV = "Revise";
	private static final String SPL_PLTS = "Special Plates";
	private static final String UNACCPT = "Unacceptable";
	private static final String LABEL1 = "JLabel1";
	private static final String LABEL2 = "JLabel2";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC018_FRAME_TITLE =
		" Security Access Rights Special Plates   SEC018";

	private RTSButton[] carrRTSBtn = new RTSButton[3];
	private JCheckBox[] carrBtnSec = new JCheckBox[8];
	private int ciSelect = 0;
	private int ciSelectSec = 0;

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);

	}

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018(
		Dialog aaOwner,
		String String,
		boolean abModal)
	{
		super(aaOwner, String, abModal);
	}

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsSpecialPltsSEC018 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsSpecialPltsSEC018(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);

	}

	/**
	 * Invoke when an action occurs
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
				caSecData.setSEC018(false);
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
				// defect 10004
				// defect 9085 
				//RTSException leRTSEx =
				//	new RTSException(
				//		RTSException.WARNING_MESSAGE,
				//		ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
				//		"Information");
				//leRTSEx.displayError(this);
				// end defect 9085
				RTSHelp.displayHelp(RTSHelp.SEC018);
				// end defect 10004 
			}
			else if (aaAE.getSource() == getchkSpecialPlates())
			{
				if (!getchkSpecialPlates().isSelected())
				{
					cbSelectAll = false;
				}
				else
				{
					cbSelectAll = true;
				}
				selectChkButtons(cbSelectAll);
			}
			else if (aaAE.getSource() == getchkApplication())
			{
				if (getchkApplication().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkDelete())
			{
				if (getchkDelete().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkRenewPlateOnly())
			{
				if (getchkRenewPlateOnly().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkReports())
			{
				if (getchkReports().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkReserve())
			{
				if (getchkReserve().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkRevise())
			{
				if (getchkRevise().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkUnacceptable())
			{
				if (getchkUnacceptable().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
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
	public void checkCountZero()
	{
		if (ciEnableCount == 0)
		{
			getchkSpecialPlates().setSelected(false);
		}
		else
		{
			getchkSpecialPlates().setSelected(true);
		}

	}
	// defect 9124
	private void disableBtnOnOfcId()
	{
		getchkDelete().setEnabled(SystemProperty.isHQ());
		getchkReserve().setEnabled(SystemProperty.isHQ());
		getchkUnacceptable().setEnabled(SystemProperty.isHQ());
	}

	// end defect 9124

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
				ivjButtonPanel1.setBounds(92, 364, 382, 66);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}
	/**
	 * Return the chkApplication property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkApplication()
	{
		if (ivjchkApplication == null)
		{
			try
			{
				ivjchkApplication = new JCheckBox();
				ivjchkApplication.setName("chkApplicaion");
				ivjchkApplication.setMnemonic('a');
				ivjchkApplication.setText(APPL);
				ivjchkApplication.setMaximumSize(
					new Dimension(101, 22));
				ivjchkApplication.setActionCommand(APPL);
				ivjchkApplication.setBounds(167, 138, 218, 22);
				ivjchkApplication.setMinimumSize(
					new Dimension(101, 22));
				ivjchkApplication.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkApplication;
	}

	/**
	 * Return the chkDelete property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkDelete()
	{
		if (ivjchkDelete == null)
		{
			try
			{
				ivjchkDelete = new JCheckBox();
				ivjchkDelete.setName("chkDelete");
				ivjchkDelete.setMnemonic('d');
				ivjchkDelete.setText(DEL);
				ivjchkDelete.setMaximumSize(new Dimension(85, 22));
				ivjchkDelete.setActionCommand(DEL);
				ivjchkDelete.setBounds(167, 298, 218, 22);
				ivjchkDelete.setMinimumSize(new Dimension(85, 22));
				ivjchkDelete.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkDelete;
	}
	/**
	 * Return the chkRenewPlateOnly property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkRenewPlateOnly()
	{
		if (ivjchkRenewPlateOnly == null)
		{
			try
			{
				ivjchkRenewPlateOnly = new JCheckBox();
				ivjchkRenewPlateOnly.setName("chkRenewPlateOnly");
				ivjchkRenewPlateOnly.setMnemonic('n');
				ivjchkRenewPlateOnly.setText(RENEW_PLATE_ONLY);
				ivjchkRenewPlateOnly.setMaximumSize(
					new Dimension(151, 22));
				ivjchkRenewPlateOnly.setActionCommand(RENEW_PLATE_ONLY);
				ivjchkRenewPlateOnly.setBounds(167, 170, 218, 22);
				ivjchkRenewPlateOnly.setMinimumSize(
					new Dimension(151, 22));
				ivjchkRenewPlateOnly.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkRenewPlateOnly;
	}

	/**
	 * Return the chkRevise property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkRevise()
	{
		if (ivjchkRevise == null)
		{
			try
			{
				ivjchkRevise = new JCheckBox();
				ivjchkRevise.setName("chkRevise");
				ivjchkRevise.setMnemonic('v');
				ivjchkRevise.setText(REV);
				ivjchkRevise.setMaximumSize(new Dimension(85, 22));
				ivjchkRevise.setActionCommand(REV);
				ivjchkRevise.setBounds(167, 202, 218, 22);
				ivjchkRevise.setMinimumSize(new Dimension(85, 22));
				ivjchkRevise.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkRevise;
	}
	/**
	 * Return the chkReserve property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkReserve()
	{
		if (ivjchkReserve == null)
		{
			try
			{
				ivjchkReserve = new JCheckBox();
				ivjchkReserve.setName("chkReserve");
				ivjchkReserve.setMnemonic('r');
				ivjchkReserve.setText(RSVR);
				ivjchkReserve.setMaximumSize(new Dimension(85, 22));
				ivjchkReserve.setActionCommand(RSVR);
				ivjchkReserve.setBounds(167, 234, 218, 22);
				ivjchkReserve.setMinimumSize(new Dimension(85, 22));
				ivjchkReserve.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkReserve;
	}

	/**
	 * Return the chkReport property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkReports()
	{
		if (ivjchkReports == null)
		{
			try
			{
				ivjchkReports = new JCheckBox();
				ivjchkReports.setName("chkReports");
				ivjchkReports.setMnemonic('p');
				ivjchkReports.setText(RPTS);
				ivjchkReports.setMaximumSize(new Dimension(85, 22));
				ivjchkReports.setActionCommand(RPTS);
				ivjchkReports.setBounds(167, 330, 218, 22);
				ivjchkReports.setMinimumSize(new Dimension(85, 22));
				ivjchkReports.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkReports;
	}

	/**
	 * Return the chkSpecialPlates property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSpecialPlates()
	{
		if (ivjchkSpecialPlates == null)
		{
			try
			{
				ivjchkSpecialPlates = new JCheckBox();
				ivjchkSpecialPlates.setName("chkSpecialPlates");
				ivjchkSpecialPlates.setMnemonic('s');
				ivjchkSpecialPlates.setText(SPL_PLTS);
				ivjchkSpecialPlates.setMaximumSize(
					new Dimension(151, 22));
				ivjchkSpecialPlates.setActionCommand(SPL_PLTS);
				ivjchkSpecialPlates.setBounds(167, 106, 218, 22);
				ivjchkSpecialPlates.setMinimumSize(
					new Dimension(151, 22));
				ivjchkSpecialPlates.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkSpecialPlates;
	}

	/**
	 * Return the chkUnacceptable property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkUnacceptable()
	{
		if (ivjchkUnacceptable == null)
		{
			try
			{
				ivjchkUnacceptable = new JCheckBox();
				ivjchkUnacceptable.setName("chkUnacceptable");
				ivjchkUnacceptable.setMnemonic('u');
				ivjchkUnacceptable.setText(UNACCPT);
				ivjchkUnacceptable.setMaximumSize(
					new Dimension(101, 22));
				ivjchkUnacceptable.setActionCommand(UNACCPT);
				ivjchkUnacceptable.setBounds(167, 266, 218, 22);
				ivjchkUnacceptable.setMinimumSize(
					new Dimension(101, 22));
				ivjchkUnacceptable.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkUnacceptable;
	}

	/**
	 * Return the FrmSecurityAccessRightsSpecialPltsSEC018ContentPanel
	 * property value
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
	{
		if (ivjFrmSecurityAccessRightsSplPltsSEC018ContentPanel
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsSplPltsSEC018ContentPanel =
					new JPanel();
				ivjFrmSecurityAccessRightsSplPltsSEC018ContentPanel
					.setName(
					"FrmSecurityAccessRightsSpecialPltsSEC018ContentPanel");
				ivjFrmSecurityAccessRightsSplPltsSEC018ContentPanel
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsSplPltsSEC018ContentPanel
					.setMaximumSize(
					new Dimension(482, 325));
				ivjFrmSecurityAccessRightsSplPltsSEC018ContentPanel
					.setMinimumSize(
					new Dimension(482, 325));
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getstcLblSplPlts(),
					getstcLblSplPlts().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getchkSpecialPlates(),
					getchkSpecialPlates().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getchkApplication(),
					getchkApplication().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getchkRenewPlateOnly(),
					getchkRenewPlateOnly().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getchkRevise(),
					getchkRevise().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getchkReserve(),
					getchkReserve().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getchkUnacceptable(),
					getchkUnacceptable().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getchkDelete(),
					getchkDelete().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getchkReports(),
					getchkReports().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel()
					.add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());

			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSecurityAccessRightsSplPltsSEC018ContentPanel;
	}

	/**
	 * Get the int equivalent for the boolean parameter
	 * Returns 1 if parameter is true or return 0.
	 * 
	 * @return int
	 * @param lbVal boolean
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
	private JLabel getlblEmployeeFirstName()
	{
		if (ivjlblEmployeeFirstName == null)
		{
			try
			{
				ivjlblEmployeeFirstName = new JLabel();
				ivjlblEmployeeFirstName.setName("lblEmployeeId");
				ivjlblEmployeeFirstName.setText(LABEL1);
				ivjlblEmployeeFirstName.setBounds(342, 35, 183, 14);

			}
			catch (Throwable aeIVJEx)
			{
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
				ivjlblEmployeeId.setBounds(159, 12, 119, 14);

			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjlblEmployeeId;
	}
	/**
	 * Return the lblEmployeeLastName property value.
	 *  
	 * @return JLabel
	 */
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
				ivjlblEmployeeLastName.setBounds(159, 35, 173, 14);

			}
			catch (Throwable aeIVJEx)
			{
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
	private JLabel getlblEmployeeMName()
	{
		if (ivjlblEmployeeMName == null)
		{
			try
			{
				ivjlblEmployeeMName = new JLabel();
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(LABEL2);
				ivjlblEmployeeMName.setBounds(528, 36, 56, 14);

			}
			catch (Throwable aeIVJEx)
			{
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
				ivjstcLblEmployeeId.setBounds(33, 8, 83, 18);

			}
			catch (Throwable aeIVJEx)
			{
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
				ivjstcLblEmployeeName.setBounds(33, 35, 113, 14);

			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEmployeeName;
	}

	/**
	 * Return the stcLblSplPlts property value.
	 *  
	 * @return JLabel
	 */
	private JLabel getstcLblSplPlts()
	{
		if (ivjstcLblSplPlts == null)
		{
			try
			{
				ivjstcLblSplPlts = new JLabel();
				ivjstcLblSplPlts.setName("stcLblSplPlts");
				ivjstcLblSplPlts.setAlignmentX(
					Component.CENTER_ALIGNMENT);
				ivjstcLblSplPlts.setText(SPL_PLTS);
				ivjstcLblSplPlts.setMaximumSize(new Dimension(154, 14));
				ivjstcLblSplPlts.setMinimumSize(new Dimension(154, 14));
				ivjstcLblSplPlts.setBounds(192, 68, 263, 14);

			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblSplPlts;
	}

	/**
	 * Called whenever the part throws an excepton.
	 * 
	 * @param aaEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
	}

	/**
	 * Initialized the class
	 *  
	 */
	private void initialize()
	{
		try
		{
			setName("FrmSecurityAccessRightsSpecialPltsSEC018");
			setSize(600, 472);
			setTitle(SEC018_FRAME_TITLE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setContentPane(
				getFrmSecurityAccessRightsSpecialPltsSEC018ContentPanel());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		carrRTSBtn[0] = getButtonPanel1().getBtnEnter();
		carrRTSBtn[1] = getButtonPanel1().getBtnCancel();
		carrRTSBtn[2] = getButtonPanel1().getBtnHelp();
		carrBtnSec[0] = getchkSpecialPlates();
		carrBtnSec[1] = getchkApplication();
		carrBtnSec[2] = getchkRenewPlateOnly();
		carrBtnSec[3] = getchkRevise();
		carrBtnSec[4] = getchkReserve();
		carrBtnSec[5] = getchkUnacceptable();
		carrBtnSec[6] = getchkDelete();
		carrBtnSec[7] = getchkReports();
	}

	/**
	 * Invoke when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		if (aaKE.getSource() instanceof JCheckBox)
		{
			ciSelectSec = 0;
			for (int i = 0; i < 8; i++)
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
					if (ciSelectSec > 7)
					{
						ciSelectSec = 0;
					}
					if (ciSelectSec < 8
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
						ciSelectSec = 7;
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
		super.keyPressed(aaKE);

	}

	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSecurityAccessRightsSpecialPltsSEC018 laFrmSecurityAccessRightsSpecialPltsSEC018;
			laFrmSecurityAccessRightsSpecialPltsSEC018 =
				new FrmSecurityAccessRightsSpecialPltsSEC018();
			laFrmSecurityAccessRightsSpecialPltsSEC018.setModal(true);
			laFrmSecurityAccessRightsSpecialPltsSEC018
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSecurityAccessRightsSpecialPltsSEC018.show();
			Insets insets =
				laFrmSecurityAccessRightsSpecialPltsSEC018.getInsets();
			laFrmSecurityAccessRightsSpecialPltsSEC018.setSize(
				laFrmSecurityAccessRightsSpecialPltsSEC018.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSecurityAccessRightsSpecialPltsSEC018.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSecurityAccessRightsSpecialPltsSEC018.setVisibleRTS(
				true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeIVJEx.printStackTrace(System.out);
		}

	}
	/**
	 * Sets the various check boxes depending on whether
	 * they are neabled and updates iEnableCount
	 * 
	 * @param abVal boolean
	 */
	private void selectChkButtons(boolean abVal)
	{
		if (getchkReserve().isEnabled())
		{
			if (abVal && !getchkReserve().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkReserve().isSelected())
			{
				ciEnableCount--;
			}
			getchkReserve().setSelected(abVal);
		}
		else
		{
			if (getchkReserve().isSelected())
			{
				getchkReserve().setSelected(false);
				ciEnableCount--;
			}
		}
		if (getchkRenewPlateOnly().isEnabled())
		{
			if (abVal && !getchkRenewPlateOnly().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkRenewPlateOnly().isSelected())
			{
				ciEnableCount--;
			}
			getchkRenewPlateOnly().setSelected(abVal);
		}
		if (getchkReports().isEnabled())
		{
			if (abVal && !getchkReports().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkReports().isSelected())
			{
				ciEnableCount--;
			}
			getchkReports().setSelected(abVal);
		}
		if (getchkRevise().isEnabled())
		{
			if (abVal && !getchkRevise().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkRevise().isSelected())
			{
				ciEnableCount--;
			}
			getchkRevise().setSelected(abVal);
		}
		if (getchkApplication().isEnabled())
		{
			if (abVal && !getchkApplication().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkApplication().isSelected())
			{
				ciEnableCount--;
			}
			getchkApplication().setSelected(abVal);
		}
		if (getchkUnacceptable().isEnabled())
		{
			if (abVal && !getchkUnacceptable().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkUnacceptable().isSelected())
			{
				ciEnableCount--;
			}
			getchkUnacceptable().setSelected(abVal);
		}
		else
		{
			if (getchkUnacceptable().isSelected())
			{
				getchkUnacceptable().setSelected(false);
			}

		}
		if (getchkDelete().isEnabled())
		{
			if (abVal && !getchkDelete().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkDelete().isSelected())
			{
				ciEnableCount--;
			}
			getchkDelete().setSelected(abVal);
		}
		else
		{
			if (getchkDelete().isSelected())
			{
				getchkDelete().setSelected(false);
			}

		}

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
			// defect 9124
			disableBtnOnOfcId();
			// end defect 9124
			if (laSecData.getSpclPltAccs() == 1)
			{
				getchkSpecialPlates().setSelected(true);
			}
			if (laSecData.getSpclPltApplAccs() == 1)
			{
				getchkApplication().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getSpclPltRenwPltAccs() == 1)
			{
				getchkRenewPlateOnly().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getSpclPltRevisePltAccs() == 1)
			{
				getchkRevise().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getSpclPltResrvPltAccs() == 1)
			{
				getchkReserve().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getSpclPltUnAccptblPltAccs() == 1)
			{
				getchkUnacceptable().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getSpclPltDelPltAccs() == 1)
			{
				getchkDelete().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getSpclPltRptsAccs() == 1)
			{
				getchkReports().setSelected(true);
				ciEnableCount++;
			}
		}
		checkCountZero();
	}
	/**
	 * Populate the SecurityClientDataObject with security access
	 * rights for Special Plates
	 * 
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC018(true);
		
		// Take the new values from the window and set to data object
		caSecData.getSecData().setSpclPltAccs(
			getIntValue(getchkSpecialPlates().isSelected()));
			
		caSecData.getSecData().setSpclPltApplAccs(
			getIntValue(getchkApplication().isSelected()));
			
		caSecData.getSecData().setSpclPltDelPltAccs(
			getIntValue(getchkDelete().isSelected()));
			
		caSecData.getSecData().setSpclPltRenwPltAccs(
			getIntValue(getchkRenewPlateOnly().isSelected()));
			
		caSecData.getSecData().setSpclPltResrvPltAccs(
			getIntValue(getchkReserve().isSelected()));
			
		caSecData.getSecData().setSpclPltRevisePltAccs(
			getIntValue(getchkRevise().isSelected()));
			
		caSecData.getSecData().setSpclPltRptsAccs(
			getIntValue(getchkReports().isSelected()));
			
		caSecData.getSecData().setSpclPltUnAccptblPltAccs(
			getIntValue(getchkUnacceptable().isSelected()));

	}

}
