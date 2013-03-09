package com.txdot.isd.rts.client.general.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.plaf.metal.MetalCheckBoxIcon;

/*
 * RedCheckBoxIcon.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Woodson	06/07/2011	Created
 * 							defect 10865 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

	/**
	 * 
	 * This draws a red checkmark
	 *  and sets the accompanying checkbox text 
	 *  to red if selected, black if deselected
	 * 
	 * To implement:
	 *  You must pass an graph of this class as an argument to the checkbox constructor
	 *  You must pass an graph of this class as an argument to the checkbox's addActionListener method
	 * @version	6.8.0			06/07/2011
	 * @author	BWOODS-C
	 * <br>Creation Date:		06/07/2011 09:38:30
	 */
	public class RedCheckBoxIcon
		extends MetalCheckBoxIcon
		implements ActionListener
	{

		/**
		 * draws a red checkmark
		 */
		protected void drawCheck(Component c, Graphics g, int x, int y)
		{
			int controlSize = getControlSize();
			g.setColor(Color.RED);
			g.fillRect(x + 3, y + 5, 2, controlSize - 8);
			g.drawLine(
				x + (controlSize - 4),
				y + 3,
				x + 5,
				y + (controlSize - 6));
			g.drawLine(
				x + (controlSize - 4),
				y + 4,
				x + 5,
				y + (controlSize - 5));
		}

		/**
		 * When the accompanying checkbox is selected this method sets its text to red
		 */
		public void actionPerformed(ActionEvent aaAE)
		{
			JCheckBox jCheckBox = (JCheckBox) aaAE.getSource();
			if (jCheckBox.isSelected())
			{
				jCheckBox.setForeground(Color.RED);
			}
			else
			{
				jCheckBox.setForeground(Color.BLACK);
			}

		}

	}
