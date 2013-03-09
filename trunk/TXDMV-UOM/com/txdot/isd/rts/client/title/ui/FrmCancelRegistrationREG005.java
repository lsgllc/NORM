package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * 
 * FrmCancelRegistrationREG005.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validations update
 * J Rue		02/16/2005	VAJ to WASD cleanup
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/08/2005	Change AbstractViewController to
 * 							getController()
 * 							deprecated setController()
 * 							modify actionPerformed()
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/16/2005	Add constant ERR_MSG_MAIN_JDIALOG
 * 							REG005_FRAME__NAME, REG005_FRAME__TITLE
 * 							defect 7898 Ver 5.2.3
 * T Pederson	10/31/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							defect 7898 Ver 5.2.3
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * B Hargrove	04/02/2008	Display error msg 150 if user does not 
 * 							select 'Cancel Registration'. Re-flow to 
 * 							move setDataToVehObj() processing back into
 * 							actionPerformed().
 * 							Removed getBuilderData()
 * 							modify actionPerformed()
 * 							deprecate setDataToVehObj() 
 * 							defect 9144 Ver Defect POS A
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to capture the cancel registration indicator 
 * in status change.
 *
 * @version	POS A 			04/02/2008
 * @author	T Pederson
 * <br>Creation Date:		12/09/2001 12:18:56
 */

public class FrmCancelRegistrationREG005
	extends RTSDialogBox
	implements ActionListener, FocusListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkCancelReg = null;
	private JPanel ivjFrmCancelRegistrationREG005ContentPane1 = null;
	private VehicleInquiryData caVehInqData = null;
	
	// defect 7898
	//private int ciSelectedBtn = 0;
	private final static String CANCEL_REGISTRATION = 
		"Cancel Registration";
	private final static String CANCEL = "Cancel";
	// end defect 7898
	
	/**
	 * FrmStolenSRSTTL037 constructor comment.
	 */
	public FrmCancelRegistrationREG005()
	{
		super();
		initialize();
	}
	/**
	 * FrmStolenSRSTTL037 constructor comment.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmCancelRegistrationREG005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Set Controller.
	 * 
	 * @param aaAVC AbstractViewController
	 * @deprecated
	 */
	public void setController(AbstractViewController aaAVC)
	{
		super.setController(aaAVC);
	}
	/**
	 * FrmStolenSRSTTL037 constructor comment.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmCancelRegistrationREG005(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		//Code to avoid clicking on the button multiple times
		if (!startWorking())
		{
			return;
		}

		try
		{
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 9144
				// User must check 'Cancel Registration'.
				// setDataToVehObj() function is now done here.
				//Set data to the data object
				//setDataToVehObj();
				if (getchkCancelReg().isSelected())
				{
					RegistrationData laRegData =
						caVehInqData.getMfVehicleData().getRegData();
					if (laRegData != null)
					{
						laRegData.setRegInvldIndi(1);
					}
					// defect 7898
					//	Change controller to getController()
					getController().processData(
						AbstractViewController.ENTER,
						getController().getData());
				}
				else
				{
					RTSException leRTSEx150 = new RTSException();
					leRTSEx150.addException(
						new RTSException(150),
						getchkCancelReg());
					leRTSEx150.getFirstComponent().requestFocus();
					leRTSEx150.displayError(this);
				}
				// end defect 9144
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
				// end defect 7898
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.REG005);
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
		// empty code block
	}
	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE java.awt.event.FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// empty code block
	}
	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(197, 25));
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
	 * Return the chkStolen property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkCancelReg()
	{
		if (ivjchkCancelReg == null)
		{
			try
			{
				ivjchkCancelReg = new javax.swing.JCheckBox();
				ivjchkCancelReg.setName("chkCancelReg");
				ivjchkCancelReg.setMnemonic('a');
				ivjchkCancelReg.setText(CANCEL_REGISTRATION);
				ivjchkCancelReg.setActionCommand(CANCEL);
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
		return ivjchkCancelReg;
	}
	/**
	 * Return the FrmStolenSRSTTL037ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmCancelRegistrationREG005ContentPane1()
	{
		if (ivjFrmCancelRegistrationREG005ContentPane1 == null)
		{
			try
			{
				ivjFrmCancelRegistrationREG005ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmCancelRegistrationREG005ContentPane1.setName(
					"FrmCancelRegistrationREG005ContentPane1");
				ivjFrmCancelRegistrationREG005ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmCancelRegistrationREG005ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmCancelRegistrationREG005ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(872, 266));

				java.awt.GridBagConstraints constraintschkCancelReg =
					new java.awt.GridBagConstraints();
				constraintschkCancelReg.gridx = 1;
				constraintschkCancelReg.gridy = 1;
				constraintschkCancelReg.ipadx = 38;
				constraintschkCancelReg.insets =
					new java.awt.Insets(58, 122, 19, 130);
				getFrmCancelRegistrationREG005ContentPane1().add(
					getchkCancelReg(),
					constraintschkCancelReg);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 2;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 23;
				constraintsButtonPanel1.ipady = 38;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(20, 100, 12, 106);
				getFrmCancelRegistrationREG005ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmCancelRegistrationREG005ContentPane1;
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
			setName(ScreenConstant.REG005_FRAME__NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(426, 194);
			setTitle(ScreenConstant.REG005_FRAME_TITLE);
			setContentPane(
				getFrmCancelRegistrationREG005ContentPane1());
		}
		catch (java.lang.Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		getchkCancelReg().requestFocus();
		// user code end
	}
// defect 7898
// This method is not longer used due to 8240
//	/**
//	 * Initialize the class.
//	 * 
//	 * @param aaKE java.awt.event.KeyEvent
//	 */
//	/* WARNING: THIS METHOD WILL BE REGENERATED. */
//	public void keyPressed(java.awt.event.KeyEvent aaKE)
//	{
//		if (aaKE.getSource() instanceof JButton)
//		{
//
//			if (aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
//			{
//				if (ciSelectedBtn == 0)
//				{
//					ciSelectedBtn = 2;
//				}
//				else
//				{
//					ciSelectedBtn--;
//				}
//
//			}
//			else if (aaKE.getKeyCode() == KeyEvent.VK_DOWN
//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
//			{
//				if (ciSelectedBtn == 2)
//				{
//					ciSelectedBtn = 0;
//				}
//				else
//				{
//					ciSelectedBtn++;
//				}
//
//			}
//
//			if (ciSelectedBtn == 0)
//			{
//				getButtonPanel1().getBtnEnter().requestFocus();
//			}
//			else if (ciSelectedBtn == 1)
//			{
//				getButtonPanel1().getBtnCancel().requestFocus();
//			}
//			else
//			{
//				getButtonPanel1().getBtnHelp().requestFocus();
//			}
//
//		}
//
//		super.keyPressed(aaKE);
//	}
// end defect 7898
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
			FrmStolenSRSTTL037 laFrmStolenSRSTTL037;
			laFrmStolenSRSTTL037 = new FrmStolenSRSTTL037();
			laFrmStolenSRSTTL037.setModal(true);
			laFrmStolenSRSTTL037
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmStolenSRSTTL037.show();
			java.awt.Insets insets = laFrmStolenSRSTTL037.getInsets();
			laFrmStolenSRSTTL037.setSize(
				laFrmStolenSRSTTL037.getWidth()
					+ insets.left
					+ insets.right,
				laFrmStolenSRSTTL037.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmStolenSRSTTL037.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
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
			caVehInqData = (VehicleInquiryData) aaDataObject;
			RegistrationData laRegData =
				caVehInqData.getMfVehicleData().getRegData();

			if (laRegData.getRegInvldIndi() == 1)
			{
				getchkCancelReg().setSelected(true);
			}
		}
	}
	/**
	 * Set RegInvInvldIndi to local variable.
	 * @deprecated
	 * 
	 */
	private void setDataToVehObj()
	{

		// defect 7898
		//	Review the use of setting local variable.
		//	This may not be used.
		
		RegistrationData laRegData =
			caVehInqData.getMfVehicleData().getRegData();

		if (laRegData != null)
		{
			if (getchkCancelReg().isSelected())
			{
				laRegData.setRegInvldIndi(1);
			}
			else
			{
				laRegData.setRegInvldIndi(0);
			}
		}
	}
}
