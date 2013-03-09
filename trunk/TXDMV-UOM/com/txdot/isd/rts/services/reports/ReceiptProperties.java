package com.txdot.isd.rts.services.reports;

/*
 * ReceiptProperties.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	08/24/2001	Methods created
 * R Duggirala	09/04/2001	Added comments
 * S Johnston	05/09/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify
 *							defect 7896 Ver 5.2.3	  
 * ---------------------------------------------------------------------
 */
/**
 * The ReportProperties Class is intended to capture all the properties
 * that defines a report.  eg.  Pagewidth, height, margins, uniqueName 
 * etc
 *
 * @version	5.2.3			05/09/2005
 * @author	Rakesh Duggirala
 * <br>Creation Date:		02/27/2002 15:27:19
 */
public class ReceiptProperties
{
	protected int cvReceipt;
	protected String csUniqueName;
	protected int ciWorkstationId;
	protected int ciPageWidth;
	protected int ciPageHeight;
	protected int ciOfficeIssuanceId;
	public ReportProperties caRptProps;
	
	/**
	 * ReceiptProperties constructor
	 */
	public ReceiptProperties()
	{
		caRptProps = new ReportProperties();
	}
	
	/**
	 * ReceiptProperties constructor
	 */
	public ReceiptProperties(String asReceiptName)
	{
		caRptProps = new ReportProperties(asReceiptName);
	}
	
	/**
	 * getOfficeIssuanceId
	 * 
	 * @return int
	 */
	public int getOfficeIssuanceId()
	{
		int liOfficeIssuanceId = caRptProps.getOfficeIssuanceId();
		return liOfficeIssuanceId;
	}
	
	/**
	 * getPageHeight
	 * 
	 * @return int
	 */
	public int getPageHeight()
	{
		int liPageHeight = caRptProps.getPageHeight();
		return liPageHeight;
	}
	
	/**
	 * Get the page width from the ReportProperties.
	 * 
	 * @return int
	 */
	public int getPageWidth()
	{
		int liPageWidth = caRptProps.getPageWidth();
		return liPageWidth;
	}
	
	/**
	 * getUniqueName
	 * 
	 * @return String
	 */
	public String getUniqueName()
	{
		String liReceiptName = caRptProps.getUniqueName();
		return liReceiptName;
	}
	
	/**
	 * getWorkstationId
	 * 
	 * @return int
	 */
	public int getWorkstationId()
	{
		int liWorkstationId = caRptProps.getWorkstationId();
		return liWorkstationId;
	}
	
	/**
	 * setOfficeIssuanceId
	 * 
	 * @param aiNewOfficeIssuanceId int
	 */
	public void setOfficeIssuanceId(int aiNewOfficeIssuanceId)
	{
		ciOfficeIssuanceId = aiNewOfficeIssuanceId;
	}
	
	/**
	 * setPageHeight
	 * 
	 * @param aiNewPageHeight int
	 */
	public void setPageHeight(int aiNewPageHeight)
	{
		ciPageHeight = aiNewPageHeight;
	}
	
	/**
	 * setPageWidth
	 * 
	 * @param aiNewPageWidth int
	 */
	public void setPageWidth(int aiNewPageWidth)
	{
		ciPageWidth = aiNewPageWidth;
	}
	
	/**
	 * setUniqueName
	 * 
	 * @param asNewUniqueName String
	 */
	public void setUniqueName(String asNewUniqueName)
	{
		csUniqueName = asNewUniqueName;
	}
	
	/**
	 * setWorkstationId
	 * 
	 * @param aiNewWorkstationId int
	 */
	public void setWorkstationId(int aiNewWorkstationId)
	{
		ciWorkstationId = aiNewWorkstationId;
	}
}