package com.txdot.isd.rts.client.registration.ui;

import java.io.*;

import com.txdot.isd.rts.services.data.SubcontractorRenewalCacheData;
import com.txdot.isd.rts.services.exception.RTSException;

/*
 * 
 * SubconBundleManager
 *
 * (c) Texas Department of Transportation 2001 
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ---------------------------------------------------------------
 * N Ting		04/12/2002	Fix CQU100003507, save issued inventory 
 *							so that in restoring a set aside Subcon 
 *							transaction, inventory can be saved
 * N Ting		04/29/2002	Save copy of subcon to disk
 * N Ting		05/30/2002	Fix CQU100004180, always set listbox to 
 *							false before saving
 * N Ting		06/5/2002	Fix CQU100004224
 * K Salvi		11/19/2002	Added performance enhancements from the 
 *							main branch to the saveBundle() method
 * K Harrell	01/26/2004	5.2.0 Merge.  See PCR 34 comments. 
 *							Formatted.
 * 							Version 5.2.0
 * K Harrell	10/04/2004	Remove unnecessary processing
 *							modify saveBundle()
 *							defect 7586  Ver 5.2.1
 * K Harrell	04/28/2005	Java 1.4 Work
 * 							modify deleteBundle()
 * 							defect 8020 Ver 5.2.3 
 * ---------------------------------------------------------------
 */
/**
 * Manages the saved Bundle for Subcontractor
 * 
 * @version	5.2.3		04/28/2004
 * @author Nancy Ting
 * <br>Creation Date: 	11/27/2001
 */
public class SubconBundleManager
{
	// Constant 
	private final static String SUBCON_CACHE_FILE = "subcon.ser";

	/**
	 * SubconBundleManager constructor.
	 */
	public SubconBundleManager()
	{
		super();
	}
	/**
	 * Check if Subcontractor bundle exists
	 * 
	 * @return boolean
	 */
	public static boolean bundleExists()
	{
		File laFile = new File(SUBCON_CACHE_FILE);
		return laFile.exists();
	}
	/**
	 * Delete bundle for subcontractor
	 * 
	 * @return boolean
	 * @throws RTSException 
	 */
	public static boolean deleteBundle() throws RTSException
	{
		File laFile = new File(SUBCON_CACHE_FILE);
		boolean lbDeleted = false;
		if (laFile.exists())
		{
			lbDeleted = laFile.delete();
			if (!lbDeleted)
			{
				throw new RTSException(
					RTSException.WARNING_MESSAGE,
					"Subcon Bundle exists but cannot be deleted",
					"ERROR");
			}
		}
		return lbDeleted;

	}
	/**
	 * Get the Subcontractor bundle
	 * 
	 * @throws RTSException
	 */
	public static SubcontractorRenewalCacheData getBundle()
		throws RTSException
	{
		SubcontractorRenewalCacheData laSubcontractorRenewalCacheData =
			null;
		try
		{
			File laFile = new File(SUBCON_CACHE_FILE);
			if (laFile.exists())
			{
				FileInputStream laFileInputStream =
					new FileInputStream(SUBCON_CACHE_FILE);
				ObjectInputStream laObjectInputStream =
					new ObjectInputStream(laFileInputStream);
				laSubcontractorRenewalCacheData =
					(SubcontractorRenewalCacheData) laObjectInputStream
						.readObject();
				laObjectInputStream.close();
			}
		}
		catch (IOException aeIOEx)
		{
			RTSException rtsException =
				new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			throw rtsException;
		}
		catch (ClassNotFoundException aeClassNFEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeClassNFEx);
			throw leRTSEx;
		}
		return laSubcontractorRenewalCacheData;
	}
	/**
	 * Save the subcontractor bundle, called whenever a new transaction 
	 * is added or deleted.
	 * 
	 * @param aaSubcontractorRenewalCacheData SubcontractorRenewalCacheData
	 * @throws RTSException 
	 */
	public static void saveBundle(SubcontractorRenewalCacheData aaSubcontractorRenewalCacheData)
		throws RTSException
	{
		try
		{
			// defect 7586 
			//Always save the latest inventory in memory
			//			CommonClientBusiness lCommonClientBusiness =
			//				new CommonClientBusiness();
			//			lCommonClientBusiness.processData(
			//				GeneralConstant.COMMON,
			//				CommonConstant.SAVE_INVENTORY,
			//				CommonClientBusiness.ERASE_SAVED_INV);
			//			lCommonClientBusiness.processData(
			//				GeneralConstant.COMMON,
			//				CommonConstant.SAVE_INVENTORY,
			//				aSubcontractorRenewalCacheData.getSubconIssuedInv());
			//			//Set Transaction amount
			//			com.txdot.isd.rts.services.util.Dollar d =
			//				new com.txdot.isd.rts.services.util.Dollar("0.00");
			//			Vector lvSubcon =
			//				aSubcontractorRenewalCacheData.getSubconRenewalData();
			//			if (lvSubcon != null)
			//			{
			//				for (int i = 0; i < lvSubcon.size(); i++)
			//				{
			//					Object currItm = lvSubcon.get(i);
			//					if (currItm instanceof SubcontractorRenewalData)
			//					{
			//						d =
			//							d.add(
			//								((SubcontractorRenewalData) lvSubcon
			//									.get(i))
			//									.getRenwlTotalFees());
			//					}
			//				}
			//				Transaction.setRunningSubtotal(d);
			//			}
			//Begin - Performance enhancements
			//reset VC
			//			SubcontractorRenewalCacheData lSaveCopy =
			//			    (SubcontractorRenewalCacheData) UtilityMethods.copy(aSubcontractorRenewalCacheData);
			//			lSaveCopy.setNextVC(0);
			//			lSaveCopy.setCompleteOneTrans(false);
			//			lSaveCopy.setListBox(false);
			//			lSaveCopy.setException(null);
			//			lSaveCopy.setExceptionField(0);
			//			FileOutputStream lFileOutputStream = new FileOutputStream(fileName);
			//			ObjectOutputStream lObjectOutputStream =
			//			    new ObjectOutputStream(lFileOutputStream);
			//			lObjectOutputStream.writeObject(lSaveCopy);
			//			lObjectOutputStream.flush();
			//			lObjectOutputStream.close();
			//			
			//boolean lCompleteOneTrans =
			//	aSubcontractorRenewalCacheData.isCompleteOneTrans();
			//boolean lListBox =
			//	aSubcontractorRenewalCacheData.isListBox();
			// end defect 7586
			int liNextVC = aaSubcontractorRenewalCacheData.getNextVC();
			RTSException leRTSEx =
				aaSubcontractorRenewalCacheData.getException();
			int liExceptionField =
				aaSubcontractorRenewalCacheData.getExceptionField();
			aaSubcontractorRenewalCacheData.setNextVC(0);
			// defect 7586 
			// aSubcontractorRenewalCacheData.setCompleteOneTrans(false);
			// aSubcontractorRenewalCacheData.setListBox(false);
			// end defect 7586 
			aaSubcontractorRenewalCacheData.setException(null);
			aaSubcontractorRenewalCacheData.setExceptionField(0);
			FileOutputStream laFileOutputStream =
				new FileOutputStream(SUBCON_CACHE_FILE);
			ObjectOutputStream laObjectOutputStream =
				new ObjectOutputStream(laFileOutputStream);
			laObjectOutputStream.writeObject(
				aaSubcontractorRenewalCacheData);
			laObjectOutputStream.flush();
			laObjectOutputStream.close();
			//Restore
			aaSubcontractorRenewalCacheData.setNextVC(liNextVC);
			// defect 7586
			//aSubcontractorRenewalCacheData.setCompleteOneTrans(
			//lCompleteOneTrans);
			//aSubcontractorRenewalCacheData.setListBox(lListBox);
			// end defect 7586
			aaSubcontractorRenewalCacheData.setException(leRTSEx);
			aaSubcontractorRenewalCacheData.setExceptionField(
				liExceptionField);
			//End - performance enhancements
		}
		catch (IOException aeIOEx)
		{
			RTSException laRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIOEx);
			throw laRTSEx;
		}
	}
}
