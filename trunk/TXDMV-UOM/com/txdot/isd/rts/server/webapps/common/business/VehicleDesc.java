package com.txdot.isd.rts.server.webapps.common.business;

import com.txdot.isd.rts.services.data.*;

/*
 * VehicleDesc.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 * Min Wang		10/11/2006	New Requirement for handling plate age 
 * 							modify setVehicleDescData() 
 *							defect 8901 Ver Exempts
 * K Harrell	07/03/2009	Implement new OwnerData
 * 							delete getVehMkDesc() 
 * 							modify setVehicleDescData() 
 * 							defect 10112 Ver Defect_POS_F
 * S Carlin		09/06/2011	Set major and minor color 
 * 							defect 10985 Ver 6.8.1 
 *----------------------------------------------------------------------
 */

/**
 * Vehicle Description
 *  
 * @version	Defect_POS_F	07/03/2009
 * @author	Administrator
 * <br>Creation Date:		10/08/2001 18:14:47
 */
public class VehicleDesc
{
	/**
	 * VehicleDesc constructor comment.
	 */
	public VehicleDesc()
	{
		super();
	}

	//	/**
	//	 * Get Vehicle Make Desc
	//	 * 
	//	 * @return String
	//	 * @param String asVehMk
	//	 */
	//	private static String getVehMkDesc(String asVehMk)
	//	{
	//
	//		String lsVehMkDesc = asVehMk;
	//		// ***************************
	//		/* Smarter translation, need VTR to say yes. 
	//		try
	//		{
	//			// Get the Make Description from cache
	//			VehicleMakesCache VMC = new VehicleMakesCache();
	//			Vector lvVehModelDesc = VMC.getVehMksVec();	
	//		
	//			int liMatchCount=0;
	//			for (int i = 0; i < lvVehModelDesc.size(); i++)
	//			{
	//				VehicleMakesData lVehMakesData = (VehicleMakesData) lvVehModelDesc.elementAt(i);
	//					if (asVehMk.equals(lVehMakesData.getVehMk()))
	//				{
	//					 // set it to the found description.
	//					 lsVehMkDesc=lVehMakesData.getVehMkDesc();				 
	//					 liMatchCount++;				 
	//				}
	//			}
	//			if(liMatchCount>1)
	//				// duplicate found, no translation to desc.
	//				lsVehMkDesc=asVehMk;
	//		}
	//		catch (Exception e){
	//			// no need to do anything
	//		}
	//		*/
	//		return lsVehMkDesc;
	//	}

	/**
	 * Get Vehicle Desc
	 * 
	 * @return Object VehicleDescData
	 * @param VehicleInquiryData aaVehicleInquiryData
	 */
	public static VehicleDescData setVehicleDescData(VehicleInquiryData aaVehicleInquiryData)
	{

		// Get various data objects from VehicleInquiryData object.
		MFVehicleData laMFVehicleData =
			aaVehicleInquiryData.getMfVehicleData();
		RegistrationData laRegistrationData =
			laMFVehicleData.getRegData();
		VehicleData laVehicleData = laMFVehicleData.getVehicleData();
		TitleData laTitleData = laMFVehicleData.getTitleData();

		// Set VehicleDescData from the various objects
		VehicleDescData laVehicleDescData = new VehicleDescData();
		laVehicleDescData.setModelYr(
			String.valueOf(laVehicleData.getVehModlYr()));
		laVehicleDescData.setEmptyWeight(
			String.valueOf(laVehicleData.getVehEmptyWt()));
		laVehicleDescData.setTonnage(
			String.valueOf(laVehicleData.getVehTon()));

		// defect 10112  
		// getVehMkDesc() just returned the parameter passed 
		//String lsVehMkDesc = getVehMkDesc(laVehicleData.getVehMk());
		//laVehicleDescData.setMake(lsVehMkDesc);
		laVehicleDescData.setMake(laVehicleData.getVehMk());
		// end defect 10112 

		// This is what Phase1 is showing.
		//lVehicleDescData.setMake(lVehicleData.getVehMk()+" "+lVehicleData.getVehBdyType());  

		laVehicleDescData.setExpMo(
			String.valueOf(laRegistrationData.getRegExpMo()));
		laVehicleDescData.setExpYr(
			String.valueOf(laRegistrationData.getRegExpYr()));
		// defect 8901
		//laVehicleDescData.setPlateAge(
		//	String.valueOf(laRegistrationData.getRegPltAge()));
		laVehicleDescData.setPlateAge(
			String.valueOf(laRegistrationData.getRegPltAge(true)));
		// end defect 8901

		// defect 10112 
		laVehicleDescData.setOwnerName(
			String.valueOf(laMFVehicleData.getOwnerData().getName1()));
		// end defect 10112 

		laVehicleDescData.setGrossWeight(
			String.valueOf(laRegistrationData.getVehGrossWt()));
		laVehicleDescData.setTitleIssDt(
			String.valueOf(laTitleData.getTtlIssueDate()));

		//defect 10985
		laVehicleDescData.setMajorColorCd(laVehicleData.getVehMjrColorCd());
		laVehicleDescData.setMinorColorCd(laVehicleData.getVehMnrColorCd());
		//end defect 10985
		
		return laVehicleDescData;
	}
}
