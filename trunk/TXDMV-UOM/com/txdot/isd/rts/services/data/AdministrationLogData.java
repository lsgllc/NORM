package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 *
 * AdministrationLogData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	08/17/2009	add getReportSearchData()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	08/22/2009	add constructors
 * 							add AdministrationLogData(ReportSearchData), 
 * 							AdministrationLogData()
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * AdministrationLogData
 *
 * @version	Defect_POS_F	08/22/2009
 * @author	Administrator
 * <br>Creation Date:		09/17/2001 
 */

public class AdministrationLogData implements Serializable
{
	// int 
	protected int ciOfcIssuanceNo;
	protected int ciSubStaId;
	protected int ciTransAMDate;
	protected int ciTransWsId;
	protected int ciTransTime;

	// Object 
	protected RTSDate caTransTimestmp;

	// String
	protected String csAction;
	protected String csEntity;
	protected String csEntityValue;
	protected String csTransEmpId;

	private final static long serialVersionUID = 1748589365692401182L;

	/**
	 * AdministrationLogData.java Constructor
	 *
	 */
	public AdministrationLogData()
	{
		super();
	}

	/**
	 * AdministrationLogData.java Constructor
	 *
	 */
	public AdministrationLogData(ReportSearchData aaRptSearchData)
	{
		ciOfcIssuanceNo = aaRptSearchData.getIntKey1();
		ciSubStaId = aaRptSearchData.getIntKey2();
		ciTransWsId = aaRptSearchData.getIntKey3();
		csTransEmpId = aaRptSearchData.getKey1();
		ciTransAMDate = RTSDate.getCurrentDate().getAMDate();
		ciTransTime = RTSDate.getCurrentDate().get24HrTime();
	}
	
	/**
	 * 
	 * AdministrationLogData.java Constructor
	 * 
	 */
	public AdministrationLogData(boolean abSourceType)
	{
		super();
		
		if (abSourceType == ReportConstant.CLIENT)
		{
			ciOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo();
			ciSubStaId = SystemProperty.getSubStationId();
			ciTransWsId = SystemProperty.getWorkStationId();
			csTransEmpId = SystemProperty.getCurrentEmpId();
		}
		ciTransAMDate = RTSDate.getCurrentDate().getAMDate();
		ciTransTime = RTSDate.getCurrentDate().get24HrTime();
	}

	/**
	 * Returns the value of Action
	 * 
	 * @return  String 
	 */
	public final String getAction()
	{
		return csAction;
	}
	/**
	 * Returns the value of Entity
	 * 
	 * @return  String 
	 */
	public final String getEntity()
	{
		return csEntity;
	}
	/**
	 * Returns the value of EntityValue
	 * 
	 * @return  String 
	 */
	public final String getEntityValue()
	{
		return csEntityValue;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Return ReportSearchData with 
	 * OfcIssuanceno,SubstaId, TransWsid, EmpId 
	 * 
	 * @return ReportSearchData
	 */
	public ReportSearchData getReportSearchData()
	{
		ReportSearchData laRptSearchData = new ReportSearchData();
		laRptSearchData.setKey1(csTransEmpId);
		laRptSearchData.setIntKey1(ciOfcIssuanceNo);
		laRptSearchData.setIntKey2(ciSubStaId);
		laRptSearchData.setIntKey3(ciTransWsId);
		return laRptSearchData;
	}

	/**
	 * Returns the value of SubStaId
	 * 
	 * @return  int 
	 */
	public final int getSubStaId()
	{
		return ciSubStaId;
	}
	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return  int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}
	/**
	 * Returns the value of TransEmpId
	 * 
	 * @return  String 
	 */
	public final String getTransEmpId()
	{
		return csTransEmpId;
	}
	/**
	 * Returns the value of TransTime
	 * 
	 * @return  int 
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}
	/**
	 * Returns the value of TransTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getTransTimestmp()
	{
		return caTransTimestmp;
	}
	/**
	 * Returns the value of TransWsId
	 * 
	 * @return  int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}
	/**
	 * This method sets the value of Action.
	 * 
	 * @param asAction   String 
	 */
	public final void setAction(String asAction)
	{
		csAction = asAction;
	}
	/**
	 * This method sets the value of Entity.
	 * 
	 * @param asEntity   String 
	 */
	public final void setEntity(String asEntity)
	{
		csEntity = asEntity;
	}
	/**
	 * This method sets the value of EntityValue.
	 * 
	 * @param asEntityValue   String 
	 */
	public final void setEntityValue(String asEntityValue)
	{
		csEntityValue = asEntityValue;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of SubStaId.
	 * 
	 * @param aiSubStaId   int 
	 */
	public final void setSubStaId(int aiSubStaId)
	{
		ciSubStaId = aiSubStaId;
	}
	/**
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate   int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}
	/**
	 * This method sets the value of TransEmpId.
	 * 
	 * @param asTransEmpId   String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
	/**
	 * This method sets the value of TransTime.
	 * 
	 * @param aiTransTime   int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
	/**
	 * This method sets the value of TransTimestmp.
	 * 
	 * @param aaTransTimestmp   RTSDate 
	 */
	public final void setTransTimestmp(RTSDate aaTransTimestmp)
	{
		caTransTimestmp = aaTransTimestmp;
	}
	/**
	 * This method sets the value of TransWsId.
	 * 
	 * @param aiTransWsId   int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}
}
