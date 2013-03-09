package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.ImageData;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.ItemCodesData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Sai			05/21/2002	Changing table column to I(t)em Code for 
 * 							the hot key
 * 							defect 4167 
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup	
 * 							modify getValueAt()
 * 							defect 7890 Ver 5.2.3
 * K Harrell	05/19/2005	Use ImageData constants vs. get Methods
 * 							for CHECK, DELETE, FAILED
 * 							modify getValueAt()
 * 							defect 7899  Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3  
 * Min Wang		08/01/2005	Remove Item code from table.
 * 							modify getColumnCount(), getColumnName() 
 * 							getValueAt()
 * 							defect 8269 Ver 5.2.2 Fix 6 
 * Ray Rowehl	08/12/2005	Cleanup pass.
 * 							Add white space between constants.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/17/2005	Use InventoryConstant for column headings.
 * 							defect 7890 Ver 5.2.3	  
 * B Hargrove	09/29/2005	Re-do column name handling using array.
 * 							defect 7890 Ver 5.2.3	  
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for INV002.
 * 
 * @version	5.2.3		09/29/2005
 * @author	Charlie Walker
 * <br>Creation Date:	10/11/2001 11:07:33
 */

public class TMINV002 extends AbstractTableModel
{

	// Vector 
	private Vector cvTblData = new Vector();
	
	//private static int NUMBER_OF_COLUMNS = 6;
	private final static String[] carrColumn_Name = 
		{CommonConstant.STR_SPACE_EMPTY, InventoryConstant.TXT_ITEM_DESCRIPTION, 
		 InventoryConstant.TXT_YEAR, InventoryConstant.TXT_QUANTITY,
		 InventoryConstant.TXT_BEGIN_NO, InventoryConstant.TXT_END_NO};

	/**
	 * TMINV002 constructor comment.
	 */
	public TMINV002()
	{
		super();
	}

	/**
	 * Add the data to the table.
	 * 
	 * @param avTableData Vector
	 */
	public void add(Vector avTableData)
	{
		cvTblData = new Vector(avTableData);
		fireTableDataChanged();
	}

	/**
	 * get the number of Columns.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return NUMBER_OF_COLUMNS;
		return carrColumn_Name.length;
	}

	/**
	 * Get the column name based on position.
	 * Returns empty string if not defined.
	 * 
	 * @param aCol int
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
	 * Get the number of Rows.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTblData.size();
	}

	/**
	 * getValueAt method comment.
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
						if (((InventoryAllocationUIData) cvTblData
							.get(aiRow))
							.getStatusCd()
							.equals(InventoryConstant.CHECK))
						{
							return new ImageData(ImageData.CHECK);
						}
						else if (
							((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getStatusCd()
								.equals(
								InventoryConstant.DELETE))
						{
							return new ImageData(ImageData.DELETE);
						}
						else if (
							((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getStatusCd()
								.equals(
								InventoryConstant.FAILED))
						{
							return new ImageData(ImageData.FAILED);
						}
						break;
					}
				case 1 :
					{
						String lsItmDesc =
							new String(CommonConstant.STR_SPACE_ONE);
						String lsItmCd =
							((InventoryAllocationUIData) cvTblData
								.get(aiRow))
								.getItmCd();
						ItemCodesData laICD =
							ItemCodesCache.getItmCd(lsItmCd);
						if (laICD != null)
						{
							lsItmDesc = laICD.getItmCdDesc();
						}
						return lsItmDesc;

					}
				case 2 :
					{
						String lsYr =
							new String(CommonConstant.STR_SPACE_ONE);
						int liYr =
							((InventoryAllocationData) cvTblData
								.get(aiRow))
								.getInvItmYr();
						if (liYr != 0)
						{
							lsYr = Integer.toString(liYr);
						}
						return lsYr;
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
