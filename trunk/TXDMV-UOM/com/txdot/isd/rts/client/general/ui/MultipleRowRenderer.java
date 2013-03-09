package com.txdot.isd.rts.client.general.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.data.ImageData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * MultipleRowRenderer.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	05/9/2002	Fix CQU100003829, allow image rendering
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Ray Rowehl	02/23/2005	Move ImageCellRender
 * 							organize imports, format source,
 * 							rename fields
 * 							modify getTableCellRenderer()
 * 							defect 7705 Ver 5.2.3
 * B Hargrove	08/18/2005	RTS 5.2.3 Code Cleanup - replace strings 
 * 							with constants.
 * 							modify ButtonPanel()
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
 * Handles the painting for RTSTables where MULTIPLE_SELECTIONS are 
 * allowed.
 * 
 * @version	Broadcast Message	06/27/2007
 * @author	Michael Abernethy
 * <br>Creation Date:	?
 */
public class MultipleRowRenderer
	extends JLabel
	implements TableCellRenderer
{
	// defect 7768
	private final static Font DEFAULT_FONT =
		new java.awt.Font("Dialog", java.awt.Font.BOLD, 12);
	// end defect 7768
	private boolean cbSelected;
	private java.util.Set caSelectedRows;
	private int ciRow;
	private final static java.awt.Color HIGHLIGHT_BACKGROUND =
			new Color(204, 204, 255);
	private final static java.awt.Color HIGHLIGHT_FOREGROUND =
		new java.awt.Color(255, 102, 102);

	/**
	 * Creates a CustomRowRenderer.
	 */
	public MultipleRowRenderer()
	{
		super();
	}
	/**
	 *  This method is sent to the renderer by the drawing table to
	 *  configure the renderer appropriately before drawing.  Return
	 *  the Component used for drawing.
	 *
	 * @param	aaTable	JTable   that is asking the renderer to draw.
	 *				This parameter can be null.
	 * @param	aaValue	Object   the value of the cell to be rendered.  
	 *				It is up to the specific renderer to interpret
	 *				and draw the value.  eg. if value is the
	 *				String "true", it could be rendered as a
	 *				string or it could be rendered as a check
	 *				box that is checked.  null is a valid value.
	 * @param	abSelected boolean	true if the cell is to be renderer 
	 *				with selection highlighting
	 * @param 	abFocus boolean
	 * @param	aiRow int    the ciRow index of the cell being drawn.  
	 *				When drawing the header the rowIndex is -1.
	 * @param	aiColumn int the column index of the cell being drawn
	 */
	public java.awt.Component getTableCellRendererComponent(
		JTable aaTable,
		Object aaValue,
		boolean abSelected,
		boolean abFocus,
		int aiRow,
		int aiColumn)
	{
		this.cbSelected = abSelected;
		this.ciRow = aiRow;
		if (aaValue == null)
		{
			setText(CommonConstant.STR_SPACE_EMPTY);
			// defect 7768
			setIcon(null);
			// end defect 7768
		}
		else if (aaValue instanceof ImageData)
		{
			setIcon(((ImageData) aaValue).getImageIcon());
			setText(((ImageData) aaValue).getText());
			// defect 7768
			setHorizontalAlignment(
				((ImageData) aaValue).getHorizontalAlignment());
			setVerticalAlignment(
				((ImageData) aaValue).getVerticalAlignment());
			setFont(DEFAULT_FONT);
			// end defect 7768
		}
		else if (aaValue instanceof javax.swing.ImageIcon)
		{
			setIcon((javax.swing.ImageIcon) aaValue);
			setText(CommonConstant.STR_SPACE_EMPTY);
			// defect 7768
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
			// end defect 7768
		}
		// defect 7768
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
		// end defect 7768
		else
		{
			setText(aaValue.toString());
			setIcon(null);
		}

		caSelectedRows = ((RTSTable) aaTable).getSelectedRowNumbers();

		if (abSelected)
		{
			setForeground(HIGHLIGHT_FOREGROUND);
		}
		else
		{
			setForeground(Color.black);
		}

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
		if (caSelectedRows.contains(new Integer(ciRow)))
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
