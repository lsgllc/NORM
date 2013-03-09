package com.txdot.isd.rts.client.desktop;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

/*
 * WorkaroundMenuItemUI.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		01/17/2006	Added class for workaround for bug 4911422
 * 							defect 8519 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * Created to address 
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4911422
 * until JRE 1.5.
 *
 * @version	5.2.3			01/17/2006
 * @author	Jeff S.
 * <br>Creation Date:		01/17/2006 13:13:00
 */
public class WorkaroundMenuItemUI extends BasicMenuItemUI
{
	public static final String MENUITEM_UI = "MenuItemUI";
	/**
	 * WorkaroundMenuItemUI.java Constructor
	 */
	private WorkaroundMenuItemUI()
	{
	}
	/**
	 * @param aaJComponent JComponent
	 */
	public static ComponentUI createUI(JComponent aaJComponent)
	{
		return new WorkaroundMenuItemUI();
	}
	/**
	 * @param aaJComponent JComponent
	 */
	protected MouseInputListener createMouseInputListener(JComponent aaJComponent)
	{
		return new ModifiedMouseInputHandler();
	}
	/**
	 * Created to address 
	 * http://developer.java.sun.com/developer/bugParade/bugs/4911422.html 
	 * until JRE 1.5.
	 *
	 * @version	5.2.3			01/17/2006
	 * @author	Jeff S.
	 * <br>Creation Date:		01/17/2006 13:13:00
	 */
	private class ModifiedMouseInputHandler extends MouseInputHandler
	{
		/**
		 * Handle Mouse Exit event
		 * 
		 * @param aaME MouseEvent
		 */
		public void mouseExited(MouseEvent aaME)
		{
			MenuSelectionManager laMenuSelectionManager =
				MenuSelectionManager.defaultManager();
			// 4188027: drag enter/exit added in JDK 1.1.7A, JDK1 .2 
			if ((aaME.getModifiers()
				& (InputEvent.BUTTON1_MASK
					| InputEvent.BUTTON2_MASK
					| InputEvent.BUTTON3_MASK))
				!= 0)
			{

				MenuSelectionManager
					.defaultManager()
					.processMouseEvent(
					aaME);
			}
			else
			{
				MenuElement laPath[] =
					laMenuSelectionManager.getSelectedPath();
				if (laPath.length > 1
					&& !(laPath[laPath.length - 1] instanceof JPopupMenu))
				{
					laMenuSelectionManager.setSelectedPath(
						removeLastPathElement(laPath));
				}
			}
		}
		/**
		 * Removes last path element.  This is the heart of the work
		 * around.
		 * 
		 * @param aaPath MenuElement[]
		 * @return MenuElement[]
		 */
		private MenuElement[] removeLastPathElement(MenuElement[] aaPath)
		{
			MenuElement laNewPath[] = new MenuElement[aaPath.length - 1];
			for (int i = 0; i < aaPath.length - 1; i++)
			{
				laNewPath[i] = aaPath[i];
			}
			return laNewPath;
		}
	}
}