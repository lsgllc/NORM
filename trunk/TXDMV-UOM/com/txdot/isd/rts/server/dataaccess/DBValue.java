package com.txdot.isd.rts.server.dataaccess;

import java.lang.String;

/*
 * DBValue.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks      10/06/2001  Added JavaDoc
 * R Hicks	  	11/15/2001  Added support for Phase 2 BLOB
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 *----------------------------------------------------------------------
 */

/**
 * This class is used by the DatabaseAccess class and the server-side
 * database objects to represent a value in the DB2 database.  It 
 * contains two values:
 * <ul>
 * <li>csDbValue - A string representing the database value.</li>
 * <li>ciDbType  - An integer representing the value's type.  The value
 * of ciDbType is a static from the java.sql.Types class.</li>
 * </ul>
 * <p>
 * DBValues are used by the DatabaseAccess class when setting values
 * in SQL PreparedStetments.
 * 
 * @version	5.2.3		05/02/2005
 * @author	Richard Hicks
 * <br>Creation Date:	06/18/2004 16:40:00
 */
public class DBValue
{
	/**  Instance variable representing a value in a database. **/
	private String csDbValue;
	/**  Instance variable representing the type of the csDbValue. **/
	private int ciDbType;
	private byte[] carrObjectBytes;

	/**
	 * Default DBValue constructor.
	 */
	public DBValue()
	{
		super();
	}
	/**
	 * DBValue Constructor.
	 *   
	 * @param aiType int Type of value as defined in java.sql.Types.
	 * @param asValue String String representation of a database value.
	 */
	public DBValue(int aiType, String asValue)
	{
		super();
		ciDbType = aiType;
		csDbValue = asValue;
		carrObjectBytes = null;
	}
	/**
	 * Returns the type associated with this csDbValue.
	 * 
	 * @return int
	 */
	public int getDbType()
	{
		return ciDbType;
	}
	/**
	 * Returns the String repesentation of a database value.
	 *
	 * @return String
	 */
	public String getDbValue()
	{
		return csDbValue;
	}
	/**
	 * Get Object Bytes
	 *  
	 * @return byte[]
	 */
	public byte[] getObjectBytes()
	{
		return carrObjectBytes;
	}
	/**
	 * Set the ciDbType graph variable.
	 * 
	 * @param aiNewDbType int
	 */
	public void setDbType(int aiNewDbType)
	{
		ciDbType = aiNewDbType;
	}
	/**
	 * Set the csDbValue graph variable
	 * 
	 * @param asNewDbValue String
	 */
	public void setDbValue(String asNewDbValue)
	{
		csDbValue = asNewDbValue;
	}
	/**
	 * Set Object Bytes
	 *  
	 * @param aarrNewObjectBytes byte[]
	 */
	public void setObjectBytes(byte[] aarrNewObjectBytes)
	{
		carrObjectBytes = aarrNewObjectBytes;
	}

	/**
	 * DB AbstractValue
	 *  
	 * @param aiType int
	 * @param aarrOValue byte[]
	 */
	public DBValue(int aiType, byte[] aarrOValue)
	{
		super();
		ciDbType = aiType;
		csDbValue = null;
		carrObjectBytes = aarrOValue;
	}
}
