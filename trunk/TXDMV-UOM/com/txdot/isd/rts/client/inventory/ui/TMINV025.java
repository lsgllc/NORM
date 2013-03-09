package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV025.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3  
 * Min Wang		08/01/2005	Remove Item code from table.
 * 							modify getColumnName(), getValueAt()
 * 							defect 8269 Ver 5.2.2 Fix 6  
 * Ray Rowehl	08/13/3005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Use InventoryConstant for column headings
 * 							defect 7890 Ver 5.2.3	     	   	
 * B Hargrove	09/29/2005	Re-do column name handling using array.
 * 							defect 7890 Ver 5.2.3	
 * Min Wang		05/20/2008	Display “” when the year of plate is 0. 
 * 							modify getValueAt()
 * 							defect 8312 Ver POS_A 
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for INV025.
 * 
 * @version	Ver POS_A		05/20/2008	
 * @author	Charlie Walker
 * <br>Creation Date:	12/06/2001 17:07:33
 */

public class TMINV025 extends AbstractTableModel
{
	//private static final int COLUMN_COUNT = 5;
	private final static String[] carrColumn_Name = 
		{InventoryConstant.TXT_ITEM_DESCRIPTION, 
		 InventoryConstant.TXT_YEAR, InventoryConstant.TXT_QUANTITY,
		 InventoryConstant.TXT_BEGIN_NO, InventoryConstant.TXT_END_NO};

	private Vector cvTblData = new Vector();

	/**
	 * TMINV025 constructor comment.
	 */
	public TMINV025()
	{
		super();
	}

	/**
	 * Add data to the table model.
	 * 
	 * @param avDataIn Vector
	 */
	public void add(Vector avDataIn)
	{
		cvTblData = new Vector(avDataIn);
		fireTableDataChanged();
	}

	/**
	 * Get the number of Columns.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return COLUMN_COUNT;
		return carrColumn_Name.length;
	}

	/**
	 * Get the column name at the specified location.
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
	 * Get the row count.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTblData.size();
	}

	/**
	 * Get the value at the specified location.
	 * <p>Return empty string if the location is not defined.
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
							new String(CommonConstant.STR_SPACE_EMPTY);
						lsStr =
							((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getItmCd();
						ItemCodesData laICC =
							ItemCodesCache.getItmCd(lsStr);
						lsStr = laICC.getItmCdDesc();
						return lsStr;
					}
				// defect 8312
				case 1 :
					{
						String lsYr =
								new String(CommonConstant.STR_SPACE_ONE);
						int liYr =
								((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getInvItmYr();
						if (liYr != 0)
						{
							lsYr = Integer.toString(liYr);
						}
						return lsYr;
						//return Integer.toString(
						//	((InventoryAllocationUIData) cvTblData
						//		.get(aiRow))
						//		.getInvItmYr());
					}
					// end defect 8312
				case 2 :
					{
						return Integer.toString(
							((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getInvQty());
					}
				case 3 :
					{
						return (
							(InventoryAllocationUIData) cvTblData.get(
								aiRow))
							.getInvItmNo();
					}
				case 4 :
					{
						return (
							(InventoryAllocationUIData) cvTblData.get(
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
