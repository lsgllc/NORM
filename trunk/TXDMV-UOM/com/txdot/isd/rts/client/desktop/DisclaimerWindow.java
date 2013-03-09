package com.txdot.isd.rts.client.desktop;

import java.awt.Dialog;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSTextArea;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * DisclaimerWindow.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	1/10/2003	Update text displayed.
 *							CQU100005244
 * Ray Rowehl	12/01/2003	Change to use RTSDialogBox.
 *							add constructors for RTSDialogBox
 *							add setData()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							modify getJTextArea1()
 * 							defect 6445 Ver 5.1.6
 * Min Wang		04/20/2004	Resize the screen for showing the Warning 
 *                          message.
 *                          modify visual composition.
 *                          defect 7010 Ver 5.1.6
 * Ray Rowehl	04/01/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7885 Ver 5.2.3
 * Min Wang		07/11/2005	Display an error message if the java version
 * 							is not correct.  Then exit.
 * 							modify actionPerformed()
 * 							defect 8242 Ver 5.2.3
 * Ray Rowehl	08/22/2005 Constants cleanup 
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	07/13/2009	Reflow so that ciReturnCode is only set to
 * 							Accept when verything else is a go.
 * 							rename aiReturnCode.
 * 							General cleanup.
 * 							add TXT_DISCLAIMER_IMG, DETECTEDJAVAVERSION,
 * 								ciReturnCode
 * 							delete EDTECTEDJAVAVERSION, liReturnCode
 * 							delete getBuilderData()
 * 							modify actionPerformed(), getJLabel1()
 * 							defect 10103 Ver POS_Defect_F
 * B Hargrove	09/17/2009	Use new logos at cutover for new DMV agency.
 * 							add TXT_DMV_DISCLAIMER_IMG, 
 * 							TXT_DMV_DISCLAIMER_MSG
 * 							modify getJLabel1()
 * 							defect 10155 Ver Defect_POS_F
 * Ray Rowehl	10/19/2009	Add logging to see when "I Accept" is 
 * 							pressed.  This was done to help show how 
 * 							big the slow logon problem is.
 * 							modify actionPerformed()
 * 							defect 10255 Ver Defect_G 		 
 * ---------------------------------------------------------------------
 */

/**
 * The Disclaimer Window displaying the Privacy warning to the user.
 *
 * @version	POS_Defect_G	10/19/2009
 * @author	Michael Abernethy
 * <br>Creation date 		10/01/2001 12:41:16 
 */

public class DisclaimerWindow
	extends com.txdot.isd.rts.client.general.ui.RTSDialogBox
	implements java.awt.event.ActionListener
{
	public static final int ACCEPT = 1;
	public static final int DECLINE = 2;
	private static final String DETECTEDJAVAVERSION =
		".  Detected Java Version is ";
	private static final String DISCLAIMER_FRM_TITLE = "Disclaimer";
	private static final String EXPECTEDJAVAVERSION =
		" Expected Java Version is  ";
	private static final String JAVAVERSION = "java.version";
	private static final String JAVAVERSIONNOTMATCH =
		"Java Version does not match.";
	private static final String LOGOFF = ".  Logging off.";
	private static final String TXT_DISCLAIMER_IMG = "/images/Disclaimer.jpg";
	private static final String TXT_DISCLAIMER_MSG =
		"Warning:  This is an official State of Texas Department of " +
		"Transportation computer system operated for authorized use " +
		"only.  This system is monitored to ensure proper operation, " +
		"to verify the function of applicable security features, " +
		"and for other like purposes.  Attempts to access and " +
		"utilize this system for other than its intended purposes " +
		"are prohibited and may result in prosecution under the " +
		"Computer Fraud and Abuse act of 1986 or other applicable " +
		"statutes and regulations.  Users of this system should not " +
		"expect a \"right to privacy\" for any and all data " +
		"transmissions.";
		
	// defect 10155
	private static final String TXT_DMV_DISCLAIMER_IMG = 
		"/images/disclaimer_dmv.jpg";
	private static final String TXT_DMV_DISCLAIMER_MSG =
		"Warning:  This is an official Department of " +
		"Motor Vehicles computer system operated for authorized use " +
		"only.  This system is monitored to ensure proper operation, " +
		"to verify the function of applicable security features, " +
		"and for other like purposes.  Attempts to access and " +
		"utilize this system for other than its intended purposes " +
		"are prohibited and may result in prosecution under the " +
		"Computer Fraud and Abuse act of 1986 or other applicable " +
		"statutes and regulations.  Users of this system should not " +
		"expect a \"right to privacy\" for any and all data " +
		"transmissions.";
	// end defect 10155
	private static final String TXT_I_ACCEPT = "I Accept";
	private static final String TXT_I_DO_NOT_ACCEPT = "I Do Not Accept";
	
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			DisclaimerWindow laDisclaimerWindow;
			laDisclaimerWindow = new DisclaimerWindow();
			laDisclaimerWindow
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laDisclaimerWindow.show();
			java.awt.Insets insets = laDisclaimerWindow.getInsets();
			laDisclaimerWindow.setSize(
				laDisclaimerWindow.getWidth()
					+ insets.left
					+ insets.right,
				laDisclaimerWindow.getHeight()
					+ insets.top
					+ insets.bottom);
			laDisclaimerWindow.setVisible(true);
		}
		catch (Throwable leThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leThrowable.printStackTrace(System.out);
		}
	}
	
	// defect 10103
	private int ciReturnCode = DECLINE;
	// end defect 10103
	
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjJButton1 =
		null;
	private com.txdot.isd.rts.client.general.ui.RTSButton ivjJButton2 =
		null;
	private JPanel ivjJFrameContentPane = null;
	private JLabel ivjJLabel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSTextArea ivjJTextArea1 =
		null;
		
	/**
	 * Creates a Disclaimer Window
	 */
	public DisclaimerWindow()
	{
		super();
		initialize();
	}
	
	/**
	 * Creates a Disclaimer Window
	 * 
	 * @param aaParent Dialog
	 */
	public DisclaimerWindow(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * Creates a Disclaimer Window
	 * 
	 * @param aaParent JDialog
	 */
	public DisclaimerWindow(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * Creates a Disclaimer Window
	 * 
	 * @param aaParent JFrame
	 */
	public DisclaimerWindow(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (aaAE.getSource() == getJButton1())
		{
			// defect 10255
			Log.write(Log.SQL_EXCP, this, "I Accept has been pressed!");
			// end defect 10255
			
			// defect 10103
			// we should only set this if everything is good!
			// ciReturnCode = ACCEPT;
			// end defect 10103
			
			if (!SystemProperty.isJavaVersionCorrect())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						JAVAVERSIONNOTMATCH
							+ EXPECTEDJAVAVERSION
							+ SystemProperty.getJavaVersion()
							+ DETECTEDJAVAVERSION
							+ System.getProperty(JAVAVERSION)
							+ LOGOFF,
						ScreenConstant.CTL001_FRM_TITLE);
				leRTSEx.displayError(this);
				ciReturnCode = DECLINE;
				// defect 10103
			}
			else 
			{
				// this should be the only place where returncode is 
				// set to ACCEPT..
				ciReturnCode = ACCEPT;
			}
			// we should always default to decline if not accept
			// else if (aaAE.getSource() == getJButton2())
		}
		else
		{
			// end defect 10103
			ciReturnCode = DECLINE;
		}
		hide();
	}
	
	/**
	 * Return the JButton1 property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getJButton1()
	{
		if (ivjJButton1 == null)
		{
			try
			{
				ivjJButton1 =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjJButton1.setName("JButton1");
				ivjJButton1.setMnemonic('I');
				ivjJButton1.setText(TXT_I_ACCEPT);
				ivjJButton1.setBackground(java.awt.Color.white);
				ivjJButton1.setBounds(65, 398, 128, 25);
				ivjJButton1.setEnabled(true);
				this.getRootPane().setDefaultButton(ivjJButton1);
				ivjJButton1.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjJButton1;
	}
	
	/**
	 * Return the JButton2 property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getJButton2()
	{
		if (ivjJButton2 == null)
		{
			try
			{
				ivjJButton2 =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjJButton2.setName("JButton2");
				ivjJButton2.setMnemonic('D');
				ivjJButton2.setText(TXT_I_DO_NOT_ACCEPT);
				ivjJButton2.setBackground(java.awt.Color.white);
				ivjJButton2.setBounds(282, 398, 128, 25);
				ivjJButton2.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjJButton2;
	}
	
	/**
	 * Return the JFrameContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJFrameContentPane()
	{
		if (ivjJFrameContentPane == null)
		{
			try
			{
				ivjJFrameContentPane = new javax.swing.JPanel();
				ivjJFrameContentPane.setName("JFrameContentPane");
				ivjJFrameContentPane.setLayout(null);
				ivjJFrameContentPane.setBackground(
					java.awt.Color.white);
				getJFrameContentPane().add(
					getJLabel1(),
					getJLabel1().getName());
				getJFrameContentPane().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getJFrameContentPane().add(
					getJButton1(),
					getJButton1().getName());
				getJFrameContentPane().add(
					getJButton2(),
					getJButton2().getName());
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjJFrameContentPane;
	}
	
	/**
	 * Return the JLabel1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJLabel1()
	{
		if (ivjJLabel1 == null)
		{
			try
			{
				ivjJLabel1 = new javax.swing.JLabel();
				ivjJLabel1.setName("JLabel1");
				// defect 10103
				// defect 10155
				// Change to DMV logo on DMV Start Date
				//ivjJLabel1.setIcon(
				//	new javax.swing.ImageIcon(
				//		getClass().getResource(
				//			TXT_DISCLAIMER_IMG)));
				if (RTSDate.getCurrentDate().getYYYYMMDDDate()
					>= SystemProperty
						.getDMVStartDate()
						.getYYYYMMDDDate())
				{
					ivjJLabel1.setIcon(
						new javax.swing.ImageIcon(
							getClass().getResource(
								TXT_DMV_DISCLAIMER_IMG)));
				}
				else
				{
					ivjJLabel1.setIcon(
						new javax.swing.ImageIcon(
							getClass().getResource(
								TXT_DISCLAIMER_IMG)));
				}
				// end defect 10155

				// end defect 10103
				ivjJLabel1.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjJLabel1.setBounds(78, 10, 343, 234);
				ivjJLabel1.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjJLabel1;
	}
	
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setBounds(27, 246, 446, 144);
				ivjJScrollPane1.setRequestFocusEnabled(false);
				getJScrollPane1().setViewportView(getJTextArea1());
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}
	
	/**
	 * Return the JTextArea1 property value.
	 * 
	 * @return RTSTextArea
	 */
	private RTSTextArea getJTextArea1()
	{
		if (ivjJTextArea1 == null)
		{
			try
			{
				ivjJTextArea1 =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSTextArea();
				ivjJTextArea1.setName("JTextArea1");
				ivjJTextArea1.setLineWrap(true);
				ivjJTextArea1.setWrapStyleWord(true);
				ivjJTextArea1.setBounds(0, 0, 160, 150);
				ivjJTextArea1.setEditable(false);
				ivjJTextArea1.setForeground(java.awt.Color.black);
				// defect 10155
				// Change to DMV text on DMV Start Date
				//ivjJTextArea1.setText(TXT_DISCLAIMER_MSG);
				if (RTSDate.getCurrentDate().getYYYYMMDDDate()
					>= SystemProperty
						.getDMVStartDate()
						.getYYYYMMDDDate())
				{
					ivjJTextArea1.setText(TXT_DMV_DISCLAIMER_MSG);
				}
				else
				{
					ivjJTextArea1.setText(TXT_DISCLAIMER_MSG);
				}
				// end defect 10155
			}
			catch (java.lang.Throwable ivjExc)
			{
				handleException(ivjExc);
			}
		}
		return ivjJTextArea1;
	}
	
	/**
	 * Returns the return code of the disclaimer window
	 * 
	 * @return int
	 */
	public int getReturnCode()
	{
		return ciReturnCode;
	}
	
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(java.lang.Throwable aeThrowable)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
		leRTSEx.displayError(this);
	}
	
	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("DisclaimerWindow");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(DISCLAIMER_FRM_TITLE);
			setSize(500, 467);
			setModal(true);
			setResizable(false);
			setContentPane(getJFrameContentPane());
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		setLocation(
			((int) (java
				.awt
				.Toolkit
				.getDefaultToolkit()
				.getScreenSize()
				.width
				/ 2
				- getSize().width / 2)),
			((int) (java
				.awt
				.Toolkit
				.getDefaultToolkit()
				.getScreenSize()
				.height
				/ 2
				- getSize().height / 2)));
		getJScrollPane1().getVerticalScrollBar().setValue(0);
	}
	
	/**
	 * Resets the scrollbars of the message
	 */
	public void reset()
	{
		getJScrollPane1().getVerticalScrollBar().setValue(0);
	}
	
	/**
	 * This method does nothing since we are not expecting any data.
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		// empty code block
	}
}
