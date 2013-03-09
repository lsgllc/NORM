package com.txdot.isd.rts.server.systemcontrolbatch;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * MFTrans.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7897 Ver 5.2.3 
 * J Rue		08/22/2008	Add SendTrans Tesing code and Filename
 * 							add SENDTRANSTEST, getFileName(),
 * 								setFileName()
 * 							defect 8984 Ver ELT_MfAccess
 * J Rue		07/08/2009	New variable for identifing if the keep
 * 							TransTime stamp for duplicate transaction
 * 							testing or create a unique transaction
 * 							add csChangeTransTime
 * 							defect 10080 Ver Defect_POS_F
 *----------------------------------------------------------------------
 */

/**
 * MF Trans
 *  
 * @version	Defect_POS_F	07/08/2009
 * @author	Michael Abernethy
 * <br>Creation Date:	  	10/17/01 10:51:48
 */
public class MFTrans
{
	private int ciType;
	public final static int TRANSACTION = 0;
	public final static int LOG_FUNC_TRANS = 1;
	public final static int TRANS_PAYMENT = 2;
	
	// defect 8984
	private String csFileName = CommonConstant.STR_SPACE_EMPTY;
	public final static int SENDTRANSTEST = 3;
	// DEFECT 10080
	//	CHANGETRANSTIME = 0 means NOT to change TransTime stamp. This is 
	//	for testing duplicate transaction from MfAccessTest
	//	CHANGETRANSTIME = 1 means to change TransTime to unique number
	public int csChangeTransTime = 1;
	// end defect 10080
	// end defect 8984
	
	private java.util.Vector cvTransObjects;
	/**
	 * MFTrans constructor comment.
	 */
	public MFTrans()
	{
		super();
	}
	/**
	 * Get Trans Objects
	 * 
	 * @return java.util.Vector
	 */
	public java.util.Vector getTransObjects()
	{
		return cvTransObjects;
	}
	/**
	 * Get Type
	 * 
	 * @return int
	 */
	public int getType()
	{
		return ciType;
	}
	/**
	 * Get File name
	 * 
	 * @return
	 */
	public String getFileName()
	{
		return csFileName;
	}
	/**
	 * Set Trans Objects
	 * 
	 * @param avNewTransObjects java.util.Vector
	 */
	public void setTransObjects(java.util.Vector avNewTransObjects)
	{
		cvTransObjects = avNewTransObjects;
	}
	/**
	 * Set Type
	 * 
	 * @param aiNewType int
	 */
	public void setType(int aiNewType)
	{
		ciType = aiNewType;
	}
	/**
	 * File name
	 *  
	 * @param asFileName
	 */
	public void setFileName(String asFileName)
	{
		csFileName = asFileName;
	}
}
