package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.data.InventoryProfileData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV030.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/03/2005	Remove unused field
 * 							modify getValueAt()
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
 * Table Model for INV030.
 * 
 * @version 5.2.3			09/29/2005
 * @author	Sai Machavarapu
 * <br>Creation Date:		10/10/2001 18:07:33
 */

public class TMINV030 extends AbstractTableModel
{
	//private static final int COLUMN_COUNT = 6;
	private final static String[] carrColumn_Name = 
		{InventoryConstant.TXT_ENTITY_AND_ID, 
		 InventoryConstant.TXT_ITEM_DESCRIPTION, 
		 InventoryConstant.TXT_YEAR, InventoryConstant.TXT_MAX,
		 InventoryConstant.TXT_MIN, InventoryConstant.TXT_NEXT_ITEM};

	private Vector cvTblData = new Vector();

	/**
	 * TMINV030 constructor comment.
	 */
	public TMINV030()
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
		if (avDataIn != null)
		{
			cvTblData = new Vector(avDataIn);
		}
		else
		{
			cvTblData = null;
		}
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
	 * Get the column nane at the specified location.
	 * 
	 * <p>Returns emtry string if the location is not defined.
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
							new String(CommonConstant.STR_SPACE_EMPTY);
						lsStr =
							((InventoryProfileData) cvTblData
								.get(aiRow))
								.getEntity();
						if (lsStr.equals(InventoryConstant.CHAR_C))
						{
							return lsStr;
						}
						else
						{
							lsStr =
								lsStr
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE
									+ CommonConstant.STR_SPACE_ONE;
							lsStr =
								lsStr
									+ ((InventoryProfileData) cvTblData
										.get(aiRow))
										.getId();
							return lsStr;
						}
					}
				case 1 :
					{
						return (
							(InventoryProfileData) cvTblData.get(
								aiRow))
							.getItmCdDesc();
					}
				case 2 :
					{
						int liYear =
							((InventoryProfileData) cvTblData
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
							((InventoryProfileData) cvTblData
								.get(aiRow))
								.getMaxQty());
					}
				case 4 :
					{
						return Integer.toString(
							((InventoryProfileData) cvTblData
								.get(aiRow))
								.getMinQty());
					}
				case 5 :
					{
						if (((InventoryProfileData) cvTblData
							.get(aiRow))
							.getNextAvailIndi()
							== 1)
						{
							return InventoryConstant.TXT_YES_UC;
						}
						else
						{
							return InventoryConstant.TXT_NO_UC;
						}
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