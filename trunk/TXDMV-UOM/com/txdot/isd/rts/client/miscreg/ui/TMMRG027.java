package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMMRG027.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for FrmDisabledPlacardReportMRG027 
 *
 * @version	POS_Defect_B	10/21/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/21/2008
 */

public class TMMRG027 extends javax.swing.table.AbstractTableModel
{

	private final static String[] carrColumn_Name =
		{
			CommonConstant.TBL_TXT_OFFICENO,
			CommonConstant.TBL_TXT_OFFICENAME };

	private Vector lvData;

	/**
	 * TableModel constructor comment.
	 */
	public TMMRG027()
	{
		super();
		lvData = new Vector();
	}

	/**
	 * Add Data to the table to post rows.
	 *  
	 * @param avDataIn Vector
	 */
	public void add(Vector avDataIn)
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
