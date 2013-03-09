package com.txdot.isd.rts.server.v21.transaction.data;
/*
 * WsPLDV21ReqData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	02/19/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	04/07/2008	Add ciV21UniqueId, get/set methods 
 * 							defect 9582 Ver 3 Amigos PH B 
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Transaction - Plate Dispositiion Request.
 *
 * @version	3_Amigos_PH_B	04/07/2008
 * @author	B Hargrove
 * <br>Creation Date:		02/06/2008 13:35
 */
public class WsPLDV21ReqData
{

	private String csDocumentNumber = "";
	private String csPlateNumber = "";
	private String csVTNSource = "";

	private int ciPlateDisposition = 0;
	private int ciRegExpMonth = 0;
	private int ciRegExpYear = 0;
	// defect 9582 
	private int ciV21UniqueId = 0;
	// end defect 9528

	private long clSpecialRegID = 0;

	/**
	 * Returns Document Number.
	 * 
	 * @return String
	 */
	public String getV21DocumentNumberReq()
	{
		return csDocumentNumber;
	}

	/**
	 * Returns Plate Number.
	 * 
	 * @return String
	 */
	public String getV21PlateNumberReq()
	{
		return csPlateNumber;
	}

	/**
	 * Returns Plate Disposition.
	 * 
	 * @return int
	 */
	public int getV21PlateDisposition()
	{
		return ciPlateDisposition;
	}

	/**
	 * Returns Registration Expiration Month.
	 * 
	 * @return int
	 */
	public int getV21RegExpMonthReq()
	{
		return ciRegExpMonth;
	}

	/**
	 * Returns Registration Expiration Year.
	 * 
	 * @return int
	 */
	public int getV21RegExpYearReq()
	{
		return ciRegExpYear;
	}

	/**
	 * Returns Special Registration ID.
	 * 
	 * @return long
	 */
	public long getV21SpecialRegIDReq()
	{
		return clSpecialRegID;
	}

	/**
	 * Return V21 UniqueId  
	 * 
	 * @return int
	 */
	public int getV21UniqueId()
	{
		return ciV21UniqueId;
	}

	/**
	 * Returns Vehicle Transaction Notice Source.
	 * 
	 * @return String
	 */
	public String getV21VTNSourceReq()
	{
		return csVTNSource;
	}

	/**
	 * Sets Document Number.
	 * 
	 * @param String asDocumentNumber
	 */
	public void setV21DocumentNumberReq(String asDocumentNumber)
	{
		csDocumentNumber = asDocumentNumber;
	}

	/**
	 * Sets Plate Number.
	 * 
	 * @param String asPlateNumber
	 */
	public void setV21PlateNumberReq(String asPlateNumber)
	{
		csPlateNumber = asPlateNumber;
	}

	/**
	 * Sets Plate Disposition.
	 * 
	 * @param int aiPlateDisposition
	 */
	public void setV21PlateDisposition(int aiPlateDisposition)
	{
		ciPlateDisposition = aiPlateDisposition;
	}

	/**
	 * Sets Registration Expiration Month.
	 * 
	 * @param int aiRegExpMonth
	 */
	public void setV21RegExpMonthReq(int aiRegExpMonth)
	{
		ciRegExpMonth = aiRegExpMonth;
	}

	/**
	 * Sets Registration Expiration Year.
	 * 
	 * @param int aiRegExpYear
	 */
	public void setV21RegExpYearReq(int aiRegExpYear)
	{
		ciRegExpYear = aiRegExpYear;
	}

	/**
	 * Sets Special Registration ID.
	 * 
	 * @param long alSpecialRegID
	 */
	public void setV21SpecialRegIDReq(long alSpecialRegID)
	{
		clSpecialRegID = alSpecialRegID;
	}

	/**
	 * Set V21 UniqueId  
	 * 
	 * @param aiV21UniqueId
	 */
	public void setV21UniqueId(int aiV21UniqueId)
	{
		ciV21UniqueId = aiV21UniqueId;
	}

	/**
	 * Sets Vehicle Transaction Notice Source.
	 * 
	 * @param String asVTNSource
	 */
	public void setV21VTNSourceReq(String asVTNSource)
	{
		csVTNSource = asVTNSource;
	}

}
