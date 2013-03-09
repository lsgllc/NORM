package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * PermitSearchKeys.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/09/2010	Created
 * 							defect 10492 Ver 6.5.0
 * ---------------------------------------------------------------------
 */

/**
 * Data class to facilitate assignment of Key Data for Permit Searcyh
 *
 * @version	6.5.0			06/09/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		06/09/2010 
 */
public class PermitSearchKeys implements Serializable
{

	private int ciBeginDate;
	private int ciEndDate;

	private String csCustBsnName = new String();
	private String csCustLstName = new String();
	private String csPrmtIssuanceId = new String();
	private String csPrmtNo = new String();
	private String csPrmtVIN = new String();

	static final long serialVersionUID = 7150852058736036569L;

	/**
	 * PermitSearchKeys.java Constructor
	 * 
	 */
	public PermitSearchKeys()
	{
		csPrmtIssuanceId = new String();
		csPrmtNo = new String();
		csCustLstName = new String();
		csCustBsnName = new String();
		csPrmtVIN = new String();
	}

	/**
	 * PermitSearchKeys.java Constructor
	 * 
	 * @param aaGSD  
	 */
	public PermitSearchKeys(GeneralSearchData aaGSD)
	{
		super();

		String lsKey = aaGSD.getKey1();
		String lsValue = aaGSD.getKey2();
		if (lsKey.equals(CommonConstant.PRMT_PRMTNO))
		{
			csPrmtNo = lsValue;
		}
		else if (lsKey.equals(CommonConstant.PRMT_PRMTID))
		{
			csPrmtIssuanceId = lsValue;
		}
		else if (lsKey.equals(CommonConstant.PRMT_VIN))
		{
			csPrmtVIN = lsValue;
		}
		else if (lsKey.equals(CommonConstant.PRMT_BSNNAME))
		{
			csCustBsnName = lsValue;
		}
		else if (lsKey.equals(CommonConstant.PRMT_LSTNAME))
		{
			csCustLstName = lsValue;
		}
		if (aaGSD.getDate1() != null)
		{
			ciBeginDate = aaGSD.getDate1().getYYYYMMDDDate();
		}
		if (aaGSD.getDate2() != null)
		{
			ciEndDate = aaGSD.getDate2().getYYYYMMDDDate();
		}
	}

	/**
	 * Gets value of ciBeginDate
	 * 
	 * @return int
	 */
	public int getBeginDate()
	{
		return ciBeginDate;
	}

	/**
	 * Gets value of csCustBsnName
	 * 
	 * @return String
	 */
	public String getCustBsnName()
	{
		return csCustBsnName;
	}

	/**
	 * Gets value of csCustBsnName
	 * 
	 * @return String
	 */
	public String getCustLstName()
	{
		return csCustLstName;
	}

	/**
	 * Gets value of ciEndDate
	 * 
	 * @return int
	 */
	public int getEndDate()
	{
		return ciEndDate;
	}

	/**
	 * Gets value of csPrmtIssuanceId
	 * 
	 * @return String
	 */
	public String getPrmtIssuanceId()
	{
		return csPrmtIssuanceId;
	}

	/**
	 * Gets value of csPrmtNo
	 * 
	 * @return String
	 */
	public String getPrmtNo()
	{
		return csPrmtNo;
	}

	/**
	 * Gets value of csPrmtVIN
	 * 
	 * @return String
	 */
	public String getPrmtVIN()
	{
		return csPrmtVIN;
	}

	/**
	 * Sets value of ciBeginDate
	 * 
	 * @param aiBeginDate
	 */
	public void setBeginDate(int aiBeginDate)
	{
		ciBeginDate = aiBeginDate;
	}

	/**
	 * Sets value of csCustBsnName
	 * 
	 * @param asCustBsnName
	 */
	public void setCustBsnName(String asCustBsnName)
	{
		csCustBsnName = asCustBsnName;
	}

	/**
	 * Sets value of csCustLstName
	 * 
	 * @param asCustLstName
	 */
	public void setCustLstName(String asCustLstName)
	{
		csCustLstName = asCustLstName;
	}

	/**
	 * Sets value of ciEndDate
	 * 
	 * @param aiEndDate
	 */
	public void setEndDate(int aiEndDate)
	{
		ciEndDate = aiEndDate;
	}

	/**
	 * Sets value of csPrmtIssuanceId
	 * 
	 * @param asPrmtIssuanceId
	 */
	public void setPrmtIssuanceId(String asPrmtIssuanceId)
	{
		csPrmtIssuanceId = asPrmtIssuanceId;
	}

	/**
	 * Sets value of csPrmtNo
	 * 
	 * @param asPrmtNo
	 */
	public void setPrmtNo(String asPrmtNo)
	{
		csPrmtNo = asPrmtNo;
	}

	/**
	 * Sets value of csPrmtVIN
	 * 
	 * @param asPrmtVIN
	 */
	public void setPrmtVIN(String asPrmtVIN)
	{
		csPrmtVIN = asPrmtVIN;
	}

}
