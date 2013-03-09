package com.txdot.isd.rts.services.util.constants;
/*
 * ApplicationControlConstants.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	10/09/2006	New class
 * 							defect 6701 Ver Exempts
 * Ray Rowehl	02/27/2007	Add MfgStatusCd constants for Inventory.
 * 							add SA_MFGSTATUS_DO_NOT, 
 * 								SA_MFGSTATUS_RESERVED
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	04/21/2010	Remove MfgStatusCd constants.
 * 							Use the ones defined in SpecialPlatesConstant.
 * 							delete SA_MFGSTATUS_DO_NOT, 
 * 								SA_MFGSTATUS_RESERVED
 * 							defect 10415 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * This class stores the Application Control Constants.
 *
 * @version	6.4.0			04/21/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		10/09/2006 08:38:00
 */
public class ApplicationControlConstants
{
	/** 
	 * Used in MfAccess.
	 * 
	 * <p>
	 * Switch for the <code>insert</code> and <code>resize</code> 
	 * methods. 
	 */
	public final static String SC_MFA_INSERT_AT_BEGINNING =
		ProjectDictionary.SA_BEGINNING_UC;

	/** 
	 * Used in MfAccess.
	 * 
	 * <p>
	 * Switch for the <code>insert</code> and <code>resize</code> 
	 * methods. 
	 */
	public final static String SC_MFA_INSERT_AT_END =
		ProjectDictionary.SA_END_UC;

	/**
	 * This reflects CICS Cobol Version T.
	 * 
	 * <p>This is the version to be used for the Exmepts Project (5.3.0).
	 */
	public static final String SC_MFA_VERSION_T = "T";

	/**
	 * This reflects CICS Cobol Version U.
	 * 
	 * <p>The is the version used for RTS I (AM).
	 * 
	 * <p>This version will be used for the Special Plates Project 
	 * (5.4.0).
	 */
	public static final String SC_MFA_VERSION_U = "U";

	/**
	 * This reflects CICS Cobol Version V.
	 * 
	 * <p>This is the version used for RTS II until the Exempts Project 
	 * (5.3.0).
	 */
	public static final String SC_MFA_VERSION_V = "V";
	
	
// defect 10415
//	/**
//	 * MfgStatusCd indicating that the plate number should not be 
//	 * manufactured.
//	 */
//	public final static String SA_MFGSTATUS_DO_NOT = "N";
//	
//	/**
//	 * MfgStatusCd indicating that the plate number is reserved
//	 * to be issued later.
//	 */
//	public final static String SA_MFGSTATUS_RESERVED = "R";
// end defect 10415
}
