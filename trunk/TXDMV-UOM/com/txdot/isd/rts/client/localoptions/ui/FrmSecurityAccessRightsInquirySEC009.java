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
 *
 * FrmSecurityAccessRightsInquirySEC009.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		10/25/2002	Modify actionPerformed(), disableBtnOnOfcId()
 *							windowOpened(), and 
 *							getFrmSecirotyAccessRightsInquirySEC009.
 *							Added getlblTextInq() and getlblTextInq2().
 *							Removed getxtInq() for shift tab. 
 *							defect 4882 
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		03/15/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3 
 * Min Wang		03/30/2005	Remove setNextFocusable's
 * 							defect 7891 ver 5.2.3
 * Min Wang 	04/06/2005	remove array since they are causing 
 * 							initialization errors.
 * 							delete carrRTSBtn
 * 							modify initialize(), keyPressed()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	remove unused method
 * 							delete selectChkButtons()
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
 * Min Wang		11/01/2005  Uncomment code for arrow key function.
 * 							Add carrRTSBtn, ciSelect
 * 							modify keyPressed()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	01/03/2006	Removed color; Mnemonic work
 * 							defect 7891, 8400 Ver 5.2.3 
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty() 
 * 							modify disableBtnOnOfcId()
 * 							defect 9085 Ver Special Plates     
 * --------------------------------------------------------------------- 
 */

/**
 * This class is used for managing security access rights for
 * Inquiry. Data on this screen is managed through 
 * SecurityClientDataObject.
 *
 * @version	Special Plates	04/20/2007 
 * @author	Administrator
 * <br>Creation Date:		06/28/2001 11:50:09  
 */

public class FrmSecurityAccessRightsInquirySEC009
	extends RTSDialogBox
	implements ActionListener, WindowListener
{
	private JCheckBox ivjchkInquiry = null;
	private JCheckBox ivjchkPlateNo = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblInquiry = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSecurityAccessRightsInquirySEC009ContentPane1 =
		null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblTextInq = null;
	private JLabel ivjlblTextInq2 = null;

	// int 		
	private int ciEnableCount = 0;

	// Object 
	private SecurityClientDataObject caSecData = null;

	private static final String TEXT =
		"If 'Inquiry' is not checked, access will not be allowed for";
	private static final String TEXT2 =
		"Vehicle Information, Plate Options & Plate No.";
	private static final String TEXT3 =
		"If 'Inquiry' is checked, access will be allowed for Vehicle";
	private static final String TEXT4 = "Information & Plate Options.";
	private static final String SEC009_FRAME_TITLE =
		"Security Access Rights Inquiry   SEC009";
	private static final String INQUIRY = "Inquiry";
	private static final String PLATENO = "Plate No";
	private static final String LABEL1 = "JLabel1";
	private static final String LABEL2 = "JLabel2";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	// defect 7891
	private RTSButton[] carrRTSBtn = new RTSButton[3];
	private int ciSelect = 0;
	// end defect 7891
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 */
	public FrmSecurityAccessRightsInquirySEC009()
	{
		super();
		initialize();
	}
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsInquirySEC009(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsInquirySEC009(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsInquirySEC009(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsInquirySEC009(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsInquirySEC009(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsInquirySEC009(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsInquirySEC009(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}
	/**
	 * FrmSecurityAccessRightsInquirySEC009 constructor comment.
	 * 
	 * @param aaOwner  Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsInquirySEC009(
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
				caSecData.setSEC009(false);
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
				RTSHelp.displayHelp(RTSHelp.SEC009);
				return;
			}
			else if (aaAE.getSource() == getchkInquiry())
			{
				getchkPlateNo().setEnabled(
					getchkInquiry().isSelected());
				if (!getchkInquiry().isSelected())
				{
					getchkPlateNo().setSelected(false);
					getchkPlateNo().setEnabled(false);
					getlblTextInq().setText(TEXT);
					getlblTextInq2().setText(TEXT2);
				}
				else
				{
					getlblTextInq().setText(TEXT3);
					getlblTextInq2().setText(TEXT4);
				}
				if (getchkInquiry().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkPlateNo())
			{
				if (getchkPlateNo().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
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
			getchkInquiry().setSelected(false);
		}
		else
		{
			getchkInquiry().setSelected(true);
		}
	}

	/**
	 * Disable the various checkbox for County or Region or VTR
	 */
	private void disableBtnOnOfcId()
	{
		if (SystemProperty.isCounty())
		{
			getlblTextInq().setText(TEXT);
			getlblTextInq2().setText(TEXT2);
		}
		getchkPlateNo().setEnabled(
			caSecData.getSecData().getInqAccs() == 1);

		// defect 9085 
		//		if (caSecData.getWorkStationType()
		//			== LocalOptionConstant.COUNTY)
		//		{
		//			getchkPlateNo().setEnabled(false);
		//			getlblTextInq().setText(TEXT);
		//			getlblTextInq2().setText(TEXT2);
		//		}
		//		else if (
		//			caSecData.getWorkStationType()
		//				== LocalOptionConstant.REGION)
		//		{
		//		}
		//		else if (
		//			caSecData.getWorkStationType() == LocalOptionConstant.VTR)
		//		{
		//		}
		//		if (caSecData.getSecData().getInqAccs() == 1)
		//		{
		//			getchkPlateNo().setEnabled(true);
		//		}
		//		else
		//		{
		//			getchkPlateNo().setEnabled(false);
		//		}
		// end defect 9085
	}
	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return gui.ButtonPanel
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
				ivjButtonPanel1.setBounds(177, 249, 269, 87);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				//ivjButtonPanel1.getBtnHelp().setNextFocusableComponent(
				//	getchkInquiry());
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
	 * Return the chkInquiry property value.
	 * 
	 * @return  JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkInquiry()
	{
		if (ivjchkInquiry == null)
		{
			try
			{
				ivjchkInquiry = new JCheckBox();
				ivjchkInquiry.setName("chkInquiry");
				ivjchkInquiry.setMnemonic(java.awt.event.KeyEvent.VK_I);
				ivjchkInquiry.setText(INQUIRY);
				ivjchkInquiry.setMaximumSize(new Dimension(63, 22));
				ivjchkInquiry.setActionCommand(INQUIRY);
				ivjchkInquiry.setBounds(192, 121, 111, 22);
				ivjchkInquiry.setMinimumSize(new Dimension(63, 22));
				// user code begin {1}
				ivjchkInquiry.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkInquiry;
	}

	/**
	 * Return the chkPlateNo property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkPlateNo()
	{
		if (ivjchkPlateNo == null)
		{
			try
			{
				ivjchkPlateNo = new JCheckBox();
				ivjchkPlateNo.setName("chkPlateNo");
				ivjchkPlateNo.setMnemonic(java.awt.event.KeyEvent.VK_P);
				ivjchkPlateNo.setText(PLATENO);
				ivjchkPlateNo.setMaximumSize(new Dimension(72, 22));
				ivjchkPlateNo.setActionCommand(PLATENO);
				ivjchkPlateNo.setBounds(246, 156, 99, 22);
				ivjchkPlateNo.setMinimumSize(new Dimension(72, 22));
				// user code begin {1}
				ivjchkPlateNo.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkPlateNo;
	}

	/**
	 * Return the FrmSecurityAccessRightsInquirySEC009ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsInquirySEC009ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsInquirySEC009ContentPane1
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsInquirySEC009ContentPane1 =
					new JPanel();
				ivjFrmSecurityAccessRightsInquirySEC009ContentPane1
					.setName(
					"FrmSecurityAccessRightsInquirySEC009ContentPane1");
				ivjFrmSecurityAccessRightsInquirySEC009ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsInquirySEC009ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmSecurityAccessRightsInquirySEC009ContentPane1
					.setMinimumSize(
					new Dimension(498, 315));
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getstcLblInquiry(),
					getstcLblInquiry().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getchkInquiry(),
					getchkInquiry().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getchkPlateNo(),
					getchkPlateNo().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getlblTextInq(),
					getlblTextInq().getName());
				getFrmSecurityAccessRightsInquirySEC009ContentPane1()
					.add(
					getlblTextInq2(),
					getlblTextInq2().getName());
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
		return ivjFrmSecurityAccessRightsInquirySEC009ContentPane1;
	}

	/**
	 * Get the int equivalent for the boolean parameter
	 * Returns 1 if parameter is true or returns 0.
	 * 
	 * @param bVal boolean
	 * @return int
	 */
	private int getIntValue(boolean bVal)
	{
		if (bVal == true)
		{
			return 1;
		}
		return 0;
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
				ivjlblEmployeeFirstName.setBounds(333, 42, 181, 14);
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
				ivjlblEmployeeId.setBounds(143, 17, 106, 14);
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
	private javax.swing.JLabel getlblEmployeeLastName()
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
				ivjlblEmployeeLastName.setBounds(143, 42, 175, 14);
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
				ivjlblEmployeeMName = new javax.swing.JLabel();
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(LABEL2);
				ivjlblEmployeeMName.setBounds(525, 42, 53, 14);
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
	 * Return the lblTextInq property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblTextInq()
	{
		if (ivjlblTextInq == null)
		{
			try
			{
				ivjlblTextInq = new JLabel();
				ivjlblTextInq.setName("lblTextInq");
				ivjlblTextInq.setFont(new Font("dialog", 0, 12));
				ivjlblTextInq.setText(LABEL1);
				ivjlblTextInq.setBounds(132, 189, 378, 21);
				//ivjlblTextInq.setForeground(new Color(102, 102, 171));
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
		return ivjlblTextInq;
	}

	/**
	 * Return the lblTextInq2 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblTextInq2()
	{
		if (ivjlblTextInq2 == null)
		{
			try
			{
				ivjlblTextInq2 = new JLabel();
				ivjlblTextInq2.setName("lblTextInq2");
				ivjlblTextInq2.setFont(new Font("dialog", 0, 12));
				ivjlblTextInq2.setText(LABEL1);
				ivjlblTextInq2.setBounds(132, 204, 378, 21);
				//ivjlblTextInq2.setForeground(new Color(102, 102, 171));
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
		return ivjlblTextInq2;
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
				ivjstcLblEmployeeId.setBounds(15, 17, 83, 18);
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
				ivjstcLblEmployeeName.setBounds(15, 40, 113, 14);
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
	 * Return the stcLblInquiry property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInquiry()
	{
		if (ivjstcLblInquiry == null)
		{
			try
			{
				ivjstcLblInquiry = new javax.swing.JLabel();
				ivjstcLblInquiry.setName("stcLblInquiry");
				ivjstcLblInquiry.setText(INQUIRY);
				ivjstcLblInquiry.setMaximumSize(new Dimension(38, 14));
				ivjstcLblInquiry.setMinimumSize(new Dimension(38, 14));
				ivjstcLblInquiry.setBounds(231, 80, 122, 14);
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
		return ivjstcLblInquiry;
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
		leRTSEx.writeExceptionToLog();
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
			// focus manager necessary to make form follow tab tag order
			//FocusManager.setCurrentManager(
			//	new ContainerOrderFocusManager());
			// user code end
			setName("FrmSecurityAccessRightsInquirySEC009");
			setSize(600, 357);
			setTitle(SEC009_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmSecurityAccessRightsInquirySEC009ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		addWindowListener(this);
		// defect 7891 
		carrRTSBtn[0] = getButtonPanel1().getBtnEnter();
		carrRTSBtn[1] = getButtonPanel1().getBtnCancel();
		carrRTSBtn[2] = getButtonPanel1().getBtnHelp();
		// end defect 7891
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
			//empty code block
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
				boolean bStop = true;
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
						bStop = false;
					}
				}
				while (bStop);
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_LEFT
					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				boolean bStop = true;
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
						bStop = false;
					}
				}
				while (bStop);
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
			FrmSecurityAccessRightsInquirySEC009 laFrmSecurityAccessRightsInquirySEC009;
			laFrmSecurityAccessRightsInquirySEC009 =
				new FrmSecurityAccessRightsInquirySEC009();
			laFrmSecurityAccessRightsInquirySEC009.setModal(true);
			laFrmSecurityAccessRightsInquirySEC009
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmSecurityAccessRightsInquirySEC009.show();
			Insets insets =
				laFrmSecurityAccessRightsInquirySEC009.getInsets();
			laFrmSecurityAccessRightsInquirySEC009.setSize(
				laFrmSecurityAccessRightsInquirySEC009.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSecurityAccessRightsInquirySEC009.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSecurityAccessRightsInquirySEC009.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}
	// defect 7891
	///**
	// * Sets the various check boxes depending on whether 
	// * they are enabled and updates iEnableCount
	// * 
	// * @param abVal boolean
	// */
	//private void selectChkButtons(boolean abVal)
	//{
	//	if (getchkPlateNo().isEnabled())
	//	{
	//		if (abVal && !getchkPlateNo().isSelected())
	//		{
	//			ciEnableCount++;
	//		}
	//		else if (!abVal && getchkPlateNo().isSelected())
	//		{
	//			ciEnableCount--;
	//		}
	//		getchkPlateNo().setSelected(abVal);
	//	}
	//	if (getchkInquiry().isEnabled())
	//	{
	//		if (abVal && !getchkInquiry().isSelected())
	//		{
	//			ciEnableCount++;
	//		}
	//		else if (!abVal && getchkInquiry().isSelected())
	//		{
	//			ciEnableCount--;
	//		}
	//		getchkInquiry().setSelected(abVal);
	//	}
	//}

	/**
	 * Sets the view for the frame
	 *
	 * @param aaController Controller
	 */
	//public void setController(AbstractViewController aaController)
	//{
	//	super.setController(aaController);
	//}

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
			SecurityData secData =
				(SecurityData) caSecData.getSecData();
			getlblEmployeeId().setText(secData.getEmpId());
			getlblEmployeeLastName().setText(secData.getEmpLastName());
			getlblEmployeeFirstName().setText(
				secData.getEmpFirstName());
			getlblEmployeeMName().setText(secData.getEmpMI());
			if (secData.getInqAccs() == 1)
			{
				getchkInquiry().setSelected(true);
				ciEnableCount++;
			}
			if (secData.getPltNoAccs() == 1)
			{
				getchkPlateNo().setSelected(true);
				ciEnableCount++;
			}
		}
		//Enable disable check boxes
		disableBtnOnOfcId();
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Inquiry. 
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC009(true);
		//Take the new Values fromt the window and set to data object
		caSecData.getSecData().setInqAccs(
			getIntValue(getchkInquiry().isSelected()));
		caSecData.getSecData().setPltNoAccs(
			getIntValue(getchkPlateNo().isSelected()));
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowActivated(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosed(WindowEvent aaWE)
	{
		//empty code block	
	}

	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.  If the program does not 
	 * explicitly hide or dispose the window while processing 
	 * this event, the window close operation will be cancelled.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosing(WindowEvent aaWE)
	{
		//empty code block	
	}

	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowDeactivated(WindowEvent aaWE)
	{
		//empty code block	
	}

	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowDeiconified(WindowEvent e)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * iconImage property.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowIconified(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked the first time a window is made visible.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowOpened(WindowEvent aaWE)
	{
		if (getchkInquiry().isSelected())
		{
			getlblTextInq().setText(TEXT3);
			getlblTextInq2().setText(TEXT4);
		}
		else
		{
			getlblTextInq().setText(TEXT);
			getlblTextInq2().setText(TEXT2);
		}
	}
}
