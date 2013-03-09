package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * ErrorMessagesData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/05/2001	Add categories 
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 							  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * ErrorMessagesData
 *
 * @version	5.2.3			06/19/2005 
 * @author	Administrator
 * <br>Creation Date:		08/30/2001
 */

public class ErrorMessagesData implements Serializable
{
	// int 
	protected int ciErrMsgNo;
	protected int ciMFErrLogIndi;
	protected int ciPCErrLogIndi;

	// String 
	protected String csErrMsgCat;
	protected String csErrMsgDesc;
	protected String csErrMsgType;

	/* Category */
	/**
	 * Category in database - CONFIRMATION
	 */
	public final static String CONFIRMATION = "C"; //YES, NO, CANCEL

	/**
	* Category in database - FATAL_PROCESSING
	*/
	public final static String FATAL_PROCESSING = "FP"; //OK, HELP

	/**
	 * Category in database - FAILED_VALIDATION
	 */
	public final static String FAILED_VALIDATION = "FV"; //OK, HELP

	/**
	 * Category in database - INFORMATION
	 */
	public final static String INFORMATION = "I"; //OK, HELP

	/**
	 * Category in database - LOG
	 */
	public final static String LOG = "L"; // do not show

	/**
	 * Category in database - WARNING
	 */
	public final static String WARNING = "W";

	/* mf and pc log indicator state */
	public final static int INDICATOR_OFF = 0;
	public final static int INDICATOR_ON = 1;

	private final static long serialVersionUID = -3191068650321312458L;
	/**
	 * ErrorMsgsData default constructor.
	 */
	public ErrorMessagesData()
	{
		super();
	}
	/**
	 * ErrorMessagesData constructor
	 * 
	 * @param aiMsgNumber int
	 * @param asMsgCategory String
	 * @param asMsgDescription String
	 * @param aiMFIndicator int
	 * @param aiPCIndicator int
	 */
	public ErrorMessagesData(
		int aiMsgNumber,
		String asMsgCategory,
		String asMsgDescription,
		int aiMFIndicator,
		int aiPCIndicator)
	{
		setErrMsgNo(aiMsgNumber);
		setErrMsgCat(asMsgCategory);
		setErrMsgDesc(asMsgDescription);
		setMFErrLogIndi(aiMFIndicator);
		setPCErrLogIndi(aiPCIndicator);
	}
	/**
	 * Returns the value of ErrMsgCat
	 * 
	 * @return  String 
	 */
	public final String getErrMsgCat()
	{
		return csErrMsgCat;
	}
	/**
	 * Returns the value of ErrMsgDesc
	 * 
	 * @return  String 
	 */
	public final String getErrMsgDesc()
	{
		return csErrMsgDesc;
	}
	/**
	 * Returns the value of ErrMsgNo
	 * 
	 * @return  int  
	 */
	public final int getErrMsgNo()
	{
		return ciErrMsgNo;
	}
	/**
	 * Returns the value of ErrMsgType
	 * 
	 * @return  String 
	 */
	public final String getErrMsgType()
	{
		return csErrMsgType;
	}
	/**
	 * Returns the value of MFErrLogIndi
	 * 
	 * @return  int  
	 */
	public final int getMFErrLogIndi()
	{
		return ciMFErrLogIndi;
	}
	/**
	 * Returns the value of PCErrLogIndi
	 * 
	 * @return  int  
	 */
	public final int getPCErrLogIndi()
	{
		return ciPCErrLogIndi;
	}
	/**
	 * This method sets the value of ErrMsgCat.
	 * 
	 * @param asErrMsgCat   String 
	 */
	public final void setErrMsgCat(String asErrMsgCat)
	{
		csErrMsgCat = asErrMsgCat;
	}
	/**
	 * This method sets the value of ErrMsgDesc.
	 * 
	 * @param asErrMsgDesc   String 
	 */
	public final void setErrMsgDesc(String asErrMsgDesc)
	{
		csErrMsgDesc = asErrMsgDesc;
	}
	/**
	 * This method sets the value of ErrMsgNo.
	 * 
	 * @param aiErrMsgNo   int  
	 */
	public final void setErrMsgNo(int aiErrMsgNo)
	{
		ciErrMsgNo = aiErrMsgNo;
	}
	/**
	 * This method sets the value of ErrMsgType.
	 * 
	 * @param asErrMsgType   String 
	 */
	public final void setErrMsgType(String asErrMsgType)
	{
		csErrMsgType = asErrMsgType;
	}
	/**
	 * This method sets the value of MFErrLogIndi.
	 * 
	 * @param aiMFErrLogIndi   int  
	 */
	public final void setMFErrLogIndi(int aiMFErrLogIndi)
	{
		ciMFErrLogIndi = aiMFErrLogIndi;
	}
	/**
	 * This method sets the value of PCErrLogIndi.
	 * 
	 * @param aiPCErrLogIndi   int  
	 */
	public final void setPCErrLogIndi(int aiPCErrLogIndi)
	{
		ciPCErrLogIndi = aiPCErrLogIndi;
	}
}
