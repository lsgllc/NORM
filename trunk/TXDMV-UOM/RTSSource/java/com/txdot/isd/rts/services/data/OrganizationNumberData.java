package com.txdot.isd.rts.services.data;import java.io.Serializable;/* (c) Texas Department of Transportation 2007 * --------------------------------------------------------------------- * Change HistorSets the value of y: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	01/31/2007	Created; * 							defect 9085 Ver Special Plates * K Harrell	02/08/2007	Add csOrgNo, get/set methods * 							defect 9085 Ver Special Plates   * K Harrell	02/16/2010	add ciTermBasedIndi, get/set methods.  * 							defect 10366 Ver POS_640 * J Zwiener	01/24/2011	add csRestyleAcctItmCd, get/set methods. * 							defect 10627 Ver POS_670 * J Zwiener	02/01/2011	add ciCrossoverIndi, get/set methods. * 							add ciCrossoverPosDate, get/set methods. * 							defect 10704 Ver POS_670  * --------------------------------------------------------------------- *//** * This Data class contains attributes and get and set methods for  * OrganizationNumberData * * @version	POS_670 	02/01/2011 * @author	Kathy Harrell * <br>Creation Date:	01/31/2007 16:13:00 *//* &OrganizationNumberData& */public class OrganizationNumberData implements Serializable{	//int/* &OrganizationNumberData'ciMfgPndingIndi& */	protected int ciMfgPndingIndi;/* &OrganizationNumberData'ciRTSEffDate& */	protected int ciRTSEffDate;/* &OrganizationNumberData'ciRTSEffEndDate& */	protected int ciRTSEffEndDate;/* &OrganizationNumberData'ciSunsetDate& */	protected int ciSunsetDate;		// defect 10366 /* &OrganizationNumberData'ciTermBasedIndi& */	protected int ciTermBasedIndi;	// end defect 10366	// defect 10704/* &OrganizationNumberData'ciCrossoverIndi& */	protected int ciCrossoverIndi;/* &OrganizationNumberData'ciCrossoverPosDate& */	protected int ciCrossoverPosDate;	// end defect 10704   	// String/* &OrganizationNumberData'csAcctItmCd& */	protected String csAcctItmCd;/* &OrganizationNumberData'csBaseRegPltCd& */	protected String csBaseRegPltCd;/* &OrganizationNumberData'csOrgNo& */	protected String csOrgNo;/* &OrganizationNumberData'csOrgNoDesc& */	protected String csOrgNoDesc;/* &OrganizationNumberData'csSponsorPltGrpId& */	protected String csSponsorPltGrpId;		// defect 10627/* &OrganizationNumberData'csRestyleAcctItmCd& */	protected String csRestyleAcctItmCd;	// end defect 10627	/* &OrganizationNumberData'serialVersionUID& */	static final long serialVersionUID = -3459117487943637328L;	/**	 * Returns the value of csAcctItmCd	 * 	 * @return String	 *//* &OrganizationNumberData.getAcctItmCd& */	public String getAcctItmCd()	{		return csAcctItmCd;	}	/**	 * Returns the value of csBaseRegPltCdd	 * 	 * @return String	 *//* &OrganizationNumberData.getBaseRegPltCd& */	public String getBaseRegPltCd()	{		return csBaseRegPltCd;	}		/**	 * Returns the value of ciCrossoverIndi	 * 	 * @return int	 *//* &OrganizationNumberData.getCrossoverIndi& */	public int getCrossoverIndi()	{		return ciCrossoverIndi;	}	/**	 * Returns the value of ciCrossoverPosDate	 * 	 * @return int	 *//* &OrganizationNumberData.getCrossoverPosDate& */	public int getCrossoverPosDate()	{		return ciCrossoverPosDate;	}	/**	 * Returns the value of ciMfgPndingIndi	 * 	 * @return int	 *//* &OrganizationNumberData.getMfgPndingIndi& */	public int getMfgPndingIndi()	{		return ciMfgPndingIndi;	}	/**	 * Returns the value of csOrgNo	 * 	 * @return String	 *//* &OrganizationNumberData.getOrgNo& */	public String getOrgNo()	{		return csOrgNo;	}	/**	 * Returns the value of csOrgNoDesc	 * 	 * @return String	 *//* &OrganizationNumberData.getOrgNoDesc& */	public String getOrgNoDesc()	{		return csOrgNoDesc;	}	/**	 * Returns the value of csRestyleAcctItmCd	 * 	 * @return String	 *//* &OrganizationNumberData.getRestyleAcctItmCd& */	public String getRestyleAcctItmCd()	{		return csRestyleAcctItmCd;	}	/**	 * Returns the value of ciRTSEffDate	 * 	 * @return int	 *//* &OrganizationNumberData.getRTSEffDate& */	public int getRTSEffDate()	{		return ciRTSEffDate;	}	/**	 * Returns the value of ciRTSEffEndDate	 * 	 * @return int	 *//* &OrganizationNumberData.getRTSEffEndDate& */	public int getRTSEffEndDate()	{		return ciRTSEffEndDate;	}		/**	 * Returns the value of csSponsorPltGrpId	 * 	 * @return String	 *//* &OrganizationNumberData.getSponsorPltGrpId& */	public String getSponsorPltGrpId()	{		return csSponsorPltGrpId;	}	/**	 * Returns the value of ciSunsetDate	 * 	 * @return int	 *//* &OrganizationNumberData.getSunsetDate& */	public int getSunsetDate()	{		return ciSunsetDate;	}	/**	 * Gets the value of ciVendorPrtpIndi	 * 	 * @return int	 *//* &OrganizationNumberData.getTermBasedIndi& */	public int getTermBasedIndi()	{		return ciTermBasedIndi;	}	/**	 * Sets the value of csAcctItmCd	 * 	 * @param asAcctItmCd	 *//* &OrganizationNumberData.setAcctItmCd& */	public void setAcctItmCd(String asAcctItmCd)	{		csAcctItmCd = asAcctItmCd;	}	/**	 * Sets the value of csBaseRegPltCd	 * 	 * @param asBaseRegPltCd	 *//* &OrganizationNumberData.setBaseRegPltCd& */	public void setBaseRegPltCd(String asBaseRegPltCd)	{		csBaseRegPltCd = asBaseRegPltCd;	}	/**	 * Sets the value of ciCrossoverIndi	 * 	 * @param aiCrossoverIndi	 *//* &OrganizationNumberData.setCrossoverIndi& */	public void setCrossoverIndi(int aiCrossoverIndi)	{		ciCrossoverIndi = aiCrossoverIndi;	}	/**	 * Sets the value of ciCrossoverPosDate	 * 	 * @param aiCrossoverPosDate	 *//* &OrganizationNumberData.setCrossoverPosDate& */	public void setCrossoverPosDate(int aiCrossoverPosDate)	{		ciCrossoverPosDate = aiCrossoverPosDate;	}	/**	 * Sets the value of ciMfgPndingIndi	 * 	 * @param aiMfgPndingIndi	 *//* &OrganizationNumberData.setMfgPndingIndi& */	public void setMfgPndingIndi(int aiMfgPndingIndi)	{		ciMfgPndingIndi = aiMfgPndingIndi;	}	/**	 * Sets the value of csOrgNo	 * 	 * @param asOrgNo	 *//* &OrganizationNumberData.setOrgNo& */	public void setOrgNo(String asOrgNo)	{		csOrgNo = asOrgNo;	}	/**	 * Sets the value of csOrgNoDesc	 * 	 * @param asOrgNoDesc	 *//* &OrganizationNumberData.setOrgNoDesc& */	public void setOrgNoDesc(String asOrgNoDesc)	{		csOrgNoDesc = asOrgNoDesc;	}	/**	 * Sets the value of csRestyleAcctItmCd	 * 	 * @param asRestyleAcctItmCd	 *//* &OrganizationNumberData.setRestyleAcctItmCd& */	public void setRestyleAcctItmCd(String asRestyleAcctItmCd)	{		csRestyleAcctItmCd = asRestyleAcctItmCd;	}	/**	 * Sets the value of ciRTSEffDate	 * 	 * @param aiRTSEffDate	 *//* &OrganizationNumberData.setRTSEffDate& */	public void setRTSEffDate(int aiRTSEffDate)	{		ciRTSEffDate = aiRTSEffDate;	}	/**	 * Sets the value of ciRTSEffEndDate	 * 	 * @param aiRTSEffEndDate	 *//* &OrganizationNumberData.setRTSEffEndDate& */	public void setRTSEffEndDate(int aiRTSEffEndDate)	{		ciRTSEffEndDate = aiRTSEffEndDate;	}	/**	 * Sets the value of csSponsorPltGrpId	 * 	 * @param asSponsorPltGrpId	 *//* &OrganizationNumberData.setSponsorPltGrpId& */	public void setSponsorPltGrpId(String asSponsorPltGrpId)	{		csSponsorPltGrpId = asSponsorPltGrpId;	}	/**	 * Sets the value of ciSunsetDate	 * 	 * @param aiSunsetDate	 *//* &OrganizationNumberData.setSunsetDate& */	public void setSunsetDate(int aiSunsetDate)	{		ciSunsetDate = aiSunsetDate;	}	/**	 * Sets the value of ciTermBasedIndi	 * 	 * @param aiTermBasedIndi	 *//* &OrganizationNumberData.setTermBasedIndi& */	public void setTermBasedIndi(int aiTermBasedIndi)	{		ciTermBasedIndi = aiTermBasedIndi;	}}/* #OrganizationNumberData# */