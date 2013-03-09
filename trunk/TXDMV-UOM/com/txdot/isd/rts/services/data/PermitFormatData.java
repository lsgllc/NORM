package com.txdot.isd.rts.services.data;

import java.util.Hashtable;

/*
 * PermitFormatData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2010	Created
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/02/2010	Update w/ method comments
 * 							defect 10491 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */

/**
 * Design based on StickerFormatData, this data object is used to assign
 * the various attributes associated with Permit Format Content, 
 * including the following for either Motorcycle or Regular:
 *  - Top/Bottom Border Line Border Line
 *  - 4 Bolt positions
 *  - Permit Type
 *  - Permit Messages (top/bottom of Permit)
 *  - Permit Number
 *  - Permit Effective Date/Time
 *  - Permit Expiration Date/Time 
 *  - Permit Vehicle Year/Make
 *  - Permit VIN
 *  - Permit Origin/Destination (if applicable) 
 *  - Permit OfcIssuanceNo 
 *
 * @version	6.5.0 			07/02/2010
 * @author	K Harrell
 * <br>Creation Date:		06/14/2010	10:15:59
 */
public class PermitFormatData
{
	private static Hashtable chtPermitFormats = new Hashtable();
	private int ciPRMT_FONT_SIZE;
	private int ciPRMT_HORI;
	private int ciPRMT_VERT;
	private String csPRMT_FONT;
	private String csPRMT_JUST;
	private String csPRMT_STROKE;

	/**
	 * PermitFormatData.java Constructor
	 */
	public PermitFormatData()
	{
		super();
	}

	/**
	 * PermitFormatData Constructor
	 * 
	 * @param asKey String
	 * @param aiPRMT_HORI int
	 * @param aiPRMT_VERT int
	 * @param aiPRMT_FONT_SIZE int
	 * @param asPRMT_FONT String
	 * @param asPRMT_STROKE String
	 * @param asPRMT_JUST String
	 */
	public PermitFormatData(
		String asKey,
		int aiPRMT_HORI,
		int aiPRMT_VERT,
		int aiPRMT_FONT_SIZE,
		String asPRMT_FONT,
		String asPRMT_STROKE,
		String asPRMT_JUST)
	{
		super();

		this.ciPRMT_HORI = aiPRMT_HORI;
		this.ciPRMT_VERT = aiPRMT_VERT;
		this.ciPRMT_FONT_SIZE = aiPRMT_FONT_SIZE;
		this.csPRMT_FONT = asPRMT_FONT;
		this.csPRMT_STROKE = asPRMT_STROKE;
		this.csPRMT_JUST = asPRMT_JUST;
		addLayout(this, asKey);
	}

	/**
	 * Adds the new format to the static hashtable that holds all of
	 * the format parameters.
	 * 
	 * @param aaFormat PermitFormatData
	 * @param asKey String
	 */
	public static void addLayout(
		PermitFormatData aaFormat,
		String asKey)
	{
		chtPermitFormats.put(asKey, aaFormat);
	}
	
	/**
	 * Given the key this returns a format used to position the field 
	 * on the Permit
	 * 
	 * @param asKey String
	 * @return PermitFormatData
	 */
	public static PermitFormatData getLayout(String asKey)
	{
		return (PermitFormatData) chtPermitFormats.get(asKey);
	}

	/**
	 * Return Hashtable w/ PermitFormats
	 * 
	 * @return Hashtable
	 */
	public static Hashtable getPermitFormats()
	{
		return chtPermitFormats;
	}

	/**
	 * Return Font size 
	 * 
	 * @return int 
	 */
	public int getPRMT_FONT_SIZE()
	{
		return ciPRMT_FONT_SIZE;
	}

	/**
	 * Return Horizontal Position 
	 * 
	 * @return
	 */
	public int getPRMT_HORI()
	{
		return ciPRMT_HORI;
	}

	/**
	 * Return Veritical Position 
	 * 
	 * @return
	 */
	public int getPRMT_VERT()
	{
		return ciPRMT_VERT;
	}

	/**
	 * Return Font
	 * 
	 * @return String
	 */
	public String getPRMT_FONT()
	{
		return csPRMT_FONT;
	}

	/**
	 * Return Justification
	 * 
	 * @return String 
	 */
	public String getPRMT_JUST()
	{
		return csPRMT_JUST;
	}

	/**
	 * Return Font Stroke
	 * 
	 * @return String 
	 */
	public String getPRMT_STROKE()
	{
		return csPRMT_STROKE;
	}

	/**
	 * Set Permit Formats
	 * 
	 * @param ahtPermitFormats
	 */
	public static void setPermitFormats(Hashtable ahtPermitFormats)
	{
		chtPermitFormats = ahtPermitFormats;
	}

	/**
	 * Set Format Size
	 * 
	 * @param aiPRMT_FONT_SIZE
	 */
	public void setPRMT_FONT_SIZE(int aiPRMT_FONT_SIZE)
	{
		ciPRMT_FONT_SIZE = aiPRMT_FONT_SIZE;
	}

	/**
	 * Set Horizontal Positioning
	 * 
	 * @param aiPRMT_HORI
	 */
	public void setPRMT_HORI(int aiPRMT)
	{
		ciPRMT_HORI = aiPRMT;
	}

	/**
	 * Set Vertical Positioning
	 * 
	 * @param aiPRMT_VERT
	 */
	public void setPRMT_VERT(int aiPRMT)
	{
		ciPRMT_VERT = aiPRMT;
	}

	/**
	 * Set Font
	 * 
	 * @param asPRMT_FONT
	 */
	public void setPRMT_FONT(String asPRMT_FONT)
	{
		csPRMT_FONT = asPRMT_FONT;
	}

	/**
	 * Set Justification
	 * 
	 * @param asPRMT_JUST
	 */
	public void setPRMT_JUST(String asPRMT)
	{
		csPRMT_JUST = asPRMT;
	}

	/**
	 * Set Stroke
	 * 
	 * @param asPRMT_STROKE
	 */
	public void setPRMT_STROKE(String asPRMT)
	{
		csPRMT_STROKE = asPRMT;
	}
}
