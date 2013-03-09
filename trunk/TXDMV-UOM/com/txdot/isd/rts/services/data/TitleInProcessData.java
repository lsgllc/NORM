package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * TitleInProcessData.java  
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Rajangam	10/15/2001	Created for MfAccess method
 * 							retrieveTitleInProcessData
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3	
 * K Harrell	10/04/2010	add cvInProcsTransDataList, get/set methods.
 * 							add hasInProcsTrans() 
 * 							defect 10598 Ver 6.6.0  	
 * ---------------------------------------------------------------------
 */
/**
 * Title Reject/Release information class 
 
 *  
 * @version	6.6.0 		10/04/2010 
 * @author		Marx Rajangam
 * <br>Creation Date:	10/15/2001 13:28:57  
 */

public class TitleInProcessData implements java.io.Serializable
{

	// int
	private int ciDPSStlnIndi;
	private int ciOfcIssuanceNo;
	private int ciOwnrshpEvidCd;

	// String 
	private String csDocNo;
	private String csOwnrTtlName1;
	private String csVehMk;
	private String csVIN;

	// defect 10598 
	private Vector cvInProcsTransDataList = new Vector();
	// end defect 10598
	
	// Object 
	private RTSDate caTransAMDate;
	
	private final static long serialVersionUID = 3742910014890157074L;
	
	/**
	 * TitleInProcessData constructor comment.
	 */
	public TitleInProcessData()
	{
		super();
	}
	/**
	 * Return the value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}
	/**
	 * Return the value of DPSStlnIndi
	 * 
	 * @return int
	 */
	public int getDPSStlnIndi()
	{
		return ciDPSStlnIndi;
	}
	/**
	 * Return cvInProcsTransDataList
	 * 
	 * @return Vector 
	 */
	public Vector getInProcsTransDataList()
	{
		return cvInProcsTransDataList;
	}
	/**
	 * Return the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return the value of OwnrshpEvidCd
	 * 
	 * @return int
	 */
	public int getOwnrshpEvidCd()
	{
		return ciOwnrshpEvidCd;
	}
	/**
	 * Return the value of OwnrTtlName1
	 * 
	 * @return String
	 */
	public String getOwnrTtlName1()
	{
		return csOwnrTtlName1;
	}
	/**
	 * Return the value of TransAMDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getTransAMDate()
	{
		return caTransAMDate;
	}
	/**
	 * Return the value of VehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}
	/**
	 * Return the value of VIN
	 * 
	 * @return String
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Has In Process Transactions
	 * 
	 * @return boolean 
	 */
	public boolean hasInProcsTrans()
	{
		return cvInProcsTransDataList != null
			&& cvInProcsTransDataList.size() > 0;
	}
	/**
	 * Set the value of DocNo
	 * 
	 * @param asDocNo String
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}
	/**
	 * Set the value of DPSStlnIndi
	 * 
	 * @param aiDPSStlnIndi int
	 */
	public void setDPSStlnIndi(int aiDPSStlnIndi)
	{
		ciDPSStlnIndi = aiDPSStlnIndi;
	}
	/**
	 * Set value of cvInProcsTransDataList
	 * 
	 * @param avInProcsTransDataList
	 */
	public void setInProcsTransDataList(Vector avInProcsTransDataList)
	{
		cvInProcsTransDataList = avInProcsTransDataList;
	}
	/**
	 * Set the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Set the value of OwnrshpEvidCd
	 * 
	 * @param aiOwnrshpEvidCd int
	 */
	public void setOwnrshpEvidCd(int aiOwnrshpEvidCd)
	{
		ciOwnrshpEvidCd = aiOwnrshpEvidCd;
	}
	/**
	 * Set the value of OwnrTtlName1
	 * 
	 * @param asOwnrTtlName1 String
	 */
	public void setOwnrTtlName1(String asOwnrTtlName1)
	{
		csOwnrTtlName1 = asOwnrTtlName1;
	}
	/**
	 * Set the value of TransAMDate
	 * 
	 * @param aaTransAMDate RTSDate
	 */
	public void setTransAMDate(RTSDate aaTransAMDate)
	{
		caTransAMDate = aaTransAMDate;
	}
	/**
	 * Set the value of VehMk
	 * 
	 * @param asVehMk String
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}
	/**
	 * Set the value of VIN
	 * 
	 * @param asVIN String
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}
}
