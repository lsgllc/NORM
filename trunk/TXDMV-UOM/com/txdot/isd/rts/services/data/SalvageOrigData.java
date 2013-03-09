package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * SalvageOrigData.java
 *
 * (c) Texas Department of Transportation 2003
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Class used to retain Salvage Data 
 * 
 * @version	5.2.3		05/19/2005  
 * @author	J Rue
 * <br>Creation Date:	05/22/2003 08:39:50  
 */

public class SalvageOrigData implements java.io.Serializable
{
	// int 
	private int ciLienNotRlsedIndi;
	private int ciLienNotRlsedIndi2;
	private int ciLienNotRlsedIndi3;
	private int ciOwnrEvdncCd;
	private int ciSlvgCd;

	//	Object 
	private RTSDate caSlvgDt;

	// String 
	private String csOthrGovtTtlNo;
	private String csOthrStateCntry;
	private String csSalvYardNo;

	private final static long serialVersionUID = 5611707490090092914L;

	/**
	 * SalvageOrigData constructor comment.
	 */
	public SalvageOrigData()
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
	 * @param aLienNotRlsedIndi int
	 */
	public void setLienNotRlsedIndi(int aLienNotRlsedIndi)
	{
		ciLienNotRlsedIndi = aLienNotRlsedIndi;
	}
	/**
	 * Set value of LienNotRlsedIndi2
	 * 
	 * @param aLienNotRlsedIndi2 int
	 */
	public void setLienNotRlsedIndi2(int aLienNotRlsedIndi2)
	{
		ciLienNotRlsedIndi2 = aLienNotRlsedIndi2;
	}
	/**
	 * Set value of LienNotRlsedIndi3
	 * 
	 * @param aLienNotRlsedIndi3 int
	 */
	public void setLienNotRlsedIndi3(int aLienNotRlsedIndi3)
	{
		ciLienNotRlsedIndi3 = aLienNotRlsedIndi3;
	}
	/**
	 * Set value of OthrGovtTtlNo
	 * 
	 * @param aOthrGovtTtlNo int
	 */
	public void setOthrGovtTtlNo(String aOthrGovtTtlNo)
	{
		csOthrGovtTtlNo = aOthrGovtTtlNo;
	}
	/**
	 * Set value of OthrStateCntry
	 * 
	 * @param aOthrStateCntry String
	 */
	public void setOthrStateCntry(String aOthrStateCntry)
	{
		csOthrStateCntry = aOthrStateCntry;
	}
	/**
	 * Set value of OwnrEvdncCd
	 * 
	 * @param aOwnrEvdncCd int
	 */
	public void setOwnrEvdncCd(int aOwnrEvdncCd)
	{
		ciOwnrEvdncCd = aOwnrEvdncCd;
	}
	/**
	 * Set value of SalvYardNo
	 * 
	 * @param aSalvYardNo String
	 */
	public void setSalvYardNo(String aSalvYardNo)
	{
		csSalvYardNo = aSalvYardNo;
	}
	/**
	 * Set value of SlvgCd
	 * 
	 * @param aSlvgCd int
	 */
	public void setSlvgCd(int aSlvgCd)
	{
		ciSlvgCd = aSlvgCd;
	}
	/**
	 * Set value of SlvgDt
	 * 
	 * @param aSlvgDt RTSDate
	 */
	public void setSlvgDt(RTSDate aSlvgDt)
	{
		caSlvgDt = aSlvgDt;
	}
}
