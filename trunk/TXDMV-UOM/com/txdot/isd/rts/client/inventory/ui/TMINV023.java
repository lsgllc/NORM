package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.data.InventoryPatternsDescriptionData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV023.java
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
 * Table Model for INV023.
 * 
 * @version	5.2.3		09/29/2005
 * @author: Charlie Walker
 * <br>Creation Date:	12/01/2001 18:54:33
 */

public class TMINV023 extends AbstractTableModel
{
	//private static final int COLUMN_COUNT = 2;
	private final static String[] carrColumn_Name = 
		{InventoryConstant.TXT_ITEM_DESCRIPTION, 
		 InventoryConstant.TXT_YEAR};

	private Vector cvTblData = new Vector();

	/**
	 * TMINV022 constructor comment.
	 */
	public TMINV023()
	{
		super();
	}

	/**
	 * Add the data to the table model.
	 * 
	 * @param avDataIn Vector
	 */
	public void add(Vector avDataIn)
	{
		cvTblData = new Vector(avDataIn);
		fireTableDataChanged();
	}

	/**
	 * Get the Column Count.
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
	 * <p>Returns empty string is the location is not defined.
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
						return (
							(
								InventoryPatternsDescriptionData) cvTblData
									.get(
								aiRow))
							.getItmCdDesc();
					}
				case 1 :
					{
						int liYr =
							(
								(InventoryPatternsDescriptionData) cvTblData
								.get(aiRow))
								.getInvItmYr();
						if (liYr == 0)
						{
							return new String(
								CommonConstant.STR_SPACE_ONE);
						}
						else
						{
							return String.valueOf(liYr);
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
