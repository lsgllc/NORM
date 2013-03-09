package com.txdot.isd.rts.server.webapps.addresschange;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.services.webapps.util.constants.AddressChangeConstants;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.MessageConstants;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.general.CacheManagerServerBusiness;
import com.txdot.isd.rts.server.webapps.common.business.*;

/*
 * AddrChgServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/08/2005	Change import for Transaction
 *							modify import
 *							defect 7705 Ver 5.2.3
 * Jeff S.		02/23/2005	Get code to standard. Changed a non-static
 * 							call to a static call.
 * 							modify purgeAddrChg()
 *							defect 7888 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7888 Ver 5.2.3
 * K Harrell	08/31/2005	remove liResult & reference to 
 * 							Transaction.Failure as it is never used 
 * 							after assignment 
 * 							modify doAddressChange() 
 * 							defect 8353 Ver 5.2.3
 * K Harrell	10/07/2005	Refactoring work
 * 							delete purgeAddrChg()
 * 							defect 7889 Ver 5.2.3   
 * K Harrell	06/17/2010	add processing for RecpntEMail, 
 * 							 EMailRenwlReqCd
 * 							modify doAddressChange()
 * 							defect 10508 Ver 6.5.0
 * K Harrell	06/22/2010	add retry for IADDR/IRNR transactions
 * 							add INT_MAX_RETRY, MSG_RETRY,
 * 							  MSG_RTSEX_ON_TRANS_CREATE,
 * 							  MSG_SLEEP_INTERRUPTED 
 * 							modify doAddressChange()
 * 							defect 10466 Ver 6.5.0
 * K Harrell	08/04/2010	throw exception after Max Retry
 * 							add MSG_RETRY_EXCEEDED
 * 							modify doAddressChange() 
 * 							defect 1466 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */
/**
 * This frame displays the address info.
 *
 * @version	6.5.0 			08/04/2010
 * @author	James Giroux
 * <br>Creation Date:		08/08/2001 12:31:10
 */
public class AddrChgServerBusiness
{
	// defect 10466 
	private final static int INT_MAX_RETRY = 5;
	private static final String MSG_RETRY =
		"Retrying IADDR/IRNR transaction request ";
	private static final String MSG_RETRY_EXCEEDED =
		"Maximum Retry for IADDR/IRNR transaction exceeded";
	private static final String MSG_RTSEX_ON_TRANS_CREATE =
		"RTSException processing IADDR/IRNR trans request";
	private static final String MSG_SLEEP_INTERRUPTED =
		"Sleep interrupted ";
	// end defect 10466  

	/**
	 * AddrChgServerBusiness constructor comment.
	 */
	public AddrChgServerBusiness()
	{
		super();
	}
	/**
	 * This method returns a String or a CompleteAddrChgData object.
	 * If a String is returned, some kind of problem occured.
	 * The problem could be an exception error, vehicle not found 
	 * vehicle not eligible etc.
	 *
	 * @param aaVehBaseData VehicleBaseData
	 * @return Object
	 * @throws RTSException
	 */
	private Object confirmVehicle(VehicleBaseData aaVehBaseData)
		throws RTSException
	{
		Object laObj = null;
		try
		{
			SearchVehicle laSearchVeh =
				new SearchVehicle(
					aaVehBaseData,
					CommonConstants.ADDRESS_CHANGE);
			laObj = laSearchVeh.getVehicle();
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		if (laObj == null)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				new NullPointerException(
					"Retrieved object from "
						+ "SearchVehicle is null !!!"));
		}
		if (laObj instanceof String)
		{
			// An error has occurred. Return the String (as Object).
			return laObj;
		}
		if (!(laObj instanceof VehicleInquiryData))
		{
			return MessageConstants.MSG_SYSTEM_ERROR;
		}

		// Vehicle was successfully found.
		VehicleInquiryData laVehInquiryData =
			(VehicleInquiryData) laObj;

		// Check for eligibility
		CommonEligibility laCommElig =
			new CommonEligibility(laVehInquiryData);

		// Moved check for DocTypeCd 10 to isEligibleForAddressChange
		if (!laCommElig.isEligibleForAddressChange())
		{
			return MessageConstants.MSG_NOT_ELIGIBLE_FOR_ADDRESS_CHANGE;
		}

		// Vehicle meets eligibility.
		// Set VehicleBaseData
		aaVehBaseData =
			VehicleBase.setVehicleBaseData(laVehInquiryData);
		// Set VehicleDescData
		VehicleDescData laVehDescData =
			VehicleDesc.setVehicleDescData(laVehInquiryData);
		// Set VehicleUserData (It will become the OldVehicleUserData)
		VehicleUserData laOldVehUserData =
			VehicleUser.setVehUserData(laVehInquiryData);
		// Set CompleteAddrChgData		
		CompleteAddrChgData laCompleteAddrChgData =
			new CompleteAddrChgData();
		laCompleteAddrChgData.setVehBaseData(aaVehBaseData);
		laCompleteAddrChgData.setVehDescData(laVehDescData);
		laCompleteAddrChgData.setOldVehUserData(laOldVehUserData);
		return laCompleteAddrChgData;
	}

	/**
	 * Address Change
	 * 
	 * @param aaCompAddrChgData CompleteAddrChgData
	 * @return Object
	 * @throws RTSException
	 */
	private Object doAddressChange(CompleteAddrChgData aaCompAddrChgData)
		throws RTSException
	{
		Object laObj = null;
		try
		{
			// change VIN to last four characters of VIN
			VehicleBaseData laVehBaseData =
				aaCompAddrChgData.getVehBaseData();
			String lsVIN = laVehBaseData.getVin();
			if (lsVIN.length() > 4)
			{
				lsVIN =
					lsVIN.substring(lsVIN.length() - 4, lsVIN.length());
			}
			laVehBaseData.setVin(lsVIN);

			// Query the mainframe to get the complete data
			SearchVehicle laSearchVeh =
				new SearchVehicle(
					laVehBaseData,
					CommonConstants.ADDRESS_CHANGE);
			laObj = laSearchVeh.getVehicle();
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		if (laObj == null)
		{
			throw new RTSException(
				RTSException.JAVA_ERROR,
				new NullPointerException(
					"Retrieved object from "
						+ "SearchVehicle is null !!!"));
		}

		if (!(laObj instanceof VehicleInquiryData))
		{
			return MessageConstants.MSG_SYSTEM_ERROR;
		}

		// Vehicle was successfully found.
		VehicleInquiryData laVehInqData = (VehicleInquiryData) laObj;
		MFVehicleData laMFVehDataOriginal =
			laVehInqData.getMfVehicleData();
		MFVehicleData laMFVehDataModified =
			(MFVehicleData) UtilityMethods.copy(laMFVehDataOriginal);
		RegistrationData laRegData = laMFVehDataModified.getRegData();
		// Get the VehicleUserData from the CompleteAddrChgData...
		VehicleUserData laVehUserData =
			aaCompAddrChgData.getVehUserData();
		// ...and put the AddressData and Recipient Name and CountyNo 
		// into RegistrationData...

		// defect 10508 
		laRegData.setRecpntEMail(laVehUserData.getEmail());
		laRegData.setEMailRenwlReqCd(
			laVehUserData.getEMailRenwlReqCd());
		// end defect 10508

		laRegData.setRenwlMailAddr(laVehUserData.getAddress());
		laRegData.setRecpntName(laVehUserData.getRecipientName());
		laRegData.setResComptCntyNo(
			aaCompAddrChgData.getVehBaseData().getOwnerCounty());
		// ...then put RegistrationData into MFVehicleData
		laMFVehDataModified.setRegData(laRegData);
		// Build CompleteTransactionData object
		CompleteTransactionData laCompTransData =
			new CompleteTransactionData();
		// set Modified data
		laCompTransData.setVehicleInfo(laMFVehDataModified);
		// set Original data
		laCompTransData.setOrgVehicleInfo(laMFVehDataOriginal);

		// Determine if we need to generate a new Renewal Notice.
		// If so, use IRNR as the transCode. Otherwise use IADDR
		String lsTransCode = TransCdConstant.IADDR;
		CommonEligibility laCommonElig =
			new CommonEligibility(laVehInqData);

		if (laCommonElig.isEligibleForNewRenewalNotice())
		{
			lsTransCode = TransCdConstant.IRNR;
		}

		// Add the transaction and return the result

		// defect 10466  
		// Add Retry for IADDR/IRNR 
		for (int i = 0; i < INT_MAX_RETRY; i++)
		{
			try
			{
				CacheManagerServerBusiness.setCacheServer(true);
				laCompTransData.setTransCode(lsTransCode);
				CommonServerBusiness laCSB = new CommonServerBusiness();
				laCSB.processData(
					GeneralConstant.COMMON,
					CommonConstant.PROC_INTERNET_ADDR_CHNG,
					laCompTransData);
				break;
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					MSG_RTSEX_ON_TRANS_CREATE);

				// Check to see if this is a duplicate key.
				if (aeRTSEx.getDetailMsg() != null
					&& aeRTSEx.getDetailMsg().indexOf(
						CommonConstant.DUPLICATE_KEY_EXCEPTION)
						> -1)
				{
					String lsVin =
						laMFVehDataModified.getVehicleData().getVin();

					String lsRegPltNo = laRegData.getRegPltNo();

					if (i < INT_MAX_RETRY - 1)
					{

						Log.write(
							Log.SQL_EXCP,
							this,
							MSG_RETRY
								+ i
								+ " "
								+ lsTransCode
								+ " VIN: "
								+ lsVin
								+ " PlateNo: "
								+ lsRegPltNo);
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException aeIEx)
						{
							Log.write(
								Log.SQL_EXCP,
								this,
								MSG_SLEEP_INTERRUPTED + i);
						}
					}
					else
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							MSG_RETRY_EXCEEDED
								+ " "
								+ " VIN: "
								+ lsVin
								+ " PlateNo: "
								+ lsRegPltNo);

						throw aeRTSEx;
					}
				}
				else
				{
					// Some other exception.  Just throw it.
					throw aeRTSEx;
				}
			}
			// end defect 10466 
		}
		return (Object) MessageConstants.MSG_SUCCESSFUL_ADDRESS_CHANGE;
	}
	/**
	 * Used to Test Class.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		//		CompleteAddrChgData lCACD = new CompleteAddrChgData();
		//		VehicleBaseData lVBD = new VehicleBaseData();
		//		//lVBD.setPlateNo("XLH89S");
		//		lVBD.setPlateNo("L70GHW");
		//		//	lCACD.setVehBaseData(lVBD);
		//		AddrChgServerBusiness lACSB = new AddrChgServerBusiness();
		//		try {
		//			Object obj = lACSB.processData(0, 
		//							AddressChangeConstants.GET_ADDR_CHG_VEHICLE,
		//							 (Object)lVBD);
		//			if (obj != null) {
		//				if (obj instanceof String) {
		//					System.out.println("It is a string - value = " 
		//											+ (String)obj);
		//				} else {
		//					lCACD = (CompleteAddrChgData)obj;
		//					VehicleDescData lVDD = lCACD.getVehDescData();
		//					System.out.println(lVDD.getMake());
		//				}
		//		
		//			}
		//		}
		//		catch (Exception e) {}
		com.txdot.isd.rts.services.communication.Comm.setIsServer(true);
		//	AddrChgServerBusiness laAddrChg = new AddrChgServerBusiness();
		//	laAddrChg.deleteAddrChg();
	}
	/**
	 * Used to process the data.
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	public Object processData(
		int aiModuleName,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		try
		{
			switch (aiFunctionId)
			{
				case AddressChangeConstants.GET_ADDR_CHG_VEHICLE :
					{
						return confirmVehicle((VehicleBaseData) aaData);
					}
				case AddressChangeConstants.UPDATE_ADDRESS :
					{
						return doAddressChange(
							(CompleteAddrChgData) aaData);
					}
				default :
					{
						throw new RTSException(
							RTSException.JAVA_ERROR,
							new Exception(
								"AddrChgServerBusiness - No such "
									+ "functionId:"
									+ aiFunctionId));
					}
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (Exception aeRTSEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeRTSEx);
		}
	}
	/**
	 * Used to purge Address change transactions.
	 * 
	 * @param aiDaysOld
	 * @return boolean
	 * @deprecated 
	 */
	//	public boolean purgeAddrChg(int aiDaysOld)
	//	{
	//		DatabaseAccess laDBAccess = new DatabaseAccess();
	//		boolean lbResult = false;
	//		try
	//		{
	//			// defect 7889
	//			// methods moved from InternetAddressChange 
	//			InternetTransaction laItrntTrans = 
	//			new InternetTransaction(laDBAccess); 
	//			laDBAccess.beginTransaction();
	//
	//			laItrntTrans.purgeIAddrIRnrTrans(aiDaysOld);
	//			// end defect 7889 
	//
	//			lbResult = true;
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			BatchLog.error(aeRTSEx);
	//		}
	//		finally
	//		{
	//			try
	//			{
	//				if (lbResult == true)
	//				{
	//					laDBAccess.endTransaction(DatabaseAccess.COMMIT);
	//				}
	//				else
	//				{
	//					laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
	//				}
	//			}
	//			catch (RTSException aeRTSEx)
	//			{
	//				lbResult = false;
	//			}
	//		}
	//		return lbResult;
	//	}
}
