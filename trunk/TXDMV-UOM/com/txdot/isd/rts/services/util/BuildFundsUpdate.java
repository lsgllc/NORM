package com.txdot.isd.rts.services.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.services.exception.RTSException;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.ApplicationControlConstants;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * BuildFundsUpdate.java
 *
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		01/29/2009	Application to build Funds Update
 *							defect 9833 Ver Defect_POS_D
 * J Rue		03/26/2009	Set up base files
 * 							modify actionPerformed()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		04/17/2009	Set defaut MfInterfaceVersionCode to 
 * 							server.cfg
 * 							modify populateMfVerCodes()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		04/23/2009	Change getJListTestFiles().getSelectedValue()
 * 							to cvDeleteFile for delete file from folder
 * 							Add Test Files List box to a ScroolPane
 * 							add getJScrollPane()
 * 							modify actionPerformed()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		06/05/2009	Set Test Files list box to single selection
 * 							Move method constants to class constants
 * 							add CANNOT_DEL_BASE_FILE, BASE_FILE_143, 
 * 								BASE_FILE_012
 * 							modify getJListTestFiles()
 * 							defect 10080 Ver Defect_POS_F
 * J Rue		06/11/2009	Remove references to cvDeleteFile. Remove 
 *							references to getJListTestFiles().
 *							getSelectedValues() for checking multiply
 * 							selections in getJListTestFiles() list box
 * 							actionPerformwed()
 * 							delete cvDeleteFile
 * J Rue		06/16/2009	Move setDefaultMfVerCd() from UtiltiyMethods
 * 							to FileUtil class
 * 							modify populateMfVerCodes()	
 * 							defect 10080 Ver Defect_POS_F
 * J Rue		07/21/2009	Move clas from RTS project to RCCPI project
 * 							defect 10140 Ver Defecf_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Build Funds Update class.
 *
 * <p>This class is designed to build unique transaction for mainframe
 * testing
 * 
 * @version	Defect_POS_F	07/22/2009
 * @author	Jeff Rue
 * <br>Creation Date:		01/29/2009 14:53:36 
 */

public class BuildFundsUpdate
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	
	private static final String ACCNTNOCD = "ACCNTNOCD:";
	private static final int ACCNTNOCD_LENGTH = 1;
	private final static String BASE_FILE_012 = "012 Base Rec CHK.TXT";
	private final static String BASE_FILE_143 = "143 Base Rec EFT.TXT";
	private static final int BUTTON_TYPE_1 = 1;
	private static final int BUTTON_TYPE_2 = 2;
	private static final int BUTTON_TYPE_3 = 3;
	private static final int BUTTON_TYPE_4 = 4;
	private static final int BUTTON_TYPE_5 = 5;
	private final static String CANNOT_DEL_BASE_FILE =
		"You cannot delete a base file.";
	private static final String CKNO = "CKNO:";
	private static final int CKNO_LENGTH = 7;

	// Header lengths
	private static final int COMPTCNTYNO_LENGTH = 3;

	// String
	private final static String DELETE_FUNDS_FILE = "Delete Funds File";
	//	private static final String FUNDS_DATA = "Funds Data";
	private static final String FUNDS_RECORD = "Funds Record";
	private static final String FUNDSCAT = "FUNDSCAT";
	private static final int FUNDSCAT_LENGTH = 14;
	//	private static final String COMPTCNTYNO = "COMPTCNTYNO:";
	private static final String FUNDSPYMNTDATE = "FUNDSPYMNTDATE:";
	private static final int FUNDSPYMNTDATE_LENGTH = 8;
	private static final String FUNDSRCVDAMT = "FUNDSRCVDAMT";

	// FUNDS RECORD field names
	private static final String FUNDSRPTDATE = "FUNDSRPTDATE";
	// Record lengths
	private static final int FUNDSRPTDATE_LENGTH = 8;
	private static final String FUNDSUPDATE_FILE_LOC_ROOT =
		SystemProperty.getFundsUpdateFileLoc();
	//	private static final String BASE_RECORDS = "Base Records\\";
	private static final String FUNDSUPDATE_FOLDER = " - FundsUpdate\\";

	// String
	private static String MF_NAME = "mainframe";
	private static final String MF_VERSION = "MF Version";
	private static final String OFCISSUANCENO = "OFCISSUANCENO:";
	private static final int OFCISSUANCENO_LENGTH = 3;
	private static final String PAYMENT_HEADER = "Payment Header";
	private static final int PYMNTAMT_LENGTH = 11;
	private static final String PYMNTSTATUSCD = "PYMNTSTATUSCD:";
	private static final int PYMNTSTATUSCD_LENGTH = 1;
	private static final String PYMNTTYPECD = "PYMNTTYPECD:";
	private static final int PYMNTTYPECD_LENGTH = 2;
	private static final String RPTNGDATE = "RPTNGDATE";
	private static final int RPTNGDATE_LENGTH = 8;
	static final long serialVersionUID = -3528159199764710880L;
	public static final String T_FUNDSFILE = "T - FundsUpdate";

	// Interface Version Codea
	private final static String T_VER = "T";

	private static final String TEST_FILES = "Test Files";
	private static final String TRACENO = "TRACENO:";
	private static final int TRACENO_LENGTH = 9;
	private static final String TRANSEMPID = "TRANSEMPID:";
	private static final int TRANSEMPID_LENGTH = 7;
	private static final String TXT = ".TXT";
	public static final String U_FUNDSFILE = "U - FundsUpdate";
	private final static String U_VER = "U";
	public static final String V_FUNDSFILE = "V - FundsUpdate";
	private final static String V_VER = "V";

	// ArrayList
	ArrayList carrMfVerCodes = new ArrayList();
	ArrayList carrTestFiles = new ArrayList();

	// boolean
	boolean cbDeletedPaymnt = false;
	boolean cbValueChanged = true;
	private int ciCurrentFundsRecNo = 1;

	// int
	private int ciHeader_Len = 41;
	private int ciRec_Len = 41;
	private String csFundsUpdate_TestFiles_Loc =
		CommonConstant.STR_SPACE_EMPTY;

	// Vector
	Vector cvRecords = new Vector();
	//JPanel
	private JPanel ivjBldFundsUpdtContentPane1 = null;

	// RTSButtons
	private RTSButton ivjbtnBuild = null;
	private RTSButton ivjbtnExit = null;

	// JComboBox

	// JList
	private JList ivjJListMfVersion = null;
	private JList ivjJListTestFiles = null;
	private JLabel ivjlblRecNo = null;
	private JLabel ivjlblTotRecCnt = null;
	private JPanel ivjpnlFundsData = null;
	private JPanel ivjpnlFundsRecs = null;
	private JPanel ivjpnlPaymentHeader = null;
	private JLabel ivjstclblAccntNoCd = null;
	private JLabel ivjstclblCkNo = null;
	private JLabel ivjstclblCntyCompNo = null;
	private JLabel ivjstclblFndPymntDate = null;
	private JLabel ivjstclblFundsCat = null;
	private JLabel ivjstclblFundsRptDate = null;
	private JLabel ivjstclblOf = null;
	private JLabel ivjstclblOfcIssuanceNo = null;
	private JLabel ivjstclblPymntAmt = null;
	private JLabel ivjstclblPymntStatusCd = null;
	private JLabel ivjstclblPymntTypeCd = null;
	private JLabel ivjstclblRptngDate = null;
	private JLabel ivjstclblTestFiles = null;
	private JLabel ivjstclblTraceNo = null;
	private JLabel ivjstclblTransEmpId = null;

	// Label
	private JLabel ivjstclblVersion = null;
	private RTSInputField ivjtxtAccntNoCd = null;
	private RTSInputField ivjtxtCkNo = null;
	private RTSInputField ivjtxtComptCntyNo = null;
	private RTSInputField ivjtxtFdsPymntDate = null;
	private RTSInputField ivjtxtFundsCat = null;

	// RTSInputFields
	private RTSInputField ivjtxtFundsRptDate = null;
	private RTSInputField ivjtxtOfcissuanceNo = null;
	private RTSInputField ivjtxtPymntAmt = null;
	private RTSInputField ivjtxtPymntStatusCd = null;
	private RTSInputField ivjtxtPymntTypeCd = null;
	private RTSInputField ivjtxtRptDate = null;
	private RTSInputField ivjtxtTraceNo = null;
	private RTSInputField ivjtxtTransEmpId = null;
	private RTSButton wsdbtnAdd = null;
	//	Vector cvDeleteFile = new Vector();

	private RTSButton wsdbtnDeleteFile = null;
	private RTSButton wsdbtnDeletePymnt = null;
	private RTSButton wsdbtnFirst = null;
	private RTSButton wsdbtnLast = null;
	private RTSButton wsdbtnNext = null;
	private RTSButton wsdbtnPrev = null;
	private JScrollPane wsdJScrollPaneTestFiles = null;
	private JLabel wsdstclblRecNo = null;
	/**
	 * 
	 */
	public BuildFundsUpdate()
	{
		super();
		initialize();
	}
	public static void main(String[] aarrArgs)
	{
		try
		{
			if (aarrArgs.length > 0)
			{
				System.setProperty("ServerId", aarrArgs[0]);
			}
			if (aarrArgs.length > 1)
			{
				MF_NAME = aarrArgs[1];
			}
			BuildFundsUpdate laBldFundsUpdt;
			laBldFundsUpdt = new BuildFundsUpdate();
			laBldFundsUpdt.setModal(true);
			laBldFundsUpdt
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laBldFundsUpdt.setData(null);
			Log.write(Log.START_END, null, "Starting BuildFundsUpdate");
			laBldFundsUpdt.show();
			java.awt.Insets insets = laBldFundsUpdt.getInsets();
			laBldFundsUpdt.setSize(
				laBldFundsUpdt.getWidth() + insets.left + insets.right,
				laBldFundsUpdt.getHeight()
					+ insets.top
					+ insets.bottom);
			laBldFundsUpdt.setVisible(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(
				"Exception occurred in main() of com.txdot.isd.rts."
					+ "client.general.ui.RTSDialogBox");
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (aaAE.getSource() == getbtnBuild())
		{
			// Save data
			savePymntData();
			saveFdsDetailData();
			// Write SendTrans data to file
			writeStringToFile();
			populateTestFiles();
		}

		// Delete file from folder
		if (aaAE.getSource() == getbtnDeleteFile())
		{
			if (getJListTestFiles().getSelectedValue() == null)
			{
				RTSException laRTSEX =
					new RTSException(
						RTSException.INFORMATION_MESSAGE,
						"A file has not been selected. You must select a file.",
						DELETE_FUNDS_FILE);
				laRTSEX.displayError();
			}
			else if (
				getJListTestFiles()
					.getSelectedValue()
					.toString()
					.equals(
					BASE_FILE_143)
					|| getJListTestFiles()
						.getSelectedValue()
						.toString()
						.equals(
						BASE_FILE_012))
			{
				RTSException laRTSEX =
					new RTSException(
						RTSException.INFORMATION_MESSAGE,
						CANNOT_DEL_BASE_FILE,
						DELETE_FUNDS_FILE);
				laRTSEX.displayError();
			}
			else
			{
				RTSException laRTSEX =
					new RTSException(
						RTSException.CTL001,
						"You about to delete this file. Are you sure?",
						DELETE_FUNDS_FILE);
				int liAns = laRTSEX.displayError();
				if (liAns == 2)
				{
					FileUtil.deleteFile(
						SystemProperty.getFundsUpdateFileLoc()
							+ getJListMfVerCodes()
								.getSelectedValue()
								.toString()
							+ FUNDSUPDATE_FOLDER
							+ getJListTestFiles()
								.getSelectedValue()
								.toString());

					populateTestFiles();
				}
			}
		}
		if (aaAE.getSource() == getbtnAdd())
		{

		}
		if (aaAE.getSource() == getbtnDeletePymnt())
		{
			RTSException laRTSEX =
				new RTSException(
					RTSException.CTL001,
					"You about to delete this record. Are you sure?",
					"Delete FundsUpdate Record");
			int liAns = laRTSEX.displayError();
			if (liAns == 2)
			{
				// Delete record from vector
				cvRecords.remove(ciCurrentFundsRecNo);
				// Test if no records remain.
				if (ciCurrentFundsRecNo >= cvRecords.size() - 1)
				{
					ciCurrentFundsRecNo = cvRecords.size() - 1;
					clearPaymentFields();
				}
				else
				{
					setTxtFields(ciCurrentFundsRecNo);
				}
				cbDeletedPaymnt = true;
			}
		}
		if (aaAE.getSource() == getbtnFirst())
		{
			saveFdsDetailData();
			int liFirstRec = 1;
			setTxtFields(liFirstRec);
			setFundButtons(BUTTON_TYPE_1);
			//setAddDeleteButtons();
		}
		if (aaAE.getSource() == getbtnPrev())
		{
			saveFdsDetailData();
			int liPrevRec = ciCurrentFundsRecNo - 1;
			setTxtFields(liPrevRec);
			setFundButtons(BUTTON_TYPE_2);
			//setAddDeleteButtons();
		}
		if (aaAE.getSource() == getbtnNext())
		{
			saveFdsDetailData();
			int liNextRec = ciCurrentFundsRecNo + 1;
			setTxtFields(liNextRec);
			setFundButtons(BUTTON_TYPE_3);
			//setAddDeleteButtons();
		}
		if (aaAE.getSource() == getbtnLast())
		{
			saveFdsDetailData();
			int liLastRec = cvRecords.size() - 1;
			setTxtFields(liLastRec);
			setFundButtons(BUTTON_TYPE_4);
		}
		if (aaAE.getSource() == getbtnExit())
		{
			Log.write(Log.START_END, null, "Closing BuildFundsUpdate");
			System.exit(0);
		}

	}
	/**
	 * Clear Payment text input fields
	 *
	 */
	private void clearPaymentFields()
	{
		gettxtRecNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtTotRecCnt().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtFundsRptDate().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtRptDate().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPymntAmt().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtFundsCat().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtRecNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtTotRecCnt().setText(CommonConstant.STR_SPACE_EMPTY);
	}
	/**
	 * Compare current and class Vector data. If different prompt for 
	 * file name and save the data.
	 */
	private boolean compareRecords()
	{
		boolean lbRtn = false;
		String lsNewPymntData = CommonConstant.STR_SPACE_EMPTY;

		// Get Payment data from screen
		String lsPymntData =
			UtilityMethods.addPadding(
				gettxtComptCntyNo().getText(),
				3,
				CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtFdsPymntDate().getText(),
					8,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtAccntNoCd().getText(),
					1,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtTraceNo().getText(),
					9,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtTransEmpId().getText(),
					7,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtOfcissuanceNo().getText(),
					3,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtPymntTypeCd().getText(),
					2,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtPymntStatusCd().getText(),
					1,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtCkNo().getText(),
					7,
					CommonConstant.STR_SPACE_ONE);

		// Get Funds Detail data from screen
		String lsFdsDetailData =
			UtilityMethods.addPadding(
				gettxtFundsRptDate().getText(),
				8,
				CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtRptDate().getText(),
					8,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtPymntAmt().getText(),
					11,
					CommonConstant.STR_SPACE_ONE)
				+ UtilityMethods.addPadding(
					gettxtFundsCat().getText(),
					14,
					CommonConstant.STR_SPACE_ONE);

		// Concantate the payment and funds data

		String lsCurrData = lsPymntData + lsFdsDetailData;
		// Trim lsCurrData to remove blanks. The result for first time
		// through is empty lsCurrData.
		String lsCurrDataTrim = lsCurrData.trim();

		// Get data from class Vector (original)
		//	index = 0              		- Header record
		//  index = ciCurrentFundsRecNo - Funds Detail record number
		if (cvRecords.size() != 0)
		{
			lsNewPymntData =
				(String) cvRecords.get(0) + (String) cvRecords.get(1);

			// Check if current data from screen is not empty
			//	&& compare payment data on screen to new payment data
			if (lsCurrDataTrim.length() > 0
				&& lsCurrData.substring(0, 41).compareTo(
					(String) cvRecords.get(0))
					!= 0)
			{
				System.out.println("lsVctData is not equal to lsCData");
				cvRecords.size();
				lbRtn = true;
			}
		}
		return lbRtn;
	}
	/**
	 * Convert class Vector object to String
	 *
	 * @return Vector
	 */
	private Vector convertObjectToString()
	{
		Vector lvRec = new Vector();
		String lsRec = CommonConstant.STR_SPACE_EMPTY;
		for (int liIndex = 0; liIndex < cvRecords.size(); liIndex++)
		{
			lsRec = lsRec + ((String) cvRecords.get(liIndex));
		}
		lvRec.add(lsRec);

		return lvRec;
	}
	/**
	 * This method initializes wsdbtnAdd
	 * 
	 * @return wsdbtnDeleteFile
	 */
	private RTSButton getbtnAdd()
	{
		if (wsdbtnAdd == null)
		{
			try
			{
				wsdbtnAdd = new RTSButton();
				wsdbtnAdd.setBounds(30, 211, 70, 20);
				wsdbtnAdd.setText("Add");
				wsdbtnAdd.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return wsdbtnAdd;
	}
	/**
	 * Return the Build property value.
	 * 
	 * @return wsdbtnAdd
	 /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnBuild()
	{
		if (ivjbtnBuild == null)
		{
			try
			{
				ivjbtnBuild =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnBuild.setBounds(383, 346, 90, 24);
				ivjbtnBuild.setName("btnBuild");
				ivjbtnBuild.setText("Build");
				ivjbtnBuild.addActionListener(this);
				// begin user code
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnBuild;
	}
	/**
	 * This method initializes wsdbtnDeleteFile
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.wsdbtnDeleteFile
	 */
	private RTSButton getbtnDeleteFile()
	{
		if (wsdbtnDeleteFile == null)
		{
			try
			{
				wsdbtnDeleteFile = new RTSButton();
				wsdbtnDeleteFile.setBounds(126, 102, 90, 22);
				wsdbtnDeleteFile.setText("Delete");
				wsdbtnDeleteFile.addActionListener(this);
				// begin user code
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return wsdbtnDeleteFile;
	}
	/**
	 * This method initializes wsdbtnDeletePymnt
	 * 
	 * @return wsdbtnDeleteFile
	 */
	private RTSButton getbtnDeletePymnt()
	{
		if (wsdbtnDeletePymnt == null)
		{
			try
			{
				wsdbtnDeletePymnt = new RTSButton();
				wsdbtnDeletePymnt.setBounds(145, 211, 70, 20);
				wsdbtnDeletePymnt.setText("Delete");
				wsdbtnDeletePymnt.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return wsdbtnDeletePymnt;
	}

	/**
	 * Return the Exit property value.
	 * 
	 * @return wsdbtnAdd
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnExit()
	{
		if (ivjbtnExit == null)
		{
			try
			{
				ivjbtnExit = new RTSButton();
				ivjbtnExit.setBounds(487, 345, 90, 24);
				ivjbtnExit.setName("btnExit");
				ivjbtnExit.setText("Exit");
				ivjbtnExit.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnExit;
	}
	/**
	 * This method initializes wsdbtnFirst
	 * 
	 * @return wsdbtnDeleteFile
	 */
	private RTSButton getbtnFirst()
	{
		if (wsdbtnFirst == null)
		{
			try
			{
				wsdbtnFirst = new RTSButton();
				wsdbtnFirst.setBounds(5, 239, 60, 20);
				wsdbtnFirst.setText("First");
				wsdbtnFirst.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return wsdbtnFirst;
	}
	/**
	 * This method initializes wsdbtnLast
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.wsdbtnLast
	 */
	private RTSButton getbtnLast()
	{
		if (wsdbtnLast == null)
		{
			try
			{
				wsdbtnLast = new RTSButton();
				wsdbtnLast.setBounds(185, 239, 60, 20);
				wsdbtnLast.setText("Last");
				wsdbtnLast.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return wsdbtnLast;
	}
	/**
	 * This method initializes wsdbtnNext
	 * 
	 * @return wsdbtnDeleteFile
	 */
	private RTSButton getbtnNext()
	{
		if (wsdbtnNext == null)
		{
			try
			{
				wsdbtnNext = new RTSButton();
				wsdbtnNext.setBounds(125, 239, 60, 20);
				wsdbtnNext.setText("Next");
				wsdbtnNext.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return wsdbtnNext;
	}
	/**
	 * This method initializes wsdbtnPrev
	 * 
	 * @return wsdbtnDeleteFile
	 */
	private RTSButton getbtnPrev()
	{
		if (wsdbtnPrev == null)
		{
			try
			{
				wsdbtnPrev = new RTSButton();
				wsdbtnPrev.setBounds(65, 239, 60, 20);
				wsdbtnPrev.setText("Prev");
				wsdbtnPrev.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return wsdbtnPrev;
	}
	/**
	 * This method initializes ivjpnlFundsData
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getFundsData()
	{
		if (ivjpnlFundsData == null)
		{
			try
			{

				ivjpnlFundsData = new javax.swing.JPanel();
				ivjpnlFundsData.setLayout(null);
				ivjpnlFundsData.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						CommonConstant.STR_SPACE_EMPTY));
				//						FUNDS_DATA));

				ivjpnlFundsData.add(getstclblFundsRptDate(), null);
				ivjpnlFundsData.add(getstclblRptngDate(), null);
				ivjpnlFundsData.add(getstclblPymntAmt(), null);
				ivjpnlFundsData.add(getstclblFundsCat(), null);
				ivjpnlFundsData.add(
					gettxtRptDate(),
					gettxtRptDate().getName());
				ivjpnlFundsData.add(
					gettxtFundsRptDate(),
					gettxtFundsRptDate().getName());
				ivjpnlFundsData.add(
					gettxtPymntAmt(),
					gettxtPymntAmt().getName());
				ivjpnlFundsData.add(
					gettxtFundsCat(),
					gettxtFundsCat().getName());

				ivjpnlFundsData.setBounds(7, 64, 235, 141);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}

		}
		return ivjpnlFundsData;
	}
	/**
	* Return the MfSendTransContentPane1 property value.
	* 
	* @return JPanel
	*/
	private javax.swing.JPanel getFundsUpdtContentPane()
	{
		if (ivjBldFundsUpdtContentPane1 == null)
		{
			try
			{
				ivjBldFundsUpdtContentPane1 = new javax.swing.JPanel();
				ivjBldFundsUpdtContentPane1.setName(
					"BldSndTransContentPane1");
				ivjBldFundsUpdtContentPane1.setLayout(null);
				ivjBldFundsUpdtContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjBldFundsUpdtContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjBldFundsUpdtContentPane1.add(
					getpnlFundsRecs(),
					getpnlFundsRecs().getName());
				ivjBldFundsUpdtContentPane1.add(
					getpnlPymntFundsRecs(),
					getpnlPymntFundsRecs().getName());
				ivjBldFundsUpdtContentPane1.add(
					getScrollPaneTestFiles(),
					getScrollPaneTestFiles().getName());
				ivjBldFundsUpdtContentPane1.add(
					getJListMfVerCodes(),
					getJListMfVerCodes().getName());
				ivjBldFundsUpdtContentPane1.add(
					getstclblMfVersion(),
					getstclblMfVersion().getName());
				ivjBldFundsUpdtContentPane1.add(
					getstclblTestFiles(),
					getstclblTestFiles().getName());
				ivjBldFundsUpdtContentPane1.add(
					getbtnExit(),
					getbtnExit().getName());
				ivjBldFundsUpdtContentPane1.add(
					getbtnBuild(),
					getbtnBuild().getName());
				ivjBldFundsUpdtContentPane1.add(
					getbtnDeleteFile(),
					getbtnDeleteFile().getName());
				ivjBldFundsUpdtContentPane1.setSize(641, 444);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBldFundsUpdtContentPane1;
	}
	/**
	* This method initializes JComboBox
	* 
	* @return JComboBox
	*/
	private JList getJListMfVerCodes()
	{
		if (ivjJListMfVersion == null)
		{
			try
			{
				ivjJListMfVersion = new JList();
				ivjJListMfVersion.setName("JListMfVersion");
				ivjJListMfVersion.setBounds(14, 36, 102, 50);
				// user code begin {1}
				ivjJListMfVersion.addListSelectionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJListMfVersion;
	}
	/**
	 * This method initializes ivjJListTestFiles
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListTestFiles()
	{
		if (ivjJListTestFiles == null)
		{
			try
			{
				ivjJListTestFiles = new JList();
				ivjJListTestFiles.setName("JListTestFiles");
				// Set list box to single selection
				ivjJListTestFiles.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjJListTestFiles.addListSelectionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjJListTestFiles;
	}

	/**
	 * This method initializes ivjpnlFundsRecs
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getpnlFundsRecs()
	{
		if (ivjpnlFundsRecs == null)
		{
			ivjpnlFundsRecs = new javax.swing.JPanel();
			ivjpnlFundsRecs.setLayout(null);
			ivjpnlFundsRecs.add(getstclblRecNo(), null);
			ivjpnlFundsRecs.add(gettxtRecNo(), null);
			ivjpnlFundsRecs.add(getstclblOF(), null);
			ivjpnlFundsRecs.add(gettxtTotRecCnt(), null);
			ivjpnlFundsRecs.add(getFundsData(), null);
			ivjpnlFundsRecs.add(getbtnAdd(), null);
			ivjpnlFundsRecs.add(getbtnDeletePymnt(), null);
			ivjpnlFundsRecs.add(getbtnFirst(), null);
			ivjpnlFundsRecs.add(getbtnPrev(), null);
			ivjpnlFundsRecs.add(getbtnNext(), null);
			ivjpnlFundsRecs.add(getbtnLast(), null);
			ivjpnlFundsRecs.setBounds(480, 36, 255, 268);
			ivjpnlFundsRecs.setBorder(
				new javax.swing.border.TitledBorder(
					new javax.swing.border.EtchedBorder(),
					FUNDS_RECORD));
		}
		return ivjpnlFundsRecs;
	}

	/**
	 * This method initializes ivjpnlFundsRecs
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getpnlPymntFundsRecs()
	{
		if (ivjpnlPaymentHeader == null)
		{
			try
			{
				ivjpnlPaymentHeader = new javax.swing.JPanel();
				ivjpnlPaymentHeader.setLayout(null);
				ivjpnlPaymentHeader.setVisible(true);
				ivjpnlPaymentHeader.add(getstclblCntyCompNo(), null);
				ivjpnlPaymentHeader.add(getstclblFdsPymntDate(), null);
				ivjpnlPaymentHeader.add(getstclblAccntNoCd(), null);
				ivjpnlPaymentHeader.add(getstclblCkNo(), null);
				ivjpnlPaymentHeader.add(getstclblPymntStatusCd(), null);
				ivjpnlPaymentHeader.add(getstclblPymntTypeCd(), null);
				ivjpnlPaymentHeader.add(getstclblOfcIssuanceNo(), null);
				ivjpnlPaymentHeader.add(getstclblTransEmpId(), null);
				ivjpnlPaymentHeader.add(getstclblTraceNo(), null);
				ivjpnlPaymentHeader.add(gettxtComptCntyNo(), null);
				ivjpnlPaymentHeader.add(gettxtFdsPymntDate(), null);
				ivjpnlPaymentHeader.add(gettxtAccntNoCd(), null);
				ivjpnlPaymentHeader.add(gettxtTraceNo(), null);
				ivjpnlPaymentHeader.add(gettxtTransEmpId(), null);
				ivjpnlPaymentHeader.add(gettxtOfcissuanceNo(), null);
				ivjpnlPaymentHeader.add(gettxtPymntTypeCd(), null);
				ivjpnlPaymentHeader.add(gettxtPymntStatusCd(), null);
				ivjpnlPaymentHeader.add(gettxtCkNo(), null);
				ivjpnlPaymentHeader.setBounds(236, 36, 240, 268);
				ivjpnlPaymentHeader.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						PAYMENT_HEADER));
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjpnlPaymentHeader;
	}
	/**
	 * This method initializes wsdJScrollPaneTestFiles
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getScrollPaneTestFiles()
	{
		if (wsdJScrollPaneTestFiles == null)
		{
			try
			{
				wsdJScrollPaneTestFiles = new javax.swing.JScrollPane();
				wsdJScrollPaneTestFiles.setViewportView(
					getJListTestFiles());
				wsdJScrollPaneTestFiles.setBounds(13, 134, 202, 249);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return wsdJScrollPaneTestFiles;
	}
	/**
	 * This method initializes ivjstclblCntyCompNo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblAccntNoCd()
	{
		if (ivjstclblAccntNoCd == null)
		{
			try
			{
				ivjstclblAccntNoCd = new javax.swing.JLabel();
				ivjstclblAccntNoCd.setBounds(41, 74, 79, 20);
				ivjstclblAccntNoCd.setName("stclblAccntNoCd");
				ivjstclblAccntNoCd.setText(ACCNTNOCD);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblAccntNoCd;
	}
	/**
	 * This method initializes ivjstclblFundsRptDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblCkNo()
	{
		if (ivjstclblCkNo == null)
		{
			try
			{
				ivjstclblCkNo = new javax.swing.JLabel();
				ivjstclblCkNo.setBounds(78, 224, 42, 20);
				ivjstclblCkNo.setName("stclblCkNo");
				ivjstclblCkNo.setText(CKNO);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblCkNo;
	}
	/**
	 * This method initializes ivjstclblCntyCompNo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblCntyCompNo()
	{
		if (ivjstclblCntyCompNo == null)
		{
			try
			{
				ivjstclblCntyCompNo = new javax.swing.JLabel();
				ivjstclblCntyCompNo.setBounds(28, 24, 92, 20);
				ivjstclblCntyCompNo.setName("stclblCntyCompNo");
				ivjstclblCntyCompNo.setText("COMPTCNTYNO:");
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblCntyCompNo;
	}
	/**
	 * This method initializes ivjstclblFndPymntDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblFdsPymntDate()
	{
		if (ivjstclblFndPymntDate == null)
		{
			try
			{
				ivjstclblFndPymntDate = new javax.swing.JLabel();
				ivjstclblFndPymntDate.setBounds(9, 49, 111, 20);
				ivjstclblFndPymntDate.setName("stclblFdsPymntDate");
				ivjstclblFndPymntDate.setText(FUNDSPYMNTDATE);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblFndPymntDate;
	}
	/**
	 * This method initializes ivjstclblFundsCat
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblFundsCat()
	{
		if (ivjstclblFundsCat == null)
		{
			try
			{
				ivjstclblFundsCat = new javax.swing.JLabel();
				ivjstclblFundsCat.setBounds(52, 100, 65, 20);
				ivjstclblFundsCat.setName("stclblFundsCat");
				ivjstclblFundsCat.setText(FUNDSCAT);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblFundsCat;
	}

	/**
	 * This method initializes ivjstclblRptngDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblFundsRptDate()
	{
		if (ivjstclblFundsRptDate == null)
		{
			try
			{
				ivjstclblFundsRptDate = new javax.swing.JLabel();
				ivjstclblFundsRptDate.setBounds(25, 9, 92, 20);
				ivjstclblFundsRptDate.setName("stclblFundsRptDate");
				ivjstclblFundsRptDate.setText(FUNDSRPTDATE);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblFundsRptDate;
	}
	/**
	 * This method initializes ivjlblMfVersion
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblMfVersion()
	{
		try
		{

			if (ivjstclblVersion == null)
			{
				ivjstclblVersion = new javax.swing.JLabel();
				ivjstclblVersion.setName("stclblVersion");
				ivjstclblVersion.setBounds(14, 12, 95, 20);
				ivjstclblVersion.setText(MF_VERSION);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjstclblVersion;
	}
	/**
	 * This method initializes ivjstclblOf
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblOF()
	{
		if (ivjstclblOf == null)
		{
			try
			{
				ivjstclblOf = new javax.swing.JLabel();
				ivjstclblOf.setBounds(116, 29, 15, 15);
				ivjstclblOf.setText("of");
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblOf;
	}
	/**
	 * This method initializes ivjstclblFundsRptDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblOfcIssuanceNo()
	{
		if (ivjstclblOfcIssuanceNo == null)
		{
			try
			{
				ivjstclblOfcIssuanceNo = new javax.swing.JLabel();
				ivjstclblOfcIssuanceNo.setBounds(12, 149, 108, 20);
				ivjstclblOfcIssuanceNo.setName("stclblOfcIssuanceNo");
				ivjstclblOfcIssuanceNo.setText(OFCISSUANCENO);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblOfcIssuanceNo;
	}
	/**
	 * This method initializes ivjstclblPymntAmt
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblPymntAmt()
	{
		if (ivjstclblPymntAmt == null)
		{
			try
			{
				ivjstclblPymntAmt = new javax.swing.JLabel();
				ivjstclblPymntAmt.setBounds(19, 69, 98, 20);
				ivjstclblPymntAmt.setName("stclblPymntAmt");
				ivjstclblPymntAmt.setText(FUNDSRCVDAMT);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblPymntAmt;
	}
	/**
	 * This method initializes ivjstclblPymntStatusCd
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblPymntStatusCd()
	{
		if (ivjstclblPymntStatusCd == null)
		{
			try
			{
				ivjstclblPymntStatusCd = new javax.swing.JLabel();
				ivjstclblPymntStatusCd.setBounds(12, 199, 108, 20);
				ivjstclblPymntStatusCd.setName("stclblPymntStatusCd");
				ivjstclblPymntStatusCd.setText(PYMNTSTATUSCD);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblPymntStatusCd;
	}
	/**
	 * This method initializes ivjstclblFundsRptDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblPymntTypeCd()
	{
		if (ivjstclblPymntTypeCd == null)
		{
			try
			{
				ivjstclblPymntTypeCd = new javax.swing.JLabel();
				ivjstclblPymntTypeCd.setBounds(25, 174, 95, 20);
				ivjstclblPymntTypeCd.setName("stclblPymntTypeCd");
				ivjstclblPymntTypeCd.setText(PYMNTTYPECD);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblPymntTypeCd;
	}

	/**
	 * This method initializes wsdstclblRecNo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblRecNo()
	{
		if (wsdstclblRecNo == null)
		{
			try
			{
				wsdstclblRecNo = new javax.swing.JLabel();
				wsdstclblRecNo.setBounds(6, 29, 70, 15);
				wsdstclblRecNo.setText("Record No: ");
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}

		}
		return wsdstclblRecNo;
	}
	/**
	 * This method initializes ivjstclblRptngDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblRptngDate()
	{
		if (ivjstclblRptngDate == null)
		{
			try
			{
				ivjstclblRptngDate = new javax.swing.JLabel();
				ivjstclblRptngDate.setBounds(46, 40, 71, 20);
				ivjstclblRptngDate.setName("stclblRptngDate");
				ivjstclblRptngDate.setText(RPTNGDATE);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblRptngDate;
	}
	/**
	 * This method initializes ivjlblTestFiles
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblTestFiles()
	{
		if (ivjstclblTestFiles == null)
		{
			try
			{
				ivjstclblTestFiles = new javax.swing.JLabel();
				ivjstclblTestFiles.setBounds(14, 102, 61, 20);
				ivjstclblTestFiles.setName("stclblTestFiles");
				ivjstclblTestFiles.setText(TEST_FILES);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}

		return ivjstclblTestFiles;
	}

	/**
	 * This method initializes ivjstclblFundsPymntDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblTraceNo()
	{
		if (ivjstclblTraceNo == null)
		{
			try
			{
				ivjstclblTraceNo = new javax.swing.JLabel();
				ivjstclblTraceNo.setBounds(55, 99, 65, 20);
				ivjstclblTraceNo.setName("stclblTraceNo");
				ivjstclblTraceNo.setText(TRACENO);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblTraceNo;
	}
	/**
	 * This method initializes ivjstclblFundsRptDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblTransEmpId()
	{
		if (ivjstclblTransEmpId == null)
		{
			try
			{
				ivjstclblTransEmpId = new javax.swing.JLabel();
				ivjstclblTransEmpId.setBounds(33, 124, 87, 20);
				ivjstclblTransEmpId.setName("stclblTransEmpId");
				ivjstclblTransEmpId.setText(TRANSEMPID);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblTransEmpId;
	}
	/**
	 * This method initializes ivjtxtAccntNoCd
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtAccntNoCd()
	{
		if (ivjtxtAccntNoCd == null)
		{
			try
			{
				ivjtxtAccntNoCd = new RTSInputField();
				ivjtxtAccntNoCd.setBounds(128, 74, 100, 20);
				ivjtxtAccntNoCd.setName("txtAccntNoCd");
				ivjtxtAccntNoCd.setMaxLength(ACCNTNOCD_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtAccntNoCd;
	}
	/**
	 * This method initializes ivjtxtCkNo
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtCkNo()
	{
		if (ivjtxtCkNo == null)
		{
			try
			{
				ivjtxtCkNo = new RTSInputField();
				ivjtxtCkNo.setBounds(128, 224, 100, 20);
				ivjtxtCkNo.setName("txtCkNo");
				ivjtxtCkNo.setMaxLength(CKNO_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtCkNo;
	}
	/**
	 * This method initializes ivjtxtComptCntyNo
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtComptCntyNo()
	{
		if (ivjtxtComptCntyNo == null)
		{
			try
			{
				ivjtxtComptCntyNo = new RTSInputField();
				ivjtxtComptCntyNo.setBounds(128, 24, 100, 20);
				ivjtxtComptCntyNo.setName("txtComptCntyNo");
				ivjtxtComptCntyNo.setMaxLength(COMPTCNTYNO_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtComptCntyNo;
	}
	/**
	 * This method initializes ivjtxtFdsPymntDate
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtFdsPymntDate()
	{
		if (ivjtxtFdsPymntDate == null)
		{
			try
			{
				ivjtxtFdsPymntDate = new RTSInputField();
				ivjtxtFdsPymntDate.setBounds(128, 49, 100, 20);
				ivjtxtFdsPymntDate.setName("txtFdsPymntDate");
				ivjtxtFdsPymntDate.setMaxLength(FUNDSPYMNTDATE_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtFdsPymntDate;
	}
	/**
	 * This method initializes ivjtxtFundsCat
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtFundsCat()
	{
		if (ivjtxtFundsCat == null)
		{
			try
			{
				ivjtxtFundsCat = new RTSInputField();
				ivjtxtFundsCat.setBounds(122, 99, 100, 20);
				ivjtxtFundsCat.setName("txtFundsCat");
				ivjtxtFundsCat.setMaxLength(FUNDSCAT_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtFundsCat;
	}
	/**
	 * This method initializes ivjtxtFundsRptDate
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtFundsRptDate()
	{
		if (ivjtxtFundsRptDate == null)
		{
			try
			{
				ivjtxtFundsRptDate = new RTSInputField();
				ivjtxtFundsRptDate.setBounds(122, 9, 100, 20);
				ivjtxtFundsRptDate.setName("txtFundsRptDate");
				ivjtxtFundsRptDate.setMaxLength(FUNDSRPTDATE_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtFundsRptDate;
	}
	/**
	 * This method initializes ivjtxtOfcissuanceNo
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtOfcissuanceNo()
	{
		if (ivjtxtOfcissuanceNo == null)
		{
			try
			{
				ivjtxtOfcissuanceNo = new RTSInputField();
				ivjtxtOfcissuanceNo.setBounds(128, 149, 100, 20);
				ivjtxtOfcissuanceNo.setName("txtOfcissuanceNo");
				ivjtxtOfcissuanceNo.setMaxLength(OFCISSUANCENO_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtOfcissuanceNo;
	}
	/**
	 * This method initializes ivjtxtPymntAmt
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtPymntAmt()
	{
		if (ivjtxtPymntAmt == null)
		{
			try
			{
				ivjtxtPymntAmt = new RTSInputField();
				ivjtxtPymntAmt.setBounds(122, 69, 100, 20);
				ivjtxtPymntAmt.setName("txtPymntAmt");
				ivjtxtPymntAmt.setMaxLength(PYMNTAMT_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtPymntAmt;
	}
	/**
	 * This method initializes ivjtxtPymntStatusCd
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtPymntStatusCd()
	{
		if (ivjtxtPymntStatusCd == null)
		{
			try
			{
				ivjtxtPymntStatusCd = new RTSInputField();
				ivjtxtPymntStatusCd.setBounds(128, 199, 100, 20);
				ivjtxtPymntStatusCd.setName("txtPymntStatusCd");
				ivjtxtPymntStatusCd.setMaxLength(PYMNTSTATUSCD_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtPymntStatusCd;
	}
	/**
	 * This method initializes ivjtxtPymntTypeCd
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtPymntTypeCd()
	{
		if (ivjtxtPymntTypeCd == null)
		{
			try
			{
				ivjtxtPymntTypeCd = new RTSInputField();
				ivjtxtPymntTypeCd.setBounds(128, 174, 100, 20);
				ivjtxtPymntTypeCd.setName("txtPymntTypeCd");
				ivjtxtPymntTypeCd.setMaxLength(PYMNTTYPECD_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtPymntTypeCd;
	}
	/**
	 * This method initializes ivjlblRecNo
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.ivjlblRecNo
	 */
	private JLabel gettxtRecNo()
	{
		if (ivjlblRecNo == null)
		{
			try
			{
				ivjlblRecNo = new JLabel();
				ivjlblRecNo.setBounds(80, 26, 30, 20);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
			return ivjlblRecNo;

		}
		return ivjlblRecNo;
	}
	/**
	 * This method initializes ivjtxtRptDate
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtRptDate()
	{
		if (ivjtxtRptDate == null)
		{
			try
			{
				ivjtxtRptDate = new RTSInputField();
				ivjtxtRptDate.setBounds(122, 39, 100, 20);
				ivjtxtRptDate.setName("txtRptDate");
				ivjtxtRptDate.setMaxLength(RPTNGDATE_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtRptDate;
	}
	/**
	 * This method initializes ivjlblTotRecCnt
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.ivjlblTotRecCnt
	 */
	private JLabel gettxtTotRecCnt()
	{
		if (ivjlblTotRecCnt == null)
		{
			try
			{
				ivjlblTotRecCnt = new JLabel();
				ivjlblTotRecCnt.setBounds(138, 27, 31, 20);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}

		}
		return ivjlblTotRecCnt;
	}
	/**
	 * This method initializes ivjtxtTraceNo
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtTraceNo()
	{
		if (ivjtxtTraceNo == null)
		{
			try
			{
				ivjtxtTraceNo = new RTSInputField();
				ivjtxtTraceNo.setBounds(128, 99, 100, 20);
				ivjtxtTraceNo.setName("txtTraceNo");
				ivjtxtTraceNo.setMaxLength(TRACENO_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtTraceNo;
	}
	/**
	 * This method initializes ivjtxtTransEmpId
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtTransEmpId()
	{
		if (ivjtxtTransEmpId == null)
		{
			try
			{
				ivjtxtTransEmpId = new RTSInputField();
				ivjtxtTransEmpId.setBounds(128, 124, 100, 20);
				ivjtxtTransEmpId.setName("txtTransEmpId");
				ivjtxtTransEmpId.setMaxLength(TRANSEMPID_LENGTH);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjtxtTransEmpId;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception Throwable
	 */
	private void handleException(java.lang.Throwable aeThrowable)
	{ /* Uncomment the following lines to print uncaught exceptions to
																																																																																																																																																																																																																								 *  stdout 
																																																																																																																																																																																																																								 */
		System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		aeThrowable.printStackTrace(System.out);
	}
	/**
	 * Initialize the class.
	*/ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("BuildFundsUpdate");
			setSize(751, 442);
			setTitle("Build Funds Update    (R29)");
			setContentPane(getFundsUpdtContentPane());
			// user code begin {2}
			setRequestFocus(false);
			// user code end
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
	}
	/**
	 * Load Funds data from selected file name
	 */
	private void loadFundsDataFromFile()
	{
		// Initialize vector
		cvRecords = new Vector();
		String lsFileName = CommonConstant.STR_SPACE_EMPTY;
		// Get Funds Update base file. Load to vector
		try
		{
			lsFileName =
				getJListTestFiles().getSelectedValue().toString();
		}
		catch (NullPointerException leNPEx)
		{
			// Catch NullPointerException because list box is empty
		}
		if (lsFileName.length() > 0)
		{
			Vector lvRecords =
				FileUtil.copyFileToVector(
					SystemProperty.getFundsUpdateFileLoc()
						+ getJListMfVerCodes()
							.getSelectedValue()
							.toString()
						+ FUNDSUPDATE_FOLDER
						+ lsFileName);
			// Add header data to class vector					
			cvRecords.add(
				((String) lvRecords.get(0)).substring(0, ciHeader_Len));
			// Add Funds records to class vector
			int liCharLen = ciRec_Len;
			int liTotFundsRecs =
				(((String) lvRecords.get(0)).substring(liCharLen))
					.length()
					/ ciRec_Len;
			// Set Begin/End position for record
			int liCharLenPos = ciRec_Len + ciRec_Len;
			int liBegRecPos = ciRec_Len;

			for (int liIndex = 0; liIndex < liTotFundsRecs; liIndex++)
			{
				cvRecords.add(
					((String) lvRecords.get(0)).substring(
						liBegRecPos,
						liCharLenPos));
				liBegRecPos = liBegRecPos + liCharLen;
				liCharLenPos = liCharLenPos + ciRec_Len;
			}
		}
	}

	/**
	 * Populate the MF Access Methods Combo Box MF_VERSION_CODES 
	 */
	private void populateMfVerCodes()
	{
		//		setVisibleFields(false);

		// Retrieve field names only
		carrMfVerCodes.add(T_VER);
		carrMfVerCodes.add(U_VER);
		carrMfVerCodes.add(V_VER);

		// defect 9965
		//	Make call to get MfInterfaceVerCd from server.cfg
		String lsMfInterfaceVerCd = FileUtil.setDefaultMfVerCd();
		// end defect 9965

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(carrMfVerCodes.toArray());
		getJListMfVerCodes().setModel(laMdl);

		// defect 9965
		//	Set selection based on MfInterfaceVerCd in server.cfg
		getJListMfVerCodes().setSelectedValue(lsMfInterfaceVerCd, true);
		// end defect 9965
	}
	/**
	 * Populate the FundsUpdate test files combo box 
	 */
	private void populateTestFiles()
	{
		Vector lvFdsUpdtFiles = new Vector();
		String lsFileLoc = returnFundsUpdateFileLoc();

		File laFile = new File(lsFileLoc);
		String[] larrFiles = laFile.list();

		try
		{
			for (int liIndex = 0;
				liIndex < larrFiles.length;
				liIndex++)
			{
				lvFdsUpdtFiles.add(larrFiles[liIndex]);
			}

			DefaultComboBoxModel mdl =
				new DefaultComboBoxModel(lvFdsUpdtFiles);
			getJListTestFiles().setModel(mdl);
		}
		catch (NullPointerException leNPEx)
		{
			leNPEx.printStackTrace();
		}
	}
	/**
	 * Return FundsUpdate file folder location
	 * 
	 * Save the file folder selected
	 */
	private String returnFundsUpdateFileLoc()
	{
		String lsRtnStr = FUNDSUPDATE_FILE_LOC_ROOT;

		if (getJListMfVerCodes()
			.getSelectedValue()
			.toString()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			lsRtnStr =
				lsRtnStr + U_FUNDSFILE + CommonConstant.STR_BACK_SLASH;

		}
		else if (
			getJListMfVerCodes().getSelectedValue().toString().equals(
				ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			lsRtnStr =
				lsRtnStr + V_FUNDSFILE + CommonConstant.STR_BACK_SLASH;
		}
		else if (
			getJListMfVerCodes().getSelectedValue().toString().equals(
				ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			lsRtnStr =
				lsRtnStr + T_FUNDSFILE + CommonConstant.STR_BACK_SLASH;
		}

		// Set Folder/File location
		csFundsUpdate_TestFiles_Loc = lsRtnStr;
		return lsRtnStr;
	}
	/**
	 * Save funds detail data to class vector
	 */
	private void saveFdsDetailData()
	{
		if (cvRecords.size() != 0)
		{
			// Build Funds records
			String lsData =
				UtilityMethods.addPadding(
					gettxtFundsRptDate().getText(),
					8,
					CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtRptDate().getText(),
						8,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtPymntAmt().getText(),
						11,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtFundsCat().getText(),
						14,
						CommonConstant.STR_SPACE_ONE);

			// Add record then delect old record
			// Note: first record is always the header record
			//       if ciCurrentFundsRecNo = 0 means no funds detail
			//		  records exist
			if (ciCurrentFundsRecNo > 0)
			{
				cvRecords.insertElementAt(lsData, ciCurrentFundsRecNo);
				cvRecords.remove(ciCurrentFundsRecNo + 1);
			}
		}
	}
	/**
	 * Save payment data to class vector
	 * 
	 */
	private void savePymntData()
	{
		if (cvRecords.size() != 0)
		{
			// Build Funds records
			String lsData =
				UtilityMethods.addPadding(
					gettxtComptCntyNo().getText(),
					3,
					CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtFdsPymntDate().getText(),
						8,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtAccntNoCd().getText(),
						1,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtTraceNo().getText(),
						9,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtTransEmpId().getText(),
						7,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtOfcissuanceNo().getText(),
						3,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtPymntTypeCd().getText(),
						2,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtPymntStatusCd().getText(),
						1,
						CommonConstant.STR_SPACE_ONE)
					+ UtilityMethods.addPadding(
						gettxtCkNo().getText(),
						7,
						CommonConstant.STR_SPACE_ONE);

			// Add record then delect old record
			// Note: first record is always the header record
			cvRecords.insertElementAt(lsData, 0);
			cvRecords.remove(1);
		}
	}
	/**
	 * Set Add/Delete push buttons 
	 */
	private void setAddDeleteButtons()
	{
		// Turn on buttons
		getbtnDeletePymnt().setEnabled(true);
		getbtnAdd().setEnabled(true);

		// test for first record
		if (cvRecords.size() == 2)
		{
			getbtnDeletePymnt().setEnabled(false);
		}
		// Test for last record
		if (cvRecords.size() == ciCurrentFundsRecNo + 1)
		{
			getbtnDeletePymnt().setEnabled(false);
		}
		// Test for max record
		if (cvRecords.size() == 99)
		{
			getbtnAdd().setEnabled(false);
		}
	}
	/**
	 * Clear all text input fields
	 *
	 */
	private void setClearFundsTxtFields()
	{
		gettxtComptCntyNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtFdsPymntDate().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtAccntNoCd().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtTraceNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtTransEmpId().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtOfcissuanceNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPymntTypeCd().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPymntStatusCd().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtCkNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtRecNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtTotRecCnt().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtFundsRptDate().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtRptDate().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPymntAmt().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtFundsCat().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtRecNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtTotRecCnt().setText(CommonConstant.STR_SPACE_EMPTY);
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData 
	 */
	public void setData(java.lang.Object aaData)
	{
		populateMfVerCodes();
		populateTestFiles();
	}
	/**
	 * Set Funds Record push buttons 
	 * BUTTON_TYPE_1 - First, Prev setEnabled(false)
	 * BUTTON_TYPE_2 - (CurrentRecNo == 1) First, Prev setEnabled(false)
	 * BUTTON_TYPE_3 - (CurrentRecNo == cvRecords.size()) 
	 * 						Next, Last setEnabled(false)
	 * BUTTON_TYPE_4 - Next, Last setEnabled(false)
	 * BUTTON_TYPE_5 - One record. All buttons disabled
	 */
	private void setFundButtons(int aiButtonType)
	{
		// Turn on buttons
		setFundButtonsEnabled();

		if (aiButtonType == BUTTON_TYPE_1)
		{
			getbtnFirst().setEnabled(false);
			getbtnPrev().setEnabled(false);
		}
		if (aiButtonType == BUTTON_TYPE_2)
		{
			if (ciCurrentFundsRecNo == 1)
			{
				getbtnFirst().setEnabled(false);
				getbtnPrev().setEnabled(false);
			}
		}
		if (aiButtonType == BUTTON_TYPE_3)
		{
			if (ciCurrentFundsRecNo + 1 == cvRecords.size())
			{
				getbtnNext().setEnabled(false);
				getbtnLast().setEnabled(false);
			}
		}
		if (aiButtonType == BUTTON_TYPE_4)
		{
			getbtnNext().setEnabled(false);
			getbtnLast().setEnabled(false);
		}
		if (aiButtonType == BUTTON_TYPE_5)
		{
			getbtnFirst().setEnabled(false);
			getbtnPrev().setEnabled(false);
			getbtnNext().setEnabled(false);
			getbtnLast().setEnabled(false);
		}
	}
	/**
	 * Set Funds buttons to enabled
	 *
	 */
	private void setFundButtonsEnabled()
	{
		// Turn on buttons
		getbtnFirst().setEnabled(true);
		getbtnPrev().setEnabled(true);
		getbtnNext().setEnabled(true);
		getbtnLast().setEnabled(true);
	}
	/**
	 * Set all text fields. Get data for class Vector
	 * 
	 * @param aiRecNo	int
	 */
	private void setTxtFields(int aiRecNo)
	{
		// Initialize character counter
		int liCharCnt = 0;

		// Save the record number to be viewed
		if (aiRecNo > 1)
		{
			ciCurrentFundsRecNo = aiRecNo;
		}

		// Get header data from vector
		String lsHeader = (String) cvRecords.get(0);

		// Load Header
		gettxtComptCntyNo().setText(
			lsHeader.substring(0, COMPTCNTYNO_LENGTH));

		liCharCnt = liCharCnt + COMPTCNTYNO_LENGTH;
		gettxtFdsPymntDate().setText(
			lsHeader.substring(
				liCharCnt,
				liCharCnt + FUNDSPYMNTDATE_LENGTH));

		liCharCnt = liCharCnt + FUNDSPYMNTDATE_LENGTH;
		gettxtAccntNoCd().setText(
			lsHeader.substring(
				liCharCnt,
				liCharCnt + ACCNTNOCD_LENGTH));

		liCharCnt = liCharCnt + ACCNTNOCD_LENGTH;
		gettxtTraceNo().setText(
			lsHeader.substring(liCharCnt, liCharCnt + TRACENO_LENGTH));

		liCharCnt = liCharCnt + TRACENO_LENGTH;
		gettxtTransEmpId().setText(
			lsHeader.substring(
				liCharCnt,
				liCharCnt + TRANSEMPID_LENGTH));

		liCharCnt = liCharCnt + TRANSEMPID_LENGTH;
		gettxtOfcissuanceNo().setText(
			lsHeader.substring(
				liCharCnt,
				liCharCnt + OFCISSUANCENO_LENGTH));

		liCharCnt = liCharCnt + OFCISSUANCENO_LENGTH;
		gettxtPymntTypeCd().setText(
			lsHeader.substring(
				liCharCnt,
				liCharCnt + PYMNTTYPECD_LENGTH));

		liCharCnt = liCharCnt + PYMNTTYPECD_LENGTH;
		gettxtPymntStatusCd().setText(
			lsHeader.substring(
				liCharCnt,
				liCharCnt + PYMNTSTATUSCD_LENGTH));

		liCharCnt = liCharCnt + PYMNTSTATUSCD_LENGTH;
		gettxtCkNo().setText(
			lsHeader.substring(liCharCnt, liCharCnt + CKNO_LENGTH));
		// *********************************************************
		// Set Funds Detail Record counter
		if (ciCurrentFundsRecNo > 0)
		{
			String lsRecords =
				(String) cvRecords.get(ciCurrentFundsRecNo);
			gettxtRecNo().setText(String.valueOf(aiRecNo));
			gettxtTotRecCnt().setText(
				String.valueOf(cvRecords.size() - 1));

			// *********************************************************
			// Set record
			liCharCnt = 0;
			gettxtFundsRptDate().setText(
				lsRecords.substring(
					liCharCnt,
					liCharCnt + FUNDSRPTDATE_LENGTH));

			liCharCnt = FUNDSRPTDATE_LENGTH;
			gettxtRptDate().setText(
				lsRecords.substring(
					liCharCnt,
					liCharCnt + RPTNGDATE_LENGTH));

			liCharCnt = liCharCnt + RPTNGDATE_LENGTH;
			gettxtPymntAmt().setText(
				lsRecords.substring(
					liCharCnt,
					liCharCnt + PYMNTAMT_LENGTH));

			liCharCnt = liCharCnt + PYMNTAMT_LENGTH;
			gettxtFundsCat().setText(
				lsRecords.substring(
					liCharCnt,
					liCharCnt + FUNDSCAT_LENGTH));
		}
	}
	/**
	 * 
	 */
	public void valueChanged(ListSelectionEvent aaLSevent)
	{
		if (cbValueChanged)
		{
			if (aaLSevent.getSource() == getJListMfVerCodes())
			{
				populateTestFiles();
				setClearFundsTxtFields();
			}
			else if (aaLSevent.getSource() == getJListTestFiles())
			{
				// Load data from file to class vector cvRecords
				loadFundsDataFromFile();

				// Save data.
				// Change in MfVersionCd or file will prompt to save data
				// cvRecords = 0 means this is the first time
				if (compareRecords() || cbDeletedPaymnt)
				{
					//					savePymntData();
					//					saveFdsDetailData();
					//					writeStringToFile();
					cbDeletedPaymnt = false;
					cbValueChanged = false;
				}
				// Set up Funds and Payment screens.
				// Null means call is from start up when the JList box is 
				//	being built.
				if (getJListTestFiles().getSelectedValue() != null)
				{
					// Populate text fields with the first Funds record
					setTxtFields(1);
					//					populateTestFiles();
					cbValueChanged = false;
					if (cvRecords.size() == 2)
					{
						setFundButtons(BUTTON_TYPE_5);
					}
					else
					{
						setFundButtons(BUTTON_TYPE_1);
					}
					//setAddDeleteButtons();
				}
			}
		}
		else
		{
			cbValueChanged = true;
		}
	}
	/**
	 * Convert class Vector to data String. Prompt message for file name.
	 * Write data String to file.
	 */
	private void writeStringToFile()
	{
		try
		{
			// Convert class Vector to String. Save to Vector
			Vector lvRec = convertObjectToString();
			// Display meassage to enter file name
			// Write file to folder
			String lsFileName =
				JOptionPane.showInputDialog(
					this,
					"Enter file name for FundsUpdate.\n"
						+ " Extension '.txt' will be appended to file name");
			//Write data to folder
			if (lsFileName != null && lsFileName.length() > 0)
			{
				FileUtil.writeVectorToFile(
					SystemProperty.getFundsUpdateFileLoc()
						+ getJListMfVerCodes()
							.getSelectedValue()
							.toString()
						+ FUNDSUPDATE_FOLDER
						+ lsFileName
						+ TXT,
					lvRec);
			}
			else
			{

				RTSException leRTSEx =
					new RTSException(
						RTSException.INFORMATION_MESSAGE,
						"Funds Update file was not created.",
						"Funds Update");
				leRTSEx.displayError();

				// Remove data from Payment Header and Funds Record
				cvRecords = new Vector();
				setClearFundsTxtFields();
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.APPLICATION, aeRTSEx, null);
		}
	}
}
