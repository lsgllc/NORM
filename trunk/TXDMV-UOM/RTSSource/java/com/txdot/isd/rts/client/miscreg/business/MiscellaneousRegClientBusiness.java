package com.txdot.isd.rts.client.miscreg.business;import java.util.Calendar;import java.util.Vector;import com.txdot.isd.rts.client.registration.business.RegistrationVerify;import com.txdot.isd.rts.services.cache.AccountCodesCache;import com.txdot.isd.rts.services.cache.CommonFeesCache;import com.txdot.isd.rts.services.common.Transaction;import com.txdot.isd.rts.services.communication.Comm;import com.txdot.isd.rts.services.data.*;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.CommonValidations;import com.txdot.isd.rts.services.util.RTSDate;import com.txdot.isd.rts.services.util.UtilityMethods;import com.txdot.isd.rts.services.util.constants.*;/*  * MiscellaneousRegClientBusiness.java *  * (c) Texas Department of Transportation  2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * B Hargrove	06/30/2005	Modify for move to Jave 1.4. Bring code to * 							standards. Chg '/**' to '/*' to begin prolog. * 							Format, Hungarian notation for variables.  *  	 					Remove unused imports, variables.  * 							Add if-else braces. * 							defect 7893 Ver 5.2.3 * J Zwiener	07/17/2005  Enhancement for Disable Placard event *							modify setTimedPrmtTime() * 							defect 8268 Ver 5.2.2 Fix 6 * J Zwiener	01/09/2005	Seasonal Ags not permitted to do Temp *							Addl Wt.  Added SAg regclasscds (70-76) *							to ssWtBasedList. *							modify class variable ssWtBasedList *							defect 8508 Ver 5.2.2 Fix 7 * J Zwiener	02/01/2006	Add a month to Disabled Placard *							expiration. *							Temporary hard-coding is used so that *							a subsequent release can streamline *							the definition of PrmtValidtyPeriod *							in Acct_Codes table. *							modify setTimedPrmtExpDtAndTime() *							defect 8443 Ver 5.2.2 Fix 8 * B Hargrove	10/09/2006	Do not allow Temp Addl weight event for  * 							Regular or Standard Exempts.  *							modify initTempAddlWt() *							defect 8900 Ver Exempts * K Harrell	09/16/2008	Remove temporary coding for BPM, etc. * 							Where appropriate, translate PrmtValidtyPeriod * 							 into months and add to determine the  * 							 Expiration Date.  * 							modify setTimedPrmtExpDtAndTime() * 							defect 8586 Ver Defect_POS_B  * K Harrell	10/21/2008	Add processing for calling Server functions. * 							Modify mechanism for determining if  * 							 Disabled Placard transaction.  * 							modify processData(), setTimedPrmtTime()  * 							defect 9831 Ver Defect_POS_B * K Harrell	11/01/2008	On RTSException, Cache call to Reset  * 							InprocsIndi on RTS_DSABLD_PLCRD_CUST * 							add resetInProcs()   * 							modify processData() *  						defect 9831 Ver Defect_POS_B  * B Hargrove	01/06/2009	To determine if a RegClassCd is 'temp addl  * 							weight allowed', use the new column in  * 							Common Fees table, not the old hard-coded * 							list of RegClassCds. * 							modify isWeightBasedFee() * 							delete WEIGHT_BASED_LIST *  						defect 9129 Ver Defect_POS_D  * K Harrell	07/16/2009	Do not validate RegEffDate() * 							modify initTempAddlWt()  * 							defect 8414 Ver Defect_POS_F * K Harrell	08/17/2009  Implement UtilityMethods.addPCLandSaveReport() * 							delete saveReports() * 							modify processData()  * 							defect 8628 Ver Defect_POS_F * K Harrell	11/02/2009	Handle DB Down/Server Down via   * 							 checking returned Boolean * 							modify resetInProcs() * 							defect 10254 Ver Defect_POS_G * K Harrell 	05/24/2010	modify processData()  * 							defect 10491 Ver 6.5.0     * K Harrell	06/01/2010	add assignVIAllocData() * 							modify processData()  * 							defect 10491 Ver 6.5.0  * K Harrell	08/13/2010	On Same Vehicle, make new Effective Date one  * 							 last Expiration Date + one second * 							modify initTimedPrmt()  * 							defect 10491 Ver 6.5.0  * K Harrell	07/27/2011	Include check for Regional AcctItmCds for  * 							72PT-R & 144PT-R.  * 							modify setTimedPrmtExpDtAndTime()  * 							defect 10844 Ver 6.8.0  * --------------------------------------------------------------------- *//** * Main entry class for miscellaneous registration client business layer. *  * @version	6.8.0 			07/27/2011 * @author	Joseph Kwik * <br>Creation Date:		11/07/2001 17:20:12 *//* &MiscellaneousRegClientBusiness& */public class MiscellaneousRegClientBusiness{/* &MiscellaneousRegClientBusiness'MIDNIGHT_CACHE_VERSION& */	private final static int MIDNIGHT_CACHE_VERSION = 2400;/* &MiscellaneousRegClientBusiness'MIDNIGHT_HOUR& */	private final static int MIDNIGHT_HOUR = 0;/* &MiscellaneousRegClientBusiness'NO_TIME_CACHE_VERSION& */	private final static int NO_TIME_CACHE_VERSION = 0;/* &MiscellaneousRegClientBusiness'NOON_CACHE_VERSION& */	private final static int NOON_CACHE_VERSION = 1200;/* &MiscellaneousRegClientBusiness'NOON_HOUR& */	private final static int NOON_HOUR = 12;	/**	 * Assign VIAlocData 	 * 	 * @param aiRegClassCd	 * @return Object 	 *//* &MiscellaneousRegClientBusiness.assignVIAllocData& */	private static PermitData assignVIAllocData(		Object aaObject,		PermitData aaPrmtData)	{		if (aaObject != null			&& aaObject instanceof InventoryAllocationData)		{			InventoryAllocationData laInvAllocData =				(InventoryAllocationData) aaObject;			aaPrmtData.setVIAllocData(laInvAllocData);			aaPrmtData.setPrmtNo(laInvAllocData.getInvItmNo());		}		return aaPrmtData;	}	/**	 * Determines if reg class code is weight-based fee.	 * 	 * @param aiRegClassCd int 	 * @return boolean return true if reg class code is weight-based.	 *//* &MiscellaneousRegClientBusiness.isWeightBasedFee& */	public static boolean isWeightBasedFee(int aiRegClassCd)	{		// defect 9219		// Use new Common Fees column, not old hard-coded list 		//StringTokenizer laWtBasedList =		//	new StringTokenizer(WEIGHT_BASED_LIST, " ");		//String lsItem;		//String lsRegClassCd = Integer.toString(aiRegClassCd);		boolean lbRetVal = false;		//while (laWtBasedList.hasMoreTokens())		//{		//	lsItem = laWtBasedList.nextToken();		//	if (lsItem.equalsIgnoreCase(lsRegClassCd))		//	{		//		lbRetVal = true;		//		break;		//	}		//}		int liRTSEffDate = new RTSDate().getYYYYMMDDDate();		CommonFeesData laCommonFeesData =			CommonFeesCache.getCommonFee(aiRegClassCd, liRTSEffDate);		if (laCommonFeesData.getTempAddlWtAllowdIndi() == 1)		{			lbRetVal = true;		}		// end defect 9129				return lbRetVal;	}	/**	 * Set expiration date and time for timed permit.	 *  	 * @param aaTimedPermitData TimedPermitData 	 *//* &MiscellaneousRegClientBusiness.setTimedPrmtExpDtAndTime& */	public static void setTimedPrmtExpDtAndTime(TimedPermitData aaTimedPermitData)	{		// Set expiration date and time for permit and get 		// PrmtStndrdExpTime from cache		AccountCodesData laData =			AccountCodesCache.getAcctCd(				aaTimedPermitData.getTimedPrmtType(),				aaTimedPermitData.getRTSDateEffDt().getYYYYMMDDDate());		if (laData != null)		{			// defect 8586			// Use "Banking days/month, i.e. 30 days = 1 month when 			//  > 30 days or TAWPTMO			// Previously just added the days. 			// defect 8443			// Temporary Hard-coding for 5.2.2.8			// if (laData.getAcctItmCd().equals("BPM")			//		|| laData.getAcctItmCd().equals("RPNM"))			//	{			//		laData.setPrmtValidtyPeriod(1485);			//	}			//	else if (			//		laData.getAcctItmCd().equals("BTM")			//			|| laData.getAcctItmCd().equals("RTNM"))			//	{			//		laData.setPrmtValidtyPeriod(210);			//	}			// end defect 8443			// Get effective date			RTSDate laEffDate = aaTimedPermitData.getRTSDateEffDt();			RTSDate laRTSDateExpDt = laEffDate;			// 30 for TAWPTMO means 1 month, others mean 30 days			if (laData.getPrmtValidtyPeriod() > 30				|| laData.getAcctItmCd().equals(					AcctCdConstant.TEMP_ADDL_WT_1_MO))			{				int liNoMonths = laData.getPrmtValidtyPeriod() / 30;				laRTSDateExpDt =					laRTSDateExpDt.add(Calendar.MONTH, liNoMonths);			}			else			{				laRTSDateExpDt =					laRTSDateExpDt.add(						RTSDate.DATE,						laData.getPrmtValidtyPeriod());			}			// end defect 8586 			// Set expiration date and time			if (laData.getPrmtStndrdExpTime() == NOON_CACHE_VERSION)			{				aaTimedPermitData.setRTSDateExpDt(					new RTSDate(						laRTSDateExpDt.getYear(),						laRTSDateExpDt.getMonth(),						laRTSDateExpDt.getDate(),						NOON_HOUR,						0,						0,						0));			}			else if (				laData.getPrmtStndrdExpTime()					== MIDNIGHT_CACHE_VERSION)			{				aaTimedPermitData.setRTSDateExpDt(					new RTSDate(						laRTSDateExpDt.getYear(),						laRTSDateExpDt.getMonth(),						laRTSDateExpDt.getDate(),						MIDNIGHT_HOUR,						0,						0,						0));			}			else if (				laData.getPrmtStndrdExpTime() == NO_TIME_CACHE_VERSION)			{				// defect 10844 				//				if (aaTimedPermitData				//					.getTimedPrmtType()				//					.equals(TransCdConstant.PT72)				//					|| aaTimedPermitData.getTimedPrmtType().equals(				//						TransCdConstant.PT144))				if (aaTimedPermitData					.getTimedPrmtType()					.startsWith(TransCdConstant.PT72)					|| aaTimedPermitData.getTimedPrmtType().startsWith(						TransCdConstant.PT144))				{					// end defect 10844					aaTimedPermitData.setRTSDateExpDt(						new RTSDate(							laRTSDateExpDt.getYear(),							laRTSDateExpDt.getMonth(),							laRTSDateExpDt.getDate(),							laRTSDateExpDt.getHour(),							laRTSDateExpDt.getMinute(),							laRTSDateExpDt.getSecond(),							laRTSDateExpDt.getMillisecond()));				}				else				{					aaTimedPermitData.setRTSDateExpDt(						new RTSDate(							laRTSDateExpDt.getYear(),							laRTSDateExpDt.getMonth(),							laRTSDateExpDt.getDate(),							0,							0,							0,							0));				}			}		}	}	/**	* Set time based on miscellaneous reg event.	*  	* @param aaTimedPermitData TimedPermitData  	*//* &MiscellaneousRegClientBusiness.setTimedPrmtTime& */	public static void setTimedPrmtTime(TimedPermitData aaTimedPermitData)	{		// defect 9831 		String lsTransCd = aaTimedPermitData.getTimedPrmtType();		if (UtilityMethods			.getEventType(lsTransCd)			.equals(TransCdConstant.ADD_DP_EVENT_TYPE)			|| UtilityMethods.getEventType(lsTransCd).equals(				TransCdConstant.RPL_DP_EVENT_TYPE))			//			.getTimedPrmtType()			//			.equals(TransCdConstant.BPM)			//			|| aaTimedPermitData.getTimedPrmtType().equals(			//				TransCdConstant.BTM)			//			|| aaTimedPermitData.getTimedPrmtType().equals(			//				TransCdConstant.RPNM)			//			|| aaTimedPermitData.getTimedPrmtType().equals(			//				TransCdConstant.RTNM))			// end defect 9831 		{			RTSDate laEffDate = aaTimedPermitData.getRTSDateEffDt();			aaTimedPermitData.setRTSDateEffDt(				new RTSDate(					laEffDate.getYear(),					laEffDate.getMonth(),					1,					0,					0,					0,					0));		}		else if (			aaTimedPermitData.getTimedPrmtType().equals(				TransCdConstant.NRIPT)				|| aaTimedPermitData.getTimedPrmtType().equals(					TransCdConstant.NROPT)				|| aaTimedPermitData.getTimedPrmtType().equals(					TransCdConstant.TAWPT))		{			RTSDate laEffDate = aaTimedPermitData.getRTSDateEffDt();			// set effective time to zero			aaTimedPermitData.setRTSDateEffDt(				new RTSDate(					laEffDate.getYear(),					laEffDate.getMonth(),					laEffDate.getDate(),					0,					0,					0,					0));		}	}	// defect 9129	//private final static String WEIGHT_BASED_LIST =	//	"10 15 31 32 35 36 37 43 46 47 49 54 57 60 61 62 70 71 72 73 "	//		+ "74 75 76";	// end defect 9129	/**	 * MiscellaneousRegClientBusiness constructor comment.	 *//* &MiscellaneousRegClientBusiness.MiscellaneousRegClientBusiness& */	public MiscellaneousRegClientBusiness()	{		super();	}	/**	 * Initialize temporary additional weight.	 * 	 * @param aaData Object VehicleInquiryData 	 * @return Object VehicleInquiryData 	 * @throws RTSException	 *//* &MiscellaneousRegClientBusiness.initTempAddlWt& */	private Object initTempAddlWt(Object aaData) throws RTSException	{		VehicleInquiryData laVehInqData = (VehicleInquiryData) aaData;		RegistrationData laRegData =			laVehInqData.getMfVehicleData().getRegData();		// Test for valid doc types		RegistrationVerify.verifyDocType(laVehInqData);		// defect 8900		// Do not allow Temp Addl Wt for Standard or Regular Exempts		if (laRegData.getExmptIndi() == 1)		{			throw new RTSException(471);		}		// end defect 8900				// Check for valid registration class		// Cannot use cache since it returns codes which are not in the 		// hard-coded list in RTS1.		// defect 8900		// Note:  the hard-coded list in constant 'WEIGHT_BASED_LIST' are		// all FeeCalcCat = 2 (weight based), but they are not ALL of 		// FeeCalcCat = 2. They leave out things like motor bus, travel 		// trailer, etc (RegClassCds 21, 26, 34, 38, 40, 41, 50, 53,		// 56, and 59). To determine eligibility for Perm\Temp Addl Wt,		// we should probably be looking more at Vehicle Class and		// RTS_REGIS_CLASS CaryngCapReqd = 1.		// end defect 8900		// Test for weight-based registration classes using Registration		// Weight Fees cache		// Vector lvData = RegistrationWeightFeesCache.getRegWtFee(		// lRegData.getRegClassCd(), RTSDate.getCurrentDate().		// getYYYYMMDDDate(), lRegData.getVehGrossWt());		if (!isWeightBasedFee(laRegData.getRegClassCd()))		{			throw new RTSException(370);		}		// Check to see if currently registered vehicle		if (CommonValidations			.isRegistrationExpired(				laRegData.getRegExpMo(),				laRegData.getRegExpYr()))		{			throw new RTSException(58);		}		// Check for valid registration effective date		// defect 8414 		//		if (laRegData.getRegEffDt() < 1)		//		{		//			throw new RTSException(372);		//		}		// end defect 8414 		return aaData;	}	/**	 * Initialize timed permit frame.	 * 	 * @param aaData Object TimedPermitData 	 * @return Object	 * @throws RTSException 	 *//* &MiscellaneousRegClientBusiness.initTimedPrmt& */	private Object initTimedPrmt(Object aaData) throws RTSException	{		TimedPermitData laTimedPrmtData = (TimedPermitData) aaData;		RTSDate laEffDate = RTSDate.getCurrentDate();		if (laTimedPrmtData.isSameVeh())		{			laEffDate =				laTimedPrmtData.getRTSDateExpDt().add(					RTSDate.SECOND,					1);		}		laTimedPrmtData.setRTSDateEffDt(laEffDate);		setTimedPrmtExpDtAndTime(laTimedPrmtData);		return laTimedPrmtData;	}	/**	 * Main entry method for Miscellaneous Registration Client Business.	 * This method will be called by the BusinessInterface and will 	 * parse the command based on the functionId.	 * It will then decide what to do by either calling an internal 	 * method for any business logic that can be handled by the client.	 * 	 * @param aiModule int 	 * @param aiFunctionId int 	 * @param aaData java.lang.Object 	 * @return java.lang.Object 	 * @throws com.txdot.isd.rts.services.exception.RTSException	 *//* &MiscellaneousRegClientBusiness.processData& */	public Object processData(		int aiModule,		int aiFunctionId,		Object aaData)		throws RTSException	{		switch (aiFunctionId)		{			case MiscellaneousRegConstant.NO_DATA_TO_BUSINESS :				{					return aaData;				}				// Intentionally dropping through				// INIT_TIMED_PERMIT, INIT_DISABLED_PARKING_CARD  			case MiscellaneousRegConstant.INIT_TIMED_PERMIT :			case MiscellaneousRegConstant.INIT_DISABLED_PARKING_CARD :				{					return initTimedPrmt(aaData);				}			case MiscellaneousRegConstant.INIT_TEMP_ADDL_WEIGHT :				{					return initTempAddlWt(aaData);				}				// defect 9831			case MiscellaneousRegConstant				.GENERATE_DSABLD_PLCRD_REPORT :				{					// defect 8628 					return UtilityMethods.addPCLandSaveReport(						Comm.sendToServer(							aiModule,							aiFunctionId,							aaData));					// end defect 8628 				}				// defect 10491			case InventoryConstant.INV_VI_RELEASE_HOLD :			case InventoryConstant.INV_GET_NEXT_VI_ITEM_NO :				{					PermitData laPrmtData = (PermitData) aaData;					InventoryAllocationData laInvAllocData =						laPrmtData.getVIAllocData();					return assignVIAllocData(						Comm.sendToServer(							GeneralConstant.INVENTORY,							aiFunctionId,							laInvAllocData),						laPrmtData);				}				// end defect 10491 			case MiscellaneousRegConstant.RESETINPROCS :				{					return resetInProcs(aaData);				}				// Intentionally dropping through for SEARCH, UPDATE, 				//  INSERT, CHKIFVOIDABLD, SETINPROCS, GET_PRMT			case MiscellaneousRegConstant.SEARCH :			case MiscellaneousRegConstant.UPDATE :			case MiscellaneousRegConstant.INSERT :			case MiscellaneousRegConstant.CHKIFVOIDABLE :			case MiscellaneousRegConstant.SETINPROCS :				// defect 10491 			case MiscellaneousRegConstant.PRMTINQ :				// end defect 10491  				{					return Comm.sendToServer(						aiModule,						aiFunctionId,						aaData);				}			default :				{					return null;				}		}	}	/**	 * Reset Customer Id In Process 	 * 	 * @param aaData	 * @return Object 	 * @throws RTSException 	 *//* &MiscellaneousRegClientBusiness.resetInProcs& */	private Object resetInProcs(Object aaData) throws RTSException	{		DisabledPlacardCustomerData laDPCustData =			(DisabledPlacardCustomerData) aaData;		RTSDate laRTSDateTrans = new RTSDate();		String lsHour = String.valueOf(laRTSDateTrans.getHour());		String lsMinute = String.valueOf(laRTSDateTrans.getMinute());		String lsSecond = String.valueOf(laRTSDateTrans.getSecond());		int liTransTime =			Integer.parseInt(				UtilityMethods.addPadding(					new String[] { lsHour, lsMinute, lsSecond },					new int[] { 2, 2, 2 },					CommonConstant.STR_ZERO));		laDPCustData.setTransAMDate(laRTSDateTrans.getAMDate());		laDPCustData.setTransTime(liTransTime);		TransactionCacheData laTransactionCacheData =			new TransactionCacheData();		laTransactionCacheData.setObj(laDPCustData);		laTransactionCacheData.setProcName(			TransactionCacheData.RESETINPROCESS);		Vector lvTrans = new Vector();		lvTrans.addElement(laTransactionCacheData);		try		{			// defect 10254 			// Transaction.postTrans() returns Boolean if DB_DOWN, 			//    SERVER_DOWN 			// Transaction.postTrans(lvTrans); 			Object laResult = Transaction.postTrans(lvTrans);			if (laResult != null				&& laResult instanceof Boolean				&& !((Boolean) laResult).booleanValue())			{				Transaction.writeToCache(lvTrans);			}		}		catch (RTSException aeRTSEx)		{			//Transaction.writeToCache(lvTrans);			throw aeRTSEx;		}		// end defect 10254		return null;	}	//		/**	//		 * Method to save the reports to the report directory.	//		 * 	//		 * @param aaData Object The report to be saved in the form of 	//		 *		ReportSearchData.	//		 * @return Vector of reports with each in the form of 	//		 *		ReportSearchData.	//		 * @throws RTSException Thrown when there is an error.	//		 */	//		private Vector saveReports(Object aaData) throws RTSException	//		{	//			ReportSearchData laRptSearchData = (ReportSearchData) aaData;	//			UtilityMethods laUtil = new UtilityMethods();	//			Print laPrint = new Print();	//			Vector lvReports = new Vector();	//	//			String lsPageProps = laPrint.getDefaultPageProps();	//			laRptSearchData.setIntKey2(ReportConstant.LANDSCAPE);	//			lsPageProps =	//				lsPageProps.substring(0, 2)	//					+ Print.getPRINT_LANDSCAPE()	//					+ lsPageProps.substring(2);	//			// Always print from tray 2	//			String lsRpt =	//				lsPageProps	//					+ Print.getPRINT_TRAY_2()	//					+ System.getProperty("line.separator")	//					+ laRptSearchData.getKey1();	//			String lsFileName =	//				laUtil.saveReport(	//					lsRpt,	//					laRptSearchData.getKey2(),	//					laRptSearchData.getIntKey1());	//			laRptSearchData.setKey1(lsFileName);	//			lvReports.add(laRptSearchData);	//			return lvReports;	//		}}/* #MiscellaneousRegClientBusiness# */