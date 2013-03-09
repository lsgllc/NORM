package com.txdot.isd.rts.client.common.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.event.BarCodeEvent;
import com.txdot.isd.rts.services.util.event.BarCodeListener;

/*
 * FrmVinKeySelectionKEY006.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Joe Peters   09/12/2001	Added Comments and intitialized screen items
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()       
 * MAbs			04/19/2002 	Owner ID must be exactly 9 digits 
 * 							defect 3589
 * T Pederson   04/30/2002  Don't allow spaces in input field
 * J Rue		07/02/2002	Display last 4 digits of DlrDocNo. 
 * 							add setDlrDocNo4()
 * 							modify setData()
 * 							defect 4390 
 * J Rue		08/07/2002	Remove the line that sets the 
 * 							VIN to a blank. 
 * 							modify actionPerformed()
 * 							defect 4550 
 * J Rue		08/21/2002	Title "VIN Key Selection KEY006" with the 
 * 							acronym VIN is set to capital letters, VIN.
 * 							defect 4644
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 * 							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Made changes for the 
 * 							user help guide so had to make changes
 *				12/23/2002	in actionPerformed().
 *							defect 5147 
 * J Rue		02/14/2003	Remove check in NOVIN box if 
 * 							cancel or escape from COA. 
 * 							modify actionPerformed().
 * 							defect 5377 
 * S Govindappa 03/03/2003  Added code 
 * 							actionPerformed(..) to clear the VIN number 
 * 							on KEY006 screen on enter from TTL015 screen
 * 							in COA event.
 * 							defect 5646 
 * J Rue		03/11/2003	Defect 5620, Modify check for DocNo char 
 * 							length >= 4, get DocNo, esle set to blank. 
 * 							method setDlrDocNo4().
 * Ray Rowehl	04/30/2003  Correct problems preventing entering of VIN 
 *							after coming back from salvage.
 *							clean up flow of actionPerformed and reset.
 *							add some tabbing control to setData for HQ.
 *							defect 5933
 * Min Wang 	05/08/2003  Defect 6106. Modified setData() to correct 
 * 							same vehicle enabled when it is HQ.
 * Ray Rowehl	06/09/2003  Defect 6174. Lengthen ownerId field to 9.
 * Bob Brown	09/18/2003  Commented out the request focus for the vin 
 * 							in 2 places in actionPerformed
 *                          to keep focus on the enter button when 
 * 							returning to FRM...KEY006. This helps 
 * 							tabbing work when the class file is deployed.
 *							defect 6526  Ver 5.1.6
 * Ray Rowehl	01/05/2004	Refer to constants and static methods as 
 * 							static. Formatted code as well.
 * 							modified actionPerformed(), processEnter()
 * 							defect 6596  Ver 5.1.5 fix 2 (actually 5.1.6)
 * Min Wang		05/05/2004	Fixed double cursors on screen.
 *							modify actionPerformed()
 *							defect 7033 Ver 5.1.6
 * Ray Rowehl	02/08/2005	Changed package import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	02/18/2005	Move reference for CommonClientUIConstants
 * 							defect 7705 Ver 5.2.3
 * B Hargrove	03/15/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * Min Wang		03/28/2005	Modify to handle focus more properly for
 * 							Java 1.4.  Remove references to 
 * 							setNextFocusableComponent().
 * 							Use Panels to control tabbing.
 * 							defect 7011,7012  Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * S Johnston	06/27/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 *							delete keyPressed
 * 							modify getButtonPanel1
 * 							defect 8240 Ver 5.2.3   
 * T Pederson	08/09/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/30/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/27/2005	Disable key entry fields when either 
 * 							checkboxes are selected. Handle arrow key
 * 							movement between checkboxes.
 * 							Removed "setRequestFocusEnabled(false)" 
 * 							add ItemListener
 * 							add itemStateChanged(),keyPressed()
 * 							modify actionPerformed(),getButtonPanel1(),
 * 							getchkSame(), getchkNoVin(), initialize(),
 * 							setData()
 * 							rename ciWkstnId to ciOfcIssuanceCd
 *  						defect 8485 Ver 5.2.3
 * K Harrell	01/24/2006	Align checkboxes w/ entry fields
 * 							defect 7885 Ver 5.2.3  
 * Jeff S.		02/02/2006	Moved forcus to the default field everytime 
 * 							we go through actionPerformed().  This will
 * 							make the focus be in the correct field every
 * 							time we return to this screen.
 * 							modify actionPerformed(), setData()
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		04/11/2006	When we are clearing multiple screens (ie.
 * 							lien & salvage) and returning to this screen
 * 							focus is taken from the Vin field and forced
 * 							to the enter field.  Thought is was mediator
 * 							but we are setting focus after mediator and
 * 							something else is taking focus after that.
 * 							add focusLost(), focusGained(), 
 * 								cbRequestedDefaultFocus
 * 							modify actionPerformed(), gettxtVin(), 
 * 								keyPressed()
 * 							defect 8671 Ver 5.2.3
 * Jeff S.		04/18/2006	Added a check for 
 * 								gettxtApplicant().isEnabled() when 
 * 								tabbing out of the Vin field b/c in 
 * 								title this field is visble but disabled.
 * 							modify keyPressed()
 * 							defect 8671 Ver 5.2.3
 * K Harrell	04/25/2006	Removed GridBagLayout from ContentPane1
 * 							through Visual Editor.  Primary change in 
 * 							Content Panel.
 * 							modify getFrmVinKeySelectionKEY006ContentPane1()
 * 							defect 8731 Ver 5.2.3 
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify processEnter()
 * 							defect 8902 Ver Exempts
 * K Harrell	04/06/2007	CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/07/2007	Use SystemProperty.isHQ()
 * 							modify setData(), itemStateChanged()
 * 							defect 9085 Ver Special Plates
 * J Rue		12/17/2007	Do not allow Same Vehicle for DTA
 * 							modify setData()
 * 							defect 8582 Ver Defect POS A
 * K Harrell	04/25/2008	Add checkbox for Salvage Processing to 
 * 							save Applicant Id for next request.
 * 							add TXT_SAVE_ID 
 * 							add ivjchkSaveId
 * 							add getchkSaveId()
 * 							modify actionPerformed(), setData(),
 * 							  getSelectionPanel() 
 * 							defect 9635 Ver Defect POS A  
 * K Harrell	05/13/2008	Use SCOT vs. SLVG 
 * 							modify setData(), actionPerformed(),
 * 							processEnter()
 * 							defect 9636 Ver Defect POS A
 * K Harrell	05/13/2008	Remove references to COA 
 * 							modify actionPerformed(), setData()  
 * 							defect 9642 Ver Defect POS A
 * K Harrell	06/09/2008	Updated for new Title Constants
 * 							modify actionPerformed()
 * 							defect 9635 Ver Defect POS A
 * K Harrell	08/28/2008	Correct tabbing 
 * 							modify keyPressed()
 * 							defect 9065 Ver Defect POS B 
 * K Harrell	06/05/2009	Throw error if Applicant Id is 0. Use Error
 * 							Constants. Additional class cleanup.
 * 							Note: Further cleanup required. 
 * 							add caDlrTtlData
 * 							delete ciDlrTtlData
 * 							delete setDlrTitlDataObjs(),
 * 							 setDlrDataContainer() 
 * 							modify actionPerformed()
 * 							defect 10003 Ver Defect_POS_F
 * J Zwiener	06/26/2009	delete MAX_VIN_NO,BOTH_ADDL_AND_CUMUL
 * 							defect 10091 Ver Defect_POS_F
 * K Harrell 	12/16/2009 	Modify to accommodate new DTA coding, i.e. 
 *        					single DlrTtlData object vs. vector.
 * 							Additional cleanup. 
 * 							add buildGSD()
 *  						delete csOwnerId, caDlrDataContainer, 
 * 							  cvDlrTitlDataObjs,  getDlrDataContainer(), 
 * 							  getDlrTitlDataObjs()
 * 							modify actionPerformed(), setData()
 * 							refactor caVehData to caMFVehData
 * 							refactor caInquiryData to csSavedVehInqData
 *        					defect 10290 Ver Defect_POS_H
 * Min Wang		04/05/2010	Only set focus on entry field if this is in 
 * 							Headquarters.
 * 							modify	actionPerformed()
 * 							defect 10426 Ver POS_640
 * R Pilon		06/13/2012  Change to only log the exception when attempting to 
 * 							initialize the BarCodeScanner.
 * 							modify setData(Object)
 * 							defect 11071 Ver 7.0.0
 * R Pilon		08/23/2012	Comment out code in focusLost method attempting to 
 * 							  request foces in the VIN input.  This is causing
 * 							  focus to be "stuck" in the VIN input even after
 * 							  displaying TTL016 preventing TTL016 from getting
 * 							  focus.  This bug was introduce with the migration 
 * 							  to Java 1.7.
 * 							defect 11437 Ver 7.0.0
 * ---------------------------------------------------------------------
 */
/**
 * Frame Vin Key Selection KEY006
 * 
 * @version	POS_700			08/23/2012
 * @author	Joseph Peters
 * <br>Creation Date:		08/21/2001 09:14:56
 */
public class FrmVinKeySelectionKEY006
	extends RTSDialogBox
	implements BarCodeListener, ActionListener, ItemListener, FocusListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkNoVin = null;
	private JCheckBox ivjchkSame = null;
	private JCheckBox ivjchkSaveId = null;
	private JPanel ivjFrmVinKeySelectionKEY006ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjSelectionPanel = null;
	private JLabel ivjstcLblApplicant = null;
	private JLabel ivjstcLblLast4 = null;
	private JLabel ivjstcLblVin = null;
	private RTSInputField ivjtxtApplicant = null;
	private RTSInputField ivjtxtLast4 = null;
	private RTSInputField ivjtxtVin = null;

	// boolean 
	private boolean cbRequestedDefaultFocus = false;

	// String 
	private String csTransCd;
	private String csVin;
	private static String csLast4Digits = null;

	// Object
	private DealerTitleData caDlrTtlData = null;
	private MFVehicleData caMFVehData = new MFVehicleData();
	private VehicleInquiryData caSavedVehInqData =
		new VehicleInquiryData();

	// Constant 
	private final static String FRM_NAME_KEY006 =
		"FrmVinKeySelectionKEY006";
	private final static String FRM_TITLE_KEY006 =
		"VIN Key Selection     KEY006";
	private final static String TXT_APPL_OWNR_ID =
		"Applicant Owner Id:";
	private final static String TXT_LAST_4_DIGIT_DOC_NO =
		"Last 4 digits of Document No:";
	private final static String TXT_NO_VIN = "No VIN";
	private final static String TXT_SAME_VEH = "Same Vehicle";
	private final static String TXT_SAVE_ID = "Save Id";
	private final static String TXT_VIN = "VIN:";

	/** 
	 * Return Last4Digits 
	 * 
	 * @return String 
	 */
	public static String getLast4Digits()
	{
		return csLast4Digits;
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmVinKeySelectionKEY006 laFrmVinKeySelectionKey006;
			laFrmVinKeySelectionKey006 = new FrmVinKeySelectionKEY006();
			laFrmVinKeySelectionKey006.setModal(true);
			laFrmVinKeySelectionKey006
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmVinKeySelectionKey006.show();
			Insets laInsets = laFrmVinKeySelectionKey006.getInsets();
			laFrmVinKeySelectionKey006.setSize(
				laFrmVinKeySelectionKey006.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmVinKeySelectionKey006.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmVinKeySelectionKey006.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Creates a FrmVinKeySelectionKey006
	 */
	public FrmVinKeySelectionKEY006()
	{
		super();
		initialize();
	}

	/**
	 * Creates a FrmVinKeySelectionKey006
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmVinKeySelectionKEY006(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Creates a FrmVinKeySelectionKey006
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmVinKeySelectionKEY006(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Creates a FrmVinKeySelectionKey006
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmVinKeySelectionKEY006(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked for Enter/Cancel/Help
	 * 
	 * @param ActionEvent aaAE
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

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10290 
				if (getchkNoVin().isSelected())
				{
					getController().processData(
						VCVinKeySelectionKEY006.PLATE_KEY,
						caDlrTtlData);
				}
				else if (getchkSame().isSelected())
				{
					// This was missed in defect 8596 
					// Should not reset until new transaction completed 
					//	CommonClientBusiness laCommonClientBusiness =
					//		new CommonClientBusiness();
					//	laCommonClientBusiness.processData(
					//		GeneralConstant.COMMON,
					//		CommonConstant.CLEAR_SAVE_VEH_INFO,
					//		null);
					//	Transaction.setCumulativeTransIndi(0);
					
					getController().processData(
						VCVinKeySelectionKEY006.SINGLE_REC,
						caSavedVehInqData);
				}
				else
				{
					csVin = gettxtVin().getText();

					csVin =
						CommonValidations.convert_i_and_o_to_1_and_0(
							csVin);

					ivjtxtVin.setText(csVin);

					if (validateData())
					{
						if (gettxtVin().getText().length()
							!= CommonConstant.LENGTH_VIN)
						{
							// 555
							new RTSException(
								ErrorsConstant
									.ERR_NUM_VIN_NOT_17_DIGITS)
									.displayError(
								this);
						}
						csLast4Digits = gettxtLast4().getText();

						GeneralSearchData laGSD = buildGSD();

						// Clear Vin now as SCOT will return 
						if (csTransCd.equals(TransCdConstant.SCOT))
						{
							gettxtVin().setText(
								CommonConstant.STR_SPACE_EMPTY);

							if (!getchkSaveId().isSelected())
							{
								gettxtApplicant().setText(
									CommonConstant.STR_SPACE_EMPTY);
							}
						}

						getButtonPanel1().getBtnEnter().setSelected(
							false);

						getController().processData(
							AbstractViewController.ENTER,
							laGSD);
						// end defect 10290 
					}
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
				// TODO Revisit later  (KPH) 
				String lsTransCd = csTransCd;

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
				// defect 10290
				// No DTANTK at this point
				if (lsTransCd.equals(TransCdConstant.DTAORK))
					//|| lsTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.KEY006B);
				}
				// No DTANTD at this point 
				else if (
					lsTransCd.equals(TransCdConstant.DTAORD))
						//|| lsTransCd.equals(TransCdConstant.DTANTD))
				{
					RTSHelp.displayHelp(RTSHelp.KEY006C);
				}
				else if (csTransCd.equals(TransCdConstant.SCOT))
				{
					RTSHelp.displayHelp(RTSHelp.KEY006E);
				}
				// Reorganize 
				//   - No NONTTL at this point
				//   - No need to check else !isMFUP()
				else if (
					csTransCd.equals(TransCdConstant.TITLE)
						//|| lsTransCd.equals(TransCdConstant.NONTTL)
						|| csTransCd.equals(TransCdConstant.REJCOR))
				{
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.KEY006A);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.KEY006F);
					}

				}
				// end defect 10290 
			}
		}
		finally
		{
			doneWorking();
			// defect 8671
			// Added the use of the boolean b/c enter button is stealing
			// the focus away from the vin text field when clearing
			// multiple screens in a salvage event.
			// defect 7885
			// Move focus to the txt vin field anytime you go 
			// through action performed for Help, Enter, Cancel
			// defect 10426
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter()
				&& gettxtVin() != null
				&& gettxtVin().isEnabled()
				&& SystemProperty.isHQ())
			{
				// end defect 10426
				cbRequestedDefaultFocus = true;
				gettxtVin().requestFocus();
			}
			// end defect 7885
			// end defect 8671
		}
	}

	/**
	 * Used to capture Bar Code Event
	 * 
	 * @param BarCodeEvent aaBCE
	 */
	public void barCodeScanned(BarCodeEvent aaBCE)
	{
		if (aaBCE.getBarCodeData() instanceof RenewalBarCodeData)
		{
			RenewalBarCodeData laData =
				(RenewalBarCodeData) aaBCE.getBarCodeData();
			gettxtVin().setText(laData.getVin());
		}
	}

	/**
	 * Used to handle focus gained events.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		// Empty FocusGained
	}

	/**
	 * Used to handle focus lost events.
	 * Added to handle the problem with the enter button is stealing
	 * focus from the vin text field when returning from multiple
	 * screens during salvage.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		// If focus has been moved from the Vin field and we where
		// trying to force it there then put it back.
		if (aaFE.getSource() == gettxtVin() && cbRequestedDefaultFocus)
		{
			// defect 11437
//			gettxtVin().requestFocus();
			// end defect 11437
			cbRequestedDefaultFocus = false;
		}
	}

	/**
	 * Return the ivjButtonPanel1 property value.
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
				ivjButtonPanel1.setBounds(122, 134, 332, 51);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
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
	 * Return the ivjchkNoVin property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkNoVin()
	{
		if (ivjchkNoVin == null)
		{
			try
			{
				ivjchkNoVin = new JCheckBox();
				ivjchkNoVin.setBounds(24, 12, 62, 21);
				ivjchkNoVin.setName("ivjchkNoVin");
				ivjchkNoVin.setText(TXT_NO_VIN);
				ivjchkNoVin.setActionCommand(TXT_NO_VIN);
				// user code begin {1}
				ivjchkNoVin.setMnemonic(KeyEvent.VK_V);
				ivjchkNoVin.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkNoVin;
	}

	/**
	 * Return the ivjchkSame property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSame()
	{
		if (ivjchkSame == null)
		{
			try
			{
				ivjchkSame = new JCheckBox();
				ivjchkSame.setBounds(24, 45, 103, 21);
				ivjchkSame.setName("ivjchkSame");
				ivjchkSame.setText(TXT_SAME_VEH);
				ivjchkSame.setMaximumSize(new Dimension(103, 22));
				ivjchkSame.setMinimumSize(new Dimension(103, 22));
				ivjchkSame.setActionCommand(TXT_SAME_VEH);
				// user code begin {1}
				ivjchkSame.setMnemonic(KeyEvent.VK_M);
				ivjchkSame.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSame;
	}

	/**
	 * This method initializes ivjchkSaveId
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSaveId()
	{
		if (ivjchkSaveId == null)
		{
			try
			{
				ivjchkSaveId = new JCheckBox();
				ivjchkSaveId.setSize(93, 21);
				ivjchkSaveId.setLocation(24, 78);
				ivjchkSaveId.setText(TXT_SAVE_ID);
				// user code begin {1}			
				ivjchkSaveId.setMnemonic(KeyEvent.VK_I);
				ivjchkSaveId.addItemListener(this);
				// user code end 
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkSaveId;
	}

	/**
	 * Get Dealer Title Data
	 * 
	 * @return DealerTitleData
	 */
	public DealerTitleData getDlrTtlData()
	{
		return caDlrTtlData;
	}

	/**
	 * Return the ivjFrmVinKeySelectionKEY006ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmVinKeySelectionKEY006ContentPane1()
	{
		if (ivjFrmVinKeySelectionKEY006ContentPane1 == null)
		{
			try
			{
				ivjFrmVinKeySelectionKEY006ContentPane1 = new JPanel();
				ivjFrmVinKeySelectionKEY006ContentPane1.setName(
					"ivjFrmVinKeySelectionKEY006ContentPane1");
				ivjFrmVinKeySelectionKEY006ContentPane1.setLayout(null);
				ivjFrmVinKeySelectionKEY006ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmVinKeySelectionKEY006ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmVinKeySelectionKEY006ContentPane1.add(
					getSelectionPanel(),
					null);
				ivjFrmVinKeySelectionKEY006ContentPane1.setMaximumSize(
					new Dimension(500, 200));
				ivjFrmVinKeySelectionKEY006ContentPane1.setMinimumSize(
					new Dimension(500, 200));
				ivjFrmVinKeySelectionKEY006ContentPane1.setBounds(
					0,
					0,
					0,
					0);
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
		return ivjFrmVinKeySelectionKEY006ContentPane1;
	}

	/**
	 * This method is used to assign the search variables to 
	 * GeneralSearchData after a user has pressed enter.
	 * 
	 * @return GeneralSearchData
	 */
	private GeneralSearchData buildGSD()
	{
		GeneralSearchData laGSD = new GeneralSearchData();

		if (gettxtApplicant().getText() != null
			&& !gettxtApplicant().getText().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			laGSD.setKey4(ivjtxtApplicant.getText());
		}
		laGSD.setKey1(CommonConstant.VIN);
		laGSD.setKey2(csVin);
		return laGSD;
	}

	/**
	 * Return the ivjJPanel1 property value.
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
				ivjJPanel1.setBounds(11, 9, 388, 105);
				ivjJPanel1.setLayout(new GridBagLayout());
				ivjJPanel1.setMaximumSize(new Dimension(392, 112));
				ivjJPanel1.setMinimumSize(new Dimension(392, 112));
				GridBagConstraints laConstraintstxtVin =
					new GridBagConstraints();
				laConstraintstxtVin.gridx = 2;
				laConstraintstxtVin.gridy = 1;
				laConstraintstxtVin.fill =
					GridBagConstraints.HORIZONTAL;
				laConstraintstxtVin.weightx = 1.0;
				laConstraintstxtVin.ipadx = 136;
				laConstraintstxtVin.insets = new Insets(7, 3, 7, 3);
				getJPanel1().add(gettxtVin(), laConstraintstxtVin);
				GridBagConstraints laConstraintsstcLblVin =
					new GridBagConstraints();
				laConstraintsstcLblVin.gridx = 1;
				laConstraintsstcLblVin.gridy = 1;
				laConstraintsstcLblVin.ipadx = 23;
				laConstraintsstcLblVin.insets =
					new Insets(10, 132, 10, 3);
				getJPanel1().add(
					getstcLblVin(),
					laConstraintsstcLblVin);
				GridBagConstraints laConstraintsstcLblApplicant =
					new GridBagConstraints();
				laConstraintsstcLblApplicant.gridx = 1;
				laConstraintsstcLblApplicant.gridy = 2;
				laConstraintsstcLblApplicant.ipadx = 51;
				laConstraintsstcLblApplicant.insets =
					new Insets(11, 56, 12, 3);
				getJPanel1().add(
					getstcLblApplicant(),
					laConstraintsstcLblApplicant);
				GridBagConstraints laConstraintstxtApplicant =
					new GridBagConstraints();
				laConstraintstxtApplicant.gridx = 2;
				laConstraintstxtApplicant.gridy = 2;
				laConstraintstxtApplicant.fill =
					GridBagConstraints.HORIZONTAL;
				laConstraintstxtApplicant.weightx = 1.0;
				laConstraintstxtApplicant.ipadx = 72;
				laConstraintstxtApplicant.insets =
					new Insets(8, 3, 9, 87);
				getJPanel1().add(
					gettxtApplicant(),
					laConstraintstxtApplicant);
				GridBagConstraints laConstraintstxtLast4 =
					new GridBagConstraints();
				laConstraintstxtLast4.gridx = 2;
				laConstraintstxtLast4.gridy = 3;
				laConstraintstxtLast4.fill =
					GridBagConstraints.HORIZONTAL;
				laConstraintstxtLast4.weightx = 1.0;
				laConstraintstxtLast4.ipadx = 32;
				laConstraintstxtLast4.insets =
					new java.awt.Insets(10, 3, 17, 147);
				getJPanel1().add(gettxtLast4(), laConstraintstxtLast4);
				GridBagConstraints laConstraintsstcLblLast4 =
					new GridBagConstraints();
				laConstraintsstcLblLast4.gridx = 1;
				laConstraintsstcLblLast4.gridy = 3;
				laConstraintsstcLblLast4.ipadx = 8;
				laConstraintsstcLblLast4.insets =
					new Insets(13, 4, 20, 3);
				getJPanel1().add(
					getstcLblLast4(),
					laConstraintsstcLblLast4);
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
		return ivjJPanel1;
	}

	/**
	 * Return the ivjSelectionPanel property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getSelectionPanel()
	{
		if (ivjSelectionPanel == null)
		{
			try
			{
				ivjSelectionPanel = new JPanel();
				ivjSelectionPanel.setName("ivjSelectionPanel");
				ivjSelectionPanel.setBounds(401, 4, 132, 111);
				ivjSelectionPanel.setLayout(null);
				ivjSelectionPanel.add(getchkSame(), null);
				ivjSelectionPanel.add(getchkNoVin(), null);
				// defect 9635
				ivjSelectionPanel.add(getchkSaveId(), null);
				// end defect 9635
				ivjSelectionPanel.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjSelectionPanel.setMinimumSize(new Dimension(0, 0));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjSelectionPanel;
	}

	/**
	 * Return the ivjstcLblApplicant property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblApplicant()
	{
		if (ivjstcLblApplicant == null)
		{
			try
			{
				ivjstcLblApplicant = new JLabel();
				ivjstcLblApplicant.setName("ivjstcLblApplicant");
				ivjstcLblApplicant.setText(TXT_APPL_OWNR_ID);
				ivjstcLblApplicant.setEnabled(false);
				ivjstcLblApplicant.setMinimumSize(
					new Dimension(70, 14));
				ivjstcLblApplicant.setMaximumSize(
					new Dimension(70, 14));
				ivjstcLblApplicant.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblApplicant;
	}

	/**
	 * Return the ivjstcLblLast4 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLast4()
	{
		if (ivjstcLblLast4 == null)
		{
			try
			{
				ivjstcLblLast4 = new JLabel();
				ivjstcLblLast4.setName("ivjstcLblLast4");
				ivjstcLblLast4.setText(TXT_LAST_4_DIGIT_DOC_NO);
				ivjstcLblLast4.setMaximumSize(new Dimension(165, 14));
				ivjstcLblLast4.setMinimumSize(new Dimension(165, 14));
				ivjstcLblLast4.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblLast4;
	}

	/**
	 * Return the ivjstcLblVin property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVin()
	{
		if (ivjstcLblVin == null)
		{
			try
			{
				ivjstcLblVin = new JLabel();
				ivjstcLblVin.setName("ivjstcLblVin");
				ivjstcLblVin.setText(TXT_VIN);
				ivjstcLblVin.setMaximumSize(new Dimension(22, 14));
				ivjstcLblVin.setMinimumSize(new Dimension(22, 14));
				// user code begin {1}
				ivjstcLblVin.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVin;
	}

	/**
	 * Return the ivjtxtApplicant property value.
	 * 
	 * @return RTSInputField 
	 */
	private RTSInputField gettxtApplicant()
	{
		if (ivjtxtApplicant == null)
		{
			try
			{
				ivjtxtApplicant = new RTSInputField();
				ivjtxtApplicant.setName("ivjtxtApplicant");
				ivjtxtApplicant.setManagingFocus(false);
				ivjtxtApplicant.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtApplicant.setMaximumSize(new Dimension(50, 20));
				ivjtxtApplicant.setEnabled(false);
				ivjtxtApplicant.setMinimumSize(new Dimension(50, 20));
				// user code begin {1}
				ivjtxtApplicant.setMaxLength(
					CommonConstant.LENGTH_OWNERID);
				ivjtxtApplicant.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtApplicant;
	}

	/**
	 * Return the ivjtxtLast4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLast4()
	{
		if (ivjtxtLast4 == null)
		{
			try
			{
				ivjtxtLast4 = new RTSInputField();
				ivjtxtLast4.setName("ivjtxtLast4");
				ivjtxtLast4.setManagingFocus(false);
				ivjtxtLast4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLast4.setMaximumSize(new Dimension(30, 20));
				ivjtxtLast4.setMinimumSize(new Dimension(30, 20));
				// user code begin {1}
				ivjtxtLast4.setMaxLength(4);
				ivjtxtLast4.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLast4;
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
				ivjtxtVin.setName("ivjtxtVin");
				ivjtxtVin.setManagingFocus(false);
				ivjtxtVin.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVin.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjtxtVin.setMaximumSize(new Dimension(70, 20));
				ivjtxtVin.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVin.setMinimumSize(new Dimension(70, 20));
				ivjtxtVin.setMaxLength(22);
				// user code begin {1}
				// defect 8671
				// Added to handle the problem when enter button is
				// stealing focus away.
				ivjtxtVin.addFocusListener(this);
				// Added code so that the backward and forward
				// FocusTraversalKeys would not pass up KeyPressed.  We
				// will have to handle where the focus should go there.
				ivjtxtVin.setFocusTraversalKeys(
					KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtVin.setFocusTraversalKeys(
					KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
					new HashSet());
				ivjtxtVin.addKeyListener(this);
				ivjtxtVin.addFocusListener(this);
				// end defect 8671
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVin;
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
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(FRM_NAME_KEY006);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setRequestFocus(false);
			setSize(550, 221);
			setTitle(FRM_TITLE_KEY006);
			setContentPane(getFrmVinKeySelectionKEY006ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
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
	public void itemStateChanged(ItemEvent aaIE)
	{
		// defect 8485
		// Disable/Enable Inquiry Field/Radio Buttons/Label 
		if (aaIE.getSource() == getchkSame()
			|| aaIE.getSource() == getchkNoVin())
		{
			if (getchkNoVin().isSelected()
				|| getchkSame().isSelected())
			{
				clearAllColor(this);
				getstcLblLast4().setEnabled(false);
				gettxtLast4().setText("");
				gettxtLast4().setEnabled(false);

				getstcLblVin().setEnabled(false);
				gettxtVin().setText("");
				gettxtVin().setEnabled(false);

				getstcLblApplicant().setEnabled(false);
				gettxtApplicant().setText("");
				gettxtApplicant().setEnabled(false);

				if (aaIE.getSource() == getchkNoVin()
					&& aaIE.getStateChange() == 1)
				{
					getchkSame().setSelected(false);
					getchkNoVin().requestFocus();
				}
				else if (
					aaIE.getSource() == getchkSame()
						&& aaIE.getStateChange() == 1)
				{
					getchkNoVin().setSelected(false);
					getchkSame().requestFocus();
				}
			}
			else
			{
				clearAllColor(this);
				getstcLblLast4().setEnabled(true);
				gettxtLast4().setEnabled(true);

				getstcLblVin().setEnabled(true);
				gettxtVin().setEnabled(true);

				getchkSame().setSelected(false);
				getchkNoVin().setSelected(false);

				if (SystemProperty.isHQ())
				{
					getstcLblApplicant().setEnabled(true);
					gettxtApplicant().setEnabled(true);
				}
				gettxtVin().requestFocus();
			}
		}
		else if (aaIE.getSource() == getchkSaveId())
		{
			clearAllColor(this);
			gettxtApplicant().requestFocus();
		}
	}

	/**
	 * Handle Cursor Movement Keys for Check Boxes
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		// defect 8671
		// Added to handle tabbing off of the VIN field after
		// finishing a salvage transaction.  Before this change
		// a user would have to tab two times to leave the VIN 
		// field.
		if (aaKE.getSource().equals(gettxtVin())
			&& (aaKE.getKeyCode() == KeyEvent.VK_TAB))
		{
			cbRequestedDefaultFocus = false;
			if (aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
			{
				getButtonPanel1().getBtnHelp().requestFocus();
			}
			else
			{
				// Added check for isEnabled b/c in regular title
				// we leave the field visible but disable it.
				if (gettxtApplicant().isVisible()
					&& gettxtApplicant().isEnabled())
				{
					gettxtApplicant().requestFocus();
				}
				else if (
					gettxtLast4().isVisible()
						&& gettxtLast4().isEnabled())
				{
					gettxtLast4().requestFocus();
				}
				// defect 9065 
				else if (
					getchkNoVin().isVisible()
						&& getchkNoVin().isEnabled())
				{
					getchkNoVin().requestFocus();
				}
				else if (
					getchkSame().isVisible()
						&& getchkSame().isEnabled())
				{
					getchkSame().requestFocus();
				}
				else if (
					getchkSaveId().isVisible()
						&& getchkSaveId().isEnabled())
				{
					getchkSaveId().requestFocus();
				}
				// end defect 9065 
			}
		}
		else
			// end defect 8671
			if (aaKE.getKeyCode() == KeyEvent.VK_UP
				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
			{
				if (getchkNoVin().hasFocus()
					&& getchkSame().isEnabled())
				{
					getchkSame().requestFocus();
				}
				else if (
					getchkSame().hasFocus()
						&& getchkNoVin().isEnabled())
				{
					getchkNoVin().requestFocus();
				}
			}
		super.keyPressed(aaKE);
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		csLast4Digits = null; 
		csTransCd = getController().getTransCode();

		// Add chkSaveId Processing for Salvage  
		getchkSaveId().setVisible(false);
		getchkSaveId().setEnabled(false);

		if (csTransCd.equals(TransCdConstant.SCOT)
			|| csTransCd.equals(TransCdConstant.REJCOR))
		{
			gettxtLast4().setVisible(false);
			getstcLblLast4().setVisible(false);

			if (csTransCd.equals(TransCdConstant.SCOT))
			{
				getchkNoVin().setEnabled(false);
				getchkNoVin().setVisible(false);

				getchkSaveId().setVisible(SystemProperty.isHQ());
				getchkSaveId().setEnabled(SystemProperty.isHQ());
			}
		}
		// defect 10290 
		// Reorganize for clarity
		if (SystemProperty.isHQ())
		{
			Transaction.setCumulativeTransIndi(0);
			ivjchkSame.setEnabled(false);
			getstcLblApplicant().setEnabled(true);
			gettxtApplicant().setEnabled(true);
		}
		else if (
			UtilityMethods.isDTA(csTransCd)
				&& aaDataObject instanceof DealerTitleData)
		{
			getstcLblApplicant().setVisible(false);
			gettxtApplicant().setVisible(false);

			ivjchkSame.setSelected(false);
			ivjchkSame.setVisible(false);

			caDlrTtlData =
				(DealerTitleData) UtilityMethods.copy(aaDataObject);

			setDlrDocNo4(caDlrTtlData);

			MFVehicleData laMfVehData = caDlrTtlData.getMFVehicleData();

			VehicleData laVehData = laMfVehData.getVehicleData();

			if (!UtilityMethods.isEmpty(laVehData.getVin()))
			{
				gettxtVin().setText(laVehData.getVin().trim());
			}
			else if (!caDlrTtlData.isKeyBoardEntry())
			{
				getchkNoVin().setSelected(true);
			}
		}
		else if (
			Transaction.getCumulativeTransIndi()
				!= CommonConstant.BOTH_ADDL_AND_CUMUL)
		{
			ivjchkSame.setEnabled(false);
		}
		else
		{
			setupSavedVehicle();
		}
		// end defect 10290 

		// defect 7885
		// Used to set the default focus field that focus is sent
		// to on windowOpened.  This is also used in action performed
		// to move focus back to the default field after we go through
		// actionPerformed()
		//gettxtVin().requestFocus();
		setDefaultFocusField(gettxtVin());
		// end defect 7885
		try
		{
			BarCodeScanner laBarCodeScanner =
				getController()
					.getMediator()
					.getAppController()
					.getBarCodeScanner();
			laBarCodeScanner.addBarCodeListener(this);
		}
		catch (RTSException aeRTSEx)
		{
			// defect 11071
			Log.write(Log.DEBUG, this, aeRTSEx.getDetailMsg());
			// end defect 11071
		}
	}

	/**
	 * Set last 4 digits for the VIN key selection screen.
	 * 
	 * @param aaDlrTtlData DealerTitleData
	 */
	private void setDlrDocNo4(DealerTitleData aaDlrTtlData)
	{
		MFVehicleData laMfVehData = aaDlrTtlData.getMFVehicleData();
		gettxtLast4().setText(CommonConstant.STR_SPACE_EMPTY);
		if (laMfVehData != null)
		{
			TitleData laDlrTtlData = laMfVehData.getTitleData();

			if (laDlrTtlData != null)
			{
				String lsDocNo = laDlrTtlData.getDocNo();
				if (lsDocNo != null && lsDocNo.length() >= 4)
				{
					String lsDocNo4 =
						lsDocNo.substring(lsDocNo.length() - 4);
					gettxtLast4().setText(lsDocNo4);
				}
			}
		}
	}

	/**
	 * Setup Saved Vehicle
	 */
	private void setupSavedVehicle()
	{
		try
		{
			CommonClientBusiness laCommonClientBusiness =
				new CommonClientBusiness();

			Object laTmpVeh =
				laCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.GET_SAVED_VEH,
					null);

			if (laTmpVeh != null && laTmpVeh instanceof MFVehicleData)
			{
				caMFVehData = (MFVehicleData) laTmpVeh;
			}

			Object laTmpMiscData =
				laCommonClientBusiness.processData(
					GeneralConstant.COMMON,
					CommonConstant.GET_VEH_MISC,
					null);

			if (laTmpMiscData != null
				&& laTmpMiscData instanceof VehMiscData)
			{
				caSavedVehInqData.setVehMiscData(
					(VehMiscData) laTmpMiscData);
			}
			caSavedVehInqData.setMfVehicleData(caMFVehData);
			caSavedVehInqData.setNoMFRecs(1);
			ivjchkSame.setEnabled(true);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
	}

	/**
	 * Set Vin Check Box
	 * 
	 * @param abChecked boolean
	 */
	public void setVinCheckBox(boolean abChecked)
	{
		getchkNoVin().setSelected(abChecked);
	}

	/**
	 * Set Vin Text
	 * 
	 * @param asVin String
	 */
	public void setVinText(String asVin)
	{
		gettxtVin().setText(asVin);
	}

	/**
	 * Validate Data
	 * 
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;
		RTSException leRTSEx = new RTSException();

		// defect 10003
		// Throw Error on ApplicantId = 0, Implement ErrorConstant

		// defect 9635, 9654 
		int liApplicantId = 0;

		if (!gettxtApplicant().isEmpty())
		{
			liApplicantId =
				Integer.parseInt(gettxtApplicant().getText());

			if (liApplicantId == 0)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtApplicant());
			}
		}

		boolean lbSCOT = csTransCd.equals(TransCdConstant.SCOT);

		if (!lbSCOT
			|| (lbSCOT && liApplicantId > TitleConstant.MAX_POS_DEALERID))
		{
			if (!gettxtApplicant().isEmpty()
				&& gettxtApplicant().getText().length()
					!= TitleConstant.REQD_MF_OWNERID_LENGTH)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtApplicant());
			}
		}
		if (getchkSaveId().isSelected() && gettxtApplicant().isEmpty())
		{
			// 150 
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtApplicant());
		}
		// end defect 9635, 9654
		if (gettxtVin().isEmpty()
			&& !(getchkNoVin().isSelected() || getchkSame().isSelected()))
		{
			int liErrNo =
				lbSCOT
					? ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID
					: ErrorsConstant.ERR_NUM_ENTER_VIN_OR_CHECK_NO_VIN;

			leRTSEx.addException(
				new RTSException(liErrNo),
				gettxtVin());
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
	}
}
