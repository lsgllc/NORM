package com.txdot.isd.rts.services.data;import com.txdot.isd.rts.services.util.RTSDate;/* * * TimedPermitData.java * * (c) Texas Department of Transportation 2001 * * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	05/19/2005	Java 1.4 Work *							defect 7899 Ver 5.2.3 * J Zwiener	07/17/2005  Enhancement for Disable Placard event * 							add class variable ciIssueTwoPlacardsIndi * 							add getIssueTwoPlacardsIndi() * 							add setIssueTwoPlacardsIndi() * 							defect 8268 Ver 5.2.2 Fix 6 * K Harrell	10/27/2008	Add DisabledPlacardCustomerData object for * 							transition to new Disabled Placard events. * 							defect 9831 Ver Defect_POS_B	 * K Harrell	05/25/2010	delete cbEnterSelected,  * 							 getsetEnterSelected(),  * 							 isEnterSelected() * 							modify getOneTripData() * 							defect 10491 Ver 6.5.0  * K Harrell	10/10/2011	delete ciIssueTwoPlacardsIndi, get/set methods * 							defect 11050 Ver 6.9.0	   * --------------------------------------------------------------------- *//** * Timed permit data class for Miscellaneous Registration. *   * @version	6.9.0 			10/10/2011 * @author	Joseph Kwik * <br>Creation Date:		11/07/2001 10:31:44   *//* &TimedPermitData& */public class TimedPermitData implements java.io.Serializable{	/* &TimedPermitData'serialVersionUID& */	private final static long serialVersionUID = 6262243315220354410L;	// boolean /* &TimedPermitData'cbRegistered& */	private boolean cbRegistered;/* &TimedPermitData'cbSameVeh& */	private boolean cbSameVeh;	// defect 10491 	//private boolean cbEnterSelected = false;	// end defect 10491 		// int/* &TimedPermitData'ciChrgFeeIndi& */	private int ciChrgFeeIndi;/* &TimedPermitData'ciEffDt& */	private int ciEffDt;/* &TimedPermitData'ciEffTime& */	private int ciEffTime;/* &TimedPermitData'ciExpDt& */	private int ciExpDt;/* &TimedPermitData'ciExpTime& */	private int ciExpTime;		// defect 11050 	//private int ciIssueTwoPlacardsIndi;	// end defect 11050 	/* &TimedPermitData'ciNumPlacardsIssued& */	private int ciNumPlacardsIssued; /* &TimedPermitData'ciTempVehCaryngCap& */	private int ciTempVehCaryngCap;/* &TimedPermitData'ciTempVehGrossWt& */	private int ciTempVehGrossWt;	// String /* &TimedPermitData'csDlsCertNo& */	private String csDlsCertNo = "";/* &TimedPermitData'csEntryOriginPnt& */	private String csEntryOriginPnt = null;/* &TimedPermitData'csItmCd& */	private String csItmCd = "";/* &TimedPermitData'csTimedPrmtType& */	private String csTimedPrmtType = "";/* &TimedPermitData'csTowTrkPltNo& */	private String csTowTrkPltNo = "";/* &TimedPermitData'csVehRegState& */	private String csVehRegState = "";	// Object/* &TimedPermitData'caDPCustData& */	 private DisabledPlacardCustomerData caDPCustData = null;/* &TimedPermitData'caOneTripData& */	 private OneTripData caOneTripData = null;/* &TimedPermitData'caOwnrData& */	 private OwnerData caOwnrData = null;/* &TimedPermitData'caRTSDateEffDt& */	 private RTSDate caRTSDateEffDt = null;/* &TimedPermitData'caRTSDateExpDt& */	 private RTSDate caRTSDateExpDt = null;/* &TimedPermitData'caVehData& */	 private VehicleData caVehData = null;		/**	 * TimedPermitData constructor comment.	 *//* &TimedPermitData.TimedPermitData& */	public TimedPermitData()	{		super();	}	/**	 * Return value of ChrgFeeIndi	 * 	 * @return int	 *//* &TimedPermitData.getChrgFeeIndi& */	public int getChrgFeeIndi()	{		return ciChrgFeeIndi;	}		/**	 * Return value of DlsCertNo	 * 	 * @return String	 *//* &TimedPermitData.getDlsCertNo& */	public String getDlsCertNo()	{		return csDlsCertNo;	}		/**	 * Get DP Customer Data	 * 	 * @return	 *//* &TimedPermitData.getDPCustData& */	public DisabledPlacardCustomerData getDPCustData()	{		return caDPCustData;	}		/**	 * Return value of EffDt	 * 	 * @return int	 *//* &TimedPermitData.getEffDt& */	public int getEffDt()	{		return ciEffDt;	}		/**	 * Return value of EffTime	 * 	 * @return int	 *//* &TimedPermitData.getEffTime& */	public int getEffTime()	{		return ciEffTime;	}		/**	 * Return value of EntryOriginPnt	 * 	 * @return String	 *//* &TimedPermitData.getEntryOriginPnt& */	public String getEntryOriginPnt()	{		return csEntryOriginPnt;	}		/**	 * Return value of ExpDt	 * 	 * @return int	 *//* &TimedPermitData.getExpDt& */	public int getExpDt()	{		return ciExpDt;	}		/**	 * Return value of ExpTime	 * 	 * @return int	 *//* &TimedPermitData.getExpTime& */	public int getExpTime()	{		return ciExpTime;	}		// defect 11050 	//	/**	//	* Return value of IssueTwoPlacardsIndi	//	* 	//	* @return int	//	*/	//	public int getIssueTwoPlacardsIndi()	//	{	//	return ciIssueTwoPlacardsIndi;	//	}	//	end defect 11050		/**	 * Return value of ItmCd	 * 	 * @return String	 *//* &TimedPermitData.getItmCd& */	public String getItmCd()	{		return csItmCd;	}		/**	 * Return value of OneTripData	 * 	 * @return OneTripData	 *//* &TimedPermitData.getOneTripData& */	public OneTripData getOneTripData()	{		if (caOneTripData == null) 		{			caOneTripData = new OneTripData(); 			}		return caOneTripData;	}		/**	 * Return value of OwnrData	 * 	 * @return OwnerData	 *//* &TimedPermitData.getOwnrData& */	public OwnerData getOwnrData()	{		return caOwnrData;	}		/**	 * Return value of RTSDateEffDt	 * 	 * @return RTSDate	 *//* &TimedPermitData.getRTSDateEffDt& */	public RTSDate getRTSDateEffDt()	{		return caRTSDateEffDt;	}		/**	 * Return value of RTSDateExpDt	 * 	 * @return RTSDate	 *//* &TimedPermitData.getRTSDateExpDt& */	public RTSDate getRTSDateExpDt()	{		return caRTSDateExpDt;	}		/**	 * Return value of TempVehCaryngCap	 * 	 * @return int	 *//* &TimedPermitData.getTempVehCaryngCap& */	public int getTempVehCaryngCap()	{		return ciTempVehCaryngCap;	}		/**	 * Return value of TempVehGrossWt	 * 	 * @return int	 *//* &TimedPermitData.getTempVehGrossWt& */	public int getTempVehGrossWt()	{		return ciTempVehGrossWt;	}		/**	 * Return value of TimedPrmtType	 * 	 * @return String	 *//* &TimedPermitData.getTimedPrmtType& */	public String getTimedPrmtType()	{		return csTimedPrmtType;	}		/**	 * Return value of TowTrkPltNo	 * 	 * @return String	 *//* &TimedPermitData.getTowTrkPltNo& */	public String getTowTrkPltNo()	{		return csTowTrkPltNo;	}		/**	 * Return value of VehData	 * 	 * @return VehicleData	 *//* &TimedPermitData.getVehData& */	public VehicleData getVehData()	{		return caVehData;	}		/**	 * Return value of VehRegState	 * 	 * @return String	 *//* &TimedPermitData.getVehRegState& */	public String getVehRegState()	{		return csVehRegState;	}		/**	 * Return value of Registered	 * 	 * @return boolean	 *//* &TimedPermitData.isRegistered& */	public boolean isRegistered()	{		return cbRegistered;	}		/**	 * Return value of SameVeh	 * 	 * @return boolean	 *//* &TimedPermitData.isSameVeh& */	public boolean isSameVeh()	{		return cbSameVeh;	}		/**	 * Set value of ChrgFeeIndi	 * 	 * @param aiChrgFeeIndi int	 *//* &TimedPermitData.setChrgFeeIndi& */	public void setChrgFeeIndi(int aiChrgFeeIndi)	{		ciChrgFeeIndi = aiChrgFeeIndi;	}		/**	 * Set value of DlsCertNo	 * 	 * @param asDlsCertNo String	 *//* &TimedPermitData.setDlsCertNo& */	public void setDlsCertNo(String asDlsCertNo)	{		csDlsCertNo = asDlsCertNo;	}	/**	 * Set DP Customer Data	 * 	 * @param data	 *//* &TimedPermitData.setDPCustData& */	public void setDPCustData(DisabledPlacardCustomerData data)	{		caDPCustData = data;	}		/**	 * Set value of EffDt	 * 	 * @param aiEffDt int	 *//* &TimedPermitData.setEffDt& */	public void setEffDt(int aiEffDt)	{		ciEffDt = aiEffDt;	}		/**	 * Set value of EffTime	 * 	 * @param aiEffTime int	 *//* &TimedPermitData.setEffTime& */	public void setEffTime(int aiEffTime)	{		ciEffTime = aiEffTime;	}		/**	 * Set value of EntryOriginPnt	 * 	 * @param asEntryOriginPnt String	 *//* &TimedPermitData.setEntryOriginPnt& */	public void setEntryOriginPnt(String asEntryOriginPnt)	{		csEntryOriginPnt = asEntryOriginPnt;	}		/**	 * Set value of ExpDt	 * 	 * @param aiExpDt int	 *//* &TimedPermitData.setExpDt& */	public void setExpDt(int aiExpDt)	{		ciExpDt = aiExpDt;	}		/**	 * Set value of ExpTime	 * 	 * @param aiExpTime int	 *//* &TimedPermitData.setExpTime& */	public void setExpTime(int aiExpTime)	{		ciExpTime = aiExpTime;	}	// defect 11050 	//	/**	//	* Set value of IssueTwoPlacardsIndi	//	* 	//	* @param aiChrgFeeIndi int	//	*/	//	public void setIssueTwoPlacardsIndi(int aiIssueTwoPlacardsIndi)	//	{	//	ciIssueTwoPlacardsIndi = aiIssueTwoPlacardsIndi;	//	}	// end defect 11050 		/**	 * Set value of ItmCd	 * 	 * @param asItmCd String	 *//* &TimedPermitData.setItmCd& */	public void setItmCd(String asItmCd)	{		csItmCd = asItmCd;	}		/**	 * Set value of OneTripData	 * 	 * @param aaOneTripData OneTripData	 *//* &TimedPermitData.setOneTripData& */	public void setOneTripData(OneTripData aaOneTripData)	{		caOneTripData = aaOneTripData;	}		/**	 * Set value of OwnrData	 * 	 * @param aaOwnrData OwnerData	 *//* &TimedPermitData.setOwnrData& */	public void setOwnrData(OwnerData aaOwnrData)	{		caOwnrData = aaOwnrData;	}		/**	 * Set value of Registered	 * 	 * @param abRegistered boolean	 *//* &TimedPermitData.setRegistered& */	public void setRegistered(boolean abRegistered)	{		cbRegistered = abRegistered;	}		/**	 * Set value of RTSDateEffDt	 * 	 * @param aaRTSDateEffDt RTSDate	 *//* &TimedPermitData.setRTSDateEffDt& */	public void setRTSDateEffDt(RTSDate aaRTSDateEffDt)	{		caRTSDateEffDt = aaRTSDateEffDt;	}		/**	 * Set value of RTSDateExpDt	 * 	 * @param aaRTSDateExpDt RTSDate	 *//* &TimedPermitData.setRTSDateExpDt& */	public void setRTSDateExpDt(RTSDate aaRTSDateExpDt)	{		caRTSDateExpDt = aaRTSDateExpDt;	}		/**	 * Set value of SameVeh	 * 	 * @param abSameVeh boolean	 *//* &TimedPermitData.setSameVeh& */	public void setSameVeh(boolean abSameVeh)	{		cbSameVeh = abSameVeh;	}		/**	 * Set value of TempVehCaryngCap	 * 	 * @param aiTempVehCaryngCap int	 *//* &TimedPermitData.setTempVehCaryngCap& */	public void setTempVehCaryngCap(int aiTempVehCaryngCap)	{		ciTempVehCaryngCap = aiTempVehCaryngCap;	}		/**	 * Set value of TempVehGrossWt	 * 	 * @param aiTempVehGrossWt int	 *//* &TimedPermitData.setTempVehGrossWt& */	public void setTempVehGrossWt(int aiTempVehGrossWt)	{		ciTempVehGrossWt = aiTempVehGrossWt;	}		/**	 * Set value of TimedPrmtType	 * 	 * @param asTimedPrmtType String	 *//* &TimedPermitData.setTimedPrmtType& */	public void setTimedPrmtType(String asTimedPrmtType)	{		csTimedPrmtType = asTimedPrmtType;	}		/**	 * Set value of TowTrkPltNo	 * 	 * @param asTowTrkPltNo String	 *//* &TimedPermitData.setTowTrkPltNo& */	public void setTowTrkPltNo(String asTowTrkPltNo)	{		csTowTrkPltNo = asTowTrkPltNo;	}		/**	 * Set value of VehData	 * 	 * @param aaVehData VehicleData	 *//* &TimedPermitData.setVehData& */	public void setVehData(VehicleData aaVehData)	{		caVehData = aaVehData;	}		/**	 * Set value of VehRegState	 * 	 * @param asVehRegState String	 *//* &TimedPermitData.setVehRegState& */	public void setVehRegState(String asVehRegState)	{		csVehRegState = asVehRegState;	}	/**	 * @return the ciNumPlacardsIssued	 *//* &TimedPermitData.getNumPlacardsIssued& */	public int getNumPlacardsIssued()	{		return ciNumPlacardsIssued;	}	/**	 * @param ciNumPlacardsIssued the ciNumPlacardsIssued to set	 *//* &TimedPermitData.setNumPlacardsIssued& */	public void setNumPlacardsIssued(int ciNumPlacardsIssued)	{		this.ciNumPlacardsIssued = ciNumPlacardsIssued;	}}/* #TimedPermitData# */