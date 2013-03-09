package com.txdot.isd.rts.server.v21.vehicleinfo.data;

import com.txdot.isd.rts.services.util.Log;

/*
 * WsVehicleInfoV21DataRes.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/14/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/07/2008	Add a conversion method for handling the 
 * 							error numbers.
 * 							add setResult(int)
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/19/2008	Change the expiration month and year to use 
 * 							Reg instead of Plate per guidance from Saber
 * 							modify getRegExpMonth(), getRegExpYear(),
 * 								setRegExpMonth(), setRegExpYear()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * Contains the Vehicle Info in response to a V21 Vehicle Info Request.
 * 
 * <p>This is the first version (" ").
 *
 * @version	3_Amigos_PH_A	02/19/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		01/14/2008 13:07:36
 */
public class WsVehicleInfoV21DataRes
{
	/**
	 * VehModlYr
	 */
	private int ciModelYear;

	/**
	 * PltBirthDate
	 */
	private int ciPlateCreatedDate;

	/**
	 * RegExpMo
	 */
	private int ciRegExpMonth;

	/**
	 * RegExpYr
	 */
	private int ciRegExpYear;

	/**
	 * RegClassCd
	 */
	private int ciRegClassCode;

	/**
	 * SpeclRegId
	 */
	private long clSpecialRegId;

	/**
	 * VehBdyType
	 */
	private String csBodyStyle;

	/**
	 * DocNo
	 */
	private String csDocumentNumber;

	/**
	 * VehMk
	 */
	private String csMake;

	/**
	 * VehModl
	 */
	private String csModel;

	/**
	 * OwnrTtlName1
	 */
	private String csNameOnTitleLine1;

	/**
	 * OwnrTtlName2
	 */
	private String csNameOnTitleLine2;

	/**
	 * RegPltNo
	 */
	private String csPlateNumber;

	/**
	 * Not defined at this time.
	 * <br>DisAssociatedCd.
	 */
	private String csPlateStatus;

	/**
	 * RegPltCd
	 */
	private String csPlateType;

	/**
	 * Result indicator.
	 */
	private String csResult;

	/**
	 * VIN
	 */
	private String csVIN;

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getBodyStyle()
	{
		return csBodyStyle;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getDocumentNumber()
	{
		return csDocumentNumber;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getMake()
	{
		return csMake;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getModel()
	{
		return csModel;
	}

	/**
	 * Return the Model Year of the Vehicle found.
	 * 
	 * @return int
	 */
	public int getModelYear()
	{
		return ciModelYear;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getNameOnTitleLine1()
	{
		return csNameOnTitleLine1;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getNameOnTitleLine2()
	{
		return csNameOnTitleLine2;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public int getPlateCreatedDate()
	{
		return ciPlateCreatedDate;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public int getRegExpMonth()
	{
		return ciRegExpMonth;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public int getRegExpYear()
	{
		return ciRegExpYear;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getPlateNumber()
	{
		return csPlateNumber;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getPlateStatus()
	{
		return csPlateStatus;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getPlateType()
	{
		return csPlateType;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public int getRegClassCode()
	{
		return ciRegClassCode;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getResult()
	{
		return csResult;
	}

	/**
	 * Method description
	 * 
	 * @return long
	 */
	public long getSpecialRegId()
	{
		return clSpecialRegId;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setBodyStyle(String string)
	{
		csBodyStyle = string;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setDocumentNumber(String string)
	{
		csDocumentNumber = string;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setMake(String string)
	{
		csMake = string;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setModel(String string)
	{
		csModel = string;
	}

	/**
	 * Method description
	 * 
	 * @param i
	 */
	public void setModelYear(int i)
	{
		ciModelYear = i;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setNameOnTitleLine1(String string)
	{
		csNameOnTitleLine1 = string;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setNameOnTitleLine2(String string)
	{
		csNameOnTitleLine2 = string;
	}

	/**
	 * Method description
	 * 
	 * @param int
	 */
	public void setPlateCreatedDate(int aiPlateCreatedDate)
	{
		ciPlateCreatedDate = aiPlateCreatedDate;
	}

	/**
	 * Method description
	 * 
	 * @param i
	 */
	public void setRegExpMonth(int i)
	{
		ciRegExpMonth = i;
	}

	/**
	 * Method description
	 * 
	 * @param i
	 */
	public void setRegExpYear(int i)
	{
		ciRegExpYear = i;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setPlateNumber(String string)
	{
		csPlateNumber = string;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setPlateStatus(String string)
	{
		csPlateStatus = string;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setPlateType(String string)
	{
		csPlateType = string;
	}

	/**
	 * Method description
	 * 
	 * @param i
	 */
	public void setRegClassCode(int i)
	{
		ciRegClassCode = i;
	}

	public void setResult(int aiErrorCode)
	{
		try
		{
			setResult(String.valueOf(aiErrorCode));
		}
		catch (NumberFormatException aeNFE)
		{
			setResult("");
			Log.write(Log.SQL_EXCP, this, "Setting result failed");
		}
	}

	/**
	 * Put the result string in the response.
	 * 
	 * @param asResult
	 */
	public void setResult(String asResult)
	{
		csResult = asResult;
	}

	/**
	 * Method description
	 * 
	 * @param alSpecialRegId
	 */
	public void setSpecialRegId(long alSpecialRegId)
	{
		clSpecialRegId = alSpecialRegId;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setVIN(String string)
	{
		csVIN = string;
	}
}
