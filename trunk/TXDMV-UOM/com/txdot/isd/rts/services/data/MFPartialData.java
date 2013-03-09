package com.txdot.isd.rts.services.data;

import java.io.*;

/*
 *
 * MFPartialData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * J Rue		04/04/2008	Add getter/setter DissociatedCd
 * 							add getDissociatedCd(), setDissociatedCd()
 * 							defect 9557 Ver Defect_POS_A
 * J Rue		04/30/2008	Replace DissociateCd with PltRmvCd
 * 							PltRmvCd replace DissociateCd
 * 							add getPltRmvCd(), setPltRmvCd()
 * 							defect 9557, Ver Defect_POS_A
 * J Rue		06/11/2008	Change Ver to Defect_POS_A
 * 							defect 9557 Ver Defect_POS_A
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * MFPartialData 
 * 
 * @version	Defect_POS_A	06/11/2008 
 * @author	Administrator
 * <br>Creation Date:		08/22/2001 13:28:00     
 */

public class MFPartialData implements Serializable
{

	// int 
	private int ciRegExpMo;
	private int ciRegExpYr;
	private int ciVehModlYr;
	// defect 9630
	//	Add dissociateCd
	// defect 9557
	//	DissociateCd has been change to PltRmvCd
	// private int csDissociateCd;
	private int ciPltRmvCd;
	// end defect 9557
	// end defect 9630

	// String
	private String csDocNo;
	private String csFiller;
	private String csOwnrTtlName;
	private String csRegPltNo;
	private String csVehMk;
	private String csVin;

	private final static long serialVersionUID = -8836108020115872198L;
	/**
	 * MFPartialData constructor comment.
	 */
	public MFPartialData()
	{
		super();
	}
	/**
	 * Return value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}
	/**
	 * Return value of Filler
	 * 
	 * @return String
	 */
	public String getFiller()
	{
		return csFiller;
	}
	/**
	 * Return value of OwnrTtlName
	 * 
	 * @return String
	 */
	public String getOwnrTtlName()
	{
		return csOwnrTtlName;
	}
	/**
	 * Return value of RegExpMo
	 * 
	 * @return int
	 */
	public int getRegExpMo()
	{
		return ciRegExpMo;
	}
	/**
	 * Return value of RegExpYr
	 * 
	 * @return int
	 */
	public int getRegExpYr()
	{
		return ciRegExpYr;
	}
	/**
	 * Return value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
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
	 * Return value of VehModlYr
	 * 
	 * @return int
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
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
	 * Set value of DocNo 
	 * 
	 * @param asDocNo String
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}
	/**
	 * Set value of Filler 
	 * 
	 * @param asFiller String
	 */
	public void setFiller(String asFiller)
	{
		csFiller = asFiller;
	}
	/**
	 * Set value of OwnrTtlName 
	 * 
	 * @param asOwnrTtlName String
	 */
	public void setOwnrTtlName(String asOwnrTtlName)
	{
		csOwnrTtlName = asOwnrTtlName;
	}
	/**
	 * Set value of RegExpMo 
	 * 
	 * @param aiRegExpMo int
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}
	/**
	 * Set value of RegExpYr 
	 * 
	 * @param aiRegExpYr int
	 */
	public void setRegExpYr(int aiRegExpYr)
	{
		ciRegExpYr = aiRegExpYr;
	}
	/**
	 * Set value of RegPltNo 
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
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
	 * Set value of VehModlYr 
	 * 
	 * @param aiVehModlYr int
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
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
	 * get DissociatedCd
	 * 
	 * @return
	 */
//	public int getDissociateCd()
//	{
//		return csDissociateCd;
//	}

	/**
	 * set DissociatedCd
	 * 
	 * @param aiDissociated
	 */
//	public void setDissociateCd(int aiDissociated)
//	{
//		csDissociateCd = aiDissociated;
//	}

	/**
	 * get Plate Remove Cd
	 * 
	 * @return int
	 */
	public int getPltRmvCd()
	{
		return ciPltRmvCd;
	}

	/**
	 * set Plate Remove Cd
	 * @param aiPltRmvCd
	 */
	public void setPltRmvCd(int aiPltRmvCd)
	{
		ciPltRmvCd = aiPltRmvCd;
	}

}
