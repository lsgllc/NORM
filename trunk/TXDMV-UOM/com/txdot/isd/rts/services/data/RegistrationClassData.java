package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * RegistrationClassData.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			08/27/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * K Harrell	07/13/2005	Java 1.4 Work
 * 							moved to services.data
 * 							rename ciDieselReqd & get/set methods. 
 * 							defect 7786 Ver 5.2.3
 * K Harrell	06/20/2008	add csTtlTrnsfrPnltyExmptCd, get/set methods
 * 							defect 9583 Ver Defect POS A
 * B Hargrove	09/23/2010	Add column DfltRegClassCdIndi 
 * 							add ciDfltRegClassCdIndi, get/set method
 * 							defect 10600 Ver 6.6.0
 *----------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * RegistrationClassData
 * 
 * @version Ver 6.6.0		09/23/2010
 * @author Administrator
 * <br>Creation Date: 			unknown
 *---------------------------------------------------------------
*/

public class RegistrationClassData
	implements Serializable, java.lang.Comparable
{
	// int 
	protected int ciBdyVinReqd;
	protected int ciCaryngCapReqd;
	protected int ciCaryngCapValidReqd;
	// defect 10600
	protected int ciDfltRegClassCdIndi;
	// end defect 10600
	protected int ciDieselReqd;
	protected int ciEmptyWtReqd;
	protected int ciFarmTrlrMaxWt;
	protected int ciFarmTrlrMinWt;
	protected int ciFxdWtReqd;
	protected int ciHvyVehUseWt;
	protected int ciLngthReqd;
	protected int ciOdmtrReqd;
	protected int ciPrmtToMoveReqd;
	protected int ciRegClassCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	protected int ciToknTrlrFeeReqd;
	protected int ciTonReqd;
	protected int ciTrlrCapValidReqd;
	protected int ciTrlrTypeReqd;
	protected int ciTrlrWtValidReqd;
	protected int ciVINAIndi;
	protected int ciWidthReqd;

	// String 
	// defect 9724 
	protected String csTtlTrnsfrPnltyExmptCd;
	// end defect 9724 
	protected String csVehClassCd;

	private final static long serialVersionUID = 7793936768055588638L;

	/**
	 * Sorts the Data Object by VehicleClassCd
	 * 
	 */
	public int compareTo(Object aaObject)
	{
		RegistrationClassData laRegCacheData =
			(RegistrationClassData) aaObject;

		String lsCurrentString = csVehClassCd;
		String lsCompareToString = laRegCacheData.getVehClassCd();

		return lsCurrentString.compareTo(lsCompareToString);

	}

	/**
	 * Returns the value of BdyVinReqd
	 * 
	 * @return  int  
	 */
	public final int getBdyVinReqd()
	{
		return ciBdyVinReqd;
	}

	/**
	 * Returns the value of CaryngCapReqd
	 * 
	 * @return  int  
	 */
	public final int getCaryngCapReqd()
	{
		return ciCaryngCapReqd;
	}

	/**
	 * Returns the value of CaryngCapValidReqd
	 * 
	 * @return  int  
	 */
	public final int getCaryngCapValidReqd()
	{
		return ciCaryngCapValidReqd;
	}
	
	/**
	 * Returns the value of Default Reg Class for a Veh Class indi
	 * 
	 * @return  int 
	 */
	public final int getDfltRegClassCdIndi()
	{
		return ciDfltRegClassCdIndi;
	}

	/**
	 * Returns the value of DieselReqd
	 * 
	 * @return  int  
	 */
	public final int getDieselReqd()
	{
		return ciDieselReqd;
	}

	/**
	 * Returns the value of EmptyWtReqd
	 * 
	 * @return  int  
	 */
	public final int getEmptyWtReqd()
	{
		return ciEmptyWtReqd;
	}

	/**
	 * Returns the value of FarmTrlrMaxWt
	 * 
	 * @return  int  
	 */
	public final int getFarmTrlrMaxWt()
	{
		return ciFarmTrlrMaxWt;
	}

	/**
	 * Returns the value of FarmTrlrMinWt
	 * 
	 * @return  int  
	 */
	public final int getFarmTrlrMinWt()
	{
		return ciFarmTrlrMinWt;
	}

	/**
	 * Returns the value of FxdWtReqd
	 * 
	 * @return  int  
	 */
	public final int getFxdWtReqd()
	{
		return ciFxdWtReqd;
	}

	/**
	 * Returns the value of HvyVehUseWt
	 * 
	 * @return  int  
	 */
	public final int getHvyVehUseWt()
	{
		return ciHvyVehUseWt;
	}

	/**
	 * Returns the value of LngthReqd
	 * 
	 * @return  int  
	 */
	public final int getLngthReqd()
	{
		return ciLngthReqd;
	}

	/**
	 * Returns the value of OdmtrReqd
	 * 
	 * @return  int  
	 */
	public final int getOdmtrReqd()
	{
		return ciOdmtrReqd;
	}

	/**
	 * Returns the value of PrmtToMoveReqd
	 * 
	 * @return  int  
	 */
	public final int getPrmtToMoveReqd()
	{
		return ciPrmtToMoveReqd;
	}

	/**
	 * Returns the value of RegClassCd
	 * 
	 * @return  int  
	 */
	public final int getRegClassCd()
	{
		return ciRegClassCd;
	}

	/**
	 * Returns the value of RTSEffDate
	 * 
	 * @return  int  
	 */
	public final int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Returns the value of RTSEffEndDate
	 * 
	 * @return  int  
	 */
	public final int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}

	/**
	 * Returns the value of ToknTrlrFeeReqd
	 * 
	 * @return  int  
	 */
	public final int getToknTrlrFeeReqd()
	{
		return ciToknTrlrFeeReqd;
	}

	/**
	 * Returns the value of TonReqd
	 * 
	 * @return  int  
	 */
	public final int getTonReqd()
	{
		return ciTonReqd;
	}

	/**
	 * Returns the value of TrlrCapValidReqd
	 * 
	 * @return  int  
	 */
	public final int getTrlrCapValidReqd()
	{
		return ciTrlrCapValidReqd;
	}

	/**
	 * Returns the value of TrlrTypeReqd
	 * 
	 * @return  int  
	 */
	public final int getTrlrTypeReqd()
	{
		return ciTrlrTypeReqd;
	}

	/**
	 * Returns the value of TrlrWtValidReqd
	 * 
	 * @return  int  
	 */
	public final int getTrlrWtValidReqd()
	{
		return ciTrlrWtValidReqd;
	}

	/**
	 * Returns value of csTtlTrnsfrPnltyExmptCd
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrPnltyExmptCd()
	{
		return csTtlTrnsfrPnltyExmptCd;
	}

	/**
	 * Returns the value of VehClassCd
	 * 
	 * @return  String 
	 */
	public final String getVehClassCd()
	{
		return csVehClassCd;
	}

	/**
	 * Returns the value of VINAIndi
	 * 
	 * @return  int  
	 */
	public final int getVINAIndi()
	{
		return ciVINAIndi;
	}

	/**
	 * Returns the value of WidthReqd
	 * 
	 * @return int
	 */
	public int getWidthReqd()
	{
		return ciWidthReqd;
	}

	/**
	 * This method sets the value of BdyVinReqd.
	 * 
	 * @param aiBdyVinReqd   int  
	 */
	public final void setBdyVinReqd(int aiBdyVinReqd)
	{
		ciBdyVinReqd = aiBdyVinReqd;
	}

	/**
	 * This method sets the value of CaryngCapReqd.
	 * 
	 * @param aiCaryngCapReqd   int  
	 */
	public final void setCaryngCapReqd(int aiCaryngCapReqd)
	{
		ciCaryngCapReqd = aiCaryngCapReqd;
	}

	/**
	 * This method sets the value of CaryngCapValidReqd.
	 * 
	 * @param aiCaryngCapValidReqd   int  
	 */
	public final void setCaryngCapValidReqd(int aiCaryngCapValidReqd)
	{
		ciCaryngCapValidReqd = aiCaryngCapValidReqd;
	}

	/**
	 * This method sets the value of Default Reg Class for a Veh Class indi
	 * 
	 * @param aiDfltRegClassCdIndi   int  
	 */
	public final void setDfltRegClassCdIndi(int aiDfltRegClassCdIndi)
	{
		ciDfltRegClassCdIndi = aiDfltRegClassCdIndi;
	}


	/**
	 * This method sets the value of DiesleReqd.
	 * 
	 * @param aiDiesleReqd   int  
	 */
	public final void setDieselReqd(int aiDiesleReqd)
	{
		ciDieselReqd = aiDiesleReqd;
	}

	/**
	 * This method sets the value of EmptyWtReqd.
	 * 
	 * @param aiEmptyWtReqd   int  
	 */
	public final void setEmptyWtReqd(int aiEmptyWtReqd)
	{
		ciEmptyWtReqd = aiEmptyWtReqd;
	}

	/**
	 * This method sets the value of FarmTrlrMaxWt.
	 * 
	 * @param aiFarmTrlrMaxWt   int  
	 */
	public final void setFarmTrlrMaxWt(int aiFarmTrlrMaxWt)
	{
		ciFarmTrlrMaxWt = aiFarmTrlrMaxWt;
	}

	/**
	 * This method sets the value of FarmTrlrMinWt.
	 * 
	 * @param aiFarmTrlrMinWt   int  
	 */
	public final void setFarmTrlrMinWt(int aiFarmTrlrMinWt)
	{
		ciFarmTrlrMinWt = aiFarmTrlrMinWt;
	}

	/**
	 * This method sets the value of FxdWtReqd.
	 * 
	 * @param aiFxdWtReqd   int  
	 */
	public final void setFxdWtReqd(int aiFxdWtReqd)
	{
		ciFxdWtReqd = aiFxdWtReqd;
	}

	/**
	 * This method sets the value of HvyVehUseWt.
	 * 
	 * @param aiHvyVehUseWt   int  
	 */
	public final void setHvyVehUseWt(int aiHvyVehUseWt)
	{
		ciHvyVehUseWt = aiHvyVehUseWt;
	}

	/**
	 * This method sets the value of LngthReqd.
	 * 
	 * @param aiLngthReqd   int  
	 */
	public final void setLngthReqd(int aiLngthReqd)
	{
		ciLngthReqd = aiLngthReqd;
	}

	/**
	 * This method sets the value of OdmtrReqd.
	 * 
	 * @param aiOdmtrReqd   int  
	 */
	public final void setOdmtrReqd(int aiOdmtrReqd)
	{
		ciOdmtrReqd = aiOdmtrReqd;
	}

	/**
	 * This method sets the value of PrmtToMoveReqd.
	 * 
	 * @param aiPrmtToMoveReqd   int  
	 */
	public final void setPrmtToMoveReqd(int aiPrmtToMoveReqd)
	{
		ciPrmtToMoveReqd = aiPrmtToMoveReqd;
	}

	/**
	 * This method sets the value of RegClassCd.
	 * 
	 * @param aiRegClassCd   int  
	 */
	public final void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}

	/**
	 * This method sets the value of RTSEffDate.
	 * 
	 * @param aiRTSEffDate   int  
	 */
	public final void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * This method sets the value of RTSEffEndDate.
	 * 
	 * @param aiRTSEffEndDate   int  
	 */
	public final void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}

	/**
	 * This method sets the value of ToknTrlrFeeReqd.
	 * 
	 * @param aiToknTrlrFeeReqd   int  
	 */
	public final void setToknTrlrFeeReqd(int aiToknTrlrFeeReqd)
	{
		ciToknTrlrFeeReqd = aiToknTrlrFeeReqd;
	}

	/**
	 * This method sets the value of TonReqd.
	 * 
	 * @param aiTonReqd   int  
	 */
	public final void setTonReqd(int aiTonReqd)
	{
		ciTonReqd = aiTonReqd;
	}

	/**
	 * This method sets the value of TrlrCapValidReqd.
	 * 
	 * @param aiTrlrCapValidReqd   int  
	 */
	public final void setTrlrCapValidReqd(int aiTrlrCapValidReqd)
	{
		ciTrlrCapValidReqd = aiTrlrCapValidReqd;
	}

	/**
	 * This method sets the value of TrlrTypeReqd.
	 * 
	 * @param aiTrlrTypeReqd   int  
	 */
	public final void setTrlrTypeReqd(int aiTrlrTypeReqd)
	{
		ciTrlrTypeReqd = aiTrlrTypeReqd;
	}

	/**
	 * This method sets the value of TrlrWtValidReqd.
	 * 
	 * @param aiTrlrWtValidReqd   int  
	 */
	public final void setTrlrWtValidReqd(int aiTrlrWtValidReqd)
	{
		ciTrlrWtValidReqd = aiTrlrWtValidReqd;
	}

	/**
	 * Set value of csTtlTrnsfrPnltyExmptCd
	 * 
	 * @param asTtlTrnsfrPnltyExmptCd
	 */
	public void setTtlTrnsfrPnltyExmptCd(String asTtlTrnsfrPnltyExmptCd)
	{
		csTtlTrnsfrPnltyExmptCd = asTtlTrnsfrPnltyExmptCd;
	}

	/**
	 * This method sets the value of VehClassCd.
	 * 
	 * @param asVehClassCd   String 
	 */
	public final void setVehClassCd(String asVehClassCd)
	{
		csVehClassCd = asVehClassCd;
	}

	/**
	 * This method sets the value of VINAIndi.
	 * 
	 * @param aiVINAIndi   int  
	 */
	public final void setVINAIndi(int aiVINAIndi)
	{
		ciVINAIndi = aiVINAIndi;
	}

	/**
	 * This method sets the value of WidthReqd
	 * 
	 * @param aiWidthReqd int
	 */
	public void setWidthReqd(int aiWidthReqd)
	{
		ciWidthReqd = aiWidthReqd;
	}
}
