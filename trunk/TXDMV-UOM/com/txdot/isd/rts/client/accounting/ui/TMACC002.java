package com.txdot.isd.rts.client.accounting.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.ProductServiceData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMACC002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	07/25/2005	movement of services.cache.*Data to 
 * 							services.data 
 * 							defect 7899  Ver 5.2.3
 * B Hargrove	09/30/2005	Re-do column name handling using array.
 * 							defect 7884 Ver 5.2.3	  
 * --------------------------------------------------------------------- 
 */
/**
 * The table model for the table in the ACC002 screen.  It stores the 
 * table data and knows how to display it.
 * 
 * @version	5.2.3		09/30/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	12/18/2001 09:08:25  
 */
public class TMACC002 extends javax.swing.table.AbstractTableModel
{
	// Vector 
	private Vector cvTableData;

	private final static String[] carrColumn_Name = 
		{"Select Item:"};
	
	/**
	 * TMACC002 constructor comment.
	 */
	public TMACC002()
	{
		super();
		cvTableData = new Vector();
	}
	/**
	 * Updates the table data with the new data in the vector
	 * 
	 * @param avTableData 
	 */
	public void add(Vector avTableData)
	{
		cvTableData = avTableData;
		fireTableDataChanged();
	}
	/**
	 * getColumnCount method comment.
	 * 
	 * @return int 
	 */
	public int getColumnCount()
	{
		//return 1;
		return carrColumn_Name.length;
	}
	/**
	 * Return the Column Name 
	 * 
	 * @param  aiCol	int
	 * @return String 
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
	 * getRowCount method comment.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTableData.size();
	}
	/**
	 * getValueAt method comment.
	 * 
	 * @param  aiRow int
	 * @param  aiCol int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		if (aiCol == 0)
		{
			return CommonConstant.STR_SPACE_TWO
				+ ((ProductServiceData) cvTableData.get(aiRow))
					.getPrdctSrvcDesc();
		}
		else
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
	}
}
