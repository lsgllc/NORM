package com.txdot.isd.rts.client.general.ui;

/* 
 * RTSTextPane.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	10/03/2001  New Class
 * B Hargrove	04/27/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * New Class that implements horizontal scrolling.
 * 
 * @version	5.2.3		04/27/2005
 * @author	Rakesh Duggirala
 * <br>Creation Date:	10/03/2001 13:49:53
 */
public class RTSTextPane extends javax.swing.JTextPane
{
	/**
	 * RTSTextPane constructor comment.
	 */
	public RTSTextPane()
	{
		super();
	}
	/**
	 * RTSTextPane constructor comment.
	 * 
	 * @param aaDoc javax.swing.text.StyledDocument
	 */
	public RTSTextPane(javax.swing.text.StyledDocument aaDoc)
	{
		super(aaDoc);
	}
	/**
	 * Get Scrollable Tracks Viewport Width
	 *  
	 * @return boolean
	 */
	public boolean getScrollableTracksViewportWidth()
	{
		return false;
	}
}
