package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.OwnershipEvidenceCodesData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TitleValidObj.java 
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * J Rue		03/22/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3                  
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * ---------------------------------------------------------------------
 */

/**
 * The table model for the table in the ACC001 screen.  It stores the 
 * table data and knows how to display it.
 *
 * @version	5.2.3		08/23/2005
 * @author	Michael Abernethy
 * <br>Creation Date:	08/29/2001 10:36:49 
 */

public class TMTTL011 extends javax.swing.table.AbstractTableModel
{

	/**
	*	An graph of the table data
	*/
	private Vector cvVector;
	/**
	 * Creates a TMACC006.
	 */
	public TMTTL011()
	{
		super();
		cvVector = new Vector();
	}
	/**
	 * Updates the table data with the new data in the vector
	 * 
	 * @param cvVector Vector the new data
	 */
	public void add(Vector avVector)
	{
		cvVector = avVector;
		fireTableDataChanged();
	}
	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return 1;
	}
	/**
	 * Returns the column name.
	 * 
	 * @param aiCol int
	 * @return String
	 */
	public String getColumnName(int aiCol)
	{
		if (aiCol == 0)
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
	/**
	 * Returns the number of rows.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
	}
	/**
	 * Returns the value in the table.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (aiColumn == 0)
		{
			return ((OwnershipEvidenceCodesData) cvVector.get(aiRow))
				.getOwnrshpEvidCdDesc();
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
