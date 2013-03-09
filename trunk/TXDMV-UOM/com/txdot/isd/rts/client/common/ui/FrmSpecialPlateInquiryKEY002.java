package com.txdot.isd.rts.client.common.ui;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.client.specialplates.business.SpecialPlatesClientUtilityMethods;

import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.SpecialPlatesRegisData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/* 
 * FrmSpecialPlateInquiryKEY002.java
 * 
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		02/08/2007	New class
 * 							defect 9086 Ver Special Plates 
 * J Rue		02/09/2007	Add inquiry code. Disable Same and Help
 * 							defect 9086 Ver Special Plates
 * J Rue		02/15/2007	Add ECH button panel
 * 							add getButtonPanel1()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/23/2007	Move from .client.SpecialPlates.ui
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/04/2007	Removed exception upon return from search
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates 
 * J Rue		03/07/2007	Remove unused and copy code from KEY001.
 * 							add dispInvalidFieldEntryExp()
 * 							actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	03/21/2007	Do not present Same Special Plate when not
 * 							 in Special Plates Events. Clear and disable 
 * 							 gettxtSpecialPlateNo() if getchkSame() 
 * 							 selected. 
 * 							add itemStateChanged()
 * 							modify actionPerformed(),getchkSame(),
 * 							 setData(),plateKeyValidation()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/09/2007	use buildGSD from SPClientUtilityMethods.
 * 							Manage focus upon return from search w/ 
 * 							error. 
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * B Hargrove	04/17/2007	Add Customer Supplied checkbox. Disable this
 * 							checkbox if in a Special Plates event.
 * 							Handle selection\enabling based on selection
 * 							of 'Same Plate'.
 * 							add wsdchkCustSupplied, getchkCustSupplied(),
 * 							isCustomerSupplied()
 * 							modify itemStateChanged()
 * 							defect 9126 Ver Special Plates
 * K Harrell	04/18/2007	add cbInitRegRcdInMFDown,
 * 							 setInitRegRcdInMFDown()
 * 							defect 9085 Ver Special Plates
 * B Hargrove	05/31/2007	After further review, remove Customer 
 * 							Supplied checkbox (it will be on REG029).
 * 							Un-do all the stuff I added on 04/17.
 * 							defect 9126 Ver Special Plates
 * K Harrell	06/18/2007	Add temporary error msg for Help. 
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	04/09/2010	Remove message if Plate No length is 7
 * 							delete REQD_PLT
 * 							modify plateKeyValidation() 
 * 							defect 10430 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**
 * Special Plates, enter RegPltNo. Query Special Plates Regis file.
 * 
 * @version	POS_640 		04/09/2010
 * @author	Jeff Rue
 * <br>Creation Date:		02/08/007 16:18:09
 */

public class FrmSpecialPlateInquiryKEY002
	extends RTSDialogBox
	implements ActionListener, ItemListener
{

	private JPanel wsdRTSDialogBoxContentPane = null;
	private JLabel wsdstcLblSpecialPlateNo = null;
	private JCheckBox wsdchkSame = null;
	private RTSInputField wsdtxtSpecialPlateNo = null;
	private ButtonPanel wsdButtonPanel1 = null;

	// String
	private String csTransCd;

	// Object 
	private VehicleInquiryData caVehInqData = new VehicleInquiryData();
	private SpecialPlatesRegisData caSpclPltRegisData =
		new SpecialPlatesRegisData();

	// boolean 
	private boolean cbInitRegRcdInMFDown = false;

	// Constant 
	private static final int MAX_PLATE_NO = 7;

	// defect 10430 
	//private final static String REQD_PLT =
	//	"Requested plate number is 7 digits.";
	// end defect 10430 

	private static final String SPECIAL_PLATE_NO = "Special Plate No:";
	private static final String SAME_SPECIAL_PLATE =
		"Same Special Plate";

	/**
	 * SpecialPlateInquiryKey002 constructor
	 */
	public FrmSpecialPlateInquiryKEY002()
	{
		super();
		initialize();
	}
	
	/**
	 * SpecialPlateInquiryKey002 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSpecialPlateInquiryKEY002(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * SpecialPlateInquiryKey002 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSpecialPlateInquiryKEY002(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking() || isVisible() == false)
		{
			return;
		}
		try
		{
			clearAllColor(this);
			
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (getchkSame().isSelected())
				{
					VehicleInquiryData laVehInqData =
						new VehicleInquiryData();
						
					MFVehicleData laMFVehData = new MFVehicleData();
					
					laMFVehData.setSpclPltRegisData(caSpclPltRegisData);
					
					laVehInqData.setMfVehicleData(laMFVehData);
					
					SpecialPlatesClientUtilityMethods
						.verifyRecordApplicable(
						caVehInqData,
						laVehInqData,
						csTransCd);

					getController().processData(
						VCSpecialPlateInquiryKEY002.GET_SAME_SPCL_PLT,
						laVehInqData);
				}
				else
				{

					// Validate plate number
					String lsRegPltNo =
						gettxtSpecialPlateNo().getText().trim();
						
					if (plateKeyValidation(lsRegPltNo))
					{
						// Build GSD
						GeneralSearchData laSearchCriteria =
							SpecialPlatesClientUtilityMethods
								.buildSpclPltNoSearchGSD(
								lsRegPltNo,
								csTransCd);
								
						getController().processData(
							AbstractViewController.ENTER,
							laSearchCriteria);
					}
					else
					{
						gettxtSpecialPlateNo().requestFocus();
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
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
		finally
		{
			doneWorking();
			// defect 9085 
			if (getDefaultFocusField() != null
				&& getDefaultFocusField().isEnabled()
				&& getDefaultFocusField().isEnabled())
			{
				getDefaultFocusField().requestFocus();
			}
			// end defect 9085 
		}
	}

	/**
	 * Return the stcLbl property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (wsdRTSDialogBoxContentPane == null)
		{
			try
			{
				wsdRTSDialogBoxContentPane = new JPanel();
				wsdRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				wsdRTSDialogBoxContentPane.setLayout(null);
				wsdRTSDialogBoxContentPane.add(
					getstcLblSpecialPlateNo(),
					getstcLblSpecialPlateNo().getName());
				wsdRTSDialogBoxContentPane.add(
					getchkSame(),
					getchkSame().getName());
				wsdRTSDialogBoxContentPane.add(
					gettxtSpecialPlateNo(),
					gettxtSpecialPlateNo().getName());
				wsdRTSDialogBoxContentPane.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return wsdRTSDialogBoxContentPane;
	}

	/**
	 * This method initializes the class
	 * 
	 */
	private void initialize()
	{
		try
		{

			this.setContentPane(getRTSDialogBoxContentPane());
			setName(ScreenConstant.KEY002_FRM_TITLE);
			setTitle(ScreenConstant.KEY002_FRM_TITLE);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(413, 214);
			setRequestFocus(false);
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}
	
	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		clearAllColor(this);

		if (aaIE.getSource() == wsdchkSame)
		{
			if (wsdchkSame.isSelected())
			{
				gettxtSpecialPlateNo().setEnabled(false);
				wsdstcLblSpecialPlateNo.setEnabled(false);
				wsdtxtSpecialPlateNo.setText("");
			}
			else
			{
				gettxtSpecialPlateNo().setEnabled(true);
				wsdstcLblSpecialPlateNo.setEnabled(true);
				gettxtSpecialPlateNo().requestFocus(true);
			}
		}
	}
	
	/**
	 * This method initializes wsdstcLblSpecialPlateNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSpecialPlateNo()
	{
		if (wsdstcLblSpecialPlateNo == null)
		{
			try
			{
				wsdstcLblSpecialPlateNo = new JLabel();
				wsdstcLblSpecialPlateNo.setSize(95, 20);
				wsdstcLblSpecialPlateNo.setText(SPECIAL_PLATE_NO);
				// user code begin {1}
				wsdstcLblSpecialPlateNo.setLocation(84, 43);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return wsdstcLblSpecialPlateNo;
	}
	
	/**
	 * This method initializes wsdchkSame
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSame()
	{
		if (wsdchkSame == null)
		{
			try
			{
				wsdchkSame = new JCheckBox();
				wsdchkSame.setSize(149, 20);
				wsdchkSame.setName("chkSame");
				wsdchkSame.setFont(new Font("Arial", 1, 12));
				wsdchkSame.setMnemonic(KeyEvent.VK_M);
				wsdchkSame.setText(SAME_SPECIAL_PLATE);
				wsdchkSame.setLocation(245, 82);
				wsdchkSame.addItemListener(this);

			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return wsdchkSame;
	}
	
	/**
	 * Return the ECH property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel1()
	{
		if (wsdButtonPanel1 == null)
		{
			try
			{
				wsdButtonPanel1 = new ButtonPanel();
				wsdButtonPanel1.setBounds(66, 137, 273, 42);
				wsdButtonPanel1.setName("ButtonPanel1");
				wsdButtonPanel1.addActionListener(this);
				wsdButtonPanel1.setAsDefault(this);
				// user code 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return wsdButtonPanel1;
	}
	
	/**
	 * Return the wsdtxtSpecialPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtSpecialPlateNo()
	{
		if (wsdtxtSpecialPlateNo == null)
		{
			try
			{
				wsdtxtSpecialPlateNo = new RTSInputField();
				wsdtxtSpecialPlateNo.setSize(90, 20);
				wsdtxtSpecialPlateNo.setName("txtSpecialPlateNo");
				wsdtxtSpecialPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				wsdtxtSpecialPlateNo.setMaxLength(MAX_PLATE_NO);
				wsdtxtSpecialPlateNo.setLocation(186, 43);
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
		return wsdtxtSpecialPlateNo;
	}
	
	/**
	 * Called whenever an exception is thrown
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
	 * Plate key validation on empty and length
	 * 
	 * @param asRegPltNo 
	 * @return boolean 
	 */
	private boolean plateKeyValidation(String asRegPltNo)
	{
		RTSException leRTSEx = new RTSException();
		boolean lbRtn = true;

		// Check if plate number was entered
		if ((asRegPltNo == null || asRegPltNo.length() == 0)
			&& !(getchkSame().isVisible() && getchkSame().isSelected()))
		{
			leRTSEx.addException(
				new RTSException(150),
				gettxtSpecialPlateNo());
			leRTSEx.displayError(this);
			lbRtn = false;
		}
		// defect 10430 
		//		// Check if plate number = 7 digits
		//		else if (asRegPltNo.length() == MAX_PLATE_NO)
		//		{
		//			leRTSEx =
		//				new RTSException(RTSException.CTL001, REQD_PLT, null);
		//			int liResult = leRTSEx.displayError(this);
		//
		//			// If answer is NO, return to input field
		//			if (liResult == RTSException.NO
		//				|| liResult == RTSException.CANCEL)
		//			{
		//				lbRtn = false;
		//			}
		// 		}
		// end defect 10430 
		return lbRtn;
	}
	
	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		csTransCd = getController().getTransCode();
		setDefaultFocusField(gettxtSpecialPlateNo());
		caVehInqData = (VehicleInquiryData) aaDataObject;

		// Do not enable "Same Special Plate" if in 
		// Special Plates Events
		setInitRegRcdInMFDown();
		if (Transaction.getSavedSpecialPlate() == false
			|| UtilityMethods.isSpecialPlates(csTransCd)
			|| cbInitRegRcdInMFDown)
		{
			wsdchkSame.setEnabled(false);
		}
		else
		{
			//get the saved special plate if available
			try
			{
				CommonClientBusiness laCommonClientBusiness =
					new CommonClientBusiness();
				Object laSpclPlt =
					laCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.GET_SAVED_SPCL_PLT,
						null);

				if (laSpclPlt != null
					&& laSpclPlt instanceof SpecialPlatesRegisData)
				{
					caSpclPltRegisData =
						(SpecialPlatesRegisData) laSpclPlt;
					wsdchkSame.setEnabled(true);
				}
			}
			catch (RTSException aeRTSException)
			{
				aeRTSException.displayError(this);
			}
		}
	}
	
	/**
	 * 
	 * Set value of InitRegRecInMFDown
	 * 
	 */
	public void setInitRegRcdInMFDown()
	{
		cbInitRegRcdInMFDown =
			UtilityMethods.getEventType(csTransCd).equals(
				TransCdConstant.REG_EVENT_TYPE)
				&& caVehInqData != null
				&& caVehInqData.getNoMFRecs() == 0;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"