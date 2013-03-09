package com.txdot.isd.rts.services.data;
import java.io.Serializable;
/*
 * FraudTypeData.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/06/2011	Created
 * 							defect 10865 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * Object to manage types of Fraud Indicators assigned 
 *
 * @version	6.8.0			06/06/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/06/2011	17:09:17
 */
public class FraudStateData implements Serializable, Comparable
{
	private boolean cbIdentification;
	private boolean cbLetterOfAuthorization;
	private boolean cbPowerOfAttorney;
	private boolean cbReleaseOfLien;

	public final static int IDENTIFICATION = 1;
	public final static int RELEASE_OF_LIEN = 2;
	public final static int POWER_OF_ATTORNEY = 3;
	public final static int LETTER_OF_AUTHORIZATION = 4;

	public final static String IDENTIFICATION_TXT = "Identification";
	public final static String RELEASE_OF_LIEN_TXT = "Release of Lien";
	public final static String POWER_OF_ATTORNEY_TXT =
		"Power of Attorney";
	public final static String LETTER_OF_AUTHORIZATION_TXT =
		"Letter of Authorization";

	public final static int FRAUD_UNCHANGED = 0;
	public final static int FRAUD_ADDED = 1;
	public final static int FRAUD_REMOVED = -1;
	public final static int FRAUD_MODIFIED = 2;

	static final long serialVersionUID = -3289910072135963078L;

	/**
	 * FraudTypeData.java Constructor
	 * 
	 * All initialized to false 
	 */
	public FraudStateData()
	{
		super();
	}

	/**
	 * FraudTypeData.java Constructor
	 * 
	 * @param abIdentification,
	 * @param abReleaseOfLien,
	 * @param abPowerOfAttorney,
	 * @param abLetterOfAuthorization)
	 */
	public FraudStateData(
		boolean abIdentification,
		boolean abReleaseOfLien,
		boolean abPowerOfAttorney,
		boolean abLetterOfAuthorization)
	{
		super();
		cbIdentification = abIdentification;
		cbReleaseOfLien = abReleaseOfLien;
		cbPowerOfAttorney = abPowerOfAttorney;
		cbLetterOfAuthorization = abLetterOfAuthorization;
	}

	/**
	 * Return Array of Indicators
	 * 
	 * @return Boolean[]
	 */
	public Boolean[] getIndicatorArray()
	{
		Boolean lbIndicator[] = new Boolean[5];
		lbIndicator[IDENTIFICATION] = new Boolean(cbIdentification);
		lbIndicator[RELEASE_OF_LIEN] = new Boolean(cbReleaseOfLien);
		lbIndicator[POWER_OF_ATTORNEY] = new Boolean(cbPowerOfAttorney);
		lbIndicator[LETTER_OF_AUTHORIZATION] =
			new Boolean(cbLetterOfAuthorization);
		return lbIndicator;
	}

	/**
	 * Returns:
	 * -1 if deleted 
	 * 	0 if unchanged
	 *  1 if added 
	 *  2 if added & deleted  ("modified")
	 *   
	 * @param   aaObject Object
	 * @return  int
	 */
	public int compareTo(Object aaObject)
	{
		FraudStateData laCompData = (FraudStateData) aaObject;
		Boolean[] larrIndi = getIndicatorArray();
		Boolean[] larrCompIndi = laCompData.getIndicatorArray();

		boolean lbAdd = false;
		boolean lbDelete = false;
		int liReturn = FRAUD_UNCHANGED;

		for (int i = 1; i < 5; i++)
		{
			if (!((Boolean) larrIndi[i])
				.equals((Boolean) larrCompIndi[i]))
			{
				if (((Boolean) larrCompIndi[i]).booleanValue())
				{
					lbAdd = true;
				}
				else
				{
					lbDelete = true;
				}
			}
		}
		if (lbAdd || lbDelete)
		{
			liReturn =
				(lbAdd && lbDelete)
					? FRAUD_MODIFIED
					: (lbAdd ? FRAUD_ADDED : FRAUD_REMOVED);
		}
		return liReturn;
	}

	/** 
	 * Return String to denote the parms for Fraud Type 
	 * 
	 * @return String
	 */
	public String getInClauseParms()
	{
		String lsIn = cbIdentification ? IDENTIFICATION + "," : "";
		lsIn = lsIn + (cbReleaseOfLien ? RELEASE_OF_LIEN + "," : "");
		lsIn =
			lsIn + (cbPowerOfAttorney ? POWER_OF_ATTORNEY + "," : "");
		lsIn =
			lsIn
				+ (cbLetterOfAuthorization
					? LETTER_OF_AUTHORIZATION + ","
					: "");
		lsIn = lsIn.substring(0, lsIn.length() - 1);
		return lsIn;
	}

	/** 
	 * Return String to denote the parms for Fraud Type 
	 * 
	 * @return String
	 */
	public String getFraudNames()
	{
		String lsIn = cbIdentification ? IDENTIFICATION_TXT + ", " : "";
		lsIn =
			lsIn + (cbReleaseOfLien ? RELEASE_OF_LIEN_TXT + ", " : "");
		lsIn =
			lsIn
				+ (cbPowerOfAttorney ? POWER_OF_ATTORNEY_TXT + ", " : "");
		lsIn =
			lsIn
				+ (cbLetterOfAuthorization
					? LETTER_OF_AUTHORIZATION_TXT + ", "
					: "");
		lsIn = lsIn.substring(0, lsIn.length() - 2);
		return lsIn.toUpperCase();
	}

	
	/**
	 * Get value of  cbIdentification
	 * 
	 * @return
	 */
	public boolean isIdentification()
	{
		return cbIdentification;
	}
	/**
	 * Get value of cbLetterOfAuthorization
	 * 
	 * @return
	 */
	public boolean isLetterOfAuthorization()
	{
		return cbLetterOfAuthorization;
	}
	/**
	 * Get value of cbPowerOfAttorney
	 * 
	 * @return
	 */
	public boolean isPowerOfAttorney()
	{
		return cbPowerOfAttorney;
	}
	/**
	 * Get value of cbReleaseOfLien
	 * 
	 * @return
	 */
	public boolean isReleaseOfLien()
	{
		return cbReleaseOfLien;
	}
	/**
	 * Return boolean to denote if any type true 
	 * 
	 * @return boolean
	 */
	public boolean isAnyFraud()
	{
		return cbIdentification
			|| cbLetterOfAuthorization
			|| cbPowerOfAttorney
			|| cbReleaseOfLien;
	}

	/**
	 * Return boolean to denote if all types true 
	 * 
	 * @return boolean
	 */
	public boolean isAllTypes()
	{
		return cbIdentification
			&& cbLetterOfAuthorization
			&& cbPowerOfAttorney
			&& cbReleaseOfLien;
	}
	/**
	 * Set value of cbIdentification
	 * 
	 * @param abIdentification
	 */
	public void setIdentification(boolean abIdentification)
	{
		cbIdentification = abIdentification;
	}
	/**
	 * Set value of cbLetterOfAuthorization
	 * 
	 * @param abLetterOfAuthorization
	 */
	public void setLetterOfAuthorization(boolean abLetterOfAuthorization)
	{
		cbLetterOfAuthorization = abLetterOfAuthorization;
	}
	/**
	 * Set value of cbPowerOfAttorney
	 * 
	 * @param abPowerOfAttorney
	 */
	public void setPowerOfAttorney(boolean abPowerOfAttorney)
	{
		cbPowerOfAttorney = abPowerOfAttorney;
	}
	/**
	 * Set value of cbReleaseOfLien
	 * 
	 * @param abReleaseOfLien
	 */
	public void setReleaseOfLien(boolean abReleaseOfLien)
	{
		cbReleaseOfLien = abReleaseOfLien;
	}

}
