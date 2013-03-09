package com.txdot.isd.rts.webservices.test;

/*
 * CICSWebTest2.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * ---------------------------------------------------------------------
 */

/**
 * Simple test of String in String out web service.
 *
 * @version	X.X.X Fix X		MM/DD/YYYY
 * @author	RROWEHL
 * <br>Creation Date:		10/26/2010 16:39:31
 */
public class CICSWebTest2
{

public String getString(String asGuess)
{
	System.out.println("Got a call old school");
	String lsRay = "What";
	if (asGuess != null && asGuess.equalsIgnoreCase("Knock"))
	{
		lsRay = "Whos There";
	}
	return lsRay;
}

/**
 * Simple web service test method.
 * 
 * @param asGuess
 * @return String
 */
public String getTheAnswer(String asGuess)
{
	System.out.println("Got a call ");
	if (asGuess != null)
	{
		System.out.println("Guess was " + asGuess);
	}
	else
	{
		System.out.println("No Guess!!");
	}
	
	String lsAnswer = "What";
	
	if (asGuess != null && asGuess.equalsIgnoreCase("Knock"))
	{
		lsAnswer = "Whos There";
	}
	else if (asGuess != null && asGuess.equals("FBI"))
	{
		lsAnswer = "Federal Bureau of Investigation";
	}
	else if (asGuess != null && asGuess.equals("CrazyRay"))
	{
		lsAnswer = "MM1076";
	}

	
	System.out.println("Answer was " + lsAnswer);
	
	return lsAnswer;
}

}
