package com.txdot.isd.rts.webservices.ses.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 * RtsLDAPADSearch.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Pilon		05/16/2011	Class Created.
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	07/11/2011	Add handling for search by name.
 * 							add getUserByName(), printAttributes()
 * 							defect 10670 Ver 6.8.0
 * R Pilon		08/03/2011	Refactor class.
 * 							modify getUserByCommonName(), bind(), 
 * 							  printAttributes() 
 * 							added getUserByUserId(),
 * 							  getUserByEmailAddr(),
 * 							  getUserByFirstLastName(),
 * 							  populateRtsUser()
 * 							deleted getUserIdByCommonName(), 
 * 							  getUserIdByEmailAddr(), 
 * 							  getUserIdByFirstLastName(),
 * 							  searchByEmailAddr(), getUser(), 
 * 							  searchByFirstLastName(), getUserId()
 * 							defect 10718 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * This class provides an interface for searching Active Directory.
 * 
 * @version 6.8.1 			08/03/2011
 * @author 	RPILON-C 
 * <br>Creation Date: 		05/16/2011 14:00:00
 */
public class RtsLDAPADSearch
{
	private final static String INIT_CTX_FACTORY =
		"com.sun.jndi.ldap.LdapCtxFactory";
	private final static String NAME = "dc=dot,dc=state,dc=tx,dc=us";
	private String csADProviderUrl =
		SystemProperty.getActiveDirServer();
	private String csProxyPassword =
		SystemProperty.getActiveDirPassword();
	private String csProxyUserId = SystemProperty.getActiveDirUserId();

	/**
	 * Get Active Directory user(s) for a given common name (cn).
	 * 
	 * @param asCn
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 * @throws RTSException
	 */
	protected List getUserByCommonName(String asCn)
		throws NamingException, RTSException
	{
		if (asCn == null)
		{
			throw new RTSException("150", new Exception(
				"Parameter asCn may not be null"));
		}

		return this.search("cn=" + asCn);
	}

	/**
	 * Get Active Directory user(s) for a given user id (cn).
	 * 
	 * @param asUserId
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 * @throws RTSException
	 */
	protected List getUserByUserId(String asUserId)
		throws NamingException, RTSException
	{
		if (asUserId == null)
		{
			throw new RTSException("150", new Exception(
				"Parameter asUserId may not be null"));
		}

		return this.search("cn=" + asUserId);
	}

	/**
	 * Get Active Directory user(s) for a given email address.
	 * 
	 * @param asEmailAddr
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 * @throws RTSException
	 */
	protected List getUserByEmailAddr(String asEmailAddr)
		throws NamingException, RTSException
	{
		if (asEmailAddr == null)
		{
			throw new RTSException("150", new Exception(
				"Parameter asEmailAddr may not be null"));
		}

		return this.search("mail=" + asEmailAddr);
	}

	/**
	 * Get Active Directory user(s) for a given first name (givenName)
	 * and last name (sn).
	 * 
	 * @param asFirstName
	 * @param asLastName
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 * @throws RTSException
	 */
	protected List getUserByFirstLastName(String asFirstName,
		String asLastName) throws NamingException, RTSException
	{
		if (asLastName == null)
		{
			throw new RTSException("150", new Exception(
				"Parameter asLastName may not be null"));
		}

		String lsSearchFilter = "sn=" + asLastName;

		if (asFirstName != null && asFirstName.length() > 0)
		{
			lsSearchFilter =
				lsSearchFilter + ", givenName=" + asFirstName;
		}

		return this.search(lsSearchFilter);
	}

	/**
	 * Perform LDAP Active Directory search for the given attributes.
	 * 
	 * @param asFilter
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 */
	private List search(String asFilter) throws NamingException
	{
		SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		DirContext laCtx = null;
		NamingEnumeration laNamingEnum = null;

		try
		{
			// bind with proxy admin credentials
			laCtx = this.bind();

			// perform search for given attributes
			laNamingEnum = laCtx.search(NAME, asFilter, searchCtls);
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

		List laUsers = new ArrayList();

		if (laNamingEnum != null && laNamingEnum.hasMoreElements())
		{
			Attributes laAttrs = null;

			// iterate through each user found, populate the
			// RtsSecurityUser object and put into the laUsers List
			// object
			while (laNamingEnum.hasMoreElements())
			{
				laAttrs =
					((SearchResult) laNamingEnum.nextElement())
						.getAttributes();
				laUsers.add(this.populateRtsUser(laAttrs));
			}
		}

		return laUsers;
	}

	/**
	 * If eDir data exists for the corresponding property in the
	 * RtsSecurityUser object, populated it.
	 * 
	 * @param aaAttrs
	 * @return RtsSecurityUser object
	 */
	private RtsSecurityUser populateRtsUser(Attributes aaAttrs)
	{
		RtsSecurityUser laUser = new RtsSecurityUser();

		if (aaAttrs.get("cn") != null
			&& aaAttrs.get("cn").toString().length() > 4)
		{
			laUser.setCommonName(aaAttrs.get("cn").toString()
				.substring(4));
			laUser.setUserId(aaAttrs.get("cn").toString().substring(4));
		}

		if (aaAttrs.get("mail") != null
			&& aaAttrs.get("mail").toString().length() > 6)
		{
			laUser.setEmailAddr(aaAttrs.get("mail").toString()
				.substring(6));
		}

		if (aaAttrs.get("givenName") != null
			&& aaAttrs.get("givenName").toString().length() > 11)
		{
			laUser.setFirstName(aaAttrs.get("givenName").toString()
				.substring(11));
		}

		if (aaAttrs.get("sn") != null
			&& aaAttrs.get("sn").toString().length() > 4)
		{
			laUser.setLastName(aaAttrs.get("sn").toString()
				.substring(4));
		}

		if (aaAttrs.get("telephoneNumber") != null
			&& aaAttrs.get("telephoneNumber").toString().length() > 17)
		{
			laUser.setPhoneNbr(aaAttrs.get("telephoneNumber")
				.toString().substring(17));
		}

		return laUser;
	}

	/**
	 * Perform LDAP Active Directory Bind for proxy admin user.
	 * 
	 * @return DirContext
	 * @throws NamingException
	 */
	private DirContext bind() throws NamingException
	{
		DirContext laCtx = null;

		Hashtable laEnv = new Hashtable();
		String lsADString = csProxyUserId + "@dot.state.tx.us";
		laEnv.put(Context.INITIAL_CONTEXT_FACTORY, INIT_CTX_FACTORY);
		laEnv.put(Context.PROVIDER_URL, csADProviderUrl);
		laEnv.put(Context.SECURITY_PRINCIPAL, lsADString);
		laEnv.put(Context.SECURITY_CREDENTIALS, csProxyPassword);

		laCtx = new InitialDirContext(laEnv);

		return laCtx;
	}

	public static void main(String[] aaArgs)
	{
		if (aaArgs.length > 0)
		{
			try
			{
				// initialize SystemProperty to load eDirectory user id
				// password and server
				com.txdot.isd.rts.services.communication.Comm
					.setIsServer(true);
				SystemProperty.initialize();

				RtsLDAPADSearch laTest = new RtsLDAPADSearch();

				if (aaArgs.length > 0 && aaArgs[0] != null)
				{
					// List laTestData = laTest
					// .getUserByCommonName(aaArgs[0]);
					// List laTestData = laTest
					// .getUserByEmailAddr(aaArgs[0]);
					// List laTestData = laTest.getUserByFirstLastName(
					// aaArgs[0], aaArgs[1]);
					List laTestData =
						laTest.getUserByFirstLastName(null, aaArgs[0]);
					// List laTestData =
					// laTest.getUserByUserId(aaArgs[0]);
					Iterator laTestIterator = laTestData.iterator();
					while (laTestIterator.hasNext())
					{
						laTest
							.printAttributes((RtsSecurityUser) laTestIterator
								.next());
					}
				}
			}
			catch (RTSException rtsEx)
			{
				rtsEx.printStackTrace();
			}
			catch (Exception aeEx)
			{
				aeEx.printStackTrace();
			}
		}
		else
		{
			System.out.println("No input for search");
		}
	}

	/**
	 * Print out the selected attributes of a user.
	 * 
	 * @param securityUser
	 */
	private void printAttributes(RtsSecurityUser securityUser)
	{
		System.out.println("   UserId: " + securityUser.getUserId());
		System.out.println(" LastName: " + securityUser.getLastName());
		System.out.println("FirstName: " + securityUser.getFirstName());
		System.out.println("    EMail: " + securityUser.getEmailAddr());
		System.out.println("    Phone: " + securityUser.getPhoneNbr());
		System.out.println("       CN: " + securityUser.getCommonName());
		System.out.println("-----------------------------------");
	}
}
