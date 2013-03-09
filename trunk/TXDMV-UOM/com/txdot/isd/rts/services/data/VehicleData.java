package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * VehicleData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	01/05/2011	add csVehMjrColorCd, csVehMnrColorCd, 
 * 							  get/set methods
 * 							defect 10712 Ver 6.7.0 
 * K Harrell	06/22/2011	add csVehTypeCd, get/set methods
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * VehicleData 
 * 
 * @version	6.8.0			06/22/2011
 * @author	Administrator
 * <br>Creation Date:		08/22/2001 10:43:23 
 */

public class VehicleData implements java.io.Serializable
{
	// double 
	private double cdVehWidth; // in inches PCR 30C

	// int
	private int ciDieselIndi;
	private int ciDisableVehClassIndi;
	private int ciDotStndrdsIndi;
	private int ciDpsStlnIndi;
	private int ciFloodDmgeIndi;
	private int ciFxdWtIndi;
	private int ciPrmtReqrdIndi;
	private int ciReContIndi;
	private int ciRenwlYrMismatchIndi;
	private int ciReplicaVehModlYr;
	private int ciVehEmptyWt;
	private int ciVehLngth;
	private int ciVehModlYr;
	private int ciVINAVehEmptyWt;
	private int ciVinaBegTon;
	private int ciVinaEndTon;
	private int ciVinErrIndi;

	// Object 
	private Dollar cdVehTon;

	// String
	private String csAuditTrailTransId;
	private String csRecondCd;
	private String csReplicaVehMk;
	private String csTrlrType;
	private String csVehBdyType;
	private String csVehBdyVin;
	private String csVehClassCd;
	private String csVehMk;
	private String csVehModl;
	private String csVehOdmtrBrnd;
	private String csVehOdmtrReadng;
	private String csVin;

	// defect 10712 
	private String csVehMjrColorCd;
	private String csVehMnrColorCd;
	// end defect 10712 

	// defect 10844
	private String csVehTypeCd;
	// end defect 10844

	private final static long serialVersionUID = 4569227784230336875L;

	/**
	 * VehicleData constructor comment.
	 */
	public VehicleData()
	{
		super();
	}
	/**
	 * Return value of AuditTrailTransId
	 * 
	 * @return String
	 */
	public String getAuditTrailTransId()
	{
		return csAuditTrailTransId;
	}
	/**
	 * Return value of DieselIndi
	 * 
	 * @return int
	 */
	public int getDieselIndi()
	{
		return ciDieselIndi;
	}
	/**
	 * Return value of DisableVehClassIndi
	 * 
	 * @return int
	 */
	public int getDisableVehClassIndi()
	{
		return ciDisableVehClassIndi;
	}
	/**
	 * Return value of DotStndrdsIndi
	 * 
	 * @return int
	 */
	public int getDotStndrdsIndi()
	{
		return ciDotStndrdsIndi;
	}
	/**
	 * Return value of DpsStlnIndi
	 * 
	 * @return int
	 */
	public int getDpsStlnIndi()
	{
		return ciDpsStlnIndi;
	}
	/**
	 * Return value of FloodDmgeIndi
	 * 
	 * @return int
	 */
	public int getFloodDmgeIndi()
	{
		return ciFloodDmgeIndi;
	}
	/**
	 * Return value of FxdWtIndi
	 * 
	 * @return int
	 */
	public int getFxdWtIndi()
	{
		return ciFxdWtIndi;
	}
	/**
	 * Return value of PrmtReqrdIndi
	 * 
	 * @return int
	 */
	public int getPrmtReqrdIndi()
	{
		return ciPrmtReqrdIndi;
	}
	/**
	 * Return value of RecondCd
	 * 
	 * @return String
	 */
	public String getRecondCd()
	{
		return csRecondCd;
	}
	/**
	 * Return value of ReContIndi
	 * 
	 * @return int
	 */
	public int getReContIndi()
	{
		return ciReContIndi;
	}
	/**
	 * Return value of RenwlYrMismatchIndi
	 * 
	 * @return int
	 */
	public int getRenwlYrMismatchIndi()
	{
		return ciRenwlYrMismatchIndi;
	}
	/**
	 * Return value of ReplicaVehMk
	 * 
	 * @return String
	 */
	public String getReplicaVehMk()
	{
		return csReplicaVehMk;
	}
	/**
	 * Return value of ReplicaVehModlYr
	 * 
	 * @return int
	 */
	public int getReplicaVehModlYr()
	{
		return ciReplicaVehModlYr;
	}
	/**
	 * Return value of TrlrType
	 * 
	 * @return String
	 */
	public String getTrlrType()
	{
		return csTrlrType;
	}
	/**
	 * Return value of VehBdyType
	 * 
	 * @return String
	 */
	public String getVehBdyType()
	{
		return csVehBdyType;
	}
	/**
	 * Return value of VehBdyVin
	 * 
	 * @return String
	 */
	public String getVehBdyVin()
	{
		return csVehBdyVin;
	}
	/**
	 * Return value of VehClassCd
	 * 
	 * @return String
	 */
	public String getVehClassCd()
	{
		return csVehClassCd;
	}
	/**
	 * Return value of VehEmptyWt
	 * 
	 * @return int
	 */
	public int getVehEmptyWt()
	{
		return ciVehEmptyWt;
	}
	/**
	 * Return value of VehLngth
	 * 
	 * @return int
	 */
	public int getVehLngth()
	{
		return ciVehLngth;
	}
	/**
	 * Return value of VehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}
	/**
	 * Return value of VehModl
	 * 
	 * @return String
	 */
	public String getVehModl()
	{
		return csVehModl;
	}
	/**
	 * Return value of VehModlYr
	 * 
	 * @return int
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}
	/**
	 * Return value of VehOdmtrBrnd
	 * 
	 * @return String
	 */
	public String getVehOdmtrBrnd()
	{
		return csVehOdmtrBrnd;
	}
	/**
	 * Return value of VehOdmtrReadng
	 * 
	 * @return String
	 */
	public String getVehOdmtrReadng()
	{
		return csVehOdmtrReadng;
	}
	/**
	 * Return value of VehTon
	 * 
	 * @return int
	 */
	public Dollar getVehTon()
	{
		return cdVehTon;
	}
	/**
	 * Return value of VehWidth
	 * 
	 * @return double
	 */
	public double getVehWidth()
	{
		return cdVehWidth;
	}
	/**
	 * Return value of Vin
	 * 
	 * @return String
	 */
	public String getVin()
	{
		return csVin;
	}
	/**
	 * Return value of VinaBegTon
	 * 
	 * @return int
	 */
	public int getVinaBegTon()
	{
		return ciVinaBegTon;
	}
	/**
	 * Return value of VinaEndTon
	 * 
	 * @return int
	 */
	public int getVinaEndTon()
	{
		return ciVinaEndTon;
	}
	/**
	 * Return value of VINAVehEmptyWt
	 * 
	 * @return int
	 */
	public int getVINAVehEmptyWt()
	{
		return ciVINAVehEmptyWt;
	}
	/**
	 * Return value of VinErrIndi
	 * 
	 * @return int
	 */
	public int getVinErrIndi()
	{
		return ciVinErrIndi;
	}
	/**
	 * Return value of csVehTypeCd
	 * 
	 * @return String 
	 */
	public String getVehTypeCd()
	{
		return csVehTypeCd;
	}
	/**
	 * Set value of AuditTrailTransId
	 * 
	 * @param asAuditTrailTransId String
	 */
	public void setAuditTrailTransId(String asAuditTrailTransId)
	{
		csAuditTrailTransId = asAuditTrailTransId;
	}
	/**
	 * Set value of DieselIndi
	 * 
	 * @param aiieselIndi int
	 */
	public void setDieselIndi(int aiDieselIndi)
	{
		ciDieselIndi = aiDieselIndi;
	}
	/**
	 * Set value of DisableVehClassIndi
	 * 
	 * @param aiDisableVehClassIndi int
	 */
	public void setDisableVehClassIndi(int aiisableVehClassIndi)
	{
		ciDisableVehClassIndi = aiisableVehClassIndi;
	}
	/**
	 * Set value of DotStndrdsIndi
	 * 
	 * @param aiDotStndrdsIndi int
	 */
	public void setDotStndrdsIndi(int aiDotStndrdsIndi)
	{
		ciDotStndrdsIndi = aiDotStndrdsIndi;
	}
	/**
	 * Set value of DpsStlnIndi
	 * 
	 * @param aiDpsStlnIndi int
	 */
	public void setDpsStlnIndi(int aiDpsStlnIndi)
	{
		ciDpsStlnIndi = aiDpsStlnIndi;
	}
	/**
	 * Set value of FloodDmgeIndi
	 * 
	 * @param aiFloodDmgeIndi int
	 */
	public void setFloodDmgeIndi(int aiFloodDmgeIndi)
	{
		ciFloodDmgeIndi = aiFloodDmgeIndi;
	}
	/**
	 * Set value of FxdWtIndi
	 * 
	 * @param aiFxdWtIndi int
	 */
	public void setFxdWtIndi(int aiFxdWtIndi)
	{
		ciFxdWtIndi = aiFxdWtIndi;
	}

	/**
	 * Set value of PrmtReqrdIndi
	 * 
	 * @param aiPrmtReqrdIndi int
	 */
	public void setPrmtReqrdIndi(int aiPrmtReqrdIndi)
	{
		ciPrmtReqrdIndi = aiPrmtReqrdIndi;
	}
	/**
	 * Set value of RecondCd
	 * 
	 * @param asRecondCd String
	 */
	public void setRecondCd(String asRecondCd)
	{
		csRecondCd = asRecondCd;
	}
	/**
	 * Set value of ReContIndi
	 * 
	 * @param aiReContIndi int
	 */
	public void setReContIndi(int aiReContIndi)
	{
		ciReContIndi = aiReContIndi;
	}
	/**
	 * Set value of RenwlYrMismatchIndi
	 * 
	 * @param aiRenwlYrMismatchIndi int
	 */
	public void setRenwlYrMismatchIndi(int aiRenwlYrMismatchIndi)
	{
		ciRenwlYrMismatchIndi = aiRenwlYrMismatchIndi;
	}
	/**
	 * Set value of ReplicaVehMk
	 * 
	 * @param asReplicaVehMk String
	 */
	public void setReplicaVehMk(String asReplicaVehMk)
	{
		csReplicaVehMk = asReplicaVehMk;
	}
	/**
	 * Set value of ReplicaVehModlYr
	 * 
	 * @param aiReplicaVehModlYr int
	 */
	public void setReplicaVehModlYr(int aiReplicaVehModlYr)
	{
		ciReplicaVehModlYr = aiReplicaVehModlYr;
	}
	/**
	 * Set value of TrlrType
	 * 
	 * @param asTrlrType String
	 */
	public void setTrlrType(String asTrlrType)
	{
		csTrlrType = asTrlrType;
	}
	/**
	 * Set value of VehBdyType
	 * 
	 * @param asVehBdyType String
	 */
	public void setVehBdyType(String asVehBdyType)
	{
		csVehBdyType = asVehBdyType;
	}
	/**
	 * Set value of VehBdyVin 
	 * 
	 * @param asVehBdyVin String
	 */
	public void setVehBdyVin(String asVehBdyVin)
	{
		csVehBdyVin = asVehBdyVin;
	}
	/**
	 * Set value of VehClassCd
	 * 
	 * @param asVehClassCd String
	 */
	public void setVehClassCd(String asVehClassCd)
	{
		csVehClassCd = asVehClassCd;
	}
	/**
	 * Set value of VehEmptyWt
	 * 
	 * @param aiVehEmptyWt int
	 */
	public void setVehEmptyWt(int aiVehEmptyWt)
	{
		ciVehEmptyWt = aiVehEmptyWt;
	}
	/**
	 * Set value of VehLngth
	 * 
	 * @param aiVehLngth int
	 */
	public void setVehLngth(int aiVehLngth)
	{
		ciVehLngth = aiVehLngth;
	}
	/**
	 * Set value of VehMk
	 * 
	 * @param asVehMk String
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}
	/**
	 * Set value of VehModl
	 * 
	 * @param asVehModl String
	 */
	public void setVehModl(String asVehModl)
	{
		csVehModl = asVehModl;
	}
	/**
	 * Set value of VehModlYr
	 * 
	 * @param aiVehModlYr int
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}
	/**
	 * Set value of VehOdmtrBrnd
	 * 
	 * @param asVehOdmtrBrnd String
	 */
	public void setVehOdmtrBrnd(String asVehOdmtrBrnd)
	{
		csVehOdmtrBrnd = asVehOdmtrBrnd;
	}
	/**
	 * Set value of VehOdmtrReadng
	 * 
	 * @param asVehOdmtrReadng String
	 */
	public void setVehOdmtrReadng(String asVehOdmtrReadng)
	{
		csVehOdmtrReadng = asVehOdmtrReadng;
	}
	/**
	 * Set value of VehTon
	 * 
	 * @param aaVehTon int
	 */
	public void setVehTon(Dollar aaVehTon)
	{
		cdVehTon = aaVehTon;
	}

	/**
	 * Set value of csVehTypeCd
	 * 
	 * @param asVehTypeCd
	 */
	public void setVehTypeCd(String asVehTypeCd)
	{
		csVehTypeCd = asVehTypeCd;
	}

	/**
	 * Set value of VehWidth
	 * 
	 * @param adVehWidth double
	 */
	public void setVehWidth(double adVehWidth)
	{
		cdVehWidth = adVehWidth;
	}
	/**
	 * Set value of Vin
	 * 
	 * @param asVin String
	 */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}
	/**
	 * Set value of VinaBegTon
	 * 
	 * @param aiVinaBegTon int
	 */
	public void setVinaBegTon(int aiVinaBegTon)
	{
		ciVinaBegTon = aiVinaBegTon;
	}
	/**
	 * Set value of VinaEndTon
	 * 
	 * @param aiVinaEndTon int
	 */
	public void setVinaEndTon(int aiVinaEndTon)
	{
		ciVinaEndTon = aiVinaEndTon;
	}
	/**
	 * Set value of VINAVehEmptyWt
	 * 
	 * @param aiVINAVehEmptyWt int
	 */
	public void setVINAVehEmptyWt(int aiVINAVehEmptyWt)
	{
		ciVINAVehEmptyWt = aiVINAVehEmptyWt;
	}
	/**
	 * Set value of VinErrIndi
	 * 
	 * @param aiVinErrIndi int
	 */
	public void setVinErrIndi(int aiVinErrIndi)
	{
		ciVinErrIndi = aiVinErrIndi;
	}

	/**
	 * Get AbstractValue of csVehMjrColorCd
	 * 
	 * @return String
	 */
	public String getVehMjrColorCd()
	{
		return csVehMjrColorCd;
	}

	/**
	 * Get AbstractValue of csVehMnrColorCd
	 * 
	 * @return String 
	 */
	public String getVehMnrColorCd()
	{
		return csVehMnrColorCd;
	}

	/**
	 * Set AbstractValue of csVehMjrColorCd
	 * 
	 * @param asVehMjrColorCd
	 */
	public void setVehMjrColorCd(String asVehMjrColorCd)
	{
		csVehMjrColorCd = asVehMjrColorCd;
	}

	/**
	 * Set AbstractValue of csVehMnrColorCd
	 * 
	 * @param asVehMnrColorCd
	 */
	public void setVehMnrColorCd(String asVehMnrColorCd)
	{
		csVehMnrColorCd = asVehMnrColorCd;
	}

}
