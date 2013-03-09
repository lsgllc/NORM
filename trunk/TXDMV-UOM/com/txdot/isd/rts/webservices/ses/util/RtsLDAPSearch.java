package com.txdot.isd.rts.webservices.ses.util;

import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 * RtsLDAPSearch.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Pilon		05/18/2011	Class Created.
 * 							defect 10670 Ver 6.7.1
 * R Pilon		08/03/2011	Refactored.
 * 							modify getADUserByUserId(), 
 * 							  getEDirUserByUserId()
 * 							deleted getADUserByName(), 
 * 							  getEDirUserByUserId()
 * 							added getADUserByLastName(),
 * 							  getADUserByFirstLastName(), 
 * 							  getEDirUserByLastName()
 * 							  getEDirUserByFirstLastName()
 * 							defect 10718 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * This class provides the access to the Active Directory and eDirectory
 * LDAP search utilities.
 * 
 * @version 6.8.1 			08/03/2011
 * @author 	RPILON-C 
 * <br>Creation Date: 		05/18/2011 14:00:00
 */
public class RtsLDAPSearch
{
	/**
	 * Get the Active Directory user attributes for a user id. An
	 * exception is thrown if more than one user is returned from the
	 * search as only one user should exist for a given user id.
	 * 
	 * NOTE: The CN is considered the user id in Active Directory.
	 * 
	 * @param asUserId
	 * @return RtsSecurityUser
	 * @throws NamingException
	 * @throws RTSException
	 */
	public RtsSecurityUser getADUserByUserId(String asUserId)
		throws NamingException, RTSException
	{
		RtsLDAPADSearch laADSearch = new RtsLDAPADSearch();
		List secUsers = laADSearch.getUserByUserId(asUserId);
		RtsSecurityUser secUser = null;
		if (secUsers != null && secUsers.size() == 1)
		{
			secUser = (RtsSecurityUser) secUsers.get(0);
		}
		else if (secUsers.size() > 1)
		{
			throw new RTSException("2302", new Exception(
				"Multiple users found for user id"));
		}

		return secUser;
	}

	/**
	 * Get the Active Directory user attributes for a last name.
	 * 
	 * @param asLastName
	 * @return RtsSecurityUser
	 * @throws NamingException
	 * @throws RTSException
	 */
	public List getADUserByLastName(String asLastName)
		throws NamingException, RTSException
	{
		RtsLDAPADSearch laADSearch = new RtsLDAPADSearch();
		return laADSearch.getUserByFirstLastName(null, asLastName);
	}

	/**
	 * Get the Active Directory user attributes for a first and last
	 * name. First name may be null, forcing to search by last name
	 * only.
	 * 
	 * @param asFirstName
	 * @param asLastName
	 * @return RtsSecurityUser
	 * @throws NamingException
	 * @throws RTSException
	 */
	public List getADUserByFirstLastName(String asFirstName,
		String asLastName) throws NamingException, RTSException
	{
		RtsLDAPADSearch laADSearch = new RtsLDAPADSearch();
		return laADSearch.getUserByFirstLastName(asFirstName,
			asLastName);
	}
	
	/**
	 * Get the eDirectory user attributes for a user id. An exception is
	 * thrown if more than one user is returned from the search as only
	 * one user should exist for a given user id.
	 * 
	 * NOTE: The TXDOT-V21ALIAS is considered the user id in eDirectory.
	 * 
	 * @param asUserId
	 * @return RtsSecurityUser
	 * @throws NamingException
	 * @throws RTSException
	 */
	public RtsSecurityUser getEDirUserByUserId(String asUserId)
		throws NamingException, RTSException
	{
		RtsLDAPeDirSearch laEDirSearch = new RtsLDAPeDirSearch();
		List secUsers = laEDirSearch.getUserByUserId(asUserId);
		RtsSecurityUser secUser = null;
		if (secUsers != null && secUsers.size() == 1)
		{
			secUser = (RtsSecurityUser) secUsers.get(0);
		}
		else if (secUsers.size() > 1)
		{
			throw new RTSException("2302", new Exception(
				"Multiple users found for user id"));
		}

		return secUser;
	}

	/**
	 * Get the eDirectory user attributes for a last name.
	 * 
	 * @param asLastName
	 * @return RtsSecurityUser
	 * @throws NamingException
	 * @throws RTSException
	 */
	public List getEDirUserByLastName(String asLastName)
		throws NamingException, RTSException
	{
		RtsLDAPeDirSearch laEDirSearch = new RtsLDAPeDirSearch();
		return laEDirSearch.getUserByFirstLastName(null, asLastName);
	}

	/**
	 * Get the eDirectory user attributes for a first and last name.
	 * First name may be null, forcing to search by last name only.
	 * 
	 * @param asFirstName
	 * @param asLastName
	 * @return RtsSecurityUser
	 * @throws NamingException
	 * @throws RTSException
	 */
	public List getEDirUserByFirstLastName(String asFirstName,
		String asLastName) throws NamingException, RTSException
	{
		RtsLDAPeDirSearch laEDirSearch = new RtsLDAPeDirSearch();
		return laEDirSearch.getUserByFirstLastName(asFirstName,
			asLastName);
	}

	public static void main(String[] aaArgs)
	{
		if (aaArgs.length > 0 && aaArgs[0] != null)
		{
			try
			{
				// initialize SystemProperty to load user ids, passwords
				// and servers used for LDAP server connections
				com.txdot.isd.rts.services.communication.Comm
					.setIsServer(true);
				SystemProperty.initialize();

				RtsLDAPSearch laTest = new RtsLDAPSearch();

				// AD
				// Attributes laAttrs = laTest
				// .getADUserByUserId(aaArgs[0]);
				// if (laAttrs == null || laAttrs.size() < 1)
				// {
				// System.out
				// .println("\nNo users found for search criteria");
				// }
				// else
				// {
				// System.out.println("\n" + laAttrs.get("cn"));
				// System.out.println("\n" + laAttrs.get("sn"));
				// System.out.println("\n" + laAttrs.get("givenName"));
				// System.out.println("\n" + laAttrs.get("mail"));
				// System.out.println("\n"
				// + laAttrs.get("telephoneNumber"));
				// }

				// eDir
				// RtsSecurityUser user = laTest
				// .getEDirUserByUserId(aaArgs[0]);
				// if (user != null)
				// {
				// System.out.println(user.getEmailAddr());
				// System.out.println(user.getFirstName());
				// System.out.println(user.getLastName());
				// System.out.println(user.getPhoneNbr());
				// System.out.println(user.getUserId());
				// } else {
				// System.out.println("No users found");
				// }

				List secUsers;
				if (aaArgs.length > 1)
				{
					secUsers = laTest.getEDirUserByFirstLastName(
					// null, null);
						aaArgs[0], aaArgs[1]);
				}
				else
				{
					secUsers =
						laTest.getEDirUserByLastName(aaArgs[0]);
				}
				if (secUsers != null && secUsers.size() > 0)
				{
					Iterator secUsersIter = secUsers.iterator();
					while (secUsersIter.hasNext())
					{
						RtsSecurityUser user =
							(RtsSecurityUser) secUsersIter.next();
						if (user != null)
						{
							System.out.println(user.getEmailAddr());
							System.out.println(user.getFirstName());
							System.out.println(user.getLastName());
							System.out.println(user.getPhoneNbr());
							System.out.println(user.getUserId());
							System.out.println(user.getPhoneNbrNoHyphens());
						}
					}
				}
				else
				{
					System.out.println("No users found");
				}
			}
			catch (RTSException rtsEx)
			{
				System.out.println(rtsEx.getMessage());
			}
			catch (Exception laEx)
			{
				laEx.printStackTrace();
			}
		}
	}
}
