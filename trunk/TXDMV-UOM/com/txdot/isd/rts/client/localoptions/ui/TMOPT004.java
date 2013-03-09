package com.txdot.isd.rts.client.localoptions.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.data.PaymentAccountData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TMOPT004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		04/16/2005	RTS 5.2.3 Code Cleanup
 * 							defect 7891 Ver 5.2.3
 * Min Wang		09/02/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * B Hargrove	09/30/2005	Re-do column name handling using array.
 * 							defect 7891 Ver 5.2.3	  
 * --------------------------------------------------------------------- 
 */

/**
 * Table model for screen OPT004.  Provides methods to get 
 * the column count, column names and data value as well as 
 * a method for inserting rows into the table.
 * 
 * @version	5.2.3		09/30/2005
 * @author	Administrator
 * <p>Creation Date: 	
 */

public class TMOPT004 extends AbstractTableModel
{

	private Vector cvTblData = new Vector();
	
	private final static String[] carrColumn_Name = 
		{"Comptroller Location Code", "Description"};

	/**
	 * TMOPT004 constructor 
	 */
	public TMOPT004()
	{
		super();
	}

	/**
	 * Adds the data to the table
	 * 
	 * @param avData Vector
	 */
	public void add(Vector avData)
	{
		if (avData != null)
		{
			cvTblData = new Vector(avData);
		}
		else
		{
			cvTblData = null;
		}
		fireTableDataChanged();
	}

	/**
	 * Specify the number of columns in table.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 2;
		return carrColumn_Name.length;
	}

	/**
	 * Specify the names of each column in the table.
	 *  
	 * @return String
	 * @param aiCol int
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
	 * Return the number of rows in the table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTblData.size();
	}
	
	/**
	 * Get the data value at a given row and column in the table
	 * 
	 * @return Object 
	 * @param aiRow int
	 * @param aiCol int
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		if (cvTblData != null)
		{
			if (aiCol == 0)
			{
				return ((PaymentAccountData) cvTblData.get(aiRow))
					.getPymntLocId();
			}
			else if (aiCol == 1)
			{
				return ((PaymentAccountData) cvTblData.get(aiRow))
					.getPymntLocDesc();
			}
			// return default value
			return CommonConstant.STR_SPACE_EMPTY;
		}
		// return default value
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
