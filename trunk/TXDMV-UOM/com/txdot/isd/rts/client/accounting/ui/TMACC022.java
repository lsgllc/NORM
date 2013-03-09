package com.txdot.isd.rts.client.accounting.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsPaymentData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMACC022.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * K Harrell	07/26/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3 
 * B Hargrove	09/30/2005	Re-do column name handling using array.
 * 							defect 7884 Ver 5.2.3	  
 * --------------------------------------------------------------------- 
 */
/**
 * The table model for the table in the ACC022 screen.  It stores the 
 * table data and knows how to display it.
 * 
 * @version	5.2.3		09/30/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:	08/02/2001 10:17:16
 */
public class TMACC022 extends javax.swing.table.AbstractTableModel
{
	// Vector 
	private Vector cvTableData;

	private final static String[] carrColumn_Name = 
		{"Funds Report Date", "Reporting Date", "Amount Paid",
		 "Funds Category"};
	
	/**
	 * Creates a TMACC022.
	 */
	public TMACC022()
	{
		super();
		cvTableData = new Vector();
	}
	/**
	 * Updates the table data with the new data in the vector
	 * 
	 * @param avTableData	Vector 
	 */
	public void add(Vector avTableData)
	{
		cvTableData = avTableData;
		fireTableDataChanged();
	}
	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 4;
		return carrColumn_Name.length;
	}
	/**
	 * Returns the column name.
	 * 
	 * @param  aiCol int
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
	 * Returns the number of rows.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTableData.size();
	}
	/**
	 * Returns the value in the table.
	 * 
	 * @param  aiRow int
	 * @param  aiCol int
	 * @return Object 
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		if (aiCol == 0)
		{
			return ((FundsPaymentData) cvTableData.get(aiRow))
				.getFundsReportDate();
		}
		else if (aiCol == 1)
		{
			return ((FundsPaymentData) cvTableData.get(aiRow))
				.getReportingDate();
		}
		else if (aiCol == 2)
		{
			return ((FundsPaymentData) cvTableData.get(aiRow))
				.getTotalPaymentAmount()
				+ CommonConstant.STR_SPACE_THREE;
		}
		else if (aiCol == 3)
		{
			return ((FundsPaymentData) cvTableData.get(aiRow))
				.getFundsCategory();
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
