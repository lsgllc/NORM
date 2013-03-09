package com.txdot.isd.rts.client.misc.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.TransactionHeaderData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;

/*
 *
 * TMCUS001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Zwiener	09/28/2005	Java 1.4
 *							defect 7892 Ver 5.2.3
 * K Harrell	01/29/2011	modify getValueAt() 
 * 							defect 10734 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen CUS001.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 *
 * @version	6.7.0			01/29/2011
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 * 
 */

public class TMCUS001 extends javax.swing.table.AbstractTableModel
{

	private Vector cvVector;
	private final static String[] carrColumn_Name =
		{ "Cust. No", "Name" };

	/**
	 * TMFUN001 constructor comment.
	 */
	public TMCUS001()
	{
		super();
		cvVector = new Vector();
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
		return carrColumn_Name.length;
	}
	
	/**
	 * Specify the names of each column in the table.
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
	 * @param aiRow int, 
	 * @param aiCol int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		// defect 10734 
		TransactionHeaderData laTransHdrData =
			(TransactionHeaderData) cvVector.get(aiRow);

		if (aiCol == MiscellaneousConstant.CUS001_COL_CUSTSEQNO)
		{
			return new Integer(laTransHdrData.getCustSeqNo());
		}
		else if (aiCol == MiscellaneousConstant.CUS001_COL_TRANSNAME)
		{
			return laTransHdrData.getTransName();
		}
		// end defect 10734 

		return CommonConstant.STR_SPACE_EMPTY;
	}
}
