package com.txdot.isd.rts.server.webapps.reports;

/*
 * VendorReportData.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford		10/09/2002	Defect 4795, Failed Refund transaction 
 * 							handling data type constants and field added.
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 * Bob Brown 	06/22/2006 	Add csTransStatus
 *       					add setTransStatus(), getTransStatus()
 *       					defect 8609 Ver 5.2.2 Fix 8
 * Bob Brown	12/16/2008	Add credit card type
 * 							add csCardType, and its getter and setters.
 * 							defect 9878 Ver Defect_POS_C
 *----------------------------------------------------------------------
 */

/**
 * Internet transaction Electronic payment data
 * for report.
 *  
 * @version	Defect_POS_C	12/16/2008
 * @author	Administrator
 * <br>Creation Date:		02/25/2002 16:18:46
 */
public class VendorReportData
{
	public static final int CAPTURE = 0;
	public static final int REFUND = 1;
	public static final int REFUND_FAILED = 2;

	private String csItrntTraceNo;
	private String csOrderId;
	private String csTransDate;
	private String csOrigTransDate;
	private String csCardNo;
	// defect 9878
	private String csCardType;
	// end defect 9878
	private String csAmount;
	
	// defect 8609
	private String csTransStatus;
	// end defect 8609


	private int ciType = 0;
	
	/**
	 * VendorReportData constructor comment.
	 */
	public VendorReportData()
	{
		super();
		csOrigTransDate = "";
	}
	
	/**
	 * VendorReportData constructor comment.
	 * 
	 * @param int aiType
	 */
	public VendorReportData(int aiType)
	{
		super();
		csOrigTransDate = "";
		ciType = aiType;
	}
	
	/**
	 * Get Amount
	 * 
	 * @return double
	 */
	public double getAmount()
	{
		return Double.parseDouble(csAmount);
	}
	
	/**
	 * Get Amount String
	 * 
	 * @return String
	 */
	public String getAmountString()
	{
		if (ciType == REFUND_FAILED)
			return "[" + csAmount + "]";
		else
			return csAmount;
	}
	
	/**
	 * Get Card Number
	 * 
	 * @return String
	 */
	public java.lang.String getCardNo()
	{
		return csCardNo;
	}
	
	/**
	 * Get Internet Trace Number
	 * 
	 * @return String
	 */
	public java.lang.String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}
	
	/**
	 * Get Order Id
	 * 
	 * @return String
	 */
	public String getOrderId()
	{
		return csOrderId;
	}
	
	/**
	 * Get Original Trans Date
	 * 
	 * @return String
	 */
	public String getOrigTransDate()
	{
		return csOrigTransDate;
	}
	
	/**
	 * Get Trans Date
	 * 
	 * @return String
	 */
	public java.lang.String getTransDate()
	{
		return csTransDate;
	}
	
	/**
	 * Get Type
	 * 
	 * @return int
	 */
	public int getType()
	{
		return ciType;
	}
	
	/**
	 * Set Amount
	 * 
	 * @param double adNewAmount
	 */
	public void setAmount(double adNewAmount)
	{
		csAmount = "" + adNewAmount;
	}
	
	/**
	 * Set Amount String
	 * 
	 * @param String asNewAmount
	 */
	public void setAmountString(String asNewAmount)
	{
		csAmount = asNewAmount;
	}
	
	/**
	 * Set Card Number
	 * 
	 * @param String asNewCardNo
	 */
	public void setCardNo(java.lang.String asNewCardNo)
	{
		csCardNo = asNewCardNo;
	}
	
	/**
	 * Set Internet Trace Number
	 * 
	 * @param String asNewItrntTraceNo
	 */
	public void setItrntTraceNo(java.lang.String asNewItrntTraceNo)
	{
		csItrntTraceNo = asNewItrntTraceNo;
	}
	
	/**
	 * Set Order Id
	 * 
	 * @param String asNewOrderId
	 */
	public void setOrderId(String asNewOrderId)
	{
		csOrderId = asNewOrderId;
	}
	
	/**
	 * Set Original Trans Date
	 * 
	 * @param String asNewOrigTransDate
	 */
	public void setOrigTransDate(String asNewOrigTransDate)
	{
		csOrigTransDate = asNewOrigTransDate;
	}
	
	/**
	 * Set Trans Date
	 * 
	 * @param String asNewTransDate
	 */
	public void setTransDate(java.lang.String asNewTransDate)
	{
		csTransDate = asNewTransDate;
	}
	
	/**
	 * Set Type
	 * 
	 * @param int aiNewType
	 */
	public void setType(int aiNewType)
	{
		ciType = aiNewType;
	}
	/**
	 * Get Trans Status
	 * 
	 * @return String
	 */
	public String getTransStatus()
	{
		return csTransStatus;
	}

	/**
	 * Set Trans Status
	 * 
	 * @param String asNewTransStatus
	 */
	public void setTransStatus(String asNewTransStatus)
	{
		csTransStatus = asNewTransStatus;
	}

	/**
	 * get the card type
	 * 
	 * @return String
	 */
	public String getCardType()
	{
		return csCardType;
	}

	/**
	 * set the card type
	 * 
	 * @param asCardType String
	 */
	public void setCardType(String asCardType)
	{
		csCardType = asCardType;
	}

}
