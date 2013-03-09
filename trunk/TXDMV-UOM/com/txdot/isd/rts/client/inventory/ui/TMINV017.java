package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV017.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Min Wang		08/01/2005	Remove Item code from table.
 * 							modify getColumnCount(), getColumnName() 
 * 							getValueAt()
 * 							defect 8269 Ver 5.2.2 Fix 6  
 * Ray Rowehl	08/13/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Use InventoryConstant for column headings.
 * 							defect 7890 Ver 5.2.3
 * B Hargrove	09/29/2005	Re-do column name handling using array.
 * 							defect 7890 Ver 5.2.3	  
 * ---------------------------------------------------------------------
 */

/**
 * Tablemodel for Frame INV017.
 * 
 * @version 5.2.3 		09/29/2005
 * @author	Charlie Walker
 * <br>Creation Date:	11/30/2001 14:24:40
 */

public class TMINV017 extends javax.swing.table.AbstractTableModel
{
	//private final static int COLUMN_COUNT = 6;
	private final static String[] carrColumn_Name = 
		{InventoryConstant.TXT_ENTITY_ID, InventoryConstant.TXT_ITEM_DESCRIPTION, 
		 InventoryConstant.TXT_YEAR, InventoryConstant.TXT_QUANTITY,
		 InventoryConstant.TXT_BEGIN_NO, InventoryConstant.TXT_END_NO};

	private Vector cvTblData = new Vector();

	/**
	 * TMINV017 constructor comment.
	 */
	public TMINV017()
	{
		super();
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @param avDataIn Vector
	 */
	public void add(Vector avDataIn)
	{
		cvTblData = new Vector(avDataIn);
		fireTableDataChanged();
	}

	/**
	 * Get the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return COLUMN_COUNT;
		return carrColumn_Name.length;
	}

	/**
	 * Return the Column Name at the specified location.
	 * 
	 * <p>Return empty string if the location is not defined.
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
	 * Get the Row Count.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTblData.size();
	}

	/**
	 * Get the value at the specified location.
	 * 
	 * <p>Returns empty string if the location is not defined.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (cvTblData != null)
		{
			switch (aiColumn)
			{
				case 0 :
					{
						String lsStr =
							((InventoryAllocationData) cvTblData
								.get(aiRow))
								.getInvLocIdCd();
						if (!lsStr.equals(InventoryConstant.CHAR_C))
						{
							lsStr =
								lsStr + CommonConstant.STR_SPACE_ONE;
							lsStr =
								lsStr
									+ (
										(InventoryAllocationData) cvTblData
										.get(aiRow))
										.getInvId();
						}
						return lsStr;
					}
				case 1 :
					{
						return (
							ItemCodesCache.getItmCd(
								((InventoryAllocationData) cvTblData
									.get(aiRow))
									.getItmCd()))
							.getItmCdDesc();
					}
				case 2 :
					{
						int liYear =
							((InventoryAllocationData) cvTblData
								.get(aiRow))
								.getInvItmYr();
						if (liYear == 0)
						{
							return CommonConstant.STR_SPACE_EMPTY;
						}
						else
						{
							return Integer.toString(liYear);
						}
					}
				case 3 :
					{
						return Integer.toString(
							((InventoryAllocationData) cvTblData
								.get(aiRow))
								.getInvQty());
					}
				case 4 :
					{
						return (
							(InventoryAllocationData) cvTblData.get(
								aiRow))
							.getInvItmNo();
					}
				case 5 :
					{
						return (
							(InventoryAllocationData) cvTblData.get(
								aiRow))
							.getInvItmEndNo();
					}
				default :
					{
						break;
					}
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
