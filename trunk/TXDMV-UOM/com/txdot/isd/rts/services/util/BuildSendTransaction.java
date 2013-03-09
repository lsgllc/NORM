package com.txdot.isd.rts.services.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * BuildSendTransaction.java
 *
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		10/01/2008	Application to build SendTrans
 *							defect 9833 Ver ELT_MfAccess
 * J Rue		10/21/2008	Additional changes. Turn on Updtaes
 * 							deefct 9833 Ver Defect_POS_B
 * J Rue		10/29/2008	Copy code from Defect_POS_B
 * 								Add MfVersionCode list box.
 * 								Turn on Update
 * 							defect 9833 Ver Defect_POS_B
 * J Rue		10/30/2008	Do not clear getlblEditFlds()
 * 							modify actionPerformed()
 * 							defect 8984 Ver MyPlatesII
 * J Rue		11/12/2008	Correct merge problem
 * 							modify addUpdates()
 * 							defect 8984 Ver MyPlatesII
 * J Rue		11/17/2008	Add/Remove all files buttons
 * 							add AddAllToSelectedFiles()
 * 								RmvdAllToSelectedFiles()
 * 							defect 8984 Ver MyPlatesII
 * J Rue		11/17/2008	Add populate all files
 * 							modify buildTransaction()
 * 							defect 8984 Ver MyPlatesII
 * J Rue		11/18/2008	Call Sleep thread to handle updates not 
 * 							applied.
 * 							modify valueChanged()
 * 							defect 8984 Ver MyPlatesII
 * J Rue		11/24/2008	Turn on Liens
 * 							add rmvUpdates()
 * 							modify removeItemSelectedFiles(),
 * 								addToSelectedFiles(),
 * 								addAllToSelectedFiles(),
 * 								rmvdAllSelectedFiles()
 * 							defect 8984 Ver MyPlatesII
 * J Rue		12/10/2008	Handle On/Off Liens buttons
 * 							modify resetAll(), addToSelectedFiles(),
 * 								AddAllToSelectedFiles(), 
 * 								rmvdAllSelectedFiles(),
 * 								removeItemSelectedFiles()
 * 							defect 8984 Ver MyPlatesII
 * J Rue		12/11/2008	Add no liens option to Liens
 * 							modify resetAll(), addToSelectedFiles(),
 * 								AddAllToSelectedFiles(), 
 * 								rmvdAllSelectedFiles(),
 * 								removeItemSelectedFiles()
 * 							defect 8984 Ver MyPlatesII
 * J Rue		03/23/2009	Adjust offsets for lien data
 * 							modify applyLienData()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		04/16/2009	Set defaut MfInterfaceVersionCode to 
 * 							server.cfg
 * 							modify setData(), populateMfVerCodes()
 * 							defect 9065 Ver Defect_POS_E
 * J Rue		05/25/2009	Add check to ensure selection of a file
 * 							from Selected File list box before removing.
 * 							modify removeItemFromSelectedFiles()
 * 							defect 10080	Ver Defect_POS_F
 * J Rue		05/25/2009	Capatolize 'F' in fields
 * 							modify POPULATE_ALL_FLDS
 * 							defect 10080	Ver Defect_POS_F
 * J Rue		07/21/2009	Move clas from RTS project to RCCPI project
 * 							defect 10140 Ver Defecf_POS_F
 * Ray Rowehl	09/01/2009	Clear red x
 * 							organize imports		
 * 							defect 10140 Ver Defect_POS_F
 * M Reyes		06/29/2010	Added RTS-PRMT-TRANS.
 * 							Modified actionPerformed(), addUpdates(),
 * 							buildHeader(), buildTransaction(), 
 * 							closeAllComboBoxes(), 
 * 							getBldSndTransContentPane1(),getFormats()
 *    						initializeJComboBox(), itemStateChanged(),
 * 							populateFiles(), rtnFrmtStr(), 
 * 							sortArrayList(), sortUpdtVec(),
 * 							valueChanged().
 * 							Added getJComboBoxPrmtTrans(),
 * 							populatePrmtTrans().  
 * 							defect 10492 Ver 6.5
 * ---------------------------------------------------------------------
 */

/**
 * Build SendTransaction class.
 *
 * <p>This class is designed to build unique transaction for mainframe
 * testing
 * 
 * @version	6.5				06/29/2010
 * @author	Jeff Rue
 * <br>Creation Date:		10/01/2008 09:55:36 
 */

public class BuildSendTransaction
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener, ItemListener
{
	private static final String _FULL = "_FULL";
	private static final String ADDL_SETUP = "Additional Setup:";
	private static final String ALL_UPDTS_REMOVED =
		"<html>All updates have been removed</html>";
	private static final String EMPTY_LIEN = "EmptyLien";
	private static final String FIELDS = "Fields:";
	private static final String FLD_UPDT = "Field Updates:";
	private static final String FUND_FUNC = "FUND_FUNC";
	private static final int FUND_FUNC_POS = 4;
	private static final String HEADER =
		"Build SendTrans Transaction,"
			+ " Select file(s), Test Individual fields and"
			+ " BuildSendTransaction does the rest.";

	//Mainframe db names
	private static final String HEADER_FILE = "Header";
	private static final String INV_FUNC = "INV_FUNC";
	private static final int INV_FUNC_POS = 3;

	// Constants String
	//	private static final String SERVER_CONFIG = "cfg/server";
	//	private static final String FILE_EXTENSION = "cfg";
	//	private static final String MFINTERFACEVERSIONCD =
	//		"MFInterfaceVersionCode";
	private static final String LIEN = "lien";
	private static final String LOG_FUNC_TRANS = "LOG_FUNC_TRANS";
	private static final int LOG_FUNC_TRANS_POS = 9;

	// Constants int
	private static final int MAX_FIELDS = 17;

	// String
	private static String csMF_NAME = "mainframe";
	private static final String MF_VERSION = "MF Version";
	private static final String MV_FUNC_TRANS = "MV_FUNC_TRANS";
	private static final int MV_FUNC_TRANS_POS = 1;
	private static final String NO_LIENS = "Liens";
	private static final String POPULATE_ALL_FLDS =
		"Populate All Fields";
	private static final String RTS_FUND_FUNC_TRNS =
		"RTS-FUND-FUNC-TRNS.";
	private static final String RTS_INV_FUNC_TRANS =
		"RTS-INV-FUNC-TRANS.";
	private static final String RTS_LOG_FUNC_TRANS =
		"RTS-LOG-FUNC-TRANS.";
	private static final String RTS_MV_FUNC_TRANS =
		"RTS-MV-FUNC-TRANS.";
	private static final String RTS_SR_FUNC_TRANS =
		"RTS-SR-FUNC-TRANS.";
	private static final String RTS_TR_FDS_DETAIL =
		"RTS-TR-FDS-DETAIL.";
	private static final String RTS_TR_INV_DETAIL =
		"RTS-TR-INV-DETAIL.";
	// defect 10492
	private static final String RTS_PRMT_TRANS = "RTS-PRMT-TRANS.";
	// end defect 10492

	// SendTrans file name in ADABAS report
	private static final String RTS_TRANS = "RTS-TRANS.";
	private static final String RTS_TRANS_PAYMENT =
		"RTS-TRANS-PAYMENT.";
	private static final String SELECTED_FILES = "Selected Files";
	private static final String SENDTRANS_BASE_LOC =
		" - SendTransBase\\";
	private static final String SENDTRANS_FILES = "SendTrans File ";

	// ADABAS file formats
	private static final String SENDTRANS77T =
		"(RT77)-Transactions data layout (P446071T)";
	private static final String SENDTRANS77U =
		"(RU77)-Transactions data layout (P446071U)";
	private static final String SENDTRANS77V =
		"(RV77)-Transactions data layout (P446071V)";
	private static final String SR_FUNC_TRANS = "SR_FUNC_TRANS";
	private static final int SR_FUNC_TRANS_POS = 2;
	// Interface Version Code
	private final static String T_VER = "T";
	private static final String TR_FDS_DLT = "TR_FDS_DTL";
	private static final int TR_FDS_DLT_POS = 6;
	private static final String TR_INV_DTL = "TR_INV_DTL";
	private static final int TR_INV_DTL_POS = 5;
	private static final String TRANS = "TRANS";
	private static final int TRANS_POS = 0;
	// defect 10492
	private static final String PRMT_TRANS = "PRMT_TRANS";
	private static final int PRMT_TRANS_POS = 7;
	// end defect492
	private static final String TRANS_PYMNT = "TRANS_PYMNT";
	private static final int TRANS_PYMNT_POS = 8;
	private static final String TRANSACTION_FILES =
		"Transaction Files\\";
	private static final String TXT = ".txt";
	private final static String U_VER = "U";
	private static final String UPDTS_REMOVED =
		"<html> Updates have been removed for selected file</html>";
	private final static String V_VER = "V";

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
				csMF_NAME = aarrArgs[1];
			}
			BuildSendTransaction laBldSndTrans;
			laBldSndTrans = new BuildSendTransaction();
			laBldSndTrans.setModal(true);
			laBldSndTrans
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laBldSndTrans.setData(null);

			Log.write(
				Log.START_END,
				null,
				"Starting BuildSendTransaction");
			laBldSndTrans.show();
			java.awt.Insets insets = laBldSndTrans.getInsets();
			laBldSndTrans.setSize(
				laBldSndTrans.getWidth() + insets.left + insets.right,
				laBldSndTrans.getHeight() + insets.top + insets.bottom);
			laBldSndTrans.setVisible(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(
				"Exception occurred in main() of com.txdot.isd.rts."
					+ "client.general.ui.RTSDialogBox");
			aeThrowable.printStackTrace(System.out);
		}
	}
	ArrayList carrFDSDTL = new ArrayList();
	ArrayList carrFDSFunc = new ArrayList();
	ArrayList carrFiles = new ArrayList();
	ArrayList carrInvDTL = new ArrayList();
	ArrayList carrInvFunc = new ArrayList();
	ArrayList carrLogFunc = new ArrayList();
	ArrayList carrMfVerCodes = new ArrayList();
	ArrayList carrMvTrans = new ArrayList();
	ArrayList carrPymnt = new ArrayList();

	// ArrayList
	// The default is 10 items
	ArrayList carrSelectedFiles = new ArrayList();
	ArrayList carrSendTransFileList = new ArrayList();
	ArrayList carrSRTrans = new ArrayList();
	ArrayList carrTrans = new ArrayList();
	// defect 10492
	ArrayList carrPrmtTrans = new ArrayList();
	// defect 10492
	int ciFdsFuncFrmtSize = 0;
	int ciInvFuncFrmtSize = 0;
	int ciLogFuncTransFrmtSize = 0;
	int ciMvFuncFrmtSize = 0;
	int ciSrFuncFrmtSize = 0;
	// defect 10492
	int ciPrmtTransFrmtSize = 0;
	// end defect 10492

	// int
	int ciTransFrmtSize = 0;
	int ciTransPymntFrmtSize = 0;
	int ciTRFdsDtlFrmtSize = 0;
	int ciTRInvDltFrmtSize = 0;
	Vector cvFundFuncFrmt = new Vector();
	Vector cvInvFuncFrmt = new Vector();
	Vector cvLogFuncFrmt = new Vector();
	Vector cvMvFuncFrmt = new Vector();
	Vector cvSendTransFileNames = new Vector();
	Vector cvSRFuncFrmt = new Vector();
	// Formats to the SendTrans files
	Vector cvTransFrmt = new Vector();
	Vector cvTransPymntFrmt = new Vector();
	Vector cvTRFDSDtlFrmt = new Vector();
	Vector cvTRInvDtlFrmt = new Vector();
	// defect 10492
	Vector cvPrmtTransFrmt = new Vector();
	// end defect 10492

	// Vector
	Vector cvUpdates = new Vector();
	//JPanel
	private JPanel ivjBldSndTransContentPane1 = null;
	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnAllFilesAdd = null;
	private RTSButton ivjbtnAllFilesRmvd = null;

	// RTSButtons
	private RTSButton ivjbtnBuild = null;
	private RTSButton ivjbtnExit = null;

	// Radio Buttons
	private JRadioButton ivjbtnLien0 = null;
	private JRadioButton ivjbtnLien1 = null;
	private JRadioButton ivjbtnLien2 = null;
	private JRadioButton ivjbtnLien3 = null;
	private JRadioButton ivjbtnPopAllFlds = null;
	private RTSButton ivjbtnRemove = null;
	private RTSButton ivjbtnUpdate = null;
	private JComboBox ivjJComboBoxFundFunc = null;
	private JComboBox ivjJComboBoxInvFunc = null;
	private JComboBox ivjJComboBoxLogFuncTrans = null;
	private JComboBox ivjJComboBoxMvFuncTrans = null;
	private JComboBox ivjJComboBoxSRFuncTrans = null;

	// ComboBox
	private JComboBox ivjJComboBoxTrans = null;
	private JComboBox ivjJComboBoxTransPymnt = null;
	private JComboBox ivjJComboBoxTRFDSDLT = null;
	private JComboBox ivjJComboBoxTRInvDTL = null;
	// defect 10492
	private JComboBox ivjJComboBoxPrmtTrans = null;
	// end defect 10492
	private JList ivjJListFiles = null;
	private JList ivjJListSelectedFiles = null;

	// JList
	private JList ivjJListVersion = null;
	private JLabel ivjlblEditFlds = null;
	private javax.swing.JLabel ivjlblRevDate = null;
	private JLabel ivjlblUpdateCnt = null;
	private javax.swing.JLabel ivjlblUpdtsNotApplied = null;
	private javax.swing.JPanel ivjpnlAddlInfo = null;
	private javax.swing.JPanel ivjpnlFldUpdt = null;
	private javax.swing.JPanel ivjpnlLiens = null;
	private JLabel ivjstclblEditFlds = null;
	private JLabel ivjstclblFiles = null;
	private JLabel ivjstclblHeader = null;
	private JLabel ivjstclblNoOfUpdts = null;

	private javax.swing.JLabel ivjstclblRevDate = null;
	private JLabel ivjstclblSelectedfields = null;
	private JLabel ivjstclblSelectedFiles = null;

	// Label
	private JLabel ivjstclblVersion = null;

	// RTSInputFields
	private RTSInputField ivjtxtEditFlds = null;
	/**
	 * instantiate class
	 */
	public BuildSendTransaction()
	{
		super();
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		Vector lvBaseTransactions = new Vector();

		// Build the SendTrans transaction set
		if (aaAE.getSource() == getbtnBuild()
			&& getJListSelectedFiles().getModel().getSize() > 0)
		{
			// SendTrans data string
			String lsHeader = CommonConstant.STR_SPACE_EMPTY;

			// Build header
			lsHeader = buildHeader();

			// Build SendTrans transaction
			if (checkSelectedFilesList(TRANS))
			{
				lvBaseTransactions.add(buildTransaction(TRANS_POS));
			}
			if (checkSelectedFilesList(MV_FUNC_TRANS))
			{
				lvBaseTransactions.add(
					buildTransaction(MV_FUNC_TRANS_POS));
			}
			if (checkSelectedFilesList(SR_FUNC_TRANS))
			{
				lvBaseTransactions.add(
					buildTransaction(SR_FUNC_TRANS_POS));
			}
			if (checkSelectedFilesList(INV_FUNC))
			{
				lvBaseTransactions.add(buildTransaction(INV_FUNC_POS));
			}
			if (checkSelectedFilesList(FUND_FUNC))
			{
				lvBaseTransactions.add(buildTransaction(FUND_FUNC_POS));
			}
			if (checkSelectedFilesList(TR_INV_DTL))
			{
				lvBaseTransactions.add(
					buildTransaction(TR_INV_DTL_POS));
			}
			if (checkSelectedFilesList(TR_FDS_DLT))
			{
				lvBaseTransactions.add(
					buildTransaction(TR_FDS_DLT_POS));
			}
			if (checkSelectedFilesList(TRANS_PYMNT))
			{
				lvBaseTransactions.add(
					buildTransaction(TRANS_PYMNT_POS));
			}
			if (checkSelectedFilesList(LOG_FUNC_TRANS))
			{
				lvBaseTransactions.add(
					buildTransaction(LOG_FUNC_TRANS_POS));
			}
			// defect 10492
			if (checkSelectedFilesList(PRMT_TRANS))
			{
				lvBaseTransactions.add(
					buildTransaction(PRMT_TRANS_POS));
			}
			// end defect 10492

			// Add Header and data to vector
			Vector lvRtn = new Vector();
			String lsConcData = CommonConstant.STR_SPACE_EMPTY;
			lvRtn.add(lsHeader);
			for (int liIndex = 0;
				liIndex < lvBaseTransactions.size();
				liIndex++)
			{
				String lsHold =
					((String) lvBaseTransactions.get(liIndex));
				lsConcData = lsConcData + lsHold;
			}
			lvRtn.add(lsConcData);

			// Write SendTrans data to file
			try
			{
				String lsFileName =
					JOptionPane.showInputDialog(
						null,
						"Enter file name for SendTrans");
				// Write data to folder
				FileUtil.writeVectorToFile(
					SystemProperty.getSendTransFileLoc()
						+ getJListMfVerCodes()
							.getSelectedValue()
							.toString()
						+ " - SendTrans\\"
						+ lsFileName
						+ TXT,
					lvRtn);

				// Empty updates
				cvUpdates = new Vector();
				getUpdateCnt().setText(CommonConstant.STR_SPACE_EMPTY);
				gettxtEditFlds().setText(
					CommonConstant.STR_SPACE_EMPTY);
				getJListSelectedFiles().setSelectedIndex(-1);
				// Empty combo boxes
				closeAllComboBoxes();
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(Log.APPLICATION, aeRTSEx, null);
			}
		}
		// Add SendTrans file to Selected file combo box
		else if (aaAE.getSource() == getbtnAdd())
		{
			addToSelectedFiles();
		}
		// Remove SendTrans file to Selected file combo box
		else if (aaAE.getSource() == getbtnRemove())
		{
			removeItemFromSelectedFiles();
		}
		// Add all SendTrans file to Selected file combo box
		else if (aaAE.getSource() == getbtnAllFilesAdd())
		{
			addAllToSelectedFiles();
		}
		// Remove all SendTrans file to Selected file combo box
		else if (aaAE.getSource() == getbtnAllFilesRmvd())
		{
			rmvdAllSelectedFiles();
		}
		else if (aaAE.getSource() == getbtnUpdate())
		{
			calcUpdtFrmts();
		}
		else if (
			aaAE.getSource() == getbtnLien0()
				|| aaAE.getSource() == getbtnLien1()
				|| aaAE.getSource() == getbtnLien2()
				|| aaAE.getSource() == getbtnLien3())
		{
			setLiens(aaAE.getSource());
		}
		else if (aaAE.getSource() == getbtnExit())
		{
			Log.write(
				Log.START_END,
				null,
				"Closing BuildSendTransaction");
			System.exit(0);
		}
	}
	/**
	 * Adjust record formats. Reset offsets to 0
	 *
	 * @param asFormat	String - Format of field
	 * @param aiTotFldsLen	int - Total length of file
	 * @return String
	 */
	private String addAdjFrmts(String asFormat, int aiTotFldsLen)
	{
		String lsRtn = CommonConstant.STR_SPACE_EMPTY;

		int liSLen = 57;
		int liELen = 59;
		int liBegSPos = 41;
		int liBegEPos = 45;
		int liEndSPos = 48;
		int liEndEPos = 52;
		int liPosLen = 4;

		// Get non-adjusted bytes
		String liPre = asFormat.substring(0, liBegSPos);
		String liDash = asFormat.substring(liBegEPos, liEndSPos);
		String liPos = asFormat.substring(liEndEPos);

		String lsSPos =
			UtilityMethods.addPadding(
				String.valueOf(
					(aiTotFldsLen
						- Integer.parseInt(
							asFormat.substring(liSLen, liELen).trim()))
						+ 1),
				liPosLen,
				CommonConstant.STR_SPACE_ONE);
		String lsEPos =
			UtilityMethods.addPaddingRight(
				String.valueOf(aiTotFldsLen),
				liPosLen,
				CommonConstant.STR_SPACE_ONE);

		lsRtn = liPre + lsSPos + liDash + lsEPos + liPos;

		return lsRtn;
	}

	/**
	* Add all SendTrans files to Select List Box 
	* Adjust Files List Box
	*/
	private void addAllToSelectedFiles()
	{
		// Add all files to Selected List Box.
		for (int liIndex = 0; liIndex < carrFiles.size(); liIndex++)
		{
			carrSelectedFiles.add(carrFiles.get(liIndex));
		}

		// Sort ArrayList
		carrSelectedFiles = sortArrayList(carrSelectedFiles);

		// Set data model
		DefaultListModel laSFListModel = new DefaultListModel();
		for (int liIndex = 0;
			liIndex < carrSelectedFiles.size();
			liIndex++)
		{
			laSFListModel.addElement(
				(String) carrSelectedFiles.get(liIndex));
		}
		getJListSelectedFiles().setModel(laSFListModel);
		// Remove select item from File list box
		// Remove/Add all files to Selected List Box.
		for (int liIndex = carrFiles.size() - 1;
			liIndex >= CommonConstant.ELEMENT_0;
			liIndex--)
		{
			carrFiles.remove(liIndex);
		}
		// Set data model
		getJListFiles().setModel(new DefaultListModel());

		// Turn on Liens if MvFuncTrans is added to Selected List
		getbtnLien0().setEnabled(true);
		getbtnLien1().setEnabled(true);
		getbtnLien2().setEnabled(true);
		getbtnLien3().setEnabled(true);
		if (!(getbtnLien0().isSelected()
			|| getbtnLien1().isSelected()
			|| getbtnLien2().isSelected()))
		{
			getbtnLien3().setSelected(true);
		}

	}
	/**
	* Populate the MF Access Methods List Box 
	*/
	private void addToSelectedFiles()
	{
		// Get select item from Files List box
		String lsSelItm = ((String) getJListFiles().getSelectedValue());
		// If the selected item from the Files combo box exist in the 
		//	Selected files combo box, remove the item.
		if (carrSelectedFiles.contains(lsSelItm))
		{
			carrSelectedFiles.remove(lsSelItm);
		}

		// Add item to array list
		carrSelectedFiles.add(lsSelItm);

		// Sort ArrayList
		carrSelectedFiles = sortArrayList(carrSelectedFiles);

		// Set data model
		DefaultListModel laSFListModel = new DefaultListModel();
		for (int liIndex = 0;
			liIndex < carrSelectedFiles.size();
			liIndex++)
		{
			laSFListModel.addElement(
				(String) carrSelectedFiles.get(liIndex));
		}
		getJListSelectedFiles().setModel(laSFListModel);
		// Remove select item from File list box
		if (carrFiles.contains(lsSelItm))
		{
			carrFiles.remove(lsSelItm);
		}
		// Set data model
		DefaultListModel laFListModel = new DefaultListModel();
		for (int liIndex = 0; liIndex < carrFiles.size(); liIndex++)
		{
			laFListModel.addElement((String) carrFiles.get(liIndex));
		}
		getJListFiles().setModel(laFListModel);

		// Turn on Liens if MvFuncTrans is added to Selected List
		if (lsSelItm.equals(MV_FUNC_TRANS))
		{
			getbtnLien0().setEnabled(true);
			getbtnLien1().setEnabled(true);
			getbtnLien2().setEnabled(true);
			getbtnLien3().setEnabled(true);
			getbtnLien3().setSelected(true);
		}
	}
	/**
	*  Return format of data file
	* 
	* @param avSendTransFile	Vector of file transactions
	* @return String
	*/
	private String addUpdates(int aiFileNo, String asBaseTransactions)
	{
		int liFileNameIndx = 0;
		String lsSendTransFile = CommonConstant.STR_SPACE_EMPTY;

		// Sort update vector (cvUpdates)
		sortUpdtVec();

		// Set data from base transaction
		lsSendTransFile = asBaseTransactions;
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			// Get the updates
			Vector lvUpdts = (Vector) cvUpdates.get(liIndex);
			// Get file name
			String lsFileName = lvUpdts.get(liFileNameIndx).toString();
			// ****************** TRANS
			if (aiFileNo == TRANS_POS && lsFileName.equals(TRANS))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
			// ****************** MV_FUNC_TRANS
			else if (
				aiFileNo == MV_FUNC_TRANS_POS
					&& lsFileName.equals(MV_FUNC_TRANS))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
			else if (
				aiFileNo == INV_FUNC_POS
					&& lsFileName.equals(INV_FUNC))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
			else if (
				aiFileNo == FUND_FUNC_POS
					&& lsFileName.equals(FUND_FUNC))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
			else if (
				aiFileNo == TR_INV_DTL_POS
					&& lsFileName.equals(TR_INV_DTL))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
			else if (
				aiFileNo == TR_FDS_DLT_POS
					&& lsFileName.equals(TR_FDS_DLT))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
			// defect 10492
			else if (
				aiFileNo == PRMT_TRANS_POS
					&& lsFileName.equals(PRMT_TRANS))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
			// end defect 10492
			else if (
				aiFileNo == TRANS_PYMNT_POS
					&& lsFileName.equals(TRANS_PYMNT))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
			if (aiFileNo == LOG_FUNC_TRANS_POS
				&& lsFileName.equals(LOG_FUNC_TRANS))
			{
				lsSendTransFile = addUpdtStr(lsSendTransFile, lvUpdts);
			}
		}

		return lsSendTransFile;
	}

	/**
	 * Add update to SendTrans 
	 *
	 * @param asSendTransFile	String - Base file format
	 * @param avUpdts			Vector - Updates
	 * @return String
	 */
	private String addUpdtStr(String asSendTransFile, Vector avUpdts)
	{
		int liDataIndx = 2;
		int liSPosIndx = 3;
		int liEposIndx = 4;
		int liCharType = 5;
		int liLength = 6;

		// Base file
		String lsSendTransFile = asSendTransFile;
		// Update
		Vector lvUpdts = avUpdts;

		// Get pre-data
		String lsPre =
			lsSendTransFile.substring(
				0,
				Integer.parseInt(
					lvUpdts.get(liSPosIndx).toString().trim())
					- 1);
		String lsPost =
			lsSendTransFile.substring(
				Integer.parseInt(
					lvUpdts.get(liEposIndx).toString().trim()));
		// Add update to base record, by char type
		if (lvUpdts.get(liCharType).toString().equals("A"))
		{
			lsSendTransFile =
				lsPre
					+ UtilityMethods.addPaddingRight(
						lvUpdts.get(liDataIndx).toString(),
						Integer.parseInt(
							lvUpdts.get(liLength).toString().trim()),
						CommonConstant.STR_SPACE_ONE)
					+ lsPost;

		}
		else
		{
			lsSendTransFile =
				lsPre
					+ UtilityMethods.addPadding(
						lvUpdts.get(liDataIndx).toString(),
						Integer.parseInt(
							lvUpdts.get(liLength).toString().trim()),
						CommonConstant.STR_ZERO)
					+ lsPost;
		}

		return lsSendTransFile;
	}
	/**
	 * Add Lien holder data
	 * 
	 * @param avData	Vector
	 * @return vector
	 */
	private Vector applyLienData(Vector avData)
	{
		int liSPosDataSet1 = 0;
		int liSPosDataSet2 = 0;
		// Set start-end position for lien data
		if ((getJListMfVerCodes().getSelectedValue().toString())
			.equals(T_VER))
		{
			liSPosDataSet1 = 868;
			liSPosDataSet2 = 1411;
		}
		else if (
			(
				getJListMfVerCodes()
					.getSelectedValue()
					.toString())
					.equals(
				U_VER))
		{
			liSPosDataSet1 = 868;
			liSPosDataSet2 = 1411;
		}
		else if (
			(
				getJListMfVerCodes()
					.getSelectedValue()
					.toString())
					.equals(
				V_VER))
		{
			liSPosDataSet1 = 870;
			liSPosDataSet2 = 1413;
		}

		String lsLiens = CommonConstant.STR_SPACE_EMPTY;
		Vector lvData = new Vector();
		String lsMvFunc = (String) avData.get(0);

		// Get pre/post lien data
		String lsDataSet1 = lsMvFunc.substring(0, liSPosDataSet1);
		String lsDataSet2 = lsMvFunc.substring(liSPosDataSet2);

		// How many liens
		int liNoOfLiens = numOfLiens();

		// Get lien data from file
		Vector lvLien =
			FileUtil.copyFileToVector(
				SystemProperty.getSendTransFileLoc()
					+ TRANSACTION_FILES
					+ getJListMfVerCodes().getSelectedValue().toString()
					+ SENDTRANS_BASE_LOC
					+ LIEN
					+ TXT);
		String lsLien = (String) lvLien.get(0);
		// Get Empty lien data from file
		Vector lvEmptyLien =
			FileUtil.copyFileToVector(
				SystemProperty.getSendTransFileLoc()
					+ TRANSACTION_FILES
					+ getJListMfVerCodes().getSelectedValue().toString()
					+ SENDTRANS_BASE_LOC
					+ EMPTY_LIEN
					+ TXT);
		String lsEmptyLien = (String) lvEmptyLien.get(0);

		// Build liens
		//	Start with build the liens and determine number of empty 
		//	spaces.
		switch (liNoOfLiens)
		{
			case 0 : // No liens
				{
					lsLiens = lsEmptyLien + lsEmptyLien + lsEmptyLien;
					break;
				}
			case 1 : // 1 lien
				{
					lsLiens = lsLien + lsEmptyLien + lsEmptyLien;
					break;
				}
			case 2 : // 2 Liens
				{
					lsLiens = lsLien + lsLien + lsEmptyLien;
					break;
				}
			case 3 : // 3 Liens
				{
					lsLiens = lsLien + lsLien + lsLien;
					break;
				}
		}

		// Cancatinate data
		String lsData = lsDataSet1 + lsLiens + lsDataSet2;
		lvData.add(lsData);

		return lvData;
	}

	/**
	 * Build header - file counts
	 * 
	 * @param aiFileNo
	 * @return
	 */
	private String buildHeader()
	{
		String lsHeader = CommonConstant.STR_SPACE_EMPTY;
		String lsRec_Cnt = "01";

		Vector lvRtnData =
			FileUtil.copyFileToVector(
				SystemProperty.getSendTransFileLoc()
					+ TRANSACTION_FILES
					+ getJListMfVerCodes().getSelectedValue().toString()
					+ SENDTRANS_BASE_LOC
					+ HEADER_FILE
					+ TXT);
		// Get header data
		lsHeader = (String) lvRtnData.get(0);

		// Build SendTrans transaction
		if (checkSelectedFilesList(TRANS))
		{
			String lsHold = lsHeader.substring(10);
			String lsUpdt = lsHeader.substring(0, 8);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		if (checkSelectedFilesList(MV_FUNC_TRANS))
		{
			String lsHold = lsHeader.substring(20);
			String lsUpdt = lsHeader.substring(0, 18);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		if (checkSelectedFilesList(SR_FUNC_TRANS))
		{
			String lsHold = lsHeader.substring(30);
			String lsUpdt = lsHeader.substring(0, 28);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		if (checkSelectedFilesList(INV_FUNC))
		{
			String lsHold = lsHeader.substring(40);
			String lsUpdt = lsHeader.substring(0, 38);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		if (checkSelectedFilesList(FUND_FUNC))
		{
			String lsHold = lsHeader.substring(50);
			String lsUpdt = lsHeader.substring(0, 48);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		if (checkSelectedFilesList(TR_INV_DTL))
		{
			String lsHold = lsHeader.substring(60);
			String lsUpdt = lsHeader.substring(0, 58);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		if (checkSelectedFilesList(TR_FDS_DLT))
		{
			String lsHold = lsHeader.substring(70);
			String lsUpdt = lsHeader.substring(0, 68);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		// defect 10492
		if (checkSelectedFilesList(PRMT_TRANS))
		{
			String lsHold = lsHeader.substring(80);
			String lsUpdt = lsHeader.substring(0, 78);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		// end defect 10492
		if (checkSelectedFilesList(TRANS_PYMNT))
		{
			String lsHold = lsHeader.substring(90);
			String lsUpdt = lsHeader.substring(0, 88);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt + lsHold;
		}
		if (checkSelectedFilesList(LOG_FUNC_TRANS))
		{
			String lsUpdt = lsHeader.substring(0, 98);
			lsUpdt = lsUpdt + lsRec_Cnt;
			lsHeader = lsUpdt;
		}

		return lsHeader;
	}
	/**
	 * Get bases files
	 * 
	 * @param aiFileNo		File number
	 * @return String
	 */
	private String buildTransaction(int aiFileNo)
	{
		Vector lvFileData = new Vector();
		String lsRtnData = CommonConstant.STR_SPACE_EMPTY;

		switch (aiFileNo)
		{
			case (TRANS_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(TRANS);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Add updtaes for TRANS
					lsRtnData =
						addUpdates(
							TRANS_POS,
							(String) lvFileData.get(0));
					break;
				}
			case (MV_FUNC_TRANS_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(MV_FUNC_TRANS);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Add Lien data
					lvFileData = applyLienData(lvFileData);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							MV_FUNC_TRANS_POS,
							(String) lvFileData.get(0));
					break;
				}
			case (SR_FUNC_TRANS_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(SR_FUNC_TRANS);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							SR_FUNC_TRANS_POS,
							(String) lvFileData.get(0));
					break;
				}
			case (INV_FUNC_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(INV_FUNC);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							INV_FUNC_POS,
							(String) lvFileData.get(0));
					break;
				}
			case (FUND_FUNC_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(FUND_FUNC);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							FUND_FUNC_POS,
							(String) lvFileData.get(0));
					break;
				}
			case (TR_INV_DTL_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(TR_INV_DTL);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							TR_INV_DTL_POS,
							(String) lvFileData.get(0));
					break;
				}
			case (TR_FDS_DLT_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(TR_FDS_DLT);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							TR_FDS_DLT_POS,
							(String) lvFileData.get(0));
					break;
				}
				// defect 10492
			case (PRMT_TRANS_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(PRMT_TRANS);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							PRMT_TRANS_POS,
							(String) lvFileData.get(0));
					break;
				}
				// end defect 10492
			case (TRANS_PYMNT_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(TRANS_PYMNT);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							TRANS_PYMNT_POS,
							(String) lvFileData.get(0));
					break;
				}
			case (LOG_FUNC_TRANS_POS) :
				{
					// Set file name
					String lsFileName = rtnBaseFileName(LOG_FUNC_TRANS);
					// Get base record data
					lvFileData = rtnData(lsFileName);

					// Copy record to string.
					lsRtnData =
						addUpdates(
							LOG_FUNC_TRANS_POS,
							(String) lvFileData.get(0));
					break;
				}
		}

		return lsRtnData;
	}
	/**
	 * Build update vector
	 * 
	 * 	0 >	Selected File
	 *  1 >	Field Name
	 *  2 >	Start Position
	 *  3 >	End Popsition
	 *  4 > Character Type
	 *  5 >	Field Length
	 *
	 */
	private void calcUpdtFrmts()
	{
		Vector lvData = new Vector();
		// Get data from input field
		String lsData = gettxtEditFlds().getText().toString();
		String lsFormat = rtnFrmtStr();
		// Build vector data
		lvData.add(
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.trim());
		// Field Name
		lvData.add(lsFormat.substring(4, 20));
		// Input data
		lvData.add(lsData);
		// Start Position
		lvData.add(lsFormat.substring(41, 45));
		// End Position
		lvData.add(lsFormat.substring(48, 52));
		// Character type
		lvData.add(lsFormat.substring(55, 56));
		// Field length 
		lvData.add(lsFormat.substring(57, 59));
		cvUpdates.add(lvData);
		getUpdateCnt().setText(String.valueOf(cvUpdates.size()));
	}

	/**
	 * Check if one of the 8 SendTrans files exist in the selected 
	 * file list.
	 *  
	 * @param asData
	 * @return boolean
	 */
	private boolean checkSelectedFilesList(String asData)
	{
		boolean lbRtn = false;

		// Check for select file in selected file list
		for (int liIndex = 0;
			liIndex < carrSelectedFiles.size();
			liIndex++)
		{
			String lsSelFiles = (String) carrSelectedFiles.get(liIndex);
			// Add Trans
			if (lsSelFiles.equalsIgnoreCase(asData))
			{
				lbRtn = true;
				break;
			}
		}

		return lbRtn;
	}
	/**
	 * Set visible to false for all combo boxes
	 * 
	 */
	private void closeAllComboBoxes()
	{
		getJComboBoxTrans().setVisible(false);
		getJComboBoxMvFuncTrans().setVisible(false);
		getJComboBoxSRFuncTrans().setVisible(false);
		getJComboBoxInvFunc().setVisible(false);
		getJComboBoxFundFunc().setVisible(false);
		getJComboBoxTRInvDTL().setVisible(false);
		getJComboBoxTRFDSDLT().setVisible(false);
		getJComboBoxTransPymnt().setVisible(false);
		getJComboBoxLogFuncTrans().setVisible(false);
		// defect 10492
		getJComboBoxPrmtTrans().setVisible(false);
	}
	/**
	* Find index of string in vector
	* 
	* @param avrecs	Vector of Records
	* @param asData	Search string
	* @param aiSPos	Start position for search string
	* @param aiEPos	End position for search string
	* @return int
	*/
	private int findIndexOfStrInVec(
		Vector avRecs,
		String asFldName,
		int aiSPos,
		int aiEPos)
	{
		int liRtn = 0;
		for (int liIndex = 0; liIndex < avRecs.size(); liIndex++)
		{
			if (avRecs
				.get(liIndex)
				.toString()
				.substring(aiSPos, aiEPos)
				.trim()
				.equals(asFldName))
			{
				liRtn = liIndex;
				break;
			}
		}

		return liRtn;
	}
	/**
	* Return the MfSendTransContentPane1 property value.
	* 
	* @return JPanel
	*/
	private javax.swing.JPanel getBldSndTransContentPane1()
	{
		if (ivjBldSndTransContentPane1 == null)
		{
			try
			{
				ivjBldSndTransContentPane1 = new javax.swing.JPanel();
				ivjBldSndTransContentPane1.setName(
					"BldSndTransContentPane1");
				ivjBldSndTransContentPane1.setLayout(null);
				ivjBldSndTransContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjBldSndTransContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjBldSndTransContentPane1.add(
					getJListMfVerCodes(),
					getJListMfVerCodes().getName());
				ivjBldSndTransContentPane1.add(
					getJListFiles(),
					getJListFiles().getName());
				ivjBldSndTransContentPane1.add(
					getstclblVersion(),
					getstclblVersion().getName());
				ivjBldSndTransContentPane1.add(
					getstclblFiles(),
					getstclblFiles().getName());
				ivjBldSndTransContentPane1.add(
					getbtnBuild(),
					getbtnBuild().getName());
				ivjBldSndTransContentPane1.add(
					getbtnExit(),
					getbtnExit().getName());
				ivjBldSndTransContentPane1.add(
					getstclblHeader(),
					getstclblHeader().getName());
				ivjBldSndTransContentPane1.add(
					getJComboBoxMvFuncTrans(),
					getJComboBoxMvFuncTrans().getName());
				ivjBldSndTransContentPane1.add(
					getJComboBoxTRInvDTL(),
					getJComboBoxTRInvDTL().getName());
				ivjBldSndTransContentPane1.add(
					getJComboBoxTRFDSDLT(),
					getJComboBoxTRFDSDLT().getName());
				ivjBldSndTransContentPane1.add(
					getJComboBoxLogFuncTrans(),
					getJComboBoxLogFuncTrans().getName());
				// defect 10492
				ivjBldSndTransContentPane1.add(
					getJComboBoxPrmtTrans(),
					getJComboBoxPrmtTrans().getName());
				// end defect 10492
				ivjBldSndTransContentPane1.add(
					getJComboBoxTransPymnt(),
					getJComboBoxTransPymnt().getName());
				ivjBldSndTransContentPane1.add(
					getJComboBoxSRFuncTrans(),
					getJComboBoxSRFuncTrans().getName());
				ivjBldSndTransContentPane1.add(
					getJComboBoxFundFunc(),
					getJComboBoxFundFunc().getName());
				ivjBldSndTransContentPane1.add(
					getstclblSelectedFiles(),
					getstclblSelectedFiles().getName());
				ivjBldSndTransContentPane1.add(
					getJListSelectedFiles(),
					getJListSelectedFiles().getName());
				ivjBldSndTransContentPane1.add(
					getstclblSelectFields(),
					getstclblSelectFields().getName());
				ivjBldSndTransContentPane1.add(
					getbtnAdd(),
					getbtnAdd().getName());
				ivjBldSndTransContentPane1.add(
					getbtnRemove(),
					getbtnRemove().getName());
				ivjBldSndTransContentPane1.add(
					getbtnAllFilesAdd(),
					getbtnAllFilesAdd().getName());
				ivjBldSndTransContentPane1.add(
					getbtnAllFilesRmvd(),
					getbtnAllFilesRmvd().getName());
				ivjBldSndTransContentPane1.add(
					getJComboBoxInvFunc(),
					getJComboBoxInvFunc().getName());
				ivjBldSndTransContentPane1.add(
					getJComboBoxTrans(),
					getJComboBoxTrans().getName());
				ivjBldSndTransContentPane1.add(
					getpnlAddlInfo(),
					getpnlAddlInfo().getName());
				ivjBldSndTransContentPane1.add(
					getbtnUpdate(),
					getbtnUpdate().getName());
				ivjBldSndTransContentPane1.add(getRevDate(), null);
				ivjBldSndTransContentPane1.add(
					getlblRevDate(),
					getlblRevDate().getName());
				ivjBldSndTransContentPane1.add(getpnlFldUpdt(), null);
				ivjBldSndTransContentPane1.add(ivjbtnExit, null);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBldSndTransContentPane1;
	}

	/**
	 * This method initializes ivjbtnAdd
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnAdd()
	{
		try
		{

			if (ivjbtnAdd == null)
			{
				ivjbtnAdd = new RTSButton();
				ivjbtnAdd.setBounds(254, 85, 93, 20);
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setText("Add >>");
				ivjbtnAdd.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjbtnAdd;
	}
	/**
	 * This method initializes ivjbtnAllFilesAdd
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnAllFilesAdd()
	{
		try
		{

			if (ivjbtnAllFilesAdd == null)
			{
				ivjbtnAllFilesAdd = new RTSButton();
				ivjbtnAllFilesAdd.setBounds(254, 140, 94, 20);
				ivjbtnAllFilesAdd.setName("btnAllFilesAdd");
				ivjbtnAllFilesAdd.setText("All Files>>");
				ivjbtnAllFilesAdd.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjbtnAllFilesAdd;
	}
	/**
	 * This method initializes ivjbtnAllFilesRmvd
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnAllFilesRmvd()
	{
		try
		{

			if (ivjbtnAllFilesRmvd == null)
			{
				ivjbtnAllFilesRmvd = new RTSButton();
				ivjbtnAllFilesRmvd.setBounds(255, 167, 93, 20);
				ivjbtnAllFilesRmvd.setName("btnAllFileRmvd");
				ivjbtnAllFilesRmvd.setText("<<All Files");
				ivjbtnAllFilesRmvd.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjbtnAllFilesRmvd;
	}

	/**
	 * Return the Build property value.
	 * 
	 * @return RTSButton
	 /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnBuild()
	{
		if (ivjbtnBuild == null)
		{
			try
			{
				ivjbtnBuild =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnBuild.setBounds(313, 432, 85, 24);
				ivjbtnBuild.setName("btnBuild");
				ivjbtnBuild.setText("Build");
				ivjbtnBuild.addActionListener(this);
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
	 * Return the Exit property value.
	 * 
	 * @return RTSButton
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnExit()
	{
		if (ivjbtnExit == null)
		{
			try
			{
				ivjbtnExit =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnExit.setBounds(405, 432, 85, 24);
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
	 * This method initializes ivjbtnLien0 Lien
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getbtnLien0()
	{
		try
		{

			if (ivjbtnLien0 == null)
			{
				ivjbtnLien0 = new javax.swing.JRadioButton();
				ivjbtnLien0.setBounds(5, 24, 35, 20);
				ivjbtnLien0.setText("0");
				ivjbtnLien0.setEnabled(false);
				ivjbtnLien0.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjbtnLien0;
	}
	/**
	 * This method initializes ivjbtnLien1 Lien
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getbtnLien1()
	{
		try
		{

			if (ivjbtnLien1 == null)
			{
				ivjbtnLien1 = new javax.swing.JRadioButton();
				ivjbtnLien1.setBounds(40, 24, 35, 20);
				ivjbtnLien1.setText("1");
				ivjbtnLien1.setEnabled(false);
				ivjbtnLien1.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjbtnLien1;
	}
	/**
	 * This method initializes ivjbtnLien2 Lien
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getbtnLien2()
	{
		try
		{

			if (ivjbtnLien2 == null)
			{
				ivjbtnLien2 = new javax.swing.JRadioButton();
				ivjbtnLien2.setBounds(75, 24, 35, 20);
				ivjbtnLien2.setText("2");
				ivjbtnLien2.setEnabled(false);
				ivjbtnLien2.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjbtnLien2;
	}
	/**
	 * This method initializes ivjbtnLien3 Lien
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getbtnLien3()
	{
		try
		{

			if (ivjbtnLien3 == null)
			{
				ivjbtnLien3 = new javax.swing.JRadioButton();
				ivjbtnLien3.setBounds(110, 24, 35, 20);
				ivjbtnLien3.setText("3");
				ivjbtnLien3.setEnabled(false);
				ivjbtnLien3.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjbtnLien3;
	}
	/**
	 * This method initializes ivjbtnPopAllFlds
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getbtnPopAllFlds()
	{
		try
		{
			if (ivjbtnPopAllFlds == null)
			{
				ivjbtnPopAllFlds = new javax.swing.JRadioButton();
				ivjbtnPopAllFlds.setBounds(11, 76, 142, 21);
				ivjbtnPopAllFlds.setText(POPULATE_ALL_FLDS);
				ivjbtnPopAllFlds.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}

		return ivjbtnPopAllFlds;
	}
	/**
	 * This method initializes ivjbtnRemove
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnRemove()
	{
		try
		{

			if (ivjbtnRemove == null)
			{
				ivjbtnRemove = new RTSButton();
				ivjbtnRemove.setBounds(254, 107, 94, 20);
				ivjbtnRemove.setName("btnRemove");
				ivjbtnRemove.setText("<<Remove");
				ivjbtnRemove.addActionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjbtnRemove;
	}

	/**
	 * Return the Build property value.
	 * 
	 * @return RTSButton
	 /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSButton getbtnUpdate()
	{
		if (ivjbtnUpdate == null)
		{
			try
			{
				ivjbtnUpdate =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnUpdate.setBounds(230, 432, 75, 24);
				ivjbtnUpdate.setName("btnUpdate");
				ivjbtnUpdate.setText("Update");
				ivjbtnUpdate.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnUpdate;
	}

	/**
	 * Get formats for each of the SendTrans files
	 * Accumulate total bytes for each file.
	 *
	 * @param asMfFrmnt	String
	 */
	private void getFormats(String asMfFrmnt)
	{
		Vector lvRec = new Vector();
		Vector lvAdjFrmts = new Vector();
		int liFPosBeg = 57;
		int liFPosEnd = 59;

		// Get SendTrans Format files. Load to vector
		lvRec =
			FileUtil.copyFileToVector(
				SystemProperty.getSendTransFileLoc()
					+ TRANSACTION_FILES
					+ getJListMfVerCodes().getSelectedValue().toString()
					+ SENDTRANS_BASE_LOC
					+ asMfFrmnt
					+ TXT);

		// Remove Header and blank lines
		for (int liIndex = 3; liIndex < lvRec.size(); liIndex++)
		{
			if (((String) lvRec.get(3)).length() > 0)
			{
				lvAdjFrmts.add(lvRec.get(liIndex).toString().trim());
			}
		}

		// Get Formats
		for (int liIndex = 0; liIndex < lvAdjFrmts.size(); liIndex++)
		{

			// Trans
			if (lvAdjFrmts
				.get(liIndex)
				.toString()
				.substring(4)
				.trim()
				.equals(RTS_TRANS))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_TRANS
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvTransFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						// Accumulate the byte count for file.
						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciTransFrmtSize =
								ciTransFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							TRANS
								+ ": "
								+ String.valueOf(ciTransFrmtSize));
						break;
					}
				}
			} // MV_FUNC_Trans
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_MV_FUNC_TRANS))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_MV_FUNC_TRANS
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						if (lvAdjFrmts
							.get(liIndx1)
							.toString()
							.substring(3, 11)
							.equals("LIENDATA"))
						{
							cvMvFuncFrmt.add(
								lvAdjFrmts.get(liIndx1).toString()
									+ UtilityMethods.addPaddingRight(
										CommonConstant.STR_SPACE_ONE,
										liFPosBeg
											- lvAdjFrmts
												.get(liIndx1)
												.toString()
												.length(),
										CommonConstant.STR_SPACE_ONE)
									+ "LIENDATA");
						}
						else
						{
							// Accumulate total byte count of file
							liTotFldsLen =
								liTotFldsLen
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
							// Add adjusted ADABAS format line to vector
							cvMvFuncFrmt.add(
								addAdjFrmts(
									lvAdjFrmts.get(liIndx1).toString(),
									liTotFldsLen));

							// Accumulate the byte count for file.
							if (UtilityMethods
								.isNumeric(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim()))
							{
								ciMvFuncFrmtSize =
									ciMvFuncFrmtSize
										+ Integer.parseInt(
											lvAdjFrmts
												.get(liIndx1)
												.toString()
												.substring(
													liFPosBeg,
													liFPosEnd)
												.trim());
							}
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							MV_FUNC_TRANS
								+ ": "
								+ String.valueOf(ciMvFuncFrmtSize));
						break;
					}
				}
			} // SR_FUNC_Trans
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_SR_FUNC_TRANS))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_SR_FUNC_TRANS
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvSRFuncFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						// Accumulate the byte count for file.
						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciSrFuncFrmtSize =
								ciSrFuncFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							SR_FUNC_TRANS
								+ ": "
								+ String.valueOf(ciSrFuncFrmtSize));
						break;
					}
				}
			} // INV_FUNC_TRANS
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_INV_FUNC_TRANS))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_INV_FUNC_TRANS
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvInvFuncFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						// Accumulate the byte count for file.
						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciInvFuncFrmtSize =
								ciInvFuncFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							INV_FUNC
								+ ": "
								+ String.valueOf(ciInvFuncFrmtSize));
						break;
					}
				}
			} // FUND_FUNC_TRANS
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_FUND_FUNC_TRNS))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_FUND_FUNC_TRNS
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvFundFuncFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						// Accumulate the byte count for file.
						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciFdsFuncFrmtSize =
								ciFdsFuncFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							FUND_FUNC
								+ ": "
								+ String.valueOf(ciFdsFuncFrmtSize));
						break;
					}
				}
			} // TR_INV_DETAIL
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_TR_INV_DETAIL))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_TR_INV_DETAIL
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvTRInvDtlFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						// Accumulate the byte count for file.
						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciTRInvDltFrmtSize =
								ciTRInvDltFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							TR_INV_DTL
								+ ": "
								+ String.valueOf(ciTRInvDltFrmtSize));
						break;
					}
				}
			} // TR_FDS_DETAIL
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_TR_FDS_DETAIL))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_TR_FDS_DETAIL
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvTRFDSDtlFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						// Accumulate the byte count for file.
						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciTRFdsDtlFrmtSize =
								ciTRFdsDtlFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							TR_FDS_DLT
								+ ": "
								+ String.valueOf(ciTRFdsDtlFrmtSize));
						break;
					}
				}
			}
			// defect 10492
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_PRMT_TRANS))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_PRMT_TRANS
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvPrmtTransFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						// Accumulate the byte count for file.
						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciPrmtTransFrmtSize =
								ciPrmtTransFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							PRMT_TRANS
								+ ": "
								+ String.valueOf(ciPrmtTransFrmtSize));
						break;
					}
				}
				// end defect 10492
			} // Trans_PYMNT
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_TRANS_PAYMENT))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_TRANS_PAYMENT
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvTransPymntFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciTransPymntFrmtSize =
								ciTransPymntFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						System.out.println(
							TRANS_PYMNT
								+ ": "
								+ String.valueOf(ciTransPymntFrmtSize));
						break;
					}
				}
			} // LOG_FUNC_Trans
			else if (
				lvAdjFrmts
					.get(liIndex)
					.toString()
					.substring(4)
					.trim()
					.equals(
					RTS_LOG_FUNC_TRANS))
			{
				int liTotFldsLen = 0;
				// Get data for RTS_LOG_FUNC_TRANS
				for (int liIndx1 = liIndex + 1;
					liIndx1 < lvAdjFrmts.size();
					liIndx1++)
				{
					if (!lvAdjFrmts
						.get(liIndx1)
						.toString()
						.substring(0, 2)
						.equals("01"))
					{
						// Accumulate total byte count of file
						liTotFldsLen =
							liTotFldsLen
								+ Integer.parseInt(
									lvAdjFrmts
										.get(liIndx1)
										.toString()
										.substring(liFPosBeg, liFPosEnd)
										.trim());
						// Add adjusted ADABAS format line to vector
						cvLogFuncFrmt.add(
							addAdjFrmts(
								lvAdjFrmts.get(liIndx1).toString(),
								liTotFldsLen));

						liIndex = liIndx1 + 1;
						// Accumulate the byte count for file.
						if (UtilityMethods
							.isNumeric(
								lvAdjFrmts
									.get(liIndx1)
									.toString()
									.substring(liFPosBeg, liFPosEnd)
									.trim()))
						{
							ciLogFuncTransFrmtSize =
								ciLogFuncTransFrmtSize
									+ Integer.parseInt(
										lvAdjFrmts
											.get(liIndx1)
											.toString()
											.substring(
												liFPosBeg,
												liFPosEnd)
											.trim());
						}
					}
					else
					{
						liIndex = liIndx1 - 1;
						break;
					}
				}
				System.out.println(
					LOG_FUNC_TRANS
						+ ": "
						+ String.valueOf(ciLogFuncTransFrmtSize));

			}
		}
	}
	/**
	 * This method initializes JComboBox FundFunc
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBoxFundFunc()
	{
		try
		{

			if (ivjJComboBoxFundFunc == null)
			{
				ivjJComboBoxFundFunc = new JComboBox();
				ivjJComboBoxFundFunc.setBounds(496, 76, 231, 23);
				ivjJComboBoxFundFunc.setName("JComboBoxFundFunc");
				ivjJComboBoxFundFunc.addItemListener(this);
				// user code begin 
				ivjJComboBoxFundFunc.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxFundFunc;
	}
	/**
	 * This method initializes JComboBox FundFunc
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBoxInvFunc()
	{
		try
		{

			if (ivjJComboBoxInvFunc == null)
			{
				ivjJComboBoxInvFunc = new JComboBox();
				ivjJComboBoxInvFunc.setBounds(497, 76, 231, 23);
				ivjJComboBoxInvFunc.setName("JComboBoxInvFunc");
				ivjJComboBoxInvFunc.addItemListener(this);
				// user code begin 
				ivjJComboBoxInvFunc.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxInvFunc;
	}
	/**
	 * This method initializes JComboBox LogFunTrans
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBoxLogFuncTrans()
	{
		try
		{

			if (ivjJComboBoxLogFuncTrans == null)
			{
				ivjJComboBoxLogFuncTrans = new JComboBox();
				ivjJComboBoxLogFuncTrans.setBounds(496, 76, 231, 23);
				ivjJComboBoxLogFuncTrans.setName(
					"JComboBoxLogFuncTrans");
				ivjJComboBoxLogFuncTrans.addItemListener(this);
				// user code begin
				ivjJComboBoxLogFuncTrans.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxLogFuncTrans;
	}
	/**
	 * This method initializes JComboBox MvFuncTrans
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBoxMvFuncTrans()
	{
		try
		{

			if (ivjJComboBoxMvFuncTrans == null)
			{
				ivjJComboBoxMvFuncTrans = new JComboBox();
				ivjJComboBoxMvFuncTrans.setBounds(496, 76, 231, 23);
				ivjJComboBoxMvFuncTrans.setName("JComboBoxMvFuncTrans");
				ivjJComboBoxMvFuncTrans.addItemListener(this);
				// user code begin 
				ivjJComboBoxMvFuncTrans.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxMvFuncTrans;
	}
	/**
	 * This method initializes JComboBox SRFuncTrans
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBoxSRFuncTrans()
	{
		try
		{

			if (ivjJComboBoxSRFuncTrans == null)
			{
				ivjJComboBoxSRFuncTrans = new JComboBox();
				ivjJComboBoxSRFuncTrans.setBounds(496, 76, 231, 23);
				ivjJComboBoxSRFuncTrans.setName("JComboBoxSRFuncTrans");
				ivjJComboBoxSRFuncTrans.addItemListener(this);
				// user code begin 
				ivjJComboBoxSRFuncTrans.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxSRFuncTrans;
	}
	/**
	 * This method initializes JComboBox Trans
	 * 
	 * @return JComboBox
	*/
	private JComboBox getJComboBoxTrans()
	{
		try
		{

			if (ivjJComboBoxTrans == null)
			{
				ivjJComboBoxTrans = new JComboBox();
				ivjJComboBoxTrans.setBounds(496, 76, 231, 23);
				ivjJComboBoxTrans.setName("JComboBoxTrans");
				// user code begin 
				ivjJComboBoxTrans.setMaximumRowCount(MAX_FIELDS);
				ivjJComboBoxTrans.addItemListener(this);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxTrans;
	}
	/**
	 * This method initializes JComboBox TransPymnt
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBoxTransPymnt()
	{
		try
		{

			if (ivjJComboBoxTransPymnt == null)
			{
				ivjJComboBoxTransPymnt = new JComboBox();
				ivjJComboBoxTransPymnt.setBounds(496, 76, 231, 23);
				ivjJComboBoxTransPymnt.setName("JComboBoxTransPymnt");
				ivjJComboBoxTransPymnt.addItemListener(this);
				// user code begin 
				ivjJComboBoxTransPymnt.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxTransPymnt;
	}
	/**
	 * This method initializes JComboBox TRFDSDLT
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBoxTRFDSDLT()
	{
		try
		{

			if (ivjJComboBoxTRFDSDLT == null)
			{
				ivjJComboBoxTRFDSDLT = new JComboBox();
				ivjJComboBoxTRFDSDLT.setBounds(496, 76, 231, 23);
				ivjJComboBoxTRFDSDLT.setName("JComboBoxTRFDSDLT");
				ivjJComboBoxTRFDSDLT.addItemListener(this);
				// user code begin 
				ivjJComboBoxTRFDSDLT.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxTRFDSDLT;
	}
	/**
	 * This method initializes JComboBox TRInvDTL
	 * 
	 * @return JComboBox
	 */
	private JComboBox getJComboBoxTRInvDTL()
	{
		try
		{

			if (ivjJComboBoxTRInvDTL == null)
			{
				ivjJComboBoxTRInvDTL = new JComboBox();
				ivjJComboBoxTRInvDTL.setBounds(496, 76, 231, 23);
				ivjJComboBoxTRInvDTL.setName("JComboBoxTRInvDTL");
				ivjJComboBoxTRInvDTL.addItemListener(this);
				// user code begin 
				ivjJComboBoxTRInvDTL.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxTRInvDTL;
	}
	// defect 10492
	/**
		 * This method initializes JComboBox PrmtTrans
		 * 
		 * @return JComboBox
		 */
	private JComboBox getJComboBoxPrmtTrans()
	{
		try
		{

			if (ivjJComboBoxPrmtTrans == null)
			{
				ivjJComboBoxPrmtTrans = new JComboBox();
				ivjJComboBoxPrmtTrans.setBounds(496, 76, 231, 23);
				ivjJComboBoxPrmtTrans.setName("JComboBoxPrmtTrans");
				ivjJComboBoxPrmtTrans.addItemListener(this);
				// user code begin 
				ivjJComboBoxPrmtTrans.setMaximumRowCount(MAX_FIELDS);
				// user code end
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJComboBoxPrmtTrans;
	}
	// end 10492

	/**
	* This method initializes JComboBox
	* 
	* @return JComboBox
	*/
	private JList getJListFiles()
	{
		try
		{

			if (ivjJListFiles == null)
			{
				ivjJListFiles = new JList();
				ivjJListFiles.setName("JListFiles");
				ivjJListFiles.setBounds(116, 76, 132, 181);
				ivjJListFiles.setSelectedIndex(0);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJListFiles;
	}
	/**
	* This method initializes JComboBox
	* 
	* @return JComboBox
	*/
	private JList getJListMfVerCodes()
	{
		try
		{

			if (ivjJListVersion == null)
			{
				ivjJListVersion = new JList();
				ivjJListVersion.setName("JListFiles");
				ivjJListVersion.setBounds(6, 76, 102, 50);
				ivjJListVersion.addListSelectionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJListVersion;
	}
	/**
	 * This method initializes JListBox
	 * 
	 * @return JComboBox
	 */
	private JList getJListSelectedFiles()
	{
		try
		{
			if (ivjJListSelectedFiles == null)
			{
				ivjJListSelectedFiles = new JList();
				ivjJListSelectedFiles.setBounds(352, 76, 139, 180);
				ivjJListSelectedFiles.setName("JListSelectedFiles");
				ivjJListSelectedFiles.setSelectedIndex(0);
				ivjJListSelectedFiles.addListSelectionListener(this);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjJListSelectedFiles;
	}
	/**
	 * This method initializes ivjlblUpdateCnt Edit Fields
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblEditFlds()
	{
		try
		{
			if (ivjlblEditFlds == null)
			{
				ivjlblEditFlds = new javax.swing.JLabel();
				ivjlblEditFlds.setBounds(78, 16, 207, 16);
				ivjlblEditFlds.setName("stclblEditFlds");
				ivjlblEditFlds.setText("None");
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjlblEditFlds;
	}
	/**
	 * This method initializes ivjlblRevDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblRevDate()
	{
		try
		{
			if (ivjlblRevDate == null)
			{
				ivjlblRevDate = new javax.swing.JLabel();
				ivjlblRevDate.setBounds(75, 450, 70, 15);
				ivjlblRevDate.setText(CommonConstant.STR_SPACE_EMPTY);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjlblRevDate;
	}
	/**
	 * This method initializes ivjpnlFldUpdt
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getpnlAddlInfo()
	{
		if (ivjpnlAddlInfo == null)
		{
			ivjpnlAddlInfo = new javax.swing.JPanel();
			ivjpnlAddlInfo.setLayout(null);
			ivjpnlAddlInfo.add(getpnlLiens(), null);
			ivjpnlAddlInfo.add(getbtnPopAllFlds(), null);
			ivjpnlAddlInfo.setBounds(18, 265, 163, 174);
			ivjpnlAddlInfo.setBorder(
				new javax.swing.border.TitledBorder(
					new javax.swing.border.EtchedBorder(),
					ADDL_SETUP));
		}
		return ivjpnlAddlInfo;
	}

	/**
	 * This method initializes ivjpnlFldUpdt
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getpnlFldUpdt()
	{
		try
		{
			if (ivjpnlFldUpdt == null)
			{
				ivjpnlFldUpdt = new javax.swing.JPanel();
				ivjpnlFldUpdt.setLayout(null);
				ivjpnlFldUpdt.add(getstclblEditFlds(), null);
				ivjpnlFldUpdt.add(getlblEditFlds(), null);
				ivjpnlFldUpdt.add(gettxtEditFlds(), null);
				ivjpnlFldUpdt.add(getstclblnNoOfUpdts(), null);
				ivjpnlFldUpdt.add(getUpdateCnt(), null);
				ivjpnlFldUpdt.add(gettxtlblUpdtsNotApplied(), null);
				ivjpnlFldUpdt.setBounds(197, 267, 293, 119);
				ivjpnlFldUpdt.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						FLD_UPDT));

			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}

		return ivjpnlFldUpdt;
	}
	/**
	 * This method initializes ivjpnlLiens
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getpnlLiens()
	{
		if (ivjpnlLiens == null)
		{
			ivjpnlLiens = new javax.swing.JPanel();
			ivjpnlLiens.setLayout(null);
			ivjpnlLiens.add(getbtnLien0(), getbtnLien0().getName());
			ivjpnlLiens.add(getbtnLien1(), getbtnLien1().getName());
			ivjpnlLiens.add(getbtnLien2(), getbtnLien2().getName());
			ivjpnlLiens.add(getbtnLien3(), getbtnLien3().getName());
			ivjpnlLiens.setBounds(6, 20, 150, 53);
			ivjpnlLiens.setBorder(
				new javax.swing.border.TitledBorder(
					new javax.swing.border.EtchedBorder(),
					NO_LIENS));
		}
		return ivjpnlLiens;
	}
	/**
	 * This method initializes ivjstclblRevDate
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getRevDate()
	{
		try
		{
			if (ivjstclblRevDate == null)
			{
				ivjstclblRevDate = new javax.swing.JLabel();
				ivjstclblRevDate.setBounds(10, 450, 60, 15);
				ivjstclblRevDate.setText("Rev Date:");
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjstclblRevDate;
	}
	/**
	 * This method initializes ivjlblUpdateCnt Edit Fields
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblEditFlds()
	{
		try
		{

			if (ivjstclblEditFlds == null)
			{
				ivjstclblEditFlds = new javax.swing.JLabel();
				ivjstclblEditFlds.setBounds(15, 16, 53, 16);
				ivjstclblEditFlds.setName("stclblEditFlds");
				ivjstclblEditFlds.setText("Edit Field:");
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjstclblEditFlds;
	}
	/**
	 * This method initializes ivjlblUpdateCnt
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblFiles()
	{
		try
		{

			if (ivjstclblFiles == null)
			{
				ivjstclblFiles = new javax.swing.JLabel();
				ivjstclblFiles.setName("stclblFiles");
				ivjstclblFiles.setBounds(118, 53, 95, 20);
				ivjstclblFiles.setText(SENDTRANS_FILES);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjstclblFiles;
	}
	/**
	 * This method initializes ivjlblUpdateCnt
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblHeader()
	{
		try
		{

			if (ivjstclblHeader == null)
			{
				ivjstclblHeader = new javax.swing.JLabel();
				ivjstclblHeader.setBounds(26, 17, 600, 20);
				ivjstclblHeader.setName("stclblHeader");
				ivjstclblHeader.setText(HEADER);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjstclblHeader;
	}
	/**
	 * This method initializes ivjstclblNoOfUpdts Number Of Updates
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblnNoOfUpdts()
	{
		try
		{

			if (ivjstclblNoOfUpdts == null)
			{
				ivjstclblNoOfUpdts = new javax.swing.JLabel();
				ivjstclblNoOfUpdts.setBounds(14, 63, 60, 20);
				ivjstclblNoOfUpdts.setName("stclblnNoOfUpdts");
				ivjstclblNoOfUpdts.setText("Updates# :");
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjstclblNoOfUpdts;
	}
	/**
	 * This method initializes ivjlblUpdateCnt
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblSelectedFiles()
	{
		try
		{

			if (ivjstclblSelectedFiles == null)
			{
				ivjstclblSelectedFiles = new javax.swing.JLabel();
				ivjstclblSelectedFiles.setBounds(346, 53, 95, 20);
				ivjstclblSelectedFiles.setName("stclblSelectedFiles");
				ivjstclblSelectedFiles.setText(SELECTED_FILES);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjstclblSelectedFiles;
	}
	/**
	 * This method initializes ivjlblUpdateCnt
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblSelectFields()
	{
		try
		{

			if (ivjstclblSelectedfields == null)
			{
				ivjstclblSelectedfields = new javax.swing.JLabel();
				ivjstclblSelectedfields.setBounds(496, 53, 150, 20);
				ivjstclblSelectedfields.setName("stclblFiles");
				ivjstclblSelectedfields.setText(FIELDS);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjstclblSelectedfields;
	}
	/**
	 * This method initializes ivjlblMfVersion
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblVersion()
	{
		try
		{

			if (ivjstclblVersion == null)
			{
				ivjstclblVersion = new javax.swing.JLabel();
				ivjstclblVersion.setName("stclblVersion");
				ivjstclblVersion.setBounds(7, 53, 95, 20);
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
	 * This method initializes ivjtxtEditFlds
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtEditFlds()
	{
		try
		{

			if (ivjtxtEditFlds == null)
			{
				ivjtxtEditFlds = new RTSInputField();
				ivjtxtEditFlds.setBounds(21, 38, 100, 20);
				ivjtxtEditFlds.setName("txtEditFlds");
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjtxtEditFlds;
	}
	/**
	 * This method initializes ivjlblUpdtsNotApplied
	 * 
	 * @return javax.swing.JLabel
	 */
	public javax.swing.JLabel gettxtlblUpdtsNotApplied()
	{
		try
		{
			if (ivjlblUpdtsNotApplied == null)
			{
				ivjlblUpdtsNotApplied = new javax.swing.JLabel();
				ivjlblUpdtsNotApplied.setBounds(150, 47, 89, 46);
				ivjlblUpdtsNotApplied.setText(ALL_UPDTS_REMOVED);
				ivjlblUpdtsNotApplied.setForeground(Color.RED);
				ivjlblUpdtsNotApplied.setVisible(false);
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		return ivjlblUpdtsNotApplied;
	}
	/**
	* This method initializes ivjlblUpdateCnt
	* 
	* @return javax.swing.JLabel
	*/
	private javax.swing.JLabel getUpdateCnt()
	{
		try
		{
			if (ivjlblUpdateCnt == null)
			{
				ivjlblUpdateCnt = new javax.swing.JLabel();
				ivjlblUpdateCnt.setBounds(78, 63, 24, 20);
				ivjlblUpdateCnt.setText("");
			}
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		return ivjlblUpdateCnt;
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
			setName("MfSendTrans");
			setSize(751, 496);
			setTitle("MfSendTrans");
			setContentPane(getBldSndTransContentPane1());
			// user code begin {2}
			populateMfVerCodes();
			populateFiles();
			setVisibleFields(false);
			setRequestFocus(false);
			// user code end
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
	}
	/**
	 * Initialize the JComboBox when Remove is Remove file from list box
	 * is selected.
	 * 
	 * @param asFileName
	 */
	private void initializeJComboBox(String asFileName)
	{
		if (asFileName.equals(TRANS))
		{
			getJComboBoxTrans().setModel(new DefaultComboBoxModel());
		}
		else if (asFileName.equals(MV_FUNC_TRANS))
		{
			getJComboBoxMvFuncTrans().setModel(
				new DefaultComboBoxModel());
		}
		else if (asFileName.equals(SR_FUNC_TRANS))
		{
			getJComboBoxSRFuncTrans().setModel(
				new DefaultComboBoxModel());
		}
		else if (asFileName.equals(INV_FUNC))
		{
			getJComboBoxInvFunc().setModel(new DefaultComboBoxModel());
		}
		else if (asFileName.equals(FUND_FUNC))
		{
			getJComboBoxFundFunc().setModel(new DefaultComboBoxModel());
		}
		else if (asFileName.equals(TR_INV_DTL))
		{
			getJComboBoxTRInvDTL().setModel(new DefaultComboBoxModel());
		}
		else if (asFileName.equals(TR_FDS_DLT))
		{
			getJComboBoxTRFDSDLT().setModel(new DefaultComboBoxModel());
		}
		// defect 10492
		else if (asFileName.equals(PRMT_TRANS))
		{
			getJComboBoxPrmtTrans().setModel(
				new DefaultComboBoxModel());
		}
		// end defect 10492
		else if (asFileName.equals(TRANS_PYMNT))
		{
			getJComboBoxTransPymnt().setModel(
				new DefaultComboBoxModel());
		}
		if (asFileName.equals(LOG_FUNC_TRANS))
		{
			getJComboBoxLogFuncTrans().setModel(
				new DefaultComboBoxModel());
		}
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
		if (getJComboBoxTrans().isVisible()
			&& ((int) getJComboBoxTrans().getSelectedIndex()) >= 0)
		{
			// Display selected field name
			getlblEditFlds().setText(
				getJComboBoxTrans()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector.
			//	This position will point to the field length
			int liPos =
				findIndexOfStrInVec(
					cvTransFrmt,
					getJComboBoxTrans()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvTransFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvTransFrmt, liPos, TRANS);
		}
		if (getJComboBoxMvFuncTrans().isVisible()
			&& ((int) getJComboBoxMvFuncTrans().getSelectedIndex()) >= 0)
		{
			// Display selected filed name
			getlblEditFlds().setText(
				getJComboBoxMvFuncTrans()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvMvFuncFrmt,
					getJComboBoxMvFuncTrans()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvMvFuncFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvMvFuncFrmt, liPos, MV_FUNC_TRANS);
		}
		if (getJComboBoxSRFuncTrans().isVisible()
			&& ((int) getJComboBoxSRFuncTrans().getSelectedIndex()) >= 0)
		{
			getlblEditFlds().setText(
				getJComboBoxSRFuncTrans()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvSRFuncFrmt,
					getJComboBoxSRFuncTrans()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvSRFuncFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvSRFuncFrmt, liPos, SR_FUNC_TRANS);
		}
		if (getJComboBoxInvFunc().isVisible()
			&& ((int) getJComboBoxInvFunc().getSelectedIndex()) >= 0)
		{
			getlblEditFlds().setText(
				getJComboBoxInvFunc()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvInvFuncFrmt,
					getJComboBoxInvFunc()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvInvFuncFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvInvFuncFrmt, liPos, INV_FUNC);
		}
		if (getJComboBoxFundFunc().isVisible()
			&& ((int) getJComboBoxFundFunc().getSelectedIndex()) >= 0)
		{
			getlblEditFlds().setText(
				getJComboBoxFundFunc()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvFundFuncFrmt,
					getJComboBoxFundFunc()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvFundFuncFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvFundFuncFrmt, liPos, FUND_FUNC);
		}
		if (getJComboBoxTRInvDTL().isVisible()
			&& ((int) getJComboBoxTRInvDTL().getSelectedIndex()) >= 0)
		{
			getlblEditFlds().setText(
				getJComboBoxTRInvDTL()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvTRInvDtlFrmt,
					getJComboBoxTRInvDTL()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvTRInvDtlFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvTRInvDtlFrmt, liPos, TR_INV_DTL);
		}
		if (getJComboBoxTRFDSDLT().isVisible()
			&& ((int) getJComboBoxTRFDSDLT().getSelectedIndex()) >= 0)
		{
			getlblEditFlds().setText(
				getJComboBoxTRFDSDLT()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvTRFDSDtlFrmt,
					getJComboBoxTRFDSDLT()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvTRFDSDtlFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvTRFDSDtlFrmt, liPos, TR_FDS_DLT);
		}
		// defect 10492
		if (getJComboBoxPrmtTrans().isVisible()
			&& ((int) getJComboBoxPrmtTrans().getSelectedIndex()) >= 0)
		{
			getlblEditFlds().setText(
				getJComboBoxPrmtTrans()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvPrmtTransFrmt,
					getJComboBoxPrmtTrans()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvPrmtTransFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvPrmtTransFrmt, liPos, PRMT_TRANS);
		}
		// end defect 10492
		if (getJComboBoxTransPymnt().isVisible()
			&& ((int) getJComboBoxTransPymnt().getSelectedIndex()) >= 0)
		{
			getlblEditFlds().setText(
				getJComboBoxTransPymnt()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvTransPymntFrmt,
					getJComboBoxTransPymnt()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvTransPymntFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvTransPymntFrmt, liPos, TRANS_PYMNT);
		}
		if (getJComboBoxLogFuncTrans().isVisible()
			&& ((int) getJComboBoxLogFuncTrans().getSelectedIndex()) >= 0)
		{
			getlblEditFlds().setText(
				getJComboBoxLogFuncTrans()
					.getSelectedItem()
					.toString()
					.trim());
			// Get position in vector
			int liPos =
				findIndexOfStrInVec(
					cvLogFuncFrmt,
					getJComboBoxLogFuncTrans()
						.getSelectedItem()
						.toString()
						.trim(),
					4,
					20);
			// Set input field length and size
			setMaxLenSizeInpFld(cvLogFuncFrmt, liPos);

			// Get data in base file
			setEditFldFromBase(cvLogFuncFrmt, liPos, LOG_FUNC_TRANS);
		}
		// Set focus to input field
		gettxtEditFlds().requestFocus();
		gettxtEditFlds().setCaretPosition(0);
	}
	/**
	 * Return number of liens
	 * 
	 * @return int
	 */
	private int numOfLiens()
	{
		if (getbtnLien1().isSelected())
		{
			return 1;
		}
		if (getbtnLien2().isSelected())
		{
			return 2;
		}
		if (getbtnLien3().isSelected())
		{
			return 3;
		}

		return 0;
	}

	/**
	 * Populate the MF Access Methods Combo Box 
	 */
	private void populateFiles()
	{
		carrFiles = new ArrayList();
		carrFiles.add(TRANS);
		carrFiles.add(MV_FUNC_TRANS);
		carrFiles.add(SR_FUNC_TRANS);
		carrFiles.add(INV_FUNC);
		carrFiles.add(FUND_FUNC);
		carrFiles.add(TR_INV_DTL);
		carrFiles.add(TR_FDS_DLT);
		// defect 10492
		carrFiles.add(PRMT_TRANS);
		// end defect 10492
		carrFiles.add(TRANS_PYMNT);
		carrFiles.add(LOG_FUNC_TRANS);
		DefaultListModel listModel = new DefaultListModel();
		for (int liIndex = 0; liIndex < carrFiles.size(); liIndex++)
		{
			listModel.addElement((String) carrFiles.get(liIndex));
		}
		getJListFiles().setModel(listModel);
	}
	/**
	 * Populate the MF Access Methods Combo Box FUND_FUNC_TRANS 
	 */
	private void populateFundFuncTrans()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0;
			liIndex < cvFundFuncFrmt.size();
			liIndex++)
		{
			lvFldNames.add(
				cvFundFuncFrmt.get(liIndex).toString().substring(
					4,
					20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxFundFunc().setModel(laMdl);
		getJComboBoxFundFunc().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + FUND_FUNC);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxFundFunc().setMaximumRowCount(
				lvFldNames.size());
		}
		// Set selection to first item
		ivjJComboBoxFundFunc.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxFundFunc.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvFundFuncFrmt,
				getJComboBoxFundFunc()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvFundFuncFrmt, liPos);
	}
	/**
	* Populate the MF Access Methods Combo Box INV_FUNC_TRANS 
	*/
	private void populateInvFuncTrans()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0;
			liIndex < cvInvFuncFrmt.size();
			liIndex++)
		{
			lvFldNames.add(
				cvInvFuncFrmt.get(liIndex).toString().substring(4, 20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxInvFunc().setModel(laMdl);
		getJComboBoxInvFunc().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + INV_FUNC);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxInvFunc().setMaximumRowCount(lvFldNames.size());
		}
		// Set selection to first item
		ivjJComboBoxInvFunc.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxInvFunc.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvInvFuncFrmt,
				getJComboBoxInvFunc()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvInvFuncFrmt, liPos);
	}
	/**
	 * Populate the MF Access Methods Combo Box LOG_FUNC_TRANS 
	 */
	private void populateLogFuncTrans()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0;
			liIndex < cvLogFuncFrmt.size();
			liIndex++)
		{
			lvFldNames.add(
				cvLogFuncFrmt.get(liIndex).toString().substring(4, 20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxLogFuncTrans().setModel(laMdl);
		getJComboBoxLogFuncTrans().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + LOG_FUNC_TRANS);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxLogFuncTrans().setMaximumRowCount(
				lvFldNames.size());
		}
		// Set selection to first item
		ivjJComboBoxLogFuncTrans.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxLogFuncTrans.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvLogFuncFrmt,
				getJComboBoxLogFuncTrans()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvLogFuncFrmt, liPos);
	}

	/**
	 * Populate the MF Access Methods Combo Box MF_VERSION_CODES 
	 */
	private void populateMfVerCodes()
	{
		setVisibleFields(false);

		// Load version codes T, U and V into array
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
		getJListMfVerCodes().setSelectedValue(lsMfInterfaceVerCd, true);
	}
	/**
	* Populate the MF Access Methods Combo Box MV_FUNC_TRANS 
	*/
	private void populateMvFunvTrans()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0; liIndex < cvMvFuncFrmt.size(); liIndex++)
		{
			// Manage LienData
			if (cvMvFuncFrmt
				.get(liIndex)
				.toString()
				.substring(3, 11)
				.equals("LIENDATA"))
			{
				lvFldNames.add(
					CommonConstant.STR_SPACE_THREE
						+ cvMvFuncFrmt.get(liIndex).toString());
			}
			else
			{
				lvFldNames.add(
					cvMvFuncFrmt.get(liIndex).toString().substring(
						4,
						20));
			}
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxMvFuncTrans().setModel(laMdl);
		getJComboBoxMvFuncTrans().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + MV_FUNC_TRANS);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxMvFuncTrans().setMaximumRowCount(
				lvFldNames.size());
		}

		// Load data from MvFuncTrans.txt
		//		cvInpFlds = loadDataFromTxtFile(getJComboBoxMvFuncTrans());
		// Set selection to first item
		ivjJComboBoxMvFuncTrans.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxMvFuncTrans.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvMvFuncFrmt,
				getJComboBoxMvFuncTrans()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvMvFuncFrmt, liPos);

	}
	/**
	 * Populate the MF Access Methods Combo Box SR_FUNC_TRANS 
	 */
	private void populateSRFuncTrans()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0; liIndex < cvSRFuncFrmt.size(); liIndex++)
		{
			lvFldNames.add(
				cvSRFuncFrmt.get(liIndex).toString().substring(4, 20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxSRFuncTrans().setModel(laMdl);
		getJComboBoxSRFuncTrans().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + SR_FUNC_TRANS);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxSRFuncTrans().setMaximumRowCount(
				lvFldNames.size());
		}
		// Set selection to first item
		ivjJComboBoxSRFuncTrans.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxSRFuncTrans.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvSRFuncFrmt,
				getJComboBoxSRFuncTrans()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvSRFuncFrmt, liPos);
	}
	/**
	 * Populate the MF Access Methods Combo Box TRANS
	 */
	private void populateTrans()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only from ADABAS list
		for (int liIndex = 0; liIndex < cvTransFrmt.size(); liIndex++)
		{
			lvFldNames.add(
				cvTransFrmt.get(liIndex).toString().substring(4, 20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxTrans().setModel(laMdl);
		getJComboBoxTrans().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + TRANS);
		// Set MaximumRowCount if less van 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxTrans().setMaximumRowCount(lvFldNames.size());
		}

		// Set selection to first item
		ivjJComboBoxTrans.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxTrans.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvTransFrmt,
				getJComboBoxTrans().getSelectedItem().toString().trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvTransFrmt, liPos);

	}
	// defect 10492
	/**
	 * Populate the MF Access Methods Combo Box TRANS_PYMNT 
	 */
	private void populatePrmtTrans()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0;
			liIndex < cvPrmtTransFrmt.size();
			liIndex++)
		{
			lvFldNames.add(
				cvPrmtTransFrmt.get(liIndex).toString().substring(
					4,
					20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxPrmtTrans().setModel(laMdl);
		getJComboBoxPrmtTrans().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + PRMT_TRANS);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxPrmtTrans().setMaximumRowCount(
				lvFldNames.size());
		}
		// Set selection to first item
		ivjJComboBoxPrmtTrans.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxPrmtTrans.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvPrmtTransFrmt,
				getJComboBoxPrmtTrans()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvPrmtTransFrmt, liPos);
	}
	// end defect 10492
	/**
	 * Populate the MF Access Methods Combo Box TRANS_PYMNT 
	 */
	private void populateTransPymnt()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0;
			liIndex < cvTransPymntFrmt.size();
			liIndex++)
		{
			lvFldNames.add(
				cvTransPymntFrmt.get(liIndex).toString().substring(
					4,
					20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxTransPymnt().setModel(laMdl);
		getJComboBoxTransPymnt().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + TRANS_PYMNT);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxTransPymnt().setMaximumRowCount(
				lvFldNames.size());
		}
		// Set selection to first item
		ivjJComboBoxTransPymnt.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxTransPymnt.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvTransPymntFrmt,
				getJComboBoxTransPymnt()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvTransPymntFrmt, liPos);
	}
	/**
	 * Populate the MF Access Methods Combo Box TR_FDS_DETAIL 
	 */
	private void populateTRFDSDetail()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0;
			liIndex < cvTRFDSDtlFrmt.size();
			liIndex++)
		{
			lvFldNames.add(
				cvTRFDSDtlFrmt.get(liIndex).toString().substring(
					4,
					20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxTRFDSDLT().setModel(laMdl);
		getJComboBoxTRFDSDLT().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + TR_FDS_DLT);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxTRFDSDLT().setMaximumRowCount(
				lvFldNames.size());
		}
		// Set selection to first item
		ivjJComboBoxTRFDSDLT.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxTRFDSDLT.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvTRFDSDtlFrmt,
				getJComboBoxTRFDSDLT()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvTRFDSDtlFrmt, liPos);
	}
	/**
	 * Populate the MF Access Methods Combo Box TR_INV_DETAIL 
	 */
	private void populateTRInvDetail()
	{
		setVisibleFields(false);
		Vector lvFldNames = new Vector();

		// Retrieve field names only
		for (int liIndex = 0;
			liIndex < cvTRInvDtlFrmt.size();
			liIndex++)
		{
			lvFldNames.add(
				cvTRInvDtlFrmt.get(liIndex).toString().substring(
					4,
					20));
		}

		DefaultComboBoxModel laMdl =
			new DefaultComboBoxModel(lvFldNames.toArray());
		getJComboBoxTRInvDTL().setModel(laMdl);
		getJComboBoxTRInvDTL().setSelectedIndex(0);
		getstclblSelectFields().setText(
			FIELDS + CommonConstant.STR_SPACE_TWO + TR_INV_DTL);
		// Set MaximumRowCount if less tan 17 (MAX_FIELDS)
		if (lvFldNames.size() < MAX_FIELDS)
		{
			getJComboBoxTRInvDTL().setMaximumRowCount(
				lvFldNames.size());
		}
		// Set selection to first item
		ivjJComboBoxTRInvDTL.setSelectedIndex(0);
		getlblEditFlds().setText(
			(String) ivjJComboBoxTRInvDTL.getSelectedItem());
		// Get position in vector
		int liPos =
			findIndexOfStrInVec(
				cvTRInvDtlFrmt,
				getJComboBoxTRInvDTL()
					.getSelectedItem()
					.toString()
					.trim(),
				4,
				20);
		// Set input field length and size
		setMaxLenSizeInpFld(cvTRInvDtlFrmt, liPos);
	}
	/**
	 * Populate the MF Access Methods List Box 
	 */
	private void removeItemFromSelectedFiles()
	{
		// Check is item was selected.
		String lsSelItm =
			((String) getJListSelectedFiles().getSelectedValue());

		// defect 10080
		//	Check if item was selected before continuing
		if (lsSelItm == null)
		{
			RTSException laRTSEX =
				new RTSException(
					RTSException.INFORMATION_MESSAGE,
					"A file has not been selected. You must select a file.",
					"Remove Selected File");
			laRTSEX.displayError();
		}
		// end defect 10080
		else
		{
			// Remove updates
			rmvUpdates();

			// Remove user selected item from the Selected Files list box 
			if (carrSelectedFiles.contains(lsSelItm))
			{
				carrSelectedFiles.remove(lsSelItm);
			}
			// Set data model for Select Files list box
			DefaultListModel laSFListModel = new DefaultListModel();
			for (int liIndex = 0;
				liIndex < carrSelectedFiles.size();
				liIndex++)
			{
				laSFListModel.addElement(
					(String) carrSelectedFiles.get(liIndex));
			}
			getJListSelectedFiles().setModel(laSFListModel);
			// Add selected item from the Selected Files to Files list box.
			if (!carrFiles.contains(lsSelItm))
			{
				// Add item to array list
				carrFiles.add(lsSelItm);
			}
			// Sort list
			carrFiles = sortArrayList(carrFiles);
			// Set data model for Files list box
			DefaultListModel laFListModel = new DefaultListModel();
			for (int liIndex = 0;
				liIndex < carrFiles.size();
				liIndex++)
			{
				laFListModel.addElement(
					(String) carrFiles.get(liIndex));
			}
			getJListFiles().setModel(laFListModel);
			// Reset Fields Label and combo box
			getstclblSelectFields().setText(FIELDS);
			initializeJComboBox(lsSelItm);

			// Turn off Liens if MvFuncTrans is removed from Selected List
			if (lsSelItm.equals(MV_FUNC_TRANS))
			{
				getbtnLien0().setEnabled(false);
				getbtnLien0().setSelected(false);
				getbtnLien1().setEnabled(false);
				getbtnLien1().setSelected(false);
				getbtnLien2().setEnabled(false);
				getbtnLien2().setSelected(false);
				getbtnLien3().setEnabled(false);
				getbtnLien3().setSelected(false);
			}
		}
	}

	/**
	 * Reset all combo and list boxes to startup mode
	 * Remove all updates
	 */
	private void resetAll()
	{
		// Clear Updt counts and update fields
		getUpdateCnt().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtEditFlds().setText(CommonConstant.STR_SPACE_EMPTY);
		getlblEditFlds().setText(CommonConstant.STR_SPACE_EMPTY);
		closeAllComboBoxes();

		// Turn off Liens
		getbtnLien0().setEnabled(false);
		getbtnLien0().setSelected(false);
		getbtnLien1().setEnabled(false);
		getbtnLien1().setSelected(false);
		getbtnLien2().setEnabled(false);
		getbtnLien2().setSelected(false);
		getbtnLien3().setEnabled(false);
		getbtnLien3().setSelected(false);

		// Remove updates
		if (cvUpdates.size() > 0)
		{
			// Remove all updates
			cvUpdates = new Vector();
			gettxtlblUpdtsNotApplied().setVisible(true);
			// reset size of input text field
			gettxtEditFlds().setBounds(21, 38, 100, 20);

			//	Call Sleep thread to handle display updates not applied.
			BuildSendTransSleep lsBldSndTrnsSleep =
				new BuildSendTransSleep(this);
			Thread t = new Thread(lsBldSndTrnsSleep);
			t.start();
		}

	}
	/**
	* Remove all SendTrans files from Select List Box 
	* Load all files to Files List Box
	*/
	private void rmvdAllSelectedFiles()
	{
		populateFiles();
		// Remove select item from File list box
		// Remove/Add all files to Selected List Box.
		for (int liIndex = carrSelectedFiles.size() - 1;
			liIndex >= CommonConstant.ELEMENT_0;
			liIndex--)
		{
			carrSelectedFiles.remove(liIndex);
		}
		// Set data model
		getJListSelectedFiles().setModel(new DefaultListModel());

		// Reset all input fields, combo and updates
		resetAll();
	}
	/**
	 * Remove updates for selected file
	 */
	private void rmvUpdates()
	{
		boolean lbUpDateRmv = false;

		// cvUpdates is a vector of updates.
		//	Get each record and check if the first item = MV_FUNC_TRANS
		//	Remove if true
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{ // 
			if (((Vector) cvUpdates.get(liIndex))
				.get(0)
				.equals(
					getJListSelectedFiles()
						.getSelectedValue()
						.toString()))
			{
				cvUpdates.remove(liIndex);
				liIndex--;
				lbUpDateRmv = true;
			}
		}

		// Remove updates
		if (lbUpDateRmv)
		{
			gettxtlblUpdtsNotApplied().setVisible(true);
			gettxtlblUpdtsNotApplied().setText(UPDTS_REMOVED);
			// Clear edit boxes
			gettxtEditFlds().setText(CommonConstant.STR_SPACE_EMPTY);
			getlblEditFlds().setText(CommonConstant.STR_SPACE_EMPTY);
			// Reset update counts
			getUpdateCnt().setText(String.valueOf(cvUpdates.size()));

			//	Call Sleep thread to handle display updates not applied.
			BuildSendTransSleep lsBldSndTrnsSleep =
				new BuildSendTransSleep(this);
			Thread t = new Thread(lsBldSndTrnsSleep);
			t.start();
		}

	}
	/**
	 * Return base file name, partials or full population
	 *
	 * @param asFile	String - Base file name
	 * @return String
	 */
	private String rtnBaseFileName(String asFile)
	{
		// Set file name
		String lsFileName = asFile;
		if (getbtnPopAllFlds().isSelected())
		{
			lsFileName = lsFileName + _FULL;
		}
		return lsFileName;
	}
	/**
	 * Get base file data
	 * 
	 * @param asFile
	 * @return
	 */
	private Vector rtnData(String asFileName)
	{
		// The location:
		//	 T:\RTS II Team\DEV\Build\MF Access\SendTransaction\
		//		Transaction Files\T - SendTransBase
		// where T is the MfVersionCd retrieve from list box.
		Vector lvFileData =
			FileUtil.copyFileToVector(
				SystemProperty.getSendTransFileLoc()
					+ TRANSACTION_FILES
					+ getJListMfVerCodes().getSelectedValue().toString()
					+ SENDTRANS_BASE_LOC
					+ asFileName
					+ TXT);

		return lvFileData;

	}
	/**
	 *  Return format of data file
	 * 
	 * @param asData	String of the input field
	 * @return String
	 */
	private String rtnFrmtStr()
	{
		int liSPos = 4;
		int liEPos = 20;
		String lsFormat = CommonConstant.STR_SPACE_EMPTY;
		// Get format Trans
		if (getJListSelectedFiles()
			.getSelectedValue()
			.toString()
			.equals(TRANS))
		{
			lsFormat =
				cvTransFrmt
					.get(
						findIndexOfStrInVec(
							cvTransFrmt,
							getJComboBoxTrans()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
		} // Get format MvFuncTrans
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
				MV_FUNC_TRANS))
		{
			lsFormat =
				cvMvFuncFrmt
					.get(
						findIndexOfStrInVec(
							cvMvFuncFrmt,
							getJComboBoxMvFuncTrans()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
		} // Get format SRFuncTrans
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
				SR_FUNC_TRANS))
		{
			lsFormat =
				cvSRFuncFrmt
					.get(
						findIndexOfStrInVec(
							cvSRFuncFrmt,
							getJComboBoxSRFuncTrans()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
		} // Get format InvFundFunc
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
				INV_FUNC))
		{
			lsFormat =
				cvInvFuncFrmt
					.get(
						findIndexOfStrInVec(
							cvInvFuncFrmt,
							getJComboBoxInvFunc()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
		} // Get format FundsFunc
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
				FUND_FUNC))
		{
			lsFormat =
				cvFundFuncFrmt
					.get(
						findIndexOfStrInVec(
							cvFundFuncFrmt,
							getJComboBoxFundFunc()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
		} // Get format TRInvDetail
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
				TR_INV_DTL))
		{
			lsFormat =
				cvTRInvDtlFrmt
					.get(
						findIndexOfStrInVec(
							cvTRInvDtlFrmt,
							getJComboBoxTRInvDTL()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
		} // Get format trFDSDetail
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
				TR_FDS_DLT))
		{
			lsFormat =
				cvTRFDSDtlFrmt
					.get(
						findIndexOfStrInVec(
							cvTransFrmt,
							getJComboBoxTRFDSDLT()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
			// defect 10492
		} // Get format PrmtTrans
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
					PRMT_TRANS))
		{
			lsFormat =
				cvPrmtTransFrmt
					.get(
						findIndexOfStrInVec(
							cvPrmtTransFrmt,
							getJComboBoxPrmtTrans()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
			// end defect 10492
		} // Get format TransPymnt
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
				TRANS_PYMNT))
		{
			lsFormat =
				cvTransPymntFrmt
					.get(
						findIndexOfStrInVec(
							cvTransPymntFrmt,
							getJComboBoxTransPymnt()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
		} // Get format LogFuncTrans
		else if (
			getJListSelectedFiles()
				.getSelectedValue()
				.toString()
				.equals(
				LOG_FUNC_TRANS))
		{
			lsFormat =
				cvLogFuncFrmt
					.get(
						findIndexOfStrInVec(
							cvLogFuncFrmt,
							getJComboBoxLogFuncTrans()
								.getSelectedItem()
								.toString()
								.trim(),
							liSPos,
							liEPos))
					.toString();
		}

		return lsFormat;
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
		// Set Revise date
		getlblRevDate().setText(
			SystemProperty.getBuildSendTransReviseDate());
	}
	/**
	 * Return data for selected field
	 *
	 * @param avData	Vector - Base file format
	 * @param aiPos		int - Row of format for field
	 * @param aiFileNo	int - Index for case stmt
	 * @return String
	 */
	private void setEditFldFromBase(
		Vector avData,
		int aiPos,
		String asFileBase)
	{
		// Get base record format
		String lsFormatData = (String) avData.get(aiPos);
		int liSPos =
			Integer.parseInt(lsFormatData.substring(41, 45).trim()) - 1;
		int liEPos =
			Integer.parseInt(lsFormatData.substring(48, 52).trim());

		// Get base file, partial or full file name
		//	partial - TRANS.txt or FULL - TRANS_FULL.txt
		// Get vector of data
		Vector lvData =
			(Vector) rtnData((String) rtnBaseFileName(asFileBase));
		String lsData = (String) lvData.get(0);
		gettxtEditFlds().setText(lsData.substring(liSPos, liEPos));
	}
	/**
	 * Add lien info (0,1,2 or 3 liens to SendTrans)
	 *
	 * @param aaLienBtn	String
	 */
	private void setLiens(Object aaLienBtn)
	{
		if (aaLienBtn == getbtnLien0())
		{
			// Turn off liens 2 and 3
			getbtnLien0().setSelected(true);
			getbtnLien1().setSelected(false);
			getbtnLien2().setSelected(false);
			getbtnLien3().setSelected(false);

		}
		else if (aaLienBtn == getbtnLien1())
		{
			// Turn off liens 2 and 3
			getbtnLien0().setSelected(false);
			getbtnLien1().setSelected(true);
			getbtnLien2().setSelected(false);
			getbtnLien3().setSelected(false);

		}
		else if (aaLienBtn == getbtnLien2())
		{
			// Turn off liens 1 and 3
			getbtnLien0().setSelected(false);
			getbtnLien1().setSelected(false);
			getbtnLien3().setSelected(false);
			getbtnLien2().setSelected(true);

		}
		else if (aaLienBtn == getbtnLien3())
		{
			// Turn off liens 1 and 2
			getbtnLien0().setSelected(false);
			getbtnLien1().setSelected(false);
			getbtnLien2().setSelected(false);
			getbtnLien3().setSelected(true);
		}
	}
	/**
				 *  Set the maximum length and size for input field
				 * 
				 * @param avRecs	Vector of Records
				 * @param aiPos		Position in vector
				 */
	private void setMaxLenSizeInpFld(Vector avRecs, int aiPos)
	{
		int liSPos = 57;
		int liEPos = 59;
		int liMultPlier = 14;
		gettxtEditFlds().setMaxLength(
			Integer.parseInt(
				avRecs
					.get(aiPos)
					.toString()
					.substring(liSPos, liEPos)
					.trim()));
		gettxtEditFlds().setSize(
			Integer.parseInt(
				avRecs
					.get(aiPos)
					.toString()
					.substring(liSPos, liEPos)
					.trim())
				* liMultPlier,
			20);
	}
	/**
	 * Set the enable and visible
	 */
	private void setVisibleFields(boolean abValue)
	{
		getJComboBoxTrans().setEnabled(abValue);
		getJComboBoxTrans().setVisible(abValue);
		getJComboBoxMvFuncTrans().setEnabled(abValue);
		getJComboBoxMvFuncTrans().setVisible(abValue);
		getJComboBoxSRFuncTrans().setEnabled(abValue);
		getJComboBoxSRFuncTrans().setVisible(abValue);
		getJComboBoxInvFunc().setEnabled(abValue);
		getJComboBoxInvFunc().setVisible(abValue);
		getJComboBoxFundFunc().setEnabled(abValue);
		getJComboBoxFundFunc().setVisible(abValue);
		getJComboBoxTRInvDTL().setEnabled(abValue);
		getJComboBoxTRInvDTL().setVisible(abValue);
		getJComboBoxTRFDSDLT().setEnabled(abValue);
		getJComboBoxTRFDSDLT().setVisible(abValue);
		getJComboBoxTransPymnt().setEnabled(abValue);
		getJComboBoxTransPymnt().setVisible(abValue);
		getJComboBoxLogFuncTrans().setEnabled(abValue);
		getJComboBoxLogFuncTrans().setVisible(abValue);
	}

	/**
	* Sort ArrayList to SendTrans File default order
	*
	* @param aarrList	ArrayList - List to be sorted
	* @return ArrayList
	*/
	private ArrayList sortArrayList(ArrayList aarrList)
	{
		// The return sorted array  
		ArrayList larrTemp = new ArrayList();

		// Go through the passed files list and sort by default order. 

		if (aarrList.contains(TRANS))
		{
			larrTemp.add(TRANS);
		}
		if (aarrList.contains(MV_FUNC_TRANS))
		{
			larrTemp.add(MV_FUNC_TRANS);
		}
		if (aarrList.contains(SR_FUNC_TRANS))
		{
			larrTemp.add(SR_FUNC_TRANS);
		}
		if (aarrList.contains(INV_FUNC))
		{
			larrTemp.add(INV_FUNC);
		}
		if (aarrList.contains(FUND_FUNC))
		{
			larrTemp.add(FUND_FUNC);
		}
		if (aarrList.contains(TR_INV_DTL))
		{
			larrTemp.add(TR_INV_DTL);
		}
		if (aarrList.contains(TR_FDS_DLT))
		{
			larrTemp.add(TR_FDS_DLT);
		}
		// defect 10492
		if (aarrList.contains(PRMT_TRANS))
				{
					larrTemp.add(PRMT_TRANS);
				}
		// end defect 10492
		if (aarrList.contains(TRANS_PYMNT))
		{
			larrTemp.add(TRANS_PYMNT);
		}
		if (aarrList.contains(LOG_FUNC_TRANS))
		{
			larrTemp.add(LOG_FUNC_TRANS);
		}
		//		}

		//		}

		return larrTemp;
	}

	/**
	 * 
	 * @author jrue
	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	private void sortUpdtVec()
	{
		Vector lvTrans = new Vector();

		String lsFileName = CommonConstant.STR_SPACE_EMPTY;
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(TRANS))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(MV_FUNC_TRANS))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(SR_FUNC_TRANS))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(INV_FUNC))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(FUND_FUNC))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(TR_INV_DTL))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(TR_FDS_DLT))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		// defect 10492
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
				{
					lsFileName =
						((Vector) cvUpdates.get(liIndex)).get(0).toString();
					if (lsFileName.equals(PRMT_TRANS))
					{
						lvTrans.add(cvUpdates.get(liIndex));
					}
				}
		// end defect 10492
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(TRANS_PYMNT))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		for (int liIndex = 0; liIndex < cvUpdates.size(); liIndex++)
		{
			lsFileName =
				((Vector) cvUpdates.get(liIndex)).get(0).toString();
			if (lsFileName.equals(LOG_FUNC_TRANS))
			{
				lvTrans.add(cvUpdates.get(liIndex));
			}
		}
		cvUpdates = new Vector();
		for (int liIndex = 0; liIndex < lvTrans.size(); liIndex++)
		{
			cvUpdates.add((Vector) lvTrans.get(liIndex));
		}
	}
	/**
	 * 
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getSource() == getJListMfVerCodes())
		{
			//do my stuff
			setVisibleFields(false);
			// Get formats (Version T, U orV)
			if (getJListMfVerCodes().getSelectedValue() != null)
			{
				String liSelItm =
					(String) getJListMfVerCodes().getSelectedValue();
				if (liSelItm.equals(T_VER))
				{
					getFormats(SENDTRANS77T);
				}
				if (liSelItm.equals(U_VER))
				{
					getFormats(SENDTRANS77U);
				}
				if (liSelItm.equals(V_VER))
				{
					getFormats(SENDTRANS77V);
				}

				// Reset all input fields, combo and updates
				resetAll();
			}
		}
		else if (getJListSelectedFiles().getSelectedValue() != null)
		{

			if (e.getSource() == getJListSelectedFiles())
			{
				//do my stuff
				setVisibleFields(false);
				String liSelItm =
					(String) getJListSelectedFiles().getSelectedValue();
				if (liSelItm.equals(TRANS))
				{
					populateTrans();
					getJComboBoxTrans().setVisible(true);
					getJComboBoxTrans().setEnabled(true);
				}
				if (liSelItm.equals(MV_FUNC_TRANS))
				{
					populateMvFunvTrans();
					getJComboBoxMvFuncTrans().setVisible(true);
					getJComboBoxMvFuncTrans().setEnabled(true);
				}
				if (liSelItm.equals(SR_FUNC_TRANS))
				{
					populateSRFuncTrans();
					getJComboBoxSRFuncTrans().setVisible(true);
					getJComboBoxSRFuncTrans().setEnabled(true);
				}
				if (liSelItm.equals(INV_FUNC))
				{
					populateInvFuncTrans();
					getJComboBoxInvFunc().setVisible(true);
					getJComboBoxInvFunc().setEnabled(true);
				}
				if (liSelItm.equals(FUND_FUNC))
				{
					populateFundFuncTrans();
					getJComboBoxFundFunc().setVisible(true);
					getJComboBoxFundFunc().setEnabled(true);
				}
				if (liSelItm.equals(TR_INV_DTL))
				{
					populateTRInvDetail();
					getJComboBoxTRInvDTL().setVisible(true);
					getJComboBoxTRInvDTL().setEnabled(true);
				}
				if (liSelItm.equals(TR_FDS_DLT))
				{
					populateTRFDSDetail();
					getJComboBoxTRFDSDLT().setVisible(true);
					getJComboBoxTRFDSDLT().setEnabled(true);
				}
				// defect 10492
				if (liSelItm.equals(PRMT_TRANS))
				{
					populatePrmtTrans();
					getJComboBoxPrmtTrans().setVisible(true);
					getJComboBoxPrmtTrans().setEnabled(true);
				}
				// end defect 10492
				if (liSelItm.equals(TRANS_PYMNT))
				{
					populateTransPymnt();
					getJComboBoxTransPymnt().setVisible(true);
					getJComboBoxTransPymnt().setEnabled(true);
				}
				if (liSelItm.equals(LOG_FUNC_TRANS))
				{
					populateLogFuncTrans();
					getJComboBoxLogFuncTrans().setVisible(true);
					getJComboBoxLogFuncTrans().setEnabled(true);
				}
				// Set focus to input field
				gettxtEditFlds().requestFocus();
			}
		}

	}
}
