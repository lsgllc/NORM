package com.txdot.isd.rts.client.specialplates.ui;
import java.util.Vector;

import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
/*
 * TMSPL003.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/11/2007	Created from TMRPR005
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	Renamed to TMSPL003
 * 							defect 9085 Ver Special Plates  
 * ---------------------------------------------------------------------
 */

/**
* Table model for screen SPL003.  Sets the column count and names
* as well as inserting rows into the table and other table functions.
* 
* @version Special Plates	02/27/2006
* @author	Kathy Harrell
* <br>Creation Date:		02/11/2006  15:13:00
*/
public class TMSPL003 extends javax.swing.table.AbstractTableModel
{

	private final static String[] carrColumn_Name =
		{
			CommonConstant.TBL_TXT_OFFICENO,
			CommonConstant.TBL_TXT_OFFICENAME };

	private java.util.Vector lvData;

	/**
	 * TableModel constructor comment.
	 */
	public TMSPL003()
	{
		super();
		lvData = new Vector();
	}

	/**
	 * Add Data to the table to post rows.
	 *  
	 * @param avDataIn Vector
	 */
	public void add(java.util.Vector avDataIn)
	{
		lvData = new Vector(avDataIn);
		fireTableDataChanged();
	}

	/**
	 * Get the number of columns in the table.
	 * 
	 * @return	int
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}

	/**
	 * Get the column name at the specified location.
	 * 
	 * <p>Returns empty string if location is not defined.
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
	 * Return the number of rows in the table.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return lvData.size();
	}

	/**
	 * Return the value at the specified location.
	 * 
	 * <p>Returns <b>null</b> if the location is not defined.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		switch (aiColumn)
		{
			case 0 :
				{
					OfficeIdsData laOfcIdsData =
						(OfficeIdsData) lvData.get(aiRow);
					return new Integer(laOfcIdsData.getOfcIssuanceNo());
				}
			case 1 :
				{
					OfficeIdsData laOfcIdsData =
						(OfficeIdsData) lvData.get(aiRow);
					return laOfcIdsData.getOfcName();
				}
			default :
				{
					return null;
				}
		}
	}
}
