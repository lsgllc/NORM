package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * 
 * FrmAdditionalSalesTaxScreenTTL033.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validations update
 * T Pederson	04/22/2002	made doc no accept numeric only
 * MAbs			05/29/2002	Lined up textfields CQU100004129
 * J Zwiener    12/30/2003  Catch exception when entering just a "."
 * 							in Sales Tax Amt.
 *                          Method actionPerformed(ActionEvent)
 *                          defect 6418  Ver 5.1.5.2
 * J Rue		02/16/2005	Cleanup VAJ to WSAD
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/08/2005	Change AbstractViewController to 
 * 							getController()
 * 							modify actionPerformed()
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/06/2005	Set catch Parm aa to le
 * 							deprecated displayError(int)
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3       
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/16/2005	Add constants ZERO_FLT, SLSTX
 * 							TTL033_FRAME_TITLE, TTL033_FRAME_NAME
 * 							defect 7898 
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	12/09/2005	Convert I/O to 1/0 
 * 							add FocusListener
 * 							add focusGained(),focusLost()
 * 							modify gettxtVIN()
 * 							defect 8466 Ver 5.2.2 Fix 8    
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify actionPerformed()
 * 							defect 8902 Ver Exempts
 * K Harrell	04/06/2007	CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * This frame is used to capture any additional sales tax amount
 * and associated vehicle info.
 *
 * @version	Special Plates	04/06/2007
 * @author	Ashish Mahajan
 * <br>Creation Date:		08/03/2001 16:39:44
 */

public class FrmAdditionalSalesTaxScreenTTL033
	extends RTSDialogBox
	implements ActionListener, FocusListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblAdditionalSalesTaxAmt = null;
	private JLabel ivjstcLblAPlateNo = null;
	private JLabel ivjstcLblDocumentNo = null;
	private JLabel ivjstcLblEnterthefollowing = null;
	private JLabel ivjstcLblExpYr = null;
	private JLabel ivjstcLblVIN = null;
	private RTSInputField ivjtxtAdditionalSalesTaxAmt = null;
	private RTSInputField ivjtxtDocNo = null;
	private RTSInputField ivjtxtExpYr = null;
	private RTSInputField ivjtxtPlateNo = null;
	private RTSInputField ivjtxtVIN = null;
	private JPanel ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1 =
		null;
	private CompleteTransactionData caCompleteTtlTrans = null;

	// defect 7898
	// Constant int
	private final static int ADDL_SALES_TAX_MAX_LEN = 9;
	private final static int DOCNO_MAX_LEN = 17;
	private final static int YEAR_MAX_LEN = 4;
	private final static int PLATE_MAX_LEN = 7;
	private final static int VIN_MAX_LEN = 22;

	//	Add constant Sales Tax
	private final static String SLSTX = ReportConstant.SLSTX_UC;
	private final static String S999999_99 = "999999.99";
	private final static String ADDL_SALES_TAX_AMT =
		"Additional Sales Tax Amt:";
	private final static String PLATE_NO = "Plate No:";
	private final static String DOCUMENT_NO = "Document No:";
	private final static String ENTER_THE_FOLLOWING =
		"Enter the following:";
	private final static String EXP_YR = "Exp Yr:";
	private final static String VIN = "VIN:";

	/**
	 * FrmAdditionalSalesTaxScreenTTL033 constructor comment.
	 */
	public FrmAdditionalSalesTaxScreenTTL033()
	{
		super();
		initialize();
	}
	/**
	 * FrmAdditionalSalesTaxScreenTTL033 constructor comment.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmAdditionalSalesTaxScreenTTL033(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmAdditionalSalesTaxScreenTTL033 constructor comment.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmAdditionalSalesTaxScreenTTL033(
		javax.swing.JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when user performs an action.  The user completes the 
	 * event by
	 * inputting the amount and pressing enter.
	 *
	 * Cancel or Help buttons respectively result in destroying the 
	 * window or
	 * providing appropriate help.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		//Code to avoid clicking on the button multiple times
		if (!startWorking())
		{
			return;
		}

		RTSException leRTSEx = new RTSException();

		try
		{
			//Clear All Fields
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				//convert i to 1 and o to 0
				String lsVin = gettxtVIN().getText().trim();
				// defect 8902
				lsVin =
					CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
				// end defect 8902

				String lsPlateNo = gettxtPlateNo().getText().trim();
				String lsDocNo = gettxtDocNo().getText().trim();
				String lsYr = gettxtExpYr().getText().trim();
				String lsSlsPrc =
					gettxtAdditionalSalesTaxAmt().getText().trim();
				Dollar laSlsPrc =
					new Dollar(CommonConstant.STR_ZERO_DOLLAR);
				// Defect 6418 begin			
				try
				{
					if (lsSlsPrc != null
						&& !lsSlsPrc.equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						laSlsPrc = new Dollar(lsSlsPrc);
					}
				}
				catch (NumberFormatException aeNFEx)
				{
					gettxtAdditionalSalesTaxAmt().setText(
						CommonConstant.STR_SPACE_EMPTY);
					leRTSEx.addException(
						new RTSException(150),
						gettxtAdditionalSalesTaxAmt());
				}
				// Defect 6418 end	
				Dollar laZero =
					new Dollar(CommonConstant.STR_ZERO_DOLLAR);
				Dollar laUprLmt = new Dollar(S999999_99);
				int liYr = 0;
				try
				{
					liYr = Integer.parseInt(lsYr);
				}
				catch (NumberFormatException aeNFEx)
				{
					liYr = 0;

				}

				// Check that expiration year is between 1900 and 2100
				if (!(liYr == 0 || (liYr >= 1900 && liYr <= 2100)))
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtExpYr());
				}
				//document no validation - must be length of 17 if 
				//	entered
				if (lsDocNo.length() > 0
					&& lsDocNo.length() < DOCNO_MAX_LEN)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtDocNo());
				}
				if ((lsYr.length() > 0
					|| lsPlateNo.length() > 0
					|| lsVin.length() > 0
					|| lsDocNo.length() > 0)
					&& lsSlsPrc.length() < 1)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtAdditionalSalesTaxAmt());
				}
				// Check that amount entered is greater than zero and 
				//	less than 1,000,000	
				if (!(laSlsPrc.compareTo(laZero) > 0)
					|| laSlsPrc.compareTo(laUprLmt) > 0)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtAdditionalSalesTaxAmt());

				}
				//decimal validation
				int liDotIndex =
					lsSlsPrc.indexOf(CommonConstant.STR_PERIOD);
				if (liDotIndex != -1)
				{
					int liStringLength = lsSlsPrc.length();
					if ((liStringLength - liDotIndex - 1) > 2)
						leRTSEx.addException(
							new RTSException(150),
							gettxtAdditionalSalesTaxAmt());
				}

				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
			}

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				doCompleteTransaction();
				// defect 7898
				//	Change controller to getController()
				getController().processData(
					AbstractViewController.ENTER,
					caCompleteTtlTrans);

			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caCompleteTtlTrans);
				// end defect 7898
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL033);
			}
		}
		finally
		{
			doneWorking();
		}

	}
	/**
	 * Creates the CompleteTransaction object which includes all data 
	 * input on the TTL033 screen.
	 * 
	 */
	private void doCompleteTransaction()
	{
		try
		{
			CompleteTitleTransaction laTtlTrans =
				new CompleteTitleTransaction(
					null,
					TransCdConstant.ADLSTX);
			caCompleteTtlTrans = laTtlTrans.doCompleteTransaction();

			String lsPltNo = gettxtPlateNo().getText().trim();
			String lsVIN = gettxtVIN().getText().trim();
			String lsExpYr = gettxtExpYr().getText().trim();
			String lsDocNo = gettxtDocNo().getText().trim();
			String lsSlsTaxAmt =
				gettxtAdditionalSalesTaxAmt().getText().trim();
			int liExpYr = 0;
			// defect 7898
			//	Use constant ZERO_FLT
			Dollar laSalesAmt =
				new Dollar(CommonConstant.STR_ZERO_DOLLAR);
			// end defect 7898
			try
			{
				if (!lsExpYr.equals(CommonConstant.STR_SPACE_EMPTY)
					&& lsExpYr != null)
				{
					liExpYr = Integer.parseInt(lsExpYr);
				}
				laSalesAmt = new Dollar(lsSlsTaxAmt);

			}
			catch (NumberFormatException aeNFEx)
			{
				// This page left empty extensionally
			}
			if (caCompleteTtlTrans != null)
			{
				MFVehicleData laMfVehData = new MFVehicleData();
				TitleData laTtlDatla = new TitleData();
				RegistrationData laRegData = new RegistrationData();
				VehicleData laVehData = new VehicleData();
				FeesData laFees = new FeesData();

				laMfVehData.setTitleData(laTtlDatla);
				laMfVehData.setRegData(laRegData);
				laMfVehData.setVehicleData(laVehData);

				// Set up the fee vector
				laFees.setItemPrice(laSalesAmt);
				// defect 7898
				//	Set "SLSTX" to a constant
				laFees.setAcctItmCd(SLSTX);
				AccountCodesData laAcctData =
					com
						.txdot
						.isd
						.rts
						.services
						.cache
						.AccountCodesCache
						.getAcctCd(
						SLSTX,
						new RTSDate().getYYYYMMDDDate());
				// end defect 7898
				if (laAcctData != null)
				{
					laFees.setDesc(laAcctData.getAcctItmCdDesc());
				}
				laFees.setItmQty(1);

				RegFeesData laRegFeesData =
					caCompleteTtlTrans.getRegFeesData();
				Vector lvFeesData = laRegFeesData.getVectFees();
				lvFeesData.add(laFees);

				// Set data input on the screen
				laRegData.setRegPltNo(lsPltNo);
				laRegData.setRegExpYr(liExpYr);
				laRegData.setRegEffDt(
					RTSDate.getCurrentDate().getYYYYMMDDDate());
				laTtlDatla.setDocNo(lsDocNo);
				laVehData.setVin(lsVIN);
				laTtlDatla.setSalesTaxPdAmt(laSalesAmt);

				caCompleteTtlTrans.setVehicleInfo(laMfVehData);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
		catch (Exception aeRTSEx)
		{
			// empty code block
		}
	}
	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE  FocusEvent 
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// defect 8466
		// Convert I/O => 1/0 
		if (!aaFE.isTemporary() && aaFE.getSource() == gettxtVIN())
		{
			String lsVin = gettxtVIN().getText().trim().toUpperCase();
			// defect 8902
			lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
			// end defect 8902
			gettxtVIN().setText(lsVin);
		}
		// end defect 8466 
	}
	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE  FocusEvent 
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		// Intentially left blank
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
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}
	/**
	 * Return the FrmAdditionalSalesTaxScreenTTL033ContentPane1 property
	 *  value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmAdditionalSalesTaxScreenTTL033ContentPane1()
	{
		if (ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1 == null)
		{
			try
			{
				ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1
					.setName(
					"FrmAdditionalSalesTaxScreenTTL033ContentPane1");
				ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(672, 454));
				ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

				java
					.awt
					.GridBagConstraints constraintsstcLblEnterthefollowing =
					new java.awt.GridBagConstraints();
				constraintsstcLblEnterthefollowing.gridx = 1;
				constraintsstcLblEnterthefollowing.gridy = 1;
				constraintsstcLblEnterthefollowing.ipadx = 19;
				constraintsstcLblEnterthefollowing.insets =
					new java.awt.Insets(16, 26, 13, 18);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					getstcLblEnterthefollowing(),
					constraintsstcLblEnterthefollowing);

				java.awt.GridBagConstraints constraintsstcLblAPlateNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblAPlateNo.gridx = 1;
				constraintsstcLblAPlateNo.gridy = 2;
				constraintsstcLblAPlateNo.ipadx = 18;
				constraintsstcLblAPlateNo.insets =
					new java.awt.Insets(16, 99, 6, 4);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					getstcLblAPlateNo(),
					constraintsstcLblAPlateNo);

				java.awt.GridBagConstraints constraintsstcLblExpYr =
					new java.awt.GridBagConstraints();
				constraintsstcLblExpYr.gridx = 3;
				constraintsstcLblExpYr.gridy = 2;
				constraintsstcLblExpYr.ipadx = 6;
				constraintsstcLblExpYr.ipady = 6;
				constraintsstcLblExpYr.insets =
					new java.awt.Insets(13, 4, 3, 0);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					getstcLblExpYr(),
					constraintsstcLblExpYr);

				java.awt.GridBagConstraints constraintsstcLblVIN =
					new java.awt.GridBagConstraints();
				constraintsstcLblVIN.gridx = 1;
				constraintsstcLblVIN.gridy = 3;
				constraintsstcLblVIN.ipadx = 23;
				constraintsstcLblVIN.insets =
					new java.awt.Insets(6, 122, 6, 4);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					getstcLblVIN(),
					constraintsstcLblVIN);

				java.awt.GridBagConstraints constraintsstcLblDocumentNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblDocumentNo.gridx = 1;
				constraintsstcLblDocumentNo.gridy = 4;
				constraintsstcLblDocumentNo.ipadx = 13;
				constraintsstcLblDocumentNo.insets =
					new java.awt.Insets(7, 75, 7, 4);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					getstcLblDocumentNo(),
					constraintsstcLblDocumentNo);

				java
					.awt
					.GridBagConstraints constraintsstcLblAdditionalSalesTaxAmt =
					new java.awt.GridBagConstraints();
				constraintsstcLblAdditionalSalesTaxAmt.gridx = 1;
				constraintsstcLblAdditionalSalesTaxAmt.gridy = 5;
				constraintsstcLblAdditionalSalesTaxAmt.ipadx = 8;
				constraintsstcLblAdditionalSalesTaxAmt.insets =
					new java.awt.Insets(8, 15, 14, 4);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					getstcLblAdditionalSalesTaxAmt(),
					constraintsstcLblAdditionalSalesTaxAmt);

				java.awt.GridBagConstraints constraintstxtPlateNo =
					new java.awt.GridBagConstraints();
				constraintstxtPlateNo.gridx = 2;
				constraintstxtPlateNo.gridy = 2;
				constraintstxtPlateNo.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtPlateNo.weightx = 1.0;
				constraintstxtPlateNo.ipadx = 63;
				constraintstxtPlateNo.insets =
					new java.awt.Insets(13, 5, 3, 3);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					gettxtPlateNo(),
					constraintstxtPlateNo);

				java.awt.GridBagConstraints constraintstxtExpYr =
					new java.awt.GridBagConstraints();
				constraintstxtExpYr.gridx = 4;
				constraintstxtExpYr.gridy = 2;
				constraintstxtExpYr.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtExpYr.weightx = 1.0;
				constraintstxtExpYr.ipadx = 45;
				constraintstxtExpYr.insets =
					new java.awt.Insets(13, 0, 3, 148);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					gettxtExpYr(),
					constraintstxtExpYr);

				java.awt.GridBagConstraints constraintstxtVIN =
					new java.awt.GridBagConstraints();
				constraintstxtVIN.gridx = 2;
				constraintstxtVIN.gridy = 3;
				constraintstxtVIN.gridwidth = 3;
				constraintstxtVIN.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtVIN.weightx = 1.0;
				constraintstxtVIN.ipadx = 261;
				constraintstxtVIN.insets =
					new java.awt.Insets(3, 5, 3, 51);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					gettxtVIN(),
					constraintstxtVIN);

				java.awt.GridBagConstraints constraintstxtDocNo =
					new java.awt.GridBagConstraints();
				constraintstxtDocNo.gridx = 2;
				constraintstxtDocNo.gridy = 4;
				constraintstxtDocNo.gridwidth = 3;
				constraintstxtDocNo.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtDocNo.weightx = 1.0;
				constraintstxtDocNo.ipadx = 196;
				constraintstxtDocNo.insets =
					new java.awt.Insets(4, 5, 4, 116);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					gettxtDocNo(),
					constraintstxtDocNo);

				java
					.awt
					.GridBagConstraints constraintstxtAdditionalSalesTaxAmt =
					new java.awt.GridBagConstraints();
				constraintstxtAdditionalSalesTaxAmt.gridx = 2;
				constraintstxtAdditionalSalesTaxAmt.gridy = 5;
				constraintstxtAdditionalSalesTaxAmt.gridwidth = 2;
				constraintstxtAdditionalSalesTaxAmt.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtAdditionalSalesTaxAmt.weightx = 1.0;
				constraintstxtAdditionalSalesTaxAmt.ipadx = 94;
				constraintstxtAdditionalSalesTaxAmt.insets =
					new java.awt.Insets(5, 5, 11, 21);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					gettxtAdditionalSalesTaxAmt(),
					constraintstxtAdditionalSalesTaxAmt);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 6;
				constraintsButtonPanel1.gridwidth = 4;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 105;
				constraintsButtonPanel1.ipady = 34;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(12, 85, 18, 85);
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmAdditionalSalesTaxScreenTTL033ContentPane1;
	}
	/**
	 * Return the stcLblAdditionalSalesTaxAmt property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblAdditionalSalesTaxAmt()
	{
		if (ivjstcLblAdditionalSalesTaxAmt == null)
		{
			try
			{
				ivjstcLblAdditionalSalesTaxAmt =
					new javax.swing.JLabel();
				ivjstcLblAdditionalSalesTaxAmt.setName(
					"stcLblAdditionalSalesTaxAmt");
				ivjstcLblAdditionalSalesTaxAmt.setText(
					ADDL_SALES_TAX_AMT);
				ivjstcLblAdditionalSalesTaxAmt.setMaximumSize(
					new java.awt.Dimension(144, 14));
				ivjstcLblAdditionalSalesTaxAmt.setMinimumSize(
					new java.awt.Dimension(144, 14));
				ivjstcLblAdditionalSalesTaxAmt.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblAdditionalSalesTaxAmt;
	}
	/**
	 * Return the stcLblAPlateNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblAPlateNo()
	{
		if (ivjstcLblAPlateNo == null)
		{
			try
			{
				ivjstcLblAPlateNo = new javax.swing.JLabel();
				ivjstcLblAPlateNo.setName("stcLblAPlateNo");
				ivjstcLblAPlateNo.setText(PLATE_NO);
				ivjstcLblAPlateNo.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblAPlateNo.setMinimumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblAPlateNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblAPlateNo;
	}
	/**
	 * Return the stcLblDocumentNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblDocumentNo()
	{
		if (ivjstcLblDocumentNo == null)
		{
			try
			{
				ivjstcLblDocumentNo = new javax.swing.JLabel();
				ivjstcLblDocumentNo.setName("stcLblDocumentNo");
				ivjstcLblDocumentNo.setText(DOCUMENT_NO);
				ivjstcLblDocumentNo.setMaximumSize(
					new java.awt.Dimension(79, 14));
				ivjstcLblDocumentNo.setMinimumSize(
					new java.awt.Dimension(79, 14));
				ivjstcLblDocumentNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDocumentNo;
	}
	/**
	 * Return the stcLblEnterthefollowing property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblEnterthefollowing()
	{
		if (ivjstcLblEnterthefollowing == null)
		{
			try
			{
				ivjstcLblEnterthefollowing = new javax.swing.JLabel();
				ivjstcLblEnterthefollowing.setName(
					"stcLblEnterthefollowing");
				ivjstcLblEnterthefollowing.setText(ENTER_THE_FOLLOWING);
				ivjstcLblEnterthefollowing.setMaximumSize(
					new java.awt.Dimension(108, 14));
				ivjstcLblEnterthefollowing.setMinimumSize(
					new java.awt.Dimension(108, 14));
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEnterthefollowing;
	}
	/**
	 * Return the stcLblExpYr property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblExpYr()
	{
		if (ivjstcLblExpYr == null)
		{
			try
			{
				ivjstcLblExpYr = new javax.swing.JLabel();
				ivjstcLblExpYr.setName("stcLblExpYr");
				ivjstcLblExpYr.setText(EXP_YR);
				ivjstcLblExpYr.setMaximumSize(
					new java.awt.Dimension(39, 14));
				ivjstcLblExpYr.setMinimumSize(
					new java.awt.Dimension(39, 14));
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblExpYr;
	}
	/**
	 * Return the stcLblVIN property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			try
			{
				ivjstcLblVIN = new javax.swing.JLabel();
				ivjstcLblVIN.setName("stcLblVIN");
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setMinimumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVIN;
	}
	/**
	 * Return the txtAdditionalSalesTaxAmt property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtAdditionalSalesTaxAmt()
	{
		if (ivjtxtAdditionalSalesTaxAmt == null)
		{
			try
			{
				ivjtxtAdditionalSalesTaxAmt = new RTSInputField();
				ivjtxtAdditionalSalesTaxAmt.setName(
					"txtAdditionalSalesTaxAmt");
				ivjtxtAdditionalSalesTaxAmt.setInput(
					RTSInputField.DOLLAR_ONLY);
				ivjtxtAdditionalSalesTaxAmt.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtAdditionalSalesTaxAmt.setMaxLength(
					ADDL_SALES_TAX_MAX_LEN);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtAdditionalSalesTaxAmt;
	}
	/**
	 * Return the txtDocNo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtDocNo()
	{
		if (ivjtxtDocNo == null)
		{
			try
			{
				ivjtxtDocNo = new RTSInputField();
				ivjtxtDocNo.setName("txtDocNo");
				ivjtxtDocNo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtDocNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtDocNo.setMaxLength(DOCNO_MAX_LEN);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtDocNo;
	}
	/**
	 * Return the txtExpYr property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtExpYr()
	{
		if (ivjtxtExpYr == null)
		{
			try
			{
				ivjtxtExpYr = new RTSInputField();
				ivjtxtExpYr.setName("txtExpYr");
				ivjtxtExpYr.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtExpYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtExpYr.setMaxLength(YEAR_MAX_LEN);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtExpYr;
	}
	/**
	 * Return the txtPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtPlateNo()
	{
		if (ivjtxtPlateNo == null)
		{
			try
			{
				ivjtxtPlateNo = new RTSInputField();
				ivjtxtPlateNo.setName("txtPlateNo");
				ivjtxtPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPlateNo.setMaxLength(PLATE_MAX_LEN);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPlateNo;
	}
	/**
	 * Return the txtVIN property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVIN()
	{
		if (ivjtxtVIN == null)
		{
			try
			{
				ivjtxtVIN = new RTSInputField();
				ivjtxtVIN.setName("txtVIN");
				ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVIN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVIN.setMaxLength(VIN_MAX_LEN);
				// user code begin {1}
				// defect 8466 
				ivjtxtVIN.addFocusListener(this);
				// end defect 8466
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVIN;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable aeException)
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
			setName(ScreenConstant.TTL033_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(492, 268);
			setTitle(ScreenConstant.TTL033_FRAME_TITLE);
			setContentPane(
				getFrmAdditionalSalesTaxScreenTTL033ContentPane1());
		}
		catch (java.lang.Throwable aeIVJEx)
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
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmAdditionalSalesTaxScreenTTL033 laFrmAdditionalSalesTaxScreenTTL033;
			laFrmAdditionalSalesTaxScreenTTL033 =
				new FrmAdditionalSalesTaxScreenTTL033();
			laFrmAdditionalSalesTaxScreenTTL033.setModal(true);
			laFrmAdditionalSalesTaxScreenTTL033
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmAdditionalSalesTaxScreenTTL033.show();
			java.awt.Insets insets =
				laFrmAdditionalSalesTaxScreenTTL033.getInsets();
			laFrmAdditionalSalesTaxScreenTTL033.setSize(
				laFrmAdditionalSalesTaxScreenTTL033.getWidth()
					+ insets.left
					+ insets.right,
				laFrmAdditionalSalesTaxScreenTTL033.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmAdditionalSalesTaxScreenTTL033.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeIVJEx.printStackTrace(System.out);
		}
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 */
	public void setData(Object dataObject)
	{
		// empty code block
	}
}
