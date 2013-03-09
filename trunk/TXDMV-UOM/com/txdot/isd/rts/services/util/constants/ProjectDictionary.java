package com.txdot.isd.rts.services.util.constants;
/*
 * ProjectDictionary.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	10/10/2006	New Class
 * 							defect 6701 Ver Exempts
 * Jeff S.		10/16/2006	add LAUNCH
 * 							defect 8900 Ver Exempts
 * ---------------------------------------------------------------------
 */

/**
 * Project Book.
 *
 * @version	Exempts			10/16/2006
 * @author	Ray Rowehl
 * <br>Creation Date:		10/10/2006 17:28:00
 */
public class ProjectDictionary
{
	/**
	 * BEGINNING
	 * 
	 * <ul>
	 * <li>ApplicationControlConstants.SC_MFA_INSERT_AT_BEGINNING
	 * <eul>
	 */
	public static final String SA_BEGINNING_UC = "BEGINNING";
	
	/**
	 * END
	 * 
	 * <ul>
	 * <li>ApplicationControlConstants.SC_MFA_INSERT_AT_END
	 * <eul>
	 */
	public static final String SA_END_UC = "END"; 
	
	// defect 8900
	/**
	 * Used to Launch an external webpage or form from a menu
	 * item.
	 */
	public final static String LAUNCH = "Launch";
	// end defect 8900
}
