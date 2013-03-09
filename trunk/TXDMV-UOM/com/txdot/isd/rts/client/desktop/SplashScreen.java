package com.txdot.isd.rts.client.desktop;

import javax.swing.JWindow;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * SplashScreen.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		09/14/2005	Created Class.
 * 							defect 8372 Ver 5.2.3
 * B Hargrove	09/17/2009	Use new logos at cutover for new DMV agency.
 * 							add DMV_LOGO
 * 							modify getJlblTxdotLogo()
 * 							defect 10155 Ver Defect_POS_F 		 
 * ---------------------------------------------------------------------
 */

/**
 * This class is the spash screen for the RTS application.
 *
 * @version	Defect_POS_F	09/17/2009
 * @author	Jeff S.
 * <br>Creation Date:		09/14/2005 13:45:00
 */
public class SplashScreen extends JWindow
{
	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JLabel jlblProgress = null;
	private javax.swing.JLabel jlblTitle = null;
	private javax.swing.JLabel jlblTxdotLogo = null;
	private javax.swing.JLabel jlblVersion = null;
	private javax.swing.JLabel jlblVersionTag = null;
	
	/**
	 * Constants
	 */
	private static final String FONT = "Arial";
	private static final String TITLE = "Registration and Title System";
	private static final String TXDOT_LOGO = "/images/Disclaimer.jpg";
	// defect 10155
	private static final String DMV_LOGO = "/images/disclaimer_dmv.jpg";
	// end defect 10155
	private static final String VERSION = "Version";

	/**
	 * Used to test the splash screen.  This test shows tha splash 
	 * screen then sleeps for a few seconds then exits.
	 * 
	 * @param aarrArgs
	 */
	public static void main(String[] aarrArgs)
	{
		SplashScreen laSpashScreen = new SplashScreen();
		laSpashScreen.show();
		laSpashScreen.setVisible(true);
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			// Empty - do not do anything
		}
		System.exit(0);
	}
	/**
	 * This is the default constructor
	 */
	public SplashScreen()
	{
		super();
		initialize();
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
			jContentPane.add(getJlblTxdotLogo(), null);
			jContentPane.add(getJlblTitle(), null);
			jContentPane.add(getJlblVersionTag(), null);
			jContentPane.add(getJlblVersion(), null);
			jContentPane.add(getJlblProgress(), null);
			jContentPane.setBorder(
				javax.swing.BorderFactory.createLineBorder(
					java.awt.Color.darkGray,
					1));
			jContentPane.setBackground(java.awt.Color.white);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jlblProgress
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlblProgress()
	{
		if (jlblProgress == null)
		{
			jlblProgress = new javax.swing.JLabel();
			jlblProgress.setBounds(18, 283, 315, 17);
			jlblProgress.setText(CommonConstant.STR_SPACE_EMPTY);
			jlblProgress.setFont(new java.awt.Font(FONT, java.awt.Font.PLAIN, 10));
		}
		return jlblProgress;
	}
	/**
	 * This method initializes jlblTitle
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlblTitle()
	{
		if (jlblTitle == null)
		{
			jlblTitle = new javax.swing.JLabel();
			jlblTitle.setBounds(14, 6, 322, 26);
			jlblTitle.setText(TITLE);
			jlblTitle.setFont(
				new java.awt.Font(FONT, java.awt.Font.BOLD, 18));
		}
		return jlblTitle;
	}
	/**
	 * This method initializes jlblTxdotLogo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlblTxdotLogo()
	{
		if (jlblTxdotLogo == null)
		{
			jlblTxdotLogo = new javax.swing.JLabel();
			jlblTxdotLogo.setBounds(14, 57, 322, 225);
			jlblTxdotLogo.setText(CommonConstant.STR_SPACE_EMPTY);
			// defect 10155
			// Change to DMV logo on DMV Start Date
			//jlblTxdotLogo.setIcon(
			//	new javax.swing.ImageIcon(
			//		getClass().getResource(TXDOT_LOGO)));
			if (RTSDate.getCurrentDate().getYYYYMMDDDate()
				>= SystemProperty
					.getDMVStartDate()
					.getYYYYMMDDDate())
			{
				jlblTxdotLogo.setIcon(
					new javax.swing.ImageIcon(
						getClass().getResource(DMV_LOGO)));
			}
			else
			{
				jlblTxdotLogo.setIcon(
					new javax.swing.ImageIcon(
						getClass().getResource(TXDOT_LOGO)));
			}
			// end defect 10155
		}
		return jlblTxdotLogo;
	}
	/**
	 * This method initializes jlblVersion
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlblVersion()
	{
		if (jlblVersion == null)
		{
			jlblVersion = new javax.swing.JLabel();
			jlblVersion.setBounds(65, 33, 271, 17);
			jlblVersion.setText(
				SystemProperty.getVersionNo()
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_DASH
					+ CommonConstant.STR_SPACE_ONE
					+ SystemProperty.getVersionDate());
		}
		return jlblVersion;
	}
	/**
	 * This method initializes jlblVersionTag
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlblVersionTag()
	{
		if (jlblVersionTag == null)
		{
			jlblVersionTag = new javax.swing.JLabel();
			jlblVersionTag.setBounds(14, 33, 48, 17);
			jlblVersionTag.setText(VERSION);
		}
		return jlblVersionTag;
	}
	/**
	 * This method initializes this JWindow and sets the location to
	 * the center of the screen.
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(350, 305);
		this.setContentPane(getJContentPane());
		this.setLocation(
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
	}
	/**
	 * Used to update the progress label on the Splash Screen.
	 * 
	 * @param asProgress String
	 */
	public void updateProgress(String asProgress)
	{
		getJlblProgress().setText(asProgress);
		this.repaint();
	}
}