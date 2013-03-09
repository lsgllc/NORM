package com.txdot.isd.rts.client.title.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.DocumentTypesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import java.awt.Rectangle;
import javax.swing.JCheckBox;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.awt.Point;

/*
 *
 * FrmCCOTTL018.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 *				06/26/2001	Created
 * M Rajangam				validations update
 * M Rajangam	04/11/2002	Adding javadoc, comments
 * MAbs			06/26/2002	Deployment Error 300300 Change 0.00 to -0.01
 * MAbs			06/26/2002	Allows for blanks
 * S Govindappa 07/17/2002	Fixed defect CQU100004464. Made changes to 
 * 							setData method not to change the fee when 
 * 							VTR authorization code is entered.
 *							Also moved the code for fees validation 
 *							above the code for VTR Authorozation in 
 *							actionPerformed method.
 * Min Wang		04/24/2003	Modified windowOpened(). Defect 5701
 * K Harrell    05/14/2003  Defect 6093 Removed setting of CCOIssueDate
 *  B Arredondo             modified actionPerformed()
 * K Harrell	12/30/2003	Increased field size for VIN.
 *							modified in Visual Composition
 *							Defect 6611.  Version 5.1.5 Fix 2.
 * J Rue		02/17/2005	VAJ to WSAD Clean Up
 *							defect 7898 Ver 5.2.3
 * J Rue		02/22/2005	Change AbstractViewController to
 * 							getController().
 * 							Replace printStackTrace() w/ displayError()
 * 							modify actionPerformed(),
 * 							doCompleteTransaction()
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/07/2005	Remove setNextFocusable and unused variables
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/12/2005	Set RequestFocusEnabled to false to 
 * 							ensure cursor does not tab to lienholder
 * 							info
 * 							modify gettxtALienHldr1Addr(), getJPanel1()
 * 							defect 7898 Ver 5.2.3
 * S Johnston	06/21/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify keyPressed
 * 							defect 8240 Ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3
 * T Pederson	10/31/2005	Comment out keyPressed() and keyReleased()  
 * 							methods entirely. 
 * 							Code already handled in RTSDialogBox.
 * 							defect 7898 Ver 5.2.3
 * J Rue		11/04/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	12/12/2005	Manage cursor/focus
 * 							add ciNumIndis
 * 							deprecate getNoOfIndicators() 
 * 							modify focusLost(),focusGained(),setData(),
 * 							getIndicators()  
 * 							defect 7898 Ver 5.2.3    
 * T Pederson	12/21/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * K Harrell	06/15/2006	"Owner Name" label should be in black.
 * 							Lienholder Data should be in bold.
 * 							Minimal alignment cleanup. 
 * 							modify getstcLblOwnerName(),
 * 							 gettxtALienHldr1Addr()
 * 							defect 8780 Ver 5.2.3 
 * T Pederson	08/04/2006	Moved setting default focus to 
 * 							windowActivated.
 * 							add windowActivated()
 * 							modify setData() 	
 * 							defect 8846 Ver 5.2.4
 * K Harrell	08/14/2006	Do not set color for OwnerTitleName2
 * 							modify getlblOwnerTitleName2()
 * 							defect 8885 Ver 5.2.4
 * K Harrell	10/13/2006	Do not present "0" for Exp Mo / Exp Yr.
 * 							Delete deprecated methods. 
 * 							delete getNoOfIndicators(), getBuilderData()
 * 							modify setData()
 * 							defect 8900 Ver Exempts  
 * K Harrell	04/22/2007	Use SystemProperty.isRegion()
 * 							delete getOfcIssuanceCd()
 * 							modify actionPerformed(),doCompleteTransaction()
 * 							defect 9085 Ver Special Plates 
 * Min Wang		05/15/2008	Fix no beep with invalid fee message
 * 							modify actionPerformed()
 * 							defect 8774 Ver Defect_POS_A
 * K Harrell	09/17/2008	Use VCCCOCCDOScreenTTL018 constant vs. 
 * 							  VCCOATTL015 constant for VTR_AUTHORIZATION
 * 							modify actionPerformed()
 * 							defect 9810 Ver Defect_POS_B
 * K Harrell	02/26/2009	Modify screen to increase size of Remarks 
 * 							panel via Visual Composition. Use 
 * 							CommonConstant.FONT_JLIST in Remarks Listing.
 * 							add MAX_INDI_NO_SCROLL
 * 							modify getlstIndiDescription1() 
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	03/07/2009	Enhancements to creation of LienData
 * 							Use DocTypeConstant. 
 * 							add LENGTH_FEE, LENGTH_DATE_DISPLAY,
 * 							  EMPTY, DAYS_IN_2_WEEKS
 * 							add buildDateLine(), getComplTransData()
 * 							delete doCompleteTransaction()  
 * 							delete FEE_CHRG_VALUE 
 * 							modify setData(), actionPerformed(), 	
 * 							  windowActivated()  
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	04/10/2009	Enhance for corrupt records: LienDate = 0, 
 * 							Invalid PermLienhldrId 
 * 							modify setData(), buildDateLine()  
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	07/01/2009	Implement new LienholderData, methods
 * 							modify setData() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/04/2009	refactored from FrmCCOCCDOScreenTTL018
 *							CCDO discontinued via RTB 007-02, 1/14/2002,  
 *  						implemented in RTS 4.3
 * 							Updated to use CommonConstant.MAX_INDI_NO_SCROLL 
 * 							deleted MAX_INDI_NO_SCROLL
 * 							modify windowActivated() 
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	08/30/2009	Append Date/Id to Lienholder Address
 * 							modify setData()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	10/18/2010	Do not allow CCO when TtlProcsCd is 
 * 							populated
 * 							modify actionPerformed() 
 * 							defect 10624 Ver 6.6.0 
 * K Harrell	10/22/2010	delete DAYS_IN_2_WEEKS
 * 							modify verifyCCOIssueDate() 
 * 							defect 10639 Ver 6.6.0
 * K Harrell/	06/08/2011  Flag Fraud for CCOs
 * B Woodson 				add jpnlGrpFraud, jchkFraudIdentification,
 * 						     jchkFraudReleaseOfLien,
 * 							 jchkFraudPowerOfAttorney,
 * 							 jchkFraudLetterOfAuthorization,
 * 							 caFraudBox, get methods.
 * 							add caFraudTypesRadioGroup, cbAlreadySet, 
 * 							 ciFraudChangedState, ciCCONotAvailableErrNo,
 *                           caInitFraudData, caNewFraudData 
 * 							add getFraudDataSnapShot(),
 * 							 handleFraudChange()
 * 							add LETTER_OF_AUTHORIZATION,
 * 							 POWER_OF_ATTORNEY, RELEASE_OF_LIEN,
 * 							 IDENTIFICATION, SUSPECTED_FRAUD,
 * 							 TXT_CNFRM_CHNG_PREFIX,
 * 							 TXT_CNFRM_CHNG_SUFFIX 
 * 							modify actionPerformed(), initialize(),
 * 							 setData() 
 * 							defect 10865 Ver 6.8.0 
 * K Harrell	01/13/2011	Add screen changes, logic for Mail In and 
 * 							No Charge. Sorted methods.  
 * 							add implements ItemListener, Key Listener 
 * 							add ivjchkMailIn, ivjchkNoCharge,ivjJPanel4, 
 * 							 ivjJPanel5, get methods
 * 							add itemStateChanged(), keyPressed(), getAcctItmCd(),
 * 							  enableFees() 
 * 							add CCO_NO_CHRG, CCO_NO_CHRG_MAIL,
 * 							  CCO_MAIL
 * 							modify actionPerformed(), getchkFraudReleaseOfLien(), 
 * 							 getFraudIdentification(), getFraudPowerOfAttorney(),
 * 							 getFraudLetterOfAuthorization(),getComplTransData(),
 * 							 getstcLblFeeToCharge(),main()()  
 * 							modify DEFAULT_CCO_FEE, FEE_TO_CHARGE 
 * 							defect 11230 Ver 6.10.0 
 * K Harrell	03/01/2012	ESC not working if Fraud Indicator set 
 * 							modify windowActivated() 
 * 							defect 11299 Ver 6.10.0 
 * Min Wang		06/14/2012	Show ownername field to be white on red 
 * 							like other displays of owner name. 
 * 							modify getlblOwnerTitleName1(), 
 * 							getlblOwnerTitleName2()
 * 							defect 11373 Ver 7.0.0
 * Min Wang		06/28/2012	Use the default foreground color which is black.
 *							modify getlblOwnerTitleName2()
 * 							defect 11399 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
 * CCO Screen. Displays vehicle information.
 * 
 * @version 7.0.0	06/28/2012
 * @author Todd Pederson
 * @author Buck Woodson
 * @author Kathy Harrell 
 * @since 			06/26/2001 16:40:04
 */

public class FrmCCOTTL018 extends RTSDialogBox implements
		ActionListener
		// defect 11230
		, ItemListener
		// end defect 11230 
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmCCOCCDOScreenTTL018ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblDocNo = null;
	private JLabel ivjlblOwnerTitleName1 = null;
	private JLabel ivjlblOwnerTitleName2 = null;
	private JLabel ivjlblPlateNo = null;
	private JLabel ivjlblRegExpiresMo = null;
	private JLabel ivjlblRegExpiresYr = null;
	private JLabel ivjlblTitleIssueDate = null;
	private JLabel ivjlblVehicleMake = null;
	private JLabel ivjlblVehicleYear = null;
	private JLabel ivjlblVIN = null;
	private JList ivjlstIndiDescription1 = null;
	private JLabel ivjstcLblDocNo = null;
	private JLabel ivjstcLblExpires = null;
	private JLabel ivjstcLblFeeToCharge = null;
	private JLabel ivjstcLblissued = null;
	private JLabel ivjstcLblNontitledVehicle = null;
	private JLabel ivjstcLblOwnerName = null;
	private JLabel ivjstcLblPlate = null;
	private JLabel ivjstcLblSlash = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblYearMake = null;
	private JTextArea ivjtxtLienHldr1Addr = null; 
	private RTSInputField ivjtxtFeeToChargeValue = null;
	
	// defect 11230
	private JCheckBox ivjchkNoCharge = null;
	private JCheckBox ivjchkMailIn = null;
	private JPanel ivjJPanel4 = null;
	private JPanel ivjJPanel5 = null;
	// end defect 11230
	
	// defect 10865
	private JPanel jpnlGrpFraud = null;
	private JCheckBox jchkFraudIdentification = null;
	private JCheckBox jchkFraudReleaseOfLien = null;
	private JCheckBox jchkFraudPowerOfAttorney = null;
	private JCheckBox jchkFraudLetterOfAuthorization = null;
	private Box caFraudBox;
	private RTSButtonGroup caFraudTypesRadioGroup;
	private boolean cbAlreadySet = false;
	private int ciFraudChangedState = FraudStateData.FRAUD_UNCHANGED;
	private int ciCCONotAvailableErrNo = 0;
	private FraudStateData caInitFraudData = new FraudStateData();
	private FraudStateData caNewFraudData = new FraudStateData();
	// end defect 10865
	
	private int ciNumIndis = 0;

	/**
	 * Dollar value. Used for initialization.
	 */
	private Dollar caFee = new Dollar(CommonConstant.STR_ZERO_DOLLAR);

	/**
	 * Vehicle inquiry data object for the class
	 */
	private VehicleInquiryData caVehicleInquiryData = new VehicleInquiryData();

	
	// Constants
	
	// defect 10865
	private static final String LETTER_OF_AUTHORIZATION = "Letter of Authorization";
	private static final String POWER_OF_ATTORNEY = "Power of Attorney";
	private static final String RELEASE_OF_LIEN = "Release of Lien";
	private static final String IDENTIFICATION = "Identification            ";
	private static final String SUSPECTED_FRAUD = "Suspected Fraud:";
	private static final String TXT_CNFRM_CHNG_PREFIX = "Are you sure you want to ";
	private static final String TXT_CNFRM_CHNG_SUFFIX = " fraud indicator(s)?";
	// end defect 10865

	// defect 11230
	private final static String CCO_ACCTITMCD = "CCO-R";
	private final static String CCO_NO_CHRG = "CCONC-R";
	private final static String CCO_NO_CHRG_MAIL = "CCOMNC-R";
	private final static String CCO_MAIL = "CCOM-R";
	// private final static String DEFAULT_CCO_FEE = "5.45";
	private final static String DEFAULT_CCO_FEE = new String();
	//private final static String FEE_TO_CHARGE = " Fee to charge:";
	private final static String FEE_TO_CHARGE = " Fee to Charge:";
	// end defect 11230
	
	private final static String DOC_NO = "Doc No:";
	private final static String EMPTY = CommonConstant.STR_SPACE_EMPTY;
	private final static String EXPIRES = "Expires:";
	private final static String FIRST_LIEN_DATE = "1st LIEN DATE: ";
	private final static String ISSUED = "Issued: ";
	private final static String MAX_CCO_FEE = "99.99";
	private final static String MIN_CCO_FEE = "0.00";
	private final static String NEXT_LINE = CommonConstant.SYSTEM_LINE_SEPARATOR;
	private final static String OWNER_NAME = "Owner Name:";
	private final static String PLATE = "Plate: ";
	private final static String UNKNOWN = "UNKNOWN";
	private final static String VIN = " VIN:";
	private final static String YEAR_MAKE = " Year/Make:";
	// defect 9971
	private final static int LENGTH_FEE = 5;
	private final static int LENGTH_DATE_DISPLAY = 30;
	// end defect 9971
	
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs
	 *            String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			// defect 11230 
			// Refactored laFrmCCOCCDOScreenTTL018 to laFrmCCOScreenTTL018
			FrmCCOTTL018 laFrmCCOScreenTTL018;
			laFrmCCOScreenTTL018 = new FrmCCOTTL018();
			laFrmCCOScreenTTL018.setModal(true);
			laFrmCCOScreenTTL018
					.addWindowListener(new WindowAdapter()
					{
						public void windowClosing(WindowEvent aeWE)
						{
							System.exit(0);
						};
					});
			laFrmCCOScreenTTL018.show();
			Insets insets = laFrmCCOScreenTTL018.getInsets();
			laFrmCCOScreenTTL018.setSize(laFrmCCOScreenTTL018
					.getWidth()
					+ insets.left + insets.right,
					laFrmCCOScreenTTL018.getHeight() + insets.top
							+ insets.bottom);
			laFrmCCOScreenTTL018.setVisibleRTS(true);
			// end defect 11230 
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmCCOCCDOScreenTTL018 constructor
	 */
	public FrmCCOTTL018()
	{
		super();
		initialize();
	}

	/**
	 * FrmCCOCCDOScreenTTL018 constructor
	 * 
	 * @param aaParent
	 *            JFrame
	 */
	public FrmCCOTTL018(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmCCOCCDOScreenTTL018 constructor
	 * 
	 * @param aaParent
	 *            JFrame
	 */
	public FrmCCOTTL018(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE
	 *            ActionEvent Action event thrown by the window
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}

		RTSException leRTSEx = new RTSException();
		try
		{
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{

				if (ciCCONotAvailableErrNo != 0)
				{
					displayError(ciCCONotAvailableErrNo);
					getController().processData(
							AbstractViewController.CANCEL, null);
					return;
				}

				// defect 10865
				caNewFraudData = getFraudDataSnapshot();

				boolean lbFraudAfterUpdate = caNewFraudData
						.isAnyFraud();

				if (!lbFraudAfterUpdate)
				{
					// set the FeeToCharge value if visible
					if (gettxtFeeToChargeValue().isVisible()
							&& gettxtFeeToChargeValue().isEditable())
					{
						if (gettxtFeeToChargeValue().getText().equals(
								EMPTY))
						{
							gettxtFeeToChargeValue().setText(
									CommonConstant.STR_ZERO_DOLLAR);
						}

						caFee = new Dollar(gettxtFeeToChargeValue()
								.getText().trim());

						Dollar laZeroDollar = new Dollar(MIN_CCO_FEE);
						Dollar laMaxFee = new Dollar(MAX_CCO_FEE);

						// if value outside range, add Exception
						if (caFee.compareTo(laZeroDollar) == -1
								|| caFee.compareTo(laMaxFee) == 1)
						{
							leRTSEx
									.addException(
											new RTSException(
													RTSException.FAILURE_MESSAGE,
													ErrorsConstant.ERR_MSG_FEE_OUTSIDE_CCO_RANGE,
													ErrorsConstant.ERR_MSG_INVALID_FEE_TITLE),
											gettxtFeeToChargeValue());
						}
					}
					// defect 11230 
					if (caFee.compareTo(new Dollar(MIN_CCO_FEE)) == 0
							&& getchkNoCharge().isEnabled() && 
							!getchkNoCharge().isSelected())
					{
						leRTSEx.addException(new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
								gettxtFeeToChargeValue());
						leRTSEx.addException(new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
								getchkNoCharge());
					}
					// end defect 11230 

					if (leRTSEx.isValidationError())
					{
						leRTSEx.setBeep(RTSException.BEEP);
						leRTSEx.displayError(this);
						leRTSEx.getFirstComponent().requestFocus();
						return;
					}

					// Check for hard stop and process hard stop
				if (hasHardStop())
					{
						VehMiscData laVehMiscData = caVehicleInquiryData
								.getVehMiscData();
						if (laVehMiscData == null
								|| UtilityMethods.isEmpty(laVehMiscData
										.getAuthCd()))
						{
							getController().processData(
									VCCCOTTL018.VTR_AUTHORIZATION,
									caVehicleInquiryData);

							// check if 'Enter' is selected in VTR
							// authorization
							if (laVehMiscData == null
									|| UtilityMethods
											.isEmpty(laVehMiscData
													.getAuthCd()))
							{
								return;
							}
						}
					}

				}

				if (caInitFraudData.isAnyFraud() || lbFraudAfterUpdate)
				{
					// Prompt user to verify they want to change fraud
					ciFraudChangedState = caInitFraudData
							.compareTo(caNewFraudData);

					if (ciFraudChangedState != FraudStateData.FRAUD_UNCHANGED)
					{
						Vector lvFraud = handleFraudChange();

						String lsTxt = "modify";
						if (ciFraudChangedState == FraudStateData.FRAUD_ADDED
								|| ciFraudChangedState == FraudStateData.FRAUD_REMOVED)
						{
							lsTxt = ciFraudChangedState == FraudStateData.FRAUD_ADDED ? "add"
									: "remove";
						}
						lsTxt = TXT_CNFRM_CHNG_PREFIX + lsTxt
								+ TXT_CNFRM_CHNG_SUFFIX;

						leRTSEx = new RTSException(RTSException.CTL001,
								lsTxt, null);

						int liRetCode = leRTSEx.displayError(this);

						if (liRetCode == RTSException.YES)
						{
							getController()
									.processData(
											TitleConstant.FRAUDCD_MGMT,
											lvFraud);
						}
						else if (liRetCode == RTSException.NO)
						{
							return;
						}
					}
				}

				if (lbFraudAfterUpdate)
				{
					getController().processData(
							AbstractViewController.CANCEL, null);

				}
				else
				{
					caInitFraudData = (FraudStateData) UtilityMethods
							.copy(caNewFraudData);
					// end defect 10865

					// defect 9971
					// Use copy of object when moving from screen
					VehicleInquiryData laNewVehInqData = (VehicleInquiryData) UtilityMethods
							.copy(caVehicleInquiryData);

					TitleData laNewTitleData = laNewVehInqData
							.getMfVehicleData().getTitleData();

					// Assign here vs. Transaction class
					laNewTitleData.setCcoIssueDate(new RTSDate()
							.getYYYYMMDDDate());

					// defect 9971
					// Set to default ETtlCd per DocTypeCd
					laNewTitleData.setETtlCd(DocumentTypesCache
							.getDefaultETtlCd(laNewTitleData
									.getDocTypeCd()));
					// end defect 9971

					// Region uses CompleteTransactionData
					if (SystemProperty.isRegion())
					{
						getController().processData(
								AbstractViewController.ENTER,
								getComplTransData(laNewVehInqData));
					}
					else
					{
						getController().processData(
								AbstractViewController.ENTER,
								laNewVehInqData);
					}
					// end defect 9971
				}
			}
			else if (aaAE.getSource() == getButtonPanel1()
					.getBtnCancel())
			{
				getController().processData(
						AbstractViewController.CANCEL, null);
			}
			else if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL018);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError();
		}

		finally
		{
			doneWorking();
		}
	}

	/**
	 * Build Date Line
	 * 
	 * @param asString
	 * @param asPermLienId
	 * @return String
	 */
	private String buildDateLine(String asString, String asPermLienId)
	{
		return (UtilityMethods.addPaddingRight(asString,
				LENGTH_DATE_DISPLAY, " ") + TitleClientUtilityMethods
				.getPermLienhldrLabel(asPermLienId));
	}

	/**
	 * Displays the error message
	 * 
	 * @param aiCode
	 *            int
	 */
	private void displayError(int aiCode)
	{
		RTSException leRTSEx = new RTSException(aiCode);
		leRTSEx.displayError(this);
	}

	/**
	 * Enable objects associated with Fees 
	 * 
	 *  @param abEnable 
	 */
	private void enableFees(boolean abEnable)
	{
		abEnable = abEnable && !SystemProperty.isHQ();
		getJPanel4().setEnabled(abEnable); 
		getchkMailIn().setEnabled(abEnable); 
		getchkNoCharge().setEnabled(abEnable); 
		getstcLblFeeToCharge().setEnabled(abEnable); 
		gettxtFeeToChargeValue().setEnabled(abEnable);
		if (!abEnable)
		{
			getchkMailIn().setSelected(abEnable); 
			getchkNoCharge().setSelected(abEnable); 
			gettxtFeeToChargeValue().setText(new String());
		}
	}

	/**
	 * Return the appropriate AcctItmCd for CCO 
	 * 
	 * @return String 
	 */
	private String getAcctItmCd() 
	{
		String lsAcctItmCd = CCO_ACCTITMCD; 
		if (getchkNoCharge().isSelected())
		{
			lsAcctItmCd = getchkMailIn().isSelected()? CCO_NO_CHRG_MAIL : CCO_NO_CHRG; 
		}
		else if (getchkMailIn().isSelected())
		{
			lsAcctItmCd = CCO_MAIL; 
		}
		return lsAcctItmCd; 
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(140, 436, 311, 37);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
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
	 * This method initializes jchkFraudIdentification
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkFraudIdentification()
	{
		if (jchkFraudIdentification == null)
		{
			jchkFraudIdentification = new JCheckBox(IDENTIFICATION,
					new RedCheckBoxIcon());
			jchkFraudIdentification
					.addActionListener((ActionListener) jchkFraudIdentification
							.getIcon());
			jchkFraudIdentification.setMnemonic(KeyEvent.VK_I);
			// defect 11230
			jchkFraudIdentification.addItemListener(this);
			// end defect 11230
		}
		return jchkFraudIdentification;
	}

	/**
	 * This method initializes jchkFraudLetterOfAuthorization
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkFraudLetterOfAuthorization()
	{
		if (jchkFraudLetterOfAuthorization == null)
		{
			jchkFraudLetterOfAuthorization = new JCheckBox(
					LETTER_OF_AUTHORIZATION, new RedCheckBoxIcon());
			jchkFraudLetterOfAuthorization
					.addActionListener((ActionListener) jchkFraudLetterOfAuthorization
							.getIcon());
			jchkFraudLetterOfAuthorization.setMnemonic(KeyEvent.VK_L);
			// defect 11230 
			jchkFraudLetterOfAuthorization.addItemListener(this);
			// end defect 11230
		}
		return jchkFraudLetterOfAuthorization;
	}

	/**
	 * This method initializes jchkFraudPowerOfAttorney
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkFraudPowerOfAttorney()
	{
		if (jchkFraudPowerOfAttorney == null)
		{
			jchkFraudPowerOfAttorney = new JCheckBox(POWER_OF_ATTORNEY,
					new RedCheckBoxIcon());
			jchkFraudPowerOfAttorney
					.addActionListener((ActionListener) jchkFraudPowerOfAttorney
							.getIcon());
			jchkFraudPowerOfAttorney.setMnemonic(KeyEvent.VK_P);
			// defect 11230 
			jchkFraudPowerOfAttorney.addItemListener(this);
			// end defect 11230 
		}
		return jchkFraudPowerOfAttorney;
	}

	/**
	 * This method initializes jchkFraudReleaseOfLien
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkFraudReleaseOfLien()
	{
		if (jchkFraudReleaseOfLien == null)
		{
			jchkFraudReleaseOfLien = new JCheckBox(RELEASE_OF_LIEN,
					new RedCheckBoxIcon());
			jchkFraudReleaseOfLien
					.addActionListener((ActionListener) jchkFraudReleaseOfLien
							.getIcon());
			jchkFraudReleaseOfLien.setMnemonic(KeyEvent.VK_R);
			// defect 11230 
			jchkFraudReleaseOfLien.addItemListener(this);
			// end defect 11230 
		}
		return jchkFraudReleaseOfLien;
	}

	/**
	 * This method initializes ivjchkMailIn
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkMailIn()
	{
		if (ivjchkMailIn == null)
		{
			ivjchkMailIn = new JCheckBox();
			ivjchkMailIn.setMnemonic(KeyEvent.VK_M);
			ivjchkMailIn.setBounds(new Rectangle(6, 6, 76, 21));
			ivjchkMailIn.setText("Mail-In");
		}
		return ivjchkMailIn;
	}

	/**
	 * This method initializes ivjchkNoCharge
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkNoCharge()
	{
		if (ivjchkNoCharge == null)
		{
			ivjchkNoCharge = new JCheckBox();
			ivjchkNoCharge.setMnemonic(KeyEvent.VK_N);
			ivjchkNoCharge.setBounds(new Rectangle(6, 33, 92, 21));
			ivjchkNoCharge.setText("No Charge");
			ivjchkNoCharge.addItemListener(this);
		}
		return ivjchkNoCharge;
	}

	/**
	 * Completes the transaction. Uses the Title Transaction data object to
	 * complete the transcation. Operates on the class variable.
	 * 
	 * 
	 * @return CompleteTransactionData
	 * @throws RTSException
	 */
	private CompleteTransactionData getComplTransData(
			VehicleInquiryData aaVehInqData) throws RTSException
	{
		CompleteTitleTransaction laTtlTrans = new CompleteTitleTransaction(
				aaVehInqData, getController().getTransCode());

		CompleteTransactionData laCompTransData = laTtlTrans
				.doCompleteTransaction();

		// Set the fee
		if (laCompTransData != null)
		{
			FeesData laFeesData = new FeesData();
			laFeesData.setItemPrice(caFee);
			
			// defect 11230 
			//laFeesData.setAcctItmCd(CCO_ACCTITMCD);
			laFeesData.setAcctItmCd(getAcctItmCd()); 
			laFeesData.setItmQty(1);

			//	AccountCodesData laAcctData = AccountCodesCache.getAcctCd(
			//			CCO_ACCTITMCD, new RTSDate().getYYYYMMDDDate());
			AccountCodesData laAcctData = AccountCodesCache.getAcctCd(
					laFeesData.getAcctItmCd(), new RTSDate().getYYYYMMDDDate());
			// end defect 11230 
			
			if (laAcctData != null)
			{
				laFeesData.setDesc(laAcctData.getAcctItmCdDesc());
			}

			RegFeesData laRegFeesData = laCompTransData
					.getRegFeesData();

			laRegFeesData.getVectFees().add(laFeesData);

			if (aaVehInqData.getMfVehicleData().getRegData() != null)
			{
				laRegFeesData.setToMonthDflt(aaVehInqData
						.getMfVehicleData().getRegData().getRegExpMo());

				laRegFeesData.setToYearDflt(aaVehInqData
						.getMfVehicleData().getRegData().getRegExpYr());
			}
		}
		return laCompTransData;
	}

	/**
	 * 
	 * builds and returns the Box that contains the fraud checkboxes
	 * 
	 * @return Box
	 */
	private Box getFraudBox()
	{
		if (caFraudBox == null)
		{
			caFraudBox = Box.createVerticalBox();
			caFraudBox.add(getchkFraudIdentification(), null);
			caFraudBox.add(Box.createVerticalStrut(5));
			caFraudBox.add(getchkFraudReleaseOfLien(), null);
			caFraudBox.add(Box.createVerticalStrut(5));
			caFraudBox.add(getchkFraudPowerOfAttorney(), null);
			caFraudBox.add(Box.createVerticalStrut(5));
			caFraudBox.add(getchkFraudLetterOfAuthorization(), null);
		}
		return caFraudBox;
	}

	/**
	 * Captures the currently selected fraud state
	 * 
	 * @return FraudStateData
	 */
	private FraudStateData getFraudDataSnapshot()
	{
		return new FraudStateData(getchkFraudIdentification()
				.isSelected(), getchkFraudReleaseOfLien().isSelected(),
				getchkFraudPowerOfAttorney().isSelected(),
				getchkFraudLetterOfAuthorization().isSelected());
	}

	/**
	 * Return the FrmCCOCCDOScreenTTL018ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmCCOCCDOScreenTTL018ContentPane1()
	{
		if (ivjFrmCCOCCDOScreenTTL018ContentPane1 == null)
		{
			try
			{
				ivjFrmCCOCCDOScreenTTL018ContentPane1 = new JPanel();
				ivjFrmCCOCCDOScreenTTL018ContentPane1
						.setName("FrmCCOCCDOScreenTTL018ContentPane1");
				ivjFrmCCOCCDOScreenTTL018ContentPane1
						.setAlignmentX(1.0F);
				ivjFrmCCOCCDOScreenTTL018ContentPane1.setLayout(null);
				ivjFrmCCOCCDOScreenTTL018ContentPane1
						.setMaximumSize(new java.awt.Dimension(
								2147483647, 2147483647));
				ivjFrmCCOCCDOScreenTTL018ContentPane1
						.setMinimumSize(new java.awt.Dimension(796, 501));
				ivjFrmCCOCCDOScreenTTL018ContentPane1.setBounds(0, 0,
						0, 0);
				getFrmCCOCCDOScreenTTL018ContentPane1().add(
						getJPanel3(), getJPanel3().getName());
				getFrmCCOCCDOScreenTTL018ContentPane1().add(
						getJPanel2(), getJPanel2().getName());
				getFrmCCOCCDOScreenTTL018ContentPane1().add(
						getJScrollPane1(), getJScrollPane1().getName());
				getFrmCCOCCDOScreenTTL018ContentPane1().add(
						getButtonPanel1(), getButtonPanel1().getName());
				getFrmCCOCCDOScreenTTL018ContentPane1().add(
						getJPanel1(), getJPanel1().getName());
				ivjFrmCCOCCDOScreenTTL018ContentPane1.add(
						getJpnlGrpFraud(), null);
				ivjFrmCCOCCDOScreenTTL018ContentPane1
						.setForeground(java.awt.Color.red);
				ivjFrmCCOCCDOScreenTTL018ContentPane1.add(getJPanel4(),
						null);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjFrmCCOCCDOScreenTTL018ContentPane1;
	}

	/**
	 * Get indicators and build string to display indicators in text area.
	 */
	private void getIndicators()
	{
		// get the indicators from MF Vehicle data and add them one by
		// one to the
		// Jlist -> lstIndiDescription

		MFVehicleData laMFVehicleData = caVehicleInquiryData
				.getMfVehicleData();
		Vector lvIndis = IndicatorLookup.getIndicators(laMFVehicleData,
				getController().getTransCode(), IndicatorLookup.SCREEN);
		StringBuffer lsIndis = new StringBuffer(EMPTY);

		// get the number of indicators
		ciNumIndis = lvIndis.size();
		if (ciNumIndis > 0)
		{
			Vector lvRows = new Vector();
			for (int liIndex = 0; liIndex < ciNumIndis; liIndex++)
			{
				IndicatorData laIndicatorData = (IndicatorData) lvIndis
						.get(liIndex);
				lsIndis
						.append(laIndicatorData.getStopCode() == null ? CommonConstant.STR_SPACE_ONE
								: laIndicatorData.getStopCode());
				lsIndis.append(CommonConstant.STR_SPACE_TWO);
				lsIndis.append(laIndicatorData.getDesc());
				lvRows.add(lsIndis.toString());
				lsIndis = new StringBuffer(EMPTY);
			}
			getlstIndiDescription1().setListData(lvRows);
			getlstIndiDescription1().setSelectedIndex(0);
		}
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
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(298, 122, 279, 125);
				getJPanel1().add(gettxtLienHldr1Addr(),
						gettxtLienHldr1Addr().getName());
				// user code begin {1}
				// cursor does not tab into panel
				ivjJPanel1.setRequestFocusEnabled(false);
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
				ivjJPanel2.setMaximumSize(new Dimension(2147483647,
						2147483647));
				ivjJPanel2.setMinimumSize(new Dimension(284, 88));
				ivjJPanel2.setBounds(298, 31, 279, 86);
				getJPanel2().add(getlblOwnerTitleName2(),
						getlblOwnerTitleName2().getName());
				getJPanel2().add(getlblOwnerTitleName1(),
						getlblOwnerTitleName1().getName());
				getJPanel2().add(getstcLblOwnerName(),
						getstcLblOwnerName().getName());
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
		return ivjJPanel2;
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
				ivjJPanel3.setBorder(new EtchedBorder());
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setMaximumSize(new Dimension(2147483647,
						2147483647));
				ivjJPanel3.setMinimumSize(new java.awt.Dimension(0, 0));
				ivjJPanel3.setBounds(17, 27, 274, 220);
				getJPanel3().add(getstcLblissued(),
						getstcLblissued().getName());
				getJPanel3().add(getstcLblVIN(),
						getstcLblVIN().getName());
				getJPanel3().add(getlblVIN(), getlblVIN().getName());
				getJPanel3().add(getlblTitleIssueDate(),
						getlblTitleIssueDate().getName());
				getJPanel3()
						.add(getlblDocNo(), getlblDocNo().getName());
				getJPanel3().add(getlblVehicleYear(),
						getlblVehicleYear().getName());
				getJPanel3().add(getlblRegExpiresMo(),
						getlblRegExpiresMo().getName());
				getJPanel3().add(getlblPlateNo(),
						getlblPlateNo().getName());
				getJPanel3().add(getstcLblNontitledVehicle(),
						getstcLblNontitledVehicle().getName());
				getJPanel3().add(getstcLblDocNo(),
						getstcLblDocNo().getName());
				getJPanel3().add(getstcLblExpires(),
						getstcLblExpires().getName());
				getJPanel3().add(getstcLblPlate(),
						getstcLblPlate().getName());
				getJPanel3().add(getstcLblYearMake(),
						getstcLblYearMake().getName());
				getJPanel3().add(getstcLblSlash(),
						getstcLblSlash().getName());
				getJPanel3().add(getlblRegExpiresYr(),
						getlblRegExpiresYr().getName());
				getJPanel3().add(getlblVehicleMake(),
						getlblVehicleMake().getName());
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
		return ivjJPanel3;
	}

	/**
	 * This method initializes ivjJPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			ivjJPanel4 = new JPanel();
			ivjJPanel4.setLayout(null);
			ivjJPanel4.setBorder(new EtchedBorder());
			ivjJPanel4.setSize(new Dimension(301, 70));
			ivjJPanel4.setLocation(new Point(274, 337));
			ivjJPanel4.add(getstcLblFeeToCharge(), null);
			ivjJPanel4.add(gettxtFeeToChargeValue(), null);
			ivjJPanel4.add(getJPanel5(), null);
			RTSButtonGroup laBtnGrp = new RTSButtonGroup();
			laBtnGrp.add(getchkMailIn());
			laBtnGrp.add(getchkNoCharge());
		}
		return ivjJPanel4;
	}

	/**
	 * This method initializes ivjJPanel5
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5()
	{
		if (ivjJPanel5 == null)
		{
			ivjJPanel5 = new JPanel();
			ivjJPanel5.setLayout(null);
			ivjJPanel5.setBounds(new Rectangle(25, 6, 95, 60));
			ivjJPanel5.add(getchkMailIn(), null);
			ivjJPanel5.add(getchkNoCharge(), null);
		}
		return ivjJPanel5;
	}

	// defect 10865
	/**
	 * This method initializes jpnlGrpFraud
	 * 
	 * @return JPanel
	 */
	private JPanel getJpnlGrpFraud()
	{
		if (jpnlGrpFraud == null)
		{
			jpnlGrpFraud = new JPanel();
			jpnlGrpFraud.setBounds(18, 255, 247, 154);
			jpnlGrpFraud.add(getFraudBox(), null);
			javax.swing.border.TitledBorder ivjTitledBorder = new javax.swing.border.TitledBorder(
					new EtchedBorder(), SUSPECTED_FRAUD);
			ivjTitledBorder
					.setBorder(javax.swing.BorderFactory
							.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
			ivjTitledBorder.setTitleColor(java.awt.Color.red);
			jpnlGrpFraud.setBorder(ivjTitledBorder);

		}
		return jpnlGrpFraud;
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setOpaque(true);
				getJScrollPane1().setViewportView(
						getlstIndiDescription1());
				// user code begin {1}
				ivjJScrollPane1.setBounds(274, 255, 301, 79);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the lblDocNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblDocNo()
	{
		if (ivjlblDocNo == null)
		{
			try
			{
				ivjlblDocNo = new JLabel();
				ivjlblDocNo.setName("lblDocNo");
				ivjlblDocNo.setText(EMPTY);
				ivjlblDocNo.setMaximumSize(new Dimension(52, 14));
				ivjlblDocNo.setMinimumSize(new Dimension(52, 14));
				ivjlblDocNo.setBounds(104, 118, 136, 20);
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
		return ivjlblDocNo;
	}

	/**
	 * Return the lblOwnerTitleName1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblOwnerTitleName1()
	{
		if (ivjlblOwnerTitleName1 == null)
		{
			try
			{
				ivjlblOwnerTitleName1 = new JLabel();
				ivjlblOwnerTitleName1.setSize(228, 18);
				ivjlblOwnerTitleName1.setName("lblOwnerTitleName1");
				ivjlblOwnerTitleName1.setOpaque(true);
				ivjlblOwnerTitleName1.setText(EMPTY);
				ivjlblOwnerTitleName1
						.setBackground(new Color(255, 0, 0));
				ivjlblOwnerTitleName1.setMaximumSize(new Dimension(90,
						14));
				// defect 11373
//				ivjlblOwnerTitleName1
//						.setForeground(SystemColor.activeCaptionText);
				ivjlblOwnerTitleName1
				.setForeground(SystemColor.WHITE);
				ivjlblOwnerTitleName1.setMinimumSize(new Dimension(90,
						14));
				// end defect 11373
				// user code begin {1}
				ivjlblOwnerTitleName1.setLocation(4, 33);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblOwnerTitleName1;
	}

	/**
	 * Return the lblOwnerTitleName2 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblOwnerTitleName2()
	{
		if (ivjlblOwnerTitleName2 == null)
		{
			try
			{
				ivjlblOwnerTitleName2 = new JLabel();
				ivjlblOwnerTitleName2.setSize(227, 18);
				ivjlblOwnerTitleName2.setName("lblOwnerTitleName2");
				ivjlblOwnerTitleName2.setText(EMPTY);
				ivjlblOwnerTitleName2.setMaximumSize(new Dimension(90,
						14));
				ivjlblOwnerTitleName2.setMinimumSize(new Dimension(90,
						14));
				ivjlblOwnerTitleName2.setLocation(4, 60);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlblOwnerTitleName2;
	}

	/**
	 * Return the lblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblPlateNo()
	{
		if (ivjlblPlateNo == null)
		{
			try
			{
				ivjlblPlateNo = new JLabel();
				ivjlblPlateNo.setSize(83, 20);
				ivjlblPlateNo.setName("lblPlateNo");
				ivjlblPlateNo.setText(EMPTY);
				ivjlblPlateNo.setMaximumSize(new Dimension(45, 14));
				ivjlblPlateNo.setMinimumSize(new Dimension(45, 14));
				ivjlblPlateNo.setLocation(104, 12);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlblPlateNo;
	}

	/**
	 * Return the lblRegExpiresMo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblRegExpiresMo()
	{
		if (ivjlblRegExpiresMo == null)
		{
			try
			{
				ivjlblRegExpiresMo = new JLabel();
				ivjlblRegExpiresMo.setSize(17, 20);
				ivjlblRegExpiresMo.setName("lblRegExpiresMo");
				ivjlblRegExpiresMo.setText(EMPTY);
				ivjlblRegExpiresMo
						.setMaximumSize(new Dimension(45, 14));
				ivjlblRegExpiresMo
						.setMinimumSize(new Dimension(45, 14));
				ivjlblRegExpiresMo.setLocation(104, 37);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlblRegExpiresMo;
	}

	/**
	 * Return the lblRegExpiresYr property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblRegExpiresYr()
	{
		if (ivjlblRegExpiresYr == null)
		{
			try
			{
				ivjlblRegExpiresYr = new JLabel();
				ivjlblRegExpiresYr.setSize(45, 20);
				ivjlblRegExpiresYr.setName("lblRegExpiresYr");
				ivjlblRegExpiresYr.setText(EMPTY);
				ivjlblRegExpiresYr
						.setMaximumSize(new Dimension(28, 14));
				ivjlblRegExpiresYr
						.setMinimumSize(new Dimension(28, 14));
				ivjlblRegExpiresYr.setLocation(130, 37);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlblRegExpiresYr;
	}

	/**
	 * Return the lblTitleIssueDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblTitleIssueDate()
	{
		if (ivjlblTitleIssueDate == null)
		{
			try
			{
				ivjlblTitleIssueDate = new JLabel();
				ivjlblTitleIssueDate.setSize(71, 20);
				ivjlblTitleIssueDate.setName("lblTitleIssueDate");
				ivjlblTitleIssueDate.setText(EMPTY);
				ivjlblTitleIssueDate.setMaximumSize(new Dimension(52,
						14));
				ivjlblTitleIssueDate.setMinimumSize(new Dimension(52,
						14));
				ivjlblTitleIssueDate.setLocation(104, 174);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlblTitleIssueDate;
	}

	/**
	 * Return the lblVehicleMake property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVehicleMake()
	{
		if (ivjlblVehicleMake == null)
		{
			try
			{
				ivjlblVehicleMake = new JLabel();
				ivjlblVehicleMake.setName("lblVehicleMake");
				ivjlblVehicleMake.setText(EMPTY);
				ivjlblVehicleMake.setMaximumSize(new Dimension(33, 14));
				ivjlblVehicleMake.setMinimumSize(new Dimension(33, 14));
				ivjlblVehicleMake.setBounds(152, 62, 43, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlblVehicleMake;
	}

	/**
	 * Return the lblVehicleYear property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVehicleYear()
	{
		if (ivjlblVehicleYear == null)
		{
			try
			{
				ivjlblVehicleYear = new JLabel();
				ivjlblVehicleYear.setName("lblVehicleYear");
				ivjlblVehicleYear.setText(EMPTY);
				ivjlblVehicleYear.setMaximumSize(new Dimension(52, 14));
				ivjlblVehicleYear.setMinimumSize(new Dimension(52, 14));
				ivjlblVehicleYear.setBounds(104, 62, 42, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlblVehicleYear;
	}

	/**
	 * Return the lblVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVIN()
	{
		if (ivjlblVIN == null)
		{
			try
			{
				ivjlblVIN = new JLabel();
				ivjlblVIN.setSize(166, 20);
				ivjlblVIN.setName("lblVIN");
				ivjlblVIN.setText(EMPTY);
				ivjlblVIN.setMaximumSize(new Dimension(52, 14));
				ivjlblVIN.setMinimumSize(new Dimension(52, 14));
				ivjlblVIN.setLocation(104, 90);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlblVIN;
	}

	/**
	 * Return the lstIndiDescription1 property value.
	 * 
	 * @return JList
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JList getlstIndiDescription1()
	{
		if (ivjlstIndiDescription1 == null)
		{
			try
			{
				ivjlstIndiDescription1 = new JList();
				ivjlstIndiDescription1.setName("lstIndiDescription1");
				// defect 9971
				ivjlstIndiDescription1.setFont(new java.awt.Font(
						CommonConstant.FONT_JLIST, 0, 12));
				// end defect 9971
				ivjlstIndiDescription1.setVisibleRowCount(6);
				ivjlstIndiDescription1.setBounds(-23, 0, 364, 61);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjlstIndiDescription1;
	}

	/**
	 * Return the stcLblDocNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDocNo()
	{
		if (ivjstcLblDocNo == null)
		{
			try
			{
				ivjstcLblDocNo = new JLabel();
				ivjstcLblDocNo.setSize(43, 20);
				ivjstcLblDocNo.setName("stcLblDocNo");
				ivjstcLblDocNo.setText(DOC_NO);
				ivjstcLblDocNo.setMaximumSize(new Dimension(43, 14));
				ivjstcLblDocNo.setMinimumSize(new Dimension(43, 14));
				ivjstcLblDocNo.setLocation(52, 118);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblDocNo;
	}

	/**
	 * Return the stcLblExpires property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblExpires()
	{
		if (ivjstcLblExpires == null)
		{
			try
			{
				ivjstcLblExpires = new JLabel();
				ivjstcLblExpires.setSize(46, 20);
				ivjstcLblExpires.setName("stcLblExpires");
				ivjstcLblExpires.setText(EXPIRES);
				ivjstcLblExpires.setMaximumSize(new Dimension(46, 14));
				ivjstcLblExpires.setMinimumSize(new Dimension(46, 14));
				ivjstcLblExpires.setLocation(49, 37);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblExpires;
	}

	/**
	 * Return the lblFeeToCharge property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblFeeToCharge()
	{
		if (ivjstcLblFeeToCharge == null)
		{
			try
			{
				ivjstcLblFeeToCharge = new JLabel();
				ivjstcLblFeeToCharge.setName("lblFeeToCharge");
				
				// defect 11230 
				ivjstcLblFeeToCharge
						.setDisplayedMnemonic(KeyEvent.VK_F);
				ivjstcLblFeeToCharge.setLabelFor(gettxtFeeToChargeValue());
				// end defect 11230 
				
				ivjstcLblFeeToCharge.setSize(new Dimension(92, 21));
				ivjstcLblFeeToCharge.setLocation(new Point(147, 12));
				ivjstcLblFeeToCharge.setText(FEE_TO_CHARGE);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblFeeToCharge;
	}

	/**
	 * Return the stcLblissued property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblissued()
	{
		if (ivjstcLblissued == null)
		{
			try
			{
				ivjstcLblissued = new JLabel();
				ivjstcLblissued.setSize(44, 20);
				ivjstcLblissued.setName("stcLblissued");
				ivjstcLblissued.setText(ISSUED);
				ivjstcLblissued.setMaximumSize(new Dimension(44, 14));
				ivjstcLblissued.setMinimumSize(new Dimension(44, 14));
				ivjstcLblissued.setLocation(51, 174);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblissued;
	}

	/**
	 * Return the stcLblNontitledVehicle property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblNontitledVehicle()
	{
		if (ivjstcLblNontitledVehicle == null)
		{
			try
			{
				ivjstcLblNontitledVehicle = new JLabel();
				ivjstcLblNontitledVehicle.setSize(188, 20);
				ivjstcLblNontitledVehicle
						.setName("stcLblNontitledVehicle");
				ivjstcLblNontitledVehicle.setText(EMPTY);
				ivjstcLblNontitledVehicle.setMaximumSize(new Dimension(
						120, 14));
				ivjstcLblNontitledVehicle.setMinimumSize(new Dimension(
						120, 14));
				ivjstcLblNontitledVehicle.setLocation(52, 146);
				// user code end
				ivjstcLblNontitledVehicle
						.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				ivjstcLblNontitledVehicle
						.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblNontitledVehicle;
	}

	/**
	 * Return the stcLblOwnerName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOwnerName()
	{
		if (ivjstcLblOwnerName == null)
		{
			try
			{
				ivjstcLblOwnerName = new JLabel();
				ivjstcLblOwnerName.setSize(83, 20);
				ivjstcLblOwnerName.setName("stcLblOwnerName");
				ivjstcLblOwnerName.setText(OWNER_NAME);
				ivjstcLblOwnerName
						.setMaximumSize(new Dimension(77, 14));
				ivjstcLblOwnerName
						.setMinimumSize(new Dimension(77, 14));
				// user code begin {1}
				ivjstcLblOwnerName.setLocation(4, 8);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblOwnerName;
	}

	/**
	 * Return the stcLblPlate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPlate()
	{
		if (ivjstcLblPlate == null)
		{
			try
			{
				ivjstcLblPlate = new JLabel();
				ivjstcLblPlate.setSize(35, 20);
				ivjstcLblPlate.setName("stcLblPlate");
				ivjstcLblPlate.setText(PLATE);
				ivjstcLblPlate.setMaximumSize(new Dimension(35, 14));
				ivjstcLblPlate.setMinimumSize(new Dimension(35, 14));
				ivjstcLblPlate.setLocation(60, 12);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblPlate;
	}

	/**
	 * Return the stcLblSlash property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblSlash()
	{
		if (ivjstcLblSlash == null)
		{
			try
			{
				ivjstcLblSlash = new JLabel();
				ivjstcLblSlash.setSize(4, 20);
				ivjstcLblSlash.setName("stcLblSlash");
				ivjstcLblSlash.setText(CommonConstant.STR_SLASH);
				ivjstcLblSlash.setMaximumSize(new Dimension(3, 14));
				ivjstcLblSlash.setMinimumSize(new Dimension(3, 14));
				ivjstcLblSlash.setLocation(123, 37);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblSlash;
	}

	/**
	 * Return the stcLblVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			try
			{
				ivjstcLblVIN = new JLabel();
				ivjstcLblVIN.setSize(25, 20);
				ivjstcLblVIN.setName("stcLblVIN");
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(new Dimension(22, 14));
				ivjstcLblVIN.setMinimumSize(new Dimension(22, 14));
				ivjstcLblVIN.setLocation(70, 90);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblVIN;
	}
	
	/**
	 * Return the stcLblyearmake property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblYearMake()
	{
		if (ivjstcLblYearMake == null)
		{
			try
			{
				ivjstcLblYearMake = new JLabel();
				ivjstcLblYearMake.setSize(66, 20);
				ivjstcLblYearMake.setName("stcLblyearmake");
				ivjstcLblYearMake.setText(YEAR_MAKE);
				ivjstcLblYearMake.setMaximumSize(new Dimension(63, 14));
				ivjstcLblYearMake.setMinimumSize(new Dimension(63, 14));
				ivjstcLblYearMake.setLocation(29, 62);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblYearMake;
	}

	/**
	 * Return the txtFeeToChargeValue property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtFeeToChargeValue()
	{
		if (ivjtxtFeeToChargeValue == null)
		{
			try
			{
				ivjtxtFeeToChargeValue = new RTSInputField();
				ivjtxtFeeToChargeValue.setName("txtFeeToChargeValue");
				ivjtxtFeeToChargeValue
						.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtFeeToChargeValue.setBounds(new Rectangle(242, 12,
						45, 21));
				ivjtxtFeeToChargeValue.setMaxLength(LENGTH_FEE);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtFeeToChargeValue;
	}

	/**
	 * Return the txtLienHldr1Addr property value.
	 * 
	 * @return JTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JTextArea gettxtLienHldr1Addr()
	{
		if (ivjtxtLienHldr1Addr == null)
		{
			try
			{
				ivjtxtLienHldr1Addr = new JTextArea();
				ivjtxtLienHldr1Addr.setName("txtLienHldr1Addr");
				ivjtxtLienHldr1Addr.setBackground(new Color(204, 204,
						204));
				ivjtxtLienHldr1Addr.setBounds(4, 7, 272, 113);
				// user code begin {1}
				// defect 8780
				// Set to bold
				ivjtxtLienHldr1Addr.setFont(new java.awt.Font("Dialog",
						java.awt.Font.BOLD, 12));
				// end defect 8780
				ivjtxtLienHldr1Addr.setFocusable(false);
				ivjtxtLienHldr1Addr.setEditable(false);
				ivjtxtLienHldr1Addr.setLineWrap(true);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtLienHldr1Addr;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException
	 *            Throwable
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx = new RTSException(
				RTSException.JAVA_ERROR, (Exception) aeException);
		leRTSEx.displayError(this);
	}

	/**
	 * 
	 * set the data object according to fraud checkboxes and set the overall
	 * state for proper messaging
	 * 
	 */
	private Vector handleFraudChange()
	{
		Vector lvFraud = new Vector();

		if (!caInitFraudData.isIdentification() == getchkFraudIdentification()
				.isSelected())
		{
			// setFraudChangedState(getchkFraudIdentification());
			FraudLogData laFraudLogData = new FraudLogData(
					caVehicleInquiryData.getMfVehicleData());

			laFraudLogData.setAddFraudIndi(getchkFraudIdentification()
					.isSelected() ? 1 : 0);

			laFraudLogData.setFraudCd(1);

			lvFraud.add(laFraudLogData);
		}
		if (!caInitFraudData.isReleaseOfLien() == getchkFraudReleaseOfLien()
				.isSelected())
		{
			// setFraudChangedState(getchkFraudReleaseOfLien());
			FraudLogData laFraudLogData = new FraudLogData(
					caVehicleInquiryData.getMfVehicleData());

			laFraudLogData.setAddFraudIndi(getchkFraudReleaseOfLien()
					.isSelected() ? 1 : 0);

			laFraudLogData.setFraudCd(2);

			lvFraud.add(laFraudLogData);
		}
		if (!caInitFraudData.isPowerOfAttorney() == getchkFraudPowerOfAttorney()
				.isSelected())
		{
			// setFraudChangedState(getchkFraudPowerOfAttorney());
			FraudLogData laFraudLogData = new FraudLogData(
					caVehicleInquiryData.getMfVehicleData());

			laFraudLogData.setAddFraudIndi(getchkFraudPowerOfAttorney()
					.isSelected() ? 1 : 0);

			laFraudLogData.setFraudCd(3);

			lvFraud.add(laFraudLogData);
		}
		if (!caInitFraudData.isLetterOfAuthorization() == getchkFraudLetterOfAuthorization()
				.isSelected())
		{
			// setFraudChangedState(getchkFraudLetterOfAuthorization());
			FraudLogData laFraudLogData = new FraudLogData(
					caVehicleInquiryData.getMfVehicleData());

			laFraudLogData
					.setAddFraudIndi(getchkFraudLetterOfAuthorization()
							.isSelected() ? 1 : 0);

			laFraudLogData.setFraudCd(4);

			lvFraud.add(laFraudLogData);
		}
		return lvFraud;
	}

	/**
	 * Returns whether a hard stop indicator is set or not.
	 * 
	 * @return boolean
	 */
	private boolean hasHardStop()
	{
		MFVehicleData laMFVehicleData = caVehicleInquiryData
				.getMfVehicleData();
		Vector lvIndis = IndicatorLookup.getIndicators(laMFVehicleData,
				getController().getTransCode(), IndicatorLookup.SCREEN);

		boolean lbHasHardStop = IndicatorLookup.hasHardStop(lvIndis);

		return lbHasHardStop;
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName(ScreenConstant.TTL018_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setTitle(ScreenConstant.TTL018_FRAME_TITLE);
			setContentPane(getFrmCCOCCDOScreenTTL018ContentPane1());
			this.setBounds(0, 0, 589, 515);
			// defect 10865
			caFraudTypesRadioGroup = new RTSButtonGroup();
			caFraudTypesRadioGroup.add(getchkFraudIdentification());
			caFraudTypesRadioGroup.add(getchkFraudReleaseOfLien());
			caFraudTypesRadioGroup.add(getchkFraudPowerOfAttorney());
			caFraudTypesRadioGroup
					.add(getchkFraudLetterOfAuthorization());
			// end defect 10865
		}
		catch (Throwable aeEx)
		{
			handleException(aeEx);
		}
	}
	/**
	 * ItemListener method.
	 * 
	 * @param aaIE
	 *            java.awt.event.ItemEvent
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		if (aaIE.getSource() instanceof JCheckBox) 
		{
			clearAllColor(this);
			
			if (aaIE.getSource() == getchkNoCharge())
			{

				if (getchkNoCharge().isSelected())
				{
					gettxtFeeToChargeValue().setText(
							CommonConstant.STR_ZERO_DOLLAR);
					gettxtFeeToChargeValue().setEnabled(false);
				}
				else
				{
					gettxtFeeToChargeValue().setText(new String());
					gettxtFeeToChargeValue().setEnabled(true);
				}
			}
			else if (aaIE.getSource() != getchkMailIn())
			{
				boolean lbEnable = !getFraudDataSnapshot().isAnyFraud();
				enableFees(lbEnable);
			}
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaDataObject
	 *            Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 10865
		if (aaDataObject != null
				&& aaDataObject instanceof VehicleInquiryData
				&& !cbAlreadySet)
		{
			// end defect 10865
			try
			{
				caVehicleInquiryData = (VehicleInquiryData) aaDataObject;
				caVehicleInquiryData.setValidationObject(null);

				if (caVehicleInquiryData.getValidationObject() == null)
				{
					TitleValidObj laValidObj = new TitleValidObj();

					// Make copy of the incoming inquiry object
					// needed by complete transaction
					MFVehicleData laMfVehOrigCpy = (MFVehicleData) UtilityMethods
							.copy(caVehicleInquiryData
									.getMfVehicleData());

					laValidObj.setMfVehOrig(laMfVehOrigCpy);

					caVehicleInquiryData
							.setValidationObject(laValidObj);
				}

				MFVehicleData laMFVehicleData = caVehicleInquiryData
						.getMfVehicleData();

				TitleData laTitleData = laMFVehicleData.getTitleData();
				OwnerData laOwnerData = laMFVehicleData.getOwnerData();
				RegistrationData laRegData = laMFVehicleData
						.getRegData();
				VehicleData laVehData = laMFVehicleData
						.getVehicleData();

				// Check for errors
				verifyCCOIssueDate();

				// set title Issue date, if not 0
				int liTtlIssueDate = laTitleData.getTtlIssueDate();
				if (liTtlIssueDate != 0)
				{
					getlblTitleIssueDate().setText(
							Integer.toString(liTtlIssueDate));
				}

				// build Indicator table
				getIndicators();

				// Initialize Screen variables

				/*
				 * set values for all fields except LienHolderAddress and
				 * indicators
				 */

				// panel 2
				getlblPlateNo().setText(
						laRegData.getRegPltNo().trim().toUpperCase());

				// defect 8900
				// Do not present "0" for Exp Mo/Yr
				int liRegExpMo = laRegData.getRegExpMo();
				getlblRegExpiresMo().setText(
						liRegExpMo == 0 ? "" : Integer
								.toString(liRegExpMo));
				int liRegExpYr = laRegData.getRegExpYr();
				getlblRegExpiresYr().setText(
						liRegExpYr == 0 ? "" : Integer
								.toString(liRegExpYr));
				// end defect 8900

				getlblVehicleYear().setText(
						Integer.toString(laVehData.getVehModlYr()));
				getlblVehicleMake().setText(
						laVehData.getVehMk().trim().toUpperCase());
				getlblVIN().setText(
						laVehData.getVin().trim().toUpperCase());
				getlblDocNo().setText(
						laTitleData.getDocNo().trim().toUpperCase());
				RTSDate laIssueDate = new RTSDate();
				if (laTitleData.getTtlIssueDate() != 0)
				{
					laIssueDate = new RTSDate(RTSDate.YYYYMMDD,
							laTitleData.getTtlIssueDate());
					getlblTitleIssueDate().setText(
							laIssueDate.toString());
				}

				int liDocTypeCd = laTitleData.getDocTypeCd();

				String lsDocTypeCd = DocumentTypesCache.getDocType(
						liDocTypeCd).getDocTypeCdDesc();

				getstcLblNontitledVehicle().setText(
						lsDocTypeCd.trim().toUpperCase());

				// panel 3

				// defect 10112
				getlblOwnerTitleName1().setText(laOwnerData.getName1());
				getlblOwnerTitleName2().setText(laOwnerData.getName2());

				// defect 9971
				String lsText1 = "";
				LienholderData laLien1 = laTitleData
						.getLienholderData(TitleConstant.LIENHLDR1);

				// defect 10112
				String lsPermLienhldrId = laTitleData
						.getPermLienHldrId1();

				if (laLien1.isPopulated()
						|| (!UtilityMethods.isEmpty(lsPermLienhldrId)))
				{
					// end defect 10112

					// FIRST LIEN ONLY
					String lsDate = UNKNOWN;

					if (laLien1.getLienDate() != 0)
					{
						lsDate = new RTSDate(RTSDate.YYYYMMDD, laLien1
								.getLienDate()).toString();
					}

					lsText1 = buildDateLine(FIRST_LIEN_DATE + lsDate,
							laTitleData.getPermLienHldrId1())
							+ NEXT_LINE;

					// defect 10112
					gettxtLienHldr1Addr()
							.setText(
									lsText1
											+ laLien1
													.getNameAddressStringBuffer()
													.toString());
					// end defect 10112

				}
				// end defect 9971

				// Set data on FeeToChargeValue (Region Only)
				boolean lbRegion = SystemProperty.isRegion();
				if (lbRegion)
				{
					VehMiscData laVehMiscData = caVehicleInquiryData
							.getVehMiscData();

					if (laVehMiscData == null
							|| UtilityMethods.isEmpty(laVehMiscData
									.getAuthCd()))
					{
						gettxtFeeToChargeValue().setText(
								DEFAULT_CCO_FEE);
					}
				}
				else
				{
					gettxtFeeToChargeValue().setText(
							CommonConstant.STR_ZERO_DOLLAR);
				}
				getstcLblFeeToCharge().setVisible(lbRegion);
				gettxtFeeToChargeValue().setEnabled(lbRegion);
				gettxtFeeToChargeValue().setEditable(lbRegion);
				gettxtFeeToChargeValue().setVisible(lbRegion);
				
				// defect 11230 
				getchkMailIn().setVisible(lbRegion); 
				getchkNoCharge().setVisible(lbRegion);
				getchkMailIn().setEnabled(lbRegion); 
				getchkNoCharge().setEnabled(lbRegion);
				getJPanel4().setVisible(lbRegion);
				// end defect 11230 

				// defect 10865
				// if condition for which fraud is not allowed disable fraud
				// checkboxes
				if (liDocTypeCd != DocTypeConstant.REGULAR_TITLE
						&& liDocTypeCd != DocTypeConstant.OFF_HIGHWAY_USE_ONLY
						&& liDocTypeCd != DocTypeConstant.OS_REGISTERED_APPORTIONED_VEH
						&& liDocTypeCd != DocTypeConstant.INSURANCE_NO_REGIS_TITLE)
				{
					ciCCONotAvailableErrNo = ErrorsConstant.ERR_NUM_DOC_TYPE_UNAVAILABLE_FOR_CCO;

				}
				else if (caVehicleInquiryData.hasTtlProcsCd())
				{
					ciCCONotAvailableErrNo = ErrorsConstant.ERR_NUM_DO_NOT_ALLOW_CCO_TTL_IN_PROCS;
				}

				caInitFraudData = caVehicleInquiryData
						.getFraudStateData();

				LinkedList laChkBoxes = new LinkedList(
						caFraudTypesRadioGroup.getSelectableElements());

				if (ciCCONotAvailableErrNo == 0)
				{
					if (!(caInitFraudData == null)
							&& caInitFraudData.isAnyFraud())
					{
						Boolean larrFraudIndi[] = caInitFraudData
								.getIndicatorArray();

						for (int i = 0; i < laChkBoxes.size(); i++)
						{
							if (((Boolean) larrFraudIndi[i + 1])
									.booleanValue())
							{
								((JCheckBox) laChkBoxes.get(i))
										.setSelected(true);
								((JCheckBox) laChkBoxes.get(i))
										.setForeground(java.awt.Color.RED);
							}
						}
						// defect 11230
						enableFees(false);
						// end defect 11230 
					}
				}
				else
				{
					for (int i = 0; i < laChkBoxes.size(); i++)
					{
						((JCheckBox) laChkBoxes.get(i))
								.setEnabled(false);
					}
				}
			

				cbAlreadySet = true;
				// end defect 10865
			}
			catch (ClassCastException aeCCEx)
			{
			}
		}
	}
	
	/**
	 * Verify CCO Issue Date
	 * 
	 */
	private void verifyCCOIssueDate()
	{
		int liCCOIssueDate = caVehicleInquiryData.getMfVehicleData()
				.getTitleData().getCcoIssueDate();

		// defect 10639
		if (liCCOIssueDate != 0)
		{
			RTSDate laCCOIssueDate = new RTSDate(RTSDate.YYYYMMDD,
					liCCOIssueDate);

			if (laCCOIssueDate.showMsgForRecentCCO())
			{
				displayError(ErrorsConstant.ERR_NUM_CCO_WITHIN_SPECIFIED_INTERVAL);
			}
		}
		// end defect 10639
	}
	
	/*
	 * Invoked when the window is set to be the user's active window, which
	 * means the window (or one of its subcomponents) will receive keyboard
	 * events.
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowActivated(java.awt.event.WindowEvent aaWE)
	{
		// defect 9971
		if (ciNumIndis > CommonConstant.MAX_INDI_NO_SCROLL)
		{
			// end defect 9971
			getlstIndiDescription1().requestFocus();
		}
		// defect 11299
		else if (gettxtFeeToChargeValue().isVisible() 
				&& gettxtFeeToChargeValue().isEnabled())
		{
			// end defect 11299
			gettxtFeeToChargeValue().requestFocus();
		}
		else
		{
			getButtonPanel1().getBtnEnter().requestFocus();
		}
	}

} // @jve:visual-info decl-index=0 visual-constraint="10,10"
