package com.txdot.isd.rts.client.general.ui;

/* 
 * RTSTextArea.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Custom TextArea that prohibits focus traversal
 * 
 * @version	5.2.3		04/27/2005
 * @author	Nancy Ting
 * <br>Creation Date:	10/16/2001 19:48:22
 */
public class RTSTextArea extends javax.swing.JTextArea
{
	/**
	 * RTSTextArea constructor comment.
	 */
	public RTSTextArea()
	{
		super();
	}
	/**
	 * RTSTextArea constructor comment.
	 * 
	 * @param aiRows int
	 * @param aiColumns int
	 */
	public RTSTextArea(int aiRows, int aiColumns)
	{
		super(aiRows, aiColumns);
	}
	/**
	 * RTSTextArea constructor comment.
	 * 
	 * @param asText java.lang.String
	 */
	public RTSTextArea(String asText)
	{
		super(asText);
	}
	/**
	 * RTSTextArea constructor comment.
	 * 
	 * @param asText java.lang.String
	 * @param aiRows int
	 * @param aiColumns int
	 */
	public RTSTextArea(String asText, int aiRows, int aiColumns)
	{
		super(asText, aiRows, aiColumns);
	}
	/**
	 * RTSTextArea constructor comment.
	 * 
	 * @param aaDoc javax.swing.text.Document
	 */
	public RTSTextArea(javax.swing.text.Document aaDoc)
	{
		super(aaDoc);
	}
	/**
	 * RTSTextArea constructor comment.
	 * 
	 * @param aaDoc javax.swing.text.Document
	 * @param asText java.lang.String
	 * @param aiRows int
	 * @param aiColumns int
	 */
	public RTSTextArea(
		javax.swing.text.Document aaDoc,
		String asText,
		int aiRows,
		int aiColumns)
	{
		super(aaDoc, asText, aiRows, aiColumns);
	}
	/**
	 * Returns the value of a flag that indicates whether
	 * this component can be traversed using
	 * Tab or Shift-Tab keyboard focus traversal.  If this method
	 * returns "false", this component may still request the keyboard
	 * focus using <code>requestFocus()</code>, but it will not 
	 * automatically be assigned focus during tab traversal.
	 * 
	 * @return    boolean <code>true</code> if this component is
	 *            focus-traverable; <code>false</code> otherwise.
	 * @since     JDK1.1
	 */
	public boolean isFocusTraversable()
	{
		return false;
	}
}
