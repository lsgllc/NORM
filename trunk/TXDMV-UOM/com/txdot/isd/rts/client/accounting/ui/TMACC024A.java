package com.txdot.isd.rts.client.accounting.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsDueData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/**
 *
 * TMACC024A.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy 	09/07/2001 	Added comments
 * K Harrell	07/26/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3 	
 * B Hargrove	09/30/2005	Re-do column name handling using array.
 * 							defect 7884 Ver 5.2.3	  
 * --------------------------------------------------------------------- 
 */
/**
 * The table model for the first table in the ACC024 screen.
 * It stores the table data and knows how to display it.
 *
 * @version	5.2.3		09/30/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:	08/02/2001 10:17:16
 */

public class TMACC024A extends javax.swing.table.AbstractTableModel
{
	// Vector 
	private Vector cvTableData;

	private final static String TXDOT = "TXDOT";
	private final static String[] carrColumn_Name = 
		{"Receiving Entity", "Reporting Date", "Funds Category",
		 "Due Date", "Amount Due", "Amount Paid"};

	/**
	 * Creates a TMACC024A.
	 */
	public TMACC024A()
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
		cvTableData =
			(
				Vector) com
					.txdot
					.isd
					.rts
					.services
					.util
					.UtilityMethods
					.copy(
				avTableData);
		insertSortB();
		insertSortA();
		fireTableDataChanged();
	}
	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 6;
		return carrColumn_Name.length;
	}
	/**
	 * Returns the column name.
	 *
	 * @param aiCol int 
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
	 * @param  aiRow	int
	 * @param  aiColumn	int
	 * @return Object 
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			return CommonConstant.STR_SPACE_TWO
				+ ((FundsDueData) cvTableData.get(aiRow))
					.getFundsReceivingEntity();
		}
		else if (aiColumn == 1)
		{
			return ((FundsDueData) cvTableData.get(aiRow))
				.getReportingDate();
		}
		else if (aiColumn == 2)
		{
			return ((FundsDueData) cvTableData.get(aiRow))
				.getFundsCategory();
		}
		else if (aiColumn == 3)
		{
			return ((FundsDueData) cvTableData.get(aiRow))
				.getFundsDueDate();
		}
		else if (aiColumn == 4)
		{
			return ((FundsDueData) cvTableData.get(aiRow))
				.getEntDueAmount()
				+ CommonConstant.STR_SPACE_TWO;
		}
		else if (aiColumn == 5)
		{
			if (((FundsDueData) cvTableData.get(aiRow))
				.getFundsReceivingEntity()
				.trim()
				.equals(TXDOT))
			{
				return ((FundsDueData) cvTableData.get(aiRow))
					.getFundsReceivedAmount()
					+ CommonConstant.STR_SPACE_TWO;
			}
			else
			{
				return CommonConstant.STR_SPACE_EMPTY;
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
	/**
	 * Insertion sort by Funds Category.
	 */
	private void insertSortA()
	{
		int k, aiCurrentRowNo;
		int aiMinRowNo = 0;
		int aiMaxRowNo = cvTableData.size() - 1;

		for (k = aiMinRowNo + 1; k <= aiMaxRowNo; k++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) cvTableData.get(k);
			aiCurrentRowNo = k;
			while (aiMinRowNo < aiCurrentRowNo
				&& laFundsDueData.getFundsCategory().compareTo(
					((FundsDueData) cvTableData
						.get(aiCurrentRowNo - 1))
						.getFundsCategory())
					< 0)
			{
				cvTableData.set(
					aiCurrentRowNo,
					cvTableData.get(aiCurrentRowNo - 1));
				aiCurrentRowNo--;
			}
			cvTableData.set(aiCurrentRowNo, laFundsDueData);
		}
	}
	/**
	 * Insertion sort by Reporting Date.
	 */
	private void insertSortB()
	{

		int k, aiCurrentRowNo;
		int aiMinRowNo = 0;
		int aiMaxRowNo = cvTableData.size() - 1;

		for (k = aiMinRowNo + 1; k <= aiMaxRowNo; k++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) cvTableData.get(k);
			aiCurrentRowNo = k;
			while (aiMinRowNo < aiCurrentRowNo
				&& laFundsDueData.getReportingDate().compareTo(
					((FundsDueData) cvTableData
						.get(aiCurrentRowNo - 1))
						.getReportingDate())
					< 0)
			{
				cvTableData.set(
					aiCurrentRowNo,
					cvTableData.get(aiCurrentRowNo - 1));
				aiCurrentRowNo--;
			}
			cvTableData.set(aiCurrentRowNo, laFundsDueData);
		}

	}
}
