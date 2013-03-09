package com.txdot.isd.rts.client.title.ui;

import java.awt.Dialog;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.CertifiedLienholderCache;
import com.txdot.isd.rts.services.cache.LienholdersCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * 
 * FrmSalvageLienEntryTTL001.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validation updated
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			05/15/2002	Added validations for fields CQU100003936
 * MAbs			05/16/2002	Added capturing of mailing information 
 * 							CQU100003963
 * MAbs			05/28/2002	Added mail object in VehInqData to store 
 * 							mail info CQU100004122
 * MAbs			05/29/2002	Cleared lienholder info for Delete lien 
 * 							checkbox CQU100004146
 * J Zwiener	09/30/2005	Disable fields when CCONRT
 * 							modify setData()
 * 							defect 7762 Ver 5.2.3
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbiage to class level.
 * 							Replace magic nums with meaningful verbiage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	03/07/2006	Remove color from labels
 * 							modify getstcLblAddress(), 
 * 							getstcLblDate(),getstcLblName()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	04/30/2006	Added jpanel to tab flow. Alignment work 
 * 							on fields; Field length adjustment.
 * 							defect 7898 Ver 5.2.3  
 * K Harrell	06/08/2008 	Correct handling of USA/Non-USA Screen, 
 * 							  presentation and saving of data
 * 							add ivjLienholderCntry, ivjLienholderCntryZip,
 * 							  get/set methods, validateFields(), 
 * 							  itemStateChanged()
 * 							deleted ivjCountry, ivjCountryZip, get/set
 * 							 methods 
 * 							modify actionPerformed(), initialize(), 
 * 							  getchkDelete1stLien(), getchkUSA()
 * 							  setData(), setDataToDataObject(),
 * 							  gettxtLienholderCntryZip()   
 * 							defect 9646 Ver Defect POS A
 * K Harrell	06/24/2008	Non-USA Lienholder Cntry & Zip are cleared
 * 							when exist on record.  Enlarge Cntry field
 * 							slightly via Visual Editor. Clear Country
 * 							and CountryZip with Delete Lien checkbox  
 * 							add cbSetDataFinished
 * 							modify setData(), itemStateChanged()
 * 							defect 9726 Ver Defect_POS_A 
 * K Harrell	03/07/2009	Use Lienholder AddressData 
 * 							modify setDataToDataObject(), 
 * 							 setupLienhldrDataToDisplay()
 * 							defect 9969 Ver Defect_POS_E 
 * K Harrell	03/07/2009	Incorporating selection of Local and 
 * 							Certified Lienholders.
 * 							add FocusListener 
 * 							add LOCAL, ID, CERTIFIED
 * 							add ivjradioCertified, ivjradioLocal,
 * 							  ivjcomboLienholder, ivjbtnDeleteLien,
 * 							  ivjJPanel2,ivjstclblId, ivjtxtLienholderId,
 * 							  plus get methods.  
 * 							add caTitleData, caLienholderData,
 * 							  svCurrentCertLienhldr, svLatestCertLienhldr,
 * 							  cvComboLienhlderDatacbCCO, caButtonGroup, 
 * 							  focusLost(), focusGained(), 
 * 							  isLienHolderDataEntered(), 
 * 							  setupLienhldrDataToDisplay(),
 * 							  buildLatestCertifiedLienVector(), 
 * 							  buildCertifiedLienVector(),
 * 							  resetScreen(), 
 * 							  populateCertifiedCombo(),
 * 							  validateFields()
 * 							delete ivjchkDelete1stLien,
 * 							  getchkDelete1stLien()
 * 							modify actionPerformed(), itemStateChanged(),
 * 							 getFrmSalvageLienEntryTTL001ContentPane1(),
 * 							 setData(), setDataToDataObject()
 * 							 defect 9975 Ver Defect_POS_E
 * K Harrell	03/27/2009	Reset caLienhldrData to new LienholdersData()
 * 							if not found on local lookup.
 * 							modify focusLost()
 * 							defect 9975 Ver Defect_POS_E
 * K Harrell	07/03/2009	Implement new LienholderData  
 * 							modify setData() , setDataToDataObject(), 
 * 							 setupLienhldrDataToDisplay()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/19/2009	Implement new NameAddressComponent. 
 * 							refactor RTSInputFields, get methods to 
 * 							 standards 
 * 							add caNameAddrComp
 * 							delete setNonUSAAddressDisplay(), 
 * 							 setUSAAddressDisplay()  
 * 							modify initialize(), itemStateChanged(), 
 * 							  refreshScreen(), setData(),
 * 							  setupLienhldrDataToDisplay(),  
 * 							  setDataToDataObject(), validateFields() 
 * 							defect 10127 Ver Defect_POS_F 
 * T Pederson	06/17/2010 	Added check to not allow a lien date  
 * 							greater than the current date.  
 * 							modify validateFields()  
 *							defect 10504  Ver POS_650
 * K Harrell	10/06/2010	Modify to handle Delete Button per 6.6.0 
 * 							requirements
 * 							add keyReleased(), setupDeleteButton()  
 * 							modify setData(), itemStateChanged(), 
 * 							 actionPerformed() 
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	01/12/2011	Validate Lien Date against Certified 
 * 							Lienholder Data
 * 							modify validateFields() 
 * 							defect 10631 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to capture Lien Info for Salvage
 *  
 * @version	6.7.0			01/12/2011
 * @author	Michael Abernethy
 * <br>Creation Date:		06/20/2001 22:03:11
 */

public class FrmSalvageLienEntryTTL001
	extends RTSDialogBox
	implements ActionListener, ItemListener, FocusListener
{
	private RTSButton ivjbtnDeleteLien = null;
	private ButtonPanel ivjbtnPanel = null;
	private JCheckBox ivjchkUSA = null;
	private JComboBox ivjcomboLienholder = null;
	private JPanel ivjFrmSalvageLienEntryTTL001ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JRadioButton ivjradioCertified = null;
	private JRadioButton ivjradioLocal = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblAddress = null;
	private JLabel ivjstcLblDate = null;
	private JLabel ivjstclblId = null;
	private JLabel ivjstcLblName = null;
	private RTSDateField ivjtxtLienDate = null;
	private RTSInputField ivjtxtLienholderCity = null;
	private RTSInputField ivjtxtLienholderCntry = null;
	private RTSInputField ivjtxtLienholderCntryZpcd = null;
	private RTSInputField ivjtxtLienholderId = null;
	private RTSInputField ivjtxtLienholderName1 = null;
	private RTSInputField ivjtxtLienholderName2 = null;
	private RTSInputField ivjtxtLienholderState = null;
	private RTSInputField ivjtxtLienholderStreet1 = null;
	private RTSInputField ivjtxtLienholderStreet2 = null;
	private RTSInputField ivjtxtLienholderZpcd = null;
	private RTSInputField ivjtxtLienholderZpcdP4 = null;
	private RTSButtonGroup caButtonGroup1 = new RTSButtonGroup();

	// boolean 
	private boolean cbCCO = false;
	private boolean cbSetDataFinished = false;

	// int 
	private int ciSavedLienholderId = 0;

	// Vector 
	private Vector cvComboLienhlderData = new Vector();

	// Object 	
	// defect 10127 
	private NameAddressComponent caNameAddrComp = null;
	// end defect 10127 

	private LienholderData caLienhldrData = null;
	private TitleData caTitleData = null;
	private VehicleInquiryData caVehInqData = null;

	// Constant String
	private final static String ADDRESS = "Address";
	private final static String CERTIFIED = "Certified";
	private final static String DATE = "Date:";
	private final static String DEL_LIEN = "Delete Lien";
	private final static String EMPTY = CommonConstant.STR_SPACE_EMPTY;
	private final static String ID = "Id:";
	private final static String LOCAL = "Local";
	private final static String NAME = "Name";
	private final static String USA = "USA";

	// Static Vector 
	private static Vector svCurrentCertLienhldr = new Vector();
	private static Vector svLatestCertLienhldr = new Vector();

	static {
		buildLatestCertifiedLienVector();
	}

	static {
		buildCertifiedLienVector();
	}

	/**
	 * buildCertifiedLienVector from Cache 
	 */

	private static void buildCertifiedLienVector()
	{
		svCurrentCertLienhldr =
			CertifiedLienholderCache.getCurrentCertfdLienhldrs(false);
	}

	/**
	 * build Latest CertifiedLienVector from Cache 
	 */

	private static void buildLatestCertifiedLienVector()
	{
		svLatestCertLienhldr =
			CertifiedLienholderCache.getLatestCertfdLienhldrs();
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
			FrmSalvageLienEntryTTL001 laFrmSalvageLienEntryTTL001;
			laFrmSalvageLienEntryTTL001 =
				new FrmSalvageLienEntryTTL001();
			laFrmSalvageLienEntryTTL001.setModal(true);
			laFrmSalvageLienEntryTTL001
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laRTSDBox)
				{
					System.exit(0);
				};
			});
			laFrmSalvageLienEntryTTL001.show();
			java.awt.Insets insets =
				laFrmSalvageLienEntryTTL001.getInsets();
			laFrmSalvageLienEntryTTL001.setSize(
				laFrmSalvageLienEntryTTL001.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSalvageLienEntryTTL001.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSalvageLienEntryTTL001.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSalvageLienEntryTTL001 constructor comment.
	 */
	public FrmSalvageLienEntryTTL001()
	{
		super();
		initialize();
	}

	/**
	 * FrmSalvageLienEntryTTL001 constructor.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmSalvageLienEntryTTL001(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSalvageLienEntryTTL001 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSalvageLienEntryTTL001(JFrame aaParent)
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

			if (aaAE.getSource() == getbtnDeleteLien())
			{
				resetScreen();

				// defect 10592 
				getbtnDeleteLien().setEnabled(false);
				// end defect 10592  
			}
			else if (aaAE.getSource() == getbtnPanel().getBtnEnter())
			{
				// defect 9646 
				if (validateFields())
				{
					setDataToDataObject();

					CompleteTitleTransaction laTtlTransData =
						new CompleteTitleTransaction(
							caVehInqData,
							getController().getTransCode());

					CompleteTransactionData laCompTransData = null;

					try
					{
						laCompTransData =
							laTtlTransData.doCompleteTransaction();

						laCompTransData.setMailingAddrData(
							caVehInqData.getMailingAddress());
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(this);
						return;
					}
					getController().processData(
						AbstractViewController.ENTER,
						laCompTransData);
				}
				// end defect 9646 
			}
			// defect 9975 
			else if (
				aaAE.getSource() == getcomboLienholder()
					&& getradioCertified().isSelected())
			{
				int aiSelection =
					getcomboLienholder().getSelectedIndex();

				if (aiSelection >= 0)
				{
					CertifiedLienholderData laData =
						(
							CertifiedLienholderData) svCurrentCertLienhldr
								.elementAt(
							aiSelection);

					// defect 10112 
					caLienhldrData = laData.getLienholderData();
					// end defect 10112 
					setupLienhldrDataToDisplay(false);

					// defect 10592 
					setupDeleteButton();
					// end defect 10592  
				}
			}
			// end defect 9975 
			else if (aaAE.getSource() == getbtnPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caVehInqData);

			}
			else if (aaAE.getSource() == getbtnPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL001);
			}

		}
		finally
		{
			doneWorking();
		}
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
			&& aeFE.getSource() == gettxtLienholderId()
			&& !aeFE.getOppositeComponent().equals(null)
			&& !aeFE.getOppositeComponent().equals(
				getbtnPanel().getBtnCancel()))
		{
			cbSetDataFinished = false;
			clearAllColor(this);
			String lsLienHolderId =
				UtilityMethods.removeLeadingZeros(
					gettxtLienholderId().getText());
			gettxtLienholderId().setText(lsLienHolderId);
			if (lsLienHolderId.length() > 0)
			{
				int liLienholderId = Integer.parseInt(lsLienHolderId);

				if (liLienholderId != ciSavedLienholderId)
				{
					ciSavedLienholderId = liLienholderId;

					try
					{
						caLienhldrData =
							LienholdersCache.getLienhldr(
								SystemProperty.getOfficeIssuanceNo(),
								SystemProperty.getSubStationId(),
								liLienholderId);

						if (caLienhldrData != null)
						{
							setupLienhldrDataToDisplay(true);
						}
						else
						{
							caLienhldrData = new LienholderData();
						}
					}
					catch (RTSException aeRTSEx)
					{

					}
				}
			}
			cbSetDataFinished = true;
		}
	}

	/**
	 * This method initializes ivjbtnDeleteLien
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDeleteLien()
	{
		if (ivjbtnDeleteLien == null)
		{
			ivjbtnDeleteLien = new RTSButton();
			ivjbtnDeleteLien.setBounds(26, 170, 97, 26);
			// user code begin {1}
			ivjbtnDeleteLien.setText(DEL_LIEN);
			ivjbtnDeleteLien.setMnemonic(java.awt.event.KeyEvent.VK_D);
			ivjbtnDeleteLien.addActionListener(this);
			// user code end 
		}
		return ivjbtnDeleteLien;
	}

	/**
	 * Return the ivjbtnPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbtnPanel()
	{
		if (ivjbtnPanel == null)
		{
			try
			{
				ivjbtnPanel = new ButtonPanel();
				ivjbtnPanel.setBounds(153, 212, 258, 38);
				ivjbtnPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjbtnPanel.addActionListener(this);
				getbtnPanel().setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnPanel;
	}

	/**
	 * Return the chkUSA property value.
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
				ivjchkUSA.setBounds(464, 86, 56, 22);
				ivjchkUSA.setMaximumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA.setActionCommand(USA);
				ivjchkUSA.setSelected(true);
				ivjchkUSA.setMinimumSize(
					new java.awt.Dimension(49, 22));
				// user code begin {1}
				ivjchkUSA.setText(USA);
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
	 * This method initializes ivjcomboLienholder
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboLienholder()
	{
		if (ivjcomboLienholder == null)
		{
			ivjcomboLienholder = new JComboBox();
			ivjcomboLienholder.setSize(328, 25);
			ivjcomboLienholder.setLocation(5, 14);
		}
		return ivjcomboLienholder;
	}

	/**
	 * Return the FrmSalvageLienEntryTTL001ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSalvageLienEntryTTL001ContentPane1()
	{
		if (ivjFrmSalvageLienEntryTTL001ContentPane1 == null)
		{
			try
			{
				ivjFrmSalvageLienEntryTTL001ContentPane1 = new JPanel();
				ivjFrmSalvageLienEntryTTL001ContentPane1.setName(
					"FrmSalvageLienEntryTTL001ContentPane1");
				ivjFrmSalvageLienEntryTTL001ContentPane1.setLayout(
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSalvageLienEntryTTL001ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));

				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getstcLblName(),
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					gettxtLienholderStreet1(),
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					gettxtLienholderStreet2(),
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getbtnPanel(),
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getchkUSA(),
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getstcLblAddress(),
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getJPanel3(),
					null);
				// defect 9975
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getcomboLienholder(),
					null);
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getbtnDeleteLien(),
					null);
				// end defect 9975 
				ivjFrmSalvageLienEntryTTL001ContentPane1.add(
					getJPanel2(),
					null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSalvageLienEntryTTL001ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setLayout(null);
				getJPanel1().add(
					gettxtLienholderCity(),
					gettxtLienholderCity().getName());
				getJPanel1().add(
					gettxtLienholderState(),
					gettxtLienholderState().getName());
				getJPanel1().add(
					gettxtLienholderZpcd(),
					gettxtLienholderZpcd().getName());
				getJPanel1().add(
					gettxtLienholderZpcdP4(),
					gettxtLienholderZpcdP4().getName());
				getJPanel1().add(
					getstcLblDash(),
					getstcLblDash().getName());
				getJPanel1().add(
					gettxtLienholderCntry(),
					gettxtLienholderCntry().getName());
				getJPanel1().add(
					gettxtLienholderCntryZpcd(),
					gettxtLienholderCntryZpcd().getName());
				// user code begin {1}
				ivjJPanel1.setSize(254, 31);
				ivjJPanel1.setLocation(276, 157);
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
	 * This method initializes ivjJPanel2
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			ivjJPanel2 = new JPanel();
			ivjJPanel2.add(getradioLocal(), null);
			ivjJPanel2.add(getradioCertified(), null);
			ivjJPanel2.setBounds(346, 10, 186, 33);
			caButtonGroup1 = new RTSButtonGroup();
			caButtonGroup1.add(getradioLocal());
			caButtonGroup1.add(getradioCertified());
		}
		return ivjJPanel2;
	}

	/**
	 * This method initializes ivjJPanel3
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			ivjJPanel3 = new JPanel();
			ivjJPanel3.setLayout(null);
			ivjJPanel3.add(getstcLblDate(), null);
			ivjJPanel3.add(gettxtLienDate(), null);
			ivjJPanel3.add(gettxtLienholderName1(), null);
			ivjJPanel3.add(gettxtLienholderName2(), null);
			ivjJPanel3.add(gettxtLienholderId(), null);
			ivjJPanel3.add(getstcLblId(), null);
			ivjJPanel3.setBounds(5, 81, 270, 88);
		}
		return ivjJPanel3;
	}

	/**
	 * This method initializes ivjradioCertified
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCertified()
	{
		if (ivjradioCertified == null)
		{
			ivjradioCertified = new JRadioButton();
			ivjradioCertified.setText(CERTIFIED);
			ivjradioCertified.setMnemonic(java.awt.event.KeyEvent.VK_C);
		}
		return ivjradioCertified;
	}

	/**
	 * This method initializes ivjradioLocal
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioLocal()
	{
		if (ivjradioLocal == null)
		{
			ivjradioLocal = new JRadioButton();
			// user code begin {1}
			ivjradioLocal.setText(LOCAL);
			ivjradioLocal.setMnemonic(java.awt.event.KeyEvent.VK_L);
			// user code end 
		}
		return ivjradioLocal;
	}

	/**
	 * Return the ivjstcLblDash property value.
	 * 
	 * @return JLabel
	 */
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
				ivjstcLblDash.setBounds(207, 7, 10, 20);
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
	 * Return the stcLblAddress property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblAddress()
	{
		if (ivjstcLblAddress == null)
		{
			try
			{
				ivjstcLblAddress = new JLabel();
				ivjstcLblAddress.setSize(75, 20);
				ivjstcLblAddress.setText(ADDRESS);
				ivjstcLblAddress.setMaximumSize(
					new java.awt.Dimension(48, 14));
				ivjstcLblAddress.setMinimumSize(
					new java.awt.Dimension(48, 14));
				ivjstcLblAddress.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				ivjstcLblAddress.setLocation(321, 59);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblAddress;
	}

	/**
	 * Return the stcLblDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDate()
	{
		if (ivjstcLblDate == null)
		{
			try
			{
				ivjstcLblDate = new JLabel();
				ivjstcLblDate.setSize(32, 20);
				ivjstcLblDate.setText(DATE);
				ivjstcLblDate.setMaximumSize(
					new java.awt.Dimension(29, 14));
				ivjstcLblDate.setMinimumSize(
					new java.awt.Dimension(29, 14));
				// user code begin {1}
				ivjstcLblDate.setLocation(146, 9);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDate;
	}

	/**
	 * This method initializes ivjstclblEnterId
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblId()
	{
		if (ivjstclblId == null)
		{
			ivjstclblId = new JLabel();
			ivjstclblId.setSize(15, 21);
			ivjstclblId.setLocation(21, 8);
			ivjstclblId.setText(ID);
			// user code begin {1}
			ivjstclblId.setDisplayedMnemonic(KeyEvent.VK_I);
			ivjstclblId.setLabelFor(gettxtLienholderId());
			// user code end 

		}
		return ivjstclblId;
	}

	/**
	 * Return the stcLblName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblName()
	{
		if (ivjstcLblName == null)
		{
			try
			{
				ivjstcLblName = new JLabel();
				ivjstcLblName.setSize(85, 20);
				ivjstcLblName.setText(NAME);
				ivjstcLblName.setMaximumSize(
					new java.awt.Dimension(33, 14));
				ivjstcLblName.setMinimumSize(
					new java.awt.Dimension(33, 14));
				ivjstcLblName.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				ivjstcLblName.setLocation(73, 59);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblName;
	}
	/**
	 * Return the ivjtxtLienDate property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSDateField gettxtLienDate()
	{
		if (ivjtxtLienDate == null)
		{
			try
			{
				ivjtxtLienDate = new RTSDateField();
				ivjtxtLienDate.setBounds(191, 9, 73, 20);
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
		return ivjtxtLienDate;
	}

	/**
	 * Return the ivjtxtLienholderCity property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholderCity()
	{
		if (ivjtxtLienholderCity == null)
		{
			try
			{
				ivjtxtLienholderCity = new RTSInputField();
				ivjtxtLienholderCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholderCity.setBounds(4, 7, 120, 20);
				// user code begin {1}				
				ivjtxtLienholderCity.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtLienholderCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLienholderCity;
	}

	/**
	 * Return the ivjtxtLienholderCntry property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholderCntry()
	{
		if (ivjtxtLienholderCntry == null)
		{
			try
			{
				ivjtxtLienholderCntry = new RTSInputField();
				ivjtxtLienholderCntry.setSize(45, 20);
				ivjtxtLienholderCntry.setLocation(129, 7);
				// user code begin {1}								
				ivjtxtLienholderCntry.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtLienholderCntry.setMaxLength(
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
		return ivjtxtLienholderCntry;
	}

	/**
	 * Return the ivjtxtLienholderCntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholderCntryZpcd()
	{
		if (ivjtxtLienholderCntryZpcd == null)
		{
			try
			{
				ivjtxtLienholderCntryZpcd = new RTSInputField();
				ivjtxtLienholderCntryZpcd.setBounds(177, 7, 71, 20);
				// user code begin {1}				
				ivjtxtLienholderCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtLienholderCntryZpcd.setMaxLength(
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
		return ivjtxtLienholderCntryZpcd;
	}

	/**
	 * This method initializes ivjtxtLienholderId
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholderId()
	{
		if (ivjtxtLienholderId == null)
		{
			ivjtxtLienholderId = new RTSInputField();
			ivjtxtLienholderId.setSize(31, 20);
			ivjtxtLienholderId.setLocation(47, 9);
			// user code begin {1}
			ivjtxtLienholderId.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtLienholderId.setMaxLength(
				LocalOptionConstant.LENGTH_LIENHOLDER_ID);
			ivjtxtLienholderId.addFocusListener(this);
			// user code end 
		}
		return ivjtxtLienholderId;
	}
	/**
	 * Return the ivjtxtLienholderName1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholderName1()
	{
		if (ivjtxtLienholderName1 == null)
		{
			try
			{
				ivjtxtLienholderName1 = new RTSInputField();
				ivjtxtLienholderName1.setBounds(21, 34, 243, 20);
				ivjtxtLienholderName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1} 
				ivjtxtLienholderName1.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholderName1.setMaxLength(
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
		return ivjtxtLienholderName1;
	}

	/**
	 * Return the ivjtxtLienholderName2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholderName2()
	{
		if (ivjtxtLienholderName2 == null)
		{
			try
			{
				ivjtxtLienholderName2 = new RTSInputField();
				ivjtxtLienholderName2.setBounds(21, 59, 243, 20);
				ivjtxtLienholderName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}						
				ivjtxtLienholderName2.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholderName2.setMaxLength(
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
		return ivjtxtLienholderName2;
	}

	/**
	 * Return the ivjtxtLienholderState property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholderState()
	{
		if (ivjtxtLienholderState == null)
		{
			try
			{
				ivjtxtLienholderState = new RTSInputField();
				ivjtxtLienholderState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholderState.setBounds(130, 7, 25, 20);
				// user code begin {1}				
				ivjtxtLienholderState.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtLienholderState.setMaxLength(
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
		return ivjtxtLienholderState;
	}

	/**
	 * Return the ivjtxtLienholderStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholderStreet1()
	{
		if (ivjtxtLienholderStreet1 == null)
		{
			try
			{
				ivjtxtLienholderStreet1 = new RTSInputField();
				ivjtxtLienholderStreet1.setBounds(280, 115, 243, 20);
				ivjtxtLienholderStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtLienholderStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholderStreet1.setMaxLength(
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
		return ivjtxtLienholderStreet1;
	}

	/**
	 * Return the ivjtxtLienholderStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholderStreet2()
	{
		if (ivjtxtLienholderStreet2 == null)
		{
			try
			{
				ivjtxtLienholderStreet2 = new RTSInputField();
				ivjtxtLienholderStreet2.setBounds(280, 140, 243, 20);
				ivjtxtLienholderStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}						
				ivjtxtLienholderStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholderStreet2.setMaxLength(
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
		return ivjtxtLienholderStreet2;
	}

	/**
	 * Return the ivjtxtLienholderZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholderZpcd()
	{
		if (ivjtxtLienholderZpcd == null)
		{
			try
			{
				ivjtxtLienholderZpcd = new RTSInputField();
				ivjtxtLienholderZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholderZpcd.setBounds(158, 7, 45, 20);
				// user code begin {1}				
				ivjtxtLienholderZpcd.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholderZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLienholderZpcd;
	}

	/**
	 * Return the ivjtxtLienholderZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholderZpcdP4()
	{
		if (ivjtxtLienholderZpcdP4 == null)
		{
			try
			{
				ivjtxtLienholderZpcdP4 = new RTSInputField();
				ivjtxtLienholderZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholderZpcdP4.setBounds(213, 7, 35, 20);
				// user code begin {1}				
				ivjtxtLienholderZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholderZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLienholderZpcdP4;
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
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			setName(ScreenConstant.TTL001_FRAME_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(553, 291);
			setTitle(ScreenConstant.TTL001_FRAME_TITLE);
			setContentPane(getFrmSalvageLienEntryTTL001ContentPane1());
			// user code begin {1}
			// defect 9646 
			setRequestFocus(false);
			// end defect 9646 

			// defect 10127 
			caNameAddrComp =
				new NameAddressComponent(
					gettxtLienholderName1(),
					gettxtLienholderName2(),
					gettxtLienholderStreet1(),
					gettxtLienholderStreet2(),
					gettxtLienholderCity(),
					gettxtLienholderState(),
					gettxtLienholderZpcd(),
					gettxtLienholderZpcdP4(),
					gettxtLienholderCntry(),
					gettxtLienholderCntryZpcd(),
					getchkUSA(),
					getstcLblDash(),
					ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA,
					ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA,
					CommonConstant.TX_NOT_DEFAULT_STATE);
			// end defect 10127 
			// user code end
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	* ItemListener method.
	* 
	* @param aaIE ItemEvent
	*/
	public void itemStateChanged(ItemEvent aaIE)
	{
		clearAllColor(this);

		if (aaIE.getSource() == getchkUSA())
		{
			// defect 10127 
			caNameAddrComp.resetPerUSA(
				aaIE.getStateChange() == ItemEvent.SELECTED);
			// end defect 10127
		}
		// defect 9975 
		else if (aaIE.getSource() instanceof JRadioButton)
		{
			caLienhldrData = new LienholderData();
			getcomboLienholder().removeActionListener(this);

			if (aaIE.getSource() == getradioLocal()
				&& getradioLocal().isSelected())
			{
				gettxtLienholderId().setEnabled(true);
				gettxtLienholderName1().requestFocus();
				getcomboLienholder().setSelectedIndex(-1);
				setupLienhldrDataToDisplay(true);
				gettxtLienholderId().requestFocus();
			}
			else if (
				aaIE.getSource() == getradioCertified()
					&& getradioCertified().isSelected())
			{
				getstcLblId().setEnabled(false);
				gettxtLienholderId().setText(EMPTY);
				setupLienhldrDataToDisplay(false);
				getcomboLienholder().requestFocus();
			}
			getcomboLienholder().addActionListener(this);
			// defect 10592 
			setupDeleteButton();
			// end defect 10592
		}
		// end defect 9975 
	}
	/**
	 * Request focus on components
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyChar() == KeyEvent.VK_ESCAPE)
		{
			getController().processData(
				AbstractViewController.CANCEL,
				caVehInqData);

		}
		else
		{
			setupDeleteButton();
		}
	}

	/**
	 *  Populate Certified Combo 
	 */
	private void populateCertifiedCombo()
	{
		if (cbCCO)
		{
			cvComboLienhlderData =
				(Vector) UtilityMethods.copy(svLatestCertLienhldr);
		}
		else
		{
			cvComboLienhlderData =
				(Vector) UtilityMethods.copy(svCurrentCertLienhldr);
		}

		for (int i = 0; i < cvComboLienhlderData.size(); i++)
		{
			CertifiedLienholderData laData =
				(
					CertifiedLienholderData) cvComboLienhlderData
						.elementAt(
					i);
			getcomboLienholder().addItem(
				laData.getName1()
					+ "    "
					+ laData.getPermLienHldrId());
		}
		getcomboLienholder().setSelectedIndex(-1);
	}

	/**
	 * Reset Screen Fields 
	 */
	private void resetScreen()
	{
		gettxtLienDate().setDate(null);
		gettxtLienholderName1().setText(EMPTY);
		gettxtLienholderName2().setText(EMPTY);
		gettxtLienholderStreet1().setText(EMPTY);
		gettxtLienholderStreet2().setText(EMPTY);
		gettxtLienholderCity().setText(EMPTY);
		gettxtLienholderState().setText(EMPTY);
		gettxtLienholderZpcd().setText(EMPTY);
		gettxtLienholderZpcdP4().setText(EMPTY);
		gettxtLienholderCntry().setText(EMPTY);
		gettxtLienholderCntryZpcd().setText(EMPTY);
		// defect 9975 
		gettxtLienholderId().setText(EMPTY);
		getcomboLienholder().setSelectedIndex(-1);
		getradioLocal().setSelected(true);
		// end defect 9975 
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
		getcomboLienholder().setEnabled(false);

		if (aaDataObject != null)
		{
			caVehInqData = (VehicleInquiryData) aaDataObject;

			// defect 9975  
			// New Class Variables, Lienholder Lookup via Id, Combo 
			// Lookup to validate PermLienhldrid   
			caTitleData =
				caVehInqData.getMfVehicleData().getTitleData();

			// defect 10112 
			caLienhldrData =
				caTitleData.getLienholderData(TitleConstant.LIENHLDR1);

			int liDt1 = caLienhldrData.getLienDate();
			// end defect 10112 

			if (liDt1 > 0)
			{
				RTSDate laRTSDate =
					new RTSDate(RTSDate.YYYYMMDD, liDt1);
				gettxtLienDate().setDate(laRTSDate);
			}

			// defect 9646
			caLienhldrData =
				caLienhldrData == null
					? new LienholderData()
					: (LienholderData) UtilityMethods.copy(
						caLienhldrData);

			String lsPermLienhldrId = caTitleData.getPermLienHldrId1();

			cbCCO =
				getController().getTransCode().equals(
					TransCdConstant.CCOSCT)
					|| getController().getTransCode().equals(
						TransCdConstant.CCONRT);

			// Default to Local Lienholders 
			boolean lbLocal = true;

			// Populate based upon TransCd 
			populateCertifiedCombo();

			// Disregard if Invalid Lienholder Id 
			if (TitleClientUtilityMethods
				.isValidPermLienhldrId(lsPermLienhldrId))
			{
				lbLocal = false;
				int liSelected = -1;
				for (int i = 0; i < cvComboLienhlderData.size(); i++)
				{
					CertifiedLienholderData laData =
						(
							CertifiedLienholderData) cvComboLienhlderData
								.elementAt(
							i);

					if (lsPermLienhldrId
						.equals(laData.getPermLienHldrId()))
					{
						liSelected = i;
						caLienhldrData =
							(LienholderData) UtilityMethods.copy(
								laData.getLienholderData());
						break;
					}
				}

				if (liSelected >= 0)
				{
					getcomboLienholder().setSelectedIndex(liSelected);
				}
				else
				{
					caLienhldrData = new LienholderData();
					RTSException laRTSEx = new RTSException(805);
					laRTSEx.displayError(this);
				}
			}

			boolean lbEnable = !cbCCO && lbLocal;
			getradioLocal().setSelected(lbLocal);
			getradioCertified().setSelected(!lbLocal);
			setupLienhldrDataToDisplay(lbEnable);
			getradioLocal().addItemListener(this);
			getradioCertified().addItemListener(this);
			getcomboLienholder().addActionListener(this);
			if (lbLocal)
			{
				setDefaultFocusField(gettxtLienholderId());
			}
			// end defect 9975 			
		}

		// defect 10592 
		setupDeleteButton();
		// end defect 10592 

		// defect 9726 
		cbSetDataFinished = true;
		// end defect 9726 
	}

	/**
	 * Set data to Vehicle InquIry Object.
	 */
	private void setDataToDataObject()
	{
		// defect 10127 
		caNameAddrComp.setNameAddressToDataObject(caLienhldrData);

		int liDate = 0;

		if (gettxtLienDate().isValidDate())
		{
			liDate = gettxtLienDate().getDate().getYYYYMMDDDate();
		}
		caLienhldrData.setLienDate(liDate);
		// end defect 10127		

		// defect 10112 
		caTitleData.setLienholderData(
			TitleConstant.LIENHLDR1,
			caLienhldrData);
		// end defect 10112

		// defect 9975		
		// This data is also cleared upon processing at the MF   
		caTitleData.clearLien2Data();
		caTitleData.clearLien3Data();

		if (getradioCertified().isSelected()
			&& getcomboLienholder().getSelectedIndex() >= 0)
		{
			int liIndex = getcomboLienholder().getSelectedIndex();

			CertifiedLienholderData laCertLienData =
				(
					CertifiedLienholderData) cvComboLienhlderData
						.elementAt(
					liIndex);

			caTitleData.setPermLienHldrId1(
				laCertLienData.getPermLienHldrId());
		}
		else
		{
			caTitleData.setPermLienHldrId1(EMPTY);
		}
		// end defect 9975 
	}

	/**
	 * Enable/Disabled Delete Buttons per data
	 */
	private void setupDeleteButton()
	{
		getbtnDeleteLien().setEnabled(
			!caNameAddrComp.isNameAddressEmpty()
				|| !gettxtLienholderId().isEmpty()
				|| !gettxtLienDate().isDateEmpty());
	}

	/**
	 * Setup Lienholder Data To Display 
	 * 
	 * @param abVisible
	 */
	private void setupLienhldrDataToDisplay(boolean abEnable)
	{
		// defect 10127
		caNameAddrComp.setNameAddressDataToDisplay(caLienhldrData);
		// end defect 9969

		getchkUSA().setEnabled(abEnable);
		getstcLblDash().setEnabled(abEnable);
		gettxtLienholderCntry().setEnabled(abEnable);
		gettxtLienholderCntryZpcd().setEnabled(abEnable);
		gettxtLienholderCity().setEnabled(abEnable);
		gettxtLienholderName1().setEnabled(abEnable);
		gettxtLienholderName2().setEnabled(abEnable);
		gettxtLienholderState().setEnabled(abEnable);
		gettxtLienholderStreet1().setEnabled(abEnable);
		gettxtLienholderZpcd().setEnabled(abEnable);
		gettxtLienholderZpcdP4().setEnabled(abEnable);
		gettxtLienholderStreet2().setEnabled(abEnable);
		getstcLblDate().setEnabled(!cbCCO);
		gettxtLienDate().setEnabled(!cbCCO);

		// defect 9975 
		getbtnDeleteLien().setEnabled(!cbCCO);
		getstcLblId().setEnabled(!cbCCO && abEnable);
		gettxtLienholderId().setEnabled(!cbCCO && abEnable);
		getradioCertified().setEnabled(!cbCCO);
		getradioLocal().setEnabled(!cbCCO);
		getcomboLienholder().setEnabled(
			!cbCCO & getradioCertified().isSelected());
		// end defect 9975  

	}

	/**
	 * Validate Fields on Screen
	 * 
	 * @return boolean
	 */
	private boolean validateFields()
	{
		// defect 10127 
		boolean lbValid = true;

		RTSException leRTSEx = new RTSException();
		if (!gettxtLienDate().isDateEmpty()
			|| !caNameAddrComp.isNameAddressEmpty())
		{
			if (!gettxtLienDate().isValidDate())
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
					gettxtLienDate());
			}
			// defect 10504 
			else if (gettxtLienDate().isFutureDate())
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_MSG_DATE_CANNOT_BE_IN_FUTURE),
					gettxtLienDate());
			}
			// end defect 10504 
			// defect 10631 
			else if (
				getradioCertified().isSelected()
					&& getcomboLienholder().getSelectedIndex() >= 0)
			{
				int liIndex = getcomboLienholder().getSelectedIndex();

				CertifiedLienholderData laCertLienData =
					(
						CertifiedLienholderData) cvComboLienhlderData
							.elementAt(
						liIndex);

				CommonValidations.validateLienDate(
					gettxtLienDate(),
					laCertLienData.getPermLienHldrId(),
					leRTSEx);
			}
			// end defect 10631 

			caNameAddrComp.validateNameAddressFields(leRTSEx);

			if (leRTSEx.isValidationError())
			{
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
				lbValid = false;
			}
		}
		// end defect 10127

		return lbValid;
	} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
}