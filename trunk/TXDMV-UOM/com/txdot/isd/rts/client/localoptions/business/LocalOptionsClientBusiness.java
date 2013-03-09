package com.txdot.isd.rts.client.localoptions.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.DealersCache;
import com.txdot.isd.rts.services.cache.RSPSUpdCache;
import com.txdot.isd.rts.services.cache.SecurityCache;
import com.txdot.isd.rts.services.cache.SubcontractorCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.localoptions.JniAdInterface;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;

/*
 * LocalOptionsClientBusiness.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		09/21/2001	Added logic to perform Logon
 * R Duggirala	12/12/2001  Added method to save reports
 * Ray Rowehl	02/20/2003	Do not trim passwords that are incrypted.
 *							also pad out to 8 characters for emp 
 *							passwords.
 *							CQU100004735
 * Min Wang		02/20/2003	Modified saveReport(). Defect CQU100005534.
 * Ray Rowehl	05/16/2003	Modify login process to also handle XP 
 *							changes
 *							modify login()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	10/20/2003	Add AD Security Interface Routines.
 *							Add local Security processing routines.
 *							add addEmpSec(), 
 *							delEmpSec(), updtEmpSec()					
 *							modify processData()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	12/01/2003	Rename AdUserId to SysUserId
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	12/18/2003	Rename SysUserId to UserName.
 *							Make aiModule and aiFunctionId consistent.
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	01/28/2004	Moved Active Directory methods to 
 *							JniAdInterface.
 *							Updated the 10/20/2003 comments as well.
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/05/2004	Update messages for 5.1.6.
 *							modify delEmpSec(), updateEmpSec()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							Fix static constants.
 * 							modify currEmployeeSecObject(),
 * 							employeeSecObject(), saveReports()
 * 							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/18/2004	update for isRtsUserName change in 
 *							JniAdInterface.
 *							modify delEmpSec(), updtEmpSec()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/26/2004	Move Active Directory Updates back to VC.
 *							modify updtEmpSec()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	03/04/2004	Move Updates back to client business.
 *							Found a different way to handle password. 
 *							modify delEmpSec(), updtEmpSec()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	03/12/2004	Show the password reset message after
 *							successfully reseting the password.
 *							modify updtEmpSec()
 *							defect 6926 Ver 5.1.6
 * K Harrell	03/28/2004	Remove reference to isWindowsPlatform
 *							modify delEmpSec(),login(),updtEmpSec()
 *							defect 6955 Ver 5.2.0
 * K Harrell	04/02/2004	Remove test for OS/2 logon processing
 *							modify login()
 *							defect 6955 Ver 5.2.0
 * Ray Rowehl	04/03/2004  When doing cached login, we should also
 *							make sure deleteindi is not set.
 *							modify login()
 *							done in 5.1.6 and being merged into 5.2.0
 *							defect 7017 Ver 5.2.0
 * Jeff S.		05/26/2004	Removed call to 
 *							Print.getDefaultPrinterProps() b/c it is not
 *							being used anymore.
 *							modify saveReports(Object)
 *							defect 7078 Ver 5.2.0
 * Min Wang 	06/28/2004	Design a way to move RSPS updates to flash 
 *							drive from a POS county machine.
 *							add getRSPSIdList(), getRSPSUpdt()
 *							modify processData()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/20/2004	Reflow getRSPSUpdt to try sending updates
 * Min Wang					directly to the server first.  This keeps
 *							the clerk aware of status.  Also found that
 *							we were checking for updates before the
 *							update process had posted.
 *							modify getRSPSUpdt()
 *							defect 7135 Ver  5.2.1
 * Ray Rowehl	08/23/2004	Put updates on jump drive when it starts
 *							out empty.
 *							Avoid processing xml files that contain
 *							error messages instead of data.
 *							modify getRSPSUpdt()
 *							defect 7135 Ver 5.2.1
 * Jeff Seifert	08/24/2004	Moved RSPSRefreshUtility inside Phase1.jar
 *							had to alter import statement to show the
 *							change.
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/13/2004	Move refresh over to frm to allow showing of
 *							message of how many updates to apply.
 *							modify getRSPSUpdt()
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/15/2004	add ScanEngine to parsing process
 *							modify getRSPSUpdt()
 *							defect 7135 Ver 5.2.1
 * Min Wang 	11/01/2004	blocking empty update strings
 *							modify getRSPSUpdt()
 *							defect 7670 Ver 5.2.1.1
 * Min Wang		09/27/2004	if object is not null, do not delete 
 *							username from active dir.
 *							modify delEmpSec()
 *							defect 7462 Ver 5.2.2
 * Jeff S.		04/15/2005	Added call to server if there
 * 							where no logs to process.  This call will
 * 							get all updates already installed.
 * 							modify getRSPSUpdt()
 * 							defect 8143 Ver. 5.2.2 Fix 4
 * Jeff S.		04/15/2005	When getting .jar date the format was diff 
 * 							than what we where looking for so the 
 * 							current date was used.
 * 							modify getRSPSUpdt()
 * 							defect 8155 Ver. 5.2.2 Fix 4
 * Jeff S.		04/18/2005	Removed import for RSPSRefreshUtility
 * 							b/c it was not needed and the class is now
 * 							deprecated.  Removed unused objects.
 * 							modify currEmployeeSecObject(), delEmpSec(),
 * 								getRSPSUpdt()
 * 							defect 8014 Ver. 5.2.3
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7891 Ver 5.2.3 
 * Ray Rowehl	06/11/2005	Remove unused methods.
 * 							delete prntConnectionStatusRpt(),
 * 								prntDlrRpt(), prntEmpSecrtyRpt(), 
 * 								prntEventSecrtyRpt(), prntLienhldrRpt(),
 * 								prntPublishingRpt(), prntSecrtyChngRpt(),
 * 								prntSubconRpt()
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		07/11/2005	Cleaned up constants.
 * 							defect 7891 Ver 5.2.3
 * Jeff S.		07/29/2005	Handled Nullpointer when there was not an
 * 							XML file but there where logs.
 * 							modify getRSPSUpdt()
 * 							defect 8309 Ver 5.2.2 Fix 6
 * Jeff S.		07/29/2005	Added ability to move .mdb files when
 * 							updating the flash drive.
 * 							add csMDBFileExten
 * 							modify getRSPSUpdt()
 * 							defect 8305 Ver 5.2.2 Fix 6
 * Jeff S.		10/05/2005	Added ability to move .mdb files when
 * 							updating the flash drive. Added logging when
 * 							the file was moved.  Also set the modified
 * 							date on the new file to that of the old.
 * 							add MOVING_FILE
 * 							delete csMDBFileExten
 * 							modify getRSPSUpdt()
 * 							defect 8322 Ver 5.2.3
 * Jeff S.		10/05/2005	Now we are collecting logdate.  The date 
 * 							that the laptop was updated.
 * 							modify getRSPSUpdt()
 * 							defect 8381 Ver 5.2.3
 * Jeff S.		12/01/2005	Handled Number Format Exception with DAT LVL
 * 							modify getRSPSUpdt()
 * 							defect 8442 Ver 5.2.2 Fix 7
 * K Harrell	04/20/2007	SecurityData.ciWorkStationType eliminated 
 * 							modify currEmployeeSecObject(),
 * 							 employeeSecObject
 * 							defect 9085 Ver Special Plates
 * J Zwiener	03/04/2009  Implement Certified Lienholder Report
 * 							  Add Landscape orientation
 * 							modify processData()
 * 							modify saveReports()
 * 							defect 9968 Ver Defect_POS_E
 * K Harrell	06/25/2009	Implement new DealerData
 * 							modify getRSPSIdList() 
 * 							defect 10112 Ver Defect_POS_F
 * Ray Rowehl	07/13/2009	rename constants to match constants class.
 * 							Add final to MAX_RSPS_HOSTNAME_LENGTH
 * 							modify MAX_RSPS_HOSTNAME_LENGTH
 * 							modify login()
 * 							defect 10103 Ver Defect_POS_F
 * K Harrell	08/17/2009	Implement UtilityMethods.addPCLandSaveReport() 
 * 							delete saveReports() 
 * 							modify processData()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							modify getRSPSIdList() 
 * 							defect 10161 Ver POS_640  
 * K Harrell	04/04/2010	TimeZone now set in VC
 * 							modify login() 
 * 							defect 10427 Ver POS_640 
 * K Harrell	09/12/2011	Add caching for Logon when Server Down 
 * 							modify login()
 * 							defect 10994 Ver 6.8.1    
 * ---------------------------------------------------------------------
 */

/**
 * Local Options client business layer.
 * 
 * <p>
 * Client side processing for Employee Security, Dealer List, Subcontractor
 * List, and Local Options Reports.
 * 
 * @version 6.8.1 09/12/2011
 * @author Ashish Mahajan
 * @since 09/05/2001 13:30:59
 */

public class LocalOptionsClientBusiness
{
	private static final int MAX_RSPS_HOSTNAME_LENGTH = 5;

	private static final String AD_DELETE_MSG = "About to do AD delete";

	private static final String AD_DELETE_RESULT_MSG = "AD Delete got ";

	private static final String GOT_EXCEPTION_MSG = "Got an exception ";

	private static final String SUBCON = "S";

	private static final String DEALER = "D";

	private static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	// defect 8322
	private static final String MOVING_FILE = "Moving  file from flash drive to ";

	// end defect 8322
	private static final String XML_UPDATE_BYPASS_MSG = " XML Update issue, bypass.";

	private static final String START_THREAD_MSG = "Starting RSPS Update Thread to process queued requests";

	private static final String RSPS_CACHE_CATCHUP_MSG = " RSPS Cache catchup is still running!";

	private static final String AD_UPDATE_PROB_MSG = " Ad had problems updating ";

	private static final String THREAD_NAME = "RSPSUpdate";

	private static final String ERROR_PARS_DAT_LVL = "Error converting DAT Level to integer using 0 ";

	/**
	 * LocalOptionsClientBusiness default constructor
	 */
	public LocalOptionsClientBusiness()
	{
		super();
	}

	/**
	 * Add a new Employee.
	 * 
	 * <p>
	 * Add to Directory is done at VCSEC005.
	 * 
	 * @return Object
	 * @param aiModule
	 *            int
	 * @param aiFunctionId
	 *            int
	 * @param aaData
	 *            Object
	 * @throws RTSException
	 */
	private Object addEmpSec(int aiModule, int aiFunctionId,
			Object aaData) throws RTSException
	{
		try
		{
			aaData = Comm.sendToServer(aiModule, aiFunctionId, aaData);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
	}

	/**
	 * Retrieves and Adds the current employee's security information to the
	 * vector of employee security information. The first element in the vector
	 * is the current employee's security information.
	 * 
	 * @return Object Vector of employees security information.
	 * @param aiModName
	 *            int
	 * @param aaData
	 *            Object
	 */
	private Object currEmployeeSecObject(int aiModule, Object aaData)
			throws RTSException
	{
		try
		{
			Vector lvsecObj = new Vector();
			SecurityClientDataObject laCurrEmpSecObj = new SecurityClientDataObject();
			SecurityData laCurrEmpData = new SecurityData();
			laCurrEmpData.setEmpId(SystemProperty.getCurrentEmpId());
			laCurrEmpData.setOfcIssuanceNo(SystemProperty
					.getOfficeIssuanceNo());
			laCurrEmpData.setSubstaId(SystemProperty.getSubStationId());
			Object laObjData = Comm.sendToServer(aiModule,
					LocalOptionConstant.GET_EMP_DATA_ONID,
					laCurrEmpData);
			laCurrEmpSecObj.setSecData((SecurityData) laObjData);
			lvsecObj.addElement(laCurrEmpSecObj);
			lvsecObj.addElement(aaData);
			return lvsecObj;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
	}

	/**
	 * Delete an Employee.
	 * 
	 * <p>
	 * This calls both the routines to delete the UserName from the Directory as
	 * well as the database. Directory deletes only occur on Windows.
	 * 
	 * @return Object
	 * @param aiModule
	 *            int
	 * @param aiFunctionId
	 *            int
	 * @param aaData
	 *            Object
	 * @throws RTSException
	 */
	private Object delEmpSec(int aiModule, int aiFunctionId,
			Object aaData) throws RTSException
	{
		// not being used
		// SecurityClientDataObject secObj;
		try
		{
			// do server side of delete first because it must verify
			// Inventory has been cleaned up first.
			Object laUnNeeded = Comm.sendToServer(aiModule,
					aiFunctionId, aaData);
			// do ad interface functions
			// parse out the Security Data
			Vector lvc = (Vector) aaData;
			SecurityData laSecData = (SecurityData) lvc.get(0);

			// defect 7462
			// if object is not null, do not delete username from
			// active dir.
			if (laUnNeeded == null)
			{
				if (JniAdInterface.isRtsUserName(laSecData
						.getUserName(), laSecData.getOfcIssuanceNo()))
				{
					if (SystemProperty.getProdStatus() != 0)
					{
						System.out.println(AD_DELETE_MSG);
					}
					int liRCP = JniAdInterface.adDelUser(laSecData);
					if (SystemProperty.getProdStatus() != 0)
					{
						System.out
								.println(AD_DELETE_RESULT_MSG + liRCP);
					}
					if (liRCP != JniAdInterface.NORMALRETURNCODE
							&& liRCP != JniAdInterface.USERNAMENOTFOUND)
					{
						// Add the user back to the database if there
						// was an ad error
						aaData = Comm
								.sendToServer(
										aiModule,
										LocalOptionConstant.ADD_EMP_ACCS_RIGHTS,
										aaData);
						// say UserName is invalid.
						throw (new RTSException(754));
					}
				}
				// end defect 7462
			}
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			if (SystemProperty.getProdStatus() != 0)
			{
				System.out.println(GOT_EXCEPTION_MSG + aeRTSEx);
			}
			throw aeRTSEx;
		}
	}

	/**
	 * Sets the employee's security information by setting
	 * SecurityClientDataObject
	 * 
	 * @return Object
	 * @param aiModule
	 *            int
	 * @param aiFunctionId
	 *            int
	 * @param aadata
	 *            Object
	 * @throws RTSException
	 */
	private Object employeeSecObject(int aiModule, int aiFunctionId,
			Object aaData) throws RTSException
	{
		try
		{
			SecurityClientDataObject laSecObj = new SecurityClientDataObject();
			Object laObjData = Comm.sendToServer(aiModule,
					aiFunctionId, aaData);
			laSecObj.setSecData((SecurityData) laObjData);

			// defect 9085
			// Get the Workstation type from cache based on office
			// issuance#
			// int liOffIssNo = SystemProperty.getOfficeIssuanceNo();
			// int liOfcId = 0;
			// not being used
			// OfficeIdsCache laOffCach = new OfficeIdsCache();
			// OfficeIdsData laOfcData =
			// OfficeIdsCache.getOfcId(liOffIssNo);
			// if (laOfcData != null)
			// {
			// liOfcId = laOfcData.getOfcIssuanceCd();
			// laSecObj.setWorkStationType(liOfcId);
			// }
			// end defect 9085

			return laSecObj;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
	}

	/**
	 * This method returns the RSPS Id List
	 * 
	 * @return Vector
	 */
	private Vector getRSPSIdList()
	{
		Vector lvSubconIdList = new Vector();
		Vector lvSubcon = SubcontractorCache.getSubcons(SystemProperty
				.getOfficeIssuanceNo(), SystemProperty
				.getSubStationId());
		for (int i = 0; i < lvSubcon.size(); i++)
		{
			SubcontractorData laSubcon = (SubcontractorData) lvSubcon
					.get(i);
			if (laSubcon.getDeleteIndi() != 0)
			{
				continue;
			}
			RSPSIdsData laRSPSIdsData = new RSPSIdsData();
			laRSPSIdsData.setIdType(SUBCON);
			// defect 10161
			laRSPSIdsData.setId(laSubcon.getId());
			laRSPSIdsData.setIdName(laSubcon.getName1());
			// end defect 10161
			lvSubconIdList.add(laRSPSIdsData);
		}

		Vector lvDealerIdList = new Vector();
		Vector lvDealer = DealersCache.getDealers(SystemProperty
				.getOfficeIssuanceNo(), SystemProperty
				.getSubStationId());
		for (int i = 0; i < lvDealer.size(); i++)
		{
			// defect 10112
			DealerData laDealer = (DealerData) lvDealer.get(i);
			if (laDealer.getDeleteIndi() != 0)
			{
				continue;
			}
			// end defect 10112
			RSPSIdsData laRSPSIdsData = new RSPSIdsData();
			laRSPSIdsData.setIdType(DEALER);
			laRSPSIdsData.setId(laDealer.getId());
			laRSPSIdsData.setIdName(laDealer.getName1());
			lvDealerIdList.add(laRSPSIdsData);
		}

		Vector lvIdList = new Vector();
		lvIdList.add(lvSubconIdList);
		lvIdList.add(lvDealerIdList);

		return lvIdList;
	}

	/**
	 * This method returns the RSPS Id List
	 * 
	 * @return RSPSUpdData
	 * @param aRSPSUpdData
	 *            Object
	 * @throws RTSException
	 */
	private RSPSUpdData getRSPSUpdt(Object aRSPSUpdData)
			throws RTSException
	{
		// get the passed data. It contains some client side info
		RSPSUpdData laPassedRSPSUpdData = (RSPSUpdData) aRSPSUpdData;
		RSPSWsStatusData laPassedRSPSWsStatusData = (RSPSWsStatusData) laPassedRSPSUpdData
				.getRSPSWsStatusData();
		File[] larrRSPSFiles = null;
		File laRSPSUpdtFilesDir = null;

		laRSPSUpdtFilesDir = new File(SystemProperty
				.getFlashDriveSourceDirectory()
				+ SystemProperty.getRSPSLogDir() + FILE_SEPARATOR);
		larrRSPSFiles = laRSPSUpdtFilesDir.listFiles();

		// setup the return object
		RSPSUpdData laReturnData = new RSPSUpdData();

		// Read in Laptop update file and parse data out.
		// this has to loop through all log files on jump drive
		try
		{
			Vector lvProcessData = new Vector();

			if (larrRSPSFiles != null && larrRSPSFiles.length > 0)
			{
				for (int i = 0; i < larrRSPSFiles.length; i++)
				{
					String lsFileName = larrRSPSFiles[i].toString()
							.toUpperCase();
					// defect 8322
					// defect 8305
					// Allow the mdb file to be moved to the logs
					// directory
					// if (lsFileName
					// .indexOf(SystemProperty.getRSPSLogSuffix())
					// > 0
					// || lsFileName.indexOf(csMDBFileExten) > 0)
					// end defect 8305
					if (SystemProperty.isRSPSValidSuffix(lsFileName))
					// end defect 8322
					{
						int liIndxOfLastSlashLog = lsFileName
								.lastIndexOf(FILE_SEPARATOR) + 1;
						File laFileInLog = new File(lsFileName);
						File laFileOutLog = new File(
								SystemProperty.getCountyRSPSDirectory()
										+ SystemProperty
												.getRSPSLogDir()
										+ FILE_SEPARATOR
										+ lsFileName
												.substring(liIndxOfLastSlashLog));
						long laLastModified = laFileInLog
								.lastModified();
						// copy and delete the app log file
						UtilityMethods.copyFile(laFileInLog,
								laFileOutLog, true);
						// defect 8322
						// Added logging for when we are moving file
						// from flash drive to d:\rts\rsps\logs
						Log.write(Log.SQL_EXCP, this, MOVING_FILE
								+ laFileOutLog.getAbsolutePath());
						// Set the last modified date of the new file
						// to the same as it was on the flash drive
						laFileOutLog.setLastModified(laLastModified);
						// end defect 8322
						continue;
					}
					else if (lsFileName.indexOf(SystemProperty
							.getRSPSUpdateSuffix()) < 0
							|| (lsFileName
									.indexOf(CommonConstant.POS_DTA) < 0 && lsFileName
									.indexOf(CommonConstant.POS_SUB) < 0))
					{
						// this file is not part of our processing
						continue;
					}

					RSPSWsStatusData laRSPSWsStatusData = new RSPSWsStatusData();
					laRSPSWsStatusData
							.setLastProcsdHostName(laPassedRSPSWsStatusData
									.getLastProcsdHostName());
					laRSPSWsStatusData
							.setLastProcsdUserName(laPassedRSPSWsStatusData
									.getLastProcsdUserName());
					RSPSUpdData laRSPSUpdData = new RSPSUpdData();
					laRSPSUpdData
							.setRSPSWsStatusData(laRSPSWsStatusData);

					try
					{
						int liIndxOfLastSlash = lsFileName
								.lastIndexOf(FILE_SEPARATOR) + 1;
						laRSPSWsStatusData.setOfcIssuanceNo(Integer
								.parseInt(lsFileName.substring(
										0 + liIndxOfLastSlash,
										3 + liIndxOfLastSlash)));
						laRSPSWsStatusData.setLocIdCd(lsFileName
								.substring(3 + liIndxOfLastSlash,
										4 + liIndxOfLastSlash));
						laRSPSWsStatusData.setLocId(Integer
								.parseInt(lsFileName.substring(
										4 + liIndxOfLastSlash,
										7 + liIndxOfLastSlash)));
					}
					catch (NumberFormatException aeNFEx)
					{
						// must not be a good file to work with
						continue;
					}

					// open the file for reading
					File laFileIn = new File(lsFileName);

					// not being used
					// String lsNewFileName = laFileIn.getName();

					FileInputStream lpfsFileInputStream = new FileInputStream(
							laFileIn);
					BufferedReader laBufferedReader = new BufferedReader(
							new InputStreamReader(lpfsFileInputStream));
					String lsBufRd;

					while ((lsBufRd = laBufferedReader.readLine()) != null)
					{
						// if this is an XML Close, just skip it.
						if (lsBufRd
								.startsWith(RSPSUpdData.XML_END_MARKER))
						{
							// do nothing
						}
						// parse out Code Version String
						else if (lsBufRd
								.equals(RSPSUpdData.CODE_VERSION))
						{
							// next line is Code Version
							lsBufRd = laBufferedReader.readLine();
							laRSPSWsStatusData.setRSPSVersion(lsBufRd);
						}
						else if (lsBufRd.equals(RSPSUpdData.JAR_SIZE))
						{
							// next line is Jar Size
							lsBufRd = laBufferedReader.readLine();
							laRSPSWsStatusData.setRSPSJarSize(lsBufRd);
						}
						else if (lsBufRd.equals(RSPSUpdData.JAR_DATE))
						{
							// next line is Jar Date
							lsBufRd = laBufferedReader.readLine();
							RTSDate laJarDate = new RTSDate();
							try
							{
								// defect 8155
								// Make sure that the date is in the
								// format 04/15/2005 else use todays
								// date
								// if (lsBufRd.length() > 7)
								if (lsBufRd.trim().length() == 10)
								{
									// laJarDate.setYear(
									// Integer.parseInt(
									// lsBufRd.substring(0, 4)));
									// laJarDate.setMonth(
									// Integer.parseInt(
									// lsBufRd.substring(4, 6)));
									// laJarDate.setDate(
									// Integer.parseInt(
									// lsBufRd.substring(6, 8)));
									laJarDate.setMonth(Integer
											.parseInt(lsBufRd
													.substring(0, 2)));
									laJarDate.setDate(Integer
											.parseInt(lsBufRd
													.substring(3, 5)));
									laJarDate.setYear(Integer
											.parseInt(lsBufRd
													.substring(6)));
									// end defect 8155
								}
							}
							catch (NumberFormatException aeNFEx)
							{
								// do not do anything to recover
							}
							laRSPSWsStatusData
									.setRSPSJarDate(laJarDate);
						}
						else if (lsBufRd.equals(RSPSUpdData.DB_VERSION))
						{
							// next line is DB Version
							lsBufRd = laBufferedReader.readLine();
							laRSPSWsStatusData.setDbVersion(lsBufRd);
						}
						// defect 8381
						// Now we want to collect this date
						else if (lsBufRd.equals(RSPSUpdData.LOG_DATE))
						{
							// next line is Log Date
							lsBufRd = laBufferedReader.readLine();
							RTSDate laRSPSProcsdDate = new RTSDate();
							try
							{
								// Make sure that the date is in the
								// format 20050927 else use todays
								// date
								if (lsBufRd.trim().length() == 8)
								{
									laRSPSProcsdDate.setYear(Integer
											.parseInt(lsBufRd
													.substring(0, 4)));
									laRSPSProcsdDate.setMonth(Integer
											.parseInt(lsBufRd
													.substring(4, 6)));
									laRSPSProcsdDate.setDate(Integer
											.parseInt(lsBufRd
													.substring(6)));
								}
							}
							catch (NumberFormatException aeNFEx)
							{
								// do not do anything to recover
							}
							laRSPSWsStatusData
									.setLastRSPSProcsdTimeStmp(laRSPSProcsdDate);
						}
						// end defect 8381
						else if (lsBufRd.equals(RSPSUpdData.DAT_LEVEL))
						{
							// next line is Dat Level
							lsBufRd = laBufferedReader.readLine();
							// defect 8442
							// We where getting unexpected data in the
							// XML field for DAT_LEVEL. Below is what
							// we where getting. This caused a Number
							// Format Exception.
							// lsBufRd = "4.0.45148";
							// laRSPSWsStatusData.setDATLvl(
							// Integer.parseInt(lsBufRd));

							// Save the lsBufRd string for logging later
							String lsBufRdOrig = lsBufRd;
							int liDatLvl = 0;

							try
							{
								int liPeriodLoc = lsBufRd
										.lastIndexOf(CommonConstant.STR_PERIOD);
								if (liPeriodLoc > -1)
								{
									lsBufRd = lsBufRd.substring(
											liPeriodLoc + 1, lsBufRd
													.length());
								}
								liDatLvl = Integer.parseInt(lsBufRd);
							}
							catch (NumberFormatException aeNFEx)
							{
								RTSException leRTSEx = new RTSException(
										RTSException.JAVA_ERROR,
										ERROR_PARS_DAT_LVL
												+ RSPSUpdData.DAT_LEVEL
												+ CommonConstant.STR_EQUAL
												+ lsBufRdOrig,
										CommonConstant.STR_SPACE_EMPTY);
							}
							laRSPSWsStatusData.setDATLvl(liDatLvl);
							// end defect 8442
						}
						else if (lsBufRd
								.equals(RSPSUpdData.SCAN_ENGINE))
						{
							// next line is Dat Level
							lsBufRd = laBufferedReader.readLine();
							laRSPSWsStatusData.setScanEngine(lsBufRd);
						}
						else if (lsBufRd.equals(RSPSUpdData.HOST_NAME))
						{
							// next line is Host Name
							lsBufRd = laBufferedReader.readLine();
							// trim back the host name to just have the
							// local rsps id.
							if (lsBufRd.length() > MAX_RSPS_HOSTNAME_LENGTH)
							{
								lsBufRd = lsBufRd.substring(lsBufRd
										.length()
										- MAX_RSPS_HOSTNAME_LENGTH);
							}
							laRSPSWsStatusData.setRSPSId(lsBufRd
									.toUpperCase());
						}
						else if (lsBufRd.equals(RSPSUpdData.IP_ADDRESS))
						{
							// next line is the Ip Address
							lsBufRd = laBufferedReader.readLine();
							laRSPSWsStatusData.setIPAddr(lsBufRd);
						}
						else if (lsBufRd
								.equals(RSPSUpdData.SYSTEM_UPDATES_LIST))
						{
							if (laRSPSUpdData.getSysUpdates() == null)
							{
								// initialize with a new vector
								laRSPSUpdData
										.setSysUpdates(new Vector());
							}
							else
							{
								// empty the vector before adding to it
								laRSPSUpdData.getSysUpdates().clear();
							}

							while ((lsBufRd = laBufferedReader
									.readLine()) != null)
							{
								// next line is System Updates List
								if (lsBufRd.trim().equals(
										RSPSUpdData.UPDATES))
								{
									// defect 7670
									String lsUpdates = laBufferedReader
											.readLine().trim();
									// setup the vector if it is null
									if (laRSPSUpdData.getSysUpdates() == null)
									{
										laRSPSUpdData
												.setSysUpdates(new Vector());
									}
									if (lsUpdates.length() > 0)
									{
										laRSPSUpdData.getSysUpdates()
												.add(lsUpdates);
									}
									// end defect 7670
								}
								else if (lsBufRd
										.trim()
										.equals(
												RSPSUpdData.SYSTEM_UPDATES_LIST_END))
								{
									// if we have processed all
									// updates, break out
									break;
								}
							}
						}
						else if (lsBufRd.equals(RSPSUpdData.HOST_NAME))
						{
							// next line is Host Name
							lsBufRd = laBufferedReader.readLine();
							laRSPSWsStatusData.setRSPSId(lsBufRd);
						}
					}

					laBufferedReader.close();
					lpfsFileInputStream.close();

					// bypass if there was no rspsid.
					// this is a sign of a bad update file
					if (laRSPSWsStatusData.getRSPSId().length() > 0)
					{
						lvProcessData.add(laRSPSUpdData);

						// send vector to RSPSUpdtCache
						RSPSUpdCache.processQueueRequest(
								RSPSUpdCache.ADD_TO_QUEUE,
								lvProcessData);
					}
					else
					{
						Log.write(Log.SQL_EXCP, this,
								XML_UPDATE_BYPASS_MSG);
					}

					// copy over the xml file now that it has been
					// cached
					File laFileOut = new File(SystemProperty
							.getCountyRSPSDirectory()
							+ SystemProperty.getRSPSLogDir()
							+ FILE_SEPARATOR + laFileIn.getName());

					UtilityMethods.copyFile(laFileIn, laFileOut, true);

					lvProcessData.clear();
				}

				// create the inital Update List Vector
				Vector lvSysUpdateList = new Vector();

				// if the thread is not running, try sending the update
				// directly first
				if (!RSPSUpdateProcess.isRunning())
				{
					try
					{
						// vector of updates to apply
						Vector lvUpdatesToSend = RSPSUpdCache
								.processQueueRequest(
										RSPSUpdCache.GET_QUEUE_ITEMS,
										lvProcessData);

						// if there are no updates to process
						// let the frame know there were no updates
						// processed.
						if (lvUpdatesToSend.size() < 1)
						{
							laReturnData = null;

							// send all updates
							lvSysUpdateList = new Vector();
						}
						else
						{
							// send the updates to the server
							lvProcessData = (Vector) Comm
									.sendToServer(
											GeneralConstant.LOCAL_OPTIONS,
											LocalOptionConstant.PROCESS_RSPS_UPDT,
											lvUpdatesToSend);

							// Remove the applied updates from the queue
							lvUpdatesToSend = RSPSUpdCache
									.processQueueRequest(
											RSPSUpdCache.DELETE_ITEMS,
											lvUpdatesToSend);

							// get the update list from the server
							// This is the list of all updates already
							// applied
							lvSysUpdateList = (Vector) Comm
									.sendToServer(
											GeneralConstant.LOCAL_OPTIONS,
											LocalOptionConstant.GET_SYSUPDATE_LIST,
											laPassedRSPSUpdData);
						}
					}
					catch (RTSException aeRTSEx)
					{

						// start the thread up on error
						if (!RSPSUpdateProcess.isRunning())
						{
							Log.write(Log.SQL_EXCP, this,
									START_THREAD_MSG);
							RSPSUpdateProcess laRSPSUpdate = new RSPSUpdateProcess();
							Thread laThread = new Thread(laRSPSUpdate,
									THREAD_NAME);
							laThread.setDaemon(true);
							laThread.start();
						}

						// check to see if the exception needs to be
						// thrown
						if (aeRTSEx.getMsgType().equals(
								RTSException.DB_DOWN)
								|| aeRTSEx.getMsgType().equals(
										RTSException.SERVER_DOWN))
						{
							// if server or db down
							// send all updates
							lvSysUpdateList = new Vector();
						}
						else
						{
							// otherwise throw the exception
							throw aeRTSEx;
						}
					}

				}
				else
				{
					Log.write(Log.SQL_EXCP, this,
							RSPS_CACHE_CATCHUP_MSG);
				}

				// if the thread is running, no point in trying to
				// get the list of updates. We are having troubles
				// updating the database
				// defect 8309
				// If laReturnData was null then we got a nullpointer
				if (laReturnData != null)
				{
					laReturnData.setSysUpdates(lvSysUpdateList);
				}
				// end defect 8309
			}
			else
			{
				// defect 8143
				// when there are no updates to pick up from the XML log
				// get the update list from the server
				// This is the list of all updates already applied
				// If we get an exception send them all.
				//
				// there are no updates to pick up
				// laReturnData.setSysUpdates(new Vector());
				try
				{
					laReturnData
							.setSysUpdates((Vector) Comm
									.sendToServer(
											GeneralConstant.LOCAL_OPTIONS,
											LocalOptionConstant.GET_SYSUPDATE_LIST,
											laPassedRSPSUpdData));
				}
				catch (RTSException aeRTSEx)
				{
					// Error getting update list from server just send
					// then all
					laReturnData.setSysUpdates(new Vector());
				}
				// end defect 8143
			}

			return laReturnData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (Exception aeEx)
		{
			RTSException laRTSException = new RTSException(
					RTSException.JAVA_ERROR, aeEx);
			throw laRTSException;
		}
	}

	/**
	 * Verifies Employee Logging in. If server is up, the request is verified at
	 * the server. If server is down, the request is verified via cache.
	 * 
	 * @return Object
	 * @param aiModule
	 *            int
	 * @param aiFunctionId
	 *            int
	 * @param aadata
	 *            Object
	 * @throws RTSException
	 */
	private Object login(int aiModule, int aiFunctionId, Object aaData)
			throws RTSException
	{
		LogonData laLogonData = (LogonData) aaData;

		// defect 10427
		// AssignedWorkstationIdsData laWsData =
		// AssignedWorkstationIdsCache.getAsgndWsId(
		// SystemProperty.getOfficeIssuanceNo(),
		// SystemProperty.getSubStationId(),
		// SystemProperty.getWorkStationId());
		//		
		// laLogonData.setTimeZone(laWsData.getTimeZone());
		// end defect 10427

		try
		{
			// send to server for processing
			laLogonData = (LogonData) Comm.sendToServer(aiModule,
					aiFunctionId, laLogonData);
		}
		catch (RTSException aeRTSEx)
		{
			// Get SecurityData from Cache
			SecurityData laSecurityData = SecurityCache.getSecurity(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId(), laLogonData
							.getUserName().trim().toUpperCase());
			// defect 7017
			// Add check for deleteindi for cached login
			if (laSecurityData != null
					&& laSecurityData.getDeleteIndi() == 0)
			{
				laLogonData.setSecurityData(laSecurityData);
				laLogonData
						.setReturnCode(LocalOptionConstant.LOGIN_SUCCESS_FROM_CACHE);
			}
			// end defect 7017
			else
			{
				// defect 10103
				laLogonData
						.setReturnCode(LocalOptionConstant.LOGIN_FAIL);
				// end defect 10103
			}

			// defect 10994
			// Write Logon Request to TrxCache
			int liSuccessful = laLogonData.getReturnCode() == LocalOptionConstant.LOGIN_FAIL ? 0:1;
			
			LogonFunctionTransactionData laLogFuncTrans = new LogonFunctionTransactionData(
					laLogonData);
			RTSDate laRTSDate = new RTSDate();
			laLogFuncTrans.setCacheTransAMDate(laRTSDate.getAMDate());
			laLogFuncTrans.setCacheTransTime(laRTSDate.get24HrTime());
			laLogFuncTrans.setSuccessfulIndi(liSuccessful);
			laLogFuncTrans.setTransPostedMfIndi(0);
			laLogFuncTrans.setMfVersionNo(SystemProperty
					.getMainFrameVersion());
			TransactionCacheData laTransactionCacheData = new TransactionCacheData();
			laTransactionCacheData.setObj(laLogFuncTrans);
			laTransactionCacheData
					.setProcName(TransactionCacheData.INSERT);
			Vector lvTrans = new Vector();
			lvTrans.addElement(laTransactionCacheData);
			Transaction.writeToCache(lvTrans);
			// end defect 10994
		}
		return laLogonData;
	}

	/**
	 * Process data method to direct requests to client layer or on to server
	 * layer.
	 * 
	 * @return Object
	 * @param aiModule
	 *            String Module name
	 * @param aiFunctionId
	 *            int Function id of the method
	 * @param aaData
	 *            Object data object
	 * @throws RTSException
	 */
	public Object processData(int aiModule, int aiFunctionId,
			Object aaData) throws RTSException
	{
		// defect 9968 - added Certfd_Lienhldr_Rpt; consolidated CASEs
		// Pass the call to the communication layer
		switch (aiFunctionId)
		{
		case LocalOptionConstant.LOGIN:
		case LocalOptionConstant.CHANGE_PASS:
		{
			return login(aiModule, aiFunctionId, aaData);
		}
		case LocalOptionConstant.ADD_EMP_ACCS_RIGHTS:
		{
			// defect 10102
			return addEmpSec(aiModule, aiFunctionId, aaData);
			// end defect 10102
		}
		case LocalOptionConstant.DEL_EMP_ACCS_RIGHTS:
		{
			// defect 10102
			return delEmpSec(aiModule, aiFunctionId, aaData);
			// end defect 10102
		}
		case LocalOptionConstant.GET_EMP_DATA_ONID:
		{
			return employeeSecObject(aiModule, aiFunctionId, aaData);
		}
		case LocalOptionConstant.REVISE_EMP_ACCS_RIGHTS:
		{
			return updtEmpSec(aiModule, aiFunctionId, aaData);
		}
		case LocalOptionConstant.GENERATE_DLR_RPT:
		case LocalOptionConstant.GENERATE_SUBCON_RPT:
		case LocalOptionConstant.GENERATE_LIENHLDR_RPT:
		case LocalOptionConstant.GENERATE_SECRTY_CHNG_RPT:
		case LocalOptionConstant.GENERATE_EMP_SECRTY_RPT:
		case LocalOptionConstant.GENERATE_EVENT_SECRTY_RPT:
		case LocalOptionConstant.GENERATE_PUBLISHING_RPT:
		case LocalOptionConstant.GENERATE_CERTFD_LIENHLDR_RPT:
		{
			// defect 8628
			// return saveReports(
			return UtilityMethods.addPCLandSaveReport(Comm
					.sendToServer(aiModule, aiFunctionId, aaData));
			// end defect 8628
		}

		case LocalOptionConstant.ADD_CURRENT_EMP_DATA_ONID:
		{
			return currEmployeeSecObject(aiModule, aaData);
		}
			// defect 7135
		case LocalOptionConstant.GET_RSPS_IDS:
		{
			return getRSPSIdList();
		}
		case LocalOptionConstant.GET_RSPS_UPDT:
		{
			return getRSPSUpdt(aaData);
		}
			// end defect 7135
			// end defect 9968
		default:
		{
			return Comm.sendToServer(aiModule, aiFunctionId, aaData);
		}
		}
	}

	// /**
	// * This method saves the reports for Local Options.
	// *
	// * @return Object
	// * @param aaData Object
	// * @throws RTSException
	// */
	// private Object saveReports(Object aData) throws RTSException
	// {
	// ReportSearchData laRptSearchData = (ReportSearchData) aData;
	// UtilityMethods laUtil = new UtilityMethods();
	// Print laPrint = new Print();
	// Vector lvReports = new Vector();
	// String lsPageProps = laPrint.getDefaultPageProps();
	// // defect 9968
	// if (laRptSearchData.getIntKey2()== ReportConstant.LANDSCAPE)
	// {
	// lsPageProps =
	// lsPageProps.substring(0, 2)
	// + Print.getPRINT_LANDSCAPE()
	// + lsPageProps.substring(2);
	// }
	// // end defect 9968
	// String lsRpt =
	// lsPageProps
	// + Print.getPRINT_TRAY_2()
	// + LINE_SEPARATOR
	// + laRptSearchData.getKey1();
	// String lsfileName =
	// laUtil.saveReport(
	// lsRpt,
	// laRptSearchData.getKey2(),
	// laRptSearchData.getIntKey1());
	// laRptSearchData.setKey1(lsfileName);
	// // defect 9968
	// if (laRptSearchData.getIntKey2()!=ReportConstant.LANDSCAPE)
	// {
	// laRptSearchData.setIntKey2(ReportConstant.PORTRAIT);
	// }
	// // end defect 9968
	// lvReports.add(laRptSearchData);
	//
	// return lvReports;
	// }

	/**
	 * Update the Employee security info.
	 * 
	 * <p>
	 * User Information is also updated in the Directory if on Windows.
	 * 
	 * <p>
	 * If the reset password flag is set, reset the password through the
	 * Directory interface.
	 * 
	 * @return Object
	 * @param aiModule
	 *            int
	 * @param aiFunctionId
	 *            int
	 * @param aaData
	 *            Object
	 * @throws RTSException
	 */
	private Object updtEmpSec(int aiModule, int aiFunctionId,
			Object aaData) throws RTSException
	{
		try
		{
			// parse out the Security Data
			Vector lvc = (Vector) aaData;
			SecurityData laSecData = (SecurityData) lvc.get(0);
			// only do ad interface to update a UserName if it is
			// an rts user and an AD Update is indicated.
			// do not go here for non-rts users.
			if (JniAdInterface.isRtsUserName(laSecData.getUserName(),
					laSecData.getOfcIssuanceNo())
					&& laSecData.getJniUpdate())
			{
				// reset the password
				if (laSecData.getResetPassword())
				{
					int liRCP = JniAdInterface
							.adResetPassword(laSecData);
					// defect 6926
					// show message about status of password reset.
					if (liRCP != 0)
					{
						// say password can not be reset .
						throw (new RTSException(755));
					}
					else
					{
						// show the password reset message
						RTSException laRTSEx = new RTSException(164);
						laRTSEx.displayError((javax.swing.JFrame) null);
					}
					// end defect 6926
				}
				// call ad routine to do update
				int liRC = JniAdInterface.adUpdtUser(laSecData);
				if (liRC != 0)
				{
					if (SystemProperty.getProdStatus() > 0)
					{
						System.out.println(AD_UPDATE_PROB_MSG + liRC);
						Log.write(Log.SQL_EXCP, this,
								AD_UPDATE_PROB_MSG + liRC);
					}
					// say connection failure.
					throw (new RTSException(756));
				}
			}
			// }
			Comm.sendToServer(aiModule, aiFunctionId, aaData);
			return null;
		}
		catch (RTSException aeRTSEx)
		{
			if (SystemProperty.getProdStatus() != 0)
			{
				System.out.println(GOT_EXCEPTION_MSG + aeRTSEx);
			}
			throw aeRTSEx;
		}
	}
}