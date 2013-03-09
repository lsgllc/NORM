package com.txdot.isd.rts.services.data;

import java.io.File;
import java.lang.reflect.Field;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * ReceiptLogData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add getAttributes()new class variables and 
 * 							associated getters/setters.
 * 							add import reflect.*;
 *							add serialVersionUID
 *							add getAttributes()
 * 							Ver 5.2.0	
 * J Zwiener	06/22/2005	Java 1.4 changes
 * 							Format source, organize imports, rename
 * 							fields.
 * 							defect 7892 Ver 5.2.3 
 * J Zwiener	06/24/2005	Moved class from client.misc.ui
 * 							defect 7892 Ver 5.2.3
 * K Harrell	12/27/2010	add csPermitFile, get/set methods 
 * 							defect 10700 Ver 6.7.0 	
 * K Harrell	04/08/2011	add printFileExists() 
 * 							defect 10796 Ver 6.7.1  
 * K Harrell	06/19/2011	add csPrmtIssuanceId, get/set methods 
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
*/

/**
 * This data class contains attributes and get/set methods for
 * ReceiptLogData
 * 
 * @version	6.8.0 			06/19/2011
 * @author	Administrator
 * <br>Creation Date: 		08/30/2001
 */

public class ReceiptLogData
	implements java.io.Serializable, Displayable
{
	private String csBarCodeFile;
	private String csPermitFile;
	private String csRcptFileName;
	private String csTransCd;
	private String csTransId;
	private String csTransType;
	private String csVIN;

	// defect 10844 
	private String csPrmtIssuanceId;
	// end defect 10844 

	private final static long serialVersionUID = -1164225979446141580L;

	/**
	 * ReceiptLogData constructor comment.
	 */
	public ReceiptLogData()
	{
		super();
	}

	/**
	 * Returns a Map of the internal attributes.  
	 * 
	 * @return java.util.Map
	 */
	public java.util.Map getAttributes()
	{
		java.util.HashMap hash = new java.util.HashMap();
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			try
			{
				hash.put(fields[i].getName(), fields[i].get(this));
			}
			catch (IllegalAccessException e)
			{
				continue;
			}
		}
		return hash;
	}

	/**
	 * Return value of csBarCodeFile
	 * 
	 * @return csBarCodeFile String 
	 */
	public String getBarCodeFile()
	{
		return csBarCodeFile;
	}
	/**
	 * Return value of csPermitFile
	 * 
	 * @return String 
	 */
	public String getPermitFile()
	{
		return csPermitFile;
	}

	/**
	 * Return value of csPrmtIssuanceId
	 * 
	 * @return String
	 */
	public String getPrmtIssuanceId()
	{
		return csPrmtIssuanceId;
	}
	/**
	 * return csRcptFileName.
	 * 
	 * @return csRcptFileName String 
	 */
	public String getRcptFileName()
	{
		return csRcptFileName;
	}
	/**
	 * return csTransCd.
	 * 
	 * @return csTransCd String 
	 */
	public String getTransCd()
	{
		return csTransCd;
	}
	/**
	 * return csTransId.
	 * 
	 * @return csTransId String 
	 */
	public String getTransId()
	{
		return csTransId;
	}
	/**
	 * return csTransType.
	 * 
	 * @return csTransType String 
	 */
	public String getTransType()
	{
		return csTransType;
	}
	/**
	 * return csVIN.
	 * 
	 * @return csVIN String 
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Return boolean to denote if any print file exists
	 *  - Receipt File
	 *  - BarCode File 
	 *  - Permit File 
	 * 
	 * @return boolean
	 */
	public boolean printFileExists()
	{
		return (
			(!UtilityMethods.isEmpty(csRcptFileName)
				&& new File(csRcptFileName).exists())
				|| (!UtilityMethods.isEmpty(csBarCodeFile)
					&& new File(csBarCodeFile).exists())
				|| (!UtilityMethods.isEmpty(csPermitFile)
					&& new File(csPermitFile).exists()));
	}

	/**
	 * Set csBarCodeFile.
	 * 
	 * @param newBarCodeFile String 
	 */
	public void setBarCodeFile(String newBarCodeFile)
	{
		csBarCodeFile = newBarCodeFile;
	}

	/**
	 * Set value of csPermitFile
	 * 
	 * @param asPermitFile
	 */
	public void setPermitFile(String asPermitFile)
	{
		csPermitFile = asPermitFile;
	}

	/**
	 * Set value of csPrmtIssuanceId
	 * 
	 * @param asPrmtIssuanceId
	 */
	public void setPrmtIssuanceId(String asPrmtIssuanceId)
	{
		csPrmtIssuanceId = asPrmtIssuanceId;
	}
	/**
	 * Set csRcptFileName.
	 * 
	 * @param newRcptFileName String 
	 */
	public void setRcptFileName(String newRcptFileName)
	{
		csRcptFileName = newRcptFileName;
	}
	/**
	 * Set csTransCd.
	 * 
	 * @param newTransCd String 
	 */
	public void setTransCd(String newTransCd)
	{
		csTransCd = newTransCd;
	}
	/**
	 * Set csTransCd.
	 *
	 * @param newTransId String 
	 */
	public void setTransId(String newTransId)
	{
		csTransId = newTransId;
	}
	/**
	 * Set csTransType.
	 * 
	 * @param newTransType String 
	 */
	public void setTransType(String newTransType)
	{
		csTransType = newTransType;
	}
	/**
	 * Set csVIN.
	 * 
	 * @param newVIN String 
	 */
	public void setVIN(String newVIN)
	{
		csVIN = newVIN;
	}

}
