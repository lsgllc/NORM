package com.txdot.isd.rts.server.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/*
 * ProxyAuthenticator.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		09/07/2006	Created Class
 * 							defect 8926 Ver 5.2.5
 * 							add promptForProxyInfo()
 * Jeff S.		09/15/2006	Made the code real simple to handle just
 * 							username and password not being there.
 * 							Must have host and port setup on server 
 * 							setup.
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
 *
 * @version	5.2.5			09/15/2006
 * @author	Jeff Seifert
 * <br>Creation Date:		09/07/2006 11:50:00
 */
public class ProxyAuthenticator extends Authenticator
{
	public static final String HTTP_PROXY_PASSWORD =
		"http.proxyPassword";
	public static final String HTTP_PROXY_USER = "http.proxyUser";

	/**
	 * Called when Authentication is needed.
	 * 
	 * @return PasswordAuthentication
	 */
	protected PasswordAuthentication getPasswordAuthentication()
	{
		if (System.getProperty(HTTP_PROXY_USER) == null
			|| System.getProperty(HTTP_PROXY_USER).length() == 0
			|| System.getProperty(HTTP_PROXY_PASSWORD) == null
			|| System.getProperty(HTTP_PROXY_PASSWORD).length() == 0)
		{
			FrmProxyAuthenticator laProxyPropt =
				new FrmProxyAuthenticator(
					System.getProperty(HTTP_PROXY_USER),
					System.getProperty(HTTP_PROXY_PASSWORD));
			laProxyPropt.setVisible(true);

			return new PasswordAuthentication(
				laProxyPropt.getUsername(),
				laProxyPropt.getPassword());
		}
		else
		{
			return new PasswordAuthentication(
				System.getProperty(HTTP_PROXY_USER),
				System.getProperty(HTTP_PROXY_PASSWORD).toCharArray());
		}
	}
}