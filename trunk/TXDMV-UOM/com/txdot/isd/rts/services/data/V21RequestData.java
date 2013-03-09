package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * V21RequestData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/03/2008 	Created 
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/11/2008	DisassociateCd to int
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/19/2008	add csReqIPAddr, get/set methods
 * 							modify ciDisassociateCd to 
 * 							  ciDissociateCd, get/set methods  
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/18/2010	Track passed values of VinaLookupNeeded in 
 * 							V21 Get Vehicle. 
 * 							add cbVinaLookupNeeded, isVinaLookupNeeded(), 
 * 							 setVinaLookupNeeded(), getVinaLookupIndi()
 * 							defect 10337 Ver POS_640 
 * K Harrell	07/23/2010	delete ciV21UniqueId, get/set methods
 * 							add ciV21IntrfcLogId, get/set methods 
 * 							defect 10482 Ver 6.5.0
 * K Harrell	08/22/2011	delete csReqIPAddr, get/set methods 
 * 							defect 10979 Ver 6.8.1  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * V21RequestData
 *
 * @version	6.8.1			08/22/2011
 * @author	Kathy Harrell 
 * @since					02/03/2007  11:55:00 
 */
public class V21RequestData
{

	// RTSDate  
	private RTSDate caReqTimestmp;
	private RTSDate caRespTimestmp;

	// TransactionKey  (TransId) 
	private TransactionKey caTransactionKey;

	// boolean 
	private boolean cbTransRequest;

	// defect 10037 
	private boolean cbVinaLookupNeeded;
	// end defect 10337 
	
	//	Integer
	private int ciDissociateCd;
	private int ciErrMsgNo;
	private int ciRegExpMo;
	private int ciRegExpYr;
	private int ciSuccessfulIndi;
	private int ciV21ReqId;
	private int ciV21IntrfcLogId;
	private int ciVehSoldDate;

	// Long 
	private long clSpclRegId;

	// String 
	private String csDocNo;
	private String csLast4VIN;
	private String csRegPltNo;
	
	// defect 10979 
	//private String csReqIPAddr;
	// end defect 10979 
	private String csRTSTblName;
	private String csV21ReqTypeCd;
	private String csVIN;
	private String csVTNSource;

	/**
	 * Return value of DissociateCd
	 * 
	 * @return int
	 */
	public int getDissociateCd()
	{
		return ciDissociateCd;
	}

	/**
	 * Return value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Returns the value of ErrMsgNo
	 * 
	 * @return int
	 */
	public int getErrMsgNo()
	{
		return ciErrMsgNo;
	}

	/**
	 * Returns the value of Last4VIN
	 * 
	 * @return String 
	 */
	public String getLast4VIN()
	{
		return csLast4VIN;
	}
	/**
	 * Return value of RegExpMo
	 * 
	 * @return int 
	 */
	public int getRegExpMo()
	{
		return ciRegExpMo;
	}

	/**
	 * Return value of RegExpYr
	 * 
	 * @return int
	 */
	public int getRegExpYr()
	{
		return ciRegExpYr;
	}

	/**
	 * Return value of 
	 * 
	 * @return String 
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

//	/**
//	 * Return value of ReqIPAddr
//	 * 
//	 * @return String 
//	 */
//	public String getReqIPAddr()
//	{
//		return csReqIPAddr;
//	}

	/**
	 * Returns the value of ReqTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getReqTimestmp()
	{
		return caReqTimestmp;
	}

	/**
	 * Returns the value of RespTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getRespTimestmp()
	{
		return caRespTimestmp;
	}

	/**
	 * Return value of RTSTblName 
	 * 
	 * @return String 
	 */
	public String getRTSTblName()
	{
		return csRTSTblName;
	}

	/**
	 * Return value of SpclRegId
	 * 
	 * @return long 
	 */
	public long getSpclRegId()
	{
		return clSpclRegId;
	}

	/**
	 * Returns the value of SuccessfulIndi
	 * 
	 * @return int
	 */
	public int getSuccessfulIndi()
	{
		return ciSuccessfulIndi;
	}

	/**
	 * Return value of caTransactionKey
	 * 
	 * @return TransactionKey
	 */
	public TransactionKey getTransactionKey()
	{
		return caTransactionKey;
	}

	/**
	 * Return value of V21ReqId
	 * 
	 * @return int
	 */
	public int getV21ReqId()
	{
		return ciV21ReqId;
	}

	/**
	 * Returns the value of V21ReqTypeCd
	 * 
	 * @return String
	 */
	public String getV21ReqTypeCd()
	{
		return csV21ReqTypeCd;
	}

	/**
	 * Return value of V21IntrfcLogId
	 * 
	 * @return int 
	 */
	public int getV21IntrfcLogId()
	{
		return ciV21IntrfcLogId;
	}

	/**
	 * Return value of VehSoldDate
	 * 
	 * @return int
	 */
	public int getVehSoldDate()
	{
		return ciVehSoldDate;
	}

	/**
	 * Returns the value of VIN
	 * 
	 * @return
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Return 'int' value of VinaLookupNeeded 
	 *
	 * @return int 
	 */
	public int getVinaLookupIndi()
	{
		return cbVinaLookupNeeded ? 1 : 0;
	}

	/**
	 * Return value of VTNSource
	 * 
	 * @return String 
	 */
	public String getVTNSource()
	{
		return csVTNSource;
	}

	/**
	 * Returns boolean indicator for SuccessfulIndi value
	 * 
	 * @return int
	 */
	public boolean isSuccessful()
	{
		return ciSuccessfulIndi == 1;
	}

	/**
	 * Return value of TransRequest
	 * 
	 * @return boolean 
	 */
	public boolean isTransRequest()
	{
		return cbTransRequest;
	}

	/**
	 * Determine value of cbVinaLookupNeeded
	 * 
	 * @return boolean
	 */
	public boolean isVinaLookupNeeded()
	{
		return cbVinaLookupNeeded;
	}

	/**
	 * Set value of DissociateCd
	 * 
	 * @param aiDissociateCd
	 */
	public void setDissociateCd(int aiDissociateCd)
	{
		ciDissociateCd = aiDissociateCd;
	}

	/**
	 * Set value of DocNo
	 * 
	 * @param asDocNo
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Sets the value of ErrMsgNo
	 * 
	 * @param aiErrMsgNo
	 */
	public void setErrMsgNo(int aiErrMsgNo)
	{
		ciErrMsgNo = aiErrMsgNo;
	}

	/**
	 * Sets the value of Last4VIN 
	 * 
	 * @param asLast4VIN
	 */
	public void setLast4VIN(String asLast4VIN)
	{
		csLast4VIN = asLast4VIN;
	}

	/**
	 * Set value of RegExpMo
	 * 
	 * @param aiRegExpMo
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}

	/**
	 * Set value of RegExpYr
	 * 
	 * @param aiRegExpYr
	 */
	public void setRegExpYr(int aiRegExpYr)
	{
		ciRegExpYr = aiRegExpYr;
	}

	/**
	 * Set value of RegPltNo
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

//	/**
//	 * Set value of ReqIPAddr
//	 * 
//	 * @param asReqIPAddr
//	 */
//	public void setReqIPAddr(String asReqIPAddr)
//	{
//		csReqIPAddr = asReqIPAddr;
//	}

	/**
	 * Sets the value of ReqTimestmp
	 * 
	 * @param aaReqTimestmp
	 */
	public void setReqTimestmp(RTSDate aaReqTimestmp)
	{
		caReqTimestmp = aaReqTimestmp;
	}

	/**
	 * Sets the value of RespTimestmp
	 * 
	 * @param aaRespTimestmp
	 */
	public void setRespTimestmp(RTSDate aaRespTimestmp)
	{
		caRespTimestmp = aaRespTimestmp;
	}

	/**
	 * Set value of RTSTblName 
	 * 
	 * @param asRTSTblName
	 */
	public void setRTSTblName(String asRTSTblName)
	{
		csRTSTblName = asRTSTblName;
	}

	/**
	 * Set value of SpclRegId
	 * 
	 * @param alSpclRegId
	 */
	public void setSpclRegId(long alSpclRegId)
	{
		clSpclRegId = alSpclRegId;
	}

	/**
	 * Sets the value of SuccessfulIndi
	 * 
	 * @param aiSuccessfulIndi
	 */
	public void setSuccessfulIndi(int aiSuccessfulIndi)
	{
		ciSuccessfulIndi = aiSuccessfulIndi;
	}

	/**
	 * Set value of caTransactionKey
	 * 
	 * @param aaTransKey
	 */
	public void setTransactionKey(TransactionKey aaTransKey)
	{
		caTransactionKey = aaTransKey;
	}

	/**
	 * Set value of TransRequest
	 * 
	 * @param abTransRequest
	 */
	public void setTransRequest(boolean abTransRequest)
	{
		cbTransRequest = abTransRequest;
	}

	/**
	 * Set value of V21ReqId
	 * 
	 * @param aiV21ReqId
	 */
	public void setV21ReqId(int aiV21ReqId)
	{
		ciV21ReqId = aiV21ReqId;
	}

	/**
	 * Sets the value of V21ReqTypeCd 
	 * 
	 * @param asV21ReqTypeCd
	 */
	public void setV21ReqTypeCd(String asV21ReqTypeCd)
	{
		csV21ReqTypeCd = asV21ReqTypeCd;
	}

	/**
	 * Set value of V21IntrfcLogId
	 * 
	 * @param aiV21IntrfcLogId
	 */
	public void setV21IntrfcLogId(int aiV21IntrfcLogId)
	{
		ciV21IntrfcLogId = aiV21IntrfcLogId;
	}

	/**
	 * Set value of VehSoldDate
	 * 
	 * @param aiVehSoldDate
	 */
	public void setVehSoldDate(int aiVehSoldDate)
	{
		ciVehSoldDate = aiVehSoldDate;
	}

	/**
	 * Sets the value of VIN 
	 * 
	 * @param asVIN
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

	/**
	 * Set value of cbVinaLookupNeeded
	 * 
	 * @param abVinaLookupNeeded
	 */
	public void setVinaLookupNeeded(boolean abVinaLookupNeeded)
	{
		cbVinaLookupNeeded = abVinaLookupNeeded;
	}

	/**
	 * Set value of VTNSource
	 * 
	 * @param asVTNSource
	 */
	public void setVTNSource(String asVTNSource)
	{
		csVTNSource = asVTNSource;
	}
}
