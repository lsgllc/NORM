package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * WorkstationStatusData.java
 *
 * (c) Texas Department of Transportation 2004.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/05/2004	Created Class - Data Class to handle 
 *							updating of the RTS_WS_Status table with the
 *							workstation's current status.
 *							defect 6918 Ver 5.1.6
 * K Harrell	04/12/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	03/31/2010	add Displayable, Comparable  
 * 							add WorkstationStatusData() 
 * 							add csCPName, csCDSrvrCPName, cbCDSrvr, 
 * 							 clJarSize, csServletHost, ciServletPort,
 * 							 caLastRestartTstmp, get/set methods. 
 * 							add getAttributes(), compareTo(), 
 * 							 getRTSVersionRTSDate()   
 * 							modify getRTSVerstionDate(), getRTSVersion(), 
 * 							 getCDSrvrCPName(), getCPName(), 
 * 							 getLastRestartTstmp() 
 * 							defect 8087 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This contains the data pertaining to the workstations status.
 *
 * @version	POS_640 		03/31/2010
 * @author	Jeff Seifert
 * <br>Creation Date:		03/05/2004 15:15:53
 */
public class WorkstationStatusData implements Serializable
// defect 8087 
, Displayable, Comparable
// end defect 8087 
{
	private int ciOfcIssuanceNo;
	private int ciSubStaId;
	private int ciWSid;

	// String 
	private String csRTSVersion;
	private String csRTSVersionDate;

	// defect 8087
	private RTSDate caLastRestartTstmp;
	private boolean cbCDSrvr;
	private int ciServletPort;
	private long clJarSize;
	private String csCDSrvrCPName;
	private String csCPName;
	private String csServletHost;
	// end defect 8087 

	private final static long serialVersionUID = 8573817843645547416L;

	/**
	 * Default WorkstationStatusData constructor.
	 */
	public WorkstationStatusData()
	{
		super();
	}

	/**
	 * Returns attributes for display in ShowCache
	 * 
	 * @return HashSet
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
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	/**
	 * Compares this object with the specified object for order.  
	 * Returns a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the specified 
	 * object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.
	 * (This implies that <tt>x.compareTo(y)</tt> must throw an 
	 * exception iff <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt>
	 * implies <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that 
	 * <tt>x.compareTo(y)==0</tt> implies that 
	 * <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for all 
	 * <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally 
	 * speaking, any class that implements the <tt>Comparable</tt> 
	 * interface and violates this condition should clearly indicate 
	 * this fact.  The recommended language is "Note: this class has 
	 * a natural ordering that is inconsistent with equals."
	 * 
	 * @param   aaObj the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this 
	 * object is less than, equal to, or greater than the specified 
	 * object.
	 * 
	 * @throws ClassCastException if the specified object's type 
	 * prevents it from being compared to this Object.
	 */
	public int compareTo(java.lang.Object aaObj)
	{
		WorkstationStatusData laData = (WorkstationStatusData) aaObj;

		boolean lbSame =
			getRTSVersion().equals(laData.getRTSVersion())
				&& getRTSVersionRTSDate().equals(
					laData.getRTSVersionRTSDate())
				&& getCPName().equals(laData.getCPName())
				&& getCDSrvrCPName().equals(laData.getCDSrvrCPName())
				&& isCDSrvr() == laData.isCDSrvr()
				&& getJarSize() == laData.getJarSize()
				&& getServletHost().equals(laData.getServletHost())
				&& getServletPort() == laData.getServletPort();
		return lbSame ? 0 : 1;
	}

	/**
	 * Set value of csCDSrvrCPName
	 * 
	 * @return String 
	 */
	public String getCDSrvrCPName()
	{
		// defect 8087
		return csCDSrvrCPName == null ? new String() : csCDSrvrCPName;
		// end defect 8087
	}

	/**
	 * Set value of csCPName
	 * 
	 * @return String 
	 */
	public String getCPName()
	{
		// defect 8087
		return csCPName == null ? new String() : csCPName;
		// end defect 8087 
	}

	/**
	 * Gets the value of ciJarSize
	 * 
	 * @return long
	 */
	public long getJarSize()
	{
		return clJarSize;
	}

	/**
	 * Gets value of caLastRestartTstmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getLastRestartTstmp()
	{
		// defect 8087 
		return caLastRestartTstmp == null
			? new RTSDate(1900, 1, 1)
			: caLastRestartTstmp;
		// end defect 8087 
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
	 * Returns the value of RTSVersion
	 *
	 * @return  String 
	 */

	public final String getRTSVersion()
	{
		// defect 8087 
		return csRTSVersion == null ? new String() : csRTSVersion;
		// end defect 8087 
	}

	/**
	 * Returns the value of RTSVersionDate
	 *
	 * @return  String 
	 */
	public final String getRTSVersionDate()
	{
		// defect 8087 
		return csRTSVersionDate == null
			? "01/01/1900"
			: csRTSVersionDate;
		// end defect 8087 
	}

	/** 
	 * Returns RTSDate for RTSVersionDate 
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getRTSVersionRTSDate()
	{
		RTSDate laRTSVersionRTSDate = new RTSDate(1900, 1, 1);

		try
		{
			String lsDate = getRTSVersionDate();

			// Month 
			int liPos = lsDate.indexOf('/');
			int liMo = Integer.parseInt(lsDate.substring(0, liPos));
			lsDate = lsDate.substring(liPos + 1);

			// Day 
			liPos = lsDate.indexOf('/');
			int liDay = Integer.parseInt(lsDate.substring(0, liPos));

			// Year 
			int liYear = Integer.parseInt(lsDate.substring(liPos + 1));

			laRTSVersionRTSDate = new RTSDate(liYear, liMo, liDay);

			if (!laRTSVersionRTSDate.isValidDate())
			{
				laRTSVersionRTSDate = new RTSDate(1900, 1, 1);
			}
		}
		catch (Exception aeEx)
		{
			System.out.println(
				"Invalid RTSVersionDate "
					+ csRTSVersionDate
					+ " "
					+ csCPName);
		}
		return laRTSVersionRTSDate;
	}

	/**
	 * Gets the value of csServletHost
	 * 
	 * @return String
	 */
	public String getServletHost()
	{
		return csServletHost == null ? new String() : csServletHost;
	}

	/**
	 * Set value of ciServletPort
	 * 
	 * @return int
	 */
	public int getServletPort()
	{
		return ciServletPort;
	}

	/**
	 * Returns the value of SubStaId
	 *
	 * @return  int 
	 */
	public final int getSubStaId()
	{
		return ciSubStaId;
	}

	/**
	 * Returns the value of WSid
	 *
	 * @return  int 
	 */
	public final int getWSid()
	{
		return ciWSid;
	}

	/**
	 * Get value of cbCDSrvr
	 * 
	 * @return boolean 
	 */
	public boolean isCDSrvr()
	{
		return cbCDSrvr;
	}

	/**
	 * Set value of cbCDSrvr
	 * 
	 * @param abCDSrvr
	 */
	public void setCDSrvr(boolean abCDSrvr)
	{
		cbCDSrvr = abCDSrvr;
	}

	/**
	 * Set value of csCDSrvrCPName
	 * 
	 * @param asCDSrvrCPName
	 */
	public void setCDSrvrCPName(String asCDSrvrCPName)
	{
		csCDSrvrCPName = asCDSrvrCPName;
	}

	/**
	 * Set value of csCPName
	 * 
	 * @param asCPName
	 */
	public void setCPName(String asCPName)
	{
		csCPName = asCPName;
	}

	/**
	 * Sets the value of clJarSize
	 * 
	 * @param aiJarSize
	 */
	public void setJarSize(long alJarSize)
	{
		clJarSize = alJarSize;
	}

	/**
	 * Sets value of caLastRestartTstmp
	 * 
	 * @param aaLastRestartTstmp
	 */
	public void setLastRestartTstmp(RTSDate aaLastRestartTstmp)
	{
		caLastRestartTstmp = aaLastRestartTstmp;
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
	 * This method sets the value of RTSVersion.
	 *
	 * @param asRTSVersion   String 
	 */
	public final void setRTSVersion(String asRTSVersion)
	{
		csRTSVersion = asRTSVersion;
	}

	/**
	 * This method sets the value of RTSVersionDate.
	 *
	 * @param asRTSVersionDate   String 
	 */
	public final void setRTSVersionDate(String asRTSVersionDate)
	{
		csRTSVersionDate = asRTSVersionDate;
	}

	/**
	 * Sets the value of csServletHost
	 * 
	 * @param asServletHost
	 */
	public void setServletHost(String asServletHost)
	{
		csServletHost = asServletHost;
	}

	/**
	 * Set value of ciServletPort
	 * 
	 * @param aiServletPort
	 */
	public void setServletPort(int aiServletPort)
	{
		ciServletPort = aiServletPort;
	}

	/**
	 * This method sets the value of SubStaId.
	 *
	 * @param aiSubStaId   int 
	 */
	public final void setSubStaId(int aiSubStaId)
	{
		ciSubStaId = aiSubStaId;
	}

	/**
	 * This method sets the value of WSid.
	 *
	 * @param aiWSid   int 
	 */
	public final void setWSid(int aiWSid)
	{
		ciWSid = aiWSid;
	}
}
