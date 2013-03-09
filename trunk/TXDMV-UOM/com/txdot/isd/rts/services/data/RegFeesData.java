package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * RegFeesData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/17/2001	Make class serializable
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B. Brown		08/19/2005  Regain removed fields needed due to Complete
 * 							TransactionData being stored in table. 
 * 							rts_itrnt_data.
 * 							add cvVectFees
 * 							add getCvVectFees() 
 * 							modify getVectFees()			
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get and set methods for 
 * RegFeesData 
 * 
 * @version	5.2.3		08/19/2005 
 * @author	
 * <br>Creation Date:	08/30/2001 16:01:09
 */

public class RegFeesData implements java.io.Serializable
{
	// int
	private int ciExpMaxMonths; // Trans Restore
	private int ciExpMinMonths; // Trans Restore
	private int ciFromMo;
	private int ciFromYr;
	private int ciMaxExpMoYr;
	private int ciMinExpMoYr;
	private int ciPltAge;
	private int ciStkrNo;
	private int ciToMonthDflt; // Trans Restore
	private int ciToMonthMax;
	private int ciToMonthMin;
	private int ciToYearDflt; // Trans Restore
	private int ciToYearMax;
	private int ciToYearMin;

	//	Object 
	private Dollar caFeeTotalMax;
	private Dollar caFeeTotalMin;

	// String
	private String csExpMoYrMin;
	private String csExpMoYrMax;
	private String csHdrDesc = null;
	private String csPltNo;
	private String csReason;
	private String csTransCd; // Trans Restore

	// Vector
	private Vector cvFees;
	
	// defect 7889
	private Vector cvVectFees;
	// end defect 7889

	private final static long serialVersionUID = -6155275335895274947L;
	/**
	 * RegFeesData constructor comment.
	 */
	public RegFeesData()
	{
		super();
		cvFees = new Vector();
		//cvVectFees = new Vector();
	}
	/**
	 * Return value of ExpMaxMonths
	 * 
	 * @return int
	 */
	public int getExpMaxMonths()
	{
		return ciExpMaxMonths;
	}
	/**
	 * Return value of ExpMinMonths
	 * 
	 * @return int
	 */
	public int getExpMinMonths()
	{
		return ciExpMinMonths;
	}
	/**
	 * Return value of ExpMoYrMax
	 * 
	 * @return String
	 */
	public String getExpMoYrMax()
	{
		return csExpMoYrMax;
	}
	/**
	 * Return value of ExpMoYrMin 
	 * 
	 * @return String
	 */
	public String getExpMoYrMin()
	{
		return csExpMoYrMin;
	}
	/**
	 * Return value of FeeTotalMax
	 * 
	 * @return Dollar
	 */
	public Dollar getFeeTotalMax()
	{
		return caFeeTotalMax;
	}
	/**
	 * Return value of FeeTotalMin
	 * 
	 * @return Dollar
	 */
	public Dollar getFeeTotalMin()
	{
		return caFeeTotalMin;
	}
	/**
	 * Return value of FromMo
	 * 
	 * @return int
	 */
	public int getFromMo()
	{
		return ciFromMo;
	}
	/**
	 * Return value of FromYr
	 * 
	 * @return int
	 */
	public int getFromYr()
	{
		return ciFromYr;
	}
	/**
	 * Return value of HdrDesc
	 * 
	 * @return String
	 */
	public String getHdrDesc()
	{
		return csHdrDesc;
	}
	/**
	 * Return value of MaxExpMoYr 
	 * 
	 * @return int
	 */
	public int getMaxExpMoYr()
	{
		return ciMaxExpMoYr;
	}
	/**
	 * Return value of MinExpMoYr
	 * 
	 * @return int
	 */
	public int getMinExpMoYr()
	{
		return ciMinExpMoYr;
	}
	/**
	 * Return value of PltAge
	 * 
	 * @return int
	 */
	public int getPltAge()
	{
		return ciPltAge;
	}
	/**
	 * Return value of PltNo
	 * 
	 * @return String
	 */
	public String getPltNo()
	{
		return csPltNo;
	}
	/**
	 * Return value of Reason
	 * 
	 * @return String
	 */
	public String getReason()
	{
		return csReason;
	}
	/**
	 * Return value of StkrNo
	 * 
	 * @return int
	 */
	public int getStkrNo()
	{
		return ciStkrNo;
	}
	/**
	 * Return value of ToMonthDflt
	 * 
	 * @return int
	 */
	public int getToMonthDflt()
	{
		return ciToMonthDflt;
	}
	/**
	 * Return value of ToMonthMax
	 * 
	 * @return int
	 */
	public int getToMonthMax()
	{
		return ciToMonthMax;
	}
	/**
	 * Return value of ToMonthMin
	 * 
	 * @return int
	 */
	public int getToMonthMin()
	{
		return ciToMonthMin;
	}
	/**
	 * Return value of ToYearDflt
	 * 
	 * @return int
	 */
	public int getToYearDflt()
	{
		return ciToYearDflt;
	}
	/**
	 * Return value of ToYearMax
	 * 
	 * @return int
	 */
	public int getToYearMax()
	{
		return ciToYearMax;
	}
	/**
	 * Return value of ToYearMin
	 * 
	 * @return int
	 */
	public int getToYearMin()
	{
		return ciToYearMin;
	}
	/**
	 * Return value of TransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}
	/**
	 * Return value of Fees
	 * 
	 * @return Vector
	 */
	public Vector getVectFees()
	{
		// defect 7889
		if (cvFees==null)
		{
			setVectFees(getCvVectFees());
		}
		// end defect 7889
		return cvFees;
	}
	/**
	 * Set value of ExpMaxMonths
	 * 
	 * @param aiExpMaxMonths int
	 */
	public void setExpMaxMonths(int aiExpMaxMonths)
	{
		ciExpMaxMonths = aiExpMaxMonths;
	}
	/**
	 * Set value of ExpMinMonths
	 * 
	 * @param aiExpMinMonths int
	 */
	public void setExpMinMonths(int aiExpMinMonths)
	{
		ciExpMinMonths = aiExpMinMonths;
	}
	/**
	 * Set value of ExpMoYrMax
	 * 
	 * @param asExpMoYrMax String
	 */
	public void setExpMoYrMax(String asExpMoYrMax)
	{
		csExpMoYrMax = asExpMoYrMax;
	}
	/**
	 * Set value of ExpMoYrMin
	 * 
	 * @param asExpMoYrMin String
	 */
	public void setExpMoYrMin(String asExpMoYrMin)
	{
		csExpMoYrMin = asExpMoYrMin;
	}
	/**
	 * Set value of FeeTotalMax 
	 * 
	 * @param aaFeeTotalMax Dollar
	 */
	public void setFeeTotalMax(Dollar aaFeeTotalMax)
	{
		caFeeTotalMax = aaFeeTotalMax;
	}
	/**
	 * Set value of FeeTotalMin
	 * 
	 * @param aaFeeTotalMin Dollar
	 */
	public void setFeeTotalMin(Dollar aaFeeTotalMin)
	{
		caFeeTotalMin = aaFeeTotalMin;
	}
	/**
	 * Set value of FromMo
	 * 
	 * @param aiFromMo int
	 */
	public void setFromMo(int aiFromMo)
	{
		ciFromMo = aiFromMo;
	}
	/**
	 * Set value of FromYr
	 * 
	 * @param aiFromYr int
	 */
	public void setFromYr(int aiFromYr)
	{
		ciFromYr = aiFromYr;
	}
	/**
	 * Set value of HdrDesc
	 * 
	 * @param asHdrDesc String
	 */
	public void setHdrDesc(String asHdrDesc)
	{
		csHdrDesc = asHdrDesc;
	}
	/**
	 * Set value of MaxExpMoYr 
	 * 
	 * @param aiMaxExpMoYr int
	 */
	public void setMaxExpMoYr(int aiMaxExpMoYr)
	{
		ciMaxExpMoYr = aiMaxExpMoYr;
	}
	/**
	 * Set value of MinExpMoYr
	 * 
	 * @param aiMinExpMoYr int
	 */
	public void setMinExpMoYr(int aiMinExpMoYr)
	{
		ciMinExpMoYr = aiMinExpMoYr;
	}
	/**
	 * Set value of PltAge
	 * 
	 * @param aiPltAge int
	 */
	public void setPltAge(int aiPltAge)
	{
		ciPltAge = aiPltAge;
	}
	/**
	 * Set value of PltNo
	 * 
	 * @param asPltNo String
	 */
	public void setPltNo(String asPltNo)
	{
		csPltNo = asPltNo;
	}
	/**
	 * Set value of Reason
	 * 
	 * @param asReason String
	 */
	public void setReason(String asReason)
	{
		csReason = asReason;
	}
	/**
	 * Set value of StkrNo
	 * 
	 * @param aiStkrNo int
	 */
	public void setStkrNo(int aiStkrNo)
	{
		ciStkrNo = aiStkrNo;
	}
	/**
	 * Set value of ToMonthDflt
	 * 
	 * @param aiToMonthDflt int
	 */
	public void setToMonthDflt(int aiToMonthDflt)
	{
		ciToMonthDflt = aiToMonthDflt;
	}
	/**
	 * Set value of ToMonthMax
	 * 
	 * @param aiToMonthMax int
	 */
	public void setToMonthMax(int aiToMonthMax)
	{
		ciToMonthMax = aiToMonthMax;
	}
	/**
	 * Set value of ToMonthMin
	 * 
	 * @param aiToMonthMin int
	 */
	public void setToMonthMin(int aiToMonthMin)
	{
		ciToMonthMin = aiToMonthMin;
	}
	/**
	 * Set value of ToYearDflt
	 * 
	 * @param aToYearDflt int
	 */
	public void setToYearDflt(int aToYearDflt)
	{
		ciToYearDflt = aToYearDflt;
	}
	/**
	 * Set value of ToYearMax
	 * 
	 * @param aiToYearMax int
	 */
	public void setToYearMax(int aiToYearMax)
	{
		ciToYearMax = aiToYearMax;
	}
	/**
	 * Set value of ToYearMin
	 * 
	 * @param aiToYearMin int
	 */
	public void setToYearMin(int aiToYearMin)
	{
		ciToYearMin = aiToYearMin;
	}
	/**
	 * Set value of TransCd
	 * 
	 * @param asTransCd String
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}
	/**
	 * Set value of VectFees
	 * 
	 * @param avVectFees Vector
	 */
	public void setVectFees(Vector avVectFees)
	{
		cvFees = avVectFees;
	}
	
	/**
	 * Return value of cvVectFees
	 * 
	 * @return Vector
	 * @deprecated
	 */
	public Vector getCvVectFees()
	{
		return cvVectFees;
	}

}
