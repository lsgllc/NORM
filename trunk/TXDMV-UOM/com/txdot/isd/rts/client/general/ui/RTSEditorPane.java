package com.txdot.isd.rts.client.general.ui;

/* 
 * RTSEditorPane.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	08/19/2005	Java 1.4 code changes. Format,
 * 							Hungarian notation for variables, etc. 
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * RTS Editor Pane
 * 
 * @version	5.2.3		08/19/2005
 * @author	Nancy Ting
 * <br>Creation Date:	02/08/2002 11:15:06
 */
public class RTSEditorPane extends javax.swing.JEditorPane
{
	private boolean cbIsManagingFocus = false;
	/**
	 * RTSEditorPane constructor comment.
	 */
	public RTSEditorPane()
	{
		super();
	}
	/**
	 * RTSEditorPane constructor comment.
	 * 
	 * @param asURL String
	 * @throws java.io.IOException The exception description.
	 */
	public RTSEditorPane(String asURL) throws java.io.IOException
	{
		super(asURL);
	}
	/**
	 * RTSEditorPane constructor comment.
	 * 
	 * @param asType String
	 * @param asText String
	 */
	public RTSEditorPane(String asType, String asText)
	{
		super(asType, asText);
	}
	/**
	 * RTSEditorPane constructor comment.
	 * 
	 * @param aaInitialPage java.net.URL
	 * @throws java.io.IOException The exception description.
	 */
	public RTSEditorPane(java.net.URL aaInitialPage)
		throws java.io.IOException
	{
		super(aaInitialPage);
	}
	/**
	 * Returns the value of a flag that indicates whether
	 * this component can be traversed using
	 * Tab or Shift-Tab keyboard focus traversal.  If this method
	 * returns "false", this component may still request the keyboard
	 * focus using <code>requestFocus()</code>, but it will not 
	 * automatically be assigned focus during tab traversal.
	 * 
	 * @return boolean   <code>true</code> if this component is
	 *            focus-traverable; <code>false</code> otherwise.
	 * @since     JDK1.1
	 */
	public boolean isFocusTraversable()
	{
		return false;
	}
	/**
	 * Is Managing Focus
	 * 
	 * @return boolean
	 */
	public boolean isManagingFocus()
	{
		return cbIsManagingFocus;
	}
	/**
	 * Set Managing Focus
	 * 
	 * @param abNewManagingFocus boolean
	 */
	public void setManagingFocus(boolean abNewManagingFocus)
	{
		cbIsManagingFocus = abNewManagingFocus;
	}
}
