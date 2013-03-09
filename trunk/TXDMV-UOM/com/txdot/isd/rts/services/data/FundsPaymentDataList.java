package com.txdot.isd.rts.services.data;

import java.util.Vector;

/*
 *
 * FundsPaymentDataList.java
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
 * FundsPaymentDataList is the data object used by the Funds Inquiry 
 * actions.
 * <p>It contains a vector of FundsPaymentData, and when a search is 
 * performed by Funds Due Date, it also contains a vector of 
 * FundsDueData.
 *
 * @version	5.2.3		05/19/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	07/18/2001 16:11:09  
 */

public class FundsPaymentDataList implements java.io.Serializable
{
	// boolean 
	private boolean cbTooManyRecords;

	// Object 
	private FundsPaymentData caSelectedData;

	// Vector 
	private Vector cvFundsDue;
	private Vector cvFundsPymnt;

	private final static long serialVersionUID = -6488379957946993375L;
	/**
	 * Creates a FundsPaymentDataList.
	 */
	public FundsPaymentDataList()
	{
		super();
		cvFundsPymnt = new Vector();
		cvFundsDue = new Vector();
	}
	/**
	 * Returns the selected FundsPaymentData object.
	 * 
	 * @return FundsPaymentData 
	 */
	public FundsPaymentData getSelectedData()
	{
		return caSelectedData;
	}
	/**
	 * Returns the vector of FundsPaymentData.
	 * 
	 * @return Vector
	 */
	public Vector getFundsPymnt()
	{
		return cvFundsPymnt;
	}
	/**
	 * Returns the vector of FundsDueData.
	 * 
	 * @return Vector
	 */
	public Vector getFundsDue()
	{
		return cvFundsDue;
	}
	/**
	 * Indicates whether too many records were found as a 
	 * result of the MF call.
	 * 
	 * @return boolean 
	 */
	public boolean isTooManyRecords()
	{
		return cbTooManyRecords;
	}
	/**
	 * Sets the selected FundsPaymentData object.
	 * 
	 * @param aaSelectedData FundsPaymentData the selected object
	 */
	public void setSelectedData(FundsPaymentData aaSelectedData)
	{
		caSelectedData = aaSelectedData;
	}
	/**
	 * Sets whether  too many records were found as a result 
	 * of the MF call.
	 * 
	 * @param abTooManyRecords boolean
	 */
	public void setTooManyRecords(boolean abTooManyRecords)
	{
		cbTooManyRecords = abTooManyRecords;
	}
	/**
	 * Sets the vector of FundsPaymentData.
	 * 
	 * @param avFundsPymnt Vector
	 */
	public void setFundsPymnt(Vector avFundsPymnt)
	{
		cvFundsPymnt = avFundsPymnt;
	}
	/**
	 * Sets the vector of FundsDueData.
	 * 
	 * @param avVectorFundsDue Vector
	 */
	public void setVectorFundsDue(Vector avVectorFundsDue)
	{
		cvFundsDue = avVectorFundsDue;
	}
}
