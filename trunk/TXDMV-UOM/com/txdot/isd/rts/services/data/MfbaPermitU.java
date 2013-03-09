package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;

import com.txdot.isd.rts.server.dataaccess.MfAccess;

/*
 * MfbaPermitU.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/08/2010	Created
 * 							defect 10492 Ver 6.5.0 
 * K Harrell	06/23/2010	Ignore All Zero Phone No
 * 							modify setPermitDataFromMfResponse() 
 * 							defect 10492 Ver 6.5.0
 * K Harrell	07/07/2010	add assignment to ciIssuedMFDwnCd 
 * 							modify setPermitDataFromMfResponse() 
 * 							defect 10492 Ver 6.5.0
 * M Reyes		10/05/2010	Copy version V to version T for MF
 * 							MF version T. Modified All.
 * 							defect 10595 Ver POS_660
 * M Reyes		12/31/2010	Copy version T to version U for MF
 * 							version U. Modified All.
 * 							defect 10710 Ver POS_670    
 * K Harrell	06/02/2011	add logic to extract AuditTrailTransId 
 * 							modify setPrmitDataFromMfResponse() 
 * 							defect 10844 Ver 6.8.0
 * ---------------------------------------------------------------------
 */

/**
 * Parse CICS transactions into Permit Data 
 *
 * @version	6.8.0			06/02/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/08/2010 19:36:17
 */
public class MfbaPermitU
{
	private MfAccess caMfAccess = new MfAccess();

	// String 
	private String csMfResponseData = new String();

	private final static int TRANSCD_OFFSET = 0;
	private final static int TRANSCD_LENGTH = 6;

	private final static int PRMTISSUANCEID_OFFSET = 6;
	private final static int PRMTISSUANCEID_LENGTH = 17;

	private final static int TRANSEMPID_OFFSET = 23;
	private final static int TRANSEMPID_LENGTH = 7;

	private final static int CUSTLSTNAME_OFFSET = 30;
	private final static int CUSTNAME_LENGTH = 30;

	private final static int CUSTFSTNAME_OFFSET = 60;

	private final static int CUSTMINAME_OFFSET = 90;
	private final static int CUSTMINAME_LENGTH = 1;

	private final static int CUSTBSNNAME_OFFSET = 91;
	private final static int CUSTBSNNAME_LENGTH = 60;

	private final static int CUSTST1_OFFSET = 151;
	private final static int STREET_LENGTH = 30;

	private final static int CUSTST2_OFFSET = 181;

	private final static int CUSTCITY_OFFSET = 211;
	private final static int CITY_LENGTH = 19;

	private final static int CUSTSTATE_OFFSET = 230;
	private final static int STATE_LENGTH = 2;

	private final static int CUSTZPCD_OFFSET = 232;
	private final static int ZPCD_LENGTH = 5;

	private final static int CUSTZPCDP4_OFFSET = 237;
	private final static int ZPCDP4_LENGTH = 4;

	private final static int CUSTCNTRY_OFFSET = 241;
	private final static int CNTRY_LENGTH = 4;

	private final static int CUSTEMAIL_OFFSET = 245;
	private final static int EMAIL_LENGTH = 50;

	private final static int CUSTPHONE_OFFSET = 295;
	private final static int PHONE_LENGTH = 10;

	private final static int PRMTNO_OFFSET = 305;
	private final static int PRMTNO_LENGTH = 7;

	private final static int ITMCD_OFFSET = 312;
	private final static int ITMCD_LENGTH = 8;

	private final static int ACCTITMCD_OFFSET = 320;
	private final static int ACCTITMCD_LENGTH = 8;

	private final static int PRMTPDAMT_OFFSET = 328;
	private final static int PRMTPDAMT_LENGTH = 11;

	private final static int EFFDATE_OFFSET = 339;
	private final static int DATE_LENGTH = 8;

	private final static int EFFTIME_OFFSET = 347;
	private final static int TIME_LENGTH = 6;

	private final static int EXPDATE_OFFSET = 353;

	private final static int EXPTIME_OFFSET = 361;

	private final static int VEHBDYTYPE_OFFSET = 367;
	private final static int VEHBDYTYPE_LENGTH = 2;

	private final static int VEHMK_OFFSET = 369;
	private final static int VEHMK_LENGTH = 4;

	private final static int VEHMKDESC_OFFSET = 373;
	private final static int VEHMKDESC_LENGTH = 15;

	private final static int VEHMODLYR_OFFSET = 388;
	private final static int VEHMODLYR_LENGTH = 4;

	private final static int VIN_OFFSET = 392;
	private final static int VIN_LENGTH = 22;

	private final static int VEHREGCNTRY_OFFSET = 414;

	private final static int VEHREGSTATE_OFFSET = 418;

	private final static int VEHREGPLTNO_OFFSET = 420;
	private final static int VEHREGPLTNO_LENGTH = 15;

	private final static int ONETRIPPRMTORIGTNPNT_OFFSET = 435;
	private final static int ONETRIPPT_LENGTH = 32;

	private final static int ONETRIPPRMTPNT1_OFFSET = 467;

	private final static int ONETRIPPRMTPNT2_OFFSET = 499;

	private final static int ONETRIPPRMTPNT3_OFFSET = 531;

	private final static int ONETRIPPRMTDESTPNT_OFFSET = 563;

	private final static int DELINDI_OFFSET = 595;
	private final static int DELINDI_LENGTH = 1;

	private final static int MFDWNCD_OFFSET = 596;
	private final static int MFDWNCD_LENGTH = 1;

	private final static int BULKPRMTVENDORID_OFFSET = 597;
	private final static int BULKPRMTVENDORID_LENGTH = 11;

	// defect 10844 
	private final static int AUDITTRAILTRANSID_OFFSET = 608;
	private final static int AUDITTRAILTRANSID_LENGTH = 17;
	// end defect 10844 

	//remove the header from the response
	private final static int HEADER_LENGTH = 256;

	/**
	 * MfbaPermitV.java Constructor
	 * 
	 */
	public MfbaPermitU()
	{
		super();
	}
	/**
	 * get Integer Data 
	 * 
	 * @param aiRecordOffset
	 * @param aiItmOffset
	 * @param aiLength
	 * 
	 * @return int 
	 */
	private int getIntegerData(int aiItmOffset, int aiLength)
	{
		return Integer
			.valueOf(
				caMfAccess.getStringFromZonedDecimal(
					getStringData(aiItmOffset, aiLength)))
			.intValue();
	}

	/**
	 * get String Data
	 * 
	 * @param aiRecordOffset
	 * @param aiItmOffset
	 * @param aiLength
	 * 
	 * @return String 
	 */
	private String getStringData(int aiItmOffset, int aiLength)
	{
		int liEnd = aiItmOffset + aiLength;

		return (
			caMfAccess.trimMfString(
				csMfResponseData.substring(aiItmOffset, liEnd)));
	}

	/**
	 * Sets the PermitData object from the mainframe response. 
	 *
	 * @param asMfPartialResponse 
	 * @return Vector 
	 */
	public PermitData setPermitDataFromMfResponse(String asMfResponse)
	{
		PermitData laPrmtData = new PermitData();

		if (!UtilityMethods.isEmpty(asMfResponse))
		{
			csMfResponseData = asMfResponse.substring(HEADER_LENGTH);

			// TransCd 
			laPrmtData.setTransCd(
				getStringData(TRANSCD_OFFSET, TRANSCD_LENGTH));

			// PrmtIssuanceId 
			laPrmtData.setPrmtIssuanceId(
				getStringData(
					PRMTISSUANCEID_OFFSET,
					PRMTISSUANCEID_LENGTH));

			// TransEmpId 
			laPrmtData.setTransEmpId(
				getStringData(TRANSEMPID_OFFSET, TRANSEMPID_LENGTH));

			// Customer Lame Fields 
			CustomerData laCustomerData = new CustomerData();
			CustomerNameData laCustNameData =
				laCustomerData.getCustNameData();
			AddressData laCustAddrData =
				laCustomerData.getAddressData();
			laPrmtData.setCustomerData(laCustomerData);

			// CustLstName 
			laCustNameData.setCustLstName(
				getStringData(CUSTLSTNAME_OFFSET, CUSTNAME_LENGTH));

			// CustFstName 
			laCustNameData.setCustFstName(
				getStringData(CUSTFSTNAME_OFFSET, CUSTNAME_LENGTH));

			// CustMIName 
			laCustNameData.setCustMIName(
				getStringData(CUSTMINAME_OFFSET, CUSTMINAME_LENGTH));

			// CustBsnName 
			laCustNameData.setCustBsnName(
				getStringData(CUSTBSNNAME_OFFSET, CUSTBSNNAME_LENGTH));

			// CustSt1 
			laCustAddrData.setSt1(
				getStringData(CUSTST1_OFFSET, STREET_LENGTH));

			// CustSt2 
			laCustAddrData.setSt2(
				getStringData(CUSTST2_OFFSET, STREET_LENGTH));

			// CustCity 
			laCustAddrData.setCity(
				getStringData(CUSTCITY_OFFSET, CITY_LENGTH));

			// CustState  
			laCustAddrData.setState(
				getStringData(CUSTSTATE_OFFSET, STATE_LENGTH));

			// CustZpCd   
			laCustAddrData.setZpcd(
				getStringData(CUSTZPCD_OFFSET, ZPCD_LENGTH));

			// CustZpCdP4   
			laCustAddrData.setZpcdp4(
				getStringData(CUSTZPCDP4_OFFSET, ZPCDP4_LENGTH));

			// CustCntry   
			laCustAddrData.setCntry(
				getStringData(CUSTCNTRY_OFFSET, CNTRY_LENGTH));

			// CustEMail   
			laCustomerData.setEMail(
				getStringData(CUSTEMAIL_OFFSET, EMAIL_LENGTH));

			// CustPhone				
			String lsPhoneNo =
				getStringData(CUSTPHONE_OFFSET, PHONE_LENGTH);
			if (UtilityMethods.isAllZeros(lsPhoneNo))
			{
				lsPhoneNo = new String();
			}

			laCustomerData.setPhoneNo(lsPhoneNo);

			// PermitNo 
			laPrmtData.setPrmtNo(
				getStringData(PRMTNO_OFFSET, PRMTNO_LENGTH));

			// ItmCd 
			laPrmtData.setItmCd(
				getStringData(ITMCD_OFFSET, ITMCD_LENGTH));

			// AcctItmCd 
			laPrmtData.setAcctItmCd(
				getStringData(ACCTITMCD_OFFSET, ACCTITMCD_LENGTH));

			//TODO: Validate! 
			// Permit Paid Amount 
			StringBuffer lsPdAmt =
				new StringBuffer(
					caMfAccess.getStringFromZonedDecimal(
						csMfResponseData.substring(
							PRMTPDAMT_OFFSET,
							PRMTPDAMT_OFFSET + PRMTPDAMT_LENGTH)));

			int liDecimalInsert =
				PRMTPDAMT_LENGTH - (lsPdAmt.charAt(0) == '-' ? 1 : 2);

			lsPdAmt.insert(liDecimalInsert, '.');

			laPrmtData.setPrmtPdAmt(new Dollar(lsPdAmt.toString()));

			// Effective Date 
			laPrmtData.setEffDt(
				getIntegerData(EFFDATE_OFFSET, DATE_LENGTH));

			// Effective Time 
			laPrmtData.setEffTime(
				getIntegerData(EFFTIME_OFFSET, TIME_LENGTH));

			// Expiration Date 
			laPrmtData.setExpDt(
				getIntegerData(EXPDATE_OFFSET, DATE_LENGTH));

			// Expiration Time 
			laPrmtData.setExpTime(
				getIntegerData(EXPTIME_OFFSET, TIME_LENGTH));

			// VehBdyType 
			laPrmtData.setVehBdyType(
				getStringData(VEHBDYTYPE_OFFSET, VEHBDYTYPE_LENGTH));

			// VehMk 
			laPrmtData.setVehMk(
				getStringData(VEHMK_OFFSET, VEHMK_LENGTH));

			// VehMkDesc
			laPrmtData.setVehMkDesc(
				getStringData(VEHMKDESC_OFFSET, VEHMKDESC_LENGTH));

			// VehModlYr 
			laPrmtData.setVehModlYr(
				getIntegerData(VEHMODLYR_OFFSET, VEHMODLYR_LENGTH));

			// Vin 
			laPrmtData.setVin(getStringData(VIN_OFFSET, VIN_LENGTH));

			// VehRegCntry 
			laPrmtData.setVehRegCntry(
				getStringData(VEHREGCNTRY_OFFSET, CNTRY_LENGTH));

			// VehRegState 
			laPrmtData.setVehRegState(
				getStringData(VEHREGSTATE_OFFSET, STATE_LENGTH));

			// VehRegPltNo 
			laPrmtData.setVehRegPltNo(
				getStringData(VEHREGPLTNO_OFFSET, VEHREGPLTNO_LENGTH));

			// OneTripPrmtOrigtnPnt
			OneTripData laOTData = laPrmtData.getOneTripData();
			laOTData.setOrigtnPnt(
				getStringData(
					ONETRIPPRMTORIGTNPNT_OFFSET,
					ONETRIPPT_LENGTH));

			// OneTripPrmtPnt1 
			laOTData.setIntrmdtePnt1(
				getStringData(
					ONETRIPPRMTPNT1_OFFSET,
					ONETRIPPT_LENGTH));

			// OneTripPrmtPnt2 
			laOTData.setIntrmdtePnt2(
				getStringData(
					ONETRIPPRMTPNT2_OFFSET,
					ONETRIPPT_LENGTH));

			// OneTripPrmtPnt3 
			laOTData.setIntrmdtePnt3(
				getStringData(
					ONETRIPPRMTPNT3_OFFSET,
					ONETRIPPT_LENGTH));

			// OneTripPrmtDestPnt 
			laOTData.setDestPnt(
				getStringData(
					ONETRIPPRMTDESTPNT_OFFSET,
					ONETRIPPT_LENGTH));

			// DelIndi 
			laPrmtData.setDelIndi(
				getIntegerData(DELINDI_OFFSET, DELINDI_LENGTH));

			// IssuedMfDwnCd
			laPrmtData.setIssuedMFDwnCd(
				getIntegerData(MFDWNCD_OFFSET, MFDWNCD_LENGTH));

			// BulkPrmtVendorId 
			laPrmtData.setBulkPrmtVendorId(
				getStringData(
					BULKPRMTVENDORID_OFFSET,
					BULKPRMTVENDORID_LENGTH));

			// defect 10844
			// AuditTrailTransId 
			laPrmtData.setAuditTrailTransId(
				getStringData(
					AUDITTRAILTRANSID_OFFSET,
					AUDITTRAILTRANSID_LENGTH));
			// end defect 10844 
		}
		return laPrmtData;
	}
}
