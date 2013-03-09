package com.txdot.isd.rts.client.funds.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 *
 * TMFUN014.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/30/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	11/02/2005	Consistent verbiage for Closeout
 * 							Change "Last Close Out" to "Last Closeout" 
 * 							rename carrColumn_Name to COLUMN_NAME_ARRAY
 * 							defect 8379 Ver 5.2.3  
 * K Harrell	06/08/2009	Implement RTSDate.getClockTimeNoMs(),
 * 							 FundsConstant
 * 							modify getValueAt()  
 * 							defect 9943 Ver Defect_POS_F    
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen FUN014.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	Defect_POS_F	06/08/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class TMFUN014 extends javax.swing.table.AbstractTableModel
{
	// defect 8379 
	private final static String[] COLUMN_NAME_ARRAY =
		{ "Id", "Last Closeout Request Time" };
	// end defect 8379

	private Vector cvVector;

	/**
	 * TMFUN014 constructor comment.
	 */
	public TMFUN014()
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
		return COLUMN_NAME_ARRAY.length;
	}

	/**
	 * Specify the names of each column in the table.
	 * 
	 * @return String
	 * @param aiCol int
	 */
	public String getColumnName(int aiCol)
	{
		if (aiCol >= 0 && aiCol < COLUMN_NAME_ARRAY.length)
		{
			return COLUMN_NAME_ARRAY[aiCol];
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
		// defect 9943
		//  Use getClockTimeNoMs(), FundsConstant
		switch (aiCol)
		{
			case FundsConstant.FUN014_ID :
				//if (aiCol == 0)			
				{
					return new Integer(
						((CashWorkstationCloseOutData) cvVector
							.get(aiRow))
							.getCashWsId());

				}
			case FundsConstant.FUN014_LAST_CLOSEOUT_REQ_TIME :
				//else if (aiCol == 1)
				{
					return (
						(CashWorkstationCloseOutData) cvVector.get(
							aiRow))
						.getCloseOutEndTstmp()
						.getClockTimeNoMs();
						//.getClockTime().substring(0, 8);
				}
			default :
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
		}
		// end defect 9943 
	}
}
