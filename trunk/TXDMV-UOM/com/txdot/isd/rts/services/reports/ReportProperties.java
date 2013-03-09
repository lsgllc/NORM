package com.txdot.isd.rts.services.reports;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.SubstationCache;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.data.SubstationData;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 * ReportProperties.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	08/24/2001	Methods created
 * R Duggirala	09/04/2001	Added comments 
 * B Hargrove	07/27/2004	Format Class javadoc, comments to standard 
 *							Change 'siPageHeight' to 75 from 77 so that
 *							footer prints on correct page.
 *							modified ReportProperties() and (String)
 *							defect 7250 Ver 5.2.1
 * K Harrell	08/29/2004	Remove defect 7250
 *							defect 7431, 7499 Ver 5.2.1
 * S Johnston	05/09/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify
 *							defect 7896 Ver 5.2.3
 * K Harrell	06/22/2009	add initClientInfo()
 * 							defect 10023 Ver Defect_POS_F
 * B Brown		08/06/2010	add a constructor to allow the changing of
 * 							ciPageHeight for reports that do not use 
 * 							the POS reports pcl code and fonting
 * 							add constructor ReportProperties(String,int) 
 * 							defect 10512 Ver POS_650
 * ---------------------------------------------------------------------
 */
/**
 * The ReportProperties Class is intended to capture all the properties
 * that defines a report. eg. Pagewidth, height, margins, uniqueName etc
 *
 * @version	Defect_POS_F	06/22/2009 
 * @author	Rakesh Duggirala
 * <br>Creation Date:		08/24/2001 14:13:54
 */
public class ReportProperties
{
	protected int ciOfficeIssuanceId;
	protected int ciPageWidth; //Defining the page width
	protected int ciPageHeight; //Defining the page height
	protected int ciSubstationId;
	protected int ciWorkstationId;
	protected String csOfficeIssuanceName = "NULL VALUE- OFFICEID";
	protected String csSubstationName = "NULL VALUE- SUBSTATION";
	protected String csUniqueName; //Defining the page uniqueName
	protected String csRequestedBy = "RDUGGIRALA";

	/**
	 * ReportProperties constructor
	 */
	public ReportProperties()
	{
		ciPageWidth = 132;
		// defect 7431, 7499
		// remove 7250 
		// defect 7250
		ciPageHeight = 77;
		//siPageHeight = 75;
		// end 7250
		// end defect 7431, 7499
	}

	/**
	 * ReportProperties constructor
	 * 
	 * @param asUniqueName String
	 */
	public ReportProperties(String asUniqueName)
	{
		ciPageWidth = 132;
		// defect 7431, 7499
		// remove 7250 
		// defect 7250
		ciPageHeight = 77;
		//siPageHeight = 75;
		// end 7250
		// end defect 7431, 7499
		csUniqueName = asUniqueName;
	}
	
	/**
	 * ReportProperties constructor
	 * 
	 * @param asUniqueName String
	 */
	public ReportProperties(String asUniqueName, int aiPageHeight)
	{
		ciPageWidth = 132;
		ciPageHeight = aiPageHeight;
		csUniqueName = asUniqueName;
	}


	/**
	 * Method to Get Office Issuance Id
	 * 
	 * @return int
	 */
	public int getOfficeIssuanceId()
	{
		return ciOfficeIssuanceId;
	}

	/**
	 * Method to Get Office Issuance Name
	 * 
	 * @return String
	 */
	public String getOfficeIssuanceName()
	{
		return csOfficeIssuanceName;
	}

	/**
	 * Method to get the page height
	 * 
	 * @return int
	 */
	public int getPageHeight()
	{
		return ciPageHeight;
	}

	/**
	 * Method to get the page width
	 * 
	 * @return int
	 */
	public int getPageWidth()
	{
		return ciPageWidth;
	}

	/**
	 * Method to Get Requested By
	 * 
	 * @return String
	 */
	public String getRequestedBy()
	{
		return csRequestedBy;
	}

	/**
	 * Method to Get Substation Id
	 * 
	 * @return int
	 */
	public int getSubstationId()
	{
		return ciSubstationId;
	}

	/**
	 * Method to Get Substation Name
	 * 
	 * @return String
	 */
	public String getSubstationName()
	{
		return csSubstationName;
	}

	/**
	 * Method to get the uniqueName
	 * 
	 * @return String
	 */
	public String getUniqueName()
	{
		return csUniqueName;
	}

	/**
	 * Method to Get Workstation Id
	 * 
	 * @return int
	 */
	public int getWorkstationId()
	{
		return ciWorkstationId;
	}

	/**
	 * Initialize w/ Client Info 
	 * 
	 */
	public void initClientInfo()
	{
		ciOfficeIssuanceId = SystemProperty.getOfficeIssuanceNo();
		ciSubstationId = SystemProperty.getSubStationId();

		OfficeIdsData laOfficeName =
			OfficeIdsCache.getOfcId(ciOfficeIssuanceId);
		csOfficeIssuanceName  = laOfficeName.getOfcName();

		SubstationData laSubstationData =
			SubstationCache.getSubsta(
				ciOfficeIssuanceId,
				ciSubstationId);
		csSubstationName = laSubstationData.getSubstaName();

		ciWorkstationId = SystemProperty.getWorkStationId();
		csRequestedBy = SystemProperty.getCurrentEmpId();
	}

	/**
	 * Method to Set Office Issuance Id
	 * 
	 * @param aiNewOfficeIssuanceId int
	 */
	public void setOfficeIssuanceId(int aiNewOfficeIssuanceId)
	{
		ciOfficeIssuanceId = aiNewOfficeIssuanceId;
	}

	/**
	 * Method to Set Office Issuance Name
	 * 
	 * @param asNewOfficeIssuanceName String
	 */
	public void setOfficeIssuanceName(String asNewOfficeIssuanceName)
	{
		csOfficeIssuanceName = asNewOfficeIssuanceName;
	}

	/**
	 * Method to set the page height
	 * 
	 * @param aiNewPageHeight int
	 */
	public void setPageHeight(int aiNewPageHeight)
	{
		ciPageHeight = aiNewPageHeight;
	}

	/**
	 * This method sets the page height and width depending on the page
	 * orientation that is needed. The defualt is portrait, 1 indicates
	 * landscape
	 * 
	 * @param aiPageOrientation int
	 */
	public void setPageOrientation(int aiPageOrientation)
	{
		switch (aiPageOrientation)
		{
			case 1 :
				{
					setPageHeight(52);
					setPageWidth(165);
					return;
				}
			default :
				{
					setPageHeight(77);
					setPageWidth(132);
					return;
				}
		}
	}

	/**
	 * Method to set the page width
	 * 
	 * @param newSiPageWidth int
	 */
	public void setPageWidth(int aiNewPageWidth)
	{
		ciPageWidth = aiNewPageWidth;
	}

	/**
	 * Method to Set Requested By
	 * 
	 * @param asNewRequestedBy String
	 */
	public void setRequestedBy(String asNewRequestedBy)
	{
		csRequestedBy = asNewRequestedBy;
	}

	/**
	 * Method to Set Substation Id
	 * 
	 * @param aiNewSubstationId int
	 */
	public void setSubstationId(int aiNewSubstationId)
	{
		ciSubstationId = aiNewSubstationId;
	}

	/**
	 * Method to set Substation Name
	 * 
	 * @param asNewSubstationName String
	 */
	public void setSubstationName(String asNewSubstationName)
	{
		csSubstationName = asNewSubstationName;
	}

	/**
	 * Method to set the uniqueName
	 * 
	 * @param asNewUniqueName String
	 */
	public void setUniqueName(String asNewUniqueName)
	{
		csUniqueName = asNewUniqueName;
	}

	/**
	 * Method to Set Workstation Id
	 * 
	 * @param aiNewWorkstationId int
	 */
	public void setWorkstationId(int aiNewWorkstationId)
	{
		ciWorkstationId = aiNewWorkstationId;
	}
}
