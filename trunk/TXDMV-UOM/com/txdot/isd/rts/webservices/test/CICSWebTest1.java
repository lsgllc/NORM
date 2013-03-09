package com.txdot.isd.rts.webservices.test;
/*
 * CICSWebTest1.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * ---------------------------------------------------------------------
 */

/**
 * Simple test of number in number out web service.
 *
 * @version	X.X.X Fix X		MM/DD/YYYY
 * @author	RROWEHL
 * <br>Creation Date:		10/26/2010 15:25:30
 */
public class CICSWebTest1
{

public int getNumber(int aiGuess)
{
	int liRay = 6;
	if (aiGuess == 11)
	{
		liRay = 55;
	}
	return liRay;
}

}
