package com.txdot.isd.rts.services.localoptions;

import java.util.Properties;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.data.WorkstationInfo;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 * 
 * JniAdInterface.java
 * 
 * (c) Texas Department of Transportation  2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	10/15/2003	new class
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/03/2004	Return invalid user (54) if Active 
 *							Directory rejected the User Name.
 *							Clean up of adAddUser to pass aData 
 *							instead of data.
 *							modify adAddUser()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/04/2004	Set up error numbers to reflect what
 *							the actually errors are.
 *							modify adAddUser(), adDelUser(),
 *								adResetPassword(), adUpdtUser()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/18/2004	reflow for updated java standards
 *							Pass ofcissuanceno into isRtsUserName for
 *							server side check.
 *							modify isRtsUserName(), adAddUser()
 *								adDelUser(), adUpdtUser() 
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/18/2004	return 751 if not an acceptable username.
 *							modify adAddUser()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/20/2004	Do not replace RTSExceptions with 752.
 *							Do not attempt to load the dll on server
 *							side.
 *							modify adAddUser(), adDelUser(), 
 *							adUpdtUser()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/23/2004	Use UserName Min Length for comparision
 *							before attempting to do substring in check
 *							to see if UserName is and RTS User.
 *							modify isRtsUserName()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/24/2004	Log an error when using a production ou
 *							in test mode.
 *							modify determineAdOu()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/27/2004	Check for null first on isRtsUserName
 *							modify isRtsUserName()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	03/05/2004	Add check to ensure that we are using the
 *							AdOu property in test mode.  Exit if not
 *							the property is not set.
 *							modify determineAdOu()
 *							defect 6445 Ver 5.1.6
 * K Harrell	03/08/2004	New implementation for username increment
 *							modify adAddUser()
 *							deprecate incrementUserName(String)
 *							add incrementUserName(String,int)
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	04/15/2004	Do not do an add if the county 
 *							administrator provided the UserName
 *							modified adAddUser()
 *							defect 7007 Ver 5.1.6
 * Ray Rowehl	06/23/2004	Found that if the dll gets an error, it
 *							returns a null.  Need to throw an exception
 *							in this case.  Using error 54.
 *							modify getWindowsUserName(int)
 *							defect 7232 Ver 5.2.1
 * Ray Rowehl	09/20/2004	Update the javadoc on delUser() to reflect
 *							how the dll side of this interface works.
 *							modify delUser()
 *							defect none Ver 5.2.1
 * Min Wang		11/22/2004	Allow Search by User Name to start to add 
 *							new user to Active Directory.
 *							add validateUserProvidedUserName()
 *							modify adAddUser()
 *							defect 7463 Ver 5.2.2
 * Ray Rowehl	06/11/2005	Code cleanup for RTS 5.2.3
 * 							organize imports, format source,
 * 							rename fields, change exception to throwable
 * 							defect 7891 Ver 5.2.3
 * Min Wang		06/16/2005	Add a check to prevent incrementing the 
 * 							UserName if the Security Administrator 
 * 							was searching by UserName. This allows the
 * 							Non-Publishing counties to add users 
 * 							to substations where the USer is already in 
 * 							Active Directory.
 * 							modify adAddUser()
 * 							defect 8233 Ver 5.2.2 Fix 5
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3    
 * Jeff S.		06/28/2006	Added a method used to call a native method 
 * 							to fix the focus problem with the JRE.
 * 							Sun Internal Review ID (704517).
 * 							add RUNNING_FOCUS_FIX
 * 							add fixRTSFocus(), focusFix()
 * 							defect 8756 Ver 5.2.3   
 * Jeff S.		07/20/2006	Added a method used to call a native method 
 * 							to fix the focus problem with the JRE.  This 
 * 							fixes the problem when they cancel from one
 * 							JDialog to another JDialog.
 * 							Sun Internal Review ID (704517).
 * 							add RUNNING_FOCUS_FIX2
 * 							add fixRTSFocus2(String), focusFix2(String)
 * 							defect 8756 Ver 5.2.3 
 * Min Wang		02/02/2009  Retry call to Windows to look up the 
 * 							User Name if the first result is null.  
 *							If the result is still null on the 
 *							second try, get the User Name from Java.
 *            				modify getWindowsUserName()
 *                          defect 9925 Ver Defect_POS_D
 * Ray Rowehl	10/19/2009	Add logging to see how long it is taking to
 * 							get the user name from windows.
 * 							modify getWindowsUserName()
 * 							defect 10255 Ver Defect_POS_G
 * ---------------------------------------------------------------------
 */

/**
 * This class provides the JNI Interface to call the 
 * AD User Maintainence routines.
 * These routines are stored in the RTSJniAdInterface.dll.
 * <p>
 * The routines are:
 * <ul>
 * <li>addUser - Adds a user to the Directory
 * <li>delUser - Deletes a user from the Directory
 * <li>getWindowsUserName - Returns a string containing the User Name.
 * This is the User logged into Windows.
 * <li>resetPassword - Resets the User's Password in the Directory.
 * <li>updtUser - Updates the User's Information in the Directory
 * <li>fixRTSFocus - Calls a C++ function that fixes the focus problem
 * <eul>
 *
 * <p>
 * This class also contains some helper routines.  They are:
 * <ul>
 * <li>determineAdOu - Determines if there is a testing OU and returns 
 * it.
 * <li>determineUserType - Determines if User is regular or 
 * administration.
 * <li>incrementUserName - Increments the User Name.
 * <li>isRTSUserName - Determines if User Name is in RTS Format.
 * <li>focusFix - Calls Native method fixRTSFocus and handles logging
 * <eul>
 *
 * <p>
 * The Java Native Interface is used to interface between the Java
 * Application and the C++ programs that interface to Active Directory
 * (Directory).
 * <p>
 * System.loadLibrary is used to load the dll (RTSJniAdInterface.dll).
 * The loadLibrary is loaded in static.  The first time it is 
 * referenced, it is loaded.  The dll will not be unloaded until the 
 * jvm is terminated.
 *
 * @version Defect_POS_G		10/19/2009
 * @author	Ray Rowehl
 * <br>Creation Date:			10/15/2003 10:30:59
 */

public class JniAdInterface
{
	// Load the library
	static {
		try
		{
			// do not attempt to load the dll on server side
			if (!Comm.isServer())
			{
				if (SystemProperty.getProdStatus() != 0)
				{
					System.out.println("loading dll");
				}

				System.loadLibrary("RTSJniAdInterface");

				if (SystemProperty.getProdStatus() != 0)
				{
					System.out.println("loaded dll");
				}
			}
		}
		catch (Throwable aeThrowable)
		{
			Log.write(
				Log.SQL_EXCP,
				aeThrowable,
				" Could not load the JNI Interface DLL");
			System.out.println("Link Error");
		}
	}
	/**
	 * Normal Return Code (0).
	 */
	public final static int NORMALRETURNCODE = 0;
	/**
	 * User Name already exists, it can not be added (1).
	 */
	public final static int DUPUSERNAME = 1;
	/**
	 * Non Standard UserName, add was rejected (2).
	 * <br>
	 * User Name must be of the form First Initial and Last Name
	 * up to a length of 7.  
	 * Numbers are used to make the User Name unique.
	 */
	public final static int NONSTANDARDUSERNAME = 2;
	/**
	 * User Name not found (3).
	 */
	public final static int USERNAMENOTFOUND = 3;
	/**
	 * User Name was incremented to make it unique (4).
	 */
	public final static int USERNAMEINCREMENTED = 4;
	/**
	 * Directory Problem, there was an unexpected error (99).
	 */
	public final static int DIRECTORYPROBLEM = 99;
	/**
	 * User Type ADMIN
	 */
	public final static String UTADMIN = "ADMIN";
	/**
	 * User Type NORMAL
	 */
	public final static String UTNORMAL = "NORMAL";

	/**
	 * Test Get User Name (1)
	 */
	public final static int GETUSERNAME = 1;

	/**
	 * Test Delete User Name (2)
	 */
	public final static int DELETEUSERNAME = 2;

	/**
	 * Test Add User Name (3)
	 */
	public final static int ADDUSERNAME = 3;

	/**
	 * Test Update User Name (4)
	 */
	public final static int UPDATEUSERNAME = 4;

	/**
	 * Test Reset the User's Password (5)
	 */
	public final static int RESETPASSWORD = 5;

	// defect 8756
	private final static String RUNNING_FOCUS_FIX =
		"Running JNI fixRTSFocus.";

	private final static String RUNNING_FOCUS_FIX2 =
		"Running JNI fixRTSFocus - ";
	// end defect 8756

	/**
	 * Add a User to the Directory via the ad interface.
	 * If the User Id is not unique, increment and try again.
	 * 
	 * @return int - Success Indicator
	 * @param  aaData - Object (SecurityData)
	 * @throws RTSException
	 */
	public static int adAddUser(Object aaData) throws RTSException
	{
		int liReturnCode = JniAdInterface.DUPUSERNAME;
		int liDupRetry = 0;
		// switch to determine if the user id was changed.
		boolean lbChangedUser = false;
		try
		{
			// cast the security data object
			SecurityData laSecData = (SecurityData) aaData;
			// make sure middle initial is populated with something
			if (laSecData.getEmpMI().length() < 1)
			{
				laSecData.setEmpMI(" ");
			}

			// defect 7007
			// only do ad interface to add a user if it is an rts user
			// do not go here for "ddo" users.
			// Also make sure we did not provide the UserName.  This prevents
			// the administrator from creating their own UserName.
			if (JniAdInterface
				.isRtsUserName(
					laSecData.getUserName(),
					laSecData.getOfcIssuanceNo())
				//&& !(lSecData.isUserProvidedUserName() 
				&& UtilityMethods.getOfficeCode(
					laSecData.getOfcIssuanceNo())
					== 3)
			{
				// defect 7463
				// verify username provided is correct
				if (laSecData.isUserProvidedUserName())
				{
					validateUserProvidedUserName(laSecData);
				}
				// end defect 7463

				// end defect 7007
				String lsOfcName =
					JniAdInterface.determineAdOu(
						laSecData.getOfcIssuanceNo());
				String lsUserName = laSecData.getUserName().trim();
				String lsBaseUserName = lsUserName;
				// setup User Type
				String lsUserType =
					JniAdInterface.determineUserType(
						laSecData.getEmpSecrtyAccs());
				// Attempt to add user to Active Directory
				// If the user is a dup of an existing username,
				// increment and try again.
				while (liReturnCode == JniAdInterface.DUPUSERNAME)
				{
					if (SystemProperty.getProdStatus() != 0)
					{
						System.out.println(
							"Attempting add for "
								+ lsOfcName
								+ " "
								+ lsUserName
								+ " "
								+ lsUserType
								+ " "
								+ laSecData.getEmpLastName()
								+ " "
								+ laSecData.getEmpFirstName()
								+ " "
								+ laSecData.getEmpMI()
								+ " "
								+ laSecData.getEmpId()
								+ " ");
					}
					// Call the ad add user routine
					liReturnCode =
						JniAdInterface.addUser(
							lsOfcName,
							lsUserName,
							lsUserType,
							laSecData.getEmpLastName(),
							laSecData.getEmpFirstName(),
							laSecData.getEmpMI(),
							laSecData.getEmpId());
					// defect 8233
					if (liReturnCode == JniAdInterface.DUPUSERNAME
						&& laSecData.isUserProvidedUserName())
					{
						liReturnCode = JniAdInterface.NORMALRETURNCODE;
					}
					// end defect 8233	
					// add debug statement
					if (SystemProperty.getProdStatus() != 0)
					{
						System.out.println(
							"Ad Add return code is " + liReturnCode);
					}
					// check for invalid User Name format
					if (liReturnCode
						== JniAdInterface.NONSTANDARDUSERNAME)
					{
						if (SystemProperty.getProdStatus() != 0)
						{
							System.out.println(
								"This user name was "
									+ "rejected by Active Directory");
						}
						Log.write(
							Log.SQL_EXCP,
							laSecData,
							"User Name "
								+ "was rejected by Active Directory");
						throw new RTSException(751);
					}
					if (liReturnCode == JniAdInterface.DUPUSERNAME)
					{
						liDupRetry = liDupRetry + 1;
						// Handling of incrementing UserName here
						//
						// lsUserName = 
						//     JniAdInterface.incrementUserName(lsUserName);
						lsUserName =
							JniAdInterface.incrementUserName(
								lsBaseUserName,
								liDupRetry);
						// we did change the user id!
						lbChangedUser = true;
					}
				}
				if (lbChangedUser)
				{
					// set return code to reflect that user id was 
					// incremented
					liReturnCode = JniAdInterface.USERNAMEINCREMENTED;
					// set the security object to have the new user id
					laSecData.setUserName(lsUserName);
				}
			}
			else
			{
				// just return the normal return code if this is not an 
				// rts user id
				liReturnCode = JniAdInterface.NORMALRETURNCODE;
			}
			return liReturnCode;
		}
		catch (RTSException aeRTSEx)
		{
			// There was a problem during cache processing
			if (SystemProperty.getProdStatus() != 0)
			{
				System.out.println(
					" Error while attempting to add a user");
				System.out.println(aeRTSEx);
			}
			Log.write(
				Log.SQL_EXCP,
				aeRTSEx,
				" Error while attempting to" + " add a user");
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw aeRTSEx;
		}
		// catch link error related to dll and logout.
		catch (UnsatisfiedLinkError aeULE)
		{
			Log.write(
				Log.SQL_EXCP,
				aeULE,
				" Error while attempting add "
					+ "a user, AD Interface Problem"
					+ aeULE);
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw new RTSException(750);
		}
		catch (Throwable aeThrowable)
		{
			// Probably a problem while attempting the AD Interface routine
			Log.write(
				Log.SQL_EXCP,
				aeThrowable,
				" Error while attempting add a "
					+ "user, AD Interface Problem"
					+ aeThrowable);
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw new RTSException(752);
		}
	}
	/**
	 * Delete a User on the Directory via the ad interface.
	 * <p>
	 * The delete is done by passing the office name and UserName.
	 * <ol>
	 * <li>If the UserName under RTS Control, continue.
	 * <li>If there a systemproperty ou definition, use it to look up 
	 * UserName.
	 * <li>Otherwise, use office name as the ou to look up UserName.
	 * <eol>
	 * <br>If the UserName is not under RTS Control, 
	 * just return normal return code.
	 * 
	 * @return int - Return Code
	 * @param aaData - Object (SecurityData)
	 * @throws RTSException 
	 */
	public static int adDelUser(Object aaData) throws RTSException
	{
		int liReturnCode = JniAdInterface.DIRECTORYPROBLEM;

		try
		{
			// cast the security data object
			SecurityData laSecData = (SecurityData) aaData;

			// only do the reset if the user is in the RTS Group
			if (JniAdInterface
				.isRtsUserName(
					laSecData.getUserName(),
					laSecData.getOfcIssuanceNo()))
			{
				String lsOfcName =
					JniAdInterface.determineAdOu(
						laSecData.getOfcIssuanceNo());

				// print the attempted call if not production
				if (SystemProperty.getProdStatus() != 0)
				{
					System.out.println(
						"Attempting AD Delete for "
							+ lsOfcName
							+ " "
							+ laSecData.getUserName());
				}
				// Call the ad delete user routine
				liReturnCode =
					JniAdInterface.delUser(
						lsOfcName,
						laSecData.getUserName());

				// print the results of the attempted call if not production
				if (SystemProperty.getProdStatus() != 0)
				{
					System.out.println(
						"Result of AD Delete is " + liReturnCode);
				}

			}
			else
			{
				liReturnCode = JniAdInterface.NORMALRETURNCODE;
			}

			// caller will determine if an exception is needed.
			return liReturnCode;
		}
		catch (RTSException aeRTSEx)
		{
			// There was a problem during cache processing
			Log.write(
				Log.SQL_EXCP,
				aeRTSEx,
				" Error while attempting to" + " delete a user");
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw aeRTSEx;
		}
		// catch link error related to dll and logout.
		catch (UnsatisfiedLinkError aeULE)
		{
			Log.write(
				Log.SQL_EXCP,
				aeULE,
				" Error while attempting delete"
					+ " a user, AD Interface Problem"
					+ aeULE);
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw new RTSException(750);
		}
		catch (Throwable aeThrowable)
		{
			// Probably a problem while attempting the AD Interface routine
			Log.write(
				Log.SQL_EXCP,
				aeThrowable,
				" Error while attempting delete "
					+ "a user, AD Interface Problem"
					+ aeThrowable);
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw new RTSException(752);
		}
	}
	/**
	 * This is the interface to the native C++ method 
	 * to call addUser routine.
	 * Add User to Active Directory.
	 * 
	 * @return int - Return code from call
	 * @param  asOfcName   - String - Name of the office for this user.
	 * @param  asUserName - String - Active Directory User Id for User.
	 * @param  asUserType  - String - User Type.  Admin or Normal.
	 * @param  asLastName  - String - Last Name of User
	 * @param  asFirstName - String - First Name of User
	 * @param  asMidInt    - String - Middle Initial of User
	 * @param  asNickName  - String - EmpId of User
	 * @throws Exception
	 */
	public static native int addUser(
		String asOfcName,
		String asUserName,
		String asUserType,
		String asLastName,
		String asFirstName,
		String asMidInt,
		String asNickName)
		throws Exception;
	/**
	 * Reset the user's password via the Directory interface.
	 * <p>
	 * The password reset is done by passing the office name and UserName.
	 * <ol>
	 * <li>If the UserName under RTS Control, continue.
	 * <li>If there a systemproperty ou definition, use it to look up 
	 * UserName.
	 * <li>Otherwise, use office name as the ou to look up UserName.
	 * <eol>
	 * If the UserName is not under RTS Control, 
	 * just return normal return code.
	 * 
	 * @return int - Return Code
	 * @param aaData - Object (SecurityData)
	 */
	public static int adResetPassword(Object aaData)
	
	{
		int liReturnCode = JniAdInterface.DIRECTORYPROBLEM;

		try
		{
			// cast the security data object
			SecurityData laSecData = (SecurityData) aaData;

			// only do the reset if the UserName is in the RTS Group
			if (JniAdInterface
				.isRtsUserName(
					laSecData.getUserName(),
					laSecData.getOfcIssuanceNo()))
			{
				String lsOfcName =
					JniAdInterface.determineAdOu(
						laSecData.getOfcIssuanceNo());

				// print the attempted call if not production
				if (SystemProperty.getProdStatus() != 0)
				{
					System.out.println(
						"Attempting AD Password Reset for "
							+ lsOfcName
							+ " "
							+ laSecData.getUserName());
				}
				// Call the ad password reset routine
				liReturnCode =
					JniAdInterface.resetPassword(
						lsOfcName,
						laSecData.getUserName());
			}
			else
			{
				liReturnCode = JniAdInterface.NORMALRETURNCODE;
			}
		}
		catch (RTSException aeRTSEx)
		{
			// There was a problem during cache processing
			Log.write(
				Log.SQL_EXCP,
				aeRTSEx,
				" Error while attempting reset" + " of password");
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
		}
		// catch link error related to dll and logout.
		catch (UnsatisfiedLinkError aeULE)
		{
			RTSException exception = new RTSException(750);
			exception.displayError((javax.swing.JFrame) null);
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
		}
		catch (Throwable aeThrowable)
		{
			// Probably a problem while attempting the AD Interface routine
			Log.write(
				Log.SQL_EXCP,
				aeThrowable,
				" Error while attempting reset"
					+ " of password, AD Interface Problem"
					+ aeThrowable);
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
		}
		return liReturnCode;
	}
	/**
	 * Update a UserName on the Directory via the ad interface.
	 * <p>
	 * Where the UserName update occurs depends on the type of user.
	 * <ol>
	 * <li>If the UserName under RTS Control, continue.
	 * <li>If there a systemproperty ou definition, use it to look up 
	 * UserName.
	 * <li>Otherwise, use office name as the ou to look up UserName.
	 * <eol>
	 * If the UserName is not under RTS Control, 
	 * just return normal return code.
	 * <p>
	 * Creation date: (10/20/2003 7:56:44 AM)
	 *
	 * @return int - Return Code
	 * @param aaData - Object (SecurityData)
	 * @throws RTSException
	 */
	public static int adUpdtUser(Object aaData) throws RTSException
	{
		int liReturnCode = JniAdInterface.DIRECTORYPROBLEM;

		try
		{
			// cast the security data object
			SecurityData laSecData = (SecurityData) aaData;

			// only do the reset if the UserName is in the RTS Group
			if (JniAdInterface
				.isRtsUserName(
					laSecData.getUserName(),
					laSecData.getOfcIssuanceNo()))
			{
				String lsOfcName =
					JniAdInterface.determineAdOu(
						laSecData.getOfcIssuanceNo());

				// setup the string for User Type
				String lsUserType =
					JniAdInterface.determineUserType(
						laSecData.getEmpSecrtyAccs());

				// make sure middle initial has something
				if (laSecData.getEmpMI().length() < 1)
				{
					laSecData.setEmpMI(" ");
				}

				// print the attempted call if not production
				if (SystemProperty.getProdStatus() != 0)
				{
					System.out.println(
						"Attempting AD Update for "
							+ lsOfcName
							+ " "
							+ laSecData.getUserName()
							+ " "
							+ lsUserType
							+ " "
							+ laSecData.getEmpLastName()
							+ " "
							+ laSecData.getEmpFirstName()
							+ " "
							+ laSecData.getEmpMI()
							+ " "
							+ laSecData.getEmpId());
				}

				// Call the ad update UserName routine
				liReturnCode =
					JniAdInterface.updtUser(
						lsOfcName,
						laSecData.getUserName(),
						lsUserType,
						laSecData.getEmpLastName(),
						laSecData.getEmpFirstName(),
						laSecData.getEmpMI(),
						laSecData.getEmpId());
			}
			else
			{
				// if we did not call the interface, return a good status
				liReturnCode = JniAdInterface.NORMALRETURNCODE;
			}

			return liReturnCode;
		}
		catch (RTSException aeRTSEx)
		{
			// There was a problem during cache processing
			Log.write(
				Log.SQL_EXCP,
				aeRTSEx,
				" Error while attempting to" + " update a UserName");
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw aeRTSEx;
		}
		// catch link error related to dll and logout.
		catch (UnsatisfiedLinkError aeULE)
		{
			// There was a problem during cache processing
			Log.write(
				Log.SQL_EXCP,
				aeULE,
				" Error while attempting to " + "update a UserName");
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw new RTSException(750);
		}
		catch (Throwable aeThrowable)
		{
			// Probably a problem while attempting the AD Interface routine
			Log.write(
				Log.SQL_EXCP,
				aeThrowable,
				" Error while attempting update"
					+ " a UserName, AD Interface Problem"
					+ aeThrowable);
			liReturnCode = JniAdInterface.DIRECTORYPROBLEM;
			throw new RTSException(752);
		}

	}
	/**
	 * This is the interface to the native C++ method 
	 * to call delUser routine.
	 * Delete the User's Entry from Active Directory.
	 *
	 * <p>The dll concatenates the Office Name and the UserName
	 * together for the Delete.
	 * A return code of NotFound (3) is given if the combination is
	 * not found.  This means you can not change the AdOu in hopes
	 * of forcing an AD Error.  It will just return a NotFound.
	 * Testing of AD Errors for this method will require unplugging
	 * the network.
	 *
	 * @return int - Return code from call
	 * @param  asOfcName   - String - Name of the office for this user.
	 * @param  asUserName  - String - Active Directory User Id for User.
	 */
	public static native int delUser(
		String asOfcName,
		String asUserName)
		throws Exception;
	/**
	 * Determine the Organizational Unit to use with Active Directory.
	 * 
	 * <p>NOTE! : If the we are running in Production mode and the 
	 * workstation is not a real RTS Workstation, we will do a 
	 * System.exit(16).  This is the force the clerk to call in 
	 * about their non-standard workstation.
	 * 
	 * @return java.lang.String
	 * @param aiOfcIssuanceNo - int - Office Number 
	 */
	public static String determineAdOu(int aiOfcIssuanceNo)
	
	{
		String lsOfcName = "";

		if (SystemProperty.getAdOu() != null)
		{
			// use the testing AD OU.
			lsOfcName = SystemProperty.getAdOu();
			System.out.println("Using Test OU AbstractProperty " + lsOfcName);
		}
		else
		{
			// get OfficeIds data
			OfficeIdsData lOfcId =
				(OfficeIdsData) OfficeIdsCache.getOfcId(
					aiOfcIssuanceNo);
			lsOfcName = lOfcId.getOfcName();

			// display message if prod ou in test mode
			if (!WorkstationInfo.isRTSWorkstation())
			{
				Log.write(
					Log.START_END,
					lsOfcName,
					"Workstation "
						+ WorkstationInfo.getWorkstationName()
						+ " does not match the County "
						+ lsOfcName);
				Log.write(
					Log.START_END,
					lsOfcName,
					"Exiting application");
				BatchLog.write(
					"Workstation "
						+ WorkstationInfo.getWorkstationName()
						+ " does not match the County "
						+ lsOfcName
						+ " Exiting");
				System.exit(16);
			}
		}
		return lsOfcName;
	}
	/**
	 * Determine User Type For Active Directory.
	 *  
	 * @return java.lang.String
	 * @param aiEmpSecrtyAccs int
	 */
	public static String determineUserType(int aiEmpSecrtyAccs)
	{
		// setup the string for User Type
		String lsUserType = "";
		if (aiEmpSecrtyAccs == 1)
		{
			// if the user has EmpSecrtyAccs, they are an admin 
			// for the county
			lsUserType = JniAdInterface.UTADMIN;
		}
		else
		{
			// if the user does not have EmpSecrtyAccs, they are not admin
			lsUserType = JniAdInterface.UTNORMAL;
		}

		return lsUserType;
	}

	/**
	 * JNI call to fix the focus problem.
	 * This is used when canceling back to the desktop.
	 * 
	 * @return int - Return code from call
	 */
	public static native int fixRTSFocus() throws Exception;

	/**
	 * JNI call to fix the focus problem.
	 * This is used when canceling from another dialog box.
	 * 
	 * @return int - Return code from call
	 */
	public static native int fixRTSFocus2(String asWindowTitle)
		throws Exception;

	/**
	 * Method used for the temporary work around to the focus problem.
	 */
	public static void focusFix()
	
	{
		try
		{
			if (SystemProperty.getProdStatus() != 0)
			{
				Log.write(Log.START_END, null, RUNNING_FOCUS_FIX);
			}
			JniAdInterface.fixRTSFocus();
		}
		catch (Throwable aeThrowable)
		{
			new RTSException(RTSException.JAVA_ERROR, aeThrowable);
		}
	}

	/**
	 * Method used for the temporary work around to the focus problem.
	 * This will fix the focus problem when canceling from another
	 * dialog back to the previos dialog.
	 * 
	 * @param asWindowTitle String
	 */
	public static void focusFix(String asWindowTitle)
	{
		try
		{
			if (SystemProperty.getProdStatus() != 0)
			{
				Log.write(
					Log.START_END,
					null,
					RUNNING_FOCUS_FIX2 + asWindowTitle);
			}
			int liReturn = JniAdInterface.fixRTSFocus2(asWindowTitle);
			if (SystemProperty.getProdStatus() != 0)
			{
				Log.write(
					Log.START_END,
					null,
					RUNNING_FOCUS_FIX2 + liReturn);
			}
		}
		catch (Throwable aeThrowable)
		{
			new RTSException(RTSException.JAVA_ERROR, aeThrowable);
		}
	}

	/**
	 * This is the interface to the native C++ method 
	 * to call getUser routine.
	 * A String of null means there was a problem on
	 * the windows side
	 * @return String - UserId aquired from Windows
	 */
	public static native String getUser() throws Exception;

	/**
	 * This method gets the UserName from the OS.
	 * This method has been replaced by a new method that
	 * calls the dll routine.
	 * 
	 * @return java.lang.String
	 * @deprecated
	 */
	public static String getWindowsUserName() throws RTSException
	{
		if (SystemProperty.getProdStatus() != 0)
		{
			Properties lProp = System.getProperties();
			System.out.println(lProp);
		}

		String lsUserName = System.getProperty("user.name");

		lsUserName =
			UtilityMethods.addPaddingRight(
				lsUserName,
				com
					.txdot
					.isd
					.rts
					.services
					.util
					.constants
					.LocalOptionConstant
					.USER_NAME_MAX_LENGTH,
				" ");

		if (SystemProperty.getProdStatus() != 0)
		{
			System.out.println(
				"old method for getting windows id was used");
			System.out.println(lsUserName);
		}

		return lsUserName;
	}
	/**
	 * This method gets the UserName from the OS through 
	 * the JniAdInterface.getUser() function.
	 * <p>
	 * The use of int is to just overload the method.  
	 * The int is not actually used.
	 *  
	 * @param  aiNotUsed  - int - Not used.
	 * @return String 
	 */
	public static String getWindowsUserName(int aiNotUsed)
		throws RTSException
	{
		// defect 10255
		Log.write(Log.SQL_EXCP, new JniAdInterface(), "Getting User Name");
		// end defect 10255
		
		String lsUserName = "";

		// this section provides for a copy of all the system properties
		if (SystemProperty.getProdStatus() != 0)
		{
			Properties lProp = System.getProperties();
			System.out.println(lProp);
			System.out.println(aiNotUsed);
		}

		try
		{
			//  Go get the id
			lsUserName = JniAdInterface.getUser();
			// defect 9925
			if (lsUserName == null)
			{
				Log.write(
					Log.SQL_EXCP,
					new RTSDate(),
					" Try to get the user name second time.");
				lsUserName = JniAdInterface.getUser();
			}
			if (lsUserName == null)
			{
				Log.write(
					Log.SQL_EXCP,
					new RTSDate(),
					" Try to get the user name from Java.");
				lsUserName = getWindowsUserName();
			}
			// end defect 9925
		}
		catch (UnsatisfiedLinkError aeULE)
		{
			Log.write(Log.SQL_EXCP, new RTSDate(), " Link error");
			throw new RTSException(54);
		}
		catch (Throwable aeThrowable)
		{
			Log.write(Log.SQL_EXCP, new RTSDate(), " General error");
			throw new RTSException(54);
		}

		if (lsUserName != null)
		{
			lsUserName =
				UtilityMethods.addPaddingRight(
					lsUserName,
					com
						.txdot
						.isd
						.rts
						.services
						.util
						.constants
						.LocalOptionConstant
						.USER_NAME_MAX_LENGTH,
					" ");
		}
		// defect 7232
		else
		{
			// if jni returns a null user name, throw an exception.
			Log.write(
				Log.SQL_EXCP,
				new RTSDate(),
				" JNI came back with a null user name!");
			throw new RTSException(54);
		}
		// end defect 7232

		// Show the userid on the console
		if (SystemProperty.getProdStatus() != 0)
		{
			System.out.println(lsUserName);
		}

		// defect 10255
		Log.write(Log.SQL_EXCP, new JniAdInterface(), 
			"Finished getting User Name " + lsUserName);
		// end defect 10255
		
		return lsUserName;
	}

	/**
	 * This method handles incrementing the Active Directory User Name.
	 *  
	 * @return java.lang.String
	 * @param asUserName - java.lang.String - User Name
	 * @param aiDupRetry - int - Dup Retry
	 */
	public static String incrementUserName(
		String asUserName,
		int aiDupRetry)
	{
		String lsUserName = asUserName.trim();
		int liCurrentUserNameLength = lsUserName.length();
		int liSuffixLength = String.valueOf(aiDupRetry).length();
		// Determine if have to cut off
		int liStripCharCount = 0;
		if (liCurrentUserNameLength + liSuffixLength
			> LocalOptionConstant.USER_NAME_MAX_LENGTH)
		{
			liStripCharCount =
				liCurrentUserNameLength
					+ liSuffixLength
					- LocalOptionConstant.USER_NAME_MAX_LENGTH;
		}
		String lsNewBase =
			lsUserName.substring(
				0,
				liCurrentUserNameLength - liStripCharCount);
		lsUserName = lsNewBase + aiDupRetry;

		if (SystemProperty.getProdStatus() != 0)
		{
			System.out.println(
				"Incremented User Id will be "
					+ lsUserName
					+ " was "
					+ asUserName);
			Log.write(
				Log.SQL_EXCP,
				asUserName,
				"Incremented User Id will be "
					+ lsUserName
					+ " was "
					+ asUserName);
		}
		return lsUserName;
	}
	/**
	 * This method determines if the user name has a Prefix or not.
	 * <p>
	 * If the user name has a prefix, it is part of the RTS area.
	 *  
	 * @return boolean
	 * @param asUserName - String - User Name
	 * @param aiOfcIssuanceNo - int - Office Issuance No
	 */
	static public boolean isRtsUserName(
		String asUserName,
		int aiOfcIssuanceNo)
	{
		boolean lbRC = false;

		// check to see if the user id has a prefix
		if (asUserName != null
			&& asUserName.length()
				>= LocalOptionConstant.USER_NAME_MIN_LENGTH
			&& asUserName.substring(0, 3).equals(
				UtilityMethods.addPadding(
					String.valueOf(aiOfcIssuanceNo),
					3,
					"0")))
		{
			lbRC = true;
		}
		return lbRC;
	}
	/**
	 * Stand alone testing of JNI Interface.
	 * 
	 * <p>This is the list of stand alone tests:
	 * <ul>
	 * <li>Get the current User ID (1)
	 * <li>Delete the Test User Id (2)
	 * <li>Add the Test User Id as a Normal User (3)
	 * <li>Update the Test User Id with Admin Access (4)
	 * <li>Reset the Password of the Test User Id (5)
	 * <li>Delete the Test User Id to clean out (2)
	 * <eul>
	 * 
	 * <p>The County Name is determined from the RTSCLS OU parameter.
	 * 
	 * <p>This is the list of possible test parameters:
	 * <ol>
	 * <li>TestCaseList	 - List of test cases to run (123542)
	 * <li>OfficeName	 - Passed from RTSCLS
	 * <li>UserName      - "257-RTSUSER"
	 * <li>UserType		 - "NORMAL"
	 * <li>LastName      - "TSUSER"
	 * <li>FirstName     - "RTS"
	 * <li>MiddleInitial - "X"
	 * <li>NickName      - "RTSTEST"
	 * <eol>
	 * 
	 * @param aaArgs java.lang.String[]
	 */
	public static void main(String[] aaArgs)
	{
		try
		{
			// Set up static test properties
			String lsTestCaseList =
				String.valueOf(GETUSERNAME)
					+ String.valueOf(DELETEUSERNAME)
					+ String.valueOf(ADDUSERNAME)
					+ String.valueOf(UPDATEUSERNAME)
					+ String.valueOf(RESETPASSWORD)
					+ String.valueOf(DELETEUSERNAME);

			String lsOfcName = SystemProperty.getAdOu();
			String lsUserName = "257-RTSUSER";
			String lsUserType = UTNORMAL;
			String lsLastName = "tsuser";
			String lsFirstName = "rts";
			String lsMidInit = "X";
			String lsNickName = "rtstest";
			// if there were any properties passed in, try them
			// get the ou name
			if (aaArgs.length > 0)
			{
				lsTestCaseList = (String) aaArgs[0];
			}
			// get the ou name
			if (aaArgs.length > 1)
			{
				lsOfcName = (String) aaArgs[1];
			}
			// get User Name
			if (aaArgs.length > 2)
			{
				lsUserName = (String) aaArgs[2];
			}
			//	get user type
			if (aaArgs.length > 3)
			{
				lsUserType = (String) aaArgs[3];
			}
			// get last name
			if (aaArgs.length > 4)
			{
				lsLastName = (String) aaArgs[4];
			}
			// get first name
			if (aaArgs.length > 5)
			{
				lsFirstName = (String) aaArgs[5];
			}
			// get middle initial
			if (aaArgs.length > 6)
			{
				lsMidInit = (String) aaArgs[6];
			}
			// get empid
			if (aaArgs.length > 7)
			{
				lsNickName = (String) aaArgs[7];
			}
			//----------------------------------------------------------
			// Exit if office is null
			if (lsOfcName == null)
			{
				System.out.println("Office Name is null");
				System.exit(0);
			}
			//----------------------------------------------------------
			// run the selected test cases
			for (int i = 0; i < lsTestCaseList.length(); i++)
			{
				// parse out the test case number
				int liCaseNumber =
					Integer.parseInt(
						lsTestCaseList.substring(i, i + 1));
				switch (liCaseNumber)
				{
					// do get user
					case GETUSERNAME :
						{
							testGetUser();
							break;
						}
						// delete user
					case DELETEUSERNAME :
						{
							testDelUser(lsOfcName, lsUserName);
							break;
						}
						// Add user
					case ADDUSERNAME :
						{
							testAddUser(
								lsOfcName,
								lsUserName,
								lsUserType,
								lsLastName,
								lsFirstName,
								lsMidInit,
								lsNickName);
							break;
						}
						// Update user
					case UPDATEUSERNAME :
						{
							if (aaArgs.length > 0)
							{
								testUpdtUser(
									lsOfcName,
									lsUserName,
									lsUserType,
									lsLastName,
									lsFirstName,
									lsMidInit,
									lsNickName);
							}
							else
							{
								testUpdtUser(
									lsOfcName,
									lsUserName,
									UTADMIN,
									lsLastName + "U",
									lsFirstName + "U",
									lsMidInit,
									lsNickName + "U");
							}
							break;
						}
						// reset password
					case RESETPASSWORD :
						{
							testResetPassword(lsOfcName, lsUserName);
							break;
						}
					default :
						{
							testGetUser();
						}
				}
				// sleep for 2 seconds
				Thread.sleep(2000);
			}
			//----------------------------------------------------------
			// end testing
		}
		catch (Throwable aeThrowable)
		{
			System.out.println("Got an error " + aeThrowable);
			aeThrowable.printStackTrace();
		}
	}
	/**
	 * This is the interface to the native C++ method 
	 * to call resetPassword routine.
	 * Reset the User's Password in Active Directory.
	 * 
	 * @return int - Return code from call
	 * @param  asOfcName   - String - Name of the office for this user.
	 * @param  asUserName  - String - Active Directory User Id for User.
	 */
	public static native int resetPassword(
		String asOfcName,
		String asUserName)
		throws Exception;
	/**
	 * This method runs the test for the addUser native method.
	 *  
	 * @param asOfcName
	 * @param asUserName
	 * @param asUserType
	 * @param asLastName
	 * @param asFirstName
	 * @param asMidInit
	 * @param asNickName
	 */
	public static void testAddUser(
		String asOfcName,
		String asUserName,
		String asUserType,
		String asLastName,
		String asFirstName,
		String asMidInit,
		String asNickName)
	
	{
		try
		{
			// Get the Windows User
			//JniAdInterface lJAI = new JniAdInterface();
			// say what we are doing
			System.out.println(
				"Add Test for : "
					+ asOfcName
					+ " "
					+ asUserName
					+ " "
					+ asUserType
					+ " "
					+ asLastName
					+ " "
					+ asFirstName
					+ " "
					+ asMidInit
					+ " "
					+ asNickName);
			// test add.
			int liAddRtn =
				JniAdInterface.addUser(
					asOfcName,
					asUserName,
					asUserType,
					asLastName,
					asFirstName,
					asMidInit,
					asNickName);
			System.out.println(
				"The results of the add test is " + liAddRtn);
			System.out.println("");
		}
		catch (Throwable aeThrowable)
		{
			System.out.println("Got an error while adding user id!");
			aeThrowable.printStackTrace();
		}
	}
	/**
	 * This method runs the test for the delUser native method.
	 *  
	 * @param asOfcName - String
	 * @param asUserName - String
	 */
	public static void testDelUser(String asOfcName, String asUserName)
	{
		try
		{
			System.out.println(
				"Delete Test for : " + asOfcName + " " + asUserName);

			// delete the active directory User
			int liDelRtn =
				JniAdInterface.delUser(asOfcName, asUserName);
			System.out.println(
				"The results of the delete test is " + liDelRtn);
			System.out.println("");
		}
		catch (Exception e)
		{
			System.out.println("Got an error while deleting user id!");
			e.printStackTrace();
		}
	}
	/**
	 * This method runs the test for the getUser native method.
	 */
	public static void testGetUser()
	{
		try
		{
			// Get the Windows User
			System.out.println(
				"Logged in UserId " + JniAdInterface.getUser());
			System.out.println("");
		}
		catch (Throwable aeThrowable)
		{
			System.out.println("Got an error while getting user id!");
			aeThrowable.printStackTrace();
		}
	}
	/**
	 * This method runs the test for the resetPassword native method.
	 *  
	 * @param asOfcName - String
	 * @param asUserName - String
	 */
	public static void testResetPassword(
		String asOfcName,
		String asUserName)
	{
		try
		{
			System.out.println(
				"Test Reset Password for "
					+ asOfcName
					+ " "
					+ asUserName);
			// delete the active directory User
			int liRtn =
				JniAdInterface.resetPassword(asOfcName, asUserName);
			System.out.println(
				"The results of the reset password test is " + liRtn);
			System.out.println("");
		}
		catch (Throwable aeThrowable)
		{
			System.out.println("Got an error while reseting password!");
			aeThrowable.printStackTrace();
		}
	}
	/**
	 * This method runs the test for the updtUser native method.
	 *  
	 * @param asOfcName - String
	 * @param asUserName - String
	 * @param asUserType - String
	 * @param asLastName - String
	 * @param asFirstName - String
	 * @param asMidInit - String
	 * @param asNickName - String
	 */
	public static void testUpdtUser(
		String asOfcName,
		String asUserName,
		String asUserType,
		String asLastName,
		String asFirstName,
		String asMidInit,
		String asNickName)
	{
		try
		{
			// Get the Windows User
			//JniAdInterface lJAI = new JniAdInterface();
			// say what we are doing
			System.out.println(
				"Update Test for : "
					+ asOfcName
					+ " "
					+ asUserName
					+ " "
					+ asUserType
					+ " "
					+ asLastName
					+ " "
					+ asFirstName
					+ " "
					+ asMidInit
					+ " "
					+ asNickName);
			// test update.
			//int liRtn =
			//	lJAI.updtUser(
			int liRtn =
				JniAdInterface.updtUser(
					asOfcName,
					asUserName,
					asUserType,
					asLastName,
					asFirstName,
					asMidInit,
					asNickName);
			System.out.println(
				"The results of the update test is " + liRtn);
			System.out.println("");
		}
		catch (Throwable aeThrowable)
		{
			System.out.println("Got an error while updating user id!");
			aeThrowable.printStackTrace();
		}
	}
	/**
	 * This is the interface to the native C++ method 
	 * to call updtUser routine.
	 * Update the User's Entry in Active Directory.
	 * 
	 * @param  asOfcName   - String - Name of the office for this user.
	 * @param  asUserName  - String - Active Directory User Id for User.
	 * @param  asUserType  - String - User Type.  Admin or Normal.
	 * @param  asLastName  - String - Last Name of User
	 * @param  asFirstName - String - First Name of User
	 * @param  asMidInt    - String - Middle Initial of User
	 * @param  asNickName  - String - EmpId of User
	 * @return int - Return code from call
	 */
	public static native int updtUser(
		String asOfcName,
		String asUserName,
		String asUserType,
		String asLastName,
		String asFirstName,
		String asMidInt,
		String asNickName)
		throws Exception;
	/**
	 * Edit User Provided User Name.
	 * 
	 * <p>This edit allows the RTS Security Administrator to create 
	 * User Names that differ from the normal first initial and last 
	 * name standard for User Names.
	 * This new way allows numbers to be used in place of letters.
	 * 
	 * <p>The User Name can also be shorter than normal.
	 * For example, Irma Majerk.  Would become IMAJERK.
	 * 
	 * <p>New way the county types in IM1234 for User Name.
	 * 
	 * @param aSecData - SecurityData
	 * @throws RTSException
	 */
	private static void validateUserProvidedUserName(SecurityData aaSecData)
		throws RTSException
	{
		int liNum;
		String lsIntegers = new String("0123456789");
		String lsUserName = aaSecData.getUserName().trim();
		String lsLastName = aaSecData.getEmpLastName().trim();

		// check first initial
		if (!lsUserName
			.substring(4, 5)
			.equalsIgnoreCase(
				aaSecData.getEmpFirstName().substring(0, 1)))
		{
			throw new RTSException(751);
		}

		// check first letter of last name
		if (!lsUserName
			.substring(5, 6)
			.equalsIgnoreCase(lsLastName.substring(0, 1)))
		{
			throw new RTSException(751);
		}
		// check second letter of last name
		if (lsUserName.length() > 6
			&& lsLastName.length() > 1
			&& !lsUserName.substring(6, 7).equalsIgnoreCase(
				lsLastName.substring(1, 2)))
		{
			liNum = lsIntegers.indexOf(lsUserName.substring(6, 7));
			if (liNum < 0)
			{
				throw new RTSException(751);
			}
		}
		// check third letter of last name
		if (lsUserName.length() > 7
			&& (lsLastName.length() <= 2
				|| lsLastName.length() > 2
				&& !lsUserName.substring(7, 8).equalsIgnoreCase(
					lsLastName.substring(2, 3))))
		{
			liNum = lsIntegers.indexOf(lsUserName.substring(7, 8));
			if (liNum < 0)
			{
				throw new RTSException(751);
			}
		}

		// check forth letter of last name
		if (lsUserName.length() > 8
			&& (lsLastName.length() <= 3
				|| lsLastName.length() > 3
				&& !lsUserName.substring(8, 9).equalsIgnoreCase(
					lsLastName.substring(3, 4))))
		{
			liNum = lsIntegers.indexOf(lsUserName.substring(8, 9));
			if (liNum < 0)
			{
				throw new RTSException(751);
			}
		}

		// check fifth letter of last name
		if (lsUserName.length() > 9
			&& (lsLastName.length() <= 4
				|| lsLastName.length() > 4
				&& !lsUserName.substring(9, 10).equalsIgnoreCase(
					lsLastName.substring(4, 5))))
		{
			liNum = lsIntegers.indexOf(lsUserName.substring(9, 10));
			if (liNum < 0)
			{
				throw new RTSException(751);
			}
		}

		// check sixth letter of last name
		if (lsUserName.length() > 10
			&& (lsLastName.length() <= 5
				|| lsLastName.length() > 5
				&& !lsUserName.substring(10, 11).equalsIgnoreCase(
					lsLastName.substring(5, 6))))
		{
			liNum = lsIntegers.indexOf(lsUserName.substring(10, 11));
			if (liNum < 0)
			{
				throw new RTSException(751);
			}
		}
	}
}
