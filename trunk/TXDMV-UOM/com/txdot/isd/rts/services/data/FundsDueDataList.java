package com.txdot.isd.rts.services.data;

import java.util.Vector;

/*
 *
 * FundsDueDataList.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			09/07/2001  Added comments
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * FundsDueDataList is the data object used by the Funds Remittance 
 * actions.
 * <p>It contains a vector of FundsDueData and a vector of 
 * PaymentAccountData.
 * 
 * @version	5.2.3		05/19/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	07/18/2001 08:38:00  
 */

public class FundsDueDataList implements java.io.Serializable
{

	// int 
	private int ciFundsReportYear;

	//	Object 
	private FundsDueData caSelectedRecord;

	//	Vector 
	private Vector cvPaymentAccounts;
	private Vector cvFundsDue;

	private final static long serialVersionUID = 8210892091880533061L;

	/**
	 * Creates a FundsDueDataList.
	 */
	public FundsDueDataList()
	{
		super();
		cvFundsDue = new Vector();
		cvPaymentAccounts = new Vector();
	}
	/**
	 * Returns the Funds Report Year.
	 * 
	 * @return int
	 */
	public int getFundsReportYear()
	{
		return ciFundsReportYear;
	}
	/**
	 * Returns a Vector of PaymentAccountData.
	 * 
	 * @return Vector
	 */
	public Vector getPaymentAccounts()
	{
		return cvPaymentAccounts;
	}
	/**
	 * Returns the selected FundsDueData object.
	 * 
	 * @return FundsDueData
	 */
	public FundsDueData getSelectedRecord()
	{
		return caSelectedRecord;
	}
	/**
	 * Returns a Vector of FundsDueData.
	 * 
	 * @return Vector 
	 */
	public Vector getFundsDue()
	{
		return cvFundsDue;
	}
	/**
	 * Sets the Funds Report Year.
	 * 
	 * @param aiFundsReportYear int 
	 */
	public void setFundsReportYear(int aiFundsReportYear)
	{
		ciFundsReportYear = aiFundsReportYear;
	}
	/**
	 * Sets the Vector of PaymentAccountData.
	 * 
	 * @param avPaymentAccounts Vector 
	 */
	public void setPaymentAccounts(Vector avPaymentAccounts)
	{
		cvPaymentAccounts = avPaymentAccounts;
	}
	/**
	 * Sets the selected FundsDueData object.
	 * 
	 * @param aaSelectedRecord FundsDueData
	 */
	public void setSelectedRecord(FundsDueData aaSelectedRecord)
	{
		caSelectedRecord = aaSelectedRecord;
	}
	/**
	 * Sets the Vector of FundsDueData.
	 * 
	 * @param avFundsDue  Vector 
	 */
	public void setFundsDue(Vector avFundsDue)
	{
		cvFundsDue = avFundsDue;
	}
}
