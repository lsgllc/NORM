package com.txdot.isd.rts.server.webapps.order.specialplateinfo.data;

import com.txdot.isd.rts.server.webapps.order.common.data.AbstractRequest;

/*
 * SpecialPlatesInfoData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/02/2007	Created class.
 * 							defect 9120 Ver Special Plates 
 * Bob B.		01/23/2008	Add pltimage to the request 
 * 							add pltImage
 * 							add getPltImage(), 
 * 								setPltImage()	
 * 							defect 9473 Ver Tres Amigos Prep 
 * ---------------------------------------------------------------------
 */

/**
 * Object used to handle the Special Plate Info Request Functions.
 *
 * @version	Tres Amigos Prep 01/23/2008
 * @author	Jeff Seifert
 * <br>Creation Date:		 03/02/2007 14:30:00
 */
public class SpecialPlatesInfoRequest extends AbstractRequest
{
	private int grpId = 0;
	// Used to query by plate design.  Mostly used for testing.
	private String pltDesign = "";
	private int pltId = 0;
	private boolean spanish = false;
	// defect 9473
	private String pltImage;
	// end defect 9473

	/**
	 * Gets the plate group id.
	 * 
	 * @return int
	 */
	public int getGrpId()
	{
		return grpId;
	}

	/**
	 * Gets the plate design.  Used to query all the plates for a given
	 * plate design.
	 * 
	 * @return String
	 */
	public String getPltDesign()
	{
		return pltDesign;
	}

	/**
	 * Gets the plate id.
	 * 
	 * @return int
	 */
	public int getPltId()
	{
		return pltId;
	}

	/**
	 * Returns if the response text will be in spanish or not.
	 * 
	 * @return boolean
	 */
	public boolean isSpanish()
	{
		return spanish;
	}

	/**
	 * Sets the plate group id.
	 * This value is required for ACTION_GET_GROUP_INFO.
	 * 
	 * @param aiGrpId int
	 */
	public void setGrpId(int aiGrpId)
	{
		grpId = aiGrpId;
	}

	/**
	 * Sets the plate design.  Used to query all the plates for a given
	 * plate design.
	 * 
	 * @param asPltDesign String
	 */
	public void setPltDesign(String asPltDesign)
	{
		pltDesign = asPltDesign;
	}

	/**
	 * Sets the plate id.
	 * 
	 * @param aiPltId int
	 */
	public void setPltId(int aiPltId)
	{
		pltId = aiPltId;
	}

	/**
	 * Sets if the response text will be in spanish or not.
	 * 
	 * @param abSpanish boolean
	 */
	public void setSpanish(boolean abSpanish)
	{
		spanish = abSpanish;
	}

	/**
	 * get the name of the plate image
	 * 
	 * @return String
	 */
	public String getPltImage()
	{
		return pltImage;
	}

	/**
	 * set the anme of the plate image
	 * 
	 * @param string
	 */
	public void setPltImage(String asPltImage)
	{
		pltImage = asPltImage;
	}

}
