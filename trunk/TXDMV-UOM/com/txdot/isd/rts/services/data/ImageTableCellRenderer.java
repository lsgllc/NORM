package com.txdot.isd.rts.services.data;

import javax.swing.table.DefaultTableCellRenderer;

/*
 *
 * ImageTableCellRenderer.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							modify setValue()
 * 							defect 7890 Ver 5.2.3
 * K Harrell	04/22/2005	Organize imports. 
 * 							defect 7988 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Sets the Image AbstractValue.
 * 
 * @version	5.2.3		04/22/2005
 * @author	Charlie Walker
 * <br>Creation Date:	10/16/2001 18:49:33
 */
public class ImageTableCellRenderer extends DefaultTableCellRenderer
{
	/**
	 * ImageTableCellRenderer constructor comment.
	 */
	public ImageTableCellRenderer()
	{
		super();
	}
	/**
	 * Set the ICON AbstractValue.
	 * 
	 * @param aaObject Object
	 */
	public void setValue(Object aaObject)
	{
		ImageData laImageData = (ImageData) aaObject;
		if (laImageData.getImageIcon() != null)
		{
			setIcon(laImageData.getImageIcon());
			setText(laImageData.getText());
		}
		else
		{
			setText("");
		}
	}
}
