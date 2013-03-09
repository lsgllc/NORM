package com.txdot.isd.rts.server.webapps.order.specialplateinfo.data;

/*
 * SpecialPlateDesign.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/26/2007	Created class.
 * 							defect 9120 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * Object used to hold the plate design information for a given plate.
 * Image, Height, Width and starting points for the characters.
 * 
 * We also use this data class to hold information for the plate no
 * fill area.  This is used when we automaticlly fill in the plate image
 * covering the plate no.
 *
 * @version	Special Plates	03/26/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		03/26/2007 12:06:00
 */
public class SpecialPlateDesign
{
	private int pltCharHeight = 0;
	private int pltCharWidth = 0;
	private String pltDesign = "";
	private int pltFillHeight = 0;
	private int pltFillWidth = 0;
	private int pltFillX = 0;
	private int pltFillY = 0;
	private int pltHeight = 0;
	private int pltLeftSpace = 0;
	private int pltPLPMaxChar = 0;
	private int pltTopSpace = 0;
	private int pltWidth = 0;
	
	/**
	 * Gets the Plates Chraracter Height in pixels.
	 * 
	 * @return int
	 */
	public int getPltCharHeight()
	{
		return pltCharHeight;
	}

	/**
	 * Gets the Plates Chraracter Width in pixels.
	 * 
	 * @return int
	 */
	public int getPltCharWidth()
	{
		return pltCharWidth;
	}

	/**
	 * Gets the plate design name of this plate.
	 * 
	 * @return
	 */
	public String getPltDesign()
	{
		return pltDesign;
	}

	/**
	 * Gets the plate no fill height.
	 * 
	 * @return int
	 */
	public int getPltFillHeight()
	{
		return pltFillHeight;
	}

	/**
	 * Gets the plate no fill width.
	 * 
	 * @return int
	 */
	public int getPltFillWidth()
	{
		return pltFillWidth;
	}

	/**
	 * Gets the plate no fill starting point from the left.
	 * 
	 * @return int
	 */
	public int getPltFillX()
	{
		return pltFillX;
	}

	/**
	 * Gets the plate no fill starting point from the top.
	 * 
	 * @return int
	 */
	public int getPltFillY()
	{
		return pltFillY;
	}

	/**
	 * Gets the over all Plates Height in pixels.
	 * 
	 * @return int
	 */
	public int getPltHeight()
	{
		return pltHeight;
	}

	/**
	 * Gets the number of pixels from the left to start the characters.
	 * 
	 * @return int
	 */
	public int getPltLeftSpace()
	{
		return pltLeftSpace;
	}

	/**
	 * Gets the maximum number of characters on the plate.
	 * 
	 * @return int
	 */
	public int getPltPLPMaxChar()
	{
		return pltPLPMaxChar;
	}

	/**
	 * Gets the number of pixels from the top to start the characters.
	 * 
	 * @return int
	 */
	public int getPltTopSpace()
	{
		return pltTopSpace;
	}

	/**
	 * Gets the Plates Width in pixels.
	 * 
	 * @return int
	 */
	public int getPltWidth()
	{
		return pltWidth;
	}

	/**
	 * Sets the Plates Characters Height in pixels.
	 * 
	 * @param aiPltCharHeight int
	 */
	public void setPltCharHeight(int aiPltCharHeight)
	{
		pltCharHeight = aiPltCharHeight;
	}

	/**
	 * Sets the Plates Characters Width in pixels.
	 * 
	 * @param aiPltCharWidth int
	 */
	public void setPltCharWidth(int aiPltCharWidth)
	{
		pltCharWidth = aiPltCharWidth;
	}

	/**
	 * Sets the plate design name of this plate.
	 * 
	 * @param asPltDesign String
	 */
	public void setPltDesign(String asPltDesign)
	{
		pltDesign = asPltDesign;
	}

	/**
	 * Sets the plate no fill height.
	 * 
	 * @param aiPltFillHeight int
	 */
	public void setPltFillHeight(int aiPltFillHeight)
	{
		pltFillHeight = aiPltFillHeight;
	}

	/**
	 * Sets the plate no fill width.
	 * 
	 * @param aiPltFillWidth int
	 */
	public void setPltFillWidth(int aiPltFillWidth)
	{
		pltFillWidth = aiPltFillWidth;
	}

	/**
	 * Sets the plate no fill starting point from the left.
	 * 
	 * @param aiPltFillX int
	 */
	public void setPltFillX(int aiPltFillX)
	{
		pltFillX = aiPltFillX;
	}

	/**
	 * Sets the plate no fill starting point from the top.
	 * 
	 * @param aiPltFillY int
	 */
	public void setPltFillY(int aiPltFillY)
	{
		pltFillY = aiPltFillY;
	}

	/**
	 * Sets the Plates overall Height in pixles.
	 * 
	 * @param i
	 */
	public void setPltHeight(int aiPltHeight)
	{
		pltHeight = aiPltHeight;
	}

	/**
	 * Sets the number of pixels from the left to start the characters.
	 * 
	 * @param aiPltLeftSpace int
	 */
	public void setPltLeftSpace(int aiPltLeftSpace)
	{
		pltLeftSpace = aiPltLeftSpace;
	}

	/**
	 * Sets the maximum number of characters on the plate.
	 * 
	 * @param aiPltPLPMaxChar int
	 */
	public void setPltPLPMaxChar(int aiPltPLPMaxChar)
	{
		pltPLPMaxChar = aiPltPLPMaxChar;
	}

	/**
	 * Sets the number of pixels from the top to start the characters.
	 * 
	 * @param aiPltTopSpace int
	 */
	public void setPltTopSpace(int aiPltTopSpace)
	{
		pltTopSpace = aiPltTopSpace;
	}

	/**
	 * Sets the Plates Width in pixels.
	 * 
	 * @param aiPltWidth int
	 */
	public void setPltWidth(int aiPltWidth)
	{
		pltWidth = aiPltWidth;
	}

}
