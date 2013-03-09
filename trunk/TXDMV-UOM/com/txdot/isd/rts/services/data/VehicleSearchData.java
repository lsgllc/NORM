package com.txdot.isd.rts.services.data;

/*
 *
 * VehicleSearchData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		08/01/2002	Added ability to arrow up and down through 
 * 							the registration records as soon as the data
 * 							is displayed in REG102. Changed 
 * 							getScrollPaneTable method to use table model
 * 							TMREG102, and changed populateReportsTable 
 * 							method to add to a vector, with each vector 
 * 							row (a VehicleSearchData object) loaded in 
 * 							the table that will be viewed in 
 * 							frmSearchResults (REG102). Added classes 
 * 							TMREG102 and VehicleSearchData.
 * 							defect 4416 Ver
 * Jeff S.		02/23/2005	Get code up to standards.
 * 							Moved from webapps.registrationrenewal.ui
 *							defect 7889 Ver 5.2.3
 * Jeff S.		06/17/2005	Renamed FrmSearchResultsREG102 and a comment
 * 							was changed to correspond with the change.
 *							defect 7889 Ver 5.2.3
 * K Harrell	09/30/2005	Moved to services.data	
 * 							defect 7889 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Data Object that holds the Phase Vehicle Data used on 
 * FrmSearchResultsREG102 and the Table model used on that frame.
 *
 * @version	5.2.3		09/30/2005
 * @author	B Brown
 * <br>Creation Date:	08/02/2002 12:57:45
 */
public class VehicleSearchData
{
	public String csPlateNo;
	public String csName;
	public String csRenewalDateTime;
	public String csCountyProcessedDate;
	public String csStatus;
	
	/**
	 * VehicleSearchData constructor comment.
	 */
	public VehicleSearchData()
	{
		super();
	}
	/**
	 * Get the County Processed Date.
	 * 
	 * @return String
	 */
	public String getCountyProcessedDate()
	{
		return csCountyProcessedDate;
	}
	/**
	 * Get the name of the person that is being processed.
	 * 
	 * @return String
	 */
	public String getName()
	{
		return csName;
	}
	/**
	 * Get the Plate Number of the person that is being processed.
	 * 
	 * @return String
	 */
	public String getPlateNo()
	{
		return csPlateNo;
	}
	/**
	 * Get the date and time of the renewal.
	 * 
	 * @return String
	 */
	public String getRenewalDateTime()
	{
		return csRenewalDateTime;
	}
	/**
	 * Get the status of the Internet Renewal.
	 * 
	 * @return String
	 */
	public String getStatus()
	{
		return csStatus;
	}
	/**
	 * Set the County processed date.
	 * 
	 * @param asCountyProcessedDate String
	 */
	public void setCountyProcessedDate(String asCountyProcessedDate)
	{
		csCountyProcessedDate = asCountyProcessedDate;
	}
	/**
	 * Set the Name of the person that is being processed.
	 * 
	 * @param asName String
	 */
	public void setName(String asName)
	{
		csName = asName;
	}
	/**
	 * Set the plate number of the person being processed.
	 * 
	 * @param asPlateNo String
	 */
	public void setPlateNo(String asPlateNo)
	{
		csPlateNo = asPlateNo;
	}
	/**
	 * Set the Date of the renewal.
	 * 
	 * @param asRenewalDateTime String
	 */
	public void setRenewalDateTime(String asRenewalDateTime)
	{
		csRenewalDateTime = asRenewalDateTime;
	}
	/**
	 * Set the status of the internet renewal.
	 * 
	 * @param asStatus String
	 */
	public void setStatus(String asStatus)
	{
		csStatus = asStatus;
	}
}
