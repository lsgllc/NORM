package com.txdot.isd.rts.client.general.ui;

import javax.swing.*;
import javax.swing.table.*;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.data.ImageData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import java.awt.*;
/* 
 * SingleRowRenderer.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * B Hargrove	08/19/2005	Java 1.4 code changes. Format,
 * 							Hungarian notation for variables, etc. 
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		04/02/2007	Added to handle images and custom allignment
 * 							of images to the RTS Table.  Also allow the
 * 							ability to use custom fonts.
 * 							add DEFAULT_FONT
 * 							modify getTableCellRendererComponent()
 * 							defect 7768 Ver Broadcast Message
 * Jeff S.		06/27/2007	Removed the allignment settings for the else
 * 							that allowed everything to default.
 * 							modify getTableCellRendererComponent()
 * 							defect 7768 Ver Broadcast Message
 * ---------------------------------------------------------------------
 */
/**
 * Handles the painting for RTSTables where SINGLE_SELECTION is allowed
 * 
 * @version	Broadcast Message	06/27/2007
 * @author	Michael Abernethy
 * <br>Creation Date:			08/09/2001 13:55:33
 */
public class SingleRowRenderer
	extends JLabel
	implements TableCellRenderer
{
	private boolean cbIsSelected;
	private final static java.awt.Color HIGHLIGHT_BACKGROUND =
		new Color(204, 204, 255);
	// defect 7768
	private final static Font DEFAULT_FONT = 
		new java.awt.Font("Dialog", java.awt.Font.BOLD, 12);
	// end defect 7768

	/**
	 * Creates a CustomRowRenderer.
	 */
	public SingleRowRenderer()
	{
		super();
	}

	/**
	 *  This method is sent to the renderer by the drawing table to
	 *  configure the renderer appropriately before drawing.  Return
	 *  the Component used for drawing.
	 *
	 * @param aaTable JTable that is asking the renderer to draw.
	 *			This parameter can be null.
	 * @param aaValue Object	the value of the cell to be rendered. It  
	 *			is up to the specific renderer to interpret and draw the 
	 *			value.  eg. if value is the String "true", it could be 
	 *			rendered as a string or it could be rendered as a check
	 *			box that is checked.  null is a valid value.
	 * @param abIsSelected boolean	true if the cell is to be renderer 
	 *			with selection highlighting
	 * @param abHasFocus boolean
	 * @param aiRow	int   the row index of the cell being drawn.  When
	 *				drawing the header the rowIndex is -1.
	 * @param aiColumn int   the column index of the cell being drawn
	 */
	public java.awt.Component getTableCellRendererComponent(
		JTable aaTable,
		Object aaValue,
		boolean abIsSelected,
		boolean abHasFocus,
		int aiRow,
		int aiColumn)
	{
		// defect 7768
		// Added Font and Horizontal, Vertical settings.
		this.cbIsSelected = abIsSelected;
		if (aaValue == null)
		{
			setText(CommonConstant.STR_SPACE_EMPTY);
			setIcon(null);
		}
		else if (aaValue instanceof javax.swing.ImageIcon)
		{
			setIcon((javax.swing.ImageIcon) aaValue);
			setText(CommonConstant.STR_SPACE_EMPTY);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
		}
		else if (aaValue instanceof ImageData)
		{
			setIcon(((ImageData) aaValue).getImageIcon());
			setText(((ImageData) aaValue).getText());
			setHorizontalAlignment(
				((ImageData) aaValue).getHorizontalAlignment());
			setVerticalAlignment(
				((ImageData) aaValue).getVerticalAlignment());
			setFont(DEFAULT_FONT);
		}
		else if (aaValue instanceof CustomTableData)
		{
			CustomTableData laCustomeData = (CustomTableData) aaValue;
			setFont(laCustomeData.getFont());
			setText(laCustomeData.getData().toString());
			setIcon(null);
			setHorizontalAlignment(
				laCustomeData.getHorizontalAlignment());
			setVerticalAlignment(laCustomeData.getVerticalAlignment());
		}
		else
		{
			setText(aaValue.toString());
			setIcon(null);
		}
		// end defect 7768

		setForeground(Color.black);

		return this;

	}
	/**
	 * Hack code since there is a bug with JLabel painting their 
	 * background in JTables.
	 * 
	 * @param aaGraphics Graphics
	 */
	public void paint(Graphics aaGraphics)
	{
		Color laColor;
		if (cbIsSelected)
		{
			laColor = HIGHLIGHT_BACKGROUND;
		}
		else
		{
			laColor = Color.white;
		}

		aaGraphics.setColor(laColor);
		aaGraphics.fillRect(0, 0, getWidth(), getHeight());
		super.paint(aaGraphics);
	}
}
