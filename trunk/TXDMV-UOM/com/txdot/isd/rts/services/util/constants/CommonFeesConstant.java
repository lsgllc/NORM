package com.txdot.isd.rts.services.util.constants;

/*
 *
 * CommonFeesConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Peters		10/17/2001	Added Constants
 * Ray Rowehl	06/21/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	05/29/2009	add FEE_CALC_CAT_MODL_YR,
 * 							FEE_CALC_CAT_WEIGHT_BASED,
 * 							FEE_CALC_CAT_FLAT_FEE,
 * 							FEE_CALC_CAT_NO_FEE
 * 							defect 8749 Ver Defect_POS_F
 * K Harrell	11/07/2011	Cleanup
 * 							delete all but those added in 8749 
 * 							defect 10949 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * Constants for Common Fees
 *
 * @version	6.9.0			11/07/2011
 * @author	Joseph Peters
 * @since 					10/17/2001 11:21:00
 */

public class CommonFeesConstant
{
	// defect 8749 
	public final static int FEE_CALC_CAT_MODL_YR = 1;
	public final static int FEE_CALC_CAT_WEIGHT_BASED = 2;
	public final static int FEE_CALC_CAT_FLAT_FEE = 3;
	public final static int FEE_CALC_CAT_NO_FEE = 4; 
	// end defect 8749 
}
