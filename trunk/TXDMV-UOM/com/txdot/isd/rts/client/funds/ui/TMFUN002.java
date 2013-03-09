package com.txdot.isd.rts.client.funds.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 *
 * TMFUN002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	11/16/2004  Include test for null for FUN002	
 *							modify getValueAt()
 *							defect 7654  Ver 5.2.2 
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
 * Table model for screen FUN002.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	Defect_POS_F	06/08/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class TMFUN002 extends javax.swing.table.AbstractTableModel
{

	private Vector cvVector;
	
	// defect 8379
	private final static String[] COLUMN_NAME_ARRAY =
		{ "Id", "Last Closeout" };
	// end defect 8379 	

	/**
	 * TMFUN002 constructor comment.
	 */
	public TMFUN002()
	{
		super();
		cvVector = new Vector();
	}
	
	/**
	 * Add a vector to the table to post rows.
	 * 
	 * @param avPostRows Vector
	 */
	public void add(Vector avPostRows)
	{
		cvVector = new Vector(avPostRows);
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
	 * @param aiCol int 
	 * @return String 
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
	 * @param aiRow int
	 * @param aiCol int 
	 * @return Object 
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		// defect 9943 
		// Use getClockTimeNoMs(), FundsConstant
		switch (aiCol)
		{
			case FundsConstant.FUN002_ID :
				//if (aiCol == 0)
				{
					return new Integer(
						((CashWorkstationCloseOutData) cvVector
							.get(aiRow))
							.getCashWsId());
				}
				
			case FundsConstant.FUN002_LAST_CLOSEOUT :
				//else if (aiCol == 1)
				{
					RTSDate laDate =
						((CashWorkstationCloseOutData) cvVector
							.get(aiRow))
							.getCloseOutEndTstmp();

					// defect 7654 
					//If date = null or MIN Date return a blank string, so table
					//will have a blank space
					if (laDate != null
						&& !laDate.equals(
							new RTSDate(FundsConstant.RTS_MIN_DATE)))
					{
						return (
							(CashWorkstationCloseOutData) cvVector.get(
								aiRow))
							.getCloseOutEndTstmp()
							+ CommonConstant.STR_SPACE_THREE
							+ (String)
								(
									(
										(CashWorkstationCloseOutData) cvVector
								.get(aiRow))
								.getCloseOutEndTstmp())
								.getClockTimeNoMs();
						//.getClockTime().substring(0, 8);
					}
					else
					{
						return CommonConstant.STR_SPACE_EMPTY;
					}
					// end defect 7654 
				}
			default :
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
		}
		// end defect 9943		
	}
}
