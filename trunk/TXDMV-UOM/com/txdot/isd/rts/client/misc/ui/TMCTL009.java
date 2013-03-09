package com.txdot.isd.rts.client.misc.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.AssignedWorkstationIdsData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMCTL009.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K.Harrell    05/29/2002  Use WsId vs. CashWsId
 * 							defect 4143 
 * J Zwiener	03/01/2005	Java 1.4
 * 							defect 7892 Ver 5.2.3
 * K Harrell	09/28/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3    
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen CTL009.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	5.2.3 			09/28/2005
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class TMCTL009 extends javax.swing.table.AbstractTableModel
{
	private Vector cvVector;
	private final static String[] carrColumn_Name = 
		{"ID", "CP Name"};

	/**
	 * TMFUN001 constructor comment.
	 */
	public TMCTL009()
	{
		super();
		cvVector = new java.util.Vector();
	}
	/**
	 * Add a vector to the table to post rows.
	 * 
	 * @param avV Vector
	 */
	public void add(Vector avV)
	{
		cvVector = new Vector(avV);
		fireTableDataChanged();
	}
	/**
	 * Specify the number of columns in table.
	 * 
	 * @return int
	 */

	public int getColumnCount()
	{
		//return 2;
		return carrColumn_Name.length;
	}
	/**
	 * Specify the names of each column in the table.
	 * 
	 * @param aiCol int
	 */

	public String getColumnName(int aiCol)
	{
		if (aiCol >= 0 && aiCol < carrColumn_Name.length)
		{
			return carrColumn_Name[aiCol];
		}
		else
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
	}
	/**
	 * Return the number of rows in the able
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
	 * @param aiRow int
	 * @param aiCol int
	 * @return Object 
	 */

	public Object getValueAt(int aiRow, int aiCol)
	{
		if (aiCol == 0)
		{
			// defect 4143
			// Replace CashWsId w/ WsId
			// return new Integer ( ((AssignedWorkstationIdsData)
			// vector.get(row)).getCashWsId());
			// end defect 4143 

			return new Integer(
				((AssignedWorkstationIdsData) cvVector.get(aiRow))
					.getWsId());

		}
		else if (aiCol == 1)
		{
			return ((AssignedWorkstationIdsData) cvVector.get(aiRow))
				.getCPName();
		}

		return CommonConstant.STR_SPACE_EMPTY;

	}
}
