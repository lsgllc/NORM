package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * AssignedWorkstationIdsData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005  Java 1.4 Work
 * 							moved to services.data
 * 							Compare should be on WSID vs. CASHWSID 	
 *							modify compareTo()
 *							defect 7908 Ver 5.2.3 
 * Jeff S.		07/24/2006	Added new field to AssignedWorkstations 
 * 							table to manage the sticker version.
 * 							add ciStkrVersionNo
 * 							add getStkrVersionNo(), setStkrVersionNo()
 * 							defect 8829 Ver. 5.2.4
 * K Harrell	08/17/2009	add SERVER_CD, WORKSTATION_CD  
 * 							add isServer(), add isWorkstation()
 * 							defect 8628 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * AssignedWorkstationIdsData
 *
 * @version	Defect_POS_F	08/17/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		09/04/2001 
 */

public class AssignedWorkstationIdsData
	implements Serializable, Comparable, Displayable
{
	// int
	protected int ciAMDate; // for SendCache
	protected int ciCashWsId;
	protected int ciDeleteIndi;
	protected int ciOfcIssuanceCd;
	protected int ciOfcIssuanceNo = Integer.MIN_VALUE;
	protected int ciRedirPrtWsId;
	// defect 8829
	// Use 1 for the default sticker version, just in case
	// the table is not populated.
	protected int ciStkrVersionNo;
	// end defect 8829
	protected int ciSubstaId = Integer.MIN_VALUE;
	protected int ciTranstime; // for SendCache
	protected int ciWsId = Integer.MIN_VALUE;

	// Object 
	protected RTSDate caChngTimestmp;

	// String
	protected String csCashWsCd;
	protected String csCPName;
	protected String csProdStatusCd;
	protected String csTimeZone;
	protected String csWsCd;

	// defect 8628 
	private final static String SERVER_CD = "S"; 
	private final static String WORKSTATION_CD = "W";
	// end defect 8628 
	
	private final static long serialVersionUID = -3817673804876964589L;

	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 * 
	 * @param   aaObject Object
	 * @return  int
	 */
	public int compareTo(Object aaObject)
	{
		// defect 7908
		// Compare based upon WSID vs. CASHWSID 
		//return getCashWsId() - ((AssignedWorkstationIdsData) o).getCashWsId();
		return getWsId()
			- ((AssignedWorkstationIdsData) aaObject).getWsId();
		// end defect 7908 
	}
	/**
	 * Returns value of AMDate
	 * 
	 * @return int
	 */
	public int getAMDate()
	{
		return ciAMDate;
	}
	/**
	 * Return Map of attributes
	 *
	 * @return Map
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException aeIAEx)
			{
				continue;
			}
		}
		return lhmHash;
	}
	/**
	 * Returns the value of CashWsCd
	 * 
	 * @return  String 
	 */
	public final String getCashWsCd()
	{
		return csCashWsCd;
	}
	/**
	 * Returns the value of CashWsId
	 * 
	 * @return  int 
	 */
	public final int getCashWsId()
	{
		return ciCashWsId;
	}
	/**
	 * Returns the value of ChngTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * Returns the value of CPName
	 * 
	 * @return  String 
	 */
	public final String getCPName()
	{
		return csCPName;
	}
	/**
	 * Returns the value of DeleteIndi
	 * 
	 * @return  int 
	 */
	public final int getDeleteIndi()
	{
		return ciDeleteIndi;
	}
	/**
	 * Returns the value of OfcIssuanceCd
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of ProdStatusCd
	 * 
	 * @return  String 
	 */
	public final String getProdStatusCd()
	{
		return csProdStatusCd;
	}
	/**
	 * Returns the value of RedirPrtWsId
	 * 
	 * @return  int 
	 */
	public final int getRedirPrtWsId()
	{
		return ciRedirPrtWsId;
	}
	/**
	 * Gets the sticker version number.
	 * 
	 * @return int
	 */
	public final int getStkrVersionNo()
	{
		return ciStkrVersionNo;
	}
	/**
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Returns the value of TimeZone
	 * 
	 * @return  String 
	 */
	public final String getTimeZone()
	{
		return csTimeZone;
	}
	/**
	 * Returns the value of TransTime
	 * 
	 * @return int
	 */
	public int getTranstime()
	{
		return ciTranstime;
	}
	/**
	 * Returns the value of WsCd
	 * 
	 * @return  String 
	 */
	public final String getWsCd()
	{
		return csWsCd;
	}
	/**
	 * Returns the value of WsId
	 * 
	 * @return  int 
	 */
	public final int getWsId()
	{
		return ciWsId;
	}
	
	/** 
	 * Return boolean to denote that workstation is "Data" Server
	 * 
	 */
	public boolean isServer()
	{
		return csWsCd != null && csWsCd.equals(SERVER_CD); 	
	}
	
	/** 
	 * Return boolean to denote that workstation is Workstation 
	 * 
	 */
	public boolean isWorkstation()
	{
		return csWsCd != null && csWsCd.equals(WORKSTATION_CD); 	
	}
	
	/**
	 * Set value of AMDate
	 * 
	 * @param aiAMDate int
	 */
	public void setAMDate(int aiAMDate)
	{
		ciAMDate = aiAMDate;
	}
	/**
	 * This method sets the value of CashWsCd.
	 * 
	 * @param asCashWsCd   String 
	 */
	public final void setCashWsCd(String asCashWsCd)
	{
		csCashWsCd = asCashWsCd;
	}
	/**
	 * This method sets the value of CashWsId.
	 * 
	 * @param aiCashWsId   int 
	 */
	public final void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * This method sets the value of ChngTimestmp.
	 * 
	 * @param aaChngTimestmp   RTSDate 
	 */
	public final void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}
	/**
	 * This method sets the value of CPName.
	 * 
	 * @param asCPName   String 
	 */
	public final void setCPName(String asCPName)
	{
		csCPName = asCPName;
	}
	/**
	 * This method sets the value of DeleteIndi.
	 * 
	 * @param aiDeleteIndi   int 
	 */
	public final void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}
	/**
	 * This method sets the value of OfcIssuanceCd.
	 * 
	 * @param aiOfcIssuanceCd   int 
	 */
	public final void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of ProdStatusCd.
	 * 
	 * @param asProdStatusCd   String 
	 */
	public final void setProdStatusCd(String asProdStatusCd)
	{
		csProdStatusCd = asProdStatusCd;
	}
	/**
	 * This method sets the value of RedirPrtWsId.
	 * 
	 * @param aiRedirPrtWsId   int 
	 */
	public final void setRedirPrtWsId(int aiRedirPrtWsId)
	{
		ciRedirPrtWsId = aiRedirPrtWsId;
	}
	/**
	 * Sets the sticker version number.
	 * 
	 * @param aiStkrVersionNo int
	 */
	public final void setStkrVersionNo(int aiStkrVersionNo)
	{
		ciStkrVersionNo = aiStkrVersionNo;
	}
	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of TimeZone.
	 * 
	 * @param asTimeZone   String 
	 */
	public final void setTimeZone(String asTimeZone)
	{
		csTimeZone = asTimeZone;
	}
	/**
	 * This method sets the value of TransTime
	 * 
	 * @param aiTranstime int
	 */
	public void setTranstime(int aiTranstime)
	{
		ciTranstime = aiTranstime;
	}
	/**
	 * This method sets the value of WsCd.
	 * 
	 * @param asWsCd   String 
	 */
	public final void setWsCd(String asWsCd)
	{
		csWsCd = asWsCd;
	}
	/**
	 * This method sets the value of WsId.
	 * 
	 * @param aiWsId   int 
	 */
	public final void setWsId(int aiWsId)
	{
		ciWsId = aiWsId;
	}
}
