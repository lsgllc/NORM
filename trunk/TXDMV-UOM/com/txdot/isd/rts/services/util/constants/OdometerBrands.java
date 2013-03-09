package com.txdot.isd.rts.services.util.constants;

/*
 *
 * OdometerBrands.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		07/25/2002	Add EXEMPT to OdometerBrands fields
 * 							defect 4513
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Odometer brands describing the type of odometer reading
 * 
 * @version	5.2.3			06/21/2005
 * @author	Todd Pederson
 * <br>Creation Date:		09/26/2001 16:11:09
 */
public class OdometerBrands
{
	public static final String EXEMPT = "EXEMPT";
	public static final String ACTUAL = "ACTUAL MILEAGE";
	public static final String NOTACT = "NOT ACTUAL MILEAGE";
	public static final String EXCEED = "EXCEEDS MECH. LIMITS";
	public static final String ACTUAL_CODE = "A";
	public static final String NOTACT_CODE = "N";
	public static final String EXCEED_CODE = "X";
}
