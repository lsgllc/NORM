package com.txdot.isd.rts.services.util.constants;
/*
 * V21Constant.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/03/2008	Created
 * 							defect 9546 Ver 3 Amigos PH A 
 * ---------------------------------------------------------------------
 */

/**
 * Constants for V21 processing 
 *
 * @version	3 Amigos PH A	02/03/2008
 * @author	Kathy Harrell 
 * <br>Creation Date:		02/03/2007  12:57:00 
 */
public class V21Constant
{
	// Constants for Process Data 
	public final static int V21_GET_VEH_REQ = 1;
	public final static int V21_GET_ADMIN_TBL_REQ = 2;
	public final static int V21_VEH_SOLD_REQ = 3;
	public final static int V21_PLT_DISP_REQ = 4;
	public final static int V21_COMPL_REQ = 5;
	
	// Request Type Constants   
	public final static String V21_GET_VEH = "GETVEH"; 
	public final static String V21_GET_ADMIN_TBL = "GETADMIN";
	public final static String V21_VEH_SOLD = "VEHSOLD";
	public final static String V21_PLT_DISP = "PLTDISP";
}
