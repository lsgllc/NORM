package com.txdot.isd.rts.client.localoptions.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.CreditCardFeeData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMOPT006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang 	04/16/2005	RTS 5.2.3 Code Cleanup
 * 							defect 7891 Ver 5.2.3
 * Min Wang		09/02/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * B Hargrove	09/30/2005	Re-do column name handling using array.
 * 							defect 7891 Ver 5.2.3	  
 * --------------------------------------------------------------------- 
 */

/**
 * Table model for screen OPT006.  Provides methods to get 
 * the column count, column names and data value as well as 
 * a method for inserting rows into the table.
 * 
 * @version	5.2.3		09/30/2005
 * @author 	Michael Abernethy
 * <br>Creation Date:	04/25/2002 09:53:20  
 */

public class TMOPT006 extends javax.swing.table.AbstractTableModel
{

	private java.util.Vector cvVector;
	
	private static final String DOLLAR_SIGN = "$";
	private static final String PERCENTAGE_SIGN = "%  ";
	private final static String[] carrColumn_Name = 
		{"Credit Card Fee", "Effective Date", "End Date"};

	/**
	 * TMOPT006 constructor comment.
	 */
	public TMOPT006()
	{
		super();
		cvVector = new Vector();
	}

	/**
	 * Add data to the table.
	 * 
	 * @param avData Vector
	 */
	public void add(Vector avData)
	{
		cvVector = avData;
		fireTableDataChanged();
	}

	/**
	 * Get the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 3;
		return carrColumn_Name.length;
	}

	/**
	 * Get the selected column's name.
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
	 * Get the number of rows.
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}

	/**
	 * Get the selected value.
	 * 
	 * @param aiRow int
	 * @param aiCol int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		if (aiCol == 0)
		{
			CreditCardFeeData d =
				(CreditCardFeeData) cvVector.get(aiRow);
			if (d.isPercentage())
			{
				return ((CreditCardFeeData) cvVector.get(aiRow))
					.getItmPrice()
					+ PERCENTAGE_SIGN;
			}
			else
			{
				return DOLLAR_SIGN
					+ ((CreditCardFeeData) cvVector.get(aiRow))
						.getItmPrice()
					+ CommonConstant.STR_SPACE_TWO;
			}
		}
		else if (aiCol == 1)
		{
			return ((CreditCardFeeData) cvVector.get(aiRow))
				.getRTSEffDate();
		}
		else
		{
			return ((CreditCardFeeData) cvVector.get(aiRow))
				.getRTSEffEndDate();
		}
	}
}
