package com.txdot.isd.rts.services.data;

/*
 * ScreenTTL007SavedData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/13/2010	Created
 * 							defect 10592 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * Object to save data from TTL007
 *
 * @version	6.6.0 		09/13/2010
 * @author	Kathy Harrell 
 * <br>Creation Date:	09/13/2010 16:42:00
 */
public class ScreenTTL007SavedData implements java.io.Serializable
{
	private OwnerData caOwnerData;
	private RegistrationData caRegisData;
	private TitleData caTitleData;
	private boolean cbModifiedRecordLien;
	private boolean cbRecordLien;
	static final long serialVersionUID = -7536012447411399592L;

	/**
	 * ScreenTTL007SavedData.java Constructor
	 */
	public ScreenTTL007SavedData()
	{
		super();
	}

	/**
	 * Return value of caOwnerData
	 * 
	 * @return OwnerData
	 */
	public OwnerData getOwnerData()
	{
		return caOwnerData;
	}

	/**
	 * Return value of caRegisData
	 * 
	 * @return RegistrationData
	 */
	public RegistrationData getRegisData()
	{
		return caRegisData;
	}

	/**
	 * Return value of caTitleData
	 * 
	 * @return TitleData
	 */
	public TitleData getTitleData()
	{
		return caTitleData;
	}

	/**
	 * Is Modified Record Lien
	 * 
	 * @return boolean 
	 */
	public boolean isModifiedRecordLien()
	{
		return cbModifiedRecordLien;
	}

	/**
	 * Is Record Lien
	 * 
	 * @return boolean 
	 */
	public boolean isRecordLien()
	{
		return cbRecordLien;
	}

	/**
	 * Set value of cbModifiedRecordLien
	 * 
	 * @param abModifiedRecordLien
	 */
	public void setModifiedRecordLien(boolean abModifiedRecordLien)
	{
		cbModifiedRecordLien = abModifiedRecordLien;
	}

	/**
	 * Set value of caOwnerData
	 * 
	 * @param aaOwnerData
	 */
	public void setOwnerData(OwnerData aaOwnerData)
	{
		caOwnerData = aaOwnerData;
	}

	/**
	 * Set value of cbRecordLien
	 * 
	 * @param abRecordLien
	 */
	public void setRecordLien(boolean abRecordLien)
	{
		cbRecordLien = abRecordLien;
	}

	/**
	 * Set value of caRegisData
	 * 
	 * @param aaRegisData
	 */
	public void setRegisData(RegistrationData aaRegisData)
	{
		caRegisData = aaRegisData;
	}

	/**
	 * Set value of caTitleData
	 * 
	 * @param aaTitleData
	 */
	public void setTitleData(TitleData aaTitleData)
	{
		caTitleData = aaTitleData;
	}

}
