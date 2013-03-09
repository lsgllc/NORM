package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * CountyCalendarYearData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	08/03/2003	Added csCntyTERPStatusCd
 *                          defect 6447
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 							  
 * B Hargrove	07/10/2008	Add new local fee: Mobility Fee (NOT the  
 * 							same as Title TERP Mobility Fee from 
 * 							defect 8552). 
 * 							add caMobilityFee, getMobilityFee(), 
 * 							setMobilityFee() 
 * 							defect 9728 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * CountyCalendarYearData
 *
 * @version	MyPlates_POS	07/10/2008 
 * @author	Joe Peters
 * <br>Creation Date:		08/30/2001
 */

public class CountyCalendarYearData implements Serializable
{
	// double 
	protected double cdSalesTaxCrdt;

	// int 
	protected int ciCntyRdMilesNo;
	protected int ciFscalYr;
	protected int ciOfcIssuanceNo;

	// Object 
	protected Dollar caChildSaftyFee;
	protected Dollar caCntyRdBrdgFee;
	// defect 9728
	protected Dollar caMobilityFee;
	// defect 9728

	// String 
	protected String csCntySizeCd;
	protected String csCntyTERPStatusCd;

	private final static long serialVersionUID = -5848679478332575576L;
	/**
	 * Returns the value of ChildSaftyFee
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getChildSaftyFee()
	{
		return caChildSaftyFee;
	}
	/**
	 * Returns the value of CntyRdBrdgFee
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getCntyRdBrdgFee()
	{
		return caCntyRdBrdgFee;
	}
	/**
	 * Returns the value of MobilityFee
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getMobilityFee()
	{
		return caMobilityFee;
	}
	/**
	 * Returns the value of CntyRdMilesNo
	 * 
	 * @return  int  
	 */
	public final int getCntyRdMilesNo()
	{
		return ciCntyRdMilesNo;
	}
	/**
	 * Returns the value of CntySizeCd
	 * 
	 * @return  String 
	 */
	public final String getCntySizeCd()
	{
		return csCntySizeCd;
	}
	/**
	 * Returns the value of CntyTERPStatusCd
	 * 
	 * @return  String 
	 */
	public String getCntyTERPStatusCd()
	{
		return csCntyTERPStatusCd;
	}
	/**
	 * Returns the value of FscalYr
	 * 
	 * @return  int  
	 */
	public final int getFscalYr()
	{
		return ciFscalYr;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int  
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of SalesTaxCrdt
	 * 
	 * @return  double 
	 */
	public final double getSalesTaxCrdt()
	{
		return cdSalesTaxCrdt;
	}
	/**
	 * This method sets the value of ChildSaftyFee.
	 * 
	 * @param aaChildSaftyFee   Dollar  
	 */
	public final void setChildSaftyFee(Dollar aaChildSaftyFee)
	{
		caChildSaftyFee = aaChildSaftyFee;
	}
	/**
	 * This method sets the value of CntyRdBrdgFee.
	 * 
	 * @param aaCntyRdBrdgFee   Dollar  
	 */
	public final void setCntyRdBrdgFee(Dollar aaCntyRdBrdgFee)
	{
		caCntyRdBrdgFee = aaCntyRdBrdgFee;
	}
	/**
	 * This method sets the value of MobilityFee.
	 * 
	 * @param aaMobilityFee   Dollar  
	 */
	public final void setMobilityFee(Dollar aaMobilityFee)
	{
		caMobilityFee = aaMobilityFee;
	}
	/**
	 * This method sets the value of CntyRdMilesNo.
	 * 
	 * @param aiCntyRdMilesNo   int  
	 */
	public final void setCntyRdMilesNo(int aiCntyRdMilesNo)
	{
		ciCntyRdMilesNo = aiCntyRdMilesNo;
	}
	/**
	 * This method sets the value of CntySizeCd.
	 * 
	 * @param asCntySizeCd   String 
	 */
	public final void setCntySizeCd(String asCntySizeCd)
	{
		csCntySizeCd = asCntySizeCd;
	}
	/**
	 * This method sets the value of CntyTERPStatusCd
	 * 
	 * @param asCntySizeCd   String 
	 */
	public void setCntyTERPStatusCd(String asCntyTERPStatusCd)
	{
		csCntyTERPStatusCd = asCntyTERPStatusCd;
	}
	/**
	 * This method sets the value of FscalYr.
	 * 
	 * @param aiFscalYr   int  
	 */
	public final void setFscalYr(int aiFscalYr)
	{
		ciFscalYr = aiFscalYr;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int  
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of SalesTaxCrdt.
	 * 
	 * @param adSalesTaxCrdt   double 
	 */
	public final void setSalesTaxCrdt(double adSalesTaxCrdt)
	{
		cdSalesTaxCrdt = adSalesTaxCrdt;
	}
}
