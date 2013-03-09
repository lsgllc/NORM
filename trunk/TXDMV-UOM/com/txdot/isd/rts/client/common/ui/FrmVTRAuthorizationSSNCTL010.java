package com.txdot.isd.rts.client.common.ui;

import java.awt.event.*;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.LienholderData;
import com.txdot.isd.rts.services.data.VehMiscData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmVTRAuthorizationSSNCTL010.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()         
 * B Hargrove	04/25/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD (Java 1.4)
 *							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3  
 * T Pederson	08/09/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	09/28/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/25/2009	Refactored LienholdersData to LienholderData
 * 							delete getBuilderData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/06/2009	modify input to AlphaNumericOnly
 * 							modify gettxtAuthorizationCode() 
 * 							defect 10127 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/**
 * Screen for VTR Authorization SSNCTL010
 *
 * @version	Defect_POS_F	08/06/2009
 * @author	Nancy Ting
 * <br>Creation Date:		06/26/2001 09:32:53
 */

public class FrmVTRAuthorizationSSNCTL010
	extends RTSDialogBox
	implements ActionListener, FocusListener, KeyListener
{
	private JLabel ivjstcLblAuthorizationCode = null;
	private JPanel ivjFrmVTRAuthorizationSSNCTL010ContentPane1 = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnEnter = null;
	private RTSInputField ivjtxtAuthorizationCode = null;
	private RTSTextArea ivjtaMessage = null;

	// Objects
	private LienholderData caLienData = null;
	private VehicleInquiryData caVehicleInquiryData = null;
	private VehMiscData caVehMiscData = null;

	// Text Constants 
	private final static String FRM_NAME_SSNCTL010 =
		"FrmVTRAuthorizationSSNCTL010";
	private final static String FRM_TITLE_SSNCTL010 =
		"VTR Authorization (SSN)      CTL010";
	private final static String TXT_ENT_AUTH_CD =
		"Enter authorization code:";
	private final static String TXT_SSN_MSG =
		"A Name Field appears to contain a Social"
			+ " Security Number.  \"Cancel\" and correct.  "
			+ "If the numbers do not represent a Social "
			+ "Security Number, enter a VTR Authorization "
			+ "Code to proceed.";
	private final static String TXT_CANCEL = "Cancel";
	private final static String TXT_ENTER = "Enter";

	/**
	 * FrmVTRAuthorizationSSNCTL010 constructor.
	 */
	public FrmVTRAuthorizationSSNCTL010()
	{
		super();
		initialize();
	}

	/**
	 * FrmVTRAuthorizationSSNCTL010 constructor.
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmVTRAuthorizationSSNCTL010(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmVTRAuthorizationSSNCTL010 constructor.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmVTRAuthorizationSSNCTL010(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel is pressed
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
			clearAllColor(this);
			Object laObjectSource = aaAE.getSource();

			if (laObjectSource == getbtnEnter())
			{
				try
				{
					FrmVTRAuthorizationCTL003.verifyInput(
						gettxtAuthorizationCode().getText());
				}
				catch (RTSException aeRTSEx)
				{
					RTSException leEx = new RTSException();
					leEx.addException(
						aeRTSEx,
						gettxtAuthorizationCode());
					gettxtAuthorizationCode().setText(
						gettxtAuthorizationCode()
							.getText()
							.toUpperCase());
					leEx.displayError(this);
					leEx.getFirstComponent().requestFocus();
					return;
				}
				if (caVehicleInquiryData != null)
				{

					caVehMiscData.setAuthCd(
						gettxtAuthorizationCode()
							.getText()
							.toUpperCase());
				}
				else if (caLienData != null)
				{
					caLienData.setVTRAuth(true);
				}

				if (caVehicleInquiryData != null)
				{
					getController().processData(
						AbstractViewController.ENTER,
						caVehicleInquiryData);
				}
				else if (caLienData != null)
				{
					getController().processData(
						AbstractViewController.ENTER,
						caLienData);
				}
			}
			else if (laObjectSource == getbtnCancel())
			{
				if (caVehicleInquiryData != null)
				{
					caVehMiscData.setAuthCd(
						CommonConstant.STR_SPACE_EMPTY);
					getController().processData(
						AbstractViewController.CANCEL,
						caVehicleInquiryData);
				}
				else if (caLienData != null)
				{
					getController().processData(
						AbstractViewController.CANCEL,
						caLienData);
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
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		if (aaFE.getSource() == gettaMessage())
		{
			gettaMessage().transferFocus();
		}
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		//empty code block
	}

	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(TXT_CANCEL);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				ivjbtnCancel.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the btnEnter property value.
	 * 
	 * @returnRTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnEnter()
	{
		if (ivjbtnEnter == null)
		{
			try
			{
				ivjbtnEnter = new RTSButton();
				ivjbtnEnter.setName("btnEnter");
				ivjbtnEnter.setText(TXT_ENTER);
				// user code begin {1}
				ivjbtnEnter.addKeyListener(this);
				ivjbtnEnter.addActionListener(this);
				getRootPane().setDefaultButton(ivjbtnEnter);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnEnter;
	}

	/**
	 * Return FrmVTRAuthorizationSSNCTL010ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmVTRAuthorizationSSNCTL010ContentPane1()
	{
		if (ivjFrmVTRAuthorizationSSNCTL010ContentPane1 == null)
		{
			try
			{
				ivjFrmVTRAuthorizationSSNCTL010ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmVTRAuthorizationSSNCTL010ContentPane1.setName(
					"FrmVTRAuthorizationSSNCTL010ContentPane1");
				ivjFrmVTRAuthorizationSSNCTL010ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmVTRAuthorizationSSNCTL010ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmVTRAuthorizationSSNCTL010ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(487, 200));

				java
					.awt
					.GridBagConstraints constraintsstcLblAuthorizationCode =
					new java.awt.GridBagConstraints();
				constraintsstcLblAuthorizationCode.gridx = 1;
				constraintsstcLblAuthorizationCode.gridy = 2;
				constraintsstcLblAuthorizationCode.ipadx = 6;
				constraintsstcLblAuthorizationCode.insets =
					new java.awt.Insets(6, 13, 22, 6);
				getFrmVTRAuthorizationSSNCTL010ContentPane1().add(
					getstcLblAuthorizationCode(),
					constraintsstcLblAuthorizationCode);

				java
					.awt
					.GridBagConstraints constraintstxtAuthorizationCode =
					new java.awt.GridBagConstraints();
				constraintstxtAuthorizationCode.gridx = 2;
				constraintstxtAuthorizationCode.gridy = 2;
				constraintstxtAuthorizationCode.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtAuthorizationCode.weightx = 1.0;
				constraintstxtAuthorizationCode.ipadx = 149;
				constraintstxtAuthorizationCode.insets =
					new java.awt.Insets(3, 7, 19, 15);
				getFrmVTRAuthorizationSSNCTL010ContentPane1().add(
					gettxtAuthorizationCode(),
					constraintstxtAuthorizationCode);

				java.awt.GridBagConstraints constraintstaMessage =
					new java.awt.GridBagConstraints();
				constraintstaMessage.gridx = 1;
				constraintstaMessage.gridy = 1;
				constraintstaMessage.gridwidth = 2;
				constraintstaMessage.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintstaMessage.weightx = 1.0;
				constraintstaMessage.weighty = 1.0;
				constraintstaMessage.ipady = 38;
				constraintstaMessage.insets =
					new java.awt.Insets(9, 19, 2, 20);
				getFrmVTRAuthorizationSSNCTL010ContentPane1().add(
					gettaMessage(),
					constraintstaMessage);

				java.awt.GridBagConstraints constraintsbtnEnter =
					new java.awt.GridBagConstraints();
				constraintsbtnEnter.gridx = 1;
				constraintsbtnEnter.gridy = 3;
				constraintsbtnEnter.ipadx = 36;
				constraintsbtnEnter.insets =
					new java.awt.Insets(20, 46, 43, 19);
				getFrmVTRAuthorizationSSNCTL010ContentPane1().add(
					getbtnEnter(),
					constraintsbtnEnter);

				java.awt.GridBagConstraints constraintsbtnCancel =
					new java.awt.GridBagConstraints();
				constraintsbtnCancel.gridx = 2;
				constraintsbtnCancel.gridy = 3;
				constraintsbtnCancel.ipadx = 28;
				constraintsbtnCancel.insets =
					new java.awt.Insets(20, 27, 43, 47);
				getFrmVTRAuthorizationSSNCTL010ContentPane1().add(
					getbtnCancel(),
					constraintsbtnCancel);
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
		return ivjFrmVTRAuthorizationSSNCTL010ContentPane1;
	}

	/**
	 * Return the stcLblAuthorizationCode property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblAuthorizationCode()
	{
		if (ivjstcLblAuthorizationCode == null)
		{
			try
			{
				ivjstcLblAuthorizationCode = new javax.swing.JLabel();
				ivjstcLblAuthorizationCode.setName(
					"stcLblAuthorizationCode");
				ivjstcLblAuthorizationCode.setText(TXT_ENT_AUTH_CD);
				ivjstcLblAuthorizationCode.setMaximumSize(
					new java.awt.Dimension(141, 14));
				ivjstcLblAuthorizationCode.setMinimumSize(
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
		return ivjstcLblAuthorizationCode;
	}

	/**
	 * Return the taMessage property value.
	 * 
	 * @return RTSTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTextArea gettaMessage()
	{
		if (ivjtaMessage == null)
		{
			try
			{
				ivjtaMessage = new RTSTextArea();
				ivjtaMessage.setName("taMessage");
				ivjtaMessage.setLineWrap(true);
				ivjtaMessage.setWrapStyleWord(true);
				ivjtaMessage.setText(TXT_SSN_MSG);
				ivjtaMessage.setBackground(
					new java.awt.Color(204, 204, 204));
				ivjtaMessage.setForeground(java.awt.Color.black);
				ivjtaMessage.setFont(new java.awt.Font("Arial", 1, 14));
				ivjtaMessage.setEditable(false);
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
		return ivjtaMessage;
	}

	/**
	 * Return the txtAuthorizationCode property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtAuthorizationCode()
	{
		if (ivjtxtAuthorizationCode == null)
		{
			try
			{
				ivjtxtAuthorizationCode = new RTSInputField();
				ivjtxtAuthorizationCode.setName("txtAuthorizationCode");
				// defect 10127 
				ivjtxtAuthorizationCode.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				// end defect 10127 
				ivjtxtAuthorizationCode.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtAuthorizationCode.setMaxLength(15);
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
		return ivjtxtAuthorizationCode;
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
			setName(FRM_NAME_SSNCTL010);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(341, 259);
			setTitle(FRM_TITLE_SSNCTL010);
			setContentPane(
				getFrmVTRAuthorizationSSNCTL010ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Handles the navigation between the buttons of the ButtonPanel
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
			|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (getbtnEnter().hasFocus())
			{
				getbtnCancel().requestFocus();
			}
			else if (getbtnCancel().hasFocus())
			{
				getbtnEnter().requestFocus();
			}
		}
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
			FrmVTRAuthorizationSSNCTL010 laFrmVTRAuthorizationSSNCTL010;
			laFrmVTRAuthorizationSSNCTL010 =
				new FrmVTRAuthorizationSSNCTL010();
			laFrmVTRAuthorizationSSNCTL010.setModal(true);
			laFrmVTRAuthorizationSSNCTL010
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmVTRAuthorizationSSNCTL010.show();
			java.awt.Insets laInsets =
				laFrmVTRAuthorizationSSNCTL010.getInsets();
			laFrmVTRAuthorizationSSNCTL010.setSize(
				laFrmVTRAuthorizationSSNCTL010.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmVTRAuthorizationSSNCTL010.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmVTRAuthorizationSSNCTL010.setVisibleRTS(true);
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
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		//Case of REG
		if (aaDataObject instanceof VehicleInquiryData)
		{
			caVehicleInquiryData = (VehicleInquiryData) aaDataObject;
			caVehMiscData =
				(VehMiscData) caVehicleInquiryData.getVehMiscData();
		}
		else if (
			aaDataObject
				instanceof com.txdot.isd.rts.services.data.LienholderData)
		{
			caLienData = (LienholderData) aaDataObject;
		}
	}
}
