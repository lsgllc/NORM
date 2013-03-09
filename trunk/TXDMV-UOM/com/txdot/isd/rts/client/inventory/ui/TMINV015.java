package com.txdot.isd.rts.client.inventory.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.InventoryHistoryLogData;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 *
 * TMINV015.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/23/2005	RTS 5.2.3 Code Cleanup
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Move constants to InventoryConstant.
 * 							defect 7890 Ver 5.2.3
 * B Hargrove	09/29/2005	Re-do column name handling using array.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	10/12/2009	Inventory History Report interface 
 * 							standardization
 * 							modify carrColumn_Name, getValueAt() 
 * 							defect 10207 Ver Defect_POS_G	  
 * ---------------------------------------------------------------------
 */

/**
 * Table model for screen INV015.  Sets the column count and names
 * as well as inserting rows into the table and other table functions.
 * 
 * @version Defect_POS_G	10/12/2009
 * @author	Sunil Govindappa
 * <br>Creation Date:		06/22/2001 13:47:40
 */

public class TMINV015 extends javax.swing.table.AbstractTableModel
{
	// defect 10207 
	private final static String[] carrColumn_Name =
		{
			CommonConstant.TBL_TXT_OFFICENO,
			CommonConstant.TBL_TXT_OFFICENAME,
			InventoryConstant.TXT_LAST_ACTIVITY };
	// end defect 10207 

	private Vector lvData;

	/**
	 * CashDrawerTableModel constructor comment.
	 */
	public TMINV015()
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
		// defect 10207 
		// Implement Column Constants;  Remove "AL OFFICE" from Region

		InventoryHistoryLogData laDataObj =
			(InventoryHistoryLogData) lvData.get(aiRow);

		switch (aiColumn)
		{
			case InventoryConstant.INV015_COL_OFFICE_NO :
				{
					return new Integer(laDataObj.getOfcIssuanceNo());
				}
			case InventoryConstant.INV015_COL_OFFICE_NAME :
				{
					String lsName = laDataObj.getOfcName();
					
					// Region Only 
					if (laDataObj.getOfcIssuanceNo()
						== SystemProperty.getOfficeIssuanceNo())
					{
						int liIndex =
							lsName.toUpperCase().indexOf("AL OFFICE");
							
						if (liIndex > 0)
						{
							lsName = lsName.substring(0, liIndex);
						}
					}
					return lsName;
				}
			case InventoryConstant.INV015_COL_LAST_ACTIVITY :
				{
					Object laLastInsertDate = null;
					if (laDataObj == null)
					{
						laLastInsertDate =
							new String(CommonConstant.STR_SPACE_EMPTY);
					}
					else
					{
						laLastInsertDate =
							laDataObj.getLastInsertDate();
					}
					return laLastInsertDate;
				}
				// end defect 10207 
			default :
				{
					return null;
				}
		}
	}
}