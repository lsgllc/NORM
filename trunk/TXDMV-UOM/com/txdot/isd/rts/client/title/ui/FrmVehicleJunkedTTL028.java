package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.services.cache.IndicatorDescriptionsCache;
import com.txdot.isd.rts.services.data.IndicatorDescriptionsData;
import com.txdot.isd.rts.services.data.SalvageData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
import java.awt.Rectangle;

/*
 * 
 * FrmVehicleJunkedTTL028.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			Made changes for validations
 * B Arredondo	12/15/2003	Modified visual composition to allow
 *							for the reason to fit with no dots at the 
 *							end.
 *							defect 6192 Ver 5.1.5.2
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3                 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * T Pederson	12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify populateJunkReasons().
 * 							defect 8479 Ver 5.2.3  
 * K Harrell	04/02/2010	Update the last element on Junk Vector
 * 							delete getBuilderData()  
 * 							modify setDataToVehObj() 
 * 							defect 10428 Ver POS_640 
 * K Harrell	10/13/2011 	State/Cntry field to Alpha Only
 * 							add ivjtxtStateCountry(), get method
 * 							delete ivjtxtState, get method
 * 							delete STATE_MAX_LEN
 * 							modify getFrmVehicleJunkedTTL028ContentPane1()  
 * 							defect 11004 Ver 6.9.0
 * K Harrell	10/15/2011	OSTitle, Salvage Yard to AlphaNumeric; 
 * 							 additional cleanup,e.g. mnemonics 
 *  						add ivjdateVehicleJunked, get method
 * 							delete ivjdateVehicleJunked, get method
 * 							modify gettxtOSTitle(), gettxtSalvage(),
 * 							 getJPanel1() 
 * 							defect 11004 Ver 6.9.0
 * K Harrell	11/17/2011	Clear Color when combo selected
 * 							modify getcomboReason() 
 * 							defect 11004 Ver 6.9.0 
 * K Harrell	11/30/2011  Restore non-standard names from pre-10/2011
 * 							for Global 360 
 * 							defect 11004 Ver 6.9.0  
 * T Pederson	02/07/2012	Added validation for salvage yard entry.
 * 							Added setting SLVGINDI for Evidence Surrendered
 * 							drop down selections.
 * 							modify setData(), setDataToVehObj()
 * 							modify VEHICLE_JUNK_DATE
 * 							defect 11097 Ver 6.10.0  
 * ------------------------------------------------------------------
 */

/**
 * This form is used to capture vehicle junk information in status 
 * change.
 * 
 * @version	6.10.0			02/07/2012
 * @author	Marx Rajangam
 * <br>Creation Date:		06/26/2001 15:11:35
 */

public class FrmVehicleJunkedTTL028
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	
	/******************************************************** 
	 * WARNING: Do not cleanup w/o consideration of G360 
	 *           automated usage. 
	 ***************************************************/
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JLabel ivjstcLblOtherGovtTitleNo = null;
	private JLabel ivjstcLblSalvageYardNo = null;
	private JLabel ivjstcLblStateCountry = null;
	private JPanel ivjFrmVehicleJunkedTTL028ContentPane1 = null;
	private JCheckBox ivjchkFirst = null;
	private JCheckBox ivjchkSecond = null;
	private JCheckBox ivjchkThird = null;
	private JComboBox ivjcomboReason = null;
	private RTSDateField ivjdateVehicleJunked = null;
	private JLabel ivjstcLblVehicleJunked = null;
	private RTSInputField ivjtxtOSTitle = null;
	private RTSInputField ivjtxtSalvage = null;
	private RTSInputField ivjtxtState = null;
	// defect 11004
	//private final static int STATE_MAX_LEN = 2;
	// end defect 11004 
	
	private VehicleInquiryData caVehInqData = null;
	private Vector cvJnkCds = new Vector();
	
	// Constant int
	private final static int SALVAGE_MAX_LEN = 9;

	private final static int OSTITLE_MAX_LEN = 11;

	// Constant String
	private final static String SELECT_JUNK_REASON =
		"Select junk reason:";
	private final static String SELECT_IS_APPLICABLE =
		"Select if applicable:";
	private final static String FIRST_LIEN_NOT_RELEASED =
		"First Lien Not Released";
	private final static String SECOND_LIEN_NOT_RELEASED =
		"Second Lien Not Released";
	private final static String THIRD_LIEN_NOT_RELEASED =
		"Third Lien Not Released";
	private final static String OS_TITLE_NO = "O/S Title No:";
	private final static String SALVAGE_YARD_NO = "Salvage Yard No:";
	private final static String STATE_COUNTRY = "State/Country:";
	// defect 11097 
	private final static String VEHICLE_JUNK_DATE =
		"Vehicle Junked/Salvaged Date:";
	// end defect 11097 

	/**
	 * FrmVehicleJunkedTTL028 constructor comment.
	 */
	public FrmVehicleJunkedTTL028()
	{
		super();
		initialize();
	}

	/**
	 * FrmVehicleJunkedTTL028 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmVehicleJunkedTTL028(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmVehicleJunkedTTL028 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmVehicleJunkedTTL028(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
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

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 11004 
				if (validateData())
				{
					setDataToVehObj();

					getController().processData(
							AbstractViewController.ENTER,
							getController().getData());
				}
				// end defect 11004 
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
				RTSHelp.displayHelp(RTSHelp.TTL028);
			}
		}
		finally
		{
			doneWorking();
		}
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
				ivjButtonPanel1.setBounds(22, 335, 305, 82);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
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
	 * Return the chkFirst property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkFirst()
	{
		if (ivjchkFirst == null)
		{
			try
			{
				ivjchkFirst = new JCheckBox();
				ivjchkFirst.setName("chkFirst");
				ivjchkFirst.setMnemonic(KeyEvent.VK_F);
				ivjchkFirst.setText(FIRST_LIEN_NOT_RELEASED);
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
		return ivjchkFirst;
	}

	/**
	 * Return the chkSecond property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSecond()
	{
		if (ivjchkSecond == null)
		{
			try
			{
				ivjchkSecond = new JCheckBox();
				ivjchkSecond.setName("chkSecond");
				ivjchkSecond.setMnemonic(KeyEvent.VK_S);
				ivjchkSecond.setText(SECOND_LIEN_NOT_RELEASED);
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
		return ivjchkSecond;
	}

	/**
	 * Return the chkThird property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkThird()
	{
		if (ivjchkThird == null)
		{
			try
			{
				ivjchkThird = new JCheckBox();
				ivjchkThird.setName("chkThird");
				ivjchkThird.setMnemonic(KeyEvent.VK_T);
				ivjchkThird.setText(THIRD_LIEN_NOT_RELEASED);
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
		return ivjchkThird;
	}

	/**
	 * Return the comboReason property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboReason()
	{
		if (ivjcomboReason == null)
		{
			try
			{
				ivjcomboReason = new JComboBox();
				ivjcomboReason.setName("comboReason");
				ivjcomboReason.setBackground(java.awt.Color.white);
				// defect 11004 
				ivjcomboReason.addActionListener(this); 
				// end defect 11004 

				ivjcomboReason.addItemListener(this); 
				
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
		return ivjcomboReason;
	}

	/**
	 * Return the ivjdateVehicleJunked property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField getdateVehicleJunked()
	{
		if (ivjdateVehicleJunked == null)
		{
			try
			{
				ivjdateVehicleJunked = new RTSDateField();
				ivjdateVehicleJunked.setName("dateVehicleJunked");
				ivjdateVehicleJunked.setBounds(193, 18, 101, 20);
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
		return ivjdateVehicleJunked;
	}

	/**
	 * Return the FrmVehicleJunkedTTL028ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmVehicleJunkedTTL028ContentPane1()
	{
		if (ivjFrmVehicleJunkedTTL028ContentPane1 == null)
		{
			try
			{
				ivjFrmVehicleJunkedTTL028ContentPane1 = new JPanel();
				ivjFrmVehicleJunkedTTL028ContentPane1.setName(
					"FrmVehicleJunkedTTL028ContentPane1");
				ivjFrmVehicleJunkedTTL028ContentPane1.setLayout(null);
				ivjFrmVehicleJunkedTTL028ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmVehicleJunkedTTL028ContentPane1.setMinimumSize(
					new java.awt.Dimension(643, 507));
				getFrmVehicleJunkedTTL028ContentPane1().add(
					getstcLblStateCountry(),
					getstcLblStateCountry().getName());
				getFrmVehicleJunkedTTL028ContentPane1().add(
					getstcLblOtherGovtTitleNo(),
					getstcLblOtherGovtTitleNo().getName());
				getFrmVehicleJunkedTTL028ContentPane1().add(
					getstcLblVehicleJunked(),
					getstcLblVehicleJunked().getName());
				getFrmVehicleJunkedTTL028ContentPane1().add(
					getdateVehicleJunked(),
					getdateVehicleJunked().getName());
				getFrmVehicleJunkedTTL028ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmVehicleJunkedTTL028ContentPane1().add(
					gettxtState(),
					gettxtState().getName());
				getFrmVehicleJunkedTTL028ContentPane1().add(
					gettxtOSTitle(),
					gettxtOSTitle().getName());
				getFrmVehicleJunkedTTL028ContentPane1().add(
					getJPanel2(),
					getJPanel2().getName());
				getFrmVehicleJunkedTTL028ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				// user code begin {1}
				ivjFrmVehicleJunkedTTL028ContentPane1.add(gettxtSalvage(), null);
				ivjFrmVehicleJunkedTTL028ContentPane1.add(getstcLblSalvageYardNo(), null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmVehicleJunkedTTL028ContentPane1;
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
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				ivjJPanel1.setBounds(19, 40, 317, 73);

				java.awt.GridBagConstraints constraintscomboReason =
					new java.awt.GridBagConstraints();
				constraintscomboReason.gridx = 1;
				constraintscomboReason.gridy = 1;
				constraintscomboReason.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintscomboReason.weightx = 1.0;
				constraintscomboReason.ipadx = 173;
				constraintscomboReason.ipady = 3;
				constraintscomboReason.insets =
					new java.awt.Insets(25, 11, 22, 13);
				getJPanel1().add(
					getcomboReason(),
					constraintscomboReason);
				// user code begin {1}
				javax.swing.border.Border b =
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_JUNK_REASON);
				ivjJPanel1.setBorder(b);
				// user code end
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
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(new java.awt.GridBagLayout());
				ivjJPanel2.setBounds(22, 212, 305, 119);

				java.awt.GridBagConstraints constraintschkFirst =
					new java.awt.GridBagConstraints();
				constraintschkFirst.gridx = 1;
				constraintschkFirst.gridy = 1;
				constraintschkFirst.ipadx = 82;
				constraintschkFirst.insets =
					new java.awt.Insets(10, 25, 5, 43);
				getJPanel2().add(getchkFirst(), constraintschkFirst);

				java.awt.GridBagConstraints constraintschkSecond =
					new java.awt.GridBagConstraints();
				constraintschkSecond.gridx = 1;
				constraintschkSecond.gridy = 2;
				constraintschkSecond.ipadx = 52;
				constraintschkSecond.insets =
					new java.awt.Insets(5, 25, 5, 55);
				getJPanel2().add(getchkSecond(), constraintschkSecond);

				java.awt.GridBagConstraints constraintschkThird =
					new java.awt.GridBagConstraints();
				constraintschkThird.gridx = 1;
				constraintschkThird.gridy = 3;
				constraintschkThird.ipadx = 61;
				constraintschkThird.insets =
					new java.awt.Insets(5, 25, 11, 60);
				getJPanel2().add(getchkThird(), constraintschkThird);
				// user code begin {1}
				javax.swing.border.Border b =
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_IS_APPLICABLE);
				ivjJPanel2.setBorder(b);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the stcLblOtherGovtTitleNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOtherGovtTitleNo()
	{
		if (ivjstcLblOtherGovtTitleNo == null)
		{
			try
			{
				ivjstcLblOtherGovtTitleNo = new JLabel();
				ivjstcLblOtherGovtTitleNo.setName(
					"stcLblOtherGovtTitleNo");
				ivjstcLblOtherGovtTitleNo.setText(OS_TITLE_NO);
				ivjstcLblOtherGovtTitleNo.setMaximumSize(
					new java.awt.Dimension(68, 14));
				ivjstcLblOtherGovtTitleNo.setMinimumSize(
					new java.awt.Dimension(68, 14));
				ivjstcLblOtherGovtTitleNo.setBounds(77, 187, 86, 14);
				ivjstcLblOtherGovtTitleNo.setHorizontalAlignment(
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
		return ivjstcLblOtherGovtTitleNo;
	}

	/**
	 * Return the ivjstcLblSalvageYardNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSalvageYardNo()
	{
		if (ivjstcLblSalvageYardNo == null)
		{
			try
			{
				ivjstcLblSalvageYardNo = new JLabel();
				ivjstcLblSalvageYardNo.setName("stcLblSalvageYardNo");
				ivjstcLblSalvageYardNo.setBounds(new Rectangle(69, 133, 95, 16));
				ivjstcLblSalvageYardNo.setText(SALVAGE_YARD_NO);
				ivjstcLblSalvageYardNo.setMaximumSize(
					new java.awt.Dimension(95, 14));
				ivjstcLblSalvageYardNo.setMinimumSize(
					new java.awt.Dimension(95, 14));
				ivjstcLblSalvageYardNo.setHorizontalAlignment(
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
		return ivjstcLblSalvageYardNo;
	}

	/**
	 * Return the ivjstcLblStateCountry property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblStateCountry()
	{
		if (ivjstcLblStateCountry == null)
		{
			try
			{
				ivjstcLblStateCountry = new JLabel();
				ivjstcLblStateCountry.setName("stcLblStateCountry");
				ivjstcLblStateCountry.setText(STATE_COUNTRY);
				ivjstcLblStateCountry.setMaximumSize(
					new java.awt.Dimension(80, 14));
				ivjstcLblStateCountry.setMinimumSize(
					new java.awt.Dimension(80, 14));
				ivjstcLblStateCountry.setBounds(66, 159, 97, 14);
				ivjstcLblStateCountry.setHorizontalAlignment(
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
		return ivjstcLblStateCountry;
	}

	/**
	 * Return the ivjstcLblVehicleJunked property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVehicleJunked()
	{
		if (ivjstcLblVehicleJunked == null)
		{
			try
			{
				ivjstcLblVehicleJunked = new JLabel();
				ivjstcLblVehicleJunked.setName("stcLblVehicleJunked");
				ivjstcLblVehicleJunked.setText(VEHICLE_JUNK_DATE);
				ivjstcLblVehicleJunked.setBounds(5, 21, 177, 14);
				ivjstcLblVehicleJunked.setHorizontalAlignment(
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
		return ivjstcLblVehicleJunked;
	}

	/**
	 * Return the ivjtxtOSTitle property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOSTitle()
	{
		if (ivjtxtOSTitle == null)
		{
			try
			{
				ivjtxtOSTitle = new RTSInputField();
				ivjtxtOSTitle.setName("txtOSTitle");
				
				// defect 11004 
				//ivjtxtOSTitle.setInput(RTSInputField.DEFAULT);
				ivjtxtOSTitle.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 11004
				
				ivjtxtOSTitle.setBounds(184, 184, 110, 20);
				ivjtxtOSTitle.setMaxLength(OSTITLE_MAX_LEN);
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
		return ivjtxtOSTitle;
	}

	/**
	 * Return the ivjtxtSalvage property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtSalvage()
	{
		if (ivjtxtSalvage == null)
		{
			try
			{
				ivjtxtSalvage = new RTSInputField();
				ivjtxtSalvage.setName("txtSalvage");
				
				// defect 11004 
				// Not specified earlier; defaulted to RTSInputField.DEFAULT
				ivjtxtSalvage.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 11004 
				ivjtxtSalvage.setBounds(new Rectangle(184, 130, 111, 20));
				
				ivjtxtSalvage.setMaxLength(SALVAGE_MAX_LEN);
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
		return ivjtxtSalvage;
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
				ivjtxtState.setName("txtState");
				
				// defect 11004
				//ivjtxtState.setInput(RTSInputField.DEFAULT);
				//ivjtxtState.setMaxLength(STATE_CNTRY_MAX_LEN);
				ivjtxtState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtState.setMaxLength(TitleConstant.STATE_CNTRY_MAX_LEN);
				// end defect 11004 
				
				ivjtxtState.setBounds(184, 156, 38, 20);
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
		return ivjtxtState;
	}
	
	/**
	 * Called whenever the part throws an exception.
	 * @param exception Throwable
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
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(ScreenConstant.TTL028_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(354, 428);
			setModal(true);
			setTitle(ScreenConstant.TTL028_FRAME_TITLE);
			setContentPane(getFrmVehicleJunkedTTL028ContentPane1());
		}
		catch (Throwable aeException)
		{
			handleException(aeException);
		}
		// user code begin {2}
		
		// user code end
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
		if (aaIE.getSource() == getcomboReason())
		{
			int liJnkRsnIndx = getcomboReason().getSelectedIndex();
			int liJunkCode = 0;	

			if (liJnkRsnIndx >= 0)
			{
				IndicatorDescriptionsData laIDD =
					(IndicatorDescriptionsData) cvJnkCds.get(
						liJnkRsnIndx);
				liJunkCode =	
					Integer.parseInt(laIDD.getIndiFieldValue());
			}

			if (liJunkCode == 4)
			{
				getstcLblSalvageYardNo().setEnabled(true);
				gettxtSalvage().setEnabled(true);
			}
			else
			{
				gettxtSalvage().setText("");
				getstcLblSalvageYardNo().setEnabled(false);
				gettxtSalvage().setEnabled(false);
			}
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
			FrmVehicleJunkedTTL028 laFrmVehicleJunkedTTL028;
			laFrmVehicleJunkedTTL028 = new FrmVehicleJunkedTTL028();
			laFrmVehicleJunkedTTL028.setModal(true);
			laFrmVehicleJunkedTTL028
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmVehicleJunkedTTL028.show();
			java.awt.Insets laInsets =
				laFrmVehicleJunkedTTL028.getInsets();
			laFrmVehicleJunkedTTL028.setSize(
				laFrmVehicleJunkedTTL028.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmVehicleJunkedTTL028.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmVehicleJunkedTTL028.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Populates the vehicle makes
	 */
	private void populateJunkReasons()
	{
		try
		{
			if (getcomboReason().isEnabled())
			{

				Vector lvVct =
					IndicatorDescriptionsCache.getIndiDescs(
						IndicatorDescriptionsCache.JNKCD);

				if (lvVct != null)
				{
					for (int liIndex = lvVct.size() - 1;
						liIndex >= 0;
						liIndex--)
					{
						IndicatorDescriptionsData laData =
							(IndicatorDescriptionsData) lvVct.get(
								liIndex);
						if (laData.getJnkCdSysIndi() == 0)
						{
							String lsDesc = laData.getIndiDesc();
							cvJnkCds.addElement(laData);
							getcomboReason().addItem(lsDesc);
						}
					}
					getcomboReason().setSelectedIndex(-1);
				}
				// defect 8479
				comboBoxHotKeyFix(getcomboReason());
				// end defect 8479
			}
		}
		catch (RTSException aeRTSEx)
		{
			RTSException leExc =
				new RTSException(
					CommonConstant.STR_SPACE_EMPTY,
					aeRTSEx);
			leExc.displayError(this);
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
			if (aaDataObject != null)
			{
				caVehInqData = (VehicleInquiryData) aaDataObject;

				gettxtState().setText(CommonConstant.STR_TX);

				getdateVehicleJunked().setDate(
					RTSDate.getCurrentDate());

				populateJunkReasons();

				gettxtSalvage().setText("");
				// defect 11097 
				getstcLblSalvageYardNo().setEnabled(false);
				gettxtSalvage().setEnabled(false);
				// end defect 11097 
			}
		}

		catch (NullPointerException aeNPE)
		{
			RTSException leRTSEx =
				new RTSException(CommonConstant.STR_SPACE_EMPTY, aeNPE);
			leRTSEx.displayError(this);
		}
	}

	/**
	 * Set data to Vehicle Inquire Object.
	 */
	private void setDataToVehObj()
	{
		// defect 10428 
		// VctSalvage() is always populated w/ at least one  element. 
		// 
		// Transaction population always pulls from the last element
		// of the vector.  

		//		SalvageData laSlvgData =
		//					(SalvageData) caVehInqData
		//						.getMfVehicleData()
		//						.getVctSalvage()
		//						.get(
		//						0);
		//

		Vector lvJunk = caVehInqData.getMfVehicleData().getVctSalvage();

		SalvageData laSlvgData =
			(SalvageData) lvJunk.elementAt(lvJunk.size() - 1);
		// end defect 10428 

		// EVERY field in the SalvageData will be reset via the following, 
		//  i.e. no chance of 'left over' data from earlier entry.   
		if (laSlvgData != null)
		{
			// defect 11004 
			// Trim is not required w/ new input definitions 
			RTSDate laVehJnkDt = getdateVehicleJunked().getDate();
			String lsSlvgYrdNo = gettxtSalvage().getText();
			String lsOthStCntry = gettxtState().getText();
			String lsOSTtlNo = gettxtOSTitle().getText();
			// end defect 11004 

			int liJnkRsnIndx = getcomboReason().getSelectedIndex();

			if (laVehJnkDt != null)
			{
				laSlvgData.setSlvgDt(laVehJnkDt);
			}

			if (lsSlvgYrdNo != null)
			{
				laSlvgData.setSalvYardNo(lsSlvgYrdNo);
			}

			if (liJnkRsnIndx >= 0)
			{
				IndicatorDescriptionsData laIDD =
					(IndicatorDescriptionsData) cvJnkCds.get(
						liJnkRsnIndx);
				laSlvgData.setSlvgCd(
					Integer.parseInt(laIDD.getIndiFieldValue()));
			}

			// defect 11097 
			if (laSlvgData.getSlvgCd() == 4 ||
					laSlvgData.getSlvgCd() == 5)
			{
				laSlvgData.setSalvIndi(1);
			}
			else
			{
				laSlvgData.setSalvIndi(0);
			}
			// end defect 11097 

			if (lsOthStCntry != null)
			{
				laSlvgData.setOthrStateCntry(lsOthStCntry);
			}

			if (lsOSTtlNo != null)
			{
				laSlvgData.setOthrGovtTtlNo(lsOSTtlNo);
			}

			if (getchkFirst().isSelected())
			{
				laSlvgData.setLienNotRlsedIndi(1);
			}
			else
			{
				laSlvgData.setLienNotRlsedIndi(0);
			}

			if (getchkSecond().isSelected())
			{
				laSlvgData.setLienNotRlsedIndi2(1);
			}
			else
			{
				laSlvgData.setLienNotRlsedIndi2(0);
			}

			if (getchkThird().isSelected())
			{
				laSlvgData.setLienNotRlsedIndi3(1);
			}
			else
			{
				laSlvgData.setLienNotRlsedIndi3(0);
			}
		}
	}
	/**
	 * Validate Data on Screen
	 * 
	 * @returns boolean
	 */
	private boolean validateData() 
	{
		boolean lbValid = true;
		
		RTSException leRTSEx = new RTSException();

		// Validate the vehicle junked date
		if (!getdateVehicleJunked().isValidDate())
		{
			leRTSEx.addException(
				new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getdateVehicleJunked());
		}

		// Verify that a Junk Reason was selected
		if (getcomboReason().getSelectedItem() == null)
		{
			leRTSEx.addException(
				new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getcomboReason());
		}
		
		CommonValidations.addRTSExceptionForInvalidCntryStateCntry(gettxtState(), 
					leRTSEx,TitleConstant.NOT_REQUIRED);
		
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid; 
	}
}
