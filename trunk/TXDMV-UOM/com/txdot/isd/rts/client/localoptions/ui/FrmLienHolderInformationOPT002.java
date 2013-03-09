package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicTextUI.BasicHighlighter;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.LienholderData;
import com.txdot.isd.rts.services.data.NameAddressComponent;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmLienHolderInformationOPT002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0  
 * Min Wang		02/23/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang		03/30/2005	Remove setNextFocusable's
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	remove unused methods
 * 							delete 
 * 							displayError(String asMsg, String asTitle),
 * 							displayError(String asMsgType, String asMsg,
 * 											String asTitle),
 * 							setController(), validateZip()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		05/19/2005	reline up the components by visual edit.
 * 							defect 8166 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3 
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Work on constants.		
 * 							defect 7891 Ver 5.2.3
 * K Harrell	08/31/2008	Copy/Modify from FrmDealerInformationOPT001 
 * 							to incorporate non-USA addresses 
 * 							defect 8727 Ver Defect_POS_B
 * K Harrell	09/03/2008	Standardize population of AdminLogData
 * 							add getAdminLogData()
 * 							modify actionPerformed()
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	09/09/2008	Restored check for SSN
 * 							add cbVTRAuth
 * 							modify actionPerformed()
 * 							defect 8727 Ver Defect_POS_B  
 * K Harrell	03/02/2009	Implement AddressData for Lienholder.  Modify
 * 							to use CommonConstant lengths. Sorted 
 * 							members. 
 * 							modify setData(), setDataToDataObject()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	06/25/2009	Implement new LienHolderData 
 * 							Additional Cleanup
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/18/2009	Implement new NameAddressComponent. 
 * 							refactor RTSInputFields, get methods to 
 * 							 standards 
 * 							add LIENHOLDER_NAME_COLON
 * 							add caNameAddrComp 
 * 							delete setNonUSAAddressDisplay(), 
 * 							 setUSAAddressDisplay()  
 * 							modify initialize(), itemStateChanged(), 
 * 							  refreshScreen(), setData(), 
 * 							  setDataToDataObject(), validateFields() 
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	08/04/2009	modify validateFields() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/22/2009	Implement new AdminLogData constructor()
 * 							Implement RTSButtonGroup
 * 							add caRTSButtonGroup 
 * 							delete carrBtn  
 * 							modify getAdminLogData(), initialize() 
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	09/05/2009	Only enable Add if gettxtLienHolderId() is  
 * 							 not empty()
 * 							add cbInit
 * 							modify setData() 
 * 							defect 10127 Ver Defect_POS_F   
 * R Pilon		07/23/2012	Correct logic checking for nulls. Was calling
 * 							  !<SOMETHING>.equals(null) which is obviously 
 * 							  incorrect.
 * 							modify focusLost(FocusEvent)
 * 							defect 11418 Ver 7.0.0
 * --------------------------------------------------------------------- 
 */

/**
 * Class for screen OPT002. Displays Lienholder information
 * 
 * @version	POS_700			07/23/2012
 * @author	Ashish Mahajan  
 * <br>Creation Date:		09/05/2001 13:30:59 
 */

public class FrmLienHolderInformationOPT002
	extends RTSDialogBox
	implements ActionListener, FocusListener, KeyListener, ItemListener
{
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnRevise = null;
	private JCheckBox ivjchkUSA = null;
	private JPanel ivjFrmLienHolderInformationOPT002ContentPane1 = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblLienHolderId = null;
	private JLabel ivjstcLblName = null;
	private JLabel ivjstcLblAddress = null;
	private RTSInputField ivjtxtCity = null;
	private RTSInputField ivjtxtCntry = null;
	private RTSInputField ivjtxtCntryZpcd = null;
	private RTSInputField ivjtxtLienHolderId = null;
	private RTSInputField ivjtxtName1 = null;
	private RTSInputField ivjtxtName2 = null;
	private RTSInputField ivjtxtState = null;
	private RTSInputField ivjtxtStreet1 = null;
	private RTSInputField ivjtxtStreet2 = null;
	private RTSInputField ivjtxtZpcd = null;
	private RTSInputField ivjtxtZpcdP4 = null;

	// boolean 
	private boolean cbSetDataFinished = false;
	private boolean cbVTRAuth = false;

	//	defect 10127 
	private boolean cbInit = true;
	private NameAddressComponent caNameAddrComp = null;

	private final static String LIENHOLDER_NAME_COLON =
		"LienHolder Name:";
	// end defect 10127 

	// Object  
	private LienholderData caLienholderData = null;

	// defect 8628 
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();
	// end defect 8628 

	private final static String OPT002_FRM_NAME =
		"FrmLienHolderInformationOPT002";
	private final static String OPT002_FRM_TITLE =
		"Lienholder Information   OPT002";
	private final static String TXT_DELETE_LIENHOLDER =
		CommonConstant.BTN_TXT_DELETE
			+ CommonConstant.STR_SPACE_ONE
			+ "Lienholder";

	/**
	 * main entrypoint - starts the part when it is run 
	 * as an application.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmLienHolderInformationOPT002 laFrmLienHolderInformationOPT002;
			laFrmLienHolderInformationOPT002 =
				new FrmLienHolderInformationOPT002();
			laFrmLienHolderInformationOPT002.setModal(true);
			laFrmLienHolderInformationOPT002
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmLienHolderInformationOPT002.show();
			Insets insets =
				laFrmLienHolderInformationOPT002.getInsets();
			laFrmLienHolderInformationOPT002.setSize(
				laFrmLienHolderInformationOPT002.getWidth()
					+ insets.left
					+ insets.right,
				laFrmLienHolderInformationOPT002.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmLienHolderInformationOPT002.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 */
	public FrmLienHolderInformationOPT002()
	{
		super();
		initialize();
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmLienHolderInformationOPT002(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmLienHolderInformationOPT002(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmLienHolderInformationOPT002(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmLienHolderInformationOPT002(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmLienHolderInformationOPT002(Frame aaOwner)
	{
		super(aaOwner);
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmLienHolderInformationOPT002(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmLienHolderInformationOPT002(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmLienHolderInformationOPT002(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmLienHolderInformationOPT002 constructor comment.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmLienHolderInformationOPT002(JFrame aaOwner)
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
			clearAllColor(this);

			//When Add or Revise button is pressed
			if (aaAE.getSource() == getbtnAdd()
				|| aaAE.getSource() == getbtnRevise())
			{
				// defect 10112 
				if (!validateFields())
				{
					return;
				}
				// end defect 10112 

				String lsNm1 = gettxtName1().getText().trim();
				String lsNm2 = gettxtName2().getText().trim();
				boolean lbDoVTR = false;
				if (!cbVTRAuth)
				{
					lbDoVTR = CommonValidations.isStringWithSSN(lsNm1);
				}
				if (!cbVTRAuth && !lbDoVTR)
				{
					lbDoVTR = CommonValidations.isStringWithSSN(lsNm2);
				}
				if (lbDoVTR)
				{
					setDataToDataObject();

					getController().processData(
						VCLienHolderInformationOPT002.VTR_AUTH,
						caLienholderData);
					if (caLienholderData.isVTRAuth())
					{
						cbVTRAuth = true;
					}
					else
					{
						return;
					}
				}
			}

			// Add
			if (aaAE.getSource() == getbtnAdd())
			{
				setDataToDataObject();

				caLienholderData.setSubstaId(
					SystemProperty.getSubStationId());

				Vector lvData = new Vector();
				lvData.add(caLienholderData);
				// defect 8595
				lvData.add(
					getAdminLogData(CommonConstant.TXT_ADMIN_LOG_ADD));
				// end defect 8595 

				getController().processData(
					VCLienHolderInformationOPT002.ADD,
					lvData);
				refreshScreen();
				gettxtLienHolderId().requestFocus();
			}
			// Cancel
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					VCLienHolderInformationOPT002.CANCEL,
					caLienholderData);
			}
			// Delete
			else if (aaAE.getSource() == getbtnDelete())
			{
				// defect 10112 
				RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						TXT_DELETE_LIENHOLDER,
						ScreenConstant.CTL001_FRM_TITLE);

				if (leRTSEx.displayError(this) == RTSException.YES)
				{
					// end defect 10112 

					setDataToDataObject();

					Vector lvData = new Vector();
					lvData.add(caLienholderData);

					// defect 8595
					lvData.add(
						getAdminLogData(
							CommonConstant.TXT_ADMIN_LOG_DELETE));
					// end defect 8595 

					getController().processData(
						VCLienHolderInformationOPT002.DELETE,
						lvData);
					refreshScreen();
					gettxtLienHolderId().requestFocus();
				}
			}
			// Revise
			else if (aaAE.getSource() == getbtnRevise())
			{
				setDataToDataObject();

				Vector lvData = new Vector();
				lvData.add(caLienholderData);
				// defect 8595
				lvData.add(
					getAdminLogData(
						CommonConstant.TXT_ADMIN_LOG_REVISE));
				// end defect 8595 

				getController().processData(
					VCLienHolderInformationOPT002.REVISE,
					lvData);
				refreshScreen();
				gettxtLienHolderId().requestFocus();
			}
		}
		catch (Exception aeEx)
		{
			handleException(aeEx);
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
		if (aaFE.getSource() == gettxtLienHolderId())
		{
			caLienholderData = null;
		}
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * Validate LienHolderID and zip code.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		if (!aaFE.isTemporary()
			// defect 11418
			&& aaFE.getOppositeComponent() != null
			// end defect 11418
			&& !aaFE.getOppositeComponent().equals(getbtnCancel())
			&& aaFE.getSource() == gettxtLienHolderId())
		{
			clearAllColor(this);
			String lsLienHldrId = gettxtLienHolderId().getText().trim();
			if (lsLienHldrId.length() > 0)
			{
				int liId = Integer.parseInt(lsLienHldrId);
				if (liId > 0)
				{
					gettxtLienHolderId().setText(
						CommonConstant.STR_SPACE_EMPTY + liId);
				}
				else
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtLienHolderId());
					leRTSEx.displayError(this);
					return;
				}

				if (caLienholderData == null
					&& lsLienHldrId.length() != 0)
				{
					caLienholderData = new LienholderData();
					getbtnAdd().setEnabled(true);
					getbtnDelete().setEnabled(false);
					getbtnRevise().setEnabled(false);

					LienholderData laData = new LienholderData();
					laData.setId(
						Integer.parseInt(
							gettxtLienHolderId().getText()));
					laData.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					laData.setSubstaId(
						SystemProperty.getSubStationId());

					getController().processData(
						VCLienHolderInformationOPT002.SEARCH,
						laData);
				}
				else
				{
					getbtnAdd().setEnabled(false);
				}
			}

		}
	}

	/**
	 * Return populated AdminLogData
	 * 
	 * @param asAction 
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData(String asAction)
	{
		// defect 8628 
		AdministrationLogData laAdminlogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 
		laAdminlogData.setAction(asAction);
		laAdminlogData.setEntity(CommonConstant.TXT_LIENHOLDER);

		// defect 8595
		laAdminlogData.setEntityValue(
			caLienholderData.getId()
				+ " "
				+ gettxtName1().getText().trim());
		// end defect 8595 
		return laAdminlogData;
	}

	/**
	 * Return the ivjbtnAdd property value.
	 *  
	 * @return RTSButton
	 */
	private RTSButton getbtnAdd()
	{
		if (ivjbtnAdd == null)
		{
			try
			{
				ivjbtnAdd = new RTSButton();
				ivjbtnAdd.setSize(75, 25);
				ivjbtnAdd.setName("ivjbtnAdd");
				ivjbtnAdd.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMaximumSize(new Dimension(57, 25));
				ivjbtnAdd.setActionCommand(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMinimumSize(new Dimension(57, 25));
				ivjbtnAdd.setLocation(40, 211);
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
	 * Return the ivjbtnCancel property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setSize(75, 25);
				ivjbtnCancel.setName("ivjbtnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setMaximumSize(new Dimension(73, 25));
				ivjbtnCancel.setActionCommand(
					CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setMinimumSize(new Dimension(73, 25));
				ivjbtnCancel.setLocation(319, 211);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
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
	 * Return the ivjbtnDelete property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDelete()
	{
		if (ivjbtnDelete == null)
		{
			try
			{
				ivjbtnDelete = new RTSButton();
				ivjbtnDelete.setSize(75, 25);
				ivjbtnDelete.setName("ivjbtnDelete");
				ivjbtnDelete.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjbtnDelete.setText(CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setMaximumSize(new Dimension(71, 25));
				ivjbtnDelete.setActionCommand(
					CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setMinimumSize(new Dimension(71, 25));
				ivjbtnDelete.setLocation(225, 211);
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
	 * Return the ivjbtnRevise property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnRevise()
	{
		if (ivjbtnRevise == null)
		{
			try
			{
				ivjbtnRevise = new RTSButton();
				ivjbtnRevise.setSize(75, 25);
				ivjbtnRevise.setName("ivjbtnRevise");
				ivjbtnRevise.setMnemonic(java.awt.event.KeyEvent.VK_R);
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMaximumSize(new Dimension(73, 25));
				ivjbtnRevise.setActionCommand(
					CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMinimumSize(new Dimension(73, 25));
				ivjbtnRevise.setLocation(131, 211);
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
	 * This method initializes ivjchkUSA
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkUSA()
	{
		if (ivjchkUSA == null)
		{
			ivjchkUSA = new JCheckBox();
			ivjchkUSA.setBounds(314, 95, 57, 21);
			ivjchkUSA.setText(CommonConstant.STR_USA);
			// user code begin {1}
			ivjchkUSA.setMnemonic(KeyEvent.VK_U);
			ivjchkUSA.addItemListener(this);
			// user code end
		}
		return ivjchkUSA;
	}

	/**
	 * Return the ivjFrmLienHolderInformationOPT002ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmLienHolderInformationOPT002ContentPane1()
	{
		if (ivjFrmLienHolderInformationOPT002ContentPane1 == null)
		{
			try
			{
				ivjFrmLienHolderInformationOPT002ContentPane1 =
					new JPanel();
				ivjFrmLienHolderInformationOPT002ContentPane1.setName(
					"ivjFrmLienHolderInformationOPT002ContentPane1");
				ivjFrmLienHolderInformationOPT002ContentPane1
					.setLayout(
					null);
				ivjFrmLienHolderInformationOPT002ContentPane1
					.setMaximumSize(
					new Dimension(551, 299));
				ivjFrmLienHolderInformationOPT002ContentPane1
					.setMinimumSize(
					new Dimension(551, 299));
				getFrmLienHolderInformationOPT002ContentPane1().add(
					getstcLblLienHolderId(),
					getstcLblLienHolderId().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					getstcLblName(),
					getstcLblName().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					getstcLblAddress(),
					getstcLblAddress().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					gettxtLienHolderId(),
					gettxtLienHolderId().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					gettxtName1(),
					gettxtName1().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					gettxtStreet1(),
					gettxtStreet1().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					gettxtStreet2(),
					gettxtStreet2().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					gettxtCity(),
					gettxtCity().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					gettxtZpcd(),
					gettxtZpcd().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					gettxtZpcdP4(),
					gettxtZpcdP4().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					getbtnAdd(),
					getbtnAdd().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					getbtnRevise(),
					getbtnRevise().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					getbtnDelete(),
					getbtnDelete().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getFrmLienHolderInformationOPT002ContentPane1().add(
					getstcLblDash(),
					getstcLblDash().getName());
				// user code begin {1}
				ivjFrmLienHolderInformationOPT002ContentPane1.add(
					gettxtName2(),
					null);
				ivjFrmLienHolderInformationOPT002ContentPane1.add(
					gettxtStreet1(),
					null);
				ivjFrmLienHolderInformationOPT002ContentPane1.add(
					gettxtState(),
					null);
				ivjFrmLienHolderInformationOPT002ContentPane1.add(
					getchkUSA(),
					null);
				ivjFrmLienHolderInformationOPT002ContentPane1.add(
					gettxtCntry(),
					null);
				ivjFrmLienHolderInformationOPT002ContentPane1.add(
					gettxtCntryZpcd(),
					null);
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmLienHolderInformationOPT002ContentPane1;
	}

	/**
	 * Get the LienholdersData
	 * 
	 * @return LienholdersData
	 */
	public Object getLienData()
	{
		return caLienholderData;
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
				ivjstcLblDash.setName("ivjstcLblDash");
				ivjstcLblDash.setText(
					CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_DASH
						+ CommonConstant.STR_SPACE_ONE);
				ivjstcLblDash.setMaximumSize(new Dimension(10, 14));
				ivjstcLblDash.setMinimumSize(new Dimension(10, 14));
				ivjstcLblDash.setBounds(314, 168, 10, 14);
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
		return ivjstcLblDash;
	}

	/**
	 * Return the ivjstcLblLienHolderId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLienHolderId()
	{
		if (ivjstcLblLienHolderId == null)
		{
			try
			{
				ivjstcLblLienHolderId = new JLabel();
				ivjstcLblLienHolderId.setSize(89, 20);
				ivjstcLblLienHolderId.setName("ivjstcLblLienHolderId");
				ivjstcLblLienHolderId.setText(
					CommonConstant.TXT_ID_COLON);
				ivjstcLblLienHolderId.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblLienHolderId.setMaximumSize(
					new Dimension(125, 14));
				ivjstcLblLienHolderId.setMinimumSize(
					new Dimension(125, 14));
				ivjstcLblLienHolderId.setLocation(222, 23);
				// user code begin {1}
				ivjstcLblLienHolderId.setDisplayedMnemonic(
					KeyEvent.VK_I);
				ivjstcLblLienHolderId.setLabelFor(gettxtLienHolderId());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblLienHolderId;
	}

	/**
	 * Return the ivjstcLblName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblName()
	{
		if (ivjstcLblName == null)
		{
			try
			{
				ivjstcLblName = new JLabel();
				ivjstcLblName.setSize(109, 20);
				ivjstcLblName.setName("ivjstcLblName");
				ivjstcLblName.setMaximumSize(new Dimension(36, 14));
				ivjstcLblName.setMinimumSize(new Dimension(36, 14));
				ivjstcLblName.setLocation(78, 24);
				// user code begin {1}
				ivjstcLblName.setText(LIENHOLDER_NAME_COLON);
				ivjstcLblName.setDisplayedMnemonic(KeyEvent.VK_N);
				ivjstcLblName.setLabelFor(gettxtName1());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblName;
	}

	/**
	 * Return the ivjstcLblAddress property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAddress()
	{
		if (ivjstcLblAddress == null)
		{
			try
			{
				ivjstcLblAddress = new JLabel();
				ivjstcLblAddress.setSize(77, 20);
				ivjstcLblAddress.setName("ivjstcLblAddress");
				ivjstcLblAddress.setMaximumSize(new Dimension(38, 14));
				ivjstcLblAddress.setMinimumSize(new Dimension(38, 14));
				ivjstcLblAddress.setLocation(78, 96);
				// user code begin {1}
				ivjstcLblAddress.setText("Address:");
				ivjstcLblAddress.setDisplayedMnemonic(KeyEvent.VK_S);
				ivjstcLblAddress.setLabelFor(gettxtStreet1());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblAddress;
	}

	/**
	 * Return ivjtxtCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCity()
	{
		if (ivjtxtCity == null)
		{
			try
			{
				ivjtxtCity = new RTSInputField();
				ivjtxtCity.setSize(154, 20);
				ivjtxtCity.setName("ivjtxtCity");
				ivjtxtCity.setLocation(78, 166);
				ivjtxtCity.setHighlighter(new BasicHighlighter());
				// user code begin {1}				
				ivjtxtCity.setInput(RTSInputField.DEFAULT);
				ivjtxtCity.setMaxLength(CommonConstant.LENGTH_CITY);
				// user code end
				ivjtxtCity.setText("");
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtCity;
	}

	/**
	 * This method initializes ivjtxtCntry
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCntry()
	{
		if (ivjtxtCntry == null)
		{
			ivjtxtCntry = new RTSInputField();
			ivjtxtCntry.setSize(42, 20);
			ivjtxtCntry.setLocation(237, 166);
			// user code begin {1}
			ivjtxtCntry.setInput(RTSInputField.DEFAULT);
			ivjtxtCntry.setMaxLength(CommonConstant.LENGTH_CNTRY);
			// user code end
		}
		return ivjtxtCntry;
	}

	/**
	 * This method initializes ivjtxtCntryZpcd
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCntryZpcd()
	{
		if (ivjtxtCntryZpcd == null)
		{
			ivjtxtCntryZpcd = new RTSInputField();
			ivjtxtCntryZpcd.setBounds(284, 166, 80, 20);
			// user code begin {1}
			ivjtxtCntryZpcd.setInput(RTSInputField.ALPHANUMERIC_ONLY);
			ivjtxtCntryZpcd.setMaxLength(
				CommonConstant.LENGTH_CNTRY_ZIP);
			// user code end
		}
		return ivjtxtCntryZpcd;
	}

	/**
	 * Return ivjtxtLienHolderId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienHolderId()
	{
		if (ivjtxtLienHolderId == null)
		{
			try
			{
				ivjtxtLienHolderId = new RTSInputField();
				ivjtxtLienHolderId.setName("ivjtxtLienHolderId");
				ivjtxtLienHolderId.setHighlighter(
					new BasicHighlighter());
				ivjtxtLienHolderId.setBounds(321, 24, 40, 20);
				ivjtxtLienHolderId.setRequestFocusEnabled(true);
				ivjtxtLienHolderId.addFocusListener(this);
				// user code begin {1}
				ivjtxtLienHolderId.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtLienHolderId.setMaxLength(
					LocalOptionConstant.LENGTH_LIENHOLDER_ID);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtLienHolderId;
	}

	/**
	 * Return ivjtxtName1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtName1()
	{
		if (ivjtxtName1 == null)
		{
			try
			{
				ivjtxtName1 = new RTSInputField();
				ivjtxtName1.setSize(283, 20);
				ivjtxtName1.setName("ivjtxtName1");
				ivjtxtName1.setHighlighter(new BasicHighlighter());
				ivjtxtName1.setLocation(78, 46);
				// user code begin {1}
				ivjtxtName1.setInput(RTSInputField.DEFAULT);
				ivjtxtName1.setMaxLength(CommonConstant.LENGTH_NAME);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtName1;
	}

	/**
	 * This method initializes ivjtxtName2
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtName2()
	{
		if (ivjtxtName2 == null)
		{
			ivjtxtName2 = new RTSInputField();
			ivjtxtName2.setBounds(78, 68, 283, 20);
			ivjtxtName2.setHighlighter(new BasicHighlighter());
			// user code begin {1}
			ivjtxtName2.setInput(RTSInputField.DEFAULT);
			ivjtxtName2.setMaxLength(CommonConstant.LENGTH_NAME);
			// user code end
		}
		return ivjtxtName2;
	}

	/**
	 * This method initializes ivjtxtState
	 * 
	 * @return JTextField
	 */
	private RTSInputField gettxtState()
	{
		if (ivjtxtState == null)
		{
			ivjtxtState = new RTSInputField();
			ivjtxtState.setBounds(236, 166, 27, 20);
			// user code begin {1}				
			ivjtxtState.setInput(RTSInputField.ALPHA_ONLY);
			ivjtxtState.setMaxLength(CommonConstant.LENGTH_STATE);
			// user code end
		}
		return ivjtxtState;
	}

	/**
	 * Return ivjtxtStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtStreet1()
	{
		if (ivjtxtStreet1 == null)
		{
			try
			{
				ivjtxtStreet1 = new RTSInputField();
				ivjtxtStreet1.setSize(283, 20);
				ivjtxtStreet1.setName("ivjtxtStreet1");
				ivjtxtStreet1.setHighlighter(new BasicHighlighter());
				ivjtxtStreet1.setLocation(78, 120);
				// user code begin {1}				
				ivjtxtStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtStreet1;
	}

	/**
	 * This method initializes ivjtxtStreet2
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtStreet2()
	{
		if (ivjtxtStreet2 == null)
		{
			ivjtxtStreet2 = new RTSInputField();
			ivjtxtStreet2.setSize(283, 20);
			ivjtxtStreet2.setLocation(78, 143);
			// user code begin {1}			
			ivjtxtStreet2.setInput(RTSInputField.DEFAULT);
			ivjtxtStreet2.setMaxLength(CommonConstant.LENGTH_STREET);
			// user code end
		}
		return ivjtxtStreet2;
	}

	/**
	 * Return ivjtxtZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtZpcd()
	{
		if (ivjtxtZpcd == null)
		{
			try
			{
				ivjtxtZpcd = new RTSInputField();
				ivjtxtZpcd.setSize(43, 20);
				ivjtxtZpcd.setName("ivjtxtZpcd");
				ivjtxtZpcd.setHighlighter(new BasicHighlighter());
				ivjtxtZpcd.setLocation(270, 166);
				// user code begin {1}
				ivjtxtZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtZpcd.setMaxLength(CommonConstant.LENGTH_ZIPCODE);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtZpcd;
	}

	/**
	 * Return the ivjtxtZipLastFour property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtZpcdP4()
	{
		if (ivjtxtZpcdP4 == null)
		{
			try
			{
				ivjtxtZpcdP4 = new RTSInputField();
				ivjtxtZpcdP4.setName("ivjtxtZipLastFour");
				ivjtxtZpcdP4.setHighlighter(new BasicHighlighter());
				ivjtxtZpcdP4.setBounds(326, 166, 35, 20);
				// user code begin {1}
				ivjtxtZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtZpcdP4;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
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
			getbtnAdd().setEnabled(false);
			getbtnRevise().setEnabled(false);
			getbtnDelete().setEnabled(false);
			setRequestFocus(false);
			// user code end
			setName(OPT002_FRM_NAME);
			setSize(434, 282);
			setTitle(OPT002_FRM_TITLE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setContentPane(
				getFrmLienHolderInformationOPT002ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}

		// defect 8628 
		caRTSButtonGroup.add(getbtnAdd());
		caRTSButtonGroup.add(getbtnRevise());
		caRTSButtonGroup.add(getbtnDelete());
		caRTSButtonGroup.add(getbtnCancel());
		// end defect 8628 

		// defect 10127 
		caNameAddrComp =
			new NameAddressComponent(
				gettxtName1(),
				gettxtName2(),
				gettxtStreet1(),
				gettxtStreet2(),
				gettxtCity(),
				gettxtState(),
				gettxtZpcd(),
				gettxtZpcdP4(),
				gettxtCntry(),
				gettxtCntryZpcd(),
				getchkUSA(),
				getstcLblDash(),
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID,
				ErrorsConstant.ERR_NUM_INCOMPLETE_ADDR_DATA,
				CommonConstant.TX_DEFAULT_STATE);
		// end defect 10127 

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
		if (aaIE.getSource() == ivjchkUSA && cbSetDataFinished)
		{
			// defect 10127 
			caNameAddrComp.resetPerUSA(
				aaIE.getStateChange() == ItemEvent.SELECTED);
			// end defect 10127
		}
	}

	/**
	 * Clears screen values
	 */
	public void refreshScreen()
	{
		caLienholderData = null;
		gettxtLienHolderId().setText(CommonConstant.STR_SPACE_EMPTY);

		// defect 10127 
		caNameAddrComp.refreshScreen();
		// end defect 10127 

		getbtnAdd().setEnabled(false);
		getbtnDelete().setEnabled(false);
		getbtnRevise().setEnabled(false);

		gettxtLienHolderId().requestFocus();
		cbVTRAuth = false;
	}

	/**
	 * All subclasses must implement this method - it sets 
	 * the data on the screen and is how the controller relays 
	 * information to the view.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		cbSetDataFinished = false;

		// defect 10127 
		boolean lbFound = false;
		if (aaDataObject == null)
		{
			caLienholderData = new LienholderData();

			if (cbInit)
			{
				caNameAddrComp.setNameAddressDataToDisplay(
					caLienholderData);
				cbInit = false;
			}
		}
		else
		{
			caLienholderData = (LienholderData) aaDataObject;
			lbFound = caLienholderData.isPopulated();
			caNameAddrComp.setNameAddressDataToDisplay(
				caLienholderData);
		}
		getbtnDelete().setEnabled(lbFound);
		getbtnRevise().setEnabled(lbFound);
		getbtnAdd().setEnabled(
			!lbFound && !gettxtLienHolderId().isEmpty());
		// end defect 10127

		cbSetDataFinished = true;
	}

	/**
	 * Set the LienholdersData object with LienHolders information
	 */
	private void setDataToDataObject()
	{
		// defect 10127 
		caNameAddrComp.setNameAddressToDataObject(caLienholderData);
		caLienholderData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		caLienholderData.setId(
			Integer.parseInt(gettxtLienHolderId().getText()));
		// end defect 10127
	}
	/** 
	 * Validate Fields on Screen
	 *
	 * @return boolean 
	 */
	private boolean validateFields()
	{
		// defect 10127
		boolean lbReturn = true;
		RTSException leRTSEx = new RTSException();

		if (gettxtLienHolderId().isEmpty())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtLienHolderId());
		}

		caNameAddrComp.validateNameAddressFields(leRTSEx);

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
		// end defect 10127 
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
