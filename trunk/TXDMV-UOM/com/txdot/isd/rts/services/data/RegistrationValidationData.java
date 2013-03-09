package com.txdot.isd.rts.services.data;

import java.util.Vector;

/* 
 * RegistrationValidationData.java
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/28/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7894 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                  
 * B Hargrove	06/28/2005	Refactor\Move 
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * B Hargrove 	09/27/2006 	Counties can now do Exempts. Added Exempt
 * 							Indi to REG039, so now must handle setting
 * 							Exempt Indi and Charge Fee Indis.
 * 							add cbExemptMask, ciExemptIndi,  
 * 							getExemptIndi(), getExemptMask(), 
 * 							setExemptIndi(), setExemptMask()
 * 							defect 8900 Ver Exempts
 * K Harrell	10/19/2006	add indicators for return from REG008
 * 							add cbEnterOnClassPltStkr 
 * 							add setEnterOnClassPltStkr(), 
 * 							  isEnterOnClassPltStkr()
 *							defect 8900 Ver Exempts   
 * K Harrell	04/03/2007	add cbReplPltOptions
 * 							add getReplPltOptions(), setReplPltOptions()
 *							defect 9126 Ver Special Plates
 * K Harrell	10/12/2007	add ciChrgPltTrnsfrIndi, get/set methods
 * 							add cbPTOTrnsfrMask, get/set methods 
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	11/12/2007	add cbNPDDisabledForPTO, 
 * 							 cbNPDSelectedPriorPTO get/set methods
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	07/13/2009	delete cbOwnerAddrOK, is/set method
 * 							Only set used. 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	07/14/2009	delete ciDelLocAddrIndi, ciDelRecpntNameIndi,
 *							 ciDelRenwlAddrIndi, get/set methods
 *							defect 10127 Ver Defect_POS_F 
 * Min Wang		07/22/2010	add cbVerifyTowTruckCertMask, 
 * 							cbVerifyTowTruckCertIndi
 * 							getVerifyTowTruckCertMask(), 
 * 							setVerifyTowTruckCertMask(),
 * 							isVerifyTowTruckCertIndi(),
 * 							setVerifyTowTruckCertIndi()
 * 							defect 10007 Ver 6.5.0
 * Min Wang		07/28/2010	add cbVerifyTowTruckCertMask
 * 							delete cbVerifyTowTruckCert
 *							modify getVerifyTowTruckCertMask(), 
 * 							setVerifyTowTruckCertMask(), 
 * 							defect 10007 Ver 6.5.0
 * K Harrell	10/10/2011	add ciPTOTrnsfrIndi, cbPTOTrnsfrMask, 
 * 							  get/set methods 
 * 							delete ciChrgPltTrnsfrFeeIndi,
 * 							  cbChrgPltTrnsfrFeeMask, get/set methods 
 * 							defect 11030 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */
/**
 * Data object used in assisting Registration processing.
 * 
 * @version	6.9.0			10/10/2011
 * @author	Joseph Kwik
 * <br>Creation Date:		09/09/2001 08:57:48
 */
public class RegistrationValidationData implements java.io.Serializable
{
	private int ciApprndComptCntyNo;
	private int ciAddlToknFeeIndi;
	private int ciChngRegIndi;
	private int ciChrgFeeIndi;
	// defect 11030
	// defect 9368 
	// private boolean cbChrgPltTrnsfrFeeMask;
	// "Plate to Owner Transfer " checkbox  on REG029
	private int ciPTOTrnsfrIndi;
	// end defect 9368
	// end defect 11030 
	private int ciCorrtnEffMo;
	private int ciCorrtnEffYr;
	private int ciDieselIndi;
	private int ciEmissionsFeeIndi;
	private int ciExemptIndi;
	private int ciHvyVehUseTaxIndi;
	private int ciHvyVehUseTaxRequired;
	private int ciInvalidClassPltStkrIndi;
	private int ciInvalidMinGrossWtIndi;
	private int ciNewPltReplIndi;
	private int ciOrigCaryngCap;
	private int ciOrigDieselIndi;
	private int ciOrigRegClassCd;
	private int ciOrigRegExpMo;
	private int ciOrigRegExpYr;
	private int ciOrigResComptCntyNo;
	private int ciOrigVehEmptyWt;
	private int ciOrigVehGrossWt;
	private int ciOrigVehModlYr;
	private int ciProcsdByMailIndi;
	private int ciRegExpiredReason;
	private int ciRegistrationExpired;
	private int ciRegModify;
	private int ciRegPnltyChrgIndi;
	private int ciVehClassOK;
	private boolean cbAddlToknFeeMask;
	private boolean cbApprndComptCntyNoMask;
	private boolean cbChngRegMask;
	private boolean cbChrgFeeMask;
	
	// defect 11030 
	// defect 9368 
	// add boolean to manage enabling of  
	// "Charge Transfer Fee" checkbox on REG029
	// private boolean cbChrgPltTrnsfrFeeMask;
	private boolean cbPTOTrnsfrMask;
	// end defect 11030 
	
	private boolean cbNPDDisabledForPTO;
	private boolean cbNPDSelectedPriorPTO;
	// end defect 9368 
	private boolean cbCorrtnEffDateMask;
	private boolean cbDieselMask;
	private boolean cbEmissionsFeeMask = false;
	private boolean cbEnterOnAddlInfo = false;
	private boolean cbEnterOnClassPltStkr = false;
	private boolean cbEnterOnPlateSelection;
	private boolean cbEnterOnReplChoices;
	private boolean cbEnterOnRegCorrection;
	private boolean cbEnterOnStkrSelection = false;
	private boolean cbExemptMask;
	private boolean cbGrossWtMask;
	private boolean cbHvyVehUseTaxMask;
	private boolean cbInitREG003 = false;
	private boolean cbNewPltReplMask;
	private boolean cbProcsdByMailMask = false;
	private boolean cbRenwlReq = false;
	private boolean cbReplPltOptions = false;
	private boolean cbVehWtStatus = false;
	private boolean cbVTRAuth = false;
	// defect 10007
	private boolean cbVerifyTowTruckCertMask;
	private boolean cbVerifyTowTruckCertIndi;
	// end defect 10007

	// Vector 
	private Vector cvRTSExceptions = new Vector();
	private Vector cvInvItms = null;
	private Vector cvSameVehAllocInvItms = null;

	// String 
	private String csOrigRegPltCd = null;
	private String csOrigRegPltNo = null;
	private String csOrigRegStkrCd = null;
	private String csOrigRegStkrNo = null;
	private String csOrigTireTypeCd = null;
	private String csOrigVehBdyType = null;
	private String csOrigVehClassCd = null;
	private String csOrigVehMk = null;
	private String csOrigVehModl = null;
	private String csOrigVIN = null;
	private String csReplPltCd = null;
	private String csSupvOvride;
	private String csTransCode = null;

	// Object
	private OwnerData caOrigOwnerData = null;
	private AddressData caOrigRenwlMailAddr = null;
	private VehicleInquiryData caOrigVehInqData = null;

	private final static long serialVersionUID = -2495217437791593740L;

	/**
	 * Registration DataWrapper constructor.
	 */
	public RegistrationValidationData()
	{
		super();
	}
	/**
	 * Get Addl Token Fee Indi
	 * 
	 * @return int
	 */
	public int getAddlToknFeeIndi()
	{
		return ciAddlToknFeeIndi;
	}
	/**
	 * Get Addl Token Fee Mask
	 * 
	 * @return boolean
	 */
	public boolean getAddlToknFeeMask()
	{
		return cbAddlToknFeeMask;
	}
	/**
	 * Get Apprehended Compt Cnty Number
	 * 
	 * @return int
	 */
	public int getApprndComptCntyNo()
	{
		return ciApprndComptCntyNo;
	}
	/**
	 * Get Apprehended Compt Cnty Number Mask
	 * 
	 * @return boolean
	 */
	public boolean getApprndComptCntyNoMask()
	{
		return cbApprndComptCntyNoMask;
	}
	/**
	 * Get Change Registration Indi
	 * 
	 * @return int
	 */
	public int getChngRegIndi()
	{
		return ciChngRegIndi;
	}
	/**
	 * Get Change Registration Indi Mask
	 * 
	 * @return boolean
	 */
	public boolean getChngRegMask()
	{
		return cbChngRegMask;
	}
	/**
	 * Get Charge Fee Indi
	 * 
	 * @return int
	 */
	public int getChrgFeeIndi()
	{
		return ciChrgFeeIndi;
	}
	/**
	 * Get Charge Fee Indi Mask
	 * 
	 * @return boolean
	 */
	public boolean getChrgFeeMask()
	{
		return cbChrgFeeMask;
	}

	/**
	 * Get PTO Transfer Indicator
	 * 
	 * @return int 
	 */
	public int getPTOTrnsfrIndi()
	{
		return ciPTOTrnsfrIndi;
	}

	/**
	 * Get PTO Transfer Mask
	 * 
	 * @return boolean
	 */
	public boolean getPTOTrnsfrMask()
	{
		return cbPTOTrnsfrMask;
	}

	/**
	 * Get Correction EffDate Mask
	 * 
	 * @return boolean
	 */
	public boolean getCorrtnEffDateMask()
	{
		return cbCorrtnEffDateMask;
	}
	/**
	 * Get Correction Effectiv Month
	 * 
	 * @return int
	 */
	public int getCorrtnEffMo()
	{
		return ciCorrtnEffMo;
	}
	/**
	 * Get Correction Effective Year
	 * 
	 * @return int
	 */
	public int getCorrtnEffYr()
	{
		return ciCorrtnEffYr;
	}
	
	/**
	 * Get Diesel Indi
	 * 
	 * @return int
	 */
	public int getDieselIndi()
	{
		return ciDieselIndi;
	}
	/**
	 * Get Diesel Indi Mask
	 * 
	 * @return boolean
	 */
	public boolean getDieselMask()
	{
		return cbDieselMask;
	}
	/**
	 * Get Emissions Fee Indi
	 * 
	 * @return int
	 */
	public int getEmissionsFeeIndi()
	{
		return ciEmissionsFeeIndi;
	}
	/**
	 * Get Emissions Fee Indi Mask
	 * 
	 * @return boolean
	 */
	public boolean getEmissionsFeeMask()
	{
		return cbEmissionsFeeMask;
	}

	/**
	* Get Exempt Indi
	* 
	* @return int
	*/
	public int getExemptIndi()
	{
		return ciExemptIndi;
	}

	/**
	 * Get Exempt Indi Mask
	 * 
	 * @return boolean
	 */
	public boolean getExemptMask()
	{
		return cbExemptMask;
	}

	/**
	 * Get Gross Weight Mask
	 * 
	 * @return boolean
	 */
	public boolean getGrossWtMask()
	{
		return cbGrossWtMask;
	}
	/**
	 * Get Heavy Vehicle Use Tax Indi
	 * 
	 * @return int
	 */
	public int getHvyVehUseTaxIndi()
	{
		return ciHvyVehUseTaxIndi;
	}
	/**
	 * Get Heavy Vehicle Use Tax Indi Mask
	 * 
	 * @return boolean
	 */
	public boolean getHvyVehUseTaxMask()
	{
		return cbHvyVehUseTaxMask;
	}
	/**
	 * Get Heavy Vehicle Use Tax Required
	 * 
	 * @return int
	 */
	public int getHvyVehUseTaxRequired()
	{
		return ciHvyVehUseTaxRequired;
	}
	/**
	 * Get Invalid Class Plate Sticker Indi
	 * 
	 * @return int
	 */
	public int getInvalidClassPltStkrIndi()
	{
		return ciInvalidClassPltStkrIndi;
	}
	/**
	 * Get Invalid Minimum Gross Weight Indi
	 * 
	 * @return int
	 */
	public int getInvalidMinGrossWtIndi()
	{
		return ciInvalidMinGrossWtIndi;
	}
	/**
	 * Get Inventory Items
	 * 
	 * @return Vector
	 */
	public Vector getInvItms()
	{
		return cvInvItms;
	}
	/**
	 * Get New Plate Replacement Indi
	 * 
	 * @return int
	 */
	public int getNewPltReplIndi()
	{
		return ciNewPltReplIndi;
	}
	/**
	 * Get New Plate Replacement Indi Mask
	 * 
	 * @return boolean
	 */
	public boolean getNewPltReplMask()
	{
		return cbNewPltReplMask;
	}
	/**
	 * Get Original Carrying Capacity
	 * 
	 * @return int
	 */
	public int getOrigCaryngCap()
	{
		return ciOrigCaryngCap;
	}
	/**
	 * Get Original Diesel Indi
	 *  
	 * @return int
	 */
	public int getOrigDieselIndi()
	{
		return ciOrigDieselIndi;
	}
	/**
	 * Get Original Owner Data
	 * 
	 * @return com.txdot.isd.rts.client.common.ui.OwnerData
	 */
	public OwnerData getOrigOwnerData()
	{
		return caOrigOwnerData;
	}
	/**
	 * Get Original Reg Class Code
	 * 
	 * @return int
	 */
	public int getOrigRegClassCd()
	{
		return ciOrigRegClassCd;
	}
	/**
	 * Get Original Reg ExpMo
	 * 
	 * @return int
	 */
	public int getOrigRegExpMo()
	{
		return ciOrigRegExpMo;
	}
	/**
	 * Get Original Reg ExpYr
	 * 
	 * @return int
	 */
	public int getOrigRegExpYr()
	{
		return ciOrigRegExpYr;
	}
	/**
	 * Get Original Reg Plate Code
	 * 
	 * @return String
	 */
	public String getOrigRegPltCd()
	{
		return csOrigRegPltCd;
	}
	/**
	 * Get Original Reg Plate Number
	 *  
	 * @return String
	 */
	public String getOrigRegPltNo()
	{
		return csOrigRegPltNo;
	}
	/**
	 * Get Original Reg Sticker Code
	 *  
	 * @return String
	 */
	public String getOrigRegStkrCd()
	{
		return csOrigRegStkrCd;
	}
	/**
	 * Get Original Reg Sticker Number
	 *  
	 * @return String
	 */
	public String getOrigRegStkrNo()
	{
		return csOrigRegStkrNo;
	}
	/**
	 * Get Original Renewal Mailing Address
	 * 
	 * @return com.txdot.isd.rts.client.common.ui.AddressData
	 */
	public AddressData getOrigRenwlMailAddr()
	{
		return caOrigRenwlMailAddr;
	}
	/**
	 * Get Original Res Compt County Number
	 * 
	 * @return int
	 */
	public int getOrigResComptCntyNo()
	{
		return ciOrigResComptCntyNo;
	}
	/**
	 * Get Original Tire Type Code
	 *  
	 * @return String
	 */
	public String getOrigTireTypeCd()
	{
		return csOrigTireTypeCd;
	}
	/**
	 * Get Original Vehicle Body Type
	 *  
	 * @return String
	 */
	public String getOrigVehBdyType()
	{
		return csOrigVehBdyType;
	}
	/**
	 * Get Original Vehicle Class Code
	 * 
	 * @return String
	 */
	public String getOrigVehClassCd()
	{
		return csOrigVehClassCd;
	}
	/**
	 * Get Original Vehicle Empty Weight
	 *  
	 * @return int
	 */
	public int getOrigVehEmptyWt()
	{
		return ciOrigVehEmptyWt;
	}
	/**
	 * Get Original Vehicle Gross Weight
	 *  
	 * @return int
	 */
	public int getOrigVehGrossWt()
	{
		return ciOrigVehGrossWt;
	}
	/**
	 * Get Original Vehicle Inquiry Data
	 *  
	 * @return com.txdot.isd.rts.client.common.ui.VehicleInquiryData
	 */
	public VehicleInquiryData getOrigVehInqData()
	{
		return caOrigVehInqData;
	}
	/**
	 * Get Original Vehicle Make
	 *  
	 * @return String
	 */
	public String getOrigVehMk()
	{
		return csOrigVehMk;
	}
	/**
	 * Get Original Vehicle Model
	 *  
	 * @return String
	 */
	public String getOrigVehModl()
	{
		return csOrigVehModl;
	}
	/**
	 * Get Original Vehicle Model Year
	 *  
	 * @return int
	 */
	public int getOrigVehModlYr()
	{
		return ciOrigVehModlYr;
	}
	/**
	 * Get Original VIN
	 *  
	 * @return String
	 */
	public String getOrigVIN()
	{
		return csOrigVIN;
	}
	/**
	 * Get Processed By Mail Indi
	 * 
	 * @return int
	 */
	public int getProcsdByMailIndi()
	{
		return ciProcsdByMailIndi;
	}
	/**
	 * Get Processed By Mail Indi Mask
	 * 
	 * @return boolean
	 */
	public boolean getProcsdByMailMask()
	{
		return cbProcsdByMailMask;
	}
	/**
	 * Get Reg Expired Reason
	 * 
	 * @return int
	 */
	public int getRegExpiredReason()
	{
		return ciRegExpiredReason;
	}
	/**
	 * Get Registration Expired
	 * 
	 * @return int
	 */
	public int getRegistrationExpired()
	{
		return ciRegistrationExpired;
	}
	/**
	 * get Registration Modify
	 * 
	 * @return int
	 */
	public int getRegModify()
	{
		return ciRegModify;
	}
	/**
	 * Get Registration Penalty Charge Indi
	 * 
	 * @return int
	 */
	public int getRegPnltyChrgIndi()
	{
		return ciRegPnltyChrgIndi;
	}

	/**
	 * Get Registration Replacement Plate Code
	 * 
	 * @return String
	 */
	public String getReplPltCd()
	{
		return csReplPltCd;
	}
	/**
	 * Geet RTS Exceptions
	 * 
	 * @return Vector
	 */
	public Vector getRTSExceptions()
	{
		return cvRTSExceptions;
	}
	/**
	 * GEt Same Vehicle Allocation Inventory Items
	 *  
	 * @return Vector
	 */
	public Vector getSameVehAllocInvItms()
	{
		return cvSameVehAllocInvItms;
	}
	/**
	 * Get Supervisor Override 
	 * 
	 * @return String
	 */
	public String getSupvOvride()
	{
		return csSupvOvride;
	}
	/**
	 * Get Trans Code
	 * 
	 * @return String
	 */
	public String getTransCode()
	{
		return csTransCode;
	}
	/**
	 * Get Vehicle Class OK
	 * 
	 * @return int
	 */
	public int getVehClassOK()
	{
		return ciVehClassOK;
	}
	/**
	 * Is Enter On Additional Info
	 * 
	 * @return boolean
	 */
	public boolean isEnterOnAddlInfo()
	{
		return cbEnterOnAddlInfo;
	}
	/**
	 * Is Enter On Plate Selection
	 * defect 9126
	 * 
	 * @return boolean
	 */
	public boolean isEnterOnPlateSelection()
	{
		return cbEnterOnPlateSelection;
	}
	/**
	 * Is Enter On Reg Correction
	 * 
	 * @return boolean
	 */
	public boolean isEnterOnRegCorrection()
	{
		return cbEnterOnRegCorrection;
	}
	/**
	 * Is Enter On Replacement Choices
	 * 
	 * @return boolean
	 */
	public boolean isEnterOnReplChoices()
	{
		return cbEnterOnReplChoices;
	}
	/**
	 * Is Enter On Sticker Selection
	 * 
	 * @return boolean
	 */
	public boolean isEnterOnStkrSelection()
	{
		return cbEnterOnStkrSelection;
	}
	/**
	 * Is Init REG003
	 * 
	 * @return boolean
	 */
	public boolean isInitREG003()
	{
		return cbInitREG003;
	}

	/**
	 * Is Renewal Requested
	 * 
	 * @return boolean
	 */
	public boolean isRenwlReq()
	{
		return cbRenwlReq;
	}

	/**
	 * Is Vehicle Weight Status OK
	 * 
	 * @return boolean
	 */
	public boolean isVehWtStatusOK()
	{
		return cbVehWtStatus;
	}
	/**
	 * Is VTR Authorization
	 * 
	 * @return boolean
	 */
	public boolean isVTRAuth()
	{
		return cbVTRAuth;
	}
	/**
	 * get Verified Tow Truck Certificate
	 * 
	 * @return boolean
	 */
	public boolean getVerifyTowTruckCertMask()
	{
		return cbVerifyTowTruckCertMask;
	}

	/**
	 * Get Verified Tow Truck Certificate indi
	 * 
	 * @return int
	 */
	public boolean isVerifyTowTruckCertIndi()
	{
		return cbVerifyTowTruckCertIndi;
	}
	/**
	 * Set Verified Tow Truck Certificate
	 * 
	 * @param abVerifyTowTruckCert boolean
	 */
	public void setVerifyTowTruckCertMask(boolean abVerifyTowTruckCertMask)
	{
		cbVerifyTowTruckCertMask = abVerifyTowTruckCertMask;
	}
	/**
	 * Set Verified Tow Truck Certificate Indi
	 * 
	 * @param abVerifyTowTruckCert boolean
	 */
	public void setVerifyTowTruckCertIndi(boolean abVerifyTowTruckCertIndi)
	{
		cbVerifyTowTruckCertIndi = abVerifyTowTruckCertIndi;
	}
	/**
	 * Set Additional Token Fee Indi
	 * 
	 * @param aiNewAddlToknFeeIndi int
	 */
	public void setAddlToknFeeIndi(int aiNewAddlToknFeeIndi)
	{
		ciAddlToknFeeIndi = aiNewAddlToknFeeIndi;
	}
	/**
	 * Set Additional Token Fee Mask
	 * 
	 * @param abNewAddlToknFeeMask boolean
	 */
	public void setAddlToknFeeMask(boolean abNewAddlToknFeeMask)
	{
		cbAddlToknFeeMask = abNewAddlToknFeeMask;
	}
	/**
	 * setApprndComptCntyNo
	 * 
	 * @param aiNewApprndComptCntyNo int
	 */
	public void setApprndComptCntyNo(int aiNewApprndComptCntyNo)
	{
		ciApprndComptCntyNo = aiNewApprndComptCntyNo;
	}
	/**
	 * setApprndComptCntyNoMask
	 * 
	 * @param abNewApprndComptCntyNoMask boolean
	 */
	public void setApprndComptCntyNoMask(boolean abNewApprndComptCntyNoMask)
	{
		cbApprndComptCntyNoMask = abNewApprndComptCntyNoMask;
	}
	/**
	 * setChngRegIndi
	 * 
	 * @param aiNewChngRegIndi int
	 */
	public void setChngRegIndi(int aiNewChngRegIndi)
	{
		ciChngRegIndi = aiNewChngRegIndi;
	}
	/**
	 * setChngRegMask
	 * 
	 * @param abNewChngRegMask boolean
	 */
	public void setChngRegMask(boolean abNewChngRegMask)
	{
		cbChngRegMask = abNewChngRegMask;
	}
	/**
	 * setChrgFeeIndi
	 * 
	 * @param aiNewChrgFeeIndi int
	 */
	public void setChrgFeeIndi(int aiNewChrgFeeIndi)
	{
		ciChrgFeeIndi = aiNewChrgFeeIndi;
	}
	/**
	 * setChrgFeeMask
	 * 
	 * @param abNewChrgFeeMask boolean
	 */
	public void setChrgFeeMask(boolean abNewChrgFeeMask)
	{
		cbChrgFeeMask = abNewChrgFeeMask;
	}
	/**
	 * Set PTO Transfer Indicator 
	 * 
	 * @param aiPTOTrnsfrIndi
	 */
	public void setPTOTrnsfrIndi(int aiPTOTrnsfrIndi)
	{
		ciPTOTrnsfrIndi = aiPTOTrnsfrIndi;
	}

	/**
	 * Set PTO Transfer Mask
	 * 
	 * @param abPTOTrnsfrMask
	 */
	public void setPTOTrnsfrMask(boolean abPTOTrnsfrMask)
	{
		cbPTOTrnsfrMask = abPTOTrnsfrMask;
	}

	/**
	 * setCorrtnEffDateMask
	 * 
	 * @param abNewCorrtnEffDateMask boolean
	 */
	public void setCorrtnEffDateMask(boolean abNewCorrtnEffDateMask)
	{
		cbCorrtnEffDateMask = abNewCorrtnEffDateMask;
	}
	/**
	 * setCorrtnEffMo
	 * 
	 * @param aiNewCorrtnEffMo int
	 */
	public void setCorrtnEffMo(int aiNewCorrtnEffMo)
	{
		ciCorrtnEffMo = aiNewCorrtnEffMo;
	}
	/**
	 * setCorrtnEffYr
	 * 
	 * @param aiNewCorrtnEffYr int
	 */
	public void setCorrtnEffYr(int aiNewCorrtnEffYr)
	{
		ciCorrtnEffYr = aiNewCorrtnEffYr;
	}

	/**
	 * setDieselIndi
	 * 
	 * @param aiNewDieselIndi int
	 */
	public void setDieselIndi(int aiNewDieselIndi)
	{
		ciDieselIndi = aiNewDieselIndi;
	}
	/**
	 * setDieselMask
	 * 
	 * @param abNewDieselMask boolean
	 */
	public void setDieselMask(boolean abNewDieselMask)
	{
		cbDieselMask = abNewDieselMask;
	}
	/**
	 * setEmissionsFeeIndi
	 * 
	 * @param aiNewEmissionsFeeIndi int
	 */
	public void setEmissionsFeeIndi(int aiNewEmissionsFeeIndi)
	{
		ciEmissionsFeeIndi = aiNewEmissionsFeeIndi;
	}
	/**
	 * setEmissionsFeeMask
	 * 
	 * @param abNewEmissionsFeeMask boolean
	 */
	public void setEmissionsFeeMask(boolean abNewEmissionsFeeMask)
	{
		cbEmissionsFeeMask = abNewEmissionsFeeMask;
	}
	/**
	 * setEnterOnAddlInfo
	 * 
	 * @param abNewEnterOnAddlInfo boolean
	 */
	public void setEnterOnAddlInfo(boolean abNewEnterOnAddlInfo)
	{
		cbEnterOnAddlInfo = abNewEnterOnAddlInfo;
	}
	/**
	 * setEnterOnPlateSelection
	 * defect 9126
	 * 
	 * @param abNewEnterOnPlateSelection boolean
	 */
	public void setEnterOnPlateSelection(boolean abNewEnterOnPlateSelection)
	{
		cbEnterOnPlateSelection = abNewEnterOnPlateSelection;
	}
	/**
	 * setEnterOnRegCorrection
	 * 
	 * @param abNewEnterOnRegCorrection boolean
	 */
	public void setEnterOnRegCorrection(boolean abNewEnterOnRegCorrection)
	{
		cbEnterOnRegCorrection = abNewEnterOnRegCorrection;
	}
	/**
	 * setEnterOnReplChoices
	 * 
	 * @param abNewEnterOnReplChoices boolean
	 */
	public void setEnterOnReplChoices(boolean abNewEnterOnReplChoices)
	{
		cbEnterOnReplChoices = abNewEnterOnReplChoices;
	}
	/**
	 * setEnterOnStkrSelection
	 * 
	 * @param abNewEnterOnStkrSelection boolean
	 */
	public void setEnterOnStkrSelection(boolean abNewEnterOnStkrSelection)
	{
		cbEnterOnStkrSelection = abNewEnterOnStkrSelection;
	}

	/**
	* setExemptIndi
	* 
	* @param aiNewExemptIndi int
	*/
	public void setExemptIndi(int aiNewExemptIndi)
	{
		ciExemptIndi = aiNewExemptIndi;
	}
	/**
	 * setExemptMask
	 * 
	 * @param abNewExemptMask boolean
	 */
	public void setExemptMask(boolean abNewExemptMask)
	{
		cbExemptMask = abNewExemptMask;
	}

	/**
	 * setGrossWtMask
	 * 
	 * @param abNewGrossWtMask boolean
	 */
	public void setGrossWtMask(boolean abNewGrossWtMask)
	{
		cbGrossWtMask = abNewGrossWtMask;
	}
	/**
	 * setHvyVehUseTaxIndi
	 * 
	 * @param aiNewHvyVehUseTaxIndi int
	 */
	public void setHvyVehUseTaxIndi(int aiNewHvyVehUseTaxIndi)
	{
		ciHvyVehUseTaxIndi = aiNewHvyVehUseTaxIndi;
	}
	/**
	 * setHvyVehUseTaxMask
	 * 
	 * @param abNewHvyVehUseTaxMask boolean
	 */
	public void setHvyVehUseTaxMask(boolean abNewHvyVehUseTaxMask)
	{
		cbHvyVehUseTaxMask = abNewHvyVehUseTaxMask;
	}
	/**
	 * setHvyVehUseTaxRequired
	 * 
	 * @param aiNewHvyVehUseTaxRequired int
	 */
	public void setHvyVehUseTaxRequired(int aiNewHvyVehUseTaxRequired)
	{
		ciHvyVehUseTaxRequired = aiNewHvyVehUseTaxRequired;
	}
	/**
	 * setInitREG003
	 * 
	 * @param abNewInitREG003 boolean
	 */
	public void setInitREG003(boolean abNewInitREG003)
	{
		cbInitREG003 = abNewInitREG003;
	}
	/**
	 * setInvalidClassPltStkrIndi
	 * 
	 * @param aiNewInvalidClassPltStkrIndi int
	 */
	public void setInvalidClassPltStkrIndi(int aiNewInvalidClassPltStkrIndi)
	{
		ciInvalidClassPltStkrIndi = aiNewInvalidClassPltStkrIndi;
	}
	/**
	 * setInvalidMinGrossWtIndi
	 * 
	 * @param aiNewInvalidMinGrossWtIndi int
	 */
	public void setInvalidMinGrossWtIndi(int aiNewInvalidMinGrossWtIndi)
	{
		ciInvalidMinGrossWtIndi = aiNewInvalidMinGrossWtIndi;
	}
	/**
	 * setInvItms
	 * 
	 * @param avNewInvItms Vector
	 */
	public void setInvItms(Vector avNewInvItms)
	{
		cvInvItms = avNewInvItms;
	}
	/**
	 * setNewPltReplIndi
	 * 
	 * @param aiNewPltReplIndi int
	 */
	public void setNewPltReplIndi(int aiNewNewPltReplIndi)
	{
		ciNewPltReplIndi = aiNewNewPltReplIndi;
	}
	/**
	 * setNewPltReplMask
	 * 
	 * @param abNewPltReplMask boolean
	 */
	public void setNewPltReplMask(boolean abNewPltReplMask)
	{
		cbNewPltReplMask = abNewPltReplMask;
	}
	/**
	 * setOrigCaryngCap
	 * 
	 * @param aiNewOrigCaryngCap int
	 */
	public void setOrigCaryngCap(int aiNewOrigCaryngCap)
	{
		ciOrigCaryngCap = aiNewOrigCaryngCap;
	}
	/**
	 * setOrigDieselIndi
	 *  
	 * @param aiNewOrigDieselIndi int
	 */
	public void setOrigDieselIndi(int aiNewOrigDieselIndi)
	{
		ciOrigDieselIndi = aiNewOrigDieselIndi;
	}
	/**
	 * setOrigOwnerData
	 * 
	 * @param aaNnewOrigOwnerData OwnerData
	 */
	public void setOrigOwnerData(OwnerData aaNewOrigOwnerData)
	{
		caOrigOwnerData = aaNewOrigOwnerData;
	}
	/**
	 * setOrigRegClassCd
	 * 
	 * @param aiNewOrigRegClassCd int
	 */
	public void setOrigRegClassCd(int aiNewOrigRegClassCd)
	{
		ciOrigRegClassCd = aiNewOrigRegClassCd;
	}
	/**
	 * setOrigRegExpMo
	 *  
	 * @param aiNewOrigRegExpMo int
	 */
	public void setOrigRegExpMo(int aiNewOrigRegExpMo)
	{
		ciOrigRegExpMo = aiNewOrigRegExpMo;
	}
	/**
	 * setOrigRegExpYr
	 *  
	 * @param aiNewRegExpYr int
	 */
	public void setOrigRegExpYr(int aiNewOrigRegExpYr)
	{
		ciOrigRegExpYr = aiNewOrigRegExpYr;
	}
	/**
	 * Insert the method's description here.
	 * 
	 * @param asNewOrigRegPltCd String
	 */
	public void setOrigRegPltCd(String asNewOrigRegPltCd)
	{
		csOrigRegPltCd = asNewOrigRegPltCd;
	}
	/**
	 * setOrigRegPltNo
	 *  
	 * @param asNewOrigRegPltNo String
	 */
	public void setOrigRegPltNo(String asNewOrigRegPltNo)
	{
		csOrigRegPltNo = asNewOrigRegPltNo;
	}
	/**
	 * setOrigRegStkrCd
	 *  
	 * @param asNewOrigRegStkrCd String
	 */
	public void setOrigRegStkrCd(String asNewOrigRegStkrCd)
	{
		csOrigRegStkrCd = asNewOrigRegStkrCd;
	}
	/**
	 * setOrigRegStkrNo
	 *  
	 * @param asNewOrigRegStkrNo String
	 */
	public void setOrigRegStkrNo(String asNewOrigRegStkrNo)
	{
		csOrigRegStkrNo = asNewOrigRegStkrNo;
	}
	/**
	 * setOrigRenwlMailAddr
	 * 
	 * @param aaNewOrigRenwlMailAddr AddressData
	 */
	public void setOrigRenwlMailAddr(AddressData aaNewOrigRenwlMailAddr)
	{
		caOrigRenwlMailAddr = aaNewOrigRenwlMailAddr;
	}
	/**
	 * setOrigResComptCntyNo
	 * 
	 * @param aiNewOrigResComptCntyNo int
	 */
	public void setOrigResComptCntyNo(int aiNewOrigResComptCntyNo)
	{
		ciOrigResComptCntyNo = aiNewOrigResComptCntyNo;
	}
	/**
	 * setOrigTireTypeCd
	 *  
	 * @param asNewOrigTireTypeCd String
	 */
	public void setOrigTireTypeCd(String asNewOrigTireTypeCd)
	{
		csOrigTireTypeCd = asNewOrigTireTypeCd;
	}
	/**
	 * setOrigVehBdyType
	 *  
	 * @param asNewOrigVehBdyType String
	 */
	public void setOrigVehBdyType(String asNewOrigVehBdyType)
	{
		csOrigVehBdyType = asNewOrigVehBdyType;
	}
	/**
	 * setOrigVehClassCd
	 * 
	 * @param asNewOrigVehClasCd String
	 */
	public void setOrigVehClassCd(String asNewOrigVehClassCd)
	{
		csOrigVehClassCd = asNewOrigVehClassCd;
	}
	/**
	 * setOrigVehEmptyWt
	 *   
	 * @param aiNewOrigVehEmptyWt int
	 */
	public void setOrigVehEmptyWt(int aiNewOrigVehEmptyWt)
	{
		ciOrigVehEmptyWt = aiNewOrigVehEmptyWt;
	}
	/**
	 * setOrigVehGrossWt
	 *  
	 * @param aiNewOrigVehGrossWt int
	 */
	public void setOrigVehGrossWt(int aiNewOrigVehGrossWt)
	{
		ciOrigVehGrossWt = aiNewOrigVehGrossWt;
	}
	/**
	 * setOrigVehInqData
	 *  
	 * @param aaNewOrigVehInqData VehicleInquiryData
	 */
	public void setOrigVehInqData(VehicleInquiryData aaNewOrigVehInqData)
	{
		caOrigVehInqData = aaNewOrigVehInqData;
	}
	/**
	 * setOrigVehMk
	 *  
	 * @param asNewOrigVehMk String
	 */
	public void setOrigVehMk(String asNewOrigVehMk)
	{
		csOrigVehMk = asNewOrigVehMk;
	}
	/**
	 * setOrigVehModl
	 *  
	 * @param asNewOrigVehModl String
	 */
	public void setOrigVehModl(String asNewOrigVehModl)
	{
		csOrigVehModl = asNewOrigVehModl;
	}
	/**
	 * setOrigVehModlYr
	 *  
	 * @param aiNewOrigVehModlYr int
	 */
	public void setOrigVehModlYr(int aiNewOrigVehModlYr)
	{
		ciOrigVehModlYr = aiNewOrigVehModlYr;
	}
	/**
	 * setOrigVIN
	 *  
	 * @param asNewOrigVIN String
	 */
	public void setOrigVIN(String asNewOrigVIN)
	{
		csOrigVIN = asNewOrigVIN;
	}
	
	/**
	 * setProcsdByMailIndi
	 * 
	 * @param aiNewProcsdByMailIndi int
	 */
	public void setProcsdByMailIndi(int aiNewProcsdByMailIndi)
	{
		ciProcsdByMailIndi = aiNewProcsdByMailIndi;
	}
	/**
	 * setProcsdByMailMask
	 * 
	 * @param abNewProcsdByMailMask boolean
	 */
	public void setProcsdByMailMask(boolean abNewProcsdByMailMask)
	{
		cbProcsdByMailMask = abNewProcsdByMailMask;
	}
	/**
	 * setRegExpiredReason
	 * 
	 * @param aiNewRegExpiredReason int
	 */
	public void setRegExpiredReason(int aiNewRegExpiredReason)
	{
		ciRegExpiredReason = aiNewRegExpiredReason;
	}
	/**
	 * setRegistrationExpired
	 * 
	 * @param aiNewRegistrationExpired int
	 */
	public void setRegistrationExpired(int aiRegistrationExpired)
	{
		ciRegistrationExpired = aiRegistrationExpired;
	}
	/**
	 * setRegModify
	 * 
	 * @param aiNewRegModify int
	 */
	public void setRegModify(int aiNewRegModify)
	{
		ciRegModify = aiNewRegModify;
	}
	/**
	 * setRegPnltyChrgIndi
	 * 
	 * @param aiNewRegPnltyChrgIndi int
	 */
	public void setRegPnltyChrgIndi(int aiNewRegPnltyChrgIndi)
	{
		ciRegPnltyChrgIndi = aiNewRegPnltyChrgIndi;
	}

	/**
	 * setRenwlReq
	 * 
	 * @param abNewRenwlReq boolean
	 */
	public void setRenwlReq(boolean abNewRenwlReq)
	{
		cbRenwlReq = abNewRenwlReq;
	}
	
	/**
	 * setReplPltCd
	 * 
	 * @param asNewReplPltCd String
	 */
	public void setReplPltCd(String asNewReplPltCd)
	{
		csReplPltCd = asNewReplPltCd;
	}
	/**
	 * setRTSExceptions
	 * 
	 * @param avNewRTSException Vector
	 */
	public void setRTSExceptions(Vector avNewRTSException)
	{
		cvRTSExceptions = avNewRTSException;
	}
	/**
	 * setSameVehAllocInvItms
	 *  
	 * @param avNewSameVehAllocInvItms Vector
	 */
	public void setSameVehAllocInvItms(Vector avNewSameVehAllocInvItms)
	{
		cvSameVehAllocInvItms = avNewSameVehAllocInvItms;
	}
	/**
	 * setSupvOvride
	 * 
	 * @param asNewSupvOvride String
	 */
	public void setSupvOvride(String asNewSupvOvride)
	{
		csSupvOvride = asNewSupvOvride;
	}
	/**
	 * setTransCode
	 * 
	 * @param asNewTransCode String
	 */
	public void setTransCode(String asNewTransCode)
	{
		csTransCode = asNewTransCode;
	}
	/**
	 * setVehClassOK
	 * 
	 * @param aiNewVehClassOK int
	 */
	public void setVehClassOK(int aiNewVehClassOK)
	{
		ciVehClassOK = aiNewVehClassOK;
	}
	/**
	 * setVehWtStatusOK
	 * 
	 * @param abNewVehWtStatus boolean
	 */
	public void setVehWtStatusOK(boolean abNewVehWtStatus)
	{
		cbVehWtStatus = abNewVehWtStatus;
	}
	/**
	 * setVTRAuth
	 * 
	 * @param abNewVTRAuthIndi boolean
	 */
	public void setVTRAuth(boolean abNewVTRAuth)
	{
		cbVTRAuth = abNewVTRAuth;
	}
	/**
	 * Return boolean to denote whether have pressed Enter on REG008
	 * 
	 * @return boolean
	 */
	public boolean isEnterOnClassPltStkr()
	{
		return cbEnterOnClassPltStkr;
	}

	/**
	 * Set boolean to not have pressed Enter on REG008
	 * 
	 * @param abEnter
	 */
	public void setEnterOnClassPltStkr(boolean abEnter)
	{
		cbEnterOnClassPltStkr = abEnter;
	}

	/**
	 * Return if Plate Replacement Options Exist
	 * 
	 * @return boolean
	 */
	public boolean getReplPltOptions()
	{
		return cbReplPltOptions;
	}

	/**
	 * Set Indicator for whether Plate Replacement Options Exist
	 * 
	 * @param abReplPltOptions
	 */
	public void setReplPltOptions(boolean abReplPltOptions)
	{
		cbReplPltOptions = abReplPltOptions;
	}

	/**
	 * Return value of cbNPDDisabledForPTO
	 * 
	 * @return boolean
	 */
	public boolean isNPDDisabledForPTO()
	{
		return cbNPDDisabledForPTO;
	}

	/**
	 * Set value of cbNPDDisabledForPTO
	 * 
	 * @param abNPDDisabledForPTO
	 */
	public void setNPDDisabledForPTO(boolean abNPDDisabledForPTO)
	{
		cbNPDDisabledForPTO = abNPDDisabledForPTO;
	}

	/**
	 * Return value of cbNPDSelectedPriorPTO
	 * 
	 * @return boolean
	 */
	public boolean isNPDSelectedPriorPTO()
	{
		return cbNPDSelectedPriorPTO;
	}

	/**
	 * Set value of cbNPDSelectedPriorPTO
	 * 
	 * @param abNPDSettingPriorPTO
	 */
	public void setNPDSelectedPriorPTO(boolean abNPDSelectedPriorPTO)
	{
		cbNPDSelectedPriorPTO = abNPDSelectedPriorPTO;
	}

}
