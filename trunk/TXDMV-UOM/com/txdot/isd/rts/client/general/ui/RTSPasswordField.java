package com.txdot.isd.rts.client.general.ui;

import javax.swing.text.*;
import java.awt.event.*;

/* 
 * RTSPasswordField.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	08/19/2005	Java 1.4 code changes. Format,
 * 							Hungarian notation, etc. 
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Custom Password field:
 * <ul><li>ignores all text typed while the Alt button is pressed.  
 * <li>allows the user to specify the max number of characters allowed.
 * </ul>
 * 
 * @version	5.2.3		08/19/2005
 * @author	Nancy Ting
 * <br>Creation Date:	10/08/2001 10:55:11
 */
public class RTSPasswordField
	extends javax.swing.JPasswordField
	implements java.awt.event.KeyListener
{
	private int ciMaxLength;
	private AbstractDocument caDocument;
	
	/**
	 * RTSPasswordField constructor comment.
	 */
	public RTSPasswordField()
	{
		this(Integer.MAX_VALUE);
	}
	/**
	 * RTS Password field
	 * 
	 * @param aiMaxLength int
	 */
	public RTSPasswordField(int aiMaxLength)
	{
		ciMaxLength = aiMaxLength;
		createDefaultModel();
	}
	
	/**
	 * A method to create the RTSInputField's Document.
	 * <br>Should not be called explicitly
	 * 
	 * @return Document
	 */
	public Document createDefaultModel()
	{
		caDocument = new DocumentNoAlt(ciMaxLength);
		return caDocument;
	}
	
	/**
	 * Get Max length
	 * 
	 * @return int
	 */
	public int getMaxLength()
	{
		return ciMaxLength;
	}
	
	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		// empty code block
	}
	
	/**
	 * Invoked when a key has been released.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		// empty code block
	}
	
	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 * 
	 * @param KeyEvent aaKE
	 */
	public void keyTyped(KeyEvent aaKE)
	{
		// empty code block
	}
	
	/**
	 * Set Max length
	 * 
	 * @param aiNewMaxLength int
	 */
	public void setMaxLength(int aiNewMaxLength)
	{
		ciMaxLength = aiNewMaxLength;
		setDocument(createDefaultModel());
	}
}
