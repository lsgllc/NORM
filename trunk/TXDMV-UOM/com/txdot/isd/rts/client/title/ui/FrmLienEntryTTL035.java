package com.txdot.isd.rts.client.title.ui;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.CertifiedLienholderCache;
import com.txdot.isd.rts.services.cache.DocumentTypesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmLienEntryTTL035.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validations update
 * MAbs			06/27/2002	Placing focus properly after USA is pressed
 *							defect 4268
 * JRue			06/27/2002	4366, Add check for LienholderStr! data to
 *							LienholderName1 to get DlrTitle Lienholder
 *							data, method setLienData()
 * J Rue		07/02/2002	4410, Add condition if date not zero and
 *							from 19000101 to 20101231.
 *							method setLienData().
 * B Arredondo	07/26/2002	Defect 4511--Adjusted code to allow 3 lien
 *							dates to appear when there are 3 
 *							lienholders.
 * J Rue		07/25/2002	Defect 4442, set cursor to Street1() when
 *							changing USA check box.
 *							method itemStateChanged().
 * J Rue		07/25/2002	Defect 4514, Adjust logic flow to test for
 *							State then Country to set State/Zip/ZipP4.
 *							method setLienData()
 * J Rue 		09/06/2002	Defect 4649, Modify method setData() to
 *							check for flag to be 0. This indicates first
 *							time through this code. 
 *							This code is call everytime you tab off the
 *							LienHolder Id's fields.
 * J Rue 		09/19/2002	Defect 4769, Set country to a blank if
 *							initial value is null.
 *							method setLienData().
 * B Arredondo	12/03/2002	Made changes to user help guide so had to
 *							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the
 *							user help guide so had to make changes
 *							in actionPerformed().
 * J Rue		01/16/2003	Fixed Defect #5267. Made changes to populate
 *							all lien fields from DTA diskette.
 *							Modified method setLienData().
 * Rue/			01/16/2003 	Fixed defect #4968. Modified edits to force
 *	Arredondo				user to enter complete lienholder data.
 *							Modified doLien1,2,3Validation() and created
 *							isLienHolderDataEntered().
 * S Govindappa 02/17/2003  Fixed defect # 5296. Changed methods in
 *							setData(..) and setLienData(..) to populate
 *                          the state and zip fields properly and to
 *							keep the USA checkbox defaulting to selected
 *							on entry.
 * S Govindappa 03/04/2003  Fix defect 5636. Fix the tab sequence when
 *							tabbing off the Lein Id field by making
 *							changes to setData(..) and
 *                          itemStateChanged(..) and introducing a
 *							SetFocusOnStreet flag.
 * J Zwiener	08/19/2004	When changing from USA to non-USA for CorTtl
 *							and RejCor, USA-type address prints on rcpt.
 *							modified setDataToVehObj()
 *							modified itemStateChanged(ItemEvent)
 *							defect 6685 Ver 5.2.1
 * J Rue		03/08/2005	VAJ to WSAD Clean Up
 * 							Add tabbing Transversal for tabbing testing.
 * 							class TravPolicyTabbing
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/05/2005	Comment out unused variabled
 * 							Remove setNextFocusableComponent
 * 							Use Visual Editor to add an additional 6
 * 							panels for tabbing issues
 * 							deprecate doLien3Validation()
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/25/2005	Clean up State, ZpCd, ZpCdP4, Cntry, 
 * 							CntryZpCd
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3           
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3 
 * J Rue		12/15/2005	Set Lien Id/Date fields size to match 
 * 							Liens 1 and 3.
 * 							Align fields using Visual Editor.
 * 							Enlarge Delete Lien buttons to show blue
 * 							focus border.
 * 							modify getstcLblDate2(), gettxtLien2Date()
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/21/2005	Replace focusListener with keyListener for 
 * 							LienHldrIds. Add populate fields with Lien 
 * 							info on Enter.
 * 							add keyPressed(), keyreleased()
 * 							modify gettxtLienholder1Id(), 
 * 							modify gettxtLienholder2Id(), 
 * 							modify gettxtLienholder3Id()
 * 							modify actionPerformed(), setData()
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/29/2005	Retain FocusListener for the Lienholder ids.
 * 							modify gettxtLienholder1Id(), 
 * 							modify gettxtLienholder2Id(), 
 * 							modify gettxtLienholder3Id()
 * 							defect 7898 Ver 5.2.3
 * J Rue		01/02/2006	Add requestFocus on enter key when Delete
 * 							Lienholder 1,2 or 3 push is selected.
 * 							modify actionPerformed()
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/23/2007	Invalid date display error message 733
 * 							modify doLien1Validation, doLien2Validation
 * 							 doLien3Validation
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/11/2008	TTL035 screen appears twice, Code Cleanup 
 * 							delete getBuilderData() 
 * 							modify focusLost()
 * 							defect 9474 Ver Defect POS A 
 * K Harrell	09/03/2008	Handle non-USA Lienholders retrieved from 
 * 							the Database / Cache. Did not check Street2
 * 							when determined if LienholderData Entered.
 * 							Did not restore focusListener when non-USA 
 * 							address.  
 * 							delete displayError()
 * 							modify focusLost(),isLienHolderDataEntered(),
 * 							 setData() 
 * 							defect 8727 Ver Defect_POS_B
 * K Harrell	03/03/2009	Implement Lienholder  AddressData
 * 							modify setDataToVehObj() 
 * 							defect 9969 Ver Defect_POS_E 
 * K Harrell	03/20/2009	Major work for ETTL. Add'l class cleanup,  
 * 							 including error constants, mnemonic constants
 * 							add jPanel6, jPanel7, jPanel8, caButtonGroup1,
 * 							 caButtonGroup2, caButtonGroup3,
 * 							 ivjradioCertified1, ivjradioCertified2,
 * 							 ivjradioCertified3, ivjradioETitle,
 * 							 ivjradioLocal1, ivjradioLocal2,
 * 							 ivjradioLocal3, ivjcomboLienholder1, 
 * 							 ivjcomboLienholder2,ivjcomboLienholder3,
 * 							 get methods 
 * 							 ETITLE, LOCAL, CERTIFIED, ERROR_HDR, CHANGE
 * 							 cbETtl,cbETtlAllowd,csPermLienhldrId1,
 * 							 csPermLienhldrId2, csPermLienhldrId3,
 * 							 svCurrentCertLienhldr,svCurrentETtlCertLienhldr,
 * 							 addListeners(),assignCertfdLienholderData(),
 * 							 buildCertifiedLienVector(),
 *							 buildETtlLienVector(),
 * 							 enableLien1Data(),	enableLien2Data(), 
 *							 enableLien3Data(), getcomboSelection(), 
 *							 handleComboSelection(),handleRadioSelection(), 
 *							 isValidLienDate(),  isVTRAuthReqdForSSNInName(),
 *							 populateCertifiedCombo(),populateETtlCombo(),
 *							 removeListeners(), resetLien1(),resetLien2(), 
 *							 resetLien3(), restoreComboColor(), 
 *							 retrieveLienholderData(), 
 *							 setLienData(), setupForDTADiskette(),
 *							 setupLienHldr1Data(), setupLienHldr2Data(), 
 *							 setupLienHldr3Data(), validateLien1Data(),
 *							 validateLien2Data(), validateLien3Data(),
 * 							 verifyLienDataPopulation(), 
 * 							delete doDlrTtl(),setLienData(LienholdersData,
 *  						 int, LienholdersData,int,LienholdersData,int),
 * 							 doLien1Validation(), doLien2Validation(),
 * 							 doLien3Validation(), setDataForCor(), 
 * 							 LIEN_ID_MAX_ID
 * 							modify actionPerformed(), setDataToVehObj(),
 * 							  getFrmLienEntryTTL035ContentPane1(),
 * 							  getLienHldr1Data(), getLienHldr2Data(), 
 * 							  getLienHldr3Data(),  focusLost(), 
 * 							  itemStateChanged()
 * 							defect 9974 Ver Defect_POS_E
 * K Harrell	04/06/2009	Use Default ETtlCd 
 * 							modify setDataToVehObj() 
 * 							defect 9974 Ver Defect_POS_E 
 * K Harrell	04/10/2009	Screen not initialized for DTA Keyboard 
 * 							modify setData() 
 * 							defect 9974 Ver Defect_POS_E 
 * K Harrell	04/13/2009	Always assign ETtlCd vs. just when ETitle 
 * 							radio selected
 * 							modify setDataToVehObj()
 * 							defect 9974 Ver Defect_POS_E 
 * K Harrell	04/30/2009	Add mnemonics for radio buttons, reassign 
 * 							text, mnemonic for Additional Liens.
 * 							Additional Screen alignment tweaking via
 * 							Visual Editor.  
 * 							add CHK_IF_APPLICABLE
 * 							delete CHK_ADDIT_LIENS_EXIST
 * 							modify ADDL_LEINS  
 * 							modify getchkAdditionalLien(), 
 * 							 getradioCertified1(), getradioCertified2(),
 * 							 getradioCertified3(), ivjradioETitle(),
 * 							 getradioLocal1(), getjradioLocal2(),
 * 							 getradioLocal3(), initialize()
 *							defect 10049 Ver Defect_POS_E
 * K Harrell	07/07/2009	Implement new LienholderData, methods
 * 							modify assignCertfdLienholderData(),
 * 							 populateETtlCombo(), 
 * 							 setDataToVehObj(), setLienData(),
 * 							 setupLienHldr1Data(), setupLienHldr2Data(),
 *							 setupLienHldr3Data()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	07/20/2009	Validate Cntry vs. valid state.  Rename 
 * 							refactor RTSInputFields, get methods to 
 * 							 standards 
 * 							delete INVALID_STATE, ERROR 
 * 							modify validateLien1Data(), 
 * 							 validateLien2Data(),validateLien3Data()
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	09/08/2009	Select Additional Liens Exist for 
 * 							 Corrected Title/Rejcor. Also in 6.0.0  
 * 							modify setLienData() 
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	12/16/2009	DTA Cleanup
 * 							modify setupForDTADiskette()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	01/03/2010	Copy VehInqData Object w/in setData to 
 * 							prevent modification of original.  
 * 							modify setData()
 * 							defect 10274 Ver Defect_POS_H
 * K Harrell	03/09/2010	Remove System.out in focusLost(), 
 * 							  isValidLienDate() 
 * 							modify focusLost(), isValidLienDate() 
 * 							defect 10229 Ver POS_640  
 * T Pederson	06/17/2010 	Added check to not allow a lien date  
 * 							greater than the current date.  
 * 							modify validateLienData()  
 *							defect 10504  Ver POS_650
 * K Harrell	09/14/2010	Save LienholderData
 * 							add setSavedDataToDisplay() 
 * 							modify setData(), actionPerformed(), 
 * 							 setDatatoVehObj()  
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	10/06/2010	Add enabling/disabling of Delete buttons
 * 							based on data population  
 * 							add setupDeleteButtons() 
 * 							modify actionPerformed(), setData(), 
 * 							 handleRadioSelection(), keyReleased() 
 * 							defect 10592 Ver 6.6.0
 * K Harrell	01/12/2011	Validate Lien Date against Certified 
 * 							Lienholder Data
 * 							modify validateLienData(), 
 * 							 verifyLienDataPopulation() 
 * 							defect 10631 Ver 6.7.0   
 * --------------------------------------------------------------------- 
 */

/**
 * This form is used to capture the lien holder information submitted  
 * with a title application.
 *
 * @version	6.7.0 			01/12/2011
 * @author	Marx Rajangam
 * <br>Creation Date:		06/24/2001 11:55:59
 */
public class FrmLienEntryTTL035
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener, KeyListener
{
	private RTSButton ivjbtnDelete1stLien = null;
	private RTSButton ivjbtnDelete2ndLien = null;
	private RTSButton ivjbtnDelete3rdLien = null;
	private ButtonPanel ivjButtonPanel = null;
	private JCheckBox ivjchkAdditionalLien = null;
	private JCheckBox ivjchkUSA1 = null;
	private JCheckBox ivjchkUSA2 = null;
	private JCheckBox ivjchkUSA3 = null;
	private JPanel ivjFrmLienEntryTTL035ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel31 = null;
	private JLabel ivjstcLblAddress = null;
	private JLabel ivjstcLblDash1 = null;
	private JLabel ivjstcLblDash2 = null;
	private JLabel ivjstcLblDash3 = null;
	private JLabel ivjstcLblDate2 = null;
	private JLabel ivjstcLblDate3 = null;
	private JLabel ivjstcLblFirstLienId = null;
	private JLabel ivjstcLblLien1Date = null;
	private JLabel ivjstcLblName = null;
	private JLabel ivjstcLblSecondLienId = null;
	private JLabel ivjstcLblThirdLienId = null;
	private RTSDateField ivjtxtLien1Date = null;
	private RTSDateField ivjtxtLien2Date = null;
	private RTSDateField ivjtxtLien3Date = null;
	private RTSInputField ivjtxtLienholder1City = null;
	private RTSInputField ivjtxtLienholder1Cntry = null;
	private RTSInputField ivjtxtLienholder1CntryZpcd = null;
	private RTSInputField ivjtxtLienholder1Id = null;
	private RTSInputField ivjtxtLienholder1Name1 = null;
	private RTSInputField ivjtxtLienholder1Name2 = null;
	private RTSInputField ivjtxtLienholder1State = null;
	private RTSInputField ivjtxtLienholder1Street1 = null;
	private RTSInputField ivjtxtLienholder1Street2 = null;
	private RTSInputField ivjtxtLienholder1Zpcd = null;
	private RTSInputField ivjtxtLienholder1ZpcdP4 = null;
	private RTSInputField ivjtxtLienholder2City = null;
	private RTSInputField ivjtxtLienholder2Cntry = null;
	private RTSInputField ivjtxtLienholder2CntryZpcd = null;
	private RTSInputField ivjtxtLienholder2Id = null;
	private RTSInputField ivjtxtLienholder2Name1 = null;
	private RTSInputField ivjtxtLienholder2Name2 = null;
	private RTSInputField ivjtxtLienholder2State = null;
	private RTSInputField ivjtxtLienholder2Street1 = null;
	private RTSInputField ivjtxtLienholder2Street2 = null;
	private RTSInputField ivjtxtLienholder2Zpcd = null;
	private RTSInputField ivjtxtLienholder2ZpcdP4 = null;
	private RTSInputField ivjtxtLienholder3City = null;
	private RTSInputField ivjtxtLienholder3Cntry = null;
	private RTSInputField ivjtxtLienholder3CntryZpcd = null;
	private RTSInputField ivjtxtLienholder3Id = null;
	private RTSInputField ivjtxtLienholder3Name1 = null;
	private RTSInputField ivjtxtLienholder3Name2 = null;
	private RTSInputField ivjtxtLienholder3State = null;
	private RTSInputField ivjtxtLienholder3Street1 = null;
	private RTSInputField ivjtxtLienholder3Street2 = null;
	private RTSInputField ivjtxtLienholder3Zpcd = null;
	private RTSInputField ivjtxtLienholder3ZpcdP4 = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;

	// defect 9974
	private JComboBox ivjcomboLienholder1 = null;
	private JComboBox ivjcomboLienholder2 = null;
	private JComboBox ivjcomboLienholder3 = null;
	private JPanel jPanel6 = null;
	private JPanel jPanel7 = null;
	private JPanel jPanel8 = null;
	private ButtonGroup caButtonGroup1 = new ButtonGroup();
	private ButtonGroup caButtonGroup2 = new ButtonGroup();
	private ButtonGroup caButtonGroup3 = new ButtonGroup();
	private JRadioButton ivjradioCertified1 = null;
	private JRadioButton ivjradioCertified2 = null;
	private JRadioButton ivjradioCertified3 = null;
	private JRadioButton ivjradioETitle = null;
	private JRadioButton ivjradioLocal1 = null;
	private JRadioButton ivjradioLocal2 = null;
	private JRadioButton ivjradioLocal3 = null;
	// end defect 9974   

	// boolean
	// vate boolean cbSetFocusOnStreet = true;
	private boolean cbETtl = false;
	private boolean cbETtlAllowd = false;

	private String csLien1Id = CommonConstant.STR_SPACE_EMPTY;
	private String csLien2Id = CommonConstant.STR_SPACE_EMPTY;
	private String csLien3Id = CommonConstant.STR_SPACE_EMPTY;

	private String csPermLienhldrId1 = CommonConstant.STR_SPACE_EMPTY;
	private String csPermLienhldrId2 = CommonConstant.STR_SPACE_EMPTY;
	private String csPermLienhldrId3 = CommonConstant.STR_SPACE_EMPTY;

	// int
	private int ciFlag = 0;

	// Object
	private VehicleInquiryData caVehInqData = null;

	// defect 10127 
	private NameAddressComponent caLien1NameAddrComp = null;
	private NameAddressComponent caLien2NameAddrComp = null;
	private NameAddressComponent caLien3NameAddrComp = null;

	// Constants 
	// defect 10049 
	//private final static String ADDL_LEINS = "Additional Lien(s)";
	private final static String ADDL_LEINS = "Additional Lien(s) Exist";
	private final static String ADDRESS = "Address";
	private final static String CERTIFIED = "Certified";
	private final static String CHANGE = " CHANGE";
	//	private final static String CHK_ADDIT_LIENS_EXIST =
	//			"Check if additional lien(s) exist:";
	private final static String CHK_IF_APPLICABLE =
		"Check if Applicable:";
	// end defect 10049
	private final static String DATE = "Date:";
	private final static String DEL_FIRST_LIEN = "Delete 1st Lien";
	private final static String DEL_SECOND_LIEN = "Delete 2nd Lien";
	private final static String DEL_THIRD_LIEN = "Delete 3rd Lien";
	private final static String EMPTY = CommonConstant.STR_SPACE_EMPTY;
	private final static String ERROR_HDR =
		"<b><font color=#ff0000>PLEASE RESOLVE LIEN DATA";
	private final static String ETITLE = "ETitle";
	private final static String FIRST_LIEN_ID = "First Lien Id:";
	private final static String LOCAL = "Local";
	private final static String NAME = "Name";
	private final static String SECOND_LIEN_ID = "Second Lien Id:";
	private final static String THIRD_LIEN_ID = "Third Lien Id:";

	private static Vector svCurrentCertLienhldr = new Vector();
	private static Vector svCurrentETtlCertLienhldr = new Vector();

	static {
		buildCertifiedLienVector();
		buildETtlLienVector();
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
	 * buildCertifiedLienVector from Cache 
	 */
	private static void buildETtlLienVector()
	{
		svCurrentETtlCertLienhldr =
			CertifiedLienholderCache.getCurrentCertfdLienhldrs(true);
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
			FrmLienEntryTTL035 laFrmLienEntryTTL035;
			laFrmLienEntryTTL035 = new FrmLienEntryTTL035();
			laFrmLienEntryTTL035.setModal(true);
			laFrmLienEntryTTL035.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent laWE)
				{
					System.exit(0);
				};
			});
			laFrmLienEntryTTL035.show();
			java.awt.Insets insets = laFrmLienEntryTTL035.getInsets();
			laFrmLienEntryTTL035.setSize(
				laFrmLienEntryTTL035.getWidth()
					+ insets.left
					+ insets.right,
				laFrmLienEntryTTL035.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmLienEntryTTL035.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmLienEntryTTL035 constructor comment.
	 */
	public FrmLienEntryTTL035()
	{
		super();
		initialize();
	}

	/**
	 * FrmLienEntryTTL035 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmLienEntryTTL035(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmLienEntryTTL035 constructor comment.
	 * 
	 * @param aaOwner java.awt.Frame
	 */
	public FrmLienEntryTTL035(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			RTSException leRTSEx = new RTSException();

			clearAllColor(this);

			// defect 9974 
			restoreComboColor();
			// end defect 9974 

			//Do Validation
			if (aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				// defect 7898
				//	Add process to populate input fields wih lien data
				// when enter is selected and focus is on a LienhldrId.
				if (gettxtLienholder1Id().hasFocus())
				{
					// If the only action on Lien Entry was to enter a 
					//	Lien1HldrId, make the call to populate 
					//	Lien1Hldr1 Info and continue. 
					getLienHldr1Data();
				}
				if (gettxtLienholder2Id().hasFocus())
				{
					// If the only action on Lien Entry was to enter a 
					//	Lien2HldrId, make the call to populate 
					//	Lien2Hldr2 Info and continue. 
					getLienHldr2Data();
				}
				if (gettxtLienholder3Id().hasFocus())
				{
					// If the only action on Lien Entry was to enter a 
					//	Lien3HldrId, make the call to populate 
					//	Lien3Hldr3 Info and continue. 
					getLienHldr3Data();
				}
				// end defect 7898

				// defect 9974 
				verifyLienDataPopulation(leRTSEx);

				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				VehicleInquiryData laNewVehInqData =
					(VehicleInquiryData) UtilityMethods.copy(
						caVehInqData);

				setDataToVehObj(laNewVehInqData);

				if (isVTRAuthReqdForSSNInName())
				{
					getController().processData(
						VCLienEntryTTL035.VTR_AUTH,
						laNewVehInqData);

					if (UtilityMethods
						.isEmpty(
							laNewVehInqData
								.getVehMiscData()
								.getAuthCd()))
					{
						return;
					}
				}

				getController().processData(
					AbstractViewController.ENTER,
					laNewVehInqData);
				// end defect 9974 
			}
			// defect 9974 
			else if (aaAE.getSource() instanceof JComboBox)
			{
				handleComboSelection((JComboBox) aaAE.getSource());
			}
			// end defect 9974 
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				// defect 10592 
				VehicleInquiryData laNewVehInqData =
					(VehicleInquiryData) UtilityMethods.copy(
						caVehInqData);

				setDataToVehObj(laNewVehInqData);
				// end defect 10592

				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
			{
				String lsTransCd = getController().getTransCode();
				if (lsTransCd.equals(TransCdConstant.DTAORK)
					|| lsTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.TTL035B);
				}
				else if (
					lsTransCd.equals(TransCdConstant.DTANTD)
						|| lsTransCd.equals(TransCdConstant.DTAORD))
				{
					RTSHelp.displayHelp(RTSHelp.TTL035C);
				}
				else if (
					lsTransCd.equals(TransCdConstant.TITLE)
						|| lsTransCd.equals(TransCdConstant.NONTTL)
						|| lsTransCd.equals(TransCdConstant.REJCOR))
				{
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.TTL035A);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.TTL035D);
					}
				}
			}
			else if (aaAE.getSource() == getbtnDelete1stLien())
			{
				// defect 9974 
				resetLien1();
				// end defect 9974

				// defect 10592 
				getbtnDelete1stLien().setEnabled(false);
				// end defect 10592  

				// defect 7898
				//	Request focus on enter key
				getButtonPanel().getBtnEnter().requestFocus();
				// end defect 7898						
			}
			else if (aaAE.getSource() == getbtnDelete2ndLien())
			{
				// defect 9974 
				resetLien2();
				// end defect 9974 

				// defect 10592 
				getbtnDelete2ndLien().setEnabled(false);
				// end defect 10592

				// defect 7898
				//	Request focus on enter key
				getButtonPanel().getBtnEnter().requestFocus();
				// end defect 7898				
			}
			else if (aaAE.getSource() == getbtnDelete3rdLien())
			{
				// defect 9974 
				resetLien3();
				// end defect 9974 

				// defect 10592 
				getbtnDelete3rdLien().setEnabled(false);
				// end defect 10592

				// defect 7898
				//	Request focus on enter key
				getButtonPanel().getBtnEnter().requestFocus();
				// end defect 7898
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Add Listeners 
	 */
	private void addListeners()
	{
		getradioLocal1().addItemListener(this);
		getradioLocal2().addItemListener(this);
		getradioLocal3().addItemListener(this);
		ivjradioETitle.addItemListener(this);
		ivjradioCertified1.addItemListener(this);
		ivjradioCertified2.addItemListener(this);
		ivjradioCertified3.addItemListener(this);
		ivjcomboLienholder1.addActionListener(this);
		ivjcomboLienholder2.addActionListener(this);
		ivjcomboLienholder3.addActionListener(this);
	}

	/**
	 * Assign Lienholder Data to Screen Objects
	 * 
	 * @param aaTitleData
	 */
	private void assignCertfdLienholderData(TitleData laTitleData)
	{

		getradioETitle().setEnabled(cbETtlAllowd);

		cbETtl = laTitleData.isETitle() && cbETtlAllowd;

		if (cbETtl)
		{
			populateETtlCombo();
		}
		RTSException leRTSEx = new RTSException();
		String lsPermLienholderId = laTitleData.getPermLienHldrId1();

		// defect 10112 
		// Implement new get/setLienholderData() 
		if (TitleClientUtilityMethods
			.isValidPermLienhldrId(lsPermLienholderId))
		{
			Vector lvVector =
				cbETtl
					? svCurrentETtlCertLienhldr
					: svCurrentCertLienhldr;

			int liSelection =
				getcomboSelection(lsPermLienholderId, lvVector);

			if (liSelection >= 0)
			{
				getcomboLienholder1().setSelectedIndex(liSelection);

				laTitleData.setLienholderData(
					TitleConstant.LIENHLDR1,
					((CertifiedLienholderData) lvVector
						.elementAt(liSelection))
						.getLienholderData());
				csPermLienhldrId1 = lsPermLienholderId;
			}
			else
			{
				laTitleData.setLienholderData(
					TitleConstant.LIENHLDR1,
					new LienholderData());

				csPermLienhldrId1 = EMPTY;
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_NUM_CERTFD_LIENHLDR_NOT_AVAIL),
					getcomboLienholder1());

			}
			getradioETitle().setSelected(cbETtl);
			getradioCertified1().setSelected(!cbETtl);
			getcomboLienholder1().requestFocus();
		}
		else
		{
			getradioLocal1().setSelected(true);
			gettxtLienholder1Id().requestFocus();
		}

		lsPermLienholderId = laTitleData.getPermLienHldrId2();

		if (TitleClientUtilityMethods
			.isValidPermLienhldrId(lsPermLienholderId))
		{
			int liSelection =
				getcomboSelection(
					lsPermLienholderId,
					svCurrentCertLienhldr);

			if (liSelection >= 0)
			{
				getcomboLienholder2().setSelectedIndex(liSelection);
				laTitleData.setLienholderData(
					TitleConstant.LIENHLDR2,
					((CertifiedLienholderData) svCurrentCertLienhldr
						.elementAt(liSelection))
						.getLienholderData());
				csPermLienhldrId2 = laTitleData.getPermLienHldrId2();
			}
			else
			{
				csPermLienhldrId2 = EMPTY;
				laTitleData.setLienholderData(
					TitleConstant.LIENHLDR2,
					new LienholderData());
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_NUM_CERTFD_LIENHLDR_NOT_AVAIL),
					getcomboLienholder2());
			}
			getradioCertified2().setSelected(true);
		}
		else
		{
			getradioLocal2().setSelected(true);
		}

		lsPermLienholderId = laTitleData.getPermLienHldrId3();
		if (TitleClientUtilityMethods
			.isValidPermLienhldrId(lsPermLienholderId))
		{
			int liSelection =
				getcomboSelection(
					lsPermLienholderId,
					svCurrentCertLienhldr);
			if (liSelection >= 0)
			{
				getcomboLienholder3().setSelectedIndex(liSelection);
				laTitleData.setLienholderData(
					TitleConstant.LIENHLDR3,
					((CertifiedLienholderData) svCurrentCertLienhldr
						.elementAt(liSelection))
						.getLienholderData());
				csPermLienhldrId3 = laTitleData.getPermLienHldrId3();
			}
			else
			{
				csPermLienhldrId3 = EMPTY;
				laTitleData.setLienholderData(
					TitleConstant.LIENHLDR3,
					new LienholderData());
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_NUM_CERTFD_LIENHLDR_NOT_AVAIL),
					getcomboLienholder3());
			}
			getradioCertified3().setSelected(true);
		}
		// end defect 10112 
		else
		{
			getradioLocal3().setSelected(true);
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
		}
	}

	/**
	 * Handle Enabling of Lienholder 1 input fields  
	 */
	private void enableLien1Data()
	{
		boolean lbEnable = getradioLocal1().isSelected();
		getchkUSA1().setEnabled(lbEnable);
		getstcLblDash1().setEnabled(lbEnable);
		gettxtLienholder1Cntry().setEnabled(lbEnable);
		gettxtLienholder1CntryZpcd().setEnabled(lbEnable);
		gettxtLienholder1City().setEnabled(lbEnable);
		gettxtLienholder1Name1().setEnabled(lbEnable);
		gettxtLienholder1Name2().setEnabled(lbEnable);
		gettxtLienholder1State().setEnabled(lbEnable);
		gettxtLienholder1Street1().setEnabled(lbEnable);
		gettxtLienholder1Zpcd().setEnabled(lbEnable);
		gettxtLienholder1ZpcdP4().setEnabled(lbEnable);
		gettxtLienholder1Street2().setEnabled(lbEnable);
		getstcLblFirstLienId().setEnabled(lbEnable);
		gettxtLienholder1Id().setEnabled(lbEnable);
		getstcLblLien1Date().setEnabled(true);
		gettxtLien1Date().setEnabled(true);
		getcomboLienholder1().setEnabled(!lbEnable);
		enableLien2Data();
		enableLien3Data();
	}

	/**
	 * Handle Enabling of Lienholder 2 input fields  
	 */
	private void enableLien2Data()
	{
		boolean lbEnable =
			getradioLocal2().isSelected()
				&& !getradioETitle().isSelected();
		getchkUSA2().setEnabled(lbEnable);
		getstcLblDash2().setEnabled(lbEnable);
		gettxtLienholder2Cntry().setEnabled(lbEnable);
		gettxtLienholder2CntryZpcd().setEnabled(lbEnable);
		gettxtLienholder2City().setEnabled(lbEnable);
		gettxtLienholder2Name1().setEnabled(lbEnable);
		gettxtLienholder2Name2().setEnabled(lbEnable);
		gettxtLienholder2State().setEnabled(lbEnable);
		gettxtLienholder2Street1().setEnabled(lbEnable);
		gettxtLienholder2Zpcd().setEnabled(lbEnable);
		gettxtLienholder2ZpcdP4().setEnabled(lbEnable);
		gettxtLienholder2Street2().setEnabled(lbEnable);
		boolean lbETitle = getradioETitle().isSelected();
		gettxtLien2Date().setEnabled(!lbETitle);
		getstcLblDate2().setEnabled(!lbETitle);
		getradioCertified2().setEnabled(!lbETitle);
		getradioLocal2().setEnabled(!lbETitle);
		getstcLblSecondLienId().setEnabled(lbEnable);
		gettxtLienholder2Id().setEnabled(lbEnable);
		getcomboLienholder2().setEnabled(
			!lbETitle && getradioCertified2().isSelected());
	}

	/**
	 * Handle Enabling of Lienholder 2 input fields  
	 */
	private void enableLien3Data()
	{
		boolean lbEnable =
			getradioLocal3().isSelected()
				&& !getradioETitle().isSelected();

		getchkUSA3().setEnabled(lbEnable);
		getstcLblDash3().setEnabled(lbEnable);
		gettxtLienholder3Cntry().setEnabled(lbEnable);
		gettxtLienholder3CntryZpcd().setEnabled(lbEnable);
		gettxtLienholder3City().setEnabled(lbEnable);
		gettxtLienholder3Name1().setEnabled(lbEnable);
		gettxtLienholder3Name2().setEnabled(lbEnable);
		gettxtLienholder3State().setEnabled(lbEnable);
		gettxtLienholder3Street1().setEnabled(lbEnable);
		gettxtLienholder3Zpcd().setEnabled(lbEnable);
		gettxtLienholder3ZpcdP4().setEnabled(lbEnable);
		gettxtLienholder3Street2().setEnabled(lbEnable);
		boolean lbETitle = getradioETitle().isSelected();
		gettxtLien3Date().setEnabled(!lbETitle);
		getstcLblDate3().setEnabled(!lbETitle);
		getradioCertified3().setEnabled(!lbETitle);
		getradioLocal3().setEnabled(!lbETitle);
		getstcLblThirdLienId().setEnabled(lbEnable);
		gettxtLienholder3Id().setEnabled(lbEnable);
		getcomboLienholder3().setEnabled(
			!lbETitle && getradioCertified3().isSelected());
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aeFE FocusEvent
	 * 
	 */
	public void focusGained(FocusEvent aeFE)
	{
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aeFE FocusEvent
	 */
	public void focusLost(FocusEvent aeFE)
	{
		// defect 9474 
		if (!aeFE.isTemporary())
		{
			if (aeFE.getSource() == gettxtLienholder1Id())
			{
				getLienHldr1Data();
			}
			else if (
				aeFE.getSource() == gettxtLienholder2Id()
					&& !gettxtLienholder2Id().getText().equals(csLien2Id))
			{
				getLienHldr2Data();
			}
			else if (
				aeFE.getSource() == gettxtLienholder3Id()
					&& !gettxtLienholder3Id().getText().equals(csLien3Id))
			{
				getLienHldr3Data();
			}
		}
		// end defect 9474 
		// defect 10229 
		//		else
		//		{
		//			System.out.println("Is Temporary");
		//		}
		// end defect 10229 
	}

	/**
	 * Return the btnDelete1stLien property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnDelete1stLien()
	{
		if (ivjbtnDelete1stLien == null)
		{
			try
			{
				ivjbtnDelete1stLien = new RTSButton();
				ivjbtnDelete1stLien.setName("btnDelete1stLien");
				ivjbtnDelete1stLien.setMnemonic(KeyEvent.VK_1);
				ivjbtnDelete1stLien.setText(DEL_FIRST_LIEN);
				ivjbtnDelete1stLien.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjbtnDelete1stLien.setPreferredSize(
					new java.awt.Dimension(118, 24));
				ivjbtnDelete1stLien.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnDelete1stLien;
	}

	/**
	 * Return the btnDelete2ndLien property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnDelete2ndLien()
	{
		if (ivjbtnDelete2ndLien == null)
		{
			try
			{
				ivjbtnDelete2ndLien = new RTSButton();
				ivjbtnDelete2ndLien.setName("btnDelete2ndLien");
				ivjbtnDelete2ndLien.setMnemonic(KeyEvent.VK_2);
				ivjbtnDelete2ndLien.setText(DEL_SECOND_LIEN);
				ivjbtnDelete2ndLien.setRequestFocusEnabled(false);
				// user code begin {1}
				getbtnDelete2ndLien().addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnDelete2ndLien;
	}

	/**
	 * Return the btnDelete3rdLien property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnDelete3rdLien()
	{
		if (ivjbtnDelete3rdLien == null)
		{
			try
			{
				ivjbtnDelete3rdLien = new RTSButton();
				ivjbtnDelete3rdLien.setName("btnDelete3rdLien");
				ivjbtnDelete3rdLien.setMnemonic(KeyEvent.VK_3);
				ivjbtnDelete3rdLien.setText(DEL_THIRD_LIEN);
				ivjbtnDelete3rdLien.setRequestFocusEnabled(false);
				// user code begin {1}
				getbtnDelete3rdLien().addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnDelete3rdLien;
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel()
	{
		if (ivjButtonPanel == null)
		{
			try
			{
				ivjButtonPanel = new ButtonPanel();
				ivjButtonPanel.setName("ButtonPanel");
				ivjButtonPanel.setBounds(289, 451, 271, 42);
				ivjButtonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel;
	}

	/**
	 * Return the chkAdditionalLien property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkAdditionalLien()
	{
		if (ivjchkAdditionalLien == null)
		{
			try
			{
				ivjchkAdditionalLien = new JCheckBox();
				ivjchkAdditionalLien.setSize(184, 22);
				ivjchkAdditionalLien.setName("chkAdditionalLien");
				ivjchkAdditionalLien.setMaximumSize(
					new java.awt.Dimension(123, 22));
				ivjchkAdditionalLien.setActionCommand(
					"Additional Lien(s)");
				ivjchkAdditionalLien.setMinimumSize(
					new java.awt.Dimension(123, 22));
				// user code begin {1}
				ivjchkAdditionalLien.setText(ADDL_LEINS);
				// defect 10049 
				//ivjchkAdditionalLien.setMnemonic(KeyEvent.VK_L);
				ivjchkAdditionalLien.setMnemonic(KeyEvent.VK_X);
				// end defect 10049 
				ivjchkAdditionalLien.setLocation(50, 20);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkAdditionalLien;
	}

	/**
	 * Return the chkUSA1 property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkUSA1()
	{
		if (ivjchkUSA1 == null)
		{
			try
			{
				ivjchkUSA1 = new JCheckBox();
				ivjchkUSA1.setName("chkUSA1");
				ivjchkUSA1.setMnemonic(KeyEvent.VK_U);
				ivjchkUSA1.setText(CommonConstant.STR_USA);
				ivjchkUSA1.setMaximumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA1.setFocusPainted(true);
				ivjchkUSA1.setActionCommand("USA");
				ivjchkUSA1.setSelected(true);
				ivjchkUSA1.setMinimumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjchkUSA1.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkUSA1;
	}

	/**
	 * Return the chkUSA2 property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkUSA2()
	{
		if (ivjchkUSA2 == null)
		{
			try
			{
				ivjchkUSA2 = new JCheckBox();
				ivjchkUSA2.setName("chkUSA2");
				ivjchkUSA2.setMnemonic(KeyEvent.VK_S);
				ivjchkUSA2.setText(CommonConstant.STR_USA);
				ivjchkUSA2.setMaximumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA2.setActionCommand("USA");
				ivjchkUSA2.setSelected(true);
				ivjchkUSA2.setMinimumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA2.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjchkUSA2.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkUSA2;
	}

	/**
	 * Return the chkUSA3 property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkUSA3()
	{
		if (ivjchkUSA3 == null)
		{
			try
			{
				ivjchkUSA3 = new JCheckBox();
				ivjchkUSA3.setName("chkUSA3");
				ivjchkUSA3.setMnemonic(KeyEvent.VK_A);
				ivjchkUSA3.setText(CommonConstant.STR_USA);
				ivjchkUSA3.setMaximumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA3.setActionCommand("USA");
				ivjchkUSA3.setSelected(true);
				ivjchkUSA3.setMinimumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA3.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjchkUSA3.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkUSA3;
	}

	/**
	 * This method initializes ivjcomboLienholder1
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboLienholder1()
	{
		if (ivjcomboLienholder1 == null)
		{
			ivjcomboLienholder1 = new JComboBox();
			ivjcomboLienholder1.setSize(346, 25);
			ivjcomboLienholder1.setLocation(8, 14);
		}
		return ivjcomboLienholder1;
	}

	/**
	 * This method initializes ivjcomboLienholder2
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboLienholder2()
	{
		if (ivjcomboLienholder2 == null)
		{
			ivjcomboLienholder2 = new JComboBox();
			ivjcomboLienholder2.setSize(346, 25);
			ivjcomboLienholder2.setLocation(8, 165);
		}
		return ivjcomboLienholder2;
	}

	/**
	 * This method initializes ivjcomboLienholder3
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboLienholder3()
	{
		if (ivjcomboLienholder3 == null)
		{
			ivjcomboLienholder3 = new JComboBox();
			ivjcomboLienholder3.setBounds(8, 304, 346, 25);
		}
		return ivjcomboLienholder3;
	}

	/**
	 * Locate PermLienholderId in Vector
	 * 
	 * @param asPermLienholderId
	 * @return int
	 */
	private int getcomboSelection(
		String asPermLienholderId,
		Vector avVector)
	{
		int liSelected = -1;
		for (int i = 0; i < avVector.size(); i++)
		{
			CertifiedLienholderData laData =
				(CertifiedLienholderData) avVector.elementAt(i);

			if (asPermLienholderId.equals(laData.getPermLienHldrId()))
			{
				liSelected = i;
			}
		}
		return liSelected;
	}

	/**
	 * Return the FrmLienEntryTTL035ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmLienEntryTTL035ContentPane1()
	{
		if (ivjFrmLienEntryTTL035ContentPane1 == null)
		{
			try
			{
				ivjFrmLienEntryTTL035ContentPane1 = new JPanel();
				ivjFrmLienEntryTTL035ContentPane1.setName(
					"FrmLienEntryTTL035ContentPane1");
				ivjFrmLienEntryTTL035ContentPane1.setLayout(null);
				ivjFrmLienEntryTTL035ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmLienEntryTTL035ContentPane1.setMinimumSize(
					new java.awt.Dimension(612, 476));
				ivjFrmLienEntryTTL035ContentPane1.setBounds(0, 0, 0, 0);
				getFrmLienEntryTTL035ContentPane1().add(
					getstcLblName(),
					getstcLblName().getName());
				getFrmLienEntryTTL035ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmLienEntryTTL035ContentPane1().add(
					getstcLblAddress(),
					getstcLblAddress().getName());
				getFrmLienEntryTTL035ContentPane1().add(
					getButtonPanel(),
					getButtonPanel().getName());
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel12(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel22(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel32(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel4(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel5(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel(),
					null);
				// defect 9974 
				ivjFrmLienEntryTTL035ContentPane1.add(
					getcomboLienholder1(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getcomboLienholder2(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getcomboLienholder3(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel6(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel7(),
					null);
				ivjFrmLienEntryTTL035ContentPane1.add(
					getJPanel8(),
					null);
				// end defect 9974 
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmLienEntryTTL035ContentPane1;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel();
			java.awt.GridBagConstraints consGridBagConstraints90 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints91 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints93 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints94 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints92 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints95 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints96 =
				new java.awt.GridBagConstraints();
			consGridBagConstraints95.insets =
				new java.awt.Insets(2, 3, 1, 2);
			consGridBagConstraints95.ipadx = 262;
			consGridBagConstraints95.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints95.weightx = 1.0;
			consGridBagConstraints95.gridwidth = 3;
			consGridBagConstraints95.gridy = 2;
			consGridBagConstraints95.gridx = 0;
			consGridBagConstraints90.insets =
				new java.awt.Insets(5, 2, 3, 7);
			consGridBagConstraints90.ipady = 1;
			consGridBagConstraints90.ipadx = 25;
			consGridBagConstraints90.gridy = 0;
			consGridBagConstraints90.gridx = 0;
			consGridBagConstraints93.insets =
				new java.awt.Insets(3, 47, 2, 2);
			consGridBagConstraints93.ipadx = -44;
			consGridBagConstraints93.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints93.weightx = 1.0;
			consGridBagConstraints93.gridy = 0;
			consGridBagConstraints93.gridx = 2;
			consGridBagConstraints94.insets =
				new java.awt.Insets(2, 3, 2, 2);
			consGridBagConstraints94.ipadx = 262;
			consGridBagConstraints94.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints94.weightx = 1.0;
			consGridBagConstraints94.gridwidth = 3;
			consGridBagConstraints94.gridy = 1;
			consGridBagConstraints94.gridx = 0;
			consGridBagConstraints91.insets =
				new java.awt.Insets(3, 7, 2, 10);
			consGridBagConstraints91.ipadx = 25;
			consGridBagConstraints91.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints91.weightx = 1.0;
			consGridBagConstraints91.gridy = 0;
			consGridBagConstraints91.gridx = 1;
			consGridBagConstraints96.insets =
				new java.awt.Insets(2, 3, 5, 27);
			consGridBagConstraints96.ipady = -1;
			consGridBagConstraints96.ipadx = 3;
			consGridBagConstraints96.gridwidth = 2;
			consGridBagConstraints96.gridy = 3;
			consGridBagConstraints96.gridx = 0;
			consGridBagConstraints92.insets =
				new java.awt.Insets(3, 10, 2, 71);
			consGridBagConstraints92.ipady = 4;
			consGridBagConstraints92.ipadx = 9;
			consGridBagConstraints92.gridy = 0;
			consGridBagConstraints92.gridx = 2;
			jPanel.setLayout(new java.awt.GridBagLayout());
			jPanel.add(
				getstcLblThirdLienId(),
				consGridBagConstraints90);
			jPanel.add(gettxtLienholder3Id(), consGridBagConstraints91);
			jPanel.add(getstcLblDate3(), consGridBagConstraints92);
			jPanel.add(gettxtLien3Date(), consGridBagConstraints93);
			jPanel.add(
				gettxtLienholder3Name1(),
				consGridBagConstraints94);
			jPanel.add(
				gettxtLienholder3Name2(),
				consGridBagConstraints95);
			jPanel.add(getbtnDelete3rdLien(), consGridBagConstraints96);
			jPanel.setSize(271, 97);
			jPanel.setLocation(7, 339);
		}
		return jPanel;
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
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(285, 134));
				ivjJPanel1.setBounds(8, 443, 263, 55);
				ivjJPanel1.add(getchkAdditionalLien(), null);
				// defect 10049 
				// previously used CHK_ADDIT_LIENS_EXIST
				javax.swing.border.Border b =
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						CHK_IF_APPLICABLE);
				// end defect 10049 
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
	 * This method initializes jPanel1
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel12()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			java.awt.GridBagConstraints consGridBagConstraints105 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints104 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints106 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints107 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints109 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints110 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints108 =
				new java.awt.GridBagConstraints();
			consGridBagConstraints107.insets =
				new java.awt.Insets(5, 48, 1, 2);
			consGridBagConstraints107.ipadx = -44;
			consGridBagConstraints107.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints107.weightx = 1.0;
			consGridBagConstraints107.gridy = 0;
			consGridBagConstraints107.gridx = 2;
			consGridBagConstraints106.insets =
				new java.awt.Insets(5, 11, 1, 71);
			consGridBagConstraints106.ipady = 4;
			consGridBagConstraints106.ipadx = 9;
			consGridBagConstraints106.gridy = 0;
			consGridBagConstraints106.gridx = 2;
			consGridBagConstraints105.insets =
				new java.awt.Insets(3, 7, 3, 10);
			consGridBagConstraints105.ipadx = 25;
			consGridBagConstraints105.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints105.weightx = 1.0;
			consGridBagConstraints105.gridy = 0;
			consGridBagConstraints105.gridx = 1;
			consGridBagConstraints109.insets =
				new java.awt.Insets(2, 3, 1, 2);
			consGridBagConstraints109.ipadx = 262;
			consGridBagConstraints109.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints109.weightx = 1.0;
			consGridBagConstraints109.gridwidth = 3;
			consGridBagConstraints109.gridy = 2;
			consGridBagConstraints109.gridx = 0;
			consGridBagConstraints104.insets =
				new java.awt.Insets(5, 2, 4, 6);
			consGridBagConstraints104.ipady = 1;
			consGridBagConstraints104.ipadx = 29;
			consGridBagConstraints104.gridy = 0;
			consGridBagConstraints104.gridx = 0;
			consGridBagConstraints110.insets =
				new java.awt.Insets(2, 3, 5, 26);
			consGridBagConstraints110.ipady = -1;
			consGridBagConstraints110.ipadx = 4;
			consGridBagConstraints110.gridwidth = 2;
			consGridBagConstraints110.gridy = 3;
			consGridBagConstraints110.gridx = 0;
			consGridBagConstraints108.insets =
				new java.awt.Insets(2, 3, 1, 2);
			consGridBagConstraints108.ipadx = 262;
			consGridBagConstraints108.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints108.weightx = 1.0;
			consGridBagConstraints108.gridwidth = 3;
			consGridBagConstraints108.gridy = 1;
			consGridBagConstraints108.gridx = 0;
			jPanel1.setLayout(new java.awt.GridBagLayout());
			jPanel1.add(
				getstcLblFirstLienId(),
				consGridBagConstraints104);
			jPanel1.add(
				gettxtLienholder1Id(),
				consGridBagConstraints105);
			jPanel1.add(
				getstcLblLien1Date(),
				consGridBagConstraints106);
			jPanel1.add(gettxtLien1Date(), consGridBagConstraints107);
			jPanel1.add(
				gettxtLienholder1Name1(),
				consGridBagConstraints108);
			jPanel1.add(
				gettxtLienholder1Name2(),
				consGridBagConstraints109);
			jPanel1.add(
				getbtnDelete1stLien(),
				consGridBagConstraints110);
			jPanel1.setBounds(7, 62, 271, 97);
		}
		return jPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(null);
				getJPanel2().add(
					getstcLblDash1(),
					getstcLblDash1().getName());
				getJPanel2().add(
					gettxtLienholder1City(),
					gettxtLienholder1City().getName());
				getJPanel2().add(
					gettxtLienholder1State(),
					gettxtLienholder1State().getName());
				getJPanel2().add(
					gettxtLienholder1Zpcd(),
					gettxtLienholder1Zpcd().getName());
				getJPanel2().add(
					gettxtLienholder1ZpcdP4(),
					gettxtLienholder1ZpcdP4().getName());
				getJPanel2().add(
					gettxtLienholder1Cntry(),
					gettxtLienholder1Cntry().getName());
				ivjJPanel2.add(gettxtLienholder1CntryZpcd(), null);
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
	 * This method initializes jPanel2
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel22()
	{
		if (jPanel2 == null)
		{
			jPanel2 = new JPanel();
			java.awt.GridBagConstraints consGridBagConstraints98 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints99 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints100 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints97 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints101 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints102 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints103 =
				new java.awt.GridBagConstraints();
			consGridBagConstraints102.insets =
				new java.awt.Insets(2, 3, 1, 2);
			consGridBagConstraints102.ipadx = 262;
			consGridBagConstraints102.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints102.weightx = 1.0;
			consGridBagConstraints102.gridwidth = 3;
			consGridBagConstraints102.gridy = 2;
			consGridBagConstraints102.gridx = 0;
			consGridBagConstraints103.insets =
				new java.awt.Insets(2, 3, 5, 26);
			consGridBagConstraints103.ipady = -1;
			consGridBagConstraints103.ipadx = 1;
			consGridBagConstraints103.gridwidth = 2;
			consGridBagConstraints103.gridy = 3;
			consGridBagConstraints103.gridx = 0;
			consGridBagConstraints98.insets =
				new java.awt.Insets(3, 8, 2, 10);
			consGridBagConstraints98.ipadx = 26;
			consGridBagConstraints98.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints98.weightx = 1.0;
			consGridBagConstraints98.gridy = 0;
			consGridBagConstraints98.gridx = 1;
			consGridBagConstraints101.insets =
				new java.awt.Insets(2, 3, 2, 2);
			consGridBagConstraints101.ipadx = 262;
			consGridBagConstraints101.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints101.weightx = 1.0;
			consGridBagConstraints101.gridwidth = 3;
			consGridBagConstraints101.gridy = 1;
			consGridBagConstraints101.gridx = 0;
			consGridBagConstraints99.insets =
				new java.awt.Insets(3, 11, 2, 71);
			consGridBagConstraints99.ipady = 4;
			consGridBagConstraints99.ipadx = 9;
			consGridBagConstraints99.gridy = 0;
			consGridBagConstraints99.gridx = 2;
			consGridBagConstraints97.insets =
				new java.awt.Insets(5, 2, 5, 7);
			consGridBagConstraints97.ipady = -1;
			consGridBagConstraints97.ipadx = 8;
			consGridBagConstraints97.gridy = 0;
			consGridBagConstraints97.gridx = 0;
			consGridBagConstraints100.insets =
				new java.awt.Insets(3, 48, 2, 2);
			consGridBagConstraints100.ipadx = -44;
			consGridBagConstraints100.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints100.weightx = 1.0;
			consGridBagConstraints100.gridy = 0;
			consGridBagConstraints100.gridx = 2;
			jPanel2.setLayout(new java.awt.GridBagLayout());
			jPanel2.add(
				getstcLblSecondLienId(),
				consGridBagConstraints97);
			jPanel2.add(
				gettxtLienholder2Id(),
				consGridBagConstraints98);
			jPanel2.add(getstcLblDate2(), consGridBagConstraints99);
			jPanel2.add(gettxtLien2Date(), consGridBagConstraints100);
			jPanel2.add(
				gettxtLienholder2Name1(),
				consGridBagConstraints101);
			jPanel2.add(
				gettxtLienholder2Name2(),
				consGridBagConstraints102);
			jPanel2.add(
				getbtnDelete2ndLien(),
				consGridBagConstraints103);
			jPanel2.setSize(271, 110);
			jPanel2.setLocation(7, 194);
		}
		return jPanel2;
	}

	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(null);
				getJPanel3().add(
					getstcLblDash2(),
					getstcLblDash2().getName());
				getJPanel3().add(
					gettxtLienholder2City(),
					gettxtLienholder2City().getName());
				getJPanel3().add(
					gettxtLienholder2State(),
					gettxtLienholder2State().getName());
				getJPanel3().add(
					gettxtLienholder2Zpcd(),
					gettxtLienholder2Zpcd().getName());
				getJPanel3().add(
					gettxtLienholder2ZpcdP4(),
					gettxtLienholder2ZpcdP4().getName());
				getJPanel3().add(
					gettxtLienholder2Cntry(),
					gettxtLienholder2Cntry().getName());
				ivjJPanel3.add(gettxtLienholder2CntryZpcd(), null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * Return the JPanel31 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel31()
	{
		if (ivjJPanel31 == null)
		{
			try
			{
				ivjJPanel31 = new JPanel();
				ivjJPanel31.setName("JPanel31");
				ivjJPanel31.setLayout(null);
				getJPanel31().add(
					getstcLblDash3(),
					getstcLblDash3().getName());
				getJPanel31().add(
					gettxtLienholder3State(),
					gettxtLienholder3State().getName());
				getJPanel31().add(
					gettxtLienholder3Zpcd(),
					gettxtLienholder3Zpcd().getName());
				getJPanel31().add(
					gettxtLienholder3ZpcdP4(),
					gettxtLienholder3ZpcdP4().getName());
				ivjJPanel31.add(gettxtLienholder3City(), null);
				ivjJPanel31.add(gettxtLienholder3Cntry(), null);
				ivjJPanel31.add(gettxtLienholder3CntryZpcd(), null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel31;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel32()
	{
		if (jPanel3 == null)
		{
			jPanel3 = new JPanel();
			java.awt.GridBagConstraints consGridBagConstraints65 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints66 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints67 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints68 =
				new java.awt.GridBagConstraints();
			consGridBagConstraints67.insets =
				new java.awt.Insets(2, 6, 0, 11);
			consGridBagConstraints67.ipadx = 262;
			consGridBagConstraints67.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints67.weightx = 1.0;
			consGridBagConstraints67.gridy = 2;
			consGridBagConstraints67.gridx = 0;
			consGridBagConstraints65.insets =
				new java.awt.Insets(3, 200, 1, 27);
			consGridBagConstraints65.ipady = -6;
			consGridBagConstraints65.ipadx = 7;
			consGridBagConstraints65.gridy = 0;
			consGridBagConstraints65.gridx = 0;
			consGridBagConstraints68.insets =
				new java.awt.Insets(1, 3, 4, 7);
			consGridBagConstraints68.ipady = 28;
			consGridBagConstraints68.ipadx = 272;
			consGridBagConstraints68.fill =
				java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints68.weighty = 1.0;
			consGridBagConstraints68.weightx = 1.0;
			consGridBagConstraints68.gridy = 3;
			consGridBagConstraints68.gridx = 0;
			consGridBagConstraints66.insets =
				new java.awt.Insets(1, 6, 1, 11);
			consGridBagConstraints66.ipadx = 262;
			consGridBagConstraints66.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints66.weightx = 1.0;
			consGridBagConstraints66.gridy = 1;
			consGridBagConstraints66.gridx = 0;
			jPanel3.setLayout(new java.awt.GridBagLayout());
			jPanel3.add(getchkUSA1(), consGridBagConstraints65);
			jPanel3.add(
				gettxtLienholder1Street1(),
				consGridBagConstraints66);
			jPanel3.add(
				gettxtLienholder1Street2(),
				consGridBagConstraints67);
			jPanel3.add(getJPanel2(), consGridBagConstraints68);
			jPanel3.setBounds(292, 66, 283, 94);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel4
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel4()
	{
		if (jPanel4 == null)
		{
			jPanel4 = new JPanel();
			java.awt.GridBagConstraints consGridBagConstraints62 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints63 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints64 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints61 =
				new java.awt.GridBagConstraints();
			consGridBagConstraints64.insets =
				new java.awt.Insets(2, 3, 0, 7);
			consGridBagConstraints64.ipady = 26;
			consGridBagConstraints64.ipadx = 272;
			consGridBagConstraints64.fill =
				java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints64.weighty = 1.0;
			consGridBagConstraints64.weightx = 1.0;
			consGridBagConstraints64.gridy = 4;
			consGridBagConstraints64.gridx = 0;
			consGridBagConstraints62.insets =
				new java.awt.Insets(3, 6, 2, 11);
			consGridBagConstraints62.ipadx = 262;
			consGridBagConstraints62.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints62.weightx = 1.0;
			consGridBagConstraints62.gridy = 1;
			consGridBagConstraints62.gridx = 0;
			consGridBagConstraints61.insets =
				new java.awt.Insets(1, 200, 3, 27);
			consGridBagConstraints61.ipady = -6;
			consGridBagConstraints61.ipadx = 7;
			consGridBagConstraints61.gridy = 0;
			consGridBagConstraints61.gridx = 0;
			consGridBagConstraints63.insets =
				new java.awt.Insets(2, 6, 2, 11);
			consGridBagConstraints63.ipadx = 262;
			consGridBagConstraints63.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints63.weightx = 1.0;
			consGridBagConstraints63.gridy = 2;
			consGridBagConstraints63.gridx = 0;
			jPanel4.setLayout(new java.awt.GridBagLayout());
			jPanel4.add(getchkUSA2(), consGridBagConstraints61);
			jPanel4.add(
				gettxtLienholder2Street1(),
				consGridBagConstraints62);
			jPanel4.add(
				gettxtLienholder2Street2(),
				consGridBagConstraints63);
			jPanel4.add(getJPanel3(), consGridBagConstraints64);
			jPanel4.setSize(283, 100);
			jPanel4.setLocation(292, 199);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel5
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel5()
	{
		if (jPanel5 == null)
		{
			jPanel5 = new JPanel();
			java.awt.GridBagConstraints consGridBagConstraints57 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints59 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints60 =
				new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints58 =
				new java.awt.GridBagConstraints();
			consGridBagConstraints60.insets =
				new java.awt.Insets(11, 200, 2, 27);
			consGridBagConstraints60.ipady = -6;
			consGridBagConstraints60.ipadx = 7;
			consGridBagConstraints60.gridy = 0;
			consGridBagConstraints60.gridx = 0;
			consGridBagConstraints57.insets =
				new java.awt.Insets(3, 6, 2, 11);
			consGridBagConstraints57.ipadx = 262;
			consGridBagConstraints57.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints57.weightx = 1.0;
			consGridBagConstraints57.gridy = 1;
			consGridBagConstraints57.gridx = 0;
			consGridBagConstraints58.insets =
				new java.awt.Insets(2, 6, 1, 11);
			consGridBagConstraints58.ipadx = 262;
			consGridBagConstraints58.fill =
				java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints58.weightx = 1.0;
			consGridBagConstraints58.gridy = 2;
			consGridBagConstraints58.gridx = 0;
			consGridBagConstraints59.insets =
				new java.awt.Insets(2, 3, 1, 7);
			consGridBagConstraints59.ipady = 26;
			consGridBagConstraints59.ipadx = 272;
			consGridBagConstraints59.fill =
				java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints59.weighty = 1.0;
			consGridBagConstraints59.weightx = 1.0;
			consGridBagConstraints59.gridy = 3;
			consGridBagConstraints59.gridx = 0;
			jPanel5.setLayout(new java.awt.GridBagLayout());
			jPanel5.add(
				gettxtLienholder3Street1(),
				consGridBagConstraints57);
			jPanel5.add(
				gettxtLienholder3Street2(),
				consGridBagConstraints58);
			jPanel5.add(getJPanel31(), consGridBagConstraints59);
			jPanel5.add(getchkUSA3(), consGridBagConstraints60);
			jPanel5.setSize(283, 108);
			jPanel5.setLocation(292, 332);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jPanel6
	 * 
	 * @returnJPanel
	 */
	private JPanel getJPanel6()
	{
		if (jPanel6 == null)
		{
			jPanel6 = new javax.swing.JPanel();
			jPanel6.setLayout(null);
			jPanel6.add(getradioLocal1(), null);
			jPanel6.add(getradioCertified1(), null);
			jPanel6.add(getradioETitle(), null);
			jPanel6.setSize(215, 25);
			jPanel6.setLocation(364, 14);
			caButtonGroup1 = new RTSButtonGroup();
			caButtonGroup1.add(getradioLocal1());
			caButtonGroup1.add(getradioCertified1());
			caButtonGroup1.add(getradioETitle());
		}
		return jPanel6;
	}

	/**
	 * This method initializes jPanel7
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel7()
	{
		if (jPanel7 == null)
		{
			jPanel7 = new javax.swing.JPanel();
			jPanel7.setLayout(null);
			jPanel7.add(getradioLocal2(), null);
			jPanel7.add(getradioCertified2(), null);
			jPanel7.setBounds(364, 164, 215, 25);
			caButtonGroup2 = new RTSButtonGroup();
			caButtonGroup2.add(getradioLocal2());
			caButtonGroup2.add(getradioCertified2());
		}
		return jPanel7;
	}

	/**
	 * This method initializes jPanel8
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel8()
	{
		if (jPanel8 == null)
		{
			jPanel8 = new javax.swing.JPanel();
			jPanel8.setLayout(null);
			jPanel8.add(getradioLocal3(), null);
			jPanel8.add(getradioCertified3(), null);
			jPanel8.setBounds(364, 304, 215, 25);
			caButtonGroup3 = new RTSButtonGroup();
			caButtonGroup3.add(getradioLocal3());
			caButtonGroup3.add(getradioCertified3());
		}
		return jPanel8;
	}

	/**
	 * Get Lienholder 1 data 
	 */
	private void getLienHldr1Data()
	{
		// defect 9974 
		String lsId =
			UtilityMethods.removeLeadingZeros(
				gettxtLienholder1Id().getText());

		gettxtLienholder1Id().setText(lsId);

		if (!UtilityMethods.isEmpty(lsId) && !lsId.equals(csLien1Id))
		{
			ciFlag = 1;
			retrieveLienholderData();
		}
		// end defect 9974 
	}

	/**
	 * Get Lienholder 2 data 
	 */
	private void getLienHldr2Data()
	{
		// defect 9974 
		String lsId =
			UtilityMethods.removeLeadingZeros(
				gettxtLienholder2Id().getText());

		gettxtLienholder2Id().setText(lsId);

		if (!UtilityMethods.isEmpty(lsId) && !lsId.equals(csLien2Id))
		{
			ciFlag = 2;
			retrieveLienholderData();
		}
		// end defect 9974 
	}

	/**
	 * Get Lienholder 3 data 
	 */
	private void getLienHldr3Data()
	{
		// defect 9974 
		String lsId =
			UtilityMethods.removeLeadingZeros(
				gettxtLienholder3Id().getText());

		gettxtLienholder3Id().setText(lsId);

		if (!UtilityMethods.isEmpty(lsId) && !lsId.equals(csLien3Id))
		{
			ciFlag = 3;
			retrieveLienholderData();
		}
		// end defect 9974 
	}

	/**
	 * This method initializes ivjradioCertified1
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCertified1()
	{
		if (ivjradioCertified1 == null)
		{
			ivjradioCertified1 = new JRadioButton();
			ivjradioCertified1.setBounds(73, 0, 73, 24);
			ivjradioCertified1.setText(CERTIFIED);
			// defect 10049 
			ivjradioCertified1.setMnemonic(KeyEvent.VK_R);
			// end defect 10049 
		}
		return ivjradioCertified1;
	}

	/**
	 * This method initializes ivjradioCertified2
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCertified2()
	{
		if (ivjradioCertified2 == null)
		{
			ivjradioCertified2 = new JRadioButton();
			ivjradioCertified2.setSize(73, 24);
			ivjradioCertified2.setText(CERTIFIED);
			// defect 10049 
			ivjradioCertified2.setMnemonic(KeyEvent.VK_T);
			// end defect 10049 
			ivjradioCertified2.setLocation(73, 0);
		}
		return ivjradioCertified2;
	}

	/**
	 * This method initializes ivjradioCertified3
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCertified3()
	{
		if (ivjradioCertified3 == null)
		{
			ivjradioCertified3 = new JRadioButton();
			ivjradioCertified3.setSize(73, 24);
			ivjradioCertified3.setText(CERTIFIED);
			// defect 10049 
			ivjradioCertified3.setMnemonic(KeyEvent.VK_I);
			// end defect 10049 
			ivjradioCertified3.setLocation(73, 0);
		}
		return ivjradioCertified3;
	}

	/**
	 * This method initializes ivjradioETitle
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioETitle()
	{
		if (ivjradioETitle == null)
		{
			ivjradioETitle = new JRadioButton();
			ivjradioETitle.setBounds(149, 0, 56, 24);
			ivjradioETitle.setText(ETITLE);
			// defect 10049 
			ivjradioETitle.setMnemonic(KeyEvent.VK_E);
			// end defect 10049 
		}
		return ivjradioETitle;
	}

	/**
	 * This method initializes ivjradioLocal1
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioLocal1()
	{
		if (ivjradioLocal1 == null)
		{
			ivjradioLocal1 = new JRadioButton();
			ivjradioLocal1.setBounds(10, 0, 56, 24);
			ivjradioLocal1.setText(LOCAL);
			// defect 10049 
			ivjradioLocal1.setMnemonic(KeyEvent.VK_L);
			// end defect 10049 

		}
		return ivjradioLocal1;
	}

	/**
	 * This method initializes ivjradioLocal2
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioLocal2()
	{
		if (ivjradioLocal2 == null)
		{
			ivjradioLocal2 = new JRadioButton();
			ivjradioLocal2.setBounds(10, 0, 56, 24);
			ivjradioLocal2.setText(LOCAL);
			// defect 10049 
			ivjradioLocal2.setMnemonic(KeyEvent.VK_O);
			// end defect 10049 
		}
		return ivjradioLocal2;
	}

	/**
	 * This method initializes ivjradioLocal3
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioLocal3()
	{
		if (ivjradioLocal3 == null)
		{
			ivjradioLocal3 = new JRadioButton();
			ivjradioLocal3.setSize(56, 24);
			ivjradioLocal3.setText(LOCAL);
			// defect 10049 
			ivjradioLocal3.setMnemonic(KeyEvent.VK_C);
			// end defect 10049 
			ivjradioLocal3.setLocation(10, 0);
		}
		return ivjradioLocal3;
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
				ivjstcLblAddress.setSize(60, 14);
				ivjstcLblAddress.setName("stcLblAddress");
				ivjstcLblAddress.setText(ADDRESS);
				ivjstcLblAddress.setMaximumSize(
					new java.awt.Dimension(48, 14));
				ivjstcLblAddress.setMinimumSize(
					new java.awt.Dimension(48, 14));
				ivjstcLblAddress.setLocation(410, 44);
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
	 * Return the stcLblDash1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDash1()
	{
		if (ivjstcLblDash1 == null)
		{
			try
			{
				ivjstcLblDash1 = new JLabel();
				ivjstcLblDash1.setName("stcLblDash1");
				ivjstcLblDash1.setText(CommonConstant.STR_DASH);
				ivjstcLblDash1.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash1.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash1.setBounds(218, 8, 9, 9);
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
		return ivjstcLblDash1;
	}

	/**
	 * Return the stcLblDash2 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDash2()
	{
		if (ivjstcLblDash2 == null)
		{
			try
			{
				ivjstcLblDash2 = new JLabel();
				ivjstcLblDash2.setName("stcLblDash2");
				ivjstcLblDash2.setText(CommonConstant.STR_DASH);
				ivjstcLblDash2.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash2.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash2.setBounds(218, 8, 9, 9);
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
		return ivjstcLblDash2;
	}

	/**
	 * Return the stcLblDash3 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash3()
	{
		if (ivjstcLblDash3 == null)
		{
			try
			{
				ivjstcLblDash3 = new JLabel();
				ivjstcLblDash3.setName("stcLblDash3");
				ivjstcLblDash3.setText(CommonConstant.STR_DASH);
				ivjstcLblDash3.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash3.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash3.setBounds(218, 9, 9, 9);
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
		return ivjstcLblDash3;
	}

	/**
	 * Return the stcLblDate2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDate2()
	{
		if (ivjstcLblDate2 == null)
		{
			try
			{
				ivjstcLblDate2 = new JLabel();
				ivjstcLblDate2.setName("stcLblDate2");
				ivjstcLblDate2.setText(DATE);
				ivjstcLblDate2.setMaximumSize(
					new java.awt.Dimension(29, 14));
				ivjstcLblDate2.setMinimumSize(
					new java.awt.Dimension(29, 14));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDate2;
	}

	/**
	 * Return the stcLblDate3 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDate3()
	{
		if (ivjstcLblDate3 == null)
		{
			try
			{
				ivjstcLblDate3 = new JLabel();
				ivjstcLblDate3.setName("stcLblDate3");
				ivjstcLblDate3.setText(DATE);
				ivjstcLblDate3.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblDate3.setMinimumSize(
					new java.awt.Dimension(26, 14));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end

				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDate3;
	}

	/**
	 * Return the stcLblFirstLienId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblFirstLienId()
	{
		if (ivjstcLblFirstLienId == null)
		{
			try
			{
				ivjstcLblFirstLienId = new JLabel();
				ivjstcLblFirstLienId.setName("stcLblFirstLienId");
				ivjstcLblFirstLienId.setMaximumSize(
					new java.awt.Dimension(68, 14));
				ivjstcLblFirstLienId.setMinimumSize(
					new java.awt.Dimension(68, 14));
				// user code begin {1}					
				ivjstcLblFirstLienId.setDisplayedMnemonic(
					KeyEvent.VK_F);
				ivjstcLblFirstLienId.setLabelFor(gettxtLienholder1Id());
				ivjstcLblFirstLienId.setText(FIRST_LIEN_ID);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblFirstLienId;
	}

	/**
	 * Return the ivjstcLblLien1Date property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLien1Date()
	{
		if (ivjstcLblLien1Date == null)
		{
			try
			{
				ivjstcLblLien1Date = new JLabel();
				ivjstcLblLien1Date.setName("ivjstcLblLien1Date");
				ivjstcLblLien1Date.setText(DATE);
				ivjstcLblLien1Date.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblLien1Date.setMinimumSize(
					new java.awt.Dimension(26, 14));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblLien1Date;
	}

	/**
	 * Return the stcLblName property value.
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
				ivjstcLblName.setSize(44, 14);
				ivjstcLblName.setName("stcLblName");
				ivjstcLblName.setText(NAME);
				ivjstcLblName.setMaximumSize(
					new java.awt.Dimension(33, 14));
				ivjstcLblName.setMinimumSize(
					new java.awt.Dimension(33, 14));
				ivjstcLblName.setLocation(110, 44);
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
	 * Return the stcLblSecondLienId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSecondLienId()
	{
		if (ivjstcLblSecondLienId == null)
		{
			try
			{
				ivjstcLblSecondLienId = new JLabel();
				ivjstcLblSecondLienId.setName("stcLblSecondLienId");
				ivjstcLblSecondLienId.setMaximumSize(
					new java.awt.Dimension(83, 14));
				ivjstcLblSecondLienId.setMinimumSize(
					new java.awt.Dimension(83, 14));
				// user code begin {1}
				ivjstcLblSecondLienId.setDisplayedMnemonic(
					KeyEvent.VK_N);
				ivjstcLblSecondLienId.setLabelFor(
					gettxtLienholder2Id());
				ivjstcLblSecondLienId.setText(SECOND_LIEN_ID);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblSecondLienId;
	}

	/**
	 * Return the stcLblThirdLienId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblThirdLienId()
	{
		if (ivjstcLblThirdLienId == null)
		{
			try
			{
				ivjstcLblThirdLienId = new JLabel();
				ivjstcLblThirdLienId.setName("stcLblThirdlienId");
				ivjstcLblThirdLienId.setMaximumSize(
					new java.awt.Dimension(69, 14));
				ivjstcLblThirdLienId.setMinimumSize(
					new java.awt.Dimension(69, 14));
				// user code begin {1}					
				ivjstcLblThirdLienId.setDisplayedMnemonic(
					KeyEvent.VK_D);
				ivjstcLblThirdLienId.setLabelFor(gettxtLienholder3Id());
				ivjstcLblThirdLienId.setText(THIRD_LIEN_ID);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblThirdLienId;
	}

	/**
	 * Return the txtLien1Date property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtLien1Date()
	{
		if (ivjtxtLien1Date == null)
		{
			try
			{
				ivjtxtLien1Date = new RTSDateField();
				ivjtxtLien1Date.setName("txtLien1Date");
				ivjtxtLien1Date.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLien1Date;
	}

	/**
	 * Return the txtLien2Date property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtLien2Date()
	{
		if (ivjtxtLien2Date == null)
		{
			try
			{
				ivjtxtLien2Date = new RTSDateField();
				ivjtxtLien2Date.setName("txtLien2Date");
				ivjtxtLien2Date.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLien2Date;
	}

	/**
	 * Return the txtLien3Date property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtLien3Date()
	{
		if (ivjtxtLien3Date == null)
		{
			try
			{
				ivjtxtLien3Date = new RTSDateField();
				ivjtxtLien3Date.setName("txtLien3Date");
				ivjtxtLien3Date.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLien3Date;
	}

	/**
	 * Return the txtLienholder1City property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1City()
	{
		if (ivjtxtLienholder1City == null)
		{
			try
			{
				ivjtxtLienholder1City = new RTSInputField();
				ivjtxtLienholder1City.setName("txtLienholder1City");
				ivjtxtLienholder1City.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder1City.setBounds(4, 4, 116, 20);
				// user code begin
				ivjtxtLienholder1City.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder1City.setMaxLength(
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
		return ivjtxtLienholder1City;
	}

	/**
	 * Return the txtLienholder1Cntry property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1Cntry()
	{
		if (ivjtxtLienholder1Cntry == null)
		{
			try
			{
				ivjtxtLienholder1Cntry = new RTSInputField();
				ivjtxtLienholder1Cntry.setName("txtLienholder1Cntry");
				ivjtxtLienholder1Cntry.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder1Cntry.setBounds(124, 4, 52, 20);
				// user code begin {1}				
				ivjtxtLienholder1Cntry.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtLienholder1Cntry.setMaxLength(
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
		return ivjtxtLienholder1Cntry;
	}

	/**
	 * Return the ivjtxtLienholder1CntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1CntryZpcd()
	{
		if (ivjtxtLienholder1CntryZpcd == null)
		{
			try
			{
				ivjtxtLienholder1CntryZpcd = new RTSInputField();
				ivjtxtLienholder1CntryZpcd.setBounds(180, 3, 74, 20);
				ivjtxtLienholder1CntryZpcd.setName(
					"txtLienholder1CntryZpcd");
				ivjtxtLienholder1CntryZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}						
				ivjtxtLienholder1CntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtLienholder1CntryZpcd.setMaxLength(
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
		return ivjtxtLienholder1CntryZpcd;
	}

	/**
	 * Return the txtLienholder1Id property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1Id()
	{
		if (ivjtxtLienholder1Id == null)
		{
			try
			{
				ivjtxtLienholder1Id = new RTSInputField();
				ivjtxtLienholder1Id.setName("txtLienholder1Id");
				ivjtxtLienholder1Id.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				// Added code so that the backward and forward
				// FocusTraversalKeys would not pass up KeyPressed.  We
				// will have to handle where the focus should go there.
				ivjtxtLienholder1Id.setFocusTraversalKeys(
					KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtLienholder1Id.setFocusTraversalKeys(
					KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtLienholder1Id.addKeyListener(this);
				ivjtxtLienholder1Id.addFocusListener(this);
				ivjtxtLienholder1Id.setMaxLength(
					LocalOptionConstant.LENGTH_LIENHOLDER_ID);
				ivjtxtLienholder1Id.setInput(
					RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLienholder1Id;
	}

	/**
	 * Return the txtLienholder1Name1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1Name1()
	{
		if (ivjtxtLienholder1Name1 == null)
		{
			try
			{
				ivjtxtLienholder1Name1 = new RTSInputField();
				ivjtxtLienholder1Name1.setName("txtLienholder1Name1");
				ivjtxtLienholder1Name1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}				
				ivjtxtLienholder1Name1.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder1Name1.setMaxLength(
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
		return ivjtxtLienholder1Name1;
	}

	/**
	 * Return the txtLienholder1Name2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1Name2()
	{
		if (ivjtxtLienholder1Name2 == null)
		{
			try
			{
				ivjtxtLienholder1Name2 = new RTSInputField();
				ivjtxtLienholder1Name2.setName("txtLienholder1Name2");
				ivjtxtLienholder1Name2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}					
				ivjtxtLienholder1Name2.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder1Name2.setMaxLength(
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
		return ivjtxtLienholder1Name2;
	}

	/**
	 * Return the txtLienholder1State property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1State()
	{
		if (ivjtxtLienholder1State == null)
		{
			try
			{
				ivjtxtLienholder1State = new RTSInputField();
				ivjtxtLienholder1State.setName("txtLienholder1State");
				ivjtxtLienholder1State.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder1State.setBounds(123, 4, 29, 20);
				// user code begin {1}
				ivjtxtLienholder1State.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtLienholder1State.setMaxLength(
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
		return ivjtxtLienholder1State;
	}

	/**
	 * Return the txtLienholder1Street1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1Street1()
	{
		if (ivjtxtLienholder1Street1 == null)
		{
			try
			{
				ivjtxtLienholder1Street1 = new RTSInputField();
				ivjtxtLienholder1Street1.setName(
					"txtLienholder1Street1");
				ivjtxtLienholder1Street1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}				
				ivjtxtLienholder1Street1.setInput(
					RTSInputField.DEFAULT);
				ivjtxtLienholder1Street1.setMaxLength(
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
		return ivjtxtLienholder1Street1;
	}

	/**
	 * Return the txtLienholder1Street2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholder1Street2()
	{
		if (ivjtxtLienholder1Street2 == null)
		{
			try
			{
				ivjtxtLienholder1Street2 = new RTSInputField();
				ivjtxtLienholder1Street2.setName(
					"txtLienholder1Street2");
				ivjtxtLienholder1Street2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}					
				ivjtxtLienholder1Street2.setInput(
					RTSInputField.DEFAULT);
				ivjtxtLienholder1Street2.setMaxLength(
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
		return ivjtxtLienholder1Street2;
	}

	/**
	 * Return the txtLienholder1Zpcd property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholder1Zpcd()
	{
		if (ivjtxtLienholder1Zpcd == null)
		{
			try
			{
				ivjtxtLienholder1Zpcd = new RTSInputField();
				ivjtxtLienholder1Zpcd.setName("txtLienholder1Zpcd");
				ivjtxtLienholder1Zpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder1Zpcd.setBounds(159, 4, 51, 20);
				// user code begin {1}				
				ivjtxtLienholder1Zpcd.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholder1Zpcd.setMaxLength(
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
		return ivjtxtLienholder1Zpcd;
	}

	/**
	 * Return the ivjtxtLienholder1ZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder1ZpcdP4()
	{
		if (ivjtxtLienholder1ZpcdP4 == null)
		{
			try
			{
				ivjtxtLienholder1ZpcdP4 = new RTSInputField();
				ivjtxtLienholder1ZpcdP4.setName(
					"ivjtxtLienholder1ZpcdP4");
				ivjtxtLienholder1ZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder1ZpcdP4.setBounds(231, 4, 37, 20);
				// user code begin {1}				
				ivjtxtLienholder1ZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholder1ZpcdP4.setMaxLength(
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
		return ivjtxtLienholder1ZpcdP4;
	}

	/**
	 * Return the txtLienholder2City property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholder2City()
	{
		if (ivjtxtLienholder2City == null)
		{
			try
			{
				ivjtxtLienholder2City = new RTSInputField();
				ivjtxtLienholder2City.setName("txtLienholder2City");
				ivjtxtLienholder2City.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder2City.setBounds(3, 3, 116, 20);
				// user code begin {1}							
				ivjtxtLienholder2City.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder2City.setMaxLength(
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
		return ivjtxtLienholder2City;
	}

	/**
	 * Return the txtLienholder2Cntry property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLienholder2Cntry()
	{
		if (ivjtxtLienholder2Cntry == null)
		{
			try
			{
				ivjtxtLienholder2Cntry = new RTSInputField();
				ivjtxtLienholder2Cntry.setName("txtLienholder2Cntry");
				ivjtxtLienholder2Cntry.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder2Cntry.setBounds(123, 3, 52, 20);
				// user code begin {1}				
				ivjtxtLienholder2Cntry.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtLienholder2Cntry.setMaxLength(
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
		return ivjtxtLienholder2Cntry;
	}

	/**
	 * Return the txtLienholder2CntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2CntryZpcd()
	{
		if (ivjtxtLienholder2CntryZpcd == null)
		{
			try
			{
				ivjtxtLienholder2CntryZpcd = new RTSInputField();
				ivjtxtLienholder2CntryZpcd.setBounds(188, 3, 74, 20);
				ivjtxtLienholder2CntryZpcd.setName(
					"txtLienholder2CntryZpcd");
				ivjtxtLienholder2CntryZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}						
				ivjtxtLienholder2CntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtLienholder2CntryZpcd.setMaxLength(
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
		return ivjtxtLienholder2CntryZpcd;
	}

	/**
	 * Return the txtLienholder2Id property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2Id()
	{
		if (ivjtxtLienholder2Id == null)
		{
			try
			{
				ivjtxtLienholder2Id = new RTSInputField();
				ivjtxtLienholder2Id.setName("txtLienholder2Id");
				ivjtxtLienholder2Id.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				// Added code so that the backward and forward
				// FocusTraversalKeys would not pass up KeyPressed.  We
				// will have to handle where the focus should go there.
				ivjtxtLienholder2Id.setFocusTraversalKeys(
					KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtLienholder2Id.setFocusTraversalKeys(
					KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtLienholder2Id.addKeyListener(this);
				ivjtxtLienholder2Id.addFocusListener(this);
				ivjtxtLienholder2Id.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholder2Id.setMaxLength(
					LocalOptionConstant.LENGTH_LIENHOLDER_ID);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLienholder2Id;
	}

	/**
	 * Return the txtLienholder2Name1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2Name1()
	{
		if (ivjtxtLienholder2Name1 == null)
		{
			try
			{
				ivjtxtLienholder2Name1 = new RTSInputField();
				ivjtxtLienholder2Name1.setName("txtLienholder2Name1");
				ivjtxtLienholder2Name1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}				
				ivjtxtLienholder2Name1.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder2Name1.setMaxLength(
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
		return ivjtxtLienholder2Name1;
	}

	/**
	 * Return the txtLienholder2Name2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2Name2()
	{
		if (ivjtxtLienholder2Name2 == null)
		{
			try
			{
				ivjtxtLienholder2Name2 = new RTSInputField();
				ivjtxtLienholder2Name2.setName("txtLienholder2Name2");
				ivjtxtLienholder2Name2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}						
				ivjtxtLienholder2Name2.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder2Name2.setMaxLength(
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
		return ivjtxtLienholder2Name2;
	}

	/**
	 * Return the txtLienholder2State property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2State()
	{
		if (ivjtxtLienholder2State == null)
		{
			try
			{
				ivjtxtLienholder2State = new RTSInputField();
				ivjtxtLienholder2State.setName("txtLienholder2State");
				ivjtxtLienholder2State.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder2State.setBounds(123, 3, 29, 20);
				// user code begin {1}				
				ivjtxtLienholder2State.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtLienholder2State.setMaxLength(
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
		return ivjtxtLienholder2State;
	}

	/**
	 * Return the txtLienholder2Street1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2Street1()
	{
		if (ivjtxtLienholder2Street1 == null)
		{
			try
			{
				ivjtxtLienholder2Street1 = new RTSInputField();
				ivjtxtLienholder2Street1.setName(
					"txtLienholder2Street1");
				ivjtxtLienholder2Street1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}					
				ivjtxtLienholder2Street1.setInput(
					RTSInputField.DEFAULT);
				ivjtxtLienholder2Street1.setMaxLength(
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
		return ivjtxtLienholder2Street1;
	}

	/**
	 * Return the txtLienholder2Street2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2Street2()
	{
		if (ivjtxtLienholder2Street2 == null)
		{
			try
			{
				ivjtxtLienholder2Street2 = new RTSInputField();
				ivjtxtLienholder2Street2.setName(
					"txtLienholder2Street2");
				ivjtxtLienholder2Street2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}				
				ivjtxtLienholder2Street2.setInput(
					RTSInputField.DEFAULT);
				ivjtxtLienholder2Street2.setMaxLength(
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
		return ivjtxtLienholder2Street2;
	}

	/**
	 * Return the txtLienholder2Zpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2Zpcd()
	{
		if (ivjtxtLienholder2Zpcd == null)
		{
			try
			{
				ivjtxtLienholder2Zpcd = new RTSInputField();
				ivjtxtLienholder2Zpcd.setName("txtLienholder2Zpcd");
				ivjtxtLienholder2Zpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder2Zpcd.setBounds(157, 3, 51, 20);
				// user code begin {1}
				ivjtxtLienholder2Zpcd.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholder2Zpcd.setMaxLength(
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

		return ivjtxtLienholder2Zpcd;
	}

	/**
	 * Return the txtLienholder2ZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder2ZpcdP4()
	{
		if (ivjtxtLienholder2ZpcdP4 == null)
		{
			try
			{
				ivjtxtLienholder2ZpcdP4 = new RTSInputField();
				ivjtxtLienholder2ZpcdP4.setName("txtLienholder2ZpcdP4");
				ivjtxtLienholder2ZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder2ZpcdP4.setBounds(231, 3, 37, 20);
				// user code begin {1}
				ivjtxtLienholder2ZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholder2ZpcdP4.setMaxLength(
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
		return ivjtxtLienholder2ZpcdP4;
	}

	/**
	 * Return the txtLienholder3City property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3City()
	{
		if (ivjtxtLienholder3City == null)
		{
			try
			{
				ivjtxtLienholder3City = new RTSInputField();
				ivjtxtLienholder3City.setBounds(3, 3, 116, 20);
				ivjtxtLienholder3City.setName("txtLienholder3City");
				ivjtxtLienholder3City.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}					
				ivjtxtLienholder3City.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder3City.setMaxLength(
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
		return ivjtxtLienholder3City;
	}

	/**
	 * Return the txtLienholder3Cntry property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3Cntry()
	{
		if (ivjtxtLienholder3Cntry == null)
		{
			try
			{
				ivjtxtLienholder3Cntry = new RTSInputField();
				ivjtxtLienholder3Cntry.setBounds(123, 3, 52, 20);
				ivjtxtLienholder3Cntry.setName("txtLienholder3Cntry");
				ivjtxtLienholder3Cntry.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}						
				ivjtxtLienholder3Cntry.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtLienholder3Cntry.setMaxLength(
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
		return ivjtxtLienholder3Cntry;
	}

	/**
	 * Return the txtLienholder3CntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3CntryZpcd()
	{
		if (ivjtxtLienholder3CntryZpcd == null)
		{
			try
			{
				ivjtxtLienholder3CntryZpcd = new RTSInputField();
				ivjtxtLienholder3CntryZpcd.setBounds(184, 3, 74, 20);
				ivjtxtLienholder3CntryZpcd.setName(
					"txtLienholder3CntryZpcd");
				ivjtxtLienholder3CntryZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtLienholder3CntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtLienholder3CntryZpcd.setMaxLength(
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
		return ivjtxtLienholder3CntryZpcd;
	}

	/**
	 * Return the txtLienholder3Id property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3Id()
	{
		if (ivjtxtLienholder3Id == null)
		{
			try
			{
				ivjtxtLienholder3Id = new RTSInputField();
				ivjtxtLienholder3Id.setName("txtLienholder3Id");
				ivjtxtLienholder3Id.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				// Added code so that the backward and forward
				// FocusTraversalKeys would not pass up KeyPressed.  We
				// will have to handle where the focus should go there.
				ivjtxtLienholder3Id.setFocusTraversalKeys(
					KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtLienholder3Id.setFocusTraversalKeys(
					KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtLienholder3Id.addKeyListener(this);
				ivjtxtLienholder3Id.addFocusListener(this);
				ivjtxtLienholder3Id.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholder3Id.setMaxLength(
					LocalOptionConstant.LENGTH_LIENHOLDER_ID);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLienholder3Id;
	}

	/**
	 * Return the txtLienholder3Name1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3Name1()
	{
		if (ivjtxtLienholder3Name1 == null)
		{
			try
			{
				ivjtxtLienholder3Name1 = new RTSInputField();
				ivjtxtLienholder3Name1.setName("txtLienholder3Name1");
				ivjtxtLienholder3Name1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}	
				// defect 10299					
				//ivjtxtLienholder3Name1.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder3Name1.setInput(RTSInputField.DEFAULT);
				// end defect 10299
				ivjtxtLienholder3Name1.setMaxLength(
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
		return ivjtxtLienholder3Name1;
	}

	/**
	 * Return the txtLienholder3Name2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3Name2()
	{
		if (ivjtxtLienholder3Name2 == null)
		{
			try
			{
				ivjtxtLienholder3Name2 = new RTSInputField();
				ivjtxtLienholder3Name2.setName("txtLienholder3Name2");
				ivjtxtLienholder3Name2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}				
				ivjtxtLienholder3Name2.setInput(RTSInputField.DEFAULT);
				ivjtxtLienholder3Name2.setMaxLength(
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
		return ivjtxtLienholder3Name2;
	}

	/**
	 * Return the txtLienholder3State property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3State()
	{
		if (ivjtxtLienholder3State == null)
		{
			try
			{
				ivjtxtLienholder3State = new RTSInputField();
				ivjtxtLienholder3State.setName("txtLienholder3State");
				ivjtxtLienholder3State.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder3State.setBounds(124, 3, 29, 20);
				// user code begin {1}				
				ivjtxtLienholder3State.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtLienholder3State.setMaxLength(
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
		return ivjtxtLienholder3State;
	}

	/**
	 * Return the txtLienholder3Street1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3Street1()
	{
		if (ivjtxtLienholder3Street1 == null)
		{
			try
			{
				ivjtxtLienholder3Street1 = new RTSInputField();
				ivjtxtLienholder3Street1.setName(
					"txtLienholder3Street1");
				ivjtxtLienholder3Street1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}						
				ivjtxtLienholder3Street1.setInput(
					RTSInputField.DEFAULT);
				ivjtxtLienholder3Street1.setMaxLength(
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
		return ivjtxtLienholder3Street1;
	}

	/**
	 * Return the txtLienholder3Street2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3Street2()
	{
		if (ivjtxtLienholder3Street2 == null)
		{
			try
			{
				ivjtxtLienholder3Street2 = new RTSInputField();
				ivjtxtLienholder3Street2.setName(
					"txtLienholder3Street2");
				ivjtxtLienholder3Street2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}						
				ivjtxtLienholder3Street2.setInput(
					RTSInputField.DEFAULT);
				ivjtxtLienholder3Street2.setMaxLength(
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
		return ivjtxtLienholder3Street2;
	}

	/**
	 * Return the txtLienholder3Zpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3Zpcd()
	{
		if (ivjtxtLienholder3Zpcd == null)
		{
			try
			{
				ivjtxtLienholder3Zpcd = new RTSInputField();
				ivjtxtLienholder3Zpcd.setName("txtLienholder3Zpcd");
				ivjtxtLienholder3Zpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder3Zpcd.setBounds(159, 3, 51, 20);
				// user code begin {1}				
				ivjtxtLienholder3Zpcd.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholder3Zpcd.setMaxLength(
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
		return ivjtxtLienholder3Zpcd;
	}

	/**
	 * Return the txtLienholder3ZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLienholder3ZpcdP4()
	{
		if (ivjtxtLienholder3ZpcdP4 == null)
		{
			try
			{
				ivjtxtLienholder3ZpcdP4 = new RTSInputField();

				ivjtxtLienholder3ZpcdP4.setName("txtLienholder3ZpcdP4");
				ivjtxtLienholder3ZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLienholder3ZpcdP4.setBounds(232, 3, 37, 20);
				// user code begin {1}				
				ivjtxtLienholder3ZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtLienholder3ZpcdP4.setMaxLength(
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
		return ivjtxtLienholder3ZpcdP4;
	}

	/**
	 * 
	 * handle Combo Selection 
	 * 
	 * @param aaCombo
	 */
	private void handleComboSelection(JComboBox aaCombo)
	{
		Vector lvVector =
			(aaCombo == getcomboLienholder1()
				&& getradioETitle().isSelected())
				? svCurrentETtlCertLienhldr
				: svCurrentCertLienhldr;

		CertifiedLienholderData laData = null;
		if (aaCombo == getcomboLienholder1())
		{
			int aiSelection = getcomboLienholder1().getSelectedIndex();
			laData =
				(CertifiedLienholderData) lvVector.elementAt(
					aiSelection);
			csPermLienhldrId1 = laData.getPermLienHldrId();
			setupLienHldr1Data(laData.getLienholderData());
		}
		else if (aaCombo == getcomboLienholder2())
		{
			int aiSelection = getcomboLienholder2().getSelectedIndex();
			laData =
				(CertifiedLienholderData) lvVector.elementAt(
					aiSelection);
			csPermLienhldrId2 = laData.getPermLienHldrId();
			setupLienHldr2Data(laData.getLienholderData());
		}
		else if (aaCombo == getcomboLienholder3())
		{
			int aiSelection = getcomboLienholder3().getSelectedIndex();
			laData =
				(CertifiedLienholderData) lvVector.elementAt(
					aiSelection);
			csPermLienhldrId3 = laData.getPermLienHldrId();
			setupLienHldr3Data(laData.getLienholderData());
		}
		// defect 10592 
		setupDeleteButtons();
		// end defect 10592
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
	 * Handle Radio Button Selection
	 * 
	 * @param aaRadio
	 */
	private void handleRadioSelection(JRadioButton aaRadio)
	{
		if (aaRadio.isSelected())
		{
			removeListeners();

			if (aaRadio == getradioLocal1())
			{
				getstcLblFirstLienId().setEnabled(true);
				gettxtLienholder1Id().setEnabled(true);
				gettxtLienholder1Id().requestFocus();
				getcomboLienholder1().setSelectedIndex(-1);
				getcomboLienholder1().setEnabled(false);
				setupLienHldr1Data(new LienholderData());
				cbETtl = false;
				csPermLienhldrId1 = EMPTY;
			}
			else if (
				aaRadio == getradioCertified1()
					|| aaRadio == getradioETitle())
			{
				csLien1Id = CommonConstant.STR_SPACE_EMPTY;
				gettxtLienholder1Id().setEnabled(false);
				gettxtLienholder1Id().setText(EMPTY);
				getcomboLienholder1().setEnabled(true);
				getcomboLienholder1().requestFocus();
				getstcLblFirstLienId().setEnabled(false);

				if (getradioCertified1().isSelected())
				{
					populateCertifiedCombo(false);
					cbETtl = false;
				}
				else if (getradioETitle().isSelected())
				{
					cbETtl = true;
					populateETtlCombo();
				}
			}

			else if (aaRadio == getradioLocal2())
			{
				getstcLblSecondLienId().setEnabled(true);
				gettxtLienholder2Id().setEnabled(true);
				gettxtLienholder2Id().requestFocus();
				getcomboLienholder2().setSelectedIndex(-1);
				getcomboLienholder2().setEnabled(false);
				setupLienHldr2Data(new LienholderData());
				csPermLienhldrId2 = EMPTY;
			}
			else if (aaRadio == getradioCertified2())
			{
				csLien2Id = CommonConstant.STR_SPACE_EMPTY;
				getstcLblSecondLienId().setEnabled(false);
				gettxtLienholder2Id().setEnabled(false);
				gettxtLienholder2Id().setText(EMPTY);
				getcomboLienholder2().setEnabled(true);
				getcomboLienholder2().requestFocus();
				setupLienHldr2Data(new LienholderData());
			}

			else if (aaRadio == getradioLocal3())
			{
				getstcLblThirdLienId().setEnabled(true);
				gettxtLienholder3Id().setEnabled(true);
				gettxtLienholder3Id().requestFocus();
				getcomboLienholder3().setSelectedIndex(-1);
				getcomboLienholder3().setEnabled(false);
				setupLienHldr3Data(new LienholderData());
				csPermLienhldrId3 = EMPTY;
			}
			else if (aaRadio == getradioCertified3())
			{
				csLien3Id = CommonConstant.STR_SPACE_EMPTY;
				getstcLblThirdLienId().setEnabled(false);
				gettxtLienholder3Id().setEnabled(false);
				gettxtLienholder3Id().setText(EMPTY);
				getcomboLienholder3().setEnabled(true);
				getcomboLienholder3().requestFocus();
				setupLienHldr3Data(new LienholderData());
			}
			getchkAdditionalLien().setEnabled(!cbETtl);

			addListeners();

			if (cbETtl)
			{
				getchkAdditionalLien().setSelected(false);
				getradioLocal2().setSelected(true);
				getradioLocal3().setSelected(true);
			}
			// defect 10592 
			setupDeleteButtons();
			// end defect 10592
		}
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// focus manager necessary to make form follow tab tag order
			// user code end
			setName(ScreenConstant.TTL035_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(597, 527);
			setTitle(ScreenConstant.TTL035_FRAME_TITLE);
			setContentPane(getFrmLienEntryTTL035ContentPane1());
			// defect 10049 
			setRequestFocus(false);
			// end defect 10049 
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// defect 10127 
		caLien1NameAddrComp =
			new NameAddressComponent(
				gettxtLienholder1Name1(),
				gettxtLienholder1Name2(),
				gettxtLienholder1Street1(),
				gettxtLienholder1Street2(),
				gettxtLienholder1City(),
				gettxtLienholder1State(),
				gettxtLienholder1Zpcd(),
				gettxtLienholder1ZpcdP4(),
				gettxtLienholder1Cntry(),
				gettxtLienholder1CntryZpcd(),
				getchkUSA1(),
				getstcLblDash1(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA,
				ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA,
				CommonConstant.TX_NOT_DEFAULT_STATE);
		caLien2NameAddrComp =
			new NameAddressComponent(
				gettxtLienholder2Name1(),
				gettxtLienholder2Name2(),
				gettxtLienholder2Street1(),
				gettxtLienholder2Street2(),
				gettxtLienholder2City(),
				gettxtLienholder2State(),
				gettxtLienholder2Zpcd(),
				gettxtLienholder2ZpcdP4(),
				gettxtLienholder2Cntry(),
				gettxtLienholder2CntryZpcd(),
				getchkUSA2(),
				getstcLblDash2(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA,
				ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA,
				CommonConstant.TX_NOT_DEFAULT_STATE);
		caLien3NameAddrComp =
			new NameAddressComponent(
				gettxtLienholder3Name1(),
				gettxtLienholder3Name2(),
				gettxtLienholder3Street1(),
				gettxtLienholder3Street2(),
				gettxtLienholder3City(),
				gettxtLienholder3State(),
				gettxtLienholder3Zpcd(),
				gettxtLienholder3ZpcdP4(),
				gettxtLienholder3Cntry(),
				gettxtLienholder3CntryZpcd(),
				getchkUSA3(),
				getstcLblDash3(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA,
				ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA,
				CommonConstant.TX_NOT_DEFAULT_STATE);
		// end defect 10127 		
		// user code begin {2}
		getRootPane().setDefaultButton(getButtonPanel().getBtnEnter());
		// user code end
	}

	/**
	 * 
	 * Is valid Lienholder Date
	 * 
	 * @return boolean
	 */
	private boolean isValidLienDate(int aiDate)
	{
		int liMaxDate =
			new RTSDate().add(RTSDate.MONTH, 24).getYYYYMMDDDate();

		// defect 10229 
		// System.out.println(
		// 	new RTSDate(RTSDate.YYYYMMDD, liMaxDate).toString());
		// end defect 10229 

		return aiDate != 0 && aiDate > 19000101 && aiDate < liMaxDate;
	}

	/**
	 * Is VTR Authorization Required
	 * 
	 * @return boolean
	 */
	private boolean isVTRAuthReqdForSSNInName()
	{
		boolean lbAuthReqd = false;

		String lsAuth = caVehInqData.getVehMiscData().getAuthCd();

		if (lsAuth == null || lsAuth.length() < 1)
		{
			lbAuthReqd =
				CommonValidations.isStringWithSSN(
					gettxtLienholder1Name1().getText())
					|| CommonValidations.isStringWithSSN(
						gettxtLienholder1Name2().getText())
					|| CommonValidations.isStringWithSSN(
						gettxtLienholder2Name1().getText())
					|| CommonValidations.isStringWithSSN(
						gettxtLienholder2Name2().getText())
					|| CommonValidations.isStringWithSSN(
						gettxtLienholder3Name1().getText())
					|| CommonValidations.isStringWithSSN(
						gettxtLienholder3Name2().getText());
		}
		return lbAuthReqd;
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aeIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aeIE)
	{
		boolean lbUSA = aeIE.getStateChange() == ItemEvent.SELECTED;

		clearAllColor(this);

		// defect 10127
		if (aeIE.getSource() == getchkUSA1())
		{
			caLien1NameAddrComp.resetPerUSA(lbUSA);
		}
		else if (aeIE.getSource() == getchkUSA2())
		{
			caLien2NameAddrComp.resetPerUSA(lbUSA);
		}
		else if (aeIE.getSource() == getchkUSA3())
		{
			caLien3NameAddrComp.resetPerUSA(lbUSA);
		}
		// end defect 10127

		// defect 9974 
		else if (aeIE.getSource() instanceof JRadioButton)
		{
			handleRadioSelection((JRadioButton) aeIE.getSource());
		}
		// end defect 9974 
	}

	/**
	 * <ul>
	 * 	<li>Perform Lienholder Id navigation using tab and<li> 
	 *  <li>isShiftDown()</li>
	 * 	<li>Set focus to appropriate components</li>
	 * 	<li></li>
	 * </ul>
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		// defect 7885
		if (aaKE.getSource().equals(gettxtLienholder1Id()))
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_TAB)
			{
				clearAllColor(this);
				getLienHldr1Data();
				ivjtxtLien1Date.requestFocus(true);
				if (aaKE.isShiftDown())
				{
					ivjButtonPanel.getBtnHelp().requestFocus(true);
				}
			}
		}
		if (aaKE.getSource().equals(gettxtLienholder2Id()))
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_TAB)
			{
				clearAllColor(this);
				getLienHldr2Data();
				ivjtxtLien2Date.requestFocus(true);
				if (aaKE.isShiftDown())
				{
					if (getchkUSA1().isSelected())
					{
						ivjtxtLienholder1ZpcdP4.requestFocus(true);
					}
					else
					{
						ivjtxtLienholder1CntryZpcd.requestFocus(true);
					}
				}
			}
		}
		if (aaKE.getSource().equals(gettxtLienholder3Id()))
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_TAB)
			{
				clearAllColor(this);
				getLienHldr3Data();
				ivjtxtLien3Date.requestFocus(true);
				if (aaKE.isShiftDown())
				{
					if (getchkUSA2().isSelected())
					{
						ivjtxtLienholder2ZpcdP4.requestFocus(true);
					}
					else
					{
						ivjtxtLienholder2CntryZpcd.requestFocus(true);
					}
				}
			}
		}
		// end defect 7885
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
			VehicleInquiryData laNewVehInqData =
				(VehicleInquiryData) UtilityMethods.copy(caVehInqData);

			setDataToVehObj(laNewVehInqData);

			getController().processData(
				AbstractViewController.CANCEL,
				null);

		}
		else
		{
			setupDeleteButtons();
		}
	}

	/**
	 * Populate Certified Combo 
	 */
	private void populateCertifiedCombo(boolean abAll)
	{
		int liSelection = -1;
		boolean lbLocateRecord =
			!UtilityMethods.isEmpty(csPermLienhldrId1);

		if (!abAll)
		{
			getcomboLienholder1().removeActionListener(this);
			getcomboLienholder1().removeAllItems();
		}

		for (int i = 0; i < svCurrentCertLienhldr.size(); i++)
		{
			CertifiedLienholderData laData =
				(
					CertifiedLienholderData) svCurrentCertLienhldr
						.elementAt(
					i);
			String lsData =
				laData.getName1() + "    " + laData.getPermLienHldrId();
			getcomboLienholder1().addItem(lsData);

			// Could maintain selection if previously ETitle  
			if (lbLocateRecord
				&& laData.getPermLienHldrId().equals(csPermLienhldrId1))
			{
				liSelection = i;
			}
			if (abAll)
			{
				getcomboLienholder2().addItem(lsData);
				getcomboLienholder3().addItem(lsData);
			}
		}
		getcomboLienholder1().setSelectedIndex(liSelection);

		if (liSelection >= 0)
		{
			handleComboSelection(getcomboLienholder1());
		}
		else
		{
			setupLienHldr1Data(new LienholderData());
			csPermLienhldrId1 = EMPTY;
		}

		if (abAll)
		{
			getcomboLienholder2().setSelectedIndex(-1);
			getcomboLienholder3().setSelectedIndex(-1);
		}
	}

	/**
	 *  Populate ETitle Combo Box 
	 */
	private void populateETtlCombo()
	{
		getcomboLienholder1().removeActionListener(this);
		getcomboLienholder1().removeAllItems();
		int liSelection = -1;
		boolean lbLocateRecord =
			!UtilityMethods.isEmpty(csPermLienhldrId1);

		for (int i = 0; i < svCurrentETtlCertLienhldr.size(); i++)
		{
			CertifiedLienholderData laData =
				(
					CertifiedLienholderData) svCurrentETtlCertLienhldr
						.elementAt(
					i);

			// defect 10112 
			String lsData =
				laData.getName1() + "    " + laData.getPermLienHldrId();
			// end defect 10112 

			getcomboLienholder1().addItem(lsData);

			// Could maintain selection if previously Certified   			
			if (lbLocateRecord
				&& laData.getPermLienHldrId().equals(csPermLienhldrId1))
			{
				liSelection = i;
			}
		}
		getcomboLienholder1().setSelectedIndex(liSelection);
		if (liSelection >= 0)
		{
			handleComboSelection(getcomboLienholder1());
		}
		else
		{
			setupLienHldr1Data(new LienholderData());
			csPermLienhldrId1 = EMPTY;
		}
	}

	/**
	 * Remove Listeners
	 */
	private void removeListeners()
	{
		getradioLocal1().removeItemListener(this);
		getradioLocal2().removeItemListener(this);
		getradioLocal3().removeItemListener(this);
		ivjradioETitle.removeItemListener(this);
		ivjradioCertified1.removeItemListener(this);
		ivjradioCertified2.removeItemListener(this);
		ivjradioCertified3.removeItemListener(this);
		ivjcomboLienholder1.removeActionListener(this);
		ivjcomboLienholder2.removeActionListener(this);
		ivjcomboLienholder3.removeActionListener(this);
	}

	/**
	 * Reset Screen Fields for Lien1 
	 */
	private void resetLien1()
	{
		gettxtLienholder1Id().setText(EMPTY);
		gettxtLien1Date().setText(EMPTY);
		getradioLocal1().setSelected(true);
		setupLienHldr1Data(new LienholderData());
		getcomboLienholder1().setSelectedIndex(-1);
		csPermLienhldrId1 = EMPTY;
		csLien1Id = EMPTY;
	}

	/**
	 * Reset Screen Fields for Lien2 
	 */
	private void resetLien2()
	{
		gettxtLienholder2Id().setText(EMPTY);
		gettxtLien2Date().setText(EMPTY);
		getradioLocal2().setSelected(true);
		setupLienHldr2Data(new LienholderData());
		getcomboLienholder2().setSelectedIndex(-1);
		csPermLienhldrId2 = EMPTY;
		csLien2Id = EMPTY;
	}

	/**
	 * Reset Screen Fields for Lien3 
	 */
	private void resetLien3()
	{
		gettxtLienholder3Id().setText(EMPTY);
		gettxtLien3Date().setText(EMPTY);
		setupLienHldr3Data(new LienholderData());
		getradioLocal3().setSelected(true);
		getcomboLienholder3().setSelectedIndex(-1);
		csPermLienhldrId3 = EMPTY;
		csLien3Id = EMPTY;
	}

	/**
	 * Restore Combo Color
	 */
	private void restoreComboColor()
	{
		getcomboLienholder1().setForeground(Color.black);
		getcomboLienholder1().setBackground(new Color(204, 204, 204));

		getcomboLienholder2().setForeground(Color.black);
		getcomboLienholder2().setBackground(new Color(204, 204, 204));

		getcomboLienholder3().setForeground(Color.black);
		getcomboLienholder3().setBackground(new Color(204, 204, 204));
	}

	/**
	 * Retrieve Lienholder Data 
	 */
	private void retrieveLienholderData()
	{
		TitleValidObj laValidObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		LienholderData laLienData = new LienholderData();

		int liLienholderId = 0;

		switch (ciFlag)
		{
			case (1) :
				{
					csLien1Id = gettxtLienholder1Id().getText();
					liLienholderId = Integer.parseInt(csLien1Id);
					csLien1Id = "" + liLienholderId;
					break;
				}
			case (2) :
				{
					csLien2Id = gettxtLienholder2Id().getText();
					liLienholderId = Integer.parseInt(csLien2Id);
					csLien2Id = "" + liLienholderId;
					break;
				}
			case (3) :
				{
					csLien3Id = gettxtLienholder3Id().getText();
					liLienholderId = Integer.parseInt(csLien3Id);
					csLien3Id = "" + liLienholderId;
					break;
				}

		}
		if (liLienholderId > 0)
		{
			laLienData.setId(liLienholderId);
			laLienData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laLienData.setSubstaId(SystemProperty.getSubStationId());
			laValidObj.setLienData(laLienData);
			getController().processData(
				VCLienEntryTTL035.LIENHLDR_DATA,
				caVehInqData);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the getController( relays information
	 * to the view
	 *
	 * This method is call when enterring the frame and everytime one 
	 * tab off the LienHlr Id fields. Flag is set in method focusLost()
	 *
	 *  Flag = 0	=> Initialize and reset
	 * 	Flag = 1	=> Lien data 1
	 * 	Flag = 2	=> Lien data 2
	 * 	Flag = 3	=> Lien data 3
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{

		if (aaDataObject != null)
		{
			VehicleInquiryData laVehData =
				(VehicleInquiryData) UtilityMethods.copy(aaDataObject);

			caVehInqData = laVehData;

			TitleValidObj laValidObj =
				(TitleValidObj) laVehData.getValidationObject();

			// defect 9974 
			cbETtlAllowd =
				DocumentTypesCache.isETtlAllowd(
					laVehData
						.getMfVehicleData()
						.getTitleData()
						.getDocTypeCd());

			if (laValidObj != null)
			{
				LienholderData laLienData =
					(LienholderData) laValidObj.getLienData();

				if (laLienData != null)
				{
					if (ciFlag == 1)
					{
						setupLienHldr1Data(laLienData);
					}
					else if (ciFlag == 2)
					{
						setupLienHldr2Data(laLienData);
					}
					else if (ciFlag == 3)
					{
						setupLienHldr3Data(laLienData);
					}
				}
				// LienData != null 
				if (ciFlag == 0)
				{
					populateCertifiedCombo(true);

					String lsTransCd = getController().getTransCode();

					// defect 10592 
					Object laTTL035 =
						getController().getMediator().openVault(
							ScreenConstant.TTL035);

					if (laTTL035 != null
						&& laTTL035 instanceof ScreenTTL035SavedData)
					{
						ScreenTTL035SavedData laTTL035Data =
							(ScreenTTL035SavedData) laTTL035;

						setSavedDataToDisplay(laTTL035Data);
					}
					// end defect 10592 
					else if (
						lsTransCd.equals(TransCdConstant.DTAORD)
							|| lsTransCd.equals(TransCdConstant.DTANTD))
					{
						setupForDTADiskette();
					}
					else
					{
						initLienDates(
							caVehInqData
								.getMfVehicleData()
								.getTitleData());

						try
						{
							setLienData(
								caVehInqData
									.getMfVehicleData()
									.getTitleData());
						}
						catch (RTSException aeRTSEx)
						{
							aeRTSEx.displayError(this);
						}
					}

					addListeners();

					if (getradioLocal1().isSelected())
					{
						setDefaultFocusField(gettxtLienholder1Id());
					}
				}
				// end defect 9974 
			}

		}

		// defect 10592
		setupDeleteButtons();
		// end defect 10592

		ciFlag = 0;
	}

	/**
	 * Set data to Vehicle Inquiry Object.
	 * 
	 * @param aaVehInqData
	 */
	private void setDataToVehObj(VehicleInquiryData aaVehInqData)
	{
		// defect 9969 
		// Use Lienholder Address Object 
		TitleData laTitleData =
			aaVehInqData.getMfVehicleData().getTitleData();

		// defect 10112 
		// Implement new Lienholder Data 
		LienholderData laLienHldr1 =
			laTitleData.getLienholderData(TitleConstant.LIENHLDR1);
		LienholderData laLienHldr2 =
			laTitleData.getLienholderData(TitleConstant.LIENHLDR2);
		LienholderData laLienHldr3 =
			laTitleData.getLienholderData(TitleConstant.LIENHLDR3);

		int liDate = 0;

		// LIEN 1 
		if (laLienHldr1 != null)
		{
			if (gettxtLien1Date().isValidDate())
			{
				liDate = gettxtLien1Date().getDate().getYYYYMMDDDate();
			}
			laLienHldr1.setLienDate(liDate);
			caLien1NameAddrComp.setNameAddressToDataObject(laLienHldr1);
		}

		// LIEN 2 
		if (laLienHldr2 != null)
		{
			liDate = 0;
			if (gettxtLien2Date().isValidDate())
			{
				liDate = gettxtLien2Date().getDate().getYYYYMMDDDate();
			}
			laLienHldr2.setLienDate(liDate);
			caLien2NameAddrComp.setNameAddressToDataObject(laLienHldr2);
		}

		// LIEN 3 		
		if (laLienHldr3 != null)
		{
			liDate = 0;

			// Set Third Lien
			if (gettxtLien3Date().isValidDate())
			{
				liDate = gettxtLien3Date().getDate().getYYYYMMDDDate();
			}
			laLienHldr3.setLienDate(liDate);
			caLien3NameAddrComp.setNameAddressToDataObject(laLienHldr3);
		}
		// end defect 10127

		if (getchkAdditionalLien().isSelected())
		{
			laTitleData.setAddlLienRecrdIndi(1);
		}
		else
		{
			laTitleData.setAddlLienRecrdIndi(0);
		}

		// defect 9974 
		int liETtlCd =
			(getradioETitle().isSelected() && laTitleData.hasLien())
				? TitleConstant.ELECTRONIC_ETTLCD
				: DocumentTypesCache.getDefaultETtlCd(
					laTitleData.getDocTypeCd());

		laTitleData.setETtlCd(liETtlCd);

		laTitleData.setPermLienHldrId1(csPermLienhldrId1);
		laTitleData.setPermLienHldrId2(csPermLienhldrId2);
		laTitleData.setPermLienHldrId3(csPermLienhldrId3);

		// end defect 9974

		// defect 10592 
		ScreenTTL035SavedData laTTL035Data =
			new ScreenTTL035SavedData();

		laTTL035Data.setTitleData(
			(TitleData) UtilityMethods.copy(laTitleData));

		getController().getMediator().closeVault(
			ScreenConstant.TTL035,
			UtilityMethods.copy(laTTL035Data));
		// end defect 10592 

	}

	/**
	 * Assign Saved Data To Display
	 * 
	 * @param aaTTL035Data
	 */
	private void setSavedDataToDisplay(ScreenTTL035SavedData aaTTL035Data)
	{
		TitleData laTitleData = aaTTL035Data.getTitleData();
		try
		{
			initLienDates(laTitleData);
			setLienData(laTitleData);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
	}

	/** 
	 * Initialize Lien Dates 
	 * 
	 * @param aaTitleData
	 */
	private void initLienDates(TitleData aaTitleData)
	{
		// LIEN 1 
		int liDate =
			aaTitleData
				.getLienholderData(TitleConstant.LIENHLDR1)
				.getLienDate();
		if (isValidLienDate(liDate))
		{
			gettxtLien1Date().setDate(
				new RTSDate(RTSDate.YYYYMMDD, liDate));
		}

		// LIEN 2 
		liDate =
			aaTitleData
				.getLienholderData(TitleConstant.LIENHLDR2)
				.getLienDate();
		if (isValidLienDate(liDate))
		{
			gettxtLien2Date().setDate(
				new RTSDate(RTSDate.YYYYMMDD, liDate));
		}
		// LIEN 3
		liDate =
			aaTitleData
				.getLienholderData(TitleConstant.LIENHLDR3)
				.getLienDate();
		if (isValidLienDate(liDate))
		{
			gettxtLien3Date().setDate(
				new RTSDate(RTSDate.YYYYMMDD, liDate));
		}
	}

	/**
	 * Set Lienholder to frame.
	 * 
	 * @param aaTitleData
	 * @throws RTSException 
	 */
	private void setLienData(TitleData aaTitleData) throws RTSException
	{
		assignCertfdLienholderData(aaTitleData);

		// defect 10112
		setupLienHldr1Data(
			aaTitleData.getLienholderData(TitleConstant.LIENHLDR1));

		if (cbETtl)
		{
			// defect 10112 
			if (aaTitleData
				.getLienholderData(TitleConstant.LIENHLDR2)
				.isPopulated()
				|| aaTitleData
					.getLienholderData(TitleConstant.LIENHLDR3)
					.isPopulated())
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_ETTL_MAX_ONE_LIEN);
			}
		}
		else
		{
			setupLienHldr2Data(
				aaTitleData.getLienholderData(TitleConstant.LIENHLDR2));
			setupLienHldr3Data(
				aaTitleData.getLienholderData(TitleConstant.LIENHLDR3));
			// end defect 10112
		}
		getchkAdditionalLien().setSelected(
			aaTitleData.getAddlLienRecrdIndi() == 1);
	}
	/**
	 * Enable/Disabled Delete Buttons per data
	 *
	 */
	private void setupDeleteButtons()
	{
		getbtnDelete1stLien().setEnabled(
			!caLien1NameAddrComp.isNameAddressEmpty()
				|| !gettxtLienholder1Id().isEmpty()
				|| !gettxtLien1Date().isDateEmpty());

		getbtnDelete2ndLien().setEnabled(
			!caLien2NameAddrComp.isNameAddressEmpty()
				|| !gettxtLienholder2Id().isEmpty()
				|| !gettxtLien2Date().isDateEmpty());

		getbtnDelete3rdLien().setEnabled(
			!caLien3NameAddrComp.isNameAddressEmpty()
				|| !gettxtLienholder3Id().isEmpty()
				|| !gettxtLien3Date().isDateEmpty());
	}

	/**
	 * Apply Dealer Data to display fields.
	 * 
	 */
	private void setupForDTADiskette()
	{
		try
		{
			TitleValidObj laValidationObj =
				(TitleValidObj) caVehInqData.getValidationObject();

			// defect 10290
			// Refactored from laDlrTtlObj  
			DealerTitleData laDlrTtlData =
				(DealerTitleData) laValidationObj.getDlrTtlData();

			if (laDlrTtlData != null)
			{
				TitleData laTitleData =
					laDlrTtlData.getMFVehicleData().getTitleData();
				// end defect 10290 

				initLienDates(laTitleData);

				setLienData(laTitleData);

				getchkAdditionalLien().setSelected(
					laTitleData.getAddlLienRecrdIndi() == 1);

				Vector lvErrMsg = laDlrTtlData.getLienError();
				String lsChange =
					lvErrMsg.size() == 1 ? CHANGE : CHANGE + "S";
				if (lvErrMsg != null && lvErrMsg.size() > 0)
				{
					String lsErrMsg =
						ERROR_HDR + lsChange + ": </font>" + "<ul>";
					for (int i = 0; i < lvErrMsg.size(); i++)
					{
						lsErrMsg =
							lsErrMsg
								+ " <li>"
								+ (String) lvErrMsg.elementAt(i)
								+ "</li>";
					}
					lsErrMsg = lsErrMsg + "</ul>";

					RTSException leRTSEx =
						new RTSException(
							RTSException.FAILURE_MESSAGE,
							lsErrMsg,
							"ERROR",
							true);
					leRTSEx.setBeep(RTSException.BEEP);
					leRTSEx.displayError(this);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
		catch (NullPointerException aeNPE)
		{
			RTSException leRTSEx = new RTSException(EMPTY, aeNPE);
			leRTSEx.displayError(this);
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx = new RTSException(EMPTY, aeEx);
			leRTSEx.displayError(this);
		}
	}

	/**
	 * 
	 * Setup Lienholder1 Data
	 *
	 * @param aaLienData
	 */
	public void setupLienHldr1Data(LienholderData aaLienData)
	{
		// defect 10127 
		if (aaLienData != null)
		{
			ivjtxtLienholder1Id.removeFocusListener(this);
			caLien1NameAddrComp.setNameAddressDataToDisplay(aaLienData);
			ivjtxtLienholder1Id.addFocusListener(this);
		}
		// end defect 10127 

		enableLien1Data();

		if (getradioETitle().isSelected())
		{
			setupLienHldr2Data(new LienholderData());
			setupLienHldr3Data(new LienholderData());
			getcomboLienholder2().setSelectedIndex(-1);
			getcomboLienholder3().setSelectedIndex(-1);
			gettxtLienholder1Id().setText(EMPTY);
			gettxtLienholder2Id().setText(EMPTY);
			gettxtLienholder3Id().setText(EMPTY);
			gettxtLien2Date().setText(EMPTY);
			gettxtLien3Date().setText(EMPTY);
			gettxtLien2Date().setEnabled(false);
			gettxtLien3Date().setEnabled(false);
			getstcLblDate2().setEnabled(false);
			getstcLblDate3().setEnabled(false);
			csLien2Id = EMPTY;
			csLien3Id = EMPTY;
			enableLien2Data();
			enableLien3Data();
			getchkAdditionalLien().setSelected(false);
		}
		getchkAdditionalLien().setEnabled(
			!getradioETitle().isSelected());
	}

	/**
	 * Setup Lienholder2 Data
	 *
	 * @param aaLienData 
	 */
	public void setupLienHldr2Data(LienholderData aaLienData)
	{
		// defect 10127		
		if (aaLienData != null)
		{
			ivjtxtLienholder2Id.removeFocusListener(this);
			caLien2NameAddrComp.setNameAddressDataToDisplay(aaLienData);
			ivjtxtLienholder2Id.addFocusListener(this);
		}
		enableLien2Data();
		// end defect 10127 
	}

	/**
	 * Setup Lienholder3 Data
	 *
	 * @param aaLienData 
	 */
	public void setupLienHldr3Data(LienholderData aaLienData)
	{
		// defect 10127		
		if (aaLienData != null)
		{
			ivjtxtLienholder3Id.removeFocusListener(this);
			caLien3NameAddrComp.setNameAddressDataToDisplay(aaLienData);
			ivjtxtLienholder3Id.addFocusListener(this);
		}
		enableLien3Data();
		// end defect 10127 
	}

	/**
	 * Validate LienholderData 
	 * 
	 * @param aaDateField
	 * @param aaNameAddrComp
	 * @param asPermLienhldrId
	 * @param aeRTSEx RTSException
	 */
	private void validateLienData(
		RTSDateField aaDateField,
		NameAddressComponent aaNameAddrComp,
		String asPermLienhldrId,
		RTSException aeRTSEx)
	{
		if (!aaDateField.isDateEmpty()
			|| !aaNameAddrComp.isNameAddressEmpty())
		{
			if (!aaDateField.isValidDate())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
						aaDateField);
			}
			// defect 10504 
			else if (aaDateField.isFutureDate())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_MSG_DATE_CANNOT_BE_IN_FUTURE),
					aaDateField);
			}
			// end defect 10504

			// defect 10631 
			else if (!UtilityMethods.isEmpty(asPermLienhldrId))
			{
				CommonValidations.validateLienDate(
					aaDateField,
					asPermLienhldrId,
					aeRTSEx);
			}
			// end defect 10631 

			aaNameAddrComp.validateNameAddressFields(aeRTSEx);
		}
	}

	/**
	 * Verify Lien Data Population
	 *
	 * @param aeRTSEx 
	 */
	private void verifyLienDataPopulation(RTSException aeRTSEx)
	{
		String lsLienName1 = gettxtLienholder1Name1().getText().trim();
		String lsLienName2 = gettxtLienholder2Name1().getText().trim();
		String lsLienName3 = gettxtLienholder3Name1().getText().trim();

		if (UtilityMethods.isEmpty(lsLienName1)
			&& (!UtilityMethods.isEmpty(lsLienName2)
				|| !UtilityMethods.isEmpty(lsLienName3)
				|| getchkAdditionalLien().isSelected()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA),
				gettxtLienholder1Name1());
		}

		if (UtilityMethods.isEmpty(lsLienName2)
			&& (!UtilityMethods.isEmpty(lsLienName3)
				|| getchkAdditionalLien().isSelected()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA),
				gettxtLienholder2Name1());
		}
		if (UtilityMethods.isEmpty(lsLienName3)
			&& getchkAdditionalLien().isSelected())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_INCOMPLETE_LIEN_DATA),
				gettxtLienholder3Name1());
		}

		// defect 10631 
		// defect 10127 
		// LIEN1 
		validateLienData(
			gettxtLien1Date(),
			caLien1NameAddrComp,
			csPermLienhldrId1,
			aeRTSEx);

		// LIEN2 
		validateLienData(
			gettxtLien2Date(),
			caLien2NameAddrComp,
			csPermLienhldrId2,
			aeRTSEx);

		// LIEN3 	
		validateLienData(
			gettxtLien3Date(),
			caLien3NameAddrComp,
			csPermLienhldrId3,
			aeRTSEx);
		// end defect 10127 
		// end defect 10631 				
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
