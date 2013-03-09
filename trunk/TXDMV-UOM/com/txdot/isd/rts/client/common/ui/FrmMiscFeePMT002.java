package com.txdot.isd.rts.client.common.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.data.AccountCodesData;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.FeesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.AcctCdConstant;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import java.awt.Rectangle;
import java.awt.Dimension;

/* 
 * FrmMiscFeePMT002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/17/2002	Misc fees not handling properly in regional
 * 							collection events
 * 							defect 3547  
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()   
 * T Pederson	03/14/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * T Pederson	07/28/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/26/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/27/2005	Add logic for "ESC" processing, i.e. return
 * 							to PMT004. 
 * 							add implements KeyListener
 * 							add keyReleased() 
 * 							defect 8486 Ver 5.2.3  
 * B Hargrove	07/16/2009  Add Voluntary Veteran Fund process.
 * 							add cdFeeEntered, ciPaymentType, ciMaxAmount,
 * 							MAX_VET_FUND, TXT_ENTER_MISC_FEE, 
 * 							FRM_TITLE_PMT002_MISC_FEE,
 *  						FRM_TITLE_PMT002_VET_FUND, 
 * 							TXT_ENTER_VET_FUND, VET_FUND_CODE_REGION,
 * 							setDataToDataObject()
 * 							modify gettxtMiscFees(), handleOK(), setData()
 * 							delete displayError(), FRM_TITLE_PMT002, 
 * 							MISC_FEE_DESC, MISC_FEE_CODE,
 * 							MISC_FEE_CODE_REGION
 * 							rename handleOK() to validateFields() 
 * 							defect 10122 Ver Defect_POS_F
 * K Harrell	08/17/2009	Use Current Date for Acct Codes lookup 
 * 							modify setDataToDataObject()
 * 							defect 10175 Ver Defect_POS_E'/F    
 * B Hargrove	08/22/2011  Add Parks and Wildlife Fund process.
 * 							add MAX_PARKS_FUND, FRM_TITLE_PMT002_PARKS_FUND, 
 * 							TXT_ENTER_PARKS_FUND, PARKS_FUND_CODE_REGION
 * 							modify setData(), setDataToDataObject()
 * 							defect 10965 Ver 6.8.1
 * ---------------------------------------------------------------------
 */
/**
 * Screen for Misc Fee PMT002
 *
 * @version	6.8.1	08/22/2011
 * @author	Nancy Ting
 * <br>Creation Date:		06/26/2001 14:51:07
 */

public class FrmMiscFeePMT002
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private JLabel ivjstcLblMiscFee = null;
	private JPanel ivjFrmMiscFeePMT002ContentPane1 = null;
	private RTSButton ivjbtnOK = null;
	private RTSInputField ivjtxtMiscFees = null;

	// boolean
	protected boolean cbContainsMiscFee = false;

	// Objects
	protected CompleteTransactionData caCompleteTransactionData = null;
	protected FeesData caFeesDataMiscFee = null;

	// Vector
	protected Vector cvFeesData = null;

	// Constants 
	protected static final String csTitle = "ERROR!";
	protected static final String csErrorMsg =
		"Incorrect field entry.  Please re-enter";
	protected static final int MAX_MISC_FEE = 100;
	// defect 10122
	//protected static final String MISC_FEE_CODE = "MSC";
	//protected static final String MISC_FEE_CODE_REGION = "MISC-R";
	//protected static final String MISC_FEE_DESC = "MISCELLANEOUS FEES";
	// end defect 10122
	public static final String DOT = ".";
	public static final int DECIMAL_NUMBER = 2;

	// Text Constants 
	private final static String FRM_NAME_PMT002 = "FrmMiscFeePMT002";
	// defect 10122
	//private final static String FRM_TITLE_PMT002 =
	//	"Misc Fee      PMT002";
	protected static final int MAX_VET_FUND = 100000;
	private static double cdFeeEntered = 0.0;
	private static int ciPaymentType;
	private static int ciMaxAmount;

	private final static String TXT_ENTER_MISC_FEE =
		"Enter Miscellaneous Fee:";
	private final static String FRM_TITLE_PMT002_MISC_FEE =
		"Miscellaneous Fee    PMT002";
	private final static String FRM_TITLE_PMT002_VET_FUND =
		"Veterans' Fund         PMT002";
	private final static String TXT_ENTER_VET_FUND =
		"Enter Veterans' Fund Donation Amount:";
	// end defect 10122	
	// defect 10965
	protected static final int MAX_PARKS_FUND = 100000;
	private final static String FRM_TITLE_PMT002_PARKS_FUND =
		"State Parks Fund         PMT002";
	private final static String TXT_ENTER_PARKS_FUND =
		"Enter State Parks Fund Donation Amount. $5.00 minimum:";
	// end defect 10965
	private final static String TXT_OK = "OK";

	/**
	 * FrmMiscFeePMT002 constructor.
	 */
	public FrmMiscFeePMT002()
	{
		super();
		initialize();
	}

	/**
	 * FrmMiscFeePMT002 constructor.
	 * 
	 * @param aaOwner javax.swing.JDialog
	 */
	public FrmMiscFeePMT002(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmMiscFeePMT002 constructor.
	 * 
	 * @param aaOwner javax.swing.JFrame
	 */
	public FrmMiscFeePMT002(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when OK is clicked
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}

		if (aaAE.getSource() == getbtnOK())
		{
			if (validateFields())
			{
				setDataToDataObject();

				getController().processData(
					AbstractViewController.ENTER,
					caCompleteTransactionData);
			}
		}
	}

	/**
	 * Return the btnOK property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnOK()
	{
		if (ivjbtnOK == null)
		{
			try
			{
				ivjbtnOK =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnOK.setBounds(163, 72, 52, 26);
				ivjbtnOK.setName("btnOK");
				ivjbtnOK.setText(TXT_OK);
				// user code begin {1}
				ivjbtnOK.addActionListener(this);
				getRootPane().setDefaultButton(ivjbtnOK);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnOK;
	}

	/**
	 * Return the FrmMiscFeePMT002ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getFrmMiscFeePMT002ContentPane1()
	{
		if (ivjFrmMiscFeePMT002ContentPane1 == null)
		{
			try
			{
				ivjFrmMiscFeePMT002ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmMiscFeePMT002ContentPane1.setName(
					"FrmMiscFeePMT002ContentPane1");
				ivjFrmMiscFeePMT002ContentPane1.setLayout(null);
				ivjFrmMiscFeePMT002ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmMiscFeePMT002ContentPane1.setMinimumSize(
					new java.awt.Dimension(400, 125));

				ivjFrmMiscFeePMT002ContentPane1.add(
					getstcLblMiscFee(),
					null);
				ivjFrmMiscFeePMT002ContentPane1.add(
					gettxtMiscFees(),
					null);
				ivjFrmMiscFeePMT002ContentPane1.add(getbtnOK(), null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmMiscFeePMT002ContentPane1;
	}

	/**
	 * Return the stcLblMiscFee property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblMiscFee()
	{
		if (ivjstcLblMiscFee == null)
		{
			try
			{
				ivjstcLblMiscFee = new javax.swing.JLabel();
				ivjstcLblMiscFee.setBounds(12, 16, 333, 14);
				ivjstcLblMiscFee.setName("stcLblMiscFee");
				ivjstcLblMiscFee.setMaximumSize(
					new java.awt.Dimension(141, 14));
				ivjstcLblMiscFee.setMinimumSize(
					new java.awt.Dimension(141, 14));
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
		return ivjstcLblMiscFee;
	}

	/**
	 * Return the txtMiscFees property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMiscFees()
	{
		if (ivjtxtMiscFees == null)
		{
			try
			{
				ivjtxtMiscFees = new RTSInputField();
				ivjtxtMiscFees.setBounds(156, 44, 65, 20);
				ivjtxtMiscFees.setName("txtMiscFees");
				ivjtxtMiscFees.setMinimumSize(
					new java.awt.Dimension(4, 20));
				// defect 10122
				// Vol Vet Fund does not have max = 99.99
				//ivjtxtMiscFees.setMaxLength(5);
				ivjtxtMiscFees.setMaxLength(8);
				// end defect 10122
				ivjtxtMiscFees.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtMiscFees.setInput(5);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMiscFees;
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(FRM_NAME_PMT002);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(363, 134);
			setContentPane(getFrmMiscFeePMT002ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aaArgs String[]
	 */
	public static void main(String[] aaArgs)
	{
		try
		{
			FrmMiscFeePMT002 laFrmMiscFeePMT002;
			laFrmMiscFeePMT002 = new FrmMiscFeePMT002();
			laFrmMiscFeePMT002.setModal(true);
			laFrmMiscFeePMT002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmMiscFeePMT002.show();
			java.awt.Insets laInsets = laFrmMiscFeePMT002.getInsets();
			laFrmMiscFeePMT002.setSize(
				laFrmMiscFeePMT002.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmMiscFeePMT002.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmMiscFeePMT002.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}
	/**
	 * Process KeyReleasedEvents.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			getController().processData(
				AbstractViewController.CANCEL,
				caCompleteTransactionData);
		}
	}
	/**
	 * Received CompleteTransactionData to populate the screen
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		caCompleteTransactionData =
			(CompleteTransactionData) UtilityMethods.copy(aaDataObject);
		cvFeesData =
			caCompleteTransactionData.getRegFeesData().getVectFees();

		// defect 10122
		// Process payment type selected on PMT004

		//for (int i = 0; i < cvFeesData.size(); i++)
		//{
		//	FeesData laFeesData = (FeesData) cvFeesData.get(i);
		//	if (laFeesData.getAcctItmCd().equals(MISC_FEE_CODE)
		//		|| laFeesData.getAcctItmCd().equals(MISC_FEE_CODE_REGION))
		//	{
		//		caFeesDataMiscFee = laFeesData;
		//		gettxtMiscFees().setText(
		//			laFeesData.getItemPrice().toString());
		//		cbContainsMiscFee = true;
		//		break;
		//	}
		//}
		switch (VCFeesDuePMT004.getPMT004PymntType())
		{
			case VCFeesDuePMT004.MISC_FEES :

				// defect 10122
				ciPaymentType = VCFeesDuePMT004.getPMT004PymntType();
				ciMaxAmount = MAX_MISC_FEE;

				setTitle(FRM_TITLE_PMT002_MISC_FEE);
				ivjstcLblMiscFee.setText(TXT_ENTER_MISC_FEE);

				for (int i = 0; i < cvFeesData.size(); i++)
				{
					FeesData laFeesData = (FeesData) cvFeesData.get(i);
					if (laFeesData
						.getAcctItmCd()
						.equals(AcctCdConstant.MISC_FEE_CODE)
						|| laFeesData.getAcctItmCd().equals(
							AcctCdConstant.MISC_FEE_CODE_REGION))
					{
						caFeesDataMiscFee = laFeesData;
						gettxtMiscFees().setText(
							laFeesData.getItemPrice().toString());
						cbContainsMiscFee = true;
						break;
					}
				}
				break;

			case VCFeesDuePMT004.VET_FUND :

				ciPaymentType = VCFeesDuePMT004.getPMT004PymntType();
				ciMaxAmount = MAX_VET_FUND;

				setTitle(FRM_TITLE_PMT002_VET_FUND);
				ivjstcLblMiscFee.setText(TXT_ENTER_VET_FUND);

				for (int i = 0; i < cvFeesData.size(); i++)
				{
					FeesData laFeesData = (FeesData) cvFeesData.get(i);
					if (laFeesData
						.getAcctItmCd()
						.equals(AcctCdConstant.VET_FUND_CODE)
						|| laFeesData.getAcctItmCd().equals(
							AcctCdConstant.VET_FUND_CODE_REGION))
					{
						caFeesDataMiscFee = laFeesData;
						gettxtMiscFees().setText(
							laFeesData.getItemPrice().toString());
						cbContainsMiscFee = true;
						break;
					}
				}
				break;
			// defect 10965
			case VCFeesDuePMT004.PARKS_FUND :

				ciPaymentType = VCFeesDuePMT004.getPMT004PymntType();
				ciMaxAmount = MAX_PARKS_FUND;

				setTitle(FRM_TITLE_PMT002_PARKS_FUND);
				ivjstcLblMiscFee.setText(TXT_ENTER_PARKS_FUND);

				for (int i = 0; i < cvFeesData.size(); i++)
				{
					FeesData laFeesData = (FeesData) cvFeesData.get(i);
					if (laFeesData
						.getAcctItmCd()
						.equals(AcctCdConstant.PARKS_FUND_CODE)
						|| laFeesData.getAcctItmCd().equals(
							AcctCdConstant.PARKS_FUND_CODE_REGION))
					{
						caFeesDataMiscFee = laFeesData;
						gettxtMiscFees().setText(
							laFeesData.getItemPrice().toString());
						cbContainsMiscFee = true;
						break;
					}
				}
				break;
				// end defect 10965

			default :
				{
					// empty code block
				}
		}
		// end defect 10122

		gettxtMiscFees().requestFocus();

		if (!cbContainsMiscFee)
		{
			gettxtMiscFees().setText(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Set Screen Contents to Data Object 
	 */
	private void setDataToDataObject()
	{

		//Add data to caCompleteTransactionData    
		if (cbContainsMiscFee)
		{
			if (cdFeeEntered > 0)
			{
				//put misc fee
				caFeesDataMiscFee.setItemPrice(
					new Dollar(cdFeeEntered));
			}
			else
			{
				//remove the misc fee in vector
				cvFeesData.remove(caFeesDataMiscFee);
			}
		}
		else
		{
			if (cdFeeEntered > 0)
			{
				//add fee to vector

				// defect 10175 
				// 	MFVehicleData laMFVehicleData =
				//	caCompleteTransactionData.getVehicleInfo();
				//  int liRTSEffDate = 0;
				//	if (laMFVehicleData != null
				//		&& laMFVehicleData.getRegData() != null)
				//	{
				//		//RegistrationData lRegistrationData
				//		liRTSEffDate =
				//			laMFVehicleData.getRegData().getRegEffDt();
				//		//				}
				//	else
				//	{
				//		liRTSEffDate = new RTSDate().getYYYYMMDDDate();
				//	}
				// end defect 10175 

				// defect 10122
				// Use payment type selected on PMT004
				String lsPymntType_Cd = CommonConstant.STR_SPACE_EMPTY;

				if (ciPaymentType == VCFeesDuePMT004.MISC_FEES)
				{
					lsPymntType_Cd = AcctCdConstant.MISC_FEE_CODE;
				}
				else if (ciPaymentType == VCFeesDuePMT004.VET_FUND)
				{
					lsPymntType_Cd = AcctCdConstant.VET_FUND_CODE;
				}
				// defect 10965
				else if (ciPaymentType == VCFeesDuePMT004.PARKS_FUND)
				{
					lsPymntType_Cd = AcctCdConstant.PARKS_FUND_CODE;
				}
				// end defect 10965

				// defect 10175 				
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						lsPymntType_Cd,
						new RTSDate().getYYYYMMDDDate());
				// end defect 10175 

				FeesData laFeesData = new FeesData();
				
				//if (laAccountCodesData == null)
				//{
				//	laFeesData.setDesc(MISC_FEE_DESC);
				//}
				//else
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
					laFeesData.setCrdtAllowedIndi(
						laAccountCodesData.getCrdtAllowdIndi());
				}

				laFeesData.setItmQty(1);
				//laFeesData.setAcctItmCd(MISC_FEE_CODE);
				laFeesData.setAcctItmCd(lsPymntType_Cd);
				laFeesData.setItemPrice(new Dollar(cdFeeEntered));
				cvFeesData.addElement(laFeesData);
				// end defect 10122							
			}

		}
	}

	/**
	 * Validate fields
	 * @return boolean
	 */
	protected boolean validateFields()
	{
		boolean lbReturn = true;
		RTSException leRTSEx = new RTSException();

		try
		{
			clearAllColor(this);
			//Validation
			String lsTxtMiscFees = gettxtMiscFees().getText();
			//number validation
			try
			{
				if (lsTxtMiscFees
					.trim()
					.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					cdFeeEntered = 0;
				}
				else
				{
					cdFeeEntered = Double.parseDouble(lsTxtMiscFees);
				}
			}
			catch (NumberFormatException aeNFE)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtMiscFees());
			}

			//range validation
			// defect 10122
			// Misc Fee and Volunteer Vet Fund have different max amts
			//if ((ldMiscFees < 0) || (ldMiscFees >= MAX_MISC_FEE))
			if (cdFeeEntered < 0 || cdFeeEntered >= ciMaxAmount)
			{
				// end defect 10122
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtMiscFees());
			}

			//decimal validation
			int liDotIndex = lsTxtMiscFees.indexOf(DOT);
			if (liDotIndex != -1)
			{
				int liStringLength = lsTxtMiscFees.length();
				if ((liStringLength - liDotIndex - 1) > DECIMAL_NUMBER)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtMiscFees());
				}
			}

			// add validation errors
			if (leRTSEx.isValidationError())
			{
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
				lbReturn = false;
			}
			return lbReturn;

		}
		finally
		{
			doneWorking();
		}
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
