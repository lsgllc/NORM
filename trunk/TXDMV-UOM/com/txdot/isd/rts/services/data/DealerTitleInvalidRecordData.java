package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * DealerTitleDataInvalidRecordData.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2009	created.
 * 							defect 10290 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * Insert the class's description here. (do not leave this)
 *
 * @version	Defect_POS_H	12/28/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2009	11:17:00 
 */
public class DealerTitleInvalidRecordData implements Serializable
{
	private String csFieldName;
	private String csFieldValue;
	private String csResetValue;
	
	static final long serialVersionUID = -6639788404776500112L;

	/**
	 * Constructor
	 */
	public DealerTitleInvalidRecordData(
		String asFieldName,
		String asFieldValue,
		String asResetValue)
	{
		super();
		csFieldName = asFieldName;
		csFieldValue = asFieldValue;
		csResetValue = asResetValue;
	}

	/**
	 * Get AbstractValue of FieldName
	 * 
	 * @return String
	 */
	public String getFieldName()
	{
		return csFieldName;
	}

	/**
	 * Get AbstractValue of FieldValue
	 * 
	 * @return String
	 */
	public String getFieldValue()
	{
		return csFieldValue;
	}

	/**
	 * Get AbstractValue of ResetValue
	 * 
	 * @return String
	 */
	public String getResetValue()
	{
		return csResetValue;
	}

	/**
	 * Set AbstractValue of FieldName
	 * 
	 * @param asFieldName
	 */
	public void setFieldName(String asFieldName)
	{
		csFieldName = asFieldName;
	}

	/**
	 * Set AbstractValue of FieldValue
	 * 
	 * @param asFieldValue
	 */
	public void setFieldValue(String asFieldValue)
	{
		csFieldValue = asFieldValue;
	}

	/**
	 * Set AbstractValue of ResetValue
	 * 
	 * @param asResetValue
	 */
	public void setResetValue(String asResetValue)
	{
		csResetValue = asResetValue;
	}

}
