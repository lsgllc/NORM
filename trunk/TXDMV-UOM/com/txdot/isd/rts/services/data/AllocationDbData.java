package com.txdot.isd.rts.services.data;

import java.io.*;
import java.util.*;

/*
 *
 * AllocationDbData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * AllocationDbData
 *
 * @version	5.2.3			05/19/2005
 * @author	Charlie Walker
 * <br>Creation Date:		09/20/2001  10:30:33 
 */

public class AllocationDbData implements Serializable
{
	// Vector
	private Vector cvSubconWrap;
	private Vector cvWsWrap;
	private Vector cvDlrWrap;
	private Vector cvSecrtyWrap;
	private Vector cvSubstaWrap;
	
	private final static long serialVersionUID = 8180468725504361168L;
	/**
	 * AllocationDbData constructor comment.
	 */
	public AllocationDbData()
	{
	}
	/**
	 * Return the value of DlrWrap.
	 * 
	 * @return Vector
	 */
	public Vector getDlrWrap()
	{
		return cvDlrWrap;
	}
	/**
	 * Return the value of SecrtyWrap.
	 * 
	 * @return Vector
	 */
	public Vector getSecrtyWrap()
	{
		return cvSecrtyWrap;
	}
	/**
	 * Return the value of SubconWrap.
	 * 
	 * @return Vector
	 */
	public Vector getSubconWrap()
	{
		return cvSubconWrap;
	}
	/**
	 * Return the value of SubstaWrap.
	 * 
	 * @return Vector
	 */
	public Vector getSubstaWrap()
	{
		return cvSubstaWrap;
	}
	/**
	 * Return the value of WsWrap.
	 * 
	 * @return Vector
	 */
	public Vector getWsWrap()
	{
		return cvWsWrap;
	}
	/**
	 * Sets the value of DlrWrap.
	 * 
	 * @param avDlrWrap Vector
	 */
	public void setDlrWrap(Vector avDlrWrap)
	{
		cvDlrWrap = avDlrWrap;
	}
	/**
	 * Sets the value of SecrtyWrap.
	 * 
	 * @param avSecrtyWrap Vector
	 */
	public void setSecrtyWrap(Vector avSecrtyWrap)
	{
		cvSecrtyWrap = avSecrtyWrap;
	}
	/**
	 * Sets the value of SubconWrap.
	 * 
	 * @param avSubconWrap Vector
	 */
	public void setSubconWrap(Vector avSubconWrap)
	{
		cvSubconWrap = avSubconWrap;
	}
	/**
	 * Sets the value of SubstaWrap.
	 * 
	 * @param avSubstaWrap Vector
	 */
	public void setSubstaWrap(Vector avSubstaWrap)
	{
		cvSubstaWrap = avSubstaWrap;
	}
	/**
	 * Sets the value of WsWrap.
	 * 
	 * @param avWsWrap Vector
	 */
	public void setWsWrap(Vector avWsWrap)
	{
		cvWsWrap = avWsWrap;
	}
}
