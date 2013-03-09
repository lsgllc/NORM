package com.txdot.isd.rts.client.desktop;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 * InternetListener.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							Format comments, Hungarian notation for 
 * 							variables. 
 * 							defect 7885 Ver 5.2.3 
 * Ray Rowehl	07/16/2005	Constants
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * An ActionListener for the Internet Button on RTSDesktop
 * 
 * @version	5.2.3		07/16/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	02/25/2002 16:56:56
 */
public class InternetListener implements ActionListener
{
	private static final String TXT_INTERNET_RENEWAL_COUNT =
		"Current Internet Renewal Count: ";
	private RTSDeskTop desktop;
	/**
	 * Creates an InternetListener
	 * 
	 * @param RTSDeskTop aaDesktop
	 */
	public InternetListener(RTSDeskTop aaDesktop)
	{
		super();
		this.desktop = aaDesktop;
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		JButton laSource = (JButton) aaAE.getSource();
		try
		{
			Vector lvOfcInfo = new java.util.Vector();
			lvOfcInfo.add(
				new Integer(SystemProperty.getOfficeIssuanceNo()));

			Integer liInt =
				(
					Integer) com
						.txdot
						.isd
						.rts
						.services
						.communication
						.Comm
						.sendToServer(
					com
						.txdot
						.isd
						.rts
						.services
						.util
						.constants
						.GeneralConstant
						.INTERNET_REG_REN_PROCESSING,
					com
						.txdot
						.isd
						.rts
						.services
						.webapps
						.util
						.constants
						.RegRenProcessingConstants
						.GET_RENEWAL_COUNT,
					lvOfcInfo);
			if (liInt != null)
			{
				laSource.setText(
					TXT_INTERNET_RENEWAL_COUNT + liInt.intValue());
			}
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(desktop);
		}
	}
}
