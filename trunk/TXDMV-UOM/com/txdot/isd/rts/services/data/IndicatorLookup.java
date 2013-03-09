package com.txdot.isd.rts.services.data;

import java.util.Collections;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.IndicatorDescriptionsCache;
import com.txdot.isd.rts.services.cache.IndicatorStopCodesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.OwnershipEvidenceCodesCache;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * IndicatorLookup.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	01/24/2002  Check for NULL at 
 *							mfVeh.getVehicleData().getVehOdmtrBrnd()
 * MAbs     	04/15/2002 	Fix NullPointerException in "Same Vehicle"
 * MAbs			06/27/2002	Moving "H" to right place in Junkcodes
 *							defect 4280
 * J Zwiener	11/12/2004	When an indicator value is invalid on MF,
 *							show the indi name+value+"No Description".
 *							eg. "Vehodmtrbrand=Z No Description"
 *							This functionality for Screen scenario only.
 *							modified addIndi()
 *							modified getIndicators()
 *							defect 6568 Ver 5.2.2
 * B Hargrove	06/01/2005	Fix misprint. Change ']' to '['.
 *							defect 8039 Ver 5.2.3
 *							modify getIndicators()
 * K Harrell	06/20/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * J Rue		08/07/2007	Add LemonLawIndi to get indicators
 * 							modify getIndicators()	
 * 							defect 9225 Ver Broadcast Messaging
 * J Rue		12/14/2007	Add additional check to RegisInvalid
 * 							DTARegInvldIndi
 * 							modify getIndicators()
 * 							defect 8329 Ver DEFECT-POS-A
 * K Harrell	02/04/2008	Use ChildSupportIndi vs. LemonLawIndi
 * 							add getChildSupportIndi()
 * 							delete getLemonLawIndi() 
 * 							modify getIndicators()
 * 							defect 9546 Ver 3 Amigos PH A
 * Ray Rowehl	02/08/2008	Add V21 specific processing.  This also 
 * 							effects POS processing.
 * 							add V21
 * 							add getIndicatorsV21(), 
 * 							modify getIndicators()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/21/2008	Correct POS RecondCd processing.
 * 							Clean up Junked processing.
 * 							modify getIndicators(),
 * 								getSlvgDt()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/27/2008	Improve Salvage processing for V21
 * 							modify getSlvgDt(),
 * 								processSalvageV21()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/28/2008	Work on removing extra spaces between [].
 * 							modify getIndicators(),
 * 								processSalvageV21()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	03/21/2008	Modify handling of CCO and County Scofflaw 
 * 							for V21 so they do not get the extra 
 * 							descriptive data.
 * 							modify getCcoIssueDate(), 
 * 								getClaimComptCntyNo(), getIndicators(), 
 * 								getIndicatorsV21()
 * 							defect 9610 Ver 3_Amigos_PH_A
 * Ray Rowehl	03/26/2008	Clean up processing for NotfyngCity, 
 * 							ReplicaVehMk, and RegRefAmt.
 * 							modify getIndicators()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	03/31/2008	Send the CCOISSUEDATE with brackets to V21.
 * 							modify getCcoIssueDate()
 * 							defect 9610 Ver 3_Amigos_PH_A
 * Ray Rowehl	04/03/2008	Change City Scofflaw to not have the []
 * 							for V21.
 * 							modify getIndicators(), getIndicatorsV21(),
 * 								getNotfyngCity()
 * 							defect 9610 Ver 3_Amigos_PH_A
 * Ray Rowehl	04/04/2008	Remove the [] for pos as well on City 
 * 							Scofflaw.
 * 							modify getNotfyngCity()
 * 							defect 9610 Ver 3_Amigos_PH_A
 * J Rue		04/01/2008	Comment out references to LemonLaw
 * 							Add Child Support
 * 							add getChildSupportIndi()
 * 							modify getIndicators(), getIndicatorsV21()
 * 							deprecate getLemonLaw()
 * 							defect 9581 Ver 3_Amigos_PH_A
 * Ray Rowehl	04/11/2008	V21 has issues dealing with a 0 returned
 * 							for APPRNDCOMPTCNTYNO.
 * 							modify getApprndCntyNo()
 * 							defect 9610 Ver 3_Amigos_PH_A
 * Ray Rowehl	05/01/2008	Found that the indi lookup for Plate Owner 
 * 							for POS was using the wrong variable.
 * 							modify getIndicators()
 * 							defect 9650 Ver FRVP
 * J Rue		06/27/2008	Add trans code to get DTARegInvalidIndi
 *  						modify getRegInvldIndi(), getIndicatorsV21()
 * 								getIndicators()
 * 							deefct 8329 Ver Defect_POS_A
 * K Harrell	07/09/2008	Add processing for ETtlCd, PrismLvlCd
 * 							add getETtlCd(), getPrismLvlCd() 
 * 							modify getIndicators(), getIndicatorsV21() 
 * 							defect 9712 Ver MyPlates_POS
 * K Harrell	09/25/2008	Modify to use new IndicatorData change (added
 * 							 csIndiName) so that could use Desc for the
 * 							 Description vs. StopCode. Added 
 * 							 getNewIndicatorData to reduce repetitive 
 * 							 assignments.
 * 							 Use IndicatorData Desc vs. StopCd for Description 
 * 							add getNewIndicatorData(String, int), 
 * 								getNewIndicatorData(String, String)
 *  						delete getHardStopReasons()
 *   						modify just about everything else 
 * 							defect 9651 Ver Defect_POS_B
 * K Harrell	11/08/2008	Restored code for LemonLawIndi 
 * 							add getLemonLawIndi() 
 * 							modify getIndicators(), getIndicatorsV21() 
 * 							defect 9860 Ver Defect_POS_B
 * Ray Rowehl	12/17/2008	Fix merge issue!  appears to be related to 
 * 							defect 9651.
 * 							delete getETtlCd() (duplicated),
 * 								getPrismLvlCd() (duplicated)
 * 							defect MERGE Ver Defect_POS_C
 * K Harrell	02/03/2009	Final fix to MERGE issue - remove redundant 
 * 							code in getIndicators() for EttlCd and 
 * 							 PrismLvlCd 
 * 							modify getIndicators()
 * 							defect MERGE Ver Defect_POS_D
 * K Harrell	02/25/2009	Add logic for ELT indicators 
 * 							add getUTVMislblIndi(), getVTRRegEmrgCd1(),
 * 							 getVTRRegEmrgCd2(), getVTRTtlEmrgCd1(),
 * 							 getVTRTtlEmrgCd2()
 * 							modify getIndicators(), getIndicatorsV21()
 * 							defect 9980 Ver Defect_POS_E
 * K Harrell	05/01/2009	Apprehended Remark not printed 
 * 							delete getApprndCntyNo(MFVehicleData) 
 * 							add  getApprndCntyNo()
 * 							modify getIndicators(), getIndicatorsV21() 
 * 							defect 10050 Ver Defect_POS_E
 * K Harrell	09/14/2009	Typo in V21 Indicator Lookup: VTRTtlEmrgCd1
 * 							 sent twice.   
 * 							modify getIndicatorsV21() 
 * 							defect 9980 Ver Defect_POS_F 
 * K Harrell	10/14/2009	Use CommonConstant for PrivacyOptCd Lookup
 * 							add getPrivacyOptCd()
 * 							delete getPrivacyOptCd(MFVehicleData) 
 * 							modify getIndicators(), getIndicatorsV21()  
 * 							defect 10246 Ver Defect_POS_G
 * K Harrell	03/23/2010	Implement Lookup for TitleData 
 * 								csPvtLawEnfVehCd,csNonTtlGolfCartCd, 
 * 								csVTRTtlEmrgCd3, csVTRTtlEmrgCd4
 * 							Implement Lookup for RegistrationData 
 * 								ciSOReelectionIndi
 * 							Remove Lookup for csVTRTtlEmrgCd1, 
 * 								 	csVTRTtlEmrgCd2						
 * 							add getPvtLawEnfVehCd(), getNonTtlGolfCartCd(), 
 * 							  getVTRTtlEmrgCd3(), getVTRTtlEmrgCd4(),
 * 							  getSOReelectionIndi()  
 * 							delete getVTRTtlEmrgCd1(), getVTRTtlEmrgCd2()
 * 							modify getIndicators(), getIndicatorsV21() 
 * 							defect 10376 Ver POS_640   	
 * K Harrell	06/15/2010	add getElectionPndingIndi(),getEMailRenwlReqCd()
 * 							delete getSOReelectionIndi()
 * 							modify getIndicators(), getIndicatorsV21()
 * 							defect 10507, 10508 Ver 6.5.0 
 * K Harrell	07/27/2010	Special handling for Junked for new CTL003. 
 * 							Set JunkedAttrib(true) when processing 
 * 							 SalvageData so that shows up on CTL003
 * 							modify getIndicators()  
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	01/06/2011	add getVehMjrColorCd(), getVehMnrColorCd()  
 * 							modify getIndicators(), getIndicatorsV21() 
 * 							defect 10712 Ver 6.7.0 	
 * K Harrell	10/13/2011	add getETtlPrntDate() 
 * 						    modify getIndicators(), getIndicatorsV21()
 * 							defect 10841 Ver 6.9.0 
 * K Harrell	10/26/2011	delete call to getETtlPrntDate() for 
 * 							V21 
 * 							modify getIndicatorsV21()
 * 							defect 10841 Ver 6.9.0 
 * K Harrell	10/27/2011	remove indi lookup for Vehicle Color for POS
 * 							modify getIndicators()
 * 							defect 11132 Ver 6.9.0  
 * K Harrell	11/20/2011	modified for VTR275 
 * 							add addVTR275Separator(), VTR275
 * 							modify addIndi(), getCcoIssueDate(),
 * 							 getClaimComptCntyNo(),getETtlPrntDate(), 
 * 							 getIndicators(), getLegalRestrntNo(), 
 * 							 getRecondCdState(), getRegPltOwnrName(),  
 * 							 getReplicaVehMk(), getSlvgDt(), 
 * 							 getSurrTtlDate(),getTtlRejctnDate(), 
 * 							 getTtlRejctnOfc(), getVehSoldDate()     
 * 							defect 11052 Ver 6.9.0 
 * K Harrell	11/29/2011	Implement Vehicle Color (Major) for Receipt
 * 							modify getIndicators()  
 * 							defect 11160 Ver 6.9.0 
 * K Harrell	12/07/2011	Lost CCOIssueDate logic 
 * 							modify getIndicators()
 * 							defect 11179 Ver 6.9.0 
 * B Woodson	01/13/2012	add getExportIndi()
 * 							modify getIndicators()
 * 							defect 11228 Ver 6.10.0
 * K Harrell	01/16/2012	add getSurvshpRightsName1(), 
 * 							  getSurvshpRightsName2(), 
 * 							  getAddlSurvivorIndi() 
 * 							modify getIndicators()
 * 							defect 10827 Ver 6.10.0 
 * T Pederson	02/07/2012	add getSlvgIndi() 
 * 							modify getIndicators()
 * 							defect 11097 Ver 6.10.0 
 * K Harrell	02/14/2012	add padding for Rights of Survivorship
 * 							modify getIndicators() 
 * 							defect 10827 Ver 6.10.0 
 * K Harrell	03/27/2012	Truncate ROS Names to 20 Char for CCO "Receipt"
 * 							Do not indent ROS Names for CCO "Receipt" 
 * 							modify getIndicators() 
 * 							defect 11330 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Handles indicators
 *
 * @version 6.10.0 				03/27/2012
 * @author	Michael Abernethy
 * <br>Creation Date:			10/17/2001 13:02:47
 */

public class IndicatorLookup
{
	// Constants
	public final static int CCO = 2;
	public final static int RECEIPT = 1;
	public final static int SCREEN = 0;
	public final static int SALVAGE = 3;
	// defect 9502
	public final static int V21 = 4;
	// end defect 9502
	
	// defect 11052 
	public final static int VTR275 = 5; 
	// end defect 11052 

	/**
	 * IndicatorLookup.java Constructor
	 */
	public IndicatorLookup()
	{
		super();
	}

	/**
	 * Adds Indicators to vector  
	 * 
	 * @param avVector Vector
	 * @param asTransCd String
	 * @param aiScenario int
	 * @param asIndiName
	 * @param asIndiFieldValue 
	 */
	private static void addIndi(
		Vector avVector,
		String asTransCd,
		int aiScenario,
		String asIndiName,
		String asIndiFieldValue)
	{
		IndicatorDescriptionsData laIndiData =
			IndicatorDescriptionsCache.getIndiDesc(
				asIndiName,
				asIndiFieldValue);
		if (laIndiData == null)
		{
			// defect 6568
			if (aiScenario == SCREEN)
			{
				IndicatorData laIndicatorData = new IndicatorData();
				laIndicatorData.setPriority(99);
				laIndicatorData.setDesc(
					asIndiName
						+ "="
						+ asIndiFieldValue
						+ " No Description");
				avVector.add(laIndicatorData);
			}
			// end defect 6568
			return;
		}
		// defect 11052 
		if (aiScenario == SCREEN || aiScenario == VTR275)
		{
			// end defect 11052 
			IndicatorData laIndicatorData = new IndicatorData();
			laIndicatorData.setPriority(
				laIndiData.getIndiScrnPriority());
			laIndicatorData.setDesc(laIndiData.getIndiDesc());
			
			// Stop Code Assignment
			IndicatorStopCodesData laIndiStopCdsData =
				IndicatorStopCodesCache.getIndiStopCd(
					asIndiName,
					asIndiFieldValue,
					asTransCd);
			if (laIndiStopCdsData != null)
			{
				if (laIndiStopCdsData.getIndiName().equals("JNKCD")
					&& avVector.size() > 0)
				{
					IndicatorData laLastIndicatorData =
						(IndicatorData) avVector.lastElement();
					laLastIndicatorData.setStopCode(
						laIndiStopCdsData.getIndiStopCd());
					avVector.set(
						avVector.size() - 1,
						laLastIndicatorData);
				}
				else
				{
					laIndicatorData.setStopCode(
						laIndiStopCdsData.getIndiStopCd());
				}
			}
			avVector.add(laIndicatorData);
		}
		else if (aiScenario == RECEIPT)
		{
			IndicatorData laIndicatorData = new IndicatorData();
			laIndicatorData.setPriority(
				laIndiData.getIndiRcptPriority());
			laIndicatorData.setDesc(laIndiData.getIndiDesc());
			avVector.add(laIndicatorData);
		}
		else if (aiScenario == CCO)
		{
			IndicatorData laIndicatorData = new IndicatorData();
			laIndicatorData.setPriority(
				laIndiData.getIndiCCOPriority());
			laIndicatorData.setDesc(laIndiData.getIndiDesc());
			avVector.add(laIndicatorData);
		}
		else if (aiScenario == SALVAGE)
		{
			IndicatorData laIndicatorData = new IndicatorData();
			laIndicatorData.setPriority(
				laIndiData.getIndiSalvPriority());
			laIndicatorData.setDesc(laIndiData.getIndiDesc());
			avVector.add(laIndicatorData);
		}
	}

	// defect 9651 
	// Not used 
	//	/**
	//	 * Return Hard Stop Reasons
	//	 *   
	//	 * @param  avIndicatorData Vector
	//	 * @return String
	//	 */
	//	public static String getHardStopReasons(Vector avIndicatorData)
	//	{
	//		String lsReasons = "";
	//		for (int i = 0; i < avIndicatorData.size(); i++)
	//		{
	//			IndicatorData laIndiData =
	//				(IndicatorData) avIndicatorData.get(i);
	//			if (laIndiData.getStopCode() == null)
	//			{
	//				continue;
	//			}
	//			if (laIndiData.getStopCode().equals("H"))
	//			{
	//				lsReasons += laIndiData.getDesc() + "\n";
	//			}
	//		}
	//		lsReasons = lsReasons.substring(0, lsReasons.length() - 1);
	//		return lsReasons;
	//	}
	// end defect 9651 

	/**
	 * Process Child Support
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getChildSupportIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"CHILDSUPPORTINDI",
			aaMFVeh.getTitleData().getChildSupportIndi());
		// end defect 9651 
	}
	/**
	 * Add Indicator w/ "." for separator in returned Vector to 
	 * VTR275 Print 
	 * 
	 * @param aiScenario
	 * @param avVector
	 */
	private static void addVTR275Separator(int aiScenario,Vector avVector)
	{
		if (aiScenario == VTR275)
		{
			int liPriority = 99; 
			
			if (avVector.size() != 0)
			{
				int liLastElement = avVector.size() -1; 
				liPriority = ((IndicatorData) avVector.elementAt(liLastElement)).getPriority(); 
			}
				
			IndicatorData laIndi = new IndicatorData(); 
			laIndi.setPriority(liPriority);
			laIndi.setDesc("."); 
			avVector.add(laIndi); 
		}
	}

	/**
	 * Process Agency Loaned.
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getAgncyLoandIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651
		return getNewIndicatorData(
			"AGNCYLOANDINDI",
			aaMFVeh.getTitleData().getAgncyLoandIndi());
		// end defect 9651 
	}

	//	/**
	//	 * Process Apprended County Number
	//	 * 
	//	 * @param aaMFVeh
	//	 * @return IndicatorData
	//	 */
	//	private static IndicatorData getApprndCntyNo(MFVehicleData aaMFVeh)
	//	{
	//		IndicatorData laIndiData = new IndicatorData();
	//		// defect 9651
	//		laIndiData.setIndiName("APPRNDCOMPTCNTYNO");
	//		String lsFieldValue = "";
	//		// defect 9610
	//		// V21 does not want a 0 returned.
	//		// If number is 0, return empty string.
	//		// This works because POS does not call this routine unless the 
	//		// value is greater than 0.
	//		if (aaMFVeh.getRegData().getApprndCntyNo() > 0)
	//		{
	//			// this is the original assignment
	//			lsFieldValue =
	//				String.valueOf(aaMFVeh.getRegData().getApprndCntyNo());
	//		}
	//		// end defect 9610
	//
	//		return getNewIndicatorData("APPRNDCOMPTCNTYNO", lsFieldValue);
	//		// end defect 9651 
	//	}

	/**
	 * Process Apprended County Number. 
	 * Only called if > 0 for RTS POS client. Not called by 
	 * getIndicatorsV21() as not maintained at MF. 
	 * 
	 * @return IndicatorData
	 */
	private static IndicatorData getApprndCntyNo()
	{
		return getNewIndicatorData(
			"APPRNDCOMPTCNTYNO",
			CommonConstant.STR_SPACE_EMPTY);
	}
	/**
	 * Process Additional SurvivorIndi 
	 * Only called if > 0 for RTS POS client. Not called by 
	 * getIndicatorsV21(). 
	 * 
	 * @return IndicatorData
	 */
	private static IndicatorData getAddlSurvivorIndi(MFVehicleData aaMFVeh)
	{
		return getNewIndicatorData(
			"ADDLSURVIVORINDI",
			aaMFVeh.getTitleData().getAddlSurvivorIndi());
	}

	/**
	 * Process Bonded Title Code
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getBndedTtlCd(MFVehicleData aaMFVeh)
	{
		// defect 9651
		String lsFieldValue = "";
		if (aaMFVeh.getTitleData().getBndedTtlCd() != null
			&& !aaMFVeh.getTitleData().getBndedTtlCd().equals(""))
		{
			lsFieldValue =
				aaMFVeh.getTitleData().getBndedTtlCd().toUpperCase();
		}
		return getNewIndicatorData("BNDEDTTLCD", lsFieldValue);
		// end defect 9651
	}

	/**
	 * Process CCO Issued Date
	 * 
	 * @param aaMFVeh
	 * @param aiScenario
	 * @return IndicatorData
	 */
	private static IndicatorData getCcoIssueDate(
		MFVehicleData aaMFVeh,
		int aiScenario)
	{
		// defect 9651
		String lsFieldValue = "";

		if (aaMFVeh.getTitleData().getCcoIssueDate() > 0)
		{
			IndicatorDescriptionsData laIndiDescData =
				IndicatorDescriptionsCache.getIndiDesc(
					"CCOISSUEDATE",
					"");

			// defect 9610
			//lsIndiDesc = laIndiDescData.getIndiDesc();
			// end defect 9610

			RTSDate laRTSDate =
				new RTSDate(
					RTSDate.YYYYMMDD,
					aaMFVeh.getTitleData().getCcoIssueDate());

			// defect 9610
			// if the request is for V21, do not add the desc. stuff
			// keep the brackets though 
			if (aiScenario != V21)
			{
				if (laIndiDescData.getIndiDesc() != null)
				{
					lsFieldValue = laIndiDescData.getIndiDesc();
				}
				lsFieldValue = lsFieldValue + "  ";
			}
			
			// defect 11052 
			if (aiScenario == VTR275)
			{
				lsFieldValue = lsFieldValue.trim() +" "+ laRTSDate.toString(); 
			}
			else
			{
				lsFieldValue =
					lsFieldValue + "[" + laRTSDate.toString() + "]";
				// end defect 9610
			}
			// end defect 11052 
		}

		return getNewIndicatorData("CCOISSUEDATE", lsFieldValue);
		// end defect 9651
	}

	/**
	 * Process Claim Comptroller County Number
	 * 
	 * @param aaMFVeh
	 * @param aiScenario
	 * @return IndicatorData
	 */
	private static IndicatorData getClaimComptCntyNo(
		MFVehicleData aaMFVeh,
		int aiScenario)
	{
		// defect 9651 
		String lsFieldValue = "";

		if (aaMFVeh.getRegData().getClaimComptCntyNo() > 0)
		{
			IndicatorDescriptionsData laIndiDescData =
				IndicatorDescriptionsCache.getIndiDesc(
					"CLAIMCOMPTCNTYNO",
					"");

			if (laIndiDescData != null)
			{
				// defect 9610
				if (aiScenario != V21)
				{
					lsFieldValue = laIndiDescData.getIndiDesc();
				}
				// end defect 9610
			}
			// defect 11052 
			int liOfcNo = aaMFVeh.getRegData().getClaimComptCntyNo(); 

			if (aiScenario == VTR275)
			{
				lsFieldValue = lsFieldValue+" "+ liOfcNo; 
			}
			else
			{
				OfficeIdsData laOfcIdsData =
					OfficeIdsCache.getOfcId(liOfcNo);
				// end defect 11052 

				if (laOfcIdsData != null)
				{
					// defect 9610
					// there needs to be a space for POS but not for V21.
					if (aiScenario != V21)
					{
						lsFieldValue = lsFieldValue + " ";
					}

					lsFieldValue = lsFieldValue + laOfcIdsData.getOfcName();
					// end defect 9610
				}
			}
		}

		return getNewIndicatorData("CLAIMCOMPTCNTYNO", lsFieldValue);
		// defect 9651
	}

	/**
	 * Process Diesel Indicator
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getDieselIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"DIESELINDI",
			aaMFVeh.getVehicleData().getDieselIndi());
		// end defect 9651 
	}

	/**
	 * Process DOT Standards
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getDotStndrdsIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"DOTSTNDRDSINDI",
			aaMFVeh.getVehicleData().getDotStndrdsIndi());
		// end defect 9651 
	}

	/**
	 * Process DPS Safety Suspension.
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getDpsSaftySuspnIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"DPSSAFTYSUSPNINDI",
			aaMFVeh.getRegData().getDpsSaftySuspnIndi());
		// end defect 9651 
	}

	/**
	 * Process DPS Stolen 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getDpsStlnIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"DPSSTLNINDI",
			aaMFVeh.getVehicleData().getDpsStlnIndi());
		// end defect 9651 
	}
	/**
	 * Process ElectionPndingIndi 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getElectionPndingIndi(MFVehicleData aaMFVeh)
	{
		int liElectionPndingIndi = 0;
		if (aaMFVeh.getSpclPltRegisData() != null)
		{
			liElectionPndingIndi =
				aaMFVeh.getSpclPltRegisData().getElectionPndngIndi();
		}
		return getNewIndicatorData(
			"ELECTIONPNDNGINDI",
			liElectionPndingIndi);
	}

	/**
	 * Process EMail Renewal Request Code 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getEMailRenwlReqCd(MFVehicleData aaMFVeh)
	{
		return getNewIndicatorData(
			"EMAILRENWLREQCD",
			aaMFVeh.getRegData().getEMailRenwlReqCd());
	}

	/**
	 * Process Emmissions Source Code
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getEmissionSourceCd(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		String lsFieldValue = "";
		if (aaMFVeh.getRegData().getEmissionSourceCd() != null
			&& !aaMFVeh.getRegData().getEmissionSourceCd().equals(""))
		{
			lsFieldValue =
				aaMFVeh
					.getRegData()
					.getEmissionSourceCd()
					.toUpperCase();
		}
		return getNewIndicatorData("EMISSIONSOURCECD", lsFieldValue);
		// end defect 9651 
	}
	/**
	 * Process ETtlCd 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getETtlCd(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"ETTLCD",
			aaMFVeh.getTitleData().getETtlCd());
		// end defect 9651
	}
	
	/**
	 * Process ETtlPrntDate 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getETtlPrntDate(MFVehicleData aaMFVeh, int aiScenario)
	{
		String lsFieldValue = "";

		if (aaMFVeh.getTitleData().getETtlPrntDate() > 0)
		{
			IndicatorDescriptionsData laIndiDescData =
				IndicatorDescriptionsCache.getIndiDesc(
					"ETTLPRNTDATE",
					"");

			RTSDate laRTSDate =
				new RTSDate(
					RTSDate.YYYYMMDD,
					aaMFVeh.getTitleData().getETtlPrntDate());
			
			// Will not be called for V21; retained 
			if (aiScenario != V21)
			{
				if (laIndiDescData.getIndiDesc() != null)
				{
					lsFieldValue = laIndiDescData.getIndiDesc();
				}
			}
			// defect 11052 
			if (aiScenario != VTR275)
			{
				lsFieldValue =
					lsFieldValue + "  [" + laRTSDate.toString() + "]";
			}
			else
			{
				lsFieldValue =
					lsFieldValue + " " + laRTSDate.toString();
			}
			// end defect 11052 
		}
		return getNewIndicatorData("ETTLPRNTDATE", lsFieldValue);
	}

	/**
	 * Process Exempt 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getExmptIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"EXMPTINDI",
			aaMFVeh.getRegData().getExmptIndi());
		// end defect 9651 
	}

	//defect 11228
	/**
	 * Process Export
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getExportIndi(MFVehicleData aaMFVeh)
	{
		return getNewIndicatorData(
			"EXPORTINDI",
			aaMFVeh.getTitleData().getExportIndi());
	}
	//end defect 11228
	
	/**
	 * Process Fixed Weight
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getFxdWtIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"FXDWTINDI",
			aaMFVeh.getVehicleData().getFxdWtIndi());
		// end defect 9651 
	}

	/**
	 * Process Flood Damaged
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getFloodDmgeIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"FLOODDMGEINDI",
			aaMFVeh.getVehicleData().getFloodDmgeIndi());
		// end defect 9651 
	}

	/**
	 * Process Goverment Owned
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getGovtOwndIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"GOVTOWNDINDI",
			aaMFVeh.getTitleData().getGovtOwndIndi());
		// end defect 9651 
	}

	/**
	 * Process Heavy Vehicle Use Tax
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getHvyVehUseTaxIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"HVYVEHUSETAXINDI",
			aaMFVeh.getRegData().getHvyVehUseTaxIndi());
		// end defect 9651 
	}

	/**
	 * Return the Indictors  
	 *
	 * @param  aaMFVeh MFVehicleData
	 * @param  aaTransCd String
	 * @param  aiScenario int
	 * @return Vector
	 */
	public static Vector getIndicators(
		MFVehicleData aaMFVeh,
		String asTransCd,
		int aiScenario)
	{
		Vector lvVector = new Vector();

		//String lsIndiFieldValue = "";
		IndicatorData laTempIndicatorData = new IndicatorData();

		//defect 11228
		if (aaMFVeh.getTitleData().getExportIndi() != 0)
		{
			laTempIndicatorData = getExportIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			
			addVTR275Separator(aiScenario,lvVector); 
		}
		//end defect 11228
		
		// defect 9581
		//	New attributes for V21 (ChildSupportIndi)
		//	ChildSupport replaces LemonLaw
		if (aaMFVeh.getTitleData().getChildSupportIndi() != 0)
		{
			laTempIndicatorData = getChildSupportIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			
			addVTR275Separator(aiScenario,lvVector); 
		}
		// end defect 9581

		if (aaMFVeh.getTitleData().getAgncyLoandIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getAgncyLoandIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651 
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getRegData().getApprndCntyNo() != 0)
		{
			// defect 9502, 9651

			// defect 10050 
			// Do not need parameter  
			//laTempIndicatorData = getApprndCntyNo(aaMFVeh);
			laTempIndicatorData = getApprndCntyNo();
			// end defect 10050 

			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		// 9502, 9651
		//if (aaMFVeh.getTitleData().getBndedTtlCd() != null
		//	&& !aaMFVeh.getTitleData().getBndedTtlCd().equals(""))
		//{
		laTempIndicatorData = getBndedTtlCd(aaMFVeh);
		if (laTempIndicatorData.getDesc().length() > 0)
		{
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		//}
		// end defect 9502, 9651

		if (aaMFVeh.getVehicleData().getDieselIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getDieselIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getVehicleData().getDotStndrdsIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getDotStndrdsIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getRegData().getDpsSaftySuspnIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getDpsSaftySuspnIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
			// end defect 9502, 9651
		}

		if (aaMFVeh.getVehicleData().getDpsStlnIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getDpsStlnIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		// defect 9502, 9651
		laTempIndicatorData = getEmissionSourceCd(aaMFVeh);
		if (laTempIndicatorData.getDesc().length() > 0)
		{
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 9502, 9651

		if (aaMFVeh.getRegData().getExmptIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getExmptIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getVehicleData().getFloodDmgeIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getFloodDmgeIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getVehicleData().getFxdWtIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getFxdWtIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getTitleData().getGovtOwndIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getGovtOwndIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getRegData().getHvyVehUseTaxIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getHvyVehUseTaxIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getTitleData().getInspectnWaivedIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getInspectnWaivedIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		// defect 9860 
		if (aaMFVeh.getTitleData().getLemonLawIndi() != 0)
		{
			laTempIndicatorData = getLemonLawIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 9860  

		if (aaMFVeh.getRegData().getPltSeizedIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getPltSeizedIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getTitleData().getPriorCCOIssueIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getPriorCCOIssueIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		// defect 10246 
		//if (aaMFVeh.getOwnerData().getPrivacyOptCd() != 0)
		//{
		laTempIndicatorData = getPrivacyOptCd();
		addIndi(
			lvVector,
			asTransCd,
			aiScenario,
			laTempIndicatorData.getIndiName(),
			laTempIndicatorData.getDesc());
		//}
		// end defect 10246 

		if (aaMFVeh.getVehicleData().getPrmtReqrdIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getPrmtReqrdIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
			// end defect 9502, 9651
		}

		if (aaMFVeh.getVehicleData().getReContIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getReContIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getRegData().getRegHotCkIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getRegHotCkIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		//	Add getDTARegInvlIndi() to check
		if (aaMFVeh.getRegData().getRegInvldIndi() != 0
			|| aaMFVeh.getRegData().getDTARegInvlIndi() != 0)
		{
			// defect 9502, 9651
			//	Add trans code to getRegInvIndi()
			laTempIndicatorData = getRegInvldIndi(aaMFVeh, asTransCd);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		// defect 9502, 9651
		laTempIndicatorData = getRegRefAmt(aaMFVeh);
		if (laTempIndicatorData.getDesc().length() > 0)
		{
			// a length greater then 0 means it is set
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				"");
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 9502, 9651
		// end defect 8329

		if (aaMFVeh.getRegData().getRenwlMailRtrnIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getRenwlMailRtrnIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		// defect 10508 
		if (aaMFVeh.getRegData().getEMailRenwlReqCd() != 0)
		{
			laTempIndicatorData = getEMailRenwlReqCd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 10508 

		if (aaMFVeh.getRegData().getStkrSeizdIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getStkrSeizdIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getTitleData().getSurvshpRightsIndi() != 0)
		{
			// defect 11330
			boolean lbCCO = (aiScenario == CCO); 
			// end defect 11330 
			
			// defect 9502, 9651
			laTempIndicatorData = getSurvshpRightsIndi(aaMFVeh);
			addIndi(
					lvVector,
					asTransCd,
					aiScenario,
					laTempIndicatorData.getIndiName(),
					laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);

			// defect 10827
			if (aaMFVeh.getTitleData().getAddlSurvivorIndi() ==1)
			{
				laTempIndicatorData = getAddlSurvivorIndi(aaMFVeh);

				addIndi(
						lvVector,
						asTransCd,
						aiScenario,
						laTempIndicatorData.getIndiName(),
						laTempIndicatorData.getDesc());

				IndicatorData laIndiData =
					(IndicatorData) lvVector.lastElement();

				// defect 11330 
				// if (aiScenario != VTR275)
				if (aiScenario != VTR275 && !lbCCO)
				{
					// end defect 11330 
					laIndiData.setDesc(
							"  " + laIndiData.getDesc());
				}
				addVTR275Separator(aiScenario,lvVector);
			}
			else if (!UtilityMethods.isEmpty(aaMFVeh.getTitleData().getSurvShpRightsName1()))
			{
				laTempIndicatorData =  getSurvshpRightsName1(aaMFVeh);
				
				addIndi(
						lvVector,
						asTransCd,
						aiScenario,
						laTempIndicatorData.getIndiName(),
						laTempIndicatorData.getDesc());

				IndicatorData laIndiData = 
					(IndicatorData) lvVector.lastElement();

				// defect 11330 
				String lsName = aaMFVeh.getTitleData().getSurvShpRightsName1();
				
				if (lbCCO && lsName.length()>20)
				{
					lsName = lsName.substring(0,20); 
				}

				//laIndiData.setDesc(laIndiData.getDesc() +  aaMFVeh.getTitleData().getSurvShpRightsName1());
				laIndiData.setDesc(lsName);
				
				if (aiScenario != VTR275 && !lbCCO)
				{
					laIndiData.setDesc(
							"  " + lsName);
					// end defect 11330
				}
				addVTR275Separator(aiScenario,lvVector);

				if (!UtilityMethods.isEmpty(aaMFVeh.getTitleData().getSurvShpRightsName2()))
				{
					laTempIndicatorData =  getSurvshpRightsName2(aaMFVeh);
					addIndi(
							lvVector,
							asTransCd,
							aiScenario,
							laTempIndicatorData.getIndiName(),
							laTempIndicatorData.getDesc());

					laIndiData = 
						(IndicatorData) lvVector.lastElement();
					
					// defect 11330 
					lsName = aaMFVeh.getTitleData().getSurvShpRightsName2(); 
					
					if (lbCCO && lsName.length() >20)
					{
						lsName = lsName.substring(0,20); 
					}
					
					//laIndiData.setDesc(laIndiData.getDesc() +  aaMFVeh.getTitleData().getSurvShpRightsName2());
					
					laIndiData.setDesc(lsName);

					if (aiScenario != VTR275 && !lbCCO)
					{
						laIndiData.setDesc(
								"  " + lsName);
						// end defect 11330
					}
					addVTR275Separator(aiScenario,lvVector);
				}
			}
			// end defect 10827 
		}

		if (aaMFVeh.getRegData().getTireTypeCd() != null
			&& aaMFVeh.getRegData().getTireTypeCd().toUpperCase().equals(
				"S"))
		{
			// defect 9502, 9651
			laTempIndicatorData = getTireTypeCd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getTitleData().getTtlExmnIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getTtlExmnIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getTitleData().getTtlHotCkIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getTtlHotCkIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getTitleData().getTtlRevkdIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getTtlRevkdIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}
		// defect 11052 
		if (aaMFVeh.getTitleData().getTtlSignDate() != 0 && aiScenario == VTR275)
		{
			laTempIndicatorData = getTtlSignDate(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				"");
			 IndicatorData laIndiData =
				    (IndicatorData) lvVector.lastElement();
			 laIndiData.setDesc(laTempIndicatorData.getDesc());
			 laIndiData.setPriority(99); 
			 lvVector.set(lvVector.size() - 1, laIndiData);
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 11052 
		
		if (!UtilityMethods
			.isEmpty(aaMFVeh.getVehicleData().getVehOdmtrBrnd()))
		{
			// defect 9502, 9651
			laTempIndicatorData = getVehOdmtrBrnd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getVehicleData().getVinErrIndi() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getVinErrIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		// defect 9980 
		if (aaMFVeh.getTitleData().getUTVMislblIndi() != 0)
		{
			laTempIndicatorData = getUTVMislblIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}

		// defect 10376 
		// VTRTtlEmrgCd1 -> PvtLawEnfVehCd
		// VTRTtlEmrgCd2 -> NonTtlGolfCartCd
		// add VTRTtlEmrgCd3, 4
		if (!UtilityMethods
			.isEmpty(aaMFVeh.getTitleData().getPvtLawEnfVehCd()))
		{
			laTempIndicatorData = getPvtLawEnfVehCd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		if (!UtilityMethods
			.isEmpty(aaMFVeh.getTitleData().getNonTtlGolfCartCd()))
		{
			laTempIndicatorData = getNonTtlGolfCartCd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		if (!UtilityMethods
			.isEmpty(aaMFVeh.getTitleData().getVTRTtlEmrgCd3()))
		{
			laTempIndicatorData = getVTRTtlEmrgCd3(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		if (!UtilityMethods
			.isEmpty(aaMFVeh.getTitleData().getVTRTtlEmrgCd4()))
		{
			laTempIndicatorData = getVTRTtlEmrgCd4(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 10376

		if (!UtilityMethods
			.isEmpty(aaMFVeh.getRegData().getVTRRegEmrgCd1()))
		{
			laTempIndicatorData = getVTRRegEmrgCd1(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		if (!UtilityMethods
			.isEmpty(aaMFVeh.getRegData().getVTRRegEmrgCd2()))
		{
			laTempIndicatorData = getVTRRegEmrgCd2(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 9980 

		// defect 
		// Restored 
		if (aaMFVeh.getTitleData().getCcoIssueDate() > 0)
		{
			// defect 9502, 9651
			// defect 9610
			laTempIndicatorData = getCcoIssueDate(aaMFVeh, aiScenario);
			// end defect 9610
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				"");
			IndicatorData laIndiData =
				(IndicatorData) lvVector.lastElement();

			laIndiData.setDesc(laTempIndicatorData.getDesc());
			lvVector.set(lvVector.size() - 1, laIndiData);
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 

		if (aaMFVeh.getRegData().getClaimComptCntyNo() > 0)
		{
			// defect 9502, 9651
			// defect 9610
			laTempIndicatorData =
				getClaimComptCntyNo(aaMFVeh, aiScenario);
			// end defect 9610
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				"");

			IndicatorData laIndiData =
				(IndicatorData) lvVector.lastElement();

			laIndiData.setDesc(laTempIndicatorData.getDesc());
			lvVector.set(lvVector.size() - 1, laIndiData);
			// end defect 9502, 9651
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getJnkIndi() != 0)
		{
			for (int k = 0; k < aaMFVeh.getVctSalvage().size(); k++)
			{
				SalvageData laSalvTempData =
					(SalvageData) aaMFVeh.getVctSalvage().get(k);
				if (laSalvTempData == null)
				{
					laSalvTempData = new SalvageData();
				}

				// defect 11097
				if (laSalvTempData.getSalvIndi() == 1)
				{
					laTempIndicatorData = getSlvgIndi(aaMFVeh);
				}
				else
				{
					// defect 9502, 9651
					laTempIndicatorData = getJnkIndi(aaMFVeh);
				}
				// end defect 11097

				// defect 10491 
				laTempIndicatorData.setJnkAttrib(true);
				// end defect 10491

				addIndi(
					lvVector,
					asTransCd,
					aiScenario,
					laTempIndicatorData.getIndiName(),
					laTempIndicatorData.getDesc());
				// end defect 9502, 9651

				if (laSalvTempData.getSlvgDt() != null)
				{
					// defect 9502, 9651
					IndicatorData laIndiData =
						(IndicatorData) lvVector.lastElement();

					getSlvgDt(laSalvTempData, laIndiData, aiScenario);

					// defect 10491 
					laIndiData.setJnkAttrib(true);
					// end defect 10491 

					lvVector.set(lvVector.size() - 1, laIndiData);
					// end defect 9502, 9651 
				}

				if (laSalvTempData.getSlvgCd() != 0)
				{
					// defect 9502, 9651
					IndicatorData laIndiData =
						getSlvgCd(laSalvTempData);
					addIndi(
						lvVector,
						asTransCd,
						aiScenario,
						laIndiData.getIndiName(),
						laIndiData.getDesc());
					// end defect 9502, 9651
					IndicatorData laLastIndicatorData =
						(IndicatorData) lvVector.lastElement();
					
					// defect 11052 
					if (aiScenario != VTR275)
					{
						laLastIndicatorData.setDesc(
								"   " + laLastIndicatorData.getDesc());
					}
					else
					{
						laLastIndicatorData.setDesc(
								" " + laLastIndicatorData.getDesc());
					}
					// end defect 11052 

					// defect 10491 
					laLastIndicatorData.setJnkAttrib(true);
					// end defect 10491

					lvVector.set(
						lvVector.size() - 1,
						laLastIndicatorData);
				}

				if ((laSalvTempData.getOthrStateCntry() != null
					&& laSalvTempData.getOthrGovtTtlNo() != null)
					&& (!laSalvTempData.getOthrStateCntry().equals("")
						|| !laSalvTempData.getOthrGovtTtlNo().equals("")))
				{
					IndicatorData laIndiData = new IndicatorData();
					IndicatorData laLastIndiData =
						(IndicatorData) lvVector.lastElement();
					laIndiData.setPriority(
						laLastIndiData.getPriority());
					
					// defect 11052 
					if (aiScenario != VTR275)
					{

						laIndiData.setDesc(
								"   [" + laSalvTempData.getOthrStateCntry());

						// only add title number if it exists.
						if (laSalvTempData.getOthrGovtTtlNo() != null
								&& !laSalvTempData.getOthrGovtTtlNo().equals(""))
						{
							laIndiData.setDesc(
									laIndiData.getDesc()
									+ " "
									+ laSalvTempData.getOthrGovtTtlNo());
						}

						laIndiData.setDesc(laIndiData.getDesc() + "]");
					}
					else
					{
						laIndiData.setDesc(
								":" + laSalvTempData.getOthrStateCntry());

						// only add title number if it exists.
						if (laSalvTempData.getOthrGovtTtlNo() != null
								&& !laSalvTempData.getOthrGovtTtlNo().equals(""))
						{
							laIndiData.setDesc(
									laIndiData.getDesc()
									+ laSalvTempData.getOthrGovtTtlNo());
						}
					}
					// end defect 11052 

					// defect 10491 
					laIndiData.setJnkAttrib(true);
					// end defect 10491 

					lvVector.add(laIndiData);
				}
				
				if (laSalvTempData.getOwnrEvdncCd() != 0)
				{
					// defect 11052 
					OwnershipEvidenceCodesData laOwnrEvidCdData = 
						OwnershipEvidenceCodesCache.getOwnrEvidCd(laSalvTempData.getOwnrEvdncCd());

					if (laOwnrEvidCdData != null)
					{
						// end defect 11052
						
						IndicatorData laIndiData =
							new IndicatorData();
						IndicatorData laLastIndiData =
							(IndicatorData) lvVector.lastElement();
						laIndiData.setPriority(
								laLastIndiData.getPriority());

						laIndiData.setDesc(
								"   "
								+ laOwnrEvidCdData
								.getOwnrshpEvidCdDesc());

						// defect 10491 
						laIndiData.setJnkAttrib(true);
						// end defect 10491 

						lvVector.add(laIndiData);
					}
				}

				if (laSalvTempData.getSalvYardNo() != null
					&& !laSalvTempData.getSalvYardNo().equals(""))
				{
					IndicatorData laIndiData = new IndicatorData();
					IndicatorData laLastIndiData =
						(IndicatorData) lvVector.lastElement();
					laIndiData.setPriority(
						laLastIndiData.getPriority());
					// defect 11052 
					if (aiScenario != VTR275)
					{
						laIndiData.setDesc(
								"   "
								+ "SLVG. YARD: ["
								+ laSalvTempData.getSalvYardNo()
								+ "]");
					}
					else
					{
						laIndiData.setDesc(
								" "
								+ "SLVG. YARD:"
								+ laSalvTempData.getSalvYardNo()); 
					}
					// end defect 11052 
					
					//defect 10491 
					laIndiData.setJnkAttrib(true);
					// end defect 10491 

					lvVector.add(laIndiData);
				}

				if (laSalvTempData.getLienNotRlsedIndi() != 0)
				{
					// defect 9502, 9651
					laTempIndicatorData =
						getLienNotRlsedIndi(laSalvTempData);
					addIndi(
						lvVector,
						asTransCd,
						aiScenario,
						laTempIndicatorData.getIndiName(),
						laTempIndicatorData.getDesc());
					// end defect 9502, 9651
					IndicatorData laLastIndiData =
						(IndicatorData) lvVector.lastElement();
					laLastIndiData.setDesc(
						"   " + laLastIndiData.getDesc());

					//defect 10491 
					laLastIndiData.setJnkAttrib(true);
					// end defect 10491 

					lvVector.set(lvVector.size() - 1, laLastIndiData);
				}

				if (laSalvTempData.getLienNotRlsedIndi2() != 0)
				{
					// defect 9502, 9651
					laTempIndicatorData =
						getLienNotRlsedIndi2(laSalvTempData);
					addIndi(
						lvVector,
						asTransCd,
						aiScenario,
						laTempIndicatorData.getIndiName(),
						laTempIndicatorData.getDesc());
					// end defect 9502, 9651
					IndicatorData laLastIndiData =
						(IndicatorData) lvVector.lastElement();
					laLastIndiData.setDesc(
						"   " + laLastIndiData.getDesc());

					// defect 10491 
					laLastIndiData.setJnkAttrib(true);
					// end defect 10491 

					lvVector.set(lvVector.size() - 1, laLastIndiData);
					addVTR275Separator(aiScenario,lvVector);
				}

				if (laSalvTempData.getLienNotRlsedIndi3() != 0)
				{
					// defect 9502, 9651
					laTempIndicatorData =
						getLienNotRlsedIndi3(laSalvTempData);
					addIndi(
						lvVector,
						asTransCd,
						aiScenario,
						laTempIndicatorData.getIndiName(),
						laTempIndicatorData.getDesc());
					// end defect 9502, 9651
					IndicatorData laLastIndiData =
						(IndicatorData) lvVector.lastElement();
					laLastIndiData.setDesc(
						"   " + laLastIndiData.getDesc());

					// defect 10491 
					laLastIndiData.setJnkAttrib(true);
					// end defect 10491 

					lvVector.set(lvVector.size() - 1, laLastIndiData);
				}
				addVTR275Separator(aiScenario,lvVector);
			} // end for loop
		} // end check of JNKCD

		if (aaMFVeh.getTitleData().getLegalRestrntNo() != null
			&& !aaMFVeh.getTitleData().getLegalRestrntNo().equals(""))
		{
			// defect 9502, 9651
			laTempIndicatorData = getLegalRestrntNo(aaMFVeh,aiScenario);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				"");
			IndicatorData laLastIndiData =
				(IndicatorData) lvVector.lastElement();
			IndicatorData laIndiData = new IndicatorData();
			laIndiData.setDesc(laTempIndicatorData.getDesc());
			// end defect 9502, 9651
			laIndiData.setPriority(laLastIndiData.getPriority());
			lvVector.add(laIndiData);
			addVTR275Separator(aiScenario,lvVector);

		}

		if (aaMFVeh.getRegData().getNotfyngCity() != null
			&& !aaMFVeh.getRegData().getNotfyngCity().equals(""))
		{
			// defect 9502, 9651
			// defect 9610
			laTempIndicatorData = getNotfyngCity(aaMFVeh, aiScenario);
			// end defect 9610
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				"");
			IndicatorData laLastIndiData =
				(IndicatorData) lvVector.lastElement();
			IndicatorData laIndiData = new IndicatorData();
			laIndiData.setDesc(
				laLastIndiData.getDesc()
					+ laTempIndicatorData.getDesc());
			laIndiData.setPriority(laLastIndiData.getPriority());
			lvVector.set(lvVector.size() - 1, laIndiData);
			addVTR275Separator(aiScenario,lvVector);
			// end defect 9502, 9651
		}

		if (aaMFVeh.getVehicleData().getRecondCd() != null
			&& !aaMFVeh.getVehicleData().getRecondCd().equals("0"))
		{
			// defect 9502, 9651
			laTempIndicatorData = getRecondCd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				String.valueOf(aaMFVeh.getVehicleData().getRecondCd()));
			
			if (aaMFVeh.getVehicleData().getRecondCd().equals("4"))
			{
				IndicatorData laLastIndiData =
					(IndicatorData) lvVector.lastElement();
				IndicatorData laIndiData = new IndicatorData();
				IndicatorData laTempIndiData =
					getRecondCdState(aaMFVeh,aiScenario);
				laIndiData.setDesc(laTempIndiData.getDesc());
				// end defect 9502, 9651
				laIndiData.setPriority(laLastIndiData.getPriority());
				lvVector.add(laIndiData);
				addVTR275Separator(aiScenario,lvVector);
			}
			
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getRegData().getRegPltOwnrName() != null
			&& !aaMFVeh.getRegData().getRegPltOwnrName().equals(""))
		{
			// defect 9502, 9651
			IndicatorData laIndiData = getRegPltOwnrName(aaMFVeh,aiScenario);
			// defect 9650
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laIndiData.getIndiName(),
				"");
			// end defect 9650
			IndicatorData laLastIndiData =
				(IndicatorData) lvVector.lastElement();
			IndicatorData laIndiData2 = new IndicatorData();
			// defect 9650
			laIndiData2.setDesc(laIndiData.getDesc());
			// end defect 9650
			laIndiData2.setPriority(laLastIndiData.getPriority());
			lvVector.add(laIndiData2);
			addVTR275Separator(aiScenario,lvVector);
			// end defect 9502, 9651
		}

		if (aaMFVeh.getVehicleData().getReplicaVehMk() != null
			&& !aaMFVeh.getVehicleData().getReplicaVehMk().equals("")
			&& aaMFVeh.getVehicleData().getReplicaVehModlYr() != 0)
		{
			// defect 9502, 9651
			IndicatorData laIndiData = getReplicaVehMk(aaMFVeh,aiScenario);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laIndiData.getIndiName(),
				"");
			IndicatorData laIndiData2 =
				(IndicatorData) lvVector.lastElement();
			String lsIndiDesc = laIndiData2.getDesc();
			lsIndiDesc = lsIndiDesc + laIndiData.getDesc();
			laIndiData2.setDesc(lsIndiDesc);
			lvVector.set(lvVector.size() - 1, laIndiData2);
			addVTR275Separator(aiScenario,lvVector);
			// end defect 9502, 9651
		}

		if (aaMFVeh.getTitleData().getSurrTtlDate() != 0)
		{
			// defect 9502, 9651
			IndicatorData laIndiData = getSurrTtlDate(aaMFVeh,aiScenario);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laIndiData.getIndiName(),
				"");
			IndicatorData laLastIndiData =
				(IndicatorData) lvVector.lastElement();
			IndicatorData laIndiData2 = new IndicatorData();
			laIndiData2.setDesc(laIndiData.getDesc());
			laIndiData2.setPriority(laLastIndiData.getPriority());
			lvVector.add(laIndiData2);
			addVTR275Separator(aiScenario,lvVector);
			// end defect 9502, 9651
		}

		if (aaMFVeh.getTitleData() != null
			&& aaMFVeh.getTitleData().getTtlProcsCd() != null
			&& !aaMFVeh.getTitleData().getTtlProcsCd().equals(""))
		{
			// defect 9502, 9651
			IndicatorData laIndiData = getTtlProcsCd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laIndiData.getIndiName(),
				laIndiData.getDesc());

			if (aaMFVeh.getTitleData().getTtlProcsCd().equals("R"))
			{
				IndicatorData laLastIndiData =
					(IndicatorData) lvVector.lastElement();

				// if there is a rejection office, pick it up.
				IndicatorData laIndiData1 = getTtlRejctnOfc(aaMFVeh,aiScenario);
				if (laIndiData1.getDesc() != null
					&& laIndiData1.getDesc().length() > 0)
				{
					laIndiData1.setDesc(laIndiData1.getDesc());
					laIndiData1.setPriority(
						laLastIndiData.getPriority());
					laIndiData1.setStopCode(
						laLastIndiData.getStopCode());
					lvVector.add(laIndiData1);
					
					// if there is a rejection date, pick it up
					if (aaMFVeh.getTitleData().getTtlRejctnDate() != 0)
					{
						IndicatorData laLastIndiData2 =
							(IndicatorData) lvVector.lastElement();
						IndicatorData laIndiData2 =
							getTtlRejctnDate(aaMFVeh,aiScenario);
						laIndiData2.setDesc(laIndiData2.getDesc());
						laIndiData2.setPriority(
							laLastIndiData2.getPriority());
						laIndiData2.setStopCode(
							laLastIndiData2.getStopCode());
						lvVector.add(laIndiData2);
						// end defect 9502, 9651
					}
				}
			}
			addVTR275Separator(aiScenario,lvVector);
		}

		if (aaMFVeh.getTitleData().getVehSoldDate() != 0)
		{
			// defect 9502, 9651
			laTempIndicatorData = getVehSoldDate(aaMFVeh,aiScenario);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				"");

			IndicatorData laIndiData =
				(IndicatorData) lvVector.lastElement();

			laIndiData.setDesc(
				laIndiData.getDesc() + laTempIndicatorData.getDesc());
			lvVector.set(lvVector.size() - 1, laIndiData);
			addVTR275Separator(aiScenario,lvVector);
			// end defect 9502, 9651
		}

		// defect 9712, 9651 
		if (aaMFVeh.getTitleData().getETtlCd() >1)
		{
			laTempIndicatorData = getETtlCd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		// defect 10841
		if (aaMFVeh.getTitleData().getETtlPrntDate() != 0)
		{
			laTempIndicatorData = getETtlPrntDate(aaMFVeh, aiScenario);

			addIndi(
					lvVector,
					asTransCd,
					aiScenario,
					laTempIndicatorData.getIndiName(),
					"");
			IndicatorData laIndiData =
				(IndicatorData) lvVector.lastElement();

			laIndiData.setDesc(laTempIndicatorData.getDesc());
			lvVector.set(lvVector.size() - 1, laIndiData);
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 10841 
		
		// defect 10507 
		if (aaMFVeh.getSpclPltRegisData() != null
			&& aaMFVeh.getSpclPltRegisData().getElectionPndngIndi() != 0)
		{
			laTempIndicatorData = getElectionPndingIndi(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 10507 

		if (aaMFVeh.getRegData().getPrismLvlCd() != null
			&& aaMFVeh.getRegData().getPrismLvlCd().trim().length() != 0)
		{
			laTempIndicatorData = getPrismLvlCd(aaMFVeh);
			addIndi(
				lvVector,
				asTransCd,
				aiScenario,
				laTempIndicatorData.getIndiName(),
				laTempIndicatorData.getDesc());
			addVTR275Separator(aiScenario,lvVector);
		}
		// end defect 9712, 9651  

		// defect 11132 / 11160 
		// Present Major Color for POS Receipt Only  
		// Remove Minor Color completely 
		// defect 10712 
		// Major Color 
		if (aiScenario == RECEIPT)
		{
			if (!UtilityMethods
					.isEmpty(aaMFVeh.getVehicleData().getVehMjrColorCd()))
			{
				laTempIndicatorData = getVehMjrColorCd(aaMFVeh);
				addIndi(
						lvVector,
						asTransCd,
						aiScenario,
						laTempIndicatorData.getIndiName(),
						laTempIndicatorData.getDesc());
			}
		}
		// end defect 11160 
		//
		//		// Minor Color
		//		if (!UtilityMethods
		//			.isEmpty(aaMFVeh.getVehicleData().getVehMnrColorCd()))
		//		{
		//			laTempIndicatorData = getVehMnrColorCd(aaMFVeh);
		//			addIndi(
		//				lvVector,
		//				asTransCd,
		//				aiScenario,
		//				laTempIndicatorData.getIndiName(),
		//				laTempIndicatorData.getDesc());
		//		}
		// end defect 10712 
		// end defect 11132 
	
		int i = 0;
		while (i < lvVector.size())
		{
			// defect 11052 
			if (lvVector.get(i) instanceof IndicatorData)
			{
				IndicatorData laIndiData = (IndicatorData) lvVector.get(i); 
				if (laIndiData.getPriority() == 0)
				{
					if (!(aiScenario == VTR275 && !UtilityMethods.isEmpty(laIndiData.getDesc()) 
							&& laIndiData.getDesc().toUpperCase().startsWith("DATE OF ASSIGNMENT")))
					{
						lvVector.remove(i);
					}
					// end defect 11052 
					else
					{
						i++;
					}
				}
				else
				{
					i++; 
				}
			}
		}
		Collections.sort(lvVector);
		return lvVector;
	}

	/**
	 * Return the Indictors for V21 Processing.
	 * 
	 * <p>V21 wants all indicators regardless of setting.  
	 *
	 * @param  aaMFVeh MFVehicleData
	 * @param  aaTransCd String
	 * @param  aiScenario int
	 * @return Vector
	 */
	public Vector getIndicatorsV21(
		MFVehicleData aaMFVeh,
		String asTransCd,
		int aiScenario)
	{
		Vector lvVector = new Vector();

		// defect 9581
		//	Replace LemonLaw with ChildSupportIndi
		// process ChildSupportIndi
		IndicatorData laTempIndicatorDataCS =
			getChildSupportIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataCS);
		// end defect 9581

		// process AgncyLoandIndi
		IndicatorData laTempIndicatorDataAL =
			getAgncyLoandIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataAL);

		// defect 10050 
		// Apprehended County No is not on MF Record 
		// process ApprndCntyNo
		//		IndicatorData laTempIndicatorDataAL2 =
		//			getApprndCntyNo(aaMFVeh);
		//		lvVector.addElement(laTempIndicatorDataAL2);
		// end defect 10050 

		// process BndedTtlCd
		IndicatorData laTempIndicatorDataBT = getBndedTtlCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataBT);

		// process DieselIndi
		IndicatorData laTempIndicatorDataD = getDieselIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataD);

		// process DotStndrdsIndi
		IndicatorData laTempIndicatorDataDS =
			getDotStndrdsIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataDS);

		// process DpsSaftySuspnIndi
		IndicatorData laTempIndicatorDataDSS =
			getDpsSaftySuspnIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataDSS);

		// process DpsStlnIndi
		IndicatorData laTempIndicatorDataDSI = getDpsStlnIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataDSI);

		// end defect 10507 
		// process ElectionPndngIndi 
		IndicatorData laTempIndicatorDataEPI =
			getElectionPndingIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataEPI);
		// end defect 10507 

		// process EmissionSourceCd
		IndicatorData laTempIndicatorDataES =
			getEmissionSourceCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataES);

		// process ExmptIndi
		IndicatorData laTempIndicatorDataE = getExmptIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataE);

		// process FloodDmgeIndi
		IndicatorData laTempIndicatorDataFD = getFloodDmgeIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataFD);

		// process FxdWtIndi
		IndicatorData laTempIndicatorDataFW = getFxdWtIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataFW);

		// process GovtOwndIndi
		IndicatorData laTempIndicatorDataGO = getGovtOwndIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataGO);

		// process HvyVehUseTaxIndi
		IndicatorData laTempIndicatorDataHVUT =
			getHvyVehUseTaxIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataHVUT);

		// process InspectnWaivedIndi
		IndicatorData laTempIndicatorDataIW =
			getInspectnWaivedIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataIW);

		// process NotfyngCity
		// defect 9610
		IndicatorData laTempIndicatorDataNC =
			getNotfyngCity(aaMFVeh, aiScenario);
		// end defect 9610
		lvVector.addElement(laTempIndicatorDataNC);

		// process PltSeizedIndi
		IndicatorData laTempIndicatorDataPS = getPltSeizedIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataPS);

		// defect 9860 
		// LemonLawIndi 
		IndicatorData laTempIndicatorDataLL = getLemonLawIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataLL);
		// end defect 9860 

		// process PriorCCOIssueIndi
		IndicatorData laTempIndicatorDataPCCOI =
			getPriorCCOIssueIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataPCCOI);

		// process PrivacyOptCd
		// defect 10246 
		IndicatorData laTempIndicatorDataPO = getPrivacyOptCd();
		// end defect 10246 
		lvVector.addElement(laTempIndicatorDataPO);

		// process PrmtReqrdIndi
		IndicatorData laTempIndicatorDataPR = getPrmtReqrdIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataPR);

		// process ReContIndi
		IndicatorData laTempIndicatorDataRC = getReContIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataRC);

		// process RegHotCkIndi
		IndicatorData laTempIndicatorDataRHC = getRegHotCkIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataRHC);

		// process RegInvldIndi
		// defetc 8329
		//	Add parm trans code to getRegInvldIndi()
		IndicatorData laTempIndicatorDataRI =
			getRegInvldIndi(aaMFVeh, asTransCd);
		// end defect 8329
		lvVector.addElement(laTempIndicatorDataRI);

		// process RegRefAmt
		IndicatorData laTempIndicatorDataRRA = getRegRefAmt(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataRRA);

		// process RenwlMailRtrnIndi
		IndicatorData laTempIndicatorDataRMR =
			getRenwlMailRtrnIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataRMR);

		// defect 10508 
		// process EMailRenwlReqCd 
		// defect 10376
		IndicatorData laTempIndicatorEMRCD =
			getEMailRenwlReqCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorEMRCD);
		// end defect 10376
		// end defect 10508  

		// process StkrSeizdIndi
		IndicatorData laTempIndicatorDataSS = getStkrSeizdIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataSS);

		// process SurvshpRightsIndi
		IndicatorData laTempIndicatorDataSR =
			getSurvshpRightsIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataSR);

		// process TireTypeCd
		IndicatorData laTempIndicatorDataTT = getTireTypeCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTT);

		// process TtlExmnIndi
		IndicatorData laTempIndicatorDataTE = getTtlExmnIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTE);

		// process TtlHotCkIndi
		IndicatorData laTempIndicatorDataTHC = getTtlHotCkIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTHC);

		// process TtlRevkdIndi
		IndicatorData laTempIndicatorDataTR = getTtlRevkdIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTR);

		// process VehOdmtrBrnd
		IndicatorData laTempIndicatorDataVOB = getVehOdmtrBrnd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataVOB);

		// process VinErrIndi
		IndicatorData laTempIndicatorDataVE = getVinErrIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataVE);

		// process CcoIssueDate
		// defect 9610
		IndicatorData laTempIndicatorDataCID =
			getCcoIssueDate(aaMFVeh, aiScenario);
		// end defect 9610
		lvVector.addElement(laTempIndicatorDataCID);

		// process ClaimComptCntyNo
		// defect 9610
		IndicatorData laTempIndicatorDataCCCN =
			getClaimComptCntyNo(aaMFVeh, aiScenario);
		// end defect 9610
		lvVector.addElement(laTempIndicatorDataCCCN);

		// process LegalRestrntNo
		IndicatorData laTempIndicatorDataLRN =
			getLegalRestrntNo(aaMFVeh,aiScenario);
		lvVector.addElement(laTempIndicatorDataLRN);

		// process VehSoldDate
		IndicatorData laTempIndicatorDataVS = getVehSoldDate(aaMFVeh,aiScenario);
		lvVector.addElement(laTempIndicatorDataVS);

		// process RecondCd
		IndicatorData laTempIndicatorDataRCD = getRecondCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataRCD);

		// process RegPltOwnrName
		IndicatorData laTempIndicatorDataRON =
			getRegPltOwnrName(aaMFVeh,aiScenario);
		lvVector.addElement(laTempIndicatorDataRON);

		// process ReplicaVehMk
		IndicatorData laTempIndicatorDataRVM = getReplicaVehMk(aaMFVeh,aiScenario);
		lvVector.addElement(laTempIndicatorDataRVM);

		// process SurrTtlDate
		IndicatorData laTempIndicatorDataSTD = getSurrTtlDate(aaMFVeh,aiScenario);
		lvVector.addElement(laTempIndicatorDataSTD);

		// process TtlProcsCd
		IndicatorData laTempIndicatorDataTPC = getTtlProcsCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTPC);

		// process TtlRejctnOfc
		IndicatorData laTempIndicatorDataTRO = getTtlRejctnOfc(aaMFVeh,aiScenario);
		lvVector.addElement(laTempIndicatorDataTRO);

		// process TtlRejctnDate
		IndicatorData laTempIndicatorDataTRD =
			getTtlRejctnDate(aaMFVeh,aiScenario);
		lvVector.addElement(laTempIndicatorDataTRD);

		// defect 9712 
		// process ETtlCd
		IndicatorData laTempIndicatorDataETTL = getETtlCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataETTL);
		
		// process PrismLvlCd
		IndicatorData laTempIndicatorDataPRSM = getPrismLvlCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataPRSM);
		// end defect 9712
		
		// defect 9980 
		IndicatorData laTempIndicatorDataUTV =
			getUTVMislblIndi(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataUTV);

		IndicatorData laTempIndicatorDataRegE1 =
			getVTRRegEmrgCd1(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataRegE1);

		IndicatorData laTempIndicatorDataRegE2 =
			getVTRRegEmrgCd2(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataRegE2);

		// defect 10376 
		// VTRTtlEmrgCd1 -> PvtLawEnfVehCd
		// VTRTtlEmrgCd2 -> NonTtlGolfCartCd
		// add VTRTtlEmrgCd3, 4
		IndicatorData laTempIndicatorDataTtlPvt =
			getPvtLawEnfVehCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTtlPvt);

		IndicatorData laTempIndicatorDataTtlNTGlfCt =
			getNonTtlGolfCartCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTtlNTGlfCt);

		IndicatorData laTempIndicatorDataTtlE3 =
			getVTRTtlEmrgCd3(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTtlE3);

		IndicatorData laTempIndicatorDataTtlE4 =
			getVTRTtlEmrgCd4(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataTtlE4);
		// end defect 10376 

		// defect 10712 
		IndicatorData laTempIndicatorDataMajorColor =
			getVehMjrColorCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataMajorColor);

		IndicatorData laTempIndicatorDataMinorColor =
			getVehMnrColorCd(aaMFVeh);
		lvVector.addElement(laTempIndicatorDataMinorColor);
		// end defect 10712

		processSalvageV21(aaMFVeh, lvVector);

		// sort and return the vector
		Collections.sort(lvVector);
		return lvVector;
	}

	/**
	 * Process Inspection Waived
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getInspectnWaivedIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"INSPECTNWAIVEDINDI",
			aaMFVeh.getTitleData().getInspectnWaivedIndi());
		// end defect 9651 
	}

	/**
	 * Get New Indicator Data
	 * 
	 * @param asIndiName
	 * @param asDesc
	 * @return IndicatorData 
	 */
	private static IndicatorData getNewIndicatorData(
		String asIndiName,
		String asDesc)
	{
		IndicatorData laIndictorData = new IndicatorData();
		laIndictorData.setIndiName(asIndiName);
		laIndictorData.setDesc(asDesc);
		return laIndictorData;
	}

	/**
	 * Get New Indicator Data
	 * 
	 * @param asIndiName
	 * @param asDesc
	 * @return IndicatorData 
	 */
	private static IndicatorData getNewIndicatorData(
		String asIndiName,
		int aiDesc)
	{
		IndicatorData laIndictorData = new IndicatorData();
		laIndictorData.setIndiName(asIndiName);
		laIndictorData.setDesc(String.valueOf(aiDesc));
		return laIndictorData;
	}

	/**
	 * Process Junked Indicator
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getJnkIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData("JNKINDI", aaMFVeh.getJnkIndi());
		// end defect 9651 
	}

	/**
	 * Process Legal Restraint Number
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getLegalRestrntNo(MFVehicleData aaMFVeh, int aiScenario)
	{
		// defect 9651 
		String lsIndiFieldValue = "";
		if (aaMFVeh.getTitleData().getLegalRestrntNo() != null
			&& !aaMFVeh.getTitleData().getLegalRestrntNo().equals(""))
		{
			// defect 11052 
			if (aiScenario != VTR275)
			{
				lsIndiFieldValue =
					"   ["
					+ aaMFVeh.getTitleData().getLegalRestrntNo()
					+ "]";
			}
			else
			{
				lsIndiFieldValue =
					" (#"
					+ aaMFVeh.getTitleData().getLegalRestrntNo()
					+ ")";
			}
			// end defect 11052 
		}

		return getNewIndicatorData("LEGALRESTRNTNO", lsIndiFieldValue);
		// end defect 9651 
	}

	/**
	 * Process Lemon Law
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getLemonLawIndi(MFVehicleData aaMFVeh)
	{
		return getNewIndicatorData(
			"LEMONLAWINDI",
			aaMFVeh.getTitleData().getLemonLawIndi());
	}

	/**
	 * Process Lien Not Released
	 * 
	 * @param aaSalvTempData
	 * @return IndicatorData
	 */
	private static IndicatorData getLienNotRlsedIndi(SalvageData aaSalvTempData)
	{
		// defect 9651 
		return getNewIndicatorData(
			"LIENNOTRLSEDINDI",
			aaSalvTempData.getLienNotRlsedIndi());
		// end defect 9651 
	}

	/**
	 * Process Lien 2 Not Released
	 * 
	 * @param aaSalvTempData
	 * @return IndicatorData
	 */
	private static IndicatorData getLienNotRlsedIndi2(SalvageData aaSalvTempData)
	{
		// defect 9651
		return getNewIndicatorData(
			"LIEN2NOTRLSEDINDI",
			aaSalvTempData.getLienNotRlsedIndi2());
		// end defect 9651 	
	}

	/**
	 * Process Lien 3 Not Released
	 * 
	 * @param laSalvTempData
	 * @return IndicatorData
	 */
	private static IndicatorData getLienNotRlsedIndi3(SalvageData aaSalvTempData)
	{
		// defect 9651 
		return getNewIndicatorData(
			"LIEN3NOTRLSEDINDI",
			aaSalvTempData.getLienNotRlsedIndi3());
		// end defect 9651 
	}

	/**
	 * Process NonTtlGolfCartCd
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData 
	 */
	private static IndicatorData getNonTtlGolfCartCd(MFVehicleData aaMFVeh)
	{
		String lsFieldValue = "";
		if (aaMFVeh.getTitleData().getNonTtlGolfCartCd() != null)
		{
			lsFieldValue =
				aaMFVeh
					.getTitleData()
					.getNonTtlGolfCartCd()
					.toUpperCase();
		}
		return getNewIndicatorData("NONTTLGOLFCARTCD", lsFieldValue);
	}

	/**
	 * Process Notifying City (Scofflaw).
	 * 
	 * @param aaMFVeh
	 * @param aiScenario
	 * @return IndicatorData
	 */
	private static IndicatorData getNotfyngCity(
		MFVehicleData aaMFVeh,
		int aiScenario)
	{
		// defect 9651 
		String lsFieldValue = "";

		if (aaMFVeh.getRegData().getNotfyngCity() != null
			&& !aaMFVeh.getRegData().getNotfyngCity().trim().equals(""))
		{
			// defect 11052 
			if (aiScenario == VTR275)
			{
				lsFieldValue = " ";
			}
			else if (aiScenario != V21)
			{
				lsFieldValue = "   ";
			}
			// end defect 11052 
			
			lsFieldValue =
				lsFieldValue
					+ aaMFVeh.getRegData().getNotfyngCity().trim();
		}
		return getNewIndicatorData("NOTFYNGCITY", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process Plates Seized
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getPltSeizedIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"PLTSSEIZDINDI",
			aaMFVeh.getRegData().getPltSeizedIndi());
		// end defect 9651 
	}

	/**
	 * Process Prior CCO Issued
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getPriorCCOIssueIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"PRIORCCOISSUEINDI",
			aaMFVeh.getTitleData().getPriorCCOIssueIndi());
		// end defect 9651 
	}

	/**
	 * Process PrismLvlCd 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getPrismLvlCd(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		String lsFieldValue = "";
		if (aaMFVeh.getRegData().getPrismLvlCd() != null)
		{
			lsFieldValue = aaMFVeh.getRegData().getPrismLvlCd().trim();
		}
		return getNewIndicatorData("PRISMLVLCD", lsFieldValue);
		// end defect 9651
	}

	/**
	 * Process Privacy Opt Out Code
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getPrivacyOptCd()
	{
		return getNewIndicatorData(
			"PRIVACYOPTCD",
			CommonConstant.DEFAULT_PRIVACY_OPT_CD);
	}

	/**
	 * Process Permit Required
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getPrmtReqrdIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"PRMTREQRDINDI",
			aaMFVeh.getVehicleData().getPrmtReqrdIndi());
		// end defect 9651 
	}

	/**
	 * Process PvtLawEnfVehCd 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData 
	 */
	private static IndicatorData getPvtLawEnfVehCd(MFVehicleData aaMFVeh)
	{
		String lsFieldValue = "";
		if (aaMFVeh.getTitleData().getPvtLawEnfVehCd() != null)
		{
			lsFieldValue =
				aaMFVeh
					.getTitleData()
					.getPvtLawEnfVehCd()
					.toUpperCase();
		}
		return getNewIndicatorData("PVTLAWENFVEHCD", lsFieldValue);
	}

	/**
	 * Process Reconditioned Code
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getRecondCd(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		String lsFieldValue = "";

		if (aaMFVeh.getVehicleData().getRecondCd() != null
			&& !aaMFVeh.getVehicleData().getRecondCd().equals("0"))
		{
			lsFieldValue =
				String.valueOf(aaMFVeh.getVehicleData().getRecondCd());
		}
		return getNewIndicatorData("RECONDCD", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process State for Reconditioned Code
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getRecondCdState(MFVehicleData aaMFVeh,int aiScenario)
	{
		// defect 9651 
		String lsFieldValue = "";

		if (aaMFVeh.getVehicleData().getRecondCd() != null
			&& aaMFVeh.getVehicleData().getRecondCd().equals("4")
			&& aaMFVeh.getTitleData().getSalvStateCntry() != null)
		{
			String lsStateCntry = aaMFVeh.getTitleData().getSalvStateCntry().trim();
			
			// defect 11052 
			if (aiScenario != VTR275)
			{
				lsFieldValue =
					"   ["
					+ lsStateCntry
					+ "]";
			}
			else
			{
				if (lsStateCntry.length() ==0) 
				{
					lsFieldValue = " []"; 
				}
				else
				{
					lsFieldValue =
						" "
						+ lsStateCntry;
				}
			}
			// end defect 11052 
		}
		return getNewIndicatorData("RECONDCD", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process ReconstructedIndi.
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getReContIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"RECONTINDI",
			aaMFVeh.getVehicleData().getReContIndi());
		// end defect 9651 
	}

	/**
	 * Process Regis. Hot Check Indi.
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getRegHotCkIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"REGHOTCKINDI",
			aaMFVeh.getRegData().getRegHotCkIndi());
		// end defect 9651 
	}

	/**
	 * Process Regis. Invalid
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getRegInvldIndi(
		MFVehicleData aaMFVeh,
		String asTransCd)
	{
		// defect 9651
		// Initialize variable with NON-DTA RegInvldIndi
		int liRegInvalidIndi = aaMFVeh.getRegData().getRegInvldIndi();

		// defect 8329
		// Reassign for DTA transcation
		// If DTA then get RegInvldIndi from DTARegInvlIndi
		if (asTransCd != null && UtilityMethods.isDTA(asTransCd))
		{
			liRegInvalidIndi = aaMFVeh.getRegData().getDTARegInvlIndi();
		}
		// end defect 8329 
		return getNewIndicatorData("REGINVLDINDI", liRegInvalidIndi);
		// end defect 9651 
	}

	/**
	 * Process Regis Plate Owner
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getRegPltOwnrName(MFVehicleData aaMFVeh,int aiScenario)
	{
		String lsFieldValue = "";
		String lsRegPltOwnrName = aaMFVeh.getRegData().getRegPltOwnrName();
		
		// defect 11052
		if (!UtilityMethods.isEmpty(lsRegPltOwnrName))
		{
			if (aiScenario!= VTR275)
			{
				lsFieldValue =
					"   [" + lsRegPltOwnrName + "]";
			}
			else
			{
				lsFieldValue= lsRegPltOwnrName; 
			}
			// end defect 11052 
		}
		return getNewIndicatorData("REGPLTOWNRNAME", lsFieldValue);
	}

	/**
	 * Process Regis. Refund Amount
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getRegRefAmt(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		String lsFieldValue = "";

		try
		{
			if (aaMFVeh.getRegData().getRegRefAmt() != null
				&& Double.parseDouble(
					aaMFVeh.getRegData().getRegRefAmt().toString())
					> 0.0)
			{
				// the length of the field will determine if the indicator 
				// is set.
				lsFieldValue =
					String.valueOf(
						Double.parseDouble(
							aaMFVeh
								.getRegData()
								.getRegRefAmt()
								.toString()));
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			// Intentionally Left Empty
		}

		return getNewIndicatorData("REGREFAMT", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process Renewal Mail Returned
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getRenwlMailRtrnIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"RENWLMAILRTRNINDI",
			aaMFVeh.getRegData().getRenwlMailRtrnIndi());
		// end defect 9651 
	}

	/**
	 * Process Replica Vehicle Make
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getReplicaVehMk(MFVehicleData aaMFVeh,int aiScenario)
	{
		// defect 9651 
		String lsFieldValue = "";
		if (aaMFVeh.getVehicleData().getReplicaVehMk() != null
			&& !aaMFVeh.getVehicleData().getReplicaVehMk().equals("")
			&& aaMFVeh.getVehicleData().getReplicaVehModlYr() != 0)
		{
			// defect 11052 
			if (aiScenario != VTR275)
			{
				lsFieldValue =
					" ["
					+ aaMFVeh.getVehicleData().getReplicaVehModlYr()
					+ " "
					+ aaMFVeh.getVehicleData().getReplicaVehMk()
					+ "]";
			}
			else
			{
				lsFieldValue =
					aaMFVeh.getVehicleData().getReplicaVehModlYr() 
					+ " " + aaMFVeh.getVehicleData().getReplicaVehMk(); 
			}
			// end defect 11052 
		}
		return getNewIndicatorData("REPLICAVEHMK", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process Salvage Code
	 * 
	 * @param aaSalvTempData
	 * @return IndicatorData
	 */
	private static IndicatorData getSlvgCd(SalvageData aaSalvTempData)
	{
		// defect 9651 
		return getNewIndicatorData("JNKCD", aaSalvTempData.getSlvgCd());
		// end defect 9651 
	}

	/**
	 * Process Salvage Date.
	 * 
	 * <p>Note that V21 needs to process different from POS.
	 * 
	 * @param aaSalvTempData
	 * @param aaIndiData
	 * @param aiScenario
	 */
	private static void getSlvgDt(
		SalvageData aaSalvTempData,
		IndicatorData aaIndiData,
		int aiScenario)
	{
		if (aaSalvTempData.getSlvgDt() != null)
		{
			if (aiScenario != V21)
			{
				if (aaIndiData.getDesc() == null)
				{
					aaIndiData.setDesc("");
				}
				// defect 11052 
				if (aiScenario == VTR275)
				{
					aaIndiData.setDesc(
							aaIndiData.getDesc()
								+ " "
								+ aaSalvTempData.getSlvgDt().toString());
				}
				else
				{
					aaIndiData.setDesc(
							aaIndiData.getDesc()
							+ "   ["
							+ aaSalvTempData.getSlvgDt().toString()
							+ "]");
				}
				// end defect 11052 
			}
			else
			{
				// defect 9651 
				aaIndiData.setDesc(
					"   ["
						+ aaSalvTempData.getSlvgDt().toString()
						+ "]");
				// end defect 9651 
			}
		}
	}

	/**
	 * Process Salvaged Indicator
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getSlvgIndi(MFVehicleData aaMFVeh)
	{
		return getNewIndicatorData("SALVINDI", 1);
	}

	/**
	 * Returns the Soft Stop Reason   
	 * 
	 * @param  avIndicatorData Vector
	 * @return String
	 */
	public static String getSoftStopReasons(Vector avIndicatorData)
	{
		String lsReasons = "";
		for (int i = 0; i < avIndicatorData.size(); i++)
		{
			IndicatorData laIndicatorData =
				(IndicatorData) avIndicatorData.get(i);

			if (laIndicatorData.getStopCode() != null
				&& laIndicatorData.getStopCode().equals("S"))
			{
				lsReasons += laIndicatorData.getDesc() + "\n";
			}
			// Remove last end of line ??? 
			//lsReasons = lsReasons.substring(0, lsReasons.length() - 1);
		}
		return lsReasons;
	}

	/**
	 * Process Sticker Seized
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getStkrSeizdIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651
		return getNewIndicatorData(
			"STKRSEIZDINDI",
			aaMFVeh.getRegData().getStkrSeizdIndi());
		// end defect 9651 
	}

	/**
	 * Process Title Surrendered Date
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getSurrTtlDate(MFVehicleData aaMFVeh,int aiScenario)
	{
		// defect 9651 
		String lsFieldValue = "";
		if (aaMFVeh.getTitleData().getSurrTtlDate() != 0)
		{

			RTSDate laRTSDate =
				new RTSDate(
					RTSDate.YYYYMMDD,
					aaMFVeh.getTitleData().getSurrTtlDate());
			
			String lsOthrStateCntry = aaMFVeh.getTitleData().getOthrStateCntry();
			
			// defect 11052 
			if (aiScenario!= VTR275)
			{
				lsFieldValue = "[";
				if (!UtilityMethods.isEmpty(lsOthrStateCntry))
				{
					lsFieldValue =
						lsFieldValue
						+ aaMFVeh.getTitleData().getOthrStateCntry()
						+ " ";
				}
				lsFieldValue = lsFieldValue + laRTSDate + "]";
			}
			else 
			{
				lsFieldValue = " " + UtilityMethods.nullSafe(lsOthrStateCntry) + " ON "
				+ laRTSDate; 
			}
			// end defect 11052 
		}
		return getNewIndicatorData("SURRTTLDATE", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process Survivorship Rights
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getSurvshpRightsIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"SURVSHPRIGHTSINDI",
			aaMFVeh.getTitleData().getSurvshpRightsIndi());
		// end defect 9651 
	}
	/**
	 * Process Survivorship Rights Name1 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getSurvshpRightsName1(MFVehicleData aaMFVeh)
	{
		return getNewIndicatorData(
			"SURVSHPRIGHTSNAME1",
			new String());
	}
	/**
	 * Process Survivorship Rights Name 2 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getSurvshpRightsName2(MFVehicleData aaMFVeh)
	{
		return getNewIndicatorData(
			"SURVSHPRIGHTSNAME2",
			new String());
	}
	

	/**
	 * Process Tire Type Code
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getTireTypeCd(MFVehicleData aaMFVeh)
	{
		// defect 9651
		String lsFieldValue = "";
		if (aaMFVeh.getRegData().getTireTypeCd() != null)
		{
			lsFieldValue =
				aaMFVeh.getRegData().getTireTypeCd().toUpperCase();
		}
		return getNewIndicatorData("TIRETYPECD", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process Title Exam
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getTtlExmnIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"TTLEXMNINDI",
			aaMFVeh.getTitleData().getTtlExmnIndi());
		// end defect 9651 
	}

	/**
	 * Process Title Hot Check 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getTtlHotCkIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651
		return getNewIndicatorData(
			"TTLHOTCKINDI",
			aaMFVeh.getTitleData().getTtlHotCkIndi());
		// end defect 9651 
	}

	/**
	 * Process Title Processed Code
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getTtlProcsCd(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		String lsFieldValue = "";
		if (aaMFVeh.getTitleData().getTtlProcsCd() != null)
		{
			lsFieldValue =
				aaMFVeh.getTitleData().getTtlProcsCd().toUpperCase();
		}

		return getNewIndicatorData("TTLPROCSCD", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process Title Rejection Date
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getTtlRejctnDate(MFVehicleData aaMFVeh,int aiScenario)
	{
		// defect 9651 
		String lsFieldValue = "";
		if (aaMFVeh.getTitleData().getTtlProcsCd() != null
			&& aaMFVeh.getTitleData().getTtlProcsCd().equals("R")
			&& aaMFVeh.getTitleData().getTtlRejctnOfc() > 0
			&& aaMFVeh.getTitleData().getTtlRejctnDate() > 0)
		{
			// defect 11052 
			if (aiScenario != VTR275)
			{
				lsFieldValue =
					"   ["
					+ new RTSDate(
							RTSDate.YYYYMMDD,
							aaMFVeh.getTitleData().getTtlRejctnDate())
				+ "]";
			}
			else
			{
				lsFieldValue = " "+
				new RTSDate(
						RTSDate.YYYYMMDD,
						aaMFVeh.getTitleData().getTtlRejctnDate());
			}
			// end defect 11052 
		}
		return getNewIndicatorData("TTLREJCTNDATE", lsFieldValue);
		// end defect 9651 
	}
	
	/**
	  * Process TtlSignDate   (VTR275 ONLY) 
	  * 
	  * @param aaMFVeh
	  * @param aiScenario
	  */
	 private static IndicatorData getTtlSignDate(
	  MFVehicleData aaMFVeh)
	 {
		 String lsFieldValue = "";

		 IndicatorDescriptionsData laIndiDescData =
			 IndicatorDescriptionsCache.getIndiDesc(
					 "TTLSIGNDATE",
			 "");

		 RTSDate laRTSDate =
			 new RTSDate(
					 RTSDate.YYYYMMDD,
					 aaMFVeh.getTitleData().getTtlSignDate());


		 if (laIndiDescData.getIndiDesc() != null)
		 {
			 lsFieldValue = laIndiDescData.getIndiDesc();
		 }

		 lsFieldValue = lsFieldValue.trim() +" "+ laRTSDate.toString(); 

		 return getNewIndicatorData("TTLSIGNDATE", lsFieldValue);
	 }
	 
	/**
	 * Process Title Rejection Office
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getTtlRejctnOfc(MFVehicleData aaMFVeh,int aiScenario)
	{
		// defect 9651 
		String lsFieldValue = "";

		if (aaMFVeh.getTitleData().getTtlProcsCd() != null
			&& aaMFVeh.getTitleData().getTtlProcsCd().equals("R")
			&& aaMFVeh.getTitleData().getTtlRejctnOfc() > 0)
		{
			OfficeIdsData laOfcIdsData =
				OfficeIdsCache.getOfcId(
					aaMFVeh.getTitleData().getTtlRejctnOfc());
			if (laOfcIdsData != null)
			{
				// defect 11052 
				if (aiScenario != VTR275)
				{
					lsFieldValue = "  " + laOfcIdsData.getOfcName();
				}
				else
				{
					lsFieldValue = ": "+laOfcIdsData.getOfcName().trim();
				}
				// end defect 11052 
			}
		}
		return getNewIndicatorData("TTLREJCTNOFC", lsFieldValue);
		// defect 9651 
	}

	/**
	 * Process Title Revoked
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getTtlRevkdIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"TTLREVKDINDI",
			aaMFVeh.getTitleData().getTtlRevkdIndi());
		// end defect 9651 
	}
	/**
	 * Process UTVMislblIndi
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getUTVMislblIndi(MFVehicleData aaMFVeh)
	{
		return getNewIndicatorData(
			"UTVMISLBLINDI",
			aaMFVeh.getTitleData().getUTVMislblIndi());
	}
	/**
	 * Return the Description of Vehicle Major Color Code 
	 * 
	 * @return IndicatorData
	 */
	private static IndicatorData getVehMjrColorCd(MFVehicleData aaMFVeh)
	{
		String lsFieldValue = "";
		String lsMjrColorCd =
			aaMFVeh.getVehicleData().getVehMjrColorCd();

		// If and only if Major Color is populated 
		if (!UtilityMethods.isEmpty(lsMjrColorCd))
		{
			lsFieldValue = lsMjrColorCd;
		}
		return getNewIndicatorData("VEHMJRCOLORCD", lsFieldValue);
	}

	/**
	 * Return the Description of Vehicle Minor Color Code 
	 * 
	 * @return IndicatorData
	 */
	private static IndicatorData getVehMnrColorCd(MFVehicleData aaMFVeh)
	{
		String lsFieldValue = "";
		String lsMnrColorCd =
			aaMFVeh.getVehicleData().getVehMnrColorCd();

		// If and only if Minor Color is populated 
		if (!UtilityMethods.isEmpty(lsMnrColorCd))
		{
			lsFieldValue = lsMnrColorCd;
		}
		return getNewIndicatorData("VEHMNRCOLORCD", lsFieldValue);
	}

	/**
	 * Process Vehicle Odometer Brand
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getVehOdmtrBrnd(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		String lsFieldValue = "";
		if (aaMFVeh.getVehicleData().getVehOdmtrBrnd() != null
			&& !aaMFVeh.getVehicleData().getVehOdmtrBrnd().equals(""))
		{
			lsFieldValue =
				aaMFVeh
					.getVehicleData()
					.getVehOdmtrBrnd()
					.toUpperCase();
		}
		return getNewIndicatorData("VEHODMTRBRND", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Return the Vehicle Sold Date Indicator.
	 * 
	 * @return IndicatorData
	 */
	private static IndicatorData getVehSoldDate(MFVehicleData aaMFVeh,int aiScenario)
	{
		// defect 9651 
		String lsFieldValue = "";

		if (aaMFVeh.getTitleData().getVehSoldDate() != 0)
		{
			// defect 11052 
			if (aiScenario != VTR275)
			{
				lsFieldValue =
					"  ["
					+ new RTSDate(
							RTSDate.YYYYMMDD,
							aaMFVeh.getTitleData().getVehSoldDate())
				+ "]";
			}
			else
			{
				lsFieldValue =
					" "
					+ new RTSDate(
							RTSDate.YYYYMMDD,
							aaMFVeh.getTitleData().getVehSoldDate());
			}
			// end defect 11052 
		}
		return getNewIndicatorData("VEHSOLDDATE", lsFieldValue);
		// end defect 9651 
	}

	/**
	 * Process Vin Error Indi.
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData
	 */
	private static IndicatorData getVinErrIndi(MFVehicleData aaMFVeh)
	{
		// defect 9651 
		return getNewIndicatorData(
			"VINERRINDI",
			aaMFVeh.getVehicleData().getVinErrIndi());
		// end defect 9651 
	}

	/**
	 * Process VTRRegEmrgCd1 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData 
	 */
	private static IndicatorData getVTRRegEmrgCd1(MFVehicleData aaMFVeh)
	{
		String lsFieldValue = "";
		if (aaMFVeh.getRegData().getVTRRegEmrgCd1() != null)
		{
			lsFieldValue =
				aaMFVeh.getRegData().getVTRRegEmrgCd1().toUpperCase();
		}
		return getNewIndicatorData("VTRREGEMRGCD1", lsFieldValue);
	}

	/**
	 * Process VTRRegEmrgCd2 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData 
	 */
	private static IndicatorData getVTRRegEmrgCd2(MFVehicleData aaMFVeh)
	{
		String lsFieldValue = "";
		if (aaMFVeh.getRegData().getVTRRegEmrgCd2() != null)
		{
			lsFieldValue =
				aaMFVeh.getRegData().getVTRRegEmrgCd2().toUpperCase();
		}
		return getNewIndicatorData("VTRREGEMRGCD2", lsFieldValue);
	}

	/**
	 * Process VTRTtlEmrgCd3 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData 
	 */
	private static IndicatorData getVTRTtlEmrgCd3(MFVehicleData aaMFVeh)
	{
		String lsFieldValue = "";
		if (aaMFVeh.getTitleData().getVTRTtlEmrgCd3() != null)
		{
			lsFieldValue =
				aaMFVeh.getTitleData().getVTRTtlEmrgCd3().toUpperCase();
		}
		return getNewIndicatorData("VTRTTLEMRGCD3", lsFieldValue);
	}

	/**
	 * Process VTRTtlEmrgCd4 
	 * 
	 * @param aaMFVeh
	 * @return IndicatorData 
	 */
	private static IndicatorData getVTRTtlEmrgCd4(MFVehicleData aaMFVeh)
	{
		String lsFieldValue = "";
		if (aaMFVeh.getTitleData().getVTRTtlEmrgCd4() != null)
		{
			lsFieldValue =
				aaMFVeh.getTitleData().getVTRTtlEmrgCd4().toUpperCase();
		}
		return getNewIndicatorData("VTRTTLEMRGCD4", lsFieldValue);
	}

	/**
	 * Determines if any indicator has a hard stop 
	 * 
	 * @param  avVector Vector
	 * @return boolean
	 */
	public static boolean hasHardStop(Vector avVector)
	{
		// defect 9651
		boolean lbHardStop = false;
		for (int i = 0; i < avVector.size(); i++)
		{
			IndicatorData laIndiData = (IndicatorData) avVector.get(i);
			if (laIndiData.getStopCode() != null
				&& laIndiData.getStopCode().equals("H"))
			{
				lbHardStop = true;
				break;
			}
		}
		return lbHardStop;
		// end defect 9651
	}

	/**
	 * Determines if any indicator has a soft stop 
	 * 
	 * @param  avVector Vector
	 * @return boolean
	 */
	public static boolean hasSoftStop(Vector avVector)
	{
		// defect 9651 
		boolean lbSoftStop = false;
		for (int i = 0; i < avVector.size(); i++)
		{
			IndicatorData laIndiData = (IndicatorData) avVector.get(i);
			if (laIndiData.getStopCode() != null
				&& laIndiData.getStopCode().equals("S"))
			{
				lbSoftStop = true;
				break;
			}
		}
		return lbSoftStop;
		// end defect 9651 
	}

	/**
	 * Process Salvage Information for V21.
	 * 
	 * @param aaMFVeh
	 * @param avIndis
	 */
	private static void processSalvageV21(
		MFVehicleData aaMFVeh,
		Vector avIndis)
	{
		if (aaMFVeh.getJnkIndi() != 0
			&& aaMFVeh.getVctSalvage() != null
			&& aaMFVeh.getVctSalvage().size() > 0)
		{
			// process through each salvage record
			for (int k = 0; k < aaMFVeh.getVctSalvage().size(); k++)
			{
				SalvageData laSalvTempData =
					(SalvageData) aaMFVeh.getVctSalvage().get(k);

				if (laSalvTempData == null)
				{
					laSalvTempData = new SalvageData();
				}

				IndicatorData laTempIndicatorData = getJnkIndi(aaMFVeh);
				avIndis.add(laTempIndicatorData);

				Vector lvEvidenceCodes = null;
				try
				{
					lvEvidenceCodes =
						OwnershipEvidenceCodesCache.getOwnrEvidCds(
							OwnershipEvidenceCodesCache
								.SORT_BY_EVIDIMPRTNCDSORTNO);
				}
				catch (RTSException leRTSEx)
				{
					lvEvidenceCodes = new Vector();
				}

				// defect 9651
				// Put the Salvage Date on.
				if (laSalvTempData.getSlvgDt() != null)
				{
					IndicatorData laIndiData = new IndicatorData();
					laIndiData.setIndiName("JNKINDI");
					getSlvgDt(laSalvTempData, laIndiData, V21);
					avIndis.add(laIndiData);
				}

				// Put the Salvage Code on.
				if (laSalvTempData.getSlvgCd() != 0)
				{
					IndicatorData laIndiTempData =
						getSlvgCd(laSalvTempData);
					avIndis.add(laIndiTempData);
				}

				// return the Ownership Evidence Code
				if (laSalvTempData.getOwnrEvdncCd() != 0)
				{
					for (int i = 0; i < lvEvidenceCodes.size(); i++)
					{
						OwnershipEvidenceCodesData laOwnrEvidCdData =
							(
								OwnershipEvidenceCodesData) lvEvidenceCodes
									.get(
								i);
						if (laOwnrEvidCdData.getOwnrshpEvidCd()
							== laSalvTempData.getOwnrEvdncCd())
						{
							IndicatorData laIndiData =
								new IndicatorData();
							IndicatorData laLastIndiData =
								(IndicatorData) avIndis.lastElement();
							laIndiData.setIndiName(
								laLastIndiData.getIndiName());
							laIndiData.setPriority(
								laLastIndiData.getPriority());
							laIndiData.setDesc(
								"   "
									+ laOwnrEvidCdData
										.getOwnrshpEvidCdDesc());
							avIndis.add(laIndiData);
							break;
						}
					}
				}

				// return information on Other Title State / Country
				if ((laSalvTempData.getOthrStateCntry() != null
					&& laSalvTempData.getOthrGovtTtlNo() != null)
					&& (!laSalvTempData.getOthrStateCntry().equals("")
						|| !laSalvTempData.getOthrGovtTtlNo().equals("")))
				{
					IndicatorData laIndiData = new IndicatorData();
					IndicatorData laLastIndiData =
						(IndicatorData) avIndis.lastElement();
					laIndiData.setIndiName(
						laLastIndiData.getIndiName());
					laIndiData.setPriority(
						laLastIndiData.getPriority());

					laIndiData.setDesc(
						"   [" + laSalvTempData.getOthrStateCntry());

					// if the title number exists, add it
					if (laSalvTempData.getOthrGovtTtlNo() != null
						&& laSalvTempData
							.getOthrGovtTtlNo()
							.trim()
							.length()
							> 0)
					{
						laIndiData.setDesc(
							laIndiData.getDesc()
								+ " "
								+ laSalvTempData.getOthrGovtTtlNo());
					}

					laIndiData.setStopCode(
						laIndiData.getStopCode() + "]");

					avIndis.add(laIndiData);
				}

				// return the Salvage Yard Information
				if (laSalvTempData.getSalvYardNo() != null
					&& !laSalvTempData.getSalvYardNo().equals(""))
				{
					IndicatorData laIndiData = new IndicatorData();
					IndicatorData laLastIndiData =
						(IndicatorData) avIndis.lastElement();
					laIndiData.setIndiName(
						laLastIndiData.getIndiName());
					laIndiData.setPriority(
						laLastIndiData.getPriority());
					laIndiData.setDesc(
						"   "
							+ "SLVG. YARD: ["
							+ laSalvTempData.getSalvYardNo()
							+ "]");
					avIndis.add(laIndiData);
				}
				// end defect 9651

				// process LienNotRlsedIndi
				IndicatorData laTempIndicatorDataLR1 =
					getLienNotRlsedIndi(laSalvTempData);
				avIndis.addElement(laTempIndicatorDataLR1);

				// process Lien2NotRlsedIndi
				IndicatorData laTempIndicatorDataLR2 =
					getLienNotRlsedIndi2(laSalvTempData);
				avIndis.addElement(laTempIndicatorDataLR2);

				// process Lien3NotRlsedIndi
				IndicatorData laTempIndicatorDataLR3 =
					getLienNotRlsedIndi3(laSalvTempData);
				avIndis.addElement(laTempIndicatorDataLR3);
			}
		}
	}
}