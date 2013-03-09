package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/*
 * TMRPR003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	05/12/2003	Present WsId on RPR003 vs. CashWsId
 *                          modify getValueAt()
 * 							defect 6118 
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3 
 * S Johnston	06/29/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify  
 *							defect 7896 Ver 5.2.3 
 * K Harrell	08/25/2009	modify getValueAt()
 * 							defect 8628 Ver Defect_POS_F   
 * ---------------------------------------------------------------------
 */
/** 
 * @version	Defect_POS_F	08/25/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		06/22/2001 13:47:40 
 */
public class TMRPR003 extends AbstractTableModel
{
	private Vector cvVector;

	/**
	 * TMRPR003 constructor
	 */
	public TMRPR003()
	{
		super();
		cvVector = new Vector();
	}

	/**
	 * Add a vector to the table to post rows.
	 * 
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		cvVector = new Vector(avVector);
		fireTableDataChanged();
	}

	/**
	 * Specify the number of columns in table.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return 1;
	}

	/**
	 * Specify the names of each column in the table.
	 * 
	 * @param aiColumn int
	 * @return String
	 */
	public String getColumnName(int aiColumn)
	{
		return "";
	}

	/**
	 * Return the number of rows in the table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}

	/**
	 * Return values from the table
	 * 
	 * @param aiColumn int
	 * @param aiRow int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			return (String) cvVector.elementAt(aiRow);
		}
		return "";
	}
}