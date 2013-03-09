package com.txdot.isd.rts.client.registration.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.registration.ui.SubconBundleManager;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.SubcontractorCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.registration.GenRegistrationReceipts;
import com.txdot.isd.rts.services.reports.registration.GenSubconRenewalReport;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.xml.XMLSecurity;

import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;

/*
 * SubcontractorRenewalClientBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ---------------------------------------------------------------------
 * K Salvi		11/04/2002	Changed name for preliminary subcon report to
 * 							Subcontractor renewal diskette report.
 * K Salvi		11/13/2002	Added fix for CQU100005096
 * K Salvi 		03/13/2003	Added fix for CQU100005786
 * K Salvi		03/13/2003 	Rolled back fix for CQU100005786 since it was 
 * 							fixed in the Print class.
 * K Salvi		03/18/2003	Added fix for CQU100005821 - deleted stickers 
 * 							were still being printed.
 * Jeff S.		05/26/2004	Removed call to 
 *							Print.getDefaultPrinterProps() b/c it is not
 *							being used anymore, and getDTAPageSettings() 
 *							was doing the same thing that 
 *							getDefaultPageProps() was.  Only need one method 
 *							for ease of maintenance.
 *							modify saveReports(Object)
 *							modify printReports(ReportSearchData)
 *							defect 7078 Ver 5.2.0
 * B Hargrove	08/18/2004	Modified to handle 15 fees for Ver 4 barcode.
 *							modify recalFees()
 *							defect 7348 Ver 5.2.1
 * K Harrell	08/24/2004	Modified for PltCd processing
 *							modify createCompTransDataForReceipt()
 *							defect 7456  Ver 5.2.1
 * K Harrell	08/29/2004	Null pointer exception from Comm.sendToServer
 *							if DBDown. Now do not call if !isDBUP()
 *							modify checkDisketteInventory(),
 *							getSubconINvAllocation(),procsSubconRenwl()
 *							defect 7493  Ver 5.2.1 
 * T Pederson	09/03/2004	Changed moveDiskInfoToTransMap() to set issue
 *							date for each subcontractor transaction to the date
 *							entered on the FrmSubcontractorEntryREG006.
 *							defect 7537  Ver 5.2.1
 * K Harrell	10/10/2004  Significant Subcon ReWork
 *							All methods modified for clarity
 *							defect 7586  Ver 5.2.1
 * K Harrell	10/10/2004  Add transactions to db in order
 *							originally entered in RSPS 
 *							addDiskTransAndEndTrans()
 *							defect 7579  Ver 5.2.1
 * K Harrell	10/10/2004  Does not handle DB Down when cancel off
 *							INV014
 *							releaseINV014Inventory()
 *							defect 7611  Ver 5.2.1
 * K Harrell	10/13/2004	Add Transaction Header during initialization
 *							for diskette only so that saved bundles will
 *							show up in Complete Vehicle Transaction.
 *							add addDiskTransHdr()
 *							modify initSubconRenewal()
 *							defect 7620  Ver 5.2.1
 * K Harrell	10/14/2004	If applicable, insert NewPlateNo into
 *							RTS_RSPS_PRNT
 *							modify moveDiskInfoToTransMap()
 *							defect 7629  Ver 5.2.1
 * K Harrell	10/15/2004	Insert to RTS_RSPS_PRNT did not initialize
 *							substaid
 * 							moveDiskInfoToTransMap()
 *							defect 7636  Ver 5.2.1
 * K Harrell	10/18/2004	Clear out old receipt directory when
 *							create TransHdr for diskette
 *							modify addDiskTransHdr() 
 *							defect 7645  Ver 5.2.1
 * K Harrell	10/29/2004	Set Held Inventory to BundleSRCD 
 *							modify saveIntermediateInfo()
 *							defect 7650  Ver 5.2.1 Fix 1
 * K Harrell	10/29/2004	VIN no longer required
 *							modify moveDiskInfoToTransMap()
 *							defect 7674  Ver 5.2.1 Fix 1
 * K Harrell	10/29/2004	Validate diskette county no
 *							modify copySubconDiskette()
 *							defect 7628  Ver 5.2.1 Fix 1
 * K Harrell	01/18/2005	If modify all diskette transactions, no
 *							transactions are written to DB or cache.
 *							modify saveTransactionInfo() 
 *							defect 7900 Ver 5.2.2
 * K Harrell	02/01/2005	Restore saveTransactionInfo() to prior state.
 *							Moving fix to FrmRegistrationSubcon...REG007
 *							modify saveTransactionInfo() 
 *							defect 7900 Ver 5.2.2
 * K Harrell	02/09/2005	For scanned, keyed Renewals, save last 
 *							last transTime to prevent 
 *							duplicate TransIds when scanned/keyed
 *							modify procsSubconRenwl()
 *							defect 7974 Ver 5.2.1 Fix 3 
 * Ray Rowehl	02/09/2005	Change reference to Transaction.  changed
 * 							package. changed variable names to conform
 * 							to standards. 
 * 							organize imports, format source.
 * 							modify import
 * 							modify procsCompleteSubconRenwl()
 * 							defect 7705 Ver 5.2.3
 * K Harrell	03/22/2005	Merge defect 7974 (02/09/2005 Ver5.2.1 Fix3)
 * B Hargrove	06/16/2005	Remove unused arguments and variables.
 * 							modify addDiskTransAndEndTrans(),
 * 							cancelHeldSubcon(), cancelSubcon(),
 * 							copySubconDiskette(), getSubconInvAllocation(),
 *							initSubconRenewal(), manageSubconCacheList(),
 * 							processData(), procsCompleteSubconRenwl(),
 * 							procsDeleteSubconRenwl(),
 * 							releaseINV003Inventory()
 * 							defect 7894 Ver 5.2.3  
 * K Harrell	06/29/2005	Plate inventory is not released if allocated,
 *							issued with SBRNW transaction and SBRNW trans
 *							subsequently deleted from bundle.
 *							modify procsDeleteSubconRenwl() 
 *							defect 8230  Ver 5.2.2 Fix 5
 * K Harrell	07/20/2005	Duplicate VIN error if multiple no VIN RSPS 
 * 							trans and press no on Total Confirm screen.
 *							modify save TransactionInfo()
 *							defect 8289 Ver 5.2.2 Fix 6
 * K Harrell	08/22/2005	Remove last two of four parameters on call 
 * 							to Transaction.addTrans()
 * 							modify procsSubconRenwlTrans() 
 * 							defect 8344 Ver 5.2.3
 * K Harrell	11/02/2005	Use ReportConstant.RPT_1_COPY & RPT_7_COPIES
 * 							modify generateSubconReport()
 * 							defect 8379 Ver 5.2.3
 * J Ralph		01/18/2006	Added FormFeed to end of Receipt string
 * 							add FF
 * 							modify saveAndGenerateReceipts()
 * 							defect 8502 Ver 5.2.2 Fix 8
 * J Ralph		01/27/2006	FormFeed constant cleanup
 * 							Source format 							
 *							delete FF
 *							modify saveAndGenerateReceipts()
 * 							defect 8524 Ver 5.2.3    
 * Jeff S.		08/08/2006	Add the ability to accept XML data for the
 * 							subcon data.  There is a check to see if the
 * 							data on the diskett is XML or just delemited
 * 							data.
 * 							add XML_TRANS_TAG
 * 							modify copySubconDiskette()
 * 							defect 8451 Ver 5.2.4
 * T Pederson	10/09/2006	Allow for a subcontractor total fee amount   
 * 							of zero dollars. 
 * 							modify recalFees() 
 * 							defect 8900 Ver Exempts
 * K Harrell	02/08/2007  Move Special Plate RegPltCd, OrgNo to 
 * 							RegData to be printed on receipt.
 * 							modify createCompTransDataForReceipt()
 * 							defect 9085  Ver Special Plates
 * K Harrell	04/15/2007  Assign OrgNo & AddlSetIndi from barcode
 * 							modify recalFees()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/01/2007	Process Special Plates, FeeCalcCat = 4
 * 							modify recalcFees()
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/17/2007	Removed assignment of OrgNo,AddlSetIndi
 * 							from barcode; redundant. 
 * 							modify recalcFees()
 * 							defecct 9362 Ver Special Plates 2   
 * B Hargrove	04/16/2008	Checking references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe. (see: defect 9557).
 * 							No changes needed here. 
 * 							defect 9631 Ver Defect POS A
 * K Harrell	01/08/2009	Remove reference to RegisData.setOrgNo() 
 * 							modify createCompTransDataForReceipt() 
 * 							defect 9912 Ver Defect_POS_D   
 * B Hargrove	06/01/2009  Add Flashdrive option to RSPS Subcon. 
 * 							References to DiskParser class became
 * 							MediaParser.
 *                   		modify INVALID_RECORD_HDR, 
 * 							generateSubconReport(), processData(), 
 * 							rename copySubconDiskette() to
 * 							copySubconRSPSInput())
 * 							delete SUBCON_FILE_FROM
 * 							defect 10064 Ver Defect_POS_F
 * K Harrell	06/18/2009	Implement ReportConstant for "Next Screen"
 * 							modify generateSubconReport()
 * 							defect 10011 Ver Defect_POS_F  
 * K Harrell	06/30/2009	Implement new OwnerData()
 * 							modify createCompTransDataForReceipt()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	08/15/2009	Present CountyNo on External Media in 
 * 							 Development.  
 * 							modify copySubconRSPSInput()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	08/17/2009	Implement UtilityMethods.addPCLandSaveReport()
 * 							delete saveReports()
 * 							modify printReports(), copySubconRSPSInput(), 
 * 							recalcFees(), generateSubconReport(), 
 * 							processData(), procsCompleteSubconRenwl(), 
 * 							printReport()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	02/19/2010 	Implement new SubcontractorData()
 * 							modify createCompTransDataForReceipt(), 
 * 							 copySubconRSPSInput(), initSubconHdr(),
 * 							 getSubconInvAllocation(),
 * 							 moveDiskInfoToTransMap()
 * 							defect 10161 Ver POS_640 
 * T Pederson	04/14/2010 	Change so that new plate expiration and
 * 							plate term will be written on receipt.
 * 							modify createCompTransDataForReceipt() 
 *							defect 10392  Ver POS_640
 * T Pederson	05/02/2010 	Added calculation for plate sold months.
 * 							modify procsSubconRenwlTrans() 
 *							defect 10392  Ver POS_640
 * K Harrell	02/04/2011	modify saveAndGenerateReceipts()
 * 							defect 10745  Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * SubcontractorRenewalClientBusiness class is responsible for 
 * subcontractor renewal events.
 *
 * @version	 6.7.0			02/04/2011
 * @author	Nancy Ting
 * <br>Creation Date:		10/18/2001 10:07:24  
 */
public class SubcontractorRenewalClientBusiness implements Displayable
{
	//TODO Displayable???   (KPH)

	// Constants 
	private static final String BACK_SLASH = "\\";
	private static final String INVALID_RECORD_HDR =
		"Record input does not conform to the defined record format."
			+ "  Refer to records: ";
	private static final String INVALID_RECORD_SEPARATOR = ", ";
	private static final String SUBCON_FILE_TO =
		"d:\\rts\\rtsappl\\subcon.dat";
	private static final String XML_TRANS_TAG = "Transaction";

	/**
	 * SubcontractorRenewalClientBusiness constructor.
	 */
	public SubcontractorRenewalClientBusiness()
	{
		super();
	}

	/**
	 * Add unposted disk transactions and end the transaction
	 * 
	 * @return Object
	 * @param aaData Object
	 * @throws RTSException  
	 */
	private Object addDiskTransAndEndTrans(Object aaData)
		throws RTSException
	{
		// defect 7586
		// Post each transaction individually  
		// retrieve Bundle
		SubcontractorRenewalCacheData laBundleSRCD =
			(SubcontractorRenewalCacheData) aaData;

		// SubconTransData: SortedMap of Subcontractor Transactions
		Iterator laIterator =
			laBundleSRCD.getSubconTransData().keySet().iterator();
		SortedMap laSubconTransData = laBundleSRCD.getSubconTransData();

		// defect 7579
		// Pull transactions in TransKeyNumber order 
		Integer[] larrArray = new Integer[laSubconTransData.size()];
		int liIndex = 0;
		while (laIterator.hasNext())
		{
			SubcontractorRenewalData laSubconRenewData =
				(SubcontractorRenewalData) laSubconTransData.get(
					laIterator.next());
			larrArray[liIndex] = laSubconRenewData.getTransKeyNumber();
			liIndex = liIndex + 1;
		}
		Arrays.sort(larrArray);

		// Post any unposted transactions;  
		// Note:All posted if press "No" on the prompted total payment 
		// message. 
		for (int j = 0; j < larrArray.length; j++)
		{
			SubcontractorRenewalData laSubconRenewData =
				(SubcontractorRenewalData) laSubconTransData.get(
					larrArray[j]);
			laBundleSRCD.setTempSubconRenewalData(laSubconRenewData);
			if (laSubconRenewData.isPosted())
			{
				continue;
			}
			else
			{
				SubcontractorRenewalCacheData laToServerSRCD =
					setUpTrans(laBundleSRCD);
				// If plate not printable and plate ownership not yet 
				// resolved
				boolean lbValidatePlate =
					(laSubconRenewData.getPltItmCd() != null
						&& !(StickerPrintingUtilities
							.isStickerPrintable(
								laSubconRenewData.getPltItmCd()))
						&& laSubconRenewData.getProcInvPlt() == null);

				// Process and potentially post transaction
				procsSubconRenwlTrans(
					laBundleSRCD,
					laToServerSRCD,
					lbValidatePlate);

				// Update saved bundle info
				saveTransactionInfo(
					laBundleSRCD,
					laToServerSRCD,
					true,
					false);
			}
			laBundleSRCD.setAllTransPosted(true);
			// end defect 7579 
		}
		return laBundleSRCD;
	}

	/**
	 * add Transaction Header for Diskette Processing
	 * 
	 * @param  aaSRCD SubcontractorRenewalCacheData
	 * @return SubcontractorRenewalCacheData
	 * @throws RTSException
	 */
	private SubcontractorRenewalCacheData addDiskTransHdr(SubcontractorRenewalCacheData aaSRCD)
		throws RTSException
	{
		// defect 7620
		Transaction laTransaction =
			new Transaction(TransCdConstant.SBRNW);
		laTransaction.setUpFirstTransactionOfDay();
		CompleteTransactionData laCTD = new CompleteTransactionData();
		laCTD.setTransCode(TransCdConstant.SBRNW);
		laCTD.setOfcIssuanceNo(SystemProperty.getOfficeIssuanceNo());
		laCTD.setSubconId(SystemProperty.getSubStationId());
		laCTD.setSubcontractorRenewalCacheData(aaSRCD);

		// Setup Transaction Header for SBRNW
		TransactionHeaderData laTransactionHeaderData =
			laTransaction.addTransHeader(laCTD, false);
		laTransactionHeaderData.setSubconTrans(true);
		Transaction.setTransactionHeaderData(laTransactionHeaderData);
		TransactionCacheData laTransactionCacheData =
			new TransactionCacheData();
		laTransactionCacheData.setProcName(TransactionCacheData.INSERT);
		laTransactionCacheData.setObj(laTransactionHeaderData);
		Vector lvTransactionCacheVector = new Vector();
		lvTransactionCacheVector.addElement(laTransactionCacheData);
		laTransaction.processCacheVector(lvTransactionCacheVector);

		// defect 7645
		// clear out old receipt directory
		Transaction.createReceiptDirectory(false);
		// end defect 7645
		// Assign to aaSRCD
		aaSRCD.setTransactionHeaderData(laTransactionHeaderData);
		// defect 7620
		return aaSRCD;
	}

	/**
	 * Release the held subcontractor items
	 * 
	 * @return Object
	 * @param aaData Object
	 * @throws RTSException 
	 */
	private Object cancelHeldSubcon(Object aaData) throws RTSException
	{
		SubcontractorRenewalCacheData laBundleSRCD =
			(SubcontractorRenewalCacheData) aaData;
		// Release any inventory on hold 
		if (laBundleSRCD.getHeldInvPlt() != null
			|| laBundleSRCD.getDiskHeldPltList().size() > 0)
		{
			Vector lvTransactionCacheVector = new Vector();
			RTSDate laRTSDate = new RTSDate();
			TransactionCacheData laTransactionCacheData = null;
			// Release inventory on hold for prompting
			if (laBundleSRCD.getHeldInvPlt() != null)
			{
				laBundleSRCD.getHeldInvPlt().setTransTime(
					laRTSDate.get24HrTime());
				laBundleSRCD.getHeldInvPlt().setTransAMDate(
					laRTSDate.getAMDate());
				laBundleSRCD.getHeldInvPlt().setInvStatusCd(0);
				laTransactionCacheData = new TransactionCacheData();
				laTransactionCacheData.setObj(
					laBundleSRCD.getHeldInvPlt());
				laTransactionCacheData.setProcName(
					TransactionCacheData.UPDATE);
				lvTransactionCacheVector.addElement(
					laTransactionCacheData);
			}
			// Release any held inventory from diskette transaction if 
			// enter is pressed
			Iterator laIterator =
				laBundleSRCD.getDiskHeldPltList().keySet().iterator();
			Hashtable lhtHeldDiskInv =
				laBundleSRCD.getDiskHeldPltList();
			while (laIterator.hasNext())
			{
				laTransactionCacheData = new TransactionCacheData();
				ProcessInventoryData laProcInvData =
					(ProcessInventoryData) lhtHeldDiskInv.get(
						laIterator.next());
				laProcInvData.setInvStatusCd(0);
				laProcInvData.setTransAMDate(laRTSDate.getAMDate());
				laProcInvData.setTransTime(laRTSDate.get24HrTime());
				laTransactionCacheData.setObj(laProcInvData);
				laTransactionCacheData.setProcName(
					TransactionCacheData.UPDATE);
				lvTransactionCacheVector.addElement(
					laTransactionCacheData);
			}
			// Process request through transaction 	
			Transaction laTransaction = new Transaction();
			laTransaction.processCacheVector(lvTransactionCacheVector);
			//clear all held list
			laBundleSRCD.getDiskHeldPltList().clear();
			laBundleSRCD.setHeldInvPlt(null);
			SubconBundleManager.saveBundle(laBundleSRCD);
		}
		Transaction.reset();
		return null;
	}

	/**
	 * Cancel all the transactions entered and release all issued 
	 * inventory and held inventory
	 * 
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object cancelSubcon(Object aaData) throws RTSException
	{
		SubcontractorRenewalCacheData laBundleSRCD =
			(SubcontractorRenewalCacheData) aaData;

		Vector lvTransactionCacheVector = new Vector();

		// Transaction Delete Processing 
		// Initialize TransactionHeaderData for processing cancel 	
		TransactionHeaderData laTransactionHeaderData =
			Transaction.getTransactionHeaderData();

		// Add delete requests to lvTransactionCache Vector	for
		// transaction tables 
		if (laTransactionHeaderData != null)
		{
			Transaction.createDeleteAllTransVector(
				lvTransactionCacheVector,
				laTransactionHeaderData,
				new RTSDate());
		}

		// Inventory Delete Processing 
		RTSDate laRTSDate = new RTSDate();
		Iterator laIterator =
			laBundleSRCD.getIssuedInventories().keySet().iterator();
		Hashtable lhtIssueInv = laBundleSRCD.getIssuedInventories();

		// Add "release" request for Subcontractor Inventory (non-disk)
		while (laIterator.hasNext())
		{
			TransactionCacheData laTransactionCacheData =
				new TransactionCacheData();
			ProcessInventoryData laProcInvData =
				(ProcessInventoryData) lhtIssueInv.get(
					laIterator.next());
			if (!laProcInvData.getInvLocIdCd().equals("U"))
			{
				laProcInvData.setInvStatusCd(0);
				laProcInvData.setTransAMDate(laRTSDate.getAMDate());
				laProcInvData.setTransTime(laRTSDate.get24HrTime());
				laTransactionCacheData.setObj(laProcInvData);
				laTransactionCacheData.setProcName(
					TransactionCacheData.UPDATE);
				lvTransactionCacheVector.addElement(
					laTransactionCacheData);
			}
		}

		// Add "release" request for Subcontractor Inventory (disk)
		laIterator =
			laBundleSRCD.getDiskHeldPltList().keySet().iterator();
		Hashtable lhtHeldDiskInv = laBundleSRCD.getDiskHeldPltList();
		while (laIterator.hasNext())
		{
			TransactionCacheData laTransactionCacheData =
				new TransactionCacheData();
			ProcessInventoryData laProcInvData =
				(ProcessInventoryData) lhtHeldDiskInv.get(
					laIterator.next());
			laProcInvData.setInvStatusCd(0);
			laProcInvData.setTransAMDate(laRTSDate.getAMDate());
			laProcInvData.setTransTime(laRTSDate.get24HrTime());
			laTransactionCacheData.setObj(laProcInvData);
			laTransactionCacheData.setProcName(
				TransactionCacheData.UPDATE);
			lvTransactionCacheVector.addElement(laTransactionCacheData);
		}

		// Add "release" request for Subcontractor Inventory 
		// (held - next prompt)	
		if (laBundleSRCD.getHeldInvPlt() != null)
		{
			TransactionCacheData laTransactionCacheData =
				new TransactionCacheData();
			laBundleSRCD.getHeldInvPlt().setInvStatusCd(0);
			laBundleSRCD.getHeldInvPlt().setTransAMDate(
				laRTSDate.getAMDate());
			laBundleSRCD.getHeldInvPlt().setTransTime(
				laRTSDate.get24HrTime());
			laTransactionCacheData.setObj(laBundleSRCD.getHeldInvPlt());
			laTransactionCacheData.setProcName(
				TransactionCacheData.UPDATE);
			lvTransactionCacheVector.addElement(laTransactionCacheData);
		}

		// Process delete transactions / release inventory
		Transaction laTransaction =
			new Transaction(TransCdConstant.SBRNW);
		laTransaction.processCacheVector(lvTransactionCacheVector);
		Transaction.reset();

		// Erase subcon cache  (d:\rts\rtsappl\subcon.ser)
		SubconBundleManager.deleteBundle();

		return null;
	}

	/**
	 * Check on the server side whether the list of inventory exists
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object checkDisketteInventory(
		int aiModuleName,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		// Retrieve Subcon Bundle   
		SubcontractorRenewalCacheData laBundleSRCD =
			(SubcontractorRenewalCacheData) aaData;

		// Create the Inventory checklist 
		HashSet lhsInventoryCheckList =
			laBundleSRCD.getInventoryCheckList();
		Iterator laIterator = lhsInventoryCheckList.iterator();
		SortedMap laTransInfo = laBundleSRCD.getSubconTransData();

		String lsInvId =
			String.valueOf(
				laBundleSRCD.getSubcontractorHdrData().getSubconId());

		Hashtable lhtInventoryList = new Hashtable();
		while (laIterator.hasNext())
		{
			SubcontractorRenewalData laSubconRenewData =
				(SubcontractorRenewalData) laTransInfo.get(
					laIterator.next());
			ProcessInventoryData laProcInvData =
				new ProcessInventoryData();
			laProcInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laProcInvData.setSubstaId(SystemProperty.getSubStationId());
			laProcInvData.setInvItmNo(laSubconRenewData.getNewPltNo());
			laProcInvData.setInvItmEndNo(
				laSubconRenewData.getNewPltNo());
			laProcInvData.setItmCd(laSubconRenewData.getPltItmCd());
			laProcInvData.setInvQty(1);
			laProcInvData.setInvId(lsInvId);
			laProcInvData.setInvLocIdCd("S");
			if (laSubconRenewData.getStkrItmCd() == null)
			{
				laProcInvData.setInvItmYr(
					laSubconRenewData.getNewExpYr());
			}
			else
			{
				laProcInvData.setInvItmYr(0);
			}
			lhtInventoryList.put(
				laSubconRenewData.getTransKeyNumber(),
				laProcInvData);
		}
		// Verify Inventory at Server 
		try
		{
			// Inventory allocated to subcontractor will be returned,
			// otherwise, would be null
			// defect 7493 
			if (com
				.txdot
				.isd
				.rts
				.client
				.desktop
				.RTSApplicationController
				.isDBReady())
			{
				lhtInventoryList =
					(Hashtable) Comm.sendToServer(
						aiModuleName,
						aiFunctionId,
						lhtInventoryList);
			}
			else
			{
				lhtInventoryList.clear();
			}
			// end defect 7493
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getMsgType().equals(RTSException.SERVER_DOWN)
				|| aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
			{
				//INV003 does not pop up in server down mode
				lhtInventoryList.clear();
			}
			else
			{
				throw aeRTSEx;
			}
		}
		if (lhtInventoryList.size() != 0)
		{
			Hashtable lhtDiskHeldInv =
				laBundleSRCD.getDiskHeldPltList();
			HashSet lhsINV003List = laBundleSRCD.getInvValIndex();
			lhsINV003List.clear();
			//set error flag to specific
			laIterator = lhtInventoryList.keySet().iterator();
			while (laIterator.hasNext())
			{
				Integer liKey = (Integer) laIterator.next();
				Object laInventoryInfo = lhtInventoryList.get(liKey);
				SubcontractorRenewalData laSubconRenewData =
					(SubcontractorRenewalData) laTransInfo.get(liKey);

				if (laInventoryInfo instanceof String)
				{
					//needs to pop up INV003
					laSubconRenewData.setError(true);
					lhsINV003List.add(
						laSubconRenewData.getTransKeyNumber());
				}
				else
				{
					laSubconRenewData.setProcInvPlt(
						(ProcessInventoryData) laInventoryInfo);
					lhtDiskHeldInv.put(
						laSubconRenewData.getTransKeyNumber(),
						laInventoryInfo);
				}
			}
		}
		return laBundleSRCD;
	}

	/**
	 * Copy, parse and validate Subcon RSPS input (flashdrive or diskette)
	 * @return Object
	 * @throws RTSException
	 */
	private Object copySubconRSPSInput() throws RTSException
	{
		//Copy the file to destination
		try
		{
			// defect 10064
			// Use file input from either diskette or flashdrive
			//FileUtil.copyFile(SUBCON_FILE_FROM, SUBCON_FILE_TO);
			FileUtil.copyFile(
				SystemProperty.getExternalMedia(),
				SUBCON_FILE_TO);
			// end defect 10064
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		try
		{
			File laFile = new File(SUBCON_FILE_TO);
			if (!laFile.exists())
			{
				throw new FileNotFoundException();
			}

			// defect 8451
			// If the supplied file is an XML file then use the 
			// XMLLoader class else use the MediaParser class.
			// Build Vector from Subcontractor Diskette Records 
			Vector lvSubconDisketteRecords;
			if (XMLSecurity.checkIfXMLFile(SUBCON_FILE_TO))
			{
				lvSubconDisketteRecords =
					XMLLoader.load(
						SUBCON_FILE_TO,
						new SubcontractorRenewalData(),
						XML_TRANS_TAG);
			}
			else
			{
				// defect 10064
				// DiskParser refactored/renamed to MediaParser
				lvSubconDisketteRecords =
					MediaParser.parse(
						new File(SUBCON_FILE_TO),
						new SubcontractorRenewalData(),
						BACK_SLASH);
				// end defect 10064
			}
			// end defect 8451

			// Initialize SubcontractorRenewalCacheData
			SubcontractorRenewalCacheData laSRCD =
				new SubcontractorRenewalCacheData();
			laSRCD.setSubconDiskData(lvSubconDisketteRecords);
			// SubcontractorHdrData will be populated from diskette data
			SubcontractorHdrData laSubconHdrData =
				new SubcontractorHdrData();
			laSRCD.setSubcontractorHdrData(laSubconHdrData);
			// Booleans to denote presence of valid/invalid records 
			boolean lbFoundValidRecord = false;
			boolean lbContainInvalidRecord = false;
			// defect 7628
			// Validate OfcIssuanceNo 
			boolean lbInvalidOfcIssuanceNo = false;

			// defect 8628 
			String lsInvalidCntyNo = new String();
			// end defect 8628 

			// Build invalid record string
			StringBuffer lsStringBuf = new StringBuffer();
			lsStringBuf.append(INVALID_RECORD_HDR);
			for (int i = 0; i < lvSubconDisketteRecords.size(); i++)
			{
				// Search for the subcon id and set invalid record string
				SubcontractorRenewalData laSubconRenewData =
					(
						SubcontractorRenewalData) lvSubconDisketteRecords
							.get(
						i);
				if (laSubconRenewData.getCntyNo()
					!= SystemProperty.getOfficeIssuanceNo())
				{
					lbInvalidOfcIssuanceNo = true;

					// defect 8628 
					// Save Invalid CountyNo to include in message  
					lsInvalidCntyNo =
						", " + laSubconRenewData.getCntyNo() + ",";
					// end defect 8628 
					break;
				}
				// Set invalid message. Append comma to separate record 
				// numbers	
				if (laSubconRenewData.isInvalidRecord())
				{
					if (lbContainInvalidRecord)
					{
						lsStringBuf.append(INVALID_RECORD_SEPARATOR);
					}
					lsStringBuf.append(i + 1);
					lbContainInvalidRecord = true;
				}
				else
				{
					// Assign subcon info, issue date from first valid 
					// subcon data
					laSubconHdrData.setSubconIssueDate(
						laSubconRenewData.getSubconIssueDate());
					if (!lbFoundValidRecord)
					{
						SubcontractorData laSubconData =
							new SubcontractorData();
						// defect 8628 
						// Previously set SUBSTAID to SUBCONID  
						laSubconData.setId(
							laSubconRenewData.getSubconId());
						// laSubconData.setSubstaId(
						//	laSubconRenewData.getSubconId());
						// end defect 8628 
						laSRCD.setSubconInfo(laSubconData);
						laSubconHdrData.setLastExportOnDate(
							laSubconRenewData.getLastExport());
						laSubconHdrData.setExportOnDate(
							laSubconRenewData.getExportOn());
						laSubconHdrData.setDiskSeqNo(
							laSubconRenewData.getDiskSeqNo());
						laSubconHdrData.setProcsId(
							laSubconRenewData.getRSPSId());
						lbFoundValidRecord = true;
					}
				}
			}
			// OfcIssuanceNo doesn't match county
			if (lbInvalidOfcIssuanceNo)
			{
				// defect 8628
				String lsMsg =
					"The office number input"
						+ lsInvalidCntyNo
						+ " does not match this county.";

				// For POS Support 
				System.out.println(lsMsg);

				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					lsMsg,
					"");
				// end defect 8628
			}
			// end defect 7628
			// All records are invalid
			if (!lbFoundValidRecord)
			{
				throw new RTSException(RTSException.FAILURE_MESSAGE,
				// defect 10064
				//"All records on the diskette are corrupt.",
				"All records input are corrupt.",
				// end defect 10064
				"");
			}
			//set invalid record string
			if (lbContainInvalidRecord)
			{
				laSRCD.setInvalidRecordsMsg(lsStringBuf.toString());
			}
			//Get subcon info from SubcontractorCache
			// defect 10161
			SubcontractorData laSubconData =
				SubcontractorCache.getSubcon(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId(),
					laSRCD.getSubconInfo().getId());
			// end defect 10161 

			if (laSubconData == null)
			{
				// defect 10161 
				// For POS Support 
				System.out.println(
					"Invalid Subcontractor Id: "
						+ laSRCD.getSubconInfo().getId());
				// defect 10161

				//throw new RTSException(701);
				throw new RTSException(
					ErrorsConstant
						.ERR_NUM_CANNOT_FIND_SUBCONTRACTOR_ID);
				// end defect 8628
			}
			else
			{
				laSRCD.setSubconInfo(laSubconData);
			}
			// Retrieve allocated inventory for Subcontractor
			laSRCD =
				(SubcontractorRenewalCacheData) processData(GeneralConstant
					.REGISTRATION,
					RegistrationConstant.GET_SUBCON_ALLOCATED_INV,
					laSRCD);
			return laSRCD;
		} // end try box
		catch (FileNotFoundException aeFNFEx)
		{
			throw new RTSException("", aeFNFEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException("", aeIOEx);
		}
	}

	//	/**
	//	 * Create CompleteTransactionData Object with Sticker/Plate 
	//	 * Objects for Receipt
	//	 * 
	//	 * @param aaSRCD SubcontractorRenewalCacheData
	//	 * @param aaSubconRenewData SubcontractorRenewalData
	//	 * @return CompleteTransactionData 
	//	 */
	//	private CompleteTransactionData createCompTransDataForReceipt(
	//		SubcontractorRenewalCacheData aaSRCD,
	//		SubcontractorRenewalData aaSubconRenewData)
	//	{
	//		CompleteTransactionData laCTD = new CompleteTransactionData();
	//		laCTD.setTransCode(TransCdConstant.SBRNW);
	//		ProcessInventoryData laProcInvData = null;
	//		Vector lvInventory = new Vector();
	//		// Data for Receipt
	//		// Owner Info: Using Subcon Info as owner data 
	//		MFVehicleData laMFVehicle = new MFVehicleData();
	//		OwnerData laOwnerData = new OwnerData();
	//		SubcontractorData laSubconData = aaSRCD.getSubconInfo();
	//
	//		// defect 10161
	//		laOwnerData.setName1(laSubconData.getName1());
	//		laOwnerData.setName2(laSubconData.getName2());
	//		AddressData laAddressData =
	//			(AddressData) UtilityMethods.copy(
	//				laSubconData.getAddressData());
	//
	//		//		AddressData laAddressData = new AddressData(); 
	//		//		laAddressData.setSt1(laSubconData.getSt1());
	//		//		laAddressData.setSt2("");
	//		//		laAddressData.setCity(laSubconData.getSubconCity());
	//		//		laAddressData.setState("TX");
	//		//		laAddressData.setZpcd(laSubconData.getZpcd());
	//		//		laAddressData.setZpcdp4(laSubconData.getZpcdp4());
	//		// end defect 10161 
	//
	//		laOwnerData.setAddressData(laAddressData);
	//		laMFVehicle.setOwnerData(laOwnerData);
	//		// Title Info 
	//		TitleData laTitleData = new TitleData();
	//		laTitleData.setDocNo(aaSubconRenewData.getDocNo());
	//		laMFVehicle.setTitleData(laTitleData);
	//
	//		// Reg Data 
	//		RegistrationData laRegData = new RegistrationData();
	//		laRegData.setRegExpMo(aaSubconRenewData.getRegExpMo());
	//		laRegData.setRegEffDt(aaSubconRenewData.getSubconIssueDate());
	//		laRegData.setRegPltCd(
	//			aaSubconRenewData.getPltItmCd() == null
	//				? ""
	//				: aaSubconRenewData.getPltItmCd().trim());
	//		laRegData.setRegStkrCd(
	//			aaSubconRenewData.getStkrItmCd() == null
	//				? ""
	//				: aaSubconRenewData.getStkrItmCd().trim());
	//
	//		// defect 9085
	//		// Set RegPltCd, OrgNo in RegData
	//		if (aaSubconRenewData.getSpclPltIndi() == 1)
	//		{
	//			laRegData.setRegPltCd(
	//				aaSubconRenewData.getSpclPltRegPltCd());
	//		}
	//		// end defect 9085 
	//
	//		// defect 10392 
	//		if (PlateTypeCache.isSpclPlate(laRegData.getRegPltCd()))
	//		  {
	//		   SpecialPlatesRegisData laSpclPltRegisData =
	//			new SpecialPlatesRegisData();
	//		   laSpclPltRegisData.setPltValidityTerm(
	//		   		aaSubconRenewData.getPltVldtyTerm());
	//		   laSpclPltRegisData.setRegPltNo(
	//		   		aaSubconRenewData.getNewPltNo());
	//		   laSpclPltRegisData.setRegPltCd(
	//		   		laRegData.getRegPltCd()); 
	//		   laSpclPltRegisData.setPltExpMo(
	//		   		aaSubconRenewData.getPltNextExpMo());
	//		   laSpclPltRegisData.setPltExpYr(
	//		   		aaSubconRenewData.getPltNextExpYr());
	//		   laMFVehicle.setSpclPltRegisData(laSpclPltRegisData); 
	//		  }
	//		// end defect 10392 
	//
	//
	//		laRegData.setRegClassCd(
	//			Integer.parseInt(aaSubconRenewData.getRegClassCd()));
	//		// Inventory 
	//		// Sticker Issued
	//		int liRecordType = aaSubconRenewData.getRecordType();
	//		if (liRecordType == SubcontractorRenewalData.STKR
	//			|| liRecordType == SubcontractorRenewalData.PLT_STKR)
	//		{
	//			laProcInvData = new ProcessInventoryData();
	//			laProcInvData.setItmCd(aaSubconRenewData.getStkrItmCd());
	//			//set expiration year
	//			laProcInvData.setInvItmYr(aaSubconRenewData.getNewExpYr());
	//			lvInventory.add(laProcInvData);
	//		}
	//		// Plate Issued
	//		// defect 7456
	//		if (liRecordType == SubcontractorRenewalData.PLT_STKR
	//			|| liRecordType == SubcontractorRenewalData.PLT)
	//		{
	//			laProcInvData = new ProcessInventoryData();
	//			laProcInvData.setInvItmNo(aaSubconRenewData.getNewPltNo());
	//			laRegData.setRegPltNo(aaSubconRenewData.getNewPltNo());
	//			laRegData.setPrevPltNo(aaSubconRenewData.getRegPltNo());
	//			laProcInvData.setItmCd(aaSubconRenewData.getPltItmCd());
	//			if (liRecordType == SubcontractorRenewalData.PLT)
	//			{
	//				laProcInvData.setInvItmYr(
	//					aaSubconRenewData.getNewExpYr());
	//			}
	//			else
	//			{
	//				laProcInvData.setInvItmYr(0);
	//			}
	//			lvInventory.add(laProcInvData);
	//		}
	//		else
	//		{
	//			laRegData.setRegPltNo(aaSubconRenewData.getRegPltNo());
	//			laRegData.setPrevPltNo(aaSubconRenewData.getRegPltNo());
	//		}
	//		laCTD.setAllocInvItms(lvInventory);
	//		// end defect 7456 
	//		laMFVehicle.setRegData(laRegData);
	//		// Vehicle Info
	//		VehicleData laVehData = new VehicleData();
	//		laVehData.setVin(aaSubconRenewData.getVIN());
	//		laMFVehicle.setVehicleData(laVehData);
	//		laCTD.setVehicleInfo(laMFVehicle);
	//		// TransId 
	//		laCTD.setTransId(aaSubconRenewData.getTransID());
	//		// Fees
	//		RegFeesData laRegFeesData = new RegFeesData();
	//		laRegFeesData.setVectFees(
	//			aaSubconRenewData.getFeesDataTrFunds());
	//		laCTD.setRegFeesData(laRegFeesData);
	//		laCTD.setExpMo(aaSubconRenewData.getRegExpMo());
	//		laCTD.setExpYr(aaSubconRenewData.getNewExpYr());
	//		laCTD.setOfcIssuanceNo(SystemProperty.getOfficeIssuanceNo());
	//		return laCTD;
	//	}

	/**
	 * Create delete key and release inventory list for modify
	 * 
	 * @param aaSRCD SubcontractorRenewalCacheData
	 * @throws RTSException
	 */
	private void createDeleteKeyAndReleaseInvList(SubcontractorRenewalCacheData aaSRCD)
		throws RTSException
	{
		TransactionHeaderData laTransHdr =
			aaSRCD.getTransactionHeaderData();
		SubcontractorRenewalData laSubconRenewData =
			aaSRCD.getRecordTobeModified();
		SubcontractorRenewalData laNewSubconRenewData =
			aaSRCD.getTempSubconRenewalData();

		// Create delete key
		Vector lvDeleteKeyList = new Vector();

		// Trans key
		TransactionKey laTransKey = new TransactionKey();
		laTransKey.setOfcIssuanceNo(laTransHdr.getOfcIssuanceNo());
		laTransKey.setSubstaId(laTransHdr.getSubstaId());
		laTransKey.setCustSeqNo(laTransHdr.getCustSeqNo());
		laTransKey.setTransWsId(laTransHdr.getTransWsId());
		laTransKey.setTransAMDate(laTransHdr.getTransAMDate());
		laTransKey.setTransTime(laSubconRenewData.getTransTime());
		lvDeleteKeyList.add(laTransKey);
		aaSRCD.setDeleteTransKeyList(lvDeleteKeyList);

		// Build release inventory vector
		// Inventory not null, Found in Inv_Allocation, 
		// Not printable, & (ItmCd || ItmNo modified)
		if (laSubconRenewData.getProcInvPlt() != null
			&& !laSubconRenewData.getProcInvPlt().getInvLocIdCd().equals(
				InventoryConstant.NOT_FOUND)
			&& !StickerPrintingUtilities.isStickerPrintable(
				laSubconRenewData.getProcInvPlt())
			&& !(laSubconRenewData
				.getProcInvPlt()
				.getItmCd()
				.equals(laNewSubconRenewData.getPltItmCd())
				&& laSubconRenewData.getProcInvPlt().getInvItmNo().equals(
					laNewSubconRenewData.getNewPltNo())))
		{
			Vector lvReleaseIssuedInvList = new Vector();
			lvReleaseIssuedInvList.add(
				laSubconRenewData.getProcInvPlt());
			aaSRCD.setReleaseInventoryList(lvReleaseIssuedInvList);
		}
		else
		{
			aaSRCD.setReleaseInventoryList(null);
		}
	}

	/**
	 * Manage the Subcontractor Renewal report generation.
	 * 
	 * @param aaSRCD SubcontractorRenewalCacheData
	 * @param aiRptId int
	 * @return ReportSearchData 
	 * @throws RTSException
	 */
	private ReportSearchData generateSubconReport(
		SubcontractorRenewalCacheData aaSRCD,
		int aiRptId)
		throws RTSException
	{
		// defect 8628 
		ReportProperties laRptProps = null;
		String lsRptProp = "RTS.POS.2011";

		if (aiRptId == GenSubconRenewalReport.SUBCON_RSPS_REPORT)
		{
			lsRptProp = "RTS.POS.2012";
		}

		laRptProps = new ReportProperties(lsRptProp);
		laRptProps.initClientInfo();

		String lsRptHeader = null;
		String lsFileName = new String();
		int liCopies = ReportConstant.RPT_7_COPIES;

		switch (aiRptId)
		{
			case GenSubconRenewalReport.SUBCON_RSPS_REPORT :
				//status = "PRELIMINARY SUBCONTRACTOR RENEWAL REPORT";
				//Changed per memo from Marshall Hinton dated 10/29/2002
				// defect 10064						
				//lsRptHeader = "SUBCONTRACTOR RENEWAL DISKETTE REPORT";
				lsRptHeader = "SUBCONTRACTOR RSPS INPUT REPORT";
				lsFileName = "PSUBCON";
				// end defect 10064				
				break;
			case GenSubconRenewalReport.DRAFT_SUBCON_REPORT :
				lsRptHeader =
					"SUBCONTRACTOR RENEWAL REPORT - " + "DRAFT";
				lsFileName = "SUBCONRN";
				liCopies = 1;
				break;
			case GenSubconRenewalReport.FINAL_SUBCON_REPORT :
				lsRptHeader =
					"SUBCONTRACTOR RENEWAL REPORT - " + "FINAL";
				lsFileName = "SUBCONR";
				break;
		}
		GenSubconRenewalReport laGSRP =
			new GenSubconRenewalReport(lsRptHeader, laRptProps);

		laGSRP.formatReport(aaSRCD, aiRptId);

		// Use new constructor 
		ReportSearchData laRptSearchData =
			new ReportSearchData(
				laGSRP.getFormattedReport().toString(),
				lsFileName,
				lsRptHeader,
				liCopies,
				ReportConstant.PORTRAIT);

		if (aiRptId == GenSubconRenewalReport.DRAFT_SUBCON_REPORT)
		{
			// defect 10011
			//laRptSearchData.setNextScreen("SUBCON-DRAFT");
			laRptSearchData.setNextScreen(
				ReportConstant.RPR000_NEXT_SCREEN_PREVIOUS);
			// end defect 10011
		}
		// end defect 8628  

		laRptSearchData.setData(aaSRCD);
		return laRptSearchData;
	}

	/**
	 * Returns a Map of the internal attributes.  Implementers of this 
	 * method should use introspection to display their internal 
	 * variables and values
	 * 
	 * @return java.util.Map
	 */
	public java.util.Map getAttributes()
	{
		java.util.HashMap lhmHash = new java.util.HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException aeIAEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	/**
	 * Handle Inventory while database down.
	 * 
	 * @param aaSubconRenewData SubcontractorRenewalData
	 * @param aiTransTime int
	 * @return ProcessInventoryData 
	 * 
	 */
	private ProcessInventoryData getProcessInventoryDataDBDown(
		SubcontractorRenewalData aaSubconRenewData,
		int aiTransTime)
	{
		ProcessInventoryData laProcessInventoryData =
			new ProcessInventoryData();
		laProcessInventoryData.setInvId("0");
		laProcessInventoryData.setInvQty(1);
		laProcessInventoryData.setInvLocIdCd(InventoryConstant.DB_DOWN);
		laProcessInventoryData.setInvItmNo(
			aaSubconRenewData.getNewPltNo());
		laProcessInventoryData.setInvItmEndNo(
			aaSubconRenewData.getNewPltNo());
		laProcessInventoryData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laProcessInventoryData.setSubstaId(
			SystemProperty.getSubStationId());
		laProcessInventoryData.setItmCd(
			aaSubconRenewData.getPltItmCd());
		if (aaSubconRenewData.getRecordType()
			== SubcontractorRenewalData.PLT)
		{
			laProcessInventoryData.setInvItmYr(
				new RTSDate().getYear() + 1);
		}
		else
		{
			laProcessInventoryData.setInvItmYr(0);
		}
		laProcessInventoryData.setTransAMDate(
			new RTSDate().getAMDate());
		laProcessInventoryData.setTransTime(aiTransTime);
		laProcessInventoryData.setTransWsId(
			String.valueOf(SystemProperty.getWorkStationId()));
		return laProcessInventoryData;
	}

	/**
	 * Get the inventory allocated to the subcontractor, used in REG006
	 * 
	 * @param aaData Object
	 * @return Object  
	 * @throws RTSException
	 */
	private Object getSubconInvAllocation(Object aaData)
		throws RTSException
	{
		SubcontractorRenewalCacheData laSRCD =
			(SubcontractorRenewalCacheData) aaData;
		SubcontractorData laSubconData = laSRCD.getSubconInfo();
		//Get allocated inventory
		Vector lvGetAllocationInv = new Vector();
		InventoryAllocationData laInventoryAllocationData =
			new InventoryAllocationData();
		laInventoryAllocationData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laInventoryAllocationData.setSubstaId(
			SystemProperty.getSubStationId());
		// defect 10161 
		laInventoryAllocationData.setInvId(
			String.valueOf(laSubconData.getId()));
		// end defect 10161 
		laInventoryAllocationData.setInvLocIdCd("S");
		lvGetAllocationInv.addElement(laInventoryAllocationData);
		// defect 7493 
		if (com
			.txdot
			.isd
			.rts
			.client
			.desktop
			.RTSApplicationController
			.isDBReady())
		{
			try
			{
				lvGetAllocationInv =
					(Vector) Comm.sendToServer(
						GeneralConstant.REGISTRATION,
						RegistrationConstant.GET_SUBCON_ALLOCATED_INV,
						lvGetAllocationInv);
				if (lvGetAllocationInv.size() == 0)
				{
					lvGetAllocationInv = null;
				}
			}
			catch (RTSException aeRTSEx)
			{
				if (aeRTSEx
					.getMsgType()
					.equals(RTSException.SERVER_DOWN)
					|| aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
				{
					lvGetAllocationInv = new Vector();
				}
				else
				{
					throw aeRTSEx;
				}
			}
		}
		else
		{
			lvGetAllocationInv = new Vector();
		}
		// end defect 7493 
		laSRCD.setSubconAllocatedInventory(lvGetAllocationInv);
		return laSRCD;
	}

	/**
	 * Initialize subcon header
	 * Pre condition: SubcontractorHdr not null, SubcontractorHdr set 
	 * in SubconCache, Subcon issue date set
	 * 
	 * @param aaSRCD SubcontractorRenewalCacheData
	 * @return SubcontractorRenewalCacheData 
	 * @throws RTSException
	 */
	private SubcontractorRenewalCacheData initSubconHdr(SubcontractorRenewalCacheData aaSRCD)
		throws RTSException
	{
		SubcontractorHdrData laSubcontractorHdrData =
			aaSRCD.getSubcontractorHdrData();
		SubcontractorData laSubcontractorData = aaSRCD.getSubconInfo();
		// defect 10161 
		laSubcontractorHdrData.setSubconId(laSubcontractorData.getId());
		// end defect 10161 
		laSubcontractorHdrData.setOfcIssuanceNo(
			laSubcontractorData.getOfcIssuanceNo());
		laSubcontractorHdrData.setTransAMDate(
			new RTSDate().getAMDate());
		laSubcontractorHdrData.setTransEmpId(
			SystemProperty.getCurrentEmpId());
		laSubcontractorHdrData.setTransWsId(
			SystemProperty.getWorkStationId());
		laSubcontractorHdrData.setSubstaId(
			SystemProperty.getSubStationId());
		laSubcontractorHdrData.setPymntTotalFee(new Dollar("0.00"));
		// defect 10161 
		aaSRCD.setDisplaySubconInfo(
			UtilityMethods.addPadding(
				new String[] {
					 String.valueOf(laSubcontractorData.getId())},
				new int[] { 3 },
				"0")
				+ "   "
				+ laSubcontractorData.getName1());
		// end defect 10161 
		return aaSRCD;
	}

	/**
	 * Initialize subcon header
	 * Pre condition: SubcontractorHdr not null, SubcontractorHdr set 
	 * in SubconCache, Subcon issue date set
	 * 
	 * @param aaData Object
	 * @return SubcontractorRenewalCacheData 
	 * @throws RTSException
	 */
	private SubcontractorRenewalCacheData initSubconRenewal(Object aaData)
		throws RTSException
	{
		SubcontractorRenewalCacheData laSRCD =
			(SubcontractorRenewalCacheData) aaData;
		initSubconHdr(laSRCD);
		//clean up inventory vector for performance
		laSRCD.setSubconAllocatedInventory(null);
		if (laSRCD.getSubconDiskData() != null)
		{
			//print preliminary report, if required
			printReport(
				generateSubconReport(
					laSRCD,
					GenSubconRenewalReport.SUBCON_RSPS_REPORT));
			//if disk entry, move disk info to transmap
			moveDiskInfoToTransMap(laSRCD);
			laSRCD.setAllTransPosted(false);
			// defect 7620
			// Add Transaction Header so that saved diskette bundles will
			// show in set-aside. 
			laSRCD = addDiskTransHdr(laSRCD);
			// end defect 7620
		}
		else
		{
			laSRCD.setAllTransPosted(true);
		}
		SubconBundleManager.saveBundle(laSRCD);
		return laSRCD;
	}

	/**
	 * Maintains the local file for storing Subcontractor Renewal items.  
	 * This file is used  to retrieve renewal items for later processing.  
	 * It uses SubcontractorRenewalCacheData.
	 * 
	 * @return Object
	 * @throws RTSException 
	 */

	private Object manageSubconCacheList() throws RTSException
	{
		//retrieve Bundle   
		SubcontractorRenewalCacheData laSRCD =
			SubconBundleManager.getBundle();
		//re-build Transaction state
		Transaction.setTransactionHeaderData(
			laSRCD.getTransactionHeaderData());
		Transaction.setRcptDirForCust(laSRCD.getRcptDir());
		return laSRCD;
	}

	/**
	 * Move the disk info to the trans map
	 * Populate reprint sticker info
	 * 
	 * @param aaBundleSRCD SubcontractorRenewalCacheData
	 * @throws RTSException
	 */
	private void moveDiskInfoToTransMap(SubcontractorRenewalCacheData aaBundleSRCD)
		throws RTSException
	{
		Vector lvSubconDiskInfo = aaBundleSRCD.getSubconDiskData();
		// defect 7597 
		// Separate tables for Print vs. Reprint 
		Hashtable lhtPrintStkrInfo = new Hashtable();
		Hashtable lhtReprintStkrInfo = new Hashtable();
		Hashtable lhtVoidedTransactions = new Hashtable();
		// end defect 7597 
		HashMap lhmReprntStkrCodeInfo = new HashMap();
		HashMap lhmTransKeys = new HashMap();
		HashMap lhmDiskSeqNos = new HashMap();
		HashMap lhmRSPSIds = new HashMap();
		HashMap lhmVINs = new HashMap();
		HashMap lhmRegPltNos = new HashMap();
		// defect 7629
		HashMap lhmNewPltNos = new HashMap();
		// end defect 7629 
		for (int i = 0; i < lvSubconDiskInfo.size(); i++)
		{
			SubcontractorRenewalData laSubconRenewData =
				(SubcontractorRenewalData) lvSubconDiskInfo.get(i);
			// defect 7537
			laSubconRenewData.setSubconIssueDate(
				aaBundleSRCD
					.getSubcontractorHdrData()
					.getSubconIssueDate());
			// end defect 7537
			// Do not move record where invalid data found 
			if (!laSubconRenewData.isInvalidRecord())
			{
				// 
				// If PROCESS (Print)
				//
				//   Assign TransKeyNumber
				//   Add to HashMap SubconTransData
				//   Add to Running Total
				//   Add to HashMaps of Key with HashSet of TransKeys
				//      Current Plate
				//      New Plate
				//      Doc No
				//      VIN 
				//   Add to HashMaps with reverse keys
				//      TransKeys,ScannerIds,DiskSeqNo,VIN
				if (laSubconRenewData.getTransType()
					== SubcontractorRenewalData.PROCESS)
				{
					aaBundleSRCD.incrementTransIndex();
					//set the reverse index
					laSubconRenewData.setTransKeyNumber(
						aaBundleSRCD.getCurrTransIndex());
					//add to transaction hashmap
					aaBundleSRCD.getSubconTransData().put(
						laSubconRenewData.getTransKeyNumber(),
						laSubconRenewData);
					//add to the total
					aaBundleSRCD.setRunningTotal(
						aaBundleSRCD.getRunningTotal().add(
							laSubconRenewData.getRenwlTotalFees()));
					//add current plate
					SubcontractorHelper.hashTablePut(
						aaBundleSRCD.getTransCurrPltNo(),
						laSubconRenewData.getRegPltNo(),
						laSubconRenewData.getTransKeyNumber());
					//add new plate
					if (laSubconRenewData.getNewPltNo() != null
						&& !laSubconRenewData.getNewPltNo().trim().equals(
							""))
					{
						SubcontractorHelper.hashTablePut(
							aaBundleSRCD.getTransNewPltNo(),
							laSubconRenewData.getNewPltNo(),
							laSubconRenewData.getTransKeyNumber());
					}
					//add vin
					// defect 7674
					if (laSubconRenewData.getVIN().trim().length() > 0)
					{
						SubcontractorHelper.hashTablePut(
							aaBundleSRCD.getTransVIN(),
							laSubconRenewData.getVIN(),
							laSubconRenewData.getTransKeyNumber());
					}
					//add doc number
					SubcontractorHelper.hashTablePut(
						aaBundleSRCD.getTransDocNo(),
						laSubconRenewData.getDocNo(),
						laSubconRenewData.getTransKeyNumber());
					//add the reverse lookup to process voided 
					// transactions later.
					lhmTransKeys.put(
						laSubconRenewData.getDocNo(),
						laSubconRenewData.getTransKeyNumber());
					// add processor id and disk seq number
					// Shouldn't these be the same ProcsId & diskSeqNo ?  
					// (KPH) 
					lhmRSPSIds.put(
						laSubconRenewData.getDocNo(),
						laSubconRenewData.getRSPSId());
					lhmDiskSeqNos.put(
						laSubconRenewData.getDocNo(),
						new Integer(laSubconRenewData.getDiskSeqNo()));
					// add the vin
					// defect 7674
					// VIN no longer required
					if (laSubconRenewData.getVIN() != null
						&& !laSubconRenewData.getVIN().trim().equals(""))
					{
						lhmVINs.put(
							laSubconRenewData.getDocNo(),
							laSubconRenewData.getVIN());
					}
					// end defect 7674
					// defect 7629 
					if (laSubconRenewData.getNewPltNo() != null
						&& !laSubconRenewData.getNewPltNo().trim().equals(
							""))
					{
						//add the NewPltNo
						lhmNewPltNos.put(
							laSubconRenewData.getDocNo(),
							laSubconRenewData.getNewPltNo());
					}
					// end defect 7629 
					//add the regPltNo
					lhmRegPltNos.put(
						laSubconRenewData.getDocNo(),
						laSubconRenewData.getRegPltNo());
					//Check to see if the inventory being issued is printable. if yes then count
					//the print.
					String lsItmCd = laSubconRenewData.getStkrItmCd();
					if (lsItmCd == null)
					{
						lsItmCd = laSubconRenewData.getPltItmCd();
					}
					if (lsItmCd != null)
					{
						if (StickerPrintingUtilities
							.isStickerPrintable(lsItmCd))
						{
							// ReprtStkrInfo keeps track of DocNo & reprint counts
							// Either inserts or increments
							// Modified to lhPrintStkrInfo for Print Only
							SubcontractorHelper.counterhashTablePut(
								lhtPrintStkrInfo,
								laSubconRenewData.getDocNo());
							// lhmReprntStkrCodeInfo load 
							lhmReprntStkrCodeInfo.put(
								laSubconRenewData.getDocNo(),
								laSubconRenewData.getNewExpYr()
									+ lsItmCd);
						}
					}
				}
				// If REPRINT
				else if (
					laSubconRenewData.getTransType()
						== SubcontractorRenewalData.REPRINT)
				{
					// First verify if reprintable 
					String lsItmCd = laSubconRenewData.getStkrItmCd();
					if (lsItmCd == null)
					{
						lsItmCd = laSubconRenewData.getPltItmCd();
					}
					if (lsItmCd != null)
					{
						if (StickerPrintingUtilities
							.isStickerPrintable(lsItmCd))
						{
							// Modified to lhReprintStkrInfo for 
							// Reprints Only 
							SubcontractorHelper.counterhashTablePut(
								lhtReprintStkrInfo,
								laSubconRenewData.getDocNo());
							lhmReprntStkrCodeInfo.put(
								laSubconRenewData.getDocNo(),
								laSubconRenewData.getNewExpYr()
									+ lsItmCd);
						}
					}
				}
				// If VOID
				else if (
					laSubconRenewData.getTransType()
						== SubcontractorRenewalData.VOID)
				{
					Integer lTranskeyNumber =
						(Integer) lhmTransKeys.get(
							laSubconRenewData.getDocNo());
					if (lTranskeyNumber != null)
					{
						String lsItmCd =
							laSubconRenewData.getStkrItmCd();
						if (lsItmCd == null)
						{
							lsItmCd = laSubconRenewData.getPltItmCd();
						}
						if (lsItmCd != null)
						{
							if (StickerPrintingUtilities
								.isStickerPrintable(lsItmCd))
							{
								// Now keep count of voids per Doc No 	
								//voidedTransactions.put(
								// lSubconRenewData.getDocNo(),
								//  new Boolean(true));
								SubcontractorHelper
									.counterhashTablePut(
									lhtVoidedTransactions,
									laSubconRenewData.getDocNo());
								// What is this about? 
								lhmReprntStkrCodeInfo.put(
									laSubconRenewData.getDocNo(),
									laSubconRenewData.getNewExpYr()
										+ lsItmCd);
							}
							//remove from transaction hashmap
							aaBundleSRCD.getSubconTransData().remove(
								lTranskeyNumber);
							//add to running total.
							aaBundleSRCD.setRunningTotal(
								aaBundleSRCD.getRunningTotal().add(
									laSubconRenewData
										.getRenwlTotalFees()));
							//remove the current plate
							SubcontractorHelper.hashTableRemove(
								aaBundleSRCD.getTransCurrPltNo(),
								laSubconRenewData.getRegPltNo(),
								lTranskeyNumber);
							//remove new plate
							if (laSubconRenewData.getNewPltNo() != null
								&& !laSubconRenewData
									.getNewPltNo()
									.trim()
									.equals(
									""))
							{
								SubcontractorHelper.hashTableRemove(
									aaBundleSRCD.getTransNewPltNo(),
									laSubconRenewData.getNewPltNo(),
									lTranskeyNumber);
							}
							//remove doc number
							SubcontractorHelper.hashTableRemove(
								aaBundleSRCD.getTransDocNo(),
								laSubconRenewData.getDocNo(),
								lTranskeyNumber);
							//remove from vin
							if (laSubconRenewData.getVIN() != null
								&& !laSubconRenewData
									.getVIN()
									.trim()
									.equals(
									""))
							{
								SubcontractorHelper.hashTableRemove(
									aaBundleSRCD.getTransVIN(),
									laSubconRenewData.getVIN(),
									lTranskeyNumber);
							}
						}
					}
				}
			}
		}
		// Has to be printed prior to Void / Reprint 
		if (lhtPrintStkrInfo.size() > 0)
		{
			//generate vector
			//	Hashtable v = new Hashtable();
			RTSDate laToday = new RTSDate();
			Iterator laIterator = lhtPrintStkrInfo.keySet().iterator();
			while (laIterator.hasNext())
			{
				ReprintData laReprintData = new ReprintData();
				String lsKey = (String) laIterator.next();
				// Get Itmcd & Itm Year
				String lsCodeInfo =
					(String) lhmReprntStkrCodeInfo.get(lsKey);
				// Get number of Prints 
				//Integer count = (Integer) reprtStkrInfo.get(key);
				Integer laPrintCount =
					(Integer) lhtPrintStkrInfo.get(lsKey);
				// Get number of Reprints
				Integer laReprintCount = new Integer(0);
				if (lhtReprintStkrInfo.get(lsKey) != null)
				{
					laReprintCount =
						(Integer) lhtReprintStkrInfo.get(lsKey);
				}
				Integer laVoidCount = new Integer(0);
				if (lhtVoidedTransactions.get(lsKey) != null)
				{
					laVoidCount =
						(Integer) lhtVoidedTransactions.get(lsKey);
				}
				// Get associated diskSeqNo
				int liDiskSeqNo =
					((Integer) lhmDiskSeqNos.get(lsKey)).intValue();
				// Get RSPS Id 
				String lsRSPSId = (String) lhmRSPSIds.get(lsKey);
				// Get VIN
				// KPH WHAT HAPPENS HERE? 
				String lsVIN = (String) lhmVINs.get(lsKey);
				// Get RegPltNo
				String lsRegPltNo = (String) lhmRegPltNos.get(lsKey);
				// defect 7629
				// Use New Plate No when available for Insert/Update
				String lsNewPltNo = (String) lhmNewPltNos.get(lsKey);
				if (lsNewPltNo != null
					&& !lsNewPltNo.trim().equals(""))
				{
					lsRegPltNo = lsNewPltNo;
				}
				// end defect 7629 
				laReprintData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				// defect 7636
				// Assign SubstaId
				laReprintData.setSubstaId(
					SystemProperty.getSubStationId());
				// end defect 7636
				laReprintData.setItmYr(
					Integer.parseInt(lsCodeInfo.substring(0, 4)));
				laReprintData.setItmCd(lsCodeInfo.substring(4));
				laReprintData.setOrigin(CommonConstant.RSPS_SUB);
				// defect 10161 
				laReprintData.setOriginId(
					aaBundleSRCD.getSubconInfo().getId());
				// end defect 10161 
				laReprintData.setPrntQty(laPrintCount.intValue());
				laReprintData.setReprntQty(laReprintCount.intValue());
				laReprintData.setVoided(laVoidCount.intValue());
				laReprintData.setRegPltNo(lsRegPltNo);
				laReprintData.setReprntDate(laToday);
				laReprintData.setScannerId(lsRSPSId);
				laReprintData.setDiskNum(liDiskSeqNo);
				laReprintData.setVIN(lsVIN);
				//Update the RSPS_Prnt table as soon as this data is read.
				Transaction.updateReprintSticker(laReprintData);
			}
		}
	}

	/**
	 * Print Report
	 * 
	 * @param aaRptSearchData ReportSearchData
	 * @return Object 
	 * @throws RTSException
	 */
	private Object printReport(ReportSearchData aaRptSearchData)
		throws RTSException
	{
		// defect 8628
		Vector lvRptSearchData =
			UtilityMethods.addPCLandSaveReport(aaRptSearchData);

		if (lvRptSearchData != null && lvRptSearchData.size() != 0)
		{
			ReportSearchData laRptSearchData =
				(ReportSearchData) lvRptSearchData.elementAt(0);

			String lsFileName = laRptSearchData.getKey1();

			if (!UtilityMethods.isEmpty(lsFileName))
			{
				Print laPrint = new Print();
				laPrint.sendToPrinter(
					lsFileName,
					TransCdConstant.SBRNW);
			}
		}
		// end defect 8628  

		return aaRptSearchData;
	}

	/**
	 * Main entry method for SubcontractorRenewal Client Business.
	 *
	 * @param aiModule String
	 * @param aiFunctionId int
	 * @param aaData Object
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
			// defect 7894
			// remove unused arguments
			case RegistrationConstant
				// defect 10064
				// rename method to use one method for
				// both flashdrive and diskette RSPS input
				//.COPY_AND_VALIDATE_SUBCON_DISKETTE :
				//return copySubconDiskette();
				.COPY_AND_VALIDATE_SUBCON_MEDIA :
				return copySubconRSPSInput();
				// end defect 10064

			case RegistrationConstant.GET_SUBCON_ALLOCATED_INV :
				return getSubconInvAllocation(aaData);

			case RegistrationConstant.INIT_SUBCON_RENWL :
				return initSubconRenewal(aaData);

			case RegistrationConstant.PROCESS_SUBCON_RENWL :
				return procsSubconRenwl(aiModule, aiFunctionId, aaData);

			case RegistrationConstant.RELEASE_INV003_ITM :
				return releaseINV003Inventory(aaData);

			case RegistrationConstant.CANCEL_SUBCON :
				return cancelSubcon(aaData);

			case RegistrationConstant.CANCEL_HELD_SUBCON :
				return cancelHeldSubcon(aaData);

			case RegistrationConstant.RESTORE_SUBCON_BUNDLE :
				return manageSubconCacheList();

			case RegistrationConstant
				.DEL_SELECTED_SUBCON_RENWL_RECORD :
				return procsDeleteSubconRenwl(aaData);

				// defect 8628
			case RegistrationConstant.GENERATE_SUBCON_REPORT_DRAFT :
				return UtilityMethods.addPCLandSaveReport(
					generateSubconReport(
						(SubcontractorRenewalCacheData) aaData,
						GenSubconRenewalReport.DRAFT_SUBCON_REPORT));
				// end defect 8628 

			case RegistrationConstant.PROC_COMPLETE_SUBCON_RENWL :
				return procsCompleteSubconRenwl(aaData);

			case RegistrationConstant.CHECK_DISK_INVENTORY :
				return checkDisketteInventory(
					aiModule,
					aiFunctionId,
					aaData);

			case RegistrationConstant.ADD_DISK_TRANS_AND_END_TRANS :
				return addDiskTransAndEndTrans(aaData);

				// end defect 7894 	

		}
		return null;
	}

	/**
	 * Complete processing the Subcontractor Renewal event.
	 * 
	 * @param aaData 
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object procsCompleteSubconRenwl(Object aaData)
		throws RTSException
	{
		SubcontractorRenewalCacheData laSRCD =
			(SubcontractorRenewalCacheData) aaData;
		SubcontractorRenewalCacheData laSRCDForReport =
			(SubcontractorRenewalCacheData) UtilityMethods.copy(aaData);
		Vector lvTransCache = new Vector();
		// Request to release held inventory will be added to the 
		// transaction cache. 
		if (laSRCD.getHeldInvPlt() != null)
		{
			laSRCD.getHeldInvPlt().setInvStatusCd(0);
			RTSDate laRTSDate = new RTSDate();
			laSRCD.setTransTime(laRTSDate.get24HrTime());
			laSRCD.setTransAMDate(laRTSDate.getAMDate());
			TransactionCacheData laTransactionCacheData =
				new TransactionCacheData();
			laTransactionCacheData.setObj(laSRCD.getHeldInvPlt());
			laTransactionCacheData.setProcName(
				TransactionCacheData.UPDATE);
			lvTransCache.addElement(laTransactionCacheData);
		}
		// Insert Transaction Payment for Subcontractor Bundle
		// Payment Type will be "Check"  (2) 
		TransactionPaymentData laTransactionPaymentData =
			new TransactionPaymentData();
		laTransactionPaymentData.setPymntTypeCd(2);

		laTransactionPaymentData.setPymntTypeAmt(
			com
				.txdot
				.isd
				.rts
				.services
				.common
				.Transaction
				.getRunningSubtotal());

		// Create/Build TransactionCacheVector for Transaction processing
		Vector lvTransactionCacheVector = new Vector();

		CompleteTransactionData laCTD = new CompleteTransactionData();
		laCTD.setTransCode(TransCdConstant.SBRNW);
		lvTransactionCacheVector.addElement(laCTD);
		lvTransactionCacheVector.addElement(laTransactionPaymentData);

		CommonClientBusiness laCommonBusiness =
			new CommonClientBusiness();

		// Build Saved Inventory Vector to be  handled in endTrans
		if (laSRCD.getIssuedInventories().size() > 0)
		{
			laCommonBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_INVENTORY,
				new Vector(laSRCD.getIssuedInventories().values()));
		}
		else
		{
			laCommonBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.SAVE_INVENTORY,
				CommonClientBusiness.ERASE_SAVED_INV);
		}
		com.txdot.isd.rts.services.common.Transaction laTransaction =
			new com.txdot.isd.rts.services.common.Transaction("SBRNW");

		saveAndGenerateReceipts(laSRCD, laTransaction);

		laTransaction.endTrans(
			lvTransactionCacheVector,
			com
				.txdot
				.isd
				.rts
				.services
				.common
				.Transaction
				.getTransactionHeaderData(),
			lvTransCache);

		SubconBundleManager.deleteBundle();

		//Produce Subcon Final Report
		// defect 10123
		return UtilityMethods.addPCLandSaveReport(
			generateSubconReport(
				laSRCDForReport,
				GenSubconRenewalReport.FINAL_SUBCON_REPORT));
		// end defect 8628
	}

	/**
	 * Complete processing the Delete Subcontractor Renewal event.
	 * 
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object procsDeleteSubconRenwl(Object aaData)
		throws RTSException
	{
		SubcontractorRenewalCacheData laBundleSRCD =
			(SubcontractorRenewalCacheData) aaData;
		TransactionHeaderData laTransHdr =
			laBundleSRCD.getTransactionHeaderData();
		Transaction laTransaction = new Transaction();
		RTSDate laRTSDate = new RTSDate();
		laBundleSRCD.setTransTime(laRTSDate.get24HrTime());
		laBundleSRCD.setTransAMDate(laRTSDate.getAMDate());
		TransactionCacheData laTransactionCacheData =
			new TransactionCacheData();
		//SubcontractorRenewalCacheData to Server
		SubcontractorRenewalCacheData laToServerSRCDT =
			new SubcontractorRenewalCacheData();
		//get the index
		Set laIndex = laBundleSRCD.getDeleteIndex();
		Iterator laIterator = laIndex.iterator();
		Vector lvSubconList =
			new Vector(laBundleSRCD.getSubconTransData().values());
		Vector lvDeleteKeyList = new Vector();
		Vector lvReleaseIssuedInvList = new Vector();
		while (laIterator.hasNext())
		{
			SubcontractorRenewalData laSubconRenewData =
				(SubcontractorRenewalData) lvSubconList.get(
					((Integer) laIterator.next()).intValue());
			// Release Inventory if Posted
			if (laSubconRenewData.isPosted())
			{
				//trans key
				TransactionKey laTransKey = new TransactionKey();
				laTransKey.setOfcIssuanceNo(
					laTransHdr.getOfcIssuanceNo());
				laTransKey.setSubstaId(laTransHdr.getSubstaId());
				laTransKey.setCustSeqNo(laTransHdr.getCustSeqNo());
				laTransKey.setTransWsId(laTransHdr.getTransWsId());
				laTransKey.setTransAMDate(laTransHdr.getTransAMDate());
				laTransKey.setTransTime(
					laSubconRenewData.getTransTime());
				lvDeleteKeyList.add(laTransKey);
				//build release inventory vector
				if (laSubconRenewData.getProcInvPlt() != null)
				{
					lvReleaseIssuedInvList.add(
						laSubconRenewData.getProcInvPlt());
				}
			}
			else
			{
				//need to release inventory if enter is pressed on diskette entry mode
				//so the inventory found is on hold
				if (laSubconRenewData.isDiskEntry()
					&& laBundleSRCD.getDiskHeldPltList().get(
						laSubconRenewData.getTransKeyNumber())
						!= null)
				{
					ProcessInventoryData laHeldProcInvData =
						(ProcessInventoryData) laBundleSRCD
							.getDiskHeldPltList()
							.get(
							laSubconRenewData.getTransKeyNumber());
					laHeldProcInvData.setInvStatusCd(0);
					lvReleaseIssuedInvList.add(laHeldProcInvData);
				}
			}
		}
		//no need to go to server if data is all from unprocessed diskette information
		if (!(lvDeleteKeyList.size() == 0
			&& lvReleaseIssuedInvList.size() == 0))
		{
			laToServerSRCDT.setDeleteTransKeyList(lvDeleteKeyList);
			laToServerSRCDT.setReleaseInventoryList(
				lvReleaseIssuedInvList);
			RTSDate laNow = new RTSDate();
			laToServerSRCDT.setTransAMDate(laNow.getAMDate());
			laToServerSRCDT.setTransTime(laNow.get24HrTime());
			//set object and write to cache for server down mode
			laTransactionCacheData.setObj(laToServerSRCDT);
			laTransactionCacheData.setProcName(
				TransactionCacheData.UPDATE);
			Vector lvTransCache = new Vector();
			lvTransCache.addElement(laTransactionCacheData);
			laTransaction.processCacheVector(lvTransCache);

		}
		// after succes deletion, remove information from 
		// SubcontractorRenewalCacheData
		laIterator = laIndex.iterator();
		while (laIterator.hasNext())
		{
			SubcontractorRenewalData laSubconRenewData =
				(SubcontractorRenewalData) lvSubconList.get(
					((Integer) laIterator.next()).intValue());

			//remove from issued inventory vector
			if (laSubconRenewData.getProcInvPlt() != null)
			{
				// defect 8230 
				//laBundleSRCD.getIssuedInventories().remove(
				//	laSubconRenewData.getProcInvPlt());
				laBundleSRCD.getIssuedInventories().remove(
					laSubconRenewData.getTransKeyNumber());
				// end defect 8230 	

			}
			//remove from print vector
			if (laSubconRenewData.isPrint())
			{
				// defect 5821
				// Key for the remove should be the transkeynumber and
				// not the object itself
				laBundleSRCD.getPrintedInventories().remove(
					laSubconRenewData.getTransKeyNumber());
				//end defect 5821
			}
			//subcontract the total
			laBundleSRCD.setRunningTotal(
				laBundleSRCD.getRunningTotal().subtract(
					laSubconRenewData.getRenwlTotalFees()));

			//remove from sorted map
			laBundleSRCD.getSubconTransData().remove(
				laSubconRenewData.getTransKeyNumber());

			//remove from current plate
			SubcontractorHelper.hashTableRemove(
				laBundleSRCD.getTransCurrPltNo(),
				laSubconRenewData.getRegPltNo(),
				laSubconRenewData.getTransKeyNumber());

			//remove from new plate
			if (laSubconRenewData.getNewPltNo() != null)
			{
				SubcontractorHelper.hashTableRemove(
					laBundleSRCD.getTransNewPltNo(),
					laSubconRenewData.getNewPltNo(),
					laSubconRenewData.getTransKeyNumber());
			}
			//remove from doc number
			SubcontractorHelper.hashTableRemove(
				laBundleSRCD.getTransDocNo(),
				laSubconRenewData.getDocNo(),
				laSubconRenewData.getTransKeyNumber());

			//remove from vin
			SubcontractorHelper.hashTableRemove(
				laBundleSRCD.getTransVIN(),
				laSubconRenewData.getVIN(),
				laSubconRenewData.getTransKeyNumber());
		}
		//save subcon bundle
		SubconBundleManager.saveBundle(laBundleSRCD);
		return laBundleSRCD;
	}

	/**
	 * Process Subcontractor renewal
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int 
	 * @param aaData Object  
	 * @return SubcontractorRenewalCacheData
	 * @throws RTSException 
	 */
	private SubcontractorRenewalCacheData procsSubconRenwl(
		int aiModuleName,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		SubcontractorRenewalCacheData laBundleSRCD =
			(SubcontractorRenewalCacheData) aaData;

		// Create a copy of SubconCache w/o the cumulative info
		// (performance improvement) to send to server.  
		SubcontractorRenewalCacheData laToServerSRCD =
			setUpTrans(laBundleSRCD);

		// Modifying existing transaction
		// Delete transaction && release inventory 
		if (laToServerSRCD.getRecordTobeModified() != null
			&& laToServerSRCD.getRecordTobeModified().isPosted())
		{
			createDeleteKeyAndReleaseInvList(laToServerSRCD);
		}

		// Recalculate Fees; Unnecessary if "Item Number Not Found" 
		if (laBundleSRCD.getINV003ProcessInventoryData() == null)
		{
			recalFees(laToServerSRCD);
			if (laToServerSRCD.getException() != null)
			{
				saveIntermediateInfo(laBundleSRCD, laToServerSRCD);
				return laBundleSRCD;
			}
		}
		// If Plate issued and not printable
		// Send To Server for plate validation && get next inventory 
		// item number
		boolean lbValidatePlate =
			(laToServerSRCD.getTempSubconRenewalData().getPltItmCd()
				!= null
				&& !(StickerPrintingUtilities
					.isStickerPrintable(
						laToServerSRCD
							.getTempSubconRenewalData()
							.getPltItmCd())));
		if (lbValidatePlate)
		{
			try
			{
				laToServerSRCD =
					(SubcontractorRenewalCacheData) Comm.sendToServer(
						aiModuleName,
						aiFunctionId,
						laToServerSRCD);
				lbValidatePlate = false;
			}
			catch (RTSException aeRTSEx)
			{
				if (!aeRTSEx
					.getMsgType()
					.equals(RTSException.SERVER_DOWN)
					&& !aeRTSEx.getMsgType().equals(RTSException.DB_DOWN))
				{
					throw aeRTSEx;
				}
			}
		}
		// INV003ProcessInventoryData() will be set if the Subcontractor
		// does not own the inventory.  If this is the case, INV003 will
		// be prompted.  We will add trans when INV003 is resolved. 
		if (laToServerSRCD.getINV003ProcessInventoryData() != null)
		{
			saveIntermediateInfo(laBundleSRCD, laToServerSRCD);
			return laBundleSRCD;
		}
		else
		{
			// Post to Server if all transactions are posted
			// Initialized to false w/ diskette, else true
			if (laBundleSRCD.isAllTransPosted())
			{
				// defect 7974
				// Save prior transtime for scanned/keyed
				laToServerSRCD.setTransTime(
					laBundleSRCD.getTransTime());
				procsSubconRenwlTrans(
					laToServerSRCD,
					laBundleSRCD,
					lbValidatePlate);
				// end defect 7974 
			}
			// Transaction completed, put the temp obj into the sorted 
			// Trans Map, and other objs in the hashset
			saveTransactionInfo(
				laBundleSRCD,
				laToServerSRCD,
				true,
				true);
		}
		return laBundleSRCD;
	}

	/**
	 * Process Subcon renewal on client side 
	 *  
	 * @param aaToServerSRCD SubcontractorRenewalCacheData
	 * @param aaBundleSRCD SubcontractorRenewalCacheData
	 * @param abValidatePlate boolean 
	 * @throws RTSException 
	 */
	private void procsSubconRenwlTrans(
		SubcontractorRenewalCacheData aaToServerSRCD,
		SubcontractorRenewalCacheData aaBundleSRCD,
		boolean abValidatePlate)
		throws RTSException
	{
		// Prevent duplicate key 
		Transaction laTransaction =
			new Transaction(TransCdConstant.SBRNW);
		if (laTransaction.getTransTime()
			== aaToServerSRCD.getTransTime())
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException aeIEx)
			{
				// we will not take any action on this exception
			}
			laTransaction = new Transaction(TransCdConstant.SBRNW);
		}
		SubcontractorRenewalData laSubconRenewData =
			aaToServerSRCD.getTempSubconRenewalData();

		// abValidatePlate if and only iff ServerDown in prior call
		// Inventory will be validated w/ SendCache
		if (abValidatePlate)
		{
			laSubconRenewData.setProcInvPlt(
				getProcessInventoryDataDBDown(
					laSubconRenewData,
					laTransaction.getTransTime()));
		}
		CompleteTransactionData laCTD = new CompleteTransactionData();
		laCTD.setTransCode(TransCdConstant.SBRNW);
		laCTD.setOfcIssuanceNo(SystemProperty.getOfficeIssuanceNo());
		laCTD.setSubconId(SystemProperty.getSubStationId());

		// defect 10392 
		// Calculate number of plate months sold
		int liPltSoldMos = 0;
		if (laSubconRenewData.getSpclPltIndi() == 1)
		{
			if (laSubconRenewData.getPltExpMo()
				+ laSubconRenewData.getPltExpYr() * 12
				> laSubconRenewData.getRegExpMo()
					+ laSubconRenewData.getNewExpYr() * 12)
			{
				liPltSoldMos = 0;
			}
			else if (
				laSubconRenewData.getPltExpMo()
					== laSubconRenewData.getRegExpMo()
					&& laSubconRenewData.getPltExpYr()
						== laSubconRenewData.getNewExpYr() - 1)
			{
				liPltSoldMos = laSubconRenewData.getPltVldtyTerm() * 12;
			}
			else
			{
				liPltSoldMos =
					(laSubconRenewData.getRegExpMo()
						+ laSubconRenewData.getNewExpYr() * 12)
						- (laSubconRenewData.getPltExpMo()
							+ laSubconRenewData.getPltExpYr() * 12);
			}
		}
		laCTD.setSpclPlateNoMoCharge(liPltSoldMos);
		// end defect 10392 

		laCTD.setSubcontractorRenewalCacheData(aaToServerSRCD);
		if (laSubconRenewData.getSpclPltIndi() == 1)
		{
			System.out.println(laSubconRenewData.getSpclPltRegPltCd());
			System.out.println(laSubconRenewData.getOrgNo());
		}
		Vector lvAddlInfo = null;

		// if modify, add the delete transkey info to 
		// SubcontractorRenewalCacheData

		if (aaToServerSRCD.getRecordTobeModified() != null)
		{
			Vector lvDeleteKeyList = new Vector();
			Vector lvReleaseIssuedInvList = new Vector();
			SubcontractorRenewalData laDataToBeModified =
				aaToServerSRCD.getRecordTobeModified();

			// Trans key
			TransactionKey laTransKey = new TransactionKey();
			laTransKey.setOfcIssuanceNo(
				Transaction
					.getTransactionHeaderData()
					.getOfcIssuanceNo());
			laTransKey.setSubstaId(
				Transaction.getTransactionHeaderData().getSubstaId());
			laTransKey.setCustSeqNo(
				Transaction.getTransactionHeaderData().getCustSeqNo());
			laTransKey.setTransWsId(
				Transaction.getTransactionHeaderData().getTransWsId());
			laTransKey.setTransAMDate(
				Transaction
					.getTransactionHeaderData()
					.getTransAMDate());
			laTransKey.setTransTime(laDataToBeModified.getTransTime());
			laTransaction.setSavedTransTime(
				laDataToBeModified.getTransTime());
			lvDeleteKeyList.add(laTransKey);

			//build release inventory vector
			if (laDataToBeModified.getProcInvPlt() != null)
			{
				lvReleaseIssuedInvList.add(
					laDataToBeModified.getProcInvPlt());
			}
			aaToServerSRCD.setDeleteTransKeyList(lvDeleteKeyList);
			aaToServerSRCD.setReleaseInventoryList(
				lvReleaseIssuedInvList);
			TransactionCacheData laTransCache =
				new TransactionCacheData();
			RTSDate laNowRTSDate = new RTSDate();
			aaToServerSRCD.setTransAMDate(laNowRTSDate.getAMDate());
			aaToServerSRCD.setTransTime(laNowRTSDate.get24HrTime());
			laTransCache.setObj(aaToServerSRCD);
			laTransCache.setProcName(TransactionCacheData.UPDATE);
			lvAddlInfo = new Vector();
			lvAddlInfo.add(laTransCache);
		}

		// defect 8344 
		// last two parameters unneedec 
		//laTransaction.addTrans(laCTD, lvAddlInfo, null, true);
		laTransaction.addTrans(laCTD, lvAddlInfo);
		// end defect 8344 
		if (aaBundleSRCD.getRcptDir() == null)
		{
			aaBundleSRCD.setRcptDir(Transaction.getRcptDirForCust());
		}
		laSubconRenewData.setTransID(laCTD.getTransId());
		aaToServerSRCD.setTransactionHeaderData(
			Transaction.getTransactionHeaderData());
		aaToServerSRCD.getTempSubconRenewalData().setTransTime(
			laTransaction.getTransTime());
		aaToServerSRCD.getTempSubconRenewalData().setPosted(true);

		// Only if prior plate issued in Server Down and 
		// not from Diskette
		if (abValidatePlate)
		{
			ProcessInventoryData laNextProcInvData =
				(ProcessInventoryData) UtilityMethods.copy(
					laSubconRenewData.getProcInvPlt());
			laNextProcInvData.setInvLocIdCd("S");
			laNextProcInvData.setInvId(
				String.valueOf(
					aaToServerSRCD
						.getSubcontractorHdrData()
						.getSubconId()));
			InventoryServerBusiness laInventoryServerBusiness =
				new InventoryServerBusiness();
			InventoryAllocationUIData laInventoryAllocationUIData =
				laNextProcInvData.convertToInvAlloctnUIData(
					laNextProcInvData);
			laInventoryAllocationUIData =
				(
					InventoryAllocationUIData) laInventoryServerBusiness
						.processData(
					GeneralConstant.INVENTORY,
					InventoryConstant.GET_NEXT_INVENTORY_NO,
					laInventoryAllocationUIData);
			laInventoryAllocationUIData.setInvItmNo(
				laInventoryAllocationUIData.getInvItmEndNo());
			aaToServerSRCD.setHeldInvPlt(
				laNextProcInvData.convertFromInvAlloctnData(
					laInventoryAllocationUIData));
		}

	}

	/**
	 * calculate fees
	 * 
	 * @param aaToServerSRCD SubcontractorRenewalCacheData
	 * @throws RTSException
	 */
	private void recalFees(SubcontractorRenewalCacheData aaToServerSRCD)
		throws RTSException
	{
		SubcontractorRenewalData laSubcontractorRenewalData =
			aaToServerSRCD.getTempSubconRenewalData();
		//Break it up to CompleteTransactionData
		CompleteTransactionData laCTD = new CompleteTransactionData();
		laCTD.setTransCode(TransCdConstant.SBRNW);
		RegistrationData laRegistrationData = new RegistrationData();
		laRegistrationData.setRegExpMo(
			laSubcontractorRenewalData.getRegExpMo());
		laRegistrationData.setRegExpYr(
			laSubcontractorRenewalData.getNewExpYr() - 1);
		laRegistrationData.setRegStkrCd(
			laSubcontractorRenewalData.getStkrItmCd());
		laRegistrationData.setRegPltCd(
			laSubcontractorRenewalData.getPltItmCd());
		laRegistrationData.setRegClassCd(
			Integer.parseInt(
				laSubcontractorRenewalData.getRegClassCd()));
		MFVehicleData laMFVehicleData = new MFVehicleData();
		laMFVehicleData.setRegData(laRegistrationData);
		laCTD.setVehicleInfo(laMFVehicleData);
		laCTD.setSubConRenwlTotalFee(
			laSubcontractorRenewalData.getRenwlTotalFees());
		laCTD.setSubConRenwlBarCdIndi(
			laSubcontractorRenewalData.getBarCdIndi());
		laCTD.setOfcIssuanceNo(
			aaToServerSRCD
				.getSubcontractorHdrData()
				.getOfcIssuanceNo());
		// defect 9085
		laCTD.setSubconRenwlData(laSubcontractorRenewalData);
		// end defect 9085 

		//populate Subcon info from bar code
		if (laSubcontractorRenewalData.getBarCdIndi() == 1
			&& laSubcontractorRenewalData.getRenewalBarCodeData() != null)
		{
			RenewalBarCodeData laRenewalBarCodeData =
				laSubcontractorRenewalData.getRenewalBarCodeData();
			RegFeesData laRegFeesData = new RegFeesData();
			Vector lvFeesInfo = new Vector();
			FeesData laFeesData = null;
			if (laRenewalBarCodeData.getAcctItmCd1() != null
				&& !laRenewalBarCodeData.getAcctItmCd1().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd1());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice1());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd2() != null
				&& !laRenewalBarCodeData.getAcctItmCd2().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd2());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice2());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd3() != null
				&& !laRenewalBarCodeData.getAcctItmCd3().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd3());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice3());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd4() != null
				&& !laRenewalBarCodeData.getAcctItmCd4().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd4());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice4());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd5() != null
				&& !laRenewalBarCodeData.getAcctItmCd5().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd5());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice5());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd6() != null
				&& !laRenewalBarCodeData.getAcctItmCd6().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd6());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice6());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd7() != null
				&& !laRenewalBarCodeData.getAcctItmCd7().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd7());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice7());
				lvFeesInfo.addElement(laFeesData);
			}
			// defect 7348
			if (laRenewalBarCodeData.getAcctItmCd8() != null
				&& !laRenewalBarCodeData.getAcctItmCd8().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd8());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice8());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd9() != null
				&& !laRenewalBarCodeData.getAcctItmCd9().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd9());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice9());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd10() != null
				&& !laRenewalBarCodeData.getAcctItmCd10().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd10());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice10());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd11() != null
				&& !laRenewalBarCodeData.getAcctItmCd11().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd11());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice11());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd12() != null
				&& !laRenewalBarCodeData.getAcctItmCd12().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd12());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice12());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd13() != null
				&& !laRenewalBarCodeData.getAcctItmCd13().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd13());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice13());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd14() != null
				&& !laRenewalBarCodeData.getAcctItmCd14().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd14());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice14());
				lvFeesInfo.addElement(laFeesData);
			}
			if (laRenewalBarCodeData.getAcctItmCd15() != null
				&& !laRenewalBarCodeData.getAcctItmCd15().trim().equals(
					""))
			{
				laFeesData = new FeesData();
				laFeesData.setAcctItmCd(
					laRenewalBarCodeData.getAcctItmCd15());
				AccountCodesData laAccountCodesData =
					AccountCodesCache.getAcctCd(
						laFeesData.getAcctItmCd(),
						laSubcontractorRenewalData
							.getSubconIssueDate());
				if (laAccountCodesData != null)
				{
					laFeesData.setDesc(
						laAccountCodesData.getAcctItmCdDesc());
				}
				laFeesData.setItmQty(1);
				laFeesData.setItemPrice(
					laRenewalBarCodeData.getItmPrice15());
				lvFeesInfo.addElement(laFeesData);
			}
			// end defect 7348
			// Initialize
			if (lvFeesInfo.size() > 0)
			{
				laRegFeesData.setVectFees(lvFeesInfo);
				laCTD.setRegFeesData(laRegFeesData);
			}
		}
		CommonClientBusiness laCommonClientBusiness =
			new CommonClientBusiness();
		laCTD =
			(
				CompleteTransactionData) laCommonClientBusiness
					.processData(
				GeneralConstant.COMMON,
				CommonConstant.CAL_FEES,
				laCTD);

		// defect 8900
		// defect 9085
		// FeeCalcCat = 4 allowed  
		int liRegClassCd =
			Integer.parseInt(
				laCTD.getSubconRenwlData().getRegClassCd());

		CommonFeesData laCommonFeesData =
			CommonFeesCache.getCommonFee(
				liRegClassCd,
				new RTSDate().getYYYYMMDDDate());

		boolean lbFeeCalcCat4 = laCommonFeesData.getFeeCalcCat() == 4;

		boolean lbRegFeeGTEqZero =
			laCTD.getCustBaseRegFee() != null
				&& laCTD.getCustBaseRegFee().compareTo(
					CommonConstant.ZERO_DOLLAR)
					>= 0
				&& laCTD.getCustActualRegFee() != null
				&& laCTD.getCustActualRegFee().compareTo(
					CommonConstant.ZERO_DOLLAR)
					>= 0;

		if (!(lbFeeCalcCat4
			&& laSubcontractorRenewalData.getSpclPltIndi() == 1
			&& lbRegFeeGTEqZero)
			&& !laCTD.getSubConRenwlTotalFee().equals(
				CommonConstant.ZERO_DOLLAR)
			&& (laCTD.getCustBaseRegFee() == null
				|| laCTD.getCustBaseRegFee().compareTo(
					CommonConstant.ZERO_DOLLAR)
					< 1
				|| laCTD.getCustActualRegFee() == null
				|| laCTD.getCustActualRegFee().compareTo(
					CommonConstant.ZERO_DOLLAR)
					< 1))
			// end defect 8900
		{
			// defect 8628
			//aaToServerSRCD.setException(new RTSException(706));
			aaToServerSRCD.setException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FEE_BELOW_MIN_REG_FEE));
			// end defect 8628 
			aaToServerSRCD.setExceptionField(
				SubcontractorRenewalCacheData.FD_FEE);
		}
		else
		{
			laSubcontractorRenewalData.setCustBaseRegFees(
				laCTD.getCustBaseRegFee());
			laSubcontractorRenewalData.setFeesDataTrFunds(
				laCTD.getRegFeesData().getVectFees());
		}
		// end defect 9085 
	}

	/**
	 * Release INV003 inventory
	 * 
	 * @param aaData Object 
	 * @return SubcontractorRenewalCacheData 
	 * @throws RTSException
	 */
	private SubcontractorRenewalCacheData releaseINV003Inventory(Object aaData)
		throws RTSException
	{
		System.out.println("releaseINV003Inventory");

		SubcontractorRenewalCacheData laBundleSRCD =
			(SubcontractorRenewalCacheData) aaData;

		if (!laBundleSRCD
			.getINV003ProcessInventoryData()
			.getInvLocIdCd()
			.equals("U"))
		{
			// defect 7611
			// Use Transaction to Release Inventory;
			TransactionCacheData laTransactionCacheData =
				new TransactionCacheData();
			Vector lvTransactionCacheVector = new Vector();
			RTSDate laRTSDate = new RTSDate();
			ProcessInventoryData laProcInvData =
				laBundleSRCD.getINV003ProcessInventoryData();
			laProcInvData.setInvStatusCd(0);
			laProcInvData.setTransAMDate(laRTSDate.getAMDate());
			laProcInvData.setTransTime(laRTSDate.get24HrTime());
			laTransactionCacheData.setObj(laProcInvData);
			laTransactionCacheData.setProcName(
				TransactionCacheData.UPDATE);
			lvTransactionCacheVector.addElement(laTransactionCacheData);
			Transaction laTransaction = new Transaction();
			laTransaction.processCacheVector(lvTransactionCacheVector);
			// end defect 7611 	
		}
		laBundleSRCD.setINV003AllocatedName(null);
		laBundleSRCD.setINV003ProcessInventoryData(null);
		laBundleSRCD.setINV003Voided(false);
		SubconBundleManager.saveBundle(laBundleSRCD);
		return laBundleSRCD;
	}

	/**
	 * Save and Generate Subcon receipts
	 * 
	 * @param aaBundleSRCD SubcontractorRenewalCacheData
	 * @param aaTransaction Transaction
	 * @throws RTSException
	 */
	private void saveAndGenerateReceipts(
		SubcontractorRenewalCacheData aaBundleSRCD,
		Transaction aaTransaction)
		throws RTSException
	{
		try
		{
			Hashtable lhtPrintInvList =
				aaBundleSRCD.getPrintedInventories();
			if (lhtPrintInvList.size() == 0)
			{
				return;
			}
			Iterator laIterator = lhtPrintInvList.keySet().iterator();
			Integer[] larrArray = new Integer[lhtPrintInvList.size()];
			int liIndex = 0;
			while (laIterator.hasNext())
			{
				SubcontractorRenewalData laSubconRenewData =
					(SubcontractorRenewalData) lhtPrintInvList.get(
						laIterator.next());
				larrArray[liIndex] =
					laSubconRenewData.getTransKeyNumber();
				liIndex = liIndex + 1;
			}
			Arrays.sort(larrArray);
			for (int i = 0; i < larrArray.length; i++)
			{
				SubcontractorRenewalData laSubconRenewData =
					(SubcontractorRenewalData) lhtPrintInvList.get(
						larrArray[i]);

				// defect 10745 
				CompleteTransactionData laCTD =
					new CompleteTransactionData();
					
				laCTD.assignDataForSubconReceipt(
					aaBundleSRCD,
					laSubconRenewData,
					TransCdConstant.SBRNW);
				// end defect 10745 

				
				//generate receipt
				Vector lvCTD = new Vector();
				lvCTD.add(laCTD);
				GenRegistrationReceipts laGenRpt =
					new GenRegistrationReceipts();
				laGenRpt.formatReceipt(lvCTD);

				// Added FormFeed to the end of the Receipt string
				String lsRcptContent =
					laGenRpt.getReceipt().toString()
						+ ReportConstant.FF;

				//save receipt
				Vector lvSaveRcptInput = new Vector();
				lvSaveRcptInput.add(laCTD);
				lvSaveRcptInput.add(lsRcptContent);
				lvSaveRcptInput.add(null);
				aaTransaction.saveReceipt(lvSaveRcptInput);
			}
		}
		catch (Exception aeEx)
		{
			// TODO Shouldn't we throw an exception?
			aeEx.printStackTrace();
		}
	}

	/**
	 * Save intermediate info, when certain items are on hold in case of crash
	 *  
	 * @param aaBundleSRCD SubcontractorRenewalCacheData
	 * @param aaToServerSRCD SubcontractorRenewalCacheData
	 * @throws RTSException
	 */
	private void saveIntermediateInfo(
		SubcontractorRenewalCacheData aaBundleSRCD,
		SubcontractorRenewalCacheData aaToServerSRCD)
		throws RTSException
	{
		aaBundleSRCD.setTempSubconRenewalData(
			aaToServerSRCD.getTempSubconRenewalData());
		if (aaToServerSRCD.getINV003ProcessInventoryData() != null)
		{
			aaBundleSRCD.setINV003ProcessInventoryData(
				aaToServerSRCD.getINV003ProcessInventoryData());
			aaBundleSRCD.setINV003AllocatedName(
				aaToServerSRCD.getINV003AllocatedName());
			aaBundleSRCD.setINV003Voided(
				aaToServerSRCD.isINV003Voided());
		}
		aaBundleSRCD.setDiskHeldPltList(
			aaToServerSRCD.getDiskHeldPltList());
		// defect 7650
		// Set HeldInvPlt to that specified in aToServerSRCD 
		aaBundleSRCD.setHeldInvPlt(aaToServerSRCD.getHeldInvPlt());
		// end defect 7650 
		aaBundleSRCD.setException(aaToServerSRCD.getException());
		//save bundle
		SubconBundleManager.saveBundle(aaBundleSRCD);
	}

	//	/**
	//	 * Save report 
	//	 * 
	//	 * @param aaData Object
	//	 * @return Object 
	//	 * @throws RTSException 
	//	 */
	//	private Object saveReports(Object aaData) throws RTSException
	//	{
	//		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
	//		UtilityMethods laUtil = new UtilityMethods();
	//		Print laPrint = new Print();
	//		Vector lvReports = new Vector();
	//		String lsPageProps = laPrint.getDefaultPageProps();
	//		String lsRpt =
	//			lsPageProps
	//				+ System.getProperty("line.separator")
	//				+ laRptSearchData.getKey1();
	//		String lsFileName =
	//			laUtil.saveReport(
	//				lsRpt,
	//				laRptSearchData.getKey2(),
	//				laRptSearchData.getIntKey1());
	//		laRptSearchData.setKey1(lsFileName);
	//		laRptSearchData.setIntKey2(ReportConstant.PORTRAIT);
	//		lvReports.add(laRptSearchData);
	//		return lvReports;
	//	}

	/**
	 * Save subcontractorrenewaldata to hashmap, and all transaction related info
	 * to their respective hashsets
	 *  
	 * @param aaBundleSRCD   SubcontractorRenewalCacheData
	 * @param aaToServerSRCD SubcontractorRenewalCacheData
	 * @param abSaveBundle	 boolean
	 * @param abAddFees		 boolean
	 * @throws RTSException 
	 */
	private void saveTransactionInfo(
		SubcontractorRenewalCacheData aaBundleSRCD,
		SubcontractorRenewalCacheData aaToServerSRCD,
		boolean abSaveBundle,
		boolean abAddFees)
		throws RTSException
	{
		boolean lbModify = false;
		if (aaToServerSRCD.getRecordTobeModified() != null)
		{
			lbModify = true;
		}
		SubcontractorRenewalData laSubconRenewData =
			aaToServerSRCD.getTempSubconRenewalData();
		aaBundleSRCD.setTransTime(laSubconRenewData.getTransTime());
		Hashtable lhtReprintStickerData =
			aaBundleSRCD.getReprintStickerReportDataList();
		boolean lbIssuedNewPlate =
			laSubconRenewData.getNewPltNo() != null
				&& !laSubconRenewData.getNewPltNo().trim().equals("");
		if (lhtReprintStickerData != null)
		{
			ReprintData laReprintData =
				(ReprintData) lhtReprintStickerData.get(
					aaBundleSRCD.getTempSubconRenewalData().getDocNo());
			if (laReprintData != null)
			{
				laReprintData.setTransId(
					aaBundleSRCD
						.getTempSubconRenewalData()
						.getTransID());
				laReprintData.setCustSeqNo(
					aaBundleSRCD
						.getTransactionHeaderData()
						.getCustSeqNo());
				lhtReprintStickerData.put(
					aaBundleSRCD.getTempSubconRenewalData().getDocNo(),
					laReprintData);
			}
		}
		//set the processed flag
		laSubconRenewData.setProcessed(true);
		//copy held inventory info
		aaBundleSRCD.setHeldInvPlt(aaToServerSRCD.getHeldInvPlt());
		//do not need to increment index if modify
		if (lbModify)
		{
			//use the original index
			laSubconRenewData.setTransKeyNumber(
				aaToServerSRCD
					.getRecordTobeModified()
					.getTransKeyNumber());
			SubcontractorRenewalData laOrigSubconRenewData =
				aaToServerSRCD.getRecordTobeModified();
			//remove from issued inventory vector
			if (laOrigSubconRenewData.getProcInvPlt() != null)
			{
				aaBundleSRCD.getIssuedInventories().remove(
					laOrigSubconRenewData.getTransKeyNumber());
			}
			//remove from print vector
			if (laOrigSubconRenewData.isPrint())
			{
				aaBundleSRCD.getPrintedInventories().remove(
					laOrigSubconRenewData.getTransKeyNumber());
			}
			//subcontract the total
			aaBundleSRCD.setRunningTotal(
				aaBundleSRCD.getRunningTotal().subtract(
					laOrigSubconRenewData.getRenwlTotalFees()));
			//remove from current plate
			SubcontractorHelper.hashTableRemove(
				aaBundleSRCD.getTransCurrPltNo(),
				laOrigSubconRenewData.getRegPltNo(),
				laSubconRenewData.getTransKeyNumber());
			//remove from new plate
			if (laOrigSubconRenewData.getNewPltNo() != null)
			{
				SubcontractorHelper.hashTableRemove(
					aaBundleSRCD.getTransNewPltNo(),
					laOrigSubconRenewData.getNewPltNo(),
					laSubconRenewData.getTransKeyNumber());
			}
			//remove from doc number
			SubcontractorHelper.hashTableRemove(
				aaBundleSRCD.getTransDocNo(),
				laOrigSubconRenewData.getDocNo(),
				laSubconRenewData.getTransKeyNumber());
			//remove from vin
			SubcontractorHelper.hashTableRemove(
				aaBundleSRCD.getTransVIN(),
				laOrigSubconRenewData.getVIN(),
				laSubconRenewData.getTransKeyNumber());
		}
		else
		{
			if (laSubconRenewData.getTransKeyNumber() == null)
			{
				aaBundleSRCD.incrementTransIndex();
				//set the reverse index
				laSubconRenewData.setTransKeyNumber(
					aaBundleSRCD.getCurrTransIndex());
			}
		}
		//add to transaction hashmap
		aaBundleSRCD.getSubconTransData().put(
			laSubconRenewData.getTransKeyNumber(),
			laSubconRenewData);
		//add to hashsets
		SubcontractorHelper.hashTablePut(
			aaBundleSRCD.getTransCurrPltNo(),
			laSubconRenewData.getRegPltNo(),
			laSubconRenewData.getTransKeyNumber());
		SubcontractorHelper.hashTablePut(
			aaBundleSRCD.getTransDocNo(),
			laSubconRenewData.getDocNo(),
			laSubconRenewData.getTransKeyNumber());
		if (lbIssuedNewPlate)
		{
			SubcontractorHelper.hashTablePut(
				aaBundleSRCD.getTransNewPltNo(),
				laSubconRenewData.getNewPltNo(),
				laSubconRenewData.getTransKeyNumber());
		}
		// defect 8289
		// do not add VIN if no VIN Vehicle
		if (laSubconRenewData.getVIN() != null
			&& laSubconRenewData.getVIN().trim().length() > 0)
		{
			SubcontractorHelper.hashTablePut(
				aaBundleSRCD.getTransVIN(),
				laSubconRenewData.getVIN(),
				laSubconRenewData.getTransKeyNumber());
		}
		// end defect 8289 

		//add to issued inventory
		ProcessInventoryData laProcInventoryDataPlt =
			new ProcessInventoryData();
		if (lbIssuedNewPlate)
		{
			laProcInventoryDataPlt.setItmCd(
				laSubconRenewData.getPltItmCd());
		}
		ProcessInventoryData laProcInventoryDataStkr =
			new ProcessInventoryData();
		laProcInventoryDataStkr.setItmCd(
			laSubconRenewData.getStkrItmCd());
		if (laSubconRenewData.getProcInvPlt() != null
			&& !StickerPrintingUtilities.isStickerPrintable(
				laProcInventoryDataPlt))
		{
			aaBundleSRCD.getIssuedInventories().put(
				laSubconRenewData.getTransKeyNumber(),
				laSubconRenewData.getProcInvPlt());
		}
		//add to printed inventory list
		if (((lbIssuedNewPlate
			&& StickerPrintingUtilities.isStickerPrintable(
				laProcInventoryDataPlt))
			|| (laSubconRenewData.getStkrItmCd() != null
				&& StickerPrintingUtilities.isStickerPrintable(
					laProcInventoryDataStkr)))
			&& laSubconRenewData.isPrint())
		{
			aaBundleSRCD.getPrintedInventories().put(
				laSubconRenewData.getTransKeyNumber(),
				laSubconRenewData);
		}
		if (aaBundleSRCD.getTransactionHeaderData() == null)
		{
			aaBundleSRCD.setTransactionHeaderData(
				Transaction.getTransactionHeaderData());
		}

		//set INV003 state
		aaBundleSRCD.setINV003AllocatedName(
			aaToServerSRCD.getINV003AllocatedName());
		aaBundleSRCD.setINV003Voided(aaToServerSRCD.isINV003Voided());
		aaBundleSRCD.setINV003ProcessInventoryData(
			aaToServerSRCD.getINV003ProcessInventoryData());
		//add to subtotal
		//if from diskette enter, then do not add subtotal
		if (abAddFees)
		{
			aaBundleSRCD.setRunningTotal(
				aaBundleSRCD.getRunningTotal().add(
					laSubconRenewData.getRenwlTotalFees()));
		}
		//clean up
		if (lbModify)
		{
			aaBundleSRCD.setRecordTobeModified(null);
			aaBundleSRCD.setRecordModifyIndex(null);
			aaBundleSRCD.setModified(true);
		}

		aaBundleSRCD.setDiskHeldPltList(
			aaToServerSRCD.getDiskHeldPltList());

		// Remove the held inventory if the inventory is issued
		if (aaBundleSRCD
			.getDiskHeldPltList()
			.containsKey(laSubconRenewData.getTransKeyNumber()))
		{
			aaBundleSRCD.getDiskHeldPltList().remove(
				laSubconRenewData.getTransKeyNumber());
		}
		//save bundle
		if (abSaveBundle)
		{
			SubconBundleManager.saveBundle(aaBundleSRCD);
		}
	}

	/**
	 * Set up necessary parameters for server processing later
	 * 
	 * @param aBundleSRCD SubcontractorRenewalCacheData
	 * @return SubcontractorRenewalCacheData 
	 * @throws RTSException
	 */
	private SubcontractorRenewalCacheData setUpTrans(SubcontractorRenewalCacheData aBundleSRCD)
		throws RTSException
	{
		SubcontractorRenewalCacheData laToServerSRCD =
			new SubcontractorRenewalCacheData();
		laToServerSRCD.setTempSubconRenewalData(
			aBundleSRCD.getTempSubconRenewalData());
		laToServerSRCD.setSubcontractorHdrData(
			aBundleSRCD.getSubcontractorHdrData());
		laToServerSRCD.setTransactionHeaderData(
			aBundleSRCD.getTransactionHeaderData());
		// Populate the copy of the Subconcache sent to server with 
		// held plate, so it can be released
		laToServerSRCD.setHeldInvPlt(aBundleSRCD.getHeldInvPlt());
		if (aBundleSRCD.getTempSubconRenewalData().getValidatePltIndi()
			== SubcontractorRenewalData.VALIDATE_PLT)
		{
			if (SubcontractorHelper
				.checkIfHeldPltIsIdentical(aBundleSRCD))
			{
				// if identical, no need to validate plate, no need to 
				// release plate
				laToServerSRCD
					.getTempSubconRenewalData()
					.setValidatePltIndi(
					SubcontractorRenewalData.PLT_VALIDATED);
				aBundleSRCD.setHeldInvPlt(null);
			}
		}
		laToServerSRCD.setINV003ProcessInventoryData(
			aBundleSRCD.getINV003ProcessInventoryData());
		//set modify information
		laToServerSRCD.setRecordTobeModified(
			aBundleSRCD.getRecordTobeModified());
		laToServerSRCD.setRecordModifyIndex(
			aBundleSRCD.getRecordModifyIndex());
		laToServerSRCD.setDiskHeldPltList(
			aBundleSRCD.getDiskHeldPltList());
		return laToServerSRCD;
	}
}
