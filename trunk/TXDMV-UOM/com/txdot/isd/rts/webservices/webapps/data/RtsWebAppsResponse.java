package com.txdot.isd.rts.webservices.webapps.data;

import java.util.Calendar;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * RtsWebAppsResponse.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		07/02/2008	Created Class
 * 							defect 9676 Ver MyPlates_POS
 * Min Wang		07/22/2008	Using Calendar for Timestamp fields.
 * Min Wang		07/22/2008	rename csTransStratusCd to csTransStatusCd.
 * R Pilon		02/02/2012	Add missing setter method to prevent web service 
 * 							  validation error.
 * 							add setAction()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Response data from Web Apps service.
 *
 * @version	6.10.0			02/02/2012
 * @author	mwang
 * <br>Creation Date:		06/26/2008 03:00:00
 */
public class RtsWebAppsResponse extends RtsAbstractResponse
{
	private String csRegPltNo;
	private int ciRegPltNoReqId;
	private String csTransStatusCd;
	private String csRegPltCd;
	private String csOrgNo;
	private int ciResComptCntyNo;
	private String csPltOwnrName1;
	private String csPltOwnrName2;
	private String csPltOwnrSt1;
	private String csPltOwnrSt2;
	private String csPltOwnrCity;
	private String csPltOwnrState;
	private String csPltOwnrZpCd;
	private String csPltOwnrZpCd4;
	private String csPltOwnrPhone;
	private String csPltOwnrEmail;
	private String csMfgPltNo;
	private int ciIsaIndi;
	private int ciAddlSetIndi;
	private int ciMqUpdtIndi;
	private Dollar caPymntAmt;
	private String csAuditTrailTransId;
	private String csItrntTranceNo;
	private int ciItrntPymntStatusCd;
	private String csPymntOrderId;
	private String csReqIpAddr;
	private Calendar caInitReqTimestmp;
	private Calendar caUpdtTimestmp;
	private Calendar caEpaySendTimestmp;
	private Calendar caEpayRcveTimeStmp;
	private int ciPltExpYr;
	private int ciPltExpMo;
	private int ciPltTerm;
	private int ciReserveIndi;
	private String csTransEmpId;
	
	/**
	 * Return the value of EpayRcveTimestmp
	 * 
	 * @return Calendar
	 */
	public Calendar getEpayRcveTimestmp()
	{
		return caEpayRcveTimeStmp;
	}

	/**
	 * Return the value of EpaySendTimestmp
	 * 
	 * @return Calendar
	 */
	public Calendar getEpaySendTimestmp()
	{
		return caEpaySendTimestmp;
	}

	/**
	 * Return the value of InitReqTimestmp
	 * 
	 * @return Calendar
	 */
	public Calendar getInitReqTimestmp()
	{
		return caInitReqTimestmp;
	}

	/**
	 * Return the value of PymntAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getPymntAmt()
	{
		return caPymntAmt;
	}

	/**
	 * Return the value of UpdtTimestmp
	 * 
	 * @return Calendar
	 */
	public Calendar getUpdtTimestmp()
	{
		return caUpdtTimestmp;
	}

	/**
	 * Return the value of AddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Return the value of IsaIndi
	 * 
	 * @return int
	 */
	public int getIsaIndi()
	{
		return ciIsaIndi;
	}

	/**
	 * Return the value of ItrntPymntStatusCd
	 * 
	 * @return int
	 */
	public int getItrntPymntStatusCd()
	{
		return ciItrntPymntStatusCd;
	}

	/**
	 * Return the value of MqUpdtIndi
	 * 
	 * @return int
	 */
	public int getMqUpdtIndi()
	{
		return ciMqUpdtIndi;
	}

	/**
	 * Return the value of PltTerm
	 * 
	 * @return int
	 */
	public int getPltTerm()
	{
		return ciPltTerm;
	}

	/**
	 * Return the value of PltExpMo 
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}

	/**
	 * Return the value of PltExpYr
	 * 
	 * @return int
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}

	/**
	 * Return the value of RegPltNoReqId
	 * 
	 * @return int
	 */
	public int getRegPltNoReqId()
	{
		return ciRegPltNoReqId;
	}

	/**
	 * Return the value of ResComptCntyNo
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Return the value of ReserveIndi
	 * 
	 * @return int
	 */
	public int getReserveIndi()
	{
		return ciReserveIndi;
	}

	/**
	 * Return the value of AuditTrailTransId
	 * 
	 * @return String
	 */
	public String getAuditTrailTransId()
	{
		return csAuditTrailTransId;
	}

	/**
	 * Return the value of ItrntTranceNo
	 * 
	 * @return Sting
	 */
	public String getItrntTranceNo()
	{
		return csItrntTranceNo;
	}

	/**
	 * Return the value of MfgPltNo
	 * 
	 * @return Sting
	 */
	public String getMfgPltNo()
	{
		return csMfgPltNo;
	}

	/**
	 * Return the value of OrgNo
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Return the value of PltOwnrCity
	 * 
	 * @return String
	 */
	public String getPltOwnrCity()
	{
		return csPltOwnrCity;
	}

	/**
	 * Return the value of PltOwnrEmail
	 * 
	 * @return String
	 */
	public String getPltOwnrEmail()
	{
		return csPltOwnrEmail;
	}

	/**
	 * Return the value of PltOwnrName2 
	 * 
	 * @return String
	 */
	public String getPltOwnrName2()
	{
		return csPltOwnrName2;
	}

	/**
	 * Return the value of PltOwnrName1
	 * 
	 * @return String
	 */
	public String getPltOwnrName1()
	{
		return csPltOwnrName1;
	}

	/**
	 * Return the value of PltOwnrPhone
	 * 
	 * @return String
	 */
	public String getPltOwnrPhone()
	{
		return csPltOwnrPhone;
	}

	/**
	 * Return the value of PltOwnrSt1
	 * 
	 * @return String  
	 */
	public String getPltOwnrSt1()
	{
		return csPltOwnrSt1;
	}

	/**
	 * Return the value of PltOwnrSt2
	 * 
	 * @return String
	 */
	public String getPltOwnrSt2()
	{
		return csPltOwnrSt2;
	}

	/**
	 * Return the value of PltOwnrState
	 * 
	 * @return String
	 */
	public String getPltOwnrState()
	{
		return csPltOwnrState;
	}

	/**
	 * Return the value of PltOwnrZpCd
	 * 
	 * @return String
	 */
	public String getPltOwnrZpCd()
	{
		return csPltOwnrZpCd;
	}

	/**
	 * Return the value of PltOwnrZpCd4
	 * 
	 * @return String
	 */
	public String getPltOwnrZpCd4()
	{
		return csPltOwnrZpCd4;
	}

	/**
	 * Return the value of PymntOrderId
	 * 
	 * @return String
	 */
	public String getPymntOrderId()
	{
		return csPymntOrderId;
	}

	/**
	 * Return the value of RegPltCd
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Return the value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Return the value of ReqIpAddr
	 * 
	 * @return String
	 */
	public String getReqIpAddr()
	{
		return csReqIpAddr;
	}

	/**
	 * Return the value of TransEmpId
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}

	/**
	 * Return the value of TransStatusCd
	 * 
	 * @return String
	 */
	public String getTransStatusCd()
	{
		return csTransStatusCd;
	}

	/**
	 * Set the value of EpayRcveTimestmp
	 * 
	 * @param aaEpayRcveTimestmp Calendar
	 */
	public void setEpayRcveTimestmp(Calendar aaEpayRcveTimestmp)
	{
		caEpayRcveTimeStmp = aaEpayRcveTimestmp;
	}

	/**
	 * Set the value of EpaySendTimestmp
	 * 
	 * @param aaEpaySendTimestmp Calendar
	 */
	public void setEpaySendTimestmp(Calendar aaEpaySendTimestmp)
	{
		caEpaySendTimestmp = aaEpaySendTimestmp;
	}

	/**
	 * Set the value of InitReqTimestmp
	 * 
	 * @param aaInitReqTimestmp Calendar
	 */
	public void setInitReqTimestmp(Calendar aaInitReqTimestmp)
	{
		caInitReqTimestmp = aaInitReqTimestmp;
	}

	/**
	 * Set the value of PymntAmt
	 * 
	 * @param aaPymntAmt Dollar
	 */
	public void setPymntAmt(Dollar aaPymntAmt)
	{
		caPymntAmt = aaPymntAmt;
	}

	/**
	 * Set the value of UpdtTimestmp
	 * 
	 * @param aaUpdtTimestmp RTSDate
	 */
	public void setUpdtTimestmp(Calendar aaUpdtTimestmp)
	{
		caUpdtTimestmp = aaUpdtTimestmp;
	}

	/**
	 * Set the value of AddlSetIndi
	 * 
	 * @param aiAddlSetIndi int
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}

	/**
	 * Set the value of IsaIndi
	 * 
	 * @param aiIsaIndi int
	 */
	public void setIsaIndi(int aiIsaIndi)
	{
		ciIsaIndi = aiIsaIndi;
	}

	/**
	 * Set the value of ItrntPymntStatusCd
	 * 
	 * @param aiItrntPymntStatusCd int
	 */
	public void setItrntPymntStatusCd(int aiItrntPymntStatusCd)
	{
		ciItrntPymntStatusCd = aiItrntPymntStatusCd;
	}

	/**
	 * Set the value of MqUpdtIndi
	 * 
	 * @param aiMqUpdtIndi int
	 */
	public void setMqUpdtIndi(int aiMqUpdtIndi)
	{
		ciMqUpdtIndi = aiMqUpdtIndi;
	}

	/**
	 * Set the value of PltTerm
	 * 
	 * @param aiPltTerm int
	 */
	public void setPltTerm(int aiPltTerm)
	{
		ciPltTerm = aiPltTerm;
	}

	/**
	 * Set the value of PltExpMo
	 * 
	 * @param aiPltExpMo int
	 */
	public void setPltExpMo(int aiPltExpMo)
	{
		ciPltExpMo = aiPltExpMo;
	}

	/**
	 * Set the value of PltExpYr
	 * 
	 * @param aiPltExpYr int
	 */
	public void setPltExpYr(int aiPltExpYr)
	{
		ciPltExpYr = aiPltExpYr;
	}

	/**
	 * Set the value of RegPltNoReqId
	 * 
	 * @param aiRegPltNoReqId int
	 */
	public void setRegPltNoReqId(int aiRegPltNoReqId)
	{
		ciRegPltNoReqId = aiRegPltNoReqId;
	}

	/**
	 * Set the value of ResComptCntyNo
	 * 
	 * @param aiResComptCntyNo int
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Set the value of ReserveIndi
	 * 
	 * @param aiReserveIndi int
	 */
	public void setReserveIndi(int aiReserveIndi)
	{
		ciReserveIndi = aiReserveIndi;
	}

	/**
	 * Set the value of AuditTrailTransId
	 * 
	 * @param asAuditTrailTransId String
	 */
	public void setAuditTrailTransId(String asAuditTrailTransId)
	{
		csAuditTrailTransId = asAuditTrailTransId;
	}

	/**
	 * Set the value of ItrntTranceNo
	 * 
	 * @param asItrntTranceNo String
	 */
	public void setItrntTranceNo(String asItrntTranceNo)
	{
		csItrntTranceNo = asItrntTranceNo;
	}

	/**
	 * Set the value of MfgPltNo
	 * 
	 * @param  asMfgPltNo String
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
	}

	/**
	 * Set the value of OrgNo
	 * 
	 * @param asOrgNo String
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * Set the value of PltOwnrCity
	 * 
	 * @param asPltOwnrCity String
	 */
	public void setPltOwnrCity(String asPltOwnrCity)
	{
		csPltOwnrCity = asPltOwnrCity;
	}

	/**
	 * Set the value of PltOwnrEmail
	 * 
	 * @param asPltOwnrEmail String
	 */
	public void setPltOwnrEmail(String asPltOwnrEmail)
	{
		csPltOwnrEmail = asPltOwnrEmail;
	}

	/**
	 * Set the value of PltOwnrName2
	 * 
	 * @param asPltOwnrName2 String
	 */
	public void setPltOwnrName2(String asPltOwnrName2)
	{
		csPltOwnrName2 = asPltOwnrName2;
	}

	/**
	 * Set the value of PltOwnName1
	 * 
	 * @param asPltOwnName1 String
	 */
	public void setPltOwnrName1(String asPltOwnrName1)
	{
		csPltOwnrName1 = asPltOwnrName1;
	}

	/**
	 * Set the value of PltOwnPhone
	 * 
	 * @param asPltOwnrPhone String
	 */
	public void setPltOwnrPhone(String asPltOwnrPhone)
	{
		csPltOwnrPhone = asPltOwnrPhone;
	}

	/**
	 * Set the value of PltOwnrSt1
	 * 
	 * @param asPltOwnrSt1g String
	 */
	public void setPltOwnrSt1(String asPltOwnrSt1g)
	{
		csPltOwnrSt1 = asPltOwnrSt1g;
	}

	/**
	 * Set the value of PltOwnrSt2
	 * 
	 * @param asPltOwnrSt2 String
	 */
	public void setPltOwnrSt2(String asPltOwnrSt2)
	{
		csPltOwnrSt2 = asPltOwnrSt2;
	}

	/**
	 * Set the value of PltOwnrState
	 * 
	 * @param asPltOwnrState String
	 */
	public void setPltOwnrState(String asPltOwnrState)
	{
		csPltOwnrState = asPltOwnrState;
	}

	/**
	 * Set the value of PltOwnrZpCd
	 * 
	 * @param asPltOwnrZpCd String
	 */
	public void setPltOwnrZpCd(String asPltOwnrZpCd)
	{
		csPltOwnrZpCd = asPltOwnrZpCd;
	}

	/**
	 * Set the value of PltOwnrZpCd4
	 * 
	 * @param asPltOwnrZpCd4 String
	 */
	public void setPltOwnrZpCd4(String asPltOwnrZpCd4)
	{
		csPltOwnrZpCd4 = asPltOwnrZpCd4;
	}

	/**
	 * Set the value of PymntOrderId
	 * 
	 * @param asPymntOrderId String
	 */
	public void setPymntOrderId(String asPymntOrderId)
	{
		csPymntOrderId = asPymntOrderId;
	}

	/**
	 * Set the value of RegPltCd
	 * 
	 * @param asRegPltCd String
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Set the value of RegPltNo
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Set the value of ReqIpAddr
	 * 
	 * @param asReqIpAddr String
	 */
	public void setReqIpAddr(String asReqIpAddr)
	{
		csReqIpAddr = asReqIpAddr;
	}

	/**
	 * Set the value of TransEmpId
	 * 
	 * @param asTransEmpId String
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}

	/**
	 * Set the value of TransStatusCd
	 * 
	 * @param asTransStatusCd String
	 */
	public void setTransStatusCd(String asTransStatusCd)
	{
		csTransStatusCd = asTransStatusCd;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public int getAction()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Null setter method required to prevent web services validation 
	 * error.
	 * 
	 * @param aiAction
	 */
	public void setAction(int aiAction) 
	{
		// null setter
	}
}
