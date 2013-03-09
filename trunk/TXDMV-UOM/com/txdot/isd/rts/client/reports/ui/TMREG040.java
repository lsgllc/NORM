package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;

/*
 *
 * TMREG040.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	05/12/2003	Defect 6118 Present WsId on REG040 vs.
 * 							CashWsId method getValueAt()
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify  
 *							defect 7896 Ver 5.2.3                
 * ---------------------------------------------------------------------
 */
/**
 * Cash Drawer Selection Screen FUN001
 * Table model for screen FUN001.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 * 
 * @version	5.2.3		06/30/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:	06/22/2001 1:47:40 PM 
 * @deprecated
 */
public class TMREG040 extends AbstractTableModel
{
	private Vector cvVector;

	/**
	 * CashDrawerTableModel constructor
	 */
	public TMREG040()
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
	 * Return the number of rows in the able
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}

	/**
	 * Return values from the table
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			AssignedWorkstationIdsData laWkIds =
				(AssignedWorkstationIdsData) cvVector.get(aiRow);
			// Defect 6118 - replace getCashWsId w/ getWsId
			return String.valueOf(laWkIds.getWsId());
			// End Defect 6118
		}
		return "";
	}
}
