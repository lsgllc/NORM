package com.txdot.isd.rts.services.data;

/* 
 * RegistrationModifyData.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/28/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	06/28/2005	Refactor\Move 
 * 							RegistrationModifyData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
* Data object used with registration modify events.
 * 
 * @version	5.2.3		06/28/2005
 * @author	Joseph Kwik
 * <br>Creation Date:	10/12/01 12:12:55
 */
public class RegistrationModifyData implements java.io.Serializable
{
	private int ciRegModifyType = 0;
	private final static long serialVersionUID = 4736064654717752225L;
	/**
	 * RegistrationModifyData constructor comment.
	 */
	public RegistrationModifyData()
	{
		super();
	}
	/**
	 * gets the registration modify type.  The types are defined in 
	 * RegistrationConstant class.
	 * 
	 * @return int
	 */
	public int getRegModifyType()
	{
		return ciRegModifyType;
	}
	/**
	 * Set the registration modify type.  The types are defined in 
	 * RegistrationConstant class.
	 * 
	 * @param aiNewRegModifyType int
	 */
	public void setRegModifyType(int aiNewRegModifyType)
	{
		ciRegModifyType = aiNewRegModifyType;
	}
}
