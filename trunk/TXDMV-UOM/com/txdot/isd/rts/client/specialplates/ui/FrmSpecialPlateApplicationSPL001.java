package com.txdot.isd.rts.client.specialplates.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com
	.txdot
	.isd
	.rts
	.client
	.specialplates
	.business
	.SpecialPlatesClientUtilityMethods;

//import com.txdot.isd.rts.server.db.OrganizationNumber;
import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;
import java.awt.Rectangle;
import javax.swing.JLabel;

/*
 * FrmSpecialPlateApplicationSPL001.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		03/11/2003	Initial Prototype
 * K Harrell	04/30/2007	Working...
 * 	JRue					defect 9085 Ver Special Plates
 * K Harrell	05/04/2007	Show all organizations 
 * 							Throw error msg when select sunsetted 
 * 							organization for manufacture or reserve. 
 * 							defect 9085 Ver Special Plate
 * K Harrell	06/01/2007	Modify transcd based upon Request Type
 * 							add assgnSpclPltApplControllerTransCd()
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/04/2007	Do not allow "Reserved" if LP Plate
 * 							modify setData()
 * 							defect 9085 Ver Special Plate
 * K Harrell	06/06/2007	Only validate PLP length against 6 chars 
 * 							if Mfg. Else, validate against 7 chars
 * 							modify validatePersonalizedPlateNo()
 *							defect 9085 Ver Special Plate 
 * K Harrell	06/18/2007	Added temporary text for Help
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/19/2007	Limit calls to VI
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/26/2007	Allow Plate Ownership Change, Customer Supplied
 * 							for CP Plates in months other than Jan/Feb
 * 							modify validateRequestDateForCP()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/18/2007	Allow Plate Ownership change for OLDPLT
 * 							modify populateRequestType() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/31/2007	Do not execute System.out in production
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/03/2007	Use UtilityMethods.rTrim() vs. trim() to
 * 							truncate trailing blanks only.
 * 							modify determinePersonalizedMfgPltNo()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/06/2007	Intercept 1010 when thrown from 
 * 							ValidateInventoryPattern.validateItmNoInput()
 * 							modify validateInvItmNo()
 * 							defect 9206 Ver Special Plates
 * K Harrell	08/09/2007	csRequestType was not set upon selection of
 * 							RequestType when UserPltNoIndi = 1 
 * 							modify handleRequestType()
 * 							defect 9246 Ver Special Plates
 * Ray Rowehl	08/14/2007	Add 1004 to message bypass. 
 * 							modify validateInvItmNo()
 * 							defect 9252 Ver Special Plates          	
 * K Harrell	08/15/2007	Rollback of earlier 9206, 9252 
 * 							modify validateInvItmNo()
 * 							defects 9206, 9252  Ver Special Plates
 * K Harrell	08/28/2007	Changes per Walkthru
 * 							Use constants for mnemonics. Cleanup JavaDoc.
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/23/2007	Do not present special characters for ROP.
 * 							Do not show maxbytecount for ROP
 * 							Do not validate against MAX_PLP_PLTNO_LENGTH. 
 * 							add PERSONAL_OPTIONS_ROP, 
 * 							 PERSONAL_OPTIONS_ROP_ISA
 * 							add cbROP, cvPersonalizedComboVector,
 * 							 svPersonalizedROP, svPersonalizedROPISA
 * 							add buildPersonalizedComboVector()
 * 							modify buildPersonalizedVectors(),
 * 							 determinePersonalizedMfgPltNo(),
 * 							 handlePersonalizedSelection,
 * 							 populatePersonalizedCombos(),
 * 							 setData(), validatePersonalizedPlateNo() 
 * 	  	 					defect 9384 Ver Special Plates 2
 * K Harrell	10/23/2007	New processing for Issue from Inventory
 * 							Specify in advance, chk for record @ MF, 
 * 							call VI, prepopulate INV007 
 * 							modify actionPerformed(), handleRequestType()
 * 							defect 9386 Ver Special Plates 2
 * K Harrell	10/25/2007	New processing for PLPDPPBP (Per DP Private 
 * 							 Bus Plt). ISA always checked, disabled.
 * 							modify handlePlateTypeSelection()
 * 							defect 9389 Ver Special Plates 2
 * K Harrell	11/12/2007	Cannot request new CP Plate for same year
 * 							modify validateRequestDateForCP()  
 * 							defect 9434 Ver Special Plates 2
 * K Harrell	05/13/2008	Do not allow Issue from Inventory for 
 * 							sunsetted plates
 *  						add validateRequestType()
 * 							delete validateMfgRequest()
 * 							defect 9240 Ver Defect_POS_A 
 * K Harrell	01/07/2009  Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify setDataToDataObject()
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	01/21/2009	Deselect ISA checkbox when reselect Vehicle
 * 							modify populatePlateType(), 
 * 							 populateVehClassCd()
 * 							defect 9868 Ver Defect_POS_D
 * K Harrell	01/28/2009	Do not validate PLP plates @ client. Server
 * 							will validate, write error to 
 * 							RTS_SPCL_PLT_REJ_LOG and throw exception.
 * 							modify validateInvItmNo() 
 * 							defect 9578 Ver Defect_POS_D 
  * J Zwiener	02/03/2009	Copy over Renewal Recipient addr, if avail.,
 * 							to SR Func Trans for non-special plate
 * 							events.  Otherwise, use Owner address. 
 * 							modify setDataToDataObject()
 * 							defect 9893 Ver Defect_POS_D
 * K Harrell	02/04/2009	Corrected prolog entry for 1/28/09.  Merge 
 * 							 problem. 
 * 							defect 958 Ver Defect_POS_D
 * K Harrell	07/01/2009	Implement new OwnerData.  Additional Cleanup
 * 							add ivjPanel1, getJPanel1()
 * 							delete jPanel, getJPanel
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/28/2009	New ROP Plates introduced. Treat all same
 * 							as ROP.  Skip Server VI processing if 
 * 							 DuplsAllowdCd = 2 and not from Reserve. 
 * 							modify getRegPltCdFromDropDown(), 
 * 							 populatePersonalizedCombos(), 
 * 							 actionPerformed()  
 * 							defect 10293 Ver Defect_POS_H
 * K Harrell	01/25/2009	New hard coding for handling # characters 
 * 							for ROPMC.
 * 							add ROPMC_MAXBYTES 
 * 							modify handlePersonalizedSelection()
 * 							defect 10293 Ver Defect_POS_H 
 * K Harrell	02/16/2010	Capture RecpntEMail if not empty for SPL002
 * 							modify setData(), setDataToDataObject()   
 * 							defect 10372 Ver POS_640  
 * K Harrell	04/12/2010	Assign PltValidityTerm
 * 							defect 10441 Ver POS_640 
 * K Harrell	07/16/2010	Modify for Multi-Year State Official Plates
 * 							modify handleRequestType()  
 * 							defect 10507 Ver 6.5.0
 * K Harrell	09/14/2010	add BASE_PERSONAL_OPTIONS
 * 							delete PERSONAL_OPTIONS, PERSONAL_OPTIONS_ISA, 
 * 							  PERSONAL_OPTIONS_ROP, PERSONAL_OPTIONS_ROP_ISA
 * 							modify buildPersonalizedVectors(), 
 * 							 validatePersonalizedPlateNo()  
 * 							defect 10571 Ver 6.6.0 
 * J Zwiener	01/24/2011	Prevent orders of crossed over plates
 * 							modify validateRequestType()
 * 							defect 10704 Ver POS_670
 * K Harrell	10/29/2011	Ensure PLPDV plates either start or end with 
 * 							"DV" (excluding ISA symbol) 
 * 							add validatePLPDV() 
 * 							add REQD_PLPDV_SUFFIX_PREFIX
 * 							modify determinePersonalizedMfgPltNo()
 * 							defect 11061 Ver 6.9.0 
 * K Harrell	11/09/2011	Show message for PLPDV 
 * 							add PLPDV_MSG
 * 							add ivjstcLblPLPDVMessage, get method
 * 							modify handlePlateTypeSelection(),
 * 							 getJPanel3() 
 * 							defect 11061 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Class for screen SPL001, Special Plates Application
 * 
 * @version	6.9.0 			11/09/2011
 * @author  Kathy Harrell 
 * @author	Jeff Rue
 * <br>Creation Date:		03/11/2003	08:18:10
 */

public class FrmSpecialPlateApplicationSPL001
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkBoxISA = null;
	private JComboBox ivjcomboOrganizationName = null;
	private JComboBox ivjcomboPersonalized0 = null;
	private JComboBox ivjcomboPersonalized1 = null;
	private JComboBox ivjcomboPersonalized2 = null;
	private JComboBox ivjcomboPersonalized3 = null;
	private JComboBox ivjcomboPersonalized4 = null;
	private JComboBox ivjcomboPersonalized5 = null;
	private JComboBox ivjcomboPersonalized6 = null;
	private JComboBox ivjcomboPersonalized7 = null;
	private JComboBox ivjcomboPlateType = null;
	private JComboBox ivjcomboRequestType = null;
	private JComboBox ivjcomboVehicleClass = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel3 = null;
	private JLabel ivjlblTestInvItemNo = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblOrganization = null;
	private JLabel ivjstcLblPersonalizedPltSelection = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblPlateType = null;
	private JLabel ivjstcLblRequestType = null;
	private JLabel ivjstcLblVehicleClass = null;
	private RTSInputField ivjtxtPlateNo = null;
	private RTSInputField ivjtxtYear = null;

	// Objects 
	private PlateTypeData caPltTypeData = null;
	private SpecialPlatesRegisData caSpclPltRegisData =
		new SpecialPlatesRegisData();

	// boolean
	private boolean cbComboError = false;
	private boolean cbFromMF = false;
	private boolean cbISAIndi = false;
	private boolean cbPersonalizedIndi = false;
	private boolean cbROP = false;

	// defect 10293 
	// int
	private int ROPMC_MAXBYTES = 6;
	// end defect 10293  

	// String 
	private String csOrgNo = new String();
	private String csOrigRegPltNo = new String();
	private String csPersonalizedMfgPltNo = new String();
	private String csRegPltCd = new String();
	private String csRegPltNo = new String();
	private String csRequestType = new String();
	private String csVehClassCd = new String();

	// Vector 
	private Vector cvOrgNo = new Vector();
	private Vector cvPersonalizedComboVector = new Vector();
	private Vector cvPlateType = new Vector();

	// Constant 
	private static final boolean INIT = true;
	private static final boolean RESET = false;

	private static final String DEFAULT_VEH_CLASS_CD = "PASS";
	
	// defect 11061
	private static final String PLPDV_MSG = "Plate No Must Start or End With 'DV'"; 
	private static final String REQD_PLPDV_SUFFIX_PREFIX = "DV";  
	// end defect 11061 
	
	// defect 10571 
	private static final String BASE_PERSONAL_OPTIONS =
		" A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9";

	//	private static final String PERSONAL_OPTIONS =
	//		" . - * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9";
	//	private static final String PERSONAL_OPTIONS_ISA =
	//		" . - * % A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9";
	//
	//	private static final String PERSONAL_OPTIONS_ROP =
	//		" A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9";
	//
	//	private static final String PERSONAL_OPTIONS_ROP_ISA =
	//		" % A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9";
	// end defect 10571 

	private static Vector svPersonalized = new Vector();
	private static Vector svPersonalizedISA = new Vector();
	private static Vector svPersonalizedROP = new Vector();
	private static Vector svPersonalizedROPISA = new Vector();
	private static Vector svRequestType = new Vector();
	private static Vector svVehicleClass = new Vector();
	private JLabel ivjstclLblPLPDVMessage = null;

	static {
		svRequestType.addElement(
			SpecialPlatesConstant.CUSTOMER_SUPPLIED);
		svRequestType.addElement(
			SpecialPlatesConstant.ISSUE_FROM_INVENTORY);
		svRequestType.addElement(SpecialPlatesConstant.MANUFACTURE);
		svRequestType.addElement(
			SpecialPlatesConstant.PLATE_OWNER_CHANGE);
		svRequestType.addElement(SpecialPlatesConstant.FROM_RESERVE);
	}

	static {
		buildVehClassVector();
	}
	static {
		buildPersonalizedVectors();
	}

	/**
	 * Creates a vector of all possible letters,numbers and symbols  
	 * Except ISA Symbol - to be added if ISA selected 
	 */
	private static void buildPersonalizedVectors()
	{

		// defect 10571 
		// Implement Plate Symbols

		//	Load for Non-Personalized 
		//		svPersonalized.add(" ");
		//		StringTokenizer laPersonalOptions =
		//			new StringTokenizer(PERSONAL_OPTIONS, " ");
		//		while (laPersonalOptions.hasMoreTokens())
		//		{
		//			svPersonalized.add(laPersonalOptions.nextToken());
		//		}
		//		// Load for Personalized 
		//		svPersonalizedISA.add(" ");
		//		laPersonalOptions =
		//			new StringTokenizer(PERSONAL_OPTIONS_ISA, " ");
		//		while (laPersonalOptions.hasMoreTokens())
		//		{
		//			svPersonalizedISA.add(laPersonalOptions.nextToken());
		//		}
		//
		//		// defect 9384 
		//		// Load for ROP - non-ISA
		//		svPersonalizedROP.add(" ");
		//		laPersonalOptions =
		//			new StringTokenizer(PERSONAL_OPTIONS_ROP, " ");
		//		while (laPersonalOptions.hasMoreTokens())
		//		{
		//			svPersonalizedROP.add(laPersonalOptions.nextToken());
		//		}
		//
		//		// Load for ROP - ISA
		//		svPersonalizedROPISA.add(" ");
		//		laPersonalOptions =
		//			new StringTokenizer(PERSONAL_OPTIONS_ROP_ISA, " ");
		//
		//		while (laPersonalOptions.hasMoreTokens())
		//		{
		//			svPersonalizedROPISA.add(laPersonalOptions.nextToken());
		//		}
		//		// end defect 9384 

		Vector lvPltSymNoISA = PlateSymbolCache.getPlateSymbolsNoISA();
		Vector lvPltSymISA = PlateSymbolCache.getPlateSymbols();

		// Load for Non-ISA 
		svPersonalized.add(" ");

		for (int i = lvPltSymNoISA.size() - 1; i >= 0; i--)
		{
			PlateSymbolData laData =
				(PlateSymbolData) lvPltSymNoISA.elementAt(i);
			svPersonalized.add(laData.getPltSymCd());
		}

		StringTokenizer laPersonalOptions =
			new StringTokenizer(BASE_PERSONAL_OPTIONS, " ");
		while (laPersonalOptions.hasMoreTokens())
		{
			svPersonalized.add(laPersonalOptions.nextToken());
		}

		// Load for ISA  
		svPersonalizedISA.add(" ");
		for (int i = lvPltSymISA.size() - 1; i >= 0; i--)
		{
			PlateSymbolData laData =
				(PlateSymbolData) lvPltSymISA.elementAt(i);
			svPersonalizedISA.add(laData.getPltSymCd());
		}

		laPersonalOptions =
			new StringTokenizer(BASE_PERSONAL_OPTIONS, " ");

		while (laPersonalOptions.hasMoreTokens())
		{
			svPersonalizedISA.add(laPersonalOptions.nextToken());
		}

		// Load for ROP - non-ISA
		svPersonalizedROP.add(" ");
		laPersonalOptions =
			new StringTokenizer(BASE_PERSONAL_OPTIONS, " ");
		while (laPersonalOptions.hasMoreTokens())
		{
			svPersonalizedROP.add(laPersonalOptions.nextToken());
		}

		// Load for ROP - ISA
		svPersonalizedROPISA.add(" ");
		svPersonalizedROPISA.add(PlateSymbolCache.ISASYMBOL);
		laPersonalOptions =
			new StringTokenizer(BASE_PERSONAL_OPTIONS, " ");
		while (laPersonalOptions.hasMoreTokens())
		{
			svPersonalizedROPISA.add(laPersonalOptions.nextToken());
		}
		// end defect 10571 
	}

	/**
	 * Creates the Spcl Plt Vector of Special Plate Descriptions  
	 * to be loaded upon selection of the "Special Plate" checkbox.
	 */
	private static void buildVehClassVector()
	{
		svVehicleClass =
			VehicleClassSpclPltTypeDescCache
				.getAllVehClassWithSpclPlt();
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
			FrmSpecialPlateApplicationSPL001 aFrmSpecialPlateOrderSPL001;
			aFrmSpecialPlateOrderSPL001 =
				new FrmSpecialPlateApplicationSPL001();
			aFrmSpecialPlateOrderSPL001.setModal(true);
			aFrmSpecialPlateOrderSPL001
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			aFrmSpecialPlateOrderSPL001.show();
			java.awt.Insets insets =
				aFrmSpecialPlateOrderSPL001.getInsets();
			aFrmSpecialPlateOrderSPL001.setSize(
				aFrmSpecialPlateOrderSPL001.getWidth()
					+ insets.left
					+ insets.right,
				aFrmSpecialPlateOrderSPL001.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmSpecialPlateOrderSPL001.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of RTSDialogBox");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSpecialPlateOrderSPL001 constructor comment.
	 */
	public FrmSpecialPlateApplicationSPL001()
	{
		super();
		initialize();
	}
	/**
	 * FrmSpecialPlateOrderSPL001 constructor comment.
	 */
	public FrmSpecialPlateApplicationSPL001(JDialog parent)
	{
		super(parent);
		initialize();
	}
	/**
	 * FrmSpecialPlateOrderSPL001 constructor comment.
	 */
	public FrmSpecialPlateApplicationSPL001(JFrame parent)
	{
		super(parent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE 
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
			restoreComboColor();
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				RTSException leEx = new RTSException();
				validateComboBoxes(leEx);

				if (leEx.isValidationError())
				{
					leEx.displayError(this);
					leEx.getFirstComponent().requestFocus();
					return;
				}
				csRegPltNo = new String();
				csPersonalizedMfgPltNo = new String();
				assgnSpclPltApplControllerTransCd();
				// Validate Year First, then Plate No, then request
				// date if ordering CP (Only January and February)  
				if (validateYear()
					&& validatePlateNo()
					&& validateRequestDateForCP())
				{
					caSpclPltRegisData.setRegPltCd(csRegPltCd);
					caSpclPltRegisData.setRequestType(csRequestType);

					if (csRequestType
						.equals(SpecialPlatesConstant.MANUFACTURE)
						|| csRequestType.equals(
							SpecialPlatesConstant.FROM_RESERVE))
					{
						caSpclPltRegisData.setMFGDate(
							TransCdConstant.SPAPPL);
					}
					else if (
						csRequestType.equals(
							SpecialPlatesConstant
								.ISSUE_FROM_INVENTORY))
					{
						caSpclPltRegisData.setMFGDate(
							new RTSDate().getYYYYMMDDDate());
					}
					else
					{
						caSpclPltRegisData.setMFGDate(0);
					}
					VehicleInquiryData laVehInqData =
						setDataToDataObject();

					// defect 10293
					// Proceed to SPL002 if 
					//  - Customer Supplied && NeedsProgramCd = "O"  
					//  - Duplicates Allowed = 2 && !Reserved
					if ((csRequestType
						.equals(SpecialPlatesConstant.CUSTOMER_SUPPLIED)
						&& caPltTypeData.getNeedsProgramCd().equals(
							SpecialPlatesConstant.OWNER)) //|| cbROP
						|| (caPltTypeData.getDuplsAllowdCd()
							== SpecialPlatesConstant
								.DUPLSALLOWDCD_EVEN_IF_SAME_YR
							&& !csRequestType.equals(
								SpecialPlatesConstant.FROM_RESERVE)))
					{
						// end defect 10293
						getController().processData(
							VCSpecialPlateApplicationSPL001.SPL002,
							laVehInqData);
					}
					else if (
						csRequestType.equals(
							SpecialPlatesConstant.MANUFACTURE))
					{
						caSpclPltRegisData.setVIAllocData(
							SpecialPlatesClientUtilityMethods
								.setupInvAlloc(
								caSpclPltRegisData,
								csOrigRegPltNo));

						int liFcnId =
							(cbPersonalizedIndi
								? InventoryConstant
									.INV_VI_VALIDATE_PER_PLT
								: InventoryConstant
									.INV_GET_NEXT_VI_ITEM_NO);

						// Proceed to Special Plate Information
						getController().processData(
							liFcnId,
							laVehInqData);
					}
					// defect 9386 
					// Remaining Request Types included in the following
					//   including Issue From Inventory  
					//	if (
					//		csRequestType.equals(
					//			SpecialPlatesConstant.CUSTOMER_SUPPLIED)
					//			|| csRequestType.equals(
					//				SpecialPlatesConstant.FROM_RESERVE)
					//			|| csRequestType.equals(
					//				SpecialPlatesConstant
					//					.PLATE_OWNER_CHANGE)
					else
					{
						if (csRequestType
							.equals(
								SpecialPlatesConstant
									.CUSTOMER_SUPPLIED)
							|| csRequestType.equals(
								SpecialPlatesConstant
									.ISSUE_FROM_INVENTORY))
						{
							caSpclPltRegisData.setVIAllocData(
								SpecialPlatesClientUtilityMethods
									.setupInvAlloc(
									caSpclPltRegisData,
									csOrigRegPltNo));
						}
						// Build GSD
						GeneralSearchData laSearchCriteria =
							SpecialPlatesClientUtilityMethods
								.buildSpclPltNoSearchGSD(
								csRegPltNo,
								TransCdConstant.SPAPPL);

						// Add GSD and VehInqData to vector
						Vector lvData = new Vector();
						lvData.add(laSearchCriteria);
						lvData.add(laVehInqData);
						getController().processData(
							VCSpecialPlateApplicationSPL001.GET_SP_REC,
							lvData);
					}
					// end defect 9386 
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
			else if (aaAE.getSource() == getcomboVehicleClass())
			{
				if (getcomboVehicleClass().getSelectedIndex() != -1)
				{
					csVehClassCd =
						(String) svVehicleClass.elementAt(
							getcomboVehicleClass().getSelectedIndex());

					// Initialize Plate Types according to selected 
					// vehicle class  	
					populatePlateType();
					gettxtPlateNo().setText("");
					gettxtPlateNo().setEnabled(false);
				}
			}
			else if (aaAE.getSource() == getcomboPlateType())
			{
				handlePlateTypeSelection();
				populateOrganizationName(INIT);
				getcomboPlateType().requestFocus();
				populateRequestType(INIT);
				handleRequestType();
			}
			else if (aaAE.getSource() == getcomboOrganizationName())
			{
				getOrgNoFromDropDown(
					getcomboOrganizationName().getSelectedIndex());
			}
			else if (aaAE.getSource() == getcomboRequestType())
			{
				handleRequestType();
			}
			else if (aaAE.getSource() == getchkBoxISA())
			{
				cbISAIndi = (getchkBoxISA().isSelected());

				if (cbPersonalizedIndi)
				{
					handlePersonalizedSelection(INIT);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			aeRTSEx.getFirstComponent().requestFocus();
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Assign TransCd for Special Plate Applications 
	 */
	private void assgnSpclPltApplControllerTransCd()
	{
		if (UtilityMethods.isSPAPPL(getController().getTransCode()))
		{
			if (csRequestType
				.equals(SpecialPlatesConstant.ISSUE_FROM_INVENTORY))
			{
				getController().setTransCode(TransCdConstant.SPAPPI);
			}
			else if (
				csRequestType.equals(
					SpecialPlatesConstant.FROM_RESERVE))
			{
				getController().setTransCode(TransCdConstant.SPAPPR);
			}
			else if (
				csRequestType.equals(
					SpecialPlatesConstant.CUSTOMER_SUPPLIED))
			{
				getController().setTransCode(TransCdConstant.SPAPPC);
			}
			else if (
				csRequestType.equals(
					SpecialPlatesConstant.PLATE_OWNER_CHANGE))
			{
				getController().setTransCode(TransCdConstant.SPAPPO);
			}
			else if (
				csRequestType.equals(
					SpecialPlatesConstant.MANUFACTURE))
			{
				getController().setTransCode(TransCdConstant.SPAPPL);
			}

		}
	}

	/** 
	 * 
	 * Build Personalized component vector
	 *
	 */
	private void buildPersonalizedComboVector()
	{
		cvPersonalizedComboVector.add(getcomboPersonalized0());
		cvPersonalizedComboVector.add(getcomboPersonalized1());
		cvPersonalizedComboVector.add(getcomboPersonalized2());
		cvPersonalizedComboVector.add(getcomboPersonalized3());
		cvPersonalizedComboVector.add(getcomboPersonalized4());
		cvPersonalizedComboVector.add(getcomboPersonalized5());
		cvPersonalizedComboVector.add(getcomboPersonalized6());
		cvPersonalizedComboVector.add(getcomboPersonalized7());

	}

	
	/**
	 * Determine the MfgPltNo for Personalized Plate. Return false if 
	 * invalid. 
	 * 
	 * @return boolean 
	 */
	private boolean determinePersonalizedMfgPltNo()
	{
		RTSException leRTSEx = new RTSException();
		csPersonalizedMfgPltNo = new String();
		String lsNextChar = "";

		// defect 9384
		// Use vector of components 
		// Throw error if Spaces between letters in ROP   
		boolean lbROPCharFound = false;
		Vector lvSpaces = new Vector();
		Vector lvISA = new Vector();

		boolean lbReturn = true;

		for (int i = 0; i <= 7; i++)
		{
			JComboBox laComboBox =
				(JComboBox) cvPersonalizedComboVector.get(i);
			if (laComboBox.isEnabled())
			{
				lsNextChar = (String) laComboBox.getSelectedItem();
				csPersonalizedMfgPltNo =
					csPersonalizedMfgPltNo + lsNextChar;
				if (lsNextChar.equals("%"))
				{
					lvISA.add(laComboBox);
				}
				if (cbROP)
				{
					if (lsNextChar.equals(" "))
					{
						if (lbROPCharFound)
						{
							lvSpaces.add(laComboBox);
						}
					}
					else
					{
						lbROPCharFound = true;
						if (lvSpaces.size() > 0)
						{
							for (int j = 0; j < lvSpaces.size(); j++)
							{
								leRTSEx.addException(
									new RTSException(150),
									(Component) lvSpaces.elementAt(j));
							}
							lvSpaces = new Vector();
						}
					}
				}
			}
		}

		// Maximum number of ISA symbols allowed is 1 
		if (lvISA.size() > 1)
		{
			for (int i = 0; i < lvISA.size(); i++)
			{
				leRTSEx.addException(
					new RTSException(150),
					(Component) lvISA.get(i));
			}
		}
		
		// defect 11061
		 csPersonalizedMfgPltNo =
				UtilityMethods.rTrim(csPersonalizedMfgPltNo);
		validatePLPDV(leRTSEx);
		// end defect 11061 
		
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
			cbComboError = true;
		}
		else
		{
			 // defect 11061 
			 // csPersonalizedMfgPltNo =
			 //  UtilityMethods.rTrim(csPersonalizedMfgPltNo);
			 // end defect 11061
			caSpclPltRegisData.setMfgPltNo(csPersonalizedMfgPltNo);
		}
		// end defect 9384
		return lbReturn;
	}

	/**
	 * Display Inventory Pattern for Plate in development
	 */
	private void displayInvPatInDev()
	{
		// This label is just for testing in development mode
		getlblTestInvItemNo().setVisible(false);
		if (SystemProperty.getProdStatus()
			!= SystemProperty.APP_PROD_STATUS)
		{
			int liYear = 0;
			if (gettxtYear().isEnabled())
			{
				liYear = new RTSDate().getYear();
				try
				{
					liYear = Integer.parseInt(gettxtYear().getText());
				}
				catch (NumberFormatException aeNFE)
				{

				}
			}
			Vector lvInvPatCache =
				InventoryPatternsCache.getInvPatrns(csRegPltCd, liYear);
			if (lvInvPatCache != null && lvInvPatCache.size() > 0)
			{
				int liRandomNum =
					new Random().nextInt(lvInvPatCache.size());
				InventoryPatternsData laInvPat =
					(InventoryPatternsData) lvInvPatCache.get(
						liRandomNum);
				String lsTestInvItmNo = laInvPat.getInvItmNo();
				String lsTestInvItmEndNo = laInvPat.getInvItmEndNo();
				getlblTestInvItemNo().setText(
					lsTestInvItmNo
						+ CommonConstant.STR_SPACE_ONE
						+ CommonConstant.STR_DASH
						+ CommonConstant.STR_SPACE_ONE
						+ lsTestInvItmEndNo);
				getlblTestInvItemNo().setVisible(true);
			}
		}
	}

	/**
	 * Return the btnECH property value.
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
				ivjButtonPanel1.setName("btnECH");
				ivjButtonPanel1.setBounds(118, 274, 221, 42);
				ivjButtonPanel1.setAsDefault(this);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjButtonPanel1;
	}
	/**
	 * Return the chkBoxISA property value.
	 * 
	 * @return JCheckBox
	 */

	private JCheckBox getchkBoxISA()
	{
		if (ivjchkBoxISA == null)
		{
			try
			{
				ivjchkBoxISA = new JCheckBox();
				ivjchkBoxISA.setSize(45, 23);
				ivjchkBoxISA.setName("chkBoxISA");
				ivjchkBoxISA.setMnemonic(java.awt.event.KeyEvent.VK_I);
				ivjchkBoxISA.setText("ISA");
				ivjchkBoxISA.setLocation(407, 62);
				// user code 
				ivjchkBoxISA.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjchkBoxISA;
	}
	/**
	 * Return the ivjcomboOrganizationName property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboOrganizationName()
	{
		if (ivjcomboOrganizationName == null)
		{
			try
			{
				ivjcomboOrganizationName = new JComboBox();
				ivjcomboOrganizationName.setSize(235, 25);
				ivjcomboOrganizationName.setName(
					"comboOrganizationName");
				ivjcomboOrganizationName.setLocation(99, 90);
				ivjcomboOrganizationName.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboOrganizationName;
	}
	/**
	 * Return the ivjcomboPersonalized0 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPersonalized0()
	{
		if (ivjcomboPersonalized0 == null)
		{
			try
			{
				ivjcomboPersonalized0 = new JComboBox();
				ivjcomboPersonalized0.setSize(41, 23);
				ivjcomboPersonalized0.setName("comboPersonalized1");
				ivjcomboPersonalized0.setLocation(21, 33);
				// user code 
				ivjcomboPersonalized0.addItemListener(this);
				// user code end

			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPersonalized0;
	}
	/**
	 * Return the ivjcomboPersonalized1 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPersonalized1()
	{
		if (ivjcomboPersonalized1 == null)
		{
			try
			{
				ivjcomboPersonalized1 = new JComboBox();
				ivjcomboPersonalized1.setSize(41, 23);
				ivjcomboPersonalized1.setName("comboPersonalized2");
				ivjcomboPersonalized1.setLocation(71, 33);
				// user code 
				ivjcomboPersonalized1.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPersonalized1;
	}
	/**
	 * Return the ivjcomboPersonalized2 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPersonalized2()
	{
		if (ivjcomboPersonalized2 == null)
		{
			try
			{
				ivjcomboPersonalized2 = new JComboBox();
				ivjcomboPersonalized2.setSize(41, 23);
				ivjcomboPersonalized2.setName("comboPersonalized3");
				ivjcomboPersonalized2.setLocation(121, 33);
				// user code 
				ivjcomboPersonalized2.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPersonalized2;
	}
	/**
	 * Return the ivjcomboPersonalized3 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPersonalized3()
	{
		if (ivjcomboPersonalized3 == null)
		{
			try
			{
				ivjcomboPersonalized3 = new JComboBox();
				ivjcomboPersonalized3.setName("comboPersonalized4");
				ivjcomboPersonalized3.setBounds(171, 33, 41, 23);
				//	user code 
				ivjcomboPersonalized3.addItemListener(this);
				// user code end
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPersonalized3;
	}
	/**
	 * Return the ivjcomboPersonalized4 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPersonalized4()
	{
		if (ivjcomboPersonalized4 == null)
		{
			try
			{
				ivjcomboPersonalized4 = new JComboBox();
				ivjcomboPersonalized4.setName("comboPersonalized5");
				ivjcomboPersonalized4.setBounds(221, 33, 41, 23);
				// user code 
				ivjcomboPersonalized4.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPersonalized4;
	}
	/**
	 * Return the ivjcomboPersonalized5 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPersonalized5()
	{
		if (ivjcomboPersonalized5 == null)
		{
			try
			{
				ivjcomboPersonalized5 = new JComboBox();
				ivjcomboPersonalized5.setSize(41, 23);
				ivjcomboPersonalized5.setName("comboPersonalized6");
				ivjcomboPersonalized5.setLocation(271, 33);
				// user code 
				ivjcomboPersonalized5.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPersonalized5;
	}
	/**
	 * Return the ivjcomboPersonalized6 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPersonalized6()
	{
		if (ivjcomboPersonalized6 == null)
		{
			try
			{
				ivjcomboPersonalized6 = new JComboBox();
				ivjcomboPersonalized6.setSize(41, 23);
				ivjcomboPersonalized6.setName("comboPersonalized7");
				ivjcomboPersonalized6.setLocation(321, 33);
				// user code 
				ivjcomboPersonalized6.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPersonalized6;
	}

	/**
	 * Return the ivjcomboPersonalized7 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPersonalized7()
	{
		if (ivjcomboPersonalized7 == null)
		{
			try
			{
				ivjcomboPersonalized7 = new JComboBox();
				ivjcomboPersonalized7.setSize(41, 23);
				ivjcomboPersonalized7.setName("comboPersonalized8");
				ivjcomboPersonalized7.setLocation(371, 33);
				// user code 
				ivjcomboPersonalized7.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPersonalized7;
	}
	/**
	 * Return the ivjcomboPlateType property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboPlateType()
	{
		if (ivjcomboPlateType == null)
		{
			try
			{
				ivjcomboPlateType = new JComboBox();
				ivjcomboPlateType.setSize(235, 25);
				ivjcomboPlateType.setName("comboPlateType");
				ivjcomboPlateType.setLocation(99, 54);
				ivjcomboPlateType.addActionListener(this);
				// user code end
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboPlateType;
	}
	/**
	 * Return the ivjcomboRequestType property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboRequestType()
	{
		if (ivjcomboRequestType == null)
		{
			try
			{
				ivjcomboRequestType = new JComboBox();
				ivjcomboRequestType.setSize(235, 25);
				ivjcomboRequestType.setName("comboRequestType");
				ivjcomboRequestType.setLocation(99, 125);
				ivjcomboRequestType.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboRequestType;
	}
	/**
	 * Return the ivjcomboVehicleClass property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcomboVehicleClass()
	{
		if (ivjcomboVehicleClass == null)
		{
			try
			{
				ivjcomboVehicleClass = new JComboBox();
				ivjcomboVehicleClass.setBounds(99, 17, 235, 25);
				ivjcomboVehicleClass.setName("comboVehicleClass");
				ivjcomboVehicleClass.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboVehicleClass;
	}
	/**
	* This method initializes jPanel
	* 
	* @return JPanel
	*/
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			ivjJPanel1 = new JPanel();
			ivjJPanel1.setLayout(null);
			ivjJPanel1.add(getstcLblPlateNo(), null);
			ivjJPanel1.add(getstcLblRequestType(), null);
			ivjJPanel1.add(getstcLblOrganization(), null);
			ivjJPanel1.add(getstcLblPlateType(), null);
			ivjJPanel1.add(getcomboPlateType(), null);
			ivjJPanel1.add(getcomboOrganizationName(), null);
			ivjJPanel1.add(getcomboRequestType(), null);
			ivjJPanel1.add(gettxtPlateNo(), null);
			ivjJPanel1.add(getstcLblVehicleClass(), null);
			ivjJPanel1.add(getcomboVehicleClass(), null);
			ivjJPanel1.add(gettxtYear(), null);
			ivjJPanel1.add(getlblTestInvItemNo(), null);
			ivjJPanel1.setBounds(30, 8, 374, 188);
		}
		return ivjJPanel1;
	}
	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(21, 199, 428, 69);
				getJPanel3().add(
					getstcLblPersonalizedPltSelection(),
					getstcLblPersonalizedPltSelection().getName());
				getJPanel3().add(
					getcomboPersonalized0(),
					getcomboPersonalized0().getName());
				getJPanel3().add(
					getcomboPersonalized1(),
					getcomboPersonalized1().getName());
				getJPanel3().add(
					getcomboPersonalized2(),
					getcomboPersonalized2().getName());
				getJPanel3().add(
					getcomboPersonalized3(),
					getcomboPersonalized3().getName());
				getJPanel3().add(
					getcomboPersonalized4(),
					getcomboPersonalized4().getName());
				getJPanel3().add(
					getcomboPersonalized5(),
					getcomboPersonalized5().getName());
				getJPanel3().add(
					getcomboPersonalized6(),
					getcomboPersonalized6().getName());
				getJPanel3().add(
					getcomboPersonalized7(),
					getcomboPersonalized7().getName());
				
				// defect 11061 
				getJPanel3().add(getstcLblPLPDVMessage(), null);
				// end defect 11061 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJPanel3;
	}
	/**
	 * This method initializes ivjlblTestInvItemNo
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTestInvItemNo()
	{
		if (ivjlblTestInvItemNo == null)
		{
			ivjlblTestInvItemNo = new JLabel();
			ivjlblTestInvItemNo.setSize(198, 23);
			ivjlblTestInvItemNo.setText("");
			ivjlblTestInvItemNo.setLocation(175, 160);
			ivjlblTestInvItemNo.setFont(
				new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
			ivjlblTestInvItemNo.setForeground(
				java.awt.SystemColor.textInactiveText);
		}
		return ivjlblTestInvItemNo;
	}
	/**
	 * Get the item from the value in the combo box
	 * 
	 * @param aiIndex
	 */
	private void getOrgNoFromDropDown(int aiIndex)
	{
		String lsOrgNo = (String) cvOrgNo.elementAt(aiIndex);
		csOrgNo = lsOrgNo.substring(50).trim();
	}

	/**
	 * Get the item from the value in the combo box
	 * 
	 * @param aiIndex
	 */
	private void getRegPltCdFromDropDown(int aiIndex)
	{
		String lsRegPltCd = (String) cvPlateType.elementAt(aiIndex);
		csRegPltCd = lsRegPltCd.substring(50).trim();

		// defect 10293
		// Have added new ROP type plates: ROPMC, ROPPB, ROPTRL 
		// Assign based on BaseRegPltCd
		// cbROP =	csRegPltCd.equals(SpecialPlatesConstant.RADIO_OPERATOR_PLATE); 
		caPltTypeData = PlateTypeCache.getPlateType(csRegPltCd);
		cbROP = caPltTypeData.isROPBaseRegPltCd();
		// end defect 10293 
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				getRTSDialogBoxContentPane().add(
					getJPanel3(),
					getJPanel3().getName());
				getRTSDialogBoxContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				ivjRTSDialogBoxContentPane.add(getJPanel1(), null);
				// user code end
				ivjRTSDialogBoxContentPane.add(getchkBoxISA(), null);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}
	/**
	 * Return the stcLblOrganization property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblOrganization()
	{
		if (ivjstcLblOrganization == null)
		{
			try
			{
				ivjstcLblOrganization = new JLabel();
				ivjstcLblOrganization.setName("stcLblOrganization");
				ivjstcLblOrganization.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_Z);
				ivjstcLblOrganization.setText("Organization:");
				ivjstcLblOrganization.setLabelFor(
					getcomboOrganizationName());
				ivjstcLblOrganization.setBounds(14, 92, 75, 23);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblOrganization;
	}

	/**
	 * Return the stclblSpecialPlateRequest property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblPersonalizedPltSelection()
	{
		if (ivjstcLblPersonalizedPltSelection == null)
		{
			try
			{
				ivjstcLblPersonalizedPltSelection = new JLabel();
				ivjstcLblPersonalizedPltSelection.setName(
					"stclblSpecialPlateRequest");
				ivjstcLblPersonalizedPltSelection.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjstcLblPersonalizedPltSelection.setText(
					"Personalized Plate Selection:");
				ivjstcLblPersonalizedPltSelection.setLabelFor(
					getcomboPersonalized0());
				ivjstcLblPersonalizedPltSelection.setBounds(9, 3, 168, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPersonalizedPltSelection;
	}

	/**
	 * Return the ivjstcLblPlateNo property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			try
			{
				ivjstcLblPlateNo = new JLabel();
				ivjstcLblPlateNo.setSize(50, 23);
				ivjstcLblPlateNo.setName("stcLblPlateNo");
				ivjstcLblPlateNo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_P);
				ivjstcLblPlateNo.setText("Plate No:");
				ivjstcLblPlateNo.setLocation(39, 160);
				// user code begin {1}
				ivjstcLblPlateNo.setLabelFor(gettxtPlateNo());
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPlateNo;
	}

	/**
	 * Return the stcLblSelectSpecialPlate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblPlateType()
	{
		if (ivjstcLblPlateType == null)
		{
			try
			{
				ivjstcLblPlateType = new JLabel();
				ivjstcLblPlateType.setName("ivjstcLblPlateType");
				ivjstcLblPlateType.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_T);
				ivjstcLblPlateType.setText("Plate Type:");
				ivjstcLblPlateType.setLabelFor(getcomboPlateType());
				ivjstcLblPlateType.setBounds(27, 56, 62, 23);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPlateType;
	}
	/**
	 * Return the stclblValidEntries property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPLPDVMessage()
	{
		if (ivjstclLblPLPDVMessage == null)
		{
			try
			{
				ivjstclLblPLPDVMessage = new JLabel();
				ivjstclLblPLPDVMessage.setBounds(new Rectangle(177, 3, 235, 20));
				ivjstclLblPLPDVMessage.setText(PLPDV_MSG);
				ivjstclLblPLPDVMessage.setForeground(java.awt.Color.red); 
				ivjstclLblPLPDVMessage.setVisible(false);
				ivjstclLblPLPDVMessage.setHorizontalAlignment(SwingConstants.CENTER);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstclLblPLPDVMessage;
	}

	/**
	 * Return the stclblValidEntries property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblRequestType()
	{
		if (ivjstcLblRequestType == null)
		{
			try
			{
				ivjstcLblRequestType = new JLabel();
				ivjstcLblRequestType.setName("stcLblRequestType");
				ivjstcLblRequestType.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjstcLblRequestType.setText("Request Type: ");
				ivjstcLblRequestType.setLabelFor(getcomboRequestType());
				// user code begin {1}
				ivjstcLblRequestType.setBounds(6, 127, 83, 23);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblRequestType;
	}

	/**
	 * Return the ivjstcLblVehicleClass property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblVehicleClass()
	{
		if (ivjstcLblVehicleClass == null)
		{
			try
			{
				ivjstcLblVehicleClass = new JLabel();
				ivjstcLblVehicleClass.setName("ivjstcLblVehicleClass");
				ivjstcLblVehicleClass.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_V);
				ivjstcLblVehicleClass.setText(" Vehicle Class:");
				ivjstcLblVehicleClass.setLabelFor(
					getcomboVehicleClass());
				ivjstcLblVehicleClass.setBounds(6, 19, 83, 23);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblVehicleClass;
	}
	/**
	 * Return the ivjtxtPlateNo property value.
	 * 
	 * @return JTextField
	 */

	private RTSInputField gettxtPlateNo()
	{
		if (ivjtxtPlateNo == null)
		{
			try
			{
				ivjtxtPlateNo = new RTSInputField();
				ivjtxtPlateNo.setSize(72, 23);
				ivjtxtPlateNo.setName("ivjtxtPlateNo");
				ivjtxtPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPlateNo.setMaxLength(8);
				// user code end
				ivjtxtPlateNo.setLocation(99, 160);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtPlateNo;
	}

	/**
	 * This method initializes ivjtxtYear
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtYear()
	{
		if (ivjtxtYear == null)
		{
			ivjtxtYear = new RTSInputField();
			ivjtxtYear.setSize(35, 25);
			ivjtxtYear.setText("");
			ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtYear.setLocation(337, 54);
			ivjtxtYear.setMaxLength(4);
		}
		return ivjtxtYear;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException aeRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		aeRTSEx.displayError(this);
	}

	/**
	 * 
	 * Set Personalized Drop/Down enabled, disabled as a function
	 * of PlateTypeData.getUserPltNoIndi()
	 *
	 * @param abInit 
	 */

	private void handlePersonalizedSelection(boolean abInit)
	{
		int liMBC = 0;
		if (abInit == INIT)
		{
			// defect 9384
			// ROP - no special characters; Set to accomodate ISA
			liMBC = caPltTypeData.getMaxByteCount();

			// defect 10293 
			// Do not adjust ROPMC Max Bytes if not ISAIndi 
			//if (cbROP || cbISAIndi)
			if ((cbROP && liMBC != ROPMC_MAXBYTES) || cbISAIndi)
			{
				liMBC = liMBC - 1;
			}
			// end defect 10293 
		}
		// end defect 9384

		getcomboPersonalized0().setEnabled(cbPersonalizedIndi);
		getcomboPersonalized1().setEnabled(
			cbPersonalizedIndi & liMBC > 1);
		getcomboPersonalized2().setEnabled(
			cbPersonalizedIndi & liMBC > 2);
		getcomboPersonalized3().setEnabled(
			cbPersonalizedIndi & liMBC > 3);
		getcomboPersonalized4().setEnabled(
			cbPersonalizedIndi & liMBC > 4);
		getcomboPersonalized5().setEnabled(
			cbPersonalizedIndi & liMBC > 5);
		getcomboPersonalized6().setEnabled(
			cbPersonalizedIndi & liMBC > 6);
		getcomboPersonalized7().setEnabled(
			cbPersonalizedIndi & liMBC > 7);

		if (cbPersonalizedIndi)
		{
			getstcLblPlateNo().setEnabled(false);
			gettxtPlateNo().setEnabled(false);
			getstcLblPersonalizedPltSelection().setEnabled(true);
			populatePersonalizedCombos();
		}
		else
		{
			getstcLblPersonalizedPltSelection().setEnabled(false);
			getcomboPersonalized0().removeAllItems();
			getcomboPersonalized1().removeAllItems();
			getcomboPersonalized2().removeAllItems();
			getcomboPersonalized3().removeAllItems();
			getcomboPersonalized4().removeAllItems();
			getcomboPersonalized5().removeAllItems();
			getcomboPersonalized6().removeAllItems();
			getcomboPersonalized7().removeAllItems();
		}
	}

	/**
	 * Handle Plate Type Selection 
	 */
	private void handlePlateTypeSelection()
	{
		getRegPltCdFromDropDown(getcomboPlateType().getSelectedIndex());

		// Handle ISA chkbox

		// defect 9389 
		// Disable & Select ISA checkbox for PLPDPPBP
		getchkBoxISA().setSelected(false);
		getchkBoxISA().setEnabled(false);
		cbISAIndi = false;

		if (caPltTypeData
			.getISAAllowdCd()
			.equals(SpecialPlatesConstant.POS_EVENTS)
			|| (caPltTypeData
				.getISAAllowdCd()
				.equals(
					SpecialPlatesConstant.BOTH_POS_AND_ITRNT_EVENTS)))
		{
			if (csRegPltCd
				.equals(SpecialPlatesConstant.PER_DP_PRIVATE_BUS_PLT))
			{
				getchkBoxISA().setSelected(true);
				cbISAIndi = true;
			}
			else
			{
				getchkBoxISA().setEnabled(true);
			}
		}
		// end defect 9389

		if (caPltTypeData.getAnnualPltIndi() == 1)
		{
			gettxtYear().setVisible(true);
			gettxtYear().setEnabled(true);
			int liYear = new RTSDate().getYear();

			int liCurrentMonth = new RTSDate().getMonth();
			int liExpMonth =
				(liCurrentMonth == 1 ? 12 : liCurrentMonth - 1);

			// Reset liExpMo if Fixed;  	
			SpecialPlateFixedExpirationMonthData laSpclPltFxdExpMoData =
				SpecialPlateFixedExpirationMonthCache.getRegPltCd(
					csRegPltCd);

			// Fxd Exp Mo 
			if (laSpclPltFxdExpMoData != null)
			{
				liExpMonth = laSpclPltFxdExpMoData.getFxdExpMo();
				// Display if not production 
				if (SystemProperty.getProdStatus()
					!= SystemProperty.APP_PROD_STATUS)
				{
					System.out.println(
						csRegPltCd + " Fixed Exp Mo: " + liExpMonth);
				}
			}

			// Assess No Months 
			int liNoMonths =
				(liExpMonth + liYear * 12)
					- (liCurrentMonth + liYear * 12);
			if (liNoMonths <= 2)
			{
				liYear = liYear + 1;
			}
			gettxtYear().setText("" + liYear);
		}
		else
		{
			gettxtYear().setText("");
			gettxtYear().setVisible(false);
			gettxtYear().setEnabled(false);

		}
		// defect 11061 
		getstcLblPLPDVMessage().setVisible(caPltTypeData.isPLPDV());
		// end defect 11061 
		displayInvPatInDev();
		// Handle Personalized combo boxes
		cbPersonalizedIndi = caPltTypeData.getUserPltNoIndi() == 1;
		handlePersonalizedSelection(INIT);

	}
	/**
	 * Enable PlateNo based upon Request Type 
	 */
	private void handleRequestType()
	{
		// defect 9246
		// Set Request Type regardless of caPltTypeData.getUserPltNoIndi 
		csRequestType =
			((String) getcomboRequestType().getSelectedItem()).trim();
		// end defect 9246

		// defect 10507 
		// State Official should be able to enter Plate No 
		//   add " || caPltTypeData.isMultiYrOfclPlt()" 
		// defect 9386 
		// Enable Plate No Entry Field if not Manufacture and 
		//  UserPltNoIndi = 0    
		boolean lbEnable =
			(!csRequestType.equals(SpecialPlatesConstant.MANUFACTURE)
				&& caPltTypeData.getUserPltNoIndi() == 0)
				|| caPltTypeData.isMultiYrOfclPlt();
		// end defect 10507 

		getstcLblPlateNo().setEnabled(lbEnable);
		gettxtPlateNo().setEnabled(lbEnable);
		gettxtPlateNo().setText("");
		// end defect 9386 

		caSpclPltRegisData.setVIAllocData(null);
	}
	/**
	 * Initialize the class.
	 */

	private void initialize()
	{
		try
		{
			setName("FrmSpecialPlateOrderSPL001");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(474, 351);
			setTitle("Special Plate Application     SPL001");
			setContentPane(getRTSDialogBoxContentPane());
			// user code begin {1}
			// user code end
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aeIE 
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aeIE)
	{
		if (cbComboError)
		{
			restoreComboColor();
			cbComboError = false;
		}
	}

	/**
	 * Populate the Organization Name
	 * 
	 * @param abInit 
	 * @throws RTSException
	 */
	private void populateOrganizationName(boolean abInit)
		throws RTSException
	{
		getcomboOrganizationName().removeActionListener(this);
		getcomboOrganizationName().removeAllItems();
		getcomboOrganizationName().setSelectedIndex(-1);
		getcomboOrganizationName().setEnabled(abInit);

		// if Initialize  
		if (abInit == INIT)
		{
			// Add 2nd parameter to indicate want all plates 
			Vector lvOrgNo =
				OrganizationNumberCache.getOrgsPerPlt(
					csRegPltCd,
					false);
			OrganizationNumberData laOrgNoData =
				new OrganizationNumberData();

			if (lvOrgNo.size() != 0)
			{

				cvOrgNo = new Vector();
				for (int i = 0; i < lvOrgNo.size(); i++)
				{
					laOrgNoData =
						(OrganizationNumberData) lvOrgNo.elementAt(i);
					String lsOrgName =
						UtilityMethods.addPaddingRight(
							laOrgNoData.getOrgNoDesc(),
							50,
							" ")
							+ laOrgNoData.getOrgNo();
					cvOrgNo.add(i, lsOrgName);
				}

				UtilityMethods.sort(cvOrgNo);

				for (int i = 0; i < lvOrgNo.size(); i++)
				{
					String lsOrgNo = (String) cvOrgNo.elementAt(i);
					if (i == 0)
					{
						csOrgNo = lsOrgNo.substring(50).trim();
					}
					getcomboOrganizationName().addItem(
						lsOrgNo.substring(0, 50).trim());
				}
				getcomboOrganizationName().setEnabled(true);
				getcomboOrganizationName().setSelectedIndex(
					cvOrgNo.size() == 1 ? 0 : -1);
				comboBoxHotKeyFix(getcomboOrganizationName());
			}
		}
		getcomboOrganizationName().addActionListener(this);
		populateRequestType(abInit);
	}
	/**
	 * Populate the PersonalizedCombos based upon the selection of ISA
	 * checkbox.
	 */

	private void populatePersonalizedCombos()
	{
		// defect 9384
		// ROP options do not include special characters

		Vector lvVector;

		// defect 10293 
		// Use class variable vs. local 
		//boolean lbROP =
		//	csRegPltCd.equals(
		//		SpecialPlatesConstant.RADIO_OPERATOR_PLATE);

		if (cbISAIndi)
		{
			lvVector = cbROP ? svPersonalizedROPISA : svPersonalizedISA;
		}
		else
		{
			lvVector = cbROP ? svPersonalizedROP : svPersonalized;
			// end defect 10293 
		}
		// end defect 9384

		getcomboPersonalized0().removeAllItems();
		getcomboPersonalized1().removeAllItems();
		getcomboPersonalized2().removeAllItems();
		getcomboPersonalized3().removeAllItems();
		getcomboPersonalized4().removeAllItems();
		getcomboPersonalized5().removeAllItems();
		getcomboPersonalized6().removeAllItems();
		getcomboPersonalized7().removeAllItems();

		for (int i = 0; i < lvVector.size(); i++)
		{
			getcomboPersonalized0().addItem(lvVector.elementAt(i));
			getcomboPersonalized1().addItem(lvVector.elementAt(i));
			getcomboPersonalized2().addItem(lvVector.elementAt(i));
			getcomboPersonalized3().addItem(lvVector.elementAt(i));
			getcomboPersonalized4().addItem(lvVector.elementAt(i));
			getcomboPersonalized5().addItem(lvVector.elementAt(i));
			getcomboPersonalized6().addItem(lvVector.elementAt(i));
			getcomboPersonalized7().addItem(lvVector.elementAt(i));
		}
	}

	/**
	 * Populate the Plate Type 
	 * 
	 * @throws RTSException
	 */
	private void populatePlateType() throws RTSException
	{
		getcomboPlateType().removeActionListener(this);
		getcomboPlateType().removeAllItems();
		getcomboPlateType().setSelectedIndex(-1);

		// defect 9868
		getchkBoxISA().setSelected(false);
		getchkBoxISA().setEnabled(false);
		// end defect 9868 

		cvPlateType.removeAllElements();
		cbPersonalizedIndi = false;

		Vector lvPlateType =
			(Vector) UtilityMethods.copy(
				VehicleClassSpclPltTypeDescCache
					.getVehClassRegClassDescs(
					csVehClassCd));

		VehicleClassSpclPltTypeDescriptionData laData =
			new VehicleClassSpclPltTypeDescriptionData();

		for (int i = 0; i < lvPlateType.size(); i++)
		{
			laData =
				(
					VehicleClassSpclPltTypeDescriptionData) lvPlateType
						.elementAt(
					i);

			// do not include plate if location if out of scope 		
			if (!PlateTypeCache
				.isOutOfScopePlate(
					laData.getRegPltCd(),
					SystemProperty.getOfficeIssuanceCd(),
					SpecialPlatesConstant.ORDER_TYPE_EVENTS))
			{
				cvPlateType.add(
					UtilityMethods.addPaddingRight(
						laData.getRegPltCdDesc(),
						50,
						" ")
						+ laData.getRegPltCd());
			}
		}
		UtilityMethods.sort(cvPlateType);

		for (int i = 0; i < cvPlateType.size(); i++)
		{
			String lsElement =
				((String) cvPlateType.get(i)).substring(0, 30);
			getcomboPlateType().addItem(lsElement);
		}
		if (cvPlateType.size() == 1)
		{
			csRegPltCd =
				((String) cvPlateType.elementAt(0)).substring(50);
			getcomboPlateType().setSelectedIndex(0);
			handlePlateTypeSelection();
			populateOrganizationName(INIT);
		}
		else
		{
			getcomboPlateType().setSelectedIndex(-1);
			comboBoxHotKeyFix(getcomboPlateType());
			gettxtYear().setVisible(false);
			gettxtYear().setEnabled(false);
			getchkBoxISA().setEnabled(false);
			handlePersonalizedSelection(RESET);
			populateOrganizationName(RESET);
		}
		getcomboPlateType().addActionListener(this);
	}

	/**
	 * Populate the Request Type combo
	 * 
	 * @param abInit  
	 * @throws RTSException
	 */
	private void populateRequestType(boolean abInit)
		throws RTSException
	{
		getcomboRequestType().removeActionListener(this);
		gettxtPlateNo().setText("");
		gettxtPlateNo().setEnabled(false);
		getcomboRequestType().removeAllItems();
		getcomboRequestType().setSelectedIndex(-1);
		getcomboRequestType().setEnabled(abInit);

		// If Initialize 
		if (abInit == INIT)
		{
			boolean lbOldPlt =
				caPltTypeData.getNeedsProgramCd().equals(
					SpecialPlatesConstant.OWNER);

			boolean lbUserPltNoIndi =
				caPltTypeData.getUserPltNoIndi() == 1;

			String lsDefault = SpecialPlatesConstant.MANUFACTURE;

			if (lbOldPlt)
			{
				lsDefault = SpecialPlatesConstant.CUSTOMER_SUPPLIED;
			}
			else if (SystemProperty.isHQ() && !lbUserPltNoIndi)
			{
				lsDefault = SpecialPlatesConstant.ISSUE_FROM_INVENTORY;
			}

			boolean lbTrnsfr =
				caPltTypeData.getTrnsfrCd().equals(
					SpecialPlatesConstant.TRANSFERABLE)
					|| caPltTypeData.getTrnsfrCd().equals(
						SpecialPlatesConstant.LIMITED_TRANSFERABILITY);

			boolean lbLP =
				caPltTypeData.getNeedsProgramCd().equals(
					SpecialPlatesConstant.LP_PLATE);

			int liIndex = -1;
			// j: Number of Items in the combo box -1 
			int j = -1;

			getcomboRequestType().removeAllItems();

			for (int i = 0; i < svRequestType.size(); i++)
			{
				String lsRequestType =
					((String) svRequestType.elementAt(i)).trim();

				// only include  Plate Owner Change if transferable 
				boolean lbPltOwnrChng =
					lsRequestType.equals(
						SpecialPlatesConstant.PLATE_OWNER_CHANGE);

				boolean lbIssueInventory =
					lsRequestType.equals(
						SpecialPlatesConstant.ISSUE_FROM_INVENTORY);

				// "Reserved" also implies a manufacture request 
				boolean lbManufacture =
					lsRequestType.equals(
						SpecialPlatesConstant.MANUFACTURE)
						|| lsRequestType.equals(
							SpecialPlatesConstant.FROM_RESERVE);

				// Plate Ownership Change && Transferable
				// OR  Issue from Inventory && !UserPltNoIndi 
				// OR  !PltOwnrChange && !Issue from Inventory 
				//        && !(Mfg && (LP || OLDPLT) 
				boolean lbAdd =
					(lbTrnsfr && lbPltOwnrChng)
						|| (lbIssueInventory && !lbUserPltNoIndi)
						|| (!lbIssueInventory
							&& !lbPltOwnrChng
							&& !(lbManufacture && (lbLP || lbOldPlt)));

				if (lbAdd)
				{
					getcomboRequestType().addItem(lsRequestType);
					j = j + 1;
					if (lsRequestType.equals(lsDefault))
					{
						liIndex = j;
					}
				}
			}

			comboBoxHotKeyFix(getcomboRequestType());
			getcomboRequestType().setSelectedIndex(liIndex);
			csRequestType =
				(String) getcomboRequestType().getItemAt(liIndex);
			getcomboRequestType().addActionListener(this);
		}
	}
	/**
	 * Populate the Vehicle Class combo
	 * 
	 * @throws RTSException
	 */
	private void populateVehClassCd() throws RTSException
	{
		int liIndex = -1;
		getcomboVehicleClass().removeAllItems();
		getcomboVehicleClass().setSelectedIndex(liIndex);

		for (int i = 0; i < svVehicleClass.size(); i++)
		{
			String lsVehClassCd =
				((String) svVehicleClass.elementAt(i)).trim();
			getcomboVehicleClass().addItem(lsVehClassCd);
			if (csVehClassCd.equals(lsVehClassCd))
			{
				liIndex = i;
			}
		}
		if (cbFromMF)
		{
			if (liIndex != -1)
			{
				getcomboVehicleClass().setEnabled(false);
			}
			else
			{
				liIndex = svVehicleClass.indexOf(DEFAULT_VEH_CLASS_CD);
			}
		}
		getcomboVehicleClass().setSelectedIndex(liIndex);
		comboBoxHotKeyFix(getcomboVehicleClass());
		populatePlateType();
		// defect 9868 
		// Now handled in populatePlateType(); 
		// getchkBoxISA().setEnabled(false);
		// end defect 9868 
	}
	/**
	 *  Restore Color of Combo Boxes in case of prior error
	 */

	private void restoreComboColor()
	{
		getcomboPlateType().setForeground(Color.black);
		getcomboPlateType().setBackground(new Color(204, 204, 204));
		getcomboOrganizationName().setForeground(Color.black);
		getcomboOrganizationName().setBackground(
			new Color(204, 204, 204));
		getcomboRequestType().setForeground(Color.black);
		getcomboRequestType().setBackground(new Color(204, 204, 204));
		getcomboPersonalized0().setForeground(Color.black);
		getcomboPersonalized0().setBackground(new Color(204, 204, 204));
		getcomboPersonalized1().setForeground(Color.black);
		getcomboPersonalized1().setBackground(new Color(204, 204, 204));
		getcomboPersonalized2().setForeground(Color.black);
		getcomboPersonalized2().setBackground(new Color(204, 204, 204));
		getcomboPersonalized3().setForeground(Color.black);
		getcomboPersonalized3().setBackground(new Color(204, 204, 204));
		getcomboPersonalized4().setForeground(Color.black);
		getcomboPersonalized4().setBackground(new Color(204, 204, 204));
		getcomboPersonalized5().setForeground(Color.black);
		getcomboPersonalized5().setBackground(new Color(204, 204, 204));
		getcomboPersonalized6().setForeground(Color.black);
		getcomboPersonalized6().setBackground(new Color(204, 204, 204));
		getcomboPersonalized7().setForeground(Color.black);
		getcomboPersonalized7().setBackground(new Color(204, 204, 204));
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject 
	 */
	public void setData(Object aaDataObject)
	{
		// Initialize Screen 
		getstcLblPlateNo().setEnabled(false);
		gettxtPlateNo().setEnabled(false);
		gettxtPlateNo().setText("");

		try
		{
			getcomboVehicleClass().removeActionListener(this);
			getcomboVehicleClass().setSelectedIndex(-1);

			if (aaDataObject != null)
			{
				if (aaDataObject instanceof VehicleInquiryData)
				{
					VehicleInquiryData laVehInqData =
						(VehicleInquiryData) aaDataObject;

					MFVehicleData laMFVehData =
						laVehInqData.getMfVehicleData();

					if (laMFVehData != null)
					{
						// defect 9893					
						laMFVehData.setSpclPltRegisData(
							caSpclPltRegisData);

						if (laMFVehData.getOwnerData() == null)
						{
							caSpclPltRegisData.setOwnrData(
								new OwnerData());
							caSpclPltRegisData
								.getOwnrData()
								.setAddressData(
								new AddressData());
						}
						else
						{
							caSpclPltRegisData.setOwnrData(
								(OwnerData) UtilityMethods.copy(
									laMFVehData.getOwnerData()));

							caSpclPltRegisData.getOwnrData().setOwnrId(
								"");
							laMFVehData.assgnSpclPltRegisAddr();

							// defect 10372 
							RegistrationData laRegData =
								laMFVehData.getRegData();

							if (laRegData != null)
							{
								csOrigRegPltNo =
									laRegData.getRegPltNo();

								if (laRegData.getRecpntName() != null
									&& laRegData
										.getRecpntName()
										.trim()
										.length()
										!= 0)
								{
									caSpclPltRegisData
										.getOwnrData()
										.setName1(
										laRegData.getRecpntName());

								}
								if (laRegData.getRecpntEMail() != null
									&& laRegData.getRecpntEMail().length()
										> 0)
								{
									caSpclPltRegisData.setPltOwnrEMail(
										laRegData.getRecpntEMail());
								}
							}
							// end defect 9893

							//	RegistrationData laRegData =
							//		laMFVehData.getRegData();
							//	if (laRegData != null)
							//	{
							//		csOrigRegPltNo = laRegData.getRegPltNo();
							//	}
							// end defect 10372

							// Get VehicleClassCd
							VehicleData laVehicleData =
								laMFVehData.getVehicleData();

							if (laVehicleData != null)
							{
								csVehClassCd =
									laVehicleData.getVehClassCd();
								cbFromMF = true;
							}
						}
					}
				}
			}

			// If no vehicle specified, initialized to the default 
			else
			{
				csVehClassCd = DEFAULT_VEH_CLASS_CD;
			}
			// defect 9384
			buildPersonalizedComboVector();
			// end defect 9384 
			populateVehClassCd();
			getcomboVehicleClass().addActionListener(this);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
	}

	/**
	 * Set Data to DataObject for display on SPL002
	 */
	private VehicleInquiryData setDataToDataObject()
	{
		// Set Owner Data if from MF
		VehicleInquiryData laVehInqData = new VehicleInquiryData();
		MFVehicleData laMfVehData = new MFVehicleData();
		VehicleData laVehicleData = new VehicleData();
		RegistrationData laRegData = new RegistrationData();
		laVehInqData.setMfVehicleData(laMfVehData);
		// reset VIAlloc in case changed plate, request type 
		caSpclPltRegisData.setVIAllocData(null);
		laMfVehData.setSpclPltRegisData(caSpclPltRegisData);
		laMfVehData.setRegData(laRegData);
		laMfVehData.setVehicleData(laVehicleData);
		laVehicleData.setVehClassCd(csVehClassCd);

		// defect 9893
		//		if (caOwnerData == null)
		//		{
		//			caSpclPltRegisData.setOwnrData(new OwnerData());
		//			caSpclPltRegisData.getOwnrData().setOwnrAddr(
		//				new AddressData());
		//		}
		//		else
		//		{
		//			caSpclPltRegisData.setOwnrData(caOwnerData);
		//			caSpclPltRegisData.getOwnrData().setOwnrAddr(
		//				caOwnerData.getOwnrAddr());
		//			// Do not pass OwnerId  (SSN)
		//			caSpclPltRegisData.getOwnrData().setOwnrId("");
		//		}
		// end defect 9893

		caSpclPltRegisData.setRegPltNo(csRegPltNo);
		caSpclPltRegisData.setOrgNo(csOrgNo);
		caSpclPltRegisData.setRegPltCd(csRegPltCd);
		caSpclPltRegisData.setISAIndi(
			getchkBoxISA().isSelected() ? 1 : 0);
		if (SystemProperty.isCounty())
		{
			caSpclPltRegisData.setResComptCntyNo(
				SystemProperty.getOfficeIssuanceNo());
		}

		if (gettxtYear().isEnabled())
		{
			caSpclPltRegisData.setInvItmYr(
				Integer.parseInt(gettxtYear().getText()));
		}
		else
		{
			caSpclPltRegisData.setInvItmYr(0);
		}
		if (cbPersonalizedIndi)
		{
			caSpclPltRegisData.setMfgPltNo(csPersonalizedMfgPltNo);
		}
		else if (csRegPltNo != null && csRegPltNo.length() > 0)
		{
			caSpclPltRegisData.setMfgPltNo();
		}
		else
		{
			caSpclPltRegisData.setMfgPltNo("");
		}

		// Reset for in case return from SPL002 with changes

		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
		caSpclPltRegisData.setPltExpMo(0);
		caSpclPltRegisData.setPltExpYr(0);
		// end defect 9864 
		caSpclPltRegisData.setNoMonthsToCharge(0);
		caSpclPltRegisData.setPltOwnrPhoneNo("");
		caSpclPltRegisData.setPltOwnrDlrGDN("");

		// defect 10441
		if (caSpclPltRegisData.getPltValidityTerm() == 0)
		{
			caSpclPltRegisData.setPltValidityTerm(
				SpecialPlatesConstant.NON_VENDOR_PLT_TERM);
		}
		// end defect 10441  

		// defect 10372 
		// caSpclPltRegisData.setPltOwnrEMail("");
		// end defect 10372

		return laVehInqData;
	}

	/**
	 * Validate Combo Boxes 
	 * 
	 * @param aeRTSEx
	 */
	private void validateComboBoxes(RTSException aeRTSEx)
	{
		if (getcomboPlateType().getSelectedIndex() == -1)
		{
			aeRTSEx.addException(
				new RTSException(150),
				getcomboPlateType());
		}
		// Do not hightlight if not yet populated
		if (getcomboOrganizationName().getSelectedIndex() == -1
			&& getcomboOrganizationName().getItemCount() > 0)
		{

			aeRTSEx.addException(
				new RTSException(150),
				getcomboOrganizationName());
		}
		else
		{
			validateRequestType(aeRTSEx);
		}

		// Do not hightlight if not yet populated
		if (getcomboRequestType().getSelectedIndex() == -1
			&& getcomboRequestType().getItemCount() > 0)
		{
			aeRTSEx.addException(
				new RTSException(150),
				getcomboRequestType());

		}
	}

	/**
	 * Validate Entered Plate No
	 *  (vs. Personalized) 
	 * 
	 * @return boolean 
	 */

	private boolean validateEnteredPlateNo()
	{
		RTSException leRTSEx = new RTSException();

		boolean lbReturn = true;

		if (gettxtPlateNo().isEnabled())
		{
			csRegPltNo = gettxtPlateNo().getText().trim();
			if (csRegPltNo.length() != 0)
			{
				validateInvItmNo(leRTSEx, gettxtPlateNo());
			}
			else
			{
				leRTSEx.addException(
					new RTSException(150),
					gettxtPlateNo());
			}

			if (leRTSEx.isValidationError())
			{
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
				csRegPltNo = new String();
				lbReturn = false;
			}
		}
		return lbReturn;
	}

	/**
	 * Validate Inventory Item No against Plate No
	 * 
	 * @param aeRTSEx
	 */
	private void validateInvItmNo(
		RTSException aeRTSEx,
		Component aaComponent)
	{
		ValidateInventoryPattern laValidateInventoryPattern =
			new ValidateInventoryPattern();

		ProcessInventoryData laProcessInventoryData =
			new ProcessInventoryData();
		laProcessInventoryData.setItmCd(csRegPltCd);
		laProcessInventoryData.setInvQty(1);
		laProcessInventoryData.setInvItmNo(csRegPltNo);
		laProcessInventoryData.setInvItmYr(0);

		if (gettxtYear().isEnabled())
		{
			int liYear = Integer.parseInt(gettxtYear().getText());
			laProcessInventoryData.setInvItmYr(liYear);
		}
		// defect 9578 
		// Do not validate PLP plates @ client.  Server will validate, 
		// write error to RTS_SPCL_PLT_REJ_LOG and throw exception. 
		if (caPltTypeData.getUserPltNoIndi() == 0)
		{
			try
			{
				laValidateInventoryPattern.validateItmNoInput(
					laProcessInventoryData.convertToInvAlloctnUIData(
						laProcessInventoryData));
			}
			catch (RTSException aeRTSEx1)
			{
				aeRTSEx.addException(aeRTSEx1, aaComponent);

			}
		}
		// end defect 9578 
	}

	/**
	 * Validate Personalized Plate No 
	 * 
	 * @return boolean 
	 */
	private boolean validatePersonalizedPlateNo()
	{
		RTSException leRTSEx = new RTSException();
		boolean lbReturn = true;
		String lsNextChar = "";
		int liNumISA = 0;
		int liNumAlpha = 0;

		if ((csRegPltNo == null || csRegPltNo.length() == 0)
			&& cbPersonalizedIndi)
		{
			if (determinePersonalizedMfgPltNo())
			{
				// If No Alpha or Special Characters were entered
				// Add Exception
				if (csPersonalizedMfgPltNo.length() == 0)
				{
					leRTSEx.addException(
						new RTSException(150),
						getcomboPersonalized0());
				}
				else
				{
					for (int i = 0;
						i < csPersonalizedMfgPltNo.length();
						i++)
					{
						lsNextChar =
							csPersonalizedMfgPltNo.substring(i, i + 1);

						// defect 10571 
						// Implement PlateSymbolCache
						if (lsNextChar
							.equals(PlateSymbolCache.ISASYMBOL))
						{
							liNumISA = liNumISA + 1;
						}
						else if (
							!(lsNextChar.equals(" ")
								|| PlateSymbolCache.isPlateSymbol(
									lsNextChar)))
						{
							// end defect 10571 
							liNumAlpha = liNumAlpha + 1;
							csRegPltNo = csRegPltNo + lsNextChar;
						}
					}

					// If No AlphaNumerics were entered, throw exception
					if (liNumAlpha == 0)
					{
						leRTSEx.addException(
							new RTSException(150),
							getcomboPersonalized0());
					}
					if (liNumISA == 0 && getchkBoxISA().isSelected())
					{
						leRTSEx.addException(
							new RTSException(150),
							getcomboPersonalized0());
					}
				}

				// defect 9384 
				// Do not validate length for Radio Operator 
				if (!cbROP)
				{
					// Only validate if Request for Manufacture
					int liMaxLength =
						csRequestType.equals(
							SpecialPlatesConstant.MANUFACTURE)
							? SpecialPlatesConstant.MAX_PLP_PLTNO_LENGTH
							: SpecialPlatesConstant.MAX_PLTNO_LENGTH;

					if (csRegPltNo.length() > liMaxLength)
					{
						leRTSEx.addException(
							new RTSException(2014),
							getcomboPersonalized0());
					}

					// defect 10571
					int liByteCount = 0;

					for (int i = 0;
						i < csPersonalizedMfgPltNo.length();
						i++)
					{
						String lsChar =
							csPersonalizedMfgPltNo.substring(i, i + 1);

						PlateSymbolData laData =
							PlateSymbolCache.getPlateSymbolData(lsChar);

						int liAdd =
							laData != null
								? laData.getSymCharLngth()
								: 1;

						liByteCount = liByteCount + liAdd;
						// end defect 10571 
					}

					if (liByteCount > caPltTypeData.getMaxByteCount())
					{
						leRTSEx.addException(
							new RTSException(2014),
							getcomboPersonalized0());
					}
				}
				else
				{
					// Trim beginning/ending spaces from ROP
					csRegPltNo = csRegPltNo.trim();
					csPersonalizedMfgPltNo =
						csPersonalizedMfgPltNo.trim();

					// Throw error if spaces in middle
					if (csPersonalizedMfgPltNo.indexOf(" ") >= 0)
					{
						leRTSEx.addException(
							new RTSException(150),
							getcomboPersonalized0());
					}
				} // end defect 9384 

				validateInvItmNo(leRTSEx, getcomboPersonalized0());

				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					lbReturn = false;
					cbComboError = true;
				}
			}
			else
			{
				lbReturn = false;
			}
		}
		return lbReturn;
	}

	/**
	 * Validate Entered and Personalized Plate No
	 * 
	 * @return boolean 
	 */
	private boolean validatePlateNo()
	{
		return (
			validateEnteredPlateNo() && validatePersonalizedPlateNo());
	}
	
	/**
	 * Verify that PLPDV starts or ends with "DV" 
	 * 
	 * @param aeRTSEx
	 */
	private void validatePLPDV(RTSException aeRTSEx)
	{
		if (caPltTypeData.isPLPDV() && csPersonalizedMfgPltNo.trim().length() > 0)
		{
			String lsPltNum = csPersonalizedMfgPltNo.trim();
			
			if (cbISAIndi)
			{
				if (lsPltNum.startsWith(PlateSymbolCache.ISASYMBOL))
				{
					lsPltNum = lsPltNum.substring(1); 
				}
				
				if (lsPltNum.endsWith(PlateSymbolCache.ISASYMBOL))
				{
					lsPltNum =lsPltNum.substring(0,lsPltNum.length() - 1);  
				}
			}

			if (lsPltNum.length() != 0 && !(lsPltNum.startsWith(REQD_PLPDV_SUFFIX_PREFIX) 
					|| lsPltNum.endsWith(REQD_PLPDV_SUFFIX_PREFIX)))
			{
				int liPos = csPersonalizedMfgPltNo.indexOf(lsPltNum.substring(0,1));

				aeRTSEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_MUST_START_OR_END_WITH_DV),
						(JComboBox) cvPersonalizedComboVector.get(liPos));
			}
		}
	}
	
	/**
	 * Validate Request Date for Combination Plates
	 * 
	 * PLPCP, GOTEX2CP, PLPGOTXC 
	 * 
	 * @return boolean 
	 */
	private boolean validateRequestDateForCP()
	{
		RTSException leRTSEx = new RTSException();
		boolean lbReturn = true;
		if ((csRegPltCd.trim().equals(SpecialPlatesConstant.PLPCP)
			|| csRegPltCd.trim().equals(SpecialPlatesConstant.PLPGOTXC)
			|| csRegPltCd.trim().equals(SpecialPlatesConstant.GOTEX2CP))
			&& (!csRequestType
				.equals(SpecialPlatesConstant.PLATE_OWNER_CHANGE)
				&& !csRequestType.equals(
					SpecialPlatesConstant.CUSTOMER_SUPPLIED)))
		{
			RTSDate laRTSDate = new RTSDate();
			int liMonth = laRTSDate.getMonth();
			if (liMonth > SpecialPlatesConstant.MAX_CP_APPL_MO)
			{
				leRTSEx.addException(
					new RTSException(2015),
					getcomboPlateType());
			}

			// defect 9434
			// Error if specify same year when apply for new CP Plate 
			if (("" + laRTSDate.getYear())
				.equals(gettxtYear().getText().trim()))
			{
				leRTSEx.addException(
					new RTSException(150),
					gettxtYear());
			} // end defect 9434 

			if (leRTSEx.isValidationError())
			{
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
				csRegPltNo = new String();
				lbReturn = false;
				cbComboError = true;
			}
		}
		return lbReturn;
	}

	/**
	 * Validate Request Type 
	 * 
	 * @param aeRTSEx
	 */
	private void validateRequestType(RTSException aeRTSEx)
	{
		if ((csRequestType.equals(SpecialPlatesConstant.MANUFACTURE)
			|| csRequestType.equals(SpecialPlatesConstant.FROM_RESERVE)
			|| csRequestType.equals(
				SpecialPlatesConstant.ISSUE_FROM_INVENTORY)))
		{
			if (OrganizationNumberCache
				.isSunsetted(csRegPltCd, csOrgNo))
			{
				aeRTSEx.addException(
					new RTSException(2016),
					getcomboOrganizationName());
			}
			// defect 10704
			else if (OrganizationNumberCache
				.isCrossedOver(csRegPltCd, csOrgNo))
			{
				aeRTSEx.addException(
					new RTSException(2023),
					getcomboOrganizationName());
			}
			// end defect 10704				
		}
	}

	/**
	 * Validate Year
	 * 
	 * @return boolean 
	 */
	private boolean validateYear()
	{
		RTSException leRTSEx = new RTSException();
		boolean lbReturn = true;
		if (gettxtYear().isEnabled())
		{
			try
			{
				int liYear = Integer.parseInt(gettxtYear().getText());
				int liCurrentYear = new RTSDate().getYear();
				int liCurrentMonth = new RTSDate().getMonth();
				int liExpMonth =
					(liCurrentMonth == 1 ? 12 : liCurrentMonth - 1);
				SpecialPlateFixedExpirationMonthData laSpclPltFxdExpMoData =
					SpecialPlateFixedExpirationMonthCache.getRegPltCd(
						csRegPltCd);

				// Fxd Exp Mo 
				if (laSpclPltFxdExpMoData != null)
				{
					liExpMonth = laSpclPltFxdExpMoData.getFxdExpMo();
				}

				// Assess No Months 
				int liNoMonths =
					(liExpMonth + liYear * 12)
						- (liCurrentMonth + liCurrentYear * 12)
						+ 1;
				if (liNoMonths < 1 || liNoMonths > 15)
				{
					lbReturn = false;
					// Display if not production 
					if (SystemProperty.getProdStatus()
						!= SystemProperty.APP_PROD_STATUS)
					{
						System.out.println(
							"Number of Months = " + liNoMonths);
					}
				}
			}
			catch (NumberFormatException aeNFEx)
			{
				lbReturn = false;
				leRTSEx.addException(
					new RTSException(150),
					gettxtYear());
			}
			if (!lbReturn)
			{
				leRTSEx.addException(
					new RTSException(150),
					gettxtYear());
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
				csRegPltNo = new String();
			}
		}
		return lbReturn;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
