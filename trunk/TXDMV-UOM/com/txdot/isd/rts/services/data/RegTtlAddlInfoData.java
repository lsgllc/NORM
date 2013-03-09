package com.txdot.isd.rts.services.data;

/*
 *
 * RegTtlAddlInfoData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
  B Arredondo	09/08/2003	Added getter/setter indicator for 
 *							Charge Title TERP Fee
 *							defect 6448  Ver 5.1.5
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	10/19/2007	add ciChrgPltTrnsfrFeeIndi, get/set methods
 * 							add ciChrgBuyerTagFeeIndi, get/set methods
 * 							defect 9368 Ver Special Plates 2 
 * K Harrell	10/10/2011	add ciPTOTrnsfrIndi, get/set methods
 * 							delete ciChrgPltTrnsfrFeeIndi, get/set methods
 * 							defect 11030 Ver 6.9.0 	
 * K Harrell	10/16/2011	add ciChrgRbltSlvgFeeIndi, get/set methods
 * 							defect 11051 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * Represents the data captured on the Registration Additional Info 
 * screen [REG039] and Title Additional Info screen [TTL008]
 * 
 * @version	6.9.0		10/16/2011
 * @author	Sunil Govindappa
 * @since 				10/16/2001 16:35:19
 */

public class RegTtlAddlInfoData implements java.io.Serializable
{
	// int
	private int ciApprhndFndsCntyNo;
	private int ciAddlEviSurndIndi;
	private int ciChrgAddlTknTrlrFeeIndi;
	private int ciChrgFeeIndi;
	// defect 11051 
	private int ciChrgRbltSlvgFeeIndi; 
	// end defect 11051 
	private int ciChrgTrnsfrFeeIndi;
	// defect 9368
	// add indicator to denote GDN indentification on TTL007 w/
	//  PTO RegClassCd/RegPltCd selection   
	private int ciChrgBuyerTagFeeIndi;
	// add indicator to denote selection of 
	// "Charge Transfer Fee" checkbox  on TTL008
	private int ciPTOTrnsfrIndi;
	// end defect 9368
	private int ciChrgTtlFeeIndi;
	private int ciChrgTtlTERPFeeIndi = 1;
	private int ciCorrtnEffMo; //Apprehended Eff yr of correction
	private int ciCorrtnEffYr; //Apprehended Eff mo of correction
	private int ciNoChrgRegEmiFeeIndi;
	private int ciNewPltDesrdIndi;
	private int ciProcsByMailIndi;
	private int ciRegExpiredReason;
	private int ciTrnsfrPnltyIndi;

	private final static long serialVersionUID = -5155016198686218294L;

	/**
	 * RegTtlAddlInfoData constructor comment.
	 */
	public RegTtlAddlInfoData()
	{
		super();
		ciNoChrgRegEmiFeeIndi = 1;
	}
	/**
	 * Return value of AddlEviSurndIndi
	 * 
	 * @return int
	 */
	public int getAddlEviSurndIndi()
	{
		return ciAddlEviSurndIndi;
	}
	/**
	 * Return value of ApprhndFndsCntyNo
	 * 
	 * @return int
	 */
	public int getApprhndFndsCntyNo()
	{
		return ciApprhndFndsCntyNo;
	}
	/**
	 * Return value of ChrgAddlTknTrlrFeeIndi
	 * 
	 * @return int
	 */
	public int getChrgAddlTknTrlrFeeIndi()
	{
		return ciChrgAddlTknTrlrFeeIndi;
	}
	/**
	 * Return value of ChrgBuyerTagFeeIndi
	* 
	* @return int 
	*/
	public int getChrgBuyerTagFeeIndi()
	{
		return ciChrgBuyerTagFeeIndi;
	}
	/**
	 * Return value of ChrgFeeIndi
	 * 
	 * @return int
	 */
	public int getChrgFeeIndi()
	{
		return ciChrgFeeIndi;
	}
	
	/**
	 * @return int
	 */
	public int getChrgRbltSlvgFeeIndi()
	{
		return ciChrgRbltSlvgFeeIndi;
	}
	
	/**
	 * Return value of ChrgTrnsfrFeeIndi
	 * 
	 * @return int
	 */
	public int getChrgTrnsfrFeeIndi()
	{
		return ciChrgTrnsfrFeeIndi;
	}
	/**
	 * Return value of ChrgTtlFeeIndi
	 * 
	 * @return int
	 */
	public int getChrgTtlFeeIndi()
	{
		return ciChrgTtlFeeIndi;
	}
	/**
	 * Return value of ChrgTtlTERPFeeIndi
	 * 
	 * @return int
	 */
	public int getChrgTtlTERPFeeIndi()
	{
		return ciChrgTtlTERPFeeIndi;
	}
	/**
	 * Return value of CorrtnEffMo
	 * 
	 * @return int
	 */
	public int getCorrtnEffMo()
	{
		return ciCorrtnEffMo;
	}
	/**
	 * Return value of CorrtnEffYr
	 * 
	 * @return int
	 */
	public int getCorrtnEffYr()
	{
		return ciCorrtnEffYr;
	}
	/**
	 * Return value of NewPltDesrdIndi
	 *
	 * @return int
	 */
	public int getNewPltDesrdIndi()
	{
		return ciNewPltDesrdIndi;
	}
	/**
	 * Return value of NoChrgRegEmiFeeIndi
	 * 
	 * @return int
	 */
	public int getNoChrgRegEmiFeeIndi()
	{
		return ciNoChrgRegEmiFeeIndi;
	}
	/**
	 * Return value of ProcsByMailIndi
	 * 
	 * @return int
	 */
	public int getProcsByMailIndi()
	{
		return ciProcsByMailIndi;
	}
	/**
	 * Return value of ciPTOTrnsfrIndi
	 * 
	 * @return int 
	 */
	public int getPTOTrnsfrIndi()
	{
		return ciPTOTrnsfrIndi;
	}

	/**
	 * Return value of RegExpiredReason
	 * 
	 * @return int
	 */
	public int getRegExpiredReason()
	{
		return ciRegExpiredReason;
	}
	/**
	 * Return value of TrnsfrPnltyIndi
	 * 
	 * @return int
	 */
	public int getTrnsfrPnltyIndi()
	{
		return ciTrnsfrPnltyIndi;
	}
	/**
	 * Set value of AddlEviSurndIndi
	 * 
	 * @param iaAddlEviSurndIndi int
	 */
	public void setAddlEviSurndIndi(int aiAddlEviSurndIndi)
	{
		ciAddlEviSurndIndi = aiAddlEviSurndIndi;
	}
	/**
	 * Set value of ApprhndFndsCntyNo
	 * 
	 * @param aiApprhndFndsCntyNo int
	 */
	public void setApprhndFndsCntyNo(int aiApprhndFndsCntyNo)
	{
		ciApprhndFndsCntyNo = aiApprhndFndsCntyNo;
	}
	/**
	 * Set value of ChrgFeeIndi
	 * 
	 * @param aiChrgAddlTknTrlrFeeIndi int
	 */
	public void setChrgAddlTknTrlrFeeIndi(int aiChrgAddlTknTrlrFeeIndi)
	{
		ciChrgAddlTknTrlrFeeIndi = aiChrgAddlTknTrlrFeeIndi;
	}
	/**
	 * Set value of ChrgBuyerTagFeeIndi
	 * 
	 * @param aiChrgBuyerTagFeeIndi
	 */
	public void setChrgBuyerTagFeeIndi(int aiChrgBuyerTagFeeIndi)
	{
		ciChrgBuyerTagFeeIndi = aiChrgBuyerTagFeeIndi;
	}
	/**
	 * Set value of ChrgFeeIndi
	 * 
	 * @param aiChrgFeeIndi int
	 */
	public void setChrgFeeIndi(int aiChrgFeeIndi)
	{
		ciChrgFeeIndi = aiChrgFeeIndi;
	}
	
	/**
	 * @param aiChrgRbltSlvgFeeIndi 
	 */
	public void setChrgRbltSlvgFeeIndi(int aiChrgRbltSlvgFeeIndi)
	{
		ciChrgRbltSlvgFeeIndi = aiChrgRbltSlvgFeeIndi;
	}
	/**
	 * Set value of ChrgTrnsfrFeeIndi
	 * 
	 * @param aiChrgTrnsfrFeeIndi int
	 */
	public void setChrgTrnsfrFeeIndi(int aiChrgTrnsfrFeeIndi)
	{
		ciChrgTrnsfrFeeIndi = aiChrgTrnsfrFeeIndi;
	}
	/**
	 * Set value of ChrgTtlFeeIndi
	 * 
	 * @param aiChrgTtlFeeIndi int
	 */
	public void setChrgTtlFeeIndi(int aiChrgTtlFeeIndi)
	{
		ciChrgTtlFeeIndi = aiChrgTtlFeeIndi;
	}
	/**
	 * Set value of ChrgTtlTERPFeeIndi
	 * 
	 * @param aiChrgTtlTerpFeeIndi int
	 */
	public void setChrgTtlTERPFeeIndi(int aiChrgTtlTERPFeeIndi)
	{
		ciChrgTtlTERPFeeIndi = aiChrgTtlTERPFeeIndi;
	}
	/**
	 * Set value of CorrtnEffMo
	 * 
	 * @param aiCorrtnEffMo int
	 */
	public void setCorrtnEffMo(int aiCorrtnEffMo)
	{
		ciCorrtnEffMo = aiCorrtnEffMo;
	}
	/**
	 * Set value of CorrtnEffYr
	 * 
	 * @param aiCorrtnEffYr int
	 */
	public void setCorrtnEffYr(int aiCorrtnEffYr)
	{
		ciCorrtnEffYr = aiCorrtnEffYr;
	}
	/**
	 * Set value of NewPltDesrdIndi
	 *
	 * @param aiNewPltDesrdIndi int
	 */
	public void setNewPltDesrdIndi(int aiNewPltDesrdIndi)
	{
		ciNewPltDesrdIndi = aiNewPltDesrdIndi;
	}
	/**
	 * Set value of NoChrgRegEmiFeeIndi
	 * 
	 * @param aiChrgRegEmiFee int
	 */
	public void setNoChrgRegEmiFeeIndi(int aiNoChrgRegEmiFeeIndi)
	{
		ciNoChrgRegEmiFeeIndi = aiNoChrgRegEmiFeeIndi;
	}
	/**
	 * Set value of ProcsByMailIndi
	 * 
	 * @param aiProcsByMailIndi int
	 */
	public void setProcsByMailIndi(int aiProcsByMailIndi)
	{
		ciProcsByMailIndi = aiProcsByMailIndi;
	}
	/**
	 * Set value of ciPTOTrnsfrIndi
	 * 
	 * @param aiPTOTrnsfrIndi
	 */
	public void setPTOTrnsfrIndi(int aiPTOTrnsfrIndi)
	{
		ciPTOTrnsfrIndi = aiPTOTrnsfrIndi;
	}
	
	/**
	 * Set value of RegExpiredReason
	 * 
	 * @param aiRegExpiredReason int
	 */
	public void setRegExpiredReason(int aiRegExpiredReason)
	{
		ciRegExpiredReason = aiRegExpiredReason;
	}
	/**
	 * Set value of TrnsfrPnltyIndi
	 * 
	 * @param aiTrnsfrPnltyIndi int
	 */
	public void setTrnsfrPnltyIndi(int aiTrnsfrPnltyIndi)
	{
		ciTrnsfrPnltyIndi = aiTrnsfrPnltyIndi;
	}

}
