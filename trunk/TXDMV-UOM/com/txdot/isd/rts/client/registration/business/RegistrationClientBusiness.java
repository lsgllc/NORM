package com.txdot.isd.rts.client.registration.business;

import java.util.Vector;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.title.ui.TitleClientUtilityMethods;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.OrganizationNumberCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.cache.RegistrationClassCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.*;

/* 
 * RegistrationClientBusiness.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		09/19/2001	Modify per first code review.
 * J Kwik		04/30/2002	modified checkIfRegExpired 
 * 							method to use 
 *							CommonValidations.isRegistrationExpired 
 *							method.
 *							defect 3714 
 * MAbs			05/14/2002	Checked for Null Pointers
 * 							defect 3910
 * S Govindappa 02/24/2002  Made changes to initReg003()to reset the 
 * 							UnregisterVehIndi for any registration event.
 * 							defect 5338 
 * B Hargrove   12/05/2003  Changed edit for Vehicle Weight to test for
 * 							'<=' (it was test for '<'). Add error 431 if 
 * 							weight is equal.
 *							modify validateWeight()
 *							defect 6566  Ver 5.1.5 Fix 2
 * K Harrell	03/19/2004	5.2.0 Merge.
 *							modify processData()
 * 							Ver 5.2.0
 * K Harrell	12/16/2004	Correct Internal Data Problem with Issue
 *							Driver's Ed. (Reversal of 3/19/2004 action) 
 *							Also formatted all methods, standardized all
 *							variable names. 
 *							deprecate procsQuickCounterIssueInv()
 *							add procsRegIssueInv()
 *							modify processData()
 *							defect 7783 Ver 5.2.2 
 * Ray Rowehl	02/08/2005	Change import for Transaction.
 * 							modify import
 * 							defect 7705 Ver 5.2.3 
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7894 Ver 5.2.3
 * K Harrell	05/02/2005	rename INV014 to INV003 
 * 							defect 6966 Ver 5.2.3
 * K Harrell	05/17/2005	rename RegistrationClassData.getDiesleReqd()
 * 							to getDieselReqd()
 * 							defect 7786  Ver 5.2.3    
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                
 * B Hargrove	06/23/2005	Format code to standards.
 * 							defect 7894 Ver 5.2.3
 * B Hargrove	07/15/2005	Refactor\Move 
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
  * K Harrell	11/13/2005	Make procsHvyVehUseTaxVerify public
 *							modify procsHvyVehUseTaxVerify()
 *							defect 8404 Ver 5.2.2 Fix 7 
 * B Hargrove	01/24/2006	Since Seasonal Ag Combo (RegClassCd 76) was
 *							created, change to look up Emission Fee
 *							Percent from CommonFees table.
 *							add caCommFeesData, ZERODOLLAR
 *							modify initAddlInfo(), initReg003()
 *							delete COMBINATION_REG_CLASS_CD
 *							defect 8515, 8516 Ver 5.2.2 Fix 8
 * B Hargrove	01/30/2006	Fix lookup to CommonFees table.
 *							modify initAddlInfo(), initReg003()
 *							defect 8516 Ver 5.2.2 Fix 8
 * B Hargrove	10/09/2006 	Handle setting masks and charge fee indis
 * 							depending on Exempt Indi 
 * 							modify initAddlInfo(), initReg003(),
 * 							procsAddlInfoMasks()
 *							defect 8900 Ver Exempts
 * Min Wang		10/10/2006	New Requirement for handling plate age 
 * 							modify checkIfRegExpired(), 
 * 							procsAddlInfoMasks()
 *							defect 8901 Ver Exempts
 * K Harrell	10/22/2006	Continued work on Masks
 * 							delete TOKEN_TRAILER,ZERODOLLAR,
 * 							   caCommFeesData
 * 							delete procsHvyVehUseTaxVerify()
 * 							modify initAddlInfo(),procsAddlInfoMasks(),
 * 							validateWeight(),initReg003()
 * 							defect 8900 Ver Exempts
 * K Harrell	10/31/2006	Do not reevaluate Charge Registration
 * 							Emissions Fee when initialize REG039
 * 							modify initAddlInfo()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/21/2006	Only force replacement if RegisRenewals 
 * 							NeedsProgramCd = "R" 
 * 							add PLT_REPLACEMENT_REQUIRED 
 * 							modify procsAddlInfoMasks()
 * 							defect 9025 Ver Exempts
 * K Harrell	02/05/2007	Use PlateTypeCache vs. 
 * 							RegistrationRenewalsCache
 * 							modify procsAddlInfoMasks()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/03/2007  No longer prompt for alternate replacements
 * 							for disabled vehicles.  Handled by REG011.
 * 							delete MOPED_PLT, MOTORCHYCLE_PLT,
 * 							PASSENGER_PLT,TRUCK_PLT,MOPED,MOTORCYCLE,
 * 							PASSENGER_LESS_EQL_6000,
 *							PASSENGER_MORETHAN_6000,
 *							TRUCK_LESS_EQL_1_TON
 *							delete procsDsabldPersnPlt()
 * 							add procsReplPltInv()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/26/2007	Use Special Plate Age for New Plates 
 * 							Required analysis
 * 							modify procsAddlInfoMasks()
 * 							defect 9085 Ver Special Plates  
 * B Hargrove	05/14/2007 	Also check for FeeCalcCat = 4 (no regis fees)
 * 							when setting "don't charge fee" indi 
 * 							modify procsAddlInfoMasks()
 *							defect 9126 Ver Special Plates
 * K Harrell	05/29/2007	Do not check for FeeCalcCat = 4 (see prior)
 * 							modify procsAddlInfoMasks()
 * 							defect 9085 Ver Special Plates
 * K Harrell	11/13/2007	Enable Plate Transfer Fee checkbox on REG039
 * 							 if Renew, Exchange and applicable. Code and  
 * 							 documentation cleanup.  
 * 							modify procsAddlInfoMasks()
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	06/02/2008	New Plates are required if Renew & plates 
 * 							    are removed
 * 							modify procsAddlInfoMasks()
 * 							defect 9670 Ver Defect_POS_A
 * K Harrell	09/11/2008	Remove enabling of New Plates Desired 
 * 							  for PTO eligible vehicles (defect 9368) 	
 * 							modify procsAddlInfoMasks()
 * 							defect 9824 Ver Defect_POS_B 
 * B Hargrove 	06/01/2009 	Add Flashdrive option to RSPS Subcon.
 *                   		modify processData()
 * 							defect 10064 Ver Defect_POS_F  
 * Min Wang		07/28/2010	Add processing for Tow Truck Mask.
 * 							modify procsAddlInfoMasks()
 * 							defect 10007 Ver 6.5.0
 * K Harrell	10/10/2011	modify procsAddlInfoMasks() 
 * 							defect 11030 Ver 6.9.0 
 * K Harrell	11/07/2011	delete MAX_GROSS_WEIGHT
 * 							modify validateWeight() 
 * 							defect 10959 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Main entry class for registration client business layer.
 * 
 * @version	6.9.0 			11/07/2011
 * @author 	Joseph Kwik
 * @since 					08/21/2001 15:59:25
 */
public class RegistrationClientBusiness
{
	// Constants - String  
	private final static String ERRMSG_ERROR = "ERROR";
	private final static String ERRMSG_INVALID_TRANSCD =
		"Invalid TransCode";
	private final static String PLT_REPLACEMENT_DEFERRED = "D";
	private final static String PLT_REPLACEMENT_REQUIRED = "R";
	private final static String REGSTKRCD_SPS = "SPS";
	private final static String REGSTKRCD_US = "US";
	private final static String REGSTKRCD_WS = "WS";
	private final static String REGSTKRCD_WSPS = "WSPS";

	// Constants - int 
	// defect 10959 
	// private final static int MAX_GROSS_WEIGHT = 80000;
	// end defect 10959 
	private final static int REGEXPYR_1996 = 1996;
	private final static int THIRD_OF_VEH_WEIGHT = 3;

	private RegistrationSpecialExemptions caRegSpclExmpts =
		new RegistrationSpecialExemptions();

	/**
	 * Get the registration class data object from cache.
	 * 
	 * @param aaVehInqData 
	 * @return RegistrationClassData  
	 * @throws RTSException 
	 */
	public static RegistrationClassData getRegClassDataObject(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationClassData laRegClassData =
			RegistrationClassCache.getRegisClass(
				aaVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehClassCd(),
				aaVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegClassCd(),
				aaVehInqData.getRTSEffDt());

		if (laRegClassData == null)
		{
			laRegClassData = new RegistrationClassData();
		}
		return laRegClassData;
	}

	/**
	 * Manages plate inventory for registration replacement.
	 * 
	 * @param aaVehInqData 
	 */
	public static void procsReplPltInv(VehicleInquiryData aaVehInqData)
	{
		VehicleInquiryData laVehInqData = aaVehInqData;
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) laVehInqData
				.getValidationObject();

		// Determine if new plate should be added to inventory list
		// Test if new plate is required
		if (laRegValidData.getNewPltReplIndi() == 1)
		{
			Vector lvInvItms;
			if (laRegValidData.getInvItms() == null)
			{
				lvInvItms = new Vector();
			}
			else
			{
				lvInvItms = laRegValidData.getInvItms();
			}
			ProcessInventoryData laInvData = new ProcessInventoryData();
			laInvData.setTransEmpId(SystemProperty.getCurrentEmpId());
			laInvData.setSubstaId(SystemProperty.getSubStationId());
			laInvData.setTransWsId(
				Integer.toString(SystemProperty.getWorkStationId()));
			laInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());

			laInvData.setItmCd(laRegValidData.getReplPltCd());
			lvInvItms.add(laInvData);
			laRegValidData.setInvItms(lvInvItms);
		}
	}

	/**
	 * RegClientBusiness constructor comment.
	 */
	public RegistrationClientBusiness()
	{
		super();
	}
	/**
	 * Determine if registration is expired and set the indicator in the 
	 * RegistrationValidationData object.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	private void checkIfRegExpired(VehicleInquiryData aaVehInqData)
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();
		// get reg exp mo and yr.
		int liRegExpMo = laRegData.getRegExpMo();
		int liRegExpYr = laRegData.getRegExpYr();

		// compare expiration date to current rts date
		// defect 3714
		boolean lbExpired =
			CommonValidations.isRegistrationExpired(
				liRegExpMo,
				liRegExpYr);

		// Set registration expiration status to expired 
		if (lbExpired)
		{
			laRegValidData.setRegistrationExpired(1);

			// defect 8901
			// if (laRegData.getRegExpYr() > 0)
			// {
			//:Assign MfVeh{Row}.RegPltAge:=Year(TransAMDate)-
			//  MfVeh{0}.RegExpYr+MfVeh{0}.RegPltAge
			//	laRegData.setRegPltAge(
			//		laRTSDateCurrent.getYear()
			//			- laRegData.getRegExpYr()
			//			+ laRegData.getRegPltAge());
			// }
			// end defect 8901
		}
		else
		{
			laRegValidData.setRegistrationExpired(0);
		}
	}
	/**
	 * Eliminate 1997 special plate window and plate stickers.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	private void convertSpclPltWindwAndPltStckrs(VehicleInquiryData aaVehInqData)
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();
		if ((laRegValidData
			.getTransCode()
			.equals(TransCdConstant.RENEW))
			&& (laRegData.getRegExpYr() == REGEXPYR_1996))
		{
			if (laRegData.getRegStkrCd().equals(REGSTKRCD_SPS))
			{
				laRegData.setRegStkrCd(REGSTKRCD_US);
			}
			else if (laRegData.getRegStkrCd().equals(REGSTKRCD_WSPS))
			{
				laRegData.setRegStkrCd(REGSTKRCD_WS);
			}
		}
	}

	/**
	 * Manage retrieving allocated inv items for same vehicle.
	 * 
	 * @return Vector Allocated Inventory items
	 * @throws RTSException The exception description.
	 */
	private Vector getSameVehAllocInvItms() throws RTSException
	{
		if (Transaction.getCumulativeTransIndi() == 2)
		{
			CommonClientBusiness laCommonBus =
				new CommonClientBusiness();
			Object laObj =
				laCommonBus.processData(
					GeneralConstant.COMMON,
					CommonConstant.GET_SAVED_INVENTORY,
					null);
			if (laObj instanceof Vector)
			{
				return (Vector) laObj;
			}
		}
		return null;
	}

	/**
	 * Initialize additional info frame.
	 * 
	 * @return VehicleInquiryData
	 * @param aaData VehicleInquiryData
	 * @throws RTSException  
	 */
	private Object initAddlInfo(Object aaData) throws RTSException
	{
		VehicleInquiryData laVehInqData = (VehicleInquiryData) aaData;
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) laVehInqData
				.getValidationObject();
		String lsTransCd = laRegValidData.getTransCode();

		// Set Charge Penalty indi
		if (lsTransCd.equals(TransCdConstant.RENEW))
		{
			if (laRegValidData.getApprndComptCntyNo() != 0)
			{
				laRegValidData.setRegPnltyChrgIndi(1);
			}
		}

		// Set diesel mask
		if (lsTransCd.equals(TransCdConstant.RENEW)
			|| lsTransCd.equals(TransCdConstant.CORREG)
			|| lsTransCd.equals(TransCdConstant.EXCH)
			|| lsTransCd.equals(TransCdConstant.PAWT))
		{
			RegistrationClassData laRegClassData = null;
			if (laRegValidData.getInvalidClassPltStkrIndi() == 1)

				// reg data has been cleared in REG003.resetRegInfo() so 
				// use orig veh data.
			{
				laRegClassData =
					getRegClassDataObject(
						laRegValidData.getOrigVehInqData());
			}
			else
			{
				laRegClassData = getRegClassDataObject(laVehInqData);
			}
			int liRetVal = laRegClassData.getDieselReqd();
			if (liRetVal == 1)
			{
				laRegValidData.setDieselMask(true);
			}
			else
			{
				laRegValidData.setDieselMask(false);
			}
			// defect 8900 
			// This should not be reevaluated upon entry.  
			// Initially set from initReg003(). Subsequently, set
			//  by user or modification within REG039.   
			// Set Emission Fee
			// defect 8516 
			//RegistrationClientUtilityMethods.procsEmissions(
			//	laVehInqData);
			// end defect 8516 / 8900  
		}
		// Set Change Registration Mask
		if (lsTransCd.equals(TransCdConstant.RENEW))
		{
			if (laRegValidData.getRegPnltyChrgIndi() == 0
				|| laRegValidData.getInvalidClassPltStkrIndi() == 1)
			{
				laRegValidData.setChngRegMask(true);
			}
		}
		else if (lsTransCd.equals(TransCdConstant.EXCH))
		{
			laRegValidData.setChngRegMask(true);
		}
		else
		{
			laRegValidData.setChngRegMask(false);
		}

		// Set Change Vehicle Weight Mask
		if (laVehInqData
			.getMfVehicleData()
			.getVehicleData()
			.getFxdWtIndi()
			== 1)
		{
			laRegValidData.setGrossWtMask(false);
		}
		else if (laRegValidData.getInvalidMinGrossWtIndi() == 1)
		{
			laRegValidData.setGrossWtMask(true);
		}
		//	Set ChrgFeeIndi to 1 if ChrgFeeMask disabled
		// defect 8900
		// Do not set to 1 just because it is disabled		
		//if (!laRegValidData.getChrgFeeMask())
		//{
		//	laRegValidData.setChrgFeeIndi(1);
		//}
		// end defect 8900

		return laVehInqData;
	}

	/**
	 * Initialization for Registration REG003 screen.
	 * 
	 * @param aaData 
	 * @return Object  
	 * @throws RTSException  
	 */
	private Object initReg003(Object aaData) throws RTSException
	{
		VehicleInquiryData laVehInqData = (VehicleInquiryData) aaData;
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) laVehInqData
				.getValidationObject();
		String lsTransCd = laRegValidData.getTransCode();
		if (lsTransCd == null || lsTransCd.equals(""))
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				ERRMSG_INVALID_TRANSCD,
				ERRMSG_ERROR);
		}
		MFVehicleData laMFVehData = laVehInqData.getMfVehicleData();
		if (laMFVehData != null && laMFVehData.getRegData() != null)
		{
			RegistrationData laRegData =
				(RegistrationData) laMFVehData.getRegData();
			laRegData.setUnregisterVehIndi(0);

			// Determine if registration is expired
			// defect 8900
			// Do not check for expired if Standard Exempt
			// checkIfRegExpired(laVehInqData);
			if (!CommonFeesCache
				.isStandardExempt(laRegData.getRegClassCd()))
			{
				checkIfRegExpired(laVehInqData);
			}
			// end defect 8900
		}

		// Eliminate 1997 special plate window and plate stickers
		convertSpclPltWindwAndPltStckrs(laVehInqData);
		// Send message to RegistrationSpecialExempts.
		Vector lvExceptions = new Vector();
		lvExceptions = caRegSpclExmpts.testSpclExmpts(laVehInqData);
		procsAddlInfoMasks(laVehInqData);
		if (lvExceptions.size() > 0)
		{
			laRegValidData.setRTSExceptions(lvExceptions);
		}
		// handle same vehicle allocated inventory items
		laRegValidData.setSameVehAllocInvItms(getSameVehAllocInvItms());

		// Set Emission Fee
		// defect 8516 / 8900
		RegistrationClientUtilityMethods.procsEmissions(laVehInqData);
		// end defect 8516 / 8900  

		laRegValidData.setInitREG003(true);
		return laVehInqData;
	}

	/**
	 * Main entry method for Registration Client Business.
	 *
	 * @param aiModule  
	 * @param aiFunctionId  
	 * @param aaData 
	 * @return Object   
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			case RegistrationConstant.NO_DATA_TO_BUSINESS :
				{
					return aaData;
				}
			case RegistrationConstant.INIT_REG003 :
				{
					return initReg003(aaData);
				}
			case RegistrationConstant.INIT_ADDL_INFO :
				{
					return initAddlInfo(aaData);
				}
			case RegistrationConstant.PROCESS_ISSUE_INV :
				{
					return procsRegIssueInv(aaData);
				}
				// SubcontractorClientBusiness
			case RegistrationConstant
				// defect 10064
				//.COPY_AND_VALIDATE_SUBCON_DISKETTE :
				.COPY_AND_VALIDATE_SUBCON_MEDIA :
				// end defect 10064				
				{
					// intentionally dropping through
				}
			case RegistrationConstant.GET_SUBCON_ALLOCATED_INV :
				{
					// intentionally dropping through
				}
			case RegistrationConstant.INIT_SUBCON_RENWL :
				{
					// intentionally dropping through
				}
				// defect 6966
			case RegistrationConstant.RELEASE_INV003_ITM :
				{
					// intentionally dropping through
				}
				// end defect 6966 
			case RegistrationConstant.CANCEL_SUBCON :
				{
					// intentionally dropping through
				}
			case RegistrationConstant.RESTORE_SUBCON_BUNDLE :
				{
					// intentionally dropping through
				}
			case RegistrationConstant
				.DEL_SELECTED_SUBCON_RENWL_RECORD :
				{
					// intentionally dropping through
				}
			case RegistrationConstant.GENERATE_SUBCON_REPORT_DRAFT :
				{
					// intentionally dropping through
				}
			case RegistrationConstant.PROCESS_SUBCON_RENWL :
				{
					// intentionally dropping through
				}
			case RegistrationConstant.PROC_COMPLETE_SUBCON_RENWL :
				{
					// intentionally dropping through
				}
			case RegistrationConstant.CANCEL_HELD_SUBCON :
				{
					// intentionally dropping through
				}
			case RegistrationConstant.CHECK_DISK_INVENTORY :
				{
					// intentionally dropping through
				}
			case RegistrationConstant.ADD_DISK_TRANS_AND_END_TRANS :
				{
					SubcontractorRenewalClientBusiness srBusiness =
						new SubcontractorRenewalClientBusiness();
					return srBusiness.processData(
						aiModule,
						aiFunctionId,
						aaData);
				}
			default :
				{
					return null;
				}
		}
	}

	/**
	 * Process masks for checkbox fields and indicators for additional 
	 * info form 
	 * 
	 * @param aaVehInqData 
	 * @throws RTSException  
	 */
	private void procsAddlInfoMasks(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		MFVehicleData laMFVehData = aaVehInqData.getMfVehicleData();
		// defect 9085 
		SpecialPlatesRegisData laSpclPltRegisData =
			laMFVehData.getSpclPltRegisData();
		// end defect 9085  
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();
		RegistrationData laRegData = laMFVehData.getRegData();
		String lsTransCd = laRegValidData.getTransCode();

		// get carrying capacity to determine if gross weight change allowed
		RegistrationClassData laRegClassData =
			getRegClassDataObject(aaVehInqData);
		int liCaryingCapReqd = laRegClassData.getCaryngCapReqd();
		laRegValidData.setGrossWtMask(false);

		// set default for all transCds
		// defect 8900 
		// Enable/Disable Charge Fee depending on Exempt Indi
		boolean lbStandardExempt =
			CommonFeesCache.isStandardExempt(laRegData.getRegClassCd());
		boolean lbExempt = laRegData.getExmptIndi() == 1;

		// Init ExemptIndi; ChrgFeeIndi (1/0 if Exempt; Else 0/1)
		laRegValidData.setExemptIndi(lbExempt ? 1 : 0);
		laRegValidData.setChrgFeeIndi(lbExempt ? 0 : 1);
		laRegValidData.setExemptMask(false);

		// Enable Fees if Exempt 
		laRegValidData.setChrgFeeMask(lbExempt && !lbStandardExempt);

		// Note: No Standard Exempt in Renew  
		if (lsTransCd.equals(TransCdConstant.RENEW))
		{
			laRegValidData.setProcsdByMailMask(true);
			laRegValidData.setEmissionsFeeMask(true);
			laRegValidData.setChngRegMask(true);
			laRegValidData.setApprndComptCntyNoMask(true);
			laRegValidData.setCorrtnEffDateMask(true);
			laRegValidData.setExemptMask(true);

			// Determine if gross weight change allowed
			if (liCaryingCapReqd == 1)
			{
				laRegValidData.setGrossWtMask(true);
			}
			// Determine if plate replacement is optional or mandatory
			
			// defect 9670 
			// New Plates Required if Plate Removed 
			if (laRegData.getPltRmvCd() != 0)
			{
				laRegValidData.setNewPltReplIndi(1);
				laRegValidData.setNewPltReplMask(false);
			}
			else
			{
				// defect 9085 
				// PlateTypeCache replaces RegistrationRenewalsCache
				// RegistrationRenewalsData laRegRenwlData =
				// 	RegistrationRenewalsCache.getRegRenwl(
				//		laRegData.getRegPltCd());
				PlateTypeData laPlateTypeData =
					PlateTypeCache.getPlateType(
						laRegData.getRegPltCd());

				if (laPlateTypeData != null)
				{
					boolean lbSpcl =
						!laPlateTypeData.getPltOwnrshpCd().equals(
							SpecialPlatesConstant.VEHICLE);

					if (laSpclPltRegisData == null && lbSpcl)
					{
						laRegValidData.setNewPltReplIndi(1);
						laRegValidData.setNewPltReplMask(false);
					}

					// defect 8901 
					// defect 9025 
					// Always Replace Annual Plates
					else if (laPlateTypeData.getAnnualPltIndi() == 1)
					{
						laRegValidData.setNewPltReplIndi(1);
						laRegValidData.setNewPltReplMask(false);
					}
					// If sunsetted, cannot manufacture new plate 
					else if (
						lbSpcl
							&& laSpclPltRegisData != null
							&& OrganizationNumberCache.isSunsetted(
								laRegData.getRegPltCd(),
								laSpclPltRegisData.getOrgNo()))
					{
						laRegValidData.setNewPltReplIndi(0);
						laRegValidData.setNewPltReplMask(false);

					}
					// Only consider Optional / Mandatory if
					// NeedsProgramCd = "R" or NeedsProgramCd = "D" 
					else if (
						laPlateTypeData.getNeedsProgramCd().equals(
							PLT_REPLACEMENT_REQUIRED)
							|| laPlateTypeData.getNeedsProgramCd().equals(
								PLT_REPLACEMENT_DEFERRED))
					{
						// defect 9085 
						int liPlateAge = laRegData.getRegPltAge(true);

						// Use Special Plate Age if Attached 
						if (lbSpcl && laSpclPltRegisData != null)
						{
							liPlateAge =
								laSpclPltRegisData.getRegPltAge(true);
						}
						if (liPlateAge
							>= laPlateTypeData.getMandPltReplAge())
						{
							laRegValidData.setNewPltReplIndi(1);
						}
						else if (
							liPlateAge
								>= laPlateTypeData.getOptPltReplAge())
						{
							laRegValidData.setNewPltReplMask(true);
						}
						// defect 9824
						// Do not enable;  Will be automatically selected
						//  if Plate Transfer Fee is selected 
						// defect 9368 
						//else if (laMFVehData.isPTOEligible())
						//{
						//	laRegValidData.setNewPltReplMask(true);
						//}
						// end defect 9368
						// end defect 9824
					}

					// end defect 8901
					// end defect 9025
				}
			}
			// end defect 8900
			// end defect 9085
			// end defect 9670 

			// Determine if Additional Token Trailer Fee required
			RegistrationClientUtilityMethods.procsTokenTrailer(
				aaVehInqData);
			// Determine if Heavy Vehicle Use Tax Verify required
			RegistrationClientUtilityMethods.procsHvyVehUseTaxVerify(
				aaVehInqData);
			
			// defect 11030 
			// defect 9368 
			// Determine if Plate Transfer Fee Applicable 
			RegistrationClientUtilityMethods.procsPTOTrnsfrEligible(
				aaVehInqData);
			// end defect 9368
			// end defect 11030 
			
			// defect 10007
			RegistrationClientUtilityMethods.procsVerifyTowTruckMask(laRegValidData, laRegClassData);
			// end defect 10007
		}
		else if (lsTransCd.equals(TransCdConstant.DUPL))
		{
			laRegValidData.setChrgFeeMask(true);
		}
		else if (lsTransCd.equals(TransCdConstant.EXCH))
		{
			laRegValidData.setExemptMask(!lbStandardExempt);
			laRegValidData.setChngRegMask(true);
			laRegValidData.setApprndComptCntyNoMask(true);
			laRegValidData.setCorrtnEffDateMask(true);
			laRegValidData.setVehClassOK(0);
			// Determine if gross weight change allowed
			if (liCaryingCapReqd == 1)
			{
				laRegValidData.setGrossWtMask(true);
			}
			// Determine if Heavy Vehicle Use Tax Verify required
			RegistrationClientUtilityMethods.procsHvyVehUseTaxVerify(
				aaVehInqData);
			// defect 9368
			// Exchange implies New Plate 
			laRegValidData.setNewPltReplIndi(1);
			// defect 11030 
			// Determine if Plate Transfer Fee Applicable 
			RegistrationClientUtilityMethods.procsPTOTrnsfrEligible(
				aaVehInqData);
			// end defect 11030 
			// end defect 9368 
			// defect 10007 
			RegistrationClientUtilityMethods.procsVerifyTowTruckMask(laRegValidData, laRegClassData);
			// end defect 10007

		}
		else if (lsTransCd.equals(TransCdConstant.REPL))
		{
			// Charge Fee always enabled 
			laRegValidData.setChrgFeeMask(true);
			laRegValidData.setChngRegMask(false);
		}
		else if (lsTransCd.equals(TransCdConstant.PAWT))
		{
			laRegValidData.setDieselMask(true);
			laRegValidData.setGrossWtMask(true);

			// Determine if Heavy Vehicle Use Tax Verify required
			RegistrationClientUtilityMethods.procsHvyVehUseTaxVerify(
				aaVehInqData);
		}
		else if (lsTransCd.equals(TransCdConstant.CORREG))
		{
			laRegValidData.setDieselMask(true);

			// Determine if apprehended permanent add'l weight
			if (laRegValidData.getRegModify()
				== RegistrationConstant.REG_MODIFY_APPREHENDED)
			{

				laRegValidData.setApprndComptCntyNoMask(true);
				laRegValidData.setCorrtnEffDateMask(true);
				laRegValidData.setGrossWtMask(true);
				RegistrationClientUtilityMethods
					.procsHvyVehUseTaxVerify(
					aaVehInqData);
			}
			// Determine if registration correction	
			else if (
				laRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_REG)
			{
				laRegValidData.setChrgFeeIndi(0);
				// Determine if Additional Token Trailer Fee required
				RegistrationClientUtilityMethods.procsTokenTrailer(
					aaVehInqData);
				laRegValidData.setChrgFeeMask(
					laRegValidData.getAddlToknFeeMask());
			}
			// end defect 8900
		}
	}

	/**
	 * Process Registration Issue inventory 
	 * 
	 * @param aaData 
	 * @return Object  
	 * @throws RTSException 
	 */
	private Object procsRegIssueInv(Object aaData) throws RTSException
	{
		try
		{
			return Comm.sendToServer(
				GeneralConstant.INVENTORY,
				InventoryConstant.ISSUE_INVENTORY,
				aaData);
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getMsgType().equals(RTSException.DB_DOWN)
				|| aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN))
			{
				CompleteTransactionData laTransData =
					(CompleteTransactionData) aaData;
				laTransData.setAllocInvItms(new Vector());
				return laTransData;
			}
			else
			{
				throw aeRTSEx;
			}
		}
	}

	/**
	 * Validates vehicle's gross weight based on its carrying capacity.
	 * 
	 * @param aaData 
	 * @return Object    
	 * @throws RTSException 
	 */
	public Object validateWeight(Object aaData) throws RTSException
	{
		VehicleInquiryData laVehInqData = (VehicleInquiryData) aaData;
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) laVehInqData
				.getValidationObject();
		RegistrationData laRegData =
			laVehInqData.getMfVehicleData().getRegData();
		VehicleData laVehData =
			laVehInqData.getMfVehicleData().getVehicleData();

		if (laRegData.getVehCaryngCap()
			<= laRegValidData.getOrigCaryngCap())
		{
			// Return error message if Apprehended PAWT
			if (laRegValidData
				.getTransCode()
				.equals(TransCdConstant.CORREG)
				&& laRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_APPREHENDED)
			{
				if (laRegData.getVehCaryngCap()
					< laRegValidData.getOrigCaryngCap())
				{
					throw new RTSException(168);
				}
				else
				{
					throw new RTSException(431);
				}

			}
		}
		// Calculate vehicle gross weight
		int liVehGrossWt =
			laVehData.getVehEmptyWt() + laRegData.getVehCaryngCap();
		laRegData.setVehGrossWt(liVehGrossWt);
		RegistrationClassData laRegClassData =
			getRegClassDataObject(laVehInqData);
		//Determine if Heavy Vehicle Use Tax Verify required
		RegistrationClientUtilityMethods.procsHvyVehUseTaxVerify(
			laVehInqData);
		// Validate carrying capacity for trailer
		if (laRegClassData.getTrlrCapValidReqd() == 1)
		{
			// determine if carrying capacity is at least one-third of 
			// the empty weight
			if (laRegData.getVehCaryngCap()
				< laVehData.getVehEmptyWt() / THIRD_OF_VEH_WEIGHT)
			{
				throw new RTSException(13);
			}
		}
		
		// defect 10959 
		// Validated on REG003 
		// Test gross weight not greater than 80000 pounds
		//		if (laRegData.getVehGrossWt() > MAX_GROSS_WEIGHT)
		//		{
		//			throw new RTSException(89);
		//		}
		// end defect 10959 
		
		// Validate tonnage requirements
		CommonValidations.verifyVehTon(laVehInqData);
		return laVehInqData;
	}
}
