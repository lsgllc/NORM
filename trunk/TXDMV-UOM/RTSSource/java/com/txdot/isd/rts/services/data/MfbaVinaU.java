package com.txdot.isd.rts.services.data;import java.util.Vector;import com.txdot.isd.rts.server.dataaccess.MfAccess;import com.txdot.isd.rts.services.util.constants.CommonConstant;/* * * MfbaVinaU.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * J Rue		10/16/2006	Update offset to new format * 							deefct 6701 Ver Exempts * J Rue		10/20/2006	Update JavaDoc * 							defect 6701 Ver Exempts * J Rue		02/02/2007	Add offsets and code * 							defect 9086 Ver Special Plates * J Rue		02/08/2007	Add remove header from data string * 							modify setMfVehicleDataFromVINAResponse() * 							defect 9086 Ver Special Plates * K Harrell	04/30/2008	Copied from MfbaVinaU *      					defect 9086 Ver Defect_POS_A * J Rue		06/04/2008	Set Ver to Defect_POS_A * 							defect 9557 Ver Defect_POS_A * J Rue		09/30/2008	Convert T from V MFInterfaceVersionCode * 							modify all class variables, * 							setMfVehicleDataFromVINAResponse() * 							defect 9833 Ver ELT_MfAccess * J Rue		02/26/2009	Update JavaDoc * 							defect 9961 Ver defect_POS_E * B Hargrove	10/22/2009	Consolidate U/V/T JavaDoc. * 							defect 10244 Ver Defect_POS_G * M Reyes		03/03/2010	Copy version T to version U for MF * 							MF version U. Modified All. * 							defect 10378 Ver POS_640 * K Harrell	06/08/2010	refactored MFAccess.getMfTtlRegResponce()  * 							to correct spelling.  * 							defect 10492 Ver 6.5.0  * K Harrell 	06/22/2010  Copy version U to version V for MF *        					MF version V. Modified All. *        					defect 10492 Ver 6.5.0 * M Reyes		10/06/2010	Copy version V to version T for MF * 							MF version T. Modified All. * 							defect 10595 Ver POS_660 * M Reyes		01/10/2011	Copy version T to version U for MF * 							defect 10710 Ver POS_670 * K Harrell	05/29/2011	add  VINA_VEHICLE_TYPE_OFFSET, * 							  VINA_VEHICLE_TYPE_LENGTH * 							modify setMfVehicleDataFromVINAResponse()  * 							defect 10844 Ver 6.8.0  * --------------------------------------------------------------------- *//** * Parse CICS transactions into MfVehicleData.  * Used by the server side business layer - Exempts/TERPS.  * * @version	6.8.0 			05/29/2011 * @author	Jeff Rue * <br>Creation Date:		10/16/2006 11:14:32 *//* &MfbaVinaU& */public class MfbaVinaU{	//define the offsets and lengths/* &MfbaVinaU'VIN_OFFSET& */	private final static int VIN_OFFSET = 0;/* &MfbaVinaU'VIN_LENGTH& */	private final static int VIN_LENGTH = 22;/* &MfbaVinaU'VEH_MK_OFFSET& */	private final static int VEH_MK_OFFSET = 22;/* &MfbaVinaU'VEH_MK_LENGTH& */	private final static int VEH_MK_LENGTH = 4;/* &MfbaVinaU'VEH_MODL_YR_OFFSET& */	private final static int VEH_MODL_YR_OFFSET = 26;/* &MfbaVinaU'VEH_MODL_YR_LENGTH& */	private final static int VEH_MODL_YR_LENGTH = 4;/* &MfbaVinaU'VEH_MODL_OFFSET& */	private final static int VEH_MODL_OFFSET = 30;/* &MfbaVinaU'VEH_MODL_LENGTH& */	private final static int VEH_MODL_LENGTH = 3;/* &MfbaVinaU'VINA_VEH_EMPTY_WT_OFFSET& */	private final static int VINA_VEH_EMPTY_WT_OFFSET = 33;/* &MfbaVinaU'VINA_VEH_EMPTY_WT_LENGTH& */	private final static int VINA_VEH_EMPTY_WT_LENGTH = 6;/* &MfbaVinaU'VEH_BDY_TYPE_OFFSET& */	private final static int VEH_BDY_TYPE_OFFSET = 39;/* &MfbaVinaU'VEH_BDY_TYPE_LENGTH& */	private final static int VEH_BDY_TYPE_LENGTH = 2;/* &MfbaVinaU'DIESEL_INDI_OFFSET& */	private final static int DIESEL_INDI_OFFSET = 41;/* &MfbaVinaU'DIESEL_INDI_LENGTH& */	private final static int DIESEL_INDI_LENGTH = 1;/* &MfbaVinaU'VINA_BEG_TON_OFFSET& */	private final static int VINA_BEG_TON_OFFSET = 42;/* &MfbaVinaU'VINA_BEG_TON_LENGTH& */	private final static int VINA_BEG_TON_LENGTH = 6;/* &MfbaVinaU'VINA_END_TON_OFFSET& */	private final static int VINA_END_TON_OFFSET = 48;/* &MfbaVinaU'VINA_END_TON_LENGTH& */	private final static int VINA_END_TON_LENGTH = 6;	// defect 10844 /* &MfbaVinaU'VINA_VEHICLE_TYPE_OFFSET& */	private final static int VINA_VEHICLE_TYPE_OFFSET = 54;/* &MfbaVinaU'VINA_VEHICLE_TYPE_LENGTH& */	private final static int VINA_VEHICLE_TYPE_LENGTH = 1;	// end defect 10844 	/**	 * 	 *//* &MfbaVinaU.MfbaVinaU& */	public MfbaVinaU()	{		super();	}/* &MfbaVinaU.main& */	public static void main(String[] args)	{	}	/**	 * Returns the VINA MFVehicle data after adding the populated 	 * VehicleData to it	 * 	 * @param lsMfVINAResponse String	 * @return MFVehicleData	 *//* &MfbaVinaU.setMfVehicleDataFromVINAResponse& */	public MFVehicleData setMfVehicleDataFromVINAResponse(String asMfVINAResponse)	{		//create VehicleData to set values		VehicleData laVehicleData = new VehicleData();		MfAccess laMFAccess = new MfAccess();		// Remove header from MfResponce		String lsMfVINAResponse =			laMFAccess.getMfTtlRegResponse(asMfVINAResponse);		if (lsMfVINAResponse != null			&& (!(lsMfVINAResponse				.equals(CommonConstant.STR_SPACE_EMPTY))))		{			laVehicleData.setVin(				laMFAccess.trimMfString(					lsMfVINAResponse.substring(						VIN_OFFSET,						VIN_OFFSET + VIN_LENGTH)));			laVehicleData.setVehMk(				laMFAccess.trimMfString(					lsMfVINAResponse.substring(						VEH_MK_OFFSET,						VEH_MK_OFFSET + VEH_MK_LENGTH)));			laVehicleData.setVehModl(				laMFAccess.trimMfString(					lsMfVINAResponse.substring(						VEH_MODL_OFFSET,						VEH_MODL_OFFSET + VEH_MODL_LENGTH)));			try			{				laVehicleData.setVehModlYr(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								lsMfVINAResponse.substring(									VEH_MODL_YR_OFFSET,									VEH_MODL_YR_OFFSET										+ VEH_MODL_YR_LENGTH)))						.intValue());			}			catch (NumberFormatException aeNumFEx)			{				laVehicleData.setVehModlYr(0);			}			try			{				laVehicleData.setVINAVehEmptyWt(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								lsMfVINAResponse.substring(									VINA_VEH_EMPTY_WT_OFFSET,									VINA_VEH_EMPTY_WT_OFFSET										+ VINA_VEH_EMPTY_WT_LENGTH)))						.intValue());			}			catch (NumberFormatException aeNumFEx)			{				laVehicleData.setVINAVehEmptyWt(0);			}			laVehicleData.setVehBdyType(				laMFAccess.trimMfString(					lsMfVINAResponse.substring(						VEH_BDY_TYPE_OFFSET,						VEH_BDY_TYPE_OFFSET + VEH_BDY_TYPE_LENGTH)));			try			{				laVehicleData.setDieselIndi(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								lsMfVINAResponse.substring(									DIESEL_INDI_OFFSET,									DIESEL_INDI_OFFSET										+ DIESEL_INDI_LENGTH)))						.intValue());			}			catch (NumberFormatException aeNumFEx)			{				laVehicleData.setDieselIndi(0);			}			try			{				laVehicleData.setVinaBegTon(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								lsMfVINAResponse.substring(									VINA_BEG_TON_OFFSET,									VINA_BEG_TON_OFFSET										+ VINA_BEG_TON_LENGTH)))						.intValue());			}			catch (NumberFormatException aeNumFEx)			{				laVehicleData.setVinaBegTon(0);			}			try			{				laVehicleData.setVinaEndTon(					Integer						.valueOf(							laMFAccess.getStringFromZonedDecimal(								lsMfVINAResponse.substring(									VINA_END_TON_OFFSET,									VINA_END_TON_OFFSET										+ VINA_END_TON_LENGTH)))						.intValue());			}			catch (NumberFormatException aeNumFEx)			{				laVehicleData.setVinaEndTon(0);			}						// defect 10844			laVehicleData.setVehTypeCd(				laMFAccess					.trimMfString(						lsMfVINAResponse.substring(							VINA_VEHICLE_TYPE_OFFSET,							VINA_VEHICLE_TYPE_OFFSET								+ VINA_VEHICLE_TYPE_LENGTH)));			// end defect 10844 		}		//create the return object		MFVehicleData laMfVehicleData = new MFVehicleData();		//create other objects that are in MFVehicleData		RegistrationData laRegistrationData = new RegistrationData();		OwnerData laOwnerData = new OwnerData();		TitleData laTitleData = new TitleData();		// create empty salvage data container with atleast one empty 		//  object 		SalvageData laSalvageData = new SalvageData();		Vector laSalvageContainer = new Vector();		laSalvageContainer.addElement(laSalvageData);		//add the created Vehicle Data to it.		laMfVehicleData.setVehicleData(laVehicleData);		//add Registration data		laMfVehicleData.setRegData(laRegistrationData);		//add OwnerData		laMfVehicleData.setOwnerData(laOwnerData);		//add TitleData		laMfVehicleData.setTitleData(laTitleData);		//add SalvageData		laMfVehicleData.setVctSalvage(laSalvageContainer);		return laMfVehicleData;	}}/* #MfbaVinaU# */