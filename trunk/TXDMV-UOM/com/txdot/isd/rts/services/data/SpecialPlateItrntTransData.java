package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Vector;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * SpecialPltItrntTransData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/23/2007	Created Class.
 * Bob B.		09/12/2007	Added code to total up fees for a Special
 * 							Plate.
 * 							add getTotalFees()
 *							defect 9301 Ver Special Plates
 * Ray Rowehl	06/03/2008	Add From Reserve Indi.
 *							add ciFromReserveIndi, ciPltTerm 
 *							add getFromReserveIndi(), setFromReserveIndi(),
 *								getPltTerm(), setPltTerm()
 *							defect 9680 Ver MyPlates_POS 
 * Bob B.		06/23/2008	Added expiration month, year, employee ID
 *							add ciPltExpYr, ciPltExpMo, csTransEmpID
 *							add getPltExpMo(), setPltExpMo(),
 *								getPltExpYr(), setPltExpYr(),
 *								getTransEmpID(), setTransEmpID()
 *							defect 9711 Ver MyPlates_POS 
 * Min Wang		07/11/2008	Add new methods to return calendar versions 
 * 							of the timestamp.
 * 							add getEpayRcveTimeStmpCal(),
 * 								getEpaySendTimeStmpCal(),
 * 								getInitReqTimeStmpCal(),
 * 								getUpdtTimeStmpCal()
 *							defect 9676 Ver MyPlates_POS 
 * K Harrell	02/17/2010	add ciMktngAllowdIndi, ciPltValidityTerm, 
 * 							 ciAuctnPltIndi, csResrvReasnCd, 
 * 							 get/set methods
 * 							delete ciPltTerm, get/set methods 
 * 							defect 10366 Ver POS_640
 * K Harrell	03/24/2010	add clSpclRegId, csTransCd, get/set methods
 * 							defect 10366 Ver POS_640 
 * K Harrell	04/14/2010	add caAuctnPdAmt, get/set methods
 * 							defect 10366 Ver POS_640 
 * K Harrell	04/30/2010	delete caAuctnPdAmt, get/set methods
 * 							defect 10366 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * SpecialPlateItrntTransData used to query, insert and, update the 
 * RTS_ITRNT_SPAPP_TRANS table.
 *
 * @version	POS_640			04/30/2010
 * @author	Jeff Seifert
 * <br>Creation Date:		04/23/2007 10:25:00
 */
public class SpecialPlateItrntTransData implements Serializable
{

	// int 	
	private int ciAddlSetIndi;
	private int ciFromReserveIndi;
	private int ciISAIndi;
	private int ciItrntPymntStatusCd;
	private int ciPLPIndi;
	private int ciPltExpMo;
	private int ciPltExpYr;
	private int ciResComptCntyNo;

	// defect 10366
	//private int ciPltTerm; 
	private int ciAuctnPltIndi;
	private int ciMktngAllowdIndi;
	private int ciPltValidityTerm;
	private long clSpclRegId;
	private String csResrvReasnCd;
	private String csTransCd;
	// end defect 10366 

	// String 
	private String csAuditTrailTransId;
	private String csItrntTraceNo;
	private String csMfgPltNo;
	private String csOrgNo;
	private String csPltOwnrEmail;
	private String csPltOwnrPhone;
	private String csPymntOrderId;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csReqIPAddr;
	private String csTransEmpID;
	private String csTransStatusCd;

	// double 
	private double cdPymntAmt;

	// Vector of FeesData
	private Vector cvFeesData;

	// Object 
	private RTSDate caEpayRcveTimeStmp;
	private RTSDate caEpaySendTimeStmp;
	private RTSDate caInitReqTimeStmp;
	private OwnerData caOwnerData;
	private RTSDate caUpdtTimeStmp;

	static final long serialVersionUID = -1423950924900428889L;

	/**
	 * SpecialPlateItrntTransData.java Constructor
	 */
	public SpecialPlateItrntTransData()
	{
		super();
		cvFeesData = new Vector();
	}

	/**
	 * Adds to the fees Vector.
	 * 
	 * @param aaFeesData FeesData
	 */
	public void addFee(FeesData aaFeesData)
	{
		cvFeesData.add(aaFeesData);
	}

	/**
	 * Gets value of ciAuctnPltIndi
	 * 
	 * @return int
	 */
	public int getAuctnPltIndi()
	{
		return ciAuctnPltIndi;
	}

	/**
	 * Gets the Audit Trail Trans Id.
	 * 
	 * @return String
	 */
	public String getAuditTrailTransId()
	{
		return csAuditTrailTransId;
	}

	/**
	 * Gets the TimeStamp for when we received a response back from
	 * epay.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getEpayRcveTimeStmp()
	{
		return caEpayRcveTimeStmp;
	}

	/**
	 * get the calendar versions of the timestamp
	 * 
	 * @return Calendar
	 */
	public Calendar getEpayRcveTimeStmpCal()
	{
		if (caEpayRcveTimeStmp != null)
		{
			Calendar laCal = Calendar.getInstance();

			laCal.setTimeInMillis(
				caEpayRcveTimeStmp.getTimestamp().getTime());
			return laCal;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the TimeStamp for when we sent a request to epay.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getEpaySendTimeStmp()
	{
		return caEpaySendTimeStmp;
	}

	/**
	 * get the calendar versions of the timestamp
	 * 
	 * @return Calendar
	 */
	public Calendar getEpaySendTimeStmpCal()
	{
		if (caEpaySendTimeStmp != null)
		{
			Calendar laCal = Calendar.getInstance();
			laCal.setTimeInMillis(
				caEpaySendTimeStmp.getTimestamp().getTime());
			return laCal;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the fees data.
	 * 
	 * @return Vector
	 */
	public Vector getFeesData()
	{
		return cvFeesData;
	}

	/**
	 * Get From Reserve Indi.
	 * 
	 * @return int
	 */
	public int getFromReserveIndi()
	{
		return ciFromReserveIndi;
	}

	/**
	 * Gets the TimeStamp for when we made the initial request to insert
	 * the transaction.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getInitReqTimeStmp()
	{
		return caInitReqTimeStmp;
	}
	/**
	 * get the calendar versions of the timestamp
	 * 
	 * @return Calendar
	 */
	public Calendar getInitReqTimeStmpCal()
	{
		if (caInitReqTimeStmp != null)
		{
			Calendar laCal = Calendar.getInstance();
			laCal.setTimeInMillis(
				caInitReqTimeStmp.getTimestamp().getTime());
			return laCal;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the Internet Payment Status Cd.
	 * 
	 * @return int
	 */
	public int getItrntPymntStatusCd()
	{
		return ciItrntPymntStatusCd;
	}

	/**
	 * Gets the Internet Trace Number.
	 * 
	 * @return String
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}

	/**
	 * Gets the Manufacturing Plate Number.
	 * 
	 * @return String
	 */
	public String getMfgPltNo()
	{
		return csMfgPltNo;
	}

	/**
	 * Gets value of ciMktngAllowdIndi
	 * 
	 * @return int
	 */
	public int getMktngAllowdIndi()
	{
		return ciMktngAllowdIndi;
	}

	/**
	 * Gets the Ord No.
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Gets the Owner Data for this Transaction.
	 * 
	 * @return OwnerData
	 */
	public OwnerData getOwnerData()
	{
		return caOwnerData;
	}
	/**
	 * Get the new Special plate expiration month 
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}

	/**
	 * Get the new Special plate expiration year
	 * 
	 * @return int
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}

	/**
	 * Gets the Owner Email Address.
	 * 
	 * @return String
	 */
	public String getPltOwnrEmail()
	{
		return csPltOwnrEmail;
	}

	/**
	 * Gets the Owner Phone Number.
	 * 
	 * @return String
	 */
	public String getPltOwnrPhone()
	{
		return csPltOwnrPhone;
	}

	//	/**
	//	 * Get the Plate Term.
	//	 * 
	//	 * @return int
	//	 */
	//	public int getPltTerm()
	//	{
	//		return ciPltTerm;
	//	}

	/**
	 * Gets value of ciPltValidityTerm
	 * 
	 * @return int
	 */
	public int getPltValidityTerm()
	{
		return ciPltValidityTerm;
	}

	/**
	 * Gets the total payment amount.
	 * 
	 * @return double
	 */
	public double getPymntAmt()
	{
		return cdPymntAmt;
	}

	/**
	 * Gets the Payment Order Id.
	 * 
	 * @return String
	 */
	public String getPymntOrderId()
	{
		return csPymntOrderId;
	}

	/**
	 * Gets the Reg Plate Cd.
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Gets the Reg Plate No.
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Gets the Requesting IP address.
	 * 
	 * @return String
	 */
	public String getReqIPAddr()
	{
		return csReqIPAddr;
	}

	/**
	 * Gets the Compt County Number.
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Returns Reserve Reason Code
	 * 
	 * @return String 
	 */
	public String getResrvReasnCd()
	{
		return csResrvReasnCd;
	}

	/**
	 * Return value of clSpclRegId
	 * 
	 * @return long
	 */
	public long getSpclRegId()
	{
		return clSpclRegId;
	}

	/**
	 * Add the fees in a Special Plate Order.
	 * 
	 * @return Dollar
	 */
	public Dollar getTotalFees()
	{
		// defect 9301
		Dollar ldTotFees = new Dollar(0.00);
		for (int x = 0; x < cvFeesData.size(); x++)
		{
			FeesData laFeesData = (FeesData) cvFeesData.elementAt(x);
			ldTotFees = ldTotFees.add(laFeesData.getItemPrice());
		}
		return ldTotFees;
		// end defect 9301
	}

	/**
	 * Return value of csTranscd 
	 * 
	 * @return String 
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Get the Vendor trans employee ID
	 * 
	 * @return String
	 */
	public String getTransEmpID()
	{
		return csTransEmpID;
	}

	/**
	 * Gets the Transaction Status Code.
	 * 
	 * @return String
	 */
	public String getTransStatusCd()
	{
		return csTransStatusCd;
	}

	/**
	 * Gets the TimeStamp for when we made the update to the Transaction
	 * record after we captured payment.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getUpdtTimeStmp()
	{
		return caUpdtTimeStmp;
	}
	/**
	 * get the calendar versions of the timestamp
	 * 
	 * @return Calendar
	 */
	public Calendar getUpdtTimeStmpCal()
	{
		if (caUpdtTimeStmp != null)
		{
			Calendar laCal = Calendar.getInstance();
			laCal.setTimeInMillis(
				caUpdtTimeStmp.getTimestamp().getTime());
			return laCal;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets if this is an Additional Set.
	 * 
	 * @return int
	 */
	public int isAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Gets if this is an ISA request.
	 * 
	 * @return int
	 */
	public int isISAIndi()
	{
		return ciISAIndi;
	}

	/**
	 * Gets the PLP indi
	 * 
	 * @return ciPLPIndi
	 */
	public int isPLPIndi()
	{
		return ciPLPIndi;
	}

	/**
	 * Removes from the fees Vector.
	 * 
	 * @param aaFeesData FeesData
	 */
	public void removeFee(FeesData aaFeesData)
	{
		cvFeesData.remove(aaFeesData);
	}

	/**
	 * Sets if this is an Additional Set.
	 * 
	 * @param aiAddlSetIndi int
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}

	/**
	 * Sets value of ciAuctnPltIndi
	 * 
	 * @param aiAuctnPltIndi
	 */
	public void setAuctnPltIndi(int aiAuctnPltIndi)
	{
		ciAuctnPltIndi = aiAuctnPltIndi;
	}

	/**
	 * Sets the Audit Trail Trans Id.
	 * 
	 * @param asAuditTrailTransId String
	 */
	public void setAuditTrailTransId(String asAuditTrailTransId)
	{
		csAuditTrailTransId = asAuditTrailTransId;
	}

	/**
	 * Sets the TimeStamp for when we received a response from epay.
	 * 
	 * @param aaEpayRcveTimeStmp RTSDate
	 */
	public void setEpayRcveTimeStmp(RTSDate aaEpayRcveTimeStmp)
	{
		caEpayRcveTimeStmp = aaEpayRcveTimeStmp;
	}

	/**
	 * Sets the TimeStamp for when we sent a request to epay.
	 * 
	 * @param aaEpaySendTimeStmp RTSDate
	 */
	public void setEpaySendTimeStmp(RTSDate aaEpaySendTimeStmp)
	{
		caEpaySendTimeStmp = aaEpaySendTimeStmp;
	}

	/**
	 * Sets the fees data.
	 * 
	 * @param avFeesData
	 */
	public void setFeesData(Vector avFeesData)
	{
		cvFeesData = avFeesData;
	}

	/**
	 * Set the From Reserve Indi.
	 * 
	 * @param aiFromReserveIndi
	 */
	public void setFromReserveIndi(int aiFromReserveIndi)
	{
		ciFromReserveIndi = aiFromReserveIndi;
	}

	/**
	 * Sets the Initial TimeStamp for when we made the first insert
	 * for this transaction.
	 * 
	 * @param aaInitReqTimeStmp RTSDate
	 */
	public void setInitReqTimeStmp(RTSDate aaInitReqTimeStmp)
	{
		caInitReqTimeStmp = aaInitReqTimeStmp;
	}

	/**
	 * Sets if this Transaction is an ISA request.
	 * 
	 * @param aiISAIndi int
	 */
	public void setISAIndi(int aiISAIndi)
	{
		ciISAIndi = aiISAIndi;
	}

	/**
	 * Sets the Payment Status Code.
	 * 
	 * @param aiItrntPymntStatusCd int
	 */
	public void setItrntPymntStatusCd(int aiItrntPymntStatusCd)
	{
		ciItrntPymntStatusCd = aiItrntPymntStatusCd;
	}

	/**
	 * Sets the Internet Trace Number.
	 * 
	 * @param asItrntTraceNo String
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
	}

	/**
	 * Sets the Manufacturing Plate Number.
	 * 
	 * @param asMfgPlyNo String
	 */
	public void setMfgPltNo(String asMfgPlyNo)
	{
		csMfgPltNo = asMfgPlyNo;
	}

	/**
	 * Sets value of ciMktngAllowdIndi
	 * 
	 * @param aiMktngAllowdIndi
	 */
	public void setMktngAllowdIndi(int aiMktngAllowdIndi)
	{
		ciMktngAllowdIndi = aiMktngAllowdIndi;
	}

	/**
	 * Sets the Org No.
	 * 
	 * @param asOrgNo String
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * Sets the Owner Data for this Transaction.
	 * 
	 * @param aaOwnerData OwnerData
	 */
	public void setOwnerData(OwnerData aaOwnerData)
	{
		caOwnerData = aaOwnerData;
	}

	/**
	 * Sets the PLPIndi
	 * 
	 * @param aiPLPIndi int
	 */
	public void setPLPIndi(int aiPLPIndi)
	{
		ciPLPIndi = aiPLPIndi;
	}

	/**
	 * Set the new Special plate expiration month 
	 * 
	 * @param aiPltExpMo int
	 */
	public void setPltExpMo(int aiPltExpMo)
	{
		ciPltExpMo = aiPltExpMo;
	}

	/**
	 * Set the new Special plate expiration year 
	 * 
	 * @param aiPltExpYr int
	 */
	public void setPltExpYr(int aiPltExpYr)
	{
		ciPltExpYr = aiPltExpYr;
	}

	/**
	 * Sets the Owners Email Address.
	 * 
	 * @param asPltOwnrEmail String
	 */
	public void setPltOwnrEmail(String asPltOwnrEmail)
	{
		csPltOwnrEmail = asPltOwnrEmail;
	}

	/**
	 * Sets the Owners Phone Number.
	 * 
	 * @param asPltOwnrPhone String
	 */
	public void setPltOwnrPhone(String asPltOwnrPhone)
	{
		csPltOwnrPhone = asPltOwnrPhone;
	}

	//	/**
	//	 * Set the Plate Term.
	//	 * 
	//	 * @param aiPltTerm
	//	 */
	//	public void setPltTerm(int aiPltTerm)
	//	{
	//		ciPltTerm = aiPltTerm;
	//	}

	/**
	 * Sets value of ciPltValidityTerm
	 * 
	 * @param aiPltValidityTerm
	 */
	public void setPltValidityTerm(int aiPltValidityTerm)
	{
		ciPltValidityTerm = aiPltValidityTerm;
	}

	/**
	 * Sets the Total Payment Amount.
	 * 
	 * @param adPymntAmt double
	 */
	public void setPymntAmt(double adPymntAmt)
	{
		cdPymntAmt = adPymntAmt;
	}

	/**
	 * Sets the Payment Order Id.
	 * 
	 * @param asPymntOrderId String
	 */
	public void setPymntOrderId(String asPymntOrderId)
	{
		csPymntOrderId = asPymntOrderId;
	}

	/**
	 * Sets the Reg Plate Cd.
	 * 
	 * @param asRegPltCd String
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Sets the Reg Plate No.
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Sets the IP address of the customer making the request.
	 * 
	 * @param asReqIPAddr String
	 */
	public void setReqIPAddr(String asReqIPAddr)
	{
		csReqIPAddr = asReqIPAddr;
	}

	/**
	 * Sets the Reg Compt County No.
	 * 
	 * @param aiResComptCntyNo int
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Sets Reserve Reason Code
	 * 
	 * @param asResrvReasnCd
	 */
	public void setResrvReasnCd(String asResrvReasnCd)
	{
		csResrvReasnCd = asResrvReasnCd;
	}

	/**
	 * Set value of clSpclRegId
	 * 
	 * @param alSpclRegId
	 */
	public void setSpclRegId(long alSpclRegId)
	{
		clSpclRegId = alSpclRegId;
	}

	/**
	 * Set value of csTransCd
	 * 
	 * @param asTransCd
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * Set the vendortrans employee ID
	 * 
	 * @param aiTransEmpID String
	 */
	public void setTransEmpID(String asTransEmpID)
	{
		csTransEmpID = asTransEmpID;
	}

	/**
	 * Sets the Transaction Status Code.
	 * 
	 * @param asTransStatusCd String
	 */
	public void setTransStatusCd(String asTransStatusCd)
	{
		csTransStatusCd = asTransStatusCd;
	}

	/**
	 * Sets the TimeStamp of the Update of this recrod.
	 * 
	 * @param aaUpdtTimeStmp RTSDate
	 */
	public void setUpdtTimeStmp(RTSDate aaUpdtTimeStmp)
	{
		caUpdtTimeStmp = aaUpdtTimeStmp;
	}

}
