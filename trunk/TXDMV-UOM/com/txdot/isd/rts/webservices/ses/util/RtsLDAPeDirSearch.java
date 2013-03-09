package com.txdot.isd.rts.webservices.ses.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 * RtsLDAPSearchUtil.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Pilon		05/16/2011	Class Created.
 * 							defect 10670 Ver 6.7.1
 * R Pilon		08/03/2011	Refactor class.
 * 							modify getUserByCommonName(), bind() 
 * 							added getUserByUserId(),
 * 							  getUserByEmailAddr(),
 * 							  getUserByFirstLastName(),
 * 							  populateRtsUser(),
 * 							  printAttributes()
 * 							deleted getUserByTxdotAlias(), 
 * 							  getUserIdByCommonName(), 
 * 							  getUserIdByEmailAddr(), 
 * 							  getUserIdByFirstLastName(),
 * 							  searchByEmailAddr(), getUser(), 
 * 							  searchByFirstLastName(), getUserId()
 * 							defect 10718 Ver 6.8.1
 * R Pilon		08/31/2011	Changed all uses of txdot-alias to use
 * 							txdot-v21alias.  Added creatorsName attribute
 * 							to filter for all searches.
 * 							modify getUserByUserId(), getUserByUserId(), 
 * 							  getuserByEmailAddr(), getuserByFirstLastName(),
 * 							  populateRtsUser()
 * 							defect 10980 Ver 6.8.1
 * ---------------------------------------------------------------------
 */

/**
 * This class provides an interface for searching eDirectory.
 * 
 * @version 6.8.1 			08/03/2011
 * @author 	RPILON-C 
 * <br>Creation Date: 		05/16/2011 14:00:00
 */
public class RtsLDAPeDirSearch
{
	private final static String INIT_CTX_FACTORY =
		"com.sun.jndi.ldap.LdapCtxFactory";
	private String csEDirProviderUrl = SystemProperty.geteDirServer();
	private String csProxyUserId = SystemProperty.getEDirUserId();
	private String csProxyPassword = SystemProperty.getEDirPassword();
	private final static String NAME = "ou=Active,o=Lookup";

	/**
	 * Get eDirectory user(s) for a given common name (cn).
	 * 
	 * @param asCn
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 * @throws RTSException
	 */
	protected List getUserByCommonName(String asCn)
		throws NamingException, RTSException
	{
		if (asCn == null || asCn.length() < 1)
		{
			throw new RTSException("150", new Exception(
				"Parameter asCn must be populated"));
		}

		// instantiate with 'true' to make searches case insensitive
		Attributes laAttrs = new BasicAttributes(true);

		// set attribute to search for CN = asCn
		laAttrs.put(new BasicAttribute("cn", asCn));

		// defect 10980
		// set attribute to search for eDir accounts created by the
        // RSPSadmin service
		laAttrs.put(new BasicAttribute("creatorsName",
			"cn=RSPSadmin,o=support"));
		// end defect 10980
		
		return this.search(laAttrs);
	}

	/**
	 * Get eDirectory user(s) for a given user id (txdot-v21alias).
	 * 
	 * @param asUserId
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 * @throws RTSException
	 */
	protected List getUserByUserId(String asUserId)
		throws NamingException, RTSException
	{
		if (asUserId == null || asUserId.length() < 1)
		{
			throw new RTSException("150", new Exception(
				"Parameter asUserId must be populated"));
		}

		// instantiate with 'true' to make searches case insensitive
		Attributes laAttrs = new BasicAttributes(true);

		// defect 10980
		// set attribute to search for txdot-v21alias = asUserId
		laAttrs.put(new BasicAttribute("txdot-v21alias", asUserId));
		// end defect 10980

		// defect 10980
		// set attribute to search for eDir accounts created by the
        // RSPSadmin service
		laAttrs.put(new BasicAttribute("creatorsName",
			"cn=RSPSadmin,o=support"));
		// end defect 10980

		return this.search(laAttrs);
	}

	/**
	 * Get eDirectory user(s) for a given email address.
	 * 
	 * @param asEmailAddr
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 * @throws RTSException
	 */
	protected List getUserByEmailAddr(String asEmailAddr)
		throws NamingException, RTSException
	{
		if (asEmailAddr == null || asEmailAddr.length() < 1)
		{
			throw new RTSException("150", new Exception(
				"Parameter asEmailAddr must be populated"));
		}

		// instantiate with 'true' to make searches case insensitive
		Attributes laAttrs = new BasicAttributes(true);

		// set attribute to search for email address = asEmailAddr
		laAttrs.put(new BasicAttribute("mail", asEmailAddr));

		// defect 10980
		// set attribute to search for eDir accounts created by the
        // RSPSadmin service
		laAttrs.put(new BasicAttribute("creatorsName",
			"cn=RSPSadmin,o=support"));
		// end defect 10980

		return this.search(laAttrs);
	}

	/**
	 * Get eDirectory user(s) for a given first name (givenName) and
	 * last name (sn).
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
		if (asLastName == null || asLastName.length() < 1)
		{
			throw new RTSException("150", new Exception(
				"Parameter asLastName must be populated"));
		}

		// instantiate with 'true' to make searches case insensitive
		Attributes laAttrs = new BasicAttributes(true);

		if (asFirstName != null && asFirstName.length() > 0)
		{
			// set attribute to search for first name = asFirstName
			laAttrs.put(new BasicAttribute("givenName", asFirstName));
		}

		// set attribute to search for last name = asLastName
		laAttrs.put(new BasicAttribute("sn", asLastName));

		// defect 10980
		// set attribute to search for eDir accounts created by the
        // RSPSadmin service
		laAttrs.put(new BasicAttribute("creatorsName",
			"cn=RSPSadmin,o=support"));
		// end defect 10980

		return this.search(laAttrs);
	}

	/**
	 * Perform LDAP eDirectory search for the given attributes.
	 * 
	 * @param aaAttrs
	 * @return List of RtsSecurityUser objects
	 * @throws NamingException
	 */
	private List search(Attributes aaAttrs) throws NamingException
	{
		DirContext laCtx = null;
		NamingEnumeration laNamingEnum = null;

		try
		{
			// bind with proxy admin credentials
			laCtx = this.bind();

			// perform search for given attributes
			laNamingEnum = laCtx.search(NAME, aaAttrs);
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
			while (laNamingEnum.hasMore())
			{
				laAttrs =
					((SearchResult) laNamingEnum.next())
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

		// defect 10980
		if (aaAttrs.get("txdot-v21alias") != null
			&& aaAttrs.get("txdot-v21alias").toString().length() > 16)
		{
			laUser.setUserId(aaAttrs.get("txdot-v21alias").toString()
				.substring(16));
		}
		// end defect 10980

		return laUser;
	}

	/**
	 * Perform LDAP eDirectory Bind for proxy admin user.
	 * 
	 * @return DirContext
	 * @throws NamingException
	 */
	private DirContext bind() throws NamingException
	{
		DirContext laCtx = null;

		Hashtable laEnv = new Hashtable();
		String lseDirString = "cn=" + csProxyUserId + ",o=support";
		laEnv.put(Context.INITIAL_CONTEXT_FACTORY, INIT_CTX_FACTORY);
		laEnv.put(Context.PROVIDER_URL, csEDirProviderUrl);
		laEnv.put(Context.SECURITY_PRINCIPAL, lseDirString);
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

				RtsLDAPeDirSearch laTest = new RtsLDAPeDirSearch();

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
