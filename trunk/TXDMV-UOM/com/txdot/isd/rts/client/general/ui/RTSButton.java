package com.txdot.isd.rts.client.general.ui;

import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* 
 * RTSButton.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	08/18/2005	Work on RTS 5.2.3 code updates.
 * 							Remove changing of color on focus events.
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
 
/**
 * RTSButton mirrors the functionality of buttons in the current RTS 
 * system by throwing an ActionEvent when the button has focus
 * and "Enter" is pressed.
 * 
 * @version	5.2.3		08/18/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	08/09/2001 13:55:33
 */

public class RTSButton
	extends javax.swing.JButton
	implements KeyListener, FocusListener
{
	private boolean cbIsManagingFocus = false;
	
	/**
	 * Creates an RTSButton.
	 */
	public RTSButton()
	{
		super();
		init();
	}
	
	/**
	 * Creates an RTSButton.
	 * 
	 * @param asText String
	 */
	public RTSButton(String asText)
	{
		super(asText);
		init();
	}
	
	/**
	 * Creates an RTSButton.
	 * 
	 * @param asText String
	 * @param aaIcon javax.swing.Icon
	 */
	public RTSButton(String asText, javax.swing.Icon aaIcon)
	{
		super(asText, aaIcon);
		init();
	}
	
	/**
	 * Creates an RTSButton.
	 * 
	 * @param aaIcon javax.swing.Icon
	 */
	public RTSButton(javax.swing.Icon aaIcon)
	{
		super(aaIcon);
		init();
	}
	
	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param  aaFE FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		// defect 7885
		// do not change the color
		//setForeground(new java.awt.Color(102, 102, 153));
		// end defect 7885
		setText(getText());
	}
	
	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param  aaFE FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// defect 7885 
		// do not change the color
		//setForeground(java.awt.Color.black);
		// end defect 7885
		setText(getText());
	}
	
	/**
	 * Insert the method's description here.
	 *  
	 */
	private void init()
	{
		addKeyListener(this);
		addFocusListener(this);
	}
	
	/**
	 * Insert the method's description here.
	 * 
	 * @return boolean
	 */
	public boolean isManagingFocus()
	{
		return cbIsManagingFocus;
	}
	
	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	*/
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ENTER
			&& hasFocus()
			&& isEnabled())
		{
			fireActionPerformed(
				new ActionEvent(
					this,
					ActionEvent.ACTION_PERFORMED,
					getActionCommand()));
			aaKE.consume();
		}
	}
	
	/**
	* Invoked when a key has been released.
	 * 
	 * @param aaKE KeyEvent
	*/
	public void keyReleased(java.awt.event.KeyEvent aaKE)
	{
		// empty code block
	}
	
	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param aaKE KeyEvent
	*/
	public void keyTyped(java.awt.event.KeyEvent aaKE)
	{
		// empty code block
	}
	
	/**
	 * Returns the IsManagingFocus value.
	 * 
	 * @param abNewManagingFocus boolean
	 */
	public void setManagingFocus(boolean abNewManagingFocus)
	{
		cbIsManagingFocus = abNewManagingFocus;
	}
}
