package com.txdot.isd.rts.client.registration.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/* 
 * TMREG006.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/28/2005	Modify for move to Java 1.4. Bring code to
 * 							standards. Format, Hungarian notation, etc. 
 * 							defect 7894 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for REG006
 * 
 * @version	5.2.3		09/28/2005
 * @author	Nancy Ting
 * <br>Creation Date:	10/18/2001 17:36:28
 */
public class TMREG006 extends javax.swing.table.AbstractTableModel
{
	
	private java.util.Vector cvInvInfo;
	private final static String ERROR = "error";
	private final static String[] carrColumn_Name = 
		{"Year", "Item Code - Description", "Quantity", "Begin No.",
		 "End No."};

	/**
	 * TMREG006 constructor.
	 */
	public TMREG006()
	{
		super();
		cvInvInfo = new Vector();
	}
	/**
	 * Add the data to the table model
	 *  
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		if (avVector != null)
		{
			cvInvInfo.clear();
			for (int i = 0; i < avVector.size(); i++)
			{
				InventoryAllocationData laInventoryAllocationData =
					(InventoryAllocationData) avVector.get(i);
				cvInvInfo.addElement(laInventoryAllocationData);
			}
		}
		fireTableDataChanged();
	}
	/**
	 * Get the number of columns of the table
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 5;
		return carrColumn_Name.length;
	}
	/**
	 * Get the name of each column based on the column index
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
	 * Get number of rows in the table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvInvInfo.size();
	}
	/**
	 * Get the column value of a particular row and column
	 * 
	 * @param aiRow int
	 * @param aiCol int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiCol)
	{
		InventoryAllocationData laInventoryAllocationData =
			(InventoryAllocationData) cvInvInfo.get(aiRow);
		switch (aiCol)
		{
			case 0 :
			{
				if (laInventoryAllocationData.getInvItmYr() == 0)
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
				else
				{
					return String.valueOf(
						laInventoryAllocationData.getInvItmYr());
				}
			}
			case 1 :
			{
				ItemCodesData laItemCodesData =
					ItemCodesCache.getItmCd(
						laInventoryAllocationData.getItmCd());
				String lsItmDesc = CommonConstant.STR_SPACE_EMPTY;
				if (laItemCodesData != null)
				{
					lsItmDesc = laItemCodesData.getItmCdDesc();
				}
				return laInventoryAllocationData.getItmCd()
					+ CommonConstant.STR_DASH
					+ lsItmDesc;
			}
			case 2 :
			{
				return String.valueOf(
					laInventoryAllocationData.getInvQty());
			}
			case 3 :
			{
				return laInventoryAllocationData.getInvItmNo();
			}
			case 4 :
			{
				return laInventoryAllocationData.getInvItmEndNo();
			}
			default :
			{
				return ERROR;
			}
		}

	}
}
