package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * FundsData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/10/2004	Move assignment of variables to initClient
 *							Correct assignment of ciCashWsId
 *							modify constructor
 *							add initializeClient()
 *							defect 6638 Ver 5.2.2
 * K Harrell	11/10/2004	add ciWorkstationId and associated get/set
 *							methods.  General class cleanup. 
 *							modify initializeClient()
 *							defect 7681 Ver 5.2.2
 * K Harrell	06/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	08/17/2009	add getReportSearchData()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	03/11/2010	add caAdminLogData, get/set methods
 * 							defect 10168 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * Data object contains the cashdrawers, employees, report criteria,
 * system properties, and other flags collected in any of the Funds
 * events.
 *
 * @version	POS_640		03/11/2010
 * @author	Bobby Tulsiani
 * <br>Creation Date:	09/06/2001  13:30:59 
 */

public class FundsData implements Serializable
{
	// boolean 
	private boolean cbAllCashDrawers;
	private boolean cbAllEmployees;
	private boolean cbBatch;
	private boolean cbDisplayedFUN002;
	private boolean cbDisplayedFUN007;
	private boolean cbCountyWide;

	// int 
	private int ciCashWsId;
	private int ciOfficeIssuanceNo;
	private int ciSubStationId;
	private int ciWorkstationId;

	// Object
	private FundsReportData caFundsReportData;
	private RTSDate caSummaryEffDate;

	// defect 10168 
	private AdministrationLogData caAdminLogData;
	// end defect 10168 

	//	String 
	private String csEmployeeId;

	//	Vector 
	private Vector cvCashDrawers;
	private Vector cvEmployees;
	private Vector cvInventoryData;
	private Vector cvReportStatus;
	private Vector cvSelectedCashDrawers;
	private Vector cvSelectedEmployees;

	private final static long serialVersionUID = 4516266934097848325L;
	/**
	 * FundsData constructor comment.
	 */
	public FundsData()
	{
		super();
		// defect 6638
		if (!Comm.isServer())
		{
			initializeClient();
		}
		// end defect 6638
	}

	/**
	 * Return AdminLogData 
	 * 
	 * @return
	 */
	public AdministrationLogData getAdminLogData()
	{
		return caAdminLogData;
	}

	/**
	 * Return vector of CashDrawers
	 * 
	 * @return Vector
	 */
	public Vector getCashDrawers()
	{
		return cvCashDrawers;
	}
	/**
	 * Return CashWsId 
	 * 
	 * @return int
	 */
	public int getCashWsId()
	{
		return ciCashWsId;
	}
	/**
	 * Return EmployeeId
	 * 
	 * @return String
	 */
	public String getEmployeeId()
	{
		return csEmployeeId;
	}
	/**
	 * Return Vector of Employees
	 * 
	 * @return Vector
	 */
	public Vector getEmployees()
	{
		return cvEmployees;
	}
	/**
	 * Return FundsReportData object
	 * 
	 * @return FundsReportData
	 */
	public FundsReportData getFundsReportData()
	{
		return caFundsReportData;
	}
	/**
	 * Return Vector of InventoryData
	 * 
	 * @return Vector
	 */
	public Vector getInventoryData()
	{
		return cvInventoryData;
	}
	/**
	 * Return OfficeIssuanceNo
	 *
	 * @return int
	 */
	public int getOfficeIssuanceNo()
	{
		return ciOfficeIssuanceNo;
	}
	/**
	 * Return vector of ReportStatus
	 *
	 * @return Vector
	 */
	public Vector getReportStatus()
	{
		return cvReportStatus;
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
		laRptSearchData.setKey1(csEmployeeId);
		laRptSearchData.setIntKey1(ciOfficeIssuanceNo);
		laRptSearchData.setIntKey2(ciSubStationId);
		laRptSearchData.setIntKey3(ciWorkstationId);
		return laRptSearchData;
	}

	/**
	 * Return vector of Selected CashDrawers
	 *
	 * @return Vector
	 */
	public Vector getSelectedCashDrawers()
	{
		return cvSelectedCashDrawers;
	}
	/**
	 * Return vector of Selected Employees
	 *
	 * @return Vector
	 */
	public Vector getSelectedEmployees()
	{
		return cvSelectedEmployees;
	}
	/**
	 * Return SubstationId
	 *
	 * @return int
	 */
	public int getSubStationId()
	{
		return ciSubStationId;
	}
	/**
	 * Return Summary Effective Date
	 *
	 * @return RTSDate
	 */
	public RTSDate getSummaryEffDate()
	{
		return caSummaryEffDate;
	}
	/**
	 * Return WorkstationId
	 * 
	 * @return int
	 */
	public int getWorkstationId()
	{
		return ciWorkstationId;
	}
	/**
	 * Assign values meaningful to client only
	 * 
	 */
	private void initializeClient()
	{
		try
		{
			ciOfficeIssuanceNo = SystemProperty.getOfficeIssuanceNo();
			ciSubStationId = SystemProperty.getSubStationId();
			csEmployeeId = SystemProperty.getCurrentEmpId();
			ciCashWsId =
				(
					(AssignedWorkstationIdsData) AssignedWorkstationIdsCache
					.getAsgndWsId(
						ciOfficeIssuanceNo,
						ciSubStationId,
						SystemProperty.getWorkStationId()))
					.getCashWsId();
			// defect 7681 
			ciWorkstationId = SystemProperty.getWorkStationId();
			// end defect 7681 
		}
		catch (RTSException aeRTSEx)
		{
			ciCashWsId = SystemProperty.getWorkStationId();
		}
	}
	/**
	 * Return isAllCashDrawers
	 * 
	 * @return boolean
	 */
	public boolean isAllCashDrawers()
	{
		return cbAllCashDrawers;
	}
	/**
	 * Return isAllEmployees
	 * 
	 * @return boolean
	 */
	public boolean isAllEmployees()
	{
		return cbAllEmployees;
	}
	/**
	 * Return isBatch
	 *
	 * @return boolean
	 */
	public boolean isBatch()
	{
		return cbBatch;
	}
	/**
	 * Return DisplayedFUN002
	 * 
	 * @return boolean
	 */
	public boolean isDisplayedFUN002()
	{
		return cbDisplayedFUN002;
	}
	/**
	 * Return DisplayedFUN007
	 *
	 * @return boolean
	 */
	public boolean isDisplayedFUN007()
	{
		return cbDisplayedFUN007;
	}
	/**
	 * Return CountyWide
	 * 
	 * @return boolean
	 */
	public boolean runCountyWide()
	{
		return cbCountyWide;
	}

	/**
	 * Set AdminLogData
	 * 
	 * @param aaAdminLogData
	 */
	public void setAdminLogData(AdministrationLogData aaAdminLogData)
	{
		caAdminLogData = aaAdminLogData;
	}

	/**
	 * Assign value to AllCashDrawers
	 * 
	 * @param abAllCashDrawers boolean
	 */
	public void setAllCashDrawers(boolean abAllCashDrawers)
	{
		cbAllCashDrawers = abAllCashDrawers;
	}
	/**
	 * Assign value to AllEmployees
	 *
	 * @param abAllEmployees boolean
	 */
	public void setAllEmployees(boolean abAllEmployees)
	{
		cbAllEmployees = abAllEmployees;
	}
	/**
	 * Assign value to Batch
	 * 
	 * @param abBatch boolean
	 */
	public void setBatch(boolean abBatch)
	{
		cbBatch = abBatch;
	}
	/**
	 * Assign value to CashDrawers
	 * 
	 * @param avCashDrawers Vector
	 */
	public void setCashDrawers(Vector avCashDrawers)
	{
		cvCashDrawers = avCashDrawers;
	}
	/**
	 * Assign value to CashWsId
	 * 
	 * @param aiCashWsId int
	 */
	public void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * Assign value to runCountyWide
	 * 
	 * @param abCountyWide boolean
	 */
	public void setCountyWide(boolean abCountyWide)
	{
		cbCountyWide = abCountyWide;
	}
	/**
	 * Assign value to DisplayedFUN002
	 * 
	 * @param abDisplayedFUN002 boolean
	 */
	public void setDisplayedFUN002(boolean abDisplayedFUN002)
	{
		cbDisplayedFUN002 = abDisplayedFUN002;
	}
	/**
	 * Assign value to DisplayedFUN007
	 * 
	 * @param abDisplayedFUN007 boolean
	 */
	public void setDisplayedFUN007(boolean abDisplayedFUN007)
	{
		cbDisplayedFUN007 = abDisplayedFUN007;
	}
	/**
	 * Assign value to EmployeeId
	 * 
	 * @param asEmployeeId String
	 */
	public void setEmployeeId(String asEmployeeId)
	{
		csEmployeeId = asEmployeeId;
	}
	/**
	 * Assign value to Employees
	 * 
	 * @param avEmployees Vector
	 */
	public void setEmployees(Vector avEmployees)
	{
		cvEmployees = avEmployees;
	}
	/**
	 * Assign value to FundsReportData
	 * 
	 * @param aaFundsReportData FundsReportData
	 */
	public void setFundsReportData(FundsReportData aaFundsReportData)
	{
		caFundsReportData = aaFundsReportData;
	}
	/**
	 * Assign value to InventoryData
	 * 
	 * @param avInventoryData Vector
	 */
	public void setInventoryData(Vector avInventoryData)
	{
		cvInventoryData = avInventoryData;
	}
	/**
	 * Assign value to OfficeIssuanceNo
	 * 
	 * @param aiOfficeIssuanceNo int
	 */
	public void setOfficeIssuanceNo(int aiOfficeIssuanceNo)
	{
		ciOfficeIssuanceNo = aiOfficeIssuanceNo;
	}
	/**
	 * Assign value to ReportStatus
	 * 
	 * @param avReportStatus Vector
	 */
	public void setReportStatus(Vector avReportStatus)
	{
		cvReportStatus = avReportStatus;
	}
	/**
	 * Assign value to SelectedCashDrawers
	 * 
	 * @param avSelectedCashDrawers Vector
	 */
	public void setSelectedCashDrawers(Vector avSelectedCashDrawers)
	{
		cvSelectedCashDrawers = avSelectedCashDrawers;
	}
	/**
	 * Assign value to SelectedEmployees
	 * 
	 * @param avSelectedEmployees Vector
	 */
	public void setSelectedEmployees(Vector avSelectedEmployees)
	{
		cvSelectedEmployees = avSelectedEmployees;
	}
	/**
	 * Assign value to SubStationId
	 * 
	 * @param aiSubStationId int
	 */
	public void setSubStationId(int aiSubStationId)
	{
		ciSubStationId = aiSubStationId;
	}
	/**
	 * Assign value to SummaryEffDate
	 * 
	 * @param aaSummaryEffDate RTSDate
	 */
	public void setSummaryEffDate(RTSDate aaSummaryEffDate)
	{
		caSummaryEffDate = aaSummaryEffDate;
	}
	/**
	 * Assign value to WorkstationId
	 * 
	 * @param aiWorkstationId int
	 */
	public void setWorkstationId(int aiWorkstationId)
	{
		ciWorkstationId = aiWorkstationId;
	}

}
