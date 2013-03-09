package com.txdot.isd.rts.services.data;

import java.util.Hashtable;

/*
 * StickerFormatData.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		06/28/2006	Created Class so that we could switch 
 * 							between sticker version 1 and 2.
 * 							defect 8829 Ver 5.2.4
 * ---------------------------------------------------------------------
 */

/**
 * This data object holds the data pertaining to a certain field on a
 * sticker.  This field can be either a plate sticker or a windshield 
 * sticker.  The thought was to have a data object to store the 
 * information for each field on the sticker in a hash table that is 
 * keyed off of the field name + version.
 * 
 * Adding a version to the key allows us to store sticker information 
 * for multiple version and can switch between the versions at anytime.
 *
 * @version	5.2.4			06/28/2006
 * @author	Jeff Seifert
 * <br>Creation Date:		06/28/2006 11:07:00
 */
public class StickerFormatData
{

	private static Hashtable ahtStickerFormats = new Hashtable();
	private int ciPL_FONT_SIZE;
	private int ciPL_HORI;
	private int ciPL_VERT;
	private int ciWS_FONT_SIZE;
	private int ciWS_HORI;
	private int ciWS_VERT;
	private String csPL_FONT;
	private String csPL_JUST;
	private String csPL_STROKE;
	private String csWS_FONT;
	private String csWS_JUST;
	private String csWS_STROKE;

	/**
	 * StickerFormatData.java Constructor
	 * 
	 * @param asKey String
	 * @param aiWS_HORI int
	 * @param aiWS_VERT int
	 * @param aiWS_FONT_SIZE int
	 * @param asWS_FONT String
	 * @param asWS_STROKE String
	 * @param asWS_JUST String
	 * @param aiPL_HORI int
	 * @param aiPL_VERT int
	 * @param aiPL_FONT_SIZE int
	 * @param asPL_FONT String
	 * @param asPL_STROKE String
	 * @param asPL_JUST String
	 */
	public StickerFormatData(
		String asKey,
		int aiWS_HORI,
		int aiWS_VERT,
		int aiWS_FONT_SIZE,
		String asWS_FONT,
		String asWS_STROKE,
		String asWS_JUST,
		int aiPL_HORI,
		int aiPL_VERT,
		int aiPL_FONT_SIZE,
		String asPL_FONT,
		String asPL_STROKE,
		String asPL_JUST)
	{
		super();

		this.ciWS_HORI = aiWS_HORI;
		this.ciWS_VERT = aiWS_VERT;
		this.ciWS_FONT_SIZE = aiWS_FONT_SIZE;
		this.csWS_FONT = asWS_FONT;
		this.csWS_STROKE = asWS_STROKE;
		this.csWS_JUST = asWS_JUST;

		this.ciPL_HORI = aiPL_HORI;
		this.ciPL_VERT = aiPL_VERT;
		this.ciPL_FONT_SIZE = aiPL_FONT_SIZE;
		this.csPL_FONT = asPL_FONT;
		this.csPL_STROKE = asPL_STROKE;
		this.csPL_JUST = asPL_JUST;

		addLayout(this, asKey);
	}

	/**
	 * Adds the new format to the static hashtable that holds all of
	 * the format parameters.
	 * 
	 * @param aaFormat StickerFormatData
	 * @param asKey String
	 */
	public static void addLayout(
		StickerFormatData aaFormat,
		String asKey)
	{
		ahtStickerFormats.put(asKey, aaFormat);
	}

	/**
	 * Given the key this returns a format used to position the field 
	 * on the sticker form.
	 * 
	 * @param asKey String
	 * @return StickerFormatData
	 */
	public static StickerFormatData getLayout(String asKey)
	{
		return (StickerFormatData) ahtStickerFormats.get(asKey);
	}

	/**
	 * Gets the Plate Stickers font for this value..
	 * 
	 * @return String
	 */
	public String getPL_FONT()
	{
		return csPL_FONT;
	}

	/**
	 * Gets the Plate Stickers font size for this value..
	 * 
	 * @return String
	 */
	public int getPL_FONT_SIZE()
	{
		return ciPL_FONT_SIZE;
	}

	/**
	 * Gets the Plate Stickers horizontal location for this value.
	 * 
	 * @return String
	 */
	public int getPL_HORI()
	{
		return ciPL_HORI;
	}

	/**
	 * Gets the Plate Stickers justification for this value.
	 * 
	 * @return String
	 */
	public String getPL_JUST()
	{
		return csPL_JUST;
	}

	/**
	 * Gets the Plate Stickers stroke for this value.
	 * 
	 * @return String
	 */
	public String getPL_STROKE()
	{
		return csPL_STROKE;
	}

	/**
	 * Gets the Plate Stickers vertical location for this value.
	 * 
	 * @return String
	 */
	public int getPL_VERT()
	{
		return ciPL_VERT;
	}

	/**
	 * Gets the Windshield Stickers font for this value.
	 * 
	 * @return String
	 */
	public String getWS_FONT()
	{
		return csWS_FONT;
	}

	/**
	 * Gets the Windshield Stickers font size for this value.
	 * 
	 * @return String
	 */
	public int getWS_FONT_SIZE()
	{
		return ciWS_FONT_SIZE;
	}

	/**
	 * Gets the Windshield Stickers horizontal location for this value.
	 * 
	 * @return String
	 */
	public int getWS_HORI()
	{
		return ciWS_HORI;
	}

	/**
	 * Gets the Windshield Stickers justification for this value.
	 * 
	 * @return String
	 */
	public String getWS_JUST()
	{
		return csWS_JUST;
	}

	/**
	 * Gets the Windshield Stickers stroke for this value.
	 * 
	 * @return String
	 */
	public String getWS_STROKE()
	{
		return csWS_STROKE;
	}

	/**
	 * Gets the Windshield Stickers vertical location for this value.
	 * 
	 * @return String
	 */
	public int getWS_VERT()
	{
		return ciWS_VERT;
	}

	/**
	 * Sets the Plate Stickers font value.
	 * 
	 * @param asFont String
	 */
	public void setPL_FONT(String asFont)
	{
		csPL_FONT = asFont;
	}

	/**
	 * Sets the Plate Stickers font size value.
	 * 
	 * @param aiFontSize int
	 */
	public void setPL_FONT_SIZE(int aiFontSize)
	{
		ciPL_FONT_SIZE = aiFontSize;
	}

	/**
	 * Sets the Plate Stickers horizontal location value.
	 * 
	 * @param aiHorizLoc int
	 */
	public void setPL_HORI(int aiHorizLoc)
	{
		ciPL_HORI = aiHorizLoc;
	}

	/**
	 * Sets the Plate Stickers justification value.
	 * 
	 * @param asJust String
	 */
	public void setPL_JUST(String asJust)
	{
		csPL_JUST = asJust;
	}

	/**
	 * Sets the Plate Stickers stroke value.
	 * 
	 * @param asStroke String
	 */
	public void setPL_STROKE(String asStroke)
	{
		csPL_STROKE = asStroke;
	}

	/**
	 * Sets the Plate Stickers vertical location value.
	 * 
	 * @param aiVertLoc int
	 */
	public void setPL_VERT(int aiVertLoc)
	{
		ciPL_VERT = aiVertLoc;
	}

	/**
	 * Sets the Windshield Stickers font value.
	 * 
	 * @param asFont String
	 */
	public void setWS_FONT(String asFont)
	{
		csWS_FONT = asFont;
	}

	/**
	 * Sets the Windshield Stickers font size value.
	 * 
	 * @param aiFontSize int
	 */
	public void setWS_FONT_SIZE(int aiFontSize)
	{
		ciWS_FONT_SIZE = aiFontSize;
	}

	/**
	 * Sets the Windshield Stickers horizontal location value.
	 * 
	 * @param aiHorizLoc int
	 */
	public void setWS_HORI(int aiHorizLoc)
	{
		ciWS_HORI = aiHorizLoc;
	}

	/**
	 * Sets the Windshield Stickers justification value.
	 * 
	 * @param asJust String
	 */
	public void setWS_JUST(String asJust)
	{
		csWS_JUST = asJust;
	}

	/**
	 * Sets the Windshield Stickers stroke value.
	 * 
	 * @param asStroke String
	 */
	public void setWS_STROKE(String asStroke)
	{
		csWS_STROKE = asStroke;
	}

	/**
	 * Sets the Windshield Stickers vertical location value.
	 * 
	 * @param aiVertLoc int
	 */
	public void setWS_VERT(int aiVertLoc)
	{
		ciWS_VERT = aiVertLoc;
	}

}
