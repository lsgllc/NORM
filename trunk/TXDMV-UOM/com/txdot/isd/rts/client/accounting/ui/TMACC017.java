package com.txdot.isd.rts.client.accounting.ui;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsDueData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMACC0017.java
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
 * The table model for the table in the ACC0017 screen.  It stores the 
 * table data and knows how to display it.
 * 
 * @version	5.2.3		09/30/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:	07/26/2001 14:43:04   
 */
public class TMACC017 extends javax.swing.table.AbstractTableModel
{
	// Vector 
	private Vector cvTableData;  

	private final static String[] carrColumn_Name = 
		{"Due Date", "Funds Report Date", "Reporting Date",
		 "Total Amount Due", "Remittance Amount"};

	/**
	 * Creates a TMACC017.
	 */
	public TMACC017()
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
		consolidate();
		insertSortC();
		insertSortB();
		insertSortA();
		fireTableDataChanged();
	}
	/**
	 * Consolidates records that share the same Funds Due Date, Funds 
	 * Report Date, and Reporting Date.
	 */
	private void consolidate()
	{
		Set laTempSet = new HashSet();
		while (cvTableData.size() > 0)
		{
			FundsDueData laFundsDueData1 =
				(FundsDueData) cvTableData.firstElement();
			int i = 1;
			while (i < cvTableData.size())
			{
				FundsDueData laFundsDueData2 =
					(FundsDueData) cvTableData.get(i);
				if (laFundsDueData1
					.getFundsDueDate()
					.equals(laFundsDueData2.getFundsDueDate())
					&& laFundsDueData1.getFundsReportDate().equals(
						laFundsDueData2.getFundsReportDate())
					&& laFundsDueData1.getReportingDate().equals(
						laFundsDueData2.getReportingDate()))
				{
					laFundsDueData1.setDueAmount(
						laFundsDueData1.getDueAmount().add(
							laFundsDueData2.getDueAmount()));
					laFundsDueData1.setRemitAmount(
						laFundsDueData1.getRemitAmount().add(
							laFundsDueData2.getRemitAmount()));
					cvTableData.remove(i);
					continue;
				}
				i++;
			}
			laTempSet.add(laFundsDueData1);
			cvTableData.remove(0);
		}
		cvTableData = new Vector(laTempSet);
	}
	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 5;
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
	 * @param  aiRow	int
	 * @param  aiColumn	int
	 * @return Object 
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			return ((FundsDueData) cvTableData.get(aiRow))
				.getFundsDueDate();
		}
		else if (aiColumn == 1)
		{
			return ((FundsDueData) cvTableData.get(aiRow))
				.getFundsReportDate();
		}
		else if (aiColumn == 2)
		{
			return ((FundsDueData) cvTableData.get(aiRow))
				.getReportingDate();
		}
		else if (aiColumn == 3)
		{
			return ((FundsDueData) cvTableData.get(aiRow)).getDueAmount()
				+ CommonConstant.STR_SPACE_THREE;
		}
		else if (aiColumn == 4)
		{
			return ((FundsDueData) cvTableData.get(aiRow))
				.getRemitAmount()
				+ CommonConstant.STR_SPACE_THREE;
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
	/**
	 * Insertion sort by Funds Due Date.
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
				&& laFundsDueData.getFundsDueDate().compareTo(
					((FundsDueData) cvTableData.get(aiCurrentRowNo - 1))
						.getFundsDueDate())
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
	 * Insertion sort by Funds Report Date.
	 */
	private void insertSortB()
	{
		int k, liCurrentRowNo;
		int liMinRowNo = 0;
		int liMaxRowNo = cvTableData.size() - 1;
		for (k = liMinRowNo + 1; k <= liMaxRowNo; k++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) cvTableData.get(k);
			liCurrentRowNo = k;
			while (liMinRowNo < liCurrentRowNo
				&& laFundsDueData.getFundsReportDate().compareTo(
					((FundsDueData) cvTableData.get(liCurrentRowNo - 1))
						.getFundsReportDate())
					< 0)
			{
				cvTableData.set(
					liCurrentRowNo,
					cvTableData.get(liCurrentRowNo - 1));
				liCurrentRowNo--;
			}
			cvTableData.set(liCurrentRowNo, laFundsDueData);
		}
	}
	/**
	 * Insertion sort by Reporting Date.
	 */
	private void insertSortC()
	{
		int k, liCurrentRowNo;
		int ciMinRowNo = 0;
		int ciMaxRowNo = cvTableData.size() - 1;
		for (k = ciMinRowNo + 1; k <= ciMaxRowNo; k++)
		{
			FundsDueData laFundsDueData =
				(FundsDueData) cvTableData.get(k);
			liCurrentRowNo = k;
			while (ciMinRowNo < liCurrentRowNo
				&& laFundsDueData.getReportingDate().compareTo(
					((FundsDueData) cvTableData.get(liCurrentRowNo - 1))
						.getReportingDate())
					< 0)
			{
				cvTableData.set(
					liCurrentRowNo,
					cvTableData.get(liCurrentRowNo - 1));
				liCurrentRowNo--;
			}
			cvTableData.set(liCurrentRowNo, laFundsDueData);
		}
	}
}
