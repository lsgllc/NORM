package com.txdot.isd.rts.server.general;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;

/*
 * CacheManagerServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/05/2001	getAccountCodesCache, getStaticCache added
 * Ray Rowhl	03/05/2003	add timestamp in front of stack trace
 *							defect 5263 
 * K Harrell    08/04/2003	TERP; 
 *                          added getTitleTERPFeeCache
 *                          added getTitleTERPPercentCache
 *                          altered loadStaticCache
 *                          altered processData
 *							defect 6447
 * Ray Rowehl	04/22/2004	Change so Security Cache only gets current
 *							employees.  previously, we included
 *							deleted emplyees as well.
 *							modify getSecurityCache()
 *							defect 7017 Ver 5.1.6
 * Ray Rowehl	07/19/2004	Add handling for RSPS
 *							add getRSPSSysUpdtCache(),
 *								getRSPSWsStatusCache()
 *							modify processData()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/07/2004	Correct implementation of CacheVersionNo
 *							for handling of SysUpdt.
 *							add CACHEVERSION_1
 *							modify loadStaticCache()
 *							defect 7135 Ver 5.2.1
 * K Harrell	11/24/2004	Do not throw exception when subconid not
 *							found. Instead return null SubcontractorData
 *							object.
 *							modify getSubconData() 
 *							defect 7729 Ver 5.2.2
 * K Harrell	12/29/2004	Enable search for RSPSWsStatus with
 *							additional parameters
 *							Formatting/JavaDoc/Variable Name cleanup
 *							deprecate getMiscData()
 *							modify getRSPSWsStatusCache() 
 *							defect 7810 Ver 5.2.2
 * K Harrell	01/17/2005	remove deprecation from getMiscData()
 *							Ver 5.2.3
 * K Harrell	03/28/2005	Remove reference to CACHEVERSION_1. Not
 *							required.
 *							delete CACHEVERSION_1
 *							modify loadStaticCache()
 *							defect 7847 Ver 5.2.2 Fix 4
 * K Harrell	04/04/2005	REG_ADDL_FEE processing
 *							add getRegistrationAdditionalFeeCache()
 *							modify loadStaticCache(),processData()
 *							defect 8104 Ver 5.2.2 Fix 4
 * K Harrell	04/04/2005 	Java 1.4 Work 
 * 							delete reference to CACHEVERSION_1
 * 							defect 7847 Ver 5.2.3
 * K Harrell	05/12/2005	Removed parameter calls for loading static
 * 							cache tables and modified calls to those
 * 							methods.  Added rollback for DB exception
 * 							handling. Removed the finally blocks and 
 * 							moved endtransaction after SQL call.
 *							modify all methods EXCEPT main(),
 *								setCacheServer(),isCacheServer(),
 * 							rename getProduceServiceCache()to
 * 							 getProductServiceCache()
 * 							rename getCommVehWt()to getCommVehWtCache()
 * 							defect 7864 Ver 5.2.3
 * K Harrell	06/13/2005	ClassToPlate,ClassToPlateDescription,
 * 							PlateToSticker processing
 * 							add getClassToPlateCache(),
 * 							getClassToPlateDescriptionCache()
 * 							getPlateToStickerCache()
 * 							deprecate getRegistrationPlateStickerCache(),
 * 							getRegistrationPlateStickerDescCache()   
 *							modify loadStaticCache(),processData()
 *							defect 8218 Ver 5.2.3
 * K Harrell	07/06/2005	Process request to get LienholderData and 
 * 							deprecate getMiscData(),getSubconData() 
 * 							getAllSubconData()
 * 							modify processData(),getDealerCache(),
 * 							getLienholderCache(),getSubcontractorCache() 
 * 							defect 8283 Ver 5.2.3
 * K Harrell	07/11/2005	Take no action on RSPSSysUpdateCache
 * 							prepare for deletion from RTS_STATIC_CACHE
 * 							deprecate getRSPSSysUpdtCache()
 * 							modify loadStaticCache(),processData() 
 * 							defect 8281 Ver 5.2.3
 * K Harrell	08/16/2005 	Code Review 5.2.2 Fix n => 5.2.3
 * 							comment update for defect 7847  
 * 							Ver 5.2.3
 * K Harrell	03/17/2006	Take no action on CashWorkstationIdsCache
 * 							prepare for deletion from RTS_ADMIN_CACHE
 * 							deprecate getCashWsIdCache()
 * 							modify loadAdminCache(),processData() 
 * 							defect 8623 Ver 5.2.3  
 * Ray Rowehl	07/12/2006	Retry on StaleConnection when processing
 * 							getStaticCache() 
 * 							add csClientHost
 * 							add CacheManagerServerBusiness(String)
 * 							modify getStaticCache()
 * 							defect 8849 Ver 5.2.3 
 * Ray Rowehl	08/04/2006	Log the StaleConnection.
 * 							modify getStaticCache()
 * 							defect 8869 Ver 5.2.4  
 * K Harrell	02/11/2007	New cache tables 
 * 							add getPltTypeCache(),
 * 							    getOrgNoCache(),
 * 							 	getPltGrpIdCache(),
 * 								getPltSurchargeCache(),
 * 								getVehClassSpclPltTypeDescCache()
 * 							delete deprecated methods: 
 * 								getRegistrationPlateStickerCache(),
 * 								getRegistrationPlateStickerDescCache(),
 * 								getRSPSSysUpdtCache(),getCashWsIdCache(),
 * 								getSubconData(), getAllSubconData(),
 * 								getMiscData()
 * 							modify loadStaticCache(), processData() 							
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/05/2007	New cache table: TxDOTHoliday
 * 							add getTxDOTHolidayCache()
 * 							modify loadStaticCache(), processData()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	03/26/2007	New cache table: SpecialPlateFixedExpirationMonth
 * 							add getSpecialPlateFixedExpirationMonth()
 * 							modify loadStaticCache(), processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/27/2007	New cache table:  FundsCodes
 * 							add getFundsCodesCache()
 * 							modify loadStaticCache(), processData()
 * 							defect 6949 Ver Defect POS A   
 * K Harrell	04/02/2008	New cache tables: TitleTransferEntity,
 * 							 TitleTransferPenaltyExemptCodeData, 
 * 							 TitleTransferPenaltyFee 
 * 							add getTtlTrnsfrEntCache(),
 * 							 getTtlTrnsfrPnltyExemptCdCache(), 
 * 							 getTtlTrnsfrPnltyFeeCache()
 * 							modify loadStaticCache(), processData()
 * 							defect 9583 Ver Defect POS A
 * K Harrell	05/21/2008 	Remove references to
 * 							 TitleTransferPenaltyExemptCodeData
 * 							delete getTtlTrnsfrPnltyExemptCdCache()
 * 							modify loadStaticCache(), processData()
 * 							defect 9583 Ver Defect POS A
 * Ray Rowehl	05/29/2008	Change method call.
 * 							modify getOfcIdCache()
 * 							defect 9677 Ver MyPlates_POS 
 * K Harrell	06/22/2008	Restore references to     
 * 							 TitleTransferPenaltyExemptCodeData
 * 							add getTtlTrnsfrPnltyExemptCdCache()
 * 							modify loadStaticCache(), processData()
 * 							defect 9724 Ver Defect POS A
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	07/10/2008	Add cache function for WSServiceActionVersion.
 * 							add getWebServicesServiceActionVersionCache()
 * 							modify loadStaticCache()
 * 							defect 9675 Ver MyPlates_POS
 * K Harrell	10/21/2008	Add cache function for Disabled Placard.
 * 							add getDsabldPlcrdCustIdTypeCache(), 
 * 							  getDsabldPlcrdDelReasnCache()
 * 							modify loadStaticCache(), processData() 
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	02/26/2009	Add cache function for CertfdLienhldr 
 * 							add getCertifiedLienhldrCache() 
 * 							modify loadStaticCache(), processData()
 * 							defect 9969 Ver Defect_POS_E 
 * K Harrell	06/09/2009	No longer have to convert from GSD.
 * 							modify getDealerCache(),
 * 							getLienholderCache(),getSubcontractorCache()
 * 							defect 10003 Ver Defect_POS_F
 * K Harrell	07/03/2009	Implement new DealerData, LienholderData 
 * 							modify getDealersCache(), 
 * 							 getLienholdersCache() 
 * 							defect 10012 Ver Defect_POS_F
 * K Harrell	03/24/2010	Add cache function for PlateSymbol
 * 							add getPlateSymbolCache()
 * 							modify loadStaticCache(), processData()
 * 							defect 10366 Ver POS_640
 * K Harrell	03/24/2010	Add cache function for PostalState
 * 							add getPostalStateCache()
 * 							modify loadStaticCache(), processData()
 * 							defect 10396 Ver POS_640 
 * K Harrell	04/03/2010	Add cache function for OfficeTimeZone 
 * 							add getOfficeTimeZoneCache()
 * 							modify loadStaticCache(), processData(), 
 * 							getOfficeTimeZone() 
 * 							defect 10427 Ver POS_640  
 * K Harrell	12/09/2010	add getRegistrationClassFeeGroupCache() 
 * 							modify loadStaticCache(),processData()
 * 							defect 10695 Ver 6.7.0    
 * K Harrell	12/29/2010  add getWebAgencyBatchStatusCache(), 
 * 							 getWebAgencyType()
 * 							modify loadStaticCache(),processData()  
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	01/05/2011	add getVehicleColorCache()
 * 							modify loadStaticCache(),processData()
 * 							defect 10712 Ver 6.7.0     
 * K Harrell	01/07/2011	add getBusinessPartnerCache()
 * 							modify loadStaticCache(),processData()
 * 							defect 10726 Ver 6.7.0    
 * K Harrell	01/16/2011	add getBatchReportManagementCache()
 * 							delete loadAdminCache() 
 * 							modify processData()
 * 							defect 10701 Ver 6.7.0  
 * K Harrell	10/04/2011	add getHolidayCache() 
 * 							delete getTxDOTHolidayCache()
 * 							modify processData(), loadStaticCache() 
 * 							defect 9919 Ver 6.9.0   
 * ---------------------------------------------------------------------
 */

/**
 * Cache Manager Server Business process requests sent from the servlet and
 * query the database for all static and admin cache data
 * 
 * @version 6.9.0 	10/04/2011
 * @author Nancy Ting
 * @since 			08/14/2001 14:46:25
 */
public class CacheManagerServerBusiness
{
	private static boolean cbCacheServer;

	// defect 8849
	private String csClientHost = "Unknown";
	// end defect 8849

	/**
	 * CacheManagerServerBusiness constructor comment.
	 */
	public CacheManagerServerBusiness()
	{
		super();
	}

	/**
	 * CacheManagerServerBusiness constructor comment.
	 * 
	 * @param String
	 *            asClientHost
	 */
	public CacheManagerServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
	}

	/**
	 * Fetches the Account Code cache from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getAccountCodesCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to AccountCodes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			AccountCodes laAccountCodes = new AccountCodes(laDBA);
			// Get a vector of AccountCodesData
			Vector lvAccountCodesData = laAccountCodes
					.qryAccountCodes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to AccountCodes");
			return lvAccountCodesData;
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to AccountCodes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			// defect 11320
			// need to see what the exception is
			aeRTSException.printStackTrace();
			// end defect 11320
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the AdminCacheTable data from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getAdmCacheTblCache(Object aaObject)
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to AdministrationCacheTable");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			AdministrationCacheTable laAdmCache = new AdministrationCacheTable(
					laDBA);
			// Get a vector of Data object
			Vector lvAdmCacheTblData = laAdmCache
					.qryAdministrationCacheTable((AdministrationCacheTableData) aaObject);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to AdministrationCacheTable");
			return (lvAdmCacheTblData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to AdministrationCacheTable");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the AssignedWorkstationId data from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getAssgndWsIdsCache(Object aaObject)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = (GeneralSearchData) aaObject;
		Log.write(Log.APPLICATION, this,
				"Starting DB call to AssignedWorkstationIds");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			AssignedWorkstationIds laAssignedWorkstationIds = new AssignedWorkstationIds(
					laDBA);
			AssignedWorkstationIdsData laAssignedWorkstationIdsData = new AssignedWorkstationIdsData();
			laAssignedWorkstationIdsData
					.setOfcIssuanceNo(laGeneralSearchData.getIntKey1());
			laAssignedWorkstationIdsData
					.setSubstaId(laGeneralSearchData.getIntKey2());
			laAssignedWorkstationIdsData
					.setChngTimestmp(laGeneralSearchData.getDate1());
			// Get a vector of Data object
			Vector lvAssignedWorkstationIdsData = laAssignedWorkstationIds
					.qryAssignedWorkstationIds(laAssignedWorkstationIdsData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to AssignedWorkstationIds");
			return (lvAssignedWorkstationIdsData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to AssignedWorkstationIds");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the BusinessPartner data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getBusinessPartnerCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to BusinessPartner");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			BusinessPartner laBusinessPartner = new BusinessPartner(
					laDBA);
			// Get a vector of Data object
			Vector lvBusinessPartnerData = laBusinessPartner
					.qryBsnPartner();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to BusinessPartner");
			return (lvBusinessPartnerData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to BusinessPartner");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the BatchReportManagement data from database.
	 * 
	 * @param aaObject
	 * @return Object
	 * @throws RTSException
	 */
	private Object getBatchReportManagementCache(Object aaObject)
			throws RTSException
	{

		BatchReportManagementData laBatchRptMgmtData = (BatchReportManagementData) aaObject;

		Log.write(Log.APPLICATION, this,
				"Starting DB call to BatchReportManagement");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();

			BatchReportManagement laBatchRptMgmtSQL = new BatchReportManagement(
					laDBA);

			// Get a vector of Data object
			Vector lvBatchRptMgmtData = laBatchRptMgmtSQL
					.qryBatchReportManagement(laBatchRptMgmtData);

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to BatchReportManagement");
			return (lvBatchRptMgmtData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to BatchReportManagement");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Certified Lienholder data
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	public Object getCertifiedLienhldrCache(
			CertifiedLienholderData aaData) throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to CertifiedLienholder");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			CertifiedLienholder laCertfdLienhldr = new CertifiedLienholder(
					laDBA);
			// Get a vector of Data object
			Vector lvCertfdLienhldr = laCertfdLienhldr
					.qryCertifiedLienholder(aaData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to CertifiedLienholder");
			return (lvCertfdLienhldr);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to CertifiedLienholder");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Class To Plate data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getClassToPlateCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to ClassToPlate");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			ClassToPlate laClassToPlate = new ClassToPlate(laDBA);
			// Get a vector of Data object
			Vector lvClassToPlateData = laClassToPlate
					.qryClassToPlate();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to ClassToPlate");
			return (lvClassToPlateData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to ClassToPlate");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Class To Plate data joined to Item Codes table from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getClassToPlateDescriptionCache()
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to ClassToPlateDescription");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			ClassToPlateDescription laClassToPlateDescription = new ClassToPlateDescription(
					laDBA);
			// Get a vector of Data object
			Vector lvClassToPlateData = laClassToPlateDescription
					.qryClassToPlateDescription();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to ClassToPlateDescription");
			return (lvClassToPlateData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to ClassToPlateDescription");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Common Fees data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getCommonFeesCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to CommonFees");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			CommonFees laCommonFees = new CommonFees(laDBA);
			// Get a vector of Data object
			Vector lvCommonFeesData = laCommonFees.qryCommonFees();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to CommonFees");
			return (lvCommonFeesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to CommonFees");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Commercial Vehicle Weight data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getCommVehWtCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to CommercialVehicleWeights");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			CommercialVehicleWeights laCommercialVehicleWeights = new CommercialVehicleWeights(
					laDBA);
			// Get a vector of Data object
			Vector lvCommercialVehicleWeightsData = laCommercialVehicleWeights
					.qryCommercialVehicleWeights();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to CommercialVehicleWeights");
			return (lvCommercialVehicleWeightsData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to CommercialVehicleWeights");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Country Calendar year data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getCountyCalndrYrCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to CountyCalendarYear");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			CountyCalendarYear laCountyCalendarYear = new CountyCalendarYear(
					laDBA);
			// Get a vector of Data object
			Vector lvCountyCalendarYearData = laCountyCalendarYear
					.qryCountyCalendarYear();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to CountyCalendarYear");
			return (lvCountyCalendarYearData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to CountyCalendarYear");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Credit Card Fees Cache from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getCreditCardFeesCache(Object aaObject)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = (GeneralSearchData) aaObject;
		Log.write(Log.APPLICATION, this,
				"Starting DB call to CreditCardFeeData");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			CreditCardFee laCreditCardFee = new CreditCardFee(laDBA);
			CreditCardFeeData laCreditCardFeeData = new CreditCardFeeData();
			laCreditCardFeeData.setOfcIssuanceNo(laGeneralSearchData
					.getIntKey1());
			laCreditCardFeeData.setSubstaId(laGeneralSearchData
					.getIntKey2());
			laCreditCardFeeData.setChngTimestmp(laGeneralSearchData
					.getDate1());
			// Get a vector of Data object
			Vector lvCreditCardFeeData = laCreditCardFee
					.qryCreditCardFee(laCreditCardFeeData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to CreditCardFeeData");
			return (lvCreditCardFeeData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to CreditCardFeeData");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Delete Reason data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getDelReasnsCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to DeleteReasons");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			DeleteReasons laDeleteReasons = new DeleteReasons(laDBA);
			// Get a vector of Data object
			Vector lvDeleteReasonsData = laDeleteReasons
					.qryDeleteReasons();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to DeleteReasons");
			return (lvDeleteReasonsData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to DeleteReasons");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Disabled Placard Delete Reason data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getDsabldPlcrdDelReasnCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to DsabldPlcrdDelReasn");

		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			DisabledPlacardDeleteReason laDsabldPlcrdDelReasn = new DisabledPlacardDeleteReason(
					laDBA);

			// Get a vector of Data object
			Vector lvDsabldPlcrdDelReasn = laDsabldPlcrdDelReasn
					.qryDisabledPlacardDeleteReason();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to DsabldPlcrdDelReasn");
			return (lvDsabldPlcrdDelReasn);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to DsabldPlcrdDelReasn");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Disabled Placard Customer Id Type data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getDsabldPlcrdCustIdTypeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to getDsabldPlcrdCustIdTypeCache");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			DisabledPlacardCustomerIdType laDsabldPlcrdCustIdType = new DisabledPlacardCustomerIdType(
					laDBA);

			// Get a vector of Data object
			Vector lvDsabldPlcrdCustIdType = laDsabldPlcrdCustIdType
					.qryDisabledPlacardCustomerIdType();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log
					.write(Log.APPLICATION, this,
							"Successful DB call to getDsabldPlcrdCustIdTypeCache");
			return (lvDsabldPlcrdCustIdType);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to getDsabldPlcrdCustIdTypeCache");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Dealer data from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getDealersCache(Object aaObject) throws RTSException
	{
		Log.write(Log.APPLICATION, this, "Starting DB call to Dealers");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			// defect 10112
			Dealer laDealer = new Dealer(laDBA);
			// defect 10003
			// No longer need to convert from GSD
			DealerData laDealerData = (DealerData) aaObject;
			// end defect 10003
			// Get a vector of Data object
			Vector lvDealerData = laDealer.qryDealer(laDealerData);
			// end defect 10112
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to Dealers");
			return (lvDealerData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to Dealers");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Message data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getDMVTRMsgCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to DMVTRMessage");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			DMVTRMessage laDMVTRMessage = new DMVTRMessage(laDBA);
			// Get a vector of Data object
			Vector lvDMVTRMessageData = laDMVTRMessage
					.qryDMVTRMessage();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to DMVTRMessage");
			return (lvDMVTRMessageData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to DMVTRMessage");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Document types data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getDocTypesCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to DocumentTypes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			DocumentTypes laDocumentTypes = new DocumentTypes(laDBA);
			// Get a vector of Data object
			Vector lvDocumentTypesData = laDocumentTypes
					.qryDocumentTypes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to DocumentTypes");
			return (lvDocumentTypesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to DocumentTypes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Error Messages data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getErrMsgCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to ErrorMessages");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			ErrorMessages laErrorMessages = new ErrorMessages(laDBA);
			// Get a vector of Data object
			Vector lvErrorMessagesData = laErrorMessages
					.qryErrorMessages();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to ErrorMessages");
			return (lvErrorMessagesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to ErrorMessages");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the FundsCodes data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getFundsCodesCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to FundsCodes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			FundsCodes laFundsCodes = new FundsCodes(laDBA);
			// Get a vector of Data object
			Vector lvFundsCodesData = laFundsCodes.qryFundsCodes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to FundsCodes");
			return (lvFundsCodesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to FundsCodes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Holiday cache from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getHolidayCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this, "Starting DB call to Holiday");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Holiday laHoliday = new Holiday(laDBA);

			// Get a vector of HolidayData
			Vector lvHolidayData = laHoliday.qryHoliday();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to Holiday");
			return lvHolidayData;
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to Holiday");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Indicator Description data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getIndiDescCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to IndicatorDescriptions");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			IndicatorDescriptions laIndicatorDescriptions = new IndicatorDescriptions(
					laDBA);
			// Get a vector of Data object
			Vector lvIndicatorDescriptionsData = laIndicatorDescriptions
					.qryIndicatorDescriptions();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to IndicatorDescriptions");
			return (lvIndicatorDescriptionsData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to IndicatorDescriptions");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Indicator Stop codes data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getIndiStopCodesCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to IndicatorStopCodes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			IndicatorStopCodes laIndicatorStopCodes = new IndicatorStopCodes(
					laDBA);
			// Get a vector of Data object
			Vector lvIndicatorStopCodesData = laIndicatorStopCodes
					.qryIndicatorStopCodes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to IndicatorStopCodes");
			return (lvIndicatorStopCodesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to IndicatorStopCodes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Invalid Letter data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getInvldLtrCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to InvalidLetter");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			InvalidLetter laInvalidLetter = new InvalidLetter(laDBA);
			// Get a vector of Data object
			Vector lvInvalidLetterData = laInvalidLetter
					.qryInvalidLetter();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to InvalidLetter");
			return (lvInvalidLetterData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to InvalidLetter");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Inventory Pattern data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getInvPatrnCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to InventoryPatterns");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			InventoryPatterns laInventoryPatterns = new InventoryPatterns(
					laDBA);
			// Get a vector of Data object
			Vector lvInventoryPatternsData = laInventoryPatterns
					.qryInventoryPatterns();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to InventoryPatterns");
			return (lvInventoryPatternsData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to InventoryPatterns");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Item Codes data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getItemCodesCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to ItemCodes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			ItemCodes laItemCodes = new ItemCodes(laDBA);
			// Get a vector of Data object
			Vector lvItemCodesData = laItemCodes.qryItemCodes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to ItemCodes");
			return (lvItemCodesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to ItemCodes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Lienholder Data from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getLienholdersCache(Object aaObject)
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to Lienholders");

		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();

			// defect 10112
			Lienholder laLienholder = new Lienholder(laDBA);
			// defect 10003
			// No longer need to convert from GSD
			LienholderData laLienholderData = (LienholderData) aaObject;
			// end defect 10003
			// Get a vector of Data object
			Vector lvLienholderData = laLienholder
					.qryLienholder(laLienholderData);
			// end defect 10112

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to Lienholders");
			return (lvLienholderData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to Lienholders");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Miscellaneous data from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getMiscCache(Object aaObject) throws RTSException
	{
		GeneralSearchData laGeneralSearchData = (GeneralSearchData) aaObject;
		Log.write(Log.APPLICATION, this,
				"Starting DB call to Miscellaneous");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Miscellaneous laMiscellaneous = new Miscellaneous(laDBA);
			MiscellaneousData laMiscellaneousData = new MiscellaneousData();
			laMiscellaneousData.setOfcIssuanceNo(laGeneralSearchData
					.getIntKey1());
			laMiscellaneousData.setSubstaId(laGeneralSearchData
					.getIntKey2());
			laMiscellaneousData.setChngTimestmp(laGeneralSearchData
					.getDate1());
			// Get a vector of Data object
			Vector lvMiscellaneousData = laMiscellaneous
					.qryMiscellaneous(laMiscellaneousData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to Miscellaneous");
			return (lvMiscellaneousData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to Miscellaneous");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Office Code data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getOfcCdCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to OfficeCodes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			OfficeCodes laOfficeCodes = new OfficeCodes(laDBA);
			// Get a vector of Data object
			Vector lvOfficeCodesData = laOfficeCodes.qryOfficeCodes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to OfficeCodes");
			return (lvOfficeCodesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to OfficeCodes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Office Id data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getOfcIdCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to OfficeIds");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			OfficeIds laOfficeIds = new OfficeIds(laDBA);
			// Get a vector of Data object
			// defect 9677
			Vector lvOfficeIdsData = laOfficeIds.qryOfficeIds(-1);
			// end defect 9677
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to OfficeIds");
			return (lvOfficeIdsData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to OfficeIds");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	// defect 10427

	// /**
	// * Fetches the AssignedWorkstationId data from database.
	// *
	// * @param aiOffcId int
	// * @param aiSubstaId int
	// * @return String
	// * @throws RTSException
	// */
	// public String getOfficeTimeZone(int aiOfcId, int aiSubstaId)
	// throws RTSException
	// {
	// Log.write(
	// Log.APPLICATION,
	// this,
	// "Starting DB call to - getOfficeTimeZone");
	// DatabaseAccess laDBA = new DatabaseAccess();
	// String lsTimeZone = null;
	// AssignedWorkstationIds laAWI = null;
	// AssignedWorkstationIdsData laAWIData = null;
	// try
	// {
	// laDBA.beginTransaction();
	// laAWI = new AssignedWorkstationIds(laDBA);
	// //Get a vector of Data object
	// laAWIData =
	// laAWI.qryMinAssignedWorkstationId(aiOfcId, aiSubstaId);
	// laDBA.endTransaction(DatabaseAccess.COMMIT);
	// Log.write(
	// Log.APPLICATION,
	// this,
	// "Successful DB call to AssignedWorkstationId");
	// }
	// catch (RTSException aeRTSException)
	// {
	// Log.write(
	// Log.APPLICATION,
	// this,
	// "Failed DB call to AssignedWorkstationId");
	// laDBA.endTransaction(DatabaseAccess.ROLLBACK);
	// throw aeRTSException;
	// }
	//
	// //If assigned workstation Id does not exist throw an error
	// if (laAWIData == null)
	// {
	// throw new RTSException(
	// RTSException.WARNING_MESSAGE,
	// "Assigned Workstation is not found for TimeZone",
	// "Data Error");
	// }
	// lsTimeZone = laAWIData.getTimeZone();
	// if (lsTimeZone == null)
	// {
	// throw new RTSException(
	// RTSException.WARNING_MESSAGE,
	// "Assigned Workstation TimeZone is null",
	// "Data Error");
	// }
	// return lsTimeZone;
	// }
	// end defect 10427

	/**
	 * Fetches the OfficeTimeZone data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getOfficeTimeZoneCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to OfcTimeZone");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			OfficeTimeZone laOfcTZone = new OfficeTimeZone(laDBA);

			// Get a vector of Data object
			Vector lvOfcTZone = laOfcTZone.qryOfficeTimeZone();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to OfcTimeZone");
			return (lvOfcTZone);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to OfcTimeZone");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Organization Number data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getOrgNoCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this, "Starting DB call to OrgNo");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			OrganizationNumber laOrgNo = new OrganizationNumber(laDBA);
			// Get a vector of Data object
			Vector lvOrgNo = laOrgNo.qryOrgNo();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to OrgNo");
			return (lvOrgNo);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this, "Failed DB call to OrgNo");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Owner Evidence Code data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getOwnerEvidenceCdCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to OwnershipEvidenceCodes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			OwnershipEvidenceCodes laOwnershipEvidenceCodes = new OwnershipEvidenceCodes(
					laDBA);
			// Get a vector of Data object
			Vector lvOwnershipEvidenceCodesData = laOwnershipEvidenceCodes
					.qryOwnershipEvidenceCodes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to OwnershipEvidenceCodes");
			return (lvOwnershipEvidenceCodesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to OwnershipEvidenceCodes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Passenger Fees data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPassengerFeesCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to PassengerFees");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PassengerFees laPassengerFees = new PassengerFees(laDBA);
			// Get a vector of Data object
			Vector lvPassengerFeesData = laPassengerFees
					.qryPassengerFees();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PassengerFees");
			return (lvPassengerFeesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PassengerFees");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Payment Status Code data from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPaymentStatusCodeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to PaymentStatusCodes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PaymentStatusCodes laPaymentStatusCodes = new PaymentStatusCodes(
					laDBA);
			// Get a vector of Data object
			Vector lvPaymentStatusCodesData = laPaymentStatusCodes
					.qryPaymentStatusCodes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PaymentStatusCodes");
			return (lvPaymentStatusCodesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PaymentStatusCodes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Payment Type data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPaymentTypeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to PaymentType");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PaymentType laPaymentType = new PaymentType(laDBA);
			// Get a vector of Data object
			Vector lvPaymentTypeData = laPaymentType.qryPaymentType();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PaymentType");
			return (lvPaymentTypeData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PaymentType");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Plate Group Id data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPltGrpIdCache() throws RTSException
	{
		Log
				.write(Log.APPLICATION, this,
						"Starting DB call to PltGrpId");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PlateGroupId laPltGrpId = new PlateGroupId(laDBA);
			// Get a vector of Data object
			Vector lvPltGrpId = laPltGrpId.qryPlateGroupId();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PltGrpId");
			return (lvPltGrpId);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PltGrpId");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Plate Symbol data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPlateSymbolCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to PlateSymbol");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PlateSymbol laPlateSymbol = new PlateSymbol(laDBA);
			// Get a vector of Data object
			Vector lvPlateSymbolData = laPlateSymbol.qryPlateSymbol();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PlateSymbol");
			return (lvPlateSymbolData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PlateSymbol");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Plate To Sticker data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPlateToStickerCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to PlateToSticker");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PlateToSticker laPlateToSticker = new PlateToSticker(laDBA);
			// Get a vector of Data object
			Vector lvPlateToStickerData = laPlateToSticker
					.qryPlateToSticker();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PlateToSticker");
			return (lvPlateToStickerData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PlateToSticker");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Plate Type data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPltTypeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this, "Starting DB call to PltType");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PlateType laPltType = new PlateType(laDBA);
			// Get a vector of Data object
			Vector lvPltType = laPltType.qryPlateType();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PltType");
			return (lvPltType);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PltType");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Plate Surcharge data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPltSurchargeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to PltSurcharge");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PlateSurcharge laPltSurcharge = new PlateSurcharge(laDBA);
			// Get a vector of Data object
			Vector lvPltSurcharge = laPltSurcharge.qryPlateSurcharge();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PltSurcharge");
			return (lvPltSurcharge);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PltSurcharge");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Postal State data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getPostalStateCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to PostalState");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			PostalState laPostalState = new PostalState(laDBA);
			// Get a vector of Data object
			Vector lvPlateSymbolData = laPostalState.qryPostalState();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to PostalState");
			return (lvPlateSymbolData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to PostalState");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Product Service data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getProductServiceCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to ProductService");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			ProductService laProductService = new ProductService(laDBA);
			// Get a vector of ProductService
			Vector lvProductServiceData = laProductService
					.qryProductService();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to ProductService");
			return lvProductServiceData;
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to ProductService");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Registration Additional Fee data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getRegistrationAdditionalFeeCache()
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to RegistrationAdditionalFee");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			RegistrationAdditionalFee laRegistrationAdditionalFee = new RegistrationAdditionalFee(
					laDBA);
			// Get a vector of data object
			Vector lvRegistrationAdditionalFee = laRegistrationAdditionalFee
					.qryRegistrationAdditionalFee();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to RegistrationAdditionalFee");
			return (lvRegistrationAdditionalFee);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to RegistrationAdditionalFeee");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Registration Class data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getRegistrationClassCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to RegistrationClass");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			RegistrationClass laRegistrationClass = new RegistrationClass(
					laDBA);
			// Get a vector of Data object
			Vector lvRegistrationClassData = laRegistrationClass
					.qryRegistrationClass();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to RegistrationClass");
			return (lvRegistrationClassData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to RegistrationClass");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Registration Class Fee Group data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getRegistrationClassFeeGroupCache()
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to RegistrationClassFeeGroup");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			RegistrationClassFeeGroup laRegClassFeeGrp = new RegistrationClassFeeGroup(
					laDBA);

			// Get a vector of Data object
			Vector lvRegClassFeeGrpData = laRegClassFeeGrp
					.qryRegistrationClassFeeGroup();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to RegistrationClassFeeGroup");
			return lvRegClassFeeGrpData;
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to RegistrationClassFeeGroup");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Registration Weight Fees data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getRegistrationWeightsFeesCache()
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to RegistrationWeightFees");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			RegistrationWeightFees laRegistrationWeightFees = new RegistrationWeightFees(
					laDBA);
			// Get a vector of Data object
			Vector lvRegistrationWeightFeesData = laRegistrationWeightFees
					.qryRegistrationWeightFees();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to RegistrationWeightFees");
			return (lvRegistrationWeightFeesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to RegistrationWeightFees");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Report Category data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getReportCategoryCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to ReportCategory");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			ReportCategory laReportCategory = new ReportCategory(laDBA);
			// Get a vector of Data object
			Vector lvReportCategoryData = laReportCategory
					.qryReportCategory();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to ReportCategory");
			return (lvReportCategoryData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to ReportCategory");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Reports data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getReportsCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this, "Starting DB call to Reports");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Reports laReports = new Reports(laDBA);
			// Get a vector of Data object
			Vector lvReportsData = laReports.qryReports();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to Reports");
			return (lvReportsData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to Reports");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the RTS_RSPS_WS_STATUS data from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getRSPSWsStatusCache(Object aaObject)
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to RSPSWsStatus");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			GeneralSearchData laGSD = (GeneralSearchData) aaObject;
			RSPSWsStatusData laRWS = new RSPSWsStatusData();
			laRWS.setOfcIssuanceNo(laGSD.getIntKey1());
			// defect 7810
			// retrieve RSPSWsStatus data based upon LocIdCd/LocId
			if (laGSD.getIntKey2() != 0)
			{
				laRWS.setLocId(laGSD.getIntKey2());
			}
			if (laGSD.getKey1() != null && laGSD.getKey1().length() > 0)
			{
				laRWS.setLocIdCd(laGSD.getKey1());
			}
			// end defect 7810
			laDBA.beginTransaction();
			RSPSWsStatus laRSPSWsStatus = new RSPSWsStatus(laDBA);
			// Get a vector of RSPSWsStatus
			Vector lvRSPSWsStatus = laRSPSWsStatus
					.qryRSPSWsStatus(laRWS);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to RSPSWsStatus");
			return lvRSPSWsStatus;
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to RSPSWsStatus");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Sales Tax Category data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getSalesTaxCategoryCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to SalesTaxCategory");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			SalesTaxCategory laSalesTaxCategory = new SalesTaxCategory(
					laDBA);
			// Get a vector of Data object
			Vector lvSalesTaxCategoryData = laSalesTaxCategory
					.qrySalesTaxCategory();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to SalesTaxCategory");
			return (lvSalesTaxCategoryData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to SalesTaxCategory");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Security data from database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getSecurityCache(Object aaObject)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = (GeneralSearchData) aaObject;
		Log
				.write(Log.APPLICATION, this,
						"Starting DB call to Security");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Security laSecurity = new Security(laDBA);
			SecurityData laSecurityData = new SecurityData();
			laSecurityData.setOfcIssuanceNo(laGeneralSearchData
					.getIntKey1());
			laSecurityData
					.setSubstaId(laGeneralSearchData.getIntKey2());
			// defect 7017
			// Only get active users for security cache
			// laSecurityData.setChngTimestmp(laGeneralSearchData.getDate1());
			// end defect 7017
			// Get a vector of Data object
			Vector lvSecurityData = laSecurity
					.qrySecurity(laSecurityData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to Security");
			return (lvSecurityData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to Security");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the SpecialPlate FixedExpiration Month Data from the database
	 * 
	 * @return Object
	 */
	private Object getSpecialPlateFixedExpirationMonthCache()
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to SpecialPlateFixedExpirationMonth");

		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			SpecialPlateFixedExpirationMonth laSpecialPlateFixedExpirationMonth = new SpecialPlateFixedExpirationMonth(
					laDBA);
			// Get a vector of Data object
			Vector lvSpecialPlateFixedExpirationMonth = laSpecialPlateFixedExpirationMonth
					.qrySpecialPlateFixedExpirationMonth();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log
					.write(Log.APPLICATION, this,
							"Successful DB call to SpecialPlateFixedExpirationMonth");
			return (lvSpecialPlateFixedExpirationMonth);
		}
		catch (RTSException aeRTSException)
		{
			Log
					.write(Log.APPLICATION, this,
							"Failed DB call to SpecialPlateFixedExpirationMonth");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Static Cache table records..
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getStaticCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to StaticCache");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			StaticCache laStaticCache = new StaticCache(laDBA);

			// defect 8849
			Vector lvStaticCacheData = new Vector();
			for (int i = 0; i < 2; i++)
			{
				try
				{
					lvStaticCacheData = laStaticCache.qryStaticCache();
				}
				catch (RTSException aeRTSEx)
				{
					if (aeRTSEx.getDetailMsg() != null
							&& aeRTSEx.getDetailMsg().indexOf(
									"StaleConnection") > -1)
					{
						// add the client host to the message detail
						aeRTSEx.setDetailMsg(aeRTSEx.getDetailMsg()
								+ CommonConstant.SYSTEM_LINE_SEPARATOR
								+ "Client " + csClientHost);

						// defect 8869
						// log the StaleConnection
						aeRTSEx.writeExceptionToLog();
						// defect 8869

						// re-establish connection.
						laDBA = new DatabaseAccess();
						laStaticCache = new StaticCache(laDBA);
						laDBA.beginTransaction();
					}
					else
					{
						throw aeRTSEx;
					}
				}
			}
			// Get a vector of StaticCacheData
			// end defect 8849

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to StaticCache");
			return lvStaticCacheData;
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to StaticCache");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Subcontractor data from the database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getSubcontractorCache(Object aaObject)
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to Subcontractor");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Subcontractor laSubcontractor = new Subcontractor(laDBA);
			// defect 10003
			// No longer need to convert from GSD
			SubcontractorData laSubcontractorData = (SubcontractorData) aaObject;
			// end defect 10003
			// Get a vector of Data object
			Vector lvSubcontractorData = laSubcontractor
					.qrySubcontractor(laSubcontractorData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to Subcontractor");
			return (lvSubcontractorData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to Subcontractor");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Substation data from the database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getSubstationCache(Object aaObject)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = (GeneralSearchData) aaObject;
		Log.write(Log.APPLICATION, this,
				"Starting DB call to Substation");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			Substation laSubstation = new Substation(laDBA);
			SubstationData laSubstationData = new SubstationData();
			laSubstationData.setOfcIssuanceNo(laGeneralSearchData
					.getIntKey1());
			laSubstationData.setSubstaId(laGeneralSearchData
					.getIntKey2());
			laSubstationData.setChngTimestmp(laGeneralSearchData
					.getDate1());
			// Get a vector of Data object
			Vector lvSubstationData = laSubstation
					.qrySubstation(laSubstationData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to Substation");
			return (lvSubstationData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to Substation");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the SubstationSubscription data from the database.
	 * 
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getSubstationSubscriptionCache(Object aaObject)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = (GeneralSearchData) aaObject;
		Log.write(Log.APPLICATION, this,
				"Starting DB call to SubstationSubscription");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			SubstationSubscription laSubstationSubscription = new SubstationSubscription(
					laDBA);
			SubstationSubscriptionData laSubstationSubscriptionData = new SubstationSubscriptionData();
			laSubstationSubscriptionData
					.setOfcIssuanceNo(laGeneralSearchData.getIntKey1());
			laSubstationSubscriptionData
					.setSubstaId(laGeneralSearchData.getIntKey2());
			laSubstationSubscriptionData
					.setChngTimestmp(laGeneralSearchData.getDate1());
			// Get a vector of Data object
			Vector lvSubstationSubscriptionData = laSubstationSubscription
					.qrySubstationSubscription(laSubstationSubscriptionData);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to SubstationSubscription");
			return (lvSubstationSubscriptionData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to SubstationSubscription");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Tax Exempt Code data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTaxExemptCdCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to TaxExemptCode");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			TaxExemptCode laTaxExemptCode = new TaxExemptCode(laDBA);
			// Get a vector of Data object
			Vector lvTaxExemptCodeData = laTaxExemptCode
					.qryTaxExemptCode();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to TaxExemptCode");
			return (lvTaxExemptCodeData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to TaxExemptCode");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Transaction Code data from database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTransactionCdCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to TransactionCodes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			TransactionCodes laTransactionCodes = new TransactionCodes(
					laDBA);
			// Get a vector of Data object
			Vector lvTransactionCodesData = laTransactionCodes
					.qryTransactionCodes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to TransactionCodes");
			return (lvTransactionCodesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to TransactionCodes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Title TERP Fee data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTtlTERPFeeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to TitleTERPFee");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			TitleTERPFee laTitleTERPFee = new TitleTERPFee(laDBA);
			// Get a vector of Data object
			Vector lvTitleTERPFeeData = laTitleTERPFee
					.qryTitleTERPFee();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to TitleTERPFee");
			return (lvTitleTERPFeeData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to TitleTERPFee");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Title TERP Percent data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTtlTERPPercentCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to TitleTERPPrcnt");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			TitleTERPPercent laTitleTERPPrcnt = new TitleTERPPercent(
					laDBA);
			// Get a vector of Data object
			Vector lvTitleTERPPrcntData = laTitleTERPPrcnt
					.qryTitleTERPPercent();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to TitleTERPPrcnt");
			return (lvTitleTERPPrcntData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to TitleTERPPrcnt");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Title Transfer Entity data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTtlTrnsfrEntCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to TtlTrnsfrEnt");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			TitleTransferEntity laTtlTrnsfrEnt = new TitleTransferEntity(
					laDBA);
			// Get a vector of Data object
			Vector lvTtlTrnsfrEntData = laTtlTrnsfrEnt
					.qryTitleTransferEntity();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to TtlTrnsfrEnt");
			return (lvTtlTrnsfrEntData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to TtlTrnsfrEnt");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Title Transfer Fee data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTtlTrnsfrPnltyFeeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to TtlTrnsfrPnltyFee");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			TitleTransferPenaltyFee laTtlTrnsfrPnltyFee = new TitleTransferPenaltyFee(
					laDBA);
			// Get a vector of Data object
			Vector lvTtlTrnsfrPnltyExmptCd = laTtlTrnsfrPnltyFee
					.qryTitleTransferPenaltyFee();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to TtlTrnsfrPnltyFee");
			return (lvTtlTrnsfrPnltyExmptCd);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to TtlTrnsfrPnltyFee");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Title Transfer Penalty Exempt Code data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTtlTrnsfrPnltyExmptCdCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to TtlTrnsfrPnltyExemptCd");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			TitleTransferPenaltyExemptCode laTtlTrnsfrPnltyExmptCd = new TitleTransferPenaltyExemptCode(
					laDBA);
			// Get a vector of Data object
			Vector lvTtlTrnsfrPnltyExmptCdData = laTtlTrnsfrPnltyExmptCd
					.qryTitleTransferPenaltyExmptCd();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to TtlTrnsfrPnltyExemptCd");
			return (lvTtlTrnsfrPnltyExmptCdData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to TtlTrnsfrPnltyExemptCd");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

//	/**
//	 * Fetches the TxDOTHoliday cache from database.
//	 * 
//	 * @return Object
//	 * @throws RTSException
//	 * @deprecated 
//	 */
//	private Object getTxDOTHolidayCache() throws RTSException
//	{
//		Log.write(Log.APPLICATION, this,
//				"Starting DB call to TxDOTHoliday");
//		DatabaseAccess laDBA = new DatabaseAccess();
//		try
//		{
//			laDBA.beginTransaction();
//			TxDOTHoliday laTxDOTHoliday = new TxDOTHoliday(laDBA);
//			// Get a vector of TxDOTHolidayData
//			Vector lvTxDOTHolidayData = laTxDOTHoliday
//					.qryTxDOTHoliday();
//			laDBA.endTransaction(DatabaseAccess.COMMIT);
//			Log.write(Log.APPLICATION, this,
//					"Successful DB call to TxDOTHoliday");
//			return lvTxDOTHolidayData;
//		}
//		catch (RTSException aeRTSException)
//		{
//			Log.write(Log.APPLICATION, this,
//					"Failed DB call to TxDOTHoliday");
//			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
//			throw aeRTSException;
//		}
//	}

	/**
	 * Fetches the Vehicle Body type data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVehBodyTypesCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to VehicleBodyTypes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			VehicleBodyTypes laVehicleBodyTypes = new VehicleBodyTypes(
					laDBA);
			// Get a vector of Data object
			Vector lvVehicleBodyTypesData = laVehicleBodyTypes
					.qryVehicleBodyTypes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to VehicleBodyTypes");
			return (lvVehicleBodyTypesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to VehicleBodyTypes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Vehicle Class Spcl Plate Type Desc data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVehClassSpclPltTypeDescCache()
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to VehicleClassSpclPltTypeDesc");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			VehicleClassSpclPltTypeDesc laVehClassSpclPltTypeDesc = new VehicleClassSpclPltTypeDesc(
					laDBA);

			// Get a vector of Data object
			Vector lvVehClassSpclPltTypeDescData = laVehClassSpclPltTypeDesc
					.qryVehicleClassSpclPltTypeDesc();

			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log
					.write(Log.APPLICATION, this,
							"Successful DB call to VehicleClassSpclPltTypeDesc");
			return (lvVehClassSpclPltTypeDescData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to VehicleClassSpclPltTypeDesc");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Vehicle Class Registration Class data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVehClassRegClassCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to VehicleClassRegistrationClass");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			VehicleClassRegistrationClass laVehicleClassRegistrationClass = new VehicleClassRegistrationClass(
					laDBA);
			// Get a vector of Data object
			Vector lvVehicleClassRegistrationClassData = laVehicleClassRegistrationClass
					.qryVehicleClassRegistrationClass();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log
					.write(Log.APPLICATION, this,
							"Successful DB call to VehicleClassRegistrationClass");
			return (lvVehicleClassRegistrationClassData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to VehicleClassRegistrationClass");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Vehicle Diesel Ton Cache Class data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVehDieselTonCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to VehicleDieselTon");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			VehicleDieselTon laVehicleDieselTon = new VehicleDieselTon(
					laDBA);
			// Get a vector of Data object
			Vector lvVehicleDieselTonData = laVehicleDieselTon
					.qryVehicleDieselTon();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to VehicleDieselTon");
			return (lvVehicleDieselTonData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to VehicleDieselTon");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the Vehicle Color Cache Class data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVehColorCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to VehicleColor");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			VehicleColor laVehicleColor = new VehicleColor(laDBA);
			// Get a vector of Data object
			Vector lvVehicleColorData = laVehicleColor.qryVehColor();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to VehicleColor");
			return lvVehicleColorData;
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to VehicleColor");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Fetches the Vehicle Make data from the database.
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVehMakeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to VehicleMakes");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			VehicleMakes laVehicleMakes = new VehicleMakes(laDBA);
			// Get a vector of Data object
			Vector lvVehicleMakesData = laVehicleMakes
					.qryVehicleMakes();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to VehicleMakes");
			return (lvVehicleMakesData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to VehicleMakes");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the WebAgencyTypeCache
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getWebAgencyTypeCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to WebAgencyType");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			WebAgencyType laWebAgencyType = new WebAgencyType(laDBA);

			// Get a vector of Data object
			Vector lvWebAgencyTypeData = laWebAgencyType
					.qryWebAgencyType();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to WebAgencyType");
			return (lvWebAgencyTypeData);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to WebAgencyType");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Fetches the WebAgencyBatchStatusCache
	 * 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getWebAgencyBatchStatusCache() throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to WebAgencyBatchStatus");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			WebAgencyBatchStatus laWebAgencyBatchStatus = new WebAgencyBatchStatus(
					laDBA);

			// Get a vector of Data object
			Vector lvWebAgencyBatchStatus = laWebAgencyBatchStatus
					.qryWebAgencyBatchStatus();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log.write(Log.APPLICATION, this,
					"Successful DB call to WebAgencyBatchStatus");
			return (lvWebAgencyBatchStatus);
		}
		catch (RTSException aeRTSException)
		{
			Log.write(Log.APPLICATION, this,
					"Failed DB call to WebAgencyBatchStatus");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}

	}

	/**
	 * Indicate if it is a cache server
	 * 
	 * @return boolean
	 */
	public static boolean isCacheServer()
	{
		return cbCacheServer;
	}

	/**
	 * Populates the Static Cache at server side.
	 * 
	 * @throws RTSException
	 */
	public void loadStaticCache() throws RTSException
	{
		System.out.println("loading cache");

		new AccountCodesCache()
				.setData((Vector) getAccountCodesCache());

		// defect 10726
		new BusinessPartnerCache()
				.setData((Vector) getBusinessPartnerCache());
		// end defect 10726

		// defect 9969
		new CertifiedLienholderCache()
				.setData((Vector) getCertifiedLienhldrCache(new CertifiedLienholderData()));
		// end defect 9969

		// defect 8218
		new ClassToPlateCache()
				.setData((Vector) getClassToPlateCache());
		new ClassToPlateDescriptionCache()
				.setData((Vector) getClassToPlateDescriptionCache());
		// end defect 8218
		new CommercialVehicleWeightsCache()
				.setData((Vector) getCommVehWtCache());
		new CommonFeesCache().setData((Vector) getCommonFeesCache());
		new CountyCalendarYearCache()
				.setData((Vector) getCountyCalndrYrCache());
		new DeleteReasonsCache().setData((Vector) getDelReasnsCache());

		// defect 9831
		new DisabledPlacardCustomerIdTypeCache()
				.setData((Vector) getDsabldPlcrdCustIdTypeCache());
		new DisabledPlacardDeleteReasonCache()
				.setData((Vector) getDsabldPlcrdDelReasnCache());
		// end defect 9831

		new DMVTRMessageCache().setData((Vector) getDMVTRMsgCache());
		new DocumentTypesCache().setData((Vector) getDocTypesCache());
		new ErrorMessagesCache().setData((Vector) getErrMsgCache());
		// defect 6949
		new FundsCodesCache().setData((Vector) getFundsCodesCache());
		// end defect 6949

		// defect 9919
		new HolidayCache().setData((Vector) getHolidayCache());
		// end defect 9919

		new IndicatorDescriptionsCache()
				.setData((Vector) getIndiDescCache());
		new IndicatorStopCodesCache()
				.setData((Vector) getIndiStopCodesCache());
		new InvalidLetterCache().setData((Vector) getInvldLtrCache());
		new InventoryPatternsCache()
				.setData((Vector) getInvPatrnCache());
		new ItemCodesCache().setData((Vector) getItemCodesCache());
		new OfficeCodesCache().setData((Vector) getOfcCdCache());
		new OfficeIdsCache().setData((Vector) getOfcIdCache());
		new OwnershipEvidenceCodesCache()
				.setData((Vector) getOwnerEvidenceCdCache());
		new PassengerFeesCache()
				.setData((Vector) getPassengerFeesCache());
		new PaymentTypeCache().setData((Vector) getPaymentTypeCache());
		new PaymentStatusCodesCache()
				.setData((Vector) getPaymentStatusCodeCache());

		// defect 9085
		// Load 7 new static cache tables for Special Plates
		new OrganizationNumberCache().setData((Vector) getOrgNoCache());
		new PlateGroupIdCache().setData((Vector) getPltGrpIdCache());
		new PlateTypeCache().setData((Vector) getPltTypeCache());
		new PlateSurchargeCache()
				.setData((Vector) getPltSurchargeCache());
		new SpecialPlateFixedExpirationMonthCache()
				.setData((Vector) getSpecialPlateFixedExpirationMonthCache());
		new VehicleClassSpclPltTypeDescCache()
				.setData((Vector) getVehClassSpclPltTypeDescCache());
		
		// defect 9919 
		//new TxDOTHolidayCache()
		//		.setData((Vector) getTxDOTHolidayCache());
		// end defect 9919 
		// end defect 9085

		// defect 10366
		new PlateSymbolCache().setData((Vector) getPlateSymbolCache());
		// end defect 10366

		// defect 10396
		new PostalStateCache().setData((Vector) getPostalStateCache());
		// end defect 10396

		// defect 10427
		new OfficeTimeZoneCache()
				.setData((Vector) getOfficeTimeZoneCache());
		// end defect 10427

		// defect 10695
		new RegistrationClassFeeGroupCache()
				.setData((Vector) getRegistrationClassFeeGroupCache());
		// end defect 10695

		// defect 9583
		new TitleTransferEntityCache()
				.setData((Vector) getTtlTrnsfrEntCache());
		new TitleTransferPenaltyFeeCache()
				.setData((Vector) getTtlTrnsfrPnltyFeeCache());
		// end defect 9583

		// defect 9724
		new TitleTransferPenaltyExemptCodeCache()
				.setData((Vector) getTtlTrnsfrPnltyExmptCdCache());
		// end defect 9724

		// defect 8218
		new PlateToStickerCache()
				.setData((Vector) getPlateToStickerCache());
		// end defect 8218
		// defect 8104
		// new static table
		new RegistrationAdditionalFeeCache()
				.setData((Vector) getRegistrationAdditionalFeeCache());
		// end defect 8104
		new RegistrationClassCache()
				.setData((Vector) getRegistrationClassCache());
		new RegistrationWeightFeesCache()
				.setData((Vector) getRegistrationWeightsFeesCache());
		new ReportCategoryCache()
				.setData((Vector) getReportCategoryCache());
		new ReportsCache().setData((Vector) getReportsCache());
		new SalesTaxCategoryCache()
				.setData((Vector) getSalesTaxCategoryCache());
		new TaxExemptCodeCache()
				.setData((Vector) getTaxExemptCdCache());
		// defect 6447
		// TERP
		new TitleTERPFeeCache().setData((Vector) getTtlTERPFeeCache());
		new TitleTERPPercentCache()
				.setData((Vector) getTtlTERPPercentCache());
		// end defect 6447
		new TransactionCodesCache()
				.setData((Vector) getTransactionCdCache());
		new VehicleBodyTypesCache()
				.setData((Vector) getVehBodyTypesCache());
		new VehicleClassRegistrationClassCache()
				.setData((Vector) getVehClassRegClassCache());
		new VehicleDieselTonCache()
				.setData((Vector) getVehDieselTonCache());
		new VehicleMakesCache().setData((Vector) getVehMakeCache());

		// defect 9675
		new WebServicesServiceActionVersionCache()
				.setData((Vector) getWebServicesServiceActionVersionCache());
		// end defect 9675

		// defect 10708
		new WebAgencyTypeCache()
				.setData((Vector) getWebAgencyTypeCache());

		new WebAgencyBatchStatusCache()
				.setData((Vector) getWebAgencyBatchStatusCache());
		// end defect 10708

		// defect 10712
		new VehicleColorCache().setData((Vector) getVehColorCache());
		// end defect 10712
	}

	/**
	 * Build up cache for WebServicesServiceActionVersionCache.
	 * 
	 * @return Object
	 */
	private Object getWebServicesServiceActionVersionCache()
			throws RTSException
	{
		Log.write(Log.APPLICATION, this,
				"Starting DB call to WebServicesServiceActionVersion");
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			WebServicesServiceActionVersionSql laServiceActionVersionSql = new WebServicesServiceActionVersionSql(
					laDBA);
			// Get a vector of Data object
			Vector lvServiceActionVersionList = laServiceActionVersionSql
					.qryRtsSrvcActnVersionList();
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			Log
					.write(Log.APPLICATION, this,
							"Successful DB call to WebServicesServiceActionVersion");
			return (lvServiceActionVersionList);
		}
		catch (RTSException aeRTSException)
		{
			Log
					.write(Log.APPLICATION, this,
							"Failed DB call to WebServicesServiceActionVersion");
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSException;
		}
	}

	/**
	 * Main method
	 * 
	 * @param aarrArgs
	 *            String
	 */
	public static void main(String aarrArgs[])
	{
		try
		{
			CacheManagerServerBusiness laCB = new CacheManagerServerBusiness();
			laCB.loadStaticCache();
		}
		catch (RTSException aeRTSException)
		{
			System.err.println("CacheManagerServerBusiness error "
					+ " " + (new RTSDate()).getYYYYMMDDDate() + " "
					+ (new RTSDate()).get24HrTime());
			aeRTSException.printStackTrace();
		}
	}

	/**
	 * Main method to dispatch method calls according to function id
	 * 
	 * @param asModuldName
	 *            int
	 * @param aiFunctionId
	 *            int
	 * @param aaObject
	 *            Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object processData(int asModuleName, int aiFunctionId,
			Object aaObject) throws RTSException
	{
		switch (aiFunctionId)
		{
		case CacheConstant.ACCOUNT_CODES_CACHE:
			return getAccountCodesCache();
		case CacheConstant.ADMINISTRATION_CACHE_TABLE_CACHE:
			return getAdmCacheTblCache(aaObject);
		case CacheConstant.ASSIGNED_WORKSTATION_IDS_CACHE:
			return getAssgndWsIdsCache(aaObject);

			// defect 10701
		case CacheConstant.BATCH_REPORT_MANAGEMENT_CACHE:
			return getBatchReportManagementCache(aaObject);
			// end defect 10701

			// defect 10726
		case CacheConstant.BUSINESS_PARTNER_CACHE:
			return getBusinessPartnerCache();
			// end defect 10726

			// defect 9969
		case CacheConstant.CERTFD_LIENHLDR_CACHE:
			return getCertifiedLienhldrCache((CertifiedLienholderData) aaObject);
			// end defect 9969
			// defect 8218
		case CacheConstant.CLASS_TO_PLATE_CACHE:
			return getClassToPlateCache();
		case CacheConstant.CLASS_TO_PLATE_DESCRIPTION_CACHE:
			return getClassToPlateDescriptionCache();
			// end defect 8218
		case CacheConstant.COMMERCIAL_VEHICLE_WEIGHTS_CACHE:
			return getCommVehWtCache();
		case CacheConstant.COMMON_FEES_CACHE:
			return getCommonFeesCache();
		case CacheConstant.COUNTY_CALENDAR_YEAR_CACHE:
			return getCountyCalndrYrCache();
		case CacheConstant.CREDIT_CARD_FEES_CACHE:
			return getCreditCardFeesCache(aaObject);
		case CacheConstant.DEALERS_CACHE:
			return getDealersCache(aaObject);
		case CacheConstant.DELETE_REASONS_CACHE:
			return getDelReasnsCache();

			// defect 9831
		case CacheConstant.DSABLD_PLCRD_CUST_ID_TYPE_CACHE:
			return getDsabldPlcrdCustIdTypeCache();

		case CacheConstant.DSABLD_PLCRD_DEL_REASN_CACHE:
			return getDsabldPlcrdDelReasnCache();
			// end defect 9831

		case CacheConstant.DMVTR_MESSAGE_CACHE:
			return getDMVTRMsgCache();
		case CacheConstant.DOCUMENT_TYPES_CACHE:
			return getDocTypesCache();
		case CacheConstant.ERROR_MESSAGES_CACHE:
			return getErrMsgCache();
			// defect 6949
		case CacheConstant.FUNDS_CODES_CACHE:
			return getFundsCodesCache();
			// end defect 6949

			// defect 9919
		case CacheConstant.HOLIDAY_CACHE:
			return getHolidayCache();
			// end defect 9919

		case CacheConstant.INDICATOR_DESCRIPTIONS_CACHE:
			return getIndiDescCache();
		case CacheConstant.INDICATOR_STOP_CODES_CACHE:
			return getIndiStopCodesCache();
		case CacheConstant.INVALID_LETTER_CACHE:
			return getInvldLtrCache();
		case CacheConstant.INVENTORY_PATTERNS_CACHE:
			return getInvPatrnCache();
		case CacheConstant.ITEM_CODES_CACHE:
			return getItemCodesCache();
		case CacheConstant.LIENHOLDERS_CACHE:
			return getLienholdersCache(aaObject);
		case CacheConstant.MISCELLANEOUS_CACHE:
			return getMiscCache(aaObject);
		case CacheConstant.OFFICE_CODES_CACHE:
			return getOfcCdCache();
		case CacheConstant.OFFICE_IDS_CACHE:
			return getOfcIdCache();
		case CacheConstant.OWNER_EVIDENCE_CODES_CACHE:
			return getOwnerEvidenceCdCache();
		case CacheConstant.PASSENGER_FEES_CACHE:
			return getPassengerFeesCache();
		case CacheConstant.PAYMENT_TYPE_CACHE:
			return getPaymentTypeCache();
		case CacheConstant.PAYMENT_STATUS_CODES_CACHE:
			return getPaymentStatusCodeCache();

			// defect 9085
		case CacheConstant.ORG_NO_CACHE:
			return getOrgNoCache();
		case CacheConstant.PLT_GRP_ID_CACHE:
			return getPltGrpIdCache();
		case CacheConstant.PLT_TYPE_CACHE:
			return getPltTypeCache();
		case CacheConstant.PLT_SURCHARGE_CACHE:
			return getPltSurchargeCache();
			
			// defect 9919
			// case CacheConstant.TXDOT_HOLIDAY_CACHE :
			// return getTxDOTHolidayCache();
			// end defect 9919
			
		case CacheConstant.SPCL_PLT_FXD_EXP_MO:
			return getSpecialPlateFixedExpirationMonthCache();
		case CacheConstant.VEH_CLASS_SPCL_PLT_TYPE_DESC_CACHE:
			return getVehClassSpclPltTypeDescCache();

			// case CacheConstant.REGISTRATION_RENEWALS_CACHE :
			// return getRegistrationRenewalCache();
			// end defect 9085

			// defect 8218
		case CacheConstant.PLATE_TO_STICKER_CACHE:
			return getPlateToStickerCache();
			// end defect 8218

			// defect 10366
		case CacheConstant.PLATE_SYMBOL_CACHE:
			return getPlateSymbolCache();
			// end defect 10366

			// defect 10396
		case CacheConstant.POSTAL_STATE_CACHE:
			return getPostalStateCache();
			// end defect 10396

			// defect 10427
		case CacheConstant.OFFICE_TIMEZONE_CACHE:
			return getOfficeTimeZoneCache();
			// end defect 10427

		case CacheConstant.PRODUCT_SERVICE_CACHE:
			return getProductServiceCache();
			// defect 8104
		case CacheConstant.REGISTRATION_ADDITIONAL_FEE_CACHE:
			return getRegistrationAdditionalFeeCache();
			// end defect 8104
		case CacheConstant.REGISTRATION_CLASS_CACHE:
			return getRegistrationClassCache();

			// defect 10695
		case CacheConstant.REGISTRATION_CLASS_FEE_GRP_CACHE:
			return getRegistrationClassFeeGroupCache();
			// end defect 10695

		case CacheConstant.REGISTRATION_WEIGHT_FEES_CACHE:
			return getRegistrationWeightsFeesCache();
		case CacheConstant.REPORT_CATEGORY_CACHE:
			return getReportCategoryCache();
		case CacheConstant.REPORTS_CACHE:
			return getReportsCache();
		case CacheConstant.RSPS_WS_STATUS:
			return getRSPSWsStatusCache(aaObject);
		case CacheConstant.SALES_TAX_CATEGORY_CACHE:
			return getSalesTaxCategoryCache();
		case CacheConstant.SECURITY_CACHE:
			return getSecurityCache(aaObject);
		case CacheConstant.STATIC_CACHE_TABLE_CACHE:
			return getStaticCache();
		case CacheConstant.SUBCONTRACTOR_CACHE:
			return getSubcontractorCache(aaObject);
		case CacheConstant.SUBSTATION_CACHE:
			return getSubstationCache(aaObject);
		case CacheConstant.SUBSTATION_SUBSCRIPTION_CACHE:
			return getSubstationSubscriptionCache(aaObject);
		case CacheConstant.TAX_EXEMPT_CODE_CACHE:
			return getTaxExemptCdCache();
			// defect 6447
		case CacheConstant.TITLE_TERP_FEE_CACHE:
			return getTtlTERPFeeCache();
		case CacheConstant.TITLE_TERP_PERCENT_CACHE:
			return getTtlTERPPercentCache();
			// end defect 6447

			// defect 9583
		case CacheConstant.TTL_TRNSFR_ENT_CACHE:
			return getTtlTrnsfrEntCache();
		case CacheConstant.TTL_TRNSFR_PNLTY_FEE_CACHE:
			return getTtlTrnsfrPnltyFeeCache();
			// end defect 9583

			// defect 9724
		case CacheConstant.TTL_TRNSFR_PNLTY_EXMPT_CD_CACHE:
			return getTtlTrnsfrPnltyExmptCdCache();
			// end defect 9724

		case CacheConstant.TRANSACTION_CODES_CACHE:
			return getTransactionCdCache();
		case CacheConstant.VEHICLE_BODY_TYPES_CACHE:
			return getVehBodyTypesCache();
		case CacheConstant.VEHICLE_CLASS_REGISTRATION_CLASS_CACHE:
			return getVehClassRegClassCache();

			// defect 10712
		case CacheConstant.VEHICLE_COLOR_CACHE:
			return getVehColorCache();
			// end defect 10712

		case CacheConstant.VEHICLE_DIESEL_TON_CACHE:
			return getVehDieselTonCache();
		case CacheConstant.VEHICLE_MAKES_CACHE:
			return getVehMakeCache();

			// defect 10708
		case CacheConstant.WEB_AGENCY_TYPE_CACHE:
			return getWebAgencyTypeCache();
		case CacheConstant.WEB_AGENCY_BATCH_STATUS_CACHE:
			return getWebAgencyBatchStatusCache();
			// end defect 10708

			// This call should be made to refresh the static cache at
			// server side.
		case CacheConstant.REFRESH_STATIC_CACHE_AT_SERVER:
		{
			loadStaticCache();
			return null; // No need to return anything.
		}
		default:
			return null;
		}
	}

	/**
	 * Set if it is a cache server
	 * 
	 * @param abCacheServer
	 *            boolean
	 */
	public static void setCacheServer(boolean abCacheServer)
	{
		cbCacheServer = abCacheServer;
	}
}
