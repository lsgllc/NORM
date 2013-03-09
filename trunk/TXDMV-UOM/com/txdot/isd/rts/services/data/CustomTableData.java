package com.txdot.isd.rts.services.data;

import java.awt.Font;

import javax.swing.JLabel;

/*
 * BoldTableData.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		04/02/2007	Created Class.
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */

/**
 * Used to present data in an RTSTable row in a custom way.  You can 
 * adjust the font as well as the position.
 *
 * @version	Broadcast Message	04/02/2007
 * @author	Jeff Seifert
 * <br>Creation Date:			04/13/2006 09:47:00
 */
public class CustomTableData
{
	private Object caData;
	private Font caFont;
	// Defaults for Vertical and Horizontal
	private int ciHorizontalAlignment = JLabel.LEFT;
	private int ciVerticalAlignment = JLabel.CENTER;

	/**
	 * CustomTableData.java Constructor
	 * 
	 * @param aaData Object
	 * @param aaFont Font
	 */
	public CustomTableData(Object aaData, Font aaFont)
	{
		super();
		caData = aaData;
		caFont = aaFont;
	}

	/**
	 * Sets the data to be placed in the table.
	 * 
	 * @return Object
	 */
	public Object getData()
	{
		return caData;
	}

	/**
	 * Get the Font object used to set the font for the RTSTable Row.
	 * 
	 * @return Font
	 */
	public Font getFont()
	{
		return caFont;
	}

	/**
	 * Gets the Horizontal Alignment of the Image.
	 * LEFT
	 * CENTER
	 * RIGHT
	 * 
	 * @return int
	 */
	public int getHorizontalAlignment()
	{
		return ciHorizontalAlignment;
	}

	/**
	 * Gets the Horizontal Alignment of the Image.
	 * Use JLabel:
	 * LEFT
	 * CENTER
	 * RIGHT
	 * 
	 * @param aiHorizAli int
	 */
	public int getVerticalAlignment()
	{
		return ciVerticalAlignment;
	}

	/**
	 * Used to get the Data to be placed in the table.
	 * 
	 * @param object
	 */
	public void setData(Object aaObject)
	{
		caData = aaObject;
	}

	/**
	 * Set the font to be used when displaying the data in the RTSTable.
	 * 
	 * @param aaFont Font
	 */
	public void setFont(Font aaFont)
	{
		caFont = aaFont;
	}

	/**
	 * Gets the Horizontal Alignment of the Image.
	 * Use JLabel:
	 * LEFT
	 * CENTER
	 * RIGHT
	 * 
	 * @param aiHorizAli int
	 */
	public void setHorizontalAlignment(int aiHorizAli)
	{
		ciHorizontalAlignment = aiHorizAli;
	}

	/**
	 * Sets the Vertical Alignment of the Image.
	 * Use JLabel:
	 * LEFT
	 * CENTER
	 * RIGHT
	 * 
	 * @param aiHorizAli int
	 */
	public void setVerticalAlignment(int aiHorizAli)
	{
		ciVerticalAlignment = aiHorizAli;
	}
}