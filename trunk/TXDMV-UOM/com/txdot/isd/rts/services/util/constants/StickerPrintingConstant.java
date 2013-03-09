package com.txdot.isd.rts.services.util.constants;

/*
 * 
 * StickerPrintingConstant.java
 * 
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		05/12/2004	Sticker Layout changed.
 *							Added constants to handle building the PCL
 *							on the fly.
 *							defect 7079 Ver. 5.2.0
 * --------------------------------------------------------------------- 
 */
/**
 * Utility Method to determine if sticker is printable.
 * 
 * @version	5.2.0		05/12/2004
 * @author	Jeff Seifert
 * <br>Creation date:	05/07/2004 14:13:00  
 */ 

public class StickerPrintingConstant
{
	// Font
	public static final String ARIAL = "ARIAL";
	public static final String COURIER ="COURIER";
	public static final String TIMES_NEW_ROMAN = "TIMES NEW ROMAN";
	public static final String UNIVERS = "UNIVERS";

	// Stroke
	public static final String BOLD = "BOLD";
	public static final String MEDIUM = "MEDIUM";
	public static final String EXTRA_BOLD = "EXTRA BOLD";
	public static final String BLACK = "BLACK";
	public static final String ULTRA_BLACK = "ULTRA BLACK";

	// Justification
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public static final String CENTER = "CENTER";
}
