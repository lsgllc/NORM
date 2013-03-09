package com.txdot.isd.rts.services.data;import com.txdot.isd.rts.services.util.*;/* * * RSPSSysUpdateData.java * * (c) Texas Department of Transportation 2004 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Jeff S.		07/14/2004	Created *							defect 7135 Ver 5.2.1 * K Harrell	05/19/2005	Java 1.4 Work *							defect 7899 Ver 5.2.3 * K Harrell	07/11/2005	deprecate class * 							defect 8281 Ver 5.2.3  * --------------------------------------------------------------------- *//** * Data object containing sending RPRSTK transaction request  * * @version	5.2.3		07/11/2005 * @author	Jeff S.  * <br>Creation Date:	07/14/2004 11:47:46 *//* &RSPSSysUpdateData& */public class RSPSSysUpdateData implements java.io.Serializable{	//	Object/* &RSPSSysUpdateData'caDateAvailable& */	private RTSDate caDateAvailable;	// String /* &RSPSSysUpdateData'csSysUpdate& */	private String csSysUpdate;/* &RSPSSysUpdateData'csSysUpdateDescription& */	private String csSysUpdateDescription;/* &RSPSSysUpdateData'csSysUpdateFileName& */	private String csSysUpdateFileName;/* &RSPSSysUpdateData'serialVersionUID& */	private final static long serialVersionUID = 8469828076953983969L;	/**	 * RspsSysUpdtData constructor comment.	 *//* &RSPSSysUpdateData.RSPSSysUpdateData& */	public RSPSSysUpdateData()	{		super();	}	/**	 * Return value of DateAvailable	 * 	 * @return RTSDate	 *//* &RSPSSysUpdateData.getDateAvailable& */	public RTSDate getDateAvailable()	{		return caDateAvailable;	}	/**	 * Return value of SysUpdate	 * 	 * @return String	 *//* &RSPSSysUpdateData.getSysUpdate& */	public String getSysUpdate()	{		return csSysUpdate;	}	/**	 * Return value of SysUpdateDescription	 * 	 * @return String	 *//* &RSPSSysUpdateData.getSysUpdateDescription& */	public String getSysUpdateDescription()	{		return csSysUpdateDescription;	}	/**	 * Return value of SysUpdateFileName	 * 	 * @return String	 *//* &RSPSSysUpdateData.getSysUpdateFileName& */	public String getSysUpdateFileName()	{		return csSysUpdateFileName;	}	/**	 * Set value of DateAvailable	 * 	 * @param aaDateAvaliable RTSDate	 *//* &RSPSSysUpdateData.setDateAvailable& */	public void setDateAvailable(RTSDate aaDateAvailable)	{		caDateAvailable = aaDateAvailable;	}	/**	 * Set value of SysUpdate	 * 	 * @param asSysUpdate String	 *//* &RSPSSysUpdateData.setSysUpdate& */	public void setSysUpdate(String asSysUpdate)	{		csSysUpdate = asSysUpdate;	}	/**	 * Set value of SysUpdateDescription	 * 	 * @param asSysUpdateDescription String	 *//* &RSPSSysUpdateData.setSysUpdateDescription& */	public void setSysUpdateDescription(String asSysUpdateDescription)	{		csSysUpdateDescription = asSysUpdateDescription;	}	/**	 * Set value of SysUpdateFileName 	 * 	 * @param asSysUpdateFileName String	 *//* &RSPSSysUpdateData.setSysUpdateFileName& */	public void setSysUpdateFileName(String asSysUpdateFileName)	{		csSysUpdateFileName = asSysUpdateFileName;	}}/* #RSPSSysUpdateData# */