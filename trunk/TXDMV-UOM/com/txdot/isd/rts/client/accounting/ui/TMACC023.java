package com.txdot.isd.rts.client.accounting.ui;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsPaymentData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/**
 *
 * TMACC023.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			09/17/2001  Added comments
 * K Harrell	07/26/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3 	
 * B Hargrove	09/30/2005	Re-do column name handling using array.
 * 							defect 7884 Ver 5.2.3	  
 * --------------------------------------------------------------------- 
 */
/**
 * The table model for the table in the ACC023 screen.
 * It stores the table data and knows how to display it.
 *
 * @version	5.2.3		09/30/2005 
 * @author	Michael Abernethy
 * <br>Creation Date:	08/02/2001 10:17:16
 */

public class TMACC023 extends javax.swing.table.AbstractTableModel
{
	// Vector 
	private Vector cvTableData;

	private final static String DATE_PAD = "  /  /    ";
	private final static String[] carrColumn_Name = 
		{"Trace No.", "Payment Date", "Payment Amt", "Status",
		 "Received Date", "Check No."};

	/**
	 * Creates a TMACC023.
	 */
	public TMACC023()
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
		Collections.sort(cvTableData);
		fireTableDataChanged();
	}
	/**
	 * Consolidates records that share the same Trace No.
	 */
	private void consolidate()
	{
		Set laTempSet = new HashSet();
		while (cvTableData.size() > 0)
		{
			FundsPaymentData laFundsPaymentData1 =
				(FundsPaymentData) cvTableData.firstElement();
			int i = 1;
			while (i < cvTableData.size())
			{
				FundsPaymentData laFundsPaymentData2 =
					(FundsPaymentData) cvTableData.get(i);
				if (laFundsPaymentData1
					.getTraceNo()
					.equals(laFundsPaymentData2.getTraceNo()))
				{
					laFundsPaymentData1.setTotalPaymentAmount(
						laFundsPaymentData1
							.getTotalPaymentAmount()
							.add(
							laFundsPaymentData2
								.getTotalPaymentAmount()));
					cvTableData.remove(i);
					continue;
				}
				i++;
			}
			laTempSet.add(laFundsPaymentData1);
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
		//return 6;
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
	 * @param  aiColumn	int
	 * @param  aiRow	int
	 * @return Object 
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			return ((FundsPaymentData) cvTableData.get(aiRow))
				.getTraceNo()
				+ CommonConstant.STR_SPACE_TWO;
		}
		else if (aiColumn == 1)
		{
			return ((FundsPaymentData) cvTableData.get(aiRow))
				.getFundsPaymentDate();
		}
		else if (aiColumn == 2)
		{
			return ((FundsPaymentData) cvTableData.get(aiRow))
				.getTotalPaymentAmount()
				+ CommonConstant.STR_SPACE_TWO;
		}
		else if (aiColumn == 3)
		{
			return CommonConstant.STR_SPACE_TWO
				+ ((FundsPaymentData) cvTableData.get(aiRow))
					.getPaymentStatusDesc();
		}
		else if (aiColumn == 4)
		{
			if (((FundsPaymentData) cvTableData.get(aiRow))
				.getFundsReceivedDate()
				== null)
				return DATE_PAD;
			else
				return ((FundsPaymentData) cvTableData.get(aiRow))
					.getFundsReceivedDate();
		}
		else if (aiColumn == 5)
		{
			return ((FundsPaymentData) cvTableData.get(aiRow))
				.getCheckNo()
				+ CommonConstant.STR_SPACE_TWO;
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
