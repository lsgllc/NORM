package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * SalvageData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B Woodson	01/31/2012	add ciSalvIndi, get/set methods 
 * 							defect 11251 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Class used to retain Salvage Data 
 * 
 * @version	5.2.3		01/31/2012  
 * @author	Administrator
 * <br>Creation Date:	08/22/2001 10:43:50   
 */

public class SalvageData implements java.io.Serializable
{
	// int 
	private int ciLienNotRlsedIndi;
	private int ciLienNotRlsedIndi2;
	private int ciLienNotRlsedIndi3;
	private int ciOwnrEvdncCd;
	private int ciSlvgCd;
	private int ciSalvIndi;

	// Object 
	private RTSDate caSlvgDt;

	//	String 		
	private String csOthrGovtTtlNo;
	private String csOthrStateCntry;
	private String csSalvYardNo;

	private final static long serialVersionUID = 5611707490090092914L;
	/**
	 * SalvageData constructor comment.
	 */
	public SalvageData()
	{
		super();
	}
	/**
	 * Return value of LienNotRlsedIndi
	 * 
	 * @return int
	 */
	public int getLienNotRlsedIndi()
	{
		return ciLienNotRlsedIndi;
	}
	/**
	 * Return value of LienNotRlsedIndi2
	 * 
	 * @return int
	 */
	public int getLienNotRlsedIndi2()
	{
		return ciLienNotRlsedIndi2;
	}
	/**
	 * Return value of LienNotRlsedIndi3
	 * 
	 * @return int
	 */
	public int getLienNotRlsedIndi3()
	{
		return ciLienNotRlsedIndi3;
	}
	/**
	 * Return value of OthrGovtTtlNo
	 * 
	 * @return int
	 */
	public String getOthrGovtTtlNo()
	{
		return csOthrGovtTtlNo;
	}
	/**
	 * Return value of OthrStateCntry
	 * 
	 * @return String
	 */
	public String getOthrStateCntry()
	{
		return csOthrStateCntry;
	}
	/**
	 * Return value of OwnrEvdncCd
	 * 
	 * @return int
	 */
	public int getOwnrEvdncCd()
	{
		return ciOwnrEvdncCd;
	}
	
	/**
	 * @return the ciSalvIndi
	 */
	public int getSalvIndi()
	{
		return ciSalvIndi;
	}
	
	/**
	 * Return value of SalvYardNo
	 * 
	 * @return String
	 */
	public String getSalvYardNo()
	{
		return csSalvYardNo;
	}
	/**
	 * Return value of SlvgCd
	 * 
	 * @return int
	 */
	public int getSlvgCd()
	{
		return ciSlvgCd;
	}
	/**
	 * Return value of SlvgDt
	 * 
	 * @return RTSDate
	 */
	public RTSDate getSlvgDt()
	{
		return caSlvgDt;
	}
	/**
	 * Set value of LienNotRlsedIndi
	 * 
	 * @param aiLienNotRlsedIndi int
	 */
	public void setLienNotRlsedIndi(int aiLienNotRlsedIndi)
	{
		ciLienNotRlsedIndi = aiLienNotRlsedIndi;
	}
	/**
	 * Set value of LienNotRlsedIndi2
	 * 
	 * @param aiLienNotRlsedIndi2 int
	 */
	public void setLienNotRlsedIndi2(int aiLienNotRlsedIndi2)
	{
		ciLienNotRlsedIndi2 = aiLienNotRlsedIndi2;
	}
	/**
	 * Set value of LienNotRlsedIndi3
	 * 
	 * @param aiLienNotRlsedIndi3 int
	 */
	public void setLienNotRlsedIndi3(int aiLienNotRlsedIndi3)
	{
		ciLienNotRlsedIndi3 = aiLienNotRlsedIndi3;
	}
	/**
	 * Set value of OthrGovtTtlNo
	 * 
	 * @param asOthrGovtTtlNo int
	 */
	public void setOthrGovtTtlNo(String asOthrGovtTtlNo)
	{
		csOthrGovtTtlNo = asOthrGovtTtlNo;
	}
	/**
	 * Set value of OwnrEvdncCd
	 * 
	 * @param asOthrStateCntry String
	 */
	public void setOthrStateCntry(String asOthrStateCntry)
	{
		csOthrStateCntry = asOthrStateCntry;
	}
	/**
	 * Set value of OwnrEvdncCd
	 * 
	 * @param aiOwnrEvdncCd int
	 */
	public void setOwnrEvdncCd(int aiOwnrEvdncCd)
	{
		ciOwnrEvdncCd = aiOwnrEvdncCd;
	}
	
	/**
	 * @param aiSalvIndi t
	 */
	public void setSalvIndi(int aiSalvIndi)
	{
		ciSalvIndi = aiSalvIndi;
	}
	
	/**
	 * Set value of SalvYardNo 
	 * 
	 * @param asSalvYardNo String
	 */
	public void setSalvYardNo(String asSalvYardNo)
	{
		csSalvYardNo = asSalvYardNo;
	}
	/**
	 * Set value of SlvgCd
	 * 
	 * @param aiSlvgCd int
	 */
	public void setSlvgCd(int aiSlvgCd)
	{
		ciSlvgCd = aiSlvgCd;
	}
	/**
	 * Set value of SlvgDt
	 * 
	 * @param aaSlvgDt RTSDate
	 */
	public void setSlvgDt(RTSDate aaSlvgDt)
	{
		caSlvgDt = aaSlvgDt;
	}
}
