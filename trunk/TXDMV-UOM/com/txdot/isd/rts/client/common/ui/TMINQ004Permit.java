package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.MFPartialPermitData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMINQ004Permit.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created 
 * 							defect 10491 Ver 6.5.0
 * K Harrell	07/09/2010	modify to show ExpDate vs. Exp Mo/Yr
 * 							modify getValueAt()
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/15/2010	Modified "Customer" entry in carrColumn_Name
 * 							to "Applicant".
 * 							modify carrColumn_Name
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for INQ004 - Permit Partials  
 *
 * @version	6.5.0 			07/15/2010
 * @author	K Harrell
 * <br>Creation Date:		05/24/2010 09:38:17
 */
public class TMINQ004Permit
	extends javax.swing.table.AbstractTableModel
{
	private Vector cvDataList;

	private final static String[] carrColumn_Name =
		{
			"Year",
			"Make",
			"VIN",
			"Permit No",
			"Type",
			"Exp Date",
			"Applicant" };

	/**
	 * TMINQ004Permit constructor comment.
	 */
	public TMINQ004Permit()
	{
		super();
		cvDataList = new Vector();
	}

	/**
	 * Adds data to the INQ004 table.
	 * 
	 * @param avDataList Vector
	 */
	public void add(Vector avDataList)
	{
		cvDataList = avDataList;
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
		return cvDataList.size();
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
		MFPartialPermitData laPartialPrmtData =
			(MFPartialPermitData) cvDataList.get(aiRow);

		switch (aiColumn)
		{
			case CommonConstant.INQ004PRMT_COL_MODYR :
				{
					return Integer.toString(
						laPartialPrmtData.getVehModlYr());
				}
			case CommonConstant.INQ004PRMT_COL_MAKE :
				{
					return laPartialPrmtData.getVehMk();
				}
			case CommonConstant.INQ004PRMT_COL_VIN :
				{
					return laPartialPrmtData.getVin();
				}
			case CommonConstant.INQ004PRMT_COL_PRMTNO :
				{
					return laPartialPrmtData.getPrmtNo();
				}
			case CommonConstant.INQ004PRMT_COL_PRMTTYPE :
				{
					return laPartialPrmtData.getItmCd();
				}
			case CommonConstant.INQ004PRMT_COL_EXPMOYR :
				{
					RTSDate laExpDate =
						new RTSDate(
							RTSDate.YYYYMMDD,
							laPartialPrmtData.getExpDate());
					return laExpDate.getMMDDYYYY();
				}
			case CommonConstant.INQ004PRMT_COL_NAME :
				{
					return laPartialPrmtData.getCustName();
				}
			default :
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
		}
	}
}
