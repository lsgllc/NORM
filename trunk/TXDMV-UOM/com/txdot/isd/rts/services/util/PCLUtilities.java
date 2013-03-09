package com.txdot.isd.rts.services.util;

import java.awt.image.PixelGrabber;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.swing.JPanel;

import com.txdot.isd.rts.services.util.constants.StickerPrintingConstant;
import com.txdot.isd.rts.services.util.pdf417.PDF417;

/*
 *
 * PCLUtilities.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/06/2004	5.2.0 Merge. New Class.
 * 							Ver 5.2.0	
 * Jeff S.		05/12/2004	Sticker Layout changed.  Moved this from
 *							com.txdot.isd.rts.services.util.pdf417
 *							Created PCLUtility methods to generate the 
 *							sticker layout PCL on the fly without using 
 *							hard codded PCL in the Print class.
 *							add genCenterJustPCL()
 *							add genHorizontalPCL()
 *							add genLeftJustPCL()
 *							add genPCL()
 *							add genRightJustPCL()
 *							add genVerticalPCL()
 *							defect 7079 Ver 5.2.0
 * Jeff S.		05/27/2004	Barcode format change on WS/US stickers 
 *							(output). Moved the processing of getting
 *							data prepared for the barcode to
 *							ReceiptTemplate Class. Created another
 *							dataToPCL class that accepts PDF417.  Moved
 *							all string utilities to UtilityMethods.
 *							depricate dataToBarcode()
 *							depricate padFields(String, int)
 *							deprecate dataToPCL(CompleteTransactionData)
 *							add dataToPCL(PDF417)
 *							defect 7107 Ver 5.2.0
 * J Rue/		07/29/2005	This method is primarly used for generating 
 * J Seifert				Layout PCL for reports and receipts.
 *							add genLayoutPCL()
 *							defect 8073, 8176 Ver 5.2.2 Fix 6
 * Jeff S.		07/24/2006	Added Left margin PCL to the PCL string for 
 * 							the sticker layout.  This was done b/c we
 * 							depend on the left margin being 0 when using
 * 							positioning PCL.
 * 							add ZERO_LEFT_MARGIN
 * 							modify genStickerLayoutPCL()
 * 							defect 8829 Ver. 5.2.4
 * K Harrell	12/15/2009	delete dataToBarcode(), padFields(),
 * 							 dataToPCL(CompleteTransactionData)
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	06/09/2010	... working on cleanup
 * 							defect 10491 Ver 6.5.0 
 * B Brown		05/19/2011	Remove an escape character that causes a
 * K Harrell				expiration month spacing issue for the 
 * D Hamilton				JetPcl conversion.
 * 							modify genRightJustPCL()
 * 							defect 10714 Ver 6.7.1
 * ---------------------------------------------------------------------
 */

/**
* This class provides methods for dealing with Printer PCL codes.
*  
* @version	6.5.0 			06/09/2010
* @author 	Michael Abernethy
* <br>Creation Date:		09/12/2002 
*/
public class PCLUtilities
{
	private static final String ESCAPE = "\u001b";
	private final static int BARCODE_SCALE = 5;

	// Added for use in generating Sticker Layout PCL on the fly
	public final static String POS_BEGIN = "&a";
	public final static String VERTICAL = "V";
	public final static String HORIZONTAL = "H";
	public final static String PARENTHESES = "(";
	public final static String PROPORTIONAL = "s1p";

	// Stroke
	public final static String MEDIUM = "s0b";
	public final static String BLACK = "s5b";
	public final static String ULTRA_BLACK = "s7b";
	public final static String BOLD = "s3b";
	public final static String EXTRA_BOLD = "s4b";

	public final static String FONT_SIZE = "v0";
	public final static String FONT = "T";

	// TypeFace
	public final static String ARIAL = "16602";
	public final static String COURIER = "4099";
	public final static String TIMES_NEW_ROMAN = "16901";
	public final static String UNIVERS = "4148";

	public final static String PATTERN_TRANSP_WHITE = "*vo1T";
	public final static String PATTERN_OPAQUE_BLACK = "*v1oT";
	public final static String PRINT_DIREC_180 = "&a180P";
	public final static String PRINT_DIREC_0 = "&a0P";
	public final static String SMALL_S = "s";
	// defect 8829
	public final static String ZERO_LEFT_MARGIN = ESCAPE + "&a0L";
	// end defect 8829

	public final static String PAGE_FEED = ESCAPE + "&l0H";

	// Possible Stroke Selections
	private static Hashtable HT_STROKE = new Hashtable();
	static {
		HT_STROKE.put(StickerPrintingConstant.BOLD, BOLD);
		HT_STROKE.put(StickerPrintingConstant.MEDIUM, MEDIUM);
		HT_STROKE.put(StickerPrintingConstant.EXTRA_BOLD, EXTRA_BOLD);
		HT_STROKE.put(StickerPrintingConstant.BLACK, BLACK);
		HT_STROKE.put(StickerPrintingConstant.ULTRA_BLACK, ULTRA_BLACK);
	}
	// Possible TypeFace Selections
	private static Hashtable HT_FONT = new Hashtable();
	static {
		HT_FONT.put(StickerPrintingConstant.ARIAL, ARIAL);
		HT_FONT.put(StickerPrintingConstant.COURIER, COURIER);
		HT_FONT.put(
			StickerPrintingConstant.TIMES_NEW_ROMAN,
			TIMES_NEW_ROMAN);
		HT_FONT.put(StickerPrintingConstant.UNIVERS, UNIVERS);
	}
	
	/**
	 * Insert the method's description here.
	 * 
	 * @return String
	 * @param pdf417 com.txdot.isd.rts.services.util.pdf417.PDF417
	 */
	public static String bmpToPCL(int[][] bmp)
	{
		StringBuffer pclString = new StringBuffer("");
		double height = bmp.length * BARCODE_SCALE;
		for (int k = bmp.length - 1; k >= 0; k--)
		{
			double width = bmp[k].length * BARCODE_SCALE;
			//set rectangle height
			pclString.append(ESCAPE + "*c" + (int) height + "V");
			height = height - BARCODE_SCALE;
			for (int i = bmp[k].length - 1; i >= 0; i--)
			{
				int color = bmp[k][i];
				if (color != -1)
				{
					pclString.append(ESCAPE + "*c" + (int) width + "H");
					pclString.append(ESCAPE + "*c0P");
				}
				else
				{
					pclString.append(ESCAPE + "*c" + (int) width + "H");
					pclString.append(ESCAPE + "*c1P");
				}
				int g = i;
				while (g >= 0 && bmp[k][g] == color)
				{
					width = width - BARCODE_SCALE;
					g--;
				}
				i = g + 1;
			}
		}
		//return the result
		String strPCL = pclString.toString();
		try
		{
			FileOutputStream f = new FileOutputStream("out.txt");
			PrintWriter out = new PrintWriter(f);
			out.println(strPCL);
			out.flush();
			out.close();
			f.close();
		}
		catch (Exception e)
		{
		}
		return pclString.toString();
	}
	
	/**
	 * Insert the method's description here.
	 * 
	 * @param aBarCodeData PDF417
	 */
	public static String dataToPCL(PDF417 aaBarCodeData)
	{
		return bmpToPCL(jpanelToBMP(aaBarCodeData));
	}
	
	/**
	 * Center Justification of Printed Text in PCL 5 
	 * without concern for character widths. 
	 *
	 * 1. Position the cursor to the center of the desired text position 
	 * 2. Set pattern transparency to transparent and pattern to solid white
	 * 3. Set print direction to 180 degrees
	 * 4. Set the point size to half that of the actual desired size
	 * 5. Print the Text
	 * 6. Set pattern transparency to opaque and pattern to solid black
	 * 7. Set print direction to 0 degrees
	 * 8. Set the point size to the actual desired size
	 * 9. Print the Text 
	 *
	 * @return String 
	 * @param aiFontSize int
	 * @param String asTextValue
	 */
	private static String genCenterJustPCL(
		int aiFontSize,
		String asTextValue)
	{
		//*vo1T&a180P(s24V VOID*v1oT&a0P(s48Vtext value
		String lsReturnValue =
			ESCAPE
				+ PATTERN_TRANSP_WHITE
				+ ESCAPE
				+ PRINT_DIREC_180
				+ ESCAPE
				+ PARENTHESES
				+ SMALL_S
				+ aiFontSize / 2
				+ VERTICAL
				+ asTextValue
				+ ESCAPE
				+ PATTERN_OPAQUE_BLACK
				+ ESCAPE
				+ PRINT_DIREC_0
				+ ESCAPE
				+ PARENTHESES
				+ SMALL_S
				+ aiFontSize
				+ VERTICAL
				+ asTextValue;

		return lsReturnValue;
	}
	
	/**
	 * Generate Horizontal Positioning PCL
	 *
	 * @param aiHorizPos int
	 * @return String
	 */
	public static String genHorizontalPCL(int aiHorizPos)
	{
		String lsReturnValue =
			ESCAPE + POS_BEGIN + aiHorizPos + HORIZONTAL;
		return lsReturnValue;
	}
	
	/**
	 * Left Justification - Nothing special is done
	 *
	 * @param asTextValue
	 * @return String
	 */
	private static String genLeftJustPCL(String asTextValue)
	{
		String lsReturnValue = " " + asTextValue;
		return lsReturnValue;
	}
	
	/**
	 * Right Justification of Printed Text in PCL 5
	 * without concern for character widths.
	 *
	 * 1. Position the cursor to the right edge of the desired text position 
	 * 2. Set pattern transparency to transparent and pattern to solid white
	 * 3. Set print direction to 180 degrees
	 * 4. Print the Text
	 * 5. Set pattern transparency to opaque and pattern to solid black
	 * 6. Set print direction to 0 degrees
	 * 7. Print the Text 
	 *
	 * @param String asTextValue*
	 * @return String 
	 */
	private static String genRightJustPCL(String asTextValue)
	{
		//*vo1T&a180P 09876540*v1oT&a0P Text AbstractValue
		String lsReturnValue =
			ESCAPE
				+ PATTERN_TRANSP_WHITE
				+ ESCAPE
				+ PRINT_DIREC_180
				+ " "
				+ asTextValue
				+ ESCAPE
				+ PATTERN_OPAQUE_BLACK
				+ ESCAPE
				+ PRINT_DIREC_0
// defect 10714 
// escape below key sequence made this invalid PCL for JetPcl to convert
// properly so the large month font is in the proper position on the
// sticker 				
//				+ ESCAPE
// end defect 10714
				+ " "
				+ asTextValue;

		return lsReturnValue;
	}
	
	/**
	 * This method is primarly used for generating the Sticker Layout PCL.
	 * If a Stroke is not passed then MEDIUM stroke is used.
	 * If a Font is not passed then ARIAL font is used.
	 * If a Justification is not passed then Left Justification is used
	 *
	 * @param aiHoriz int
	 * @param aiVerti int
	 * @param asText String
	 * @param asAlignment String
	 * @param asFont String
	 * @param asFontSize String
	 * @return String
	 */
	public static String genStickerLayoutPCL(
		int aiHoriz,
		int aiVert,
		String asTextValue,
		String asJustification,
		String asFont,
		int aiFontSize,
		String asStroke)
	{
		StringBuffer lsReturnPCL = new StringBuffer();

		String lsDefaultStroke = StickerPrintingConstant.MEDIUM;
		String lsPCLStroke;

		String lsDefaultFont = StickerPrintingConstant.ARIAL;
		String lsPCLFont;

		// Get stroke PCL from hash table
		// Use Default Stroke if one was not specified
		if (asStroke == null || asStroke.equals(""))
		{
			lsPCLStroke = lsDefaultStroke;
		}
		else
		{
			lsPCLStroke = (String) HT_STROKE.get(asStroke.toUpperCase());

			// If a stroke not in the list was specified use Default Stroke
			if (lsPCLStroke == null || lsPCLStroke.equals(""))
			{
				lsPCLStroke = lsDefaultStroke;
			}
		}

		// Get font PCL from hash table
		// Use default font if one was not specified
		if (asFont == null || asFont.equals(""))
		{
			lsPCLFont = lsDefaultFont;
		}
		else
		{
			lsPCLFont = (String) HT_FONT.get(asFont.toUpperCase());

			// If a font not in the list was specified use Default Font
			if (lsPCLFont == null || lsPCLFont.equals(""))
			{
				lsPCLFont = lsDefaultFont;
			}
		}

		// Build horizontal and vertical position
		lsReturnPCL.append(
			genHorizontalPCL(aiHoriz) + genVerticalPCL(aiVert));

		// defect 8829
		// Added because we depend on the left margin being set to zero
		// when using positioning PCL.
		lsReturnPCL.append(PCLUtilities.ZERO_LEFT_MARGIN);
		// end defect 8829

		// Build stroke, font, font size
		lsReturnPCL.append(
			ESCAPE
				+ PARENTHESES
				+ PROPORTIONAL
				+ aiFontSize
				+ FONT_SIZE
				+ lsPCLStroke
				+ lsPCLFont
				+ FONT);

		// Center Justification
		if (asJustification
			.equalsIgnoreCase(StickerPrintingConstant.CENTER))
		{
			lsReturnPCL.append(genCenterJustPCL(aiFontSize, asTextValue));
		}
		// Right Justification
		else if (
			asJustification.equalsIgnoreCase(
				StickerPrintingConstant.RIGHT))
		{
			lsReturnPCL.append(genRightJustPCL(asTextValue));
		}
		// Left Justification
		else
		{
			lsReturnPCL.append(genLeftJustPCL(asTextValue));
		}

		return lsReturnPCL.toString();
	}
	
	/**
	 * Generate Vertical Positioning PCL
	 *
	 * @param aiVertPos int
	 * @return String
	 */
	public static String genVerticalPCL(int aiVertPos)
	{
		String lsReturnValue = ESCAPE + POS_BEGIN + aiVertPos + VERTICAL;
		return lsReturnValue;
	}
	
	/**
	 * Insert the method's description here.\
	 * 
	 * @param aaJPanel JPanel
	 * @return int[][]
	 */
	public static int[][] jpanelToBMP(JPanel aaJPanel)
	{
		int height = aaJPanel.getHeight();
		int width = aaJPanel.getWidth();
		int pixels[][] = new int[height][width];
		int allpixels[] = new int[height * width];
		
		java.awt.image.BufferedImage image =
			new java.awt.image.BufferedImage(
				width,
				height,
				java.awt.image.BufferedImage.TYPE_INT_RGB);
				
		java.awt.Graphics imgGraphics = image.createGraphics();
		
		aaJPanel.paint(imgGraphics);
		
		PixelGrabber grabber =
			new PixelGrabber(
				image,
				0,
				0,
				width,
				height,
				allpixels,
				0,
				width);
		try
		{
			grabber.grabPixels();
		}
		catch (InterruptedException e)
		{
		}
		for (int i = 0; i < height; i++)
		{
			for (int k = 0; k < width; k++)
			{
				pixels[i][k] = allpixels[(i * width) + k];
			}
		}
		return pixels;
	}
	
	/**
	 * This method is primarly used for generating Layout PCL for reports
	 * and receipts.
	 * <p>If a Stroke is not passed then the stroke that was already sent to 
	 * 	the printer will be used.  A stroke will not be specified.
	 * <p>If a Font is not passed then the font that was already sent to 
	 * 	the printer will be used.  A font will not be specified.
	 * <p>If a Justification is not passed then Left Justification is used
	 *
	 * @param aiHoriz int
	 * @param aiVerti int
	 * @param asText java.lang.String
	 * @param asAlignment java.lang.String
	 * @param asFont java.lang.String
	 * @param asFontSize java.lang.String
	 * @return java.lang.String
	 */
	public static String genLayoutPCL(
		int aiHoriz,
		int aiVert,
		String asTextValue,
		String asJustification,
		String asFont,
		int asFontSize,
		String asStroke)
	{
		StringBuffer lsReturnPCL = new StringBuffer();

		String lsPCLStroke = "";
		//String lsPCLFont = "";

		// Get stroke PCL from hash table
		// Use "" if one was not specified
		if (asStroke != null && !asStroke.equals(""))
		{
			lsPCLStroke =
				(String) HT_STROKE.get(asStroke.toUpperCase());
		}

		// Get font PCL from hash table
		// Use "" if one was not specified
		if (asFont != null && !asFont.equals(""))
		{
			lsPCLStroke = (String) HT_FONT.get(asFont.toUpperCase());
		}

		// Build horizontal and vertical position
		lsReturnPCL.append(
			genHorizontalPCL(aiHoriz) + genVerticalPCL(aiVert));

		// Build stroke, font, font size
		if (asFontSize > 0
			|| lsPCLStroke.length() > 0
			|| lsPCLStroke.length() > 0)
		{
			lsReturnPCL.append(
				ESCAPE
					+ PARENTHESES
					+ PROPORTIONAL
					+ asFontSize
					+ FONT_SIZE
					+ lsPCLStroke
					+ lsPCLStroke
					+ FONT);
		}

		// Center Justification
		if (asJustification
			.equalsIgnoreCase(StickerPrintingConstant.CENTER))
		{
			lsReturnPCL.append(
				genCenterJustPCL(asFontSize, asTextValue));
		}
		// Right Justification
		else if (
			asJustification.equalsIgnoreCase(
				StickerPrintingConstant.RIGHT))
		{
			lsReturnPCL.append(genRightJustPCL(asTextValue));
		}
		// Left Justification
		else
		{
			lsReturnPCL.append(genLeftJustPCL(asTextValue));
		}

		return lsReturnPCL.toString();
	}
}
