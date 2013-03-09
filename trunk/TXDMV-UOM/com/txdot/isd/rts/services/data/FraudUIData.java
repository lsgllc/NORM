package com.txdot.isd.rts.services.data;
import java.io.Serializable;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * FraudReportUIData.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/06/2011	Created
 * 							defect 10900 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * FraudUIData
 *
 * @version	6.8.0			06/06/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/06/2011	17:17:17 
 */
public class FraudUIData implements Serializable
{

	private RTSDate caBeginDate;
	private RTSDate caEndDate;
	private FraudStateData caFraudTypeData;

	private int ciTransType;

	private String csAddlSearch;
	private String csSearchKey;

	public final static int ADD = 1;
	public final static int ALL = 2;
	public final static int DELETE = 0;

	static final long serialVersionUID = 6657558705784061138L;

	/**
	 * FraudReportUIData.java Constructor
	 * 
	 */
	public FraudUIData()
	{
		super();
	}

	/**
	 * Return value of csAddlSearch
	 * 
	 * @return String 
	 */
	public String getAddlSearch()
	{
		return csAddlSearch;
	}

	/**
	 * Return value of caBeginDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBeginDate()
	{
		return caBeginDate;
	}

	/**
	 * Return value of caEndDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getEndDate()
	{
		return caEndDate;
	}

	/**
	 * Return value of caFraudTypeData
	 * 
	 * @return FraudStateData
	 */
	public FraudStateData getFraudTypeData()
	{
		return caFraudTypeData;
	}

	/**
	 * Return value of csSearchKey
	 * 
	 * @return String
	 */
	public String getSearchKey()
	{
		return csSearchKey;
	}

	/**
	 * Return value of ciTransType
	 * 
	 * @return int
	 */
	public int getTransType()
	{
		return ciTransType;
	}
	
	/**
	 * Return Where Clause Data 
	 * 
	 */
	public String getWhereClauseData()
	{
		String lsWhereClause =
			"TransAMDate between "
				+ caBeginDate.getAMDate()
				+ " and "
				+ caEndDate.getAMDate();

		if (ciTransType != ALL)
		{
			lsWhereClause =
				lsWhereClause + " and AddFraudIndi = " + ciTransType;
		}

		if (!caFraudTypeData.isAllTypes())
		{
			lsWhereClause =
				lsWhereClause
					+ " and A.FraudCd in ("
					+ caFraudTypeData.getInClauseParms()
					+ ") ";
		}

		if (!UtilityMethods.isEmpty(csAddlSearch))
		{
			lsWhereClause =
				lsWhereClause
					+ " and "
					+ csAddlSearch
					+ " = '"
					+ csSearchKey.trim()
					+ "'";
		}

		return lsWhereClause;
	}

	/**
	 * Set value of csAddlSearch
	 * 
	 * @param asAddlSearch
	 */
	public void setAddlSearch(String asAddlSearch)
	{
		csAddlSearch = asAddlSearch;
	}
	/**
	 * Set value of caBeginDate
	 * 
	 * @param aaBeginDate
	 */
	public void setBeginDate(RTSDate aaBeginDate)
	{
		caBeginDate = aaBeginDate;
	}

	/**
	 * Set value of caEndDate
	 * 
	 * @param aaEndDate
	 */
	public void setEndDate(RTSDate aaEndDate)
	{
		caEndDate = aaEndDate;
	}

	/**
	 * Set value of caFraudTypeData
	 * 
	 * @param aaFraudTypeData
	 */
	public void setFraudTypeData(FraudStateData aaFraudTypeData)
	{
		caFraudTypeData = aaFraudTypeData;
	}

	/**
	 * Set value of csSearchKey
	 * 
	 * @param asSearchKey
	 */
	public void setSearchKey(String asSearchKey)
	{
		csSearchKey = asSearchKey;
	}

	/**
	 * Set value of ciTransType
	 * 
	 * @param aiTransType
	 */
	public void setTransType(int aiTransType)
	{
		ciTransType = aiTransType;
	}
}
