package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.TransactionCodesData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMRPR007.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/26/2009	Created 
 * 							defect 9972 Ver Defect_POS_E 
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for Transaction Code selection on RPR007.
 *
 * @version	Defect_POS_E	02/26/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		02/26/2009 
 */
public class TMRPR007a extends javax.swing.table.AbstractTableModel
{
	private final static String[] carrColumn_Name =
		{ "TransCd", "TransCd Description" };
		
	private final static int TRANSCD = 0;
	private final static int TRANSCD_DESC = 1;
	
	private Vector lvData;

	/**
	 * TableModel constructor comment.
	 */
	public TMRPR007a()
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
		TransactionCodesData laData =
			(TransactionCodesData) lvData.get(aiRow);
			
		switch (aiColumn)
		{
			case TRANSCD :
				{
					return laData.getTransCd();
				}
			case TRANSCD_DESC :
				{
					String lsTransCdDesc = laData.getTransCdDesc();
					int liIndex = lsTransCdDesc.indexOf("RECEIPT"); 
					if (liIndex >=0)
					{
						lsTransCdDesc= lsTransCdDesc.substring(0,liIndex);	
					}
					return lsTransCdDesc;
				}
			default :
				{
					return null;
				}
		}
	}
}
