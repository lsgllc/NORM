package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * WorkstationInfo.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	03/05/2004	new class
 *							defect 6445 Ver 5.1.6
 * K Harrell	03/09/2004	removed test assignment of lsWorkstationName
 *							modify initialize()
 *							defect 6445 Ver 5.1.6
 * K Harrell	04/22/2005	Java 1.4 Work
 * 							deprecate setOfcissuanceNo(), 
 * 							setSubstaIdName(),setWorkstationId() 
 *							defect 7899 Ver 5.2.3
 * Ray Rowehl	05/05/2012	Rework setWorkstationName to parse out	
 *							any prefixes.
 *							delete setOfficeIssuanceNo(), 
 *								setSubstaIdName(), 
 *								setWorkstationId()
 *							modify setWorkstationName()
 *							defect 11320 Ver RTS_700
 * ---------------------------------------------------------------------
 */

/**
 * This class will gather and then report on information about the
 * workstation this is running on.
 *
 * <p>isRTSWorkstation is true only if the configuration matches
 * the parameters parsed from the TCP/IP Hostname.
 * 
 * <p>Main will show the workstation name of the machine it is run on.
 * It will also report if this is a production RTS workstation.
 * 
 * <p>Note that this class is only used in static mode.
 * 
 * <p>Remember that setters can not use getters since they call
 * initialize.
 *
 * @version	RTS_700			05/05/2012
 * @author	Ray Rowehl
 * @since					03/05/2004 07:35:11
 */

public class WorkstationInfo
{
	/**
	 * Switch to indicate if this class has been initialized  or not.
	 */
	private static boolean cbInitialized = false;

	/**
	 * Switch to indicate if this is an RTS Workstation or not
	 */
	private static boolean cbRTSWorkstation = false;

	/**
	 * Workstation Name
	 */
	private static String csWorkstationName;

	/**
	 * Office Issuance Number
	 */
	private static int ciOfficeIssuanceNo = Integer.MIN_VALUE;

	/**
	 * Substation Id Name
	 */
	private static String csSubstaIdName;

	/**
	 * Substation Id
	 */
	private static int ciSubstaId = Integer.MIN_VALUE;

	/**
	 * Workstation Id
	 */
	private static int ciWorkstationId = Integer.MIN_VALUE;
	
	/**
	 * Returns the OfficeIssuanceNo field..
	 * @return int
	 */
	public static int getOfficeIssuanceNo()
	{
		initialize();
		return ciOfficeIssuanceNo;
	}
	
	/**
	 * Returns the SubstaId field.
	 * 
	 * @return int
	 */
	public static int getSubstaId()
	{
		initialize();
		return ciSubstaId;
	}
	
	/**
	 * Returns the SubstaIdName field.
	 * 
	 * @return String
	 */
	private static String getSubstaIdName()
	{
		initialize();
		return csSubstaIdName;
	}
	
	/**
	 * Returns the WorkstationId field.
	 * 
	 * @return int
	 */
	public static int getWorkstationId()
	{
		initialize();
		return ciWorkstationId;
	}
	
	/**
	 * Returns the WorkstationName field.
	 * 
	 * @return String
	 */
	public static String getWorkstationName()
	{
		initialize();
		return csWorkstationName;
	}
	
	/**
	 * This method initializes the workstation info as needed.
	 * 
	 */
	private static void initialize()
	{
		if (!isInitialized())
		{
			try
			{
				//String lsWorkstationName = "R0110000";
				String lsWorkstationName =
					java.net.InetAddress.getLocalHost().getHostName();
				setWorkstationName(lsWorkstationName.trim());
				if (lsWorkstationName.trim().length() == 8)
				{
					parseWorkstationInfo(lsWorkstationName);
				}
				else
				{
					setRTSWorkstation(false);
				}
				setInitialized(true);
			}
			catch (Exception leEx)
			{
				System.out.println(
					"Error while initializing Workstation Info");
				leEx.printStackTrace();
				setRTSWorkstation(false);
			}
			finally
			{
				setInitialized(true);
			}
		}
	}
	
	/**
	 * Returns switch for Initialized boolean.
	 * 
	 * @return boolean
	 */
	private static boolean isInitialized()
	{
		return cbInitialized;
	}
	
	/**
	 * Returns switch for RTSWorkstation boolean.
	 * 
	 * @return boolean
	 */
	public static boolean isRTSWorkstation()
	{
		initialize();
		return cbRTSWorkstation;
	}
	
	/**
	 * Run check stand alone to see if workstation is RTS or not.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		System.out.println(
			"This allows a stand alone check of Workstation Type");
		System.out.println(
			"Workstation Name - " + getWorkstationName());
		if (isRTSWorkstation())
		{
			System.out.println("This is an RTS Workstation.");
			System.out.println(
				"Office Id is      " + getOfficeIssuanceNo());
			System.out.println(
				"SubstaId is       "
					+ getSubstaId()
					+ " "
					+ getSubstaIdName());
			System.out.println(
				"Workstation Id is " + getWorkstationId());
		}
		else
		{
			System.out.println("This is NOT an RTS Workstation.");
		}
	}
	
	/**
	 * Parse out the substation id and make it an int.
	 * 
	 * @param asSubstaId java.lang.String
	 */
	private static boolean parseSubstaId(String asSubstaId)
	{
		boolean lbStatus = false;

		// see if we can just parse the substa
		try
		{
			setSubstaId(Integer.parseInt(asSubstaId));
		}
		catch (Exception leEx)
		{
			// the substa name is not a number.
			// see if we can interpret the letter into a number.
			char lcSubStaChar = asSubstaId.charAt(0);
			setSubstaId(Character.getNumericValue(lcSubStaChar));

		}

		// check the results
		if (ciSubstaId
			== com
				.txdot
				.isd
				.rts
				.services
				.util
				.SystemProperty
				.getSubStationId())
		{
			lbStatus = true;
		}

		return lbStatus;
	}
	
	/**
	 * Break the workstation down into its components.
	 * 
	 * @param asWorkstationName String
	 */
	private static void parseWorkstationInfo(String asWorkstationName)
	{
		// boolean to track if workstaton passes each test
		boolean lbOkSoFar = false;

		// check to see if first charater is an R
		if (asWorkstationName.substring(0, 1).equals("R"))
		{
			lbOkSoFar = true;
		}
		else
		{
			lbOkSoFar = false;
		}

		// check if ofcissuanceno is ok
		if (lbOkSoFar)
		{
			String lsOfc = asWorkstationName.substring(1, 4);
			try
			{
				ciOfficeIssuanceNo = Integer.parseInt(lsOfc);

				if (ciOfficeIssuanceNo
					== com
						.txdot
						.isd
						.rts
						.services
						.util
						.SystemProperty
						.getOfficeIssuanceNo())
				{
					lbOkSoFar = true;
				}
				else
				{
					lbOkSoFar = false;
				}
			}
			catch (Exception leEx)
			{
				lbOkSoFar = false;
			}
		}

		// check on Substa
		if (lbOkSoFar)
		{
			String lsSubsta = asWorkstationName.substring(4, 5);
			lbOkSoFar = parseSubstaId(lsSubsta);
		}

		// check on wsid
		if (lbOkSoFar)
		{
			String lsWsid = asWorkstationName.substring(5, 8);
			try
			{
				ciWorkstationId = Integer.parseInt(lsWsid);
				if (ciWorkstationId
					== com
						.txdot
						.isd
						.rts
						.services
						.util
						.SystemProperty
						.getWorkStationId())
				{
					lbOkSoFar = true;
				}
				else
				{
					lbOkSoFar = false;
				}
			}
			catch (Exception e)
			{
				lbOkSoFar = false;
			}
		}

		setRTSWorkstation(lbOkSoFar);
	}
	
	/**
	 * Sets the boolean to the appropriate value.
	 * 
	 * @param abInitialized boolean
	 */
	private static void setInitialized(boolean abInitialized)
	{
		cbInitialized = abInitialized;
	}

	/**
	 * Sets the boolean to the appropriate value.
	 * 
	 * @param abRTSWorkstation boolean
	 */
	private static void setRTSWorkstation(boolean abRTSWorkstation)
	{
		cbRTSWorkstation = abRTSWorkstation;
	}
	
	/**
	 * Set the SubstaId field.
	 * 
	 * @param aiSubstaId int
	 */
	private static void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	
	/**
	 * Set the WorkstationName field.
	 * 
	 * @param asWorkstationName String
	 */
	private static void setWorkstationName(String asWorkstationName)
	{
		if (asWorkstationName != null)
		{
			// defect 11320
			UtilityMethods laUM = new UtilityMethods();
			csWorkstationName = laUM.trimControlPoint(asWorkstationName);
			//csWorkstationName = asWorkstationName;
			// end defect 11320
		}
	}
}
