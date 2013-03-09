package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * RSPSWsStatusData.java
 *
 * (c) Texas Department of Transportation 2004
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/14/2004	New Class
 * 							defect 7135 Ver 5.2.1
 * Ray Rowehl	08/20/2004	Initialize class String Variables.
 *							This is to prevent null pointer errors.
 *							defect 7135 Ver 5.2.1
 * Ray Rowehl	09/15/2004	Add fields to support the Scan Engine XML
 *							parameter.
 *							add csScanEngine, cScanEngineTimeStmp,
 *							getScanEngine(), setScanEngine(),
 *							getScanEngineTimeStmp(), 
 *							setScanEngineTimeStmp()
 *							defect 7135 Ver 5.2.1
 * K Harrell	12/29/2004	Make RSPSWsStatusData comparable,
 *							JavaDoc/Formatting/Variable Name cleanup
 *							add implements Comparable 
 *							add compareTo()
 *							defect 7801 Ver 5.2.2
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data
 * 							remove ciSubstaid and associated get/set 
 * 							methods  
 *							defect 7899 Ver 5.2.3
 * Jeff S.		09/27/2005	Now collecting the log date.  The date that
 * 							the laptop was flashed. 
 * 							add caLastRSPSProcsdTimeStmp, 
 * 								getLastRSPSProcsdTimeStmp(), 
 * 								setLastRSPSProcsdTimeStmp()
 *							defect 8381 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This is the data class for RSPS Ws Status
 * 
 * @version	5.2.3		09/27/2005
 * @author	Ray Rowehl
 * <br>Creation Date:	07/14/2004 13:12:11
 */

public class RSPSWsStatusData
	implements java.io.Serializable, Comparable
{
	/**
	 * Office Issuance Number
	 */
	private int ciOfcIssuanceNo;

	/**
	 * RSPS Site Type.
	 * Either D for dealer or S for Subcon
	 */
	private String csLocIdCd = "";
	/**
	 * RSPS Number.
	 * Either the Dealer or Subcon Number
	 */
	private int ciLocId;
	/**
	 * Laptop Identifier
	 */
	private String csRSPSId = "";
	/**
	 * IP Address for Laptop
	 */
	private String csIPAddr = "";
	/**
	 * RSPS Version
	 */
	private String csRSPSVersion = "";
	/**
	 * TimeStamp for when RSPS Version changed.
	 */
	private RTSDate caRSPSVersionTimeStmp;
	/**
	 * RSPS Jar Size
	 */
	private String csRSPSJarSize = "";
	/**
	 * TimeStamp for when Jar Size changed.
	 */
	private RTSDate caRSPSJarSizeTimeStmp;
	/**
	 * Date from RSPS Jar
	 */
	private RTSDate caRSPSJarDate;
	/**
	 * TimeStamp for when RSPS Jar Date changed.
	 */
	private RTSDate caRSPSJarDateTimeStmp;
	/**
	 * Application Database Version
	 */
	private String csDbVersion = "";
	/**
	 * TimeStamp for when Application Database Version changed.
	 */
	private RTSDate caDbVersionTimeStmp;
	/**
	 * Virus Scan DAT Level
	 */
	private int ciDATLvl = 0;
	/**
	 * TimeStamp for when Virus Scan DAT Level changed.
	 */
	private RTSDate caDATLvlTimeStmp;
	/**
	 * Virus Scan Engine Level Number
	 */
	private String csScanEngine = "";
	/**
	 * TimeStamp for when Virus Scan Engine Level Number changed.
	 */
	private RTSDate caScanEngineTimeStmp;
	// defect 8381
	/**
	 * TimeStamp for when the RSPS laptop was last updated.
	 */
	private RTSDate caLastRSPSProcsdTimeStmp;
	// end defect 8381
	/**
	 * TimeStamp for when this RSPS row was last updated.
	 */
	private RTSDate caLastProcsdTimeStmp;
	/**
	 * POS Host name from where the last update was processed.
	 */
	private String csLastProcsdHostName;
	/**
	 * The UserName of the Employee who processed the last update.
	 */
	private String csLastProcsdUserName;
	private final static long serialVersionUID = 5274873612297888888L;
	/**
	 * RSPSWsStatusData constructor comment.
	 */
	public RSPSWsStatusData()
	{
		super();
	}
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
	 * @param   aaObject the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(java.lang.Object aaObject)
	{
		RSPSWsStatusData laRSPSWsStatusData =
			(RSPSWsStatusData) aaObject;
		return getRSPSId().compareTo(laRSPSWsStatusData.getRSPSId());
	}
	/**
	 * Returns the value of DATLvl
	 *
	 * @return  int 
	 */
	public final int getDATLvl()
	{
		return ciDATLvl;
	}
	/**
	 * Returns the value of DATLvlTimeStmp
	 *
	 * @return  RTSDate 
	 */
	public final RTSDate getDATLvlTimeStmp()
	{
		return caDATLvlTimeStmp;
	}
	/**
	 * Returns the value of DbVersion
	 *
	 * @return  String 
	 */
	public final String getDbVersion()
	{
		return csDbVersion;
	}
	/**
	 * Returns the value of DbVersionTimeStmp
	 *
	 * @return  RTSDate 
	 */
	public final RTSDate getDbVersionTimeStmp()
	{
		return caDbVersionTimeStmp;
	}
	/**
	 * Returns the value of IPAddr
	 * 
	 * @return  String 
	 */
	public final String getIPAddr()
	{
		return csIPAddr;
	}
	/**
	 * Returns the value of LastProcsdHostName
	 *
	 * @return  String 
	 */
	public final String getLastProcsdHostName()
	{
		return csLastProcsdHostName;
	}
	/**
	 * Returns the value of LastProcsdTimeStmp
	 *
	 * @return  RTSDate 
	 */
	public final RTSDate getLastProcsdTimeStmp()
	{
		return caLastProcsdTimeStmp;
	}
	/**
	 * Returns the value of LastProcsdUserName
	 *
	 * @return  String 
	 */
	public final String getLastProcsdUserName()
	{
		return csLastProcsdUserName;
	}
	/**
	 * Returns the value of LastRSPSProcsdTimeStmp
	 * 
	 * @return RTSDate
	 */
	public final RTSDate getLastRSPSProcsdTimeStmp()
	{
		return caLastRSPSProcsdTimeStmp;
	}
	/**
	 * Returns the value of LocId
	 *	 
	 * @return  int 
	 */
	public final int getLocId()
	{
		return ciLocId;
	}
	/**
	 * Returns the value of LocIdCd
	 *	 
	 * @return  String 
	 */
	public final String getLocIdCd()
	{
		return csLocIdCd;
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
	 * Returns the value of RSPSId
	 *	 
	 * @return  String 
	 */
	public final String getRSPSId()
	{
		return csRSPSId;
	}
	/**
	 * Return the value of RSPSJarDate
	 *	 
	 * @return RTSDate
	 */
	public RTSDate getRSPSJarDate()
	{
		return caRSPSJarDate;
	}
	/**
	 * Return the value of RSPSJarDateTimeStmp
	 *	 
	 * @return RTSDate
	 */
	public RTSDate getRSPSJarDateTimeStmp()
	{
		return caRSPSJarDateTimeStmp;
	}
	/**
	 * Returns the value of RSPSJarSize
	 *
	 * @return  String 
	 */
	public final String getRSPSJarSize()
	{
		return csRSPSJarSize;
	}
	/**
	 * Returns the value of RSPSJarSizeTimeStmp
	 *
	 * @return  RTSDate 
	 */
	public final RTSDate getRSPSJarSizeTimeStmp()
	{
		return caRSPSJarSizeTimeStmp;
	}
	/**
	 * Returns the value of RSPSVersion
	 *
	 * @return  String 
	 */
	public final String getRSPSVersion()
	{
		return csRSPSVersion;
	}
	/**
	 * Returns the value of RSPSVersionTimeStmp
	 *
	 * @return  RTSDate 
	 */
	public final RTSDate getRSPSVersionTimeStmp()
	{
		return caRSPSVersionTimeStmp;
	}
	/**
	 * Returns the value of ScanEngine
	 *
	 * @return  String 
	 */
	public final String getScanEngine()
	{
		return csScanEngine;
	}
	/**
	 * Returns the value of ScanEngineTimeStmp
	 *
	 * @return  RTSDate 
	 */
	public final RTSDate getScanEngineTimeStmp()
	{
		return caScanEngineTimeStmp;
	}

	/**
	 * Sets the value of DATLvl
	 *
	 * @param aiDATLvl
	 */
	public final void setDATLvl(int aiDATLvl)
	{
		ciDATLvl = aiDATLvl;
	}
	/**
	 * Sets the value of DATLvlTimeStmp
	 *
	 * @param aaDATLvlTimeStmp RTSDate
	 */
	public final void setDATLvlTimeStmp(RTSDate aaDATLvlTimeStmp)
	{
		caDATLvlTimeStmp = aaDATLvlTimeStmp;
	}
	/**
	 * Sets the value of DbVersion
	 *
	 * @param asDbVersion String
	 */
	public final void setDbVersion(String asDbVersion)
	{
		csDbVersion = asDbVersion;
	}
	/**
	 * Sets the value of DbVersionTimeStmp
	 *
	 * @param aaDbVersionTimeStmp RTSDate
	 */
	public final void setDbVersionTimeStmp(RTSDate aaDbVersionTimeStmp)
	{
		caDbVersionTimeStmp = aaDbVersionTimeStmp;
	}
	/**
	 * Sets the value of IPAddr
	 *
	 * @param asIPAddr String
	 */
	public final void setIPAddr(String asIPAddr)
	{
		csIPAddr = asIPAddr;
	}
	/**
	 * Sets the value of LastProcsdHostName
	 *
	 * @param asLastProcsdHostName String
	 */
	public final void setLastProcsdHostName(String asLastProcsdHostName)
	{
		csLastProcsdHostName = asLastProcsdHostName;
	}
	/**
	 * Sets the value of LastProcsdTimeStmp
	 *
	 * @param aaLastProcsdTimeStmp RTSDate
	 */
	public final void setLastProcsdTimeStmp(RTSDate aaLastProcsdTimeStmp)
	{
		caLastProcsdTimeStmp = aaLastProcsdTimeStmp;
	}
	/**
	 * Sets the value of LastProcsdUserName
	 *
	 * @param asLastProcsdUserName String
	 */
	public final void setLastProcsdUserName(String asLastProcsdUserName)
	{
		csLastProcsdUserName = asLastProcsdUserName;
	}
	/**
	 * Sets the value of RSPS processed TimeStamp.
	 * This is the date & time of when the Laptop was last updated.
	 * 
	 * @param aaLastRSPSProcsdTimeStmp RTSDate
	 */
	public final void setLastRSPSProcsdTimeStmp(RTSDate aaLastRSPSProcsdTimeStmp)
	{
		caLastRSPSProcsdTimeStmp = aaLastRSPSProcsdTimeStmp;
	}
	/**
	 * Sets the value of LocId
	 *
	 * @param aiLocId int
	 */
	public final void setLocId(int aiLocId)
	{
		ciLocId = aiLocId;
	}
	/**
	 * Sets the value of LocIdCd
	 *
	 * @param asLocIdCd String 
	 */
	public final void setLocIdCd(String asLocIdCd)
	{
		csLocIdCd = asLocIdCd;
	}
	/**
	 * Sets the value of OfcIssuanceNo
	 *
	 * @param aiOfcIssuanceNo int
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Sets the value of RSPSId
	 *
	 * @param asRSPSId String
	 */
	public final void setRSPSId(String asRSPSId)
	{
		csRSPSId = asRSPSId;
	}
	/**
	 * sets the value of RSPSJarDate
	 *
	 * @param aaRSPSJarDate RTSDate
	 */
	public void setRSPSJarDate(RTSDate aaRSPSJarDate)
	{
		caRSPSJarDate = aaRSPSJarDate;
	}
	/**
	 * sets the value of RSPSJarDateTimeStmp
	 *
	 * @param aaRSPSJarDateTimeStmp RTSDate
	 */
	public void setRSPSJarDateTimeStmp(RTSDate aaRSPSJarDateTimeStmp)
	{
		caRSPSJarDateTimeStmp = aaRSPSJarDateTimeStmp;
	}
	/**
	 * Sets the value of RSPSJarSize
	 *
	 * @param aaRSPSJarSize String
	 */
	public final void setRSPSJarSize(String asRSPSJarSize)
	{
		csRSPSJarSize = asRSPSJarSize;
	}
	/**
	 * Sets the value of RSPSJarSizeTimeStmp
	 *
	 * @param aaRSPSJarSizeTimeStmp RTSDate
	 */
	public final void setRSPSJarSizeTimeStmp(RTSDate aaRSPSJarSizeTimeStmp)
	{
		caRSPSJarSizeTimeStmp = aaRSPSJarSizeTimeStmp;
	}
	/**
	 * Sets the value of RSPSVersion
	 *
	 * @param asRSPSVersion String
	 */
	public final void setRSPSVersion(String asRSPSVersion)
	{
		csRSPSVersion = asRSPSVersion;
	}
	/**
	 * Sets the value of RSPSVersionTimeStmp
	 *
	 * @param aaRSPSVersionTimeStmp RTSDate
	 */
	public final void setRSPSVersionTimeStmp(RTSDate aaRSPSVersionTimeStmp)
	{
		caRSPSVersionTimeStmp = aaRSPSVersionTimeStmp;
	}
	/**
	 * Sets the value of ScanEngine
	 *
	 * @param asScanEngine String
	 */
	public final void setScanEngine(String asScanEngine)
	{
		csScanEngine = asScanEngine;
	}
	/**
	 * Sets the value of ScanEngineTimeStmp
	 *
	 * @param aaScanEngineTimeStmp RTSDate
	 */
	public final void setScanEngineTimeStmp(RTSDate aaScanEngineTimeStmp)
	{
		caScanEngineTimeStmp = aaScanEngineTimeStmp;
	}
}
