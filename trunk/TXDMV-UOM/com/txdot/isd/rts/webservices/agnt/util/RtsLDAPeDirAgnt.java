package com.txdot.isd.rts.webservices.agnt.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.commons.logging.LogFactory;
import org.apache.soap.SOAPException;
//import organizations.apache.commons.logging.impl.Log4jFactory;

import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;

import com.soaprsps.objects.LoginObject;
import proxy.soap.SOAPRSPSProxy;
import com.soaprsps.objects.UserObject;

/*
 * RtsLDAPeDirAgnt.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name		 	Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob Brown	03/24/2011	Class Created.
 * Ray Rowehl	05/02/2011	Catch SOAPException and throw it so the
 * 							exception is handled upstream.
 * 							modify login()
 * 							defect 10670 Ver 671
 * R Pilon		10/19/2011	Changed logic for setting of SOAPRSPS endpoint to 
 * 							 load from SystemProperty.
 * 							modify RtsLDAPeDirAgnt()
 * 							defect 11116 Ver 6.8.2
 * R Pilon		12/19/2011	Added logic to call the SOAPRSPS service  to 
 * 							reset a passwords.
 * 							add resetPassword()
 * 							defect 11188
 * ---------------------------------------------------------------------
 */

/**
 * This class provides an interface to eDirectory using LDAP.
 *
 * @version	6.9.0			12/19/2011
 * @author	Bob Brown
 * <br>Creation Date:		03/24/2011 14:22:00
 */

public class RtsLDAPeDirAgnt
{
	public SOAPRSPSProxy caSoapPortTypeProxy;
	public  final static String APPNAME = "SOAPRSPS";
	private final static String ENDPOINT = "http://144.45.211.125/cgi-bin/RSPS/SOAPRSPS.cgi";
	private final static String ADMIN_USER = "RSPSadmin";
	private final static String ADMIN_USER_PW = "Password123";
	private final static String LOGIN_RESPONSE_TXT = "login response: lsSessionID = ";
	private final static String LOGOUT_RESPONSE_TXT = "logout response: lsSessionID = ";
	private final static String REMOTE_EXCEPTION_TXT = "RemoteException = ";
	private final static String EXCEPTION_TXT = "Exception = ";
	private final static String EMPTY_STRING = "";
	private final static String ADDUSER_RESPONSE_TXT = "addUser response: = ";
	private final static String MODUSER_RESPONSE_TXT = "modifyUser response: = ";
	private final static String DELUSER_RESPONSE_TXT = "deleteUser response: = ";
	
	
	/**
	 * RtsLDAPeDirAgnt.java Constructor
	 * 
	 * 
	 */
	public RtsLDAPeDirAgnt() 
	{
		super();
//		if (caSoapPortTypeProxy==null)
//		{
//		System.getProperties().setProperty(LogFactory.class.getName(),
//										   Log4jFactory.class.getName());
		caSoapPortTypeProxy = new SOAPRSPSProxy();
		try
		{
			// defect 11116
			caSoapPortTypeProxy.setEndPoint(new URL(SystemProperty
					.getSoapRspsEndpoint()));
			// end defect 11116
		}
		catch (MalformedURLException leMURLEx)
		{
			leMURLEx.printStackTrace();
		}		
				
	}
	/**
	 * This is the public method that is called to authenticate a user
	 * against eDir.
	 * 
	 */
	public String login(LoginObject asLoginObject) throws Exception
	{
		// recompiling in Java 1.4.2
		String lsSessionID = EMPTY_STRING;

		try
		{
			if (asLoginObject != null)
			{
				asLoginObject.getAppname();
				asLoginObject.getUsername();
				asLoginObject.getUserpass();
				asLoginObject.getIpaddress();
				lsSessionID = caSoapPortTypeProxy.login(asLoginObject);
				Log.write(
					Log.SQL,
					this,
					LOGIN_RESPONSE_TXT + lsSessionID);
			}
		}
		catch (SOAPException aeSOAPEx)
		{
			aeSOAPEx.printStackTrace();
			throw aeSOAPEx;
		}
		catch (RemoteException leRex)
		{
			leRex.printStackTrace();
			Log.write(Log.SQL, this, REMOTE_EXCEPTION_TXT + leRex.getMessage());
			throw leRex;
		}
		catch (Exception leRex)
		{
			leRex.printStackTrace();
			Log.write(Log.SQL, this,EXCEPTION_TXT + leRex.getMessage());
			throw leRex;
		}
		return lsSessionID;
	}
	
	public String logout(String asSessionID)  throws Exception 			
	{
		String lsResponse = EMPTY_STRING;
		try
		{
			lsResponse = caSoapPortTypeProxy.logout(asSessionID);			
			Log.write(Log.SQL, this, LOGOUT_RESPONSE_TXT + lsResponse);
		}
		catch (RemoteException leRex)
		{
			leRex.printStackTrace();
			Log.write(Log.SQL, this, REMOTE_EXCEPTION_TXT + leRex.getMessage());
			throw leRex;
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
			Log.write(Log.SQL, this, EXCEPTION_TXT + leEx.getMessage());
			throw leEx;
		}
		return lsResponse;
	}
	
	public String addUser(String asSessionID, UserObject aaUserObject)
		throws Exception
	{
		String lsResponse = EMPTY_STRING;
		try
		{
			aaUserObject.getGivenName();
			aaUserObject.getMail();
			aaUserObject.getSn();
			aaUserObject.getTelephoneNumber();
			lsResponse = caSoapPortTypeProxy.addUser(asSessionID, aaUserObject);
			Log.write(Log.SQL, this, ADDUSER_RESPONSE_TXT + lsResponse);
		}
		catch (RemoteException leRex) 
		{
			leRex.printStackTrace();
			Log.write(Log.SQL, this, REMOTE_EXCEPTION_TXT + leRex.getMessage());
			throw leRex;
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
			Log.write(Log.SQL, this, EXCEPTION_TXT + leEx.getMessage());
			throw leEx;
		}
		return lsResponse;
	}
	
	public String modifyUser(
		String asSessionID,
		String asUserName,
		UserObject aaUserObject)
		throws Exception
	{
		String lsResponse = EMPTY_STRING;
		try
		{
			aaUserObject.getGivenName();
			aaUserObject.getMail();
			aaUserObject.getSn();
			aaUserObject.getTelephoneNumber();
			lsResponse = caSoapPortTypeProxy.modifyUser(asSessionID, asUserName, aaUserObject);
			Log.write(Log.SQL, this, MODUSER_RESPONSE_TXT + lsResponse);
		}
		catch (RemoteException leRex) 
		{
			leRex.printStackTrace();
			Log.write(Log.SQL, this, REMOTE_EXCEPTION_TXT + leRex.getMessage());
			throw leRex;
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
			Log.write(Log.SQL, this, EXCEPTION_TXT + leEx.getMessage());
			throw leEx;
		}
		return lsResponse;
	}
	
	public String deleteUser(String asSessionID, String asUserName)
		throws Exception
	{
		String lsResponse = EMPTY_STRING;
		try
		{
			lsResponse = caSoapPortTypeProxy.deleteUser(asSessionID, asUserName);
			Log.write(Log.SQL, this, DELUSER_RESPONSE_TXT + lsResponse);
		}
		catch (RemoteException leRex) 
		{
			leRex.printStackTrace();
			Log.write(Log.SQL, this, REMOTE_EXCEPTION_TXT + leRex.getMessage());
			throw leRex;
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
			Log.write(Log.SQL, this, EXCEPTION_TXT + leEx.getMessage());
			throw leEx;
		}
		return lsResponse;
	}

	// defect 11188
	/**
	 * Call the SOAPRSPS service to reset the password for a given user.
	 * 
	 * @param asSessionID
	 * @param asUserName
	 * @return String 
	 * @throws Exception
	 */
	public String resetPassword(String asSessionID, String asUserName)
			throws Exception
	{
		String lsResponse = EMPTY_STRING;
		try
		{
			lsResponse = caSoapPortTypeProxy.resetPassword(asSessionID,
					asUserName);
			Log.write(Log.SQL, this, DELUSER_RESPONSE_TXT + lsResponse);
		}
		catch (RemoteException leRex)
		{
			leRex.printStackTrace();
			Log.write(Log.SQL, this, REMOTE_EXCEPTION_TXT
					+ leRex.getMessage());
			throw leRex;
		}
		catch (Exception leEx)
		{
			leEx.printStackTrace();
			Log.write(Log.SQL, this, EXCEPTION_TXT + leEx.getMessage());
			throw leEx;
		}
		return lsResponse;
	}
	// end defect 11188

	public static void main(String[] aaArgs)
	{
		RtsLDAPeDirAgnt laLDAPeDirAgnt = new RtsLDAPeDirAgnt();
		String lsSessionID = EMPTY_STRING;
		String lsResponse = EMPTY_STRING;
		if (aaArgs.length > 0
			&& aaArgs[0] != null
			&& aaArgs[0].length() > 0)
		{
			if (aaArgs[0].equals("login"))
			{
				LoginObject laLoginObject = new LoginObject(); 
				laLoginObject.setAppname(APPNAME);
				laLoginObject.setUsername(aaArgs[1]);
				laLoginObject.setUserpass(aaArgs[2]);
//				laLoginObject.setUsername("WARLBROWN");
//				laLoginObject.setUserpass("Password123");
				laLoginObject.setIpaddress(EMPTY_STRING);
				try 
				{
					lsSessionID = laLDAPeDirAgnt.login(laLoginObject);
					System.out.println(" login lsSessionID = " + lsSessionID);
				} 
				catch (RemoteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception leEx)
				{
					leEx.printStackTrace();
//					Log.write(Log.SQL, this, EXCEPTION_TXT + leEx.getMessage());

				}	
			}
		}
		if (aaArgs[1] != null
			&& aaArgs[1].length() > 0)
		{
			if (aaArgs[1].equals("addUser"))
			{
				UserObject laUserObject = new UserObject(); 
				laUserObject.setGivenName("Bob");
				laUserObject.setMail("bob.l.brown@txdmv.gov");
				laUserObject.setSn("Brown");
				laUserObject.setTelephoneNumber("512-467-3767");
				try 
				{
					lsResponse = laLDAPeDirAgnt.addUser(lsSessionID, laUserObject);
					System.out.println("addUser lsResponse = " + lsResponse);
				} 
				catch (RemoteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception leEx)
				{
					leEx.printStackTrace();
				}
				// lsResponse = 600:User added successfully
				// TXDOT-Alias = Username = BBROWN1
				// TXDOT-Alias= BBROWN1 cn= 21058 pass= YQ8sQ?)X$1	
			}
			else if (aaArgs[1].equals("modifyUser")) 
			{			
				UserObject laUserObject = new UserObject(); 
				laUserObject.setGivenName("Bob");
				laUserObject.setMail("bob.l.brown@txdmv.gov");
				laUserObject.setSn("Brown");
				laUserObject.setTelephoneNumber("512-891-9854");
				try 
				{
					lsResponse = laLDAPeDirAgnt.modifyUser(lsSessionID, "BBROWN1", laUserObject);
					System.out.println("modifyUser lsResponse = " + lsResponse);
				} 
				catch (RemoteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception leEx)
				{
					leEx.printStackTrace();
				}					
			}
			else if (aaArgs[1].equals("deleteUser"))  
			{
				try 
				{
					lsResponse = laLDAPeDirAgnt.deleteUser(lsSessionID, "BBROWN1");
					System.out.println("deleteUser lsResponse = " + lsResponse);
				} 
				catch (RemoteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception leEx)
				{
					leEx.printStackTrace();
				}	
			}
		}
		if (aaArgs.length > 0
			&& aaArgs[2] != null
			&& aaArgs[2].length() > 0)
		{
			if (aaArgs[2].equals("logout"))
			{
				try 
				{
					lsResponse = laLDAPeDirAgnt.logout(lsSessionID);
					System.out.println("logout lsResponse = " + lsResponse);
				} 
				catch (RemoteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception leEx)
				{
					leEx.printStackTrace();
				}		
			}
		}
	}
}