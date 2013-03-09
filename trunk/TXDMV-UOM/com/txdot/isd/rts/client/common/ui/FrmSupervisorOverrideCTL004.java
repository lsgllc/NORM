package com.txdot.isd.rts.client.common.ui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.MiscellaneousCache;
import com.txdot.isd.rts.services.data.VehMiscData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSupervisorOverrideCTL004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking()  
 * Min Wang	   	04/28/2003	Modified actionPerformed(). 
 *							defect 5897
 * B Arredondo	02/20/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * K Harrell	10/27/2004	Disable taReason
 *							set to non-editable in Visual Composition
 *							defect 7508 Ver 5.2.2
 * B Hargrove	03/15/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * B Hargrove	04/25/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7885 Ver 5.2.3 
 * S Johnston	06/20/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3
 * T Pederson	08/30/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	09/10/2008	Enlarge text area (height & width) for up 
 * 							to 3 reasons, longer reason desc. 
 * 							defect 7860 Ver Defect_POS_B  
 * K Harrell	06/27/2010	Prefill Override Code in development
 * 							add setOverrideCdInDev()
 * 							modify setData() 
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */
/**
 * This form is used to override transaction. 
 *
 * @version 6.5.0			06/27/2010 
 * @author	Administrator
 * <br>Creation Date: 		07/13/2001 09:50:41 
 */

public class FrmSupervisorOverrideCTL004
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJDialogBoxContentPane = null;
	private JLabel ivjstcLblEnterSupervisor = null;
	private JLabel ivjstcLblReason = null;
	private RTSPasswordField ivjPasswordField1 = null;
	private RTSTextArea ivjtaReason = null;

	//Objects
	private VehicleInquiryData caVehicleInquiryData = null;
	private VehMiscData caVehMiscData = null;

	// Text Constants 
	private final static String FRM_NAME_CTL004 =
		"FrmSuperVisorOverrideCTL004";
	private final static String FRM_TITLE_CTL004 =
		"Supervisor Override   CTL004";
	private final static String TXT_ENT_SUP_CD =
		"Enter supervisor code:";
	private final static String TXT_RSN = "Reason(s):";

	/**
	 * FrmSuperVisorOverrideCTL004 constructor
	 */
	public FrmSupervisorOverrideCTL004()
	{
		super();
		initialize();
	}

	/**
	 * FrmSuperVisorOverrideCTL004 constructor
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSupervisorOverrideCTL004(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSuperVisorOverrideCTL004 constructor
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSupervisorOverrideCTL004(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when an action occurs
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking() || !isVisible())
		{
			return;
		}

		try
		{
			clearAllColor(this);
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				handleEnter();
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				caVehMiscData.setSupvOvride(null);
				getController().processData(
					AbstractViewController.CANCEL,
					null);

			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.CTL004);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Display Error
	 * 
	 * @param RTSException leRTSEx
	 */
	public void displayError(RTSException aeRTSEx)
	{
		RTSException leEx = new RTSException();
		leEx.addException(aeRTSEx, getJPasswordField1());
		leEx.displayError(this);
		leEx.getFirstComponent().requestFocus();
	}

	/**
	 * Return the ButtonPanel1 property value
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
				ivjButtonPanel1.setBounds(44, 125, 234, 40);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the JDialogBoxContentPane property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJDialogBoxContentPane()
	{
		if (ivjJDialogBoxContentPane == null)
		{
			try
			{
				ivjJDialogBoxContentPane = new JPanel();
				ivjJDialogBoxContentPane.setName(
					"JDialogBoxContentPane");
				ivjJDialogBoxContentPane.setLayout(null);
				getJDialogBoxContentPane().add(
					getstcLblEnterSupervisor(),
					getstcLblEnterSupervisor().getName());
				getJDialogBoxContentPane().add(
					getJPasswordField1(),
					getJPasswordField1().getName());
				getJDialogBoxContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getJDialogBoxContentPane().add(
					getstcLblReason(),
					getstcLblReason().getName());
				getJDialogBoxContentPane().add(
					gettaReason(),
					gettaReason().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJDialogBoxContentPane;
	}

	/**
	 * Return the JPasswordField1 property value
	 * 
	 * @return RTSPasswordField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSPasswordField getJPasswordField1()
	{
		if (ivjPasswordField1 == null)
		{
			try
			{
				ivjPasswordField1 = new RTSPasswordField();
				ivjPasswordField1.setName("JPasswordField1");
				ivjPasswordField1.setBounds(191, 91, 71, 20);
				ivjPasswordField1.setMaxLength(6);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjPasswordField1;
	}

	/**
	 * Return the stcLblEnterSupervisor property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEnterSupervisor()
	{
		if (ivjstcLblEnterSupervisor == null)
		{
			try
			{
				ivjstcLblEnterSupervisor = new JLabel();
				ivjstcLblEnterSupervisor.setSize(130, 20);
				ivjstcLblEnterSupervisor.setName(
					"stcLblEnterSupervisor");
				ivjstcLblEnterSupervisor.setText(TXT_ENT_SUP_CD);
				ivjstcLblEnterSupervisor.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjstcLblEnterSupervisor.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				ivjstcLblEnterSupervisor.setLocation(44, 91);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblEnterSupervisor;
	}

	/**
	 * Return the stcLblReason property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblReason()
	{
		if (ivjstcLblReason == null)
		{
			try
			{
				ivjstcLblReason = new JLabel();
				ivjstcLblReason.setSize(81, 20);
				ivjstcLblReason.setName("stcLblReason");
				ivjstcLblReason.setText(TXT_RSN);
				ivjstcLblReason.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjstcLblReason.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				ivjstcLblReason.setLocation(41, 9);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblReason;
	}

	/**
	 * Return the taReason property value
	 * 
	 * @return RTSTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTextArea gettaReason()
	{
		if (ivjtaReason == null)
		{
			try
			{
				ivjtaReason = new RTSTextArea();
				ivjtaReason.setName("taReason");
				ivjtaReason.setText("");
				ivjtaReason.setBackground(new Color(204, 204, 204));
				ivjtaReason.setForeground(java.awt.Color.black);
				ivjtaReason.setBounds(50, 30, 263, 53);
				ivjtaReason.setEditable(false);
				ivjtaReason.setEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtaReason;
	}

	/**
	 * handleEnter
	 */
	private void handleEnter()
	{
		char[] lcPassword = getJPasswordField1().getPassword();
		String lsPassword = new String(lcPassword);
		if (lsPassword.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			displayError(new RTSException(150));
			return;
		}
		else
		{
			caVehMiscData.setSupvOvride(lsPassword);
			getController().processData(
				AbstractViewController.ENTER,
				caVehicleInquiryData);
		}
	}

	/**
	 * Called whenever the part throws an exception
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7885
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7885
	}

	/**
	 * Initialize the class
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(FRM_NAME_CTL004);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(329, 199);
			setModal(true);
			setTitle(FRM_TITLE_CTL004);
			setContentPane(getJDialogBoxContentPane());
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		// empty code block
		// user code end
	}

	/**
	 * Process Key release events
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			caVehMiscData.setSupvOvride(null);
		}
		super.keyReleased(aaKE);
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
			FrmSupervisorOverrideCTL004 laFrmSupervisorOverrideCTL004;
			laFrmSupervisorOverrideCTL004 =
				new FrmSupervisorOverrideCTL004();
			laFrmSupervisorOverrideCTL004.setModal(true);
			laFrmSupervisorOverrideCTL004
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSupervisorOverrideCTL004.show();
			Insets laInsets = laFrmSupervisorOverrideCTL004.getInsets();
			laFrmSupervisorOverrideCTL004.setSize(
				laFrmSupervisorOverrideCTL004.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmSupervisorOverrideCTL004.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmSupervisorOverrideCTL004.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
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
		//Case of REG
		if (aaDataObject instanceof VehicleInquiryData)
		{
			caVehicleInquiryData = (VehicleInquiryData) aaDataObject;
			caVehMiscData =
				(VehMiscData) caVehicleInquiryData.getVehMiscData();
			String lsReason = caVehMiscData.getSupvOvrideReason();
			if (lsReason != null && lsReason.trim().length() > 0)
			{
				gettaReason().setText(lsReason);
			}
			else
			{
				getstcLblReason().setVisible(false);
			}
			// defect 10491 
			setOverrideCdInDev();
			// end defect 10491  
		}
	}
	/**
	 * Set Override Code in Development
	 *
	 */
	private void setOverrideCdInDev()
	{
		if (SystemProperty.isDevStatus())
		{
			String lsOverrideCd =
				MiscellaneousCache.getOverrideCd(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId());
			getJPasswordField1().setText(lsOverrideCd);
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
