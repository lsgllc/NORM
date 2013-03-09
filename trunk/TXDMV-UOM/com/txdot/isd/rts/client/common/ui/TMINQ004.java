package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.MFPartialData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMINQ004.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/27/2005	Modify code for move to Java 1.4. 
 *							Code clean-up, etc.
 *							defect 7885 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for INQ004
 * 
 * @version	5.2.3		09/27/2005
 * @author	Joseph Peters
 * <br>Creation Date:	09/20/2001 10:06:44
 */

public class TMINQ004 extends javax.swing.table.AbstractTableModel
{
	private Vector cvDatalist;
	private final static String[] carrColumn_Name = 
		{"Year", "Make ", "VIN", "Plate No", "Owner", "Doc No"};
	
	/**
	 * TMCTL005 constructor comment.
	 */
	public TMINQ004()
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
		insertSortA();
		fireTableDataChanged();
	}
	
	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 6;
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

			if (aiColumn == 0)
			{
				return Integer.toString(
					((MFPartialData) cvDatalist.get(aiRow))
						.getVehModlYr());
			}
			else if (aiColumn == 1)
			{
				return ((MFPartialData) cvDatalist.get(aiRow)).
					getVehMk();
			}
			else if (aiColumn == 2)
			{
				return ((MFPartialData) cvDatalist.get(aiRow)).getVin();
			}
			else if (aiColumn == 3)
			{
				return ((MFPartialData) cvDatalist.get(aiRow))
					.getRegPltNo();
			}
			//else if (column == 4)
			//	return Integer.toString(((MFPartialData)cvDatalist.
			//	get(row)).getRegExpMo());
			//else if (column == 5)
			//	return Integer.toString(((MFPartialData)cvDatalist.
			//	get(row)).getRegExpYr());
			else if (aiColumn == 4)
			{
				return ((MFPartialData) cvDatalist.get(aiRow))
					.getOwnrTtlName();
			}
			else if (aiColumn == 5)
			{
				return ((MFPartialData) cvDatalist.get(aiRow)).
					getDocNo();
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}

	/**
	 * Insertion sort by Funds Due Date.
	 */
	private void insertSortA()
	{
		int liK, liLoc;
		int liLeft = 0;
		int liRight = cvDatalist.size() - 1;

		for (liK = liLeft + 1; liK <= liRight; liK++)
		{
			MFPartialData laMFPartialData = 
				(MFPartialData) cvDatalist.get(liK);
			liLoc = liK;
			while (liLeft < liLoc &&
				laMFPartialData.getDocNo().substring(6, 17).compareTo(
				((MFPartialData) cvDatalist.get(liLoc - 1)).getDocNo().
				substring(6,17))> 0)
			{
				cvDatalist.set(liLoc, cvDatalist.get(liLoc - 1));
				liLoc--;
			}
			cvDatalist.set(liLoc, laMFPartialData);
		}
		// defect 7885
		// not used
		//int i = 0;
		// end defect 7885
	}
}
