package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicTextUI.BasicHighlighter;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.DealerData;
import com.txdot.isd.rts.services.data.NameAddressComponent;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmDealerInformationOPT001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0  
 * Min Wang		02/17/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang		03/30/2005	Remove setNextFocusable's
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	Remove unused methods
 * 							delete displayError(), validateZip()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		05/18/2005  Reorder input fields for validation
 * 							modify actionPerformed()
 * 							defect 8164 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3     
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Ray Rowehl	08/16/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 791 Ver 5.2.3
 * K Harrell	06/08/2008	Enhance Dealer Interface to add 2nd Name, 
 * 							2nd Street, State, USA Checkbox, Cntry, 
 * 							CntryZip.  Add associated validation. 
 * 							Reorganize Screen Presentation. 
 * 							add ivjtxtName2, ivjtxtStreet2, ivjtxtCntry,
 * 							  ivjtxtCntryZipCd, ivjchkUSA, get/set methods,
 * 							  itemStateChanged()  
 * 							add validateFields()
 * 							delete "Implements MouseListener,
 * 							  MouseMotionListener"
 * 							delete getBuilderData(), keyPressed(), 
 * 							  keyReleased(), mouseClicked(),mouseDragged(),
 * 							  mouseEntered(),mouseExited(), mouseMoved(),
 * 							  mousePressed(), mouseReleased()
 * 							modify ivjtxtPhoneNo,  
 * 							  gettxtPhoneNo(), actionPerformed(), 
 * 							  refreshScreen(), setData(), 
 * 							  setDataToDataObject()
 * 							defect 9654 Ver Defect POS A 
 * K Harrell	09/04/2008	Standardize population of AdminLogData
 * 							add getAdminLogData()  
 * 							delete TXT_DEALER_UC, TXT_DELETE_UC,
 * 							 TXT_ADD_UC 
 * 							modify actionPerformed() 
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	06/25/2009	Implemented new DealerData. Additional 
 * 							 cleanup. 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/18/2009	Implement new NameAddressComponent. 
 * 							refactor RTSInputFields, get methods to 
 * 							 standards 
 * 							add DEALER_ID_COLON, DEALER_NAME_COLON 
 * 							add caNameAddrComp 
 * 							delete setNonUSAAddressDisplay(), 
 * 							 setUSAAddressDisplay()  
 * 							modify getstcLblAddress(),gettxtZpcdP4(),
 * 							  initialize(), itemStateChanged(), 
 * 							  refreshScreen(), setData(), 
 * 							  setDataToDataObject(), validateFields() 
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	08/22/2009	Implement new AdminLogData constructor()
 * 							Implement RTSButtonGroup
 * 							add caRTSButtonGroup 
 * 							delete carrBtn  
 * 							modify getAdminLogData(), initialize() 
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	09/05/2009	Only enable Add if gettxtDealerId() is not 
 * 							 empty(). Do not clear Dealer info when not
 * 							 found. 
 * 							add cbInit 
 * 							modify setData() 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	12/31/2009	add cvAddlMFValid
 * 							modify initialize(), validateFields()
 * 							defect 10299 Ver Defect_POS_H
 * R Pilon		07/23/2012	Correct logic checking for nulls. Was calling
 * 							  !<SOMETHING>.equals(null) which is obviously 
 * 							  incorrect.
 * 							modify focusLost(FocusEvent)
 * 							defect 11418 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
 * Class for screen OPT001. Displays Dealer information.
 * 
 * @version	POS_700			07/23/2012
 * @author 	Ashish Mahajan
 * <br>Creation Date:		09/05/2001 13:30:59 
 */

public class FrmDealerInformationOPT001
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnRevise = null;
	private JCheckBox ivjchkUSA = null;
	private JPanel ivjFrmDealerInformationOPT001ContentPane1 = null;
	private JLabel ivjstcLblContactPerson = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblDealerId = null;
	private JLabel ivjstcLblName = null;
	private JLabel ivjstcLblPhoneNo = null;
	private JLabel ivjstcLblAddress = null;
	private RTSInputField ivjtxtCity = null;
	private RTSInputField ivjtxtCntry = null;
	private RTSInputField ivjtxtCntryZpcd = null;
	private RTSInputField ivjtxtContactPerson = null;
	private RTSInputField ivjtxtDealerId = null;
	private RTSInputField ivjtxtName1 = null;
	private RTSInputField ivjtxtName2 = null;
	private RTSPhoneField ivjtxtPhoneNo = null;
	private RTSInputField ivjtxtState = null;
	private RTSInputField ivjtxtStreet1 = null;
	private RTSInputField ivjtxtStreet2 = null;
	private RTSInputField ivjtxtZpcd = null;
	private RTSInputField ivjtxtZpcdP4 = null;

	// boolean 
	private boolean cbSetDataFinished = false;

	// Vector
	// defect 10299 
	Vector cvAddlMFValid;
	// end defect 10299  

	//	Object
	private DealerData caDealerData = null;

	// defect 8628 
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();
	// end defect 8628 

	// defect 10127
	private boolean cbInit = true;
	private NameAddressComponent caNameAddrComp = null;

	private final static String DEALER_ID_COLON = "Dealer Id:";
	private final static String DEALER_NAME_COLON = "Dealer Name:";
	// end defect 10127 

	// Constants 
	private final static String OPT001_FRM_NAME =
		"FrmDealerInformationOPT001";
	private final static String OPT001_FRM_TITLE =
		"Dealer Information   OPT001";

	private final static String TXT_DELETE_DEALER =
		CommonConstant.BTN_TXT_DELETE
			+ CommonConstant.STR_SPACE_ONE
			+ CommonConstant.TXT_DEALER;

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
			FrmDealerInformationOPT001 laFrmDealerInformationOPT001;
			laFrmDealerInformationOPT001 =
				new FrmDealerInformationOPT001();
			laFrmDealerInformationOPT001.setModal(true);
			laFrmDealerInformationOPT001
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmDealerInformationOPT001.show();
			Insets insets = laFrmDealerInformationOPT001.getInsets();
			laFrmDealerInformationOPT001.setSize(
				laFrmDealerInformationOPT001.getWidth()
					+ insets.left
					+ insets.right,
				laFrmDealerInformationOPT001.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmDealerInformationOPT001.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 */
	public FrmDealerInformationOPT001()
	{
		super();
		initialize();
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmDealerInformationOPT001(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmDealerInformationOPT001(Dialog aaOwner, boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmDealerInformationOPT001(Dialog aaOwner, String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmDealerInformationOPT001(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmDealerInformationOPT001(Frame aaOwner)
	{
		super(aaOwner);
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmDealerInformationOPT001(Frame aaOwner, boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmDealerInformationOPT001(Frame aaOwner, String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmDealerInformationOPT001(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmDealerInformationOPT001 constructor comment.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmDealerInformationOPT001(JFrame aaOwner)
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

			RTSException leRTSEx = new RTSException();

			// Add || Revise validation 
			if (aaAE.getSource() == getbtnAdd()
				|| aaAE.getSource() == getbtnRevise())
			{
				// defect 9654 
				validateFields(leRTSEx);
				// end defect 9654 

				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
			}

			// Add 
			if (aaAE.getSource() == getbtnAdd())
			{
				setDataToDataObject();

				caDealerData.setSubstaId(
					SystemProperty.getSubStationId());

				Vector lvData = new Vector();
				lvData.add(caDealerData);
				// defect 8595			
				lvData.add(
					getAdminLogData(CommonConstant.TXT_ADMIN_LOG_ADD));
				// end defect 8595

				getController().processData(
					VCDealerInformationOPT001.ADD,
					lvData);
				refreshScreen();
				gettxtDealerId().requestFocus();
			}
			// Cancel 
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					VCDealerInformationOPT001.CANCEL,
					caDealerData);
			}
			// Delete 
			else if (aaAE.getSource() == getbtnDelete())
			{
				leRTSEx =
					new RTSException(
						RTSException.CTL001,
						TXT_DELETE_DEALER,
						ScreenConstant.CTL001_FRM_TITLE);
				int liRetCode = leRTSEx.displayError(this);
				if (liRetCode == RTSException.YES)
				{
					setDataToDataObject();

					Vector lvData = new Vector();
					lvData.add(caDealerData);
					// defect 8595
					lvData.add(
						getAdminLogData(
							CommonConstant.TXT_ADMIN_LOG_DELETE));
					// end defect 8595  

					getController().processData(
						VCDealerInformationOPT001.DELETE,
						lvData);
				}
			}
			// Revise 
			else if (aaAE.getSource() == getbtnRevise())
			{
				setDataToDataObject();

				Vector lvData = new Vector();
				lvData.add(caDealerData);
				// defect 8595 
				lvData.add(
					getAdminLogData(
						CommonConstant.TXT_ADMIN_LOG_REVISE));
				// end defect 8595 

				getController().processData(
					VCDealerInformationOPT001.REVISE,
					lvData);
				refreshScreen();
				gettxtDealerId().requestFocus();
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
		if (aaFE.getSource() == gettxtDealerId())
		{
			caDealerData = null;
		}
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * Validate DealerID and zip code.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		// defect 9654 
		if (!aaFE.isTemporary()
			// defect 11418
			&& aaFE.getOppositeComponent() != null
			// end defect 11418
			&& !aaFE.getOppositeComponent().equals(getbtnCancel())
			&& aaFE.getSource() == gettxtDealerId())
		{
			clearAllColor(this);
			String lsDlrId = gettxtDealerId().getText().trim();
			if (lsDlrId.length() > 0)
			{
				int liId = Integer.parseInt(lsDlrId);
				if (liId > 0)
				{
					gettxtDealerId().setText(
						CommonConstant.STR_SPACE_EMPTY + liId);
				}
				else
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtDealerId());
					leRTSEx.displayError(this);
					return;
				}

				if (caDealerData == null && lsDlrId.length() != 0)
				{
					caDealerData = new DealerData();

					getbtnAdd().setEnabled(true);
					getbtnDelete().setEnabled(false);
					getbtnRevise().setEnabled(false);

					DealerData laData = new DealerData();
					laData.setId(
						Integer.parseInt(gettxtDealerId().getText()));
					laData.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					laData.setSubstaId(
						SystemProperty.getSubStationId());

					getController().processData(
						VCDealerInformationOPT001.SEARCH,
						laData);
				}
				else
				{
					getbtnAdd().setEnabled(false);
				}
			}
		}
		// end defect 9654 
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
		AdministrationLogData laAdminLogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 

		laAdminLogData.setAction(asAction);
		laAdminLogData.setEntity(CommonConstant.TXT_DEALER);
		// defect 8595
		laAdminLogData.setEntityValue(
			caDealerData.getId()
				+ " "
				+ gettxtName1().getText().trim());
		// end defect 8595 
		return laAdminLogData;
	}

	/**
	 * Return the btnAdd property value.
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
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMaximumSize(new Dimension(57, 25));
				ivjbtnAdd.setActionCommand(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMinimumSize(new Dimension(57, 25));
				ivjbtnAdd.setLocation(43, 310);
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
				ivjbtnCancel.setLocation(322, 310);
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
				ivjbtnDelete.setLocation(228, 310);
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
				ivjbtnRevise.setName(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMnemonic(java.awt.event.KeyEvent.VK_R);
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMaximumSize(new Dimension(73, 25));
				ivjbtnRevise.setActionCommand(
					CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMinimumSize(new Dimension(73, 25));
				ivjbtnRevise.setLocation(134, 310);
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
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkUSA()
	{
		if (ivjchkUSA == null)
		{
			ivjchkUSA = new javax.swing.JCheckBox();
			ivjchkUSA.setBounds(314, 95, 57, 21);
			ivjchkUSA.setText(CommonConstant.STR_USA);
			// user code begin {1}
			ivjchkUSA.setMnemonic(java.awt.event.KeyEvent.VK_U);
			ivjchkUSA.addItemListener(this);
			// user code end
		}
		return ivjchkUSA;
	}

	/**
	 * Get the DealerData
	 * 
	 * @return DealerData
	 */
	public Object getDlrData()
	{
		return caDealerData;
	}

	/**
	 * Return the ivjFrmDealerInformationOPT001ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmDealerInformationOPT001ContentPane1()
	{
		if (ivjFrmDealerInformationOPT001ContentPane1 == null)
		{
			try
			{
				ivjFrmDealerInformationOPT001ContentPane1 =
					new JPanel();
				ivjFrmDealerInformationOPT001ContentPane1.setName(
					"ivjFrmDealerInformationOPT001ContentPane1");
				ivjFrmDealerInformationOPT001ContentPane1.setLayout(
					null);
				ivjFrmDealerInformationOPT001ContentPane1
					.setMaximumSize(
					new Dimension(551, 299));
				ivjFrmDealerInformationOPT001ContentPane1
					.setMinimumSize(
					new Dimension(551, 299));
				getFrmDealerInformationOPT001ContentPane1().add(
					getstcLblDealerId(),
					getstcLblDealerId().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getstcLblName(),
					getstcLblName().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getstcLblAddress(),
					getstcLblAddress().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getstcLblContactPerson(),
					getstcLblContactPerson().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getstcLblPhoneNo(),
					getstcLblPhoneNo().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					gettxtDealerId(),
					gettxtDealerId().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					gettxtName1(),
					gettxtName1().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					gettxtStreet1(),
					gettxtStreet1().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					gettxtCity(),
					gettxtCity().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					gettxtZpcd(),
					gettxtZpcd().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					gettxtZpcdP4(),
					gettxtZpcdP4().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					gettxtContactPerson(),
					gettxtContactPerson().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					gettxtPhoneNo(),
					gettxtPhoneNo().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getbtnAdd(),
					getbtnAdd().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getbtnRevise(),
					getbtnRevise().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getbtnDelete(),
					getbtnDelete().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getFrmDealerInformationOPT001ContentPane1().add(
					getstcLblDash(),
					getstcLblDash().getName());
				// user code begin {1}
				ivjFrmDealerInformationOPT001ContentPane1.add(
					gettxtName2(),
					null);
				ivjFrmDealerInformationOPT001ContentPane1.add(
					gettxtStreet2(),
					null);
				ivjFrmDealerInformationOPT001ContentPane1.add(
					gettxtState(),
					null);
				ivjFrmDealerInformationOPT001ContentPane1.add(
					getchkUSA(),
					null);
				ivjFrmDealerInformationOPT001ContentPane1.add(
					gettxtCntry(),
					null);
				ivjFrmDealerInformationOPT001ContentPane1.add(
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
		return ivjFrmDealerInformationOPT001ContentPane1;
	}

	/**
	 * Return the ivjstcLblContactPerson property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblContactPerson()
	{
		if (ivjstcLblContactPerson == null)
		{
			try
			{
				ivjstcLblContactPerson = new JLabel();
				ivjstcLblContactPerson.setSize(103, 20);
				ivjstcLblContactPerson.setName(
					"ivjstcLblContactPerson");
				ivjstcLblContactPerson.setText(
					LocalOptionConstant.TXT_CONTACT_PERSON_COLON);
				ivjstcLblContactPerson.setMaximumSize(
					new Dimension(91, 14));
				ivjstcLblContactPerson.setMinimumSize(
					new Dimension(91, 14));
				ivjstcLblContactPerson.setLocation(78, 195);
				// user code begin {1}
				ivjstcLblContactPerson.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_C);
				ivjstcLblContactPerson.setLabelFor(
					gettxtContactPerson());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblContactPerson;
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
	 * Return the ivjstcLblDealerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDealerId()
	{
		if (ivjstcLblDealerId == null)
		{
			try
			{
				ivjstcLblDealerId = new JLabel();
				ivjstcLblDealerId.setSize(75, 20);
				ivjstcLblDealerId.setName("ivjstcLblDealerId");
				ivjstcLblDealerId.setText(DEALER_ID_COLON);
				ivjstcLblDealerId.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblDealerId.setMaximumSize(
					new Dimension(125, 14));
				ivjstcLblDealerId.setMinimumSize(
					new Dimension(125, 14));
				ivjstcLblDealerId.setLocation(229, 24);
				// user code begin {1}
				ivjstcLblDealerId.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_I);
				ivjstcLblDealerId.setLabelFor(gettxtDealerId());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblDealerId;
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
				ivjstcLblName.setSize(90, 20);
				ivjstcLblName.setName("ivjstcLblName");
				ivjstcLblName.setText(DEALER_NAME_COLON);
				ivjstcLblName.setMaximumSize(new Dimension(36, 14));
				ivjstcLblName.setMinimumSize(new Dimension(36, 14));
				ivjstcLblName.setLocation(78, 24);
				ivjstcLblName.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_N);
				ivjstcLblName.setLabelFor(gettxtName1());
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
		return ivjstcLblName;
	}

	/**
	 * Return the ivjstcLblPhoneNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPhoneNo()
	{
		if (ivjstcLblPhoneNo == null)
		{
			try
			{
				ivjstcLblPhoneNo = new JLabel();
				ivjstcLblPhoneNo.setSize(73, 20);
				ivjstcLblPhoneNo.setName("ivjstcLblPhoneNo");
				ivjstcLblPhoneNo.setText(
					CommonConstant.TXT_PHONE_NO_COLON);
				ivjstcLblPhoneNo.setMaximumSize(new Dimension(173, 14));
				ivjstcLblPhoneNo.setMinimumSize(new Dimension(173, 14));
				ivjstcLblPhoneNo.setLocation(78, 241);
				ivjstcLblPhoneNo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_P);
				ivjstcLblPhoneNo.setLabelFor(gettxtPhoneNo());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblPhoneNo;
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
				ivjstcLblAddress.setName("ivjstcLblStreet");
				// defect 10127 
				ivjstcLblAddress.setText(
					CommonConstant.TXT_ADDRESS_COLON);
				// end defect 10127 
				ivjstcLblAddress.setMaximumSize(new Dimension(38, 14));
				ivjstcLblAddress.setMinimumSize(new Dimension(38, 14));
				ivjstcLblAddress.setLocation(78, 96);
				ivjstcLblAddress.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_S);
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
	 * Return the ivjtxtCity property value.
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
				ivjtxtCity.setHighlighter(new BasicHighlighter());
				ivjtxtCity.setLocation(78, 166);
				ivjtxtCity.setInput(RTSInputField.DEFAULT);
				ivjtxtCity.setMaxLength(CommonConstant.LENGTH_CITY);
				// user code begin {1}
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
			ivjtxtCntry.setMaxLength(4);
			ivjtxtCntry.setInput(RTSInputField.DEFAULT);
		}
		return ivjtxtCntry;
	}

	/**
	 * This method initializes ivjtxtCntryZipCd
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCntryZpcd()
	{
		if (ivjtxtCntryZpcd == null)
		{
			ivjtxtCntryZpcd = new RTSInputField();
			ivjtxtCntryZpcd.setBounds(284, 166, 80, 20);
			ivjtxtCntryZpcd.setInput(RTSInputField.ALPHANUMERIC_ONLY);
			ivjtxtCntryZpcd.setMaxLength(
				CommonConstant.LENGTH_CNTRY_ZIP);
		}
		return ivjtxtCntryZpcd;
	}
	/**
	 * Return the ivjtxtContactPerson property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtContactPerson()
	{
		if (ivjtxtContactPerson == null)
		{
			try
			{
				ivjtxtContactPerson = new RTSInputField();
				ivjtxtContactPerson.setSize(283, 20);
				ivjtxtContactPerson.setName("ivjtxtContactPerson");
				ivjtxtContactPerson.setHighlighter(
					new BasicHighlighter());
				ivjtxtContactPerson.setLocation(78, 219);
				ivjtxtContactPerson.setInput(RTSInputField.DEFAULT);
				ivjtxtContactPerson.setMaxLength(
					CommonConstant.LENGTH_CONTACT_PERSON);
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
		return ivjtxtContactPerson;
	}

	/**
	 * Return the ivjtxtDealerId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtDealerId()
	{
		if (ivjtxtDealerId == null)
		{
			try
			{
				ivjtxtDealerId = new RTSInputField();
				ivjtxtDealerId.setName("ivjtxtDealerId");
				ivjtxtDealerId.setHighlighter(new BasicHighlighter());
				ivjtxtDealerId.setColumns(0);
				ivjtxtDealerId.setBounds(321, 24, 40, 20);
				ivjtxtDealerId.setRequestFocusEnabled(true);
				// user code begin {1}
				ivjtxtDealerId.addFocusListener(this);
				// user code end
				ivjtxtDealerId.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtDealerId.setMaxLength(
					LocalOptionConstant.LENGTH_DEALER_ID);
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtDealerId;
	}

	/**
	 * Return the ivjtxtName1 property value.
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
				ivjtxtName1.setInput(RTSInputField.DEFAULT);
				ivjtxtName1.setMaxLength(CommonConstant.LENGTH_NAME);
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
			ivjtxtName2.setInput(RTSInputField.DEFAULT);
			ivjtxtName2.setMaxLength(CommonConstant.LENGTH_NAME);
		}
		return ivjtxtName2;
	}

	/**
	 * Return ivjtxtPhoneNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSPhoneField gettxtPhoneNo()
	{
		if (ivjtxtPhoneNo == null)
		{
			try
			{
				ivjtxtPhoneNo = new RTSPhoneField();
				ivjtxtPhoneNo.setSize(88, 20);
				ivjtxtPhoneNo.setName("ivjtxtPhoneNo");
				ivjtxtPhoneNo.setHighlighter(new BasicHighlighter());
				ivjtxtPhoneNo.setLocation(78, 265);
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
		return ivjtxtPhoneNo;
	}

	/**
	 * This method initializes ivjtxtState
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtState()
	{
		if (ivjtxtState == null)
		{
			ivjtxtState = new RTSInputField();
			ivjtxtState.setBounds(236, 166, 27, 20);
			ivjtxtState.setInput(RTSInputField.ALPHA_ONLY);
			ivjtxtState.setMaxLength(CommonConstant.LENGTH_STATE);
		}
		return ivjtxtState;
	}

	/**
	 * Return the ivjtxtStreet property value.
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
				ivjtxtStreet1.setName("ivjtxtStreet");
				ivjtxtStreet1.setHighlighter(new BasicHighlighter());
				ivjtxtStreet1.setLocation(78, 120);
				ivjtxtStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
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
			ivjtxtStreet2.setInput(RTSInputField.DEFAULT);
			ivjtxtStreet2.setMaxLength(CommonConstant.LENGTH_STREET);
		}
		return ivjtxtStreet2;
	}

	/**
	 * Return ivjtxtZipCode property value.
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
				ivjtxtZpcd.setName("ivjtxtZipCode");
				ivjtxtZpcd.setHighlighter(new BasicHighlighter());
				ivjtxtZpcd.setLocation(270, 166);
				ivjtxtZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtZpcd.setMaxLength(CommonConstant.LENGTH_ZIPCODE);
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
		return ivjtxtZpcd;
	}

	/**
	 * Return the ivjtxtZpcdP4 property value.
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
				// defect 10127 
				ivjtxtZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// end defect 10127 
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
			setName(OPT001_FRM_NAME);
			setSize(434, 380);
			setTitle(OPT001_FRM_TITLE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setContentPane(getFrmDealerInformationOPT001ContentPane1());
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

		// defect 10299  
		cvAddlMFValid = new Vector();
		cvAddlMFValid.add(gettxtContactPerson());
		// end defect 10299

		// user code end
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
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
		caDealerData = null;
		gettxtDealerId().setText(CommonConstant.STR_SPACE_EMPTY);
		// defect 10127 
		caNameAddrComp.refreshScreen();
		// end defect 10127 
		gettxtContactPerson().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPhoneNo().setPhoneNo(CommonConstant.STR_SPACE_EMPTY);

		getbtnAdd().setEnabled(false);
		getbtnDelete().setEnabled(false);
		getbtnRevise().setEnabled(false);

		gettxtDealerId().requestFocus();
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
			caDealerData = new DealerData();

			if (cbInit)
			{
				caNameAddrComp.setNameAddressDataToDisplay(
					caDealerData);
				cbInit = false;
			}
		}
		else
		{
			caDealerData = (DealerData) aaDataObject;
			lbFound = caDealerData.isPopulated();
			caNameAddrComp.setNameAddressDataToDisplay(caDealerData);
			gettxtContactPerson().setText(caDealerData.getContact());
			gettxtPhoneNo().setPhoneNo(caDealerData.getPhoneNo());
		}
		getbtnDelete().setEnabled(lbFound);
		getbtnRevise().setEnabled(lbFound);
		getbtnAdd().setEnabled(!lbFound && !gettxtDealerId().isEmpty());
		// end defect 10127
	
		cbSetDataFinished = true;
		// end defect 9654
	}

	/**
	 * Set the DealerData object with dealers information
	 */
	private void setDataToDataObject()
	{
		// defect 10127 
		caNameAddrComp.setNameAddressToDataObject(caDealerData);
		// end defect 10127

		caDealerData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		caDealerData.setId(
			Integer.parseInt(gettxtDealerId().getText()));
		caDealerData.setContact(gettxtContactPerson().getText().trim());
		caDealerData.setPhoneNo(gettxtPhoneNo().getPhoneNo());
	}

	/** 
	 * Validate Fields on Screen
	 *
	 * @param aeRTSEx 
	 */
	private void validateFields(RTSException aeRTSEx)
	{
		// defect 10127
		if (gettxtDealerId().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtDealerId());
		}

		caNameAddrComp.validateNameAddressFields(aeRTSEx);
		// end defect 10127 

		// Contact PersonOld and Phone No not required for HQ

		// Contact PersonOld
		if (gettxtContactPerson().isEmpty() && !SystemProperty.isHQ())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtContactPerson());
		}
		// defect 10299
		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvAddlMFValid,
			aeRTSEx);
		// end defect 10299

		// Phone No 
		if ((gettxtPhoneNo().isPhoneNoEmpty()
			&& !SystemProperty.isHQ())
			|| (!gettxtPhoneNo().isPhoneNoEmpty()
				&& !gettxtPhoneNo().isValidPhoneNo()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPhoneNo());
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
