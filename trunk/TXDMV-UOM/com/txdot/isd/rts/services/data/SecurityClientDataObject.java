package com.txdot.isd.rts.services.data;

/*
 * SecurityClientDataObject.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang 	04/16/2005	Move to services.data since this is a data 
 * 							object.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							defect 799  Ver 5.2.3
 * M Reyes		02/27/2007	Changes for Special Plates
 * 							add isSEC018(), setSEC018()
 * 							defect 9124 Ver Special Plates
 * K Harrell	04/20/2007	delete ciWorkstationType, get/set methods
 * 							defect 9085 Ver Special Plates 
 * --------------------------------------------------------------------- 
 */

/**
 * This the data object used by the all the frames in
 * Local Options. It contains the screen information and
 * SecurityData object as a field
 * 
 * @version	Special Plates		04/20/2007
 * @author	Ashish Mahajan
 * <br>Creation Date:			10/01/2001 18:47:51  
 */

public class SecurityClientDataObject
{

	// boolean
	private boolean cbSEC006 = false;
	private boolean cbSEC007 = false;
	private boolean cbSEC008 = false;
	private boolean cbSEC009 = false;
	private boolean cbSEC010 = false;
	private boolean cbSEC011 = false;
	private boolean cbSEC012 = false;
	private boolean cbSEC013 = false;
	private boolean cbSEC014 = false;
	private boolean cbSEC015 = false;
	private boolean cbSEC016 = false;
	// defect 9124
	private boolean cbSEC018 = false;
	// end defect 9124

	// defect 9085
	// Use SystemProperty.isHQ(), etc. instead 
//	// int 
//	private int ciWorkStationType = 0;
	// end defect 9085 

	// Object 
	private SecurityData caSecData = null;

	/**
	 * SecurityClientDataObject constructor comment.
	 */
	public SecurityClientDataObject()
	{
		super();
	}
	/**
	 * Get the SecurityData object
	 *  
	 * @return SecurityData
	 */
	public SecurityData getSecData()
	{
		return caSecData;
	}
//	/**
//	 * Get the WorkStation Type
//	 *  
//	 * @return int
//	 */
//	public int getWorkStationType()
//	{
//		return ciWorkStationType;
//	}
	/**
	 * Is the screen SEC006
	 *  
	 * @return boolean
	 */
	public boolean isSEC006()
	{
		return cbSEC006;
	}
	/**
	 * Is the screen SEC007
	 *  
	 * @return boolean
	 */
	public boolean isSEC007()
	{
		return cbSEC007;
	}
	/**
	 * Is the screen SEC007
	 *  
	 * @return boolean
	 */
	public boolean isSEC008()
	{
		return cbSEC008;
	}
	/**
	 * Is the screen SEC009
	 *  
	 * @return boolean
	 */
	public boolean isSEC009()
	{
		return cbSEC009;
	}
	/**
	 * Is the screen SEC010
	 *  
	 * @return boolean
	 */
	public boolean isSEC010()
	{
		return cbSEC010;
	}
	/**
	 * Is the screen SEC011
	 *  
	 * @return boolean
	 */
	public boolean isSEC011()
	{
		return cbSEC011;
	}
	/**
	 * Is the screen SEC012
	 *  
	 * @return boolean
	 */
	public boolean isSEC012()
	{
		return cbSEC012;
	}
	/**
	 * Is the screen SEC013
	 *  
	 * @return boolean
	 */
	public boolean isSEC013()
	{
		return cbSEC013;
	}
	/**
	 * Is the screen SEC014
	 *  
	 * @return boolean
	 */
	public boolean isSEC014()
	{
		return cbSEC014;
	}
	/**
	 * Is the screen SEC015
	 *  
	 * @return boolean
	 */
	public boolean isSEC015()
	{
		return cbSEC015;
	}
	/**
	 * Is the screen SEC016
	 *  
	 * @return boolean
	 */
	public boolean isSEC016()
	{
		return cbSEC016;
	}
	// defect 9124
	/**
	 * Is the screen SEC018
	 *  
	 * @return boolean
	 */
		public boolean isSEC018()
		{
			return cbSEC018;
		}
	// end defect 9124
	/**
	 * Set the screen to SEC006
	 *  
	 * @param abSEC006 boolean
	 */
	public void setSEC006(boolean abSEC006)
	{
		cbSEC006 = abSEC006;
	}
	/**
	 * Set the screen to SEC007
	 *  
	 * @param abSEC007 boolean
	 */
	public void setSEC007(boolean abSEC007)
	{
		cbSEC007 = abSEC007;
	}
	/**
	 * Set the screen to SEC008
	 *  
	 * @param abSEC008 boolean
	 */
	public void setSEC008(boolean abSEC008)
	{
		cbSEC008 = abSEC008;
	}
	/**
	 * Set the screen to SEC009
	 *  
	 * @param abSEC009 boolean
	 */
	public void setSEC009(boolean abSEC009)
	{
		cbSEC009 = abSEC009;
	}
	/**
	 * Set the screen to SEC010
	 *  
	 * @param abSEC010 boolean
	 */
	public void setSEC010(boolean abSEC010)
	{
		cbSEC010 = abSEC010;
	}
	/**
	 * Set the screen to SEC011
	 *  
	 * @param abSEC011 boolean
	 */
	public void setSEC011(boolean abSEC011)
	{
		cbSEC011 = abSEC011;
	}
	/**
	 * Set the screen to SEC012
	 *  
	 * @param abSEC012 boolean
	 */
	public void setSEC012(boolean abSEC012)
	{
		cbSEC012 = abSEC012;
	}
	/**
	 * Set the screen to SEC013
	 *  
	 * @param abSEC013 boolean
	 */
	public void setSEC013(boolean abSEC013)
	{
		cbSEC013 = abSEC013;
	}
	/**
	 * Set the screen to SEC014
	 *  
	 * @param abSEC014 boolean
	 */
	public void setSEC014(boolean abSEC014)
	{
		cbSEC014 = abSEC014;
	}
	/**
	 * Set the screen to SEC015
	 *  
	 * @param abSEC015 boolean
	 */
	public void setSEC015(boolean abSEC015)
	{
		cbSEC015 = abSEC015;
	}
	/**
	 * Set the screen to SEC016
	 *  
	 * @param abSEC016 boolean
	 */
	public void setSEC016(boolean abSEC016)
	{
		cbSEC016 = abSEC016;
	}
	// defect 9124
	/**
	 * Set the screen to SEC018
	 *  
	 * @param abSEC018 boolean
	 */
		public void setSEC018(boolean abSEC018)
		{
			cbSEC018 = abSEC018;
		}
	// end defect 9124
	/**
	 * Set the field for SecurityData
	 *  
	 * @param aaSecData SecurityData
	 */
	public void setSecData(SecurityData aaSecData)
	{
		caSecData = aaSecData;
	}
//	/**
//	 * Set the WorkStation Type
//	 *  
//	 * @param aiWorkStationType int
//	 */
//	public void setWorkStationType(int aiWorkStationType)
//	{
//		ciWorkStationType = aiWorkStationType;
//	}
}
