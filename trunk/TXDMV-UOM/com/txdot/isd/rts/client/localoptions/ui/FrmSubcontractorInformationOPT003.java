package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicTextUI.BasicHighlighter;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.NameAddressComponent;
import com.txdot.isd.rts.services.data.SubcontractorData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmSubcontractorInformationOPT003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		03/17/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang		03/30/2005	Remove setNextFocusable's
 * 							defect 7891 Ver 5.2.3
 * Min Wang 	04/16/2005	remove unused methods
 * 							delete
 * 							displayError(String asMsg, String asTitle),
 * 							displayError(String asMsgType, String asMsg,
 * 										String asTitle),
 * 							setController(), validateZip()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                 
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Ray Rowehl	08/24/2005	Work on constants
 * 							defect 7891 Ver 5.2.3
 * Min Wang		12/07/2005	remove equal sign from If
 * 							modify focusLost()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	09/03/2008	Standardize insert into AdminLog table 
 * 							add getAdminLogData() 
 * 							modify actionPerformed()
 * 							refactor caSearchData to caSubconData 
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	09/09/2008	Copied/Modified from Dealer Info OPT001
 * 							populated, disabled gettxtState()
 * 							defect 9812 Ver Defect_POS_B  
 * K Harrell	07/16/2009	Standardize 
 * 							modify validateFields()
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	08/22/2009	Implement new AdminLogData constructor(), 
 * 							RTSButtonGroup()  
 * 							add caRTSButtonGroup 
  * 							modify getAdminLogData(), initialize() 
 * 							defect 8628 Ver Defect_POS_F    
 * K Harrell	09/05/2009	Add getbtnDelete() to caRTSButtonGroup
 * 							modify initialize()
 * 						    defect 8628 Ver Defect_POS_F 
 * K Harrell	03/08/2010	Implement new SubcontractorData
 * 							add caNameAddrComp, cbInit  
 * 							add ivjtxtName2, ivjtxtStreet2, get methods
 * 							modify initialize(), setData(), refreshScreen(),
 * 							 setDataToDataObject(), validateFields(),
 * 							getFrmSubcontractorInformationOPT003ContentPane1() 
 * 							defect 10161 Ver POS_640 
 * K Harrell	11/03/2010	Do not clear screen when attempt to delete 
 * 							Subcontractor and exception thrown. 
 * 							modify actionPerformed() 
 * 							defect 10649 Ver 6.6.0 
 * R Pilon		07/23/2012	Correct logic checking for nulls. Was calling
 * 							  !<SOMETHING>.equals(null) which is obviously 
 * 							  incorrect.
 * 							modify focusLost(FocusEvent)
 * 							defect 11418 Ver 7.0.0
 * --------------------------------------------------------------------- 
 */

/**
 * Class for screen OPT003. Displays subcontractor information
 * 
 * @version	POS_700 		07/23/2012
 * @author	Ashish Mahajan
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmSubcontractorInformationOPT003
	extends RTSDialogBox
	implements ActionListener, FocusListener, KeyListener
{
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnRevise = null;
	private JPanel ivjFrmSubcontractorInformationOPT003ContentPane1 =
		null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblName = null;
	private JLabel ivjstcLblStreet = null;
	private JLabel ivjstcLblSubcontractorId = null;
	private RTSInputField ivjtxtCity = null;
	private RTSInputField ivjtxtName1 = null;
	private RTSInputField ivjtxtState = null;
	private RTSInputField ivjtxtStreet1 = null;
	private RTSInputField ivjtxtSubcontractorId = null;
	private RTSInputField ivjtxtZpcd = null;
	private RTSInputField ivjtxtZpcdP4 = null;
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();

	// defect 10161
	private RTSInputField ivjtxtName2 = null;
	private RTSInputField ivjtxtStreet2 = null; 
	private NameAddressComponent caNameAddrComp = null;
	private boolean cbInit = true;
	// end defect 10161 

	// Object 
	private SubcontractorData caSubcontractorData = null;

	// Constant
	private static final String OPT003_FRAME_NAME =
		"FrmSubcontractorInformationOPT003";
	private static final String OPT003_FRAME_TITLE =
		"Subcontractor Information   OPT003";
	private static final String SUBCONTRACTOR_NAME_COLON =
		"Subcontractor Name:";
	private static final String TXT_DELETE_SUBCONTRACTOR =
		"Delete Subcontractor";


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
			FrmSubcontractorInformationOPT003 laFrmSubcontractorInformationOPT003;
			laFrmSubcontractorInformationOPT003 =
				new FrmSubcontractorInformationOPT003();
			laFrmSubcontractorInformationOPT003.setModal(true);
			laFrmSubcontractorInformationOPT003
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSubcontractorInformationOPT003.show();
			Insets insets =
				laFrmSubcontractorInformationOPT003.getInsets();
			laFrmSubcontractorInformationOPT003.setSize(
				laFrmSubcontractorInformationOPT003.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSubcontractorInformationOPT003.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSubcontractorInformationOPT003.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 */
	public FrmSubcontractorInformationOPT003()
	{
		super();
		initialize();
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSubcontractorInformationOPT003(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSubcontractorInformationOPT003(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSubcontractorInformationOPT003(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSubcontractorInformationOPT003(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSubcontractorInformationOPT003(Frame aaOwner)
	{
		super(aaOwner);
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSubcontractorInformationOPT003(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSubcontractorInformationOPT003(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSubcontractorInformationOPT003(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSubcontractorInformationOPT003 constructor comment.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmSubcontractorInformationOPT003(JFrame aaOwner)
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

			// ADD || REVISE 
			if (aaAE.getSource() == getbtnAdd()
				|| aaAE.getSource() == getbtnRevise())
			{
				validateFields(leRTSEx);

				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
			}

			// ADD
			if (aaAE.getSource() == getbtnAdd())
			{
				setDataToDataObject();
				caSubcontractorData.setSubstaId(
					SystemProperty.getSubStationId());

				Vector lvData = new Vector();
				lvData.add(caSubcontractorData);
				// defect 8595
				lvData.add(
					getAdminLogData(CommonConstant.TXT_ADMIN_LOG_ADD));
				// end defect 8595 

				getController().processData(
					VCSubcontractorInformationOPT003.ADD,
					lvData);
				refreshScreen();
				gettxtSubcontractorId().requestFocus();
			}
			// CANCEL
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					VCSubcontractorInformationOPT003.CANCEL,
					caSubcontractorData);
			}
			// DELETE 
			else if (aaAE.getSource() == getbtnDelete())
			{
				leRTSEx =
					new RTSException(
						RTSException.CTL001,
						TXT_DELETE_SUBCONTRACTOR,
						ScreenConstant.CTL001_FRM_TITLE);
				int liRetCode = leRTSEx.displayError(this);
				if (liRetCode == RTSException.YES)
				{
					setDataToDataObject();

					Vector lvData = new Vector();
					lvData.add(caSubcontractorData);
					// defect 8595
					lvData.add(
						getAdminLogData(
							CommonConstant.TXT_ADMIN_LOG_DELETE));
					// end defect 8595 

					getController().processData(
						VCSubcontractorInformationOPT003.DELETE,
						lvData);
					// defect 10649 
					// refreshScreen();
					// gettxtSubcontractorId().requestFocus();
					// end defect 10649
				}
			}
			// REVISE 
			else if (aaAE.getSource() == getbtnRevise())
			{
				setDataToDataObject();

				Vector lvData = new Vector();
				lvData.add(caSubcontractorData);
				// defect 8595
				lvData.add(
					getAdminLogData(
						CommonConstant.TXT_ADMIN_LOG_REVISE));
				// end defect 8595 

				getController().processData(
					VCSubcontractorInformationOPT003.REVISE,
					lvData);
				refreshScreen();
				gettxtSubcontractorId().requestFocus();
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
		if (aaFE.getSource() == gettxtSubcontractorId())
		{
			caSubcontractorData = null;
		}
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * Validate SubcontractorID and zip code.
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
			&& aaFE.getSource() == gettxtSubcontractorId())
		{
			clearAllColor(this);
			String lsSubconId =
				gettxtSubcontractorId().getText().trim();
				
			if (lsSubconId.length() > 0)
			{
				int liId = Integer.parseInt(lsSubconId);
				if (liId > 0)
				{
					gettxtSubcontractorId().setText(
						CommonConstant.STR_SPACE_EMPTY + liId);
				}
				else
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.addException(
						new RTSException(150),
						gettxtSubcontractorId());
					leRTSEx.displayError(this);
					return;
				}

				if (caSubcontractorData == null
					&& lsSubconId.length() != 0)
				{
					caSubcontractorData = new SubcontractorData();

					getbtnAdd().setEnabled(true);
					getbtnDelete().setEnabled(false);
					getbtnRevise().setEnabled(false);

					SubcontractorData laData = new SubcontractorData();
					laData.setId(
						Integer.parseInt(
							gettxtSubcontractorId().getText()));
					laData.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					laData.setSubstaId(
						SystemProperty.getSubStationId());

					getController().processData(
						VCSubcontractorInformationOPT003.SEARCH,
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
		laAdminlogData.setEntity(CommonConstant.TXT_SUBCON);
		// defect 8595
		laAdminlogData.setEntityValue(
			caSubcontractorData.getId()
				+ " "
				+ gettxtName1().getText().trim());
		// end defect 8595 

		return laAdminlogData;
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
				ivjbtnAdd.setSize(75, 25);
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMaximumSize(new Dimension(57, 25));
				ivjbtnAdd.setActionCommand(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMinimumSize(new Dimension(57, 25));
				ivjbtnAdd.setLocation(43, 213);
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
				ivjbtnCancel.setSize(75, 25);
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setMaximumSize(new Dimension(73, 25));
				ivjbtnCancel.setActionCommand(
					CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setMinimumSize(new Dimension(73, 25));
				ivjbtnCancel.setLocation(322, 213);
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
				ivjbtnDelete.setSize(75, 25);
				ivjbtnDelete.setName("btnDelete");
				ivjbtnDelete.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjbtnDelete.setText(CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setMaximumSize(new Dimension(71, 25));
				ivjbtnDelete.setActionCommand(
					CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setMinimumSize(new Dimension(71, 25));
				ivjbtnDelete.setLocation(228, 213);
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
				ivjbtnRevise.setSize(75, 25);
				ivjbtnRevise.setName(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMnemonic(java.awt.event.KeyEvent.VK_R);
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMaximumSize(new Dimension(73, 25));
				ivjbtnRevise.setActionCommand(
					CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setMinimumSize(new Dimension(73, 25));
				ivjbtnRevise.setLocation(134, 213);
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
	 * Return the FrmSubcontractorInformationOPT003ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSubcontractorInformationOPT003ContentPane1()
	{
		if (ivjFrmSubcontractorInformationOPT003ContentPane1 == null)
		{
			try
			{
				ivjFrmSubcontractorInformationOPT003ContentPane1 =
					new JPanel();
				ivjFrmSubcontractorInformationOPT003ContentPane1
					.setName(
					"FrmSubcontractorInformationOPT003ContentPane1");
				ivjFrmSubcontractorInformationOPT003ContentPane1
					.setLayout(
					null);
				ivjFrmSubcontractorInformationOPT003ContentPane1
					.setMaximumSize(
					new Dimension(551, 299));
				ivjFrmSubcontractorInformationOPT003ContentPane1
					.setMinimumSize(
					new Dimension(551, 299));
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					getstcLblSubcontractorId(),
					getstcLblSubcontractorId().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					getstcLblName(),
					getstcLblName().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					getstcLblStreet(),
					getstcLblStreet().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					gettxtSubcontractorId(),
					gettxtSubcontractorId().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					gettxtName1(),
					gettxtName1().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					gettxtStreet1(),
					gettxtStreet1().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					gettxtCity(),
					gettxtCity().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					gettxtZpcd(),
					gettxtZpcd().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					gettxtZpcdP4(),
					gettxtZpcdP4().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					getbtnAdd(),
					getbtnAdd().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					getbtnRevise(),
					getbtnRevise().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					getbtnDelete(),
					getbtnDelete().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getFrmSubcontractorInformationOPT003ContentPane1().add(
					getstcLblDash(),
					getstcLblDash().getName());
				ivjFrmSubcontractorInformationOPT003ContentPane1.add(
					gettxtState(),
					null);
				// defect 10161 
				ivjFrmSubcontractorInformationOPT003ContentPane1.add(
					gettxtStreet2(),
					null);
				ivjFrmSubcontractorInformationOPT003ContentPane1.add(
					gettxtName2(),
					null);
				// end defect 10161 
				// user code begin {1}
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmSubcontractorInformationOPT003ContentPane1;
	}

	/**
	 * Return the LblDash property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDash()
	{
		if (ivjstcLblDash == null)
		{
			try
			{
				ivjstcLblDash = new JLabel();
				ivjstcLblDash.setSize(10, 14);
				ivjstcLblDash.setName("stcLblDash");
				ivjstcLblDash.setText(
					CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_DASH
						+ CommonConstant.STR_SPACE_ONE);
				ivjstcLblDash.setMaximumSize(new Dimension(10, 14));
				ivjstcLblDash.setMinimumSize(new Dimension(10, 14));
				ivjstcLblDash.setLocation(314, 177);
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
	 * Return the LblName property value.
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
				ivjstcLblName.setSize(142, 20);
				ivjstcLblName.setName("stcLblName");
				ivjstcLblName.setText(SUBCONTRACTOR_NAME_COLON);
				ivjstcLblName.setMaximumSize(new Dimension(36, 14));
				ivjstcLblName.setMinimumSize(new Dimension(36, 14));
				ivjstcLblName.setLocation(78, 24);
				ivjstcLblName.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_N);
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
	 * Return the ivjstcLblStreet property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblStreet()
	{
		if (ivjstcLblStreet == null)
		{
			try
			{
				ivjstcLblStreet = new JLabel();
				ivjstcLblStreet.setSize(77, 20);
				ivjstcLblStreet.setName("ivjstcLblStreet");
				ivjstcLblStreet.setText(
					CommonConstant.TXT_ADDRESS_COLON);
				ivjstcLblStreet.setMaximumSize(new Dimension(38, 14));
				ivjstcLblStreet.setMinimumSize(new Dimension(38, 14));
				ivjstcLblStreet.setLocation(77, 103);
				ivjstcLblStreet.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjstcLblStreet.setLabelFor(gettxtStreet1());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblStreet;
	}

	/**
	 * Return the LblSubcontractorID property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSubcontractorId()
	{
		if (ivjstcLblSubcontractorId == null)
		{
			try
			{
				ivjstcLblSubcontractorId = new JLabel();
				ivjstcLblSubcontractorId.setSize(65, 20);
				ivjstcLblSubcontractorId.setName(
					"ivjstcLblSubcontractorId");
				ivjstcLblSubcontractorId.setText(
					CommonConstant.TXT_ID_COLON);
				ivjstcLblSubcontractorId.setComponentOrientation(
					ComponentOrientation.RIGHT_TO_LEFT);
				ivjstcLblSubcontractorId.setMaximumSize(
					new Dimension(125, 14));
				ivjstcLblSubcontractorId.setMinimumSize(
					new Dimension(125, 14));
				ivjstcLblSubcontractorId.setLocation(249, 23);
				// user code begin {1}
				ivjstcLblSubcontractorId.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_I);
				ivjstcLblSubcontractorId.setLabelFor(
					gettxtSubcontractorId());
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjstcLblSubcontractorId;
	}

	/**
	 * Get the SubcontractorData
	 * 
	 * @return SubcontractorData
	 */
	public Object getSubconData()
	{
		return caSubcontractorData;
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
				ivjtxtCity.setLocation(78, 175);
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
			ivjtxtName2.setSize(283, 20);
			ivjtxtName2.setInput(RTSInputField.DEFAULT);
			ivjtxtName2.setMaxLength(CommonConstant.LENGTH_NAME);
			ivjtxtName2.setLocation(78, 71);
		}
		return ivjtxtName2;
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
			ivjtxtState.setBounds(236, 175, 27, 20);
			ivjtxtState.setInput(RTSInputField.ALPHA_ONLY);
			ivjtxtState.setMaxLength(2);
			ivjtxtState.setText("TX");
			ivjtxtState.setEnabled(false);
		}
		return ivjtxtState;
	}

	/**
	 * Return the ivjtxtStreet1 property value.
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
				ivjtxtStreet1.setLocation(78, 125);
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
	 * Return the ivjtxtSubcontractorId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtSubcontractorId()
	{
		if (ivjtxtSubcontractorId == null)
		{
			try
			{
				ivjtxtSubcontractorId = new RTSInputField();
				ivjtxtSubcontractorId.setName("ivjtxtSubcontractorId");
				ivjtxtSubcontractorId.setHighlighter(
					new BasicHighlighter());
				ivjtxtSubcontractorId.setColumns(0);
				ivjtxtSubcontractorId.setBounds(321, 24, 40, 20);
				ivjtxtSubcontractorId.setRequestFocusEnabled(true);
				ivjtxtSubcontractorId.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtSubcontractorId.setMaxLength(
					LocalOptionConstant.LENGTH_SUBCON_ID);
				// user code begin {1}
				ivjtxtSubcontractorId.addFocusListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtxtSubcontractorId;
	}

	/**
	 * This method initializes ivjtxtStreet2
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtStreet2()
	{
		if (ivjtxtStreet2 == null)
		{
			ivjtxtStreet2 = new RTSInputField();
			ivjtxtStreet2.setSize(283, 20);
			ivjtxtStreet2.setLocation(78, 150);
			ivjtxtStreet2.setInput(RTSInputField.DEFAULT);
			ivjtxtStreet2.setMaxLength(CommonConstant.LENGTH_STREET);
		}
		return ivjtxtStreet2;
	}

	/**
	 * Return the ivjtxtZpcd property value.
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
				ivjtxtZpcd.setLocation(270, 175);
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
				ivjtxtZpcdP4.setName("ivjtxtZpcdP4");
				ivjtxtZpcdP4.setHighlighter(new BasicHighlighter());
				ivjtxtZpcdP4.setBounds(326, 175, 35, 20);
				ivjtxtZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
			setName(OPT003_FRAME_NAME);
			setSize(434, 287);
			setTitle(OPT003_FRAME_TITLE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setContentPane(
				getFrmSubcontractorInformationOPT003ContentPane1());
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
		// user code end

		// defect 10161 
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
				null,
				null,
				null,
				getstcLblDash(),
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID,
				ErrorsConstant.ERR_NUM_INCOMPLETE_ADDR_DATA,
				CommonConstant.TX_DEFAULT_STATE);
		// end defect 10161 
	}

	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyTyped(KeyEvent aaKE)
	{
		//empty code block
	}

	/**
	 * Clears screen values
	 */
	public void refreshScreen()
	{
		caSubcontractorData = null;
		gettxtSubcontractorId().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtName1().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtStreet1().setText(CommonConstant.STR_SPACE_EMPTY);
		// defect 10161 
		gettxtName2().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtStreet2().setText(CommonConstant.STR_SPACE_EMPTY);
		// end defect 10161 
		gettxtCity().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtZpcd().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtZpcdP4().setText(CommonConstant.STR_SPACE_EMPTY);
		getbtnAdd().setEnabled(false);
		getbtnDelete().setEnabled(false);
		getbtnRevise().setEnabled(false);

		gettxtSubcontractorId().requestFocus();
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
		// defect 10161
		boolean lbFound = false;
		if (aaDataObject == null)
		{
			caSubcontractorData = new SubcontractorData();

			if (cbInit)
			{
				caNameAddrComp.setNameAddressDataToDisplay(
					caSubcontractorData);
				cbInit = false;
			}
		}
		else
		{
			caSubcontractorData = (SubcontractorData) aaDataObject;
			lbFound = caSubcontractorData.isPopulated();
			caNameAddrComp.setNameAddressDataToDisplay(
				caSubcontractorData);
		}
		getbtnDelete().setEnabled(lbFound);
		getbtnRevise().setEnabled(lbFound);
		getbtnAdd().setEnabled(
			!lbFound && !gettxtSubcontractorId().isEmpty());
		// end defect 10161
	}

	/**
	 * Set the SubcontractorData object with Subcontractors information
	 */
	private void setDataToDataObject()
	{
		// defect 10161 
		caNameAddrComp.setNameAddressToDataObject(caSubcontractorData);

		caSubcontractorData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
			
		caSubcontractorData.setId(
			Integer.parseInt(gettxtSubcontractorId().getText()));
		// end defect 10161 
	}

	/** 
	 * Validate Fields on Screen
	 *
	 * @param aeRTSEx
	 */
	private void validateFields(RTSException aeRTSEx)
	{
		// defect 10161 
		if (gettxtSubcontractorId().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtSubcontractorId());
		}
		caNameAddrComp.validateNameAddressFields(aeRTSEx);
		// end defect 10161 
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
