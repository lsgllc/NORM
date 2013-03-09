package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * MFErrorData.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rjanagm	10/25/2001	Created Data object for MF error log
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 * Ray Rowehl	08/22/2005	Move this package to services.data since 
 * 							it is a data package.
 * 							defect 7705 Ver 5.2.3
 *----------------------------------------------------------------------
 */

/**
 * MF Error log data object
 * 
 * @version	5.2.3		08/22/2005
 * @author	Marx Rajangam
 * <br>Creation Date:	10/26/2001 10:49:57
 */

public class MFErrorData implements java.io.Serializable
{
	private com.txdot.isd.rts.services.util.RTSDate caMfDate;
	private java.lang.String csMfTime = CommonConstant.STR_SPACE_EMPTY;
	private int ciComptCntyNo = 0;
	/* WorkStation ID */
	private java.lang.String csWsName = CommonConstant.STR_SPACE_EMPTY;
	private java.lang.String csWsLUName = CommonConstant.STR_SPACE_EMPTY;
	private String csPCDate = CommonConstant.STR_SPACE_EMPTY;
	private java.lang.String csPCTime = CommonConstant.STR_SPACE_EMPTY;
	private java.lang.String csModuleName = CommonConstant.STR_SPACE_EMPTY;
	private java.lang.String csErrNo = CommonConstant.STR_SPACE_EMPTY;
	private String csErrLvl = CommonConstant.STR_SPACE_EMPTY;
	private java.lang.String csErrMsg = CommonConstant.STR_SPACE_EMPTY;
	
	/**
	 * MFErrorData constructor comment.
	 */
	public MFErrorData()
	{
		super();
	}
	
	/**
	 * Get Compt County Number
	 * 
	 * @return int
	 */
	public int getComptCntyNo()
	{
		return ciComptCntyNo;
	}
	
	/**
	 * Get Error Level
	 * 
	 * @return String
	 */
	public String getErrLvl()
	{
		return csErrLvl;
	}
	
	/**
	 * Get Error Message
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getErrMsg()
	{
		return csErrMsg;
	}
	
	/**
	 * Get Error Number
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getErrNo()
	{
		return csErrNo;
	}
	
	/**
	 * Get MF Date
	 * 
	 * @return com.txdot.isd.rts.services.util.RTSDate
	 */
	public com.txdot.isd.rts.services.util.RTSDate getMfDate()
	{
		return caMfDate;
	}
	
	/**
	 * Get MF Time
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMfTime()
	{
		return csMfTime;
	}
	
	/**
	 * Get Module Name
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getModuleName()
	{
		return csModuleName;
	}
	
	/**
	 * Get PC Date
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPCDate()
	{
		return csPCDate;
	}
	
	/**
	 * Get PC Time
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPCTime()
	{
		return csPCTime;
	}
	
	/**
	 * Get Workstation LU Name
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getWsLUName()
	{
		return csWsLUName;
	}
	
	/**
	 * Get Workstation Name
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getWsName()
	{
		return csWsName;
	}
	
	/**
	 * Set Compt County Number
	 * 
	 * @param aiNewComptCntyNo int
	 */
	public void setComptCntyNo(int aiNewComptCntyNo)
	{
		ciComptCntyNo = aiNewComptCntyNo;
	}
	
	/**
	 * Set Error Level
	 * 
	 * @param asNewErrLvl String
	 */
	public void setErrLvl(String asNewErrLvl)
	{
		csErrLvl = asNewErrLvl;
	}
	
	/**
	 * Set Error Message
	 * 
	 * @param asNewErrMsg java.lang.String
	 */
	public void setErrMsg(java.lang.String asNewErrMsg)
	{
		csErrMsg = asNewErrMsg;
	}
	
	/**
	 * Set Error Number
	 * 
	 * @param asNewErrNo java.lang.String
	 */
	public void setErrNo(java.lang.String asNewErrNo)
	{
		csErrNo = asNewErrNo;
	}
	
	/**
	 * Set MF Date
	 * 
	 * @param aaNewMfDate com.txdot.isd.rts.services.util.RTSDate
	 */
	public void setMfDate(
		com.txdot.isd.rts.services.util.RTSDate aaNewMfDate)
	{
		caMfDate = aaNewMfDate;
	}
	
	/**
	 * Set MF Time
	 * 
	 * @param asNewMfTime java.lang.String
	 */
	public void setMfTime(java.lang.String asNewMfTime)
	{
		csMfTime = asNewMfTime;
	}
	
	/**
	 * Set Module Name
	 * 
	 * @param asNewModuleName java.lang.String
	 */
	public void setModuleName(java.lang.String asNewModuleName)
	{
		csModuleName = asNewModuleName;
	}
	
	/**
	 * Set PC Date
	 * 
	 * @param asNewPCDate java.lang.String
	 */
	public void setPCDate(java.lang.String asNewPCDate)
	{
		csPCDate = asNewPCDate;
	}
	
	/**
	 * Set PC Time
	 * 
	 * @param asNewPCTime java.lang.String
	 */
	public void setPCTime(java.lang.String asNewPCTime)
	{
		csPCTime = asNewPCTime;
	}
	
	/**
	 * Set Workstation LU Name
	 * 
	 * @param asNewWsLUName java.lang.String
	 */
	public void setWsLUName(java.lang.String asNewWsLUName)
	{
		csWsLUName = asNewWsLUName;
	}
	
	/**
	 * Set Workstation Name
	 * 
	 * @param asNewWsName java.lang.String
	 */
	public void setWsName(java.lang.String asNewWsName)
	{
		csWsName = asNewWsName;
	}
}
