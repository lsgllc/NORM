package com.txdot.isd.rts.server.webapps.order.countyinfo.business;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.OfficeIds;
import com.txdot.isd.rts.server.db.Substation;
import com.txdot.isd.rts.server.webapps.order.common.business.AbstractBusiness;
import com.txdot.isd.rts.server.webapps.order.common.data.Address;
import com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoRequest;
import com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoResponse;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.data.SubstationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.util.constants.ServiceConstants;

/*
 * CountyInfoBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/05/2007	Created Class.
 * 							defect 9121 Ver Special Plates
 * Jeff S.		06/26/2007	If Physical Address is empty use the mailing
 * 							address for the physical address.
 * 							defect 9121 Ver Special Plates
 * Bob B.		02/05/2008	Lookup the county info from the offide ids
 * 							table, not server cache.
 * 							deprecate getAllCountyInfo
 * 								(CountyInforequest)
 * 							add getAllCountyInfo(), getOfcIdCache(),
 * 								stdOut()
 * 							modify processData()
 * 							defect 9549 Ver Tres Amigos Prep
 * Ray Rowehl	05/29/2008	Modify menthod call.
 * 							modify getOfcIdCache()
 * 							defect 9677 Ver MyPlates_POS   
 * ---------------------------------------------------------------------
 */

/**
 * Business class used to handle the County Info Requests.
 *
 * @version	MyPlates_POS 	05/29/2008
 * @author	Jeff Seifert
 * <br>Creation Date:		03/06/2007 13:40:00
 */
public class CountyInfoBusiness extends AbstractBusiness
{

	/**
	 * Gets the mailing and physical address for all 254 counties as
	 * well al the info for the special plates division.
	 * 
	 * @param aaCIReq CountyInfoRequest
	 * @return CountyInfoResponse[]
	 * @deprecated
	 */
	private CountyInfoResponse[] getAllCountyInfo(CountyInfoRequest aaCIReq)
	{
		// Get RTS_OFFICE_IDS Cache OfficeIdsData
		Vector lvOfficeIds =
			(Vector) UtilityMethods.copy(OfficeIdsCache.getOfcIds());
		UtilityMethods.sort(lvOfficeIds);
		// 254 counties and then add the special plates division 
		// to make 255.
		CountyInfoResponse[] larrCIResponse =
			new CountyInfoResponse[255];
		// Loop through 254 times and then we will add SP division on 
		// the end.
		for (int i = 0; i < 254; i++)
		{
				OfficeIdsData laOffice =
					(OfficeIdsData) lvOfficeIds.get(i);
				larrCIResponse[i] = new CountyInfoResponse();
				larrCIResponse[i].setCountyNo(
					UtilityMethods.addPadding(
						String.valueOf(laOffice.getOfcIssuanceNo()),
						3,
						CommonConstant.STR_ZERO));
				larrCIResponse[i].setCountyName(laOffice.getOfcName());
				larrCIResponse[i].setTacName(laOffice.getTacName());
				larrCIResponse[i].setEmailAddress(
					laOffice.getEMailAddr());
				larrCIResponse[i].setPhone(laOffice.getTacPhoneNo());
				
				// Mailing Address
				Address laMailAddress = new Address();
				laMailAddress.setStreet1(laOffice.getOfcSt());
				laMailAddress.setCity(laOffice.getOfcCity());
				laMailAddress.setState(CommonConstant.STR_TX);
				laMailAddress.setZipCd(
					String.valueOf(laOffice.getOfcZpCd()));
				laMailAddress.setZipCd4(laOffice.getOfcZpCdP4());
				larrCIResponse[i].setMailingAddress(laMailAddress);
				// End Mailing Address
				
				// Physical Address
				Address laPhyAddress = new Address();
				if (laOffice.getPhysOfcLoc() != null 
					&& laOffice.getPhysOfcLoc().trim().length() > 0)
				{
					laPhyAddress.setStreet1(laOffice.getPhysOfcLoc());
				}
				else
				{
					laPhyAddress.setStreet1(laOffice.getOfcSt());
				}
				laPhyAddress.setCity(laOffice.getOfcCity());
				laPhyAddress.setState(CommonConstant.STR_TX);
				laPhyAddress.setZipCd(
					String.valueOf(laOffice.getOfcZpCd()));
				larrCIResponse[i].setPhysicalAddress(laPhyAddress);
				// End Physical Address
		}
		
		// Add the Special Plates Division to the end.
		OfficeIdsData laOffice = OfficeIdsCache.getOfcId(291);
		larrCIResponse[254] = new CountyInfoResponse();
		larrCIResponse[254].setCountyName(laOffice.getOfcName());
		larrCIResponse[254].setEmailAddress(
			laOffice.getEMailAddr());
		larrCIResponse[254].setPhone(laOffice.getTacPhoneNo());
		// Mailing Address
		Address laMailAddress = new Address();
		laMailAddress.setStreet1(laOffice.getOfcSt());
		laMailAddress.setCity(laOffice.getOfcCity());
		laMailAddress.setState(CommonConstant.STR_TX);
		laMailAddress.setZipCd(
			String.valueOf(laOffice.getOfcZpCd()));
		laMailAddress.setZipCd4(laOffice.getOfcZpCdP4());
		larrCIResponse[254].setMailingAddress(laMailAddress);
		// End Mailing Address
		
		// Physical Address
		Address laPhyAddress = new Address();
		laPhyAddress.setStreet1(laOffice.getPhysOfcLoc());
		laPhyAddress.setCity(laOffice.getOfcCity());
		laPhyAddress.setState(CommonConstant.STR_TX);
		laPhyAddress.setZipCd(
			String.valueOf(laOffice.getOfcZpCd()));
		larrCIResponse[254].setPhysicalAddress(laPhyAddress);
		// End Physical Address

		return larrCIResponse;
	}
	
	/**
	 * Gets the mailing and physical address for all 254 counties as
	 * well as all the info for the special plates division.
	 * 
	 * @return CountyInfoResponse[]
	 */
	
	private CountyInfoResponse[] getAllCountyInfo()
	{
		// Get RTS_OFFICE_IDS Cache OfficeIdsData
		CountyInfoResponse[] larrCIResponse =
			new CountyInfoResponse[255];
		try
		{
			Vector lvOfficeIds = getOfcIdCache();
			UtilityMethods.sort(lvOfficeIds);
			
			// Loop through 254 times and then we will add SP division on 
			// the end.
			for (int i = 0; i < 254; i++)
			{
					OfficeIdsData laOffice =
						(OfficeIdsData) lvOfficeIds.get(i);
					larrCIResponse[i] = new CountyInfoResponse();
					larrCIResponse[i].setCountyNo(
						UtilityMethods.addPadding(
							String.valueOf(laOffice.getOfcIssuanceNo()),
							3,
							CommonConstant.STR_ZERO));
					larrCIResponse[i].setCountyName(laOffice.getOfcName());
					larrCIResponse[i].setTacName(laOffice.getTacName());
					larrCIResponse[i].setEmailAddress(
						laOffice.getEMailAddr());
					larrCIResponse[i].setPhone(laOffice.getTacPhoneNo());
				
					// Mailing Address
					Address laMailAddress = new Address();
					laMailAddress.setStreet1(laOffice.getOfcSt());
					laMailAddress.setCity(laOffice.getOfcCity());
					laMailAddress.setState(CommonConstant.STR_TX);
					laMailAddress.setZipCd(
						String.valueOf(laOffice.getOfcZpCd()));
					laMailAddress.setZipCd4(laOffice.getOfcZpCdP4());
					larrCIResponse[i].setMailingAddress(laMailAddress);
					// End Mailing Address
				
					// Physical Address
					Address laPhyAddress = new Address();
					if (laOffice.getPhysOfcLoc() != null 
						&& laOffice.getPhysOfcLoc().trim().length() > 0)
					{
						laPhyAddress.setStreet1(laOffice.getPhysOfcLoc());
					}
					else
					{
						laPhyAddress.setStreet1(laOffice.getOfcSt());
					}
					laPhyAddress.setCity(laOffice.getOfcCity());
					laPhyAddress.setState(CommonConstant.STR_TX);
					laPhyAddress.setZipCd(
						String.valueOf(laOffice.getOfcZpCd()));
					larrCIResponse[i].setPhysicalAddress(laPhyAddress);
					// End Physical Address
			}
		
			// Add the Special Plates Division to the end.
			OfficeIdsData laOffice = OfficeIdsCache.getOfcId(291);
			larrCIResponse[254] = new CountyInfoResponse();
			larrCIResponse[254].setCountyName(laOffice.getOfcName());
			larrCIResponse[254].setEmailAddress(
				laOffice.getEMailAddr());
			larrCIResponse[254].setPhone(laOffice.getTacPhoneNo());
			// Mailing Address
			Address laMailAddress = new Address();
			laMailAddress.setStreet1(laOffice.getOfcSt());
			laMailAddress.setCity(laOffice.getOfcCity());
			laMailAddress.setState(CommonConstant.STR_TX);
			laMailAddress.setZipCd(
				String.valueOf(laOffice.getOfcZpCd()));
			laMailAddress.setZipCd4(laOffice.getOfcZpCdP4());
			larrCIResponse[254].setMailingAddress(laMailAddress);
			// End Mailing Address
		
			// Physical Address
			Address laPhyAddress = new Address();
			laPhyAddress.setStreet1(laOffice.getPhysOfcLoc());
			laPhyAddress.setCity(laOffice.getOfcCity());
			laPhyAddress.setState(CommonConstant.STR_TX);
			laPhyAddress.setZipCd(
				String.valueOf(laOffice.getOfcZpCd()));
			larrCIResponse[254].setPhysicalAddress(laPhyAddress);
			// End Physical Address
		}
		
		catch (RTSException leRTSEx)
		{
			leRTSEx.printStackTrace();
		}
		return larrCIResponse;
	}
	
	/**
	 * Fetches the Office Id data from database.
	 *
	 * @return Object
	 * @throws RTSException
	 */
	
	private Vector getOfcIdCache() throws RTSException
	{
		stdOut("Starting DB call to OfficeIds for IAPPL");
		DatabaseAccess laDBA = new DatabaseAccess();
		Vector lvOfficeIdsData = null;
		try
		{
			laDBA.beginTransaction();
			OfficeIds laOfficeIds = new OfficeIds(laDBA);
			//Get a vector of Data object
			// defect 9677
			lvOfficeIdsData = laOfficeIds.qryOfficeIds(-1);
			// end defect 9677
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			stdOut("Successful DB call to OfficeIds for IAPPL");
		}
		catch (RTSException aeRTSException)
		{
			stdOut("Failed DB call to OfficeIds for IAPPL");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
		
		return (lvOfficeIdsData);

	}
	
	/**
	 * This method writes a standard message to the std out file
	 * at TxDOT.
	 * 
	 * @param asMessage String
	 */
	private static void stdOut(String asMessage)
	{
		System.out.println(
			CommonConstant.STR_OPEN_BRACKET
				+ new java.util.Date()
				+ CommonConstant.STR_CLOSE_BRACKET
				+ CommonConstant.STR_SPACE_ONE
				+ asMessage);
	}
	/**
	 * Inherited method used to handle all of the functions within this
	 * module.
	 */
	public Object processData(Object aaObject)
	{
		CountyInfoRequest laCIReq = (CountyInfoRequest) aaObject;

		switch (laCIReq.getAction())
		{
			case ServiceConstants.CI_ACTION_GET_ALL_CNTY_INFO :
			{
				// defect 9549
				// return getAllCountyInfo(laCIReq);
				return getAllCountyInfo();
				// end defect 9549
			}
			default :
				return null;
		}
	}
}
