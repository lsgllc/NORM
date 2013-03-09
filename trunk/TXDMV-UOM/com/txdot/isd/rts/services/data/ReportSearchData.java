package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 *
 * ReportSearchData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	06/18/2009	Use ReportConstant
 * 							modify csNextScreen 
 * 							defect 10011 Ver Defect_POS_F
 * K Harrell	08/25/2009	add initForClient(),  
 * 							ReportSearchData(String,String,String,
 * 							 int,int), 
 * 							defect 10023/8628 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * ReportSearchData
 * 
 * 
 * @version	Defect_POS_F	08/25/2009 
 * @author 	Rakesh Duggirala
 * <br>Creation Date:	
 */

public class ReportSearchData extends GeneralSearchData
{

	//	Object
	private Object caData;

	// String
	// defect 10011
	//private String csNextScreen = "NONE";
	private String csNextScreen =
		ReportConstant.RPR000_NEXT_SCREEN_FINAL;
	// end defect 10011

	// Vector
	private Vector cvVector;

	/**
	 * ReportSearchData constructor comment.
	 */
	public ReportSearchData()
	{
		super();
	}

	/**
	 * ReportSearchData constructor 
	 * 
	 * Report Data/Attributes used by client.  
	 */
	public ReportSearchData(
		String asFormattedRptData,
		String asFileName,
		String asReportTitle,
		int aiNumberOfCopies,
		int aiOrientationCd)
	{
		super();
		setKey1(asFormattedRptData);
		setKey2(asFileName);
		setKey3(asReportTitle);
		setIntKey1(aiNumberOfCopies);
		setIntKey2(aiOrientationCd);
	}

	/**
	 * Return value of Data
	 * 
	 * @return Object
	 */
	public Object getData()
	{
		return caData;
	}

	/**
	 * Return value of NextScreen
	 *
	 * @return String
	 */
	public String getNextScreen()
	{
		return csNextScreen;
	}

	/**
	 * Return value of Vector
	 * 
	 * @return Vector
	 */
	public Vector getVector()
	{
		return cvVector;
	}

	/** 
	 * 
	 * Init values for Client Batch Reports
	 * 
	 */
	public void initForClient(boolean abBatch)
	{
		setIntKey1(SystemProperty.getOfficeIssuanceNo());
		setIntKey2(SystemProperty.getSubStationId());
		setIntKey3(SystemProperty.getWorkStationId());
		if (abBatch) 
		{
			setKey1(ReportConstant.SYSTEM_EMPID);
		}
		else
		{
			setKey1(SystemProperty.getCurrentEmpId()); 
		}
	}
	
	/**
	 * Return value of Data
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		caData = aaData;
	}

	/**
	 * Set value of NextScreen
	 *
	 * @param asNextScreen String
	 */
	public void setNextScreen(String asNextScreen)
	{
		csNextScreen = asNextScreen;
	}

	/**
	 * Set value of Vector
	 * 
	 * @param avVector Vector
	 */
	public void setVector(Vector avVector)
	{
		cvVector = avVector;
	}
}
