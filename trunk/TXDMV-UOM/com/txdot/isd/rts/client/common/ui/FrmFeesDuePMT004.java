package com.txdot.isd.rts.client.common.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/*
 * FrmFeesDuePMT004.java 
 *
 * (c) Texas Department of Transporation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking()
 * B Brown &	04/26/2002	CQU100003680 - Fix, in visual composition 
 *							editor, item descriptions
 * Robin Taylor             and prices appearing too close to edge of UI
 * ToddPederson	05/14/2002	Changing setData to check for null value 
 *							before displaying reg penalty.
 * MAbs			05/16/2002	Taking out tooltip 
 * Min Wang		09/16/2002  Fixed defect CQU100004717 Tab and Shift Tab 
 *							order
 *							When use the mail in fee.
 * J Rue		11/14/2002	Defect 4866, Comment actionListener for 
 *							getScrollPaneTable.addActionListener(this);.
 *							method ivjScrollPaneTable()
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 *							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 *							user help guide so had to make changes
 *							in actionPerformed().
 * S Govindappa	03/03/2003  Fixing defect# 5632. Made changes to setData 
 *							to disable the "Mail Fee" checkbox 
 *							for all title and
 *              03/06/2003  DTA events.
 * Min Wang 	11/04/2003	Made Shift Tab work. 
 *                          modify keyPressed(), getbtnCredit(), 
 *							getchkMailFee(), getScrollPaneTable(),
 *							vce change
 *                          defect 6647 Ver 5.1.6
 * Min Wang		03/23/2004	Fixed the shift tab was not working for 
 *							second transaction.
 *							add windowOpened()
 *							modify keyPressed()
 *							defect 6647 Ver 5.1.6
 * Jim Zwiener	05/12/2004	Always enable the mail check box on PMT004
 *							for RENEWs
 *							modify setData(), itemStateChanged()
 *							defect 7014 Ver 5.2.0
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * T Pederson	03/14/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	03/31/2005	Removed setNextFocusableComponent, added 
 * 							panel to frame to manage correct tabbing.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify getButtonPanel1, keyPressed
 * 							defect 8240 Ver 5.2.3  
 * T Pederson	07/18/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * J Zwiener	07/18/2005  Enhancement for Disable Placard event
 * 							modify actionPerformed()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	08/02/2005	Add Organ Donor Fee checkbox and processing
 *							modify screen via Visual Editor
 *							add caFeeOrganDonor, ORG_DN_CD, 
 *							TXT_ORGAN_DONOR_FEE
 *							add getchkOrganDonorFee(),handleChkBox()
 *							modify itemStateChanged(),setData(),
 *							getJPanel(), initialize(), getchkMailFee(),
 *							actionPerformed().
 *							defect 8276 Ver 5.2.2 Fix 6	
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/23/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * T Pederson	10/31/2005	Comment out keyPressed() method. 
 * 							defect 7885 Ver 5.2.3
 * T. Pederson	12/21/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * K Harrell	10/07/2006	Disable Mail Fee if Exempt
 * 							delete builderData(),keyPressed()
 * 							modify setData()
 * 							Also removed unused parameters
 * 							modify calculateDisplayFees(),getTotalDue() 			
 * 							defect 8900 Ver Exempts
 * K Harrell	10/18/2006	Removed 8900 changes for mail-in
 * 							modify setData() (restored)
 * 							defect !8900 Ver Exempts
 * K Harrell	04/22/2007	use SystemProperty.isRegion()
 * 							modify setData(), itemStateChanged()
 * 							defect 9085 Ver Special Plates  
 * B Hargrove 	12/18/2007 	Prior to this defect, 'credit allowed indi' 
 * 							for	windshied stickers was turned off in 
 * 							ACC004. This was done so that credit 
 * 							button would be disabled on PMT004. This 
 * 							causes no credit to be given for windshield 
 * 							stickers on receipts for this workstation
 * 							until RTS is re-started. Instead, just check
 * 							for hot check TransCd 'CKREDM' here and
 * 							disable the credit button. (see also:
 * 							FrmHotCheckACC004.convertAccountToTable()).
 * 							modify setData()
 * 							defect 6759 Ver Defect POS A
 * K Harrell	10/21/2008	Modify mechanism for determining if 
 * 							Disabled Placard Transaction. 
 * 							modify actionPerformed() 
 * 							defect 9831 Ver Defect_POS_B 
 * B Hargrove	07/20/2009	Add Voluntary Veterans Fund checkbox and 
 * 							processing (see also payment entry PMT002).
 *							modify screen via Visual Editor
 *							add TXT_VET_FUND, ivjbtnVetFund()
 *							modify actionPerformed(), getJPanel(), 
 *							setData()  
 * 							defect 10122 Ver Defect_POS_F
 * K Harrell	12/16/2009	Class Cleanup. Implement UtilityMethods.isDTA() 
 * 							add csTransCd
 * 							modify setData(), actionPerformed()
 * 							defect 10290 Ver Defect_POS_H 
 * K Harrell	09/14/2010	Tweaking via Visual Composition 
 * 							add getbtnPreviewReceipt()
 * 							add getDataForPreviewReceipt() 
 * 							modify actionPerformed(), getJPanel() 
 * 							defect 10590 Ver 6.6.0 
 * B Hargrove	08/04/2011	Add Parks and Wildlife Fund button and 
 * 							processing (see also payment entry PMT002).
 *							modify screen via Visual Editor
 *							add TXT_PARKS_FUND, ivjbtnParksFund()
 *							modify actionPerformed(), getJPanel(), 
 *							setData()  
 * 							defect 10965 Ver 6.8.1
 * K Harrell	11/14/2011	Disabled PreviewReceipt for VTR 275 Transcd
 * 							modify setData()
 * 							defect 11052 Ver 6.9.0						
 * ---------------------------------------------------------------------
 */
/**
 * Frame for Screen Fees Due PMT004
 *
 * @version 6.9.0		 	11/14/2011
 * @author	Nancy Ting
 * <br> Creation Date: 		06/26/2001 15:14:47 
 */

public class FrmFeesDuePMT004
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private RTSButton ivjbtnCredit = null;
	private RTSButton ivjbtnMiscFees = null;
	private RTSButton ivjbtnPWFund = null;
	private RTSButton ivjbtnVetFund = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkMailFee = null;
	private JCheckBox ivjchkOrganDonorFee = null;
	private JPanel ivjFrmFeesDuePMT004ContentPane1 = null;
	private JPanel ivjJPanel = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblCreditRemaining = null;
	private JLabel ivjlblPenaltyFee = null;
	private JLabel ivjlblPrevTransTotal = null;
	private JLabel ivjlblTotalDue = null;
	private JLabel ivjlblTransTotal = null;
	private RTSTable ivjScrollPaneTable = null;
	private JLabel ivjstcLblCreditRemaining = null;
	private JLabel ivjstcLblPrevTransTotal = null;
	private JLabel ivjstcLblRegPenaltyIncl = null;
	private JLabel ivjstcLblTotalDue = null;
	private JLabel ivjstcLblTransTotal = null;
	private TMPMT004 caTableModel = null;

	private Vector cvFeeData = null;

	// defect 10290 
	private String csTransCd = null;
	// end defect 10290 

	// Objects
	// defect 10290 
	private CompleteTransactionData caCTData = null;
	// end defect 10290 
	private FeesData caFeeDataMail = null;
	private FeesData caFeeOrganDonor = null;

	// Constants 
	private final static String FRM_NAME_PMT004 = "FrmFeesDuePMT004";
	private final static String FRM_TITLE_PMT004 =
		"Fees Due     PMT004";
	private final static int INDICATOR_ON = 1;
	private final static String MAIL_FEE_CD = "MAIL";
	private final static String MISC_ACCT_CD = "MISC";
	private final static String MSC_ACCT_CD = "MSC";
	private final static String ORG_DN_CD = "ORG-DN";
	private final static String REGION_ACCT_CD_SUFX = "-R";
	private final static String STR_DOLLAR_SIGN_ZERO_CENTS = "$.00";
	private final static String TXT_CRDT = "Credit";
	private final static String TXT_CRDT_REM = "Credit Remaining:";
	private final static String TXT_MAIL_FEE = "Mail Fee";
	private final static String TXT_MSC_FEES = "Misc Fees";
	private final static String TXT_ORGAN_DONOR_FEE = "Organ Donor Fee";
	private final static String TXT_PEN_FEE = "Penalty Fee";
	private final static String TXT_PREV_TOT = "Previous Trans. Total:";
	private final static String TXT_REG_PEN_INCL =
		"REGISTRATION PENALTY INCLUDED:";
	private final static String TXT_TOT_DUE = "Total Due:";
	private final static String TXT_TRANS_TOT = "Transaction Total:";
	// defect 10965
	private final static String TXT_PARKS_FUND = "State Parks";
	// end defect 10965
	private final static String TXT_VET_FUND = "Veterans' Fund";

	private javax.swing.JButton ivjbtnPreviewReceipt = null;

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
			FrmFeesDuePMT004 laFrmFeesDuePMT004;
			laFrmFeesDuePMT004 = new FrmFeesDuePMT004();
			laFrmFeesDuePMT004.setModal(true);
			laFrmFeesDuePMT004.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmFeesDuePMT004.show();
			Insets laInsets = laFrmFeesDuePMT004.getInsets();
			laFrmFeesDuePMT004.setSize(
				laFrmFeesDuePMT004.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmFeesDuePMT004.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmFeesDuePMT004.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmFeesDuePMT004 constructor
	 */
	public FrmFeesDuePMT004()
	{
		super();
		initialize();
	}

	/**
	 * FrmFeesDuePMT004 constructor
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmFeesDuePMT004(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmFeesDuePMT004 constructor
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmFeesDuePMT004(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when the button Enter/Cancel/Help/Credit/Misc Fees/Vet/Parks 
	 * Fund is clicked 
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
			Object laObjectSource = aaAE.getSource();
			ButtonPanel laButtonPanel = getButtonPanel1();

			// defect 10590  
			if (laObjectSource == getbtnPreviewReceipt())
			{
				Vector lvData = getDataForPreviewReceipt();

				getController().processData(
					VCFeesDuePMT004.PREVIEWRECEIPT,
					lvData);
			}
			else
			{
				// end defect 10590 
				if (laObjectSource == laButtonPanel.getBtnEnter())
				{
					// defect 10290 
					// Implement isDTA, csTransCd
					if (UtilityMethods.isDTA(csTransCd))
					{
						// end defect 10290 
						getController().processData(
							VCFeesDuePMT004.CONT_DTA,
							caCTData);
					}
					else
					{
						getController().processData(
							AbstractViewController.ENTER,
							caCTData);
					}
				}
				else if (
					laObjectSource == laButtonPanel.getBtnCancel())
				{
					// defect 10290 
					// null vs. CompleteTransactionData for Cancel 
					getController().processData(
						AbstractViewController.CANCEL,
						null);
					// end defect 10290 
				}
				else if (laObjectSource == laButtonPanel.getBtnHelp())
				{
					// defect 10290
					// Implement csTransCd 
					if (UtilityMethods.isMFUP())
					{
						if (csTransCd.equals(TransCdConstant.VEHINQ))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004A);
						}
						else if (
							csTransCd.equals(TransCdConstant.RENEW))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004B);
						}
						else if (
							csTransCd.equals(TransCdConstant.DUPL))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004C);
						}
						else if (
							csTransCd.equals(TransCdConstant.EXCH))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004D);
						}
						else if (
							csTransCd.equals(TransCdConstant.REPL))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004E);
						}
						else if (
							csTransCd.equals(TransCdConstant.PAWT))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004F);
						}
						else if (
							csTransCd.equals(TransCdConstant.CORREG))
						{
							MFVehicleData laMFVehData =
								(
									(CompleteTransactionData) getController()
									.getData())
									.getVehicleInfo();
							RegistrationData laRegData = null;
							if (laMFVehData != null)
							{
								laRegData = laMFVehData.getRegData();
							}
							if (laRegData != null
								&& laRegData.getApprndCntyNo() != 0)
							{
								RTSHelp.displayHelp(RTSHelp.PMT004G);
							}
							else
							{
								RTSHelp.displayHelp(RTSHelp.PMT004);
							}
						}
						else if (
							csTransCd.equals(TransCdConstant.TITLE)
								|| csTransCd.equals(
									TransCdConstant.NONTTL)
								|| csTransCd.equals(
									TransCdConstant.REJCOR))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004I);
						}
						else if (
							csTransCd.equals(TransCdConstant.ADLSTX))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004J);
						}
						else if (csTransCd.equals(TransCdConstant.CCO))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004K);
						}
						else if (
							csTransCd.equals(TransCdConstant.PT72))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004L);
						}
						else if (
							csTransCd.equals(TransCdConstant.PT144))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004M);
						}
						else if (
							csTransCd.equals(TransCdConstant.OTPT))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004N);
						}
						else if (
							csTransCd.equals(TransCdConstant.FDPT))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004O);
						}
						else if (
							csTransCd.equals(TransCdConstant.PT30))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004P);
						}
						else if (
							UtilityMethods.isDsabldPlcrdEvent(
								csTransCd))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004Q);
						}
						else if (
							csTransCd.equals(TransCdConstant.NRIPT)
								|| csTransCd.equals(
									TransCdConstant.NROPT))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004R);
						}
						else if (
							csTransCd.equals(TransCdConstant.TAWPT))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004S);
						}
						else if (
							csTransCd.equals(TransCdConstant.TOWP))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004T);
						}
						else if (
							csTransCd.equals(TransCdConstant.ADLCOL))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004ff);
						}
						else if (
							csTransCd.equals(TransCdConstant.CKREDM))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004gg);
						}
						// if doing DTA
						else if (
							csTransCd.equals(TransCdConstant.DTAORK))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004cc);
						}
						else if (
							csTransCd.equals(TransCdConstant.DTAORD))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004dd);
						}
						// doing Regional Collection				
						else if (
							csTransCd.equals(TransCdConstant.RGNCOL))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004ee);
						}
						else
						{
							RTSHelp.displayHelp(RTSHelp.PMT004);
						}
					}
					else
						// Do not have to check again  
						// if (!UtilityMethods.isMFUP())
						{
						if (csTransCd.equals(TransCdConstant.RENEW))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004U);
						}
						else if (
							csTransCd.equals(TransCdConstant.TITLE)
								|| csTransCd.equals(
									TransCdConstant.NONTTL)
								|| csTransCd.equals(
									TransCdConstant.REJCOR))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004V);
						}
						else if (
							csTransCd.equals(TransCdConstant.REPL))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004W);
						}
						else if (
							csTransCd.equals(TransCdConstant.EXCH))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004X);
						}
						else if (
							csTransCd.equals(TransCdConstant.PAWT))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004Y);
						}
						else if (
							csTransCd.equals(TransCdConstant.CORREG))
						{
							MFVehicleData laMFVehData =
								(
									(CompleteTransactionData) getController()
									.getData())
									.getVehicleInfo();
							RegistrationData laRegData = null;
							if (laMFVehData != null)
							{
								laRegData = laMFVehData.getRegData();
							}
							if (laRegData != null
								&& laRegData.getApprndCntyNo() != 0)
							{
								RTSHelp.displayHelp(RTSHelp.PMT004Z);
							}
							else
							{
								RTSHelp.displayHelp(RTSHelp.PMT004aa);
							}
						}
						else if (
							csTransCd.equals(TransCdConstant.TAWPT))
						{
							RTSHelp.displayHelp(RTSHelp.PMT004bb);
						}
						else
						{
							RTSHelp.displayHelp(RTSHelp.PMT004);
						}
					}
					// end defect 10290 
				}
				else if (laObjectSource == getbtnCredit())
				{
					getController().processData(
						VCFeesDuePMT004.CREDIT,
						caCTData);
				}
				else if (laObjectSource == getbtnMiscFees())
				{
					getController().processData(
						VCFeesDuePMT004.MISC_FEES,
						caCTData);
				}
				// defect 10122
				else if (laObjectSource == getbtnVetFund())
				{
					getController().processData(
							VCFeesDuePMT004.VET_FUND,
							caCTData);
				}
				// end defect 10122
			
				// defect 10965
				else if (laObjectSource == getbtnParksFund())
				{
					getController().processData(
							VCFeesDuePMT004.PARKS_FUND,
							caCTData);
				}
				// end defect 10965
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Recalculate and display fees
	 */
	private void calculateDisplayFees()
	{
		// calculate totals
		Dollar laDollarTransTotal = getTransactionTotal();
		Dollar laDollarPrevTotal = getPreviousTransactionTotal();
		Dollar laDollarCredit = getCreditRemaining();
		Dollar laDollarTotalDue =
			getTotalDue(laDollarTransTotal, laDollarPrevTotal);

		// set the labels to the calculated totals  
		// Transaction Total 
		getlblTransTotal().setText(getAMDollar(laDollarTransTotal));

		// Previous Transaction Total
		getlblPrevTransTotal().setText(getAMDollar(laDollarPrevTotal));

		// Credit Remaining
		if (laDollarCredit
			.equals(new Dollar(CommonConstant.STR_ZERO_DOLLAR)))
		{
			getlblCreditRemaining().setVisible(false);
			getstcLblCreditRemaining().setVisible(false);
		}
		else
		{
			getlblCreditRemaining().setText(
				getAMDollar(laDollarCredit));
		}
		// Total Due
		getlblTotalDue().setText(laDollarTotalDue.printDollar());
	}

	/**
	 * return the AM format of Dollar display
	 * 
	 * @param aaDollar Dollar
	 * @return String
	 */
	private String getAMDollar(Dollar aaDollar)
	{
		if (aaDollar
			.compareTo(new Dollar(CommonConstant.STR_ZERO_DOLLAR))
			== 0)
		{
			return STR_DOLLAR_SIGN_ZERO_CENTS;
		}
		else
		{
			return aaDollar.printDollar();
		}
	}

	/**
	 * Return the ivjbtnCredit property value
	 * 
	 * @return ivjbtnCredit
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCredit()
	{
		if (ivjbtnCredit == null)
		{
			try
			{
				ivjbtnCredit = new RTSButton();
				ivjbtnCredit.setSize(126, 25);
				ivjbtnCredit.setName("btnCredit");
				ivjbtnCredit.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjbtnCredit.setText(TXT_CRDT);
				ivjbtnCredit.setActionCommand(TXT_CRDT);
				// user code begin {1}
				ivjbtnCredit.setLocation(7, 43);
				ivjbtnCredit.addActionListener(this);
				ivjbtnCredit.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnCredit;
	}

	/**
	 * Return the ivjbtnMiscFees property value
	 * 
	 * @return ivjbtnMiscFees
	 */
	private RTSButton getbtnMiscFees()
	{
		if (ivjbtnMiscFees == null)
		{
			try
			{
				ivjbtnMiscFees = new RTSButton();
				ivjbtnMiscFees.setBounds(7, 11, 126, 25);
				ivjbtnMiscFees.setName("btnMiscFees");
				ivjbtnMiscFees.setMnemonic(
					java.awt.event.KeyEvent.VK_F);
				ivjbtnMiscFees.setText(TXT_MSC_FEES);
				ivjbtnMiscFees.setActionCommand(TXT_MSC_FEES);
				// user code begin {1}
				ivjbtnMiscFees.addActionListener(this);
				ivjbtnMiscFees.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnMiscFees;
	}

	/**
	 * This method initializes ivjbtnPreviewReceipt
	 * 
	 * @return JButton
	 */
	private JButton getbtnPreviewReceipt()
	{
		if (ivjbtnPreviewReceipt == null)
		{
			ivjbtnPreviewReceipt = new JButton();
			ivjbtnPreviewReceipt.setSize(129, 26);
			ivjbtnPreviewReceipt.setText("Preview Receipt");
			ivjbtnPreviewReceipt.setLocation(500, 295);
			ivjbtnPreviewReceipt.setMnemonic(
				java.awt.event.KeyEvent.VK_P);
			ivjbtnPreviewReceipt.addActionListener(this);
		}
		return ivjbtnPreviewReceipt;
	}
	/**
	 * This method initializes ivjbtnPWFund	
	 * 	
	 * @return com.txdot.isd.rts.client.general.ui.ivjbtnPWFund	
	 */
	private RTSButton getbtnParksFund()
	{
		if (ivjbtnPWFund == null)
		{
			ivjbtnPWFund = new RTSButton();
			ivjbtnPWFund.setBounds(new Rectangle(7, 106, 126, 25));
			ivjbtnPWFund.setMnemonic(KeyEvent.VK_S);
			ivjbtnPWFund.setName("btnPWFund");
			ivjbtnPWFund.setText(TXT_PARKS_FUND);
			ivjbtnPWFund.setActionCommand(TXT_PARKS_FUND);
			ivjbtnPWFund.addActionListener(this);
			ivjbtnPWFund.addKeyListener(this);
		}
		return ivjbtnPWFund;
	}
	/**
	 * Return the ivjbtnVetFund property value
	 * 
	 * @return ivjbtnVetFund
	 */
	private RTSButton getbtnVetFund()
	{
		if (ivjbtnVetFund == null)
		{
			try
			{
				ivjbtnVetFund = new RTSButton();
				ivjbtnVetFund.setSize(126, 25);
				ivjbtnVetFund.setName("btnVetFund");
				ivjbtnVetFund.setMnemonic(java.awt.event.KeyEvent.VK_V);
				ivjbtnVetFund.setText(TXT_VET_FUND);
				ivjbtnVetFund.setActionCommand(TXT_VET_FUND);
				ivjbtnVetFund.setLocation(7, 75);
				ivjbtnVetFund.addActionListener(this);
				ivjbtnVetFund.addKeyListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjbtnVetFund;
	}

	/**
	 * Return the ButtonPanel1 property value
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
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(204, 290, 231, 45);
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
	 * Return the chkMailFee property value
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkMailFee()
	{
		if (ivjchkMailFee == null)
		{
			try
			{
				ivjchkMailFee = new JCheckBox();
				ivjchkMailFee.setSize(98, 24);
				ivjchkMailFee.setName("ivjchkMailFee");
				ivjchkMailFee.setMnemonic(KeyEvent.VK_M);
				ivjchkMailFee.setText(TXT_MAIL_FEE);
				ivjchkMailFee.setMaximumSize(new Dimension(71, 22));
				ivjchkMailFee.setActionCommand(TXT_MAIL_FEE);
				ivjchkMailFee.setMinimumSize(new Dimension(71, 22));
				ivjchkMailFee.setLocation(6, 140);
				// user code begin {1}
				ivjchkMailFee.addItemListener(this);
				ivjchkMailFee.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkMailFee;
	}

	/**
	 * Return the chkOrganDonorFee property value
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getchkOrganDonorFee()
	{
		if (ivjchkOrganDonorFee == null)
		{
			ivjchkOrganDonorFee = new javax.swing.JCheckBox();
			ivjchkOrganDonorFee.setSize(123, 24);
			ivjchkOrganDonorFee.setLocation(6, 164);
			ivjchkOrganDonorFee.setActionCommand(TXT_ORGAN_DONOR_FEE);
			ivjchkOrganDonorFee.setMnemonic(KeyEvent.VK_O);
			ivjchkOrganDonorFee.setName("ivjchkOrganDonorFee");
			ivjchkOrganDonorFee.setText(TXT_ORGAN_DONOR_FEE);
			// user code begin {1}
			ivjchkOrganDonorFee.addItemListener(this);
			ivjchkOrganDonorFee.addKeyListener(this);
			// user code end
		}
		return ivjchkOrganDonorFee;
	}

	/**
	 * Get the Credit Remaining total
	 * to be completed
	 * 
	 * @return Dollar
	 */
	protected Dollar getCreditRemaining()
	{
		if (caCTData.getCrdtRemaining() == null)
		{
			return new Dollar(CommonConstant.STR_ZERO_DOLLAR);
		}
		else
		{
			return caCTData.getCrdtRemaining();
		}
	}

	/**
	 * Get Data for Preview Receipt 
	 * 
	 * @return Vector
	 */
	private Vector getDataForPreviewReceipt() throws RTSException
	{
		String lsTransCdDesc =
			TransactionCodesCache.getTransCdDesc(
				caCTData.getTransCode());
		Transaction laTrans = new Transaction();
		String lsFileName = laTrans.genPreviewRcpt(caCTData);
		ReportSearchData laRptSrchData = new ReportSearchData();
		laRptSrchData.initForClient(false);
		laRptSrchData.setIntKey2(0);
		laRptSrchData.setKey1(lsFileName);
		laRptSrchData.setKey3(" " + lsTransCdDesc);
		laRptSrchData.setData(lsFileName);
		laRptSrchData.setNextScreen(
			ReportConstant.RPR000_NEXT_SCREEN_CANCEL);
		Vector lvVector = new Vector();
		lvVector.add(laRptSrchData);
		return lvVector;
	}

	/**
	 * Return the ivjFrmFeesDuePMT004ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmFeesDuePMT004ContentPane1()
	{
		if (ivjFrmFeesDuePMT004ContentPane1 == null)
		{
			try
			{
				ivjFrmFeesDuePMT004ContentPane1 = new JPanel();
				ivjFrmFeesDuePMT004ContentPane1.setName(
					"ivjFrmFeesDuePMT004ContentPane1");
				ivjFrmFeesDuePMT004ContentPane1.setLayout(null);
				ivjFrmFeesDuePMT004ContentPane1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmFeesDuePMT004ContentPane1.setMinimumSize(
					new Dimension(865, 325));
				getFrmFeesDuePMT004ContentPane1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getstcLblTransTotal(),
					getstcLblTransTotal().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getstcLblPrevTransTotal(),
					getstcLblPrevTransTotal().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getstcLblTotalDue(),
					getstcLblTotalDue().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getlblTransTotal(),
					getlblTransTotal().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getlblPrevTransTotal(),
					getlblPrevTransTotal().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getlblTotalDue(),
					getlblTotalDue().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getstcLblCreditRemaining(),
					getstcLblCreditRemaining().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getlblCreditRemaining(),
					getlblCreditRemaining().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getstcLblRegPenaltyIncl(),
					getstcLblRegPenaltyIncl().getName());
				getFrmFeesDuePMT004ContentPane1().add(
					getlblPenaltyFee(),
					getlblPenaltyFee().getName());
				// user code begin {1}
				ivjFrmFeesDuePMT004ContentPane1.add(getJPanel(), null);
				ivjFrmFeesDuePMT004ContentPane1.add(
					getbtnPreviewReceipt(),
					null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmFeesDuePMT004ContentPane1;
	}

	/**
	 * This method initializes ivjJPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (ivjJPanel == null)
		{
			ivjJPanel = new JPanel();
			ivjJPanel.setLayout(null);
			ivjJPanel.add(getbtnMiscFees(), null);
			ivjJPanel.add(getbtnCredit(), null);

			// defect 10122
			ivjJPanel.add(getbtnVetFund(), null);
			// end defect 10122
			// defect 10965
			ivjJPanel.add(getbtnParksFund(), null);
			// end defect 10965 
			ivjJPanel.add(getchkMailFee(), null);
			ivjJPanel.add(getchkOrganDonorFee(), null);
			
			// defect 10590 			
			RTSButtonGroup laButtonGrp = new RTSButtonGroup();
			laButtonGrp.add(getbtnMiscFees());
			laButtonGrp.add(getbtnCredit());
			laButtonGrp.add(getbtnVetFund());
			// defect 10965
			laButtonGrp.add(getbtnParksFund());
			// end defect 10965
			laButtonGrp.add(getchkMailFee());
			laButtonGrp.add(getchkOrganDonorFee());
			// end defect 10590 

			ivjJPanel.setSize(144, 197);
			ivjJPanel.setLocation(492, 44);
		}
		return ivjJPanel;
	}

	/**
	 * Return the ivjJScrollPane1 property value
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane1().setViewportView(getScrollPaneTable());
				// user code begin {1}
				ivjJScrollPane1.setSize(465, 163);
				ivjJScrollPane1.setLocation(23, 43);
				ivjJScrollPane1.getViewport().setBackground(
					Color.white);
				// user code end
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
	 * Return the lblCreditRemaining property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCreditRemaining()
	{
		if (ivjlblCreditRemaining == null)
		{
			try
			{
				ivjlblCreditRemaining = new JLabel();
				ivjlblCreditRemaining.setName("ivjlblCreditRemaining");
				ivjlblCreditRemaining.setText("$.00");
				ivjlblCreditRemaining.setMaximumSize(
					new Dimension(24, 14));
				ivjlblCreditRemaining.setHorizontalTextPosition(2);
				ivjlblCreditRemaining.setBounds(365, 249, 106, 14);
				ivjlblCreditRemaining.setMinimumSize(
					new Dimension(24, 14));
				ivjlblCreditRemaining.setHorizontalAlignment(4);
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
		return ivjlblCreditRemaining;
	}

	/**
	 * Return the ivjlblPenaltyFee property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPenaltyFee()
	{
		if (ivjlblPenaltyFee == null)
		{
			try
			{
				ivjlblPenaltyFee = new JLabel();
				ivjlblPenaltyFee.setName("ivjlblPenaltyFee");
				ivjlblPenaltyFee.setText(TXT_PEN_FEE);
				ivjlblPenaltyFee.setMaximumSize(new Dimension(38, 14));
				ivjlblPenaltyFee.setForeground(Color.red);
				ivjlblPenaltyFee.setHorizontalTextPosition(2);
				ivjlblPenaltyFee.setBounds(349, 18, 121, 14);
				ivjlblPenaltyFee.setMinimumSize(new Dimension(38, 14));
				ivjlblPenaltyFee.setHorizontalAlignment(
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
		return ivjlblPenaltyFee;
	}

	/**
	 * Return the ivjlblPrevTransTotal property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrevTransTotal()
	{
		if (ivjlblPrevTransTotal == null)
		{
			try
			{
				ivjlblPrevTransTotal = new JLabel();
				ivjlblPrevTransTotal.setName("ivjlblPrevTransTotal");
				ivjlblPrevTransTotal.setText("$.00");
				ivjlblPrevTransTotal.setMaximumSize(
					new Dimension(24, 14));
				ivjlblPrevTransTotal.setHorizontalTextPosition(2);
				ivjlblPrevTransTotal.setBounds(365, 232, 106, 14);
				ivjlblPrevTransTotal.setMinimumSize(
					new Dimension(24, 14));
				ivjlblPrevTransTotal.setHorizontalAlignment(4);
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
		return ivjlblPrevTransTotal;
	}

	/**
	 * Return the ivjlblTotalDue property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTotalDue()
	{
		if (ivjlblTotalDue == null)
		{
			try
			{
				ivjlblTotalDue = new JLabel();
				ivjlblTotalDue.setName("ivjlblTotalDue");
				ivjlblTotalDue.setText("$999999.99");
				ivjlblTotalDue.setMaximumSize(new Dimension(38, 14));
				ivjlblTotalDue.setHorizontalTextPosition(2);
				ivjlblTotalDue.setBounds(365, 267, 106, 14);
				ivjlblTotalDue.setMinimumSize(new Dimension(38, 14));
				ivjlblTotalDue.setHorizontalAlignment(4);
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
		return ivjlblTotalDue;
	}

	/**
	 * Return the ivjlblTransTotal property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTransTotal()
	{
		if (ivjlblTransTotal == null)
		{
			try
			{
				ivjlblTransTotal = new JLabel();
				ivjlblTransTotal.setName("ivjlblTransTotal");
				ivjlblTransTotal.setText("$51.80");
				ivjlblTransTotal.setMaximumSize(new Dimension(38, 14));
				ivjlblTransTotal.setHorizontalTextPosition(2);
				ivjlblTransTotal.setBounds(365, 212, 106, 14);
				ivjlblTransTotal.setMinimumSize(new Dimension(38, 14));
				ivjlblTransTotal.setHorizontalAlignment(4);
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
		return ivjlblTransTotal;
	}

	/**
	 * Get the Previous Transaction Total
	 * 
	 * @return String
	 */
	protected Dollar getPreviousTransactionTotal()
	{
		return Transaction.getRunningSubtotal();
	}

	/**
	 * Return the ScrollPaneTable property value
	 * 
	 * @return RTSTable
	 */
	private RTSTable getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable = new RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				getJScrollPane1().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjScrollPaneTable.setModel(new TMPMT004());
				ivjScrollPaneTable.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
				ivjScrollPaneTable.setRowMargin(0);
				ivjScrollPaneTable.setIntercellSpacing(
					new Dimension(0, 0));
				ivjScrollPaneTable.setBounds(0, 0, 462, 109);
				// user code begin {1}
				caTableModel = (TMPMT004) ivjScrollPaneTable.getModel();
				TableColumn laTableColumnA =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				laTableColumnA.setPreferredWidth(300);
				TableColumn laTableColumnB =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(1));
				DefaultTableCellRenderer laDefaultTableCellRenderer =
					new DefaultTableCellRenderer();
				laDefaultTableCellRenderer.setHorizontalAlignment(
					SwingConstants.RIGHT);
				laTableColumnB.setCellRenderer(
					laDefaultTableCellRenderer);
				ivjScrollPaneTable.setTraversable(true);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjScrollPaneTable.init();
				laTableColumnA.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.LEFT));
				laTableColumnB.setCellRenderer(
					ivjScrollPaneTable.setColumnAlignment(
						RTSTable.RIGHT));
				ivjScrollPaneTable.addKeyListener(this);
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
		return ivjScrollPaneTable;
	}

	/**
	 * Return the ivjstcLblCreditRemaining property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCreditRemaining()
	{
		if (ivjstcLblCreditRemaining == null)
		{
			try
			{
				ivjstcLblCreditRemaining = new JLabel();
				ivjstcLblCreditRemaining.setSize(143, 14);
				ivjstcLblCreditRemaining.setName(
					"ivjstcLblCreditRemaining");
				ivjstcLblCreditRemaining.setText(TXT_CRDT_REM);
				ivjstcLblCreditRemaining.setMaximumSize(
					new Dimension(123, 14));
				ivjstcLblCreditRemaining.setHorizontalTextPosition(4);
				ivjstcLblCreditRemaining.setMinimumSize(
					new Dimension(123, 14));
				ivjstcLblCreditRemaining.setHorizontalAlignment(4);
				// user code begin {1}
				ivjstcLblCreditRemaining.setLocation(196, 249);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblCreditRemaining;
	}

	/**
	 * Return the ivjstcLblPrevTransTotal property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPrevTransTotal()
	{
		if (ivjstcLblPrevTransTotal == null)
		{
			try
			{
				ivjstcLblPrevTransTotal = new JLabel();
				ivjstcLblPrevTransTotal.setSize(143, 14);
				ivjstcLblPrevTransTotal.setName(
					"ivjstcLblPrevTransTotal");
				ivjstcLblPrevTransTotal.setText(TXT_PREV_TOT);
				ivjstcLblPrevTransTotal.setMaximumSize(
					new Dimension(123, 14));
				ivjstcLblPrevTransTotal.setHorizontalTextPosition(4);
				ivjstcLblPrevTransTotal.setMinimumSize(
					new Dimension(123, 14));
				ivjstcLblPrevTransTotal.setHorizontalAlignment(4);
				// user code begin {1}
				ivjstcLblPrevTransTotal.setLocation(196, 232);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblPrevTransTotal;
	}

	/**
	 * Return the ivjstcLblRegPenaltyIncl property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegPenaltyIncl()
	{
		if (ivjstcLblRegPenaltyIncl == null)
		{
			try
			{
				ivjstcLblRegPenaltyIncl = new JLabel();
				ivjstcLblRegPenaltyIncl.setName(
					"ivjstcLblRegPenaltyIncl");
				ivjstcLblRegPenaltyIncl.setText(TXT_REG_PEN_INCL);
				ivjstcLblRegPenaltyIncl.setMaximumSize(
					new Dimension(102, 14));
				ivjstcLblRegPenaltyIncl.setForeground(Color.red);
				ivjstcLblRegPenaltyIncl.setHorizontalTextPosition(4);
				ivjstcLblRegPenaltyIncl.setBounds(23, 17, 243, 17);
				ivjstcLblRegPenaltyIncl.setMinimumSize(
					new Dimension(102, 14));
				ivjstcLblRegPenaltyIncl.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjstcLblRegPenaltyIncl;
	}

	/**
	 * Return the ivjstcLblTotalDue property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTotalDue()
	{
		if (ivjstcLblTotalDue == null)
		{
			try
			{
				ivjstcLblTotalDue = new JLabel();
				ivjstcLblTotalDue.setSize(130, 14);
				ivjstcLblTotalDue.setName("ivjstcLblTotalDue");
				ivjstcLblTotalDue.setText(TXT_TOT_DUE);
				ivjstcLblTotalDue.setMaximumSize(new Dimension(56, 14));
				ivjstcLblTotalDue.setHorizontalTextPosition(4);
				ivjstcLblTotalDue.setMinimumSize(new Dimension(56, 14));
				ivjstcLblTotalDue.setHorizontalAlignment(4);
				// user code begin {1}
				ivjstcLblTotalDue.setLocation(209, 267);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTotalDue;
	}

	/**
	 * Return the ivjstcLblTransTotal property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTransTotal()
	{
		if (ivjstcLblTransTotal == null)
		{
			try
			{
				ivjstcLblTransTotal = new JLabel();
				ivjstcLblTransTotal.setSize(130, 14);
				ivjstcLblTransTotal.setName("ivjstcLblTransTotal");
				ivjstcLblTransTotal.setText(TXT_TRANS_TOT);
				ivjstcLblTransTotal.setMaximumSize(
					new Dimension(102, 14));
				ivjstcLblTransTotal.setHorizontalTextPosition(4);
				ivjstcLblTransTotal.setMinimumSize(
					new Dimension(102, 14));
				ivjstcLblTransTotal.setHorizontalAlignment(4);
				// user code begin {1}
				ivjstcLblTransTotal.setLocation(209, 212);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTransTotal;
	}

	/**
	 * Get Total Due
	 * 
	 * @param adDollarTrans Dollar
	 * @param adDollarPrevTrans Dollar
	 * @return Dollar the transaction total
	 */
	protected Dollar getTotalDue(
		Dollar adDollarTrans,
		Dollar adDollarPrevTrans)
	{
		Dollar laDollar = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
		laDollar = laDollar.add(adDollarTrans);
		laDollar = laDollar.add(adDollarPrevTrans);
		return laDollar;
	}

	/**
	 * Get the transaction total
	 * 
	 * @return Dollar
	 */
	protected Dollar getTransactionTotal()
	{
		Dollar laDollar = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
		if (cvFeeData != null)
		{
			for (int i = 0; i < cvFeeData.size(); i++)
			{
				FeesData laFeesData = (FeesData) cvFeeData.get(i);
				Dollar laDollarFeesData = laFeesData.getItemPrice();
				if (laDollarFeesData != null)
				{
					laDollar = laDollar.add(laDollarFeesData);
				}
			}
		}
		return laDollar;
	}

	/**
	 * Adds or removes FeesData Objects from Fees Vector based upon
	 * Selection / Deselection of checkbox 
	 *
	 * @param aaFeesData FeesData
	 * @param asAcctItmCd String
	 * @return aaFeesData 
	 */
	private FeesData handleChkBox(
		FeesData aaFeesData,
		String asAcctItmCd,
		java.awt.event.ItemEvent aaIE)
	{
		// Deselected 
		if (aaIE.getStateChange() == ItemEvent.DESELECTED)
		{
			cvFeeData.remove(aaFeesData);
			aaFeesData = null;
		}
		// Selected
		else if (aaFeesData == null)
		{
			aaFeesData = new FeesData();

			int liEffDate = new RTSDate().getYYYYMMDDDate();

			AccountCodesData laAccountCodesData =
				AccountCodesCache.getAcctCd(asAcctItmCd, liEffDate);

			if (laAccountCodesData != null)
			{
				aaFeesData.setAcctItmCd(
					laAccountCodesData.getAcctItmCd());
				aaFeesData.setCrdtAllowedIndi(
					laAccountCodesData.getCrdtAllowdIndi());
				aaFeesData.setDesc(
					laAccountCodesData.getAcctItmCdDesc());
				aaFeesData.setItemPrice(
					laAccountCodesData.getMiscFee());
				aaFeesData.setItmQty(1);
				cvFeeData.add(aaFeesData);
			}
		}

		caTableModel.add(cvFeeData);
		calculateDisplayFees();
		return aaFeesData;
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
			setName(FRM_NAME_PMT004);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(FRM_TITLE_PMT004);
			setContentPane(getFrmFeesDuePMT004ContentPane1());
			this.setBounds(0, 0, 651, 364);
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	* Handles the Mail Fee  && Organ Donor Fee check box
	*
	* <P>Process the selection and deselection of Mail Fee and
	* Organ Donor Fee.  If @ Region, modify the AcctItmCd for Organ 
	* Donor Fee.
	*
	* @param aaIE ItemEvent
	*/
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		// defect 8276
		if (aaIE.getSource() == getchkMailFee())
		{
			caFeeDataMail =
				handleChkBox(caFeeDataMail, MAIL_FEE_CD, aaIE);
		}

		else if (aaIE.getSource() == getchkOrganDonorFee())
		{
			String lsOrgDnCd = ORG_DN_CD;

			if (SystemProperty.isRegion())
			{
				lsOrgDnCd = lsOrgDnCd + REGION_ACCT_CD_SUFX;
			}

			caFeeOrganDonor =
				handleChkBox(caFeeOrganDonor, lsOrgDnCd, aaIE);
		}
		//end defect 8276 
	}

	/**
	* Receives CompleteTransactionData to populate screen
	* 
	* @param aaDataObject Object 
	*/
	public void setData(Object aaDataObject)
	{
		caCTData =
			(CompleteTransactionData) UtilityMethods.copy(aaDataObject);

		// defect 10290 
		csTransCd = caCTData.getTransCode();
		// end defect 10290  
		
		// defect 11052 
		if (UtilityMethods.isVTR275TransCd(csTransCd))
		{
			getbtnPreviewReceipt().setEnabled(false); 
		}
		// end defect 11052 

		// Populate Table
		cvFeeData = caCTData.getRegFeesData().getVectFees();

		//Set credit remaining
		Transaction.setCreditRemaining(caCTData.getCrdtRemaining());

		// If Region, change code to -R
		if (SystemProperty.isRegion())
		{
			int liEffDate = new RTSDate().getYYYYMMDDDate();

			for (int i = 0; i < cvFeeData.size(); i++)
			{
				FeesData laFeesData = (FeesData) cvFeeData.get(i);
				if (laFeesData.getAcctItmCd().equals(MSC_ACCT_CD))
				{
					laFeesData.setAcctItmCd(MISC_ACCT_CD);
				}
				String lsTmpRegionCd =
					laFeesData.getAcctItmCd() + REGION_ACCT_CD_SUFX;

				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						lsTmpRegionCd,
						liEffDate);

				if (laAccountCodesData != null)
				{
					laFeesData.setAcctItmCd(
						laAccountCodesData.getAcctItmCd());
					laFeesData.setCrdtAllowedIndi(
						laAccountCodesData.getCrdtAllowdIndi());
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
			}
		}

		caTableModel.add(cvFeeData);

		// Penalty
		if (caCTData.getRegPnltyChrgIndi() == INDICATOR_ON
			&& caCTData.getRegisPenaltyFee() != null)
		{
			getlblPenaltyFee().setText(
				caCTData.getRegisPenaltyFee().printDollar());
		}
		else
		{
			getlblPenaltyFee().setVisible(false);
			getstcLblRegPenaltyIncl().setVisible(false);
		}
		// Mail in Fee Checkbox, Credit Remaining, credit allowed
		boolean lbCreditAllowed = false;

		RegTtlAddlInfoData laRegTtlAddlInfoData =
			caCTData.getRegTtlAddlInfoData();

		if (laRegTtlAddlInfoData.getProcsByMailIndi() != INDICATOR_ON)
		{
			getchkMailFee().setEnabled(false);
		}

		// defect 10290
		// Implement csTransCd, isDTA()
		if (csTransCd.equals(TransCdConstant.TITLE)
			|| csTransCd.equals(TransCdConstant.CORTTL)
			|| csTransCd.equals(TransCdConstant.NONTTL)
			|| csTransCd.equals(TransCdConstant.REJCOR)
			|| UtilityMethods.isDTA(csTransCd))
		{
			getchkMailFee().setEnabled(false);
		}

		// defect 7014
		if (csTransCd.equals(TransCdConstant.RENEW))
		{
			getchkMailFee().setEnabled(true);
		}
		// end defect 7014

		boolean lbMailFeeFound = false;
		Dollar ldCreditAmt = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
		for (int i = 0; i < cvFeeData.size(); i++)
		{
			FeesData laFeesData = (FeesData) cvFeeData.get(i);
			// if (!((lbMailFeeFound) && (lbCreditCodeFound) && (lbCreditAllowed))) {
			// defect 6759
			// Add check to not enable Credit button if hot check 'CKREDM' 
			if (laFeesData.getCrdtAllowedIndi() == INDICATOR_ON
				&& !csTransCd.equals(TransCdConstant.CKREDM))
			{
				// end defect 6759
				lbCreditAllowed = true;
				ldCreditAmt =
					ldCreditAmt.add(laFeesData.getItemPrice());
			}
			// Mail In Fee 
			if (laFeesData.getAcctItmCd().equals(MAIL_FEE_CD))
			{
				caFeeDataMail = laFeesData;
				lbMailFeeFound = true;
			}

			// defect 8276
			// Organ Donor Fee
			if (laFeesData.getAcctItmCd().startsWith(ORG_DN_CD))
			{
				caFeeOrganDonor = laFeesData;
			}
			// end defect 8276
		}
		if (lbMailFeeFound
			&& getchkMailFee().isEnabled()
			&& !getchkMailFee().isSelected())
		{
			getchkMailFee().setSelected(true);
		}
		calculateDisplayFees();

		// Misc Fees Button
		if (csTransCd.equals(TransCdConstant.ADLCOL))
		{
			getbtnMiscFees().setEnabled(false);
			// defect 10122
			getbtnVetFund().setEnabled(false);
			// end defect 10122
			// defect 10965
			getbtnParksFund().setEnabled(false);
			// end defect 10965
		}

		// Credit Button
		if (!lbCreditAllowed)
		{
			getbtnCredit().setEnabled(false);
		}
		else
		{
			if (ldCreditAmt
				.compareTo(new Dollar(CommonConstant.STR_ZERO_DOLLAR))
				<= 0)
			{
				getbtnCredit().setEnabled(false);
			}
		}
		// defect 8276
		// disable OrganDonorFee for ADLCOL RGNCOL
		// defect 10122
		// defect 10965
		// Regions can do Misc Fee,Vet Fund, or Parks Fund thru Reg Collections
		if (csTransCd.equals(TransCdConstant.ADLCOL)
			|| SystemProperty.isRegion())
			// end defect 10122
		{
			getbtnCredit().setEnabled(false);
			getbtnMiscFees().setEnabled(false);
			getchkOrganDonorFee().setEnabled(false);
			// defect 10122
			// disable for Veteran Fund
			getbtnVetFund().setEnabled(false);
			// end defect 10122
			getbtnParksFund().setEnabled(false);
			// end defect 10965
		}
		if (csTransCd.equals(TransCdConstant.RGNCOL))
		{
			getchkOrganDonorFee().setEnabled(false);
		}
		// end defect 8276
		// end defect 10290

		if (cvFeeData != null && cvFeeData.size() > 0)
		{
			//highlight first row
			getScrollPaneTable().setRowSelectionInterval(0, 0);
		}
		
		// defect 8494
		setDefaultFocusField(getButtonPanel1().getBtnEnter());
		// end defect 8494
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"