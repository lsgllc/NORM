package com.txdot.isd.rts.client.registration.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.client.registration.business.RegistrationClientBusiness;
import com.txdot.isd.rts.client.title.ui.TitleClientUtilityMethods;
import com.txdot.isd.rts.services.data.RegistrationValidationData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * FrmVehicleWeightREG010.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking(),
 * 							doneWorking()
 * N Ting		06/13/2002	Fix CQU100004278
 * 							change handleValidateWeight()
 * B Hargrove	03/10/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7894 Ver 5.2.3
 * B Hargrove	04/28/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	05/13/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	06/13/2005	Clean-up User Guide bookmarks to get in 
 * 							synch with what is in User Guide Word\html.
 * 							Remove 'REG010C' because there is no weight
 * 							option for Reg Correction.
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	06/21/2005	Remove unused method.
 *  	 					modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete getRegValidData()
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	07/19/2005	Refactor\Move
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	11/07/2011	add ciVehEmptyWt, ciRegClassCd
 * 							delete cbCarryngCapValidated, 
 * 							  cbValidateWeightCalled, get/set methods,  
 * 							delete getBuilderData(), getVehInqData(),
 * 							 setRegValidData(), setSaveCarryingCap(),  
 * 							 setVehInqData() 
 * 							modify actionPerformed(), setData(),
 * 							 handleValidateWeight(), focusLost() 
 * 							defect 10959 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**   
  * This form is used within Registration to specify a change in 
  * carrying capacity.
 *
 * @version	6.9.0 	11/07/2011
 * @author 	Joseph Kwik
 * @since 			06/26/2001 12:53:43 
 * 
 */

public class FrmVehicleWeightREG010
	extends RTSDialogBox
	implements ActionListener, FocusListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblCarryingCapacity = null;
	private RTSInputField ivjtxtCarryingCapacity = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjFrmVehicleWeightREG010ContentPane1 = null;
	private VehicleInquiryData caVehInqData = null;
	private RegistrationValidationData caRegValidData = null;
	private int ciSaveCarryingCap;
	
	// defect 10959 
	private int ciVehEmptyWt; 
	private int ciRegClassCd;
	// end defect 10959 
	
	private final static String CARRY_CAP = "Carrying Capacity:";
	private final static String ENTER = "Enter the following:";
	private final static String TITLE_REG010 = 
		"Vehicle Weight   REG010";
	
	/**
	 * FrmVehicleWeightREG010 constructor comment.
	 */
	public FrmVehicleWeightREG010()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmVehicleWeightREG010 constructor with parent.
	 * 
	 * @param aaParent Dialog
	 */
	public FrmVehicleWeightREG010(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmVehicleWeightREG010 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmVehicleWeightREG010(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		// Code to avoid actionPerformed being executed more than once
		// when clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{ 
			clearAllColor(this);
			// defect 10959
			
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (handleValidateWeight())
				{
					caRegValidData.setVehWtStatusOK(true);
					getController().processData(
						AbstractViewController.ENTER,
						caVehInqData);
				}
				// end defect 10959 
			}
			else if (aaAE.getSource() == 
				getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				if (caRegValidData.getRegModify()
				== RegistrationConstant.REG_MODIFY_VOLUNTARY)
				{
					RTSHelp.displayHelp(RTSHelp.REG010A);
				}
				else if (caRegValidData.getRegModify()
				== RegistrationConstant.REG_MODIFY_APPREHENDED)
				{
					RTSHelp.displayHelp(RTSHelp.REG010B);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.REG010);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}
	
	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE java.awt.event.FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		if (!aaFE.isTemporary()
			&& aaFE.getSource() == getButtonPanel1().getBtnEnter())
		{
			// defect 10959 
				handleValidateWeight();
			// end defect 10959 
		}
	}
	
	/**
	 * Invoked when a component loses the keyboard focus.
	 * Validate DealerID and zip code.
	 * 
	 * @param aaFE java.awt.event.FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		//empty code block
	}
	
	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 =
					new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				//ivjButtonPanel1.getBtnEnter().addFocusListener(this);
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjButtonPanel1;
	}
	
	/**
	 * Return the FrmVehicleWeightREG010ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getFrmVehicleWeightREG010ContentPane1()
	{
		if (ivjFrmVehicleWeightREG010ContentPane1 == null)
		{
			try
			{
				ivjFrmVehicleWeightREG010ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmVehicleWeightREG010ContentPane1.setName(
					"FrmVehicleWeightREG010ContentPane1");
				ivjFrmVehicleWeightREG010ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmVehicleWeightREG010ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmVehicleWeightREG010ContentPane1.setMinimumSize(
					new java.awt.Dimension(250, 150));
				ivjFrmVehicleWeightREG010ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				java.awt.GridBagConstraints constraintsJPanel1 =
					new java.awt.GridBagConstraints();
				constraintsJPanel1.gridx = 1;
				constraintsJPanel1.gridy = 1;
				constraintsJPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel1.weightx = 1.0;
				constraintsJPanel1.weighty = 1.0;
				constraintsJPanel1.ipadx = 223;
				constraintsJPanel1.ipady = 53;
				constraintsJPanel1.insets =
					new java.awt.Insets(19, 16, 8, 11);
				getFrmVehicleWeightREG010ContentPane1().add(
					getJPanel1(),
					constraintsJPanel1);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 2;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 23;
				constraintsButtonPanel1.ipady = 22;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(8, 5, 5, 5);
				getFrmVehicleWeightREG010ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmVehicleWeightREG010ContentPane1;
	}
	
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						ENTER));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
				getJPanel1().add(
					getstcLblCarryingCapacity(),
					getstcLblCarryingCapacity().getName());
				getJPanel1().add(
					gettxtCarryingCapacity(),
					gettxtCarryingCapacity().getName());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel1;
	}

	
	/**
	 * Return the stcLblCarryingCapacity property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblCarryingCapacity()
	{
		if (ivjstcLblCarryingCapacity == null)
		{
			try
			{
				ivjstcLblCarryingCapacity = new javax.swing.JLabel();
				ivjstcLblCarryingCapacity.setName(
					"stcLblCarryingCapacity");
				ivjstcLblCarryingCapacity.setText(CARRY_CAP);
				ivjstcLblCarryingCapacity.setMaximumSize(
					new java.awt.Dimension(103, 14));
				ivjstcLblCarryingCapacity.setBounds(23, 33, 125, 14);
				ivjstcLblCarryingCapacity.setMinimumSize(
					new java.awt.Dimension(103, 14));
				// user code begin {1}

				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblCarryingCapacity;
	}
	
	/**
	 * Return the txtCarryingCapacity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCarryingCapacity()
	{
		if (ivjtxtCarryingCapacity == null)
		{
			try
			{
				ivjtxtCarryingCapacity =
					new RTSInputField();
				ivjtxtCarryingCapacity.setName("txtCarryingCapacity");
				ivjtxtCarryingCapacity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCarryingCapacity.setInput(1);
				ivjtxtCarryingCapacity.setBounds(169, 28, 58, 20);
				ivjtxtCarryingCapacity.setMaxLength(6);
				ivjtxtCarryingCapacity.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}

				ivjtxtCarryingCapacity.addActionListener(this);
				ivjtxtCarryingCapacity.addFocusListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtCarryingCapacity;
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
	 * manage weight validation.
	 * 
	 * @return boolean
	 */
	private boolean handleValidateWeight()
	{
		// defect 10959
		boolean lbValid = true; 

		String lsVal = gettxtCarryingCapacity().getText();
		try
		{
			if (lsVal.equals(""))
			{
				lsVal = "0";
			}
			int liCaryngCap = Integer.parseInt(lsVal); 

			caVehInqData
			.getMfVehicleData()
			.getRegData()
			.setVehCaryngCap(liCaryngCap);

			RegistrationClientBusiness laRegClntBusn =
				new RegistrationClientBusiness();
			laRegClntBusn.validateWeight(caVehInqData);

			int liGrossWt = ciVehEmptyWt + liCaryngCap; 
			
			TitleClientUtilityMethods.validateGrossWtForRegClassCd(ciRegClassCd,liGrossWt);
			String lsTransCd = caRegValidData.getTransCode();
			
			if ((lsTransCd.equals(TransCdConstant.CORREG)
					&& caRegValidData.getRegModify()
							== RegistrationConstant.REG_MODIFY_APPREHENDED)
				|| lsTransCd.equals(TransCdConstant.PAWT))
			{
				if (liCaryngCap == ciSaveCarryingCap)
				{
					throw new RTSException(ErrorsConstant.ERR_NUM_CARRYING_CAPACITY_CHG_REQD); 
				}
			}
			// end defect 10959
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(this);
			if (lsVal.equals(""))
			{
				gettxtCarryingCapacity().setText("0");
			}
			gettxtCarryingCapacity().requestFocus();
			gettxtCarryingCapacity().setBackground(
					RTSException.ERR_COLOR);
			lbValid = false;
		}
		return lbValid; 
	}
	
	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmVehicleWeightREG010");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(300, 225);
			setTitle(TITLE_REG010);
			setContentPane(getFrmVehicleWeightREG010ContentPane1());
		}
		catch (java.lang.Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
	}
	
	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmVehicleWeightREG010 laFrmVehicleWeightREG010;
			laFrmVehicleWeightREG010 = new FrmVehicleWeightREG010();
			laFrmVehicleWeightREG010.setModal(true);
			laFrmVehicleWeightREG010
			.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});

			laFrmVehicleWeightREG010.show();
			java.awt.Insets laInsets =
				laFrmVehicleWeightREG010.getInsets();
			laFrmVehicleWeightREG010.setSize(
				laFrmVehicleWeightREG010.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmVehicleWeightREG010.getHeight()
					+ laInsets.top
					+ laInsets.bottom);

			laFrmVehicleWeightREG010.setVisibleRTS(true);

		}
		catch (Throwable aeEx)
		{

			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);

			aeEx.printStackTrace(System.out);

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
		if (aaDataObject != null)
		{
			// defect 10959
			caVehInqData = 
				(VehicleInquiryData) UtilityMethods.copy(aaDataObject);
			caRegValidData = (RegistrationValidationData) caVehInqData
					.getValidationObject();
			caRegValidData.setVehWtStatusOK(false);
			ciSaveCarryingCap = 
				caRegValidData.getOrigVehInqData().getMfVehicleData()
				.getRegData().getVehCaryngCap();
			ciRegClassCd = caVehInqData.getMfVehicleData().getRegData().getRegClassCd(); 
			ciVehEmptyWt = caVehInqData.getMfVehicleData().getVehicleData().getVehEmptyWt();
			int liCarryingCap = caVehInqData.getMfVehicleData().getRegData().getVehCaryngCap();
			gettxtCarryingCapacity().setText(""+liCarryingCap);
			// end defect 10959
		}
	}
}
