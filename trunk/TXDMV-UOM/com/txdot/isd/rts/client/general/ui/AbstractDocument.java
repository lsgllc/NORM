package com.txdot.isd.rts.client.general.ui;

/*
 *
 * AbstractDocument.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	08/18/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source
 * 							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 *
 */

/**
 * Superclass for all Input specific documents
 * 
 * <p>To create a new Input specific document for use in the 
 * RTSInputField, this class should be subclassed.
 * 
 * @version	5.2.3			08/18/2005
 * @author	Michael Abernethy
 * <br>Creation Date:		??
 */
public abstract class AbstractDocument
	extends javax.swing.text.PlainDocument
{
	protected int ciMaxLength;
	/**
	 * Default constructor for subclasses to call.
	 */
	public AbstractDocument()
	{
		super();
	}
}
