package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.SpecialPlatePermitData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMINQ004.java
 * 
 * (c) Texas Department of Transportation  2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/22/2010	Created 
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for INQ004
 * 
 * @version	6.7.0
 * @author	Kathy Harrell
 * <br>Creation Date:			12/22/2010
 */
public class TMINQ004SpclPltPrmt
	extends javax.swing.table.AbstractTableModel
{

	private Vector cvDatalist;
	private final static String[] carrColumn_Name =
		{ "Plate No", "Exp Date", "VehModlYr", "VehMk", "VIN" ,"Owner Name "};

	/**
	 * TMINQ004SpclPltPrmt constructor comment.
	 */
	public TMINQ004SpclPltPrmt()
	{
		super();
		cvDatalist = new Vector();
	}
	/**
	 * Adds data to the INQ004 table.
	 * 
	 * @param avDataList Vector
	 */
	public void add(Vector avDataList)
	{
		cvDatalist = avDataList;
		fireTableDataChanged();
	}

	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}

	/**
	 * Returns the column name.
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
	 * Returns the row count.
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvDatalist.size();
	}

	/**
	 * Returns the value.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		if (cvDatalist != null)
		{
			SpecialPlatePermitData laData =
				(SpecialPlatePermitData) cvDatalist.elementAt(aiRow);

			if (aiColumn == 0)
			{
				return laData.getRegPltNo();
			}
			else if (aiColumn == 1)
			{
				return ""
					+ new RTSDate(RTSDate.YYYYMMDD, laData.getExpDate())
						.toString();
			}
			else if (aiColumn == 2)
			{
				return "" + laData.getVehModlYr();
			}
			else if (aiColumn == 3)
			{
				return laData.getVehMk();
			}
			else if (aiColumn == 4)
			{
				return laData.getVIN();
			}
			else if (aiColumn == 5)
			{
				String lsPltOwnrName1 = laData.getPltOwnrName1();
				String lsPltOwnrName2 = laData.getPltOwnrName2();
				if (lsPltOwnrName2 != null
					&& lsPltOwnrName2.trim().length() != 0)
				{
					lsPltOwnrName1 =
						lsPltOwnrName1 + " / " + lsPltOwnrName2;
				}
				return lsPltOwnrName1;
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
