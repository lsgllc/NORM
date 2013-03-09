package com.txdot.isd.rts.webservices.ses.util;

import java.util.Hashtable;

import javax.naming.*;
import javax.naming.directory.*;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * RtsLDAPeDir.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob Brown	04/01/2011	Class Created.
 * Ray Rowehl	05/02/2011	Add throws SOAPException so we can handle up
 * 							stream.
 * 							modify login()
 * 							defect 10670 Ver 671
 * Richard Pilon 05/05/2011	Redo the login method to use direct bind 
 * 							authentication instead of calling the SOAP 
 * 							authentication.
 * 							add login()
 * 							delete login()
 * 							defect 10670 Ver 671
 * Richard Pilon 05/06/2011	Modified thrown RTSExceptions to use 
 * 							ErrorsConstants.
 * 							modify login()
 * 							defect 10670 Ver 671
 * Richard Pilon 05/10/2011	Changed to use SystemProperty for the 
 * 							eDirectory URL, user id and password.
 * 							add csEDirProviderUrl, csProxyUserId, 
 * 							csProxyPassword
 * 							commented out PROVIDER_URL, ADMIN_PRINCIPAL,
 * 							ADMIN_CREDENTIAL
 * 							modify login(), getCnForAlias()
 * 							defect 10670 Ver 671
 * R Pilon		06/13/2011	Add edit to ensure user id and password are
 * 							populated
 * 							modify login()
 * 							defect 10670 Ver 6.8.0
 * R Pilon		08/31/2011	Changed eDir bind from txdot-alias to 
 * 							txdot-v21alias
 * 							modify getCnForAlias()
 * 							defect 10980 Ver 6.8.1
 * R Pilon		10/21/2011	Change exception handling for AuthenticationException 
 * 							 and OperationNotSupportedException to output a short
 * 							 measningfull message and not the stack trace.
 * 							Modify login().
 * 							defect 11042 Ver 6.9.0
 * R Pilon		01/23/2012	Modify error/exception handling to provide more
 * 							  information about the specific issue back to the 
 * 							  caller.
 * 							Modify login()
 * 							Defect 11190 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * This class provides an interface to eDirectory using LDAP.
 *
 * @version	6.10.0			01/23/2012
 * @author	Mark Reyes
 * @author	Bob Brown
 * @author	Richard Pilon
 * <br>Creation Date:		02/14/2011 14:00:00
 */
public class RtsLDAPeDir
{
	//private String EDIR_SERVER = SystemProperty.geteDirServer();
	// defect 10670
//	private final static String PROVIDER_URL =
//		"ldap://144.45.211.121:389";
//	private final static String ADMIN_PRINCIPAL = "RSPSadmin";
//	private final static String ADMIN_CREDENTIALS = "Password123";
	private String csEDirProviderUrl = SystemProperty.geteDirServer();
	private String csProxyUserId = SystemProperty.getEDirUserId();
	private String csProxyPassword = SystemProperty.getEDirPassword();
	// end defect 10670

	/**
	 * This was the former SOAP method.  Replace with a direct bind method.
	 * 
	 * This is the public method that is called to authenticate a user
	 * against eDir.
	 */
	public String login(String asUsername, String asPassword)
		throws RTSException
	{
		// defect 10670
		if (UtilityMethods.isEmpty(asUsername)
			|| UtilityMethods.isEmpty(asPassword))
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_USERID_PWD_REQUIRED);
		}
		// end defect 10670
			
		String lsReturnvalue = "false";
		DirContext laCtx = null;
		try
		{
			// defect 11190
			String lsCn;
			// get the cn for the entered txdot-v21alias (username)
			try {
				lsCn = this.getCnForAlias(asUsername);
			} catch (Exception eaExc) {
				System.out.println("An Exception occurred when authenticating proxy user id");
				eaExc.printStackTrace();
				// unrecoverable...proxy eDir userid authorization failure
				throw new RTSException(
						ErrorsConstant.ERR_NUM_EDIR_PROXY_USER_AUTH_FAILURE);
			}
			// end defect 11190
			
			if (lsCn == null || lsCn.length() < 1)
			{			
				// defect 10670
				// user was not found in eDirectory
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_TAB_ERR);
				// end defect 10670
			}

			String lsUserDN = "cn=" + lsCn + ",ou=Active,o=Lookup";

			// attempt bind for authentication
			laCtx = this.bind(csEDirProviderUrl, lsUserDN, asPassword);

			Attributes laAttrs = laCtx.getAttributes(lsUserDN);
			if (laAttrs.get("pwmlastpwdupdate") == null)
			{
				// defect 10670
				// defect 11190
				// need to change temporary password through PWM
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_EDIR_PWD_TEMPORARY);
				// end defect 11190
				// end defect 10670
			}
			else if (laAttrs.get("pwmresponseset") == null)
			{
				// defect 10670
				// need to set security responses through PWM
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_EDIR_NO_REPSONSES);
				// end defect 10670
			}

			lsReturnvalue = "true";
		}
		catch (AuthenticationException authEx)
		{
			// defect 11042
			if (authEx.toString().indexOf("failed authentication (-669)") > -1)
			{
				System.out.println("Authentication failed for username \""
						+ asUsername + "\".  Invalid credentials.");
				// defect 11190
				throw new RTSException(
						ErrorsConstant.ERR_NUM_WEBAGNT_AUTH_LDAP_ERR);
				// end defect 11190
			}
			else
			{
				authEx.printStackTrace();
			}
			// end defect 11042
		}
		catch (OperationNotSupportedException onsEx)
		{
			// -197 = login lockout; -220 = account expired
			// defect 10670
			if (onsEx.toString().indexOf("login lockout (-197)") > -1)
			{
				// defect 11042
				System.out.println("Account locked for username \""
						+ asUsername + "\".");
				// end defect 11042
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_EDIR_LOCKED);
			}
			else if (onsEx.toString().indexOf("log account expired (-220)")	> -1)
			{
				// defect 11042
				System.out.println("Account expired for username \""
						+ asUsername + "\".");
				// end defect 11042
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_EDIR_PWD_EXPIRED);
			}
			// end defect 10670

			onsEx.printStackTrace();
		}
		catch (NamingException ne)
		{
			ne.printStackTrace();
		}
		finally
		{
			if (laCtx != null)
			{
				try
				{
					laCtx.close();
				}
				catch (NamingException e1)
				{
					// probably do nothing here, but print for now
					e1.printStackTrace();
				}
				laCtx = null;
			}
		}
		return lsReturnvalue;

	}

//	/**
//	 * This was the former SOAP method.  Replace with a direct bind method.
//	 * 
//	 * This is the public method that is called to authenticate a user
//	 * against eDir.
//	 */
//	private String loginx(String asUsername, String asPassword)
//		throws SOAPException
//	{
//		// recompiling in Java 1.4.2
//		String lsSessionID = "";
//		try
//		{
//			RtsLDAPeDirAgnt laRtsLDAPeDirAgnt = new RtsLDAPeDirAgnt();
//			LoginObject laLoginObject = new LoginObject();
//			laLoginObject.setAppname(RtsLDAPeDirAgnt.APPNAME);
//			laLoginObject.setUsername(asUsername);
//			laLoginObject.setUserpass(asPassword);
//			lsSessionID = laRtsLDAPeDirAgnt.login(laLoginObject);
//		}
//		catch (RemoteException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return lsSessionID;
//	}

	/**
	 * Static main for testing.
	 * 
	 * @param aaArgs
	 */
	public static void main(String[] aaArgs)
	{
		if (aaArgs[0] != null && aaArgs.length > 1)
		{
			RtsLDAPeDir laTest = new RtsLDAPeDir();
			try
			{
				laTest.login(aaArgs[0], aaArgs[1]);
			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.printStackTrace();
			}
		}
	}
	
	/**
	 * Search eDirectory and return the cn for the given txdot-v21alias.
	 * 
	 * @param asAlias
	 * @return cn found for the txdot-v21alias
	 * @throws NamingException
	 */
	private String getCnForAlias(String asAlias) throws NamingException
	{
		String lsCn = null;

		DirContext laCtx = null;
		try
		{
			String lsUserDN = "cn=" + csProxyUserId + ",o=support";

			// bind with admin user so that we can search
			laCtx =
				this.bind(csEDirProviderUrl, lsUserDN, csProxyPassword);

			Attributes laAttrs = new BasicAttributes(true);

			// defect 10980
			// search for the txdot-v21alias 
			laAttrs.put(new BasicAttribute("txdot-v21alias", asAlias));
			// end defect 10980

			NamingEnumeration laNamingEnum =
				laCtx.search("ou=Active,o=Lookup", laAttrs);
			if (laNamingEnum.hasMore())
			{
				laAttrs =
					((SearchResult) laNamingEnum.next())
						.getAttributes();
				lsCn = laAttrs.get("cn").toString();
			}

			if (lsCn != null)
			{
				lsCn = lsCn.substring(3);
			}
		}
		finally
		{
			if (laCtx != null)
			{
				try
				{
					laCtx.close();
				}
				catch (NamingException e1)
				{
					// probably do nothing here, but print for now
					e1.printStackTrace();
				}
				laCtx = null;
			}
		}

		return lsCn;
	}

	/**
	 * Perform LDAP Bind for provided credentials.
	 * 
	 * @param asUrl
	 * @param asUserDN
	 * @param asPassword
	 * @return DirContext for the current bind
	 * @throws NamingException
	 */
	private DirContext bind(
		String asUrl,
		String asUserDN,
		String asPassword)
		throws NamingException
	{
		DirContext laCtx = null;

		Hashtable laEnv = new Hashtable();
		laEnv.put(
			Context.INITIAL_CONTEXT_FACTORY,
			"com.sun.jndi.ldap.LdapCtxFactory");
		laEnv.put(Context.PROVIDER_URL, asUrl);
		laEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		laEnv.put(Context.SECURITY_PRINCIPAL, asUserDN);
		laEnv.put(Context.SECURITY_CREDENTIALS, asPassword);

		laCtx = new InitialDirContext(laEnv);

		laEnv = null;

		return laCtx;
	}

}
