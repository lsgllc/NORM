package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * TitlePackageReportData.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * S. Haskett   12/12/2002  Added constructors, equals() and toString() 
 * 							defect 5138 
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3
 * K Harrell	10/17/2005	Moved to services.data from 
 * 							services.reports.reports
 * 							defect 7896 Ver 5.2.3 
 * K Harrell	03/04/2009	add ciETtlCd, get/set methods 
 * 							modify toString(), equals(),
 * 							 TitlePackageReportData(int,int,...)
 * 							defect 9976 Ver Defect_POS_E
 * K Harrell	09/19/2010	add TtlExmnIndi, get/set methods 
 * 							add isTtlExmn(), isVoidedTrans() 
 * 							add ciVoidTransAMDate, get/set methods 
 * 							add isNextDayVoid() 
 * 							modify toString(), equals(),
 * 							 TitlePackageReportData(int,int,...)
 * 							defect 10013 Ver 6.6.0  
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * TitlePackageReportData
 *
 * @version	6.6.0			09/19/2010  
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 13:29:16
 */
public class TitlePackageReportData implements Serializable
{
	// Dollar 
	private Dollar caItmPrice;

	// int 
	// defect 9976
	private int ciETtlCd;
	// end defect 9976

	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciTransWsId;

	// defect 10013 
	private int ciTtlExmnIndi;
	private int ciVoidTransAMDate;
	// end defect 10013

	private int ciVoidedTransIndi;

	// String 
	private String csAcctItmCd;
	private String csBatchNo;
	private String csTransCd;

	static final long serialVersionUID = 8393587652632744709L;

	/**
	 * TitlePackageReportData constructor
	 */
	public TitlePackageReportData()
	{
		super();
	}

	/**
	 * TitlePackageReportData constructor
	 * 
	 * @param aiOfcIssuanceNo int
	 * @param aiSubstaId int
	 * @param aiTransWsId int
	 * @param aiTransAMDate int
	 * @param aiTransTime int
	 * @param asTransCd String
	 * @param asBatchNo String
	 * @param asAcctItmCd String
	 * @param aiVoidedTransIndi int
	 * @param aaItmPrice Dollar
	 * @param aiETtlCd int 
	 * @param aiTtlExmnIndi int 
	 */
	public TitlePackageReportData(
		int aiOfcIssuanceNo,
		int aiSubstaId,
		int aiTransWsId,
		int aiTransAMDate,
		int aiTransTime,
		String asTransCd,
		String asBatchNo,
		String asAcctItmCd,
		int aiVoidedTransIndi,
		Dollar aaItmPrice,
		int aiETtlCd,
		int aiTtlExmnIndi)
	{
		super();
		setOfcIssuanceNo(aiOfcIssuanceNo);
		setSubstaId(aiSubstaId);
		setTransWsId(aiTransWsId);
		setTransAMDate(aiTransAMDate);
		setTransTime(aiTransTime);
		setTransCd(asTransCd);
		setBatchNo(asBatchNo);
		setAcctItmCd(asAcctItmCd);
		setVoidedTransIndi(aiVoidedTransIndi);
		setItmPrice(aaItmPrice);
		// defect 9976
		setETtlCd(aiETtlCd);
		// end defect 9976

		// defect 10013 
		setTtlExmnIndi(aiTtlExmnIndi);
		// end defect 10013  
	}

	/**
	 * equals used to compare equality b/w objects of this type
	 * 
	 * @param aaObj Object
	 * @return boolean
	 */
	public boolean equals(Object aaObj)
	{
		TitlePackageReportData laTPRD = (TitlePackageReportData) aaObj;
		return (
			laTPRD.getOfcIssuanceNo() == ciOfcIssuanceNo
			&& laTPRD.getSubstaId() == ciSubstaId
			&& laTPRD.getTransWsId() == ciTransWsId
			&& laTPRD.getTransAMDate() == ciTransAMDate
			&& laTPRD.getTransTime() == ciTransTime
			&& laTPRD.getVoidedTransIndi() == ciVoidedTransIndi
			&& laTPRD.getItmPrice().equals(caItmPrice)
			&& laTPRD.getTransCd().equals(csTransCd)
			&& laTPRD.getBatchNo().equals(csBatchNo)
			&& laTPRD.getAcctItmCd().equals(csAcctItmCd)
		// defect 9976
		&& laTPRD.getETtlCd() == ciETtlCd
		// end defect 9976 

		// defect 10013 
		&& laTPRD.getTtlExmnIndi() == ciTtlExmnIndi
		&& laTPRD.getVoidTransAMDate() == ciVoidTransAMDate);
		// end defect 10013 

	}

	/**
	 * Returns the value of AcctItmCd
	 * 
	 * @return String 
	 */
	public final String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	 * Returns the value of BatchNo
	 * 
	 * @return String 
	 */
	public final String getBatchNo()
	{
		return csBatchNo;
	}

	/**
	 * Return value of ETtlCd
	 * 
	 * @return int
	 */
	public int getETtlCd()
	{
		return ciETtlCd;
	}

	/**
	 * Returns the value of ItmPrice
	 * 
	 * @return Dollar 
	 */
	public final Dollar getItmPrice()
	{
		return caItmPrice;
	}

	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Returns the value of SubstaId
	 * 
	 * @return int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public String getTransId()
	{
		return UtilityMethods.getTransId(
			getOfcIssuanceNo(),
			getTransWsId(),
			getTransAMDate(),
			getTransTime());
	}

	/**
	 * Returns the value of TransCd
	 * 
	 * @return String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Returns the value of TransTime
	 * 
	 * @return int 
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns the value of TransWsId
	 * 
	 * @return int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Return value of TtlExmnIndi
	 * 
	 * @return int 
	 */
	public int getTtlExmnIndi()
	{
		return ciTtlExmnIndi;
	}

	/**
	 * Returns the value of VoidedTransIndi
	 * 
	 * @return int 
	 */
	public final int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}

	/**
	 * Is ETitle
	 * 
	 * @return boolean
	 */
	public boolean isETitle()
	{
		return ciETtlCd == TitleConstant.ELECTRONIC_ETTLCD;
	}
	/**
	 * Is TtlExmn
	 * 
	 * @return boolean
	 */
	public boolean isTtlExmn()
	{
		return ciTtlExmnIndi == 1;
	}

	/**
	 * Is VoidedTrans
	 * 
	 * @return boolean
	 */
	public boolean isVoidedTrans()
	{
		return ciVoidedTransIndi == 1;
	}

	/**
	 * Is Next Day Void
	 * 
	 * @return boolean
	 */
	public boolean isNextDayVoid()
	{
		return ciVoidTransAMDate > ciTransAMDate;
	}

	/**
	 * This method sets the value of AcctItmCd
	 * 
	 * @param asAcctItmCd String 
	 */
	public final void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * This method sets the value of BatchNo
	 * 
	 * @param asBatchNo String 
	 */
	public final void setBatchNo(String asBatchNo)
	{
		csBatchNo = asBatchNo;
	}

	/**
	 * Set value of ETtlCd
	 * 
	 * @param aiETtlCd
	 */
	public void setETtlCd(int aiETtlCd)
	{
		ciETtlCd = aiETtlCd;
	}

	/**
	 * This method sets the value of ItmPrice
	 * 
	 * @param aaItmPrice Dollar 
	 */
	public final void setItmPrice(Dollar aaItmPrice)
	{

		caItmPrice = aaItmPrice;

	}

	/**
	 * This method sets the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * This method sets the value of SubstaId
	 * 
	 * @param aiSubstaId int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * This method sets the value of TransAMDate
	 * 
	 * @param aiTransAMDate int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * This method sets the value of TransCd
	 * 
	 * @param asTransCd String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * This method sets the value of TransTime
	 * 
	 * @param aiTransTime int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * This method sets the value of TransWsId
	 * 
	 * @param aiTransWsId int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Set value of TtlExmnIndi
	 * 
	 * @param aiTtlExmnIndi
	 */
	public void setTtlExmnIndi(int aiTtlExmnIndi)
	{
		ciTtlExmnIndi = aiTtlExmnIndi;
	}

	/**
	 * This method sets the value of VoidedTransIndi
	 * 
	 * @param aiVoidedTransIndi int 
	 */
	public final void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}

	/**
	 * to String
	 * 
	 * @return String
	 */
	public String toString()
	{
		return "Office Issuance No: "
			+ ciOfcIssuanceNo
			+ "/nSubstation ID: "
			+ ciSubstaId
			+ "/nTransWsID: "
			+ ciTransWsId
			+ "/nTransAmDate: "
			+ ciTransAMDate
			+ "/nTransTime: "
			+ ciTransTime
			+ "/nTransCd: "
			+ csTransCd
			+ "/nBatchNo: "
			+ csBatchNo
			+ "/nAcctItmCd: "
			+ csAcctItmCd
			+ "/nVoidedTransIndi: "
			+ ciVoidedTransIndi
			+ "/nItmPrice: "
			+ caItmPrice
		// defect 9976
		+"/nETtlCd: " + ciETtlCd
		// end defect 9976
		// defect 10013 
		+"/nTtlExmnIndi: " + ciTtlExmnIndi;
		// end defect 10013  
	}

	/**
	 * Return value of ciVoidTransAMDate
	 * 
	 * @return int 
	 */
	public int getVoidTransAMDate()
	{
		return ciVoidTransAMDate;
	}

	/**
	 * Set value of ciVoidTransAMDate
	 * 
	 * @param aiVoidTransAMDate
	 */
	public void setVoidTransAMDate(int aiVoidTransAMDate)
	{
		ciVoidTransAMDate = aiVoidTransAMDate;
	}

}
