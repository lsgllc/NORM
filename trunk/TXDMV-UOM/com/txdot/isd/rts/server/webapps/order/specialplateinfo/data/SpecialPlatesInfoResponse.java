package com.txdot.isd.rts.server.webapps.order.specialplateinfo.data;

import com.txdot.isd.rts.server.webapps.order.common.data.AbstractResponse;
import com.txdot.isd.rts.server.webapps.order.common.data.Fees;

/*
 * SpecialPlatesInfoData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/02/2007	Created class.
 * 							defect 9120 Ver Special Plates 
 * Bob B.		08/22/2007	Change default value for allowAddSetOnline
 * 							to false
 * 							defect 9119 Ver Special Plates 
 * Bob B.		01/23/2008	Add the byte array to the return 
 * 							possibilities for the plate image
 * 							add barrPlateImage
 * 							add recordCount
 * 							add getPlateImageBytes(), 
 * 								setPlateImageBytes(),
 * 								getRecordCount(),
 * 								setRecordCount()	
 * 							defect 9473 Ver Tres Amigos Prep 
 * Bob B.		01/05/2011	Add rts_org_no.crossoverindi to	the Special 
 * 							plates response.
 * 							add crossoverIndi, getter and setter.
 * 							defect 10693 Ver POS_670
 * Bob B.		01/10/2011	Add rts_itrnt_spapp_grp_plt.vendorplturl to	
 * 							the Special	plates response.
 * 							add vendorPlateURL, getter and setter.
 * 							defect 10693 Ver POS_670 
 * ---------------------------------------------------------------------
 */

/**
 * This class holds the data returned from a call to the Special
 * Plate Info Function.
 *
 * @version	POS_670				01/05/2011
 * @author	Jeff Seifert
 * <br>Creation Date:			03/02/2007 14:30:00
 */
public class SpecialPlatesInfoResponse extends AbstractResponse
{
	private boolean addlSetIndi = false; 
	private String addlSetPlpRegPltCd = "";
	private String addlSetRegPltCd = "";
	//private boolean allowAddSetOnline = true;
	private boolean allowAddSetOnline = false;
	private boolean allowISAOnline = true;
	private Fees[] fees;
	private String grpDesc = "";
	private int grpId = 0;
	private String grpName = "";
	private boolean isaAllowIndi = false; 
	private boolean orderableAtCnty = true;
	private boolean orderOnline = false;
	private String orgNo = "";
	private String plateName = "";
	private String plpRegPltCd = "";
	private String pltDesc = "";
	private SpecialPlateDesign pltDesign = new SpecialPlateDesign();
	private String pltFaxForm = "";
	private String pltFormId = "";
	private int pltId = 0;
	private String pltImage = "";
	private String regPltCd = "";
	private String sampleManufacturingPltNo = "";
	private boolean showImageOnNonPLP = false;
	private boolean spanish = false;
	private String spclPltAcctitmcd = "";
	private boolean userPltNoAllowedIndi = false;
	// defect 9473
	// private byte[] plateImageBytes;
	private Object plateImageObject;
	private int recordCount;
	// end defect 9473
	// defect 10693
	private int crossoverIndi;
	private String vendorPlateURL;
	// end defect 10693

	/**
	 * Gets the RegPltCd to be used when the customer chooses to 
	 * purchase the additional set and the PLP option.
	 * 
	 * @return String
	 */
	public String getAddlSetPlpRegPltCd()
	{
		return addlSetPlpRegPltCd;
	}

	/**
	 * Gets the RegPltCd to be used when the customer chooses to 
	 * purchase the additional set.
	 * 
	 * @return
	 */
	public String getAddlSetRegPltCd()
	{
		return addlSetRegPltCd;
	}

	/**
	 * Gets the fees.
	 * 
	 * @return
	 */
	public Fees[] getFees()
	{
		return fees;
	}
	
	/**
	 * Gets the groups description.
	 * 
	 * @return
	 */
	public String getGrpDesc()
	{
		return grpDesc;
	}

	/**
	 * Gets the plate group id.
	 * 
	 * @return int
	 */
	public int getGrpId()
	{
		return grpId;
	}

	/**
	 * Gets the Plate Group Name.
	 * 
	 * @return String
	 */
	public String getGrpName()
	{
		return grpName;
	}
	/**
	 * Gets the Organization No
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return orgNo;
	}

	/**
	 * Gets the plate name.
	 * 
	 * @return String
	 */
	public String getPlateName()
	{
		return plateName;
	}

	/**
	 * Gets the Plate code.
	 * 
	 * @return String
	 */
	public String getPlpRegPltCd()
	{
		return plpRegPltCd;
	}

	/**
	 * Gets the plate description.
	 * 
	 * @return String
	 */
	public String getPltDesc()
	{
		return pltDesc;
	}

	/**
	 * Gets the Plate Design for the given plate.
	 * 
	 * @return SpecialPlateDesign
	 */
	public SpecialPlateDesign getPltDesign()
	{
		return pltDesign;
	}

	/**
	 * Gets the Fax id used to obtain a form for the given
	 * plate by fax.
	 * 
	 * @return String
	 */
	public String getPltFaxForm()
	{
		return pltFaxForm;
	}

	/**
	 * The form id for the given plate.
	 * 
	 * @return String
	 */
	public String getPltFormId()
	{
		return pltFormId;
	}

	/**
	 * Gets the Plate Id.
	 * 
	 * @return int
	 */
	public int getPltId()
	{
		return pltId;
	}

	/**
	 * Gets the Plates Image.
	 * 
	 * @return String
	 */
	public String getPltImage()
	{
		return pltImage;
	}

	/**
	 * Gets the reg plate code.
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return regPltCd;
	}

	/**
	 * Gets the sample manufacturing plate number.
	 * 
	 * @return String
	 */
	public String getSampleManufacturingPltNo()
	{
		return sampleManufacturingPltNo;
	}

	/**
	 * Gets the account item code for this plate.  This value will be
	 * stored with the fees for each transaction.
	 * 
	 * @return String
	 */
	public String getSpclPltAcctitmcd()
	{
		return spclPltAcctitmcd;
	}
	/**
	 * Additional Set Indicator.  This might can be used to
	 * determine if there Additional Sets are available.
	 * 
	 * @return
	 */
	public boolean isAddlSetIndi()
	{
		return addlSetIndi;
	}

	/**
	 * Gets if we are to allow additional sets to be ordered online or 
	 * not.  VTR says that they will not put any plates online that
	 * have additional sets but this boolean allows them to turn off
	 * the addtional set all together for each plate.
	 * 
	 * @return boolean
	 */
	public boolean isAllowAddSetOnline()
	{
		return allowAddSetOnline;
	}

	/**
	 * Gets if we are to allow ISA to be ordered online or 
	 * not.  VTR says that they will not put any plates online that
	 * have an ISA indi but this allows them to regulate it.
	 * 
	 * @return boolean
	 */
	public boolean isAllowISAOnline()
	{
		return allowISAOnline;
	}

	/**
	 * This indicator determines if the given plate can have
	 * an ISA symbol.
	 * 
	 * @return boolean
	 */
	public boolean isIsaAllowIndi()
	{
		return isaAllowIndi;
	}
	
	/**
	 * Determines if a plate is orderable at the county or if it
	 * is handled at Special Plates Division.
	 * 
	 * @return boolean
	 */
	public boolean isOrderableAtCnty()
	{
		return orderableAtCnty;
	}

	/**
	 * Returns if the given plate can be ordered online or not.
	 * 
	 * @return boolean
	 */
	public boolean isOrderOnline()
	{
		return orderOnline;
	}
	/**
	 * Determines if we should show and image on non plp requests
	 * for the plate order screen where they are entering their 
	 * information.  There was talk about showing the plate with the 
	 * inventory that they has been put on hold.  Adding this boolean
	 * allows us to make the change with out any code change.
	 * 
	 * @return boolean
	 */
	public boolean isShowImageOnNonPLP()
	{
		return showImageOnNonPLP;
	}

	/**
	 * Returns if this object is the spanish version.
	 * 
	 * @return
	 */
	public boolean isSpanish()
	{
		return spanish;
	}

	/**
	 * This indicator determines if the given plate can be 
	 * a PLP (personalized) plate.
	 * 
	 * @return boolean
	 */
	public boolean isUserPltNoAllowedIndi()
	{
		return userPltNoAllowedIndi;
	}

	/**
	 * Sets the Additional Set Indicator.  This might can be 
	 * used to determine if there Additional Sets are available.
	 * 
	 * @param abAddlSetIndi boolean
	 */
	public void setAddlSetIndi(boolean abAddlSetIndi)
	{
		addlSetIndi = abAddlSetIndi;
	}

	/**
	 * Sets the RegPltCd to be used when the customer chooses to 
	 * purchase the additional set and the PLP option.
	 * 
	 * @param asAddlSetPlpRegPltCd String
	 */
	public void setAddlSetPlpRegPltCd(String asAddlSetPlpRegPltCd)
	{
		addlSetPlpRegPltCd = asAddlSetPlpRegPltCd;
	}

	/**
	 * Sets the RegPltCd to be used when the customer chooses to 
	 * purchase the additional set.
	 * 
	 * @param asAddlSetRegPltCd String
	 */
	public void setAddlSetRegPltCd(String asAddlSetRegPltCd)
	{
		addlSetRegPltCd = asAddlSetRegPltCd;
	}

	/**
	 * Sets if we are to allow additional sets to be ordered online or 
	 * not.  VTR says that they will not put any plates online that
	 * have additional sets but this boolean allows them to turn off
	 * the addtional set all together for each plate.
	 * 
	 * @param abAllowAddSetOnline boolean
	 */
	public void setAllowAddSetOnline(boolean abAllowAddSetOnline)
	{
		allowAddSetOnline = abAllowAddSetOnline;
	}

	/**
	 * Sets if we are to allow ISA to be ordered online or 
	 * not.  VTR says that they will not put any plates online that
	 * have an ISA indi but this allows them to regulate it.
	 * 
	 * @param abAllowISAOnline boolean
	 */
	public void setAllowISAOnline(boolean abAllowISAOnline)
	{
		allowISAOnline = abAllowISAOnline;
	}
	
	/**
	 * Sets the fees.
	 * 
	 * @param aarrFees Fees[]
	 */
	public void setFees(Fees[] aarrFees)
	{
		fees = aarrFees;
	}

	/**
	 * Sets the Plate Group Description.
	 * 
	 * @param asGrpDesc String
	 */
	public void setGrpDesc(String asGrpDesc)
	{
		grpDesc = asGrpDesc;
	}

	/**
	 * Sets the plate group id.
	 * This value is required for ACTION_GET_GROUP_INFO.
	 * 
	 * @param aiGrpId int
	 */
	public void setGrpId(int aiGrpId)
	{
		grpId = aiGrpId;
	}

	/**
	 * Sets tje Plate Group Name.
	 * 
	 * @param asGrpName String
	 */
	public void setGrpName(String asGrpName)
	{
		grpName = asGrpName;
	}

	/**
	 * Sets if the given plate can have an ISA symbol on it.
	 * 
	 * @param abIsaAllowIndi boolean
	 */
	public void setIsaAllowIndi(boolean abIsaAllowIndi)
	{
		isaAllowIndi = abIsaAllowIndi;
	}

	/**
	 * Determines if a plate is orderable at the county or if it
	 * is handled at Special Plates Division.
	 * 
	 * @param abOrderableAtCnty boolean
	 */
	public void setOrderableAtCnty(boolean abOrderableAtCnty)
	{
		orderableAtCnty = abOrderableAtCnty;
	}

	/**
	 * Sets if the given plate can be ordered online or not.
	 * 
	 * @param abOrderOnline boolean
	 */
	public void setOrderOnline(boolean abOrderOnline)
	{
		orderOnline = abOrderOnline;
	}

	/**
	 * Sets the Organization No
	 * 
	 * @param asOrgNo String
	 */
	public void setOrgNo(String asOrgNo)
	{
		orgNo = asOrgNo;
	}

	/**
	 * Sets the Plate Name.
	 * 
	 * @param asPlateName String
	 */
	public void setPlateName(String asPlateName)
	{
		plateName = asPlateName;
	}

	/**
	 * Sets the PLP reg plate code.
	 * 
	 * @param asPlpRegPltCd String
	 */
	public void setPlpRegPltCd(String asPlpRegPltCd)
	{
		plpRegPltCd = asPlpRegPltCd;
	}

	/**
	 * Sets the plate Description.
	 * 
	 * @param asPltDesc String
	 */
	public void setPltDesc(String asPltDesc)
	{
		pltDesc = asPltDesc;
	}

	/**
	 * Sets the Plate Design for the given plate.
	 * 
	 * @param aaPltDesign SpecialPlateDesign
	 */
	public void setPltDesign(SpecialPlateDesign aaPltDesign)
	{
		pltDesign = aaPltDesign;
	}

	/**
	 * Sets the Fax id used to obtain a form for the given
	 * plate by fax.
	 * 
	 * @param asPltFaxForm String
	 */
	public void setPltFaxForm(String asPltFaxForm)
	{
		pltFaxForm = asPltFaxForm;
	}

	/**
	 * Sets the given plate form id.
	 * 
	 * @param asPltFormId String
	 */
	public void setPltFormId(String asPltFormId)
	{
		pltFormId = asPltFormId;
	}

	/**
	 * Sets the Plate Id.
	 * 
	 * @param aiPltId int
	 */
	public void setPltId(int aiPltId)
	{
		pltId = aiPltId;
	}

	/**
	 * Sets the Plates Image name.
	 * 
	 * @param asPltImage String
	 */
	public void setPltImage(String asPltImage)
	{
		pltImage = asPltImage;
	}

	/**
	 * Sets the Reg Plate code.
	 * 
	 * @param asRegPltCd String
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		regPltCd = asRegPltCd;
	}

	/**
	 * Sets the sample manufacturing plate number.
	 * 
	 * @param asSampleManufacturingPltNo String
	 */
	public void setSampleManufacturingPltNo(String asSampleManufacturingPltNo)
	{
		sampleManufacturingPltNo = asSampleManufacturingPltNo;
	}

	/**
	 * Determines if we should show and image on non plp requests
	 * for the plate order screen where they are entering their 
	 * information.  There was talk about showing the plate with the 
	 * inventory that they has been put on hold.  Adding this boolean
	 * allows us to make the change with out any code change.
	 * 
	 * @param abShowImageOnNonPLP boolean
	 */
	public void setShowImageOnNonPLP(boolean abShowImageOnNonPLP)
	{
		showImageOnNonPLP = abShowImageOnNonPLP;
	}

	/**
	 * Sets if this object is the spanish version.
	 * 
	 * @param abSpanish boolean
	 */
	public void setSpanish(boolean abSpanish)
	{
		spanish = abSpanish;
	}

	/**
	 * Sets the account item code for this plate.  This value will be
	 * stored with the fees for each transaction.
	 * 
	 * @param asSpclPltAcctitmcd String
	 */
	public void setSpclPltAcctitmcd(String asSpclPltAcctitmcd)
	{
		spclPltAcctitmcd = asSpclPltAcctitmcd;
	}

	/**
	 * Sets the indicator that determines if the given plate can be 
	 * a PLP (personalized) plate.
	 * 
	 * @param abUserPltNoAllowedIndi bolean
	 */
	public void setUserPltNoAllowedIndi(boolean abUserPltNoAllowedIndi)
	{
		userPltNoAllowedIndi = abUserPltNoAllowedIndi;
	}

	/**
	 * gets the plate image Object byte array
	 * 
	 * @return Object
	 */
	public Object getPlateImageObject()
	{
		return plateImageObject;
	}

	/**
	 * sets the plate image Object byte array
	 * 
	 * @param aaPlateImageObject Object
	 */
	public void setPlateImageObject(Object aaPlateImageObject)
	{
		plateImageObject = aaPlateImageObject;
	}

	/**
	 * gets the number of records returned from the db
	 * 
	 * @return Int
	 */
	public int getRecordCount()
	{
		return recordCount;
	}

	/**
	 * sets the number of records returned from the db
	 * 
	 * @param aiRecordCount int
	 */
	public void setRecordCount(int aiRecordCount)
	{
		recordCount = aiRecordCount;
	}

	/**
	 * gets the crossoverindi for a particular plate
	 * 
	 * @return int
	 */
	public int getCrossoverIndi() {
		return crossoverIndi;
	}

	/**
	 * sets the crossoverindi for a particular plate
	 * 
	 * @param aiCrossoverIndi int
	 */
	public void setCrossoverIndi(int aiCrossoverIndi) {
		crossoverIndi = aiCrossoverIndi;
	}

	/**
	 * gets the vendorPlateURL for a particular plate
	 * 
	 * @return String
	 */
	public String getVendorPlateURL() {
		return vendorPlateURL;
	}

	/**
	 * sets the vendorPlateURL for a particular plate
	 * 
	 * @param asVendorPlateURL String
	 */
	public void setVendorPlateURL(String asVendorPlateURL) {
		vendorPlateURL = asVendorPlateURL;
	}

}
