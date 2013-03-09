package com.txdot.isd.rts.services.util;

import java.awt.event.*;
import java.io.File;
import java.net.InetAddress;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.business.BusinessInterface;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.*;
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
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

import com.txdot.isd.rts.server.common.business.VehicleInquiry;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.systemcontrolbatch.MFTrans;

/*
 * MfAccessTest.java
 *
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/05/2003	Change Mainframe pointer to be 'mainframe'
 *								instead of the old ip address.
 * Ray Rowehl	05/28/2003	Change "PRD" db to "PRD2".
 * Ray Rowehl	06/10/2003	Change to separate Special Owner from 
 * 								Special Plate
 *								Also add the R99 checkbox for Ron's 
 *								Testing
 * Ray Rowehl	07/25/2003	Change R99 select to use a combo box.  That
 *							way only the selected trans will change to 
 *							99.
 * Ray Rowehl	07/27/2003	Do payment List search by Payment Date.
 *							This was left out before.
 *							Set up VINA Only call.
 * Ray Rowehl	04/08/2005	RTS 5.2.3 Code Cleanup
 * 							defect 7937 Ver 5.2.3
 * Ray Rowehl	04/18/2005	Remove object declarations coming back 
 * 							from MFA calls.  They are never used.
 * 							We just want to see the console output.
 * 							modify actionPerformed()
 * 							defect 7937 Ver 5.2.3
 * Ray Rowehl	08/12/2005	Add declarations for Port.  Need to be able 
 * 							to specify the port for production.
 * 							defect 7937	Ver 5.2.3
 * Ray Rowehl	10/16/2006	Remove undefined CICS Trans.
 * 							Do some code re-formatting.
 * 							Remove FocusListener stuff.
 * 							delete focusGained(), focusLost(), 
 * 								getBuilderData()
 * 							modify getJComboBoxMfVersion(), 
 * 								populateMfTrans(), setMFTransToR99()
 * 							defect 6701 Ver Exempts
 * Ray Rowehl	10/25/2006	Put try - catch around all the mf calls as 
 * 							a group.  This allows various calls to 
 * 							throw exceptions and we will handle.
 * 							Add some of the Trans Ids to the pulldown.
 * 							modify actionPerformed()
 * 							defect 6701 Ver Exempt
 * J Rue		02/13/2007	Add Special Plates
 * 							add gettxtSpclRegId()
 * 							add getstcLblSpclRegId()
 * 							modfiy getMfAccessTestContentPane1()
 * 							modify actionPerformed()
 * 							modfiy setData()
 * 							modify populateMfAccessMethods()
 *							defect 9086 Ver special Plates
 * J Rue		03/07/2007	Remove RegStkrNo
 * 							Move U Version to first in list
 * 							Remove Time and Date call
 * 							Change call to retrieveMfRecordsByVIN() for
 * 							R10
 * 							modify actionPerformed(), 
 * 							modify populateMfVersion(),
 * 							modify populateMfAccessMethods()
 *							defect 9086 Ver special Plates
 * J Rue		03/13/2007	Change label to HOOPSRegPltNo
 * 							modify getstcLblRegPltNo()
 *							defect 9086 Ver special Plates
 * J Rue		04/24/2007	Add R33. Clean up code
 * 							modify actionPerformed(), 
 * 							modify populateMfAccessMethods()
 *							defect 9086 Ver special Plates
 * J Rue		05/01/2007	Call retrieveVehicleFromMF()	
 * 							modify actionPerformed()
 *							defect 9086 Ver special Plates
 * J Rue		05/14/2007	Add TransCd = VehInq when calling 
 * 							retrieveVehicleFromMF()
 * 							modift actionPerformed()
 * 							defect 8984 Ver Special Plates
 * J Rue		11/06/2007	Add option for multiple retries
 * 							add getMfRecordWAS(), setMfAccesInstance()
 * 							 FocusListener, focusgained(), focusLost()
 * 							modify actionPerformed()
 * 							defect 9403 Ver Special Plates
 * J Rue		05/01/2008	Modify MFACCESS_RETRIEVE_SP_PLATE text
 * 							defect 8984 Ver 3_AMIGOS_PH_B
 * J Rue		08/25/2008	Set boolean SendTransIndi = true
 * 							Call SendTrans. Add Timeout. Reset 
 * 								MfAccess.R## to its original value. 
 * 							Add user Time Out
 * 							add gettxtTimeout(), getstclblTimeout()
 * 								resetMFTransR99()
 * 							modify setMFTransToR99(), actionPerformed()
 * 								getMfAccessTestContentPane1(),
 * 								setMfAccesInstance()
 * 							defect 8984 Ver ELT_MfAccess
 * J rue		09/19/2008	Add try-ctach for nullpointer if file not 
 * 							found
 * 							modify populateSendTransFiles()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		09/19/2008	SetEnabled for fields
 * 							modify setVisibleScreenFields(), 
 * 								actionPerformed()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		09/22/2008	Add setEnabled() seeting for resetting screen
 * 							after snedTrans() testing.
 * 							modify setVisibleScreenFields()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		09/26/2008	Increase length of SendTrans file combobox
 * 							modify getJComboBoxSendTransFiles()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		01/21/2009	Remove 02 from variable to read 
 * 							"retrieveVehicleFromArchive - R01, R03, R04"
 * 							modify retrieveVehicleFromActiveInactive
 * 							defect 8984 Ver Defect_POS_D
 * J Rue		01/29/2009	Set Key1 with DocNo and Set Key2 with DocNo 
 * 							number
 * 							modify actionPerformed()
 * 							defect 8984 Ver Defect_POS_D
 * J Rue		03/05/2009	R02, R12, R20 has been deprecated
 * 							modify resetMFTransR99(), actionPerformed()
 * 								populateMfAccessMethods(), 
 * 								populateMfTrans(), setMFTransToR99()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		03/10/2009	Add SendTrans and FundsUpdate push buttons
 * 							add getbtnSendTrans(), getbtnFundsUpdate()
 * 							modify actionPerformed()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		03/23/2009	Add else to if (cbFundsUpdateIndi)
 * 							modify actionPerformed()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		03/24/2009	Parse the RemitAmt seperating the integer 
 * 							from decimal and concantanating the 2 with a 
 * 							'.' as a seperator
 * 							modify buildFundsData()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		04/17/2009	Set defaut MfInterfaceVersionCode to 
 * 							server.cfg
 * 							modify populateMfVersion()
 * 							defect 9965 Ver Defect_POS_E
 * J Rue		06/16/2009	Move setDefaultMfVerCd() from UtiltiyMethods
 * 							to FileUtil class
 * 							modify populateMfVersion()	
 * 							defect 10080 Ver Defect_POS_F
 * J Rue		07/08/2009	Add check box to TransTime, used for unique
 * 							transactions
 * 							add getchkboxChangeTransTime()
 * 							modify setComboBoxSendTrans()
 * K Harrell	07/02/2009	modify actionPerformed(), buildFundsData()
 * 							defect 10112 Ver Defect_POS_F
 * 							defect 10080 Ver Defect_POS_F
 * J Rue		07/10/2009	Remove a transit RTSButton
 * 							defect 10080 Ver Deefct_POS_F
 * K Harrell 	06/10/2010	Changes for Temp Permit
 * 							Modify actionPerformed(), MfAccessTest() 
 * 							and populateMfAccessMethods() 
 * 							defect 10492 Ver 6.5.0 
 * M Reyes		01/21/2011  Comment out Prod as a selection.
 * 							defect 10710 Ver. 6.7.0
 * K Harrell	07/19/2012	Update w/ new DMV LPAR, port 
 * 							Ver 7.0.0 
 * ---------------------------------------------------------------------
 */

/**
 * MF Access Test class.
 *
 * <p>This class is designed to allow MF programmers to use MFAccess 
 * almost directly to verify the results of MFAccess calls.  This 
 * avoids having the results masked by the application.
 * 
 * <p>To run this method properly from a client, set the code in 
 * RTSComm layer, isServer() method should return true (so that the 
 * server side property files can be loaded).
 * 
 * @version	7.0.0 				07/19/2012
 * @author	Marx Rajangam
 * <br>Creation Date:			03/07/2002 17:28:36 
 */

public class MfAccessTest
	extends RTSDialogBox
	implements ActionListener, ItemListener, FocusListener
{

	FundsUpdateData caFundsUpdateData;
	boolean cbFundsUpdateIndi = false;
	boolean cbSendTransIndi = false;
	int ciCntIndex = 1;
	int ciNoOfRecs = 0;

	private RTSButton ivjbtnSendTrans = null;
	private JCheckBox ivjchkboxChangeTransTime = null;
	private RTSButton ivjFundsUpdate = null;
	private JComboBox ivjJComboBoxMfAccessMethod = null;
	private JComboBox ivjJComboBoxMfDBVersion = null;
	private JComboBox ivjJComboBoxMfVersion = null;
	private JComboBox ivjJComboBoxR99Selection = null;
	private JComboBox ivjJComboBoxSendTransFiles = null;
	private JPanel ivjMfAccessTestContentPane1 = null;
	private RTSButton ivjOK = null;
	private JLabel ivjstcLblCheckNo = null;
	private JLabel ivjstcLblComptCntyNo = null;
	private JLabel ivjstcLblDocNo = null;
	private JLabel ivjstcLblFundsDueDate = null;
	private JLabel ivjstcLblFundsPymntDate = null;
	private JLabel ivjstcLblFundsRptDate = null;
	private JLabel ivjstcLblHeading = null;
	private JLabel ivjstcLblInvcNo = null;
	private JLabel ivjstcLblMfAccessMethod = null;
	private JLabel ivjstcLblMfDB = null;
	private JLabel ivjstcLblOwnrId = null;
	private JLabel ivjstclblR99 = null;
	private JLabel ivjstcLblRegPltNo = null;
	private JLabel ivjstclblRetries = null;
	private JLabel ivjstcLblRptngDate = null;
	private JLabel ivjstcLblSendTransFiles = null;
	private JLabel ivjstcLblSpclRegId = null;
	private JLabel ivjstclblTimeout = null;
	private JLabel ivjstcLblTraceNo = null;
	private JLabel ivjstcLblVersionMfAPPC = null;
	private JLabel ivjstcLblVin = null;
	private RTSInputField ivjtxtCheckNo = null;
	private RTSInputField ivjtxtComptCntyNo = null;
	private RTSInputField ivjtxtDocNo = null;
	private RTSInputField ivjtxtFundsDueDate = null;
	private RTSInputField ivjtxtFundsPymntDate = null;
	private RTSInputField ivjtxtFundsRptDate = null;
	private RTSInputField ivjtxtInvcNo = null;
	private RTSInputField ivjtxtOwnrId = null;
	private RTSInputField ivjtxtRegPltNo = null;
	private RTSInputField ivjtxtRetries = null;
	private RTSInputField ivjtxtRptngDate = null;
	private RTSInputField ivjtxtSpclRegId = null;
	private RTSInputField ivjtxtTimeout = null;
	private RTSInputField ivjtxtTraceNo = null;
	private RTSInputField ivjtxtVin = null;
	private JPanel jPanel = null;

	// defect 10492 
	private JPanel ivjJPanelPermit = null;
	private JLabel ivjstcLblPrmtNo = null;
	private JLabel ivjstcLblPrmtVIN = null;
	private JLabel ivjstcLblPrmtIssuanceId = null;
	private JLabel ivjstcLblCustBsnName = null;
	private JLabel ivjstcLblCustLstName = null;
	private JLabel ivjstcLblBeginDate = null;
	private JLabel ivjstcLblEndDate = null;
	private RTSInputField ivjtxtPrmtNo = null;
	private RTSInputField ivjtxtPrmtVIN = null;
	private RTSInputField ivjtxtPrmtIssuanceId = null;
	private RTSInputField ivjtxtCustBsnName = null;
	private RTSInputField ivjtxtCustLstName = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;
	// end defect 10492 

	// String
	private String csTestFiles_Loc = CommonConstant.STR_SPACE_EMPTY;

	// Constants
	// new DMV LPAR
	//private static final int CICS_JGP_PORT_PROD = 4030;
	//private static final int CICS_JGP_PORT_TEST = 4030;
	//private static String MF_NAME = "mainframe";
	private static final int CICS_JGP_PORT_PROD = 4080;
	private static final int CICS_JGP_PORT_TEST = 4080;
	private static String MF_NAME = "dmvd.txdmv.gov";
	
	// end new DMV LPAR 
	private static int CICS_JGP_PORT = CICS_JGP_PORT_TEST;
	private static final String FUNDS = "FUNDS";
	private static final String FUNDSUPDT_FILE_LOC_ROOT =
		SystemProperty.getFundsUpdateFileLoc();
	private static final String MF_DB_PRD2 = "PRD2";
	//Mainframe db names
	private static final String MF_DB_TST3 = "TST3";
	private static final String MF_DB_TST5 = "TST5";
	private static final int MF_DEBUG_MODE = 1;
	private static final String MF_JG_PROG = "RTSJGATE";
	private static final String MF_TIMEOUT = "000030";
	private static final String MFACCESS_REGIS_BY_DOCNO =
		"retrieveRegisByDocNo - R33";
	private static final String MFACCESS_RETRIEVE_FDSPYMNT_DATALIST =
		"retrieveFundsPaymentDataList - R13, R15, R30, R31, R32";
	private static final String MFACCESS_RETRIEVE_FDSREMITTANCE_RECORD =
		"retrieveFundsRemittanceRecord - R14";
	private static final String MFACCESS_RETRIEVE_INVOICE =
		"retrieveInvoice - R17, R26";
	private static final String MFACCESS_RETRIEVE_JUNK_RECORDS =
		"retrieveJunkRecords - R28";
	// defect 9965
	//	deprecated
	//	private static final String MFACCESS_RETRIEVE_NO_OF_DOCS =
	//		"retrieveNoOfDocuments - R12";
	// end defect 9965
	private static final String MFACCESS_RETRIEVE_OWNER =
		"retrieveOwner - R18";

	// defect 10492 
	private static final String MFACCESS_RETRIEVE_PERMIT_PRMTNO =
		"retrievePermit (PrmtNo) - R02";
	private static final String MFACCESS_RETRIEVE_PERMIT_PRMTID =
		"retrievePermit (PrmtId) - R02";
	private static final String MFACCESS_RETRIEVE_PERMIT_VIN =
		"retrievePermit (VIN) - R02";
	private static final String MFACCESS_RETRIEVE_PERMIT_CUST_LST_NAME =
		"retrievePermit (CustLstName) - R02";
	private static final String MFACCESS_RETRIEVE_PERMIT_CUST_BSN_NAME =
		"retrievePermit (CustBsnName) - R02";
	// end defect 10492 

	private static final String MFACCESS_RETRIEVE_SP_PLATE =
		"retrieveSP_HOOPSRegPltNo - R08";
	//Mainframe record retrieval calls
	private static final String MFACCESS_RETRIEVE_SP_SPCLREGID =
		"retrieveSP_RegPltNo - R05";
	private static final String MFACCESS_RETRIEVE_TITLE_IN_PROCESS =
		"retrieveTitleInProcess - R09";
	private static final String MFACCESS_RETRIEVE_VEH_BY_SPL_REGIS =
		"retrieveVehBySplPlate - R19";
	private static final String MFACCESS_RETRIEVE_VEHICLE_FROM_ACTIVE_INACTIVE =
		"retrieveVehicleFromActiveInactive - R01, R03, R04";
	private static final String MFACCESS_RETRIEVE_VEHICLE_FROM_ARCHIVE =
		"retrieveVehicleFromArchive - R01, R03, R04";
	// end defect 10492  
	private static final String MFACCESS_RETRIEVE_VEHICLE_FROM_VINA =
		"retrieveVehicleFromVINA - R10";
	private static final String MFACCESS_VOID_TRANSACTIONS =
		"voidTransactions - R07";
	private static final String PROD_CICS = "CX30THD";

	// Constants
	// User defined variables
	// set the R99 varible
	private static final String R99 = "R99";
	private static final int SEARCH_ARCHIVE = 1;
	private static final String SENDTRANS = "SendTrans";
	// defect 8984
	private static final String SENDTRANS_FILE_LOC_ROOT =
		SystemProperty.getSendTransFileLoc();
	// defect 9965
	//	Add FundsUpdate
	private static final String T_FUNDSUPDT = "T - FundsUpdate";

	// InterfaceVersionCode
	private static final String T_SENDTRANS = "T - SendTrans";
	private static final String TEST_CICS = "CX80THD";
	private static final String U_FUNDSUPDT = "U - FundsUpdate";
	private static final String U_SENDTRANS = "U - SendTrans";
	private static final String V_FUNDSUPDT = "V - FundsUpdate";
	private static final String V_SENDTRANS = "V - SendTrans";

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			if (aarrArgs.length > 0)
			{
				System.setProperty("ServerId", aarrArgs[0]);
			}
			else
			{
				System.setProperty("ServerId", "XG");
			}
			if (aarrArgs.length > 1)
			{
				MF_NAME = aarrArgs[1];
			}
			MfAccessTest laMfAccessTest;
			laMfAccessTest = new MfAccessTest();
			laMfAccessTest.setModal(true);
			laMfAccessTest
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laMfAccessTest.setData(null);
			laMfAccessTest.show();
			java.awt.Insets insets = laMfAccessTest.getInsets();
			laMfAccessTest.setSize(
				laMfAccessTest.getWidth() + insets.left + insets.right,
				laMfAccessTest.getHeight()
					+ insets.top
					+ insets.bottom);
			laMfAccessTest.setVisible(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(
				"Exception occurred in main() of com.txdot.isd.rts."
					+ "client.general.ui.RTSDialogBox");
			aeThrowable.printStackTrace(System.out);
		}
	}
	// end defect 9965

	/**
	 * MfAccessTest.java Constructor
	 */
	public MfAccessTest()
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
		VehicleInquiry laVehInq = new VehicleInquiry();

		if (aaAE.getSource() == getOK())
		{
			MfAccess laMFA = setMfAccesInstance();

			//Create the MFAccessInstance
			String lsCall =
				((String) getJComboBoxMfAccessMethod()
					.getSelectedItem())
					.trim();

			// add R99 function
			// Change the selected transaction to R99.
			if (!((String) getJComboBoxR99Selection()
				.getSelectedItem())
				.equals("NONE"))
			{
				setMFTransToR99();

			}
			// end R99 function

			//get all the values from the MFAccessTest Screen
			String lsDocNo = gettxtDocNo().getText().trim();
			String lsOwnrId = gettxtOwnrId().getText().trim();
			String lsRegPltNo = gettxtRegPltNo().getText().trim();
			String lsVIN = gettxtVin().getText().trim();
			String lsInvcNo = gettxtInvcNo().getText().trim();
			String lsCheckNo = gettxtCheckNo().getText().trim();

			// defect 10492 
			// TODO Need validation Routines 
			String lsPrmtVIN = gettxtPrmtVIN().getText();
			String lsPrmtNo = gettxtPrmtNo().getText();
			String lsPrmtId = gettxtPrmtIssuanceId().getText();
			String lsCustLstName = gettxtCustLstName().getText();
			String lsCustBsnName = gettxtCustBsnName().getText();
			RTSDate laBeginDate = null;
			RTSDate laEndDate = null;
			if (!gettxtBeginDate().isDateEmpty()
				&& gettxtBeginDate().isValidDate())
			{
				laBeginDate = gettxtBeginDate().getDate();
			}

			if (!gettxtEndDate().isDateEmpty()
				&& gettxtEndDate().isValidDate())
			{
				laEndDate = gettxtEndDate().getDate();
			}
			// end defect 10492  

			int liComptCntyNo = 0;
			int liFdsRptDate = 0;
			int liRptngDate = 0;
			int liFdsDueDate = 0;
			int liFdsPymntDate = 0;
			int liTraceNo = 0;
			int liSpclRegId = 0;
			int liNumOfTries = 0;
			try
			{
				liComptCntyNo =
					Integer.parseInt(
						gettxtComptCntyNo().getText().trim());
				liFdsRptDate =
					Integer.parseInt(
						gettxtFundsRptDate().getText().trim());
				liRptngDate =
					Integer.parseInt(
						gettxtRptngDate().getText().trim());
				liFdsDueDate =
					Integer.parseInt(
						gettxtFundsDueDate().getText().trim());
				liFdsPymntDate =
					Integer.parseInt(
						gettxtFundsPymntDate().getText().trim());
				liTraceNo =
					Integer.parseInt(gettxtTraceNo().getText().trim());
				liSpclRegId =
					Integer.parseInt(
						gettxtSpclRegId().getText().trim());
				liNumOfTries =
					Integer.parseInt(gettxtRetries().getText());

			}
			catch (NumberFormatException leNFEx)
			{
				leNFEx.printStackTrace();
			}

			//==========================================================
			// determine which mf call to make and do it.
			try
			{
				GeneralSearchData laGSD = new GeneralSearchData();

				int liLoop = 0;
				ciNoOfRecs = 0;
				ciCntIndex = 1;

				// First call, SendTrans
				// defect 8984
				//	Set MFTest type = SendTrans Test
				// defect 9965
				//	Add SendTrans push button check
				if (cbSendTransIndi)
				{
					// Initialize MFTest.
					MFTrans laMFTest = new MFTrans();
					laMFTest.setType(MFTrans.SENDTRANSTEST);
					laMFTest.setTransObjects(new Vector());
					laMFTest.setFileName(
						csTestFiles_Loc
							+ (String) getJComboBoxTestFiles()
								.getSelectedItem());
					// defect 10080
					//	Set CHANGETRANSTIME 
					if (!getchkboxChangeTransTime().isSelected())
					{
						laMFTest.csChangeTransTime = 0;
					}
					laMFA.sendTransaction(laMFTest);

					// Reset screen
					getstcLblSendTransFiles().setEnabled(false);
					getJComboBoxTestFiles().setEnabled(false);
					getstcLblSendTransFiles().setVisible(false);
					getJComboBoxTestFiles().setVisible(false);
					setVisibleScreenFields(true);
					getJComboBoxR99Selection().setSelectedIndex(0);
					cbSendTransIndi = false;
					// defect 10080
					//	Set TransTime check to not visible
					getchkboxChangeTransTime().setVisible(false);
					getchkboxChangeTransTime().setEnabled(false);
					getchkboxChangeTransTime().setSelected(false);
					laMFTest.csChangeTransTime = 1;

					// end defect 10080
				}
				// end defect 8984
				// FundsUpdate
				// defect 9965
				//	Set MFTest type = FundsUpdate Test
				else if (cbFundsUpdateIndi)
				{
					// Initialize MFTest.
					//					MFTrans laMFTest = new MFTrans();
					//					laMFTest.setType(MFTrans.FUNDSUPDATE);
					//					laMFTest.setTransObjects(new Vector());
					//					laMFTest.setFileName(
					//						csTestFiles_Loc
					//							+ (String) getJComboBoxTestFiles()
					//								.getSelectedItem());
					buildFundsData();
					//laMFA.sendFundsUpdateToMF(laMFTest);

					// Reset screen
					getstcLblSendTransFiles().setEnabled(false);
					getJComboBoxTestFiles().setEnabled(false);
					getstcLblSendTransFiles().setVisible(false);
					getJComboBoxTestFiles().setVisible(false);
					setVisibleScreenFields(true);
					getJComboBoxR99Selection().setSelectedIndex(0);
					cbSendTransIndi = false;
				}
				// end defect 8984
				//TODO Consolidate somehow?
				// defect 10492 
				else if (
					lsCall.equals(MFACCESS_RETRIEVE_PERMIT_PRMTID))
				{
					laGSD.setKey1(CommonConstant.PRMT_PRMTID);
					laGSD.setKey2(lsPrmtId);
					laGSD.setDate1(laBeginDate);
					laGSD.setDate2(laEndDate);
					laMFA.retrievePermit(laGSD);
				}
				else if (
					lsCall.equals(MFACCESS_RETRIEVE_PERMIT_PRMTNO))
				{
					laGSD.setKey1(CommonConstant.PRMT_PRMTNO);
					laGSD.setKey2(lsPrmtNo);
					laGSD.setDate1(laBeginDate);
					laGSD.setDate2(laEndDate);
					laMFA.retrievePermit(laGSD);
				}
				else if (
					lsCall.equals(
						MFACCESS_RETRIEVE_PERMIT_CUST_BSN_NAME))
				{
					laGSD.setKey1(CommonConstant.PRMT_BSNNAME);
					laGSD.setKey2(lsCustBsnName);
					laGSD.setDate1(laBeginDate);
					laGSD.setDate2(laEndDate);
					laMFA.retrievePermit(laGSD);
				}
				else if (
					lsCall.equals(
						MFACCESS_RETRIEVE_PERMIT_CUST_LST_NAME))
				{
					laGSD.setKey1(CommonConstant.PRMT_LSTNAME);
					laGSD.setKey2(lsCustLstName);
					laGSD.setDate1(laBeginDate);
					laGSD.setDate2(laEndDate);
					laMFA.retrievePermit(laGSD);
				}
				else if (lsCall.equals(MFACCESS_RETRIEVE_PERMIT_VIN))
				{
					laGSD.setKey1(CommonConstant.PRMT_VIN);
					laGSD.setKey2(lsPrmtVIN);
					laGSD.setDate1(laBeginDate);
					laGSD.setDate2(laEndDate);
					laMFA.retrievePermit(laGSD);
				}
				// end defect 10492
				else if (
					lsCall //if (lsCall
				.equals(
						MFACCESS_RETRIEVE_VEHICLE_FROM_ACTIVE_INACTIVE))
				{
					while (true)
					{

						if (lsDocNo
							!= null
								& !(lsDocNo
									.equals(
										CommonConstant
											.STR_SPACE_EMPTY)))
						{
							// R01
							laGSD.setKey1(CommonConstant.DOC_NO);
							laGSD.setKey2(lsDocNo);
							laGSD.setKey3(TransCdConstant.VEHINQ);
							String lsMFErrorData =
								CommonConstant.STR_SPACE_EMPTY;
							laGSD.setKey5(lsMFErrorData);
							//laMFA.retrieveVehicleFromActiveInactive(laGSD);
						}
						else if (
							lsOwnrId != null
								&& !(lsOwnrId
									.equals(
										CommonConstant
											.STR_SPACE_EMPTY)))
						{
							// R02
							laGSD.setKey1(CommonConstant.OWNER_ID);
							laGSD.setKey2(lsOwnrId);
							laGSD.setKey3(TransCdConstant.VEHINQ);
							String lsMFErrorData =
								CommonConstant.STR_SPACE_EMPTY;
							laGSD.setKey5(lsMFErrorData);

							//laMFA.retrieveVehicleFromMF(laGSD);
							//laMFA.retrieveVehicleFromActiveInactive(laGSD);
						}
						else if (
							lsRegPltNo != null
								&& !(lsRegPltNo
									.equals(
										CommonConstant
											.STR_SPACE_EMPTY)))
						{
							// R03
							laGSD.setKey1(CommonConstant.REG_PLATE_NO);
							laGSD.setKey2(lsRegPltNo);
							laGSD.setKey3(TransCdConstant.VEHINQ);
							String lsMFErrorData =
								CommonConstant.STR_SPACE_EMPTY;
							laGSD.setKey5(lsMFErrorData);
							//laMFA.retrieveVehicleFromMF(laGSD);
						}
						else if (
							lsVIN != null
								&& !(lsVIN
									.equals(
										CommonConstant
											.STR_SPACE_EMPTY)))
						{
							// R04
							laGSD.setKey1(CommonConstant.VIN);
							laGSD.setKey2(lsVIN);
							laGSD.setKey3(TransCdConstant.VEHINQ);
							String lsMFErrorData =
								CommonConstant.STR_SPACE_EMPTY;
							laGSD.setKey5(lsMFErrorData);

							//laMFA.retrieveVehicleFromMF(laGSD);
							//laMFA.retrieveVehicleFromActiveInactive(laGSD);
						}
						// defect 9403
						// Num of Tries > 0
						if (liNumOfTries == 0)
						{
							laMFA.retrieveVehicleFromMF(laGSD);
						}
						else
						{
							if (liLoop == 0)
							{
								System.out.println(
									"Getting first set of 100 records "
										+ "... Please wait - "
										+ ", TimeStamp: "
										+ new RTSDate().getTime());
							}
							getMfRecordWAS(laGSD);
						}
						if (++liLoop > liNumOfTries)
						{
							break;
						}
						gettxtRetries().setEnabled(false);
						gettxtRetries().setText("0");
					}

				}
				// End Active/Inactive search
				else if (
					lsCall.equals(
						MFACCESS_RETRIEVE_VEHICLE_FROM_ARCHIVE))
				{
					if (lsDocNo
						!= null
							& !(lsDocNo
								.equals(
									CommonConstant.STR_SPACE_EMPTY)))
					{
						// R01
						laGSD.setKey1(CommonConstant.DOC_NO);
						laGSD.setKey2(lsDocNo);
						laGSD.setKey3(TransCdConstant.VEHINQ);
						laGSD.setIntKey2(SEARCH_ARCHIVE);
						String lsMFErrorData =
							CommonConstant.STR_SPACE_EMPTY;
						laGSD.setKey5(lsMFErrorData);

						laMFA.retrieveVehicleFromMF(laGSD);
					}
					else if (
						lsOwnrId != null
							&& !(lsOwnrId
								.equals(
									CommonConstant.STR_SPACE_EMPTY)))
					{
						// R02
						laGSD.setKey1(CommonConstant.OWNER_ID);
						laGSD.setKey2(lsOwnrId);
						laGSD.setKey3(TransCdConstant.VEHINQ);
						laGSD.setIntKey2(SEARCH_ARCHIVE);
						String lsMFErrorData =
							CommonConstant.STR_SPACE_EMPTY;
						laGSD.setKey5(lsMFErrorData);

						laMFA.retrieveVehicleFromMF(laGSD);
					}
					else if (
						lsRegPltNo != null
							&& !(lsRegPltNo
								.equals(
									CommonConstant.STR_SPACE_EMPTY)))
					{
						// R03
						laGSD.setKey1(CommonConstant.REG_PLATE_NO);
						laGSD.setKey2(lsRegPltNo);
						laGSD.setKey3(TransCdConstant.VEHINQ);
						laGSD.setIntKey2(SEARCH_ARCHIVE);
						String lsMFErrorData =
							CommonConstant.STR_SPACE_EMPTY;
						laGSD.setKey5(lsMFErrorData);

						laMFA.retrieveVehicleFromMF(laGSD);
					}
					else if (
						lsVIN != null
							&& !(lsVIN
								.equals(
									CommonConstant.STR_SPACE_EMPTY)))
					{
						// R04
						laGSD.setKey1(CommonConstant.VIN);
						laGSD.setKey2(lsVIN);
						laGSD.setKey3(TransCdConstant.VEHINQ);
						laGSD.setIntKey2(SEARCH_ARCHIVE);
						String lsMFErrorData =
							CommonConstant.STR_SPACE_EMPTY;
						laGSD.setKey5(lsMFErrorData);

						laMFA.retrieveVehicleFromMF(laGSD);
					}
				}
				// End Archive search
				else if (lsCall.equals(MFACCESS_RETRIEVE_SP_SPCLREGID))
				{
					// R05
					laGSD.setKey1(CommonConstant.SPCL_REG_ID);
					laGSD.setKey2(String.valueOf(liSpclRegId));
					laGSD.setKey3(CommonConstant.STR_SPACE_EMPTY);
					String lsMFErrorData =
						CommonConstant.STR_SPACE_EMPTY;
					laGSD.setKey5(lsMFErrorData);

					laVehInq.retrieveSpclPltRegisResponse(laGSD);
				}
				else if (lsCall.equals(MFACCESS_VOID_TRANSACTIONS))
				{
					// R07
					laGSD.setKey1(lsDocNo);
					laMFA.voidTransactions(laGSD);
				}

				else if (lsCall.equals(MFACCESS_RETRIEVE_SP_PLATE))
				{
					// R08
					laGSD.setKey1(CommonConstant.REG_PLATE_NO);
					laGSD.setKey2(lsRegPltNo);
					laGSD.setKey3(TransCdConstant.SPAPPL);
					String lsMFErrorData =
						CommonConstant.STR_SPACE_EMPTY;
					laGSD.setKey5(lsMFErrorData);

					laVehInq.retrieveSpclPltRegisResponse(laGSD);
				}
				else if (
					lsCall.equals(MFACCESS_RETRIEVE_TITLE_IN_PROCESS))
				{
					// R09
					laMFA.retrieveTitleInProcess(lsDocNo);
				}
				else if (
					lsCall.equals(MFACCESS_RETRIEVE_VEHICLE_FROM_VINA))
				{
					// R10
					laGSD.setKey1(CommonConstant.VIN);
					laGSD.setKey2(lsVIN);
					String lsMFErrorData =
						CommonConstant.STR_SPACE_EMPTY;
					laGSD.setKey5(lsMFErrorData);

					// defect 9086
					//	Call retrieveMfRecordsByVIN()
					//laMFA.retrieveVehicleFromVINAOnly(laGSD);
					laMFA.retrieveMfRecordsByVIN(laGSD, MfAccess.R10);
					// end defect 9086
				}

				// defect 9965
				//	depercated
				//				else if (lsCall.equals(MFACCESS_RETRIEVE_NO_OF_DOCS))
				//				{
				//					// R12
				//					// defect 8984
				//					//	Set Key1 with DocNo
				//					//	Set Key2 with DocNo number
				//					laGSD.setKey1(CommonConstant.DOC_NO);
				//					laGSD.setKey2(lsDocNo);
				//					// end defect 8984
				//					laMFA.retrieveNumberofDocuments(laGSD);
				//				}
				// end defect 9965
				else if (
					lsCall.equals(
						MFACCESS_RETRIEVE_FDSREMITTANCE_RECORD))
				{
					// R14
					// defect 10112 
					// 2nd parameter not needed, not used 
					//String lsMFErrorData =
					//	CommonConstant.STR_SPACE_EMPTY;
					laMFA.retrieveFundsRemittanceRecord(liComptCntyNo);
					//	lsMFErrorData);
					// end defect 10112 
				}

				else if (lsCall.equals(MFACCESS_RETRIEVE_INVOICE))
				{
					// R17, R26
					laGSD.setIntKey1(liComptCntyNo);
					laGSD.setKey1(lsInvcNo);
					String lsMFErrorData =
						CommonConstant.STR_SPACE_EMPTY;
					laGSD.setKey5(lsMFErrorData);

					laMFA.retrieveInvoice(laGSD);
				}
				else if (lsCall.equals(MFACCESS_RETRIEVE_OWNER))
				{
					// trans R18
					laGSD.setKey4(lsOwnrId);
					laGSD.setKey5(CommonConstant.STR_SPACE_EMPTY);

					laMFA.retrieveOwner(laGSD);
				}
				else if (
					lsCall.equals(MFACCESS_RETRIEVE_VEH_BY_SPL_REGIS))
				{
					// trans R19
					laGSD.setKey1(CommonConstant.REG_PLATE_NO);
					laGSD.setKey2(lsRegPltNo);
					String lsMFErrorData =
						CommonConstant.STR_SPACE_EMPTY;
					laGSD.setKey5(lsMFErrorData);

					laMFA.retrieveVehicleBySpecialOwner(laGSD);
				}
				// defect 9965
				//	deprecated
				//				else if (
				//					lsCall.equals(MFACCESS_RETRIEVE_VEH_BY_SPL_OWNER))
				//				{
				//					// trans R20
				//					laGSD.setKey1(CommonConstant.OWNER_ID);
				//					laGSD.setKey2(lsOwnrId);
				//					String lsMFErrorData =
				//						CommonConstant.STR_SPACE_EMPTY;
				//					laGSD.setKey5(lsMFErrorData);
				//
				//					laMFA.retrieveVehicleBySpecialOwner(laGSD);
				//				}
				// end defect 9965
				else if (lsCall.equals(MFACCESS_RETRIEVE_JUNK_RECORDS))
				{
					// R28
					VehicleInquiryData laVID = new VehicleInquiryData();
					MFVehicleData laMFVD = new MFVehicleData();
					TitleData laTD = new TitleData();
					laTD.setDocNo(lsDocNo);
					laMFVD.setTitleData(laTD);
					laVID.setMfVehicleData(laMFVD);

					String lsMFErrorData =
						CommonConstant.STR_SPACE_EMPTY;

					laMFA.retrieveJunkRecords(laVID, lsMFErrorData);
				}
				else if (
					lsCall.equals(MFACCESS_RETRIEVE_FDSPYMNT_DATALIST))
				{
					//search with FundsReport date without rptngdat(R13, R30)
					if (liFdsRptDate != 0 && liRptngDate == 0)
					{
						laGSD.setKey1(CommonConstant.FUNDS_REPORT_DATE);
						RTSDate laRptDate =
							new RTSDate(RTSDate.YYYYMMDD, liFdsRptDate);

						laGSD.setDate1(laRptDate);
						laGSD.setIntKey1(liComptCntyNo);

						// defect 10112 
						// 2nd parameter not used 
						//String lsMFErrorData =
						//	CommonConstant.STR_SPACE_EMPTY;

						laMFA.retrieveFundsPaymentDataList(laGSD);
						//lsMFErrorData);
						// end defect 10112 
					}
					else if (liFdsRptDate != 0 && liRptngDate != 0)
					{
						// search with FundsReport date with rptngdate  (R13, R30)
						laGSD.setKey1(CommonConstant.FUNDS_REPORT_DATE);

						RTSDate laRptDate =
							new RTSDate(RTSDate.YYYYMMDD, liFdsRptDate);
						laGSD.setDate1(laRptDate);

						RTSDate laRptngDate =
							new RTSDate(RTSDate.YYYYMMDD, liRptngDate);
						laGSD.setDate2(laRptngDate);

						laGSD.setIntKey1(liComptCntyNo);

						// defect 10112 
						// 2nd parameter not needed, not used 
						//String lsMFErrorData =
						//	CommonConstant.STR_SPACE_EMPTY;

						laMFA.retrieveFundsPaymentDataList(laGSD);
						// lsMFErrorData);
						// end defect 10112 
					}
					else if (liFdsDueDate != 0)
					{
						//search with FundsPayment for Funds Due date  (R?)
						laGSD.setKey1(
							CommonConstant.FUNDS_PAYMENT_DATE);

						RTSDate laFdsDueDate =
							new RTSDate(RTSDate.YYYYMMDD, liFdsDueDate);
						laGSD.setDate1(laFdsDueDate);
						laGSD.setIntKey1(liComptCntyNo);

						// defect 10112 
						// 2nd parameter not needed, not used 
						//String lsMFErrorData =
						//	CommonConstant.STR_SPACE_EMPTY;

						laMFA.retrieveFundsPaymentDataList(laGSD);
						//	lsMFErrorData);
						// end defect 10112 
					}
					else if (liTraceNo != 0)
					{
						//search with Trace no   (R31)
						laGSD.setKey1(CommonConstant.TRACE_NO);
						laGSD.setKey2(Integer.toString(liTraceNo));
						laGSD.setIntKey1(liComptCntyNo);

						// defect 10112 
						// 2nd parameter not needed, not used 
						//String lsMFErrorData =
						//	CommonConstant.STR_SPACE_EMPTY;

						laMFA.retrieveFundsPaymentDataList(laGSD);
						//	lsMFErrorData);
						// end defect 10112 
					}
					else if (
						lsCheckNo != null
							&& !(lsCheckNo
								.equals(
									CommonConstant.STR_SPACE_EMPTY)))
					{
						//search with Check no    (R15)
						laGSD.setKey1(CommonConstant.CHECK_NO);

						laGSD.setKey2(lsCheckNo);
						laGSD.setIntKey1(liComptCntyNo);

						// defect 10112 
						// 2nd parameter not needed, not used 
						//String lsMFErrorData =
						//	CommonConstant.STR_SPACE_EMPTY;

						laMFA.retrieveFundsPaymentDataList(laGSD);
						//lsMFErrorData);
						// end defect 10112 
					}
					else if (liFdsPymntDate != 0)
					{
						//search with Payment Date    (R32)
						laGSD.setKey1(
							CommonConstant.FUNDS_PAYMENT_DATE);

						laGSD.setKey2(lsCheckNo);
						laGSD.setIntKey1(liComptCntyNo);

						RTSDate laRptDate =
							new RTSDate(
								RTSDate.YYYYMMDD,
								liFdsPymntDate);
						laGSD.setDate1(laRptDate);

						// defect 10112 
						// 2nd parameter not needed, not used 
						//String lsMFErrorData =
						//	CommonConstant.STR_SPACE_EMPTY;

						laMFA.retrieveFundsPaymentDataList(laGSD);
						//lsMFErrorData);
						// end defect 10112 
					}
					//End of test for FUNDS PAYMENT 
				}
				if (lsCall.equals(MFACCESS_REGIS_BY_DOCNO))
				{
					// R33
					laGSD.setKey1(CommonConstant.DOC_NO);
					laGSD.setKey2(lsDocNo);
					String lsMFErrorData =
						CommonConstant.STR_SPACE_EMPTY;
					laGSD.setKey5(lsMFErrorData);
					laMFA.retrieveMfRecordsByDocNo(laGSD, MfAccess.R33);
				}
			}
			catch (RTSException aeRTSEx)
			{
				// Just print te stack trace and exit
				aeRTSEx.printStackTrace();
				System.exit(16);
			}

			// defect 8984
			//	Reset MfAccess.R## to original values
			resetMFTransR99();
			// end defect 8984
		}

		// defect 8984
		//	populate the SendTransFileLoc combo box
		else if (aaAE.getSource() == getJComboBoxMfVersion())
		{
			populateSendTransFundsFiles(aaAE);
		}
		// end defect 8984
		// defect 9965
		//	Run SendTrans from push button
		else if (aaAE.getSource() == getbtnSendTrans())
		{
			setComboBoxSendTrans();
			populateSendTransFundsFiles(aaAE);
			cbSendTransIndi = true;
		}
		//	Run SendTrans from push button
		else if (aaAE.getSource() == getbtnFundsUpdate())
		{
			setComboBoxSendTrans();
			populateSendTransFundsFiles(aaAE);
			cbFundsUpdateIndi = true;
		}
		// end defect 9965
	}
	/**
	 * Build FundsUpdateData object
	 */
	private void buildFundsData()
	{
		FundsUpdateData laFundsUpdtData = new FundsUpdateData();
		FundsDueData laFundsDueData = new FundsDueData();
		MfAccess laMfa = new MfAccess();
		Vector lvFundsDueData = new Vector();
		final int HEADER_SIZE = 41;
		final int RECORD_SIZE = 41;

		// Header positions
		//define all offsets and lengths
		final int COMPT_CNTY_NO_OFFSET = 0;
		final int COMPT_CNTY_NO_LENGTH = 3;
		final int FUNDS_PYMNT_DATE_OFFSET = 3;
		final int FUNDS_PYMNT_DATE_LENGTH = 8;
		final int ACCNTNOCD_OFFSET = 11;
		final int ACCNTNOCD_LENGTH = 1;
		final int TRACENO_OFFSET = 12;
		final int TRACENO_LENGTH = 9;
		final int TRANSEMPID_OFFSET = 21;
		final int TRANSEMPID_LENGTH = 7;
		final int OFCISSUANCENO_OFFSET = 28;
		final int OFCISSUANCENO_LENGTH = 3;
		final int PYMNTTYPECD_OFFSET = 31;
		final int PYMNTTYPECD_LENGTH = 2;
		final int PYMNTSTATUSCD_OFFSET = 33;
		final int PYMNTSTATUSCD_LENGTH = 1;
		final int CKNO_OFFSET = 34;
		final int CKNO_LENGTH = 7;

		final int FUNDS_RPT_DATE_OFFSET = 0;
		final int FUNDS_RPT_DATE_LENGTH = 8;
		final int RPTNG_DATE_OFFSET = 8;
		final int RPTNG_DATE_LENGTH = 8;
		final int REMIT_AMT_OFFSET = 16;
		final int REMIT_AMT_LENGTH = 11;
		final int REMIT_AMT_DECIMAL = 2;
		final int FUNDS_CAT_OFFSET = 27;
		final int FUNDS_CAT_LENGTH = 14;

		// Get Funds base folder
		//	T:\RTS II Team\DEV\Build\MF Access\FundsUpdates\
		String lsFundsBase = SystemProperty.getFundsUpdateFileLoc();
		// Get MfInterfaceVersionCode
		String lsVerCd =
			getJComboBoxMfVersion().getSelectedItem().toString();
		// Add MfInterfaceVersionCode to Fund folder
		// Remove the T from T_SENDTRANS and replace with VersionCode
		String lsFundsFolder = lsVerCd + T_FUNDSUPDT.substring(1);
		// Get file name
		String lsFileName =
			getJComboBoxTestFiles().getSelectedItem().toString();
		// Build complete file name
		String lsFullName =
			lsFundsBase + lsFundsFolder + "\\" + lsFileName;
		// Get Funds data. Copy data to Vector. There will be 1 line.
		//	containing the Funds then Funds Due
		Vector lvData = FileUtil.copyFileToVector(lsFullName);
		String lsData = (String) lvData.get(0);

		// Load data to Funds Data
		laFundsUpdtData.setComptCountyNo(
			Integer.parseInt(
				laMfa.trimMfString(
					lsData.substring(
						COMPT_CNTY_NO_OFFSET,
						COMPT_CNTY_NO_OFFSET + COMPT_CNTY_NO_LENGTH))));
		laFundsUpdtData.setFundsPaymentDate(
			new RTSDate(
				RTSDate.YYYYMMDD,
				Integer.parseInt(
					laMfa.trimMfString(
						lsData.substring(
							FUNDS_PYMNT_DATE_OFFSET,
							FUNDS_PYMNT_DATE_OFFSET
								+ FUNDS_PYMNT_DATE_LENGTH)))));
		laFundsUpdtData.setAccountNoCd(
			Integer.parseInt(
				laMfa.trimMfString(
					lsData.substring(
						ACCNTNOCD_OFFSET,
						ACCNTNOCD_OFFSET + ACCNTNOCD_LENGTH))));
		laFundsUpdtData.setTraceNo(
			Integer.parseInt(
				laMfa.trimMfString(
					lsData.substring(
						TRACENO_OFFSET,
						TRACENO_OFFSET + TRACENO_LENGTH))));
		laFundsUpdtData.setTransEmpId(
			laMfa.trimMfString(
				lsData.substring(
					TRANSEMPID_OFFSET,
					TRANSEMPID_OFFSET + TRANSEMPID_LENGTH)));
		laFundsUpdtData.setOfcIssuanceNo(
			Integer.parseInt(
				laMfa.trimMfString(
					lsData.substring(
						OFCISSUANCENO_OFFSET,
						OFCISSUANCENO_OFFSET + OFCISSUANCENO_LENGTH))));
		laFundsUpdtData.setPaymentTypeCd(
			Integer.parseInt(
				laMfa.trimMfString(
					lsData.substring(
						PYMNTTYPECD_OFFSET,
						PYMNTTYPECD_OFFSET + PYMNTTYPECD_LENGTH))));
		laFundsUpdtData.setPaymentStatusCd(
			laMfa.trimMfString(
				lsData.substring(
					PYMNTSTATUSCD_OFFSET,
					PYMNTSTATUSCD_OFFSET + PYMNTSTATUSCD_LENGTH)));
		laFundsUpdtData.setCheckNo(
			laMfa.trimMfString(
				lsData.substring(
					CKNO_OFFSET,
					CKNO_OFFSET + CKNO_LENGTH)));

		//find number of FDSUPDB records
		int liTotFundsRecs =
			lsData.substring(HEADER_SIZE).length() / RECORD_SIZE;
		// Set Begin/End position for record
		int liCharLenPos = RECORD_SIZE + RECORD_SIZE;
		int liBegRecPos = RECORD_SIZE;
		// Build Funds Due data
		// Add records to vector
		for (int liIndex = 0; liIndex < liTotFundsRecs; liIndex++)
		{
			String lsWorkingData =
				lsData.substring(liBegRecPos, liCharLenPos);
			laFundsDueData.setFundsReportDate(
				new RTSDate(
					RTSDate.YYYYMMDD,
					Integer.parseInt(
						laMfa.trimMfString(
							lsWorkingData.substring(
								FUNDS_RPT_DATE_OFFSET,
								FUNDS_RPT_DATE_OFFSET
									+ FUNDS_RPT_DATE_LENGTH)))));
			laFundsDueData.setReportingDate(
				new RTSDate(
					RTSDate.YYYYMMDD,
					Integer.parseInt(
						laMfa.trimMfString(
							lsWorkingData.substring(
								RPTNG_DATE_OFFSET,
								RPTNG_DATE_OFFSET
									+ RPTNG_DATE_LENGTH)))));

			// Parse the integer from decimal. Set type as dollar
			laFundsDueData.setRemitAmount(
				new Dollar(
					laMfa.trimMfString(
						lsWorkingData.substring(
							REMIT_AMT_OFFSET,
							REMIT_AMT_OFFSET
								+ REMIT_AMT_LENGTH
								- REMIT_AMT_DECIMAL)
							+ '.'
							+ lsWorkingData.substring(
								+REMIT_AMT_LENGTH - REMIT_AMT_DECIMAL,
								REMIT_AMT_LENGTH))));
			laFundsDueData.setFundsCategory(
				laMfa.trimMfString(
					lsWorkingData.substring(
						FUNDS_CAT_OFFSET,
						FUNDS_CAT_OFFSET + FUNDS_CAT_LENGTH)));

			lvFundsDueData.add(laFundsDueData);
			laFundsDueData = new FundsDueData();
			liBegRecPos = liBegRecPos + RECORD_SIZE;
			liCharLenPos = liCharLenPos + RECORD_SIZE;
		}
		laFundsUpdtData.setFundsDue(lvFundsDueData);

		try
		{
			// defect 10112 
			// 2nd parameter not needed, not used 
			laMfa.sendFundsUpdateToMF(laFundsUpdtData);
			//CommonConstant.STR_SPACE_EMPTY);
			// end defect 10112 
		}
		catch (RTSException aeRTSEx)
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.JAVA_ERROR,
					"Funds Update failed.",
					"FundsUpdate");
			leRTSEx.displayError();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e)
	{
		// Set Reties enabled/disabled
		if (e.getSource() == gettxtDocNo()
			|| e.getSource() == gettxtRegPltNo()
			|| e.getSource() == gettxtVin())
		{
			if (((String) getJComboBoxMfAccessMethod()
				.getSelectedItem())
				.trim()
				.equals(MFACCESS_RETRIEVE_VEHICLE_FROM_ACTIVE_INACTIVE))
			{
				gettxtRetries().setEnabled(true);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e)
	{

	}
	/**
	 * This method initializes ivjFundsUpdate
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.ivjFundsUpdate
	 */
	private RTSButton getbtnFundsUpdate()
	{
		if (ivjFundsUpdate == null)
		{
			try
			{
				ivjFundsUpdate = new RTSButton();
				ivjFundsUpdate.setBounds(104, 5, 93, 22);
				ivjFundsUpdate.setText(FUNDS);
				ivjFundsUpdate.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFundsUpdate;
	}
	/**
	 * This method initializes ivjbtnSendTrans
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.ivjFundsUpdate
	 */
	private RTSButton getbtnSendTrans()
	{
		if (ivjbtnSendTrans == null)
		{
			try
			{
				ivjbtnSendTrans = new RTSButton();
				ivjbtnSendTrans.setBounds(4, 5, 97, 22);
				// user code begin {1}
				ivjbtnSendTrans.addActionListener(this);
				ivjbtnSendTrans.setText(SENDTRANS);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnSendTrans;
	}
	/**
	 * This method initializes wsdchkboxChangeTransTime
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkboxChangeTransTime()
	{
		if (ivjchkboxChangeTransTime == null)
		{
			try
			{
				ivjchkboxChangeTransTime = new javax.swing.JCheckBox();
				ivjchkboxChangeTransTime.setName(
					"chkboxChangeTransTime");
				ivjchkboxChangeTransTime.setBounds(202, 7, 155, 21);
				ivjchkboxChangeTransTime.setText("Change TransTime");
				ivjchkboxChangeTransTime.setVisible(false);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkboxChangeTransTime;
	}

	/**
	 * Return the JComboBox3 property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getJComboBoxMfAccessMethod()
	{
		if (ivjJComboBoxMfAccessMethod == null)
		{
			try
			{
				ivjJComboBoxMfAccessMethod =
					new javax.swing.JComboBox();
				ivjJComboBoxMfAccessMethod.setName(
					"JComboBoxMfAccessMethod");
				ivjJComboBoxMfAccessMethod.setBounds(261, 82, 388, 23);
				// user code begin {1}
				populateMfAccessMethods();
				ivjJComboBoxMfAccessMethod.addItemListener(this);
				//ivjJComboBoxMfAccessMethod.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJComboBoxMfAccessMethod;
	}

	/**
	 * Return the JComboBox1 property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getJComboBoxMfDBVersion()
	{
		if (ivjJComboBoxMfDBVersion == null)
		{
			try
			{
				ivjJComboBoxMfDBVersion = new javax.swing.JComboBox();
				ivjJComboBoxMfDBVersion.setName("JComboBoxMfDBVersion");
				ivjJComboBoxMfDBVersion.setBounds(159, 93, 74, 23);
				// user code begin {1}
				populateMfDB();
				//ivjJComboBoxMfDBVersion.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJComboBoxMfDBVersion;
	}

	/**
	 * Return the JComboBox2 property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getJComboBoxMfVersion()
	{
		if (ivjJComboBoxMfVersion == null)
		{
			try
			{
				ivjJComboBoxMfVersion = new javax.swing.JComboBox();
				ivjJComboBoxMfVersion.setName("JComboBoxMfVersion");
				ivjJComboBoxMfVersion.setBounds(158, 55, 73, 23);
				ivjJComboBoxMfVersion.setActionCommand(
					"comboBoxChanged");
				// user code begin {1}
				populateMfVersion();
				// defect 6701
				// select "T" as a default
				// 0 = T
				// 1 = U
				// 2 = V
				//ivjJComboBoxMfVersion.setSelectedIndex(0);
				// end defect 6701
				ivjJComboBoxMfVersion.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJComboBoxMfVersion;
	}

	/**
	 * Return the JComboBoxR99Selection property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getJComboBoxR99Selection()
	{
		if (ivjJComboBoxR99Selection == null)
		{
			try
			{
				ivjJComboBoxR99Selection = new javax.swing.JComboBox();
				ivjJComboBoxR99Selection.setName(
					"JComboBoxR99Selection");
				ivjJComboBoxR99Selection.setBounds(95, 490, 130, 24);
				// user code begin {1}
				populateMfTrans();
				ivjJComboBoxR99Selection.setSelectedIndex(0);
				ivjJComboBoxR99Selection.addItemListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJComboBoxR99Selection;
	}

	/**
	 * Return the JComboBox1 property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getJComboBoxTestFiles()
	{
		if (ivjJComboBoxSendTransFiles == null)
		{
			try
			{
				ivjJComboBoxSendTransFiles =
					new javax.swing.JComboBox();
				ivjJComboBoxSendTransFiles.setName(
					"JComboBoxSendTransFiles");
				ivjJComboBoxSendTransFiles.setBounds(261, 93, 280, 23);
				// user code begin {1}
				ivjJComboBoxSendTransFiles.setVisible(false);
				ivjJComboBoxSendTransFiles.addItemListener(this);
				//				populateSendTransFiles();
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJComboBoxSendTransFiles;
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
			jPanel.add(getbtnSendTrans(), null);
			jPanel.add(getbtnFundsUpdate(), null);
			jPanel.add(getchkboxChangeTransTime(), null);
			jPanel.setBounds(21, 448, 363, 30);
		}
		return jPanel;
	}
	/**
	 * This method initializes ivjJPanelPermit
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelPermit()
	{
		if (ivjJPanelPermit == null)
		{
			ivjJPanelPermit = new javax.swing.JPanel();
			ivjJPanelPermit.setLayout(null);
			ivjJPanelPermit.add(getstcLblPrmtNo(), null);
			ivjJPanelPermit.add(gettxtPrmtNo(), null);
			ivjJPanelPermit.add(getstcLblPrmtVIN(), null);
			ivjJPanelPermit.add(gettxtPrmtVIN(), null);
			ivjJPanelPermit.add(getstcLblCustLstName(), null);
			ivjJPanelPermit.add(getstcLblCustBsnName(), null);
			ivjJPanelPermit.add(gettxtCustLstName(), null);
			ivjJPanelPermit.add(gettxtCustBsnName(), null);
			ivjJPanelPermit.add(getstcLblPrmtIssuanceId(), null);
			ivjJPanelPermit.add(gettxtPrmtIssuanceId(), null);
			ivjJPanelPermit.add(getstcLblBeginDate(), null);
			ivjJPanelPermit.add(getstcLblEndDate(), null);
			ivjJPanelPermit.add(gettxtBeginDate(), null);
			ivjJPanelPermit.add(gettxtEndDate(), null);
			ivjJPanelPermit.setBounds(10, 310, 621, 132);
			// user code begin {1}
			Border laBorder =
				new TitledBorder(
					new EtchedBorder(),
					"Select Permit Key:");
			ivjJPanelPermit.setBorder(laBorder);
			// user code end
		}
		return ivjJPanelPermit;
	}

	/**
	 * Returns a connection to the CICS Java Gateway.
	 * 
	 * @return MfAccess
	 * @param asJavaGate String
	 * @param aiJavaGatewayPort int
	 * @param asMFTimeout String
	 * @param asMFDB String
	 * @param asMFTransVersion String
	 * @param asMFServer String
	 * @param asMFJGate String
	 * @param aiMFDebugMode int
	 */
	private MfAccess getMFAccessInstance(
		String asJavaGate,
		int aiJavaGatewayPort,
		String asMFTimeout,
		String asMFDB,
		String asMFTransVersion,
		String asMFServer,
		String asMFJGate,
		int aiMFDebugMode)
	{
		// set up client name
		String lsNetName = "UNKNOWN";
		try
		{
			lsNetName = InetAddress.getLocalHost().getHostName();
		}
		catch (java.net.UnknownHostException leUKHEx)
		{
			System.out.println(
				"UnknownHost Exception in MainFrameAccess");
		}

		MfAccess laMFA =
			new MfAccess(
				asJavaGate,
				aiJavaGatewayPort,
				asMFTimeout,
				asMFDB,
				asMFTransVersion,
				asMFServer,
				asMFJGate,
				aiMFDebugMode,
				lsNetName);
		return laMFA;
	}

	/**
	 * Return the MfAccessTestContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getMfAccessTestContentPane1()
	{
		if (ivjMfAccessTestContentPane1 == null)
		{
			try
			{
				ivjMfAccessTestContentPane1 = new javax.swing.JPanel();
				ivjMfAccessTestContentPane1.setName(
					"MfAccessTestContentPane1");
				ivjMfAccessTestContentPane1.setLayout(null);
				ivjMfAccessTestContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjMfAccessTestContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				getMfAccessTestContentPane1().add(
					getstcLblDocNo(),
					getstcLblDocNo().getName());
				getMfAccessTestContentPane1().add(
					getstcLblSendTransFiles(),
					getstcLblSendTransFiles().getName());
				getMfAccessTestContentPane1().add(
					getstcLblOwnrId(),
					getstcLblOwnrId().getName());
				getMfAccessTestContentPane1().add(
					getstcLblRegPltNo(),
					getstcLblRegPltNo().getName());
				getMfAccessTestContentPane1().add(
					getstcLblVin(),
					getstcLblVin().getName());
				getMfAccessTestContentPane1().add(
					getstcLblInvcNo(),
					getstcLblInvcNo().getName());
				getMfAccessTestContentPane1().add(
					getstcLblComptCntyNo(),
					getstcLblComptCntyNo().getName());
				getMfAccessTestContentPane1().add(
					getstcLblFundsRptDate(),
					getstcLblFundsRptDate().getName());
				getMfAccessTestContentPane1().add(
					getstcLblRptngDate(),
					getstcLblRptngDate().getName());
				getMfAccessTestContentPane1().add(
					getstcLblFundsDueDate(),
					getstcLblFundsDueDate().getName());
				getMfAccessTestContentPane1().add(
					getstcLblFundsPymntDate(),
					getstcLblFundsPymntDate().getName());
				getMfAccessTestContentPane1().add(
					getstcLblTraceNo(),
					getstcLblTraceNo().getName());
				getMfAccessTestContentPane1().add(
					getstcLblCheckNo(),
					getstcLblCheckNo().getName());
				getMfAccessTestContentPane1().add(
					getstcLblHeading(),
					getstcLblHeading().getName());
				getMfAccessTestContentPane1().add(
					getstcLblVersionMfAPPC(),
					getstcLblVersionMfAPPC().getName());
				getMfAccessTestContentPane1().add(
					getstcLblMfAccessMethod(),
					getstcLblMfAccessMethod().getName());
				getMfAccessTestContentPane1().add(
					getstcLblMfDB(),
					getstcLblMfDB().getName());
				ivjMfAccessTestContentPane1.add(
					getstcLblSpclRegId(),
					getstcLblSpclRegId().getName());
				getMfAccessTestContentPane1().add(
					getJComboBoxMfVersion(),
					getJComboBoxMfVersion().getName());
				getMfAccessTestContentPane1().add(
					getJComboBoxMfDBVersion(),
					getJComboBoxMfDBVersion().getName());
				getMfAccessTestContentPane1().add(
					getJComboBoxMfAccessMethod(),
					getJComboBoxMfAccessMethod().getName());
				getMfAccessTestContentPane1().add(
					gettxtDocNo(),
					gettxtDocNo().getName());
				getMfAccessTestContentPane1().add(
					gettxtOwnrId(),
					gettxtOwnrId().getName());
				getMfAccessTestContentPane1().add(
					gettxtRegPltNo(),
					gettxtRegPltNo().getName());
				getMfAccessTestContentPane1().add(
					gettxtVin(),
					gettxtVin().getName());
				getMfAccessTestContentPane1().add(
					gettxtInvcNo(),
					gettxtInvcNo().getName());
				getMfAccessTestContentPane1().add(
					gettxtComptCntyNo(),
					gettxtComptCntyNo().getName());
				getMfAccessTestContentPane1().add(
					gettxtFundsRptDate(),
					gettxtFundsRptDate().getName());
				getMfAccessTestContentPane1().add(
					gettxtRptngDate(),
					gettxtRptngDate().getName());
				getMfAccessTestContentPane1().add(
					gettxtFundsDueDate(),
					gettxtFundsDueDate().getName());
				getMfAccessTestContentPane1().add(
					gettxtFundsPymntDate(),
					gettxtFundsPymntDate().getName());
				getMfAccessTestContentPane1().add(
					gettxtTraceNo(),
					gettxtTraceNo().getName());
				getMfAccessTestContentPane1().add(
					gettxtCheckNo(),
					gettxtCheckNo().getName());
				getMfAccessTestContentPane1().add(
					getOK(),
					getOK().getName());
				getMfAccessTestContentPane1().add(
					gettxtSpclRegId(),
					gettxtSpclRegId().getName());
				getMfAccessTestContentPane1().add(
					getJComboBoxR99Selection(),
					getJComboBoxR99Selection().getName());
				getMfAccessTestContentPane1().add(
					getstclblR99(),
					getstclblR99().getName());
				// user code begin {1}
				// defect 8984
				ivjMfAccessTestContentPane1.add(
					getJComboBoxTestFiles(),
					getJComboBoxTestFiles().getName());
				ivjMfAccessTestContentPane1.add(
					getstclblRetries(),
					null);
				// defect 8984
				// Add Timeout
				ivjMfAccessTestContentPane1.add(
					getstclblTimeout(),
					null);
				ivjMfAccessTestContentPane1.add(gettxtTimeout(), null);
				// end defect 8984
				// user code end
				ivjMfAccessTestContentPane1.add(gettxtRetries(), null);
				ivjMfAccessTestContentPane1.add(getJPanel(), null);
				ivjMfAccessTestContentPane1.add(
					getJPanelPermit(),
					null);
				ivjMfAccessTestContentPane1.setSize(670, 529);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMfAccessTestContentPane1;
	}

	/**
	 * 
	 */
	private void getMfRecordWAS(GeneralSearchData aaGSD)
	{
		BusinessInterface laBusinessInterface = new BusinessInterface();

		try
		{

			Object laMfResponse =
				laBusinessInterface.processData(
					GeneralConstant.COMMON,
					CommonConstant.GET_VEH,
					aaGSD);

			VehicleInquiryData laVehData =
				(VehicleInquiryData) laMfResponse;
			ciNoOfRecs = ciNoOfRecs + laVehData.getNoMFRecs();
			if (ciNoOfRecs / 100 == ciCntIndex)
			{
				System.out.println(
					"Search by "
						+ aaGSD.getKey1()
						+ " - "
						+ aaGSD.getKey2()
						+ "; Number of successful attempts - "
						+ String.valueOf(ciNoOfRecs)
						+ ", TimeStamp: "
						+ new RTSDate().getTime()
						+ ": Number of records founds = "
						+ String.valueOf(laVehData.getNoMFRecs()));
				ciCntIndex++;
			}

		}
		catch (RTSException aeRTSEx)
		{
			//create the RTSException with MF Down - 
			// Cannot get a connection to MF
			RTSException leRTSEx =
				new RTSException(RTSException.MF_DOWN_MSG);
			leRTSEx.setMsgType(RTSException.MF_DOWN);
			//print the error to stderr and then throw the exception
			// defect 9403
			//	Write message to error log
			System.err.println("MfAccess error .. Server down ");
			leRTSEx.printStackTrace();

		}
	}

	/**
	 * Return the OK property value.
	 * 
	 * @return ivjFundsUpdate
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com.txdot.isd.rts.client.general.ui.RTSButton getOK()
	{
		if (ivjOK == null)
		{
			try
			{
				ivjOK =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjOK.setName("OK");
				ivjOK.setText("OK");
				ivjOK.setBounds(279, 490, 101, 24);
				// user code begin {1}
				ivjOK.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjOK;
	}

	/**
	 * This method initializes ivjstcLblBeginDate
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBeginDate()
	{
		if (ivjstcLblBeginDate == null)
		{
			ivjstcLblBeginDate = new JLabel();
			ivjstcLblBeginDate.setBounds(330, 75, 92, 20);
			ivjstcLblBeginDate.setText("Begin Date:");
			ivjstcLblBeginDate.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblBeginDate;
	}

	/**
	 * Return the JLabel113 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblCheckNo()
	{
		if (ivjstcLblCheckNo == null)
		{
			try
			{
				ivjstcLblCheckNo = new javax.swing.JLabel();
				ivjstcLblCheckNo.setName("stcLblCheckNo");
				ivjstcLblCheckNo.setText("CheckNo:");
				ivjstcLblCheckNo.setBounds(371, 277, 62, 20);
				ivjstcLblCheckNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblCheckNo;
	}

	/**
	 * Return the JLabel17 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblComptCntyNo()
	{
		if (ivjstcLblComptCntyNo == null)
		{
			try
			{
				ivjstcLblComptCntyNo = new javax.swing.JLabel();
				ivjstcLblComptCntyNo.setName("stcLblComptCntyNo");
				ivjstcLblComptCntyNo.setText("ComptCntyNo:");
				ivjstcLblComptCntyNo.setBounds(349, 116, 84, 20);
				ivjstcLblComptCntyNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblComptCntyNo;
	}

	/**
	 * This method initializes ivjstcLblCustBsnName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCustBsnName()
	{
		if (ivjstcLblCustBsnName == null)
		{
			ivjstcLblCustBsnName = new JLabel();
			ivjstcLblCustBsnName.setSize(96, 20);
			ivjstcLblCustBsnName.setText("CustBsnName:");
			ivjstcLblCustBsnName.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblCustBsnName.setLocation(326, 48);
		}
		return ivjstcLblCustBsnName;
	}

	/**
	 * This method initializes ivjstcLblCustLstName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCustLstName()
	{
		if (ivjstcLblCustLstName == null)
		{
			ivjstcLblCustLstName = new JLabel();
			ivjstcLblCustLstName.setBounds(325, 21, 97, 20);
			ivjstcLblCustLstName.setText("CustLstName:");
			ivjstcLblCustLstName.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblCustLstName;
	}

	/**
	 * Return the JLabel1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblDocNo()
	{
		if (ivjstcLblDocNo == null)
		{
			try
			{
				ivjstcLblDocNo = new javax.swing.JLabel();
				ivjstcLblDocNo.setName("stcLblDocNo");
				ivjstcLblDocNo.setText("DocNo:");
				ivjstcLblDocNo.setBounds(78, 142, 51, 20);
				ivjstcLblDocNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblDocNo;
	}

	/**
	 * This method initializes ivjstcLblEndDate
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEndDate()
	{
		if (ivjstcLblEndDate == null)
		{
			ivjstcLblEndDate = new javax.swing.JLabel();
			ivjstcLblEndDate.setBounds(326, 101, 96, 20);
			ivjstcLblEndDate.setText("End Date:");
			ivjstcLblEndDate.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblEndDate;
	}

	/**
	 * Return the JLabel110 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblFundsDueDate()
	{
		if (ivjstcLblFundsDueDate == null)
		{
			try
			{
				ivjstcLblFundsDueDate = new javax.swing.JLabel();
				ivjstcLblFundsDueDate.setName("stcLblFundsDueDate");
				ivjstcLblFundsDueDate.setText("FundsDueDate:");
				ivjstcLblFundsDueDate.setBounds(348, 196, 85, 20);
				ivjstcLblFundsDueDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblFundsDueDate;
	}

	/**
	 * Return the JLabel111 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblFundsPymntDate()
	{
		if (ivjstcLblFundsPymntDate == null)
		{
			try
			{
				ivjstcLblFundsPymntDate = new javax.swing.JLabel();
				ivjstcLblFundsPymntDate.setName("stcLblFundsPymntDate");
				ivjstcLblFundsPymntDate.setText("FundsPymntDate:");
				ivjstcLblFundsPymntDate.setBounds(328, 223, 105, 20);
				ivjstcLblFundsPymntDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblFundsPymntDate;
	}

	/**
	 * Return the JLabel18 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblFundsRptDate()
	{
		if (ivjstcLblFundsRptDate == null)
		{
			try
			{
				ivjstcLblFundsRptDate = new javax.swing.JLabel();
				ivjstcLblFundsRptDate.setName("stcLblFundsRptDate");
				ivjstcLblFundsRptDate.setText("FundsRptDate:");
				ivjstcLblFundsRptDate.setBounds(349, 143, 84, 20);
				ivjstcLblFundsRptDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblFundsRptDate;
	}

	/**
	 * Return the stcLblHeading property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblHeading()
	{
		if (ivjstcLblHeading == null)
		{
			try
			{
				ivjstcLblHeading = new javax.swing.JLabel();
				ivjstcLblHeading.setName("stcLblHeading");
				ivjstcLblHeading.setText(
					"SELECT ANY METHOD AND ENTER ANY SEARCH VALUES");
				ivjstcLblHeading.setBounds(167, 14, 323, 22);
				ivjstcLblHeading.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblHeading;
	}

	/**
	 * Return the JLabel15 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblInvcNo()
	{
		if (ivjstcLblInvcNo == null)
		{
			try
			{
				ivjstcLblInvcNo = new javax.swing.JLabel();
				ivjstcLblInvcNo.setName("stcLblInvcNo");
				ivjstcLblInvcNo.setText("InvcNo:");
				ivjstcLblInvcNo.setBounds(84, 277, 45, 20);
				ivjstcLblInvcNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblInvcNo;
	}
	/**
	 * Return the stcLblMfAccessMethod property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblMfAccessMethod()
	{
		if (ivjstcLblMfAccessMethod == null)
		{
			try
			{
				ivjstcLblMfAccessMethod = new javax.swing.JLabel();
				ivjstcLblMfAccessMethod.setName("stcLblMfAccessMethod");
				ivjstcLblMfAccessMethod.setText("MfAccessMethod:");
				ivjstcLblMfAccessMethod.setBounds(260, 60, 111, 14);
				ivjstcLblMfAccessMethod.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblMfAccessMethod;
	}

	/**
	 * Return the stcLblMfDB property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblMfDB()
	{
		if (ivjstcLblMfDB == null)
		{
			try
			{
				ivjstcLblMfDB = new javax.swing.JLabel();
				ivjstcLblMfDB.setName("stcLblMfDB");
				ivjstcLblMfDB.setText("MfDatabase:");
				ivjstcLblMfDB.setBounds(64, 99, 82, 14);
				ivjstcLblMfDB.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblMfDB;
	}

	/**
	 * Return the JLabel11 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblOwnrId()
	{
		if (ivjstcLblOwnrId == null)
		{
			try
			{
				ivjstcLblOwnrId = new javax.swing.JLabel();
				ivjstcLblOwnrId.setName("stcLblOwnrId");
				ivjstcLblOwnrId.setText("OwnrId:");
				ivjstcLblOwnrId.setBounds(71, 169, 58, 20);
				ivjstcLblOwnrId.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblOwnrId;
	}

	/**
	 * This method initializes ivjstcLblPrmtNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrmtNo()
	{
		if (ivjstcLblPrmtNo == null)
		{
			ivjstcLblPrmtNo = new JLabel();
			ivjstcLblPrmtNo.setSize(66, 20);
			ivjstcLblPrmtNo.setText("PrmtNo:");
			ivjstcLblPrmtNo.setLocation(46, 21);
			ivjstcLblPrmtNo.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblPrmtNo;
	}

	/**
	 * This method initializes ivjstcLblPrmtVIN
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrmtVIN()
	{
		if (ivjstcLblPrmtVIN == null)
		{
			ivjstcLblPrmtVIN = new javax.swing.JLabel();
			ivjstcLblPrmtVIN.setSize(90, 20);
			ivjstcLblPrmtVIN.setText("VIN:");
			ivjstcLblPrmtVIN.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblPrmtVIN.setLocation(22, 48);
		}
		return ivjstcLblPrmtVIN;
	}
	/**
	 * This method initializes ivjstcLblPrmtIssuanceId
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrmtIssuanceId()
	{
		if (ivjstcLblPrmtIssuanceId == null)
		{
			ivjstcLblPrmtIssuanceId = new JLabel();
			ivjstcLblPrmtIssuanceId.setBounds(2, 75, 111, 20);
			ivjstcLblPrmtIssuanceId.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblPrmtIssuanceId.setText("PrmtIssuanceId:");
		}
		return ivjstcLblPrmtIssuanceId;
	}

	/**
	 * Return the stclblR99 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstclblR99()
	{
		if (ivjstclblR99 == null)
		{
			try
			{
				ivjstclblR99 = new javax.swing.JLabel();
				ivjstclblR99.setName("stclblR99");
				ivjstclblR99.setText("R99 Trans");
				ivjstclblR99.setBounds(23, 490, 64, 24);
				ivjstclblR99.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstclblR99;
	}

	/**
	 * Return the JLabel12 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblRegPltNo()
	{
		if (ivjstcLblRegPltNo == null)
		{
			try
			{
				ivjstcLblRegPltNo = new javax.swing.JLabel();
				ivjstcLblRegPltNo.setName("stcLblRegPltNo");
				ivjstcLblRegPltNo.setText("HOOPSRegPltNo:");
				ivjstcLblRegPltNo.setBounds(18, 196, 111, 20);
				ivjstcLblRegPltNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblRegPltNo;
	}
	/**
	 * Return the stclblRetries property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstclblRetries()
	{
		if (ivjstclblRetries == null)
		{
			try
			{
				ivjstclblRetries = new javax.swing.JLabel();
				ivjstclblRetries.setBounds(422, 488, 64, 24);
				ivjstclblRetries.setName("stclblR99");
				ivjstclblRetries.setText("Retries");
				ivjstclblRetries.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblRetries;
	}

	/**
	 * Return the JLabel19 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblRptngDate()
	{
		if (ivjstcLblRptngDate == null)
		{
			try
			{
				ivjstcLblRptngDate = new javax.swing.JLabel();
				ivjstcLblRptngDate.setName("stcLblRptngDate");
				ivjstcLblRptngDate.setText("RptngDate:");
				ivjstcLblRptngDate.setBounds(365, 169, 68, 20);
				ivjstcLblRptngDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblRptngDate;
	}

	/**
	 * Return the JLabel1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblSendTransFiles()
	{
		if (ivjstcLblSendTransFiles == null)
		{
			try
			{
				ivjstcLblSendTransFiles = new javax.swing.JLabel();
				ivjstcLblSendTransFiles.setName("stcLblSendTransFiles");
				ivjstcLblSendTransFiles.setText("Select File:");
				ivjstcLblSendTransFiles.setBounds(260, 70, 111, 14);
				ivjstcLblSendTransFiles.setVisible(false);
				ivjstcLblSendTransFiles.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblSendTransFiles;
	}

	/**
	 * Return the JLabel15 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblSpclRegId()
	{
		if (ivjstcLblSpclRegId == null)
		{
			try
			{
				ivjstcLblSpclRegId = new javax.swing.JLabel();
				ivjstcLblSpclRegId.setBounds(64, 223, 65, 20);
				ivjstcLblSpclRegId.setName("stcLblSpclRegId");
				ivjstcLblSpclRegId.setText("SpclRegId:");
				ivjstcLblSpclRegId.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblSpclRegId;
	}
	/**
	 * Return the stclblTimeout property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstclblTimeout()
	{
		if (ivjstclblTimeout == null)
		{
			try
			{
				ivjstclblTimeout = new javax.swing.JLabel();
				ivjstclblTimeout.setBounds(422, 463, 64, 24);
				ivjstclblTimeout.setName("stclblR99");
				ivjstclblTimeout.setText("Timeout");
				ivjstclblTimeout.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstclblTimeout;
	}

	/**
	 * Return the JLabel112 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblTraceNo()
	{
		if (ivjstcLblTraceNo == null)
		{
			try
			{
				ivjstcLblTraceNo = new javax.swing.JLabel();
				ivjstcLblTraceNo.setName("stcLblTraceNo");
				ivjstcLblTraceNo.setText("TraceNo:");
				ivjstcLblTraceNo.setBounds(376, 250, 57, 20);
				ivjstcLblTraceNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblTraceNo;
	}

	/**
	 * Return the stcLblVersionMfAPPC property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblVersionMfAPPC()
	{
		if (ivjstcLblVersionMfAPPC == null)
		{
			try
			{
				ivjstcLblVersionMfAPPC = new javax.swing.JLabel();
				ivjstcLblVersionMfAPPC.setName("stcLblVersionMfAPPC");
				ivjstcLblVersionMfAPPC.setText("Version MfAPPC:");
				ivjstcLblVersionMfAPPC.setBounds(44, 60, 105, 14);
				ivjstcLblVersionMfAPPC.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblVersionMfAPPC;
	}

	/**
	 * Return the JLabel13 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblVin()
	{
		if (ivjstcLblVin == null)
		{
			try
			{
				ivjstcLblVin = new javax.swing.JLabel();
				ivjstcLblVin.setName("stcLblVin");
				ivjstcLblVin.setText("VIN:");
				ivjstcLblVin.setBounds(93, 250, 36, 20);
				ivjstcLblVin.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblVin;
	}

	/**
	 * This method initializes ivjtxtBeginDate
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtBeginDate()
	{
		if (ivjtxtBeginDate == null)
		{
			ivjtxtBeginDate = new RTSDateField();
			ivjtxtBeginDate.setSize(98, 20);
			ivjtxtBeginDate.setLocation(431, 75);

		}
		return ivjtxtBeginDate;
	}

	/**
	 * Return the RTSInputField114 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtCheckNo()
	{
		if (ivjtxtCheckNo == null)
		{
			try
			{
				ivjtxtCheckNo =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtCheckNo.setName("txtCheckNo");
				ivjtxtCheckNo.setBounds(441, 277, 121, 20);
				ivjtxtCheckNo.setMaxLength(7);
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
		return ivjtxtCheckNo;
	}

	/**
	 * Return the RTSInputField18 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtComptCntyNo()
	{
		if (ivjtxtComptCntyNo == null)
		{
			try
			{
				ivjtxtComptCntyNo =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtComptCntyNo.setSize(121, 20);
				ivjtxtComptCntyNo.setName("txtComptCntyNo");
				ivjtxtComptCntyNo.setInput(MF_DEBUG_MODE);
				ivjtxtComptCntyNo.setText("0");
				ivjtxtComptCntyNo.setMaxLength(3);
				// user code begin {1}
				ivjtxtComptCntyNo.setLocation(441, 116);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtComptCntyNo;
	}

	/**
	 * This method initializes ivjtxtCustBsnName
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustBsnName()
	{
		if (ivjtxtCustBsnName == null)
		{
			ivjtxtCustBsnName = new RTSInputField();
			ivjtxtCustBsnName.setBounds(431, 48, 175, 20);
			ivjtxtCustBsnName.setMaxLength(60);
			ivjtxtCustBsnName.setInput(RTSInputField.DEFAULT);
		}
		return ivjtxtCustBsnName;
	}

	/**
	 * This method initializes ivjtxtCustLstName
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustLstName()
	{
		if (ivjtxtCustLstName == null)
		{
			ivjtxtCustLstName = new RTSInputField();
			ivjtxtCustLstName.setBounds(431, 21, 175, 20);
			ivjtxtCustLstName.setMaxLength(30);
			ivjtxtCustLstName.setInput(RTSInputField.DEFAULT);
		}
		return ivjtxtCustLstName;
	}

	/**
	 * Return the RTSInputField1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtDocNo()
	{
		if (ivjtxtDocNo == null)
		{
			try
			{
				ivjtxtDocNo =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtDocNo.setName("txtDocNo");
				ivjtxtDocNo.setBounds(137, 143, 121, 20);
				ivjtxtDocNo.setMaxLength(17);
				// user code begin {1}
				ivjtxtDocNo.addFocusListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtDocNo;
	}
	/**
	 * This method initializes ivjtxtEndDate
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtEndDate()
	{
		// End Date 
		if (ivjtxtEndDate == null)
		{
			ivjtxtEndDate = new RTSDateField();
			ivjtxtEndDate.setSize(97, 20);
			ivjtxtEndDate.setLocation(431, 102);
		}
		return ivjtxtEndDate;
	}

	/**
	 * Return the RTSInputField111 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtFundsDueDate()
	{
		if (ivjtxtFundsDueDate == null)
		{
			try
			{
				ivjtxtFundsDueDate =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtFundsDueDate.setName("txtFundsDueDate");
				ivjtxtFundsDueDate.setInput(MF_DEBUG_MODE);
				ivjtxtFundsDueDate.setText("0");
				ivjtxtFundsDueDate.setBounds(441, 196, 121, 20);
				ivjtxtFundsDueDate.setMaxLength(8);
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
		return ivjtxtFundsDueDate;
	}

	/**
	 * Return the RTSInputField112 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtFundsPymntDate()
	{
		if (ivjtxtFundsPymntDate == null)
		{
			try
			{
				ivjtxtFundsPymntDate =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtFundsPymntDate.setName("txtFundsPymntDate");
				ivjtxtFundsPymntDate.setInput(MF_DEBUG_MODE);
				ivjtxtFundsPymntDate.setText("0");
				ivjtxtFundsPymntDate.setBounds(441, 223, 121, 20);
				ivjtxtFundsPymntDate.setMaxLength(8);
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
		return ivjtxtFundsPymntDate;
	}

	/**
	 * Return the RTSInputField19 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtFundsRptDate()
	{
		if (ivjtxtFundsRptDate == null)
		{
			try
			{
				ivjtxtFundsRptDate =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtFundsRptDate.setName("txtFundsRptDate");
				ivjtxtFundsRptDate.setInput(MF_DEBUG_MODE);
				ivjtxtFundsRptDate.setText("0");
				ivjtxtFundsRptDate.setBounds(441, 143, 121, 20);
				ivjtxtFundsRptDate.setMaxLength(8);
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
		return ivjtxtFundsRptDate;
	}

	/**
	 * Return the RTSInputField15 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtInvcNo()
	{
		if (ivjtxtInvcNo == null)
		{
			try
			{
				ivjtxtInvcNo =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtInvcNo.setSize(121, 20);
				ivjtxtInvcNo.setName("txtInvcNo");
				ivjtxtInvcNo.setMaxLength(6);
				// user code begin {1}
				ivjtxtInvcNo.setLocation(137, 277);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtInvcNo;
	}

	/**
	 * Return the RTSInputField11 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtOwnrId()
	{
		if (ivjtxtOwnrId == null)
		{
			try
			{
				ivjtxtOwnrId =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtOwnrId.setSize(121, 20);
				ivjtxtOwnrId.setName("txtOwnrId");
				ivjtxtOwnrId.setMaxLength(9);
				// user code begin {1}
				ivjtxtOwnrId.setLocation(137, 169);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtOwnrId;
	}

	/**
	 * This method initializes ivjtxtPrmtNo
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPrmtNo()
	{
		if (ivjtxtPrmtNo == null)
		{
			ivjtxtPrmtNo = new RTSInputField();
			ivjtxtPrmtNo.setBounds(120, 21, 125, 20);
			ivjtxtPrmtNo.setMaxLength(7);
			ivjtxtPrmtNo.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
		}
		return ivjtxtPrmtNo;
	}

	/**
	 * This method initializes ivjtxtPrmtVIN
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPrmtVIN()
	{
		if (ivjtxtPrmtVIN == null)
		{
			ivjtxtPrmtVIN = new RTSInputField();
			ivjtxtPrmtVIN.setSize(164, 20);
			ivjtxtPrmtVIN.setLocation(120, 48);
			ivjtxtPrmtVIN.setMaxLength(22);
			ivjtxtPrmtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
		}
		return ivjtxtPrmtVIN;
	}

	/**
	 * This method initializes ivjtxtPrmtIssuanceId
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPrmtIssuanceId()
	{
		if (ivjtxtPrmtIssuanceId == null)
		{
			ivjtxtPrmtIssuanceId = new RTSInputField();
			ivjtxtPrmtIssuanceId.setSize(167, 20);
			ivjtxtPrmtIssuanceId.setMaxLength(17);
			ivjtxtPrmtIssuanceId.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtPrmtIssuanceId.setLocation(120, 75);
		}
		return ivjtxtPrmtIssuanceId;
	}

	/**
	 * Return the ivjtxtRegPltNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRegPltNo()
	{
		if (ivjtxtRegPltNo == null)
		{
			try
			{
				ivjtxtRegPltNo =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtRegPltNo.setSize(121, 20);
				ivjtxtRegPltNo.setName("txtRegPltNo");
				ivjtxtRegPltNo.setMaxLength(7);
				// user code begin {1}
				ivjtxtRegPltNo.setLocation(137, 196);
				ivjtxtRegPltNo.addFocusListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtRegPltNo;
	}

	/**
	 * Return the Retries property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRetries()
	{
		if (ivjtxtRetries == null)
		{
			try
			{
				ivjtxtRetries = new RTSInputField();
				ivjtxtRetries.setBounds(494, 490, 71, 20);
				ivjtxtRetries.setName("txtRetries");
				ivjtxtRetries.setMaxLength(6);
				ivjtxtRetries.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtRetries.setText("0");
				ivjtxtRetries.setEnabled(false);
				ivjtxtRetries.addFocusListener(this);
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
		return ivjtxtRetries;
	}

	/**
	 * Return the ivjtxtRptngDate property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRptngDate()
	{
		if (ivjtxtRptngDate == null)
		{
			try
			{
				ivjtxtRptngDate = new RTSInputField();
				ivjtxtRptngDate.setName("txtRptngDate");
				ivjtxtRptngDate.setInput(MF_DEBUG_MODE);
				ivjtxtRptngDate.setText("0");
				ivjtxtRptngDate.setBounds(441, 169, 121, 20);
				ivjtxtRptngDate.setMaxLength(8);
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
		return ivjtxtRptngDate;
	}
	/**
	 * Return the ivjtxtSpclRegId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtSpclRegId()
	{
		if (ivjtxtSpclRegId == null)
		{
			try
			{
				ivjtxtSpclRegId = new RTSInputField();
				ivjtxtSpclRegId.setSize(121, 20);
				ivjtxtSpclRegId.setName("txtSpclRegId");
				ivjtxtSpclRegId.setMaxLength(9);
				//wsdtxtSpclRegId.setInput(MF_DEBUG_MODE);
				ivjtxtSpclRegId.setInput(RTSInputField.NUMERIC_ONLY);
				// user code begin {1}
				ivjtxtSpclRegId.setLocation(137, 223);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtSpclRegId;
	}

	/**
	 * Return the Retries property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTimeout()
	{
		if (ivjtxtRetries == null)
		{
			try
			{
				ivjtxtTimeout = new RTSInputField();
				ivjtxtTimeout.setBounds(494, 463, 71, 20);
				ivjtxtTimeout.setName("txtTimeout");
				ivjtxtTimeout.setMaxLength(6);
				ivjtxtTimeout.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtTimeout.setText(MF_TIMEOUT);
				ivjtxtTimeout.setEnabled(true);
				ivjtxtTimeout.addFocusListener(this);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtTimeout;
	}

	/**
	 * Return the ivjtxtTraceNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTraceNo()
	{
		if (ivjtxtTraceNo == null)
		{
			try
			{
				ivjtxtTraceNo = new RTSInputField();
				ivjtxtTraceNo.setName("txtTraceNo");
				//ivjtxtTraceNo.setInput(MF_DEBUG_MODE);
				ivjtxtTraceNo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtTraceNo.setText("0");
				ivjtxtTraceNo.setBounds(441, 250, 121, 20);
				ivjtxtTraceNo.setMaxLength(9);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtTraceNo;
	}

	/**
	 * Return the ivjtxtVin property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVin()
	{
		if (ivjtxtVin == null)
		{
			try
			{
				ivjtxtVin = new RTSInputField();
				ivjtxtVin.setSize(155, 20);
				ivjtxtVin.setName("txtVin");
				ivjtxtVin.setMaxLength(22);
				ivjtxtVin.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				// user code begin {1}
				ivjtxtVin.setLocation(137, 250);
				ivjtxtVin.addFocusListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtVin;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		/* Uncomment the following lines to print uncaught exceptions to
		 *  stdout 
		 */
		System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		aeThrowable.printStackTrace(System.out);
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
			setName("MfAccessTest");
			setSize(720, 570);
			setTitle("MfAccessTest");
			setContentPane(getMfAccessTestContentPane1());
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
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
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		if (((String) getJComboBoxR99Selection().getSelectedItem())
			.equals(MfAccess.R77))
		{
			// Select file
			setComboBoxSendTrans();
		}
	}

	/**
	 * Populate the MF Access Methods Combo Box 
	 */
	private void populateMfAccessMethods()
	{
		Vector lvMfAccessMdComboVal = new Vector();

		lvMfAccessMdComboVal.add(
			MFACCESS_RETRIEVE_VEHICLE_FROM_ACTIVE_INACTIVE);
		lvMfAccessMdComboVal.add(
			MFACCESS_RETRIEVE_VEHICLE_FROM_ARCHIVE);

		// defect 10492 
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_PERMIT_PRMTID);
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_PERMIT_PRMTNO);
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_PERMIT_VIN);
		lvMfAccessMdComboVal.add(
			MFACCESS_RETRIEVE_PERMIT_CUST_LST_NAME);
		lvMfAccessMdComboVal.add(
			MFACCESS_RETRIEVE_PERMIT_CUST_BSN_NAME);
		// end defect 10492 

		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_SP_SPCLREGID);
		lvMfAccessMdComboVal.add(MFACCESS_VOID_TRANSACTIONS);
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_SP_PLATE);
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_TITLE_IN_PROCESS);
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_VEHICLE_FROM_VINA);
		// defect 9965
		//	depercated
		//lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_NO_OF_DOCS);
		// end defect 9965
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_FDSPYMNT_DATALIST);
		lvMfAccessMdComboVal.add(
			MFACCESS_RETRIEVE_FDSREMITTANCE_RECORD);
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_INVOICE);
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_OWNER);
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_VEH_BY_SPL_REGIS);
		// defect 9965
		//	depercated
		//lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_VEH_BY_SPL_OWNER);
		// end defect 9965
		lvMfAccessMdComboVal.add(MFACCESS_RETRIEVE_JUNK_RECORDS);
		lvMfAccessMdComboVal.add(MFACCESS_REGIS_BY_DOCNO);

		DefaultComboBoxModel mdl =
			new DefaultComboBoxModel(lvMfAccessMdComboVal);
		getJComboBoxMfAccessMethod().setModel(mdl);
	}

	/**
	 * Populate the Mainframe db version combo box
	 */
	private void populateMfDB()
	{
		Vector lvMfDBComboVal = new Vector();

		lvMfDBComboVal.add(MF_DB_TST3);
		lvMfDBComboVal.add(MF_DB_TST5);
//		lvMfDBComboVal.add(MF_DB_PRD2);

		DefaultComboBoxModel mdl =
			new DefaultComboBoxModel(lvMfDBComboVal);
		getJComboBoxMfDBVersion().setModel(mdl);
	}

	/**
	 * populate the combo box used to set the R99 Trans replacement 
	 * selection. 
	 */
	private void populateMfTrans()
	{
		Vector lvMfTrans = new Vector();

		lvMfTrans.add("NONE");
		lvMfTrans.add(MfAccess.R01);
		// defect 9965
		//	deprecated
		//lvMfTrans.add(MfAccess.R02);
		// end defect 9965
		lvMfTrans.add(MfAccess.R03);
		lvMfTrans.add(MfAccess.R04);
		lvMfTrans.add(MfAccess.R05);
		lvMfTrans.add(MfAccess.R06);
		lvMfTrans.add(MfAccess.R07);
		lvMfTrans.add(MfAccess.R08);
		lvMfTrans.add(MfAccess.R09);
		lvMfTrans.add(MfAccess.R10);
		// defect 9965
		//	deprecated
		//lvMfTrans.add(MfAccess.R12);
		// end defect 9965
		lvMfTrans.add(MfAccess.R13);
		lvMfTrans.add(MfAccess.R14);
		lvMfTrans.add(MfAccess.R15);
		lvMfTrans.add(MfAccess.R17);
		lvMfTrans.add(MfAccess.R18);
		lvMfTrans.add(MfAccess.R19);
		// defect 9965
		//	deprecated
		//lvMfTrans.add(MfAccess.R20);
		// end defect 9965
		lvMfTrans.add(MfAccess.R26);
		lvMfTrans.add(MfAccess.R28);
		lvMfTrans.add(MfAccess.R29);
		lvMfTrans.add(MfAccess.R30);
		lvMfTrans.add(MfAccess.R31);
		lvMfTrans.add(MfAccess.R32);
		lvMfTrans.add(MfAccess.R33);
		lvMfTrans.add(MfAccess.R77);
		lvMfTrans.add(MfAccess.RSE);

		DefaultComboBoxModel mdl = new DefaultComboBoxModel(lvMfTrans);
		getJComboBoxR99Selection().setModel(mdl);
	}

	/**
	 * Populates the MF version drop down
	 */
	private void populateMfVersion()
	{
		Vector lvMfVersionComboVal = new Vector();

		lvMfVersionComboVal.add(
			ApplicationControlConstants.SC_MFA_VERSION_U);
		lvMfVersionComboVal.add(
			ApplicationControlConstants.SC_MFA_VERSION_T);
		lvMfVersionComboVal.add(
			ApplicationControlConstants.SC_MFA_VERSION_V);

		// defect 9965, 10080
		//	Make call to get MfInterfaceVerCd from server.cfg
		String lsMfInterfaceVerCd = FileUtil.setDefaultMfVerCd();
		// end defect 9965, 10080

		DefaultComboBoxModel mdl =
			new DefaultComboBoxModel(lvMfVersionComboVal);
		getJComboBoxMfVersion().setModel(mdl);
		getJComboBoxMfVersion().setSelectedItem(lsMfInterfaceVerCd);

	}

	/**
	 * Populate the SendTrans/FundsUpdate test files combo box
	 * 
	 * @return String
	 */
	private String populateSendTransFundsFiles(ActionEvent aaAE)
	{
		Vector lvSendTransFundsComboVal = new Vector();
		String lsFileLoc = returnSendTransFileLoc(aaAE);

		File laFile = new File(lsFileLoc);
		String[] larrFiles = laFile.list();

		try
		{
			for (int liIndex = 0;
				liIndex < larrFiles.length;
				liIndex++)
			{
				lvSendTransFundsComboVal.add(larrFiles[liIndex]);
			}

			DefaultComboBoxModel mdl =
				new DefaultComboBoxModel(lvSendTransFundsComboVal);
			getJComboBoxTestFiles().setModel(mdl);
		}
		catch (NullPointerException leNPEx)
		{
			leNPEx.printStackTrace();
		}

		return lsFileLoc;
	}

	/**
	 * Resets transaction to its normal number.  
	 */
	private void resetMFTransR99()
	{
		MfAccess.R01 = "R01";
		// defect 9965
		//	deprecated
		//MfAccess.R02 = "R02";
		// end defect 9965
		MfAccess.R03 = "R03";
		MfAccess.R04 = "R04";
		MfAccess.R05 = "R05";
		MfAccess.R06 = "R06";
		MfAccess.R07 = "R07";
		MfAccess.R08 = "R08";
		MfAccess.R09 = "R09";
		MfAccess.R10 = "R10";
		// defect 9965
		//	deprecated
		//MfAccess.R12 = "R12";
		// end defect 9965
		MfAccess.R13 = "R13";
		MfAccess.R14 = "R14";
		MfAccess.R15 = "R15";
		MfAccess.R17 = "R17";
		MfAccess.R18 = "R18";
		MfAccess.R19 = "R19";
		// defect 9965
		//	deprecated
		//MfAccess.R20 = "R20";
		// end defect 9965
		MfAccess.R26 = "R26";
		MfAccess.R28 = "R28";
		MfAccess.R29 = "R29";
		MfAccess.R30 = "R30";
		MfAccess.R31 = "R31";
		MfAccess.R32 = "R32";
		MfAccess.R33 = "R33";
		MfAccess.R77 = "R77";
		MfAccess.RSE = MfAccess.RSE;
	}

	/**
	 * Return SendTrans file folder location
	 * Save the file folder selected
	 */
	private String returnSendTransFileLoc(ActionEvent aaAE)
	{
		String lsRtnStr = CommonConstant.STR_SPACE_EMPTY;

		// Set full path name
		if (getJComboBoxMfVersion()
			.getSelectedItem()
			.toString()
			.equals(ApplicationControlConstants.SC_MFA_VERSION_U))
		{
			if (aaAE.getSource() == getbtnFundsUpdate())
			{
				lsRtnStr =
					FUNDSUPDT_FILE_LOC_ROOT
						+ U_FUNDSUPDT
						+ CommonConstant.STR_BACK_SLASH;
			}
			else
			{
				lsRtnStr =
					SENDTRANS_FILE_LOC_ROOT
						+ U_SENDTRANS
						+ CommonConstant.STR_BACK_SLASH;
			}
		}
		else if (
			getJComboBoxMfVersion()
				.getSelectedItem()
				.toString()
				.equals(
				ApplicationControlConstants.SC_MFA_VERSION_V))
		{
			if (aaAE.getSource() == getbtnFundsUpdate())
			{
				lsRtnStr =
					FUNDSUPDT_FILE_LOC_ROOT
						+ V_FUNDSUPDT
						+ CommonConstant.STR_BACK_SLASH;
			}
			else
			{
				lsRtnStr =
					SENDTRANS_FILE_LOC_ROOT
						+ V_SENDTRANS
						+ CommonConstant.STR_BACK_SLASH;
			}
		}
		else if (
			getJComboBoxMfVersion()
				.getSelectedItem()
				.toString()
				.equals(
				ApplicationControlConstants.SC_MFA_VERSION_T))
		{
			if (aaAE.getSource() == getbtnFundsUpdate())
			{
				lsRtnStr =
					FUNDSUPDT_FILE_LOC_ROOT
						+ T_FUNDSUPDT
						+ CommonConstant.STR_BACK_SLASH;
			}
			else
			{
				lsRtnStr =
					SENDTRANS_FILE_LOC_ROOT
						+ T_SENDTRANS
						+ CommonConstant.STR_BACK_SLASH;
			}
		}

		// Set Folder/File location
		csTestFiles_Loc = lsRtnStr;
		return lsRtnStr;
	}
	/**
	 * Set ComboBox/Labels for SendTrans
	 */
	private void setComboBoxSendTrans()
	{
		// Select file
		getstcLblSendTransFiles().setVisible(true);
		getJComboBoxTestFiles().setVisible(true);
		getstcLblSendTransFiles().setEnabled(true);
		getJComboBoxTestFiles().setEnabled(true);
		setVisibleScreenFields(false);
		// defect 10080
		//	Set TransTime check box
		getchkboxChangeTransTime().setVisible(true);
		getchkboxChangeTransTime().setEnabled(true);
		getchkboxChangeTransTime().setSelected(true);
		// end defect 10080

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
		gettxtComptCntyNo().setText("0");
		gettxtFundsRptDate().setText("0");
		gettxtRptngDate().setText("0");
		gettxtFundsDueDate().setText("0");
		gettxtFundsPymntDate().setText("0");
		gettxtTraceNo().setText("0");
		gettxtSpclRegId().setText("0");
	}

	/**
	 * Defeine CICS/MfACCESS 
	 * 
	 * @return MfAccess 
	 */
	private MfAccess setMfAccesInstance()
	{
		int liNumOfRetries =
			Integer.parseInt(gettxtRetries().getText());

		MfAccess laMFA = null;

		//Create the MFAccessInstance
		//		String lsCall =
		//			((String) getJComboBoxMfAccessMethod()
		//				.getSelectedItem())
		//				.trim();
		String lsMFDB =
			((String) getJComboBoxMfDBVersion().getSelectedItem())
				.trim();
		String lsMFVersion =
			((String) getJComboBoxMfVersion().getSelectedItem()).trim();

		if (liNumOfRetries == 0)
		{
			// MFServer depends on MFDB
			String lsMFServer = TEST_CICS;
			CICS_JGP_PORT = CICS_JGP_PORT_TEST;

			if (lsMFDB.equals(MF_DB_PRD2))
			{
				lsMFServer = PROD_CICS;
				CICS_JGP_PORT = CICS_JGP_PORT_PROD;
			}

			// defect 6701
			// set the SystemProperty for Version
			SystemProperty.setMFInterfaceVersionCode(lsMFVersion);
			// end defect 6701
			// defect 8984
			//	Use users entry of MF_TIMEOUT (gettxtTimeout().getText())
			laMFA =
				getMFAccessInstance(
					MF_NAME,
					CICS_JGP_PORT,
					gettxtTimeout().getText(),
					lsMFDB,
					lsMFVersion,
					lsMFServer,
					MF_JG_PROG,
					MF_DEBUG_MODE);
		}

		return laMFA;
	}

	/**
	 * Sets the selected transaction to be an R99 transaction 
	 * instead of its normal number.  This allows MF to test 
	 * changes before putting them into the normal testing / 
	 * development path.
	 */
	private void setMFTransToR99()
	{
		String lsTrans =
			(String) getJComboBoxR99Selection().getSelectedItem();

		// defect 8984
		//	Initialize boolean for each call
		cbSendTransIndi = false;
		// end defect 8984

		if (lsTrans.equals(MfAccess.R01))
		{
			MfAccess.R01 = R99;
		}
		// defect 9965
		//	deprecated
		//		else if (lsTrans.equals(MfAccess.R02))
		//		{
		//			MfAccess.R02 = R99;
		//		}
		// end defect 9965
		else if (lsTrans.equals(MfAccess.R03))
		{
			MfAccess.R03 = R99;
		}
		else if (lsTrans.equals(MfAccess.R04))
		{
			MfAccess.R04 = R99;
		}
		else if (lsTrans.equals(MfAccess.R05))
		{
			MfAccess.R05 = R99;
		}
		else if (lsTrans.equals(MfAccess.R06))
		{
			MfAccess.R06 = R99;
		}
		else if (lsTrans.equals(MfAccess.R07))
		{
			MfAccess.R07 = R99;
		}
		else if (lsTrans.equals(MfAccess.R08))
		{
			MfAccess.R08 = R99;
		}
		else if (lsTrans.equals(MfAccess.R09))
		{
			MfAccess.R09 = R99;
		}
		else if (lsTrans.equals(MfAccess.R10))
		{
			MfAccess.R10 = R99;
		}
		// defect 9965
		//	deprecated
		//		else if (lsTrans.equals(MfAccess.R12))
		//		{
		//			MfAccess.R12 = R99;
		//		}
		// end defect 9965
		else if (lsTrans.equals(MfAccess.R13))
		{
			MfAccess.R13 = R99;
		}
		else if (lsTrans.equals(MfAccess.R14))
		{
			MfAccess.R14 = R99;
		}
		else if (lsTrans.equals(MfAccess.R15))
		{
			MfAccess.R15 = R99;
		}
		else if (lsTrans.equals(MfAccess.R17))
		{
			MfAccess.R17 = R99;
		}
		else if (lsTrans.equals(MfAccess.R18))
		{
			MfAccess.R18 = R99;
		}
		else if (lsTrans.equals(MfAccess.R19))
		{
			MfAccess.R19 = R99;
		}
		// defect 9965
		//	deprecated
		//		else if (lsTrans.equals(MfAccess.R20))
		//		{
		//			MfAccess.R20 = R99;
		//		}
		// end defect 9965
		else if (lsTrans.equals(MfAccess.R26))
		{
			MfAccess.R26 = R99;
		}
		else if (lsTrans.equals(MfAccess.R28))
		{
			MfAccess.R28 = R99;
		}
		else if (lsTrans.equals(MfAccess.R29))
		{
			MfAccess.R29 = R99;
		}
		else if (lsTrans.equals(MfAccess.R30))
		{
			MfAccess.R30 = R99;
		}
		else if (lsTrans.equals(MfAccess.R31))
		{
			MfAccess.R31 = R99;
		}
		else if (lsTrans.equals(MfAccess.R32))
		{
			MfAccess.R32 = R99;
		}
		else if (lsTrans.equals(MfAccess.R33))
		{
			MfAccess.R33 = R99;
		}
		else if (lsTrans.equals(MfAccess.R77))
		{
			MfAccess.R77 = R99;
			// defect 8984
			//	Send SendTransIndi
			cbSendTransIndi = true;
			// end defect 8984
		}
		else if (lsTrans.equals(MfAccess.RSE))
		{
			MfAccess.RSE = R99;
		}
	}

	/**
	 * Set enable status for SendTrans files
	 * 
	 * @param abValue	boolean
	 */
	private void setVisibleScreenFields(boolean abValue)
	{
		if (abValue)
		{
			// txt
			gettxtDocNo().setVisible(abValue);
			gettxtOwnrId().setVisible(abValue);
			gettxtRegPltNo().setVisible(abValue);
			gettxtSpclRegId().setVisible(abValue);
			gettxtVin().setVisible(abValue);
			gettxtInvcNo().setVisible(abValue);
			gettxtComptCntyNo().setVisible(abValue);
			gettxtComptCntyNo().setVisible(abValue);
			gettxtFundsRptDate().setVisible(abValue);
			gettxtRptngDate().setVisible(abValue);
			gettxtFundsDueDate().setVisible(abValue);
			gettxtFundsPymntDate().setVisible(abValue);
			gettxtTraceNo().setVisible(abValue);
			gettxtCheckNo().setVisible(abValue);
			gettxtRetries().setVisible(abValue);
			getJComboBoxMfAccessMethod().setVisible(abValue);

			gettxtDocNo().setEnabled(abValue);
			gettxtOwnrId().setEnabled(abValue);
			gettxtRegPltNo().setEnabled(abValue);
			gettxtSpclRegId().setEnabled(abValue);
			gettxtVin().setEnabled(abValue);
			gettxtInvcNo().setEnabled(abValue);
			gettxtComptCntyNo().setEnabled(abValue);
			gettxtComptCntyNo().setEnabled(abValue);
			gettxtFundsRptDate().setEnabled(abValue);
			gettxtRptngDate().setEnabled(abValue);
			gettxtFundsDueDate().setEnabled(abValue);
			gettxtFundsPymntDate().setEnabled(abValue);
			gettxtTraceNo().setEnabled(abValue);
			gettxtCheckNo().setEnabled(abValue);
			gettxtRetries().setEnabled(abValue);
			getJComboBoxMfAccessMethod().setEnabled(abValue);

			// stcLbl
			getstcLblDocNo().setVisible(abValue);
			getstcLblOwnrId().setVisible(abValue);
			getstcLblRegPltNo().setVisible(abValue);
			getstcLblSpclRegId().setVisible(abValue);
			getstcLblVin().setVisible(abValue);
			getstcLblInvcNo().setVisible(abValue);
			getstcLblComptCntyNo().setVisible(abValue);
			getstcLblFundsRptDate().setVisible(abValue);
			getstcLblRptngDate().setVisible(abValue);
			getstcLblFundsDueDate().setVisible(abValue);
			getstcLblFundsPymntDate().setVisible(abValue);
			getstcLblTraceNo().setVisible(abValue);
			getstcLblCheckNo().setVisible(abValue);
			getstclblRetries().setVisible(abValue);
			getstcLblMfAccessMethod().setVisible(abValue);
			getJComboBoxMfAccessMethod().setVisible(abValue);

			getstcLblDocNo().setEnabled(abValue);
			getstcLblOwnrId().setEnabled(abValue);
			getstcLblRegPltNo().setEnabled(abValue);
			getstcLblSpclRegId().setEnabled(abValue);
			getstcLblVin().setEnabled(abValue);
			getstcLblInvcNo().setEnabled(abValue);
			getstcLblComptCntyNo().setEnabled(abValue);
			getstcLblFundsRptDate().setEnabled(abValue);
			getstcLblRptngDate().setEnabled(abValue);
			getstcLblFundsDueDate().setEnabled(abValue);
			getstcLblFundsPymntDate().setEnabled(abValue);
			getstcLblTraceNo().setEnabled(abValue);
			getstcLblCheckNo().setEnabled(abValue);
			getstclblRetries().setEnabled(abValue);
			getstcLblMfAccessMethod().setEnabled(abValue);
			getJComboBoxMfAccessMethod().setEnabled(abValue);
		}
		else
		{
			// stcLbl
			getstcLblDocNo().setEnabled(abValue);
			getstcLblOwnrId().setEnabled(abValue);
			getstcLblRegPltNo().setEnabled(abValue);
			getstcLblSpclRegId().setEnabled(abValue);
			getstcLblVin().setEnabled(abValue);
			getstcLblInvcNo().setEnabled(abValue);
			getstcLblComptCntyNo().setEnabled(abValue);
			getstcLblFundsRptDate().setEnabled(abValue);
			getstcLblRptngDate().setEnabled(abValue);
			getstcLblFundsDueDate().setEnabled(abValue);
			getstcLblFundsPymntDate().setEnabled(abValue);
			getstcLblTraceNo().setEnabled(abValue);
			getstcLblCheckNo().setEnabled(abValue);
			getstclblRetries().setEnabled(abValue);
			getstcLblMfAccessMethod().setEnabled(abValue);
			getJComboBoxMfAccessMethod().setEnabled(abValue);

			getstcLblDocNo().setVisible(abValue);
			getstcLblOwnrId().setVisible(abValue);
			getstcLblRegPltNo().setVisible(abValue);
			getstcLblSpclRegId().setVisible(abValue);
			getstcLblVin().setVisible(abValue);
			getstcLblInvcNo().setVisible(abValue);
			getstcLblComptCntyNo().setVisible(abValue);
			getstcLblFundsRptDate().setVisible(abValue);
			getstcLblRptngDate().setVisible(abValue);
			getstcLblFundsDueDate().setVisible(abValue);
			getstcLblFundsPymntDate().setVisible(abValue);
			getstcLblTraceNo().setVisible(abValue);
			getstcLblCheckNo().setVisible(abValue);
			getstclblRetries().setVisible(abValue);
			getstcLblMfAccessMethod().setVisible(abValue);
			getJComboBoxMfAccessMethod().setVisible(abValue);

			// txt
			gettxtDocNo().setEnabled(abValue);
			gettxtOwnrId().setEnabled(abValue);
			gettxtRegPltNo().setEnabled(abValue);
			gettxtSpclRegId().setEnabled(abValue);
			gettxtVin().setEnabled(abValue);
			gettxtInvcNo().setEnabled(abValue);
			gettxtComptCntyNo().setEnabled(abValue);
			gettxtComptCntyNo().setEnabled(abValue);
			gettxtFundsRptDate().setEnabled(abValue);
			gettxtRptngDate().setEnabled(abValue);
			gettxtFundsDueDate().setEnabled(abValue);
			gettxtFundsPymntDate().setEnabled(abValue);
			gettxtTraceNo().setEnabled(abValue);
			gettxtCheckNo().setEnabled(abValue);
			gettxtRetries().setEnabled(abValue);
			getJComboBoxMfAccessMethod().setEnabled(abValue);

			gettxtDocNo().setVisible(abValue);
			gettxtOwnrId().setVisible(abValue);
			gettxtRegPltNo().setVisible(abValue);
			gettxtSpclRegId().setVisible(abValue);
			gettxtVin().setVisible(abValue);
			gettxtInvcNo().setVisible(abValue);
			gettxtComptCntyNo().setVisible(abValue);
			gettxtComptCntyNo().setVisible(abValue);
			gettxtFundsRptDate().setVisible(abValue);
			gettxtRptngDate().setVisible(abValue);
			gettxtFundsDueDate().setVisible(abValue);
			gettxtFundsPymntDate().setVisible(abValue);
			gettxtTraceNo().setVisible(abValue);
			gettxtCheckNo().setVisible(abValue);
			gettxtRetries().setVisible(abValue);
			getJComboBoxMfAccessMethod().setVisible(abValue);
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
