package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.MFPartialSpclPltData;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMINQ004.java
 * 
 * (c) Texas Department of Transportation  2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		02/08/2007	New class 
 *							defect 9086 Ver Special Plates
 * J Rue		02/12/2007	Format code
 *							defect 9086 Ver Special Plates
 * J Rue		02/15/2007	Add SpclRegId
 *							modify getValueAt()
 *							defect 9086 Ver Special Plates
 * J Rue		04/23/2007		Add RegPltCd
 *							modify getValueAt()
 *							defect 9086 Ver Special Plates
 * J Rue		05/01/2007	Add SpclDocNo
 *							modify getValueAt()
 *							defect 9086 Ver Special Plates
 * K Harrell	05/18/2007	Return empty string if all zeros for ExpMo, 
 * 								ExpYr, DocNo
 * 							modify getValueAt()
 * 							defect 9085 Ver Special Plates 
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for INQ004
 * 
 * @version	Special Plates		05/18/2007
 * @author	Jeff Rue
 * <br>Creation Date:			02/08/2007 14:56:04
 */
public class TMINQ004SpclPlts
	extends javax.swing.table.AbstractTableModel
{

	private Vector cvDatalist;
	private final static String[] carrColumn_Name =
		{
			"Plate No",
			"ExpMo",
			"ExpYr",
			"Owner Name",
			"RegPltCd",
			"DocNo" };

	/**
	 * TMCTL004 constructor comment.
	 */
	public TMINQ004SpclPlts()
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
			if (aiColumn == 0)
			{
				return ((MFPartialSpclPltData) cvDatalist.get(aiRow))
					.getRegPltNo();
			}
			else if (aiColumn == 1)
			{
				String lsRegExpMo =
					UtilityMethods.addPadding(
						Integer.toString(
							((MFPartialSpclPltData) cvDatalist
								.get(aiRow))
								.getRegExpMo()),
						2,
						CommonConstant.STR_ZERO);
				if (UtilityMethods.isAllZeros(lsRegExpMo))
				{
					lsRegExpMo = "";
				}

				return lsRegExpMo;
			}
			else if (aiColumn == 2)
			{
				String lsRegExpYr =
					Integer.toString(
						((MFPartialSpclPltData) cvDatalist.get(aiRow))
							.getRegExpYr());

				if (UtilityMethods.isAllZeros(lsRegExpYr))
				{
					lsRegExpYr = "";
				}
				return lsRegExpYr;
			}
			else if (aiColumn == 3)
			{
				return ((MFPartialSpclPltData) cvDatalist.get(aiRow))
					.getPltOwnrName1();
			}
			else if (aiColumn == 4)
			{
				return ((MFPartialSpclPltData) cvDatalist.get(aiRow))
					.getRegPltCd();
			}
			else if (aiColumn == 5)
			{
				String lsSpclDocNo =
					((MFPartialSpclPltData) cvDatalist.get(aiRow))
						.getSpclDocNo();
				if (UtilityMethods.isAllZeros(lsSpclDocNo.trim()))
				{
					lsSpclDocNo = "";
				}
				return lsSpclDocNo;
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
