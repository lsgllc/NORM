package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * MFPartialSpclPltData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		02/02/2007	Add serialVersionUID
 * 							defect 9086 Ver Special Plates
 * J Rue		04/23/2007	Add RegPltCd
 * 							add getRegPltCd, setRegPltCd
 * 							defect 9086 Ver Special Plates
 * J Rue		05/29/2007	Compare expiration periods
 * 							modify compareTo()
 * 							defect 9086 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * MFPartialSpclPltData 
 * 
 * @version	Special Plate	05/29/2007 
 * @author	Jeff Rue
 * <br>Creation Date:		01/31/2007 10:56:00     
 */

public class MFPartialSpclPltData implements Serializable, Comparable
{
   static final long serialVersionUID = -8836108020115872198L;	// int 

	private int ciRegExpMo;
	private int ciRegExpYr;
	private int ciSpclRegId;
	private int ciDelIndi;

	// String
	private String csSpclDocNo;
	private String csPltOwnrName1;
	private String csRegPltNo;
	private String csMFGStatusCd;
	private String csRegPltCd;

	/**
	 * 
	 */
	public MFPartialSpclPltData()
	{
		super();
	}

	/**
	 * Return value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}
	
	/**
	 * Return value of PltOwnrName1
	 * 
	 * @return String
	 */
	public String getPltOwnrName1()
	{
		return csPltOwnrName1;
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
	 * Return value of SpclRegId
	 * 
	 * @return int
	 */
	public int getSpclRegId()
	{
		return ciSpclRegId;
	}
	
	/**
	 * Return value of SpclDocNo
	 * 
	 * @return String
	 */
	public String getSpclDocNo()
	{
		return csSpclDocNo;
	}

	/**
	 * Return value of DelIndi
	 * 
	 * @return int
	 */
	public int getdelIndi()
	{
		return ciDelIndi;
	}
	
	/**
	 * Return value of MFGStatusCd
	 * 
	 * @return String
	 */
	public String getMFGStatusCd()
	{
		return csMFGStatusCd;
	}	

	/**
	 * Set value of RegPltNo
	 * 
	 * @parm String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}
	
	/**
	 * Set value of PltOwnrName1
	 * 
	 * @parm String
	 */
	public void setPltOwnrName1(String asPltOwnrname1)
	{
		csPltOwnrName1 = asPltOwnrname1;
	}
	
	/**
	 * Set value of RegExpMo
	 * 
	 * @parm int
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}
	
	/**
	 * Set value of RegExpYr
	 * 
	 * @parm int
	 */
	public void setRegExpYr(int RegExpYr)
	{
		ciRegExpYr = RegExpYr;
	}
	
	/**
	 * Set value of SpclRegId
	 * 
	 * @parm int
	 */
	public void setSpclRegId(int aiSpclRegIg)
	{
		ciSpclRegId = aiSpclRegIg;
	}
	
	/**
	 * Set value of SpclDocNo
	 * 
	 * @parm String
	 */
	public void setSpclDocNo(String asSpclDocNo)
	{
		csSpclDocNo = asSpclDocNo;
	}

	/**
	 * Set value of DelIndi
	 * 
	 * @parm int
	 */
	public void setDelIndi(int aiDelIndi)
	{
		ciDelIndi = aiDelIndi;
	}
	
	/**
	 * Set value of MFGStatusCd
	 * 
	 * @parm String
	 */
	public void setMFGStatusCd(String asMFGStatus)
	{
		csMFGStatusCd = asMFGStatus;
	}	
	/**
	 * Get RegPltCd
	 * 
	 * @return
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Set RegPltCd
	 * 
	 * @param string
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}
	/**
	 * This method is used to compare expiration periods.
	 * Return -1 if less than
	 * Return  0 if equal to
	 * Return  1 if greater than
	 * 
	 * @param aaObject Object
	 */
	public int compareTo(Object aaObject)
	{

		MFPartialSpclPltData laPartialSpcl = 
			(MFPartialSpclPltData) aaObject;

		int liCurrentValue = ciRegExpYr * 100 + ciRegExpMo;
		int liCompareToValue =
				laPartialSpcl.getRegExpYr() * 100 
					+ laPartialSpcl.getRegExpMo();

		if (liCurrentValue == liCompareToValue)
		{
			return 0;
		}
		if (liCurrentValue < liCompareToValue)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}

}
