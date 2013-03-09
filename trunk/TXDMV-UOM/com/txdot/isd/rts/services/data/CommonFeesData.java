package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * CommonFeesData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell    01/16/2003	Added InsProofReqrdIndi
 *							defect 5271
 * K Harrell	04/01/2005	delete cdRegAddlFee, csAddlFeeItmCd
 *							deprecated getAddlFeeItmCd(),setAddlFeeItmCd()
 * 							getRegAddlFee(), setRegAddlFee() 
 *							defect 8104	Ver 5.2.2 Fix 4
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							deleted getAddlFeeItmCd(),setAddlFeeItmCd(),
 * 							getRegAddlFee(),setRegAddlFee()
 * 							defect 7899 Ver 5.2.3
 * K Harrell	01/13/2006	Add EmissionsPrcnt 
 * 							add caEmissionsPrcnt
 * 							add getEmissionsPrcnt(),setEmissionsPrcnt()
 * 							defect 8514 Ver 5.2.3  							  
 * B Hargrove	01/06/2009	Add TempAddlWtAllowdIndi 
 * 							add ciTempAddlWtAllowdIndi
 * 							getTempAddlWtAllowdIndi(),
 * 							setTempAddlWtAllowdIndi()
 * 							defect 9129 Ver Defect_POS_D
 * K Harrell	12/08/2010	add RegClassFeeGrpCd, get/set methods
 * 							defect 10695 Ver 6.7.0   							  
 * K Harrell	01/07/2011	add ciIVTRSRegNotAllowdIndi, ciRegClassMinWt,
 * 							 ciRegClassMaxWt, ciSPVCkReqrdIndi, 
 * 							 get/set methods 
 * 							 ciRegClassMaxWt, get/set methods 
 * 							defect 10695 Ver 6.7.0  	
 * K Harrell	01/12/2011 	RegClassFeeGrpCd to String
 * 							defect 10695 Ver 6.7.0 					  
 * B Hargrove	01/26/2011 	While testing Fee Simplification, found
 * 							typo: refactor/rename 'reflectorization fee'  
 * 							from caReglectnFee to caReflectnFee
 * 							defect 10685 Ver 6.7.0 					  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * CommonFeesData
 *
 * @version	6.7.0			01/26/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		08/30/2001
 */

public class CommonFeesData implements Serializable
{
	// int 
	protected int ciAddOnFeeExmptIndi;
	protected int ciDieselChrgTonIndi;
	protected int ciDieselFeePrcnt;
	protected int ciFeeCalcCat;
	protected int ciFxdExpMo;
	protected int ciFxdExpYr;
	protected int ciInsProofReqrdIndi;
	protected int ciMaxallowbleRegMo;
	protected int ciMaxMYrPeriodLngth;
	protected int ciMoAdjOptIndi;
	protected int ciPltToOwnrIndi;
	protected int ciRegClassCd;
	protected int ciRegPeriodLngth;
	protected int ciRegPnltyFeePrcnt;
	protected int ciRegPrortnIncrmnt;
	protected int ciRegTrnsfrIndi;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	protected int ciTempAddlWtAllowdIndi;

	// String 
	protected String csRegClassCdDesc;

	// Dollar
	protected Dollar caEmissionsPrcnt;
	protected Dollar caMinRegFee;
	protected Dollar caRegFee;
	protected Dollar caReflectnFee;

	// defect 10695
	protected int ciIVTRSRegNotAllowdIndi; 
	protected int ciRegClassMaxWt;
	protected int ciRegClassMinWt;
	protected int ciSPVCkReqrdIndi;
	protected String csRegClassFeeGrpCd;
	// end defect 10695
	
	private final static long serialVersionUID = -3611291671336744712L; 

	/**
	 * Returns the value of AddOnFeeExmptIndi
	 * 
	 * @return  int 
	 */
	public final int getAddOnFeeExmptIndi()
	{
		return ciAddOnFeeExmptIndi;
	}
	/**
	 * Returns the value of DieselChrgTonIndi
	 * 
	 * @return  int 
	 */
	public final int getDieselChrgTonIndi()
	{
		return ciDieselChrgTonIndi;
	}
	/**
	 * Returns the value of DieselFeePrcnt
	 * 
	 * @return  int 
	 */
	public final int getDieselFeePrcnt()
	{
		return ciDieselFeePrcnt;
	}
	/*
	 * Returns the value of FeeCalcCat
	 * 
	 * @return  Dollar
	 */
	public final Dollar getEmissionsPrcnt()
	{
		return caEmissionsPrcnt;
	}
	/**
	 * Returns the value of FeeCalcCat
	 * 
	 * @return  int  
	 */
	public final int getFeeCalcCat()
	{
		return ciFeeCalcCat;
	}
	/**
	 * Returns the value of FxdExpMo
	 * 
	 * @return  int  
	 */
	public final int getFxdExpMo()
	{
		return ciFxdExpMo;
	}
	/**
	 * Returns the value of FxdExpYr
	 * 
	 * @return  int  
	 */
	public final int getFxdExpYr()
	{
		return ciFxdExpYr;
	}
	/**
	 * Returns the value of InsProofReqrdIndi
	 * 
	 * @return int
	 */
	public final int getInsProofReqrdIndi()
	{
		return ciInsProofReqrdIndi;
	}

	/**
	 * Get value of ciIVTRSRegNotAllowdIndi
	 * 
	 * @return int
	 */
	public final int getIVTRSRegNotAllowdIndi()
	{
		return ciIVTRSRegNotAllowdIndi;
	}
	/**
	 * Returns the value of MaxallowbleRegMo
	 * 
	 * @return  int  
	 */
	public final int getMaxallowbleRegMo()
	{
		return ciMaxallowbleRegMo;
	}
	/**
	 * Returns the value of MaxMYrPeriodLngth
	 * 
	 * @return  int  
	 */
	public final int getMaxMYrPeriodLngth()
	{
		return ciMaxMYrPeriodLngth;
	}
	/**
	 * Returns the value of MinRegFee
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getMinRegFee()
	{
		return caMinRegFee;
	}
	/**
	 * Returns the value of MoAdjOptIndi
	 * 
	 * @return  int  
	 */
	public final int getMoAdjOptIndi()
	{
		return ciMoAdjOptIndi;
	}
	/**
	 * Returns the value of PltToOwnrIndi
	 * 
	 * @return  int 
	 */
	public final int getPltToOwnrIndi()
	{
		return ciPltToOwnrIndi;
	}
	/**
	 * Returns the value of ReflectnFee
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getReflectnFee()
	{
		return caReflectnFee;
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
	 * Returns the value of RegClassCdDesc
	 * 
	 * @return  String 
	 */
	public final String getRegClassCdDesc()
	{
		return csRegClassCdDesc;
	}
	/**
	 * Returns value of RegClassFeeGrpCd
	 * 
	 * @return String
	 */
	public final String getRegClassFeeGrpCd()
	{
		return csRegClassFeeGrpCd;
	}

	/**
	 * Get value of ciRegClassMinWt
	 * 
	 * @return int
	 */
	public int getRegClassMaxWt()
	{
		return ciRegClassMaxWt;
	}

	/**
	 * Get value of ciRegClassMinWt
	 * 
	 * @return int
	 */
	public int getRegClassMinWt()
	{
		return ciRegClassMinWt;
	}
	/**
	 * Returns the value of RegFee
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getRegFee()
	{
		return caRegFee;
	}
	/**
	 * Returns the value of RegPeriodLngth
	 * 
	 * @return  int  
	 */
	public final int getRegPeriodLngth()
	{
		return ciRegPeriodLngth;
	}
	/**
	 * Returns the value of RegPnltyFeePrcnt
	 * 
	 * @return  int  
	 */
	public final int getRegPnltyFeePrcnt()
	{
		return ciRegPnltyFeePrcnt;
	}
	/**
	 * Returns the value of RegPrortnIncrmnt
	 * 
	 * @return  int  
	 */
	public final int getRegPrortnIncrmnt()
	{
		return ciRegPrortnIncrmnt;
	}
	/**
	 * Returns the value of RegTrnsfrIndi
	 * 
	 * @return  int  
	 */
	public final int getRegTrnsfrIndi()
	{
		return ciRegTrnsfrIndi;
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
	 * Get value of ciSPVCkReqrdIndi
	 * 
	 * @return int
	 */
	public final int getSPVCkReqrdIndi()
	{
		return ciSPVCkReqrdIndi;
	}
	/**
	 * Returns the value of TempAddlWtAllowdIndi
	 * 
	 * @return  int  
	 */
	public final int getTempAddlWtAllowdIndi()
	{
		return ciTempAddlWtAllowdIndi;
	}

	/**
	 * Returns boolean to denote if IVTRSRegNotAllowdIndi = 0
	 * 
	 * @return  boolean
	 */
	public final boolean isIVTRSRegAllowd()
	{
		return ciIVTRSRegNotAllowdIndi == 0;
	}

	/**
	 * Returns boolean to denote if IVTRSRegNotAllowdIndi = 1
	 * 
	 * @return  boolean
	 */
	public final boolean isIVTRSRegNotAllowd()
	{
		return ciIVTRSRegNotAllowdIndi == 1;
	}

	/**
	 * Returns boolean to denote if SPVCkReqrdIndi = 1
	 * 
	 * @return  boolean
	 */
	public final boolean isSPVCkReqrd()
	{
		return ciSPVCkReqrdIndi == 1;
	}

	/**
	 * This method sets the value of AddOnFeeExmptIndi.
	 * 
	 * @param aiAddOnFeeExmptIndi   int 
	 */
	public final void setAddOnFeeExmptIndi(int aiAddOnFeeExmptIndi)
	{
		ciAddOnFeeExmptIndi = aiAddOnFeeExmptIndi;
	}
	/**
	 * This method sets the value of DieselChrgTonIndi.
	 * 
	 * @param aiDieselChrgTonIndi   int 
	 */
	public final void setDieselChrgTonIndi(int aiDieselChrgTonIndi)
	{
		ciDieselChrgTonIndi = aiDieselChrgTonIndi;
	}
	/**
	 * This method sets the value of DieselFeePrcnt.
	 * 
	 * @param aiDieselFeePrcnt   int 
	 */
	public final void setDieselFeePrcnt(int aiDieselFeePrcnt)
	{
		ciDieselFeePrcnt = aiDieselFeePrcnt;
	}

	/*
	 * This method sets the value of DieselFeePrcnt.
	 * 
	 * @param aiDieselFeePrcnt	Dollar 
	 */
	public final void setEmissionsPrcnt(Dollar aaEmissionsPrcnt)
	{
		caEmissionsPrcnt = aaEmissionsPrcnt;
	}
	/**
	 * This method sets the value of FeeCalcCat.
	 * 
	 * @param aiFeeCalcCat   int  
	 */
	public final void setFeeCalcCat(int aiFeeCalcCat)
	{
		ciFeeCalcCat = aiFeeCalcCat;
	}
	/**
	 * This method sets the value of FxdExpMo.
	 * 
	 * @param aiFxdExpMo   int  
	 */
	public final void setFxdExpMo(int aiFxdExpMo)
	{
		ciFxdExpMo = aiFxdExpMo;
	}
	/**
	 * This method sets the value of FxdExpYr.
	 * 
	 * @param aiFxdExpYr   int  
	 */
	public final void setFxdExpYr(int aiFxdExpYr)
	{
		ciFxdExpYr = aiFxdExpYr;
	}
	/**
	 * This method sets the value of InsProofReqrdIndi
	 * 
	 * @param aiInsProofReqrdIndi int
	 */
	public final void setInsProofReqrdIndi(int aiInsProofReqrdIndi)
	{
		ciInsProofReqrdIndi = aiInsProofReqrdIndi;
	}

	/**
	 * Set value ofciIVTRSRegNotAllowdIndi
	 * 
	 * @param aiIVTRSRegNotAllowdIndi
	 */
	public final void setIVTRSRegNotAllowdIndi(int aiIVTRSRegNotAllowdIndi)
	{
		ciIVTRSRegNotAllowdIndi = aiIVTRSRegNotAllowdIndi;
	}
	/**
	 * This method sets the value of MaxallowbleRegMo.
	 * 
	 * @param aiMaxallowbleRegMo   int  
	 */
	public final void setMaxallowbleRegMo(int aiMaxallowbleRegMo)
	{
		ciMaxallowbleRegMo = aiMaxallowbleRegMo;
	}
	/**
	 * This method sets the value of MaxMYrPeriodLngth.
	 * 
	 * @param aiMaxMYrPeriodLngth   int  
	 */
	public final void setMaxMYrPeriodLngth(int aiMaxMYrPeriodLngth)
	{
		ciMaxMYrPeriodLngth = aiMaxMYrPeriodLngth;
	}
	/**
	 * This method sets the value of MinRegFee.
	 * 
	 * @param aaMinRegFee   Dollar  
	 */
	public final void setMinRegFee(Dollar aaMinRegFee)
	{
		caMinRegFee = aaMinRegFee;
	}
	/**
	 * This method sets the value of MoAdjOptIndi.
	 * 
	 * @param aiMoAdjOptIndi   int  
	 */
	public final void setMoAdjOptIndi(int aiMoAdjOptIndi)
	{
		ciMoAdjOptIndi = aiMoAdjOptIndi;
	}
	/**
	 * This method sets the value of PltToOwnrIndi.
	 * 
	 * @param aiPltToOwnrIndi   int 
	 */
	public final void setPltToOwnrIndi(int aiPltToOwnrIndi)
	{
		ciPltToOwnrIndi = aiPltToOwnrIndi;
	}
	/**
	 * This method sets the value of ReflectnFee.
	 * 
	 * @param aaReflectnFee   Dollar  
	 */
	public final void setReflectnFee(Dollar aaReflectnFee)
	{
		caReflectnFee = aaReflectnFee;
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
	 * This method sets the value of RegClassCdDesc.
	 * 
	 * @param asRegClassCdDesc   String 
	 */
	public final void setRegClassCdDesc(String asRegClassCdDesc)
	{
		csRegClassCdDesc = asRegClassCdDesc;
	}

	/**
	 * This method sets the value of RegClassFeeGrpCd
	 * 
	 * @param asRegClassFeeGrpCd
	 */
	public final void setRegClassFeeGrpCd(String asRegClassFeeGrpCd)
	{
		csRegClassFeeGrpCd = asRegClassFeeGrpCd;
	}

	/**
	 * Set value of ciRegClassMaxWt
	 * 
	 * @param aiRegClassMaxWt
	 */
	public final void setRegClassMaxWt(int aiRegClassMaxWt)
	{
		ciRegClassMaxWt = aiRegClassMaxWt;
	}

	/**
	 * Set value of ciRegClassMinWt
	 * 
	 * @param aiRegClassMinWt
	 */
	public final void setRegClassMinWt(int aiRegClassMinWt)
	{
		ciRegClassMinWt = aiRegClassMinWt;
	}
	/**
	 * This method sets the value of RegFee.
	 * 
	 * @param aaRegFee   Dollar 
	 */
	public final void setRegFee(Dollar aaRegFee)
	{
		caRegFee = aaRegFee;
	}
	/**
	 * This method sets the value of RegPeriodLngth.
	 * 
	 * @param aiRegPeriodLngth   int  
	 */
	public final void setRegPeriodLngth(int aiRegPeriodLngth)
	{
		ciRegPeriodLngth = aiRegPeriodLngth;
	}
	/**
	 * This method sets the value of RegPnltyFeePrcnt.
	 * 
	 * @param aiRegPnltyFeePrcnt   int  
	 */
	public final void setRegPnltyFeePrcnt(int aiRegPnltyFeePrcnt)
	{
		ciRegPnltyFeePrcnt = aiRegPnltyFeePrcnt;
	}
	/**
	 * This method sets the value of RegPrortnIncrmnt.
	 * 
	 * @param aiRegPrortnIncrmnt   int  
	 */
	public final void setRegPrortnIncrmnt(int aiRegPrortnIncrmnt)
	{
		ciRegPrortnIncrmnt = aiRegPrortnIncrmnt;
	}
	/**
	 * This method sets the value of RegTrnsfrIndi.
	 * 
	 * @param aiRegTrnsfrIndi   int  
	 */
	public final void setRegTrnsfrIndi(int aiRegTrnsfrIndi)
	{
		ciRegTrnsfrIndi = aiRegTrnsfrIndi;
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
	 * Set value of ciSPVCkReqrdIndi
	 * 
	 * @param aiSPVCkReqrdIndi
	 */
	public final void setSPVCkReqrdIndi(int aiSPVCkReqrdIndi)
	{
		ciSPVCkReqrdIndi = aiSPVCkReqrdIndi;
	}
	/**
	 * This method sets the value of TempAddlWtAllowdIndi.
	 * 
	 * @param aiTempAddlWtAllowdIndi   int  
	 */
	public final void setTempAddlWtAllowdIndi(int aiTempAddlWtAllowdIndi)
	{
		ciTempAddlWtAllowdIndi = aiTempAddlWtAllowdIndi;
	}

}
