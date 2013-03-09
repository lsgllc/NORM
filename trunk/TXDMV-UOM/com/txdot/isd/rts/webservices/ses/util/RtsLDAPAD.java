package com.txdot.isd.rts.webservices.ses.util;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * RtsLDAPeDir.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Mark Reyes	02/14/2011	Class Created.
 * Bob Brown	03/13/2011	Changed for SOAPRSPS access.
 * Ray Rowehl	04/25/2011	This class is about checking Active Directory.
 * 							Modify to more directly interate through
 * 							the various folders that contain Users.
 * 							defect 10670 Ver 6.7.1
 * Ray Rowehl	05/05/2011	Modify AD lookup for DDOS to simplify the 
 * 							lookup process!
 * 							add formulateCountyOuName(),
 * 								processADOURequest(String, String)
 * 							deprecate chkHqAndRegions(),
 * 								processADOURequest(String , 
 * 								String, String, String)
 * 							defect 10670 Ver 6.7.1
 * R Pilon		06/13/2011	Add edit to ensure user id and password are
 * 							populated
 * 							modify login()
 * 							mofidy main()
 * 							defect 10670 Ver 6.8.0
 * ---------------------------------------------------------------------
 */

/**
 * This class provides an interface to Active Directory using LDAP.
 *
 * @version	6.8.0		06/13/2011
 * @author Mark Reyes
 * @author Bob Brown
 * @author Ray Rowehl
 * <br>Creation Date:	02/14/2011 14:00:00
 */
public class RtsLDAPAD
{
	private static String[] sarrCntyNames = null;

	/**
	 * Initialization block for the county array.
	 */
	private static void initializeArray()
	{
		if (sarrCntyNames == null)
		{
			sarrCntyNames = new String[254];
			sarrCntyNames[0] = "Anderson";
			sarrCntyNames[1] = "Andrews";
			sarrCntyNames[2] = "Angelina";
			sarrCntyNames[3] = "Aransas";
			sarrCntyNames[4] = "Archer";
			sarrCntyNames[5] = "Armstrong";
			sarrCntyNames[6] = "Atacosa";
			sarrCntyNames[7] = "Austin";
			sarrCntyNames[8] = "Bailey";
			sarrCntyNames[9] = "Bandera";
			sarrCntyNames[10] = "Bastrop";
			sarrCntyNames[11] = "Baylor";
			sarrCntyNames[12] = "Bee";
			sarrCntyNames[13] = "Bell";
			sarrCntyNames[14] = "Bexar";
			sarrCntyNames[15] = "Blanco";
			sarrCntyNames[16] = "Borden";
			sarrCntyNames[17] = "Bosque";
			sarrCntyNames[18] = "Bowie";
			sarrCntyNames[19] = "Brazoria";
			sarrCntyNames[20] = "Brazos";
			sarrCntyNames[21] = "Brewster";
			sarrCntyNames[22] = "Briscoe";
			sarrCntyNames[23] = "Brooks";
			sarrCntyNames[24] = "Brown";
			sarrCntyNames[25] = "Burleson";
			sarrCntyNames[26] = "Burnet";
			sarrCntyNames[27] = "Caldwell";
			sarrCntyNames[28] = "Calhoun";
			sarrCntyNames[29] = "Callahan";
			sarrCntyNames[30] = "Cameron";
			sarrCntyNames[31] = "Camp";
			sarrCntyNames[32] = "Carson";
			sarrCntyNames[33] = "Cass";
			sarrCntyNames[34] = "Castro";
			sarrCntyNames[35] = "Chambers";
			sarrCntyNames[36] = "Cherokee";
			sarrCntyNames[37] = "Childress";
			sarrCntyNames[38] = "Clay";
			sarrCntyNames[39] = "Cochran";
			sarrCntyNames[40] = "Coke";
			sarrCntyNames[41] = "Coleman";
			sarrCntyNames[42] = "Collin";
			sarrCntyNames[43] = "Collingsworth";
			sarrCntyNames[44] = "Colorado";
			sarrCntyNames[45] = "Comal";
			sarrCntyNames[46] = "Comanche";
			sarrCntyNames[47] = "Concho";
			sarrCntyNames[48] = "Cooke";
			sarrCntyNames[49] = "Coryell";
			sarrCntyNames[50] = "Cottle";
			sarrCntyNames[51] = "Crane";
			sarrCntyNames[52] = "Crockett";
			sarrCntyNames[53] = "Crosby";
			sarrCntyNames[54] = "Culberson";
			sarrCntyNames[55] = "Dallam";
			sarrCntyNames[56] = "Dallas";
			sarrCntyNames[57] = "Dawson";
			sarrCntyNames[58] = "Deaf Smith";
			sarrCntyNames[59] = "Delta";
			sarrCntyNames[60] = "Denton";
			sarrCntyNames[61] = "DeWitt";
			sarrCntyNames[62] = "Dickens";
			sarrCntyNames[63] = "Dimmit";
			sarrCntyNames[64] = "Donley";
			sarrCntyNames[65] = "Duval";
			sarrCntyNames[66] = "Eastland";
			sarrCntyNames[67] = "Ector";
			sarrCntyNames[68] = "Edwards";
			sarrCntyNames[69] = "Ellis";
			sarrCntyNames[70] = "El Paso";
			sarrCntyNames[71] = "Ellis";
			sarrCntyNames[72] = "Erath";
			sarrCntyNames[73] = "Falls";
			sarrCntyNames[74] = "Fannin";
			sarrCntyNames[75] = "Fayette";
			sarrCntyNames[76] = "Fisher";
			sarrCntyNames[77] = "Floyd";
			sarrCntyNames[78] = "Foard";
			sarrCntyNames[79] = "Fort Bend";
			sarrCntyNames[80] = "Franklin";
			sarrCntyNames[81] = "Freestone";
			sarrCntyNames[82] = "Frio";
			sarrCntyNames[83] = "Gaines";
			sarrCntyNames[84] = "Galveston";
			sarrCntyNames[85] = "Garza";
			sarrCntyNames[86] = "Gillespie";
			sarrCntyNames[87] = "Glasscock";
			sarrCntyNames[88] = "Goliad";
			sarrCntyNames[89] = "Gonzales";
			sarrCntyNames[90] = "Gray";
			sarrCntyNames[91] = "Grayson";
			sarrCntyNames[92] = "Gregg";
			sarrCntyNames[93] = "Grimes";
			sarrCntyNames[94] = "Guadalupe";
			sarrCntyNames[95] = "Hale";
			sarrCntyNames[96] = "Hamilton";
			sarrCntyNames[97] = "Hansford";
			sarrCntyNames[98] = "Hardeman";
			sarrCntyNames[99] = "Hardin";
			sarrCntyNames[100] = "Harris";
			sarrCntyNames[101] = "Harrison";
			sarrCntyNames[102] = "Hartley";
			sarrCntyNames[103] = "Haskell";
			sarrCntyNames[104] = "Hays";
			sarrCntyNames[105] = "Hemphill";
			sarrCntyNames[106] = "Henderson";
			sarrCntyNames[107] = "Hidalgo";
			sarrCntyNames[108] = "Hill";
			sarrCntyNames[109] = "Hockley";
			sarrCntyNames[110] = "Hood";
			sarrCntyNames[111] = "Hopkins";
			sarrCntyNames[112] = "Houston";
			sarrCntyNames[113] = "Howard";
			sarrCntyNames[114] = "Hudspeth";
			sarrCntyNames[115] = "Hunt";
			sarrCntyNames[116] = "Hutchinson";
			sarrCntyNames[117] = "Irion";
			sarrCntyNames[118] = "Jack";
			sarrCntyNames[119] = "Jackson";
			sarrCntyNames[120] = "Jasper";
			sarrCntyNames[121] = "Jeff Davis";
			sarrCntyNames[122] = "Jefferson";
			sarrCntyNames[123] = "Jim Hogg";
			sarrCntyNames[124] = "Jim Wells";
			sarrCntyNames[125] = "Johnson";
			sarrCntyNames[126] = "Jones";
			sarrCntyNames[127] = "Karnes";
			sarrCntyNames[128] = "Kaufman";
			sarrCntyNames[129] = "Kendall";
			sarrCntyNames[130] = "Kenedy";
			sarrCntyNames[131] = "Kent";
			sarrCntyNames[132] = "Kerr";
			sarrCntyNames[133] = "Kimble";
			sarrCntyNames[134] = "King";
			sarrCntyNames[135] = "Kinney";
			sarrCntyNames[136] = "Kleberg";
			sarrCntyNames[137] = "Knox";
			sarrCntyNames[138] = "La Salle";
			sarrCntyNames[139] = "Lamar";
			sarrCntyNames[140] = "Lamb";
			sarrCntyNames[141] = "Lampasas";
			sarrCntyNames[142] = "Lavaca";
			sarrCntyNames[143] = "Lee";
			sarrCntyNames[144] = "Leon";
			sarrCntyNames[145] = "Liberty";
			sarrCntyNames[146] = "Limestone";
			sarrCntyNames[147] = "Lipscomb";
			sarrCntyNames[148] = "Live Oak";
			sarrCntyNames[149] = "Llano";
			sarrCntyNames[150] = "Loving";
			sarrCntyNames[151] = "Lubbock";
			sarrCntyNames[152] = "Lynn";
			sarrCntyNames[153] = "Madison";
			sarrCntyNames[154] = "Marion";
			sarrCntyNames[155] = "Martin";
			sarrCntyNames[156] = "Mason";
			sarrCntyNames[157] = "Matagorda";
			sarrCntyNames[158] = "Maverick";
			sarrCntyNames[159] = "McCulloch";
			sarrCntyNames[160] = "McLellan";
			sarrCntyNames[161] = "McMullen";
			sarrCntyNames[162] = "Medina";
			sarrCntyNames[163] = "Menard";
			sarrCntyNames[164] = "Midland";
			sarrCntyNames[165] = "Milam";
			sarrCntyNames[166] = "Mills";
			sarrCntyNames[167] = "Mitchell";
			sarrCntyNames[168] = "Montague";
			sarrCntyNames[169] = "Montgomery";
			sarrCntyNames[170] = "Moore";
			sarrCntyNames[171] = "Morris";
			sarrCntyNames[172] = "Motley";
			sarrCntyNames[173] = "Nacogdoches";
			sarrCntyNames[174] = "Navarro";
			sarrCntyNames[175] = "Newton";
			sarrCntyNames[176] = "Nolan";
			sarrCntyNames[177] = "Nueces";
			sarrCntyNames[178] = "Ochiltree";
			sarrCntyNames[179] = "Oldham";
			sarrCntyNames[180] = "Orange";
			sarrCntyNames[181] = "Palo Pinto";
			sarrCntyNames[182] = "Panola";
			sarrCntyNames[183] = "Parker";
			sarrCntyNames[184] = "Parmer";
			sarrCntyNames[185] = "Pecos";
			sarrCntyNames[186] = "Polk";
			sarrCntyNames[187] = "Potter";
			sarrCntyNames[188] = "Presido";
			sarrCntyNames[189] = "Rains";
			sarrCntyNames[190] = "Randall";
			sarrCntyNames[191] = "Reagan";
			sarrCntyNames[192] = "Real";
			sarrCntyNames[193] = "Red River";
			sarrCntyNames[194] = "Reeves";
			sarrCntyNames[195] = "Refugio";
			sarrCntyNames[196] = "Roberts";
			sarrCntyNames[197] = "Robertson";
			sarrCntyNames[198] = "Rockwall";
			sarrCntyNames[199] = "Runnels";
			sarrCntyNames[200] = "Rusk";
			sarrCntyNames[201] = "Sabine";
			sarrCntyNames[202] = "San Augustine";
			sarrCntyNames[203] = "San Jacinto";
			sarrCntyNames[204] = "San Patricio";
			sarrCntyNames[205] = "San Saba";
			sarrCntyNames[206] = "Scheicher";
			sarrCntyNames[207] = "Scurry";
			sarrCntyNames[208] = "Shackelford";
			sarrCntyNames[209] = "Shelby";
			sarrCntyNames[210] = "Sherman";
			sarrCntyNames[211] = "Smith";
			sarrCntyNames[212] = "Somervell";
			sarrCntyNames[213] = "Starr";
			sarrCntyNames[214] = "Stephens";
			sarrCntyNames[215] = "Sterling";
			sarrCntyNames[216] = "Stonewall";
			sarrCntyNames[217] = "Sutton";
			sarrCntyNames[218] = "Swisher";
			sarrCntyNames[219] = "Tarrant";
			sarrCntyNames[220] = "Taylor";
			sarrCntyNames[221] = "Terrell";
			sarrCntyNames[222] = "Terry";
			sarrCntyNames[223] = "Throckmorton";
			sarrCntyNames[224] = "Titus";
			sarrCntyNames[225] = "Tom Green";
			sarrCntyNames[226] = "Travis";
			sarrCntyNames[227] = "Trinity";
			sarrCntyNames[228] = "Tyler";
			sarrCntyNames[229] = "Upshur";
			sarrCntyNames[230] = "Upton";
			sarrCntyNames[231] = "Uvalde";
			sarrCntyNames[232] = "Val Verde";
			sarrCntyNames[233] = "Van Zandt";
			sarrCntyNames[234] = "Victoria";
			sarrCntyNames[235] = "Walker";
			sarrCntyNames[236] = "Waller";
			sarrCntyNames[237] = "Ward";
			sarrCntyNames[238] = "Washington";
			sarrCntyNames[239] = "Webb";
			sarrCntyNames[240] = "Wharton";
			sarrCntyNames[241] = "Wheeler";
			sarrCntyNames[242] = "Wichita";
			sarrCntyNames[243] = "Wilbarger";
			sarrCntyNames[244] = "Willacy";
			sarrCntyNames[245] = "Williamson";
			sarrCntyNames[246] = "Wilson";
			sarrCntyNames[247] = "Winkler";
			sarrCntyNames[248] = "Wise";
			sarrCntyNames[249] = "Wood";
			sarrCntyNames[250] = "Yoakum";
			sarrCntyNames[251] = "Young";
			sarrCntyNames[252] = "Zapata";
			sarrCntyNames[253] = "Zavala";
		}
	}

	/**
	 * Standalone test method
	 * 
	 * @param aaArgs
	 */
	public static void main(String[] aaArgs)
	{
		if (aaArgs.length == 2)
		{
			RtsLDAPAD laTest = new RtsLDAPAD();
			// defect 10670
			try
			{
				boolean lbResult;
				lbResult = laTest.login(aaArgs[0], aaArgs[1]);
				System.out.println(
					"The Result is " + lbResult + " for " + aaArgs[0]);
			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.printStackTrace();
			}
			// end defect 10670
		}
		else
		{
			System.out.println("Usage RtsLDAPAD UserName, Password");
		}
	}

	/**
	 * RtsLDAPAD.java Constructor
	 */
	public RtsLDAPAD()
	{
		super();
		initializeArray();
	}

	/**
	 * Check UserName to see if it might belong to a county.
	 * 
	 * @param asUsername
	 * @param abCheckCountyFirst
	 * @param aiOfcissuanceNo
	 */
	private boolean chkForCntyUserName(
		String asUsername,
		String asPassword)
	{
		boolean lbReturnvalue = false;
		boolean lbCheckCountyFirst = false;
		int liOfcissuanceNo = 0;

		if (asUsername.indexOf("-") > -1)
		{
			String lsCountyNo =
				asUsername.substring(0, asUsername.indexOf("-"));
			try
			{
				liOfcissuanceNo = Integer.parseInt(lsCountyNo);
				lbCheckCountyFirst = true;
			}
			catch (NumberFormatException aeNFEx)
			{
				// Nothing to do.  Just keep the boolean false!
			}
		}

		if (lbCheckCountyFirst)
		{
			if (liOfcissuanceNo > 0 && liOfcissuanceNo < 255)
			{
				String lsADString =
					formulateCountyOuName(asUsername, sarrCntyNames[liOfcissuanceNo - 1], "RTS");
				// Check the county ou
				lbReturnvalue =
					processADOURequest(
						lsADString,
						asPassword);
			}

			if (!lbReturnvalue)
			{
				String lsADString =
						formulateCountyOuName(asUsername, "_Test2", "RTS");
				// check Testing ou
				lbReturnvalue =
					processADOURequest(
					lsADString,
						asPassword);
			}
		}

		return lbReturnvalue;
	}

	/**
	 * Check HeadQuarters and Regions to find a match.
	 * 
	 * @param asUsername
	 * @param asPassword
	 * @return boolean
	 * @deprecated
	 */
	private boolean chkHqAndRegions(
		String asUsername,
		String asPassword)
	{
		boolean lbReturnvalue = false;

		// check VTR
		lbReturnvalue =
			processADOURequest(asUsername, asPassword, "VTR", "DDOs");

		if (!lbReturnvalue)
		{
			// Check ISD
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"ISD",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check ABL
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"ABL",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check AMA
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"AMA",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check BMT
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"BMT",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check CRP
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"CRP",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check DAL
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"DAL",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check ELP
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"ELP",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check FTW
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"FTW",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check HOU
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"HOU",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check LBB
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"LBB",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check ODA
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"ODA",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check PHR
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"PHR",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check SAT
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"SAT",
					"DDOs");
		}
		if (!lbReturnvalue)
		{
			// Check WAC
			lbReturnvalue =
				processADOURequest(
					asUsername,
					asPassword,
					"WAC",
					"DDOs");
		}

		return lbReturnvalue;
	}

	/**
	 * This is the public method that is called to authenticate a user
	 * against Active Directory.
	 * 
	 * @param asUsername
	 * @param asPassword
	 * @return boolean
	 * @throws RTSException
	 */
	public boolean login(String asUsername, String asPassword)
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

		boolean lbReturnvalue = false;

		// Check for a County User.  This also checks the _Test2 ou.
		lbReturnvalue = chkForCntyUserName(asUsername, asPassword);

		// If no match found, check the DDOs ous.
		if (!lbReturnvalue)
		{
			String lsPrincipal = asUsername + "@dot.state.tx.us";
			lbReturnvalue = processADOURequest(lsPrincipal, asPassword);
		}

		return lbReturnvalue;
	}

	/**
	 * Process request against request ou combination.
	 * 
	 * <p>Old version!
	 * 
	 * @param asUsername
	 * @param asPassword
	 * @param asMinorOu
	 * @param asMajorOu
	 * @return boolean
	 * @deprecated
	 */
	private boolean processADOURequest(
		String asUsername,
		String asPassword,
		String asMinorOu,
		String asMajorOu)
	{
		boolean lbReturnvalue = false;
		Hashtable laEnv = new Hashtable();
		DirContext laCtx = null;
		String lsADString = "";
		try
		{
			lsADString =
				"cn="
					+ asUsername
					+ ",ou=Users,ou="
					+ asMinorOu
					+ ",ou="
					+ asMajorOu
					+ ","
					+ "dc=dot,dc=state,dc=tx,dc=us";
					
			laEnv.put(
				Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
			laEnv.put(Context.PROVIDER_URL, "ldap://txdot-dc2:389");
			laEnv.put(Context.SECURITY_PRINCIPAL, lsADString);
			laEnv.put(Context.SECURITY_CREDENTIALS, asPassword);
			laEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			//laCtx = new InitialDirContext(laEnv);
			laCtx = new InitialLdapContext(laEnv, null);
			lbReturnvalue = true;
			//System.out.println(lbReturnvalue);
			laCtx.close();
		}
		catch (AuthenticationException aaAuthEx)
		{
			// boolean is already false, just return it
			Log.write(
				Log.SQL,
				this,
				"AuthenticationException for request " + lsADString);
		}
		catch (NamingException aeNEx)
		{
			// boolean is already false, just return it
			System.out.println("Naming Exception");
			aeNEx.printStackTrace();
		}
		return lbReturnvalue;
	}
	
	/**
	 * Process request against request ou combination.
	 * 
	 * @param asUsername
	 * @param asPassword
	 * @param asMinorOu
	 * @param asMajorOu
	 * @return boolean
	 */
	private boolean processADOURequest(
		String asPrincipal,
		String asPassword)
	{
		boolean lbReturnvalue = false;
		Hashtable laEnv = new Hashtable();
		DirContext laCtx = null;
		try
		{		
			laEnv.put(
				Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
			laEnv.put(Context.PROVIDER_URL, "ldap://txdot-dc2:389");
			laEnv.put(Context.SECURITY_PRINCIPAL, asPrincipal);
			laEnv.put(Context.SECURITY_CREDENTIALS, asPassword);
			laEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			laCtx = new InitialLdapContext(laEnv, null);
			lbReturnvalue = true;
			laCtx.close();
		}
		catch (AuthenticationException aaAuthEx)
		{
			// boolean is already false, just return it
			Log.write(
				Log.SQL,
				this,
				"AuthenticationException for request " + asPrincipal);
		}
		catch (NamingException aeNEx)
		{
			// boolean is already false, just return it
			System.out.println("Naming Exception");
			aeNEx.printStackTrace();
		}
		
		// We always want to just return the boolean.
		return lbReturnvalue;
	}


	/**
	 * Create the Principal String for AD Login.
	 * 
	 * @param asUsername
	 * @param asMinorOu
	 * @param asMajorOu
	 * @return String
	 */
	private String formulateCountyOuName(
		String asUsername,
		String asMinorOu,
		String asMajorOu)
	{
		String lsADString;
		lsADString =
			"cn="
				+ asUsername
				+ ",ou=Users,ou="
				+ asMinorOu
				+ ",ou="
				+ asMajorOu
				+ ","
				+ "dc=dot,dc=state,dc=tx,dc=us";
		return lsADString;
	}
}
