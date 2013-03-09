package com.txdot.isd.rts.client.general.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import javax.swing.*;

/*
 * RTSButtonGroup.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		12/20/2005	Created class to help manage how we handle
 * 							ButtonGroups.
 * 							Ver. 5.2.3
 * Jeff S.		12/30/2005	Added a new method.
 * 							add setFocusOnSelected()
 * 							Ver. 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * An RTSButtonGroup is a modified ButtonGroup which handles the 
 * following:
 * <ul>
 * <li>Handles the arrowing up and down through the list of buttons.
 * <li>This class also allows the buttons to be automaticlly selected
 * when the focus moves.  To use this feature you have to change the 
 * cbSelectingOnFocus field to true using the setter.  This value
 * is false by default.
 *
 * @version	5.2.3			12/30/2005
 * @author	JSEIFER
 * <br>Creation Date:		12/16/2005 01:17:00
 */
public class RTSButtonGroup
	extends ButtonGroup
	implements ActionListener, KeyListener
{
	/*
	 * Constants
	 */
	private static final int MOVEDOWN = +1;
	private static final int MOVEUP = -1;
	/*
	 * This value determines if we are to select the button when we
	 * are arrowing the focus through the group of buttons.
	 */
	private boolean cbSelectingOnFocus = false;
	/*
	 * HashTable of componets that have ActionListeners on them.  The
	 * Key will be the AbstractButton and the value is the Component
	 * that we will request focus at the time of the action.
	 */
	private Hashtable chtActionComponents = new Hashtable();
	/*
	 * Vector of all the buttons in the RTSButtonGroup
	 * Had to create a seperate vactor instead of using the Vector
	 * in ButtonGroup because JCheckBox buttons are not stored in the 
	 * vector in ButtonGroup.
	 */
	protected Vector cvButtons = new Vector();

	/**
	 * RTSButtonGroup.java Constructor
	 */
	public RTSButtonGroup()
	{
		super();
	}
	/**
	 * Handles action Events.  This is used to move focus to a field 
	 * when a button is either selected or unselected.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		Component laMoveFocusTo =
			(Component) chtActionComponents.get(aaAE.getSource());
		if (laMoveFocusTo != null
			&& laMoveFocusTo.isVisible()
			&& laMoveFocusTo.isEnabled())
		{
			laMoveFocusTo.requestFocus();
		}
	}
	/**
	 * This overriddes the add(AbstractButton) of ButtonGroup.  This is
	 * done so that we can add a KeyListener after the componet is
	 * added to the group.
	 * 
	 * If the AbstractButton passed is either a JRadioButton or a 
	 * JRadioButtonMenuItem the items will create a 
	 * multiple-exclusion scope for the list of buttons.  Anything else
	 * added will be treated a an item in the list and arrowing will be
	 * handled.
	 * 
	 * @param aaAB AbstractButton
	 */
	public void add(AbstractButton aaAB)
	{
		if (aaAB instanceof JRadioButton
			|| aaAB instanceof JRadioButtonMenuItem)
		{
			super.add(aaAB);
		}
		cvButtons.add(aaAB);
		aaAB.addKeyListener(this);
	}
	/**
	 * This overriddes the add(AbstractButton) of ButtonGroup.  This is
	 * done so that we can add a KeyListener after the componet is
	 * added to the group.
	 * 
	 * @param aaAB AbstractButton
	 * @param aaMoveFocusToOnSelect Component
	 */
	public void add(
		AbstractButton aaAB,
		Component aaMoveFocusToOnSelect)
	{
		this.add(aaAB);
		aaAB.addActionListener(this);
		chtActionComponents.put(aaAB, aaMoveFocusToOnSelect);
	}
	/**
	 * Get all of the elements that are visible and editable and return
	 * a Collection of these objects.
	 * 
	 * @return Collection
	 */
	public Collection getSelectableElements()
	{
		Vector laReturnVector = new Vector();
		for (int i = 0; i < cvButtons.size(); i++)
		{
			AbstractButton laButton = (AbstractButton) cvButtons.get(i);
			if (laButton != null
				&& laButton.isVisible()
				&& laButton.isEnabled())
			{
				laReturnVector.add(laButton);
			}
		}
		return Collections.list(laReturnVector.elements());
	}
	/**
	 * Gets the class level boolean that is used to determine if we
	 * need to select the button along with moving focus when arrowing
	 * through a group of buttons.
	 * 
	 * @return boolean
	 */
	public boolean isSelectingOnFocus()
	{
		return cbSelectingOnFocus;
	}
	/**
	 * This handles all of the KeyPressed events.  The main function of 
	 * this method is to handle the up, down, left, right arrowing 
	 * through a group of buttons.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		int liUpDown = 0;
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
		{
			liUpDown = MOVEUP;
		}
		else if (
			aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			liUpDown = MOVEDOWN;
		}
		else
		{
			// We don't care about anything but the up, down, left, 
			// right keys.
			return;
		}

		// Get all of the buttons in the group
		LinkedList laButtons = new LinkedList(getSelectableElements());
		if (laButtons == null)
		{
			return;
		}
		AbstractButton laNextButton = null;

		// Loop through and find the one that has focus and fiq. out 
		// where the focus and possible the selection goes next.
		for (int i = 0; i < laButtons.size(); i++)
		{
			if (((AbstractButton) laButtons.get(i)).hasFocus())
			{
				// If we are moving up the list of buttons and this is 
				// the first button in the group select the last one
				if (liUpDown == MOVEUP
					&& laButtons.getFirst() == laButtons.get(i))
				{
					laNextButton = (AbstractButton) laButtons.getLast();
				}
				// If we are moving down the list of buttons and this is 
				// the last button in the group select the first one
				else if (
					liUpDown == MOVEDOWN
						&& laButtons.getLast() == laButtons.get(i))
				{
					laNextButton =
						(AbstractButton) laButtons.getFirst();
				}
				// Else we are not is a unigue position so we can go to
				// the previous button or the next button.
				else
				{
					laNextButton =
						(AbstractButton) laButtons.get(i + liUpDown);
				}
			}
		}

		// If we found a button to take action on then do so.
		if (laNextButton != null)
		{
			laNextButton.requestFocus();
			if (isSelectingOnFocus())
			{
				laNextButton.setSelected(true);
			}
		}
	}
	/**
	 * This handles all of the keyReleased events. 
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		// Empty method
	}
	/**
	 * This handles all of the keyTyped events. 
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyTyped(KeyEvent aaKE)
	{
		// Empty method
	}
	/**
	 * This overriddes the remove(AbstractButton) of ButtonGroup.  This 
	 * is done so that we can remove all of the listeners and clear the
	 * button from the list of buttons to arrow through.
	 * 
	 * If the AbstractButton passed is either a JRadioButton, 
	 * JRadioButtonMenuItem the items will be removed from Java's 
	 * ButtonGroup.
	 * 
	 * @param aaAB AbstractButton
	 */
	public void remove(AbstractButton aaAB)
	{
		if (aaAB instanceof JRadioButton
			|| aaAB instanceof JRadioButtonMenuItem
			|| aaAB instanceof JToggleButton)
		{
			super.remove(aaAB);
		}
		aaAB.removeKeyListener(this);
		aaAB.removeActionListener(this);
		chtActionComponents.remove(aaAB);
		cvButtons.remove(aaAB);
	}
	/**
	 * Sets the focus to the button of the group that is selected.  If 
	 * there is not a button in the group that is selected then no 
	 * action will be taken.
	 */
	public void setFocusOnSelected()
	{
		for (int i = 0; i < cvButtons.size(); i++)
		{
			AbstractButton laButton = (AbstractButton) cvButtons.get(i);
			if (laButton != null
				&& laButton.isVisible()
				&& laButton.isEnabled()
				&& laButton.isSelected())
			{
				laButton.requestFocus();
				return;
			}
		}
	}
	/**
	 * Sets the class level boolean that is used to determine if we
	 * need to select the button along with moving focus when arrowing
	 * through a group of buttons.
	 * 
	 * @param aaSelectingOnFocus boolean
	 */
	public void setSelectingOnFocus(boolean aaSelectingOnFocus)
	{
		cbSelectingOnFocus = aaSelectingOnFocus;
	}
}