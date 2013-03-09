package com.txdot.isd.rts.server.common.business;

import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.db.FraudLog;
import com.txdot.isd.rts.server.db.SpecialRegistrationFunctionTransaction;
import com.txdot.isd.rts.server.db.Transaction;
import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * VehicleInquiry.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/20/2002	No more NullPointerException CQU100004018 
 * MAbs/TP		06/05/2002	MultiRecs in Archive CQU100004019
 * K Salvi		11/19/2002	Added fix for CQU100004903 - special plates 
 *							are returned only for VEHINQ transactions.
 * Ray Rowehl	02/03/2003	fix CQU100004588
 * 							Setup ClientHost for MFAccess.
 * Ray Rowehl	07/12/2003	Reverse defect 4903 and allow special owner 
 *							info to flow back to client.  client will 
 *							decide what to do.
 *							defect 6170
 * Bob Brown	01/20/2004	Changed method searchMfActiveInact to match 
 *							up the last 4 digits of VIN from the MF record
 *							with what the internet user entered in the 
 *							browser,when more than one MF record is returned.
 *							This keeps the internet user from seeing a 
 *							"System not available" error message when there
 *							are 2 active regis recs on the MF which is 
 *							handled in new methodcheckForMultiRegisRecs.
 *                          defect 6709. Version 5.1.5 fix 2
 * K Harrell	06/04/2004	JavaDoc Cleanup, Reapply 6709 
 *							Ver 5.2.0  
 * Ray Rowehl	03/31/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields, use static reference
 * 							delete CommonConstant object
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	05/23/2005	Only set the OwnerId data if the 
 * 							Vehicle data object has been created.
 * 							modify searchMfArchive()
 * 							defect 6742 Ver 5.2.3
 * Ray Rowehl	05/23/2005	Pass the OwnerId so it can be used later
 * 							modify searchMfArchive()
 * 							defect 7746 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Cleanup comments.
 * 							modify searchMfArchive()
 * 							defect 7746 Ver 5.2.3
 * Min Wang		10/10/2006	New Requirement for handling plate age 
 * 							modify handleRegData()
 *							defect 8901 Ver Exempts
 * J Rue		01/24/2007	Business logic move from MfAccess to 
 * 							VehicleInquiry
 * 							add getMuplRegisIndi(), getTtlRegResponse()
 * 							add setNoOfRecs(), getJunkIndi
 * 							add getCICSTransId()
 * J Rue		01/24/2007	Add Special Plates Full to method.
 * 							Moved MfAcces business logic to method.
 * 							modify searchMfActiveInact()
 * 							modify searchMfArchive()
 * 							defect 9086 Ver Special Plates 
 * J Rue		02/06/2007	Get Spec-Regis/Cancel Plate method
 * 							add retrieveVehicleBySpecialOwner()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/07/2007	Get MutlRegis and Junk Response
 * 							new retrieveMutlRegisJunkResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/08/2007	Not used
 * 							deprecate validateSearchingArchive()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/09/2007	Add new inquiry to mainfarme, Special Plates
 * 							add retrieveSpclPltRegisData()
 * 							modify manageMultRecs(), 
 * 							modify getMultiRecsFromArchive()
 * 							defect 9086 Ver Special Plates
 * J Rue/		02/13/2007	Get Special Plates Regis, Internet
 * B Brown					Remove duplicate code
 * 							modify searchMfActiveInact()
 * 							modify checkForMultiRegisRecs()
 * 							modify getMultiRecsFromArchive()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/16/2007	Cleanup Partials and Internet
 * 							Add check for empty MfVehicleData()
 * 							modify searchMfActiveInact()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/21/2007	Add getter/setter to set number of records
 * 							rtnNoOfRecsMF() returns the number of 
 * 							records set in the MF header.
 *							add getNoOfRecs(), setNoOfRecs() 						
 * 							add rtnNoOfRecsMF()
 * 							modify searchMfActiveInact()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/22/2007	Add 
 * 							!(lsTransCd.equals(TransCdConstant.SPAPPL))
 * 							modify retrieveSpclPltRegisData()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/22/2007	Rename getSpclPltRegis() to 
 * 							getSpclPltRegisData()
 * 							modify manageMultRecs(), 
 * 							modify searchMfActiveInact(),
 * 							modify getMultiRecsFromArchive(),
 * 							modify retrieveSpclPltRegisData()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/23/2007 	Set up GSD - IntKey(2) for SP lookups
 * 							modify getVeh(), searchMfActiveInact()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/26/2007	Move assignment into if block.
 * 							modify retrieveSpclPltRegisData()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/27/2007	Add convert I's and O's in RegPltNo, SpclPlt	
 * 							modify retrieveSpclPltRegisResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/08/2007	Add check for empty MfResponse
 * 							modify retrieveSpclPltRegisResponse()
 * 							modify searchMfActiveInact()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/04/2007	Clean up code
 *  Jeff Rue				Build an empty MfVehicleData object
 * 							add createVehInqDataObj()
 * 							modify retrieveSpclPltRegisData()
 * 							modify searchMfActiveInact()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/20/2007	Make correction to capture MultiRegis
 * 							Clean up code
 * 							modify manageMultRecs(), 
 * 							modify searchMfActiveInact()
 * 							deprecate getOwnerData()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/25/2007	getOwnerData() not used
 * 							Add try-catch to catch exception for MF down
 * 							Set MFDown = 1
 * 							deprecate getOwnerData() 
 * 							modify manageMultRecs(), searchMfArchive(),
 * 							modify searchMfActiveInact(),
 * 							modify retrieveMutlRegisJunkResponse(),
 * 							modify retrieveVehicleBySpecialOwner(),
 * 							modify retrieveSpclPltRegisResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/27/2007	New method to return number of records.
 * 							add rtnNoOfRecsMF()
 * 							defect 8984 Ver Special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL() vs. check for
 * 							SPAPPL 
 * 							defect 9085 Ver Special Plates
 * J Rue		06/05/2007  Add manageSpclMultRegs
 * 							Sort (Natural) MultRecs
 * 							add manageSpclMultRegs()
 * 							modify 	manageMultRecs()
 * 							defect 9086 Ver Special Plates
 * J Rue		06/07/2007	Get Special Plates Regis Data for MultRegis
 * 							add searchMultRegisSpclPltData()
 * 							modify retrieveMutlRegisJunkResponse()
 * 							defect 9086 Ver Special Plates
 * J Rue		06/22/2007	Set VINAExists based on the response 
 * 							modify retrieveVINAResponse()
 * 							defect 8984 Ver Special Plates
 * B Brown		08/03/2007  When the title MF access returns more than
 * 							one record, call checkForMultiRegisRecs 
 * 							again to access by docno to retun one
 * 							mainframe record.
 * 							modify checkForMultiRegisRecs
 * 							defect 9119 Ver Special Plates
 * J Rue		10/04/2007	For multiRegis, Capture SpclRegisId in 
 * 							handleRegData(). Make call to MF for 
 * 							SpclPltRegis data if SpclRegId exist.
 * 							modify handleRegData(), manageMultRegs()
 * 							defect 9312 Ver Special Plates
 * K Harrell	10/31/2007	Reset AddlSetIndi if PltSetImprtnceCd not in
 * 							2,3
 * 							add correctSpclPltAddlSetIndi() 
 * 							modify retrieveSpclPltRegisResponse()
 * 							defect 9390 Ver Special Plates 2
 * J Rue		11/05/2007	Add a second attempt to record retrieval
 * 							 if Exception was returned
 * 							modify searchMfActiveInact()
 * 							defect 9403 Ver Special Plates
 * J Rue		11/12/2007	Add additional Log messaging for retries
 * 							modify searchMfActiveInact()
 * 							defect 9403 Ver Special Plates
 * K Harrell	11/13/2007	Comment cleanup.  Sort members. Make variables
 * 							private instead of public as were only 
 * 							referenced locally.   
 * 							delete getOwnerData(), 
 * 							 validateSearchingArchive()
 * 							defect 9390 Ver Special pLATES
 * Ray Rowehl	02/07/2008	Allow V21VTN to access VINA like TITLE.
 * 							modify searchMfActiveInact()
 * 							defect 9502 Ver Defect_POS_A  
 * Ray Rowehl	02/08/2008	Allow V21 trans to use the IVTRS approach to 
 * 							multiple records.
 * 							modify searchMfActiveInact()
 * 							defect 9502 Ver Defect_POS_A
 * Ray Rowehl	02/20/2008	Also allow G36 trans to use the IVTRS 
 * 							approach to multiple records.
 * 							modify searchMfActiveInact()
 * 							defect 9502 Ver Defect_POS_A
 * Ray Rowehl	03/03/2008	Modify archive search to use the IVTRS 
 * 							approach to multiple records for V21.
 * 							modify getMultiRecsFromArchive(),
 * 								searchMfArchive()
 * 							defect 9502 Ver Defect_POS_A
 * B Hargrove	04/07/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify handleRegData()
 * 							defect 9631 Ver Defect POS A
 * Min Wang		12/04/2007  FRVP
 * 							add verifyInsurance()
 * 							modify getVeh()
 * 							defect 9471 Ver FRVP
 * Ray Rowehl	02/22/2008	Tie in new web service client to call the 
 * 							FRVP web service.
 * 							modify verifyInsurance()
 * 							defect 9471 Ver FRVP
 * Ray Rowehl	02/27/2008	Modify verification to handle multiple regis
 * 							records.	
 * 							modify verifyInsurance()
 * 							defect 9471 Ver FRVP
 * Ray Rowehl	04/11/2008	Send output to rtsapp.log.
 * 							modify verifyInsurance()
 * 							defect 9471 Ver FRVP
 * Ray Rowehl	04/23/2008	Add check of CheckInsurance switch before
 * 							calling the TexasSure server.
 * 							modify verifyInsurance()
 * 							defect 9471 Ver FRVP
 * Ray Rowehl	04/24/2008	Populate QueryOriginator.
 * 							modify verifyInsurance()
 * 							defect 9471 Ver FRVP
 * Ray Rowehl	04/29/2008	Match parameter order to WSDL.
 * 							modify verifyInsurance()
 * 							defect 9645 Ver FRVP
 * K Harrell	05/21/2008	Use 'SCOT' vs. 'SLVG'
 * 							 No longer use COA 
 * 							modify searchMfActiveInact(), 
 * 								search MfArchive()
 * 							defect 9636, 9642 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver tp Defect_POS_A
 * 							defect 9636 Ver Defect_POS_A
 * K Harrell	09/08/2008	Typo in loop i<= vs. i< 
 * 							modify checkForMultiRegisRecs()
 * 							defect 9816 Ver Defect_POS_B  
 * B Hargrove	12/31/2008	Also do Insurance Verification after Archive
 * 							look-up. 
 * 							modify getVeh()
 * 							defect 9644 Ver Defect_POS_D  
 * K Harrell	01/07/2009	Rollback defect 9390.  No longer reset 
 * 							AddlSetIndi 
 * 							deprecate correctSpclPltAddlSetIndi()
 * 							modify retrieveSpclPltRegisResponse()
 * 							defect 9439 Ver Defect_POS_D
 * B Hargrove	01/16/2009  In getVeh(), only verify insurance if one
 * 							record is returned (if multi regis, the
 * 							RegData object is not yet populated).
 * 							In manageMultRegs(), handle the insurance
 * 							verification after the RegData is acquired.
 * 							modify getVeh(), manageMultRegs()
 * 							defect 9691 Ver Defect_POS_D
 * K Harrell	04/01/2009	Restore 9390. 
 * 							add correctSpclPltAddlSetIndi()
 * 							modify retrieveSpclPltRegisResponse()
 * 							defect 10018 Ver Defect_POS_D
 * J Rue		05/08/2009	Add setPltBirthDate for V21 vehicle inquiry 
 * 							modify handleRegData()
 * 							defect 10034 Ver Defect_POS_D
  * K Harrell	07/20/2009	Use UtilityMethods.isEmpty() to test OwnerId
 * 							modify searchMfActiveInact(), 
 * 							  searchMfArchive()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	04/10/2010	Make call to DB for Internet Special Plate 
 * 							Application when when not found or if MF Down.   
 * 							add getSpclPltRegisFromDB() 
 * 							modify retrieveSpclPltRegisData() 
 * 							defect 9858    Ver POS_640
 * K Harrell	06/08/2010	Refactor variable names/methods to correct  
 * 							spelling error (Responce to Response)
 * 							defect 10492 Ver 6.5.0 
 * K Harrell	07/10/2010	EMailRenwlReqCd must be copied from Partials
 * 							when Multiple Regis
 * 							modify handleRegData()
 * 							defect 10508 Ver 6.5.0 
 * K Harrell	08/31/2010	caMfAccess should not be static; Write to 
 * 							System.err.log on retry.
 * 							delete rtnNoOfRecsMF() 
 * 							modify caMfAccess 
 * 							modify searchActiveInactive() 
 * 							defect 10462 Ver 6.5.0 
 * K Harrell	09/22/2010	Add Logic for gathering In Process Transaction 
 * 							Data when appropriate.
 * 							add updtInProcsTrans()  
 * 							modify getVeh() 
 * 							Ver 6.6.0 defect 10598 
 * B Brown		10/06/2010	Add the checking of key6 for IRENEW
 * 							modify getVeh()
 * 							Ver 6.6.0 defect 10608
 * B Brown		12/08/2010	Use key6 for searching mainframe for IADDRE
 * 							modify getVeh()
 * 							Ver 6.7.0 defect 10610  
 * K Harrell	03/09/2011	add IRENEW to Insurance Verification 
 * 							modify getVeh(), verifyInsurance() 
 * 							defect 10771 Ver 6.7.1 
 * K Harrell	03/09/2011	add WRENEW processing
 * 							modify verifyInsurance(),
 * 							  searchMfActiveInact() 
 * 							defect 10768 Ver 6.7.1
 * K Harrell	06/03/2011	add logic to determine prior CCO Fraud 
 * 							Indicators for returned record
 * 							add updtFraudData()
 * 							modify getVeh()
 * 							defect 10865 Ver 6.8.0 
 * Ray Rowehl	08/10/2011	Change verify insurance to allow for a 
 * 							timeout.
 * 							modify verifyInsurance()
 * 							defect 10119 Ver 6.8.1
 * Ray Rowehl	08/15/2011	Added changes proposed by Buck to enable 
 * 							jUnit testing.
 * 							add makeTexasSureCall()
 * 							modify verifyInsurance()
 * 							defect 10119 Ver 6.8.1
 * K Harrell	08/22/2011	Check for Nulls before updtFraudData()
 *        					modify getVeh()
 *        					defect 10974 Ver 6.8.0/6.8.1 
 * Buck Woodson 08/26/2011  modify verifyInsurance()
 *                          defect 10119 Ver 6.8.1
 * K Harrell	09/30/2011	RecpntEMail not copied for multiple regis 
 * 							modify handleRegData()
 * 							defect 10903 Ver 6.9.0                   
 * ---------------------------------------------------------------------
 */

/**
 * Retrieve MF Vehicle information from main frame.
 *
 * @version	6.9.0	09/30/2011
 * @author	Joseph Peters
 * @since			08/21/2001 15:40:27
 */

public class VehicleInquiry
{
	// boolean
	private boolean cbReturnIncomingVehInqData = false;

	// int 
	private int ciNoOfRecs = 0;

	// int - static  
	private static int siGetRecAttempts = 1;
	private static int siTotalRetries = 0;

	// object
	private GeneralSearchData caSearchInfo = new GeneralSearchData();
	private VehicleInquiryData caInqData = new VehicleInquiryData();
	private VehicleInquiryData caTmpInqData;

	// defect 10462 
	private MfAccess caMfAccess = new MfAccess();
	// end defect 10462 

	// object - static
	private static RTSDate saTimeStmp = new RTSDate();

	// String
	private String csClientHost = "Unknown";
	private String csMfTtlRegResponse = CommonConstant.STR_SPACE_EMPTY;

	private static String FAILED = "FAILED ";
	private static String FRVP_RESULT = "FRVP Result ";
	private static String QUERY_ID = " Query Id ";
	private static String QUERY_TIME = " Query Time ";
	private static String DOC_NO = " Doc No ";
	// end defect 9741

	/**
	 * VehicleInquiry constructor.
	 */
	public VehicleInquiry()
	{
		super();
		initialize();
	}

	/**
	 * VehicleInquiry constructor.
	 */
	public VehicleInquiry(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
		initialize();
	}

	/**
	 * Check for Multiple Regis Records.
	 * 
	 * <p>The purpose of this method is to handle MF retrieval when 
	 * retrieving a regis record with 2 active MF regis recs that have:
	 * scenario 1 - dup plateno, diff vin, diff docno.
	 * 
	 * @param aaGSD GeneralSearchData
	 * @param aaMFA MfAccess
	 */
	public void checkForMultRegisRecs(
		GeneralSearchData aaGSD,
		MfAccess aaMFA)
	{
		// At present, there can be no more than 2 active regis recs
		Vector lvData = caInqData.getPartialDataList();
		MFPartialData laPartialData1 = null;
		MFPartialData laPartialData2 = null;

		// PartialData (Multiple Titles) return from mainframe.
		//	Get current record (by DocNo) and call mainframe for 
		//	record by DocNo.
		laPartialData1 = (MFPartialData) lvData.get(0);
		laPartialData2 = (MFPartialData) lvData.get(1);
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;

		// test for same vin
		// get record with most recent TransAmDate portion by DocNo
		if (laPartialData1.getVin().equals(laPartialData2.getVin()))
		{
			// test for diff docno
			// Set GSD search by current DocNo
			if ((Integer
				.parseInt(laPartialData1.getDocNo().substring(6, 11)))
				> (Integer
					.parseInt(
						laPartialData2.getDocNo().substring(6, 11))))
			{
				aaGSD.setKey2(laPartialData1.getDocNo());
			}
			else
			{
				aaGSD.setKey2(laPartialData2.getDocNo());
			}
			aaGSD.setKey1(CommonConstant.DOC_NO);

			// Get record by DocNo
			try
			{
				lsMfResponse = aaMFA.retrieveVehicleFromMF(aaGSD);
				setNoOfRecs(rtnNoOfRecsMF(lsMfResponse));
				// Get MultRegis and Junk records
				// move mf response data into VehInq data objects
				caInqData =
					retrieveMultRegisJunkResponse(lsMfResponse, aaGSD);
				// get special plate regis data
				caInqData = retrieveSpclPltRegisData(caInqData, aaGSD);
				caInqData.setNoMFRecs(1);
			}
			catch (RTSException aeRTSEx)
			{
				caInqData.setMfDown(1);
			}
		}
		else
		{
			// test for same plate, diff vin
			// defect 9816 
			for (int i = 0;
				i < caInqData.getPartialDataList().size();
				i++)
				// end defect 9816
			{
				MFPartialData laMFPD =
					(MFPartialData) caInqData
						.getPartialDataList()
						.elementAt(
						i);
				// Compare the last 4 digits
				if (laMFPD
					.getVin()
					.substring(
						laMFPD.getVin().length() - 4,
						laMFPD.getVin().length())
					.equals(aaGSD.getKey4()))
				{
					// setting key1 to VIN means title file access
					aaGSD.setKey1(CommonConstant.VIN);
					aaGSD.setKey2(laMFPD.getVin());

					// Make MF Vehicle call
					try
					{
						// going to title file
						lsMfResponse =
							aaMFA.retrieveVehicleFromMF(aaGSD);
						setNoOfRecs(rtnNoOfRecsMF(lsMfResponse));
						// Get MultRegis and Junk records
						// move mf response data into VehInq data objects
						caInqData =
							retrieveMultRegisJunkResponse(
								lsMfResponse,
								aaGSD);
						// get special plate regis data

						caInqData =
							retrieveSpclPltRegisData(caInqData, aaGSD);
						// defect 9119
						// multiple title recs accessed by vin returned
						// here 
						// looping back thru this method to get on MF  
						// rec by docno	
						if (getNoOfRecs() > 1)
						{
							checkForMultRegisRecs(aaGSD, aaMFA);
						}
						caInqData.setNoMFRecs(getNoOfRecs());
						// end defect 9119

						break;
					}
					catch (RTSException RTSEx)
					{
						caInqData.setMfDown(1);
					}
				}
			}
		}
	}

	/**
	 * Correct SpecialPlateRegisData AddlSetIndi
	 * 
	 * @param aaVehInqData
	 */
	private void correctSpclPltAddlSetIndi(VehicleInquiryData aaVehInqData)
	{
		if (aaVehInqData.getMfVehicleData() != null
			&& aaVehInqData.getMfVehicleData().getSpclPltRegisData()
				!= null)
		{
			SpecialPlatesRegisData laSpclPltRegisData =
				aaVehInqData.getMfVehicleData().getSpclPltRegisData();
			String lsRegPltCd = laSpclPltRegisData.getRegPltCd();
			if (lsRegPltCd != null)
			{
				PlateTypeData laPltTypeData =
					PlateTypeCache.getPlateType(lsRegPltCd);
				if (laPltTypeData != null)
				{
					int liPltSetImprtnceCd =
						laPltTypeData.getPltSetImprtnceCd();
					if (liPltSetImprtnceCd
						== SpecialPlatesConstant
							.PLTSETIMPRTNCECD_NOT_IMPORTANT
						|| liPltSetImprtnceCd
							== SpecialPlatesConstant
								.PLTSETIMPRTNCECD_FIRST_SET_ONLY)
					{
						laSpclPltRegisData.setAddlSetIndi(0);
					}
				}
			}
		}
	}

	/**
	 * Create an empty MfVehicle data object
	 *
	 * @return MFVehicleData
	 */
	private MFVehicleData createVehInqDataObj()
	{
		// Construct MFVehicleData
		MFVehicleData laMFVehicleData = new MFVehicleData();
		//Create Data objects to be added to MfVehicleData
		VehicleData laVehicleData = new VehicleData();
		RegistrationData laRegistrationData = new RegistrationData();
		OwnerData laOwnerData = new OwnerData();
		TitleData laTitleData = new TitleData();
		//SalvageData laSalvageData = new SalvageData();
		Vector laSalvageContainer = new Vector();
		// Special Plate regis
		SpecialPlatesRegisData laSpclPltRegisData =
			new SpecialPlatesRegisData();

		// Add all data objects to MfVehicleData
		// Add VehicleData
		laMFVehicleData.setVehicleData(laVehicleData);
		//add Registration data
		laMFVehicleData.setRegData(laRegistrationData);
		// Add OwnerData
		laMFVehicleData.setOwnerData(laOwnerData);
		// Add TitleData
		laMFVehicleData.setTitleData(laTitleData);
		// Add SalvageData
		laMFVehicleData.setVctSalvage(laSalvageContainer);
		//	Add Special Plt Regis to MfVehicleData
		laMFVehicleData.setSpclPltRegisData(laSpclPltRegisData);

		// Return result
		return laMFVehicleData;
	}

	/**
	 * Check for JunkIndi
	 *  
	 * @param asMfTtlRegResponse String - Vehicle data
	 * @return char
	 */
	public char getJunkIndi(String asMfTtlRegResponse)
	{
		char lcJunkIndi = '0';

		if (getNoOfRecs() == 1)
		{
			//	Set MultipleReg boolean true for MultRegis data
			boolean lbMultipleReg = false;
			lcJunkIndi =
				caMfAccess.getMultipleRegJunkIndi(
					asMfTtlRegResponse,
					lbMultipleReg);
		}

		return lcJunkIndi;
	}

	/**
	 * Get Multiple Records from Archive.
	 *  
	 * @param aaData Object
	 * @return Object 
	 */
	public Object getMultiRecsFromArchive(Object aaData)
	{
		VehicleInquiryData caInqData = (VehicleInquiryData) aaData;
		int liPrintOptions = caInqData.getPrintOptions();
		Vector lvPartialDataList = caInqData.getPartialDataList();
		MFPartialData laPartialData1 = null;
		MFPartialData laPartialData2 = null;
		Vector lvSpclPltPDL = caInqData.getPartialSpclPltsDataList();
		MFPartialSpclPltData laPartialSpclPltData1 = null;
		MFPartialSpclPltData laPartialSpclPltData2 = null;

		if (lvPartialDataList.size() > 0
			&& lvPartialDataList.firstElement()
				instanceof RegistrationData)
		{
			caInqData = manageMultRegs(caInqData);
		}
		// defect 9086
		//	Remove duplicate SpclRegIds from SpecialPlatesRegisData
		else if (
			(lvSpclPltPDL.size() > 0
				&& lvSpclPltPDL.firstElement()
					instanceof SpecialPlatesRegisData))
		{
			int liSize = lvSpclPltPDL.size();
			// Loop to remove duplicate SpclRegId's
			for (int i = 0; i < liSize; i++)
			{
				laPartialSpclPltData1 =
					(MFPartialSpclPltData) lvSpclPltPDL.get(i);
				for (int j = i + 1; j < liSize; j++)
				{
					laPartialSpclPltData2 =
						(MFPartialSpclPltData) lvSpclPltPDL.get(i + 1);
					if (laPartialSpclPltData1.getSpclRegId()
						== laPartialSpclPltData2.getSpclRegId())
					{
						lvSpclPltPDL.remove(i + 1);
						liSize = liSize - 1;
					}
				}
			}

			// If only one record remains, get complete record from 
			//	mainframe.
			if (liSize == 1)
			{
				//MfAccess laMFA = new MfAccess(csClientHost);
				caSearchInfo.setKey1(CommonConstant.SPCL_REG_ID);
				// Pad zeros to the left, character length = 9
				caSearchInfo.setKey2(
					UtilityMethods.addPadding(
						String.valueOf(
							laPartialSpclPltData1.getSpclRegId()),
						9,
						CommonConstant.STR_ZERO));
				// Retrieve Special Plate record by SpclRegId.
				// Move SpclPltRegis data object to MfVehicleData object
				VehicleInquiryData laInqData =
					retrieveSpclPltRegisResponse(caSearchInfo);
				// defect 9086
				//	Change getSpclPlatesReg() to 
				//	getSpclPltRegisData()
				caInqData.getMfVehicleData().setSpclPltRegisData(
					laInqData.getMfVehicleData().getSpclPltRegisData());
				// end defec 9086
			}
			else
			{
				caInqData.setNoMFRecs(liSize);
			}
		}
		else
		{
			int liSize = lvPartialDataList.size();
			for (int i = 0; i < liSize; i++)
			{
				laPartialData1 =
					(MFPartialData) lvPartialDataList.get(i);

				// Get docNo from each list
				//	If they match remove the second record from vector
				for (int j = i + 1; j < liSize; j++)
				{
					laPartialData2 =
						(MFPartialData) lvPartialDataList.get(i + 1);
					if (laPartialData1
						.getDocNo()
						.equals(laPartialData2.getDocNo()))
					{
						lvPartialDataList.remove(i + 1);
						liSize = liSize - 1;
					}
				}
			}

			// If one record exist then get that record by DocNo
			if (liSize == 1)
			{
				MfAccess laMFA = new MfAccess(csClientHost);
				caSearchInfo.setKey1(CommonConstant.DOC_NO);
				caSearchInfo.setKey2(laPartialData1.getDocNo());
				caSearchInfo.setKey3(
					(String) caInqData.getValidationObject());
				caInqData =
					laMFA.retrieveVehicleFromArchive(caSearchInfo);
				lvPartialDataList = caInqData.getPartialDataList();
				if (lvPartialDataList.size() > 0
					&& lvPartialDataList.firstElement()
						instanceof RegistrationData)
				{
					caInqData = manageMultRegs(caInqData);
				}
			}
			else
			{
				caInqData.setNoMFRecs(liSize);
			}

			if (caInqData.getNoMFRecs() == 1)
			{
				caInqData.setMultiArchiveStatus(
					CommonConstant.MULTI_SINGLE);
			}
			else if (caInqData.getNoMFRecs() > 1)
			{
				caInqData.setMultiArchiveStatus(
					CommonConstant.MULTI_MULTI);
			}
			else
			{
				caInqData.setMultiArchiveStatus(0);
			}
		}

		caInqData.setPrintOptions(liPrintOptions);
		return caInqData;
	}

	/**
	 * Get Multiple Records from Archive.
	 *  
	 * @param aaInqData VehicleInquiryData
	 * @return VehicleInquiryData 
	 */
	public VehicleInquiryData getMultRecsFromArchive(VehicleInquiryData aaInqData)
	{
		int liPrintOptions = aaInqData.getPrintOptions();
		Vector lvPartialDataList = aaInqData.getPartialDataList();
		MFPartialData laPartialData1 = null;
		MFPartialData laPartialData2 = null;
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;

		if (lvPartialDataList.size() > 0
			&& lvPartialDataList.firstElement()
				instanceof RegistrationData)
		{
			aaInqData = manageMultRegs(aaInqData);
		}
		else
		{
			int liSize = lvPartialDataList.size();
			for (int i = 0; i < liSize; i++)
			{
				laPartialData1 =
					(MFPartialData) lvPartialDataList.get(i);
				for (int j = i + 1; j < liSize; j++)
				{
					laPartialData2 =
						(MFPartialData) lvPartialDataList.get(i + 1);
					if (laPartialData1
						.getDocNo()
						.equals(laPartialData2.getDocNo()))
					{
						lvPartialDataList.remove(i + 1);
						liSize = liSize - 1;
					}
				}
			}

			if (liSize == 1)
			{
				System.out.println("Size is now 1");
				MfAccess laMFA = new MfAccess(csClientHost);
				caSearchInfo.setKey1(CommonConstant.DOC_NO);
				caSearchInfo.setKey2(laPartialData1.getDocNo());
				// defect 9502
				// why were we ever doing this?
				//caSearchInfo.setKey3(
				//	(String) aaInqData.getValidationObject());
				// end defect 9502

				// defect 9086
				// access new MF Vehicle call
				//	aaInqData =
				//		laMFA.retrieveVehicleFromArchive(caSearchInfo);
				try
				{
					lsMfResponse =
						laMFA.retrieveVehicleFromMF(caSearchInfo);
					setNoOfRecs(rtnNoOfRecsMF(lsMfResponse));
					// Get MultRegis and Junk records
					// move mf response data into VehInq data objects
					caInqData =
						retrieveMultRegisJunkResponse(
							lsMfResponse,
							caSearchInfo);
					// get special plate regis data
					caInqData =
						retrieveSpclPltRegisData(
							caInqData,
							caSearchInfo);
					// end defect 9086	
				}
				catch (RTSException aeRTSEx)
				{
					aaInqData.setMfDown(1);
				}

				lvPartialDataList = aaInqData.getPartialDataList();
				if (lvPartialDataList.size() > 0
					&& lvPartialDataList.firstElement()
						instanceof RegistrationData)
				{
					aaInqData = manageMultRegs(aaInqData);
					// defect 9502
				}
				// originally added but then backed off.
				//				else if (lvPartialDataList.size() == 1
				//				&& lvPartialDataList.firstElement()
				//				instanceof RegistrationData)
				//				{
				//					System.out.println("PDL is down to one element");
				//					// new section to handle plate CZP445
				//					aaInqData = manageMultRegs(aaInqData);
				//					// force multi-multi
				//					aaInqData.setNoMFRecs(2);
				//					// end defect 9502
				//				}
				// end defect 9502
			}
			else
			{
				aaInqData.setNoMFRecs(liSize);
			}

			if (aaInqData.getNoMFRecs() == 1)
			{
				aaInqData.setMultiArchiveStatus(
					CommonConstant.MULTI_SINGLE);
			}
			else if (aaInqData.getNoMFRecs() > 1)
			{
				aaInqData.setMultiArchiveStatus(
					CommonConstant.MULTI_MULTI);
			}
			else
			{
				aaInqData.setMultiArchiveStatus(0);
			}
		}

		aaInqData.setPrintOptions(liPrintOptions);
		return aaInqData;
	}

	/**
	 * Check for Multiple Regis
	 *  
	 * @param asMfTtlRegResponse String - Vehicle data
	 * @return char
	 */
	public char getMultRegisIndi(String asMfTtlRegResponse)
	{
		char lcMultipleRegIndi = '0';

		if (getNoOfRecs() == 1)
		{
			//	Set MultipleReg boolean true for MultRegis data
			boolean lbMultipleReg = true;
			lcMultipleRegIndi =
				caMfAccess.getMultipleRegJunkIndi(
					asMfTtlRegResponse,
					lbMultipleReg);
		}

		return lcMultipleRegIndi;
	}

	/**
	 * Get Number Of Records
	 *
	 * @return int
	 */
	private int getNoOfRecs()
	{
		return ciNoOfRecs;
	}

	/**
	 * Get Number of Retries
	 *
	 * @return int
	 */
	private int getTotalNoOfRetries()
	{
		return siTotalRetries;
	}

	/**
	 * Update Vehicle Inquiry Data object with Vector of 
	 *  In Process Transaction Data Objects where exist 
	 * 
	 * @param laVehInqData
	 */
	public void updtInProcsTrans(
		VehicleInquiryData laVehInqData,
		GeneralSearchData aaGSD)
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			Transaction laTransSQL = new Transaction(laDBAccess);
			laDBAccess.beginTransaction();

			Vector lvInProcsTrans =
				laTransSQL.qryInProcessTransaction(aaGSD);

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);

			Vector lvInProcsFinal = new Vector();

			for (int i = 0; i < lvInProcsTrans.size(); i++)
			{
				InProcessTransactionData laPTData =
					(
						InProcessTransactionData) lvInProcsTrans
							.elementAt(
						i);

				if (laPTData.getTransPostedMfIndi() == 0)
				{
					lvInProcsFinal.add(laPTData);
				}
				else
				{
					GeneralSearchData laGSD = new GeneralSearchData();

					laGSD.setKey1(laPTData.getTransId());

					MfAccess laMFA = new MfAccess(csClientHost);
					
					// Do Not Add to Vector if already Processed by MF
					try
					{
						if (laMFA.voidTransactions(laGSD) != 0)
						{
							lvInProcsFinal.add(laPTData);
						}
					}
					catch (RTSException aeRTSEx)
					{
					}
				}

			}

			laVehInqData.setInProcsTransDataList(lvInProcsFinal);
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (Exception aeEx)
			{
			}
		}

	}
	/**
	 * Update Vehicle Inquiry Data object with Vector of 
	 *  In Process Transaction Data Objects where exist 
	 * 
	 * @param laVehInqData
	 */
	public void updtFraudData(VehicleInquiryData laVehInqData)
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			FraudLog laFraudLogSQL = new FraudLog(laDBAccess);
			
			laDBAccess.beginTransaction();

			FraudStateData laFraudData =
				laFraudLogSQL.qryFraudCdForDocNo(
					laVehInqData
						.getMfVehicleData()
						.getTitleData()
						.getDocNo());

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);

			laVehInqData.setFraudStateData(laFraudData);
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (Exception aeEx)
			{
			}
		}

	}

	/**
	 * main entry point for getting vehicle from mainframe.
	 *  
	 * @return VehicleInquiryData
	 * @param aaInput Object
	 */
	public VehicleInquiryData getVeh(Object aaInput)
	{
		VehicleInquiryData laReturnVehInqData = null;

		if (aaInput instanceof Vector)
		{
			caTmpInqData =
				(VehicleInquiryData) ((Vector) aaInput).get(0);
			caSearchInfo =
				(GeneralSearchData) ((Vector) aaInput).get(1);
		}
		else
		{
			caSearchInfo = (GeneralSearchData) aaInput;
		}
		// defect 10598 
		String lsTransCd = caSearchInfo.getKey3();
		// end defect 10598 
		// defect 10608
		// defect 10610
		//		if (caSearchInfo.getKey6() != null
		//			&& caSearchInfo.getKey6().equals("IRENEW"))
		//		{
		//			lsTransCd = caSearchInfo.getKey6();
		//		}
		if (caSearchInfo.getKey6() != null)
		{
			if (caSearchInfo.getKey6().equals(TransCdConstant.IRENEW)
				|| caSearchInfo.getKey6().equals(TransCdConstant.IADDRE))
			{
				lsTransCd = caSearchInfo.getKey6();
			}
		}
		// end defect 10610
		// end defect 10608

		if (caSearchInfo.getIntKey2()
			== CommonConstant.SEARCH_ACTIVE_INACTIVE)
		{
			caInqData = searchMfActiveInact(caSearchInfo);

			// defect 9471
			// should only if there is just one rec..
			//if (caInqData.getNoMFRecs() >= 1)
			//{
			// defect 9691
			// Only verify insurance if there is one record
			if (caInqData.getNoMFRecs() == 1)
			{
				// defect 10771 
				// Use lsTransCd to include IRENEW (not Key3) 
				// verifyInsurance(caSearchInfo.getKey3(), caInqData);
				verifyInsurance(lsTransCd, caInqData);
				// end defect 10771 
			}
			// end defect 9691
			// end defect 9471
		}
		else if (
			caSearchInfo.getIntKey2() == CommonConstant.SEARCH_ARCHIVE)
		{
			caInqData = searchMfArchive(caSearchInfo);
			// defect 9644
			// Also do insurance verify after archive lookup
			verifyInsurance(caSearchInfo.getKey3(), caInqData);
			// end defect 9644			
		}
		// defect 9086
		//	SEARCH_SPECIAL_PLATES = 2, lookup SP record
		else if (
			caSearchInfo.getIntKey2()
				== CommonConstant.SEARCH_SPECIAL_PLATES)
		{
			caInqData = searchMfActiveInact(caSearchInfo);
		}
		// end defect 9086

		if (aaInput instanceof GeneralSearchData)
		{
			laReturnVehInqData = caInqData;
		}
		else
		{
			if (cbReturnIncomingVehInqData)
			{
				laReturnVehInqData = caTmpInqData;
			}
			else
			{
				laReturnVehInqData = caInqData;
			}
		}
		// defect 10598
		if (!UtilityMethods.isEmpty(lsTransCd))
		{
			try
			{
				TransactionCodesData laTransCdData =
					TransactionCodesCache.getTransCd(lsTransCd);

				if (laTransCdData != null
					&& laTransCdData.isPndngTransLookup())
				{
					GeneralSearchData laGSD = new GeneralSearchData();

					boolean lbContinue = false;

					if (laReturnVehInqData.getNoMFRecs() == 1
						|| (laReturnVehInqData.getNoMFRecs() > 1
							&& laReturnVehInqData
								.getPartialDataList()
								.size()
								> 0
							&& laReturnVehInqData
								.getPartialDataList()
								.elementAt(
								0)
								instanceof RegistrationData))
					{
						lbContinue = true;
						laGSD.setKey1("DOCNO");
						laGSD.setKey2(
							laReturnVehInqData
								.getMfVehicleData()
								.getTitleData()
								.getDocNo());
					}
					else if (
						laReturnVehInqData.getNoMFRecs() == 0
							&& caSearchInfo.getKey1().equals("VIN"))
					{
						lbContinue = true;
						laGSD =
							(GeneralSearchData) UtilityMethods.copy(
								caSearchInfo);
					}
					if (lbContinue)
					{
						updtInProcsTrans(laReturnVehInqData, laGSD);

					}
				}
			}
			catch (RTSException aeRTSEx)
			{

			}
			// defect 10865 
			// defect 10974 
			// Check for Nulls (On Mult Titles, MfVehicleData is null) 
			   if (lsTransCd.equals(TransCdConstant.CCO)
			    && laReturnVehInqData.getNoMFRecs() != 0
			    && laReturnVehInqData.getMfVehicleData() != null
			    && laReturnVehInqData.getMfVehicleData().getTitleData()
			     != null
			    && !UtilityMethods.isEmpty(
			     laReturnVehInqData
			      .getMfVehicleData()
			      .getTitleData()
			      .getDocNo()))
			   {
			    // end defect 10974 
				updtFraudData(laReturnVehInqData);
			}
			// end defect 10865 
		}
		// end defect 10598 

		return laReturnVehInqData;
	}

	/**
	 * Retrieve Special Plate Regis Data from DB 
	 * 
	 * @param asOrigRegPltNo	String
	 * @return VehicleInquiryData
	 */
	private VehicleInquiryData getSpclPltRegisFromDB(String asOrigRegPltNo)
	{
		VehicleInquiryData laVehInqData = new VehicleInquiryData();
		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			SpecialRegistrationFunctionTransaction laSRTransQry =
				new SpecialRegistrationFunctionTransaction(laDBA);
			SpecialPlatesRegisData laSpclPltRegisData =
				laSRTransQry.qrySRFuncTransForSpclPltApp(
					asOrigRegPltNo);
			laDBA.endTransaction(DatabaseAccess.COMMIT);
			laDBA = null;
			if (laSpclPltRegisData != null)
			{
				MFVehicleData laMFVehData = new MFVehicleData();
				laMFVehData.setSpclPltRegisData(laSpclPltRegisData);
				// TODO 
				// This is necessary for Vehicle Inquiry
				laMFVehData.setSPRecordOnlyVehInq(true);
				laVehInqData.setMfVehicleData(laMFVehData);
				laVehInqData.setNoMFRecs(1);
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				CommonConstant.MSG_FAILED_DB_CALL);
		}
		finally
		{
			if (laDBA != null)
			{
				try
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}
				catch (Exception aeRTSEx)
				{
				}
				laDBA = null;
			}
		}
		return laVehInqData;
	}
	/**
	 * Get registration data values in partial data and put in 
	 * RegistrationData object.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @param aaRegData RegistrationData
	 * @return RegistrationData 
	 */
	private RegistrationData handleRegData(
		VehicleInquiryData aaVehInqData,
		RegistrationData aaRegData)
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		// defect 10034
		//	Copy PltBirthDate to returning RegistrationData
		laRegData.setPltBirthDate(aaRegData.getPltBirthDate());
		// end defect 10034
		laRegData.setDpsSaftySuspnIndi(
			aaRegData.getDpsSaftySuspnIndi());
		laRegData.setHvyVehUseTaxIndi(aaRegData.getHvyVehUseTaxIndi());
		laRegData.setNotfyngCity(aaRegData.getNotfyngCity());
		laRegData.setPrevExpMo(aaRegData.getPrevExpMo());
		laRegData.setPrevExpYr(aaRegData.getPrevExpYr());
		laRegData.setPrevPltNo(aaRegData.getPrevPltNo());
		laRegData.setRecpntName(aaRegData.getRecpntName());
		laRegData.setRegClassCd(aaRegData.getRegClassCd());
		laRegData.setRegEffDt(aaRegData.getRegEffDt());
		laRegData.setRegExpMo(aaRegData.getRegExpMo());
		laRegData.setRegExpYr(aaRegData.getRegExpYr());
		laRegData.setRegIssueDt(aaRegData.getRegIssueDt());
		// defect 8901
		// laRegData.setRegPltAge(aaRegData.getRegPltAge());
		laRegData.setRegPltAge(aaRegData.getRegPltAge(false));
		// end defect 8901
		// defect 9312
		//	Add SpclRegisId to regis data
		laRegData.setSpclRegId(aaRegData.getSpclRegId());
		// end defect 9312
		laRegData.setRegPltCd(aaRegData.getRegPltCd());
		laRegData.setRegPltNo(aaRegData.getRegPltNo());
		laRegData.setRegPltOwnrName(aaRegData.getRegPltOwnrName());
		laRegData.setRegStkrCd(aaRegData.getRegStkrCd());
		laRegData.setRegStkrNo(aaRegData.getRegStkrNo());
		laRegData.setRenwlMailRtrnIndi(
			aaRegData.getRenwlMailRtrnIndi());
		laRegData.setResComptCntyNo(aaRegData.getResComptCntyNo());
		laRegData.setTireTypeCd(aaRegData.getTireTypeCd());
		laRegData.setVehGrossWt(aaRegData.getVehGrossWt());
		laRegData.setRegInvldIndi(aaRegData.getRegInvldIndi());
		laRegData.setRegHotCkIndi(aaRegData.getRegHotCkIndi());
		laRegData.setPltSeizedIndi(aaRegData.getPltSeizedIndi());
		laRegData.setStkrSeizdIndi(aaRegData.getStkrSeizdIndi());
		laRegData.setRenwlMailAddr(aaRegData.getRenwlMailAddr());
		laRegData.setCustActlRegFee(aaRegData.getCustActlRegFee());
		// defect 9631
		//laRegData.setCustBaseRegFee(aaRegData.getCustBaseRegFee());
		//laRegData.setCustDieselFee(aaRegData.getCustDieselFee());
		// end defect 9631
		
		// defect 10903 
		laRegData.setRecpntEMail(aaRegData.getRecpntEMail());
		// end defect 10903 
		
		// defect 10508 
		laRegData.setEMailRenwlReqCd(aaRegData.getEMailRenwlReqCd());
		// end defect 10508 
		laRegData.setRegRefAmt(aaRegData.getRegRefAmt());
		return laRegData;
	}
	/**
	 * Initialize 
	 */
	private void initialize()
	{ // Reset timestamp each day for retries accumulator
		if (saTimeStmp == null
			|| saTimeStmp.compareTo(RTSDate.getCurrentDate()) == 1)
		{
			saTimeStmp = RTSDate.getCurrentDate();
			setTotalNoOfRetries(0);
			siGetRecAttempts = 1;
		}

	}
	
	/**
	 * Make the call to TexasSure
	 * 
	 * <p>This method is public to allow testing.
 * 
	 * @param lsResult
	 * @param lsDocNo
	 * @param lsVin
	 * @param lsPlate
	 * @param laTSC
	 * @param laThread
	 * @param ilTimeout
	 * @return
	 * @throws InterruptedException
	 */
	public String makeTexasSureCall(String lsResult, String lsDocNo, String lsVin, String lsPlate, TexasSureCall laTSC, Thread laThread, int ilTimeout, int ilSleepMillis) throws InterruptedException
	{
		laTSC.setTestSleepMillis(ilSleepMillis);
		laTSC.setDocNo(lsDocNo);
		laTSC.setPlate(lsPlate);
		laTSC.setVin(lsVin);
		laTSC.setTestSwitch("");
		laThread.setDaemon(true);
		
		// Make the call
		laThread.start();
		laThread.join(ilTimeout);
		laThread.interrupt();
		
		if (laTSC != null)
		{
			lsResult = laTSC.getResult();
		}
		laThread = null;
		laTSC = null;
		return lsResult;
	}	
	
	/**
	 * Manage scenario where multiple records are found
	 * 
	 * @param aaInqData VehicleInquiryData
	 * @return VehicleInquiryData 
	 */
	public VehicleInquiryData manageMultRecs(VehicleInquiryData aaInqData)
	{
		Vector lvPDL = aaInqData.getPartialDataList();
		Vector lvSpclPltPDL = aaInqData.getPartialSpclPltsDataList();
		MFPartialSpclPltData laPartialSpclPltData1 = null;
		MFPartialSpclPltData laPartialSpclPltData2 = null;
		MFPartialData laPartialData1 = null;
		MFPartialData laPartialData2 = null;
		if ((lvPDL.size() > 0
			&& lvPDL.firstElement() instanceof RegistrationData))
		{
			aaInqData = manageMultRegs(aaInqData);
		} // defect 9086
		//	Remove duplicate SpclRegIds from SpecialPlatesRegisData
		else if (
			(lvSpclPltPDL.size() > 0
				&& lvSpclPltPDL.firstElement()
					instanceof MFPartialSpclPltData))
		{
			aaInqData = manageSpclMultRegs(aaInqData);
			//sort RegistrationData Vector by ExpMo/Yr
			UtilityMethods.sort(lvSpclPltPDL);
			int liSize = lvSpclPltPDL.size();
			// Loop to remove duplicate SpclRegId's
			for (int i = 0; i < liSize; i++)
			{
				laPartialSpclPltData1 =
					(MFPartialSpclPltData) lvSpclPltPDL.get(i);
				for (int j = i + 1; j < liSize; j++)
				{
					laPartialSpclPltData2 =
						(MFPartialSpclPltData) lvSpclPltPDL.get(i + 1);
					if (laPartialSpclPltData1.getSpclRegId()
						== laPartialSpclPltData2.getSpclRegId())
					{
						lvSpclPltPDL.remove(i + 1);
						liSize = liSize - 1;
					}
				}
			} // If only one record remains, get complete record from 
			//	mainframe.
			if (liSize == 1)
			{
				//MfAccess laMFA = new MfAccess(csClientHost);
				caSearchInfo.setKey1(CommonConstant.SPCL_REG_ID);
				// Pad zeros to the left, character length = 9
				caSearchInfo.setKey2(
					UtilityMethods.addPadding(
						String.valueOf(
							laPartialSpclPltData1.getSpclRegId()),
						9,
						CommonConstant.STR_ZERO));
				// Retrieve Special Plate record by SpclRegId.
				// Move SpclPltRegis data object to MfVehicleData object
				VehicleInquiryData laInqData =
					retrieveSpclPltRegisResponse(caSearchInfo);
				aaInqData.getMfVehicleData().setSpclPltRegisData(
					laInqData.getMfVehicleData().getSpclPltRegisData());
			}
			else
			{
				aaInqData.setNoMFRecs(liSize);
			}
		}
		else
		{
			int liSize = lvPDL.size();
			// Loop to remove duplicate DocNo's
			for (int i = 0; i < liSize; i++)
			{
				laPartialData1 = (MFPartialData) lvPDL.get(i);
				for (int j = i + 1; j < liSize; j++)
				{
					laPartialData2 = (MFPartialData) lvPDL.get(i + 1);
					if (laPartialData1
						.getDocNo()
						.equals(laPartialData2.getDocNo()))
					{
						lvPDL.remove(i + 1);
						liSize = liSize - 1;
					}
				}
			} // If only one record remains, get complete record from 
			//	mainframe.
			if (liSize == 1)
			{
				//MfAccess laMFA = new MfAccess(csClientHost);
				caSearchInfo.setKey1(CommonConstant.DOC_NO);
				caSearchInfo.setKey2(laPartialData1.getDocNo());
				// defect 9086
				//	New call to mainfarme, Special Plates
				//	aaInqData =
				//		laMFA.retrieveVehicleFromActiveInactive(
				//			caSearchInfo);
				String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
				try
				{
					lsMfResponse =
						caMfAccess.retrieveVehicleFromMF(caSearchInfo);
					setNoOfRecs(rtnNoOfRecsMF(lsMfResponse));
					// Get MultRegis and Junk records
					aaInqData =
						retrieveMultRegisJunkResponse(
							lsMfResponse,
							caSearchInfo);
				}
				catch (RTSException aeRTSEx)
				{
					aaInqData.setMfDown(1);
				} // end defect 9086
				lvPDL = aaInqData.getPartialDataList();
				// Check for multiple Regis records
				//	Set record count in manageMultRegs()
				if (lvPDL.size() > 0
					&& lvPDL.firstElement() instanceof RegistrationData)
				{
					aaInqData = manageMultRegs(aaInqData);
					// defect 7746
					// copy over the owner id 
					if (aaInqData.getOwnerId() == null
						&& caSearchInfo.getKey4() != null)
					{
						aaInqData.setOwnerId(caSearchInfo.getKey4());
					} // end defect 7746
				}
			}
			else
			{
				aaInqData.setNoMFRecs(liSize);
			}
		}
		return aaInqData;
	}
	/**
	 * Manage multiple regs.
	 *   1# Sort trans vector 
	 *   2# based on transCd - Return current RegisRec only
	 *   3# Set number of records
	 * 
	 * @param aaInqData VehicleInquiryData
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData manageMultRegs(VehicleInquiryData aaInqData)
	{
		Vector lvPDL = aaInqData.getPartialDataList();

		if (!caSearchInfo.getKey3().equals(TransCdConstant.CORREG)
			&& !caSearchInfo.getKey3().equals(TransCdConstant.TAWPT)
			&& !caSearchInfo.getKey3().equals(TransCdConstant.PAWT)
			&& !caSearchInfo.getKey3().equals(TransCdConstant.VEHINQ)
			&& !caSearchInfo.getKey3().equals(TransCdConstant.REFUND)
			&& !caSearchInfo.getKey3().equals(TransCdConstant.STAT)
			&& !caSearchInfo.getKey3().equals(TransCdConstant.EXCH))
		{
			System.out.println("ManageMultRegs");
			//sort RegistrationData Vector by ExpMo/Yr
			UtilityMethods.sort(lvPDL);
			//Place the most recent ExpMo/Yr RegistrationData in the 
			//	MfVehicleObject
			aaInqData.getMfVehicleData().setRegData(
				handleRegData(
					aaInqData,
					(RegistrationData) lvPDL.get(0)));
			aaInqData.setNoMFRecs(1);
			// defect 9312
			// Make call to get SpclRegisData if SpclRegId exist
			if (aaInqData
				.getMfVehicleData()
				.getRegData()
				.getSpclRegId()
				> 0)
			{
				// Update GSD
				caSearchInfo.setKey1(CommonConstant.SPCL_REG_ID);
				caSearchInfo.setKey2(
					String.valueOf(
						aaInqData
							.getMfVehicleData()
							.getRegData()
							.getSpclRegId()));
				// Make call to get Special Plates record.
				VehicleInquiryData laVehInq =
					retrieveSpclPltRegisResponse(caSearchInfo);
				// Move SpclRegData to VehInqData
				aaInqData.getMfVehicleData().setSpclPltRegisData(
					laVehInq.getMfVehicleData().getSpclPltRegisData());
			} // end defect 9312
		}
		else
		{
			UtilityMethods.sort(lvPDL);
			// defect 9691
			// Populate RegData so can verify insurance for multi regis.
			// Insurance verify is only done for Inquiry and Renew. 
			// Only need to do for Inquiry here because if this is a 
			// Renew returning multi regis, the above 'If block' would 
			// have already stripped it down to one record and the 
			// normal 'NoMFRecs = 1' verifyInsurance() in getVeh() will 
			// handle it.	
			if (caSearchInfo.getKey3().equals(TransCdConstant.VEHINQ))
			{
				aaInqData.getMfVehicleData().setRegData(
					handleRegData(
						aaInqData,
						(RegistrationData) lvPDL.get(0)));
				verifyInsurance(caSearchInfo.getKey3(), aaInqData);
			} // end defect 9691			
			aaInqData.setNoMFRecs(lvPDL.size());
		}

		return aaInqData;
	}
	/**
	 * Manage multiple regs
	 * 
	 * @return VehicleInquiryData
	 * @param aaInqData VehicleInquiryData
	 */
	public VehicleInquiryData manageSpclMultRegs(VehicleInquiryData aaInqData)
	{
		Vector lvSpclPltPDL = aaInqData.getPartialSpclPltsDataList();
		//sort RegistrationData Vector by ExpMo/Yr
		UtilityMethods.sort(lvSpclPltPDL);
		aaInqData.setNoMFRecs(lvSpclPltPDL.size());
		aaInqData.setPartialDataList(lvSpclPltPDL);
		return aaInqData;
	}

	/**
	 * 
	 * Get MulrRegis and Junk data. Return VehicleInquiryData
	 * 
	 * @param asMfResponse	String
	 * @return VehicleInquiryData
	 */
	private VehicleInquiryData retrieveMultRegisJunkResponse(
		String asMfResponse,
		GeneralSearchData aaGSD)
	{
		//	Move businesws logic from MfAccess
		// Get mainframe Record from CICS programs 
		//		R28	> Search by DocNo
		//		R33	> Search by DocNo
		//
		//	Strings obtained from a MF call
		String lsMfResponse = asMfResponse;
		//	REGIS buffer
		String lsMfRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		//	Junk buffer
		String lsMfJunkResponse = CommonConstant.STR_SPACE_EMPTY;
		//	Partial (Multiple Title Rcords)
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;
		//	VINA buffer
		String lsMfVINAResponse = CommonConstant.STR_SPACE_EMPTY;
		//	SpecRegis buffer
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		//	CICS-COBOL Program Transaction Id
		String lsTransactionId = CommonConstant.STR_SPACE_EMPTY;
		// Special Plates Regis buffer
		String lsMfSpecialPltRegisResponse =
			CommonConstant.STR_SPACE_EMPTY;
		VehicleInquiryData laVehInqData = new VehicleInquiryData();
		// Bussiness layer logic remove from MfAccess
		if ((lsMfResponse != null)
			&& !(lsMfResponse.equals(CommonConstant.STR_SPACE_EMPTY)))
		{ // VehicleInquiry, Get MultRegis and Junk records
			// Test for 1 record that is not Special PLate
			if (getNoOfRecs() == 1)
			{
				// Remove header from MfResponse
				csMfTtlRegResponse =
					caMfAccess.getMfTtlRegResponse(lsMfResponse);
				try
				{
					if (getMultRegisIndi(csMfTtlRegResponse) == '1')
					{
						// Set KeyType = DOCNO, Key = DocNo
						aaGSD.setKey1(CommonConstant.DOC_NO);
						aaGSD.setKey2(
							MfAccess.setDocNoKey(csMfTtlRegResponse));
						// CICS PgmNo for MultRegis - R33
						lsTransactionId = MfAccess.R33;
						// Get MultRegis from mainframe						
						lsMfRegisResponse =
							caMfAccess.retrieveMfRecordsByDocNo(
								aaGSD,
								lsTransactionId);
					}
					if (getJunkIndi(csMfTtlRegResponse) == '1')
					{
						// Set KeyType = DOCNO, Key = DocNo
						aaGSD.setKey1(CommonConstant.DOC_NO);
						aaGSD.setKey2(
							MfAccess.setDocNoKey(csMfTtlRegResponse));
						// Note: Junk nevers searches archive
						int liHoldIntKey2 = aaGSD.getIntKey2();
						aaGSD.setIntKey2(0);
						// CICS PgmNo for Junk - R28
						lsTransactionId = MfAccess.R28;
						// Get Junk record from mainframe						
						lsMfJunkResponse =
							caMfAccess.retrieveMfRecordsByDocNo(
								aaGSD,
								lsTransactionId);
						// Reset IntKey2 to original value
						aaGSD.setIntKey2(liHoldIntKey2);
					}
				}
				catch (RTSException aeRTSEx)
				{
					laVehInqData.setMfDown(1);
				}
			} // Check for MFPartial
			if (getNoOfRecs() > 1)
			{
				lsMfPartialResponse = lsMfResponse;
				lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
			}
		} // Move data to thier appropriate data objects
		// If lsMfResponse is null or empty, an empty VehicleInquiry 
		//	data object will be returned.
		//
		//	lsMfResponse = Data string return from MF
		//	lsMfVINAResponse = VINA data string
		//	lsMfRegisResponse = Muitiple Regis
		//	lsMfSpecialOwnerResponse = Driver Eds
		if (laVehInqData.getMfDown() != 1)
		{
			laVehInqData =
				setVehicleInquiryDataFromMfResponse(
					lsMfResponse,
					lsMfVINAResponse,
					lsMfRegisResponse,
					lsMfSpecialOwnerResponse,
					lsMfJunkResponse,
					lsMfPartialResponse,
					lsMfSpecialPltRegisResponse);
		} // defect 9086
		//	Check for Special Plates from MultRegisData
		laVehInqData = searchMultRegisSpclPltData(laVehInqData);
		// end defect 9086
		return laVehInqData;
	}
	/**
	 * Get Special Plate Regis record
	 * There must be a Regis Record and a SPID
	 * 
	 * @param aaInqData
	 * @param aaGSD
	 * @return VehicleInquiryData  
	 */
	public VehicleInquiryData retrieveSpclPltRegisData(
		VehicleInquiryData aaInqData,
		GeneralSearchData aaGSD)
	{
		VehicleInquiryData laVehInqData = aaInqData;
		GeneralSearchData laGSD = aaGSD;
		String lsKeyType = laGSD.getKey1();
		int liTier = laGSD.getIntKey2();
		boolean lbSearchSP = false;
		// defect 9858 
		// Need to save for potential query against SR_FUNC_TRANS 
		String lsOrigRegPltNo = laGSD.getKey2();
		// end defect 9858 
		// Tier = 2, Special Plate Lookup
		// test for TransCd = SP
		//	TTL/REGIS records could exist if TransCd = SPAPPL
		if (liTier == CommonConstant.SEARCH_SPECIAL_PLATES)
		{
			lbSearchSP = true;
		} // Set SpclRegId = 9 digits
		// Check GSD for Special Plate lookup by SpclRegId
		//	TTL/REGIS records exist
		if (lsKeyType.equals(CommonConstant.SPCL_REG_ID))
		{
			// Move SpclRegId to GSD search key
			// The search key must be 9 characters
			laGSD.setKey2(
				UtilityMethods.addPadding(
					String.valueOf(laGSD.getKey2()),
					9,
					CommonConstant.STR_ZERO));
			lbSearchSP = true;
		} // Set SpclRegId = 9 digits
		// Record found in TTL/REGIS and SpclRegId exist
		if (getNoOfRecs() == 1)
		{
			if (laVehInqData
				.getMfVehicleData()
				.getRegData()
				.getSpclRegId()
				> 0)
			{
				int liSpclRegId =
					laVehInqData
						.getMfVehicleData()
						.getRegData()
						.getSpclRegId();
				// Move SpclRegId to GSD search key
				// The search key must be 9 characters
				laGSD.setKey2(
					UtilityMethods.addPadding(
						String.valueOf(liSpclRegId),
						9,
						CommonConstant.STR_ZERO));
				// Set KeyType to SPCLREGID for header key
				laGSD.setKey1(CommonConstant.SPCL_REG_ID);
				lbSearchSP = true;
			}
		}

		if (lbSearchSP)
		{
			// TODO Why are we using different object   (KPH)  
			//   laInqData vs. laVehInqData
			// Retrieve Special Plate record.
			// Move SpclPltRegis data object to MfVehicleData object
			VehicleInquiryData laInqData =
				retrieveSpclPltRegisResponse(laGSD);
			// MF down, move data to return object
			if (laInqData.getMfDown() == 1)
			{
				laVehInqData = laInqData;
			} // defect 9858
			if (laInqData.getNoMFRecs() == 0)
			{ // laGSD.getKey3() is the TransCd 
				if (UtilityMethods
					.isTransCdValidForSRFuncLookup(laGSD.getKey3()))
				{
					//	Search DB2 for Special Plate Applications
					laInqData = getSpclPltRegisFromDB(lsOrigRegPltNo);
				}
			} // end defect 9858
			//	Set Special Plates Partials object 
			if (laInqData.getNoMFRecs() > 1)
			{
				laVehInqData.setPartialSpclPltsDataList(
					laInqData.getPartialSpclPltsDataList());
			}
			else if (laInqData.getNoMFRecs() == 1)
			{
				if (laVehInqData.getMfVehicleData() == null)
				{
					// Create a new graph of MfVehicleData
					//	This will ensure that a MfVehicleData exist 
					//	when moving SpclPltRegis data to Vehicle data 
					//	object
					laVehInqData.setMfVehicleData(
						createVehInqDataObj());
				}
				laVehInqData.getMfVehicleData().setSpclPltRegisData(
					laInqData.getMfVehicleData().getSpclPltRegisData());
				// defect 9858
				laVehInqData.getMfVehicleData().setSPRecordOnlyVehInq(
					laInqData
						.getMfVehicleData()
						.isSPRecordOnlyVehInq());
				laVehInqData.setNoMFRecs(1);
				laVehInqData.setMfDown(0);
			} // end defect 9858
		}
		return laVehInqData;
	}
	/**
	 * Retrieve Special Plate Regis Data
	 * 
	 * @param aaGSD	GeneralSearchData
	 * @return VehicleInquiryData
	 */
	public VehicleInquiryData retrieveSpclPltRegisResponse(GeneralSearchData aaGSD)
	{
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfSpclPltRegisResponse =
			CommonConstant.STR_SPACE_EMPTY;
		VehicleInquiryData laVehInqData = new VehicleInquiryData();
		try
		{ // Get Special Plate Regis record
			// Throw RTSException if MF down
			if (aaGSD.getKey1().equals(CommonConstant.SPCL_REG_ID))
			{
				lsMfSpclPltRegisResponse =
					caMfAccess.retrieveVehicleFromMF(aaGSD);
			}
			else
			{
				// defect 9086
				//	Added call for converting plate number i's 
				//	and o's to 1's and 0's. 
				String lsKey = aaGSD.getKey2();
				lsKey =
					CommonValidations.convert_i_and_o_to_1_and_0(lsKey);
				aaGSD.setKey2(lsKey);
				// end defect 9086
				lsMfSpclPltRegisResponse =
					caMfAccess.retrieveMfRecordsByRegPltNo(
						aaGSD,
						MfAccess.R08);
			} // Get number of records for special plates
			int liNoOfSPRecs = 0;
			if (lsMfSpclPltRegisResponse != null
				&& !lsMfSpclPltRegisResponse.equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				liNoOfSPRecs = rtnNoOfRecsMF(lsMfSpclPltRegisResponse);
			} // Check for SpclPltMultRegis
			if (liNoOfSPRecs > 1)
			{
				lsMfPartialResponse = lsMfSpclPltRegisResponse;
			} // Move data to Special Plates data object
			laVehInqData =
				setVehicleInquiryDataFromMfResponse(
					lsMfResponse,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					lsMfPartialResponse,
					lsMfSpclPltRegisResponse);
			// Set No Of Records from SP for retrieveSpclPltRegisData()
			//	processing
			laVehInqData.setNoMFRecs(liNoOfSPRecs);
			// defect 10018 
			//   Restored 9390 
			// Call method to reset the AddlSetIndi as Military Plates,
			// etc. have changed PltSetImprtnceCd.  This code can be 
			// removed upon Statewide Release of 5.5.0 and MF conversion. 
			if (liNoOfSPRecs == 1)
			{
				correctSpclPltAddlSetIndi(laVehInqData);
			} // end defect 10018 
		} // RTSException thrown means MF down
		catch (RTSException aeRTSEx)
		{
			laVehInqData.setMfDown(1);
		}
		return laVehInqData;
	}
	/**
	 * Get Spec-Regis/Cancel Plate
	 * 
	 * @param asMfResponse	String
	 * @param asMfVINAResponse	String
	 * @param asMfRegisResponse	String
	 * @param asMfSpecialOwnerResponse	String
	 * @param asMfJunkResponse	String
	 * @param asMfPartialResponse	String
	 * @return VehicleInquiryData
	 */
	private VehicleInquiryData retrieveVehicleBySpecialOwner(GeneralSearchData aaGSD)
	{
		MfAccess laMFA = new MfAccess(csClientHost);
		VehicleInquiryData laInqData = new VehicleInquiryData();
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		// defect 9086
		//	Retrieve Spec-Regis/Cancel Plate 
		//	Business layer was move from MfAccess to 
		//	VehicleInquiry.
		// CICS PgmNo for Spec-Regis/Cancel Plates - R19
		try
		{ // Call SPEC-REGIS/CANCEL PLATES
			// Throw RTSException if MF down
			if (aaGSD.getKey1().equals(CommonConstant.REG_PLATE_NO))
			{
				String lsTransactionId = MfAccess.R19;
				lsMfResponse =
					laMFA.retrieveMfRecordsByRegPltNo(
						aaGSD,
						lsTransactionId);
			} // CICS PgmNo for Spec-Regis/Cancel Plates - R20
			else if (aaGSD.getKey1().equals(CommonConstant.OWNER_ID))
			{
				String lsTransactionId = MfAccess.R20;
				lsMfResponse =
					laMFA.retrieveMfRecordsByOwnrId(
						aaGSD,
						lsTransactionId);
			} // Remove header from MfResponse
			lsMfSpecialOwnerResponse =
				caMfAccess.getMfTtlRegResponse(lsMfResponse);
			// Move data to thier appropriate data objects
			// If lsMfResponse is null or empty, an empty VehicleInquiry 
			//	data object will be returned.
			laInqData =
				caMfAccess.setVehicleInquiryDataFromMfResponse(
					lsMfResponse,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					lsMfSpecialOwnerResponse,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY,
					CommonConstant.STR_SPACE_EMPTY);
		} // RTSException means MF is down
		catch (RTSException aeRTSEx)
		{
			laInqData.setMfDown(1);
		}

		return laInqData;
	}

	/**
	 * Retrieve VINA Only Data
	 * 
	 * @param aaGSD	GeneralSearchData
	 * @return VehicleInquiryData
	 */
	private VehicleInquiryData retrieveVINAResponse(GeneralSearchData aaGSD)
	{
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfVINAResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfRegisResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfSpecialOwnerResponse =
			CommonConstant.STR_SPACE_EMPTY;
		String lsMfJunkResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfPartialResponse = CommonConstant.STR_SPACE_EMPTY;
		String lsMfSpclPltRegisResponse =
			CommonConstant.STR_SPACE_EMPTY;
		VehicleInquiryData laVehInqData = new VehicleInquiryData();
		// Check whether Online VINA is needed
		if ((getNoOfRecs() == 0))
		{
			//In any case, get the VINA number
			String lsTransactionId = MfAccess.R10;
			// Get Special Plate Regis record
			lsMfVINAResponse =
				caMfAccess.retrieveMfRecordsByVIN(
					aaGSD,
					lsTransactionId);
		} // Move data 
		laVehInqData =
			setVehicleInquiryDataFromMfResponse(
				lsMfResponse,
				lsMfVINAResponse,
				lsMfRegisResponse,
				lsMfSpecialOwnerResponse,
				lsMfJunkResponse,
				lsMfPartialResponse,
				lsMfSpclPltRegisResponse);
		// defect 8984
		//	Set VINAExists based on the mainframe response 
		//	Check if there was a MF response and strip header and check 
		//	for data
		if (lsMfVINAResponse == null
			|| caMfAccess.getMfTtlRegResponse(lsMfVINAResponse).equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			laVehInqData.setVINAExists(false);
		}
		else
		{
			laVehInqData.setVINAExists(true);
		} // end defect 8984
		return laVehInqData;
	}
	/**
	 * Set Number Of records from MFAccess call
	 * 
	 * @param asMfResponse	String - Header/Vehicle data
	 * @return int
	 */
	private int rtnNoOfRecsMF(String asMfResponse)
	{
		int liNoOfRecs = 0;
		final int NO_OF_RECS_OFFSET = 145;
		final int NO_OF_RECS_LENGTH = 3;
		liNoOfRecs =
			Integer
				.valueOf(
					caMfAccess.getStringFromZonedDecimal(
						asMfResponse.substring(
							NO_OF_RECS_OFFSET,
							NO_OF_RECS_OFFSET + NO_OF_RECS_LENGTH)))
				.intValue();
		return liNoOfRecs;
	} // defect 10462 
	//	/**
	//	 * Set Number Of records from MFAccess call
	//	 * 
	//	 * @param asMfResponse	String - Header/Vehicle data
	//	 * @param aiOffSet	int - Record Offset
	//	 * @return int
	//	 */
	//	public int rtnNoOfRecsMF(String asMfResponse, int aiOffSet)
	//	{
	//		int liNoOfRecs = 0;
	//		final int NO_OF_RECS_OFFSET = aiOffSet;
	//		final int NO_OF_RECS_LENGTH = 3;
	//		liNoOfRecs =
	//			Integer
	//				.valueOf(
	//					caMfAccess.getStringFromZonedDecimal(
	//						asMfResponse.substring(
	//							NO_OF_RECS_OFFSET,
	//							NO_OF_RECS_OFFSET + NO_OF_RECS_LENGTH)))
	//				.intValue();
	//
	//		return liNoOfRecs;
	//	}
	// end defect 10462 
	//	/**
	//	 * Set MfTtlRegResponse by removing header data from MfResponce
	//	 * 
	//	 * @param asResponce	String - Header/Vehicle data
	//	 * @return String
	//	 */
	//	private String getTtlRegResponce(String asMfResponce)
	//	{
	//		// strip off header and return Title/Regis data
	//		csMfTtlRegResponse =
	//			caMfAccess.getMfTtlRegResponce(asMfResponce);
	//
	//		return csMfTtlRegResponse;
	//	}
	/**
	 * Search mainframe for active and inactive.
	 * If a record is not found, Special Plates is also searched.
	 * A search for owner is done if the search is by VIN and Title 
	 * type of trans.
	 *
	 * @return VehicleInquiryData
	 * @param aaGSD GeneralSearchData
	 */
	public VehicleInquiryData searchMfActiveInact(GeneralSearchData aaGSD)
	{
		//Pass to Marx's MF methods
		MfAccess laMFA = null;
		//MFVehicleData laMFVehicleData = null;
		OwnerData laOwnerData = new OwnerData();
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		// defect 9403
		//	Add additional request for MF data if RTSException returned
		//	from MfAccess()
		int liGetRecAttempts = 1;
		boolean lbRetry = true;
		while (lbRetry)
		{ // defect 9086
			// Moved business layer from MfAccess to VehicleInquiry class
			//	New
			//	 	MfAccess returns a string
			//		Set Number Of Records
			//		Get Multiple Regis and Junk Records
			//		Setup and make call to Special Plates Regis
			//		Move MF String data into there appropriate data objects
			//
			//caInqData = laMFA.retrieveVehicleFromActiveInactive(aaGSD);
			// Test: if Tier <> 2 (Special Plate lookup only)
			try
			{ // defect 10462 
				if (liGetRecAttempts > 1)
				{
					System.err.println(
						"Retrying: "
							+ aaGSD.getKey1()
							+ " "
							+ aaGSD.getKey2()
							+ " "
							+ aaGSD.getKey3());
				} // end defect 10462 
				laMFA = new MfAccess(csClientHost);
				if (aaGSD.getIntKey2()
					!= CommonConstant.SEARCH_SPECIAL_PLATES)
				{
					lsMfResponse = laMFA.retrieveVehicleFromMF(aaGSD);
					// lsMfResponse = caMfAccess.retrieveVehicleFromMF(aaGSD);
					if (lsMfResponse != null
						&& !lsMfResponse.equals(
							CommonConstant.STR_SPACE_EMPTY))
					{
						setNoOfRecs(rtnNoOfRecsMF(lsMfResponse));
						// Get MultRegis and Junk records
						caInqData =
							retrieveMultRegisJunkResponse(
								lsMfResponse,
								aaGSD);
					}
				} // end defect 9086
				/**************************************************************/ // Get Special Plate Regis record
				//	There must be a Regis Record and a SPID
				//	or the KeyType is SPCL_REG_ID
				caInqData = retrieveSpclPltRegisData(caInqData, aaGSD);
				/**************************************************************/
				lbRetry = false;
				// defect 9403
				// Log if retry was successful 
				if (liGetRecAttempts == 2)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Retry Connection successful: "
							+ csClientHost
							+ ":  "
							+ RTSDate.getCurrentDate()
							+ " - "
							+ (new RTSDate()).getClockTime());
				} // end defect 9403
			} // defect 9403
			// RTSException is thrown when MF IOException or NullPointer
			//	Exception is thrown. Incurmrnt retry counter
			catch (RTSException aeRTSEx)
			{
				try
				{
					Integer.parseInt(aeRTSEx.getMessage());
					// Write return code to log.
					// Add 1 to MF retrieve record count
					Log.write(
						Log.SQL_EXCP,
						this,
						"(VehicleInquiry) JavaGateway failed "
							+ "  ***  Retry record retrieval ");
				}
				catch (NumberFormatException aeNFEx)
				{ // Return error message
					// Do nothing
				} // Add 1 to MF retrieve record count
				liGetRecAttempts++;
			}
			// Set MfDown if 3 or more attempts were made to get MF record
			if (liGetRecAttempts > 2)
			{
				// Add to total number of retries failures
				setTotalNoOfRetries(liGetRecAttempts);
				Log.write(
					Log.SQL_EXCP,
					this,
					"Connection retry failed "
						+ csClientHost
						+ " "
						+ "MfKeys - "
						+ aaGSD.toString()
						+ ": As of "
						+ RTSDate.getCurrentDate()
						+ " "
						+ (new RTSDate()).getClockTime()
						+ " - Total retries failures "
						+ String.valueOf(getTotalNoOfRetries()));
				caInqData.setMfDown(1);
				lbRetry = false;
				liGetRecAttempts = 1;
			}
			if (liGetRecAttempts == 2)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"JavaGateway Connection retry: "
						+ csClientHost
						+ " "
						+ "MF search key - "
						+ aaGSD.getKey2());
			} // end defect 9403
			// end defect 9403
		} // end while
		// check for Partials (Multiple Records)
		if (caInqData.getPartialDataList().size() > 1
			|| caInqData.getPartialSpclPltsDataList().size() > 1)
		{
			// removes dup DocNo's or SpclRegId's
			//	set record new record count
			caInqData = manageMultRecs(caInqData);
			// defect 6709 change
			// execute checkForMultiRegisRecs only if an internet trans
			// and still more than one record returned from MF after 
			// manageMultRecs(cInqData); 
			// removes dup docno's

			// defect 10768 
			// Add WRENEW 
			// defect 9502
			// Add V21 to this processing
			if (aaGSD.getKey3().equals(TransCdConstant.INTERNET)
				|| aaGSD.getKey3().equals(TransCdConstant.WRENEW)
				|| aaGSD.getKey3().equals(TransCdConstant.V21VTN)
				|| aaGSD.getKey3().equals(TransCdConstant.G36VTN)
				|| aaGSD.getKey3().equals(TransCdConstant.V21PLD))
			{
				// end defect 9502
				// end defect 10768 

				if (caInqData.getNoMFRecs() > 1)
				{
					checkForMultRegisRecs(aaGSD, laMFA);
				}
				return caInqData;
			} // end defect 6709
		}
		// If MfVehicleData is empty, return NoOfRecords = 0
		// Set NoOfRecs to 0, Special Plates only
		else if (caInqData.getMfVehicleData() == null)
		{
			caInqData.setNoMFRecs(0);
		} //	If IntKey2 = SP lookup, you are done with MF Inquiry.
		//	Set true for Special Plate Only
		else if (
			!aaGSD.getKey3().equals(TransCdConstant.VEHINQ)
				&& aaGSD.getIntKey2()
					== CommonConstant.SEARCH_SPECIAL_PLATES)
		{
			// Set true if only Special Plate record return
			caInqData.getMfVehicleData().setSPRecordOnlyVehInq(true);
			// Set record count
			if (caInqData
				.getMfVehicleData()
				.getSpclPltRegisData()
				.getSpclRegId()
				> 0)
			{
				caInqData.setNoMFRecs(1);
			}
		}
		else
		{
			// Check if Internet Transaction
			if (aaGSD.getKey3().equals(TransCdConstant.INTERNET))
			{
				// we're done searching the mf for internet regis renewal
				// or internet address changes
				// Internet transaction NoOfRecs will always be greater
				//	than 0.
				caInqData.setNoMFRecs(1);
				return caInqData;
			}
			if (caInqData.getMfDown() != 1)
			{
				if (caInqData
					.getMfVehicleData()
					.getTitleData()
					.getDocNo()
					!= null
					&& caInqData
						.getMfVehicleData()
						.getTitleData()
						.getDocNo()
						.length()
						> 0)
				{
					// set number of records if there is a result
					caInqData.setNoMFRecs(1);
					if (aaGSD.getKey4() != null)
					{
						try
						{
							laOwnerData = laMFA.retrieveOwner(aaGSD);
							caInqData
								.getMfVehicleData()
								.setUserSuppliedOwnerData(
								laOwnerData);
						}
						catch (RTSException leRTSEx)
						{
							caInqData.setNoMFRecs(0);
						}
					}
				}
				else if (
					aaGSD.getKey1().equals(CommonConstant.REG_PLATE_NO)
						|| aaGSD.getKey1().equals(
							CommonConstant.SPCL_REG_ID))
				{
					// defect 9086
					// At this point we are No Record Found for Vehicle 
					//	data and search by RegPltNo. 
					if (aaGSD.getKey3().equals(TransCdConstant.VEHINQ))
					{
						// Make call to get SP record only.
						aaGSD.setIntKey2(
							CommonConstant.SEARCH_SPECIAL_PLATES);
						// If serach by SpclRegId, skip this call.
						//	MF has been previously searched.
						if (!aaGSD
							.getKey1()
							.equals(CommonConstant.SPCL_REG_ID))
						{
							/******************************************/ // Get Special Plate Regis record
							caInqData =
								retrieveSpclPltRegisData(
									caInqData,
									aaGSD);
							/******************************************/
						}

						// Test for Partials
						// check for Partials (Multiple Records)
						if (caInqData
							.getPartialSpclPltsDataList()
							.size()
							> 1)
						{
							// removes dup SpclRegId's
							caInqData = manageMultRecs(caInqData);
						} // Set record count
						else if (
							caInqData
								.getMfVehicleData()
								.getSpclPltRegisData()
								!= null
								&& caInqData
									.getMfVehicleData()
									.getSpclPltRegisData()
									.getSpclRegId()
									> 0)
						{
							caInqData.setNoMFRecs(1);
							caInqData
								.getMfVehicleData()
								.setSPRecordOnlyVehInq(
								true);
						}
					} // end defect 9086
					// Test for No Record Found and transaction is not 
					//	SPAPPL
					//	Search Spec-Regis/Cancel Plates file
					if (caInqData.getNoMFRecs() == 0
						&& !UtilityMethods.isSPAPPL(aaGSD.getKey3()))
					{
						// defect 9086
						//	Retrieve Spec-Regis/Cancel Plate 
						//	Business layer was move from MfAccess to 
						//	VehicleInquiry.
						//	caInqData =
						//		laMFA.retrieveVehicleBySpecialOwner(aaGSD);
						// CICS PGMNo for Spec-Regis/Cancel Plates - R19
						caInqData =
							retrieveVehicleBySpecialOwner(aaGSD);
						// end defect 9086
						if (caInqData
							.getMfVehicleData()
							.getRegData()
							.getRegPltNo()
							== null)
						{
							caInqData.setNoMFRecs(0);
						}
						else
						{
							// defect 6170
							// reverse 4903 and allow Special Owner to 
							// return a result always
							//Begin - Fix for CQU100004903
							//if (searchData.getKey3().equals(
							// TransCdConstant.VEHINQ))
							//{
							caInqData.setSpecialOwner(1);
							caInqData.setNoMFRecs(1);
							//}
							//else
							//{
							//	cInqData.setNoMFRecs(0);
							//}
							//End - Fix for CQU100004903
							// end defect 6170
						}
					} // defect 9502
					// add V21VTN to the if.
				} // defect 9636, 9642 
				// SLVG no longer exists; COA assigned within the SCOT 
				// event 

				// defect 10598 
				// Add TransCdConstant.PT72 
				else if (
					aaGSD.getKey1().equals(CommonConstant.VIN)
						&& caInqData.getMfDown() != 1
						&& (aaGSD.getKey3().equals(TransCdConstant.DTAORD)
							|| aaGSD.getKey3().equals(
								TransCdConstant.TITLE)
					//|| aaGSD.getKey3().equals(TransCdConstant.COA)
							|| aaGSD.getKey3().equals(
								TransCdConstant.SCOT)
							|| aaGSD.getKey3().equals(
								TransCdConstant.PT72)
					//TransCdConstant.SLVG)
							|| aaGSD.getKey3().equals(
								TransCdConstant.V21VTN)))
				{
					// end defect 9502
					// defect 9086
					//	Get VINA info
					//caInqData = laMFA.retrieveVehicleFromVINA(aaGSD);
					caInqData = retrieveVINAResponse(aaGSD);
					// end defect 9086
				} // end defect 9636, 9642
				// end defect 10598  
			}
		} // Check if search was by VIN and one of the Titles listed
		// defect 9636, 9642  
		if (aaGSD.getKey1().equals(CommonConstant.VIN)
			&& (aaGSD.getKey3().equals(TransCdConstant.DTAORD)
				|| aaGSD.getKey3().equals(
					TransCdConstant
						.TITLE) //|| aaGSD.getKey3().equals(TransCdConstant.COA)
		//|| aaGSD.getKey3().equals(TransCdConstant.SLVG)))
				|| aaGSD.getKey3().equals(TransCdConstant.SCOT)))
		{
			if (aaGSD.getKey4() != null
				&& !aaGSD.getKey4().equals(
					CommonConstant.STR_SPACE_EMPTY))
			{
				try
				{
					laOwnerData = laMFA.retrieveOwner(aaGSD);
					// defect 10112
					if (caInqData.getMfVehicleData() != null
						&& !UtilityMethods.isEmpty(
							laOwnerData.getOwnrId()))
					{
						// end defect 10112 	
						caInqData
							.getMfVehicleData()
							.setUserSuppliedOwnerData(
							laOwnerData);
					}
					else
					{
						caInqData.setOwnerId(aaGSD.getKey4());
					}
				} // RTSException is thrown when MF is down
				catch (RTSException leRTSEx)
				{
					// still return the ownerid entered
					caInqData.setMfDown(1);
				}
			}
		} // end defect 9636, 9642 
		return caInqData;
	}
	/**
	 * Search mainframe for archive
	 *  
	 * @param aaGSD GeneralSearchData
	 * @return VehicleInquiryData 
	 */
	public VehicleInquiryData searchMfArchive(GeneralSearchData aaGSD)
	{

		MfAccess laMFA = new MfAccess(csClientHost);
		// defect 9086
		// Moved business layer from MfAccess to VehicleInquiry class
		//	New
		//	 	MfAccess returns a string
		//		Set Number Of Records
		//		Get Multiple Regis and Junk Records
		//		Setup and make call to Special Plates Regis
		//		Move MF String data into there appropriate data objects
		//
		// Get vehicle from archive
		//caInqData = laMFA.retrieveVehicleFromArchive(aaGSD);
		String lsMfResponse = CommonConstant.STR_SPACE_EMPTY;
		try
		{
			lsMfResponse = caMfAccess.retrieveVehicleFromMF(aaGSD);
			setNoOfRecs(rtnNoOfRecsMF(lsMfResponse));
			// Get MultRegis and Junk records
			caInqData =
				retrieveMultRegisJunkResponse(lsMfResponse, aaGSD);
			// end defect 9086
			caInqData.setSearchArchiveIndi(
				CommonConstant.SEARCH_ARCHIVE);
			if (aaGSD.getIntKey4() == 1
				&& caInqData.getPartialDataList().size() > 1
				|| caInqData.getPartialSpclPltsDataList().size() > 1)
			{
				caInqData = getMultRecsFromArchive(caInqData);
				// defect 9502
				// allow V21 to use ivtrs multi rec break down for archive
				//				if ((aaGSD.getKey3().equals(TransCdConstant.V21VTN)
				//					|| aaGSD.getKey3().equals(TransCdConstant.G36VTN)
				//					|| aaGSD.getKey3().equals(TransCdConstant.V21PLD))
				//					&& caInqData.getNoMFRecs() > 1)
				//				{
				//					System.out.println("Calling V21 MultiRecs");
				//					checkForMultiRegisRecs(aaGSD, laMFA);
				//				}
				//				return caInqData;
				// end defect 9502
			}
			else
			{
				if (caInqData.getMfVehicleData() != null
					&& caInqData.getMfVehicleData().getTitleData() != null
					&& caInqData
						.getMfVehicleData()
						.getTitleData()
						.getDocNo()
						!= null
					&& !caInqData
						.getMfVehicleData()
						.getTitleData()
						.getDocNo()
						.equals(
						CommonConstant.STR_SPACE_EMPTY))
				{
					caInqData.setNoMFRecs(1);
				}
				else
				{
					if (caTmpInqData != null)
					{
						cbReturnIncomingVehInqData = true;
						caTmpInqData.setSearchArchiveIndi(1);
						caTmpInqData.setNoMFRecs(0);
					}
					else
					{
						caInqData.setNoMFRecs(0);
					}
				}
			} // defect 7746
			// if it was a multiple archive before, we are searching by
			// docno.  Allow that as well as vin.
			// For some reason reg renew leaves key3 null
			// defect 9636, 9642  
			if ((aaGSD.getKey1().equals(CommonConstant.VIN)
				|| aaGSD.getKey1().equals(CommonConstant.DOC_NO))
				&& (aaGSD.getKey3() != null)
				&& (aaGSD.getKey3().equals(TransCdConstant.DTAORD)
					|| aaGSD.getKey3().equals(
						TransCdConstant
							.TITLE) //|| aaGSD.getKey3().equals(
			//	TransCdConstant.COA)
			//|| aaGSD.getKey3().equals(TransCdConstant.SLVG)))
					|| aaGSD.getKey3().equals(TransCdConstant.SCOT)))
			{ // end defect 7746
				if (aaGSD.getKey4() != null
					&& !aaGSD.getKey4().equals(
						CommonConstant.STR_SPACE_EMPTY))
				{
					try
					{
						OwnerData laOwnerData =
							laMFA.retrieveOwner(aaGSD);
						// defect 7746
						// can not copy over ownerdata if destination is null
						// defect 10112 
						if (!UtilityMethods
							.isEmpty(laOwnerData.getOwnrId()))
						{
							// end defect 10122 
							// set the ownerid so it can be used later
							caInqData.setOwnerId(
								laOwnerData.getOwnrId());
							if (caInqData != null
								&& caInqData.getMfVehicleData() != null)
							{
								// end defect 7746
								caInqData
									.getMfVehicleData()
									.setUserSuppliedOwnerData(
									laOwnerData);
							}
						}
					} // RTSException is thrown when MF is down
					catch (RTSException leRTSEx)
					{
						caInqData.setMfDown(1);
					}
				}
			} // end defect 9636, 9642 
		} // RTSException is thrown when MF is down
		catch (RTSException aeRTSEx)
		{
			caInqData.setMfDown(1);
		}

		return caInqData;
	}

	/**
	 * Collect Special Plates data for each MiltRegisData returned
	 * 
	 * @param aaVehInqData	VehicleInquiryData
	 * @return VehicleInquiryData
	 */

	private VehicleInquiryData searchMultRegisSpclPltData(VehicleInquiryData aaVehInqData)
	{ // PartialDataList not empty and are case as RegistrationData
		//	means there are multiple RegisData records
		if (!aaVehInqData.getPartialDataList().isEmpty()
			&& aaVehInqData.getPartialDataList().get(0)
				instanceof RegistrationData)
		{
			GeneralSearchData laGSD = new GeneralSearchData();
			Vector lvSpclPltRegisData = new Vector();
			VehicleInquiryData laVehInqData = new VehicleInquiryData();
			SpecialPlatesRegisData laSPRegisData =
				new SpecialPlatesRegisData();
			for (int liIndex = 0;
				liIndex < aaVehInqData.getPartialDataList().size();
				liIndex++)
			{
				// Get Registration Data
				RegistrationData laRegisData =
					(RegistrationData) aaVehInqData
						.getPartialDataList()
						.get(
						liIndex);
				// Get the Special Plate Regis Data if SpclRegId exist
				if (laRegisData.getSpclRegId() > 0)
				{
					// Build the GeneralSearchData
					laGSD.setKey1(CommonConstant.SPCL_REG_ID);
					laGSD.setKey2(
						String.valueOf(laRegisData.getSpclRegId()));
					laGSD.setIntKey2(0);
					// Retrieve Special Plate record.
					// Move SpclPltRegis data object to MfVehicleData 
					//	object
					laVehInqData = retrieveSpclPltRegisResponse(laGSD);
					laSPRegisData =
						laVehInqData
							.getMfVehicleData()
							.getSpclPltRegisData();
				} // Move Special Plate Regis Data to vector
				// Note: If SpclRegId = 0 add SpclPltRegisData to vector
				//		 to keep index with RegistrationData vector
				lvSpclPltRegisData.add(laSPRegisData);
				// Reset SpecialPlatesRegisData object
				laSPRegisData = new SpecialPlatesRegisData();
			} // Set complete Special Plates Regis Data vector
			aaVehInqData.setPartialSpclPltsDataList(lvSpclPltRegisData);
		}
		return aaVehInqData;
	}

	/**
	 * Set Number Of records 
	 * 
	 * @param aiNoOfRecs	int
	 */
	private void setNoOfRecs(int aiNoOfRecs)
	{
		ciNoOfRecs = aiNoOfRecs;
	}

	/**
	 * Set Number of Retries
	 *
	 * @param int
	 */

	private void setTotalNoOfRetries(int aiNoOfRetries)
	{
		siTotalRetries = siTotalRetries + aiNoOfRetries;
	}

	/**
	 * Return VehicleInquiryData from MfResponse
	 * 
	 * @param asMfResponse	String
	 * @param asMfVINAResponse	String
	 * @param asMfRegisResponse	String
	 * @param asMfSpecialOwnerResponse	String
	 * @param asMfJunkResponse	String
	 * @param asMfPartialResponse	String
	 * @return VehicleInquiryData
	 */
	private VehicleInquiryData setVehicleInquiryDataFromMfResponse(
		String asMfResponse,
		String asMfVINAResponse,
		String asMfRegisResponse,
		String asMfSpecialOwnerResponse,
		String asMfJunkResponse,
		String asMfPartialResponse,
		String asSpclPltRegisResponse)
	{

		VehicleInquiryData laVehInqData;
		// Move data to their appropriate data objects
		// If lsMfResponse is null or empty, an empty VehicleInquiry 
		//	data object will be returned.
		laVehInqData =
			caMfAccess.setVehicleInquiryDataFromMfResponse(
				asMfResponse,
				asMfVINAResponse,
				asMfRegisResponse,
				asMfSpecialOwnerResponse,
				asMfJunkResponse,
				asMfPartialResponse,
				asSpclPltRegisResponse);
		return laVehInqData;
	}

	/**
	 * Determine if FRVP Verification is needed and call it
	 * if needed.
	 * 
	 * @param asTransCd
	 * @param aaInqData
	 */
	private void verifyInsurance(
		String asTransCd,
		VehicleInquiryData aaInqData)
	{
		// only handle selected trans

		// defect 10771 
		// Add IRENEW 
		// defect 10768 
		// Add WRENEW 
		if (asTransCd.equals(TransCdConstant.RENEW)
			|| asTransCd.equals(TransCdConstant.VEHINQ)
			|| asTransCd.equals(TransCdConstant.IRENEW)
			|| asTransCd.equals(TransCdConstant.WRENEW))
		{
			// end defect 10768
			// end defect 10771 

			// setup the result string
			String lsResult = FAILED;
			// make sure there is reg data to update
			if (aaInqData != null
				&& aaInqData.getMfVehicleData() != null
				&& aaInqData.getMfVehicleData().getRegData() != null)
			{
				// separate out RegData
				RegistrationData laRegData =
					(RegistrationData) aaInqData
						.getMfVehicleData()
						.getRegData();

				// Set regdata requires insurance based on cache lookup 
				laRegData.setInsuranceRequired(
					CommonFeesCache.isInsuranceRequired(
						laRegData.getRegClassCd()));

				// Only check TexasSure if Insurance is Required and
				// the Check Insurance switch is on
				if (laRegData.isInsuranceRequired()
					&& SystemProperty.isCheckInsurance())
				{
					Thread laThread;
					try
					{
						// setup the DocNo
						String lsDocNo = "";
						if (aaInqData.getMfVehicleData().getTitleData()
							!= null
							&& aaInqData
								.getMfVehicleData()
								.getTitleData()
								.getDocNo()
								!= null)
						{
							lsDocNo =
								aaInqData
									.getMfVehicleData()
									.getTitleData()
									.getDocNo();
						}

						// Setup the VIN
						String lsVin = "";
						if (aaInqData
							.getMfVehicleData()
							.getVehicleData()
							!= null
							&& aaInqData
								.getMfVehicleData()
								.getVehicleData()
								.getVin()
								!= null)
						{
							lsVin =
								aaInqData
									.getMfVehicleData()
									.getVehicleData()
									.getVin();
						}

						// Setup the PlateNo
						String lsPlate = "";
						if (aaInqData.getMfVehicleData().getRegData()
							!= null
							&& aaInqData
								.getMfVehicleData()
								.getRegData()
								.getRegPltNo()
								!= null)
						{
							lsPlate =
								aaInqData
									.getMfVehicleData()
									.getRegData()
									.getRegPltNo();
						}

						// defect 10119
					    // Setup to make the call
						TexasSureCall laTSC;
						if (SystemProperty.emulatingTexasSureHang())
						{
						    laTSC = new TexasSureCallHangTestStub();
						}
						else
						{
						    laTSC = new TexasSureCall();
						}

					    laThread = new Thread(laTSC);
					    int ilTimeout = SystemProperty.getTexasSureTimeOut();
					    int ilSleepMillis = -1;
					    
					    lsResult = makeTexasSureCall(lsResult, lsDocNo, lsVin, lsPlate, laTSC, laThread, ilTimeout, ilSleepMillis);
					    laThread.stop();
					    laThread = null;
					} 
					catch (Throwable aaThrowable)
					{
					    // If we get an exception, we are just going to
					    // log it. We will not throw it back.
					    RTSException leRTSEx = new RTSException(
						    RTSException.JAVA_ERROR, aaThrowable);
					    leRTSEx.printStackTrace();
					    laThread = null;
					    
					}
				}
				if (lsResult.equalsIgnoreCase(CommonConstant.CONFIRMED))
				{
					laRegData.setInsuranceVerified(true);

					// TODO Will this happen? 
					// set to true for multiple records.
					if (aaInqData.getNoMFRecs() > 1
						&& aaInqData.getPartialDataList() != null
						&& aaInqData.getPartialDataList().size() > 1)
					{
						for (Iterator laIter =
							aaInqData.getPartialDataList().iterator();
							laIter.hasNext();
							)
						{
							RegistrationData laElement =
								(RegistrationData) laIter.next();
							laElement.setInsuranceVerified(false);
						}
					}
				}
				else
				{
					laRegData.setInsuranceVerified(false);
				}
			}
		}
	}
}