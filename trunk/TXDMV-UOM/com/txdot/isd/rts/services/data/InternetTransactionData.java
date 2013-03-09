package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Hashtable;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 * InternetTransactionData.java 
 *
 * (c) Texas Department of Transportation  2002
 *
 *----------------------------------------------------------------------
 * Change History 
 * Name         Date        Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/29/2005	Moved & renamed from webapps.data.
 * 							Previously InternetTransData 
 * 							Java 1.4 cleanup 
 * 							defect 7889 Ver 5.2.3    
 *----------------------------------------------------------------------
 */

/**
 * This class cannot join the compilation at TexasOnline.
 * 
 * @version 5.2.3		09/29/2005
 * @author 	Administrator
 *<br>Creation Date: 	08/12/2002 10:54:25 
 */
public class InternetTransactionData
	implements Serializable, Displayable
{
	Hashtable chtData;
	RTSDate caTransDateTime;

	private final static long serialVersionUID = 2794521293306004778L;

	/**
	 * InternetTransactionData constructor comment.
	 */
	public InternetTransactionData()
	{
		super();
	}

	/**
	 * InternetTransactionData constructor comment.
	 */
	public InternetTransactionData(Hashtable ahtData)
	{
		super();
		chtData = ahtData;
	}

	/**
	 * getAttributes
	 */
	public java.util.Map getAttributes()
	{
		java.util.HashMap lhmHashMap = new java.util.HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHashMap.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException aeIll)
			{
				continue;
			}
		}
		return lhmHashMap;
	}
	/**
	 * Return Hashtable 
	 * 
	 * @return Hashtable 
	 */
	public Hashtable getData()
	{
		return chtData;
	}
	/**
	 * 
	 * Return TransDateTime 
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getTransDateTime()
	{
		return caTransDateTime;
	}
	/**
	 * 
	 * Assign value to Data  
	 * 
	 * @param ahtData
	 */
	public void setData(Hashtable ahtData)
	{
		chtData = ahtData;
	}
	/**
	 * 
	 * Assign value to TransDateTime
	 * 
	 * @param aaTransDateTime RTSDate 
	 */
	public void setTransDateTime(RTSDate aaTransDateTime)
	{
		caTransDateTime = aaTransDateTime;
	}
}
