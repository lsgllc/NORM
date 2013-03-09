package com.txdot.isd.rts.services.data;import java.util.Vector;import com.txdot.isd.rts.services.util.RTSDate;/* * * TitleInProcessData.java   * * (c) Texas Department of Transportation 2001 * * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * M Rajangam	10/15/2001	Created for MfAccess method * 							retrieveTitleInProcessData * K Harrell	05/19/2005	Java 1.4 Work *							defect 7899 Ver 5.2.3	 * K Harrell	10/04/2010	add cvInProcsTransDataList, get/set methods. * 							add hasInProcsTrans()  * 							defect 10598 Ver 6.6.0  	 * --------------------------------------------------------------------- *//** * Title Reject/Release information class   *   * @version	6.6.0 		10/04/2010  * @author		Marx Rajangam * <br>Creation Date:	10/15/2001 13:28:57   *//* &TitleInProcessData& */public class TitleInProcessData implements java.io.Serializable{	// int/* &TitleInProcessData'ciDPSStlnIndi& */	private int ciDPSStlnIndi;/* &TitleInProcessData'ciOfcIssuanceNo& */	private int ciOfcIssuanceNo;/* &TitleInProcessData'ciOwnrshpEvidCd& */	private int ciOwnrshpEvidCd;	// String /* &TitleInProcessData'csDocNo& */	private String csDocNo;/* &TitleInProcessData'csOwnrTtlName1& */	private String csOwnrTtlName1;/* &TitleInProcessData'csVehMk& */	private String csVehMk;/* &TitleInProcessData'csVIN& */	private String csVIN;	// defect 10598 /* &TitleInProcessData'cvInProcsTransDataList& */	private Vector cvInProcsTransDataList = new Vector();	// end defect 10598		// Object /* &TitleInProcessData'caTransAMDate& */	private RTSDate caTransAMDate;	/* &TitleInProcessData'serialVersionUID& */	private final static long serialVersionUID = 3742910014890157074L;		/**	 * TitleInProcessData constructor comment.	 *//* &TitleInProcessData.TitleInProcessData& */	public TitleInProcessData()	{		super();	}	/**	 * Return the value of DocNo	 * 	 * @return String	 *//* &TitleInProcessData.getDocNo& */	public String getDocNo()	{		return csDocNo;	}	/**	 * Return the value of DPSStlnIndi	 * 	 * @return int	 *//* &TitleInProcessData.getDPSStlnIndi& */	public int getDPSStlnIndi()	{		return ciDPSStlnIndi;	}	/**	 * Return cvInProcsTransDataList	 * 	 * @return Vector 	 *//* &TitleInProcessData.getInProcsTransDataList& */	public Vector getInProcsTransDataList()	{		return cvInProcsTransDataList;	}	/**	 * Return the value of OfcIssuanceNo	 * 	 * @return int	 *//* &TitleInProcessData.getOfcIssuanceNo& */	public int getOfcIssuanceNo()	{		return ciOfcIssuanceNo;	}	/**	 * Return the value of OwnrshpEvidCd	 * 	 * @return int	 *//* &TitleInProcessData.getOwnrshpEvidCd& */	public int getOwnrshpEvidCd()	{		return ciOwnrshpEvidCd;	}	/**	 * Return the value of OwnrTtlName1	 * 	 * @return String	 *//* &TitleInProcessData.getOwnrTtlName1& */	public String getOwnrTtlName1()	{		return csOwnrTtlName1;	}	/**	 * Return the value of TransAMDate	 * 	 * @return RTSDate	 *//* &TitleInProcessData.getTransAMDate& */	public RTSDate getTransAMDate()	{		return caTransAMDate;	}	/**	 * Return the value of VehMk	 * 	 * @return String	 *//* &TitleInProcessData.getVehMk& */	public String getVehMk()	{		return csVehMk;	}	/**	 * Return the value of VIN	 * 	 * @return String	 *//* &TitleInProcessData.getVIN& */	public String getVIN()	{		return csVIN;	}	/**	 * Has In Process Transactions	 * 	 * @return boolean 	 *//* &TitleInProcessData.hasInProcsTrans& */	public boolean hasInProcsTrans()	{		return cvInProcsTransDataList != null			&& cvInProcsTransDataList.size() > 0;	}	/**	 * Set the value of DocNo	 * 	 * @param asDocNo String	 *//* &TitleInProcessData.setDocNo& */	public void setDocNo(String asDocNo)	{		csDocNo = asDocNo;	}	/**	 * Set the value of DPSStlnIndi	 * 	 * @param aiDPSStlnIndi int	 *//* &TitleInProcessData.setDPSStlnIndi& */	public void setDPSStlnIndi(int aiDPSStlnIndi)	{		ciDPSStlnIndi = aiDPSStlnIndi;	}	/**	 * Set value of cvInProcsTransDataList	 * 	 * @param avInProcsTransDataList	 *//* &TitleInProcessData.setInProcsTransDataList& */	public void setInProcsTransDataList(Vector avInProcsTransDataList)	{		cvInProcsTransDataList = avInProcsTransDataList;	}	/**	 * Set the value of OfcIssuanceNo	 * 	 * @param aiOfcIssuanceNo int	 *//* &TitleInProcessData.setOfcIssuanceNo& */	public void setOfcIssuanceNo(int aiOfcIssuanceNo)	{		ciOfcIssuanceNo = aiOfcIssuanceNo;	}	/**	 * Set the value of OwnrshpEvidCd	 * 	 * @param aiOwnrshpEvidCd int	 *//* &TitleInProcessData.setOwnrshpEvidCd& */	public void setOwnrshpEvidCd(int aiOwnrshpEvidCd)	{		ciOwnrshpEvidCd = aiOwnrshpEvidCd;	}	/**	 * Set the value of OwnrTtlName1	 * 	 * @param asOwnrTtlName1 String	 *//* &TitleInProcessData.setOwnrTtlName1& */	public void setOwnrTtlName1(String asOwnrTtlName1)	{		csOwnrTtlName1 = asOwnrTtlName1;	}	/**	 * Set the value of TransAMDate	 * 	 * @param aaTransAMDate RTSDate	 *//* &TitleInProcessData.setTransAMDate& */	public void setTransAMDate(RTSDate aaTransAMDate)	{		caTransAMDate = aaTransAMDate;	}	/**	 * Set the value of VehMk	 * 	 * @param asVehMk String	 *//* &TitleInProcessData.setVehMk& */	public void setVehMk(String asVehMk)	{		csVehMk = asVehMk;	}	/**	 * Set the value of VIN	 * 	 * @param asVIN String	 *//* &TitleInProcessData.setVIN& */	public void setVIN(String asVIN)	{		csVIN = asVIN;	}}/* #TitleInProcessData# */