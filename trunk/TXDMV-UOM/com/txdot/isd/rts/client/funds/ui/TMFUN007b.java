package com.txdot.isd.rts.client.funds.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 *
 * TMFUN007b.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Tulsiani   04/29/2002  Removed system.out for date
 * K Harrell  	08/06/2002	Handle MIN_RTS_DATE for CurrStatTimestmp
 *							modify getValueAt()
 * K Harrell	11/16/2004  Include test for null for FUN007	
 *							modify getValueAt()
 *							defect 7654  Ver 5.2.2 
 * B Hargrove	09/30/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	11/02/2005	Consistent verbiage for Closeout
 * 							Change "Last Close Out" to "Last Closeout"
 * 							rename carrColumn_Name to COLUMN_NAME_ARRAY
 * 							defect 8379 Ver 5.2.3
 * K Harrell	05/07/2008	Do not show data re: Closeout,Current Status
 * 							for HQ  
 * 							modify getValueAt()
 * 							defect 9653 Ver Defect POS A     
 * K Harrell	06/08/2009	Refactored from TMFUN001b 
 * 							Implement RTSDate.getClockTimeNoMs(),
 * 							 FundsConstant
 * 							modify getValueAt() 
 * 							defect 9943 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/**
 * A Table model for screen FUN007.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	Defect_POS_F	06/08/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class TMFUN007b extends javax.swing.table.AbstractTableModel
{
	private Vector cvVector;

	private final static String[] COLUMN_NAME_ARRAY =
		{ "Id", "Last Closeout", "Last Current Status" };

	/**
	 * TMFUN007b constructor comment.
	 */
	public TMFUN007b()
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
		// Use FundsConstant, getClockTimeNoMs() 
		if (aiCol == FundsConstant.FUN007B_ID)
			//if (aiCol == 0)
		{
			return new Integer(
				((CashWorkstationCloseOutData) cvVector.get(aiRow))
					.getCashWsId());
		}
		// defect 9653
		// Do not show Last Closeout or Current Status for HQ
		else if (!SystemProperty.isHQ())
		{
			if (aiCol == FundsConstant.FUN007B_LAST_CLOSEOUT)
				//if (aiCol == 1)
			{
				RTSDate laDate =
					((CashWorkstationCloseOutData) cvVector.get(aiRow))
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
							(((CashWorkstationCloseOutData) cvVector
							.get(aiRow))
							.getCloseOutEndTstmp())
							.getClockTimeNoMs();
							//.getClockTime().substring(0, 8);
				}
				else
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
			}
			else if (
				aiCol == FundsConstant.FUN007B_LAST_CURRENT_STATUS)
				//else if (aiCol == 2)
			{
				RTSDate ladate =
					((CashWorkstationCloseOutData) cvVector.get(aiRow))
						.getCurrStatTimestmp();
				//If date = null or MIN Date return a blank string, so table
				//will have a blank space
				if (ladate != null
					&& !ladate.equals(
						new RTSDate(FundsConstant.RTS_MIN_DATE)))
				{
					return (
						(CashWorkstationCloseOutData) cvVector.get(
							aiRow))
						.getCurrStatTimestmp()
						+ CommonConstant.STR_SPACE_THREE
						+ (String)
							(((CashWorkstationCloseOutData) cvVector
							.get(aiRow))
							.getCurrStatTimestmp())
							.getClockTimeNoMs();
							//.getClockTime().substring(0, 8);
				}
				else
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
				// end defect 7654 
			}
		}
		// end defect 9653 
		// end defect 9943 
		return CommonConstant.STR_SPACE_EMPTY;
	}
}