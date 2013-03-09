package com.txdot.isd.rts.services.cache;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CacheConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;

/*
 *
 * CacheManager.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/12/2001	Static Cache done
 * K Harrell    08/01/2002  Updated dispatchAdminTable
 *							defect 4507
 * K Harrell    01/15/2003  Updated writeToDisk, refreshCache 
 *							defect 5257
 * K Harrell    08/04/2003  TERP Implementation;
 *							add TTLTFEE, TTLTPCNT
 *                          add refreshTitleTERPFeeCache(),
 *                          refreshTitleTERPPercentCache()
 *                          modify loadCache(),persistCache()
 *							defect 6447
 * Ray Rowehl	03/19/2004	Change Security Cache update to use UserName
 *							as part of the key instead of EmpId in
 *							windows.
 *							modify refreshSecurityCache()
 *							defect 6953 Ver 5.1.6
 * Ray Rowehl	04/22/2004	Always refresh all of security cache.
 *							This helps ensure that there are no deleted
 *							employees in cache.  Currently, windows does
 *							not purge users out of its cache.
 *							modify refreshSecurityCache()
 *							defect 6953 Ver 5.1.6
 * K Harrell	05/03/2004	Alter reference from getEmpId to
 *							getUserName for Security
 *							modify refreshSecurityCache()
 *							defect 6955 Ver 5.2.0
 * Ray Rowehl	07/19/2004	Add processing for RSPS
 *							add RSPSWSST, RSPSSUPDT,
 *								refreshRSPSSysUpdt(),
 *								refreshRSPSWsStsCache()
 *							modify dispatchAdminTableRefreshTask(),
 *								dispatchStaticTableRefreshTask(), 
 *								loadCache(), persistCache()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/06/2004	Update prolog with reference to 6955
 *							defect 7135 Ver 5.2.1
 * K Harrell	12/07/2004	Correct handling of new admin cache tables.
 *							Declaration Comment cleanup (for TERP) 
 *							modify dispatchAdminTableRefreshTask()
 *							defect 7771 Ver 5.2.1 Fix 2
 * K Harrell	04/04/2005	Add processing for RTS_REG_ADDL_FEE
 *							add REGADDFEE
 *							add refreshRegistrationAdditionalFeeCache()
 *							modify dispatchStaticTableRefreshTask(), 
 *							loadCache(),persistCache()
 *							defect 8104 Ver 5.2.2 Fix 4 
 * K Harrell	05/20/2005  Java 1.4 Work.  Remove parameters from 
 * 							RefreshSecurityCache() as well as all calls
 * 							to static tables.  Redefined 
 * 							static cache file name constants as 
 * 							static final. 
 * 							modify dispatchAdminTableRefreshTask(),
 *							refreshSecurityCache(), plus!   							  
 * 							defect 8152 Ver 5.2.3
 * K Harrell	06/19/2005	ClassToPlate,ClassToPlateDescription,
 * 							PlateToSticker processing
 * 							Reassigned field values
 * 							add CLASSTOPLT,CLASSTOPLTDESC,
 * 							 PLTTOSTKR
 * 							add refreshClassToPlateCache(), 
 * 							refreshClassToPlateDescriptionCache(),
 * 							refreshPlateToStickerCache()
 * 							delete REGISPLT, REGISSUB
 * 							deprecate refreshRegistrationPlateStickerCache(),
 * 							refreshRegistrationPlateStickerDescriptionCache(),
 *							modify dispatchStaticTableRefreshTask(), 
 *							loadCache(),persistCache() 
 * 							defect 8218 Ver 5.2.3
 * K Harrell	07/11/2005	Remove reference to RSPSSysUpdt in static
 * 							cache. 	
 * 							delete RSPSSUPD
 * 							deprecate refreshRSPSSysUpdt()
 * 							modify dispatchStaticTableRefreshTask(),
 * 							loadCache(),persistCache()
 * 							defect 8281 Ver 5.2.3
 * K Harrell	03/17/2006	Remove reference to CashWsIds in admin cache
 * 							delete CASHWSID
 * 							deprecate refreshCashWorkstationIdsCache()
 * 							modify dispatchAdminTableRefreshTask(),
 * 							loadCache(),persistCache()
 * K Harrell	02/10/2007	Add constants,methods, modify existings
 * 							 for Special Plates
 * 							add ORGNO,PLTGRPID, PLTSURCHARGE,PLTTYPE,
 * 							  VEHSPCLP
 *							add to vector of static cache tables	 
 * 							add refreshPlateTypeCache(), refreshPlateGroupId(),
 * 							  refreshOrganizationNumberCache(),
 * 							  refreshPlateSurcharge(), 
 * 							  refreshVehicleClassSpclPltTypeDesc()
 * 							modify dispatchStaticTableRefresh(),
 * 							  loadCache(), persistCache()
 * 							delete refreshCashWorkstationIds(),
 * 								refreshRegistrationPlateStickerCache(),
 * 								refreshRegistrationPlateStickerDescriptionCache()
 * 							deprecate refreshRegistrationRenewalsCache()
 * 							modified/renumbered constants
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/05/2007	add constants, methods, modify existing for
 * 							 TxDOT_Holiday 
 * 							add HOLIDAY
 * 							add to vector of static cahce tablees
 * 							add refreshTxDOTHolidayCache()
 * 							modify dispatchStaticTableRefresh(),
 * 							  loadCache(), persistCache()
 * 							defect 9085 Ver Special Plates 	
 * Ray Rowehl	03/15/2007	Check for null on cache timestamp before
 * 							using.  Also, bypass DMVTRMessage since
 * 							it is never persisted.
 * 							modify dispatchStaticTableRefreshTask()
 * 							defect 9139 Ver Special Plates
 * K Harrell	03/26/2007	add constants, methods, modify existing for
 * 							 SpecialPlateFixedExpMo  
 * 							add SPFDEXMO
 * 							add to vector of static cahce tablees
 * 							add refreshSpclPltFxdExpMoCache()
 * 							modify dispatchStaticTableRefresh(),
 * 							  loadCache(), persistCache()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/20/2007	PltTypeData.getLocAuthCd() does not relate
 * 							to a specific data element and caused 
 * 							PrintCache to fail. Now exclude getLocAuthCd
 * 							from methods used to print data.
 * 							modify PrintCache()	
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/11/2008	Remove reference to RegistrationPlateStickerCache,
 * 							RegistrationPlateStickerDescriptionCache, 
 * 							RSPSSysUpdateCache
 * 							modify dispatchStaticTableRefreshTask()
 *							defect 8235 Ver Defect POS A
 * K Harrell	03/11/2008	Remove references to cache for RTS_CASH_WS_IDS
 * 							modify dispatchAdminTableRefreshTask() 
 * 							defect 8282 Ver Defect POS A 
 * K Harrell	03/27/2008	Add coding for RTS_FUNDS_CODES
 * 							add FUNDSCDS 
 * 							add refreshFundsCodesCache() 
 * 							modify dispatchStaticTableRefreshTask()
 * 							defect 6949 Ver Defect POS A 
 * K Harrell	04/02/2008	Add coding for RTS_TTL_TRNSFR_ENT, 
 * 							 RTS_TTL_TRNSFR_PNLTY_EXMPT_CD, 
 * 							 RTS_TTL_TRNSFR_PNLTY_FEE 
 * 							add TXFRENT, TXFRPNTY, TXFRPFEE 
 * 							add refreshTtlTrnsfrEntCache(),
 * 							 refreshTtlTrnsfrPnltyExmptCdCache(),
 * 							 refreshTtlTrnsfrPnltyFeeCache() 
 * 							modify dispatchStaticTableRefreshTask()
 * 							defect 9583 Ver Defect POS A  
 * K Harrell	05/13/2008	Retrieve all Substations, Subscription data
 * 							modify refreshSubstationCache(),
 * 							  refreshSubstationSubscriptionCache()
 * 							defect 9488 Ver Defect POS A
 * K Harrell	05/21/2008	delete RTS_TTL_TRNSFR_PNLTY_EXMPT_CD							
 * 							delete refreshTtlTrnsfrPnltyExmptCdCache()
 * 							modify dispatchStaticTableRefreshTask()
 * 							defect 9583 Ver Defect POS A 
 * K Harrell	06/22/2008	restore RTS_TTL_TRNSFR_PNLTY_EXMPT_CD							
 * 							add refreshTtlTrnsfrPnltyExmptCdCache()
 * 							modify dispatchStaticTableRefreshTask()
 * 							defect 9583 Ver Defect POS A
 * K Harrell	09/05/2008	Delete unneeded methods, used for test during
 * 							intial development.  Add'l Cleanup.
 * 							delete persistCache()
 * 							modify main()
 * 							defect 9759 Ver Defect POS B  
 * K Harrell	10/21/2008	Add processing for Disabled Placard 
 * 							Customer Id Types, Disabled Placard 
 * 							Delete Reasons. 
 * 							add DPDELRSN, DPIDTYPE 
 * 							add refreshDisabledPlacardCustomerIdTypeCache(),
 * 							 refreshDisabledPlacardDeleteReasonCache()
 * 							modify dispatchStaticTableRefreshTask(), 
 * 							  loadCache() 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	02/26/2009  Add for CertifiedLienholder 
 * 							add CERTLIEN 
 * 							add refreshCertifiedLienholderCache() 
 * 							modify dispatchStaticTableRefreshTask(), 
 * 								loadCache() 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	06/09/2009	No longer use GSD to avoid conversion. 
 * 							modify refreshDealersCache(),
 * 							 refreshLienholdersCache(), 
 * 							 refreshSubcontractorCache()
 * 							defect 10003 Ver Defect_POS_F 
 * K Harrell	06/26/2009	Implement new DealerData, LienholderData
 * 							add refreshLienholdersCache()
 *  						delete refreshLienholderCache()
 * 							modify refreshDealersCache() 
 *							defect 10112 Ver Defect_POS_F
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							modify refreshSubcontractorCache()
 * 							defect 10161 Ver POS_640
 * K Harrell	03/01/2010	Refresh all of SubstationSubscriptions if 
 * 							any row has changed. 
 * 							add refreshSubstationSubscriptionCache() 
 * 							delete refreshSubstationSubscriptionCache(
 * 							  RTSDate,boolean)
 * 							modify dispatchAdminTableRefreshTask() 
 * 							defect 10186 Ver POS_640   	
 * K Harrell	03/24/2010	add PLTSYM 
 * 							add refreshPlateSymbolCache()
 * 							modify dispatchStaticTableRefreshTask(), 
 * 							 loadCache() 
 * 							defect 10366 Ver POS_640
 * K Harrell	03/24/2010	add POSTATE
 * 							add refreshPostalStateCache()
 * 							modify dispatchStaticTableRefreshTask(), 
 * 							 loadCache() 
 * 							defect 10396 Ver POS_640
 * K Harrell	03/29/2010	Correct type in refreshPlateSymbol()
 * 							modify refreshPlateSymbolCache() 
 * 							defect 10366 Ver POS_640 
 * K Harrell	04/03/2010	add OFCTZONE
 * 							add refreshOfficeTimeZoneCache()
 * 							modify dispatchStaticTableRefreshTask(), 
 * 							 loadCache() 
 * 							defect 10427 Ver POS_640	
 * K Harrell	07/23/2010	Error in implementation of 10186 
 * 							modify dispatchAdminTableRefreshTask()
 * 							defect 10554 Ver 6.5.0
 * K Harrell	12/08/2010	Implement Cache for RTS_REG_CLASS_FEE_GRP 
 * 							add RGCLFEGP 
 * 							add refreshRegistrationClassFeeGroup()
 *  				 		modify dispatchStaticTableRefreshTask(), 
 * 							 loadCache() 
 * 							defect 10695 Ver 6.7.0	
 * K Harrell	01/05/2011	Implement Cache for RTS_VEH_COLOR 
 * 							add VEHCOLOR 
 * 							add refreshVehicleColor()
 *  				 		modify dispatchStaticTableRefreshTask(), 
 * 							 loadCache() 
 * 							defect 10712 Ver 6.7.0
 * K Harrell	01/07/2011	Implement Cache for RTS_BSN_PARTNER 
 * 							add BSNPRTNR
 * 							add refreshBusinessPartner() 	 
 *  				 		modify dispatchStaticTableRefreshTask(), 
 * 							 loadCache() 
 * 							defect 10726 Ver 6.7.0 
 * K Harrell	01/16/2011	Implement Cache for RTS_BATCH_RPT_MGMT 
 * 							add BRPTMGMT
 * 							add refreshBatchReportManagementCache() 	 
 *  				 		modify dispatchStaticTableRefreshTask(), 
 * 							 loadCache() 
 * 							defect 10701 Ver 6.7.0 
 * K Harrell	03/16/2011	missing code for loadCache for StaticCache
 * 							 VEHCOLOR, RGCLFEGP 
 * 							modify loadCache(),dispatchStaticTableRefreshTask() 
 * 							defect 10695, l0712 Ver 6.7.0
 * K Harrell	10/08/2011	add logic for Holiday Table 
 * 							add refreshHolidayCache() 
 * 							delete refreshTxDOTHolidayCache() 
 *  				 		modify dispatchStaticTableRefreshTask(), 
 * 							 loadCache()
 * 							defect 9919 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * Cache manager is responsible for loading, refreshing and persisting cache.
 * 
 * @version 6.9.0 	10/08/2011
 * @author Nancy Ting 
 * @since 			08/10/2001 09:21:35
 */

public class CacheManager
{
	// static cache file names constants
	public static final int ACCTCODE = 0;

	// defect 8218
	public static final int CLSTOPLD = 1;

	public static final int CLSTOPLT = 2;

	// end defect 8218

	public static final int CNTYCALN = 3;

	public static final int COMMONFE = 4;

	public static final int COMMVEHW = 5;

	public static final int DELREASN = 6;

	public static final int DMVTRMSG = 7;

	public static final int DOCTYPES = 8;

	public static final int ERRMSGS = 9;

	// defect 6949
	public static final int FUNDSCDS = 10;

	// end defect 6949

	public static final int HOLIDAY = 11;

	public static final int INDIDESC = 12;

	public static final int INDISTOP = 13;

	public static final int INVALIDL = 14;

	public static final int INVPATTE = 15;

	public static final int ITEMCODE = 16;

	public static final int OFFICECD = 17;

	public static final int OFFICEID = 18;

	// defect 9085
	public static final int ORGNO = 19;

	// end defect 9085

	public static final int OWNREVID = 20;

	public static final int PASSFEES = 21;

	public static final int PAYMENTT = 22;

	// defect 9085
	public static final int PLTGRPID = 23;

	// end defect 9085

	public static final int PLTOSTKR = 24;

	// defect 9085
	public static final int PLTSURCHARGE = 25;

	public static final int PLTTYPE = 26;

	// end defect 9085

	public static final int PRODSVCE = 27;

	public static final int PYMNTSTA = 28;

	public static final int REGADFEE = 29;

	public static final int REGISCLA = 30;

	public static final int REGISWTF = 31;

	public static final int REGSUBCL = 32;

	public static final int REPORTS = 33;

	public static final int RPTCATEG = 34;

	public static final int SALESTAX = 35;

	public static final int SPFDEXMO = 36;

	public static final int STATCACH = 37;

	public static final int TAXEXMPT = 38;

	public static final int TRANSCDS = 39;

	public static final int TTLTFEE = 40;

	public static final int TTLTPCNT = 41;

	// defect 9583
	public final static int TXFRENT = 42;

	public final static int TXFRPFEE = 43;

	// end defect 9583

	// defect 9724
	public final static int TXFREXCD = 44;

	// end defect 9724

	public static final int VEHBDYTY = 45;

	public static final int VEHDIESE = 46;

	public static final int VEHMAKES = 47;

	// defect 9085
	public static final int VEHSPCLP = 48;

	// end defect 9085

	// defect 9831
	public static final int DPDELRSN = 49;

	public static final int DPIDTYPE = 50;

	// end defect 9831

	// defect 9969
	public static final int CERTLIEN = 51;

	// end defect 9969

	// defect 10366
	public static final int PLTSYM = 52;

	public static final int POSTATE = 53;

	// end defect 10366

	// defect 10427
	public static final int OFCTZONE = 54;

	// end defect 10427

	// defect 10695
	public static final int RGCLFEGP = 55;

	// end defect 10695

	// defect 10712
	public static final int VEHCOLOR = 56;

	// end defect 10712

	// defect 10726
	public static final int BSNPRTNR = 57;

	// end defect 10726

	// admin cache constants
	public static final int ADMINCAC = 58;

	public static final int ASSGNDWS = 59;

	// defect 10701
	public static final int BRPTMGMT = 60;
	// end defect 10701

	public static final int CCFEE = 61;

	public static final int DEALERS = 62;

	public static final int LIENHOLD = 63;

	public static final int MISC = 64;

	public static final int RSPSWSST = 65;

	public static final int SECURITY = 66;

	public static final int SUBCON = 67;

	public static final int SUBSSUBS = 68;

	public static final int SUBSTA = 69;

	private static final String TEMPDIR = "/temp/";

	/**
	 * Internally stores a hashtable of all cache classes with the name of the
	 * class name as key
	 */
	private static Hashtable shtCacheTables = new Hashtable();

	/**
	 * Provides the mapping for Class names and the corresponding serialized
	 * file.
	 */
	private static Vector svClass = new Vector();

	private static Vector svFile = new Vector();
	static
	{

		// Static Cache
		svClass.add(ACCTCODE, AccountCodesCache.class);
		svClass.add(CLSTOPLD, ClassToPlateDescriptionCache.class);
		svClass.add(CLSTOPLT, ClassToPlateCache.class);
		svClass.add(CNTYCALN, CountyCalendarYearCache.class);
		svClass.add(COMMONFE, CommonFeesCache.class);
		svClass.add(COMMVEHW, CommercialVehicleWeightsCache.class);
		svClass.add(DELREASN, DeleteReasonsCache.class);
		svClass.add(DMVTRMSG, DMVTRMessageCache.class);
		svClass.add(DOCTYPES, DocumentTypesCache.class);
		svClass.add(ERRMSGS, ErrorMessagesCache.class);
		// defect 6949
		svClass.add(FUNDSCDS, FundsCodesCache.class);
		// end defect 6949

		// defect 9919 
		// defect 9085
		svClass.add(HOLIDAY, HolidayCache.class);
		//svClass.add(HOLIDAY, TxDOTHolidayCache.class);
		// end defect 9085
		// end defect 9919

		svClass.add(INDIDESC, IndicatorDescriptionsCache.class);
		svClass.add(INDISTOP, IndicatorStopCodesCache.class);
		svClass.add(INVALIDL, InvalidLetterCache.class);
		svClass.add(INVPATTE, InventoryPatternsCache.class);
		svClass.add(ITEMCODE, ItemCodesCache.class);
		svClass.add(OFFICECD, OfficeCodesCache.class);
		svClass.add(OFFICEID, OfficeIdsCache.class);
		// defect 9085
		svClass.add(ORGNO, OrganizationNumberCache.class);
		// end defect 9085
		svClass.add(OWNREVID, OwnershipEvidenceCodesCache.class);
		svClass.add(PASSFEES, PassengerFeesCache.class);
		svClass.add(PAYMENTT, PaymentTypeCache.class);
		// defect 9085
		svClass.add(PLTGRPID, PlateGroupIdCache.class);
		svClass.add(PLTOSTKR, PlateToStickerCache.class);
		svClass.add(PLTSURCHARGE, PlateSurchargeCache.class);
		svClass.add(PLTTYPE, PlateTypeCache.class);
		// defect 9085
		svClass.add(PRODSVCE, ProductServiceCache.class);
		svClass.add(PYMNTSTA, PaymentStatusCodesCache.class);
		svClass.add(REGADFEE, RegistrationAdditionalFeeCache.class);
		svClass.add(REGISCLA, RegistrationClassCache.class);
		svClass.add(REGISWTF, RegistrationWeightFeesCache.class);
		svClass.add(REGSUBCL, VehicleClassRegistrationClassCache.class);
		svClass.add(REPORTS, ReportsCache.class);
		svClass.add(RPTCATEG, ReportCategoryCache.class);
		svClass.add(SALESTAX, SalesTaxCategoryCache.class);
		// defect 9085
		svClass.add(SPFDEXMO,
				SpecialPlateFixedExpirationMonthCache.class);
		// end defect 9085
		svClass.add(STATCACH, StaticCacheTableCache.class);
		svClass.add(TAXEXMPT, TaxExemptCodeCache.class);
		svClass.add(TRANSCDS, TransactionCodesCache.class);
		svClass.add(TTLTFEE, TitleTERPFeeCache.class);
		svClass.add(TTLTPCNT, TitleTERPPercentCache.class);
		// defect 9583
		svClass.add(TXFRENT, TitleTransferEntityCache.class);
		svClass.add(TXFRPFEE, TitleTransferPenaltyFeeCache.class);
		// end defect 9583

		// defect 9724
		svClass
				.add(TXFREXCD,
						TitleTransferPenaltyExemptCodeCache.class);
		// end defect 9724

		svClass.add(VEHBDYTY, VehicleBodyTypesCache.class);
		svClass.add(VEHDIESE, VehicleDieselTonCache.class);
		svClass.add(VEHMAKES, VehicleMakesCache.class);
		// defect 9085
		svClass.add(VEHSPCLP, VehicleClassSpclPltTypeDescCache.class);
		// end defect 9085

		// defect 9831
		svClass.add(DPDELRSN, DisabledPlacardDeleteReasonCache.class);
		svClass.add(DPIDTYPE, DisabledPlacardCustomerIdTypeCache.class);
		// end defect 9831

		// defect 9969
		svClass.add(CERTLIEN, CertifiedLienholderCache.class);
		// end defect 9969

		// defect 10366
		svClass.add(PLTSYM, PlateSymbolCache.class);
		// end defect 10366

		// defect 10396
		svClass.add(POSTATE, PostalStateCache.class);
		// end defect 10396

		// defect 10427
		svClass.add(OFCTZONE, OfficeTimeZoneCache.class);
		// end defect 10427

		// defect 10695
		svClass.add(RGCLFEGP, RegistrationClassFeeGroupCache.class);
		// end defect 10695

		// defect 10712
		svClass.add(VEHCOLOR, VehicleColorCache.class);
		// end defect 10712

		// defect 10726
		svClass.add(BSNPRTNR, BusinessPartnerCache.class);
		// end defect 10726

		// Admin cache
		svClass.add(ADMINCAC, AdministrationCacheTableCache.class);
		svClass.add(ASSGNDWS, AssignedWorkstationIdsCache.class);
		// defect 10701
		svClass.add(BRPTMGMT, BatchReportManagementCache.class);
		// end defect 10701
		svClass.add(CCFEE, CreditCardFeesCache.class);
		svClass.add(DEALERS, DealersCache.class);
		svClass.add(LIENHOLD, LienholdersCache.class);
		svClass.add(MISC, MiscellaneousCache.class);
		svClass.add(RSPSWSST, RSPSWsStatusCache.class);
		svClass.add(SECURITY, SecurityCache.class);
		svClass.add(SUBCON, SubcontractorCache.class);
		svClass.add(SUBSSUBS, SubstationSubscriptionCache.class);
		svClass.add(SUBSTA, SubstationCache.class);

		// Static cache file names
		svFile.add(ACCTCODE, "ACCTCODE");
		svFile.add(CLSTOPLD, "CLSTOPLD");
		svFile.add(CLSTOPLT, "CLSTOPLT");
		svFile.add(CNTYCALN, "CNTYCALN");
		svFile.add(COMMONFE, "COMMONFE");
		svFile.add(COMMVEHW, "COMMVEHW");
		svFile.add(DELREASN, "DELREASN");
		svFile.add(DMVTRMSG, "DMVTRMSG");
		svFile.add(DOCTYPES, "DOCTYPES");
		svFile.add(ERRMSGS, "ERRMSGS");
		svFile.add(FUNDSCDS, "FUNDSCDS");
		svFile.add(HOLIDAY, "HOLIDAY");
		svFile.add(INDIDESC, "INDIDESC");
		svFile.add(INDISTOP, "INDISTOP");
		svFile.add(INVALIDL, "INVALIDL");
		svFile.add(INVPATTE, "INVPATTE");
		svFile.add(ITEMCODE, "ITEMCODE");
		svFile.add(OFFICECD, "OFFICECD");
		svFile.add(OFFICEID, "OFFICEID");
		// defect 9085
		svFile.add(ORGNO, "ORGNO");
		// end defect 9085
		svFile.add(OWNREVID, "OWNREVID");
		svFile.add(PASSFEES, "PASSFEES");
		svFile.add(PAYMENTT, "PAYMENTT");
		// defect 9085
		svFile.add(PLTGRPID, "PLTGRPID");
		// end defect 9085
		svFile.add(PLTOSTKR, "PLTOSTKR");
		// defect 9085
		svFile.add(PLTSURCHARGE, "PLTSURCHARGE");
		svFile.add(PLTTYPE, "PLTTYPE");
		// end defect 9085
		svFile.add(PRODSVCE, "PRODSVCE");
		svFile.add(PYMNTSTA, "PYMNTSTA");
		svFile.add(REGADFEE, "REGADFEE");
		svFile.add(REGISCLA, "REGISCLA");
		svFile.add(REGISWTF, "REGISWTF");
		svFile.add(REGSUBCL, "REGSUBCL");
		svFile.add(REPORTS, "REPORTS");
		svFile.add(RPTCATEG, "RPTCATEG");
		svFile.add(SALESTAX, "SALESTAX");
		// defect 9085
		svFile.add(SPFDEXMO, "SPFDEXMO");
		// end defect 9085
		svFile.add(STATCACH, "STATCACH");
		svFile.add(TAXEXMPT, "TAXEXMPT");
		svFile.add(TRANSCDS, "TRANSCDS");
		svFile.add(TTLTFEE, "TTLTFEE");
		svFile.add(TTLTPCNT, "TTLTPCNT");

		// defect 9583
		svFile.add(TXFRENT, "TXFRENT");
		svFile.add(TXFRPFEE, "TXFRPFEE");
		// end defect 9583
		// defect 9724
		svFile.add(TXFREXCD, "TXFREXCD");
		// end defect 9724

		svFile.add(VEHBDYTY, "VEHBDYTY");
		svFile.add(VEHDIESE, "VEHDIESE");
		svFile.add(VEHMAKES, "VEHMAKES");
		// defect 9085
		svFile.add(VEHSPCLP, "VEHSPCLP");
		// defect 9085

		// defect 9831
		svFile.add(DPDELRSN, "DPDELRSN");
		svFile.add(DPIDTYPE, "DPIDTYPE");
		// end defect 9831

		// defect 9969
		svFile.add(CERTLIEN, "CERTLIEN");
		// end defect 9969

		// defect 10366
		svFile.add(PLTSYM, "PLTSYM");
		// end defect 10366

		// defect 10396
		svFile.add(POSTATE, "POSTATE");
		// end defect 10396

		// defect 10427
		svFile.add(OFCTZONE, "OFCTZONE");
		// end defect 10427

		// defect 10695
		svFile.add(RGCLFEGP, "RGCLFEGP");
		// end defect 10695

		// defect 10712
		svFile.add(VEHCOLOR, "VEHCOLOR");
		// end defect 10712

		// defect 10726
		svFile.add(BSNPRTNR, "BSNPRTNR");
		// end defect 10726

		// Admin cache file names
		svFile.add(ADMINCAC, "ADMINCAC");
		svFile.add(ASSGNDWS, "ASSGNDWS");

		// defect 10701
		svFile.add(BRPTMGMT, "BRPTMGMT");
		// end defect 10701

		svFile.add(CCFEE, "CCFEE");
		svFile.add(DEALERS, "DEALERS");
		svFile.add(LIENHOLD, "LIENHOLD");
		svFile.add(MISC, "MISC");
		svFile.add(RSPSWSST, "RSPSWSST");
		svFile.add(SECURITY, "SECURITY");
		svFile.add(SUBCON, "SUBCON");
		svFile.add(SUBSSUBS, "SUBSSUBS");
		svFile.add(SUBSTA, "SUBSTA");
	}

	/**
	 * CacheManager constructor comment.
	 * 
	 * @throws RTSException
	 */
	public CacheManager() throws RTSException
	{
		super();
	}

	/**
	 * Inspect the subscription timestamp as well as the last changed
	 * 
	 * timestamp
	 * 
	 * <ol>
	 * <li>If the subscription timestamp is changed, refresh the entire table
	 * <li>If the subscription timestamp is not changed, and the last changed
	 * timestamp is later than the timestamp in memory, then the backend will
	 * return the modified rows. <eol>
	 * 
	 * @param avAdministrationCacheTableData
	 *            Vector
	 * @param aaAdministrationCacheTableCache
	 *            AdministrationCacheTableCache
	 * @return boolean
	 * @throws RTSException
	 */
	private static boolean dispatchAdminTableRefreshTask(
			Vector avAdminCacheTableData,
			AdministrationCacheTableCache aaAdminCacheTableCache)
			throws RTSException
	{
		boolean lbRefreshAdminTable = false;
		for (int i = 0; i < avAdminCacheTableData.size(); i++)
		{
			boolean lbRefreshAllData = false;
			boolean lbRefreshModifiedData = false;
			AdministrationCacheTableData laAdminCacheTableData = (AdministrationCacheTableData) avAdminCacheTableData
					.elementAt(i);
			String lsCurrentCacheObjName = laAdminCacheTableData
					.getCacheObjectName();
			AdministrationCacheTableData laCacheAdminCacheTableData = null;
			if (aaAdminCacheTableCache == null)
			{
				lbRefreshAllData = true;
				laAdminCacheTableData = new AdministrationCacheTableData();
				laAdminCacheTableData.setChngTimestmp(new RTSDate(1900,
						0, 1));
			}
			else
			{
				laCacheAdminCacheTableData = AdministrationCacheTableCache
						.getAdminCacheTbl(SystemProperty
								.getOfficeIssuanceNo(), SystemProperty
								.getSubStationId(),
								lsCurrentCacheObjName);

				// if ((laAdministrationCacheTableData == null)
				// ||
				// laAdministrationCacheTableData.getSubscriptionTimestmp().getTimestamp().before(
				// laAdministrationCacheTableData.getSubscriptionTimestmp().getTimestamp()))
				// {

				// defect 7771
				// In order to support the addition of new admin tables, need to
				// test for laCacheAdministrationCacheTableData == null
				if (laAdminCacheTableData == null
						|| laCacheAdminCacheTableData == null)
				// end defect 7771
				{
					// If the subscription timestamp is changed, refresh
					// the entire table
					lbRefreshAllData = true;
					if (laAdminCacheTableData == null)
					{
						laAdminCacheTableData = new AdministrationCacheTableData();
					}
					laAdminCacheTableData.setChngTimestmp(new RTSDate(
							1900, 0, 1));
				}
				else
				{
					// If the subscription timestamp is not changed, and
					// the last changed timestamp is later than the
					// timestamp in cache, then refresh the modified rows
					if (laCacheAdminCacheTableData.getChngTimestmp()
							.getTimestamp() == null
							|| laCacheAdminCacheTableData
									.getChngTimestmp().getTimestamp()
									.before(
											laAdminCacheTableData
													.getChngTimestmp()
													.getTimestamp()))
					{
						lbRefreshModifiedData = true;
					}
				}
			}
			try
			{
				// if refreshData is true, refresh on this cache obj
				if (lbRefreshAllData || lbRefreshModifiedData)
				{
					lbRefreshAdminTable = true;
					GeneralCache laGeneralCache = (GeneralCache) Class
							.forName(
									"com.txdot.isd.rts.services.cache."
											+ lsCurrentCacheObjName)
							.newInstance();

					// defect 10554
					RTSDate laCompareDate = lbRefreshAllData ? laAdminCacheTableData
							.getChngTimestmp()
							: laCacheAdminCacheTableData
									.getChngTimestmp();

					switch (laGeneralCache.getCacheFunctionId())
					{
					// defect 10186
					// Use boolean lbRefreshAllData vs. if/else

					case CacheConstant.ADMINISTRATION_CACHE_TABLE_CACHE:
						// This table will be refreshed after this call
						break;

					// ASSIGNED_WORKSTATION_IDS_CACHE
					case CacheConstant.ASSIGNED_WORKSTATION_IDS_CACHE:
					{
						refreshAssignedWorkstationIdsCache(
								laCompareDate, lbRefreshAllData);
						break;
					}

						// defect 10701
						// BATCH_REPORT_MANAGMENT_CACHE
					case CacheConstant.BATCH_REPORT_MANAGEMENT_CACHE:
					{
						refreshBatchReportManagementCache(
								laCompareDate, lbRefreshAllData);
						break;
					}
						// end defect 10701

						// CREDIT_CARD_FEES_CACHE
					case CacheConstant.CREDIT_CARD_FEES_CACHE:
					{
						refreshCreditCardFeesCache(laCompareDate,
								lbRefreshAllData);
						break;
					}

						// DEALERS_CACHE
					case CacheConstant.DEALERS_CACHE:
					{
						refreshDealersCache(laCompareDate,
								lbRefreshAllData);
						break;
					}

						// LIENHOLDERS_CACHE
					case CacheConstant.LIENHOLDERS_CACHE:
					{
						refreshLienHoldersCache(laCompareDate,
								lbRefreshAllData);
						break;
					}

						// MISCELLANEOUS_CACHE
					case CacheConstant.MISCELLANEOUS_CACHE:
					{
						refreshMiscellaneousCache(laCompareDate,
								lbRefreshAllData);
						break;
					}

						// RSPS_WS_STATUS
					case CacheConstant.RSPS_WS_STATUS:
						refreshRSPSWsStsCache();
						break;

					// SECURITY_CACHE
					case CacheConstant.SECURITY_CACHE:
					{
						refreshSecurityCache();
						break;
					}

						// SUBCONTRACTOR_CACHE
					case CacheConstant.SUBCONTRACTOR_CACHE:
					{
						refreshSubcontractorCache(laCompareDate,
								lbRefreshAllData);
						break;
					}

						// SUBSTATION_CACHE
					case CacheConstant.SUBSTATION_CACHE:
					{
						refreshSubstationCache(laCompareDate,
								lbRefreshAllData);
						break;
					}
						// end defect 10554

						// SUBSTATION_SUBSCRIPTION_CACHE
					case CacheConstant.SUBSTATION_SUBSCRIPTION_CACHE:
					{
						refreshSubstationSubscriptionCache();
						break;
					}
						// end defect 10186

					default:
						throw new RTSException(
								RTSException.JAVA_ERROR,
								new Exception(
										"Trying to refresh admin cache code."
												+ "  Cache Code: "
												+ laGeneralCache
														.getCacheFunctionId()
												+ " does not exist"));
					}
				}
			}
			catch (Exception aeEx)
			{
				Log.write(Log.APPLICATION, CacheManager.class, aeEx
						.getMessage());
				RTSException leRTSException = new RTSException(
						RTSException.JAVA_ERROR, aeEx);
				throw leRTSException;
			}
		}
		return lbRefreshAdminTable;
	}

	/**
	 * Inspect the change timestamp and if the one stored locally is earlier
	 * than the one returned from the server, then refresh the entire table
	 * returns true if StaticTableCache needs to be updated
	 * 
	 * @param avStaticCacheTableData
	 *            Vector
	 * @param aaStaticCacheTableCache
	 *            StaticCacheTableCache
	 * @return boolean
	 * @throws RTSException
	 */
	private static boolean dispatchStaticTableRefreshTask(
			Vector avStaticCacheTableData,
			StaticCacheTableCache aaStaticCacheTableCache)
			throws RTSException
	{
		boolean lbRefreshStaticTable = false;
		for (int i = 0; i < avStaticCacheTableData.size(); i++)
		{
			boolean lbRefreshData = false;
			StaticCacheTableData laStaticCacheTableData = (StaticCacheTableData) avStaticCacheTableData
					.elementAt(i);
			String lsCurrentCacheObjName = laStaticCacheTableData
					.getCacheObjectName();

			// bypass DMVTRMessageCache since it is never saved
			if (lsCurrentCacheObjName.equals("DMVTRMessageCache"))
			{
				continue;
			}
			// defect 9919
			else if (lsCurrentCacheObjName.equals("TxDOTHolidayCache"))
			{
				continue;
			}
			// end defect 9919
			else
			{
				try
				{
					// if refreshData is true, refresh on this cache obj
					if (aaStaticCacheTableCache == null)
					{
						lbRefreshData = true;
					}
					else
					{
						StaticCacheTableData laStaticCacheTableData2 = StaticCacheTableCache
								.getStaticCacheTbl(lsCurrentCacheObjName);
						if ((laStaticCacheTableData2 == null)
								|| laStaticCacheTableData2
										.getChngTimestmp()
										.getTimestamp() == null
								|| laStaticCacheTableData2
										.getChngTimestmp()
										.getTimestamp()
										.before(
												laStaticCacheTableData
														.getChngTimestmp()
														.getTimestamp())
								|| shtCacheTables
										.get(getSerializedClassName(Class
												.forName("com.txdot.isd.rts.services.cache."
														+ lsCurrentCacheObjName))) == null)
						{
							lbRefreshData = true;
						}
					}
					if (lbRefreshData)
					{
						lbRefreshStaticTable = true;
						GeneralCache laGeneralCache2 = (GeneralCache) Class
								.forName(
										"com.txdot.isd.rts.services.cache."
												+ lsCurrentCacheObjName)
								.newInstance();
						switch (laGeneralCache2.getCacheFunctionId())
						{
						case CacheConstant.ACCOUNT_CODES_CACHE:
							refreshAccountCodeCache();
							break;

						// defect 10726
						case CacheConstant.BUSINESS_PARTNER_CACHE:
							refreshBusinessPartnerCache();
							break;
						// end defect 10726

						// defect 9969
						case CacheConstant.CERTFD_LIENHLDR_CACHE:
							refreshCertifiedLienholderCache();
							break;
						// end defect 9969
						// defect 8218
						case CacheConstant.CLASS_TO_PLATE_CACHE:
							refreshClassToPlateCache();
							break;
						case CacheConstant.CLASS_TO_PLATE_DESCRIPTION_CACHE:
							refreshClassToPlateDescriptionCache();
							break;
						// end defect 8218
						case CacheConstant.COUNTY_CALENDAR_YEAR_CACHE:
							refreshCountyCalendarYearCache();
							break;
						case CacheConstant.COMMON_FEES_CACHE:
							refreshCommonFeesCache();
							break;
						case CacheConstant.COMMERCIAL_VEHICLE_WEIGHTS_CACHE:
							refreshCommercialVehicleWeightsCache();
							break;
						case CacheConstant.DELETE_REASONS_CACHE:
							refreshDeleteReasonsCache();
							break;

						// defect 9831
						case CacheConstant.DSABLD_PLCRD_DEL_REASN_CACHE:
							refreshDisabledPlacardDeleteReasonCache();
							break;
						case CacheConstant.DSABLD_PLCRD_CUST_ID_TYPE_CACHE:
							refreshDisabledPlacardCustomerIdTypeCache();
							break;
						// end defect 9831

						case CacheConstant.DMVTR_MESSAGE_CACHE:
							// refreshDMVTRMessageCache();
							break;
						case CacheConstant.DOCUMENT_TYPES_CACHE:
							refreshDocumentTypesCache();
							break;
						case CacheConstant.ERROR_MESSAGES_CACHE:
							refreshErrorMessagesCache();
							break;
						// defect 6949
						case CacheConstant.FUNDS_CODES_CACHE:
							refreshFundsCodesCache();
							break;
						// end defect 6949
						// defect 9919
						case CacheConstant.HOLIDAY_CACHE:
							refreshHolidayCache();
							break;
						// end defect 9919
						case CacheConstant.INDICATOR_DESCRIPTIONS_CACHE:
							refreshIndicatorDescriptionsCache();
							break;
						case CacheConstant.INDICATOR_STOP_CODES_CACHE:
							refreshIndicatorStopCodesCache();
							break;
						case CacheConstant.INVALID_LETTER_CACHE:
							refreshInvalidLetterCache();
							break;
						case CacheConstant.INVENTORY_PATTERNS_CACHE:
							refreshInventoryPatternsCache();
							break;
						case CacheConstant.ITEM_CODES_CACHE:
							refreshItemCodesCache();
							break;
						case CacheConstant.OFFICE_CODES_CACHE:
							refreshOfficeCodesCache();
							break;
						case CacheConstant.OFFICE_IDS_CACHE:
							refreshOfficeIdsCache();
							break;
						case CacheConstant.OWNER_EVIDENCE_CODES_CACHE:
							refreshOwnershipEvidenceCodesCache();
							break;
						case CacheConstant.PASSENGER_FEES_CACHE:
							refreshPassengerFeesCache();
							break;
						case CacheConstant.PAYMENT_TYPE_CACHE:
							refreshPaymentTypeCache();
							break;
						case CacheConstant.PAYMENT_STATUS_CODES_CACHE:
							refreshPaymentStatusCodesCache();
							break;
						// defect 8218
						case CacheConstant.PLATE_TO_STICKER_CACHE:
							refreshPlateToStickerCache();
							break;
						// end defect 8218
						// defect 8104
						case CacheConstant.REGISTRATION_ADDITIONAL_FEE_CACHE:
							refreshRegistrationAdditionalFeeCache();
							break;
						// end defect 8104
						case CacheConstant.REGISTRATION_CLASS_CACHE:
							refreshRegistrationClassCache();
							break;
						case CacheConstant.REGISTRATION_WEIGHT_FEES_CACHE:
							refreshRegistrationWeightFeesCache();
							break;
						case CacheConstant.REPORTS_CACHE:
							refreshReportsCache();
							break;
						case CacheConstant.REPORT_CATEGORY_CACHE:
							refreshReportCategoryCache();
							break;
						case CacheConstant.SALES_TAX_CATEGORY_CACHE:
							refreshSalesTaxCategoryCache();
							break;
						case CacheConstant.TAX_EXEMPT_CODE_CACHE:
							refreshTaxExemptCodeCache();
							break;
						case CacheConstant.TITLE_TERP_FEE_CACHE:
							refreshTitleTERPFeeCache();
							break;
						case CacheConstant.TITLE_TERP_PERCENT_CACHE:
							refreshTitleTERPPercentCache();
							break;
						case CacheConstant.TTL_TRNSFR_ENT_CACHE:
							refreshTtlTrnsfrEntCache();
						case CacheConstant.TTL_TRNSFR_PNLTY_FEE_CACHE:
							refreshTtlTrnsfrPnltyFeeCache();
						case CacheConstant.TTL_TRNSFR_PNLTY_EXMPT_CD_CACHE:
							refreshTtlTrnsfrPnltyExmptCdCache();
						case CacheConstant.TRANSACTION_CODES_CACHE:
							refreshTransactionCodesCache();
							break;
						// defect 9919
						// case CacheConstant.TXDOT_HOLIDAY_CACHE :
						// refreshTxDOTHolidayCache();
						// break;
						// end defect 9919
						case CacheConstant.VEHICLE_BODY_TYPES_CACHE:
							refreshVehicleBodyTypesCache();
							break;
						// defect 10712
						case CacheConstant.VEHICLE_COLOR_CACHE:
							refreshVehicleColorCache();
							break;
						// end defect 10712
						case CacheConstant.VEHICLE_DIESEL_TON_CACHE:
							refreshVehicleDieselTonCache();
							break;
						case CacheConstant.VEHICLE_MAKES_CACHE:
							refreshVehicleMakesCache();
							break;
						case CacheConstant.VEHICLE_CLASS_REGISTRATION_CLASS_CACHE:
							refreshVehicleClassRegistrationClassCache();
							break;
						case CacheConstant.PRODUCT_SERVICE_CACHE:
							refreshProductServiceCache();
							break;

						// defect 10396
						case CacheConstant.POSTAL_STATE_CACHE:
							refreshPostalStateCache();
							break;
						// end defect 10396

						// defect 10427
						case CacheConstant.OFFICE_TIMEZONE_CACHE:
							refreshOfficeTimeZoneCache();
							break;
						// end defect 10427

						// defect 10695
						case CacheConstant.REGISTRATION_CLASS_FEE_GRP_CACHE:
							refreshRegistrationClassFeeGroupCache();
							break;
						// end defect 10695

						// defect 10366
						case CacheConstant.PLATE_SYMBOL_CACHE:
							refreshPlateSymbolCache();
							break;
						// end defect 10366

						case CacheConstant.PLT_TYPE_CACHE:
							refreshPlateTypeCache();
							break;
						case CacheConstant.PLT_GRP_ID_CACHE:
							refreshPlateGroupIdCache();
							break;
						case CacheConstant.PLT_SURCHARGE_CACHE:
							refreshPlateSurchargeCache();
							break;
						case CacheConstant.ORG_NO_CACHE:
							refreshOrganizationNumberCache();
							break;
						case CacheConstant.VEH_CLASS_SPCL_PLT_TYPE_DESC_CACHE:
							refreshVehicleClassSpclPltTypeDescCache();
						case CacheConstant.SPCL_PLT_FXD_EXP_MO:
							refreshSpclPltFxdExpMoCache();
						case CacheConstant.STATIC_CACHE_TABLE_CACHE:
							// do not do anything, it will be
							// refreshed after this method
							break;
						default:
							throw new RTSException(
									RTSException.JAVA_ERROR,
									new Exception(
											"Trying to refresh static cache code."
													+ "  Cache Code: "
													+ laGeneralCache2
															.getCacheFunctionId()
													+ " does not exist"));
						}
					}
				}
				catch (Exception aeEx)
				{
					Log.write(Log.APPLICATION, CacheManager.class, aeEx
							.getMessage());
					RTSException leRTSException = new RTSException(
							RTSException.JAVA_ERROR, aeEx);
					throw leRTSException;
				}
			}
		}
		return lbRefreshStaticTable;
	}

	/**
	 * Pass in the serialized file name and return the corresponding class
	 * 
	 * @param asSerializedFileName
	 *            String
	 * @return Class Name of serialized class
	 */
	public static Class getSerializedClass(String asSerializedFileName)
	{
		int liNameIndex = asSerializedFileName.indexOf(".");
		int liIndex = svFile.indexOf(asSerializedFileName.substring(0,
				liNameIndex));
		if (liIndex == -1)
		{
			return null;
		}
		else
		{
			return (Class) svClass.elementAt(liIndex);
		}
	}

	/**
	 * Pass in the class and return the name of the serialized file
	 * 
	 * @param aaClassObj
	 *            Class
	 * @return String
	 */
	public static String getSerializedClassName(Class aaClassObj)
	{
		int liIndex = svClass.indexOf(aaClassObj);
		if (liIndex == -1)
		{
			return null;
		}
		else
		{
			return (String) svFile.elementAt(liIndex) + ".ser";
		}
	}

	/**
	 * Load cache from the serialized files. If the system is a Comm server,then
	 * refresh from database server
	 * 
	 * @throws RTSException
	 */
	public static void loadCache() throws RTSException
	{
		GeneralCache laGeneralCache = null;

		// Static and Dynamic Cache

		// Admin
		// AdministrationCacheTableCache
		laGeneralCache = readFromDisk(getSerializedClassName(AdministrationCacheTableCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(AdministrationCacheTableCache.class),
							laGeneralCache);
		}

		// AssignedWorkstationIdsCache
		laGeneralCache = readFromDisk(getSerializedClassName(AssignedWorkstationIdsCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(AssignedWorkstationIdsCache.class),
							laGeneralCache);
		}

		// defect 10701
		// BatchReportManagementCache
		laGeneralCache = readFromDisk(getSerializedClassName(BatchReportManagementCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(BatchReportManagementCache.class),
							laGeneralCache);
		}
		// end defect 10701

		// defect 10726
		// BusinessPartnerCache
		laGeneralCache = readFromDisk(getSerializedClassName(BusinessPartnerCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(BusinessPartnerCache.class),
					laGeneralCache);
		}
		// end defect 10726

		// CreditCardFeesCache
		laGeneralCache = readFromDisk(getSerializedClassName(CreditCardFeesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(CreditCardFeesCache.class),
					laGeneralCache);
		}
		// DealersCache
		laGeneralCache = readFromDisk(getSerializedClassName(DealersCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(DealersCache.class),
					laGeneralCache);
		}

		// LienholdersCache
		laGeneralCache = readFromDisk(getSerializedClassName(LienholdersCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(LienholdersCache.class),
					laGeneralCache);
		}

		// MiscellaneousCache
		laGeneralCache = readFromDisk(getSerializedClassName(MiscellaneousCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(MiscellaneousCache.class),
					laGeneralCache);
		}

		// RSPSWsStatusCache
		// defect 7135
		laGeneralCache = readFromDisk(getSerializedClassName(RSPSWsStatusCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(RSPSWsStatusCache.class),
					laGeneralCache);
		}
		// end defect 7135

		// SecurityCache
		laGeneralCache = readFromDisk(getSerializedClassName(SecurityCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(SecurityCache.class),
					laGeneralCache);
		}
		// SubcontractorCache
		laGeneralCache = readFromDisk(getSerializedClassName(SubcontractorCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(SubcontractorCache.class),
					laGeneralCache);
		}
		// SubstationCache
		laGeneralCache = readFromDisk(getSerializedClassName(SubstationCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(SubstationCache.class),
					laGeneralCache);
		}
		// SubstationSubscriptionCache
		laGeneralCache = readFromDisk(getSerializedClassName(SubstationSubscriptionCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(SubstationSubscriptionCache.class),
							laGeneralCache);
		}

		// Static cache
		// StaticCacheTableCache
		laGeneralCache = readFromDisk(getSerializedClassName(StaticCacheTableCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(StaticCacheTableCache.class),
							laGeneralCache);
		}
		// AccountCodesCache
		laGeneralCache = readFromDisk(getSerializedClassName(AccountCodesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(AccountCodesCache.class),
					laGeneralCache);
		}
		// defect 10726
		laGeneralCache = readFromDisk(getSerializedClassName(BusinessPartnerCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(BusinessPartnerCache.class),
					laGeneralCache);
		}
		// end defect 10726

		// CertifiedLienholderCache
		// defect 9969
		laGeneralCache = readFromDisk(getSerializedClassName(CertifiedLienholderCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(CertifiedLienholderCache.class),
							laGeneralCache);
		}
		// end defect 9969

		// ClassToPlateCache
		laGeneralCache = readFromDisk(getSerializedClassName(ClassToPlateCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(ClassToPlateCache.class),
					laGeneralCache);
		}

		// ClassToPlateDescriptionCache
		laGeneralCache = readFromDisk(getSerializedClassName(ClassToPlateDescriptionCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(ClassToPlateDescriptionCache.class),
							laGeneralCache);
		}

		// CommonFeesCache
		laGeneralCache = readFromDisk(getSerializedClassName(CommonFeesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(CommonFeesCache.class),
					laGeneralCache);
		}

		// CommercialVehicleWeightsCache
		laGeneralCache = readFromDisk(getSerializedClassName(CommercialVehicleWeightsCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(CommercialVehicleWeightsCache.class),
							laGeneralCache);
		}

		// CountyCalendarYearCache
		laGeneralCache = readFromDisk(getSerializedClassName(CountyCalendarYearCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(CountyCalendarYearCache.class),
							laGeneralCache);
		}

		// DeleteReasonsCache
		laGeneralCache = readFromDisk(getSerializedClassName(DeleteReasonsCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(DeleteReasonsCache.class),
					laGeneralCache);
		}

		// defect 9831
		// DisabledPlacardDeleteReasonCache
		laGeneralCache = readFromDisk(getSerializedClassName(DisabledPlacardDeleteReasonCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(DisabledPlacardDeleteReasonCache.class),
							laGeneralCache);
		}

		// DisabledPlacardCustomerIdTypeCache
		laGeneralCache = readFromDisk(getSerializedClassName(DisabledPlacardCustomerIdTypeCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(DisabledPlacardCustomerIdTypeCache.class),
							laGeneralCache);
		}
		// end defect 9831

		// DocumentTypesCache
		laGeneralCache = readFromDisk(getSerializedClassName(DocumentTypesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(DocumentTypesCache.class),
					laGeneralCache);
		}

		// ErrorMessagesCache
		laGeneralCache = readFromDisk(getSerializedClassName(ErrorMessagesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(ErrorMessagesCache.class),
					laGeneralCache);
		}

		// defect 9919
		// HolidayCache
		laGeneralCache = readFromDisk(getSerializedClassName(HolidayCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(HolidayCache.class),
					laGeneralCache);
		}
		// end defect 9919

		// IndicatorDescriptionsCache
		laGeneralCache = readFromDisk(getSerializedClassName(IndicatorDescriptionsCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(IndicatorDescriptionsCache.class),
							laGeneralCache);
		}

		// IndicatorStopCodesCache
		laGeneralCache = readFromDisk(getSerializedClassName(IndicatorStopCodesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(IndicatorStopCodesCache.class),
							laGeneralCache);
		}

		// InvalidLetterCache
		laGeneralCache = readFromDisk(getSerializedClassName(InvalidLetterCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(InvalidLetterCache.class),
					laGeneralCache);
		}

		// InventoryPatternsCache
		laGeneralCache = readFromDisk(getSerializedClassName(InventoryPatternsCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(InventoryPatternsCache.class),
							laGeneralCache);
		}

		// ItemCodesCache
		laGeneralCache = readFromDisk(getSerializedClassName(ItemCodesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(ItemCodesCache.class),
					laGeneralCache);
		}

		// OfficeCodesCache
		laGeneralCache = readFromDisk(getSerializedClassName(OfficeCodesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(OfficeCodesCache.class),
					laGeneralCache);
		}

		// OfficeIdsCache
		laGeneralCache = readFromDisk(getSerializedClassName(OfficeIdsCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(OfficeIdsCache.class),
					laGeneralCache);
		}

		// OfficeTimeZoneCache
		laGeneralCache = readFromDisk(getSerializedClassName(OfficeTimeZoneCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(OfficeTimeZoneCache.class),
					laGeneralCache);
		}

		// OrganizationNumberCache
		laGeneralCache = readFromDisk(getSerializedClassName(OrganizationNumberCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(OrganizationNumberCache.class),
							laGeneralCache);
		}

		// OwnershipEvidenceCodesCache
		laGeneralCache = readFromDisk(getSerializedClassName(OwnershipEvidenceCodesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(OwnershipEvidenceCodesCache.class),
							laGeneralCache);
		}

		// PassengerFeesCache
		laGeneralCache = readFromDisk(getSerializedClassName(PassengerFeesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(PassengerFeesCache.class),
					laGeneralCache);
		}

		// PaymentTypeCache
		laGeneralCache = readFromDisk(getSerializedClassName(PaymentTypeCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(PaymentTypeCache.class),
					laGeneralCache);
		}

		// PaymentStatusCodesCache
		laGeneralCache = readFromDisk(getSerializedClassName(PaymentStatusCodesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(PaymentStatusCodesCache.class),
							laGeneralCache);
		}
		// PlateGroupIdCache
		// defect 9085
		laGeneralCache = readFromDisk(getSerializedClassName(PlateGroupIdCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(PlateGroupIdCache.class),
					laGeneralCache);
		}

		// defect 10366
		// PlateSymbolCache
		laGeneralCache = readFromDisk(getSerializedClassName(PlateSymbolCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(PlateSymbolCache.class),
					laGeneralCache);
		}
		// end defect 10366

		// PlateToStickerCache
		laGeneralCache = readFromDisk(getSerializedClassName(PlateToStickerCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(PlateToStickerCache.class),
					laGeneralCache);
		}

		// PlateSurchargeCache
		laGeneralCache = readFromDisk(getSerializedClassName(PlateSurchargeCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(PlateSurchargeCache.class),
					laGeneralCache);
		}

		// PlateTypeCache
		laGeneralCache = readFromDisk(getSerializedClassName(PlateTypeCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(PlateTypeCache.class),
					laGeneralCache);
		}

		// defect 10396
		// PostalStateCache
		laGeneralCache = readFromDisk(getSerializedClassName(PostalStateCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(PostalStateCache.class),
					laGeneralCache);
		}
		// end defect 10396

		// ProductServiceCache
		laGeneralCache = readFromDisk(getSerializedClassName(ProductServiceCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(ProductServiceCache.class),
					laGeneralCache);

		}

		// RegistrationAdditionalFeeCache
		laGeneralCache = readFromDisk(getSerializedClassName(RegistrationAdditionalFeeCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(RegistrationAdditionalFeeCache.class),
							laGeneralCache);
		}

		// RegistrationClassCache
		laGeneralCache = readFromDisk(getSerializedClassName(RegistrationClassCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(RegistrationClassCache.class),
							laGeneralCache);
		}

		// defect 10695
		// RegistrationClassFeeGroupCache
		laGeneralCache = readFromDisk(getSerializedClassName(RegistrationClassFeeGroupCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(RegistrationClassFeeGroupCache.class),
							laGeneralCache);
		}
		// end defect 10695

		// RegistrationWeightFeesCache
		laGeneralCache = readFromDisk(getSerializedClassName(RegistrationWeightFeesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(RegistrationWeightFeesCache.class),
							laGeneralCache);
		}
		// ReportsCache
		laGeneralCache = readFromDisk(getSerializedClassName(ReportsCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(ReportsCache.class),
					laGeneralCache);
		}
		// ReportCategoryCache
		laGeneralCache = readFromDisk(getSerializedClassName(ReportCategoryCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(ReportCategoryCache.class),
					laGeneralCache);
		}

		// SalesTaxCategoryCache
		laGeneralCache = readFromDisk(getSerializedClassName(SalesTaxCategoryCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(SalesTaxCategoryCache.class),
							laGeneralCache);
		}
		// SpecialPlatesFixedExpirationMonthCache
		laGeneralCache = readFromDisk(getSerializedClassName(SpecialPlateFixedExpirationMonthCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(SpecialPlateFixedExpirationMonthCache.class),
							laGeneralCache);
		}
		// TaxExemptCodeCache
		laGeneralCache = readFromDisk(getSerializedClassName(TaxExemptCodeCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(TaxExemptCodeCache.class),
					laGeneralCache);
		}
		// TransactionCodesCache
		laGeneralCache = readFromDisk(getSerializedClassName(TransactionCodesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(TransactionCodesCache.class),
							laGeneralCache);
		}
		// TitleTERPFeeCache
		laGeneralCache = readFromDisk(getSerializedClassName(TitleTERPFeeCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(TitleTERPFeeCache.class),
					laGeneralCache);
		}

		// TitleTERPPercentCache
		laGeneralCache = readFromDisk(getSerializedClassName(TitleTERPPercentCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(TitleTERPPercentCache.class),
							laGeneralCache);
		}

		// defect 9919
		// TxDOTHolidayCache
		// laGeneralCache =
		// readFromDisk(
		// getSerializedClassName(TxDOTHolidayCache.class));
		// if (laGeneralCache != null)
		// {
		// shtCacheTables.put(
		// getSerializedClassName(TxDOTHolidayCache.class),
		// laGeneralCache);
		// }
		// end defect 9919

		// VehicleBodyTypesCache
		laGeneralCache = readFromDisk(getSerializedClassName(VehicleBodyTypesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(VehicleBodyTypesCache.class),
							laGeneralCache);
		}

		// VehicleDieselTonCache
		laGeneralCache = readFromDisk(getSerializedClassName(VehicleDieselTonCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(VehicleDieselTonCache.class),
							laGeneralCache);
		}

		// VehicleMakesCache
		laGeneralCache = readFromDisk(getSerializedClassName(VehicleMakesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(VehicleMakesCache.class),
					laGeneralCache);
		}
		// FundsCodesCache
		laGeneralCache = readFromDisk(getSerializedClassName(FundsCodesCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(FundsCodesCache.class),
					laGeneralCache);
		}

		// TitleTransferEntityCache
		laGeneralCache = readFromDisk(getSerializedClassName(TitleTransferEntityCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(TitleTransferEntityCache.class),
							laGeneralCache);
		}

		// TitleTransferPenaltyExemptCode
		laGeneralCache = readFromDisk(getSerializedClassName(TitleTransferPenaltyExemptCodeCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(TitleTransferPenaltyExemptCodeCache.class),
							laGeneralCache);
		}

		// TitleTransferPenaltyFee
		laGeneralCache = readFromDisk(getSerializedClassName(TitleTransferPenaltyFeeCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(TitleTransferPenaltyFeeCache.class),
							laGeneralCache);
		}

		// VehicleClassSpclPltTypeDescCache
		laGeneralCache = readFromDisk(getSerializedClassName(VehicleClassSpclPltTypeDescCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(VehicleClassSpclPltTypeDescCache.class),
							laGeneralCache);
		}

		// defect 10712
		// VehicleColorCache
		laGeneralCache = readFromDisk(getSerializedClassName(VehicleColorCache.class));

		if (laGeneralCache != null)
		{
			shtCacheTables.put(
					getSerializedClassName(VehicleColorCache.class),
					laGeneralCache);
		}
		// end defect 10712

		// VehicleClassRegistrationClassCache
		// RTS_REGIS_CLASS A, RTS_COMMON_FEES B
		// WHERE A.REGCLASSCD = B.REGCLASSCD
		laGeneralCache = readFromDisk(getSerializedClassName(VehicleClassRegistrationClassCache.class));
		if (laGeneralCache != null)
		{
			shtCacheTables
					.put(
							getSerializedClassName(VehicleClassRegistrationClassCache.class),
							laGeneralCache);
		}

		// if this is a comm server, then go to the RTS server to get the
		// changes
		if (SystemProperty.isCommServer())
		{
			// If temp cache directory does not exist create it.
			File laFile = new File(SystemProperty.getCacheDirectory()
					+ TEMPDIR);
			if (!laFile.exists())
			{
				laFile.mkdir();
			}
			try
			{
				refreshCache();
			}
			catch (RTSException aeRTSException)
			{
				if (aeRTSException.getMsgType().equals(
						RTSException.SERVER_DOWN))
				{
					Log.write(Log.APPLICATION, CacheManager.class,
							"Server is down.  Cannot refresh cache.");
				}
				else if (aeRTSException.getMsgType().equals(
						RTSException.DB_DOWN))
				{
					Log.write(Log.APPLICATION, CacheManager.class,
							"DB is down.  Cannot refresh cache.");
				}
				else
				{
					throw aeRTSException;
				}
			}
		}
	}

	/**
	 * Test workings of this class.
	 * 
	 * @param aarrArgs
	 *            String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			CacheManager.loadCache();
			// defect 9759
			// Not required
			// CacheManager.refreshAssignedWorkstationIdsCache(
			// new RTSDate(1900, 1, 1),
			// false);
			// manager.refreshCashWorkstationIdsCache(new RTSDate(1900, 1, 1),
			// true);
			// manager.refreshDealersCache(new RTSDate(1900, 1, 1), true);
			// manager.refreshLienHoldersCache(new RTSDate(1900, 1, 1), true);
			// manager.refreshMiscellaneousCache(new RTSDate(1900, 1, 1), true);
			// manager.refreshSubcontractorCache(new RTSDate(1900, 1, 1), true);
			// manager.refreshSecurityCache(new RTSDate(1900, 1, 1), true);
			// manager.refreshSubstationCache(new RTSDate(1900, 1, 1), true);
			// manager.refreshSubstationSubscriptionCache(new RTSDate(1900, 1,
			// 1), true);
			// CacheManager.persistCache();
			// end defect 9759
		}
		catch (RTSException aeRTSException)
		{
			aeRTSException.printStackTrace();
		}
	}

	// /**
	// * Serialize cache in memory to file
	// *
	// * @throws RTSException
	// */
	// public static void persistCache() throws RTSException
	// {
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// AdministrationCacheTableCache.class)),
	// getSerializedClassName(
	// AdministrationCacheTableCache.class));
	//
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(StaticCacheTableCache.class)),
	// getSerializedClassName(StaticCacheTableCache.class));
	//
	// // Admin cache
	// // AssignedWorkstationIdsCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// AssignedWorkstationIdsCache.class)),
	// getSerializedClassName(AssignedWorkstationIdsCache.class));
	//
	// // defect 8623
	// // CashWorkstationIdsCache is not used
	// // writeToDisk(
	// // (GeneralCache) shtCacheTables.get(
	// // getSerializedClassName(CashWorkstationIdsCache.class)),
	// // getSerializedClassName(CashWorkstationIdsCache.class));
	// // end defect 8623
	//
	// // DealersCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(DealersCache.class)),
	// getSerializedClassName(DealersCache.class));
	//
	// // LienholdersCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(LienholdersCache.class)),
	// getSerializedClassName(LienholdersCache.class));
	//
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(MiscellaneousCache.class)),
	// getSerializedClassName(MiscellaneousCache.class));
	//
	// // RSPSWsStatusCache
	// // defect 7135
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(RSPSWsStatusCache.class)),
	// getSerializedClassName(RSPSWsStatusCache.class));
	// // end defect 7135
	//
	// // SecurityCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(SecurityCache.class)),
	// getSerializedClassName(SecurityCache.class));
	//
	// // SubcontractorCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(SubcontractorCache.class)),
	// getSerializedClassName(SubcontractorCache.class));
	//
	// // SubstationCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(SubstationCache.class)),
	// getSerializedClassName(SubstationCache.class));
	//
	// // SubstationSubscriptionCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// SubstationSubscriptionCache.class)),
	// getSerializedClassName(SubstationSubscriptionCache.class));
	//
	// // Static Cache
	// // AccountCodesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(AccountCodesCache.class)),
	// getSerializedClassName(AccountCodesCache.class));
	//
	// // ClassToPlateCache
	// // defect 8218
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(ClassToPlateCache.class)),
	// getSerializedClassName(ClassToPlateCache.class));
	//
	// // ClassToPlateDescriptionCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// ClassToPlateDescriptionCache.class)),
	// getSerializedClassName(ClassToPlateDescriptionCache.class));
	// // end defect 8218
	//
	// // CountyCalendarYearCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(CountyCalendarYearCache.class)),
	// getSerializedClassName(CountyCalendarYearCache.class));
	//
	// // CommonFeesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(CommonFeesCache.class)),
	// getSerializedClassName(CommonFeesCache.class));
	//
	// // CommercialVehicleWeightsCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// CommercialVehicleWeightsCache.class)),
	// getSerializedClassName(
	// CommercialVehicleWeightsCache.class));
	//
	// // DeleteReasonsCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(DeleteReasonsCache.class)),
	// getSerializedClassName(DeleteReasonsCache.class));
	// //writeToDisk(
	// // shCacheTables.get(getSerializedClassName(DMVTRMessageCache.class)),
	// //getSerializedClassName(DMVTRMessageCache.class));
	//
	// // DocumentTypesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(DocumentTypesCache.class)),
	// getSerializedClassName(DocumentTypesCache.class));
	//
	// // ErrorMessagesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(ErrorMessagesCache.class)),
	// getSerializedClassName(ErrorMessagesCache.class));
	//
	// // IndicatorDescriptionsCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// IndicatorDescriptionsCache.class)),
	// getSerializedClassName(IndicatorDescriptionsCache.class));
	//
	// // IndicatorStopCodesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(IndicatorStopCodesCache.class)),
	// getSerializedClassName(IndicatorStopCodesCache.class));
	//
	// // InvalidLetterCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(InvalidLetterCache.class)),
	// getSerializedClassName(InvalidLetterCache.class));
	//
	// // InventoryPatternsCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(InventoryPatternsCache.class)),
	// getSerializedClassName(InventoryPatternsCache.class));
	//
	// // ItemCodesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(ItemCodesCache.class)),
	// getSerializedClassName(ItemCodesCache.class));
	//
	// // OfficeCodesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(OfficeCodesCache.class)),
	// getSerializedClassName(OfficeCodesCache.class));
	//
	// // OfficeIdsCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(OfficeIdsCache.class)),
	// getSerializedClassName(OfficeIdsCache.class));
	//
	// // OwnershipEvidenceCodesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// OwnershipEvidenceCodesCache.class)),
	// getSerializedClassName(OwnershipEvidenceCodesCache.class));
	//
	// // PassengerFeesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(PassengerFeesCache.class)),
	// getSerializedClassName(PassengerFeesCache.class));
	//
	// // PaymentTypeCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(PaymentTypeCache.class)),
	// getSerializedClassName(PaymentTypeCache.class));
	//
	// // PaymentStatusCodesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(PaymentStatusCodesCache.class)),
	// getSerializedClassName(PaymentStatusCodesCache.class));
	//
	// // PlateToStickerCache
	// // defect 8218
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(PlateToStickerCache.class)),
	// getSerializedClassName(PlateToStickerCache.class));
	// // end defect 8218
	//
	// // RegistrationAdditionalFeeCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// RegistrationAdditionalFeeCache.class)),
	// getSerializedClassName(
	// RegistrationAdditionalFeeCache.class));
	//
	// // RegistrationClassCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(RegistrationClassCache.class)),
	// getSerializedClassName(RegistrationClassCache.class));
	//
	// // RegistrationWeightFeesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// RegistrationWeightFeesCache.class)),
	// getSerializedClassName(RegistrationWeightFeesCache.class));
	//
	// // ReportsCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(ReportsCache.class)),
	// getSerializedClassName(ReportsCache.class));
	//
	// // ReportCategoryCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(ReportCategoryCache.class)),
	// getSerializedClassName(ReportCategoryCache.class));
	//
	// // SalesTaxCategoryCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(SalesTaxCategoryCache.class)),
	// getSerializedClassName(SalesTaxCategoryCache.class));
	//
	// // TaxExemptCodeCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(TaxExemptCodeCache.class)),
	// getSerializedClassName(TaxExemptCodeCache.class));
	//
	// // TransactionCodesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(TransactionCodesCache.class)),
	// getSerializedClassName(TransactionCodesCache.class));
	//
	// // TitleTERPFeeCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(TitleTERPFeeCache.class)),
	// getSerializedClassName(TitleTERPFeeCache.class));
	//
	// // TitleTERPPercentCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(TitleTERPPercentCache.class)),
	// getSerializedClassName(TitleTERPPercentCache.class));
	//
	// // VehicleBodyTypesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(VehicleBodyTypesCache.class)),
	// getSerializedClassName(VehicleBodyTypesCache.class));
	//
	// // VehicleClassRegistrationClassCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// VehicleClassRegistrationClassCache.class)),
	// getSerializedClassName(
	// VehicleClassRegistrationClassCache.class));
	//
	// // VehicleDieselTonCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(VehicleDieselTonCache.class)),
	// getSerializedClassName(VehicleDieselTonCache.class));
	//
	// // VehicleMakesCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(VehicleMakesCache.class)),
	// getSerializedClassName(VehicleMakesCache.class));
	//
	// // defect 9085
	// // PlateTypeCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(PlateTypeCache.class)),
	// getSerializedClassName(PlateTypeCache.class));
	//
	// // PlateGroupIdCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(PlateGroupIdCache.class)),
	// getSerializedClassName(PlateGroupIdCache.class));
	//
	// // PlateSurchargeCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(PlateSurchargeCache.class)),
	// getSerializedClassName(PlateSurchargeCache.class));
	//
	// // OrganizationNumberCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(OrganizationNumberCache.class)),
	// getSerializedClassName(OrganizationNumberCache.class));
	//
	// // VehicleClassSpclPltTypeDescCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// VehicleClassSpclPltTypeDescCache.class)),
	// getSerializedClassName(
	// VehicleClassSpclPltTypeDescCache.class));
	//
	// // TxDOTHolidayCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(TxDOTHolidayCache.class)),
	// getSerializedClassName(TxDOTHolidayCache.class));
	//
	// // SpecialPlateFixedExpirationMonthCache
	// writeToDisk(
	// (GeneralCache) shtCacheTables.get(
	// getSerializedClassName(
	// SpecialPlateFixedExpirationMonthCache.class)),
	// getSerializedClassName(
	// SpecialPlateFixedExpirationMonthCache.class));
	// // end defect 9085
	//
	// }
	// end defect 9759

	/**
	 * This method creates the text files from cache files stored in cache
	 * directory.
	 */
	public static void PrintCache()
	{
		try
		{
			// load cache
			loadCache();
			String lsCacheDirectory = SystemProperty
					.getCacheDirectory();
			PrintWriter laPrintWriter = null;
			Method[] larrMethods = null;
			for (Enumeration e = shtCacheTables.elements(); e
					.hasMoreElements();)
			{
				GeneralCache laGeneralCache = (GeneralCache) e
						.nextElement();
				Hashtable lhtGenCacheHashTbl = laGeneralCache
						.getHashtable();
				int liIndi = 0;
				String lsFileName = getSerializedClassName(laGeneralCache
						.getClass());
				lsFileName = lsFileName.substring(0, lsFileName
						.indexOf("."));
				lsFileName = lsCacheDirectory + "/" + lsFileName
						+ ".txt";
				// KPH
				System.out.println("***" + lsFileName);
				// END KPH
				for (Enumeration e1 = lhtGenCacheHashTbl.elements(); e1
						.hasMoreElements();)
				{
					Vector lvCacheVector = new Vector();
					Object laObject = e1.nextElement();
					// Acct item code cache has vector in hashtable
					// so vector is used.
					if (!(laObject instanceof Vector))
					{
						lvCacheVector.add(laObject);
					}
					else
					{
						lvCacheVector = (Vector) laObject;
					}
					for (int x = 0; x < lvCacheVector.size(); x++)
					{
						laObject = lvCacheVector.elementAt(x);
						if (liIndi == 0)
						{
							Class laClass = laObject.getClass();
							FileOutputStream apfsFileOutputStream = new FileOutputStream(
									lsFileName, true);
							laPrintWriter = new PrintWriter(
									apfsFileOutputStream);
							liIndi = 1;
							larrMethods = laClass.getMethods();
							for (int k = 0; k < larrMethods.length; k++)
							{
								String lsMethodName = larrMethods[k]
										.getName();
								if ((lsMethodName.startsWith("get") || lsMethodName
										.startsWith("is"))
										&& (!lsMethodName
												.equals("getCacheFunctionId"))
										&& (!lsMethodName
												.equals("getLocAuthCd"))
										&& (!lsMethodName
												.equals("getHashTable"))
										&& (!lsMethodName
												.equals("getClass")))
								{
									laPrintWriter.print(larrMethods[k]
											.getName()
											+ "\t");
								}
							}
							laPrintWriter.print("\n");
						}
						liIndi = 1;
						for (int j = 0; j < larrMethods.length; j++)
						{
							String lsMethodName = larrMethods[j]
									.getName();
							if (lsMethodName.startsWith("get")
									&& (!lsMethodName
											.equals("getCacheFunctionId"))
									&& (!lsMethodName
											.equals("getHashTable"))
									&& (!lsMethodName
											.equals("getLocAuthCd"))
									&& (!lsMethodName
											.equals("getClass")))
							{
								laPrintWriter.print(larrMethods[j]
										.invoke(laObject, null)
										+ "\t");
							}
						}
						laPrintWriter.print("\n");
					}
				}
				laPrintWriter.flush();
				laPrintWriter.close();
			}
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Reads the serialized file and return the deserialized object
	 * 
	 * @param asSerializedFileName
	 *            String
	 * @return GeneralCache
	 * @throws RTSException
	 */
	public static GeneralCache readFromDisk(String asSerializedFileName)
			throws RTSException
	{
		GeneralCache laReturnCache = null;
		try
		{
			File laFile = new File(SystemProperty.getCacheDirectory()
					+ asSerializedFileName);
			if (laFile.exists())
			{
				FileInputStream lpfsFileInputStream = new FileInputStream(
						SystemProperty.getCacheDirectory()
								+ asSerializedFileName);
				ObjectInputStream laObjectInputStream = new ObjectInputStream(
						lpfsFileInputStream);
				Hashtable lhtReturnHashtable = (Hashtable) laObjectInputStream
						.readObject();
				Class laClassSerialized = getSerializedClass(asSerializedFileName);
				laReturnCache = (GeneralCache) laClassSerialized
						.newInstance();
				laReturnCache.setHashtable(lhtReturnHashtable);
			}
		}
		catch (InstantiationException aeInsEx)
		{
			RTSException leRTSEx = new RTSException(
					RTSException.JAVA_ERROR, aeInsEx);
			throw leRTSEx;
		}
		catch (IllegalAccessException aeIllegalAccessException)
		{
			RTSException leRTSEx = new RTSException(
					RTSException.JAVA_ERROR, aeIllegalAccessException);
			throw leRTSEx;
		}
		catch (IOException aeIOEx)
		{
			// On commServer, if there is some problem with the
			// serialization, skip this time and try to refresh it.
			if (SystemProperty.isCommServer())
			{
				return null;
			}
			RTSException leRTSEx = new RTSException(
					RTSException.JAVA_ERROR, aeIOEx);
			throw leRTSEx;
		}
		catch (ClassNotFoundException aeClassNotFoundException)
		{
			RTSException leRTSEx = new RTSException(
					RTSException.JAVA_ERROR, aeClassNotFoundException);
			throw leRTSEx;
		}
		return laReturnCache;
	}

	/**
	 * Refresh the Account Code Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshAccountCodeCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.ACCOUNT_CODES_CACHE,
				new GeneralSearchData());
		// populate cache
		AccountCodesCache laAccountCodeCache = new AccountCodesCache();
		laAccountCodeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(AccountCodesCache.class);
		shtCacheTables.put(lsClassName, laAccountCodeCache);
		// serialize
		writeToDisk(laAccountCodeCache, lsClassName);
	}

	/**
	 * Refresh the admin cache table cache
	 * 
	 * <P>
	 * Compare the subscription timestamp of the admin cache table cache from
	 * the server with the one from the serialized files. If the timestamp of
	 * the serialized files are older than the ones from the server, then
	 * refresh that particular table.<br>
	 * Then compare the change timestamp of the admin cache table cache from the
	 * server with the one from the serialized files. If the timestamp of the
	 * serialized files are older than the ones from the server, then the server
	 * will only return the modified rows of the particular table.
	 * 
	 * @throws RTSException
	 */
	private static void refreshAdminCacheTableCache()
			throws RTSException
	{
		AdministrationCacheTableData laAdministrationCacheTable = new AdministrationCacheTableData();
		laAdministrationCacheTable.setOfcIssuanceNo(SystemProperty
				.getOfficeIssuanceNo());
		laAdministrationCacheTable.setSubstaId(SystemProperty
				.getSubStationId());
		Vector lvAdminCacheTable = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.ADMINISTRATION_CACHE_TABLE_CACHE,
				laAdministrationCacheTable);
		AdministrationCacheTableCache laAdministrationCacheTableCache = (AdministrationCacheTableCache) shtCacheTables
				.get(getSerializedClassName(AdministrationCacheTableCache.class));
		// inspect the subscription timestamp as well as the last changed
		// timestamp
		// if the subscription timestamp is changed refresh the entire table
		// if the subscription timestamp is not changed, and the last changed
		// timestamp
		// is later than the timestamp in memory, then the backend will return
		// the modified
		// rows
		boolean lbRefreshAdminTable = dispatchAdminTableRefreshTask(
				lvAdminCacheTable, laAdministrationCacheTableCache);

		// update the admin table cache and serialize it on local disk
		if (lbRefreshAdminTable)
		{
			if (laAdministrationCacheTableCache == null)
			{
				laAdministrationCacheTableCache = new AdministrationCacheTableCache();
			}
			laAdministrationCacheTableCache.setData(lvAdminCacheTable);
			writeToDisk(
					laAdministrationCacheTableCache,
					getSerializedClassName(AdministrationCacheTableCache.class));
		}
	}

	/**
	 * Refresh Assigned Worstation Ids Cache
	 * 
	 * @param aaRTSDateChngTimestamp
	 *            RTSDate
	 * @param abRefreshAll
	 *            boolean
	 * @throws RTSException
	 */
	private static void refreshAssignedWorkstationIdsCache(
			RTSDate aaRTSDateChngTimestamp, boolean abRefreshAll)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = new GeneralSearchData();
		laGeneralSearchData.setIntKey1(SystemProperty
				.getOfficeIssuanceNo());
		laGeneralSearchData
				.setIntKey2(SystemProperty.getSubStationId());
		laGeneralSearchData.setDate1(aaRTSDateChngTimestamp);
		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.ASSIGNED_WORKSTATION_IDS_CACHE,
				laGeneralSearchData);
		String lsClassName = getSerializedClassName(AssignedWorkstationIdsCache.class);
		AssignedWorkstationIdsCache laAssignedWorkstationIdsCache = (AssignedWorkstationIdsCache) shtCacheTables
				.get(lsClassName);
		// populate cache
		if (abRefreshAll)
		{
			laAssignedWorkstationIdsCache = new AssignedWorkstationIdsCache();
			laAssignedWorkstationIdsCache.setData(lvReturnData);
			shtCacheTables.put(lsClassName,
					laAssignedWorkstationIdsCache);
		}
		else
		{
			Hashtable lhtHashTable = laAssignedWorkstationIdsCache
					.getHashtable();
			for (int i = 0; i < lvReturnData.size(); i++)
			{
				AssignedWorkstationIdsData laAssignedWorkstationIdsData = (AssignedWorkstationIdsData) lvReturnData
						.elementAt(i);
				lhtHashTable
						.put(
								AssignedWorkstationIdsCache.getKey(
										laAssignedWorkstationIdsData
												.getOfcIssuanceNo(),
										laAssignedWorkstationIdsData
												.getSubstaId(),
										laAssignedWorkstationIdsData
												.getWsId()),
								laAssignedWorkstationIdsData);
			}
		}
		// serialize
		writeToDisk(laAssignedWorkstationIdsCache, lsClassName);
	}

	/**
	 * Refresh cache
	 * 
	 * @throws RTSException
	 */
	public static void refreshCache() throws RTSException
	{
		refreshStaticCacheTableCache();
		refreshAdminCacheTableCache();
	}

	/**
	 * Refresh Batch Report Management Cache
	 * 
	 * @param aaRTSDateChngTimestamp
	 *            RTSDate
	 * @param abRefreshAll
	 *            boolean
	 * @throws RTSException
	 */
	private static void refreshBatchReportManagementCache(
			RTSDate aaRTSDateChngTimestamp, boolean abRefreshAll)
			throws RTSException
	{
		BatchReportManagementData laBRMData = new BatchReportManagementData();
		laBRMData
				.setOfcIssuanceNo(SystemProperty.getOfficeIssuanceNo());
		laBRMData.setSubstaId(SystemProperty.getSubStationId());
		laBRMData.setChngTimestmp(aaRTSDateChngTimestamp);
		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.BATCH_REPORT_MANAGEMENT_CACHE, laBRMData);

		String lsClassName = getSerializedClassName(BatchReportManagementCache.class);
		BatchReportManagementCache laBRMCache = (BatchReportManagementCache) shtCacheTables
				.get(lsClassName);

		// populate cache
		if (abRefreshAll)
		{
			laBRMCache = new BatchReportManagementCache();
			laBRMCache.setData(lvReturnData);
			shtCacheTables.put(lsClassName, laBRMCache);
		}
		else
		{
			Hashtable hashTable = laBRMCache.getHashtable();
			for (int i = 0; i < lvReturnData.size(); i++)
			{
				laBRMData = (BatchReportManagementData) lvReturnData
						.elementAt(i);
				hashTable.put(
						BatchReportManagementCache
								.getKey(laBRMData.getOfcIssuanceNo(),
										laBRMData.getSubstaId(),
										laBRMData.getFileName()),
						laBRMData);
			}
		}
		// end defect 10112
		// serialize
		writeToDisk(laBRMCache, lsClassName);
	}

	/**
	 * Refresh the Business Partner Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshBusinessPartnerCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.BUSINESS_PARTNER_CACHE,
				new CertifiedLienholderData());

		// populate cache
		BusinessPartnerCache laCache = new BusinessPartnerCache();
		laCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(BusinessPartnerCache.class);
		shtCacheTables.put(lsClassName, laCache);
		// serialize
		writeToDisk(laCache, lsClassName);
	}

	/**
	 * Refresh the Certified Lienholder Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshCertifiedLienholderCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.CERTFD_LIENHLDR_CACHE,
				new CertifiedLienholderData());
		// populate cache
		CertifiedLienholderCache laCertLienhldrCache = new CertifiedLienholderCache();
		laCertLienhldrCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(CertifiedLienholderCache.class);
		shtCacheTables.put(lsClassName, laCertLienhldrCache);
		// serialize
		writeToDisk(laCertLienhldrCache, lsClassName);
	}

	/**
	 * Refresh the Class To Plate Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshClassToPlateCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.CLASS_TO_PLATE_CACHE,
				new GeneralSearchData());
		// populate cache
		ClassToPlateCache laClassToPlateCache = new ClassToPlateCache();
		laClassToPlateCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(ClassToPlateCache.class);
		shtCacheTables.put(lsClassName, laClassToPlateCache);
		// serialize
		writeToDisk(laClassToPlateCache, lsClassName);
	}

	/**
	 * Refresh the Class To Plate Description Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshClassToPlateDescriptionCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.CLASS_TO_PLATE_DESCRIPTION_CACHE,
				new GeneralSearchData());
		// populate cache
		ClassToPlateDescriptionCache laClassToPlateDescriptionCache = new ClassToPlateDescriptionCache();
		laClassToPlateDescriptionCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(ClassToPlateDescriptionCache.class);
		shtCacheTables.put(lsClassName, laClassToPlateDescriptionCache);
		// serialize
		writeToDisk(laClassToPlateDescriptionCache, lsClassName);
	}

	/**
	 * Refresh the Commercial Vehicle Weights Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshCommercialVehicleWeightsCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.COMMERCIAL_VEHICLE_WEIGHTS_CACHE,
				new GeneralSearchData());
		// populate cache
		CommercialVehicleWeightsCache laCommercialVehicleWeightsCache = new CommercialVehicleWeightsCache();
		laCommercialVehicleWeightsCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(CommercialVehicleWeightsCache.class);
		shtCacheTables
				.put(lsClassName, laCommercialVehicleWeightsCache);
		// serialize
		writeToDisk(laCommercialVehicleWeightsCache, lsClassName);
	}

	/**
	 * Refresh the Common Fees Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshCommonFeesCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.COMMON_FEES_CACHE,
				new GeneralSearchData());
		// populate cache
		CommonFeesCache laCommonFeesCache = new CommonFeesCache();
		laCommonFeesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(CommonFeesCache.class);
		shtCacheTables.put(lsClassName, laCommonFeesCache);
		// serialize
		writeToDisk(laCommonFeesCache, lsClassName);
	}

	/**
	 * Refresh the County Calendar Year Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshCountyCalendarYearCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.COUNTY_CALENDAR_YEAR_CACHE,
				new GeneralSearchData());
		// populate cache
		CountyCalendarYearCache laCountyCalendarYearCache = new CountyCalendarYearCache();
		laCountyCalendarYearCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(CountyCalendarYearCache.class);
		shtCacheTables.put(lsClassName, laCountyCalendarYearCache);
		// serialize
		writeToDisk(laCountyCalendarYearCache, lsClassName);
	}

	/**
	 * Refresh Credit Card Fees Cache
	 * 
	 * @param aaRTSDateChngTimestamp
	 *            RTSDate
	 * @param abRefreshAll
	 *            boolean
	 * @throws RTSException
	 */
	private static void refreshCreditCardFeesCache(
			RTSDate aaRTSDateChngTimestamp, boolean abRefreshAll)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = new GeneralSearchData();
		laGeneralSearchData.setIntKey1(SystemProperty
				.getOfficeIssuanceNo());
		laGeneralSearchData
				.setIntKey2(SystemProperty.getSubStationId());
		laGeneralSearchData.setDate1(aaRTSDateChngTimestamp);
		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.CREDIT_CARD_FEES_CACHE,
				laGeneralSearchData);
		String lsClassName = getSerializedClassName(CreditCardFeesCache.class);
		CreditCardFeesCache laCreditCardFeesCache = (CreditCardFeesCache) shtCacheTables
				.get(lsClassName);
		// populate cache
		if (abRefreshAll)
		{
			laCreditCardFeesCache = new CreditCardFeesCache();
			laCreditCardFeesCache.setData(lvReturnData);
			shtCacheTables.put(lsClassName, laCreditCardFeesCache);
		}
		else
		{
			Hashtable hashTable = laCreditCardFeesCache.getHashtable();
			for (int i = 0; i < lvReturnData.size(); i++)
			{
				CreditCardFeeData tempData = (CreditCardFeeData) lvReturnData
						.elementAt(i);
				String key = "" + tempData.getOfcIssuanceNo();
				if (hashTable.get(key) == null)
					hashTable.put(key, new Vector());
				Vector v = (Vector) hashTable.get(key);
				v.add(tempData);
				hashTable.put(key, v);
			}
		}
		// serialize
		writeToDisk(laCreditCardFeesCache, lsClassName);
	}

	/**
	 * Refresh Dealer Cache
	 * 
	 * @param aaRTSDateChngTimestamp
	 *            RTSDate
	 * @param abRefreshAll
	 *            boolean
	 * @throws RTSException
	 */
	private static void refreshDealersCache(
			RTSDate aaRTSDateChngTimestamp, boolean abRefreshAll)
			throws RTSException
	{
		// defect 10112
		// defect 10003
		// Use DealerData vs. GeneralSearchData
		DealerData laDealerData = new DealerData();
		laDealerData.setOfcIssuanceNo(SystemProperty
				.getOfficeIssuanceNo());
		laDealerData.setSubstaId(SystemProperty.getSubStationId());
		laDealerData.setChngTimestmp(aaRTSDateChngTimestamp);
		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL, CacheConstant.DEALERS_CACHE,
				laDealerData);
		// end defect 10003

		String lsClassName = getSerializedClassName(DealersCache.class);
		DealersCache laDealersCache = (DealersCache) shtCacheTables
				.get(lsClassName);

		// populate cache
		if (abRefreshAll)
		{
			laDealersCache = new DealersCache();
			laDealersCache.setData(lvReturnData);
			shtCacheTables.put(lsClassName, laDealersCache);
		}
		else
		{
			Hashtable hashTable = laDealersCache.getHashtable();
			for (int i = 0; i < lvReturnData.size(); i++)
			{
				laDealerData = (DealerData) lvReturnData.elementAt(i);
				hashTable.put(DealersCache.getKey(laDealerData
						.getOfcIssuanceNo(),
						laDealerData.getSubstaId(), laDealerData
								.getId()), laDealerData);
			}
		}
		// end defect 10112
		// serialize
		writeToDisk(laDealersCache, lsClassName);
	}

	/**
	 * Refresh the Delete Reasons Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshDeleteReasonsCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.DELETE_REASONS_CACHE,
				new GeneralSearchData());
		// populate cache
		DeleteReasonsCache laDeleteReasonsCache = new DeleteReasonsCache();
		laDeleteReasonsCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(DeleteReasonsCache.class);
		shtCacheTables.put(lsClassName, laDeleteReasonsCache);
		// serialize
		writeToDisk(laDeleteReasonsCache, lsClassName);
	}

	/**
	 * Refresh the Disabled Placard Customer Id Type Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshDisabledPlacardCustomerIdTypeCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.DSABLD_PLCRD_CUST_ID_TYPE_CACHE,
				new GeneralSearchData());

		// populate cache
		DisabledPlacardCustomerIdTypeCache laDsabldPlcrdCustIdTypeCache = new DisabledPlacardCustomerIdTypeCache();
		laDsabldPlcrdCustIdTypeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(DisabledPlacardCustomerIdTypeCache.class);
		shtCacheTables.put(lsClassName, laDsabldPlcrdCustIdTypeCache);

		// serialize
		writeToDisk(laDsabldPlcrdCustIdTypeCache, lsClassName);
	}

	/**
	 * Refresh the Disabled Placard Delete Reason Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshDisabledPlacardDeleteReasonCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.DSABLD_PLCRD_DEL_REASN_CACHE,
				new GeneralSearchData());
		// populate cache
		DisabledPlacardDeleteReasonCache laDsabldPlcrdDelReasnCache = new DisabledPlacardDeleteReasonCache();
		laDsabldPlcrdDelReasnCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(DisabledPlacardDeleteReasonCache.class);
		shtCacheTables.put(lsClassName, laDsabldPlcrdDelReasnCache);
		// serialize
		writeToDisk(laDsabldPlcrdDelReasnCache, lsClassName);
	}

	/**
	 * Refresh the Document Types Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshDocumentTypesCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.DOCUMENT_TYPES_CACHE,
				new GeneralSearchData());
		// populate cache
		DocumentTypesCache laDocumentTypesCache = new DocumentTypesCache();
		laDocumentTypesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(DocumentTypesCache.class);
		shtCacheTables.put(lsClassName, laDocumentTypesCache);
		// serialize
		writeToDisk(laDocumentTypesCache, lsClassName);
	}

	/**
	 * Refresh the Error Messages Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshErrorMessagesCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.ERROR_MESSAGES_CACHE,
				new GeneralSearchData());
		// populate cache
		ErrorMessagesCache laErrorMessagesCache = new ErrorMessagesCache();
		laErrorMessagesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(ErrorMessagesCache.class);
		shtCacheTables.put(lsClassName, laErrorMessagesCache);
		// serialize
		writeToDisk(laErrorMessagesCache, lsClassName);
	}

	/**
	 * Refresh the FundsError Messages Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshFundsCodesCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.FUNDS_CODES_CACHE,
				new GeneralSearchData());
		// populate cache
		FundsCodesCache laFundsCodesCache = new FundsCodesCache();
		laFundsCodesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(FundsCodesCache.class);
		shtCacheTables.put(lsClassName, laFundsCodesCache);
		// serialize
		writeToDisk(laFundsCodesCache, lsClassName);
	}

	/**
	 * Refresh the Indicator Descriptions Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshIndicatorDescriptionsCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.INDICATOR_DESCRIPTIONS_CACHE,
				new GeneralSearchData());
		// populate cache
		IndicatorDescriptionsCache laIndicatorDescriptionsCache = new IndicatorDescriptionsCache();
		laIndicatorDescriptionsCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(IndicatorDescriptionsCache.class);
		shtCacheTables.put(lsClassName, laIndicatorDescriptionsCache);
		// serialize
		writeToDisk(laIndicatorDescriptionsCache, lsClassName);
	}

	/**
	 * Refresh the Indicator Stop Codes Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshIndicatorStopCodesCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.INDICATOR_STOP_CODES_CACHE,
				new GeneralSearchData());
		// populate cache
		IndicatorStopCodesCache laIndicatorStopCodesCache = new IndicatorStopCodesCache();
		laIndicatorStopCodesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(IndicatorStopCodesCache.class);
		shtCacheTables.put(lsClassName, laIndicatorStopCodesCache);
		// serialize
		writeToDisk(laIndicatorStopCodesCache, lsClassName);
	}

	/**
	 * Refresh the Invalid Letters Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshInvalidLetterCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.INVALID_LETTER_CACHE,
				new GeneralSearchData());
		// populate cache
		InvalidLetterCache laInvalidLetterCache = new InvalidLetterCache();
		laInvalidLetterCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(InvalidLetterCache.class);
		shtCacheTables.put(lsClassName, laInvalidLetterCache);
		// serialize
		writeToDisk(laInvalidLetterCache, lsClassName);
	}

	/**
	 * Refresh the Inventory Patterns Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshInventoryPatternsCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.INVENTORY_PATTERNS_CACHE,
				new GeneralSearchData());
		// populate cache
		InventoryPatternsCache laInventoryPatternsCache = new InventoryPatternsCache();
		laInventoryPatternsCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(InventoryPatternsCache.class);
		shtCacheTables.put(lsClassName, laInventoryPatternsCache);
		// serialize
		writeToDisk(laInventoryPatternsCache, lsClassName);
	}

	/**
	 * Refresh the Item Codes Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshItemCodesCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm
				.sendToServer(GeneralConstant.GENERAL,
						CacheConstant.ITEM_CODES_CACHE,
						new GeneralSearchData());
		// populate cache
		ItemCodesCache laItemCodesCache = new ItemCodesCache();
		laItemCodesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(ItemCodesCache.class);
		shtCacheTables.put(lsClassName, laItemCodesCache);
		// serialize
		writeToDisk(laItemCodesCache, lsClassName);
	}

	/**
	 * 
	 * Refresh the Holiday Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshHolidayCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL, CacheConstant.HOLIDAY_CACHE,
				new GeneralSearchData());

		// populate cache
		HolidayCache laHolidayCache = new HolidayCache();
		laHolidayCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(HolidayCache.class);
		shtCacheTables.put(lsClassName, laHolidayCache);
		// serialize
		writeToDisk(laHolidayCache, lsClassName);
	}

	/**
	 * Refresh Lienholders Cache
	 * 
	 * @param aaRTSDateChngTimestamp
	 *            RTSDate
	 * @param abRefreshAll
	 *            boolean
	 * @throws RTSException
	 */
	private static void refreshLienHoldersCache(
			RTSDate aaRTSDateChngTimestamp, boolean abRefreshAll)
			throws RTSException
	{
		// defect 10112
		// defect 10003
		// Use LienholdersData vs. GSD
		LienholderData laLienhldrData = new LienholderData();
		laLienhldrData.setOfcIssuanceNo(SystemProperty
				.getOfficeIssuanceNo());
		laLienhldrData.setSubstaId(SystemProperty.getSubStationId());
		laLienhldrData.setChngTimestmp(aaRTSDateChngTimestamp);

		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.LIENHOLDERS_CACHE, laLienhldrData);
		// end defect 10003

		String lsClassName = getSerializedClassName(LienholdersCache.class);
		LienholdersCache laLienholdersCache = (LienholdersCache) shtCacheTables
				.get(lsClassName);

		// populate cache
		if (abRefreshAll)
		{
			laLienholdersCache = new LienholdersCache();
			laLienholdersCache.setData(lvReturnData);
			shtCacheTables.put(lsClassName, laLienholdersCache);
		}
		else
		{
			Hashtable lhtHashTable = laLienholdersCache.getHashtable();
			for (int i = 0; i < lvReturnData.size(); i++)
			{
				laLienhldrData = (LienholderData) lvReturnData
						.elementAt(i);
				lhtHashTable.put(LienholdersCache.getKey(laLienhldrData
						.getOfcIssuanceNo(), laLienhldrData
						.getSubstaId(), laLienhldrData.getId()),
						laLienhldrData);
			}
		}
		// end defect 10112
		// serialize
		writeToDisk(laLienholdersCache, lsClassName);
	}

	/**
	 * Refresh Miscellaneous Cache
	 * 
	 * @param aaRTSDateChngTimestamp
	 *            RTSDate
	 * @param abRefreshAll
	 *            boolean
	 * @throws RTSException
	 */
	private static void refreshMiscellaneousCache(
			RTSDate aaRTSDateChngTimestamp, boolean abRefreshAll)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = new GeneralSearchData();
		laGeneralSearchData.setIntKey1(SystemProperty
				.getOfficeIssuanceNo());
		laGeneralSearchData
				.setIntKey2(SystemProperty.getSubStationId());
		laGeneralSearchData.setDate1(aaRTSDateChngTimestamp);
		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.MISCELLANEOUS_CACHE, laGeneralSearchData);
		String lsClassName = getSerializedClassName(MiscellaneousCache.class);
		MiscellaneousCache laMiscellaneousCache = (MiscellaneousCache) shtCacheTables
				.get(lsClassName);
		// populate cache
		if (abRefreshAll)
		{
			laMiscellaneousCache = new MiscellaneousCache();
			laMiscellaneousCache.setData(lvReturnData);
			shtCacheTables.put(lsClassName, laMiscellaneousCache);
		}
		else
		{
			Hashtable lhtHashTable = laMiscellaneousCache
					.getHashtable();
			for (int i = 0; i < lvReturnData.size(); i++)
			{
				MiscellaneousData lMiscellaneousData = (MiscellaneousData) lvReturnData
						.elementAt(i);
				lhtHashTable.put(MiscellaneousCache.getKey(
						lMiscellaneousData.getOfcIssuanceNo(),
						lMiscellaneousData.getSubstaId()),
						lMiscellaneousData);
			}
		}
		// serialize
		writeToDisk(laMiscellaneousCache, lsClassName);
	}

	/**
	 * Refresh the Office Codes Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshOfficeCodesCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.OFFICE_CODES_CACHE,
				new GeneralSearchData());
		// populate cache
		OfficeCodesCache laOfficeCodesCache = new OfficeCodesCache();
		laOfficeCodesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(OfficeCodesCache.class);
		shtCacheTables.put(lsClassName, laOfficeCodesCache);
		// serialize
		writeToDisk(laOfficeCodesCache, lsClassName);
	}

	/**
	 * Refresh the OfficeIds Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshOfficeIdsCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm
				.sendToServer(GeneralConstant.GENERAL,
						CacheConstant.OFFICE_IDS_CACHE,
						new GeneralSearchData());
		// populate cache
		OfficeIdsCache laOfficeIdsCache = new OfficeIdsCache();
		laOfficeIdsCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(OfficeIdsCache.class);
		shtCacheTables.put(lsClassName, laOfficeIdsCache);
		// serialize
		writeToDisk(laOfficeIdsCache, lsClassName);
	}

	/**
	 * Refresh the OfficeTimeZone Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshOfficeTimeZoneCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.OFFICE_TIMEZONE_CACHE,
				new GeneralSearchData());

		// populate cache
		OfficeTimeZoneCache laOfcTZCache = new OfficeTimeZoneCache();
		laOfcTZCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(OfficeTimeZoneCache.class);
		shtCacheTables.put(lsClassName, laOfcTZCache);
		// serialize
		writeToDisk(laOfcTZCache, lsClassName);
	}

	/**
	 * Refresh the OrganizationNumberCache
	 * 
	 * @throws RTSException
	 */
	private static void refreshOrganizationNumberCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL, CacheConstant.ORG_NO_CACHE,
				new GeneralSearchData());
		// populate cache
		OrganizationNumberCache laOrganizationNumberCache = new OrganizationNumberCache();
		laOrganizationNumberCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(OrganizationNumberCache.class);
		shtCacheTables.put(lsClassName, laOrganizationNumberCache);
		// serialize
		writeToDisk(laOrganizationNumberCache, lsClassName);
	}

	/**
	 * Refresh the Ownership Evidence Codes Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshOwnershipEvidenceCodesCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.OWNER_EVIDENCE_CODES_CACHE,
				new GeneralSearchData());
		// populate cache
		OwnershipEvidenceCodesCache laOwnershipEvidenceCodesCache = new OwnershipEvidenceCodesCache();
		laOwnershipEvidenceCodesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(OwnershipEvidenceCodesCache.class);
		shtCacheTables.put(lsClassName, laOwnershipEvidenceCodesCache);
		// serialize
		writeToDisk(laOwnershipEvidenceCodesCache, lsClassName);
	}

	/**
	 * Refresh the Passenger Fees Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPassengerFeesCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.PASSENGER_FEES_CACHE,
				new GeneralSearchData());
		// populate cache
		PassengerFeesCache laPassengerFeesCache = new PassengerFeesCache();
		laPassengerFeesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(PassengerFeesCache.class);
		shtCacheTables.put(lsClassName, laPassengerFeesCache);
		// serialize
		writeToDisk(laPassengerFeesCache, lsClassName);
	}

	/**
	 * Refresh the Payment Status Codes Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPaymentStatusCodesCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.PAYMENT_STATUS_CODES_CACHE,
				new GeneralSearchData());
		// populate cache
		PaymentStatusCodesCache laPaymentStatusCodesCache = new PaymentStatusCodesCache();
		laPaymentStatusCodesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(PaymentStatusCodesCache.class);
		shtCacheTables.put(lsClassName, laPaymentStatusCodesCache);
		// serialize
		writeToDisk(laPaymentStatusCodesCache, lsClassName);
	}

	/**
	 * Refresh the Payment Type Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPaymentTypeCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.PAYMENT_TYPE_CACHE,
				new GeneralSearchData());
		// populate cache
		PaymentTypeCache laPaymentTypeCache = new PaymentTypeCache();
		laPaymentTypeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(PaymentTypeCache.class);
		shtCacheTables.put(lsClassName, laPaymentTypeCache);
		// serialize
		writeToDisk(laPaymentTypeCache, lsClassName);
	}

	/**
	 * Refresh the PlateGroupId Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPlateGroupIdCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm
				.sendToServer(GeneralConstant.GENERAL,
						CacheConstant.PLT_GRP_ID_CACHE,
						new GeneralSearchData());
		// populate cache
		PlateGroupIdCache laPlateGroupIdCache = new PlateGroupIdCache();
		laPlateGroupIdCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(PlateGroupIdCache.class);
		shtCacheTables.put(lsClassName, laPlateGroupIdCache);
		// serialize
		writeToDisk(laPlateGroupIdCache, lsClassName);
	}

	/**
	 * Refresh the PlateSurcharge Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPlateSurchargeCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.PLT_SURCHARGE_CACHE,
				new GeneralSearchData());
		// populate cache
		PlateSurchargeCache laPlateSurchargeCache = new PlateSurchargeCache();
		laPlateSurchargeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(PlateSurchargeCache.class);
		shtCacheTables.put(lsClassName, laPlateSurchargeCache);
		// serialize
		writeToDisk(laPlateSurchargeCache, lsClassName);
	}

	/**
	 * Refresh the PlateToSticker Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPlateToStickerCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.PLATE_TO_STICKER_CACHE,
				new GeneralSearchData());
		// populate cache
		PlateToStickerCache laPlateToStickerCache = new PlateToStickerCache();
		laPlateToStickerCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(PlateToStickerCache.class);
		shtCacheTables.put(lsClassName, laPlateToStickerCache);
		// serialize
		writeToDisk(laPlateToStickerCache, lsClassName);
	}

	/**
	 * Refresh the Plate Symbol Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPlateSymbolCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.PLATE_SYMBOL_CACHE,
				new GeneralSearchData());

		// populate cache
		PlateSymbolCache laPlateSymbolCache = new PlateSymbolCache();

		laPlateSymbolCache.setData(lvDataSet);

		String lsClassName = getSerializedClassName(PlateSymbolCache.class);

		shtCacheTables.put(lsClassName, laPlateSymbolCache);

		// serialize
		writeToDisk(laPlateSymbolCache, lsClassName);
	}

	/**
	 * Refresh the PlateType Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPlateTypeCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL, CacheConstant.PLT_TYPE_CACHE,
				new GeneralSearchData());
		// populate cache
		PlateTypeCache laPlateTypeCache = new PlateTypeCache();
		laPlateTypeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(PlateTypeCache.class);
		shtCacheTables.put(lsClassName, laPlateTypeCache);
		// serialize
		writeToDisk(laPlateTypeCache, lsClassName);
	}

	/**
	 * Refresh the Product Service Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshProductServiceCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.PRODUCT_SERVICE_CACHE,
				new GeneralSearchData());
		// populate cache
		ProductServiceCache laProductServiceCache = new ProductServiceCache();
		laProductServiceCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(ProductServiceCache.class);
		shtCacheTables.put(lsClassName, laProductServiceCache);
		// serialize
		writeToDisk(laProductServiceCache, lsClassName);
	}

	/**
	 * Refresh the Postal State Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshPostalStateCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.POSTAL_STATE_CACHE,
				new GeneralSearchData());

		// populate cache
		PostalStateCache laPostalStateCache = new PostalStateCache();

		laPostalStateCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(PostalStateCache.class);
		shtCacheTables.put(lsClassName, laPostalStateCache);

		// serialize
		writeToDisk(laPostalStateCache, lsClassName);
	}

	/**
	 * Refresh the Registration Additional Fee Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshRegistrationAdditionalFeeCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.REGISTRATION_ADDITIONAL_FEE_CACHE,
				new GeneralSearchData());
		// populate cache
		RegistrationAdditionalFeeCache laRegistrationAdditionalFeeCache = new RegistrationAdditionalFeeCache();
		laRegistrationAdditionalFeeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(RegistrationAdditionalFeeCache.class);
		shtCacheTables.put(lsClassName,
				laRegistrationAdditionalFeeCache);
		// serialize
		writeToDisk(laRegistrationAdditionalFeeCache, lsClassName);
	}

	/**
	 * Refresh the Registration Class Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshRegistrationClassCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.REGISTRATION_CLASS_CACHE,
				new GeneralSearchData());
		// populate cache
		RegistrationClassCache laRegistrationClassCache = new RegistrationClassCache();
		laRegistrationClassCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(RegistrationClassCache.class);
		shtCacheTables.put(lsClassName, laRegistrationClassCache);
		// serialize
		writeToDisk(laRegistrationClassCache, lsClassName);
	}

	/**
	 * Refresh the Registration Class Fee Group Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshRegistrationClassFeeGroupCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.REGISTRATION_CLASS_FEE_GRP_CACHE,
				new GeneralSearchData());

		// populate cache
		RegistrationClassFeeGroupCache laRegistrationClassFeeGrpCache = new RegistrationClassFeeGroupCache();

		laRegistrationClassFeeGrpCache.setData(lvDataSet);

		String lsClassName = getSerializedClassName(RegistrationClassFeeGroupCache.class);

		shtCacheTables.put(lsClassName, laRegistrationClassFeeGrpCache);

		// serialize
		writeToDisk(laRegistrationClassFeeGrpCache, lsClassName);
	}

	/**
	 * Refresh the Registration Weight Fees Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshRegistrationWeightFeesCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.REGISTRATION_WEIGHT_FEES_CACHE,
				new GeneralSearchData());
		// populate cache
		RegistrationWeightFeesCache laRegistrationWeightFeesCache = new RegistrationWeightFeesCache();
		laRegistrationWeightFeesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(RegistrationWeightFeesCache.class);
		shtCacheTables.put(lsClassName, laRegistrationWeightFeesCache);
		// serialize
		writeToDisk(laRegistrationWeightFeesCache, lsClassName);
	}

	/**
	 * Refresh the Report Category Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshReportCategoryCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.REPORT_CATEGORY_CACHE,
				new GeneralSearchData());
		// populate cache
		ReportCategoryCache laReportCategoryCache = new ReportCategoryCache();
		laReportCategoryCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(ReportCategoryCache.class);
		shtCacheTables.put(lsClassName, laReportCategoryCache);
		// serialize
		writeToDisk(laReportCategoryCache, lsClassName);
	}

	/**
	 * Refresh the Reports Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshReportsCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL, CacheConstant.REPORTS_CACHE,
				new GeneralSearchData());
		// populate cache
		ReportsCache laReportsCache = new ReportsCache();
		laReportsCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(ReportsCache.class);
		shtCacheTables.put(lsClassName, laReportsCache);
		// serialize
		writeToDisk(laReportsCache, lsClassName);
	}

	/**
	 * Refresh the RSPS Ws Status Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshRSPSWsStsCache() throws RTSException
	{
		GeneralSearchData laGeneralSearchData = new GeneralSearchData();
		laGeneralSearchData.setIntKey1(SystemProperty
				.getOfficeIssuanceNo());
		laGeneralSearchData
				.setIntKey2(SystemProperty.getSubStationId());
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL, CacheConstant.RSPS_WS_STATUS,
				laGeneralSearchData);
		// populate cache
		// note that we always replace everything
		RSPSWsStatusCache laRSPSCache = new RSPSWsStatusCache();
		laRSPSCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(RSPSWsStatusCache.class);
		shtCacheTables.put(lsClassName, laRSPSCache);
		// serialize
		writeToDisk(laRSPSCache, lsClassName);
	}

	/**
	 * Refresh the Sales Tax Category Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshSalesTaxCategoryCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.SALES_TAX_CATEGORY_CACHE,
				new GeneralSearchData());
		// populate cache
		SalesTaxCategoryCache laSalesTaxCategoryCache = new SalesTaxCategoryCache();
		laSalesTaxCategoryCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(SalesTaxCategoryCache.class);
		shtCacheTables.put(lsClassName, laSalesTaxCategoryCache);
		// serialize
		writeToDisk(laSalesTaxCategoryCache, lsClassName);
	}

	/**
	 * Refresh Security Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshSecurityCache() throws RTSException
	{
		GeneralSearchData laGeneralSearchData = new GeneralSearchData();
		laGeneralSearchData.setIntKey1(SystemProperty
				.getOfficeIssuanceNo());
		laGeneralSearchData
				.setIntKey2(SystemProperty.getSubStationId());
		// defect 7017
		// we no longer want to include deleted users in cache
		// laGeneralSearchData.setDate1(aaRTSDateChngTimestamp);
		// end defect 7017
		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL, CacheConstant.SECURITY_CACHE,
				laGeneralSearchData);
		String lsClassName = getSerializedClassName(SecurityCache.class);
		SecurityCache laSecurityCache = (SecurityCache) shtCacheTables
				.get(lsClassName);
		// defect 7017
		// Always build all Security Cache to ensure
		// only active users are in cache
		// populate cache
		// if (abRefreshAll)
		// {
		// complete replacement of security cache
		laSecurityCache = new SecurityCache();
		laSecurityCache.setData(lvReturnData);
		shtCacheTables.put(lsClassName, laSecurityCache);
		// }
		// else
		// {
		// // incremental update of cache
		// Hashtable hashTable = lSecurityCache.getHashtable();
		// for (int i = 0; i < lvReturnData.size(); i++)
		// {
		// SecurityData lSecurityData = (SecurityData)
		// lvReturnData.elementAt(i);
		//		
		// // defect 6953
		// // On windows, use UserName instead of Empid
		// // as part of the key.
		// if (UtilityMethods.isWindowsPlatform())
		// {
		// hashTable.put(
		// SecurityCache.getKey(
		// lSecurityData.getOfcIssuanceNo(),
		// lSecurityData.getSubstaId(),
		// lSecurityData.getUserName()),
		// lSecurityData);
		// }
		// else
		// {
		// hashTable.put(
		// SecurityCache.getKey(
		// lSecurityData.getOfcIssuanceNo(),
		// lSecurityData.getSubstaId(),
		// lSecurityData.getEmpId()),
		// lSecurityData);
		// }
		// // end defect 6953
		// }
		// }
		// serialize
		writeToDisk(laSecurityCache, lsClassName);
	}

	/**
	 * Refresh the Special Plate Fixed Expiration Month
	 * 
	 * @throws RTSException
	 */
	private static void refreshSpclPltFxdExpMoCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.SPCL_PLT_FXD_EXP_MO,
				new GeneralSearchData());
		// populate cache
		SpecialPlateFixedExpirationMonthCache laSpecialPlateFixedExpirationMonthCache = new SpecialPlateFixedExpirationMonthCache();
		laSpecialPlateFixedExpirationMonthCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(SpecialPlateFixedExpirationMonthCache.class);
		shtCacheTables.put(lsClassName,
				laSpecialPlateFixedExpirationMonthCache);
		// serialize
		writeToDisk(laSpecialPlateFixedExpirationMonthCache,
				lsClassName);
	}

	/**
	 * Refreshes the static cache table cache
	 * 
	 * <P>
	 * Compare the timestamp of the static cache table cache from the server
	 * with the one from the serialized files. If the timestamp of the
	 * serialized files are older than the ones from the server, then refresh
	 * that particular table.
	 * 
	 * @throws RTSException
	 */
	private static void refreshStaticCacheTableCache()
			throws RTSException
	{
		try
		{
			Vector lvReturnData = (Vector) Comm.sendToServer(
					GeneralConstant.GENERAL,
					CacheConstant.STATIC_CACHE_TABLE_CACHE,
					new GeneralSearchData());
			StaticCacheTableCache laStaticCacheTableCache = (StaticCacheTableCache) shtCacheTables
					.get(getSerializedClassName(StaticCacheTableCache.class));
			// inspect each change time stamp with the one in the cache, refresh
			// cache
			// and serialize to disk if chngtimestmp is newer than current
			boolean lbRefreshStaticTable = dispatchStaticTableRefreshTask(
					lvReturnData, laStaticCacheTableCache);
			// update the static table cache and serialize it on local disk
			if (lbRefreshStaticTable)
			{
				if (laStaticCacheTableCache == null)
				{
					laStaticCacheTableCache = new StaticCacheTableCache();
				}
				laStaticCacheTableCache.setData(lvReturnData);
				writeToDisk(
						laStaticCacheTableCache,
						getSerializedClassName(StaticCacheTableCache.class));
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
	}

	/**
	 * Refresh Subcontractor Cache
	 * 
	 * @param aaRTSDateChngTimestamp
	 *            RTSDate
	 * @param abRefreshAll
	 *            boolean
	 * @throws RTSException
	 */
	private static void refreshSubcontractorCache(
			RTSDate aaRTSDateChngTimestamp, boolean abRefreshAll)
			throws RTSException
	{
		// defect 10003
		// Use SubcontractorData vs. GSD
		SubcontractorData laSubcontractorData = new SubcontractorData();
		laSubcontractorData.setOfcIssuanceNo(SystemProperty
				.getOfficeIssuanceNo());
		laSubcontractorData.setSubstaId(SystemProperty
				.getSubStationId());
		laSubcontractorData.setChngTimestmp(aaRTSDateChngTimestamp);

		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.SUBCONTRACTOR_CACHE, laSubcontractorData);
		// end defect 10003

		String lsClassName = getSerializedClassName(SubcontractorCache.class);
		SubcontractorCache laSubcontractorCache = (SubcontractorCache) shtCacheTables
				.get(lsClassName);
		// populate cache
		if (abRefreshAll)
		{
			laSubcontractorCache = new SubcontractorCache();
			laSubcontractorCache.setData(lvReturnData);
			shtCacheTables.put(lsClassName, laSubcontractorCache);
		}
		else
		{
			Hashtable lhtHashTable = laSubcontractorCache
					.getHashtable();
			for (int i = 0; i < lvReturnData.size(); i++)
			{
				laSubcontractorData = (SubcontractorData) lvReturnData
						.elementAt(i);

				// defect 10161
				lhtHashTable.put(SubcontractorCache.getKey(
						laSubcontractorData.getOfcIssuanceNo(),
						laSubcontractorData.getSubstaId(),
						laSubcontractorData.getId()),
						laSubcontractorData);
				// end defect 10161
			}
		}
		// serialize
		writeToDisk(laSubcontractorCache, lsClassName);
	}

	/**
	 * Refresh Substation Cache
	 * 
	 * @param aaRTSDateChngTimestamp
	 *            RTSDate
	 * @param abRefreshAll
	 *            boolean
	 * @throws RTSException
	 */
	private static void refreshSubstationCache(
			RTSDate aaRTSDateChngTimestamp, boolean abRefreshAll)
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = new GeneralSearchData();
		laGeneralSearchData.setIntKey1(SystemProperty
				.getOfficeIssuanceNo());

		// defect 9488
		laGeneralSearchData.setIntKey2(Integer.MIN_VALUE);
		// SystemProperty.getSubStationId());
		// end defect 9488

		laGeneralSearchData.setDate1(aaRTSDateChngTimestamp);
		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.SUBSTATION_CACHE, laGeneralSearchData);
		String lsClassName = getSerializedClassName(SubstationCache.class);
		SubstationCache laSubstationCache = (SubstationCache) shtCacheTables
				.get(lsClassName);
		// populate cache
		if (abRefreshAll)
		{
			laSubstationCache = new SubstationCache();
			laSubstationCache.setData(lvReturnData);
			shtCacheTables.put(lsClassName, laSubstationCache);
		}
		else
		{
			Hashtable lhtHashTable = laSubstationCache.getHashtable();
			for (int i = 0; i < lvReturnData.size(); i++)
			{
				SubstationData laSubstationData = (SubstationData) lvReturnData
						.elementAt(i);
				lhtHashTable.put(SubstationCache.getKey(
						laSubstationData.getOfcIssuanceNo(),
						laSubstationData.getSubstaId()),
						laSubstationData);
			}
		}
		// serialize
		writeToDisk(laSubstationCache, lsClassName);
	}

	/**
	 * Refresh Substation Subscription Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshSubstationSubscriptionCache()
			throws RTSException
	{
		GeneralSearchData laGeneralSearchData = new GeneralSearchData();

		laGeneralSearchData.setIntKey1(SystemProperty
				.getOfficeIssuanceNo());

		laGeneralSearchData.setIntKey2(Integer.MIN_VALUE);

		Vector lvReturnData = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.SUBSTATION_SUBSCRIPTION_CACHE,
				laGeneralSearchData);

		String lsClassName = getSerializedClassName(SubstationSubscriptionCache.class);

		SubstationSubscriptionCache laSubstationSubscriptionCache = (SubstationSubscriptionCache) shtCacheTables
				.get(lsClassName);

		laSubstationSubscriptionCache = new SubstationSubscriptionCache();
		laSubstationSubscriptionCache.setData(lvReturnData);
		shtCacheTables.put(lsClassName, laSubstationSubscriptionCache);

		// serialize
		writeToDisk(laSubstationSubscriptionCache, lsClassName);
	}

	// /**
	// * Refresh Substation Subscription Cache
	// *
	// * @param aaRTSDateChngTimestamp RTSDate
	// * @param abRefreshAll boolean
	// * @throws RTSException
	// */
	// private static void refreshSubstationSubscriptionCache(
	// RTSDate aaRTSDateChngTimestamp,
	// boolean abRefreshAll)
	// throws RTSException
	// {
	// GeneralSearchData laGeneralSearchData = new GeneralSearchData();
	// laGeneralSearchData.setIntKey1(
	// SystemProperty.getOfficeIssuanceNo());
	//
	// // defect 9488
	// laGeneralSearchData.setIntKey2(Integer.MIN_VALUE);
	// //SystemProperty.getSubStationId());
	// // end defect 9488
	//
	// laGeneralSearchData.setDate1(aaRTSDateChngTimestamp);
	// Vector lvReturnData =
	// (Vector) Comm.sendToServer(
	// GeneralConstant.GENERAL,
	// CacheConstant.SUBSTATION_SUBSCRIPTION_CACHE,
	// laGeneralSearchData);
	//
	// String lsClassName =
	// getSerializedClassName(SubstationSubscriptionCache.class);
	//
	// SubstationSubscriptionCache laSubstationSubscriptionCache =
	// (SubstationSubscriptionCache) shtCacheTables.get(
	// lsClassName);
	// //populate cache
	// if (abRefreshAll)
	// {
	// laSubstationSubscriptionCache =
	// new SubstationSubscriptionCache();
	// laSubstationSubscriptionCache.setData(lvReturnData);
	// shtCacheTables.put(
	// lsClassName,
	// laSubstationSubscriptionCache);
	// }
	// else
	// {
	// Hashtable lhtHashTable =
	// laSubstationSubscriptionCache.getHashtable();
	// for (int i = 0; i < lvReturnData.size(); i++)
	// {
	// SubstationSubscriptionData laSubstationSubscriptionData =
	// (
	// SubstationSubscriptionData) lvReturnData
	// .elementAt(
	// i);
	// lhtHashTable.put(
	// SubstationSubscriptionCache.getKey(
	// laSubstationSubscriptionData.getOfcIssuanceNo(),
	// laSubstationSubscriptionData.getSubstaId(),
	// laSubstationSubscriptionData.getTblName()),
	// laSubstationSubscriptionData);
	// }
	// }
	// //serialize
	// writeToDisk(laSubstationSubscriptionCache, lsClassName);
	// }

	/**
	 * Refresh the Tax Exempt Code Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshTaxExemptCodeCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.TAX_EXEMPT_CODE_CACHE,
				new GeneralSearchData());
		// populate cache
		TaxExemptCodeCache laTaxExemptCodeCache = new TaxExemptCodeCache();
		laTaxExemptCodeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(TaxExemptCodeCache.class);
		shtCacheTables.put(lsClassName, laTaxExemptCodeCache);
		// serialize
		writeToDisk(laTaxExemptCodeCache, lsClassName);
	}

	/**
	 * Refresh the Title TERP FEE Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshTitleTERPFeeCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.TITLE_TERP_FEE_CACHE,
				new GeneralSearchData());
		// populate cache
		TitleTERPFeeCache laTitleTERPFeeCache = new TitleTERPFeeCache();
		laTitleTERPFeeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(TitleTERPFeeCache.class);
		shtCacheTables.put(lsClassName, laTitleTERPFeeCache);
		// serialize
		writeToDisk(laTitleTERPFeeCache, lsClassName);
	}

//	/**
//	 * 
//	 * Refresh the TxDOTHoliday Cache
//	 * 
//	 * @throws RTSException
//	 * @deprecate 
//	 */
//	private static void refreshTxDOTHolidayCache() throws RTSException
//	{
//		Vector lvDataSet = (Vector) Comm.sendToServer(
//				GeneralConstant.GENERAL,
//				CacheConstant.TXDOT_HOLIDAY_CACHE,
//				new GeneralSearchData());
//		// populate cache
//		TxDOTHolidayCache laTxDOTHolidayCache = new TxDOTHolidayCache();
//		laTxDOTHolidayCache.setData(lvDataSet);
//		String lsClassName = getSerializedClassName(TxDOTHolidayCache.class);
//		shtCacheTables.put(lsClassName, laTxDOTHolidayCache);
//		// serialize
//		writeToDisk(laTxDOTHolidayCache, lsClassName);
//	}

	/**
	 * Refresh the Title TERP PERCENT Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshTitleTERPPercentCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.TITLE_TERP_PERCENT_CACHE,
				new GeneralSearchData());
		// populate cache
		TitleTERPPercentCache laTitleTERPPercentCache = new TitleTERPPercentCache();
		laTitleTERPPercentCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(TitleTERPPercentCache.class);
		shtCacheTables.put(lsClassName, laTitleTERPPercentCache);
		// serialize
		writeToDisk(laTitleTERPPercentCache, lsClassName);
	}

	/**
	 * Refresh the Title Transfer Entity Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshTtlTrnsfrEntCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.TTL_TRNSFR_ENT_CACHE,
				new GeneralSearchData());
		// populate cache
		TitleTransferEntityCache laTtlTrnsfrEntCache = new TitleTransferEntityCache();
		laTtlTrnsfrEntCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(TitleTransferEntityCache.class);
		shtCacheTables.put(lsClassName, laTtlTrnsfrEntCache);
		// serialize
		writeToDisk(laTtlTrnsfrEntCache, lsClassName);
	}

	/**
	 * Refresh the Title Transfer Penalty Exempt Code Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshTtlTrnsfrPnltyExmptCdCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.TTL_TRNSFR_PNLTY_EXMPT_CD_CACHE,
				new GeneralSearchData());

		// populate cache
		TitleTransferPenaltyExemptCodeCache laTtlTrnsfrPnltyExmptCdCache = new TitleTransferPenaltyExemptCodeCache();
		laTtlTrnsfrPnltyExmptCdCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(TitleTransferPenaltyExemptCodeCache.class);
		shtCacheTables.put(lsClassName, laTtlTrnsfrPnltyExmptCdCache);
		// serialize
		writeToDisk(laTtlTrnsfrPnltyExmptCdCache, lsClassName);
	}

	/**
	 * Refresh the Title Transfer Penalty Fee Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshTtlTrnsfrPnltyFeeCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.TTL_TRNSFR_PNLTY_FEE_CACHE,
				new GeneralSearchData());
		// populate cache
		TitleTransferPenaltyFeeCache laTtlPnltyFeeCache = new TitleTransferPenaltyFeeCache();
		laTtlPnltyFeeCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(TitleTransferPenaltyFeeCache.class);
		shtCacheTables.put(lsClassName, laTtlPnltyFeeCache);
		// serialize
		writeToDisk(laTtlPnltyFeeCache, lsClassName);
	}

	/**
	 * Refresh the Transaction Codes Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshTransactionCodesCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.TRANSACTION_CODES_CACHE,
				new GeneralSearchData());
		// populate cache
		TransactionCodesCache laTransactionCodesCache = new TransactionCodesCache();
		laTransactionCodesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(TransactionCodesCache.class);
		shtCacheTables.put(lsClassName, laTransactionCodesCache);
		// serialize
		writeToDisk(laTransactionCodesCache, lsClassName);
	}

	/**
	 * Refresh the Vehicle Body Types Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshVehicleBodyTypesCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.VEHICLE_BODY_TYPES_CACHE,
				new GeneralSearchData());
		// populate cache
		VehicleBodyTypesCache laVehicleBodyTypesCache = new VehicleBodyTypesCache();
		laVehicleBodyTypesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(VehicleBodyTypesCache.class);
		shtCacheTables.put(lsClassName, laVehicleBodyTypesCache);
		// serialize
		writeToDisk(laVehicleBodyTypesCache, lsClassName);
	}

	/**
	 * Refresh the Vehicle Class Spcl Plt Type Description Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshVehicleClassSpclPltTypeDescCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.VEH_CLASS_SPCL_PLT_TYPE_DESC_CACHE,
				new GeneralSearchData());
		// populate cache
		VehicleClassSpclPltTypeDescCache laVehicleClassSpclPltTypeDescCache = new VehicleClassSpclPltTypeDescCache();
		laVehicleClassSpclPltTypeDescCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(VehicleClassSpclPltTypeDescCache.class);
		shtCacheTables.put(lsClassName,
				laVehicleClassSpclPltTypeDescCache);
		// serialize
		writeToDisk(laVehicleClassSpclPltTypeDescCache, lsClassName);

	}

	/**
	 * Refresh the Vehicle Class Registration Class Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshVehicleClassRegistrationClassCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.VEHICLE_CLASS_REGISTRATION_CLASS_CACHE,
				new GeneralSearchData());
		// populate cache
		VehicleClassRegistrationClassCache laVehicleClassRegistrationClassCache = new VehicleClassRegistrationClassCache();
		laVehicleClassRegistrationClassCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(VehicleClassRegistrationClassCache.class);
		shtCacheTables.put(lsClassName,
				laVehicleClassRegistrationClassCache);
		// serialize
		writeToDisk(laVehicleClassRegistrationClassCache, lsClassName);
	}

	/**
	 * Refresh the Vehicle Color Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshVehicleColorCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.VEHICLE_COLOR_CACHE,
				new GeneralSearchData());

		// populate cache
		VehicleColorCache laVehicleColorCache = new VehicleColorCache();
		laVehicleColorCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(VehicleColorCache.class);
		shtCacheTables.put(lsClassName, laVehicleColorCache);
		// serialize
		writeToDisk(laVehicleColorCache, lsClassName);
	}

	/**
	 * Refresh the Vehicle Diesel Ton Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshVehicleDieselTonCache()
			throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.VEHICLE_DIESEL_TON_CACHE,
				new GeneralSearchData());
		// populate cache
		VehicleDieselTonCache laVehicleDieselTonCache = new VehicleDieselTonCache();
		laVehicleDieselTonCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(VehicleDieselTonCache.class);
		shtCacheTables.put(lsClassName, laVehicleDieselTonCache);
		// serialize
		writeToDisk(laVehicleDieselTonCache, lsClassName);
	}

	/**
	 * Refresh the Vehicle Makes Cache
	 * 
	 * @throws RTSException
	 */
	private static void refreshVehicleMakesCache() throws RTSException
	{
		Vector lvDataSet = (Vector) Comm.sendToServer(
				GeneralConstant.GENERAL,
				CacheConstant.VEHICLE_MAKES_CACHE,
				new GeneralSearchData());
		// populate cache
		VehicleMakesCache laVehicleMakesCache = new VehicleMakesCache();
		laVehicleMakesCache.setData(lvDataSet);
		String lsClassName = getSerializedClassName(VehicleMakesCache.class);
		shtCacheTables.put(lsClassName, laVehicleMakesCache);
		// serialize
		writeToDisk(laVehicleMakesCache, lsClassName);
	}

	/**
	 * serialize the Cache
	 * 
	 * @param aaGeneralCache
	 *            GeneralCache
	 * @param asSerializedFileName
	 *            String
	 * @throws RTSException
	 */
	public static void writeToDisk(GeneralCache aaGeneralCache,
			String asSerializedFileName) throws RTSException
	{
		try
		{
			// Write to Cache\Temp Directory
			FileOutputStream lpfsFileOutputStream = new FileOutputStream(
					SystemProperty.getCacheDirectory() + TEMPDIR
							+ asSerializedFileName);
			ObjectOutputStream laObjectOutputStream = new ObjectOutputStream(
					lpfsFileOutputStream);
			laObjectOutputStream.writeObject(aaGeneralCache
					.getHashtable());
			laObjectOutputStream.flush();
			lpfsFileOutputStream = new FileOutputStream(SystemProperty
					.getCacheDirectory()
					+ asSerializedFileName);
			laObjectOutputStream = new ObjectOutputStream(
					lpfsFileOutputStream);
			laObjectOutputStream.writeObject(aaGeneralCache
					.getHashtable());
			laObjectOutputStream.flush();
		}
		catch (IOException aeIOException)
		{
			RTSException leRTSException = new RTSException(
					RTSException.JAVA_ERROR, aeIOException);
			throw leRTSException;
		}
	}

	/**
	 * serialize the Cache (CacheHashTable)
	 * 
	 * @param ahtCacheHashtable
	 *            Hashtable
	 * @param asSerializedFileName
	 *            String
	 * @throws RTSException
	 */
	public static void writeToDisk(Hashtable ahtCacheHashtable,
			String asSerializedFileName) throws RTSException
	{
		try
		{
			FileOutputStream lpfsFileOutputStream = new FileOutputStream(
					SystemProperty.getCacheDirectory()
							+ asSerializedFileName);
			ObjectOutputStream laObjectOutputStream = new ObjectOutputStream(
					lpfsFileOutputStream);
			laObjectOutputStream.writeObject(ahtCacheHashtable);
			laObjectOutputStream.flush();
		}
		catch (IOException aeIOException)
		{
			RTSException leRTSException = new RTSException(
					RTSException.JAVA_ERROR, aeIOException);
			throw leRTSException;
		}
	}
}
