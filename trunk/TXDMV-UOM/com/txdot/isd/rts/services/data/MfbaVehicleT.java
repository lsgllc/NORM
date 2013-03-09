package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * MfbaVehicleT.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/27/2006	Update offset to new format
 * 							deefct 6701 Ver Exempts
 * J Rue		10/20/2006	Move process from MfAccess
 * 							defect 6701 Ver Exempts
 * J Rue		10/20/2006	Change setVehicleDataFromMFT to
 * 							setVehicleDataFromMf
 * 							modify setVehicleDataFromMf()
 * 							defect 6701 Ver Exempts
 * J Rue		12/13/2006	Adjust class var RECONT_INDI_OFFSET 
 * 							from 1416 to 1414
 * 							defect 9063 Ver Exempts
 * J Rue		01/31/2007	Clean up JavaDoc
 * 							modify setVehicleDataFromMf()
 * 							defect 9086 Ver Special Plate
 * J Rue		02/02/2007	Add offsets and code
 * 							defect 9086 Ver Special Plates
 * J Rue		05/30/2007	Update offset from 1416 to 1414
 * 							defect 9063 Special Plates
 * J Rue		04/03/2008	New attributes for V21 MF version V
 * 							modify setVehicleDataFromMf()
 * 							defect 9557 Ver Defect_POS_A
 * K Harrell	04/14/2008	Adjust offsets for Cancel Plates DocNo
 * 							defect 9557 Ver Defect_POS_A 
 * J Rue		04/29/2008	Adjust offsets for Cancel Plates DocNo
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode
 * 							modify all class variables,
 * 								setVehicleDataFromMf()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		12/30/2008	Set offsets to MFInterfaceVersionCode U
 * 							modify all class variables
 * 							defect 9655 Special Plates
 * J Rue		01/12/2009	Adjust offsets for OrgNo, RegPltAge, 
 * 							DISSOCIATECD.  
 * 							modify all class variables
 * 							defect 9655 Special Plates
 * J Rue		02/24/2009	Change AuditTrailTransId offset to 1233
 * 							modify AUDIT_TRAIL_TRANS_ID_OFFSET
 * 							defect 9963 Ver Defect_POD_D
 * J Rue		02/26/2009	Adjust offsets for ELT
 * 							defect 9961 Ver Defect_POS_E
 * J Rue		10/05/2009	Adjust offsets for RCCPI version T
 * 							modify all class variables
 * 							defect 10244 Ver Defect_POS_G
 * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc.
 * 							defect 10244 Ver Defect_POS_G
 * M Reyes		03/03/2010	Copy version T to version U for MF
 * 							MF version U. Modified All.
 * 							defect 10378 Ver POS_640
 * K Harrell	06/22/2010	Copy version U to version V for MF
 * 							MF version V. Modified All.
 * 							defect 10492 Ver 6.5.0
 * M Reyes		03/03/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver 6.6.0
 * M Reyes		01/10/2011	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver 6.7.0
 * M Reyes		01/19/2011	add VEH_MJR_COLOR_CD_OFFSET,
 * 							VEH_MJR_COLOR_CD_LENGTH,
 * 							VEH_MNR_COLOR_CD_OFFSET,
 * 							VEH_MNR_COLOR_CD_LENGTH
 * 							defect 10710 Ver 6.7.0
 * K Harrell	11/01/2011	Copy version U to V for MF Version V 
 * 							defect 11045 Ver 6.9.0   
 * B Woodson	01/20/2012	Copy version V to T for MF Version T 
 * 							deleted default constructor
 * 							defect 11251 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into VehicleData. 
 * Used by the server side business layer. 
 *
 * @version	6.10.0 			01/20/2012
 * @author	Jeff Rue
 * <br>Creation Date:		09/26/2006 14:10:32
 */
public class MfbaVehicleT
{
	//define all lengths and offsets for Vehicle Data
	// Note: offset is -1 minus from CICS file

	// Create VehicleData
	VehicleData laVehicleData = new VehicleData();

	// Title Rec
	private static final int VIN_OFFSET = 1212;
	private static final int VIN_LENGTH = 22;
	private static final int AUDIT_TRAIL_TRANS_ID_OFFSET = 1234;
	private static final int AUDIT_TRAIL_TRANS_ID_LENGTH = 17;

	// Vehicle Data
	private static final int VEH_BDY_TYPE_OFFSET = 1309;
	private static final int VEH_BDY_TYPE_LENGTH = 2;
	private static final int VEH_CLASS_CD_OFFSET = 1311;
	private static final int VEH_CLASS_CD_LENGTH = 8;
	private static final int VEH_MK_OFFSET = 1319;
	private static final int VEH_MK_LENGTH = 4;
	private static final int VEH_MODL_YR_OFFSET = 1323;
	private static final int VEH_MODL_YR_LENGTH = 4;
	private static final int VEH_MODL_OFFSET = 1327;
	private static final int VEH_MODL_LENGTH = 3;
	private static final int VEH_TON_OFFSET = 1330;
	private static final int VEH_TON_LENGTH = 4;
	private static final int VEH_TON_DECIMAL = 2;
	private static final int REPLICA_VEH_MK_OFFSET = 1334;
	private static final int REPLICA_VEH_MK_LENGTH = 4;
	private static final int REPLICA_VEH_MODL_YR_OFFSET = 1338;
	private static final int REPLICA_VEH_MODL_YR_LENGTH = 4;
	private static final int TRLR_TYPE_OFFSET = 1342;
	private static final int TRLR_TYPE_LENGTH = 1;
	private static final int VEH_BDY_VIN_OFFSET = 1343;
	private static final int VEH_BDY_VIN_LENGTH = 22;
	private static final int VEH_LNGTH_OFFSET = 1365;
	private static final int VEH_LNGTH_LENGTH = 2;
	private static final int VEH_ODMTR_BRND_OFFSET = 1367;
	private static final int VEH_ODMTR_BRND_LENGTH = 1;
	private static final int VEH_ODMTR_READNG_OFFSET = 1368;
	private static final int VEH_ODMTR_READNG_LENGTH = 6;
	private static final int VEH_EMPTY_WT_OFFSET = 1406;
	private static final int VEH_EMPTY_WT_LENGTH = 6;
	
	// Indicators
	private static final int DIESEL_INDI_OFFSET = 1414;
	private static final int DIESEL_INDI_LENGTH = 1;
	private static final int DOT_STNDRDS_INDI_OFFSET = 1415;
	private static final int DOT_STNDRDS_INDI_LENGTH = 1;
	private static final int DPS_STLN_INDI_OFFSET = 1416;
	private static final int DPS_STLN_INDI_LENGTH = 1;
	private static final int FXD_WT_INDI_OFFSET = 1418;
	private static final int FXD_WT_INDI_LENGTH = 1;
	private static final int FLOOD_DMGE_INDI_OFFSET = 1419;
	private static final int FLOOD_DMGE_INDI_LENGTH = 1;
	private static final int PRMT_REQRD_INDI_OFFSET = 1424;
	private static final int PRMT_REQRD_INDI_LENGTH = 1;
	private static final int RECOND_CD_OFFSET = 1426;
	private static final int RECOND_CD_LENGTH = 1;
	private static final int RECONT_INDI_OFFSET = 1427;
	private static final int RECONT_INDI_LENGTH = 1;
	private static final int VIN_ERR_INDI_OFFSET = 1430;
	private static final int VIN_ERR_INDI_LENGTH = 1;
	//	defect 10710
	//	New Attributes for verion U
	private static final int VEH_MJR_COLOR_CD_OFFSET = 1688;
	private static final int VEH_MJR_COLOR_CD_LENGTH = 3;
	private static final int VEH_MNR_COLOR_CD_OFFSET = 1691;
	private static final int VEH_MNR_COLOR_CD_LENGTH = 3;
	// end defect 10710

	public static void main(String[] args)
	{
	}

	/**
	 * Parse MF data from CICS transaction, VehicleData. 
	 * TTL/REG
	 * Uses CICS PGMS R01, R02, R03, R04
	 * 
	 * @param asMfTtlRegResponse String
	 * @return VehicleData
	 */
	public VehicleData setVehicleDataFromMf(String asMfTtlRegResponse)
	{
		VehicleData laVehicleData = new VehicleData();
		String lsMfTtlRegResponse = asMfTtlRegResponse;
		MfAccess laMFAccess = new MfAccess();

		//Get Values from MfResponse and add to VehicleData
		laVehicleData.setAuditTrailTransId(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					AUDIT_TRAIL_TRANS_ID_OFFSET,
					AUDIT_TRAIL_TRANS_ID_OFFSET
						+ AUDIT_TRAIL_TRANS_ID_LENGTH)));

		laVehicleData.setDieselIndi(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							DIESEL_INDI_OFFSET,
							DIESEL_INDI_OFFSET + DIESEL_INDI_LENGTH)))
				.intValue());

		laVehicleData.setDotStndrdsIndi(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							DOT_STNDRDS_INDI_OFFSET,
							DOT_STNDRDS_INDI_OFFSET
								+ DOT_STNDRDS_INDI_LENGTH)))
				.intValue());

		laVehicleData.setDpsStlnIndi(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							DPS_STLN_INDI_OFFSET,
							DPS_STLN_INDI_OFFSET
								+ DPS_STLN_INDI_LENGTH)))
				.intValue());

		laVehicleData.setFloodDmgeIndi(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							FLOOD_DMGE_INDI_OFFSET,
							FLOOD_DMGE_INDI_OFFSET
								+ FLOOD_DMGE_INDI_LENGTH)))
				.intValue());

		laVehicleData.setFxdWtIndi(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							FXD_WT_INDI_OFFSET,
							FXD_WT_INDI_OFFSET + FXD_WT_INDI_LENGTH)))
				.intValue());

		laVehicleData.setPrmtReqrdIndi(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							PRMT_REQRD_INDI_OFFSET,
							PRMT_REQRD_INDI_OFFSET
								+ PRMT_REQRD_INDI_LENGTH)))
				.intValue());

		laVehicleData.setRecondCd(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					RECOND_CD_OFFSET,
					RECOND_CD_OFFSET + RECOND_CD_LENGTH)));

		laVehicleData.setReContIndi(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							RECONT_INDI_OFFSET,
							RECONT_INDI_OFFSET + RECONT_INDI_LENGTH)))
				.intValue());

		laVehicleData.setReplicaVehMk(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					REPLICA_VEH_MK_OFFSET,
					REPLICA_VEH_MK_OFFSET + REPLICA_VEH_MK_LENGTH)));

		laVehicleData.setReplicaVehModlYr(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							REPLICA_VEH_MODL_YR_OFFSET,
							REPLICA_VEH_MODL_YR_OFFSET
								+ REPLICA_VEH_MODL_YR_LENGTH)))
				.intValue());

		laVehicleData.setTrlrType(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					TRLR_TYPE_OFFSET,
					TRLR_TYPE_OFFSET + TRLR_TYPE_LENGTH)));

		laVehicleData.setVehBdyType(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					VEH_BDY_TYPE_OFFSET,
					VEH_BDY_TYPE_OFFSET + VEH_BDY_TYPE_LENGTH)));

		laVehicleData.setVehBdyVin(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					VEH_BDY_VIN_OFFSET,
					VEH_BDY_VIN_OFFSET + VEH_BDY_VIN_LENGTH)));

		laVehicleData.setVehClassCd(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					VEH_CLASS_CD_OFFSET,
					VEH_CLASS_CD_OFFSET + VEH_CLASS_CD_LENGTH)));

		laVehicleData.setVehEmptyWt(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							VEH_EMPTY_WT_OFFSET,
							VEH_EMPTY_WT_OFFSET
								+ VEH_EMPTY_WT_LENGTH)))
				.intValue());

		laVehicleData.setVehLngth(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							VEH_LNGTH_OFFSET,
							VEH_LNGTH_OFFSET + VEH_LNGTH_LENGTH)))
				.intValue());

		laVehicleData.setVehMk(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					VEH_MK_OFFSET,
					VEH_MK_OFFSET + VEH_MK_LENGTH)));

		laVehicleData.setVehModl(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					VEH_MODL_OFFSET,
					VEH_MODL_OFFSET + VEH_MODL_LENGTH)));

		laVehicleData.setVehModlYr(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							VEH_MODL_YR_OFFSET,
							VEH_MODL_YR_OFFSET + VEH_MODL_YR_LENGTH)))
				.intValue());

		laVehicleData.setVehOdmtrBrnd(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					VEH_ODMTR_BRND_OFFSET,
					VEH_ODMTR_BRND_OFFSET + VEH_ODMTR_BRND_LENGTH)));

		laVehicleData.setVehOdmtrReadng(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					VEH_ODMTR_READNG_OFFSET,
					VEH_ODMTR_READNG_OFFSET
						+ VEH_ODMTR_READNG_LENGTH)));

		laVehicleData.setVin(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
					VIN_OFFSET,
					VIN_OFFSET + VIN_LENGTH)));

		laVehicleData.setVinErrIndi(
			Integer
				.valueOf(
					laMFAccess.getStringFromZonedDecimal(
						lsMfTtlRegResponse.substring(
							VIN_ERR_INDI_OFFSET,
							VIN_ERR_INDI_OFFSET
								+ VIN_ERR_INDI_LENGTH)))
				.intValue());
		// defect 10710
		laVehicleData.setVehMjrColorCd(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
						VEH_MJR_COLOR_CD_OFFSET,
						VEH_MJR_COLOR_CD_OFFSET
						+ VEH_MJR_COLOR_CD_LENGTH)));
						
		laVehicleData.setVehMnrColorCd(
			laMFAccess.trimMfString(
				lsMfTtlRegResponse.substring(
						VEH_MNR_COLOR_CD_OFFSET,
						VEH_MNR_COLOR_CD_OFFSET
						+ VEH_MNR_COLOR_CD_LENGTH)));
		// end defect 10710

		// Add fields from VINA response
		StringBuffer laVehTonBuffer =
			new StringBuffer(
				laMFAccess.getStringFromZonedDecimal(
					lsMfTtlRegResponse.substring(
						VEH_TON_OFFSET,
						VEH_TON_OFFSET + VEH_TON_LENGTH)));

		if (laVehTonBuffer.charAt(0) != '-')
		{
			laVehTonBuffer.insert(
				VEH_TON_LENGTH - VEH_TON_DECIMAL,
				'.');
		}
		else
		{
			laVehTonBuffer.insert(
				VEH_TON_LENGTH - VEH_TON_DECIMAL + 1,
				'.');
		}
		Dollar laVehTon = new Dollar(laVehTonBuffer.toString());
		laVehicleData.setVehTon(laVehTon);

		return laVehicleData;

	}
}