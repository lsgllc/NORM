package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created;
 * 							defect 9085 Ver Special Plates  
 * ---------------------------------------------------------------------
*/

/**
 * This Data class contains attributes and get and set methods for 
 * PlateGroupIdData
 *
 * @version	Special Plates	01/31/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		01/31/2007 16:28:00
 */
public class PlateGroupIdData implements Serializable
{
	// int 
	protected int ciCustPltsMaxNo;

	// String
	protected String csPltGrpId;
	protected String csPltGrpDesc;
	protected String csPltGrpDescInet1;
	protected String csPltGrpDescInet2;
	protected String csPltGrpTypeCd;
	
	static final long serialVersionUID = -5788680849556983039L;

	/**
	 * Return value of ciCustPltsMaxNo
	 * 
	 * @return int
	 */
	public int getCustPltsMaxNo()
	{
		return ciCustPltsMaxNo;
	}

	/**
	 * Return value of csPltGrpDesc
	 * 
	 * @return String
	 */
	public String getPltGrpDesc()
	{
		return csPltGrpDesc;
	}

	/**
	 * Return value of csPltGrpDescInet1
	 * 
	 * @return String
	 */
	public String getPltGrpDescInet1()
	{
		return csPltGrpDescInet1;
	}

	/**
	 * Return value of csPltGrpDescInet2
	 * 
	 * @return String
	 */
	public String getPltGrpDescInet2()
	{
		return csPltGrpDescInet2;
	}

	/**
	 * Return value of csPltGrpId
	 * 
	 * @return String
	 */
	public String getPltGrpId()
	{
		return csPltGrpId;
	}

	/**
	 * Return value of 
	 * 
	 * @return String
	 */
	public String getPltGrpTypeCd()
	{
		return csPltGrpTypeCd;
	}

	/**
	 * Set value of ciCustPltsMaxNo
	 * 
	 * @param aiCustPltsMaxNo
	 */
	public void setCustPltsMaxNo(int aiCustPltsMaxNo)
	{
		ciCustPltsMaxNo = aiCustPltsMaxNo;
	}

	/**
	 * Set value of csPltGrpDesc
	 * 
	 * @param asPltGrpDesc
	 */
	public void setPltGrpDesc(String asPltGrpDesc)
	{
		csPltGrpDesc = asPltGrpDesc;
	}

	/**
	 * Set value of csPltGrpDescInet1
	 * 
	 * @param asPltGrpDescInet1
	 */
	public void setPltGrpDescInet1(String asPltGrpDescInet1)
	{
		csPltGrpDescInet1 = asPltGrpDescInet1;
	}

	/**
	 * Set value of csPltGrpDescInet2
	 * 
	 * @param asPltGrpDescInet2
	 */
	public void setPltGrpDescInet2(String asPltGrpDescInet2)
	{
		csPltGrpDescInet2 = asPltGrpDescInet2;
	}

	/**
	 * Set value of csPltGrpId
	 * 
	 * @param asPltGrpId
	 */
	public void setPltGrpId(String asPltGrpId)
	{
		csPltGrpId = asPltGrpId;
	}

	/**
	 * Set value of csPltGrpTypeCd
	 * 
	 * @param asPltGrpTypeCd
	 */
	public void setPltGrpTypeCd(String asPltGrpTypeCd)
	{
		csPltGrpTypeCd = asPltGrpTypeCd;
	}

}
