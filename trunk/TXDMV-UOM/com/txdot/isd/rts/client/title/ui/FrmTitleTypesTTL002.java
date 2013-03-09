package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.title.business.TitleClientBusiness;

import com.txdot.isd.rts.services.cache.RegistrationClassCache;
import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmTitleTypeTTL002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			Made changes for validations
 * T Pederson	05/03/2002	Corrected error display in setData so that 
 * 							os/2 window would not flash.
 * MAbs			05/16/2002	Removed tooltiptext CQU100003905
 * T Pederson	05/23/2002	Corrected error display in doDlrTtl so that 
 * 							os/2 window would not flash 
 *							CQU100004055.
 * T Pederson	06/03/2002	Added beep to error display for invalid date
 * 							CQU100004193.
 * J Rue		08/02/2002	Defect 4468, fix code to ensure the ENTER 
 * 							command process has completed
 *							before the ESC can execute. 
 *							Method actionPerformed()
 * J Rue		08/23/2002	Defect 4647, Display MF expiration if 
 * 							issuing registartion out side 
 *							of the three month window. method doDlrTtl.
 * J Rue		10/24/2002	Defect 4859, Set OffHwyUse to 0 if not 
 * 							checked. method setDataToDataObject()
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 * 							user help guide so had to make changes
 *							in actionPerformed().
 * Ray Rowehl	07/12/2003 	bring vtr Auth Code back from TTL003 when 
 * 							excaping back from there.
 *							modify actionPerformed()
 *							defect 5304
 * B Arredondo	10/30/2003	Defect 6029, 6090, 6134, 6312
 *	J Rue					Replacing redundant code, added code for 
 *							setRegWaivedIndi replacing 
 *							setStolenWaivedIndi
 *							and commented code for setDpsStlnIndi 
 *							method setDatatoDataObject()
 * J Rue		08/16/2004	Use isToBePrinted to determine if new 
 * 							registration
 *							use issued from DTA Diskette.
 *							modify doDlrTtl()
 *							defect 7448 Ver 5.2.1
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were converted 
 *							to integer.
 *							methods doDlrTtl()
 *							defect 7496 Ver 5.2.1
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/31/2005	Comment out all setNextFocusableComponent()
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/04/2005	Remove variables not used.
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/15/2005	Set arrow movement to RTS stanards
 * 							modify KeyPressed()
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/25/2005	Update comments
 * 							modify keyPressed()
 * 							defect 7898 Ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		10/28/2005	Add title to borders
 * 							modify getJPanel1(), getJPanel2()
 * 							defect 8416 Ver 5.2.3
 * J Rue		11/07/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	12/15/2005	Key listener required on Title Date field
 * 							needed to add super.keyPressed()
 * 							modify keyPressed(),getdateTitleTrans() 
 * 							defect 7898 Ver 5.2.3     
 * T. Pederson	12/30/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * Jeff S.		01/6/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyReleased(), keyPressed(), 
 * 								ciSelectedNum, arrTtlType
 * 							modify getJPanel1(), initialize(), 
 * 								getradioCorrected(), getradioNonTitled(),
 * 								getradioOriginal(), getchkStolenWaived(),
 * 								getradioRegistration(), getJPanel2(), 
 * 								getdateTitleTrans(), setData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/17/2006	Change verbiage on screen to read 
 *							"Corrected - Original" vs. "Corrected"
 *							modify value of CORRECTED
 *							defect 8370 Ver 5.2.2 Fix 8
 * K Harrell	01/19/2006	Java 1.4 Work
 * 							modify doDlrTtl() 
 * 							defect 7898 Ver 5.2.3 
 * J Rue		02/14/2006	Use DlrTransDate to validate registration
 * 							window, DTA
 * 							modify doDlrTtl()
 * 							defect 7457 Ver 5.2.3
 * J Rue		03/01/2006	Defect 7457 was missing
 * 							Reapply defect 7457
 * 							modify doDlrTtl()
 * 							defect 7457 Ver 5.2.3
 * J Rue		03/01/2006	Add Fees calculations to 7457
 * 							modify doDlrTtl()
 * 							defect 7457 Ver 5.2.3
 * J Rue		03/24/2006	Add boolean to get NoMfRecs
 * 							Add to conditional to display reg issued by
 * 							dealer outside of Reg#MoWindow  
 * 							modify doDlrTtl()
 * 							defect 7457 Ver 5.2.3
 * J Rue		04/07/2006	Calculate if registration exceeds the 3 
 * 							month registration window using current 
 * 							RegExp and dealer transaction date. Set 
 * 							boolean accordingly. 
 * 							modify doDlrTtl()
 * 							defect 8645 Ver 5.2.3
 * K Harrell	04/24/2006	Focus does not return to Enter when select
 * 							Record Not Applicable in DTA.  
 * 							(same as fix from DTA008)
 * 							add implements FocusListener 
 * 							add focusGained(), focusLost() 
 * 							modify initialize()
 * 							defect 8728 Ver 5.2.3
 * K Harrell	08/01/2007	Reset TitleValid changeRegis
 * 							modify setDataToDataObject()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	03/05/2009	Moved assignment of DocTypeCd from TTL008. 
 * 							Use constants for DocTypeCd. 
 * 							modify setDataToDataObject()
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	04/13/2009	Removed above defect 9971 to TTL007 
 * 							modify setDataToDataObject() 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	06/09/2009	Non-Titled disabled in REJCOR. Additional
 * 							class cleanup.
 * 							add csTransCd, isDTA(), isRejCor() 
 * 							modify setData(), setDataToDataObject()
 * 							defect 10035 Ver Defect_POS_F
 * K Harrell	12/15/2009	DTA Cleanup
 * 							modify setData()  
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	03/19/2010	Restore to original setting/disable Off-Highway
 * 							Checkbox if Corrected Title. 
 * 							add cbOrigOffHiway
 * 							modify setData(), handleChkBoxDisplay(), 
 * 							 setDataToDataObject()  
 * 							defect 10416 Ver POS_640 
 * K Harrell	04/14/2010	Disable Off-Highway if Registration Purposes
 * 							Only. 
 * 							modify handleRadioBtnDisplay() 
 * 							defect 10453 Ver POS_640 
 * K Harrell	09/16/2010	Reset saved TTL007/TTL035 data if move 
 * 							to/from Corrected Title 
 * 							modify setDataToDataObject()
 * 							defect 10592 Ver 6.6.0  
 * B Hargrove	07/19/2011	As-of No Default Title Trans date start 
 * 							date, we will not default Title Trans Date.
 * 							Note: Rejcor has hidden the date, but still
 * 							expects it to be there, so it IS defaulted. 
 * 							modify doDlrTtl(), initialize(), setData()
 * 							defect 10949 Ver 6.8.0  
 * K Harrell	11/17/2011	Do not allow both Off-Highway and Stolen to 
 * 							be selected.
 * 							add itemStateChanged() 
 * 							modify getchkOffHighway(), 
 * 							  getchkStolenWaived()
 * 							defect 11004 Ver 6.9.0
 * K Harrell	12/02/2011	Implement DealerTitleData.getOffHghwyIndi()
 * 							modify doDlrTtl() 
 * 							defect 11047 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to capture title type, off-highway use, stolen and 
 * title transaction date.
 *
 * @version	6.9.0 			12/02/2011
 * @author	Todd Pederson
 * <br>Creation Date: 		06/21/2001 17:23:22
 */

public class FrmTitleTypesTTL002
	extends RTSDialogBox
	implements ActionListener, FocusListener 
	// defect 11004
	,ItemListener
	// end defect 11004
{
	private ButtonPanel ivjbuttonPanel = null;
	private JCheckBox ivjchkOffHighway = null;
	private JCheckBox ivjchkStolenWaived = null;
	private RTSDateField ivjtxtTitleTransDate = null;
	private JPanel ivjFrmTitleTypesTTL002ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JRadioButton ivjradioCorrected = null;
	private JRadioButton ivjradioNonTitled = null;
	private JRadioButton ivjradioOriginal = null;
	private JRadioButton ivjradioRegistration = null;
	private JLabel ivjstcLblRecordFound = null;
	private JLabel ivjstcLblTitleTransactionDate = null;

	private Border caEtched;

	// booleans
	private boolean cbSelectNonTtl;
	private boolean cbShowDTA215 = true;
	private boolean cbShutDown = false;

	// defect 10592 
	private boolean cbCorTtl = false;
	// end defect 10592  

	// defect 10416 
	private boolean cbOrigOffHiway = false;
	// end defect 10416 

	// Strings
	// defect 10035 
	private String csTransCd = null;
	// end defect 10035 

	// Objects		
	private DealerTitleData caDlrTtlData = null;
	private Object caOrigObject = null;
	private VehicleInquiryData caVehInqData = null;

	// Constants 
	private final static String CHECK_IF_APPLICABLE =
		"Check if applicable:";
	private final static String CORRECTED = "Corrected - Original";
	private final static String DTA_REC_FOUND = "DTA215   RECORD FOUND";
	private final static String ERROR = "ERROR!";
	private final static String INVALID_DATE = "Invalid date.";
	private final static String MISC = "MISC";
	private final static String NO_RECORD_FOUND = "NO RECORD FOUND";
	private final static String NON_TITLED = "Non-Titled";
	private final static String OFF_HWY_USE = "Off-Highway Use";
	private final static String ORIGINAL = "Original";
	private final static String RECORD_FOUND = "RECORD FOUND";
	private final static String REGISTRATION_EXPIRATION =
		"Registration Expiration: ";
	private final static String REGISTRATION_MSG =
		". New registration has been "
			+ "submitted but may not be "
			+ "required for this "
			+ "transaction. The "
			+ "registration is current and "
			+ "not within the renewal "
			+ "window.";
	private final static String REGISTRATION_PURPOSE_ONLY =
		"Registration Purposes Only";
	private final static String SELECT_ONE = "Select one:";
	private final static String TITLE_TRANSACTION_DATE =
		"Title Transaction Date:";
	private final static String VEH_STOLEN_WAIVED =
		"Vehicle stolen - Waive registration";

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs an array of command-line arguments
	 */
	public static void main(String[] aarrAargs)
	{
		try
		{
			FrmTitleTypesTTL002 laFrmTitleTypesTTL002;
			laFrmTitleTypesTTL002 = new FrmTitleTypesTTL002();
			laFrmTitleTypesTTL002.setModal(true);
			laFrmTitleTypesTTL002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laWE)
				{
					System.exit(0);
				};
			});
			laFrmTitleTypesTTL002.show();
			java.awt.Insets insets = laFrmTitleTypesTTL002.getInsets();
			laFrmTitleTypesTTL002.setSize(
				laFrmTitleTypesTTL002.getWidth()
					+ insets.left
					+ insets.right,
				laFrmTitleTypesTTL002.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmTitleTypesTTL002.setVisibleRTS(true);
		}
		catch (Throwable leException)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			leException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmTitleTypesTTL002 constructor comment.
	 */

	public FrmTitleTypesTTL002()
	{
		super();
		initialize();
	}

	/**
	 * FrmTitleTypesTTL002 constructor.
	 * 
	 * @param aaParent JFrame
	 */

	public FrmTitleTypesTTL002(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTitleTypesTTL002 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTitleTypesTTL002(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when user performs an action.  The user completes the 
	 * screen by making appropriate selections and pressing enter.
	 *
	 * Cancel or Help buttons respectively result in destroying the 
	 * window or providing appropriate help.
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
			//Clear All Fields
			clearAllColor(this);
			if (aaAE.getSource() instanceof JRadioButton)
			{
				handleRadioBtnDisplay();

				if (caVehInqData != null
					&& !caVehInqData.isRecordFound())
				{
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.setVehClassCd(
						null);

					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.setRegClassCd(
						0);
				}
			}
			else if (aaAE.getSource() instanceof JCheckBox)
			{
				handleChkBoxDisplay();
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				checkRegInfoForDTA();

				if (gettxtTitleTransDate().getDate() == null
					|| validateTtlTransDate())
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.setBeep(RTSException.BEEP);
					leRTSEx.addException(
						new RTSException(
							RTSException.FAILURE_MESSAGE,
							INVALID_DATE,
							ERROR),
						gettxtTitleTransDate());
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}

				//create copy
				Object laObjCopy = setDataToDataObject();

				getController().processData(
					AbstractViewController.ENTER,
					laObjCopy);

				// defect 5304
				// get auth code back from TTL003 
				if (laObjCopy != null
					&& ((VehicleInquiryData) laObjCopy).getVehMiscData()
						!= null)
				{
					if (((VehicleInquiryData) laObjCopy)
						.getVehMiscData()
						.getAuthCd()
						!= null)
					{
						caVehInqData.getVehMiscData().setAuthCd(
							((VehicleInquiryData) laObjCopy)
								.getVehMiscData()
								.getAuthCd());
					}
				}
				// end defect 5304
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caVehInqData);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				String lsTransCd = getController().getTransCode();
				Vector lvPrevControllers =
					getController().getPreviousControllers();
				if (lvPrevControllers != null
					&& lvPrevControllers.size() >= 2)
				{
					Object laDTAController =
						lvPrevControllers.elementAt(1);
					if (getController()
						.getTransCode()
						.equals(TransCdConstant.DTAORD))
					{
						if (laDTAController
							.equals(ScreenConstant.DTA007))
						{
							lsTransCd = TransCdConstant.DTAORD;
						}
						else
						{
							lsTransCd = TransCdConstant.DTAORK;
						}
					}
				}
				if (lsTransCd.equals(TransCdConstant.DTAORK)
					|| lsTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.TTL002B);
				}
				else if (
					lsTransCd.equals(TransCdConstant.DTANTD)
						|| lsTransCd.equals(TransCdConstant.DTAORD))
				{
					RTSHelp.displayHelp(RTSHelp.TTL002C);
				}
				else if (
					lsTransCd.equals(TransCdConstant.TITLE)
						|| lsTransCd.equals(TransCdConstant.NONTTL)
						|| lsTransCd.equals(TransCdConstant.REJCOR))
				{
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.TTL002A);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.TTL002D);
					}
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Determines whether the transaction in progress is a DTA ORD. 
	 * Returns <code>
	 * true</code>  if <code>transcd</code> (trans code) is DTA ORD 
	 * and <code>false</code> if not. 
	 * 
	 * @param aaDataObject Object
	 * @return boolean
	 */
	private boolean checkAndDoDlrTtl(Object aaDataObject)
	{
		boolean lbRet = false;

		//TODO I don't see how this is EVER valid.  
		if (UtilityMethods.isDTA(csTransCd)
			&& aaDataObject instanceof Vector)
		{
			lbRet = true;
			//Vector could be of size 1 or 2
			// 1 - No record is found at key006 for VIN   
			// 2 - Record found 

			Vector lvDataObj = (Vector) aaDataObject;
			Object laObj1 = null;
			Object laObj2 = null;
			if (lvDataObj.size() == 1)
			{
				laObj1 = lvDataObj.get(0);
			}
			else if (lvDataObj.size() == 2)
			{
				laObj1 = lvDataObj.get(0);
				laObj2 = lvDataObj.get(1);
			}
			if (laObj1 instanceof VehicleInquiryData)
			{
				caVehInqData = (VehicleInquiryData) laObj1;
			}
			else if (laObj1 instanceof DealerTitleData)
			{
				caDlrTtlData = (DealerTitleData) laObj1;
			}
			if (laObj2 instanceof VehicleInquiryData)
			{
				caVehInqData = (VehicleInquiryData) laObj2;
			}
			else if (laObj2 instanceof DealerTitleData)
			{
				caDlrTtlData = (DealerTitleData) laObj2;
			}
		}
		return lbRet;
	}

	/**
	 * Reset reg info if registration invalid for DTA.
	 */
	private void checkRegInfoForDTA()
	{
		if (isDTA())
		{
			RegistrationData laRegData =
				caVehInqData.getMfVehicleData().getRegData();

			//TODO Should be a method w/in RegistrationData 
			if (laRegData.getRegInvldIndi() == 1)
			{
				laRegData.setRegClassCd(0);
				laRegData.setRegEffDt(0);
				laRegData.setRegExpMo(0);
				laRegData.setRegExpYr(0);
				laRegData.setRegExpMo(0);
				laRegData.setRegPltAge(0);
				laRegData.setRegExpMo(0);
				laRegData.setRegPltCd(CommonConstant.STR_SPACE_EMPTY);
				laRegData.setRegPltNo(CommonConstant.STR_SPACE_EMPTY);
				laRegData.setRegStkrCd(CommonConstant.STR_SPACE_EMPTY);
				laRegData.setRegStkrNo(CommonConstant.STR_SPACE_EMPTY);
			}
		}
	}

	/**
	 * Performs setting of radio buttons and checks for new inventory 
	 * issued for a dealer title transaction.
	 */
	private void doDlrTtl()
	{
		if (caVehInqData != null)
		{
			TitleValidObj laTtlValidationObj =
				(TitleValidObj) caVehInqData.getValidationObject();

			if (laTtlValidationObj != null)
			{
				DealerTitleData laDlrTtlData =
					(DealerTitleData) laTtlValidationObj
						.getDlrTtlData();

				if (laDlrTtlData != null)
				{
					//Do only if dlrTtlData
					DealerTitleData
						.consolidateVehicleInfoWithDealerInfo(
						laDlrTtlData,
						caVehInqData);

					// Preselect Radio Buttons
					if (caVehInqData
						.getMfVehicleData()
						.getTitleData()
						.getDocTypeCd()
						== DocTypeConstant.NON_TITLED_VEHICLE)
					{
						getradioNonTitled().setSelected(true);
						cbSelectNonTtl = true;
					}
					// defect 11047 
					if (laDlrTtlData.getOffHghwyIndi() ==1)
					{
						getchkOffHighway().setSelected(true); 
					}
					// end defect 11047 
					
					// defect 7448
					//  Determine if sticker was issued using 
					//	'Print Sticker' check box
					//if (strNewStkrNo != null && strNewStkrNo.length() 
					//	> 0)

					// Test for dealer issued registration outside of 3 
					//	month window for record found
					// defect 8645
					//	Add check to ensure MF record was retrieved
					// defect 10949
					// After start date, do not show 'may not need reg' msg
					if (laDlrTtlData.isToBePrinted()
						&& caVehInqData.isRecordFound()
						&& RTSDate.getCurrentDate().getYYYYMMDDDate()
							< SystemProperty.getDlrInvalidRegStartDate().
								getYYYYMMDDDate())
					{
						// end defect 7448
						// end defect 8645
						// end defect 10949
						int liNewRegExpMo = 0;
						int liNewRegExpYr = 0;
						// defect 7457
						// New variables for using DlrTrans dates
						int liDTACurrMo = 0;
						int liDTACurrYr = 0;
						// end defect 7457
						try
						{
							// defect 7496
							// NewRegExpMo and NewRegExpYr were 
							//	converted to integer
							//iNewRegExpMo = Integer.parseInt(
							//	dlrTtlData.getNewRegExpMo());
							//iNewRegExpYr = Integer.parseInt(
							//	dlrTtlData.getNewRegExpYr());
							// Get NewRegExpMo/Yr
							liNewRegExpMo =
								laDlrTtlData.getNewRegExpMo();
							liNewRegExpYr =
								laDlrTtlData.getNewRegExpYr();
							// end defect 7496
							// defect 7457
							//	Get DlrTransDateMo/Yr.
							//	Use these dates as current date 
							//	replacing RTSDate.getCurrentDate()
							liDTACurrMo =
								Integer.parseInt(
									laDlrTtlData
										.getTransDate()
										.substring(
										4,
										6));
							liDTACurrYr =
								Integer.parseInt(
									laDlrTtlData
										.getTransDate()
										.substring(
										0,
										4));
							// end defect 7457
						}
						catch (NumberFormatException aeNFEx)
						{
							liNewRegExpMo = 0;
							liNewRegExpYr = 0;
						}
						if (liNewRegExpMo > 0 && liNewRegExpYr > 0)
						{
							int liRegExpMo =
								caVehInqData
									.getMfVehicleData()
									.getRegData()
									.getRegExpMo();
							int liRegExpYr =
								caVehInqData
									.getMfVehicleData()
									.getRegData()
									.getRegExpYr();

							boolean lbRegExceed3MonthWindow = false;

							int liCurrentMonths =
								(liDTACurrYr * 12) + liDTACurrMo;
							int liExpirationMonths =
								(liRegExpYr * 12) + liRegExpMo;
							if (liExpirationMonths - liCurrentMonths
								>= 3)
							{
								lbRegExceed3MonthWindow = true;
							}

							if (lbRegExceed3MonthWindow
								&& cbShowDTA215)
							{
								//Display warning that dealer issued 
								//inventory but not needed
								String lsMsg =
									REGISTRATION_EXPIRATION
										+ liRegExpMo
										+ CommonConstant.STR_DASH
										+ liRegExpYr
										+ REGISTRATION_MSG;

								RTSException laRTSEx =
									new RTSException(
										RTSException.WARNING_MESSAGE,
										lsMsg,
										DTA_REC_FOUND);

								laRTSEx.displayError(this);
								cbShowDTA215 = false;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		// defect 8728
		// Ensure that focus will return to Enter Key when 
		// select Rcd Not Applicable in DTA
		if (aaFE.getSource() == this)
		{
			getbuttonPanel().getBtnEnter().requestFocus();
		}
		// end defect 8728
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{

	}

	/**
	 * Return the ivjbuttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("ivjbuttonPanel");
				ivjbuttonPanel.setBounds(22, 298, 280, 64);
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbuttonPanel;
	}

	/**
	 * Return the ivjchkOffHighway property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkOffHighway()
	{
		if (ivjchkOffHighway == null)
		{
			try
			{
				ivjchkOffHighway = new JCheckBox();
				ivjchkOffHighway.setName("ivjchkOffHighway");
				ivjchkOffHighway.setBounds(22, 24, 181, 22);
				ivjchkOffHighway.setText(OFF_HWY_USE);
				// user code begin {1}
				ivjchkOffHighway.setMnemonic(KeyEvent.VK_U);
				// defect 11004 
				ivjchkOffHighway.addItemListener(this);
				// end defect 11004
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkOffHighway;

	}

	/**
	 * Return the ivjchkStolenWaived property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkStolenWaived()
	{
		if (ivjchkStolenWaived == null)
		{
			try
			{
				ivjchkStolenWaived = new JCheckBox();
				ivjchkStolenWaived.setName("ivjchkStolenWaived");
				ivjchkStolenWaived.setBounds(22, 53, 228, 22);
				ivjchkStolenWaived.setText(VEH_STOLEN_WAIVED);
				ivjchkStolenWaived.setVisible(true);
				// user code begin {1}
				ivjchkStolenWaived.setMnemonic(KeyEvent.VK_V);
				ivjchkStolenWaived.addActionListener(this);
				// defect 11004 
				ivjchkStolenWaived.addItemListener(this);
				// end defect 11004
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkStolenWaived;
	}

	/**
	 * Return the ivjtxtTitleTransDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtTitleTransDate()
	{
		if (ivjtxtTitleTransDate == null)
		{
			try
			{
				ivjtxtTitleTransDate = new RTSDateField();
				ivjtxtTitleTransDate.setName("ivjdateTitleTrans");
				ivjtxtTitleTransDate.setBounds(170, 269, 79, 20);
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
		return ivjtxtTitleTransDate;
	}

	/**
	 * Return the FrmTitleTypesTTL002ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmTitleTypesTTL002ContentPane1()
	{
		if (ivjFrmTitleTypesTTL002ContentPane1 == null)
		{
			try
			{
				ivjFrmTitleTypesTTL002ContentPane1 = new JPanel();
				ivjFrmTitleTypesTTL002ContentPane1.setName(
					"ivjFrmTitleTypesTTL002ContentPane1");
				ivjFrmTitleTypesTTL002ContentPane1.setLayout(null);
				ivjFrmTitleTypesTTL002ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTitleTypesTTL002ContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmTitleTypesTTL002ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmTitleTypesTTL002ContentPane1().add(
					getstcLblRecordFound(),
					getstcLblRecordFound().getName());
				getFrmTitleTypesTTL002ContentPane1().add(
					getstcLblTitleTransactionDate(),
					getstcLblTitleTransactionDate().getName());
				getFrmTitleTypesTTL002ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());

				getFrmTitleTypesTTL002ContentPane1().add(
					gettxtTitleTransDate(),
					gettxtTitleTransDate().getName());

				getFrmTitleTypesTTL002ContentPane1().add(
					getbuttonPanel(),
					getbuttonPanel().getName());

				getFrmTitleTypesTTL002ContentPane1().add(
					getJPanel2(),
					getJPanel2().getName());

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
		return ivjFrmTitleTypesTTL002ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setBounds(22, 25, 280, 150);
				ivjJPanel1.setLayout(null);

				getJPanel1().add(
					getradioOriginal(),
					getradioOriginal().getName());

				getJPanel1().add(
					getradioCorrected(),
					getradioCorrected().getName());

				getJPanel1().add(
					getradioRegistration(),
					getradioRegistration().getName());

				getJPanel1().add(
					getradioNonTitled(),
					getradioNonTitled().getName());

				// user code begin {1}
				// 	Add title to border
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_ONE));

				RTSButtonGroup laBG = new RTSButtonGroup();
				laBG.add(getradioOriginal());
				laBG.add(getradioCorrected());
				laBG.add(getradioRegistration());
				laBG.add(getradioNonTitled());
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
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setBounds(21, 184, 280, 80);
				ivjJPanel2.setLayout(null);
				getJPanel2().add(
					getchkOffHighway(),
					getchkOffHighway().getName());
				getJPanel2().add(
					getchkStolenWaived(),
					getchkStolenWaived().getName());

				// user code begin {1}
				// 	Add title to border
				ivjJPanel2.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						CHECK_IF_APPLICABLE));

				RTSButtonGroup laRTSBtnGrp = new RTSButtonGroup();
				laRTSBtnGrp.add(getchkOffHighway());
				laRTSBtnGrp.add(getchkStolenWaived());
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
	 * Return Object.
	 * 
	 * @return Object
	 */
	public Object getOrigObject()
	{
		return caOrigObject;
	}

	/**
	 * Return the ivjradioCorrected property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCorrected()
	{
		if (ivjradioCorrected == null)
		{
			try
			{
				ivjradioCorrected = new JRadioButton();
				ivjradioCorrected.setName("ivjradioCorrected");
				ivjradioCorrected.setBounds(21, 50, 184, 22);
				ivjradioCorrected.setText(CORRECTED);
				// user code begin {1}
				ivjradioCorrected.setMnemonic(KeyEvent.VK_T);
				ivjradioCorrected.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCorrected;
	}

	/**
	 * Return the ivjradioNonTitled property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioNonTitled()
	{
		if (ivjradioNonTitled == null)
		{
			try
			{
				ivjradioNonTitled = new JRadioButton();
				ivjradioNonTitled.setName("ivjradioNonTitled");
				ivjradioNonTitled.setBounds(21, 116, 108, 22);
				ivjradioNonTitled.setText(NON_TITLED);
				// user code begin {1}
				ivjradioNonTitled.setMnemonic(KeyEvent.VK_N);
				ivjradioNonTitled.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioNonTitled;
	}

	/**
	 * Return the ivjradioOriginal property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioOriginal()
	{
		if (ivjradioOriginal == null)
		{
			try
			{
				ivjradioOriginal = new JRadioButton();
				ivjradioOriginal.setName("ivjradioOriginal");
				ivjradioOriginal.setBounds(21, 19, 108, 22);
				ivjradioOriginal.setText(ORIGINAL);
				getFocusComponent(this);
				// user code begin {1}
				ivjradioOriginal.addActionListener(this);
				ivjradioOriginal.setMnemonic(KeyEvent.VK_O);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioOriginal;
	}

	/**
	 * Return the ivjradioRegistration property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioRegistration()
	{
		if (ivjradioRegistration == null)
		{
			try
			{
				ivjradioRegistration = new JRadioButton();
				ivjradioRegistration.setName("ivjradioRegistration");
				ivjradioRegistration.setBounds(21, 83, 207, 22);
				ivjradioRegistration.setText(REGISTRATION_PURPOSE_ONLY);
				// user code begin {1}
				ivjradioRegistration.setMnemonic(KeyEvent.VK_R);
				ivjradioRegistration.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioRegistration;
	}

	/**
	 * Returns the selected radioButton.
	 * 
	 * @return int
	 */
	public int getSelectedRadioBtn()
	{
		int liRtn = -1;

		if (getradioOriginal().isSelected())
		{
			liRtn = TitleTypes.INT_ORIGINAL;
		}
		else if (getradioCorrected().isSelected())
		{
			liRtn = TitleTypes.INT_CORRECTED;
		}
		else if (getradioNonTitled().isSelected())
		{
			liRtn = TitleTypes.INT_NONTITLED;
		}
		else if (getradioRegistration().isSelected())
		{
			liRtn = TitleTypes.INT_REGPURPOSE;
		}
		return liRtn;
	}

	/**
	 * Return the ivjstcLblRecordFound property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRecordFound()
	{
		if (ivjstcLblRecordFound == null)
		{
			try
			{
				ivjstcLblRecordFound = new JLabel();
				ivjstcLblRecordFound.setName("ivjstcLblRecordFound");
				ivjstcLblRecordFound.setBounds(110, 9, 118, 14);
				ivjstcLblRecordFound.setText(NO_RECORD_FOUND);
				ivjstcLblRecordFound.setMinimumSize(
					new java.awt.Dimension(90, 14));
				ivjstcLblRecordFound.setMaximumSize(
					new java.awt.Dimension(90, 14));
				// user code begin {1}
				ivjstcLblRecordFound.setForeground(java.awt.Color.red);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblRecordFound;
	}

	/**
	 * Return the ivjstcLblTitleTransactionDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTitleTransactionDate()
	{
		if (ivjstcLblTitleTransactionDate == null)
		{
			try
			{
				ivjstcLblTitleTransactionDate = new JLabel();
				ivjstcLblTitleTransactionDate.setName(
					"ivjstcLblTitleTransactionDate");
				ivjstcLblTitleTransactionDate.setDisplayedMnemonic(
					KeyEvent.VK_D);
				ivjstcLblTitleTransactionDate.setBounds(
					23,
					268,
					133,
					23);
				ivjstcLblTitleTransactionDate.setText(
					TITLE_TRANSACTION_DATE);
				ivjstcLblTitleTransactionDate.setMinimumSize(
					new java.awt.Dimension(127, 14));
				ivjstcLblTitleTransactionDate.setMaximumSize(
					new java.awt.Dimension(127, 14));

				// user code begin {1}
				ivjstcLblTitleTransactionDate.setLabelFor(
					gettxtTitleTransDate());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTitleTransactionDate;
	}

	/**
	 * Handles the display of radio button choices based on the 
	 * condition and value of the Stolen Waived check box.
	 */
	private void handleChkBoxDisplay()
	{
		if (getchkStolenWaived().isVisible()
			&& getchkStolenWaived().isSelected())
		{
			getradioRegistration().setEnabled(false);
			getradioNonTitled().setEnabled(false);
			if (getchkOffHighway().isVisible() && getchkOffHighway().isSelected())
			{
				getchkOffHighway().setSelected(false); 
			}
		}
		else if (
			getchkStolenWaived().isVisible()
				&& !getchkStolenWaived().isSelected())
		{
			getradioRegistration().setEnabled(true);
			getradioNonTitled().setEnabled(true);
		}
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
	 * Handles the enabling/disabling of the check boxes based on 
	 * the radio button selected.
	 */
	private void handleRadioBtnDisplay()
	{
		if (getradioOriginal().isSelected())
		{
			getchkOffHighway().setEnabled(true);
			if (getchkStolenWaived().isVisible())
			{
				getchkStolenWaived().setEnabled(true);
			}
		}
		else if (getradioCorrected().isSelected())
		{
			// defect 10416 
			// Reset to Original Setting
			// Disable checkbox for Corrected Title 
			getchkOffHighway().setSelected(cbOrigOffHiway);
			getchkOffHighway().setEnabled(false);
			// end defect 10416 

			if (getchkStolenWaived().isVisible())
			{
				getchkStolenWaived().setEnabled(true);
			}
		}
		else if (getradioRegistration().isSelected())
		{
			// defect 10453 
			getchkOffHighway().setSelected(false);
			getchkOffHighway().setEnabled(false);
			// end defect 10453 
			getchkStolenWaived().setSelected(false);
			getchkStolenWaived().setEnabled(false);
		}
		else if (getradioNonTitled().isSelected())
		{
			getchkOffHighway().setSelected(false);
			getchkOffHighway().setEnabled(false);
			getchkStolenWaived().setSelected(false);
			getchkStolenWaived().setEnabled(false);
		}
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setSize(338, 379);
			setModal(true);
			setTitle(ScreenConstant.TTL002_FRAME_TITLE);
			setContentPane(getFrmTitleTypesTTL002ContentPane1());
			setName(ScreenConstant.TTL002_FRAME_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);

			// user code begin {1}
			caEtched = BorderFactory.createEtchedBorder();
			getradioOriginal().setSelected(true);
			// default title transaction date to current date
			// defect 10949
			// As-of No Default Title Trans Date start date, 
			// do not default Title Transaction Date
			//gettxtTitleTransDate().setDate(RTSDate.getCurrentDate());
			// end defect 10949
			addFocusListener(this);
			// user code end
		}
		catch (Throwable leException)
		{
			handleException(leException);
		}
		// user code begin {2}
		addWindowListener(this);
		// user code end
	}

	/**
	 * Return boolean reflecting whether transaction code is of 
	 * "DTA" type
	 */
	private boolean isDTA()
	{
		return UtilityMethods.isDTA(csTransCd);

	}

	/**
	 * Return boolean reflecting whether transaction code is REJCOR 
	 */
	private boolean isRejCor()
	{
		return csTransCd.equals(TransCdConstant.REJCOR);
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
		if (aaIE.getSource() == getchkOffHighway() && getchkOffHighway().isSelected())
		{
			if (getchkStolenWaived().isVisible() && getchkStolenWaived().isSelected())
			{
				getchkStolenWaived().setSelected(false);
				handleChkBoxDisplay(); 
			}
		}
		else if (aaIE.getSource() == getchkStolenWaived() && getchkStolenWaived().isSelected())
		{
			if (getchkOffHighway().isVisible() && getchkOffHighway().isSelected())
			{
				getchkOffHighway().setSelected(false); 
			}
		}
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
		try
		{
			// defect 10290 
			// Do not process RTSException 
			if (aaDataObject != null
				&& !(aaDataObject instanceof RTSException))
			{
				// defect 10035 
				csTransCd = getController().getTransCode();
				// end defect 10035 

				// Begin BIZARRE  
				// TODO Coming Back to this in 6.3.0 or after (KPH) 
				if (caOrigObject == null)
				{
					caOrigObject = aaDataObject;
				}

				// From DTA only
				//	if (aaDataObject instanceof RTSException)
				//	{
				//		//grab object from vault
				//		aaDataObject =
				//			getController().getMediator().openVault(
				//				ScreenConstant.TTL002);
				//		}
				//	else
				//	{
				// end defect 10290 

				//If !DTA, use original vehInqData from MF
				Object aaDataObjRef = aaDataObject;
				aaDataObject = caOrigObject;

				// check if record is not applicable
				if (aaDataObjRef != null)
				{
					VehicleInquiryData aaVehInqDataForRcdNotApp =
						(VehicleInquiryData) aaDataObjRef;

					if (aaVehInqDataForRcdNotApp.getValidationObject()
						!= null)
					{
						TitleValidObj laTtlValidObjForRcdNotApp =
							(TitleValidObj) aaVehInqDataForRcdNotApp
								.getValidationObject();

						if (laTtlValidObjForRcdNotApp
							.isRecordNotApplicable())
						{
							aaDataObject = aaDataObjRef;
						}
					}
				}
				//}

				// TODO This will always return false 
				if (!checkAndDoDlrTtl(aaDataObject))
				{
					caVehInqData = (VehicleInquiryData) aaDataObject;
				}
				// End BIZARRE

				// defect 10035 
				// Error if REJCOR && invalid TtlProcsCd  
				if (isRejCor()
					&& !TitleClientBusiness
						.isCorrectTitleRejectionAllowed(
						caVehInqData
							.getMfVehicleData()
							.getTitleData()
							.getDocTypeCd(),
						caVehInqData
							.getMfVehicleData()
							.getTitleData()
							.getTtlProcsCd()))
				{
					//new RTSException(17
					new RTSException(
						ErrorsConstant
							.ERR_NUM_CANNOT_PERFORM_REJCOR)
							.displayError(
						this);
					cbShutDown = true;
				}
				else
				{
					if (caVehInqData.isRecordFound())
					{
						// Record Found 
						getstcLblRecordFound().setText(RECORD_FOUND);

						// Non-Titled Radio  		
						getradioNonTitled().setEnabled(!isRejCor());

						// Corrected-Title Radio 
						getradioCorrected().setEnabled(
							caVehInqData
								.getMfVehicleData()
								.getTitleData()
								.getDocTypeCd()
								!= DocTypeConstant.NON_TITLED_VEHICLE);

						// Off-Highway Checkbox
						// defect 10416  
						cbOrigOffHiway =
							caVehInqData
								.getMfVehicleData()
								.getTitleData()
								.getDocTypeCd()
								== DocTypeConstant.OFF_HIGHWAY_USE_ONLY;

						getchkOffHighway().setSelected(cbOrigOffHiway);
						// 	caVehInqData
						//		.getMfVehicleData()
						//		.getTitleData()
						//		.getDocTypeCd()
						//		== DocTypeConstant.OFF_HIGHWAY_USE_ONLY);
						// end defect 10416

						// Stolen Checkbox 
						getchkStolenWaived().setVisible(false);
					}
					else
					{
						// Label 
						getstcLblRecordFound().setText(NO_RECORD_FOUND);

						// Corrected-Title Radio 
						getradioCorrected().setEnabled(false);
					}

					// If REJCOR, remove title transaction date 
					if (isRejCor())
					{
						getstcLblTitleTransactionDate().setVisible(
							false);
						gettxtTitleTransDate().setVisible(false);
					}
					//  If DTA, remove title transaction date,  
					//	disable corrected and RPO radio buttons  
					//  Note:  Even keyboard is DTAORD now 
					else if (csTransCd.equals(TransCdConstant.DTAORD))
					{
						getstcLblTitleTransactionDate().setVisible(
							false);
						gettxtTitleTransDate().setVisible(false);
						getradioCorrected().setEnabled(false);
						getradioRegistration().setEnabled(false);
						getchkStolenWaived().setEnabled(false);
						getchkStolenWaived().setVisible(false);
						doDlrTtl();
					}
					// end defect 10035 
					

					// defect 8494
					// Moved from windowOpened
					if (getradioNonTitled().isSelected())
					{
						setDefaultFocusField(getradioNonTitled());
						cbSelectNonTtl = false;
					}
					// end defect 8494
				}
				// defect 10949
				// As-of No Default Title Trans Date start date, 
				// do not default Title Transaction Date
				// RejCor has date hidden, but still edits for date
				// DTA needs data in date field for validateTtlTransDate() 
				if (isRejCor() || 
					UtilityMethods.isDTA(csTransCd) ||
					RTSDate.getCurrentDate().getYYYYMMDDDate()
						< SystemProperty.getNoDefaultTtlTransDtStartDate().
							getYYYYMMDDDate())
				{
					gettxtTitleTransDate().setDate(RTSDate.getCurrentDate());
				}
				// end defect 10949
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTsEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leRTsEx.displayError(this);
			leRTsEx = null;
		}
		catch (NumberFormatException aeNFEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNFEx);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
	}

	/**
	 * This method copies the data of current screen to a new copy 
	 * object 
	 * The data is copied using java serialization. The copy object is 
	 * the one which is sent to the next screen so as to retain the 
	 * original data object with the current screen.
	 * 
	 * @return Object
	 */
	private Object setDataToDataObject()
	{
		// defect 10592 
		// Clear Saved Owner/Lienholder Data if modify from/to Corrected 
		if ((cbCorTtl && !getradioCorrected().isSelected())
			|| (getradioCorrected().isSelected() && !cbCorTtl))
		{
			getController().getMediator().closeVault(
				ScreenConstant.TTL007,
				null);

			getController().getMediator().closeVault(
				ScreenConstant.TTL035,
				null);
		}
		// end defect 10592 
		
		VehicleInquiryData laCopyVehInqData =
			(VehicleInquiryData) UtilityMethods.copy(caVehInqData);

		// create title validation object, create copy of orig 
		// mfvehicledata object
		// and store to be used for fees later
		TitleValidObj laValidObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		if (laValidObj == null)
		{
			laValidObj = new TitleValidObj();
			caVehInqData.setValidationObject(laValidObj);
		}
		// defect 9085 
		laValidObj.setChangeRegis(false);
		// end defect 9085  

		MFVehicleData laMfVehData = laCopyVehInqData.getMfVehicleData();

		//Save copy of dlrttlData
		DealerTitleData laDlrTtlData =
			(DealerTitleData) laValidObj.getDlrTtlData();
		Object laCopyMfVehData = UtilityMethods.copy(laMfVehData);
		laValidObj.setMfVehOrig(laCopyMfVehData);
		laCopyVehInqData.setValidationObject(laValidObj);

		// Determine which radio button is selected and set the 
		//	appropriate fields
		laCopyVehInqData
			.getMfVehicleData()
			.getTitleData()
			.setTtlTypeIndi(
			getSelectedRadioBtn());

		// defect 10592 
		cbCorTtl = getradioCorrected().isSelected();
		// end defect 10592 

		switch (getSelectedRadioBtn())
		{
			case TitleTypes.INT_ORIGINAL :
				{
				}
			case TitleTypes.INT_REGPURPOSE :
				{
					laCopyVehInqData
						.getMfVehicleData()
						.getRegData()
						.setClaimComptCntyNo(
						0);
					laCopyVehInqData
						.getMfVehicleData()
						.getTitleData()
						.setVehSoldDate(
						0);
					laCopyVehInqData
						.getMfVehicleData()
						.getRegData()
						.setRenwlMailRtrnIndi(
						0);
					laCopyVehInqData
						.getMfVehicleData()
						.getRegData()
						.setEmissionSourceCd(
						CommonConstant.STR_SPACE_EMPTY);
					break;
				}
			case TitleTypes.INT_NONTITLED :
				{
					laCopyVehInqData
						.getMfVehicleData()
						.getRegData()
						.setClaimComptCntyNo(
						0);
					laCopyVehInqData
						.getMfVehicleData()
						.getTitleData()
						.setVehSoldDate(
						0);
					laCopyVehInqData
						.getMfVehicleData()
						.getRegData()
						.setRenwlMailRtrnIndi(
						0);
					laCopyVehInqData
						.getMfVehicleData()
						.getRegData()
						.setEmissionSourceCd(
						CommonConstant.STR_SPACE_EMPTY);
					laCopyVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.setVehClassCd(
						MISC);
					laCopyVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.setDisableVehClassIndi(1);
					break;
				}
		}
		// defect 10035
		// defect 10416
		// Removed isEnabled() as is disabled for Corrected Title    
		int liOffHwy =
			// getchkOffHighway().isEnabled() &&
	getchkOffHighway().isSelected() ? 1 : 0;
		// end defect 10416 

		laCopyVehInqData
			.getMfVehicleData()
			.getRegData()
			.setOffHwyUseIndi(
			liOffHwy);
		// end defect 10035 

		if (getchkStolenWaived().isEnabled()
			&& getchkStolenWaived().isSelected())
		{
			//defect 6029, 6090, 6134, 6312
			//Redundant code replacing with RegWaivedIndi in 
			//FrmClassPlateStickerTypeREG008
			//		validObj.setStolenWaivedIndi(1);
			laCopyVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setTtlExmnIndi(
				1);

			//Added code for setRegWaivedIndi which replaced 
			//	setStolenWaivedIndi
			//and commented code for setDpsStlnIndi which is already set
			//	 to 1.
			laCopyVehInqData
				.getMfVehicleData()
				.getRegData()
				.setRegWaivedIndi(
				1);

			//	aCopyVehInqData.getMfVehicleData().getVehicleData().
			//	setDpsStlnIndi(1);
		}
		else
		{
			//laValidObj.setStolenWaivedIndi(0);
			laCopyVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setTtlExmnIndi(
				0);
		}

		// Set RTSEffDate in Vehicle Inquiry Data
		int liTtlTransDt = RTSDate.getCurrentDate().getYYYYMMDDDate();

		if (gettxtTitleTransDate().isVisible())
		{
			liTtlTransDt =
				gettxtTitleTransDate().getDate().getYYYYMMDDDate();
		}

		if (laValidObj.getDlrTtlData() != null)
		{
			String lsTransDt =
				((DealerTitleData) laValidObj.getDlrTtlData())
					.getTransDate();
			int liTransDt = 0;

			try
			{
				liTransDt = Integer.parseInt(lsTransDt);
				liTtlTransDt = liTransDt;
			}
			catch (NumberFormatException aeNFE)
			{
				liTtlTransDt =
					RTSDate.getCurrentDate().getYYYYMMDDDate();
			}
		}
		laCopyVehInqData.setRTSEffDt(liTtlTransDt);

		if (laDlrTtlData != null)
		{
			laValidObj.setDlrTtlData(laDlrTtlData);
		}

		// defect 9971 
		// Removed DocTypeCd assignment
		// Now in TTL007 
		// end defect 9971 
		return laCopyVehInqData;
	}

	/**
	 * Validates the Title Transaction Date.
	 * Date entered cannot be greater than todays date
	 * or more than 120 days ago
	 * 
	 * @return boolean
	 */
	private boolean validateTtlTransDate()
	{
		boolean lbValid = true;
		if ((RTSDate
			.getCurrentDate()
			.compareTo(gettxtTitleTransDate().getDate())
			== 1)
			&& ((RTSDate.getCurrentDate().add(RTSDate.DATE, -120))
				.compareTo(gettxtTitleTransDate().getDate())
				== -1))
		{
			lbValid = false;
		}
		return lbValid;
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowActivated(java.awt.event.WindowEvent aaWE)
	{
		if (cbShutDown)
		{
			getController().processData(
				AbstractViewController.CANCEL,
				getController().getData());
			cbShutDown = false;
			setVisibleRTS(false);
		}
	}
}
