package com.txdot.isd.rts.services.data;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * ImageData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add new class variables cDisk, cDiskError,
 * 							cManual, and cPrint
 * 							Ver 5.2.0	
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							add getImageIcon(), getText()
 * 							defect 7890 Ver 5.2.3 
 * K Harrell	05/19/2005	Java 1.4 Work
 * 							rename static variables with static final
 * 							deleted associated get methods
 * 							deprecate ImageData(ImageIcon) and 
 * 							ImageData(ImageIcon,String)  
 * 							defect 7899 Ver 5.2.3 
 * Jeff S.		04/02/2007	Added ability to specify the vertical
 * 							and horizontal Alignment.
 * 							add ciHorizontalAlignment, 
 * 								ciVerticalAlignment,
 * 								getHorizontalAlignment(), 
 * 								setHorizontalAlignment(), 
 * 								getVerticalAlignment(),
 * 								setVerticalAlignment()
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */
/**
 * This class contains methods to deal with image handling.  
 * 
 * @version	Broadcast Message	04/02/2007 
 * @author 	Charlie Walker
 * <br>Creation Date:			10/16/2001 
 * 
 */
public class ImageData
{
	public static final String CHECK = "tickInventory";
	public static final String DELETE = "crossInventory";
	public static final String DISK = "disk";
	public static final String DISK_ERROR = "diskerr";
	public static final String FAILED = "questionInventory";
	public static final String MANUAL = "hand";
	public static final String PRINT = "print";

	private ImageIcon caImageIcon;
	private String csText;
	// Defaults for Vertical and Horizontal
	private int ciHorizontalAlignment = JLabel.LEFT;
	private int ciVerticalAlignment = JLabel.CENTER;
	/**
	 * ImageData constructor comment.
	 */
	public ImageData()
	{
		super();
	}
	/**
	 * Constructor to set the Image ICON AbstractValue.
	 * 
	 * @param aaImageIcon ImageIcon
	 * @deprecated 
	 */
	public ImageData(ImageIcon aaImageIcon)
	{
		caImageIcon = aaImageIcon;
	}
	/**
	 * Constructor to set Image ICON and Text Values.
	 * 
	 * @param aaImageIcon ImageIcon
	 * @param asText String
	 * @deprecated 
	 */
	public ImageData(ImageIcon aaImageIcon, String asText)
	{
		caImageIcon = aaImageIcon;
		csText = asText;
	}
	/**
	 * Set the Image ICON AbstractValue.
	 * 
	 * @param asImageIcon ImageIcon
	 */
	public ImageData(String asImageIcon)
	{
		this(asImageIcon, null);
	}
	/**
	 * Set the Image ICON AbstractValue and Alignment.
	 * 
	 * @param asImageIcon ImageIcon
	 * @param aiVert int
	 * @param aiHoriz int
	 */
	public ImageData(String asImageIcon, int aiVert, int aiHoriz)
	{
		this(asImageIcon, null);
		this.ciVerticalAlignment = aiVert;
		this.ciHorizontalAlignment = aiHoriz;
	}
	/**
	 * Constructor to set then set the ICON and text values.
	 * This one does a lookup for the ICON from the string value.
	 * 
	 * @param asImageIcon ImageIcon
	 * @param asText String
	 */
	public ImageData(String asImageIcon, String asText)
	{
		URL laURL =
			getClass().getResource(
				SystemProperty.getImagesDir()
					+ asImageIcon
					+ "."
					+ SystemProperty.getImageType());
		if (laURL != null)
		{
			ImageIcon laIcon = new ImageIcon(laURL);
			if (laIcon != null)
			{
				caImageIcon = laIcon;
			}
		}
		//	cImageIcon = aImageIcon;
		csText = asText;
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
	 * Get the ImageIcon value.
	 * 
	 * @return ImageIcon
	 */
	public ImageIcon getImageIcon()
	{
		return caImageIcon;
	}

	/**
	 * Get the Image Name.
	 * 
	 * @return String
	 */
	public String getText()
	{
		return csText;
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
