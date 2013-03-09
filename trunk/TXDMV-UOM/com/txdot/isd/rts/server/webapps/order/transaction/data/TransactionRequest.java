package com.txdot.isd.rts.server.webapps.order.transaction.data;

import com.txdot.isd.rts.server.webapps.order.common.data.AbstractRequest;
import com.txdot.isd.rts.server.webapps.order.common.data.Address;
import com.txdot.isd.rts.server.webapps.order.common.data.Fees;

/*
 * TransactionRequest.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		03/01/2007	New class.
 * 							defect 9121 Ver Special Plates 
 * B Brown		03/24/2008	Add reqSessionID
 * 							add getReqSessionID(), setReqSessionID()	
 * 							defect 9601 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * Object used to handle the Transaction Request Function.
 * <BR>
 * Note the duplicate seters and geters for Fees array.  This is done
 * because of a know bug in WAS 5.X webservice code.<BR>
 * http://www.theserverside.com/news/thread.tss?thread_id=30582<BR>
 * 
 * Fees[] getFees()
 * Fees getFees(int i) 
 * void setFees(int i, Fees aaFees)
 * void setFees(Fees[] aarrFees)<BR>
 *
 * @version	Special Plates	03/01/2007
 * @author	Bob Brown
 * <br>Creation Date:		03/01/2007 14:30:00
 */
public class TransactionRequest extends AbstractRequest
{
	private int addlSetIndi;
	private Address address;
	private int createPOSTransIndi;
	private String epayRcveTimeStamp;
	private String epaySendTimeStamp;
	private Fees[] fees;
	private int isaIndi;
	private int itrntPymntStatusCd;
	private String itrntTraceNo;
	private String mfgPltNo;
	private String orgNo;
	private String phoneNo;
	private int plpIndi;
	private String pltOwnrEmail;
	private String pltOwnrName1;
	private String pltOwnrName2;
	private double pymntAmt;
	private String pymntOrderID;
	private String regPltCd;
	private String regPltNo;
	private String reqIPAddr;
	private int resComptCntyNo;
	// defect 9601
	private String reqSessionID;
	// end defect 9601

	/**
	 * gets addlSetIndi
	 *
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return addlSetIndi;
	}

	/**
	 * gets Address
	 *
	 * @return Address
	 */
	public Address getAddress()
	{
		return address;
	}

	/**
	 * Used to determine if we are going to create a POS Transaction
	 * on this Transaction Request.  This is actually used to create 
	 * POS transactions in test.  In production creating POS 
	 * transactions will be part of a batch process that creates trans
	 * off of a turnaround file.
	 * 
	 * @return int
	 */
	public int getCreatePOSTransIndi()
	{
		return createPOSTransIndi;
	}

	/**
	 * Gets the Epay Receive TimeStamp.  Used to track the response
	 * time to and from Epay.
	 * 
	 * @return String
	 */
	public String getEpayRcveTimeStamp()
	{
		return epayRcveTimeStamp;
	}

	/**
	 * Gets the Epay Send TimeStamp.  Used to track the response
	 * time to and from Epay.
	 * 
	 * @return String
	 */
	public String getEpaySendTimeStamp()
	{
		return epaySendTimeStamp;
	}
	
	/**
	 * Gets the fees.
	 * 
	 * @return Fees[]
	 */
	public Fees[] getFees()
	{
		return fees;
	}

	/**
	 * Gets the fees.
	 * 
	 * @return Fees
	 */
	public Fees getFees(int j)
	{
		return fees[j];
	}

	/**
	 * gets isaIndi
	 *
	 * @return int
	 */
	public int getIsaIndi()
	{
		return isaIndi;
	}

	/**
	 * gets itrntPymntStatusCd
	 *
	 * @return int
	 */
	public int getItrntPymntStatusCd()
	{
		return itrntPymntStatusCd;
	}

	/**
	 * gets itrntTraceNo
	 *
	 * @return String
	 */
	public String getItrntTraceNo()
	{
		return itrntTraceNo;
	}

	/**
	 * gets mfgPltNo
	 *
	 * @return String
	 */
	public String getMfgPltNo()
	{
		return mfgPltNo;
	}

	/**
	 * Gets Org no
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return orgNo;
	}

	/**
	 * Gets the Phone Number.
	 * 
	 * @return String
	 */
	public String getPhoneNo()
	{
		return phoneNo;
	}

	/**
	 * Gets the indicator that determines if the is a PLP order or not.
	 * 
	 * @return int
	 */
	public int getPlpIndi()
	{
		return plpIndi;
	}

	/**
	 * gets pltOwnrEmail
	 *
	 * @return String
	 */
	public String getPltOwnrEmail()
	{
		return pltOwnrEmail;
	}

	/**
	 * gets pltOwnrName1
	 *
	 * @return String
	 */
	public String getPltOwnrName1()
	{
		return pltOwnrName1;
	}

	/**
	 * gets pltOwnrName2
	 *
	 * @return String
	 */
	public String getPltOwnrName2()
	{
		return pltOwnrName2;
	}

	/**
	 * gets pymntAmt
	 *
	 * @return double
	 */
	public double getPymntAmt()
	{
		return pymntAmt;
	}

	/**
	 * gets pymntOrderID
	 *
	 * @return String
	 */
	public String getPymntOrderID()
	{
		return pymntOrderID;
	}

	/**
	 * Gets the Reg Plate Code
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return regPltCd;
	}

	/**
	 * gets regPltNo
	 *
	 * @return String
	 */
	public String getRegPltNo()
	{
		return regPltNo;
	}

	/**
	 * gets reqIPAddr
	 *
	 * @return String
	 */
	public String getReqIPAddr()
	{
		return reqIPAddr;
	}

	/**
	 * gets resComptCntyNo
	 *
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return resComptCntyNo;
	}

	/**
	 * sets addlSetIndi
	 *
	 * @param aiAddlSetIndi 
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		addlSetIndi = aiAddlSetIndi;
	}

	/**
	 * sets address
	 *
	 * @param aaAddress 
	 */
	public void setAddress(Address aaAddress)
	{
		this.address = aaAddress;
	}

	/**
	 * Used to determine if we are going to create a POS Transaction
	 * on this Transaction Request.  This is actually used to create 
	 * POS transactions in test.  In production creating POS 
	 * transactions will be part of a batch process that creates trans
	 * off of a turnaround file.
	 * 
	 * @param aiCreatePOSTransIndi int
	 */
	public void setCreatePOSTransIndi(int aiCreatePOSTransIndi)
	{
		createPOSTransIndi = aiCreatePOSTransIndi;
	}

	/**
	 * Sets the Epay Receive TimeStamp.  Used to track the response
	 * time to and from Epay.
	 * 
	 * @param asEpayRcveTimeStamp String
	 */
	public void setEpayRcveTimeStamp(String asEpayRcveTimeStamp)
	{
		epayRcveTimeStamp = asEpayRcveTimeStamp;
	}

	/**
	 * Sets the Epay Send TimeStamp.  Used to track the response
	 * time to and from Epay.
	 * 
	 * @param asEpaySendTimeStamp String
	 */
	public void setEpaySendTimeStamp(String asEpaySendTimeStamp)
	{
		epaySendTimeStamp = asEpaySendTimeStamp;
	}
	
	/**
	 * Sets the fees.
	 * 
	 * @param aarrFees Fees[]
	 */
	public void setFees(Fees[] aarrFees)
	{
		fees = aarrFees;
	}
	
	/**
	 * Sets the fees.
	 * 
	 * @param j int
	 * @param aaFees Fees
	 */
	public void setFees(int j, Fees aaFees)
	{
		fees[j] = aaFees;
	}

	/**
	 * sets isaIndi
	 *
	 * @param aiIsaIndi 
	 */
	public void setIsaIndi(int aiIsaIndi)
	{
		isaIndi = aiIsaIndi;
	}

	/**
	 * sets itrntPymntStatusCd
	 *
	 * @param aiItrntPymntStatusCd 
	 */
	public void setItrntPymntStatusCd(int aiItrntPymntStatusCd)
	{
		itrntPymntStatusCd = aiItrntPymntStatusCd;
	}

	/**
	 * sets itrntTraceNo
	 *
	 * @param asItrntTraceNo 
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		itrntTraceNo = asItrntTraceNo;
	}

	/**
	 * sets mfgPltNo
	 *
	 * @param asMfgPltNo 
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		mfgPltNo = asMfgPltNo;
	}

	/**
	 * Method description
	 * 
	 * @param String asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		orgNo = asOrgNo;
	}

	/**
	 * Sets the Phone Number.
	 * 
	 * @param asPhoneNo String
	 */
	public void setPhoneNo(String asPhoneNo)
	{
		phoneNo = asPhoneNo;
	}

	/**
	 * Sets the indicator that determines if the is a PLP order or not.
	 * 
	 * @param aiPlpIndi int
	 */
	public void setPlpIndi(int aiPlpIndi)
	{
		plpIndi = aiPlpIndi;
	}

	/**
	 * sets pltOwnrEmail
	 *
	 * @param asPltOwnrEmail 
	 */
	public void setPltOwnrEmail(String asPltOwnrEmail)
	{
		pltOwnrEmail = asPltOwnrEmail;
	}

	/**
	 * sets pltOwnrName1
	 *
	 * @param asPltOwnrName1 
	 */
	public void setPltOwnrName1(String asPltOwnrName1)
	{
		pltOwnrName1 = asPltOwnrName1;
	}

	/**
	 * sets pltOwnrName2
	 *
	 * @param asPltOwnrName2 
	 */
	public void setPltOwnrName2(String asPltOwnrName2)
	{
		pltOwnrName2 = asPltOwnrName2;
	}

	/**
	 * sets pymntAmt
	 *
	 * @param adPymntAmt 
	 */
	public void setPymntAmt(double adPymntAmt)
	{
		pymntAmt = adPymntAmt;
	}

	/**
	 * sets pymntOrderID
	 *
	 * @param asPymntOrderID 
	 */
	public void setPymntOrderID(String asPymntOrderID)
	{
		pymntOrderID = asPymntOrderID;
	}

	/**
	 * Gets the Reg Plate Code
	 * 
	 * @param String asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		regPltCd = asRegPltCd;
	}

	/**
	 * sets regPltNo
	 *
	 * @param asRegPltNo 
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		regPltNo = asRegPltNo;
	}

	/**
	 * sets reqIPAddr
	 *
	 * @param asReqIPAddr 
	 */
	public void setReqIPAddr(String asReqIPAddr)
	{
		reqIPAddr = asReqIPAddr;
	}

	/**
	 * sets resComptCntyNo
	 * 
	 * @param aiResComptCntyNo int
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		resComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * gets internet users requestion session ID
	 * 
	 * @return String
	 */
	public String getReqSessionID()
	{
		return reqSessionID;
	}

	/**
	 * sets internet users requestion session ID
	 * 
	 * @param string
	 */
	public void setReqSessionID(String lsReqSessionID)
	{
		reqSessionID = lsReqSessionID;
	}

}
