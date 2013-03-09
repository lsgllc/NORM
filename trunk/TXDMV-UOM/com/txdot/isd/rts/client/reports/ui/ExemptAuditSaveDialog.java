package com.txdot.isd.rts.client.reports.ui;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.plaf.ComponentUI;

/*
 * ExemptAuditSaveDialog.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Ralph		10/18/2006	Jeff S. added boolean to constructor for 
 * 							jFileChooser lock down
 * 							defect 8900 Ver Exempts
 * ---------------------------------------------------------------------
 */

/**
 * Custom JFileChooser that disables all of the buttons that allow
 * the user to navigate through the system.  The intention is to 
 * instantiate this class passing the default directory and not allow 
 * the user to navigate to any other directory.
 * 
 * This custom JFileChooser removes the following buttons: 
 * Home Folder
 * New Folder
 * Up Folder
 * 
 * This custom JFileChooser disables the following combo boxes.
 * Save in
 * File Types
 * 
 *
 * @version	Exempts			10/18/2006
 * @author	Jeff Seifert
 * <br>Creation Date:		09/28/2006 15:41:00
 */

public class ExemptAuditSaveDialog extends JFileChooser
{
	private static final String HOME_FOLDER_ICON = "HomeFolder";
	private static final String NEW_FOLDER_ICON = "NewFolder";
	private static final String UP_FOLDER_ICON = "UpFolder";
	private boolean cbLockDown;

	/**
	 * Used to test the custom JFileChooser.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		ExemptAuditSaveDialog laExemptSaveDialog =
			new ExemptAuditSaveDialog(true);
		laExemptSaveDialog.showSaveDialog(null);
		System.exit(0);
	}

	/**
	 * ExemptAuditSaveDialog.java Constructor
	 * 
	 * @param b
	 */
	public ExemptAuditSaveDialog(boolean b)
	{
		this.cbLockDown = b;
		updateUI();
	}

	/**
	 * Recursive method that will dig through all of the containers
	 * and disable all of the combo boxes.
	 * 
	 * @param aaContainer Container
	 */
	private void disableComboBoxes(Container aaContainer)
	{
		for (int i = 0; i < aaContainer.getComponentCount(); i++)
		{
			Component laComp = aaContainer.getComponent(i);
			if (laComp instanceof JComboBox)
			{
				((JComboBox) laComp).setEnabled(false);
			}
			else if (laComp instanceof Container)
			{
				disableComboBoxes((Container) laComp);
			}
		}
	}

	/**
	 * Removes the button that has an icon that resembles the Icon name
	 * that is passed.
	 * 
	 * @param aaContainer Container
	 * @param asIconName String
	 */
	private void removeButton(Container aaContainer, String asIconName)
	{
		for (int i = 0; i < aaContainer.getComponentCount(); i++)
		{
			Component laComp = aaContainer.getComponent(i);
			if (laComp instanceof JButton)
			{
				JButton laButton = (JButton) laComp;
				if (laButton.getIcon() != null
					&& laButton.getIcon().toString().indexOf(asIconName)
						> -1)
				{
					aaContainer.remove(laButton);
					return;
				}
			}
			else if (laComp instanceof Container)
			{
				removeButton((Container) laComp, asIconName);
			}
		}
	}

	/**
	 * Overrides the setUI method from JFileChooser.
	 * 
	 * @param aaNewUI ComponentUI
	 */
	protected void setUI(ComponentUI aaNewUI)
	{
		super.setUI(aaNewUI);
		if (cbLockDown)
		{
			removeButton(this, UP_FOLDER_ICON);
			removeButton(this, HOME_FOLDER_ICON);
			removeButton(this, NEW_FOLDER_ICON);
			disableComboBoxes(this);
		}
	}
}
