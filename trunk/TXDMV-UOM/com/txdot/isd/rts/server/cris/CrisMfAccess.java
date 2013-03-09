package com.txdot.isd.rts.server.cris;

import com.txdot.isd.rts.server.common.business.VehicleInquiry;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * CrisMfAccess.java
 * 
 * (c) Texas Department of Transportation  2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob Brown	6/18/2004	Initial writing.
 * Bob Brown	8/13/2004	Changed to return only VehModl,VehMk,
 *                          VehClassCd, and VehBdyType when more than 1
 *                          active regis rec exists on the MF.
 * Bob Brown	10/22/2004	Changed Field Label/DB AbstractValue parse char from
 *                          an "=" to an "*". Some data returned from 
 *                          the MF contained an "=". 
 * Bob Brown    11/17/2004  DPS wants now to only return:
 *                          "Status= Found more than 1 MF rec" when more
 *                          than 1 active regis rec exists on the MF.
 * Bob Brown    12/17/2004  Had to change the string returned when no MF
 *							record is found from:
 *						    "Status=" + lsPlateNo + " Not found #"
 *							to
 *                          "Status*" + lsPlateNo + " Not found #" 
 *                          because the parsing character changed to "*"
 *                          No RTS version connected with this.
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 * K Harrell	07/03/2009	modify getMFRec()
 * 							defect 10112 Ver Defect_POS_F
 *----------------------------------------------------------------------
 */

/**
 * CrisMfAccess is the MF access class used for DPS request for MF Vehicle info
 *
 * The client is required to pass in license plate number, username, and 
 * password, via a HEAD request.
 * The getMFRec method returns the DPS required fields to the requestor.
 * 
 * @version	Defect_POS_F	07/03/2009
 * @author	Bob Brown
 * <br>Creation Date:		05/14/2004 10:20:00
 */
public class CrisMfAccess
{
	static final String csUserName = "Cris601";
	static final String csPassword = "mf73767";
	
	/**
	 * CrisMfAccess constructor comment.
	 */
	public CrisMfAccess()
	{
		super();
	}
	
	/**
	 * Get MF Record
	 *
	 * @return String 
	 * @param String lsPlateNo
	 * @param String lsData
	 */
	public String getMFRec(String lsPlateNo, String lsData)
	{

		if (!isValidUser(lsData))
		{
			return ("Status*Invalid User Name/Password Combination #");
		}

		VehicleData laVehicleData = null;
		OwnerData laOwnerData = null;
		RegistrationData laRegData = null;
		GeneralSearchData laSearchData = null;

		VehicleInquiryData laVehicleInquiryData =
			new VehicleInquiryData();
		VehicleInquiry laVehicleInquiry = new VehicleInquiry();

		try
		{

			laSearchData = new GeneralSearchData();
			laSearchData.setKey1("REGPLTNO");
			laSearchData.setKey2(lsPlateNo);
			laSearchData.setKey3("CRIS1");
			// could return more than one record

			laVehicleInquiryData =
				laVehicleInquiry.getVeh(laSearchData);

		}

		catch (Exception leEx)
		{
			//System.out.println(lsPlateNo + " Could not be retrieved at this time");
			System.err.println(
				"CrisServlet error "
					+ " "
					+ (new RTSDate()).getYYYYMMDDDate()
					+ " "
					+ (new RTSDate()).getTime()
					+ " Plate no = "
					+ lsPlateNo
					+ " Could not be retrieved at this time"
					+ "==============");
			leEx.printStackTrace();

			return (
				"Status*"
					+ lsPlateNo
					+ " Could not be retrieved at this time #");
		}

		if (laVehicleInquiryData.getPartialDataList().size() > 1
			|| laVehicleInquiryData.getNoMFRecs() > 1)
		{

			return ("Status*Found more than 1 MF rec #");
			/*
			+
			"Plate*" + " " + "#" + 
			"Vin*" + " " + "#" + 
			"Model*" + " " + "#" +
				"Year*" + " "	+ "#" +
			"Make*" + " " + "#" +
			"VehClass*" + " " + "#" +
			"BodyType*" + " " +"#" +
			"GrossWeight*" + " " + "#" +
			
			"Name1*" + " " + "#" +
			"Name2*" + " " + "#" +
			
			//"RecipientName=" + lRegData.getRecpntName() + "#" +
			
			"Street1*" + " " + "#" + 
			 	"Street2*" + " " + "#" +
			  	"City*" + " " + "#" +
			   	"Country*" + " " + "#" +
			   	"State*" + " " + "#" +
			   	"ZipCode/first5*" + " " + "#" +
			   	"ZipCode/last4*" + " ");
			*/

			/*
			
			// DPS Cris project folks don't want anything back if there is more than one MF
			// record - the following code resolves the MF return to 1 record,and only
			// returns VehModl,VehMk,VehClassCd,VehBdyType, whcih was DPS first requirment more
			// multiple MF recs 
			
			// Save the following code, though, in case DPS changes their mind again			
			try 
			{
				searchData.setKey3("CRIS2"); // signals more than 1 rec returned
				MfAccess mf = new MfAccess();
				/*
				if (lVehicleInquiryData.getPartialDataList().firstElement() instanceof RegistrationData)
				{
					//most recent regis record returned when the type is RegistrationData
					RegistrationData loMFRegisData1 = (RegistrationData)lVehicleInquiryData.getPartialDataList().elementAt(0);
					
					 	searchData.setKey2(lVehicleInquiryData.getMfVehicleData().getVehicleData().getVin());
				}
			
				else
				
				if (lVehicleInquiryData.getPartialDataList().firstElement() instanceof MFPartialData)
				{
					
					MFPartialData loMFPartialData1 = (MFPartialData)lVehicleInquiryData.getPartialDataList().elementAt(0);
					MFPartialData loMFPartialData2 = (MFPartialData)lVehicleInquiryData.getPartialDataList().elementAt(1);
			
					if (loMFPartialData1.getDocNo().equals(loMFPartialData2.getDocNo()))
					{
						if (loMFPartialData1.getRegExpYr() >= loMFPartialData2.getRegExpYr() &&
							loMFPartialData1.getRegExpMo() >= loMFPartialData2.getRegExpMo())
						{
							searchData.setKey2(loMFPartialData1.getDocNo());
						}
						else
						{
							searchData.setKey2(loMFPartialData2.getDocNo());
						}
					}
					else
					{	
				
				        if ((Integer.parseInt(loMFPartialData1.getDocNo().substring(6,11))) >
					        (Integer.parseInt(loMFPartialData2.getDocNo().substring(6,11))))
				        {
				            searchData.setKey2(loMFPartialData1.getDocNo());
				        }
				        else
				        {
				            searchData.setKey2(loMFPartialData2.getDocNo());
				        }
					}
				}
			    searchData.setKey1(
			         com.txdot.isd.rts.services.util.constants.CommonConstant.DOC_NO);
			    lVehicleInquiryData = mf.retrieveVehicleFromActiveInactive(searchData);
			}
			
			catch (Exception ex) 
			{
				//System.out.println(lsPlateNo + " Could not be retrieved at this time");
				System.err.println(
				"CrisServlet error "
				+ " "
				+ (new RTSDate()).getYYYYMMDDDate()
				+ " "
				+ (new RTSDate()).getTime()
				+ " Plate no = " + lsPlateNo + " Could not be retrieved at this time"
				+ "==============");
				ex.printStackTrace();
			
				return ("Status=" + lsPlateNo + " Not found #");
			}
			*/

		}

		if (laVehicleInquiryData
			.getMfVehicleData()
			.getVehicleData()
			.getVin()
			== null)
		{
			System.out.println(
				"Plate number = "
					+ lsPlateNo
					+ " could not be found on the TxDOT Mainframe");
			return ("Status*" + lsPlateNo + " not found #");
			//return ("Status=" + lsPlateNo + " not found #");
		}
		else
		{

			laVehicleData =
				laVehicleInquiryData
					.getMfVehicleData()
					.getVehicleData();

			laOwnerData =
				laVehicleInquiryData.getMfVehicleData().getOwnerData();

			laRegData =
				laVehicleInquiryData.getMfVehicleData().getRegData();

			// the following if statements are so the StringTokenizer code will work

			if (laVehicleData.getVehModl().equals(""))
			{
				laVehicleData.setVehModl(" ");
			}

			if (laVehicleData.getVehMk().equals(""))
			{
				laVehicleData.setVehMk(" ");
			}

			if (laVehicleData.getVehClassCd().equals(""))
			{
				laVehicleData.setVehClassCd(" ");
			}

			if (laVehicleData.getVehBdyType().equals(""))
			{
				laVehicleData.setVehBdyType(" ");
			}

			String lsName1 = " ";
			String lsName2 = " ";
			AddressData laAddr = laOwnerData.getAddressData();

			// defect 10112 
			//	String lsStreet1 = " ";
			//	String lsStreet2 = " ";
			//	String lsCity = " ";
			//	String lsCntry = " ";
			//	String lsState = " ";
			//	String lsZpcd = " ";
			//	String lsZpcdp4 = " ";

			if (laSearchData.getKey3().equals("CRIS1"))
				// 1 record was returned.
			{

				if (laRegData.getRecpntName().equals(""))
					// lRegData.getRecpntName().equals(null))
				{

					if (!laOwnerData.getName1().equals(""))
					{
						lsName1 = laOwnerData.getName1();
						//lOwnerData.setOwnrTtlName1(" ");
					}

					if (!laOwnerData.getName2().equals(""))
					{
						lsName2 = laOwnerData.getName2();
						//lOwnerData.setOwnrTtlName2(" ");
					}

				} //lRegData.setRecpntName(" ");
				else
				{
					lsName1 = laRegData.getRecpntName();
				}

				if (laRegData.getRenwlMailAddr().isPopulated())
				{
					laAddr = laRegData.getRenwlMailAddr();
				}
				laAddr.initWhereNull();
				//	if (!laRegData.getRenwlMailAddr().getSt1().equals(""))
				//		// get all address info from the RegData class if
				//		// reg street has information
				//	{
				//		//lOwnerData.getOwnrAddr().setSt1(" ");
				//		//if (!lRegData.getRenwlMailAddr().getSt1().equals(""))
				//		//{
				//		lsStreet1 = laRegData.getRenwlMailAddr().getSt1();
				//		//}
				//
				//		if (!laRegData
				//			.getRenwlMailAddr()
				//			.getSt2()
				//			.equals(""))
				//		{
				//			lsStreet2 =
				//				laRegData.getRenwlMailAddr().getSt2();
				//		}
				//
				//		if (!laRegData
				//			.getRenwlMailAddr()
				//			.getCity()
				//			.equals(""))
				//		{
				//			lsCity = laRegData.getRenwlMailAddr().getCity();
				//		}
				//
				//		if (!laRegData
				//			.getRenwlMailAddr()
				//			.getCntry()
				//			.equals(""))
				//		{
				//			lsCntry =
				//				laRegData.getRenwlMailAddr().getCntry();
				//		}
				//
				//		if (!laRegData
				//			.getRenwlMailAddr()
				//			.getState()
				//			.equals(""))
				//		{
				//			lsState =
				//				laRegData.getRenwlMailAddr().getState();
				//		}
				//
				//		if (!laRegData
				//			.getRenwlMailAddr()
				//			.getZpcd()
				//			.equals(""))
				//		{
				//			lsZpcd = laRegData.getRenwlMailAddr().getZpcd();
				//		}
				//
				//		if (!laRegData
				//			.getRenwlMailAddr()
				//			.getZpcdp4()
				//			.equals(""))
				//		{
				//			lsZpcdp4 =
				//				laRegData.getRenwlMailAddr().getZpcdp4();
				//		}
				//	}
				//
				//	else
				//	{
				//
				//		if (!laOwnerData.getAddressData().getSt1().equals(""))
				//		{
				//			lsStreet1 = laOwnerData.getAddressData().getSt1();
				//		}
				//
				//		if (!laOwnerData.getAddressData().getSt2().equals(""))
				//		{
				//			lsStreet2 = laOwnerData.getAddressData().getSt2();
				//		}
				//
				//		if (!laOwnerData.getAddressData().getCity().equals(""))
				//		{
				//			lsCity = laOwnerData.getAddressData().getCity();
				//		}
				//
				//		if (!laOwnerData
				//			.getAddressData()
				//			.getCntry()
				//			.equals(""))
				//		{
				//			lsCntry = laOwnerData.getAddressData().getCntry();
				//		}
				//
				//		if (!laOwnerData
				//			.getAddressData()
				//			.getState()
				//			.equals(""))
				//		{
				//			lsState = laOwnerData.getAddressData().getState();
				//		}
				//
				//		if (!laOwnerData.getAddressData().getZpcd().equals(""))
				//		{
				//			lsZpcd = laOwnerData.getAddressData().getZpcd();
				//		}
				//
				//		if (!laOwnerData
				//			.getAddressData()
				//			.getZpcdp4()
				//			.equals(""))
				//		{
				//			lsZpcdp4 = laOwnerData.getAddressData().getZpcdp4();
				//		}
				//	}

			}

			return (
				"Status*Found #"
				+ "Plate*"
				+ lsPlateNo
				+ "#"
				+ "Vin*"
				+ laVehicleData.getVin()
				+ "#"
				+ "Model*"
				+ laVehicleData.getVehModl()
				+ "#"
				+ "Year*"
				+ laVehicleData.getVehModlYr()
				+ "#"
				+ "Make*"
				+ laVehicleData.getVehMk()
				+ "#"
				+ "VehClass*"
				+ laVehicleData.getVehClassCd()
				+ "#"
				+ "BodyType*"
				+ laVehicleData.getVehBdyType()
				+ "#"
				+ "GrossWeight*"
				+ laVehicleInquiryData
					.getMfVehicleData()
					.getRegData()
					.getVehGrossWt()
				+ "#"
				+ "Name1*"
				+ lsName1
				+ "#"
				+ "Name2*"
				+ lsName2
				+ "#"
				+ 

			//"RecipientName=" + lRegData.getRecpntName() + "#" +

				"Street1*"
				//+ lsStreet1
				+"#"
				+ "Street2*"
				//+ lsStreet2
				+ laAddr.getSt1()
				+ "#"
				+ "City*"
				//+ lsCity
				+ laAddr.getCity()
				+ "#"
				+ "Country*"
				//+ lsCntry
				+ laAddr.getCntry()
				+ "#"
				+ "State*"
				//+ lsState
				+ laAddr.getState()
				+ "#"
				+ "ZipCode/first5*"
				//+ lsZpcd
				+ laAddr.getZpcd()
				+ "#"
				+ "ZipCode/last4*"
				//+ lsZpcdp4);
				+ laAddr.getZpcdp4());
				// end defect 10112  
				/*
			  	"Recipient_Street1=" + lRegData.getRenwlMailAddr().getSt1() + "#" +
			  	"Recipient_Street2=" + lRegData.getRenwlMailAddr().getSt2() + "#" +
			  	"Recipient_City=" + lRegData.getRenwlMailAddr().getCity() + "#" +
			  	"Recipient_Country=" + lRegData.getRenwlMailAddr().getCntry() + "#" +
			  	"Recipient_State=" + lRegData.getRenwlMailAddr().getState() + "#" +
			  	"Recipient_ZipCode/first5=" + lRegData.getRenwlMailAddr().getZpcd() + "#" +
			  	"Recipient_ZipCode/last4=" + lRegData.getRenwlMailAddr().getZpcdp4());
			   	*/
		}

	}
	/**
	 * Is Valid User
	 *
	 * @return boolean 
	 * @param Object aaData
	 */
	private boolean isValidUser(Object aaData)
	{

		String lsFromAuth = (String) aaData;

		if (lsFromAuth == null)
			return false;

		if (lsFromAuth.indexOf(csUserName + csPassword) == 0)
			return true;
		else
			return false;
	}
}
