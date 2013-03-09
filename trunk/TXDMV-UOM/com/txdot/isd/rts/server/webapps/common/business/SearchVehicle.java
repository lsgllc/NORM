package com.txdot.isd.rts.server.webapps.common.business;

import java.util.Vector;

import com.txdot.isd.rts.client.common.business.CommonUtil;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.MessageConstants;

import com.txdot.isd.rts.server.common.business.VehicleInquiry;

/*
 * SearchVehicle.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford     10/09/2002  Activate Archive Search for Address Change.
 * Clifford     11/12/2002	Canceled Plate handling.
 * Clifford     03/18/2003	duplicate records, modified 
 * 							checkResults().
 * 							defect 5460 
 * Ray Rowehl   08/12/2003 	Set the from MF flag to true.
 *							modified searchMainFrame()
 *							defect 6011
 * Bob Brown	12/16/2003	Changed method searchMainFrame to add last 4
 *                          digits of VIN and transcd to SearchData 
 * 							Keys, so when ther are 2 active regis 
 * 							records on the MF during internet 
 * 							registration, or address change the internet
 * 							will get their vehicle info retrieved instead 
 *                          seeing a "System not available" message at 
 * 							the browser.
 *                          defect 6709 Ver 5.1.5 Fix 2
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3
 * K Harrell	10/25/2005	Java 1.4 Cleanup, structured code 
 * 							 to consolidate most returns    
 * 							deleted getMostRecentDocNo()
 * 							renamed NULL_DATA to NO_VEHICLE_FOUND
 * 							renamed csVin to csVIN 
 * 							defect 7889 Ver 5.2.3
 * K Harrell	02/13/2006	Do not modify MFVehicleData.cbFromMF as 
 * 							now initialized to true
 * 							modify searchMainFrame()
 * 							defect 6861 Ver 5.2.3  
 * B Brown		12/08/2010	Add the setting of key6 to "IRENEW"
 * 							modify searchMainFrame()
 * 							defect 10608 Ver 6.7.0   
 *-------------------------------------------------------------------------------
 */

/**
 * Provides interface between Texas OnLine and TxDOT processing for 
 * Vehicle Retrieval.
 *  
 * @version	6.7.0		12/08/2010
 * @author	James Giroux
 * <br>Creation Date:	10/02/2001 14:51:21
 */
public class SearchVehicle
{

	final static int NO_VEHICLE_FOUND = 0;
	final static int MAINFRAME_DOWN = 1;
	final static int UNIQUE_VEHICLE_FOUND = 2;
	final static int MULTIPLE_VEHICLES_FOUND = 3;

	private String csVIN;
	private String csPlateNo;

	// The application module requesting the vehicle search.
	// Default to Registration Renewal, which only search the 
	// active_inactive records. Address Change also searches archive.
	private int ciRequestModule = CommonConstants.REGISTRATION_RENEWAL;

	/**
	 * Constract a SearchVehicle object to search a mainframe
	 * vehicle record base on the plate, VIN, and docno info
	 * in a VehicleBaseData
	 * 
	 * @param aaVehBaseData VehicleBaseData 
	 */
	public SearchVehicle(VehicleBaseData aaVehBaseData)
	{
		csPlateNo = aaVehBaseData.getPlateNo();
		csVIN = aaVehBaseData.getVin();

		// Set zero length fields to null so that methods in this
		// class don't have to check for null and blank.
		if (csPlateNo != null && csPlateNo.length() == 0)
		{
			csPlateNo = null;
		}
		if (csVIN != null && csVIN.length() == 0)
		{
			csVIN = null;
		}
	}
	/**
	 * Construct a SearchVehicle object to search a mainframe
	 * vehicle record based on the plateno, vin, and docno info
	 * in a VehicleBaseData, and the request module.
	 * (Registration Renewal/Address Change) requesting the 
	 * vehicle search.
	 * 
	 * @param aaVehBaseData VehicleBaseData 
	 * @param aiRequestModule int  
	 */
	public SearchVehicle(
		VehicleBaseData aaVehBaseData,
		int aiRequestModule)
	{
		this(aaVehBaseData);

		// Overwrite the default request module setting.
		ciRequestModule = aiRequestModule;
	}
	/**
	 * Check for Match
	 *  
	 * @param asRetrievedPlateNo String
	 * @param asRetrievedVIN String
	 * @return boolean 
	 */
	private boolean checkForMatch(
		String asRetrievedPlateNo,
		String asRetrievedVIN)
	{
		boolean lbMatch = false;

		if (csPlateNo.equals(asRetrievedPlateNo))
		{
			// Retrieved VIN is not four digits (vary, can be none), 
			// and csVIN may not be four digits 
			// (when it got set the first search, and travels back).

			if (csVIN.length() >= 4 && asRetrievedVIN.length() >= 4)
			{
				String lsLastFourDigitsOfRetrievedVIN =
					asRetrievedVIN.substring(
						asRetrievedVIN.length() - 4,
						asRetrievedVIN.length());

				String lsLastFourDigitsOfVIN =
					csVIN.substring(csVIN.length() - 4, csVIN.length());

				if (lsLastFourDigitsOfVIN
					.equals(lsLastFourDigitsOfRetrievedVIN))
				{
					lbMatch = true;
				}
			}
			else if (csVIN.equals(asRetrievedVIN))
			{
				lbMatch = true;
			}
		}
		return lbMatch;
	}
	/**
	 * Check Results
	 *  
	 * @param aaVehInqData VehicleInquiryData
	 * @return int 
	 */
	private int checkResults(VehicleInquiryData aaVehInqData)
	{
		int liResult = NO_VEHICLE_FOUND;

		if (aaVehInqData != null)
		{
			MFVehicleData laMFVehicleData =
				aaVehInqData.getMfVehicleData();

			// Cancelled Plate 
			if (laMFVehicleData != null
				&& laMFVehicleData.getRegData().getCancPltIndi() == 1)
			{
				// Assigning for clarity  
				liResult = NO_VEHICLE_FOUND;
			}
			// Mainframe Down 
			else if (aaVehInqData.getMfDown() == 1)
			{
				liResult = MAINFRAME_DOWN;
			}
			// Records Returned 
			else
			{
				// check for number of vehicles returned
				int liVehicleCount = aaVehInqData.getNoMFRecs();

				if (liVehicleCount == 1)
				{
					liResult = UNIQUE_VEHICLE_FOUND;
				}
				// Could be just 'else' as should not return 0 count
				else if (liVehicleCount > 1)
				{
					liResult = MULTIPLE_VEHICLES_FOUND;
				}
			}
		}
		return liResult;
	}
	/**
	 * This method is used when a unique record cannot be found
	 * by a VIN search. It searches thru all DocNo's and finds
	 * the DocNo with the highest AMDate (Characters 7-11).
	 * It returns the DocNo with the highest AMDate
	 *  
	 * @param aaVehInqData VehicleInquiryData
	 * @return String 
	 * @deprecated
	 */
//	private String getMostRecentDocNo(VehicleInquiryData aaVehInqData)
//	{
//		// get the vector which contains MFPartialData objects
//		Vector lvPartialDataList = aaVehInqData.getPartialDataList();
//		MFPartialData laMFPartialData = new MFPartialData();
//
//		int liHighestAMDate = 0;
//		String lsHighestDocNo = null;
//		String lsWorkingDocNo = null;
//		int liWorkingAMDate = 0;
//
//		try
//		{
//			for (int i = 0; i < lvPartialDataList.size(); i++)
//			{
//				laMFPartialData =
//					(MFPartialData) lvPartialDataList.elementAt(i);
//				lsWorkingDocNo = laMFPartialData.getDocNo();
//				liWorkingAMDate =
//					new Integer(lsWorkingDocNo.substring(6, 10))
//						.intValue();
//				if (liWorkingAMDate > liHighestAMDate)
//				{
//					liHighestAMDate = liWorkingAMDate;
//					lsHighestDocNo = lsWorkingDocNo;
//				}
//			}
//		}
//		catch (Exception aeEx)
//		{
//			return null;
//		}
//
//		return lsHighestDocNo;
//	}
	/**
	 * Get Result
	 *  
	 * @param aaVehInqData VehicleInquiryData 
	 * @param aiWhere int
	 * @return Object 
	 * @throws RTSException
	 */
	private Object getResult(
		VehicleInquiryData aaVehInqData,
		int aiWhere)
		throws RTSException
	{

		Object laResult = null;
		VehicleInquiryData laVehInqData = aaVehInqData;

		switch (checkResults(laVehInqData))
		{
			case NO_VEHICLE_FOUND :
				laResult = MessageConstants.MSG_VEHICLE_NOT_FOUND;
				break;

			case MAINFRAME_DOWN :
				laResult = MessageConstants.MSG_MAINFRAME_DOWN;
				break;

			case UNIQUE_VEHICLE_FOUND :
				laResult = processUniqueVehFound(laVehInqData);
				break;

			case MULTIPLE_VEHICLES_FOUND :
				laResult = processMultiVehFound(laVehInqData, aiWhere);
				break;
		}
		return laResult;
	}
	/**
	 * Perform the search against the mainframe and bring back the
	 * record if any, search active_inactive for registration.
	 * For address change, also search the archive if not found in
	 * active_inactive search.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	public Object getVehicle() throws RTSException
	{
		// First Search Active/Inactive
		int liWhere =
			com
				.txdot
				.isd
				.rts
				.services
				.util
				.constants
				.CommonConstant
				.SEARCH_ACTIVE_INACTIVE;

		Object laResult = getVehicle(liWhere);

		// Not Found and AddressChange 
		if (!(laResult instanceof VehicleInquiryData)
			&& ciRequestModule == CommonConstants.ADDRESS_CHANGE)
		{
			// Search Archive
			liWhere =
				com
					.txdot
					.isd
					.rts
					.services
					.util
					.constants
					.CommonConstant
					.SEARCH_ARCHIVE;
			laResult = getVehicle(liWhere);
		}
		return laResult;
	}
	/**
	 * Search the vehicle based on plateno, vin and docno, against
	 * main frame area indicated. aiWhere indicates the mainframe 
	 * search area (active_inactive/archive).
	 * 
	 * @param  aiWhere int
	 * @return Object 
	 * @throws RTSException
	*/
	private Object getVehicle(int aiWhere) throws RTSException
	{
		VehicleInquiryData laVehInqData =
			searchMainFrame(null, null, aiWhere);
		Object laResult = getResult(laVehInqData, aiWhere);
		return laResult;
	}
	/**
	 * Process Multiple Vehicles Found
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @param aiWhere int
	 * @return Object 
	 * @throws RTSException
	 */
	private Object processMultiVehFound(
		VehicleInquiryData aaVehInqData,
		int aiWhere)
		throws RTSException
	{

		Object laResult = null;

		// Now search by VIN
		String lsVIN = searchPartialDataList(aaVehInqData);

		if (lsVIN == null)
		{
			laResult = MessageConstants.MSG_VEHICLE_NOT_FOUND;
		}
		else
		{
			aaVehInqData =
				searchMainFrame(CommonConstant.VIN, lsVIN, aiWhere);
			if (checkResults(aaVehInqData) == UNIQUE_VEHICLE_FOUND)
			{
				laResult =
					new CommonUtil().validateVehWts(aaVehInqData);
			}
			else
			{
				laResult = MessageConstants.MSG_CANNOT_PROCESS_VEHICLE;
			}
		}

		/* This code was requested by Christiane sometime around 03/01/2002
		// A trouble ticket was created by VTR around 04/15/2002 to have it removed.
			String docNo = getMostRecentDocNo(lVehInqData);
				if (docNo == null) 
				{
					result = MessageConstants.MSG_VEHICLE_NOT_FOUND;
				}
				else
				{
					// Now search by DocNo
					lVehInqData = searchMainFrame(CommonConstant.DOC_NO, docNo);
					if (checkResults(lVehInqData) == UNIQUE_VEHICLE_FOUND)
					{
						result = new CommonUtil().validateVehWts(lVehInqData);
					}
					else
					{
						result = MessageConstants.MSG_CANNOT_PROCESS_VEHICLE;
					}
				}
		
			}
		}
		*/
		return laResult;
	}
	/**
	 * Process Unique Vehicle Found
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @return Object
	 */
	private Object processUniqueVehFound(VehicleInquiryData aaVehInqData)
	{

		Object laResult = null;

		String lsRetrievedPlateNo =
			aaVehInqData.getMfVehicleData().getRegData().getRegPltNo();
		String lsRetrievedPrevPltNo =
			aaVehInqData.getMfVehicleData().getRegData().getPrevPltNo();
		String lsRetrievedVIN =
			aaVehInqData.getMfVehicleData().getVehicleData().getVin();
		if (checkForMatch(lsRetrievedPlateNo, lsRetrievedVIN)
			|| checkForMatch(lsRetrievedPrevPltNo, lsRetrievedVIN))
		{
			// Vehicle weight may have bad record.
			// Correct any bad data, 12-21-2001, Todd, Joseph.
			laResult = new CommonUtil().validateVehWts(aaVehInqData);
		}
		else
		{
			laResult = MessageConstants.MSG_VEHICLE_NOT_FOUND;
		}
		return laResult;
	}
	/**
	 * Search Mainframe
	 *  
	 * @param asSearchBy String
	 * @param asSearchData  String
	 * @param aiWhere  int
	 * @return VehicleInquiryData
	 * @throws RTSException
	 */
	private VehicleInquiryData searchMainFrame(
		String asSearchBy,
		String asSearchData,
		int aiWhere)
		throws RTSException
	{
		GeneralSearchData laGenSearchData = new GeneralSearchData();
		VehicleInquiryData laVehInqData = new VehicleInquiryData();

		laGenSearchData.setIntKey2(aiWhere);

		if (asSearchBy == null)
		{
			laGenSearchData.setKey1(
				com
					.txdot
					.isd
					.rts
					.services
					.util
					.constants
					.CommonConstant
					.REG_PLATE_NO);
			laGenSearchData.setKey2(csPlateNo);
			// new for defect 6709 (related to 6212) - to help get 
			// correct MF rec when multiple MF regis recs
			laGenSearchData.setKey3(
				com
					.txdot
					.isd
					.rts
					.services
					.util
					.constants
					.TransCdConstant
					.INTERNET);
			laGenSearchData.setKey4(csVIN);
			// defect 10608
			if (ciRequestModule == CommonConstants.REGISTRATION_RENEWAL)
			{
				laGenSearchData.setKey6(TransCdConstant.IRENEW);
			}
			// end defect 10608
			// defect 10610
			if (ciRequestModule == CommonConstants.ADD_EREMINDER)
			{
				laGenSearchData.setKey6(TransCdConstant.IADDRE);
			}
			// end defect 10610
			// new for 6709 (related to 6212)
		}
		else
		{
			laGenSearchData.setKey1(asSearchBy);
			laGenSearchData.setKey2(asSearchData);
			laGenSearchData.setKey3("");
		}
		// for defect 6212 - move the next statement above   
		// lGSD.setKey3("");

		try
		{
			VehicleInquiry laVI = new VehicleInquiry();
			laVehInqData = laVI.getVeh(laGenSearchData);

			// defect 6861
			// Do not set as cbFromMF now initialized to true; 
			// defect 6011
			// Set the mf status to up.  fails on mf otherwise
			// laVehInqData.setMfDown(0);
			// laVehInqData.getMfVehicleData().setFromMF(true);
			// end defect 6011
			// end defect 6861 
		}
		catch (Exception aeEx)
		{
			throw new RTSException(RTSException.SYSTEM_ERROR, aeEx);
		}
		return laVehInqData;
	}
	/**
	 * Search Partial Data List
	 *  
	 * @param aaVehInqData VehicleInquiryData
	 * @return String 
	 */
	private String searchPartialDataList(VehicleInquiryData aaVehInqData)
	{
		Vector lvPartialDataList = aaVehInqData.getPartialDataList();
		String lsRetrievedRegPltNo = null;
		String lsRetrievedVIN = null;
		MFPartialData laMFPartialData = new MFPartialData();

		for (int i = 0; i < lvPartialDataList.size(); i++)
		{
			laMFPartialData =
				(MFPartialData) lvPartialDataList.elementAt(i);
			lsRetrievedRegPltNo = laMFPartialData.getRegPltNo();
			lsRetrievedVIN = laMFPartialData.getVin();
			if (checkForMatch(lsRetrievedRegPltNo, lsRetrievedVIN))
			{
				return lsRetrievedVIN;
			}
		}
		return null;
	}
}
