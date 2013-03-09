package com.txdot.isd.rts.webservices.adm.data;

import com.txdot.isd.rts.services.data.PlateTypeData;
import com.txdot.isd.rts.services.util.Dollar;

/*
 * RtsPlateTypeData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Mark Reyes	06/12/2008	Created class
 * 							defect 9677 Ver MyPlates_POS 
 * R Pilon		02/02/2012	Added default constructor to prevent web service 
 * 							  validation error.
 * 							add RtsPlateTypeData() constructor
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * This is the format for returning PlateType row to the requestor.
 *
 * @version	6.10.0				02/02/2012
 * @author	Mark Reyes
 * <br>Creation Date:			06/12/2008 12:00:00
 */
public class RtsPlateTypeData
{
	private int ciAnnualPltIndi;
	private int ciDuplsAllowdCd;
	private int ciMandPltReplAge;
	private int ciMaxByteCount;
	private int ciMfgProcsCd;
	private int ciOptPltReplAge;
	private int ciPltSetImprtnceCd;
	private int ciPltSurchargeIndi;
	private int ciRegRenwlCd;
	private int ciRTSEffDate;
	private int ciRTSEffEndDate;
	private int ciSpclPrortnIncrmnt;
	private int ciUserPltNoIndi;
	
	//	String
	private String csBaseRegPltCd;
	private String csDispPltGrpId;
	private String csISAAllowdCd;
	private String csLimitedPltGrpId;
	private String csLocCntyAuthCd;
	private String csLocHQAuthCd;
	private String csLocInetAuthCd;
	private String csLocRegionAuthCd;
	private String csNeedsProgramCd;
	private String csPltOwnrshpCd;
	private String csPLPAcctItmCd;
	private String csRegPltCd;
	private String csRegPltCdDesc;
	private String csRegPltDesign;
	private String csRenwlRtrnAddrCd;
	private String csShpngAddrCd;
	private String csSpclPltType;
	private String csTrnsfrCd;
	
	//	Dollar
	private Dollar caAddlSetApplFee;
	private Dollar caAddlSetRenwlFee;
	private Dollar caFirstSetApplFee;
	private Dollar caFirstSetRenwlFee;
	private Dollar caPLPFee;
	private Dollar caRemakeFee;
	private Dollar caReplFee;

	/**
	 * RtsPlateTypeData.java Constructor
	 */
	public RtsPlateTypeData()
	{
		super();
	}

	/**
	 * RtsPlateTypeData.java Constructor
	 * 
	 * 
	 */
	public RtsPlateTypeData(PlateTypeData aaPLT)
	{
		super();
		ciAnnualPltIndi = aaPLT.getAnnualPltIndi();
		ciDuplsAllowdCd = aaPLT.getDuplsAllowdCd();
		ciMandPltReplAge = aaPLT.getMandPltReplAge();
		ciMaxByteCount = aaPLT.getMaxByteCount();
		ciMfgProcsCd = aaPLT.getMfgProcsCd();
		ciOptPltReplAge = aaPLT.getOptPltReplAge();
		ciPltSetImprtnceCd = aaPLT.getPltSetImprtnceCd();
		ciPltSurchargeIndi = aaPLT.getPltSurchargeIndi();
		ciRegRenwlCd = aaPLT.getRegRenwlCd();
		ciRTSEffDate = aaPLT.getRTSEffDate();
		ciRTSEffEndDate = aaPLT.getRTSEffEndDate();
		ciSpclPrortnIncrmnt = aaPLT.getSpclPrortnIncrmnt();
		ciUserPltNoIndi = aaPLT.getUserPltNoIndi();
		csBaseRegPltCd = aaPLT.getBaseRegPltCd();
		csDispPltGrpId = aaPLT.getDispPltGrpId();
		csISAAllowdCd = aaPLT.getISAAllowdCd();
		csLimitedPltGrpId = aaPLT.getLimitedPltGrpId();
		csLocCntyAuthCd = aaPLT.getLocCntyAuthCd();
		csLocHQAuthCd = aaPLT.getLocHQAuthCd();
		csLocInetAuthCd = aaPLT.getLocInetAuthCd();
		csLocRegionAuthCd = aaPLT.getLocRegionAuthCd();
		csNeedsProgramCd = aaPLT.getNeedsProgramCd();
		csPltOwnrshpCd = aaPLT.getPltOwnrshpCd();
		csPLPAcctItmCd = aaPLT.getPLPAcctItmCd();
		csRegPltCd = aaPLT.getRegPltCd();
		csRegPltCdDesc = aaPLT.getRegPltCdDesc();
		csRegPltDesign = aaPLT.getRegPltDesign();
		csRenwlRtrnAddrCd = aaPLT.getRenwlRtrnAddrCd();
		csShpngAddrCd = aaPLT.getShpngAddrCd();
		csSpclPltType = aaPLT.getSpclPltType();
		csTrnsfrCd = aaPLT.getTrnsfrCd();
		caAddlSetApplFee = aaPLT.getAddlSetApplFee();
		caAddlSetRenwlFee = aaPLT.getAddlSetRenwlFee();
		caFirstSetApplFee = aaPLT.getFirstSetApplFee();
		caFirstSetRenwlFee = aaPLT.getAddlSetRenwlFee();
		caPLPFee = aaPLT.getPLPFee();
		caRemakeFee = aaPLT.getRemakeFee();
		caReplFee = aaPLT.getReplFee();
			
		
		
		
	}
	
	/**
	 * Return AddlSetApplFee
	 * 
	 * @return Dollar
	 */
		public Dollar getAddlSetApplFee()
		{
			return caAddlSetApplFee;
		}

	/**
	 * Return value of caAddlSetRenwlFee
	 * 
	 * @return Dollar
	 */
		public Dollar getAddlSetRenwlFee()
		{
			return caAddlSetRenwlFee;
		}

	/**
	 * Return value of caFirstSetApplFee 
	 * 
	 * @return Dollar
	 */
		public Dollar getFirstSetApplFee()
		{
			return caFirstSetApplFee;
		}

	/**
	 * Return value of caFirstSetRenwlFee 
	 * 
	 * @return Dollar
	 */
		public Dollar getFirstSetRenwlFee()
		{
			return caFirstSetRenwlFee;
		}

	/**
	 * Return value of caPLPFee 
	 * 
	 * @return Dollar
	 */
		public Dollar getPLPFee()
		{
			return caPLPFee;
		}

	/**
	 * Return value of caRemakeFee
	 * 
	 * @return Dollar
	 */
		public Dollar getRemakeFee()
		{
			return caRemakeFee;
		}

	/**
	 * Return value of caReplFee 
	 * 
	 * @return Dollar
	 */
		public Dollar getReplFee()
		{
			return caReplFee;
		}
	
	/**
	 * Return value of ciAnnualPltIndi
	 * 
	 * @return int
	 */
		public int getAnnualPltIndi()
		{
			return ciAnnualPltIndi;
		}
	/**
	 * Return value of ciDuplsAllowdCd
	 * 
	 * @return  int
	 */
		public int getDuplsAllowdCd()
		{
			return ciDuplsAllowdCd;
		}
	/**
	 * Return value of ciMandPltReplAge
	 * 
	 * @return  int
	 */
		public int getMandPltReplAge()
		{
			return ciMandPltReplAge;
		}

	/**
	 * Return value of ciMaxByteCount
	 * 
	 * @return  int
	 */
		public int getMaxByteCount()
		{
			return ciMaxByteCount;
		}

	/**
	 * Return value of ciMfgProcsCd
	 * 
	 * @return  int
	 */
		public int getMfgProcsCd()
		{
			return ciMfgProcsCd;
		}

	/**
	 * Return value of ciOptPltReplAge
	 * 
	 * @return  int
	 */
		public int getOptPltReplAge()
		{
			return ciOptPltReplAge;
		}

	/**
	 * Return value of ciPltSetImprtnceCd
	 * 
	 * @return  int
	 */
		public int getPltSetImprtnceCd()
		{
			return ciPltSetImprtnceCd;
		}

	/**
	 * Return value of ciPltSurchargeIndi
	 * 
	 * @return  int
	 */
		public int getPltSurchargeIndi()
		{
			return ciPltSurchargeIndi;
		}

	/**
	 * Return value of ciRegRenwlCd
	 * 
	 * @return  int
	 */
		public int getRegRenwlCd()
		{
			return ciRegRenwlCd;
		}

	/**
	 * Return value of ciRTSEffDate
	 * 
	 * @return  int
	 */
		public int getRTSEffDate()
		{
			return ciRTSEffDate;
		}

	/**
	 * Return value of ciRTSEffEndDate
	 * 
	 * @return  int
	 */
		public int getRTSEffEndDate()
		{
			return ciRTSEffEndDate;
		}

	/**
	 * Return value of ciSpclPrortnIncrmnt
	 * 
	 * @return  int
	 */
		public int getSpclPrortnIncrmnt()
		{
			return ciSpclPrortnIncrmnt;
		}

	/**
	 * Return value of ciUserPltNoIndi
	 * 
	 * @return  int
	 */
		public int getUserPltNoIndi()
		{
			return ciUserPltNoIndi;
		}

	/**
	 * Return value of csBaseRegPltCd
	 * 
	 * @return String
	 */
		public String getBaseRegPltCd()
		{
			return csBaseRegPltCd;
		}

	/**
	 * Return value of csDispPltGrpId 
	 * 
	 * @return String
	 */
		public String getDispPltGrpId()
		{
			return csDispPltGrpId;
		}

	/**
	 * Return value of csISAAllowdCd
	 * 
	 * @return String
	 */
		public String getISAAllowdCd()
		{
			return csISAAllowdCd;
		}

	/**
	 * Return value of csLimitedPltGrpId
	 * 
	 * @return String
	 */
		public String getLimitedPltGrpId()
		{
			return csLimitedPltGrpId;
		}

	/**
	 * Return value of csLocCntyAuthCd
	 * 
	 * @return String
	 */
		public String getLocCntyAuthCd()
		{
			return csLocCntyAuthCd;
		}
	
	/**
	 * Return value of csLocHQAuthCd
	 * 
	 * @return String
	 */
		public String getLocHQAuthCd()
		{
			return csLocHQAuthCd;
		}

	/**
	 * Return value of csLocInetAuthCd
	 * 
	 * @return String
	 */
		public String getLocInetAuthCd()
		{
			return csLocInetAuthCd;
		}

	/**
	 * Return value of csLocRegionAuthCd
	 * 
	 * @return String
	 */
		public String getLocRegionAuthCd()
		{
			return csLocRegionAuthCd;
		}

	/**
	 * Return value of csNeedsProgramCd
	 * 
	 * @return String
	 */
		public String getNeedsProgramCd()
		{
			return csNeedsProgramCd;
		}

	/**
	 * Return value of csPLPAcctItmCd
	 * 
	 * @return String
	 */
		public String getPLPAcctItmCd()
		{
			return csPLPAcctItmCd;
		}

	/**
	 * Return value of csPltOwnrshpCd
	 * 
	 * @return String
	 */
		public String getPltOwnrshpCd()
		{
			return csPltOwnrshpCd;
		}

	/**
	 * Return value of csRegPltCd
	 * 
	 * @return String
	 */
		public String getRegPltCd()
		{
			return csRegPltCd;
		}

	/**
	 * Return value of csRegPltDesign
	 * 
	 * @return String
	 */
		public String getRegPltDesign()
		{
			return csRegPltDesign;
		}

	/**
	 * Return value of getRenwlRtrnAddrCd
	 * 
	 * @return String
	 */
		public String getRenwlRtrnAddrCd()
		{
			return csRenwlRtrnAddrCd;
		}

	/**
	 * Return value of csShpngAddrCd
	 * 
	 * @return String
	 */
		public String getShpngAddrCd()
		{
			return csShpngAddrCd;
		}

	/**
	 * Return value of csSpclPltType
	 * 
	 * @return String
	 */
		public String getSpclPltType()
		{
			return csSpclPltType;
		}

	/**
	 * Return value of csTrnsfrCd
	 * 
	 * @return String
	 */
		public String getTrnsfrCd()
		{
			return csTrnsfrCd;
		}
		
	/**
		 * Sets value of caAddlSetApplFee
		 * 
		 * @param aaAddlSetApplFee
		 */
		public void setAddlSetApplFee(Dollar aaAddlSetApplFee)
		{
			caAddlSetApplFee = aaAddlSetApplFee;
		}

		/**
		 * Sets value of 
		 * 
		 * @param dollar
		 */
		public void setAddlSetRenwlFee(Dollar aaAddlSetRenwlFee)
		{
			caAddlSetRenwlFee = aaAddlSetRenwlFee;
		}

		/**
		 * Sets value of caFirstSetApplFee
		 * 
		 * @param aaFirstSetApplFee
		 */
		public void setFirstSetApplFee(Dollar aaFirstSetApplFee)
		{
			caFirstSetApplFee = aaFirstSetApplFee;
		}

		/**
		 * Sets value of caFirstSetRenwlFee
		 * 
		 * @param aaFirstSetRenwlFee
		 */
		public void setFirstSetRenwlFee(Dollar aaFirstSetRenwlFee)
		{
			caFirstSetRenwlFee = aaFirstSetRenwlFee;
		}

		/**
		 * Sets value of caPLPFee
		 * 
		 * @param aaPLPFee
		 */
		public void setPLPFee(Dollar aaPLPFee)
		{
			caPLPFee = aaPLPFee;
		}

		/**
		 * Sets value of caRemakeFee
		 * 
		 * @param aaRemakeFee
		 */
		public void setRemakeFee(Dollar aaRemakeFee)
		{
			caRemakeFee = aaRemakeFee;
		}

		/**
		 * Sets value of caReplFee
		 * 
		 * @param aaReplFee
		 */
		public void setReplFee(Dollar aaReplFee)
		{
			caReplFee = aaReplFee;
		}

		/**
		 * Sets value of ciAnnualPltIndi
		 * 
		 * @param aiAnnualPltIndi
		 */
		public void setAnnualPltIndi(int aiAnnualPltIndi)
		{
			ciAnnualPltIndi = aiAnnualPltIndi;
		}

		/**
		 * Sets value of ciDuplsAllowdCd
		 * 
		 * @param aiDuplsAllowdCd
		 */
		public void setDuplsAllowdCd(int aiDuplsAllowdCd)
		{
			ciDuplsAllowdCd = aiDuplsAllowdCd;
		}

		/**
		 * Sets value of ciMandPltReplAge
		 * 
		 * @param aiMandPltReplAge
		 */
		public void setMandPltReplAge(int aiMandPltReplAge)
		{
			ciMandPltReplAge = aiMandPltReplAge;
		}

		/**
		 * Sets value of ciMaxByteCount
		 * 
		 * @param aiMaxByteCount
		 */
		public void setMaxByteCount(int aiMaxByteCount)
		{
			ciMaxByteCount = aiMaxByteCount;
		}

		/**
		 * Sets value of ciMfgProcsCd 
		 * 
		 * @param aiMfgProcsCd
		 */
		public void setMfgProcsCd(int aiMfgProcsCd)
		{
			ciMfgProcsCd = aiMfgProcsCd;
		}

		/**
		 * Sets value of ciOptPltReplAge
		 * 
		 * @param aiOptPltReplAge
		 */
		public void setOptPltReplAge(int aiOptPltReplAge)
		{
			ciOptPltReplAge = aiOptPltReplAge;
		}

		/**
		 * Sets value of ciPltSetImprtnceCd
		 * 
		 * @param aiPltSetImprtnceCd
		 */
		public void setPltSetImprtnceCd(int aiPltSetImprtnceCd)
		{
			ciPltSetImprtnceCd = aiPltSetImprtnceCd;
		}

		/**
		 * Sets value of ciPltSurchargeIndi
		 * 
		 * @param aiPltSurchargeIndi
		 */
		public void setPltSurchargeIndi(int aiPltSurchargeIndi)
		{
			ciPltSurchargeIndi = aiPltSurchargeIndi;
		}

		/**
		 * Sets value of ciRegRenwlCd
		 * 
		 * @param ciRegRenwlCd
		 */
		public void setRegRenwlCd(int aiRegRenwlCd)
		{
			ciRegRenwlCd = aiRegRenwlCd;
		}

		/**
		 * Sets value of ciRTSEffDate 
		 * 
		 * @param aiRTSEffDate
		 */
		public void setRTSEffDate(int aiRTSEffDate)
		{
			ciRTSEffDate = aiRTSEffDate;
		}

		/**
		 * Sets value of ciRTSEffEndDate
		 * 
		 * @param aiRTSEffEndDate
		 */
		public void setRTSEffEndDate(int aiRTSEffEndDate)
		{
			ciRTSEffEndDate = aiRTSEffEndDate;
		}

		/**
		 * Sets value of ciSpclPrortnIncrmnt
		 * 
		 * @param aiSpclPrortnIncrmnt
		 */
		public void setSpclPrortnIncrmnt(int aiSpclPrortnIncrmnt)
		{
			ciSpclPrortnIncrmnt = aiSpclPrortnIncrmnt;
		}

		/**
		 * Sets value of ciUserPltNoIndi
		 * 
		 * @param aiUserPltNoIndi
		 */
		public void setUserPltNoIndi(int aiUserPltNoIndi)
		{
			ciUserPltNoIndi = aiUserPltNoIndi;
		}

		/**
		 * Sets value of csBaseRegPltCd
		 * 
		 * @param asBaseRegPltCd
		 */
		public void setBaseRegPltCd(String asBaseRegPltCd)
		{
			csBaseRegPltCd = asBaseRegPltCd;
		}

		/**
		 * Sets value of csDispPltGrpId
		 * 
		 * @param asDispPltGrpId
		 */
		public void setDispPltGrpId(String asDispPltGrpId)
		{
			csDispPltGrpId = asDispPltGrpId;
		}

		/**
		 * Sets value of csISAAllowdCd
		 * 
		 * @param asISAAllowdCd
		 */
		public void setISAAllowdCd(String asISAAllowdCd)
		{
			csISAAllowdCd = asISAAllowdCd;
		}

		/**
		 * Sets value of csLimitedPltGrpId
		 * 
		 * @param asLimitedPltGrpId
		 */
		public void setLimitedPltGrpId(String asLimitedPltGrpId)
		{
			csLimitedPltGrpId = asLimitedPltGrpId;
		}

		/**
		 * Sets value of csLocCntyAuthCd
		 * 
		 * @param asLocCntyAuthCd
		 */
		public void setLocCntyAuthCd(String asLocCntyAuthCd)
		{
			csLocCntyAuthCd = asLocCntyAuthCd;
		}

		/**
		 * Sets value of csLocHQAuthCd
		 * 
		 * @param asLocHQAuthCd
		 */
		public void setLocHQAuthCd(String asLocHQAuthCd)
		{
			csLocHQAuthCd = asLocHQAuthCd;
		}

		/**
		 * Sets value of csLocInetAuthCd
		 * 
		 * @param asLocInetAuthCd
		 */
		public void setLocInetAuthCd(String asLocInetAuthCd)
		{
			csLocInetAuthCd = asLocInetAuthCd;
		}

		/**
		 * Sets value of csLocRegionAuthCd
		 * 
		 * @param asLocRegionAuthCd
		 */
		public void setLocRegionAuthCd(String asLocRegionAuthCd)
		{
			csLocRegionAuthCd = asLocRegionAuthCd;
		}

		/**
		 * Sets value of csNeedsProgramCd
		 * 
		 * @param asNeedsProgramCd
		 */
		public void setNeedsProgramCd(String asNeedsProgramCd)
		{
			csNeedsProgramCd = asNeedsProgramCd;
		}

		/**
		 * Sets value of csPLPAcctItmCd
		 * 
		 * @param asPLPAcctItmCd
		 */
		public void setPLPAcctItmCd(String asPLPAcctItmCd)
		{
			csPLPAcctItmCd = asPLPAcctItmCd;
		}

		/**
		 * Sets value of csPltOwnrshpCd 
		 * 
		 * @param asPltOwnrshpCd
		 */
		public void setPltOwnrshpCd(String asPltOwnrshpCd)
		{
			csPltOwnrshpCd = asPltOwnrshpCd;
		}

		/**
		 * Sets value of csRegPltCd
		 * 
		 * @param asRegPltCd
		 */
		public void setRegPltCd(String asRegPltCd)
		{
			csRegPltCd = asRegPltCd;
		}

		/**
		 * Sets value of csRegPltDesign
		 * 
		 * @param asRegPltDesign
		 */
		public void setRegPltDesign(String asRegPltDesign)
		{
			csRegPltDesign = asRegPltDesign;
		}

		/**
		 * Sets value of csRenwlRtrnAddrCd
		 * 
		 * @param asRenwlRtrnAddrCd
		 */
		public void setRenwlRtrnAddrCd(String asRenwlRtrnAddrCd)
		{
			csRenwlRtrnAddrCd = asRenwlRtrnAddrCd;
		}

		/**
		 * Sets value of csShpngAddrCd
		 * 
		 * @param asShpngAddrCd
		 */
		public void setShpngAddrCd(String asShpngAddrCd)
		{
			csShpngAddrCd = asShpngAddrCd;
		}

		/**
		 * Sets value of csSpclPltType
		 * 
		 * @param asSpclPltType
		 */
		public void setSpclPltType(String asSpclPltType)
		{
			csSpclPltType = asSpclPltType;
		}

		/**
		 * Sets value of csTrnsfrCd
		 * 
		 * @param asTrnsfrCd
		 */
		public void setTrnsfrCd(String asTrnsfrCd)
		{
			csTrnsfrCd = asTrnsfrCd;
		}

		/**
		 * Gets value of csRegPltCdDesc
		 * 
		 * @return String
		 */
		public String getRegPltCdDesc()
		{
			return csRegPltCdDesc;
		}

		/**
		 * Sets value of csRegPltCdDesc
		 * 
		 * @param asRegPltCdDesc
		 */
		public void setRegPltCdDesc(String asRegPltCdDesc)
		{
			csRegPltCdDesc = asRegPltCdDesc;
		}

}
