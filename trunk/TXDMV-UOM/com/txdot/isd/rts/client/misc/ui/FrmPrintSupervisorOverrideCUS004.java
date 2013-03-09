package com.txdot.isd.rts.client.misc.ui;

import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSPasswordField;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmPrintSupervisorOverrideCUS004.java
 *
 * (c) Texas Department of Transportation 2002
 * 
 * ---------------------------------------------------------------------
 *  Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							New class. Ver 5.2.0
 * J Zwiener	06/22/2005	Java 1.4 changes
 * 							Format source, organize imports, rename
 * 							fields, comment out methods.
 * 							defect 7892 Ver 5.2.3	
 * K Harrell	09/16/2008	Modify screen via Visual Editor to 
 * 							present reason for Override requirement.
 * 							Additional class cleanup. 
 * 							add ivjstclblReason, ivjlblReasonMsg, 
 * 							  get/set methods 
 * 							delete getBuilderData()
 * 							modify setData(),getRTSDialogBoxContentPane()
 * 							defect 7283 Ver Defect_POS_B 
 * ---------------------------------------------------------------------
 */

/**
 * Frame for PrintSupervisorOverrideCUS004
 * 
 * @version	Defect_POS_B	09/16/2008 
 * @author	Michael Abernethy
 * <br>Creation Date:		09/20/2002 
 */
public class FrmPrintSupervisorOverrideCUS004
	extends RTSDialogBox
	implements ActionListener
{
	private static final String TXT_SPRVSR_CD =
		"Enter supervisor code:";
	private static final String CTL004_FRM_TITLE =
		"Supervisor Override   CTL004";

	private ButtonPanel ivjbuttonPanel = null;
	private JLabel ivjstcLblOverride = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private RTSPasswordField ivjtxtPassword = null;

	// defect 7283 
	private JLabel ivjstclblReason = null;
	private JLabel ivjlblReasonMsg = null;
	// end defect 7283 

	/**
	 * FrmPrintSupervisorOverrideCUS004 constructor comment.
	 */
	public FrmPrintSupervisorOverrideCUS004()
	{
		super();
		initialize();
	}
	/**
	 * FrmPrintSupervisorOverrideCUS004 constructor comment.
	 * 
	 * @param owner java.awt.Dialog
	 */
	public FrmPrintSupervisorOverrideCUS004(java.awt.Dialog owner)
	{
		super(owner);
		initialize();
	}
	/**
	 * FrmPrintSupervisorOverrideCUS004 constructor comment.
	 * 
	 * @param owner java.awt.Frame
	 */
	public FrmPrintSupervisorOverrideCUS004(java.awt.Frame owner)
	{
		super(owner);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param e ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (e.getSource() == getbuttonPanel().getBtnEnter())
			{
				try
				{
					String lsPassword =
						new String(gettxtPassword().getPassword());
					Boolean lbOverRideNeeded =
						(Boolean) getController().directCall(
							MiscellaneousConstant.OVERRIDE_NEEDED,
							lsPassword);
					getController().processData(
						AbstractViewController.ENTER,
						lbOverRideNeeded);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(this);
				}
			}
			else if (e.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * @return com.txdot.isd.rts.client.general.ui.ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setBounds(21, 106, 264, 41);
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
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new javax.swing.JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				getRTSDialogBoxContentPane().add(
					gettxtPassword(),
					gettxtPassword().getName());
				getRTSDialogBoxContentPane().add(
					getbuttonPanel(),
					getbuttonPanel().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblOverride(),
					getstcLblOverride().getName());
				// user code begin {1}
				// user code end
				// defect 7283 
				ivjRTSDialogBoxContentPane.add(getstclblReason(), null);
				ivjRTSDialogBoxContentPane.add(getlblReasonMsg(), null);
				// end defect 7283 
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}
	/**
	 * Return the stcLblOverride property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblOverride()
	{
		if (ivjstcLblOverride == null)
		{
			try
			{
				ivjstcLblOverride = new javax.swing.JLabel();
				ivjstcLblOverride.setSize(129, 20);
				ivjstcLblOverride.setName("stcLblOverride");
				ivjstcLblOverride.setText(TXT_SPRVSR_CD);
				ivjstcLblOverride.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				ivjstcLblOverride.setLocation(32, 77);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblOverride;
	}
	/**
	 * Return the txtPassword property value.
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.RTSPasswordField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSPasswordField gettxtPassword()
	{
		if (ivjtxtPassword == null)
		{
			try
			{
				ivjtxtPassword = new RTSPasswordField();
				ivjtxtPassword.setSize(77, 20);
				ivjtxtPassword.setName("txtPassword");
				ivjtxtPassword.setMaxLength(6);
				ivjtxtPassword.setLocation(177, 77);
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
		return ivjtxtPassword;
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
			setName("FrmPrintSupervisorOverrideCUS004");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(314, 176);
			setTitle(CTL004_FRM_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
			// user code begin {1}
			// user code end
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint -starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPrintSupervisorOverrideCUS004 laFrmPrintSupervisorOverrideCUS004;
			laFrmPrintSupervisorOverrideCUS004 =
				new FrmPrintSupervisorOverrideCUS004();
			laFrmPrintSupervisorOverrideCUS004.setModal(true);
			laFrmPrintSupervisorOverrideCUS004
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmPrintSupervisorOverrideCUS004.show();
			java.awt.Insets insets =
				laFrmPrintSupervisorOverrideCUS004.getInsets();
			laFrmPrintSupervisorOverrideCUS004.setSize(
				laFrmPrintSupervisorOverrideCUS004.getWidth()
					+ insets.left
					+ insets.right,
				laFrmPrintSupervisorOverrideCUS004.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmPrintSupervisorOverrideCUS004.setVisible(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 7283
		if (aaDataObject instanceof String)
		{
			getlblReasonMsg().setText((String) aaDataObject);
		}
		// end defect 7283 
	}

	/**
	 * This method initializes ivjstclblReason
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblReason()
	{
		if (ivjstclblReason == null)
		{
			ivjstclblReason = new javax.swing.JLabel();
			ivjstclblReason.setSize(114, 20);
			ivjstclblReason.setText("Reason:");
			ivjstclblReason.setLocation(32, 17);
		}
		return ivjstclblReason;
	}
	/**
	 * This method initializes ivjlblReasonMsg
	 * 
	 * @return JLabel
	 */
	private javax.swing.JLabel getlblReasonMsg()
	{
		if (ivjlblReasonMsg == null)
		{
			ivjlblReasonMsg = new javax.swing.JLabel();
			ivjlblReasonMsg.setSize(263, 20);
			ivjlblReasonMsg.setText("");
			ivjlblReasonMsg.setFont(
				new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			ivjlblReasonMsg.setLocation(42, 39);
		}
		return ivjlblReasonMsg;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
