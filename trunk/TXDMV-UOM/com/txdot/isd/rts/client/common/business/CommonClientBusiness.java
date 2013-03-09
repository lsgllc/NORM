package com.txdot.isd.rts.client.common.business;

import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.client.common.ui.VCInventoryItemNumberInputINV001;
import com.txdot.isd.rts.client.registration.business.SubcontractorRenewalClientBusiness;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.MiscellaneousCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.common.Fees;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * CommonClientBusiness.java 
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Peters		08/22/2001  Created
 * N Ting		09/26/2001	Add addTrans method
 * MAbs			04/18/2002	Multiple records of same item displaying 
 * 							when checking VOID in INV029 
 *							defect 3468
 * MAbs			04/30/2002	Fixed DTA issues in issue inventory 
 * 							defect 3711
 * MAbs			05/08/2002	Fixed case in dealer when Id is not present 
 * 							defect 3819
 * MAbs/TP		06/05/2002	MultiRecs in Archive
 *							defect 4019
 * Ray Rowehl	06/05/2002	Show already issued message on inventory that 
 *							was issued under a different entity instead 
 *							of ocated to another entity.
 *							defect 4233
 * Robin        08/02/2002  Fixing 4480. Changed isNextVCREG029 method to 
 * S Govindappa				use carrCompTransData[0].getOfcIssuanceNo() 
 *							instead of cWSOfficeIds.getOfcIssuanceNo().
 * Ray Rowehl	08/12/2002	Return a validation error if this is DTA and 
 * 							the inventory returned is anything but the 
 * 							dealers.
 *							defect 4586
 * Min Wang  	08/22/2002	CommonClientBusiness 
 * 							to correct Inventory Validation Handling 
 *   						when Dealer Inventory is used outside of DTA.
 * 							defect 4641 
 * Min Wang		09/06/2002	Modified validateInventory() to deal with 
 * 							inventory item in stock.
 *							defect 4713
 * Min Wang		09/26/2002  Modified validateInventoryNum() to handle 
 * 							inventory ssued is cached to be sent to server  
 * 							when rocessing Server down and 
 * 							processInventoryData is 
 *							stored correctly inventory. 
 * 							defect 4734.
 * Ray Rowehl	10/24/2002  Add debug logging around inventory issue calls.
 *							defect 4872 
 * Ray Rowehl	11/11/2002	Add methods to call new void handling methods
 *							defect 4745
 * Min Wang		01/06/2002  Modify validateInventoryNum() and re-arrange 
 * 							how Inventory Server Business is called.
 * 							Now just call once. 
 *							defect 4755
 * K. Harrell   03/28/2003  Fix to 4586 - Also check for invlocidcd = "X"
 * 							for DTA. Don't go to INV029 if
 * 							server down, don't mark w/ "U"
 *							defect 5848 
 * Ray Rowehl	07/07/2003	Throw an 149 exception in getVEH if it is a 
 * 							Special Owner and Registration activity.
 *							modify getVeh()
 *							defect 6170
 * Ray Rowehl	08/13/2003	Some comment clean up on processData.
 * Ray Rowehl	08/13/2003	do not throw 149 if it is a canceled plate. 
 *							canceled plate also shows special owner as a 1.
 *							modify getVeh()
 *							defect 6170
 * K Harrell	12/08/2003	setApprndCntyNo(0) when saving MFVehicle Data 
 * 							from prior 
 *							transaction where ApprndCntyNo !=0
 *							modify setSavedVehicle()
 *							defect 6387 Version 5.1.5 Fix 2
 * Ray Rowehl	12/29/2003	Remove extra ; from setAside method.  This is
 *							causing problems under WSAD and Java 1.3.1.
 *							modify setAside()
 *							defect 6595  Ver 5.1.5 fix2
 * K Harrell	01/11/2004	Remove call to endTrans(CompleteTransactionData,
 * 							TransactionHeaderData)
 *							deprecated in Transaction.endTrans
 *							(CompleteTransactionData,TransactionHeaderData)
 *							modify endTrans()
 *							defect 6784  Ver 5.1.5 Fix 2
 * K Harrell	03/30/2004  5.2.0 Merge.  See PCR 34 Comments.
 *							modify import statements
 * 							add constant DEALER_INPUT_FILE
 * 							add writePrintedBackToDTADisk()
 * 							modify processData()
 * 							Ver 5.2.0
 * K Harrell	05/27/2004	5.2.0 Merge cont'd
 *							remove reference to ownrsuppliedstkrno
 *							Ver 5.2.0
 * K Harrell	06/03/2004	Do not call Issue Inventory if only
 *							printableindi inventory
 *							modify getNextCompleteTransVC()
 *							defect 7142  Ver 5.2.0
 * K Harrell	07/16/2004	Reset CompleteTransactionData Sticker 
 *							and AllocInvItms Vectors
 *							modify getNextCompleteTransVC()
 *							defect 7345  Ver 5.2.0
 * K Harrell	11/03/2004	Rename, cleanup method according to
 *							function
 *							add addTransIRenew()
 *							deprecate writeInvToCache()
 *							modify processData()
 *							defect 6720 Ver 5.2.2
 * K Harrell	01/13/2005	JavaDoc/Formatting/Variable Name Cleanup
 *							Validate RGNCOL inventory against previously
 *							issued.
 *							modify validateInventoryNum() 
 *							defect 7317 Ver 5.2.3
 * Ray Rowehl	02/07/2005	Move Fees to a new package.
 * 							add import
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	02/08/2005	Move Transaction to a new package.
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * B Hargrove	04/26/2005	chg '/**' to '/*' to begin prolog.
 * 							Format comments, Hungarian notation for 
 * 							variables. 
 * 							defect 7885 Ver 5.2.3 
 * K Harrell	05/02/2005	Rename INV014 to INV003
 * 							rename/modify holdInv003Itm() 
 * 							defect 6966 Ver 5.2.3 
 * K Harrell	05/19/2005	Create add'l method for handling Regional 
 * 							Collections inventory. Removed check for 
 * 							itmcd & year as can only be same in RGNCOL.
 * 							Java 1.4 work 
 * 							add validateRgnColInv()
 * 							delete writeInvToCache() 
 * 							modify validateInventoryNum()
 * 							defect 7317 Ver 5.2.3 
 * J Rue		06/15/2005	Match RSPS getters/setters set in 
 * 							DealerTitleData to better define their 
 * 							meaning
 * 							modify writePrintedBackToDTADisk()
 * 							defect 8217 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3
 * K Harrell	08/13/2005	Add validation of Disabled Placard inventory
 * 	J Zwiener				rename validateRgnColInv() to 
 *   						verifyUniqueInvSameTrans()
 * 							modify verifyUniqueInvSameTrans()
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	08/22/2005	add parameter to call to 
 * 							Transaction.addTrans()
 * 							modify addTrans()
 * 							defect 8344 Ver 5.2.3   
 * K Harrell	11/03/2005	Do not reference Inv_Patterns Cache if 
 * 							PrintableIndi = 1
 * 							modify getNextCompleteTransVC()
 * 							defect 8362 Ver 5.2.3
 * K Harrell	02/13/2006	No need to set MF Down indicator here.
 * 							modify getVeh() 
 * 							defect 6861 Ver 5.2.3
 * K Harrell	02/14/2006	New methodology for determining 
 * 							InvItmYr; Use RegistrationRenewalsCache && 
 * 							ItemCodesCache
 * 							add SECONDARY_TRCKNG_TYPE
 * 							modify getNextCompleteTransVC()
 * 							defect 8545 Ver 5.2.3
 * K Harrell	04/19/2006	Prompting for inventory iff issue sticker	
 * 							Left out ! prior to isStickerPrintable
 * 							modify getNextCompleteTransVC() 
 * 							defect 8720 Ver 5.2.3
 * B Hargrove	09/28/2006	Add check for FeeCalcCat = 4 = NOREGISFEES.
 * 							If so, do not go to REG029 screen.
 * 							modify isNextVCREG029() 
 * 							defect 8900 Ver 5.3.0 
 * K Harrell	02/05/2007	Use PlateTypeCache vs. 
 * 							 	RegistrationRenewalsCache
 * 							modify getNextCompleteTransVC()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/26/2007	Remove reference to InvProcsngCd
 * 							modify isNextVCREG029()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/28/2007	add processing for adding/restoring
 * 							special plate, special plate vector
 * 							add getSavedSpclPlt(),getSavedMfgSpclPltVector()
 * 							  setSavedMfgSpclPltVector, setSavedSpclPlt()
 * 							modify processData()
 * 							defect 9085 Ver Special Plates  
 * B Hargrove	04/10/2007	Fix check for going to REG029. If Min = Max 
 * 							months, do not go to REG029 (ie: the
 * 							'&&' should be '||' : if ANY of these checks
 * 							are true, do not got to REG029
 * 							modify isNextVCREG029()
 * 							defect 9126 Ver Special Plates
 * K Harrell	04/09/2007	Use SystemProperty.getOfficeIssuanceCd() &&
 * 							SystemProperty.isHQ() 
 * 							modify getMiscData(),isNextVCREG029(),
 * 							 validateInventoryNum()
 * 							defect 9085 ver Special Plates  
 * B Hargrove	05/22/2007	On further review, change check for Exempt 
 * 							to look for isStandardExempt(), not if
 * 							FeeCalcCat = 4 (no regis fees).
 * 							modify isNextVCREG029()
 * 							defect 9126 Ver Special Plates
 * K Harrell	06/05/2007	Final Tune-up
 * 							defect 9085 Ver Special Plates  
 * B Hargrove	06/21/2007	Remove check for 'HQ' in decision to go to  
 * 							REG029 (ie: go to REG029 even if HQ).
 * 							modify isNextVCREG029()
 * 							defect 9126 Ver Special Plates
 * K Harrell	07/01/2007	Accommdate Dealer Sticker Printing
 * 							modify getNextCompleteTransVC()
 * 							defect 9085 Ver Special Plates  
 * B Hargrove	10/16/2007	Add check for 'Plate Transfer Eligible'.
 * 							If so, go to to REG029 to allow access to 
 * 							Cust Supplied chkbox.
 * 							modify isNextVCREG029()
 * 							defect 9366 Ver Special Plates 2
 * K Harrell	10/21/2007	Added check for NewPlatesDesired to the above.
 * 							modify isNextVCREG029()
 * 							defect 9368 Ver Special Plates 2  
 * B Hargrove	11/20/2007	Add check for 'TONLY' (Title Only - No Regis).  
 * 							If so, do not go to REG029.
 * 							modify isNextVCREG029()
 * 							defect 9337 Ver Special Plates 2
 * B Hargrove	07/11/2008 	Check to see if this plate type allows
 * 							'Customer Supplied' (ie: 'OLDPLT2') and that
 * 							new plates are desired.
 * 							If so, go to REG029.
 * 							modify isNextVCREG029() 
 * 							defect 9529 Ver MyPlates_POS
 * K Harrell	10/21/2008	Modify mechanism for determining if 
 * 							Disabled Placard Transaction. 
 * 							modify verifyUniqueInvSameTrans() 
 * 							defect 9831 Ver Defect_POS_B  
 * K Harrell	01/05/2009	Do not reset Transaction.CumulativeTransIndi
 * 							 Should be set only upon transaction 
 * 							 completion. 
 * 							modify getVeh() 
 * 							defect 8596 Ver Defect_POS_D 
 * B Hargrove	06/01/2009  Add Flashdrive option to DTA. Change 
 * 							verbiage from 'diskette'.
 * 							refactor\rename\modify:
 * 							DEALER_DISK_FILE / DEALER_INPUT_FILE,
 * 							writePrintedBackToDTADisk() / 
 * 							writePrintedBackToDTAMedia()
 * 							defect 10075 Ver Defect_POS_F  
 * B Hargrove	06/08/2009	Remove all 'Cancelled Sticker' references.
 * 							modify getVeh()
 * 							defect 9953 Ver Defect_POS_F
 * K Harrell	12/16/2009	Modify for new DTA Data Flow.
 * 							Remove extraneous parameters where not used.
 * 							delete writePrintedBackToDTAMedia(), 
 * 							 DEALER_INPUT_FILE
 * 							modify validateInventoryNum(), processData(), 
 * 							 getMiscData(), takeProcInvDataOffHold()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	02/10/2010  Non-DTA transactions using DTA validation
 * 							code.  Modify verify if DTA transaction 
 * 							by checking CompleteTransactionData Transcd. 
 * 							modify validateInventoryNum() 
 * 							defect 10374 Ver Defect_POS_H 
 * K Harrell	06/01/2010	add svSavedVIPrmtData(), get/set methods
 * 							modify processData()
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 *
 * The CommonClientBusiness handles two tasks - 
 * deciding whether the function being processed can be handled on the 
 * client side, or has to go to the server side.
 * <ul>
 *	<li>If it has to be handled on the server side, 
 *			it passes on the function to the Comm class which returns 
 *			the new data object.
 *  <li>If it can be handled on the client side, 
 *			it provides functions that can do that and return the new
 *			data object.  All of the business logic of functions that 
 *			can be handled on the client side is found in this class.
 * </ul>
 *
 * @version	6.5.0 			06/01/2010
 * @author	Joseph Peters
 * <br>Creation Date:		08/22/2001
 */
public class CommonClientBusiness
{
	// Static since they needs to be retained.  Handled by Transaction
	private static MFVehicleData saSavedVehicle;
	private static Vector svSavedProcessingInventoryData;
	private static VehMiscData saVehMiscData;
	private static TimedPermitData saSavedTimedPermitData;

	// For last special plate  
	private static SpecialPlatesRegisData saSavedSpclPltRegisData;

	// Vector of Spcl Plt Regis Data for cust seqno 
	private static Vector svSavedMfgSpclPltRegisData;

	// defect 10491 
	// Vector of PrmtVirtual Inventory 
	private static Vector svSavedVIPrmtData;
	// end defect 10491  

	/**
	 * getMFVehicle returns this String constant if no MFVehicle is found.
	 */
	private static final String EMPTY = "Empty";
	private static final int NOT_IN_DB_CODE = 182;
	private static final String SECONDARY_TRCKNG_TYPE = "S";

	// defect 10290 
	// public static final String DEALER_INPUT_FILE = "dlrdisk.dat";
	// end defect 10290 

	/**
	 * Part of a kludgy switch in processData(), where the functionId 
	 * is CommonConstant.SAVE_INVENTORY.  One of the conditionals
	 * is whether the object is an instanceof String. This is the String
	 * which is used.
	 */
	public static final String ERASE_SAVED_INV = "Erase";
	/**
	 * CommonClientBusiness constructor
	 */
	public CommonClientBusiness()
	{
		super();
	}

	/**
	 * Calls the Transaction object addTrans method
	 * 
	 * @param  aaObject object
	 * @return Object
	 * @throws RTSException
	 */
	private Object addTrans(Object aaObject) throws RTSException
	{
		Transaction laTransaction = new Transaction();
		// defect 8344
		// add last parameter 
		return laTransaction.addTrans(
			(CompleteTransactionData) aaObject,
			null);
		// end defect 8344	
	}
	/**
	 * Calls the Transaction object addTransForVoid method
	 * 
	 * @param  aaObject Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object addTransForVoid(Object aaObject) throws RTSException
	{
		Transaction laTransaction = new Transaction();
		return laTransaction.addTransForVoid(
			(CompleteTransactionData) aaObject);
	}
	/**
	 * Adds transaction for IRENEW
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException	
	 */
	private Object addTransIRenew(Object aaData) throws RTSException
	{
		// defect 6720
		CompleteTransactionData laTransData =
			(CompleteTransactionData) aaData;
		try
		{
			addTrans(laTransData);
			return new Boolean(true);
		}
		catch (RTSException aeRTSEx)
		{
			return aeRTSEx;
		}
		// end defect 6720 
	}

	/**
	 * Log Error to Main Frame
	 * 
	 * @param aaObject Object
	 */
	private void appMfErrLog(Object aaObject) throws RTSException
	{
		Comm.sendToServer(
			GeneralConstant.COMMON,
			CommonConstant.APP_MF_ERR_LOG,
			aaObject);
	}

	/**
	 * Calls fee calcualtion
	 * 
	 * @param  aaObject object
	 * @return Object
	 * @throws RTSException
	 */
	private Object calculateFees(Object aaObject) throws RTSException
	{
		if (aaObject instanceof CompleteTransactionData)
		{
			Fees laFees = new Fees();
			CompleteTransactionData laCompleteTransactionData =
				(CompleteTransactionData) aaObject;
			aaObject =
				laFees.calcFees(
					laCompleteTransactionData.getTransCode(),
					laCompleteTransactionData);
			return aaObject;
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Unexpected Type",
				"ERROR");
		}
	}
	/**
	 * Clears all the saved information from additional trans
	 * 
	 */
	private void clearSavedVehInfo()
	{
		setSavedVehicle(null);
		setSavedVehicleMisc(null);
		setSavedTimePermit(null);
	}

	/**
	 * Deletes the transaction in the input vector
	 * 
	 * @param  aaObject Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object deleteSelectedTrans(Object aaObject)
		throws RTSException
	{
		Vector lvTransactionKey = (Vector) aaObject;
		Transaction laTransaction = new Transaction();
		laTransaction.deleteSelectedTrans(lvTransactionKey);
		return new Boolean(true);
	}

	/**
	 * Delete all the transactions for current Customer Seq No
	 * 
	 * @throws RTSException
	 */
	private Object deleteTrans() throws RTSException
	{
		Transaction laTransaction = new Transaction();
		laTransaction.deleteTrans();
		return null;
	}
	/**
	 * Calls the Transaction object endTrans method
	 * 
	 * @param  aaObject Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object endTrans(Object aaObject) throws RTSException
	{
		Transaction laTransaction = new Transaction();
		laTransaction.endTrans(
			(Vector) aaObject,
			Transaction.getTransactionHeaderData(),
			null);
		return new CompleteTransactionData();
	}

	/**
	 * Calls the Transaction object endTransForVoid method
	 * 
	 * @param  aaObject Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object endTransForVoid(Object aaObject) throws RTSException
	{
		Transaction laTransaction = new Transaction();
		Vector lvReturn = new Vector();
		if (aaObject != null
			&& aaObject instanceof CompleteTransactionData)
		{
			lvReturn =
				laTransaction.endTransForVoid(
					(CompleteTransactionData) aaObject,
					Transaction.getTransactionHeaderData());
		}
		return lvReturn;
	}

	/**
	 * Compare the supervisor override code supplied with the one in the  
	 * database. Throws exception 150 if not equals.  Otherwise, set 
	 * the supervisor overridecode in VehMisData.
	 * 
	 * @param  aaObject Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getMiscData(Object aaObject) throws RTSException
	{
		if (aaObject instanceof VehicleInquiryData)
		{
			VehicleInquiryData laVehicleInquiryData =
				(VehicleInquiryData) aaObject;
			VehMiscData laVehMiscData =
				laVehicleInquiryData.getVehMiscData();
			String lsSupvOverride = laVehMiscData.getSupvOvride();
			// defect 10290 
			// Not needed 
			// String lsEncrptPass =
			// UtilityMethods.encryptPassword(lsSupvOverride);
			// end defect 10290 

			// Get from Admin cache (from DB if available) 
			MiscellaneousData laMiscellaneousData =
				MiscellaneousCache.getMisc(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId());

			if (laMiscellaneousData == null)
			{
				throw new RTSException(150);
			}
			String lsPassword = laMiscellaneousData.getSupvOvrideCd();
			lsPassword = UtilityMethods.decryptPassword(lsPassword);
			if (lsPassword.trim().equals(lsSupvOverride))
			{
				laVehMiscData.setSupvOvride(lsSupvOverride);
			}
			else
			{
				throw new RTSException(150);
			}
			return laVehicleInquiryData;
		}
		return null;
	}
	/**
	 * This method is called after the REG029 screen or after 
	 * isNextREG029 returns FALSE
	 *
	 * @param  aaObject Object 
	 * @return Object a Vector with two elements.  First element is the 
	 *   name of the next VC (one of the ScreenConstants eg.  
	 *   ScreenConstant.PMT004, ScreenConstant.INV001...).Second element
	 *   is the CompleteTransactionData
	 * @throws RTSException
	 */
	private Object getNextCompleteTransVC(Object aaObject)
		throws RTSException
	{
		// defect 7142
		// flag to determine if Issue Inventory to be called 
		boolean lbIssueInventory = false;
		// end defect 7142
		if (aaObject instanceof CompleteTransactionData)
		{
			CompleteTransactionData laCompleteTransactionData =
				(CompleteTransactionData) aaObject;
			Vector lvReturn = new Vector();
			String lsNextVC = null;
			String lsTransCd = laCompleteTransactionData.getTransCode();

			//Set VehTotalSalesTaxPd and VehSalesTaxAmt
			VehMiscData laVehMisc =
				laCompleteTransactionData.getVehMisc();
			if (laVehMisc != null)
			{
				laVehMisc.setVehTaxAmt(
					laCompleteTransactionData.getVehTaxAmt());
				RegFeesData laRegFeesData =
					laCompleteTransactionData.getRegFeesData();
				Vector lvFeesData = laRegFeesData.getVectFees();
				Dollar ldVehTaxPnlty = new Dollar(0.0);
				for (int i = 0; i < lvFeesData.size(); i++)
				{
					FeesData laFeesData =
						(FeesData) lvFeesData.elementAt(i);
					String lsAcctItmCd = laFeesData.getAcctItmCd();
					if (lsAcctItmCd.equals("SLSTXPEN"))
					{
						ldVehTaxPnlty = laFeesData.getItemPrice();
						break;
					}
				}
				laCompleteTransactionData.setVehTaxPnlty(ldVehTaxPnlty);
				Dollar ldVehTtlSalesTaxPd = new Dollar(0.0);
				Dollar ldVehSalesTaxAmt = new Dollar(0.0);
				Dollar ldTaxPdOthrState = laVehMisc.getTaxPdOthrState();
				if (ldTaxPdOthrState == null)
				{
					ldTaxPdOthrState = new Dollar(0.0);
				}
				for (int i = 0; i < lvFeesData.size(); i++)
				{
					FeesData laFeesData =
						(FeesData) lvFeesData.elementAt(i);
					String lsAcctItmCd = laFeesData.getAcctItmCd();
					if (lsAcctItmCd.equals("SLSTX"))
					{
						ldVehSalesTaxAmt =
							(laFeesData.getItemPrice()).add(
								ldTaxPdOthrState);
						ldVehTtlSalesTaxPd =
							ldVehSalesTaxAmt.subtract(
								ldTaxPdOthrState).add(
								ldVehTaxPnlty);
						laCompleteTransactionData
							.getVehicleInfo()
							.getTitleData()
							.setSalesTaxPdAmt(ldVehTtlSalesTaxPd);
						break;
					}
				}
				laCompleteTransactionData.setVehSalesTaxAmt(
					ldVehSalesTaxAmt);
				ldVehTtlSalesTaxPd =
					ldVehSalesTaxAmt.subtract(ldTaxPdOthrState).add(
						ldVehTaxPnlty);
				laCompleteTransactionData.setVehTotalSalesTaxPd(
					ldVehTtlSalesTaxPd);
				TitleData laTtl1 =
					laCompleteTransactionData
						.getVehicleInfo()
						.getTitleData();
				//Change values for VehSalesTaxAmt and 
				//VehTotalSalesTaxPd for REJCOR
				if (lsTransCd.equals(TransCdConstant.REJCOR)
					&& laTtl1 != null)
				{
					Dollar ldSalesTaxPdAmt = laTtl1.getSalesTaxPdAmt();
					laCompleteTransactionData.setVehSalesTaxAmt(
						new Dollar(0.0));
					laCompleteTransactionData.setVehTotalSalesTaxPd(
						ldSalesTaxPdAmt);
				}
			}
			//Assign RegExpMo = ToMonth 
			RegistrationData laRegData =
				laCompleteTransactionData.getVehicleInfo().getRegData();
			if (!lsTransCd.equals(TransCdConstant.CCO))
			{
				int liToDfltMo =
					laCompleteTransactionData
						.getRegFeesData()
						.getToMonthDflt();
				if (liToDfltMo != 0)
				{
					laRegData.setRegExpMo(liToDfltMo);
				}
			}
			// defect 9085 
			// Preserve sticker defined in Special Plates for Dealers
			// defect 7345
			// Reset CompleteTransactionData Sticker/AllocInvItms Vectors
			if (!UtilityMethods
				.isSpecialPlates(
					laCompleteTransactionData.getTransCode()))
			{
				laCompleteTransactionData.setStickers(new Vector());
			}
			laCompleteTransactionData.setAllocInvItms(new Vector());
			// end defect 7345 	
			// end defect 9085 

			// If there is no inventory to be issued, go to PMT004 screen
			if (laCompleteTransactionData.getInvItemCount() == 0)
			{
				lsNextVC = ScreenConstant.PMT004;
			}
			else
			{
				// Call issue inventory 
				// Loop to set InvItmYr for all inventory to be issued
				Vector lvInvItms = null;
				if (laCompleteTransactionData.getInvItmsAfterFees()
					== null)
				{
					laCompleteTransactionData.setInvItmsAfterFees(
						laCompleteTransactionData.getInvItms());
				}

				lvInvItms =
					laCompleteTransactionData.getInvItmsAfterFees();

				lvInvItms = (Vector) UtilityMethods.copy(lvInvItms);

				laCompleteTransactionData.setInvItms(lvInvItms);

				for (int i = 0; i < lvInvItms.size(); i++)
				{
					ProcessInventoryData laProcInvData =
						(ProcessInventoryData) lvInvItms.elementAt(i);

					// defect 8760 
					// Added !; Issue inventory if NOT printable
					// defect 7142 
					// Set boolean for Issue Inventory  
					if (!(StickerPrintingUtilities
						.isStickerPrintable(laProcInvData)))
					{
						lbIssueInventory = true;
					}
					// end defect 7142
					// end defect 8760  

					// defect 8545  
					// (to include rewrite of 8362)
					// InvItmYr set !=0 if:
					// - Found in Regis_Renewals && AnnualPltIndi = 1
					// - or-  
					// - Secondary Tracking Type

					String lsItmCd = laProcInvData.getItmCd();

					PlateTypeData laPlateTypeData =
						PlateTypeCache.getPlateType(lsItmCd);

					ItemCodesData laItmCdsData =
						ItemCodesCache.getItmCd(lsItmCd);

					if ((laPlateTypeData != null
						&& laPlateTypeData.getAnnualPltIndi() == 1)
						|| laItmCdsData.getItmTrckngType().equals(
							SECONDARY_TRCKNG_TYPE))
					{
						laProcInvData.setInvItmYr(
							laCompleteTransactionData
								.getRegFeesData()
								.getToYearDflt());
					}
					else
					{
						laProcInvData.setInvItmYr(0);
					}
					// end defect 8545 

					// Remove the plate from the inventory if the plate 
					// is owner supplied
					if (laCompleteTransactionData
						.getOwnrSuppliedPltNo()
						!= null
						&& !laCompleteTransactionData
							.getOwnrSuppliedPltNo()
							.trim()
							.equals(
							""))
					{
						if (laProcInvData
							.getItmCd()
							.equals(laRegData.getRegPltCd()))
						{
							lvInvItms.remove(i);
							i--;
						}
					}
				}
				if (lvInvItms.size() == 0)
				{
					lsNextVC = ScreenConstant.PMT004;
				}
				else
				{
					try
					{
						// defect 7142
						// Conditionally call Issue Inventory
						if (lbIssueInventory)
						{
							// defect 4872
							Log.write(
								Log.DEBUG,
								this,
								" Begin Prompted Issue");
							laCompleteTransactionData =
								(
									CompleteTransactionData) Comm
										.sendToServer(
									GeneralConstant.INVENTORY,
									InventoryConstant.ISSUE_INVENTORY,
									laCompleteTransactionData);
							// defect 4872
							Log.write(
								Log.DEBUG,
								this,
								" End Prompted Issue");
						}
						else
						{
							laCompleteTransactionData.setAllocInvItms(
								new Vector());
						}
						// end defect 7142
						lsNextVC = ScreenConstant.INV007;
					}
					catch (RTSException leRTSEx)
					{
						laCompleteTransactionData.setAllocInvItms(
							new Vector());
						lsNextVC = ScreenConstant.INV007;
					}
				}
			}
			lvReturn.addElement(lsNextVC);
			lvReturn.addElement(laCompleteTransactionData);
			return lvReturn;
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Unexpected Type",
				"ERROR");
		}
	}

	/**
	 * Get the saved inventory in this transaction. Returns a vector of
	 * ProcessInventoryData or EMPTY if nothing was saved.
	 * 
	 * @return Object 
	 */
	private Object getSavedInv()
	{
		if (svSavedProcessingInventoryData != null)
		{
			return UtilityMethods.copy(svSavedProcessingInventoryData);
		}
		else
		{
			return EMPTY;
		}
	}

	/**
	 * 
	 * Get the saved vector of SpecialPlateRegisData in this transaction
	 * set. If nothing was saved, returns EMPTY.
	 * 
	 * @return Object
	 */
	private Object getSavedMfgSpclPltVector()
	{
		if (svSavedMfgSpclPltRegisData != null)
		{
			return UtilityMethods.copy(svSavedMfgSpclPltRegisData);
		}
		else
		{
			return EMPTY;
		}
	}

	/**
	 * Get the saved vector of PermitData in this transaction
	 * set. If nothing was saved, returns EMPTY.
	 * 
	 * @return Object
	 */
	private Object getSavedVIPrmtVector()
	{
		if (svSavedVIPrmtData != null)
		{
			return UtilityMethods.copy(svSavedVIPrmtData);
		}
		else
		{
			return EMPTY;
		}
	}

	/**
	 * Get the saved Special Plates Data object from the previous 
	 * transaction in the same Special Plate scenario. 
	 *  
	 * Returns SpecialPlateRegisData or EMPTY if nothing was saved.
	 * 
	 * @return Object
	 */
	private Object getSavedSpclPlt()
	{
		if (saSavedSpclPltRegisData != null)
		{
			return UtilityMethods.copy(saSavedSpclPltRegisData);
		}
		else
		{
			return EMPTY;
		}
	}

	/**
	 * Get the saved time permit from the previous transaction in the 
	 * same vehicle scenario. Returns TimePermitData or EMPTY if
	 * nothing was saved.
	 * 
	 * @return Object TimedPermitData 
	 */
	private Object getSavedTimedPermit()
	{
		if (saSavedTimedPermitData != null)
		{
			return UtilityMethods.copy(saSavedTimedPermitData);
		}
		else
		{
			return EMPTY;
		}
	}

	/**
	 * Get the saved MFVehicle from the previous transaction in the same
	 * vehicle scenario. Returns EMPTY if nothing was saved.
	 * 
	 * @return Object 
	 */
	private Object getSavedVehicle()
	{
		if (saSavedVehicle != null)
		{
			return UtilityMethods.copy(saSavedVehicle);
		}
		else
		{
			return EMPTY;
		}
	}

	/**
	 * Get the saved VehMiscData from the previous transaction in the 
	 * same vehicle scenario. Returns EMPTY if nothing was saved.
	 * 
	 * @return Object 
	 */
	private Object getSavedVehicleMisc()
	{
		if (saVehMiscData != null)
		{
			return UtilityMethods.copy(saVehMiscData);
		}
		else
		{
			return EMPTY;
		}
	}

	/**
	 * Get the TransactionPayment information
	 * 
	 * @param  aiModuleName int 
	 * @param  aiFunctionId int 
	 * @param  aaObject Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTransPayment(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		return Comm.sendToServer(aiModuleName, aiFunctionId, aaObject);
	}

	/**
	 * This method is used to send the searchData inofrmation to the  
	 * server and retrieve the VehicleInquiryData from the mainframe.
	 *
	 * @param  aiModuleName int 
	 * @param  aiFunctionId int 
	 * @param  aaObject Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object getVeh(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		VehicleInquiryData laVehData;
		laVehData =
			(VehicleInquiryData) Comm.sendToServer(
				aiModuleName,
				aiFunctionId,
				aaObject);

		// defect 6170
		// if this is a special plate and it is not a vehicle Inquiry or
		//  title throw a 149 exception.  Do not throw if this is a 
		//  canceled plate
		if (laVehData.getSpecialOwner() > 0
			&& (laVehData.getMfVehicleData().getRegData().getCancPltIndi()
			// defect 9953
				== 0))
			//== 0
			//&& laVehData
			///	.getMfVehicleData()
			//	.getRegData()
			//	.getCancStkrIndi()
			//	== 0))
			// end defect 9953
		{
			// get the GSD to check for trans id
			GeneralSearchData laGSD = null;
			if (aaObject instanceof Vector)
			{
				laGSD = (GeneralSearchData) ((Vector) aaObject).get(1);
			}
			else
			{
				laGSD = (GeneralSearchData) aaObject;
			}
			// check the transid
			if (laGSD.getKey3().equals(TransCdConstant.RENEW)
				|| laGSD.getKey3().equals(TransCdConstant.REPL)
				|| laGSD.getKey3().equals(TransCdConstant.EXCH)
				|| laGSD.getKey3().equals(TransCdConstant.DUPL)
				|| laGSD.getKey3().equals(TransCdConstant.ADDR)
				|| laGSD.getKey3().equals(TransCdConstant.RNR)
				|| laGSD.getKey3().equals(TransCdConstant.CORREG))
			{
				// throw exception if this is a REG event and special plates
				throw new RTSException(149);
			}
		}
		// end defect 6170

		// defect 8596
		// Saved Vehicle should not be reset until completed transaction. 
		// Clear saved vehicle after retrieval from MF, only save vehicle
		// when completing trans
		// clearSavedVehInfo();
		// Transaction.setCumulativeTransIndi(0);
		// end defect 8596 
		//set print options
		GeneralSearchData laGeneralSearchData = null;
		if (aaObject instanceof GeneralSearchData)
		{
			laGeneralSearchData = (GeneralSearchData) aaObject;
		}
		else
		{
			laGeneralSearchData =
				(GeneralSearchData) ((Vector) aaObject).get(1);
		}
		laVehData.setPrintOptions(laGeneralSearchData.getIntKey1());

		// defect 6861
		// Correct the MF Down issue 
		//MFVehicleData laMFVeh = laVehData.getMfVehicleData();

		// This should be set correctly from  
		// if (laVehData != null
		// 		&& laVehData.getMfDown() == 0
		// 		&& laMFVeh != null)
		//	{
		//		laMFVeh.setFromMF(true);
		//	}
		// end defect 6861

		return laVehData;
	}

	/**
	 * Hold the inventory confirmed in FrmINV003
	 * 
	 * @param 	aaObject Object
	 * @return	Object
	 * @throws	RTSException 
	 */
	private Object holdInv003Itm(Object aaObject) throws RTSException
	{
		SubcontractorRenewalClientBusiness laSubconClient =
			new SubcontractorRenewalClientBusiness();

		return laSubconClient.processData(
			GeneralConstant.REGISTRATION,
			RegistrationConstant.HOLD_INV003_ITM,
			aaObject);
	}

	/**
	 * This method is called to determine whether to go to REG029 or not. 
	 * Returns Vector with two elements.  First element is type 
	 * Boolean (object) and second element is CompleteTransactionData
	 * 
	 * @param  aaObject Object CompleteTransactionData
	 * @return Object 
	 */
	private Object isNextVCREG029(Object aaObject) throws RTSException
	{
		if (aaObject instanceof CompleteTransactionData)
		{
			CompleteTransactionData laCompleteTransactionData =
				(CompleteTransactionData) aaObject;
			Vector lvReturn = new Vector();
			RegFeesData laRegFeesData =
				laCompleteTransactionData.getRegFeesData();
			RegistrationData laRegData =
				laCompleteTransactionData.getVehicleInfo().getRegData();
			int liExpMinMonths = laRegFeesData.getExpMinMonths();
			int liExpMaxMonths = laRegFeesData.getExpMaxMonths();

			String lsTransCd = laCompleteTransactionData.getTransCode();
			Boolean lbGotoReg029 = new Boolean(false);
			// defect 9337
			// Do not go to REG029 if 'TONLY'
			if ((lsTransCd.equals(TransCdConstant.RENEW)
				|| lsTransCd.equals(TransCdConstant.EXCH)
				|| lsTransCd.equals(TransCdConstant.TOWP)
				|| lsTransCd.equals(TransCdConstant.TITLE)
				|| lsTransCd.equals(TransCdConstant.NONTTL)
				|| lsTransCd.equals(TransCdConstant.REJCOR))
				&& !laRegData.getRegPltCd().equals(
					CommonConstant.TONLY_REGPLTCD))
			{
				lbGotoReg029 = new Boolean(true);
				//Don't display REG029 screen for certain reasons [ 240].
				//check for special plate

				// defect 8900
				// Skip REG029 if FeeCalcCat = 4 (no reg fees)
				// defect 9126
				// Use isStandardExempt() which checks reg period length,
				// not FeeCalcCat = 4 (no regis fees)
				// ALWAYS go to REG029 is Special Plate 
				//int liRegClassCd = laRegData.getRegClassCd();
				//int liEffDate = new RTSDate().getYYYYMMDDDate();
				//CommonFeesData laCommonFeesData =
				//	CommonFeesCache.getCommonFee(
				//		liRegClassCd,
				//		liEffDate);

				// defect 9085 
				// determine if out of scope 
				boolean lbOutOfScope =
					PlateTypeCache.isOutOfScopePlate(
						laRegData.getRegPltCd());

				// Do not check Inventory Processing Code for Spcl Plt
				// If Min = Max months, do not go to REG029 (ie: the 
				// '&&' should be '||' : if ANY of these things are
				// true, do not got to REG029)
				// defect 9368
				// Add check for NewPlatesDesired w/ isPTOEligible
				// defect 9366
				// Add check for 'Plate Transfer Eligible', if so, must
				// go to REG029 to allow access to Cust Supplied chkbox.
				if (liExpMinMonths == liExpMaxMonths
					&& !PlateTypeCache.isSpclPlate(
						laRegData.getRegPltCd())
					&& !(laCompleteTransactionData
						.getVehicleInfo()
						.isPTOEligible()
						&& laCompleteTransactionData
							.getRegTtlAddlInfoData()
							.getNewPltDesrdIndi()
							== 1) //&& (laRegData.getOffHwyUseIndi() == 1
					|| (laRegData.getOffHwyUseIndi() == 1
						|| laRegData.getRegWaivedIndi() == 1
						|| lbOutOfScope //	|| (laCommonFeesData != null
				//		&& laCommonFeesData.getFeeCalcCat()
				//			== CommonConstant.NOREGISFEES)))
						|| CommonFeesCache.isStandardExempt(
							laRegData.getRegClassCd())))
				{
					lbGotoReg029 = new Boolean(false);
				}
				// end 9366
				// end 9368 
				// end defect 9126
				// Go to REG029 even if HQ				
				// end defect 8900

				// defect 9126				
				// Test if office of issuance is Headquarters [ 460]
				// skip REG029 unless Special Plates HQ doing EXCH [ 325]
				// TEST if not Special Plates HQ section
				// NOT Test if transaction code is EXCH [ 477]
				//else if (
				//	//laWSOfficeIds.getOfcIssuanceCd() == 1
				//SystemProperty
				//	.isHQ()
				//		&& (laCompleteTransactionData.getOfcIssuanceNo()
				//			!= 291
				//			|| (!lsTransCd.equals("EXCH"))))
				//{
				//	lbGotoReg029 = new Boolean(false);
				//}
				// end defect 9126				
				// end defect 9085

				// defect 9529
				// Check to see if this plate type allows
				// Customer Supplied and New Plates Desired
				if (PlateTypeCache
					.isCustSuppliedAllowed(laRegData.getRegPltCd())
					&& laCompleteTransactionData
						.getRegTtlAddlInfoData()
						.getNewPltDesrdIndi()
						== 1)
				{
					lbGotoReg029 = new Boolean(true);
				}
				// end defect 9529
			}
			// end defect 9337

			lvReturn.addElement(lbGotoReg029);
			lvReturn.addElement(laCompleteTransactionData);
			return lvReturn;
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Unexpected Type",
				"ERROR");
		}
	}

	/**
	 * This method serves as the entry point into the 
	 * CommonClientBusiness class.
	 * 
	 * This method will be called by the BusinessInterface and will 
	 * parse the command based on the aiFunctionId.  It will then decide
	 * what to do by either calling an internal method for any business
	 * logic that can be handled by the client, or the Comm layer for 
	 * logic that requires work to be done on the server side.
	 * 
	 * Thhe object passed is an graph of CompleteTransactionData
	 * 
	 * @param  aiModuleName int 
	 * @param  aiFunctionId int 
	 * @param  aaObject Object 
	 * @return Object 
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModuleName,
		int aiFunctionId,
		Object aaObject)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			// defect 10290 
			// Add brackets; Remove parameters where not needed. 
			// Refactored CommonConstant.DTA_MEDIA_WRITE, 
			//	  writePrintedBackToDTAMedia()
			case CommonConstant.GET_VEH :
				{
					return getVeh(aiModuleName, aiFunctionId, aaObject);
				}
			case CommonConstant.ADD_TRANS :
				{
					return addTrans(aaObject);
				}
			case CommonConstant.ADD_TRANS_FOR_VOID :
				{
					return addTransForVoid(aaObject);
				}
			case CommonConstant.GET_SAVED_VEH :
				{
					return getSavedVehicle();
				}
			case CommonConstant.GET_MISC_DATA :
				{
					return getMiscData(aaObject);
				}
			case CommonConstant.SAVE_VEH :
				{
					setSavedVehicle((MFVehicleData) aaObject);
					return null;
				}
			case CommonConstant.END_TRANS :
				{
					return endTrans(aaObject);
				}
			case CommonConstant.END_TRANS_FOR_VOID :
				{
					return endTransForVoid(aaObject);
				}
			case CommonConstant.GET_NEXT_COMPLETE_TRANS_VC :
				{
					return getNextCompleteTransVC(aaObject);
				}
			case CommonConstant.CAL_FEES :
				{
					return calculateFees(aaObject);
				}
			case CommonConstant.IS_NEXT_VC_REG029 :
				{
					return isNextVCREG029(aaObject);
				}
			case CommonConstant.VALIDATE :
				{
					return validateInventoryNum(aaObject);
				}
			case CommonConstant.GET_SAVED_INVENTORY :
				{
					return getSavedInv();
				}
			case CommonConstant.SAVE_INVENTORY :
				{
					if (aaObject != null)
					{
						if (aaObject instanceof Vector)
						{
							setSavedInv((Vector) aaObject);
						}
						else if (aaObject instanceof String)
						{
							setSavedInv(null);
						}
					}
					return null;
				}
			case CommonConstant.SAVE_VI_SPCL_PLT_VECTOR :
				{
					if (aaObject != null)
					{
						if (aaObject instanceof String)
						{
							setSavedMfgSpclPltVector(null);
						}
						else
						{
							setSavedMfgSpclPltVector(aaObject);
						}
					}
					return null;
				}
			case CommonConstant.GET_VI_SPCL_PLT_VECTOR :
				{
					return getSavedMfgSpclPltVector();
				}
				// defect 10491
			case CommonConstant.SAVE_VI_PRMT_VECTOR :
				{
					if (aaObject != null)
					{
						if (aaObject instanceof String)
						{
							setSavedVIPrmtVector(null);
						}
						else
						{
							setSavedVIPrmtVector(aaObject);
						}
					}
					return null;
				}
			case CommonConstant.GET_VI_PRMT_VECTOR :
				{
					return getSavedVIPrmtVector();
				}
				// end defect 10491 

			case CommonConstant.GET_SAVED_SPCL_PLT :
				{
					return getSavedSpclPlt();
				}
			case CommonConstant.SAVE_SPCL_PLT :
				{
					setSavedSpclPlt((SpecialPlatesRegisData) aaObject);
					return null;
				}
			case CommonConstant.CANCEL_TRANS :
				{
					return deleteTrans();
				}
			case CommonConstant.SET_ASIDE :
				{
					return setAside(aaObject);
				}
			case CommonConstant.GET_TRANS_PAYMENT :
				{
					return getTransPayment(
						aiModuleName,
						aiFunctionId,
						aaObject);
				}
			case CommonConstant.PUT_ON_HOLD :
				{
					return putOnHold(aaObject);
				}
			case CommonConstant.GET_VEH_MISC :
				{
					return getSavedVehicleMisc();
				}
			case CommonConstant.SAVE_VEH_MISC :
				{
					setSavedVehicleMisc((VehMiscData) aaObject);
					return null;
				}
			case CommonConstant.HOLD_INV003_ITM :
				{
					return holdInv003Itm(aaObject);
				}
			case CommonConstant.CANCEL_SELECTED_TRANS :
				{
					return deleteSelectedTrans(aaObject);
				}
			case CommonConstant.TAKE_OFF_HOLD :
				{
					return takeOffHold(aaObject);
				}
			case CommonConstant.ADD_TRANS_IRENEW :
				{
					return addTransIRenew(aaObject);
				}
			case CommonConstant.GET_TIME_PERMIT :
				{
					return getSavedTimedPermit();
				}
			case CommonConstant.SAVE_TIME_PERMIT :
				{
					setSavedTimePermit((TimedPermitData) aaObject);
					return null;
				}
			case CommonConstant.CLEAR_SAVE_VEH_INFO :
				{
					clearSavedVehInfo();
					return null;
				}
			case CommonConstant.REMOVE_INVENTORY :
				{
					removeInv((ProcessInventoryData) aaObject);
					return null;
				}
			case CommonConstant.TAKE_OFF_HOLD_TEMP :
				{
					return takeProcInvDataOffHold(
						(ProcessInventoryData) aaObject);
				}
			case CommonConstant.APP_MF_ERR_LOG :
				{
					appMfErrLog(aaObject);
					return null;
				}
			case CommonConstant.MULTI_ARCHIVE :
				{
					return Comm.sendToServer(
						aiModuleName,
						aiFunctionId,
						aaObject);
				}
				// defect 10290 
				// Don't really have to write back to DTA Media
				//  case CommonConstant.DTA_MEDIA_WRITE :
				//	  return writePrintedBackToDTAMedia(
			case CommonConstant.DTA_ADD_TRANS :
				{
					return addTrans(aaObject);
					// end defect 10290 
				}
			default :
				{
					return null;
				}
				// end defect 10290 
		}
	}

	/**
	 * Used in IssueInventory to put an inventory on hold.
	 * 
	 * @param	aaData Object
	 * @return	Object
	 * @throws 	RTSException 
	 */
	private Object putOnHold(Object aaData) throws RTSException
	{
		// DON'T PUT ON HOLD IF NOT IN DB ONLY IF ALLOCATED TO SOMEONE ELSE
		Map laMap = (Map) aaData;
		CompleteTransactionData laTransData =
			(CompleteTransactionData) laMap.get("DATA");
		ProcessInventoryData laProcInvData =
			(ProcessInventoryData) laMap.get("INV_DATA");
		ProcessInventoryData laOldData =
			(ProcessInventoryData) laMap.get("OLD_DATA");
		// if allocated to someone else, put it on hold		
		if (laProcInvData.getInvStatusCd() == 0
			&& (laProcInvData.getInvLocIdCd().equals("C")
				|| (laProcInvData.getInvLocIdCd().equals("W")
					&& laProcInvData.getInvId().equals(
						Integer.toString(
							SystemProperty.getWorkStationId())))
				|| (laProcInvData.getInvLocIdCd().equals("E")
					&& laProcInvData.getInvId().equals(
						SystemProperty.getCurrentEmpId()))))
		{
			laProcInvData.setInvQty(1);
			laProcInvData.setInvStatusCd(2);
			Vector laObjsOnHold =
				(Vector) Comm.sendToServer(
					GeneralConstant.INVENTORY,
					InventoryConstant.UPDATE_INVENTORY_STATUS_CD,
					laProcInvData);
			laMap.put("INV_DATA", laObjsOnHold.get(0));
		}
		// replace old record with new record, but take old record off
		// hold first
		if (laOldData != null)
		{
			// to release old object off hold - if not in db,
			// will throw exception
			try
			{
				takeProcInvDataOffHold(laOldData);
			}
			catch (RTSException aeRTSEx)
			{
				if (aeRTSEx.getCode() != NOT_IN_DB_CODE
					&& !aeRTSEx.getMsgType().equals(
						RTSException.SERVER_DOWN)
					&& !aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
				{
					laMap.put("EXCEPTION", aeRTSEx);
				}
			}
			laTransData.getAllocInvItms().remove(laOldData);
			laTransData.getInvItms().remove(laOldData);
			laTransData.getInvItms().add(laMap.get("INV_DATA"));
		}
		else
		{
			laTransData.getAllocInvItms().remove(laProcInvData);
			laTransData.getInvItms().remove(laProcInvData);
			laTransData.getInvItms().add(laMap.get("INV_DATA"));
		}
		if (!laTransData
			.getAllocInvItms()
			.contains(laMap.get("INV_DATA")))
		{
			laTransData.getAllocInvItms().add(laMap.get("INV_DATA"));
		}
		laMap.put("DATA", laTransData);
		laMap.put(
			"RETURN_CODE",
			new Integer(
				VCInventoryItemNumberInputINV001
					.VALIDATION_SUCCESSFUL));
		return laMap;
	}

	/**
	 * Remove the inventory specified from the memory
	 * 
	 * @param aaProcInvData ProcessInventoryData
	 */
	private void removeInv(ProcessInventoryData aaProcInvData)
	{
		if (svSavedProcessingInventoryData != null
			&& aaProcInvData != null)
		{
			for (int i = 0;
				i < svSavedProcessingInventoryData.size();
				i++)
			{
				ProcessInventoryData laCurrInv =
					(
						ProcessInventoryData) svSavedProcessingInventoryData
							.get(
						i);
				if (laCurrInv.getOfcIssuanceNo()
					== aaProcInvData.getOfcIssuanceNo()
					&& laCurrInv.getSubstaId()
						== aaProcInvData.getSubstaId()
					&& laCurrInv.getItmCd().equals(
						aaProcInvData.getItmCd())
					&& laCurrInv.getInvItmYr()
						== aaProcInvData.getInvItmYr()
					&& laCurrInv.getInvItmNo().equals(
						aaProcInvData.getInvItmNo()))
				{
					svSavedProcessingInventoryData.remove(laCurrInv);
				}
			}
			svSavedProcessingInventoryData.trimToSize();
		}
	}

	/**
	 * Calls the Transaction object setAside method
	 * 
	 * @param  aaObject Object 
	 * @return Object
	 * @throws RTSException
	 */
	private Object setAside(Object aaObject) throws RTSException
	{
		Transaction laTransaction = new Transaction();
		laTransaction.setAside((String) aaObject);
		return new CompleteTransactionData();
	}

	/**
	 * Save the issued inventory in vector. Used by Transaction to 
	 * remove all issued inventory from the inventory allocation table 
	 * in deleteIssueInventory().
	 * 
	 * @param avInvVec Vector 
	 */
	private void setSavedInv(Vector avInvVec)
	{
		if (avInvVec == null)
		{
			svSavedProcessingInventoryData = null;
			return;
		}
		if (svSavedProcessingInventoryData == null)
		{
			svSavedProcessingInventoryData = new Vector();
		}
		svSavedProcessingInventoryData.addAll(avInvVec);
	}

	/**
	 * 
	 * If Mfg Request, save SpecialPlateRegisData to vector for
	 * potential removal. 
	 * 
	 * @param avSpclPlt
	 */
	private void setSavedMfgSpclPltVector(Object aaObject)
	{
		if (aaObject == null)
		{
			svSavedMfgSpclPltRegisData = null;
		}
		else if (aaObject instanceof SpecialPlatesRegisData)
		{
			SpecialPlatesRegisData laSpclPltRegisData =
				(SpecialPlatesRegisData) aaObject;

			if (svSavedMfgSpclPltRegisData == null)
			{
				svSavedMfgSpclPltRegisData = new Vector();
			}
			svSavedMfgSpclPltRegisData.add(laSpclPltRegisData);
		}
		else if (aaObject instanceof Vector)
		{
			svSavedMfgSpclPltRegisData = (Vector) aaObject;
		}
	}

	/**
	 * Save Prmt VI to vector for potential removal
	 * 
	 * @param aaObject 
	 */
	private void setSavedVIPrmtVector(Object aaObject)
	{
		if (aaObject == null)
		{
			svSavedVIPrmtData = null;
		}
		else if (aaObject instanceof PermitData)
		{
			PermitData laPrmtData = (PermitData) aaObject;

			if (svSavedVIPrmtData == null)
			{
				svSavedVIPrmtData = new Vector();
			}
			svSavedVIPrmtData.add(laPrmtData);
		}
		else if (aaObject instanceof Vector)
		{
			svSavedVIPrmtData = (Vector) aaObject;
		}
	}

	/**
	 * 
	 * Save the SpecialPlateRegisData in Special Plates Events
	 * 
	 * @param aaSpclPltRegData
	 */
	private void setSavedSpclPlt(SpecialPlatesRegisData aaSpclPltRegData)
	{
		saSavedSpclPltRegisData = aaSpclPltRegData;
	}

	/**
	 * Save the TimePermit in Miscellaneous Registration. Used to  
	 * restore the information in more trans/same vehicle scenario.
	 * 
	 * @param aaTimedPermitData TimePermitData
	 */
	private void setSavedTimePermit(TimedPermitData aaTimedPermitData)
	{
		saSavedTimedPermitData = aaTimedPermitData;
	}

	/**
	 * Save the MFVehicleData.  Used to restore the information
	 * in more trans/same vehicle scenario.
	 * 
	 * @param aaSavedVehicle MFVehicleData
	 */
	private void setSavedVehicle(MFVehicleData aaSavedVehicle)
	{
		saSavedVehicle = aaSavedVehicle;
		// defect 6387 
		// Set ApprndCntyNo prior to saving
		if (saSavedVehicle != null
			&& saSavedVehicle.getRegData() != null
			&& saSavedVehicle.getRegData().getApprndCntyNo() != 0)
		{
			saSavedVehicle.getRegData().setApprndCntyNo(0);
		}
		// end defect 6387 
	}

	/**
	 * Save the VehMiscdata.  Used to restore the information
	 * in more trans/same vehicle scenario.
	 * 
	 * @param aaVehMiscData VehMiscData 
	 */
	private void setSavedVehicleMisc(VehMiscData aaVehMiscData)
	{
		saVehMiscData = aaVehMiscData;
	}

	/**
	 * Used by IssueInventory events to take allocated inventory off hold.
	 * 
	 * @param 	aaData Object
	 * @return 	Object
	 * @throws 	RTSException 
	 */
	private Object takeOffHold(Object aaData) throws RTSException
	{
		// If cancelling off of INV007, take all allocated items off hold
		CompleteTransactionData laTransData =
			(CompleteTransactionData) aaData;
		Vector lvInvItms = laTransData.getAllocInvItms();
		for (int i = 0; i < lvInvItms.size(); i++)
		{
			try
			{
				takeProcInvDataOffHold(
					(ProcessInventoryData) lvInvItms.get(i));
			}
			catch (RTSException aeRTSEx)
			{
				if (aeRTSEx.getCode() != NOT_IN_DB_CODE
					&& !aeRTSEx.getMsgType().equals(
						RTSException.SERVER_DOWN)
					&& !aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
					throw aeRTSEx;
			}
		}
		return laTransData;
	}

	/**
	 * Used by IssueInventory events to take allocated inventory off hold.
	 * 
	 * @param aaProcInvData ProcessInventoryData
	 * @return Object
	 * @throws RTSException
	 */
	private Object takeProcInvDataOffHold(ProcessInventoryData aaProcInvData)
		throws RTSException
	{
		try
		{
			aaProcInvData.setInvStatusCd(0);
			if (aaProcInvData.getInvItmEndNo() == null)
			{
				aaProcInvData.setInvItmEndNo(
					aaProcInvData.getInvItmNo());
			}
			// defect 10290
			// Vector unused  
			// Vector lvObjsOnHold = (Vector) 
			Comm.sendToServer(
				GeneralConstant.INVENTORY,
				InventoryConstant.UPDATE_INVENTORY_STATUS_CD,
				aaProcInvData);
			// end defect 10290 
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() != NOT_IN_DB_CODE
				&& !aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN)
				&& !aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
			{
				throw aeRTSEx;
			}
		}
		return null;
	}

	/**
	 * Verify Unique Inventory Issued within Same Transaction
	 * 
	 * @param  aaProcInvData   ProcessInventoryData
	 * @param  aaCompTransData CompleteTransactionData
	 * @throws RTSException  
	 */
	private void verifyUniqueInvSameTrans(
		ProcessInventoryData aaProcInvData,
		CompleteTransactionData aaCompTransData)
		throws RTSException
	{
		// Regional Collections / Disabled Placard can issue multiple of 
		// same itmcd/invitmyr
		// For nth issued inventory where n>1,ensure not same itmno

		// defect 9831  
		String lsTransCd = aaCompTransData.getTransCode();
		if ((lsTransCd.equals(TransCdConstant.RGNCOL)
			|| UtilityMethods.isDsabldPlcrdEvent(lsTransCd))
			&& aaCompTransData.getAllocInvItms().size() > 0)
			// || aaCompTransData.getTransCode().equals(TransCdConstant.BPM)
			// || aaCompTransData.getTransCode().equals(TransCdConstant.BTM)
			// || aaCompTransData.getTransCode().equals(TransCdConstant.RPNM)
			// || aaCompTransData.getTransCode().equals(
			// 	  TransCdConstant.RTNM))
			//&& aaCompTransData.getAllocInvItms().size() > 0)
			// end defect 9831 		
		{
			for (int i = 0;
				i < aaCompTransData.getAllocInvItms().size();
				i++)
			{
				ProcessInventoryData laTempProcInvData =
					(ProcessInventoryData) aaCompTransData
						.getAllocInvItms()
						.elementAt(
						i);
				if (laTempProcInvData.getInvItmNo() != null
					&& laTempProcInvData.getInvItmNo().equals(
						aaProcInvData.getInvItmNo()))
				{
					throw new RTSException(298);
				}
			}
		}
	}

	/**
	 * Used by IssueInventory events to take validate the inventory number
	 *
	 * @param 	aoObj Object
	 * @return	Object
	 * @throws	RTSException 
	 */
	private Object validateInventoryNum(Object aaObject)
		throws RTSException
	{
		Map laMap = (Map) aaObject;
		ProcessInventoryData laProcInvData =
			(ProcessInventoryData) laMap.get("INV_DATA");

		CompleteTransactionData laCompTransData =
			(CompleteTransactionData) laMap.get("DATA");

		try
		{
			// defect 7317
			// Ensure that same itmno not issued in same transaction
			verifyUniqueInvSameTrans(laProcInvData, laCompTransData);
			// end defect 7317
			//validate the validation number
			ValidateInventoryPattern laValidate =
				new ValidateInventoryPattern();
			laValidate.validateItmNoInput(
				laProcInvData.convertToInvAlloctnUIData(laProcInvData));
		}
		catch (RTSException aeRTSEx)
		{
			//if not valid, return to INV001 or INV005
			laMap.put(
				"RETURN_CODE",
				new Integer(
					VCInventoryItemNumberInputINV001
						.VALIDATION_FAILED));
			laMap.put("EXCEPTION", aeRTSEx);
			return laMap;
		}
		try
		{
			Vector lvObjsInDb = new Vector();
			// defect 4734
			// re-arrange how Inventory Server Business is called.
			// Now just call once.
			//
			try
			{
				// defect 4872
				Log.write(
					Log.DEBUG,
					this,
					" Begin Check for Inventory");
				lvObjsInDb =
					(Vector) Comm.sendToServer(
						GeneralConstant.INVENTORY,
						InventoryConstant
							.VERIFY_INVENTORY_ITEM_FOR_ISSUE,
						laMap.get("INV_DATA"));
				// end defect 4872

				Log.write(Log.DEBUG, this, " End Check for Inventory");

				// put new inv object into laMap
				if (lvObjsInDb != null && lvObjsInDb.size() > 0)
				{
					laMap.put("INV_DATA", lvObjsInDb.get(0));
				}
			} //end defect 4734
			catch (RTSException aeRTSEx)
			{
				if (aeRTSEx
					.getMsgType()
					.equals(RTSException.SERVER_DOWN)
					|| aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
				{
					ProcessInventoryData laTempProcInvData =
						(ProcessInventoryData) laMap.get("INV_DATA");
					laTempProcInvData.setInvLocIdCd("X");
					lvObjsInDb.add(laTempProcInvData);
				}
				else
				{
					throw aeRTSEx;
				}
			}

			if ((lvObjsInDb == null || lvObjsInDb.size() == 0)
				&& !SystemProperty.isHQ())
			{
				//if not valid, go to INV029
				laMap.put(
					"RETURN_CODE",
					new Integer(
						VCInventoryItemNumberInputINV001
							.ALLOCATION_FAILED));
				return laMap;
			}
			if ((lvObjsInDb == null
				|| lvObjsInDb.size() == 0) //&& liOfcCode == 1)
				&& SystemProperty.isHQ())
			{
				ProcessInventoryData laTempProcInvData =
					(ProcessInventoryData) laMap.get("INV_DATA");
				laTempProcInvData.setInvLocIdCd("U");
				lvObjsInDb = new Vector();
				lvObjsInDb.add(laTempProcInvData);
			}

			ProcessInventoryData laInvData =
				(ProcessInventoryData) lvObjsInDb.get(0);

			// defect 4233
			// Do not check who it is allocated to if it has already
			// been issued.

			// defect 10290
			// defect 10374
			//boolean lbDTA = Transaction.getDTADealerId() !=0;
			boolean lbDTA =
				UtilityMethods.isDTA(laCompTransData.getTransCode());
			// end defect 10374 

			if (!laInvData.isAlreadyIssued())
			{
				// defect 4586 
				// running DTA
				if (lbDTA)
				{
					String lsDealerId =
						Integer.toString(Transaction.getDTADealerId());
					//	if (laCompTransData.getDlrTtlDataObjs() != null)
					//	{
					//		String lsDealerId =
					//			Integer.toString(
					//				((DealerTitleData) laCompTransData
					//					.getDlrTtlDataObjs()
					//					.get(0))
					//					.getDealerId());
					// end defect 10290 
					// We are running DTA but the inventory does not
					// belong to the dealer
					// defect 5848
					// Don't go to INV029 if server down,
					// don't mark w/ "U"
					// replaced code 
					//

					if (!((laInvData.getInvLocIdCd().equals("D")
						&& laInvData.getInvId().equals(lsDealerId))
						|| (laInvData.getInvLocIdCd().equals("X"))))
					{
						// DTA - if not allocated to dealer, go to INV029
						laMap.put("DATA", laCompTransData);
						laMap.put(
							"RETURN_CODE",
							new Integer(
								VCInventoryItemNumberInputINV001
									.ALLOCATION_FAILED));
						return laMap;
					}
				}
				// this is when inv is allocated to dealer but the
				// transaction is not DTA
				if (laInvData.getInvLocIdCd().equals("D") && !lbDTA)
				{
					laMap.put("DATA", laCompTransData);
					laMap.put(
						"RETURN_CODE",
						new Integer(
							VCInventoryItemNumberInputINV001
								.ALLOCATION_FAILED));
					return laMap;
				}
				// end defect 4586

				if (laInvData.getInvLocIdCd().equals("E"))
				{
					if (!laInvData
						.getInvId()
						.equals(SystemProperty.getCurrentEmpId()))
					{
						laMap.put("DATA", laCompTransData);
						laMap.put(
							"RETURN_CODE",
							new Integer(
								VCInventoryItemNumberInputINV001
									.ALLOCATION_FAILED));
						return laMap;
					}
				}
				if (laInvData.getInvLocIdCd().equals("W"))
				{
					if (!laInvData
						.getInvId()
						.equals(
							Integer.toString(
								SystemProperty.getWorkStationId())))
					{
						laMap.put("DATA", laCompTransData);
						laMap.put(
							"RETURN_CODE",
							new Integer(
								VCInventoryItemNumberInputINV001
									.ALLOCATION_FAILED));
						return laMap;
					}
				}
				if (laInvData.getInvLocIdCd().equals("S"))
				{
					laMap.put("DATA", laCompTransData);
					laMap.put(
						"RETURN_CODE",
						new Integer(
							VCInventoryItemNumberInputINV001
								.ALLOCATION_FAILED));
					return laMap;
				} //defect 4713
				if (laInvData.getInvLocIdCd().equals("A"))
				{
					laMap.put("DATA", laCompTransData);
					laMap.put(
						"RETURN_CODE",
						new Integer(
							VCInventoryItemNumberInputINV001
								.ALLOCATION_FAILED));
					return laMap;
				} //end defect 4713
			} // end defect 4233
			// get old record, so we can delete it later from vctInv
			ProcessInventoryData delProcInvData =
				(ProcessInventoryData) laMap.get("INV_DATA");
			// replace old record with new record
			// if coming from INV001, replace old record in vctInv with
			// new record
			if (laMap.get("OLD_DATA") == null)
			{
				laCompTransData.getInvItms().remove(delProcInvData);
				laCompTransData.getInvItms().add(laMap.get("INV_DATA"));
			}
			// if coming from INV005, take the old record out of the
			// vectors and take off hold
			if (laMap.get("OLD_DATA") != null)
			{
				ProcessInventoryData oldData =
					(ProcessInventoryData) laMap.get("OLD_DATA");
				laCompTransData.getAllocInvItms().remove(oldData);
				laCompTransData.getInvItms().remove(oldData);
				laCompTransData.getInvItms().add(laMap.get("INV_DATA"));
				// try to take the old record off hold - if not in db,
				// just display error
				try
				{
					takeProcInvDataOffHold(oldData);
				}
				catch (RTSException aeRTSEx)
				{
					if (aeRTSEx.getCode() != NOT_IN_DB_CODE
						&& !aeRTSEx.getMsgType().equals(
							RTSException.SERVER_DOWN)
						&& !aeRTSEx.getMsgType().equals(
							RTSException.DB_DOWN))
						laMap.put("EXCEPTION", aeRTSEx);
				}
			}

			//if valid go to INV007
			laCompTransData.getAllocInvItms().add(
				laMap.get("INV_DATA"));

			laMap.put("DATA", laCompTransData);

			// the object has already been issued
			if (laInvData.isAlreadyIssued())
			{
				if (laInvData.isPreviouslyVoidedItem())
				{
					laMap.put("DATA", laCompTransData);
					laMap.put(
						"RETURN_CODE",
						new Integer(
							VCInventoryItemNumberInputINV001
								.ALLOCATION_FAILED));
					return laMap;
				}
				else
				{
					RTSException aeRTSEx =
						new RTSException(
							RTSException.CTL001,
							"The inventory item "
								+ laInvData.getInvItmNo()
								+ " has already been issued, do you"
								+ " want to continue and issue it?",
							"");
					laMap.put("EXCEPTION", aeRTSEx);
					laMap.put(
						"RETURN_CODE",
						new Integer(
							VCInventoryItemNumberInputINV001
								.ALREADY_ISSUED));
					return laMap;
				}
			}
			laMap.put(
				"RETURN_CODE",
				new Integer(
					VCInventoryItemNumberInputINV001
						.VALIDATION_SUCCESSFUL));
			return laMap;
			//}
		}
		catch (RTSException aeRTSEx)
		{
			//if not valid, return to INV001 or INV005
			laMap.put(
				"RETURN_CODE",
				new Integer(
					VCInventoryItemNumberInputINV001
						.VALIDATION_FAILED));
			laMap.put("EXCEPTION", aeRTSEx);
			return laMap;
		}
	}

	//	/**
	//	 * Write DTA data back to external media
	//	 * 
	//	 * @param aiModuleName int
	//	 * @param aiFunctionId int
	//	 * @param aaData Object
	//	 * @throws RTSException 
	//	 */
	//	private Object writePrintedBackToDTAMedia(
	//		int aiModuleName,
	//		int aiFunctionId,
	//		Object aaData)
	//		throws RTSException
	//	{
	//		CompleteTransactionData laTransData =
	//			(CompleteTransactionData) aaData;
	//		CompleteTransactionData laResultTransData =
	//			(CompleteTransactionData) addTrans(aiModuleName,
	//				aiFunctionId,
	//				aaData);
	//		Vector lvDlrObjects = laTransData.getDlrTtlDataObjs();
	//		// get the record that's being modified 
	//		int liRecNum = 0;
	//		DealerTitleData laLastChanged = null;
	//		for (int i = lvDlrObjects.size() - 1; i > -1; i--)
	//		{
	//			DealerTitleData laDealerData =
	//				(DealerTitleData) lvDlrObjects.get(i);
	//			if (laDealerData.isProcessed())
	//			{
	//				liRecNum = i;
	//				laLastChanged = laDealerData;
	//				break;
	//			}
	//		}
	//		laLastChanged.setTransId(laResultTransData.getTransId());
	//		try
	//		{
	//			Transaction.serializeObject(
	//				DEALER_INPUT_FILE,
	//				lvDlrObjects);
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			// Just ignore exceptions and continue with transaction
	//		}
	//		// Only write back that it was printed if print immediate is on.  
	//		// Otherwise write it at the end of all the transactions.
	//		// Only update diskette if it's going to be printed and the  
	//		// record came from diskette
	//		if (SystemProperty.getPrintImmediateIndi() == 1
	//			&& laLastChanged.isToBePrinted()
	//			&& !laLastChanged.isKeyBoardEntry()
	//			&& laLastChanged.isProcessed())
	//		{
	//			// defect 8217
	//			//	Reset getters to match DealerTitleData's 
	//			//int liPosPrintNum = laLastChanged.getPosPrintNum();
	//			int liPosPrintNum = laLastChanged.getPosPrntInvQty();
	//			if (laLastChanged.isToBePrinted())
	//			{
	//				//laLastChanged.setPosPrintNum(liPosPrintNum + 1);
	//				laLastChanged.setPosPrntInvQty(liPosPrintNum + 1);
	//			}
	//			// end defect 8217
	//			try
	//			{
	//				// defect 10075
	//				// Use new variable indicating either diskette or flashdrive
	//				//MediaParser.updateRecord(
	//				//	new File(TitleClientBusiness.DTA_FROM_DISKETTE),
	//				//	laLastChanged,
	//				//	TitleClientBusiness.BACK_SLASH,
	//				//	liRecNum);
	//				MediaParser.updateRecord(
	//					new File(SystemProperty.getExternalMedia()),
	//					laLastChanged,
	//					TitleClientBusiness.BACK_SLASH,
	//					liRecNum);
	//				// end defect 10075
	//			}
	//			catch (IOException aeIOEx)
	//			{
	//				// If there's an IO exception, just ignore it and
	//				// continue on with the transaction
	//			}
	//		}
	//		return laResultTransData;
	//	}

}
