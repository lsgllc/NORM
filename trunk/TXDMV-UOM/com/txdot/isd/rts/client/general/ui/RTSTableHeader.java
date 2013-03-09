package com.txdot.isd.rts.client.general.ui;

/* 
 * RTSTableHeader.java
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
 * RTS Table Header
 * 
 * @version	5.2.3		04/27/2005
 * @author	Charlie Walker
 * <br>Creation Date:	03/12/2002 20:03:28
 */
public class RTSTableHeader extends javax.swing.table.JTableHeader
{
	/**
	 * RTSTableHeader constructor comment.
	 */
	public RTSTableHeader()
	{
		super();
	}
	/**
	 * RTSTableHeader constructor comment.
	 * 
	 * @param aaColModel javax.swing.table.TableColumnModel
	 */
	public RTSTableHeader(javax.swing.table.TableColumnModel aaColModel)
	{
		super(aaColModel);
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
