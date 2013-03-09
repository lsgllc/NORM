package com.txdot.isd.rts.server.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JTextField;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * FrmProxyAuthenticator.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		08/28/2006	Created Class
 * 							defect 8926 Ver 5.2.5
 * Jeff S.		09/15/2006	Made changes to just prompt for username
 * 							and password.  Now the host and port must be
 * 							setup on the server setup.
 * 							defect 8926 Ver 5.2.5
 * ---------------------------------------------------------------------
 */

/**
 * <p>Used to proxy out of a corporate firewall to gain public access
 * to an external url.</p> 
 * 
 * <p>Use the statement below to install the proxy Authenticator.  If 
 * all of the needed fields are not provided then this class will 
 * show a Dialog and ask the user to enter the needed information.</p>
 * 
 * <p>
 * <code>Authenticator.setDefault(new ProxyAuthenticator());</code>
 * </p>
 * 
 * <p>In order to avoid the Dialog make sure that the SystemProperty 
 * fields below are saved in the system:
 * <code>
 * <br>http.proxyUser
 * <br>http.proxyPassword
 * </code></p>
 * 
 * <p>These fields below must be setup in order for this class to be 
 * used:
 * <code>
 * <br>http.proxyHost
 * <br>http.proxyPort
 * </code></p>
 *
 * @version	5.2.5			09/15/2006
 * @author	Jeff Seifert
 * <br>Creation Date:		09/07/2006 11:50:00
 */
public class FrmProxyAuthenticator
	extends JDialog
	implements ActionListener
{
	/**
	 * Error Color. This is the background color for the components if 
	 * there is any input error.
	 */
	public static Color ERR_COLOR = new Color(255, 0, 0);

	private static final String PROXY_AUTH_TITLE =
		"Proxy Authenticator";
	private static final String PROXY_PASS = "Proxy Password:";
	private static final String PROXY_USER = "Proxy Username:";
	private static final String USER_PASS_REQ = 
		"Username and Password Required:";

	private javax.swing.JButton jButtonCancel = null;
	private javax.swing.JButton jButtonOk = null;

	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JLabel jLabelProxyPassword = null;
	private javax.swing.JLabel jLabelProxyUsername = null;

	private javax.swing.JPasswordField jTextFieldProxyPassword = null;
	private javax.swing.JTextField jTextFieldProxyUsername = null;
	private javax.swing.JLabel jLabelRequired = null;
	
	/**
	 * ProxyAuthenticator.java Constructor
	 * 
	 * @param asProxyUsername String
	 * @param asProxyPassword String
	 */
	public FrmProxyAuthenticator(
		String asProxyUsername,
		String asProxyPassword)
	{
		super();
		this.setModal(true);
		initialize();

		getJTextFieldProxyUsername().setText(asProxyUsername);

		getJTextFieldProxyPassword().setText(asProxyPassword);
	}
	
	/**
	 * Handles action Events.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (aaAE.getSource() == getJButtonCancel())
		{
			this.setVisible(false);
		}
		else
		{
			boolean lbError = false;

			clearErrorFields();

			if (getJTextFieldProxyUsername().getText().trim().length()
				== 0)
			{
				lbError = true;
				setErrorField(getJTextFieldProxyUsername());
			}
			if (new String(getJTextFieldProxyPassword().getPassword())
				.trim()
				.length()
				== 0)
			{
				lbError = true;
				setErrorField(getJTextFieldProxyPassword());
			}

			if (!lbError)
			{
				this.setVisible(false);
			}
		}
	}
	
	/**
	 * Clears all the fields of the error color.
	 */
	private void clearErrorFields()
	{
		getJTextFieldProxyPassword().setBackground(Color.white);
		getJTextFieldProxyPassword().setForeground(Color.black);

		getJTextFieldProxyUsername().setBackground(Color.white);
		getJTextFieldProxyUsername().setForeground(Color.black);
	}
	
	/**
	 * This method initializes jButtonCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButtonCancel()
	{
		if (jButtonCancel == null)
		{
			jButtonCancel = new javax.swing.JButton();
			jButtonCancel.setBounds(168, 110, 78, 25);
			jButtonCancel.setText(CommonConstant.BTN_TXT_CANCEL);
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/**
	 * This method initializes jButtonOk
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButtonOk()
	{
		if (jButtonOk == null)
		{
			jButtonOk = new javax.swing.JButton();
			jButtonOk.setBounds(45, 110, 78, 25);
			jButtonOk.setText(CommonConstant.BTN_TXT_ENTER);
			jButtonOk.addActionListener(this);
			this.getRootPane().setDefaultButton(jButtonOk);
		}
		return jButtonOk;
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJTextFieldProxyUsername(), null);
			jContentPane.add(getJTextFieldProxyPassword(), null);
			jContentPane.add(getJLabelProxyUsername(), null);
			jContentPane.add(getJLabelProxyPassword(), null);
			jContentPane.add(getJButtonOk(), null);
			jContentPane.add(getJButtonCancel(), null);
			jContentPane.add(getJLabelRequired(), null);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jLabelProxyPassword
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelProxyPassword()
	{
		if (jLabelProxyPassword == null)
		{
			jLabelProxyPassword = new javax.swing.JLabel();
			jLabelProxyPassword.setBounds(19, 77, 104, 20);
			jLabelProxyPassword.setText(PROXY_PASS);
		}
		return jLabelProxyPassword;
	}
	
	/**
	 * This method initializes jLabelProxyUsername
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelProxyUsername()
	{
		if (jLabelProxyUsername == null)
		{
			jLabelProxyUsername = new javax.swing.JLabel();
			jLabelProxyUsername.setBounds(19, 44, 104, 20);
			jLabelProxyUsername.setText(PROXY_USER);
		}
		return jLabelProxyUsername;
	}
	
	/**
	 * This method initializes jLabelRequired
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabelRequired()
	{
		if (jLabelRequired == null)
		{
			jLabelRequired = new javax.swing.JLabel();
			jLabelRequired.setBounds(19, 13, 208, 18);
			jLabelRequired.setText(USER_PASS_REQ);
		}
		return jLabelRequired;
	}
	
	/**
	 * This method initializes jTextFieldProxyPassword
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JPasswordField getJTextFieldProxyPassword()
	{
		if (jTextFieldProxyPassword == null)
		{
			jTextFieldProxyPassword = new javax.swing.JPasswordField();
			jTextFieldProxyPassword.setBounds(142, 77, 130, 20);
		}
		return jTextFieldProxyPassword;
	}
	
	/**
	 * This method initializes jTextFieldProxyUsername
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJTextFieldProxyUsername()
	{
		if (jTextFieldProxyUsername == null)
		{
			jTextFieldProxyUsername = new javax.swing.JTextField();
			jTextFieldProxyUsername.setBounds(142, 44, 130, 20);
		}
		return jTextFieldProxyUsername;
	}
	
	/**
	 * This returns the Password that was entered in the password
	 * test field.
	 * 
	 * @return char[]
	 */
	public char[] getPassword()
	{
		return getJTextFieldProxyPassword().getPassword();
	}
	
	/**
	 * This returns the Username that was entered in the username
	 * test field.
	 * 
	 * @return String
	 */
	public String getUsername()
	{
		return getJTextFieldProxyUsername().getText();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(300, 177);
		this.setContentPane(getJContentPane());
		this.setTitle(PROXY_AUTH_TITLE);
	}
	
	/**
	 * Sets the fields color to red.
	 * 
	 * @param aaField JTextField
	 */
	private void setErrorField(JTextField aaField)
	{
		aaField.setBackground(ERR_COLOR);
		aaField.setForeground(Color.white);
	}
}