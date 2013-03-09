package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * FundsCodesData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/27/2008	Created
 * 							Ver Defect POS A 	
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * FundsCodesData 
 *
 * @version	Defect POS A	03/27/2008
 * @author	K Harrell
 * <br>Creation Date:		03/27/2008  10:04:00 
 */
public class FundsCodesData implements Serializable
{
	private Dollar caCommsnReductnAmt;
	private double cdEntPrcnt;

	private int ciCrdtApprhndCntyIndi;
	private int ciEntGrpId;

	private String csFeeDefrmntCd;
	private String csFundsCat;
	private String csFundsCatDesc;
	private String csFundsCatType;
	private String csFundsRcvngEnt;
	private String csHotCkStatCat;

	private static final long serialVersionUID = -5180745745137696234L;

	/**
	 * Returns value of caCommsnReductnAmt
	 * 
	 * @return Dollar 
	 */
	public Dollar getCommsnReductnAmt()
	{
		return caCommsnReductnAmt;
	}

	/**
	 * Returns value of ciCrdtApprhndCntyIndi
	 * 
	 * @return int
	 */
	public int getCrdtApprhndCntyIndi()
	{
		return ciCrdtApprhndCntyIndi;
	}

	/**
	 * Returns value of ciEntGrpId
	 * 
	 * @return int
	 */
	public int getEntGrpId()
	{
		return ciEntGrpId;
	}

	/**
	 * Returns value of cdEntPrcnt
	 * 
	 * @return double 
	 */
	public double getEntPrcnt()
	{
		return cdEntPrcnt;
	}

	/**
	 * Returns value of csFeeDefrmntCd
	 * 
	 * @return String
	 */
	public String getFeeDefrmntCd()
	{
		return csFeeDefrmntCd;
	}

	/**
	 * Returns value of 
	 * 
	 * @return String
	 */
	public String getFundsCat()
	{
		return csFundsCat;
	}

	/**
	 * Returns value of csFundsCatDesc
	 * 
	 * @return String
	 */
	public String getFundsCatDesc()
	{
		return csFundsCatDesc;
	}

	/**
	 * Returns value of csFundsCatType
	 * 
	 * @return String
	 */
	public String getFundsCatType()
	{
		return csFundsCatType;
	}

	/**
	 * Returns value of csFundsRcvngEnt
	 * 
	 * @return String
	 */
	public String getFundsRcvngEnt()
	{
		return csFundsRcvngEnt;
	}

	/**
	 * Returns value of csHotCkStatCat
	 * 
	 * @return String
	 */
	public String getHotCkStatCat()
	{
		return csHotCkStatCat;
	}

	/**
	 * Sets value of caCommsnReductnAmt
	 * 
	 * @param aaCommsnReductnAmt
	 */
	public void setCommsnReductnAmt(Dollar aaCommsnReductnAmt)
	{
		caCommsnReductnAmt = aaCommsnReductnAmt;
	}

	/**
	 * Sets value of ciCrdtApprhndCntyIndi
	 * 
	 * @param aiCrdtApprhndCntyIndi
	 */
	public void setCrdtApprhndCntyIndi(int aiCrdtApprhndCntyIndi)
	{
		ciCrdtApprhndCntyIndi = aiCrdtApprhndCntyIndi;
	}

	/**
	 * Sets value of ciEntGrpId
	 * 
	 * @param aiEntGrpId
	 */
	public void setEntGrpId(int aiEntGrpId)
	{
		ciEntGrpId = aiEntGrpId;
	}

	/**
	 * Sets value of cdEntPrcnt
	 * 
	 * @param adEntPrcnt
	 */
	public void setEntPrcnt(double adEntPrcnt)
	{
		cdEntPrcnt = adEntPrcnt;
	}

	/**
	 * Sets value of csFeeDefrmntCd
	 * 
	 * @param asFeeDefrmntCd
	 */
	public void setFeeDefrmntCd(String asFeeDefrmntCd)
	{
		csFeeDefrmntCd = asFeeDefrmntCd;
	}

	/**
	 * Sets value of caFundsCat
	 * 
	 * @param asFundsCat
	 */
	public void setFundsCat(String asFundsCat)
	{
		csFundsCat = asFundsCat;
	}

	/**
	 * Sets value of csFundsCatDesc
	 * 
	 * @param asFundsCatDesc
	 */
	public void setFundsCatDesc(String asFundsCatDesc)
	{
		csFundsCatDesc = asFundsCatDesc;
	}

	/**
	 * Sets value of csFundsCatType
	 * 
	 * @param asFundsCatType
	 */
	public void setFundsCatType(String asFundsCatType)
	{
		csFundsCatType = asFundsCatType;
	}

	/**
	 * Sets value of caFundsRcvngEnt
	 * 
	 * @param asFundsRcvngEnt
	 */
	public void setFundsRcvngEnt(String asFundsRcvngEnt)
	{
		csFundsRcvngEnt = asFundsRcvngEnt;
	}

	/**
	 * Sets value of csHotCkStatCat
	 * 
	 * @param asHotCkStatCat
	 */
	public void setHotCkStatCat(String asHotCkStatCat)
	{
		csHotCkStatCat = asHotCkStatCat;
	}

}
