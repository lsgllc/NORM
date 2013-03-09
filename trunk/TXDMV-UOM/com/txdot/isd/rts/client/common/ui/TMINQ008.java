package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.ModifyPermitTransactionHistoryData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMINQ008.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2011	Created
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for INQ008
 *
 * @version	6.8.0			06/19/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/19/2011	10:26:17 
 */
public class TMINQ008 extends javax.swing.table.AbstractTableModel
{
	private Vector cvDatalist;

	private final static String[] carrColumn_Name =
		{
			"TransId",
			"Date",
			"Type",
			"Exp Date",
			"Year",
			"Make",
			"VIN",
			"Applicant",
			"EmpId",
			"Office Name                       " };

	/**
	 * TMINQ008 constructor comment.
	 */
	public TMINQ008()
	{
		super();
		cvDatalist = new Vector();
	}

	/**
	 * Adds data to the INQ008 table.
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
		ModifyPermitTransactionHistoryData laData =
			(ModifyPermitTransactionHistoryData) cvDatalist.get(aiRow);

		Object laReturn = CommonConstant.STR_SPACE_EMPTY;

		if (cvDatalist != null)
		{
			switch (aiColumn)
			{
				case CommonConstant.INQ008_COL_TRANSID :
					{
						laReturn = laData.getTransId();
						break;
					}
				case CommonConstant.INQ008_COL_DATE :
					{
						int liAMDate = laData.getTransAMDate();

						laReturn =
							new RTSDate(RTSDate.AMDATE, liAMDate)
								.toString();
						break;
					}
				case CommonConstant.INQ008_COL_PRMTTYPE :
					{
						laReturn = laData.getItmCd();
						break;
					}
				case CommonConstant.INQ008_COL_EXPMOYR :
					{
						RTSDate laExpDate =
							new RTSDate(
								RTSDate.YYYYMMDD,
								laData.getExpDate());
						return laExpDate.getMMDDYYYY();
					}
				case CommonConstant.INQ008_COL_MODYR :
					{
						laReturn = "" + laData.getVehModlYr();
						break;
					}
				case CommonConstant.INQ008_COL_MAKE :
					{
						laReturn = laData.getVehMk();
						break;
					}
				case CommonConstant.INQ008_COL_VIN :
					{
						laReturn = laData.getVin();
						break;
					}
				case CommonConstant.INQ008_COL_APPLICANT :
					{
						laReturn =
							laData.getCustNameData().getCustName();
						break;
					}
				case CommonConstant.INQ008_COL_EMPID :
					{
						laReturn = laData.getTransEmpId();
						break;
					}
				case CommonConstant.INQ008_COL_OFCNAME :
					laReturn =
						OfficeIdsCache.getOfcName(
							laData.getOfcIssuanceNo());
					break;
			}
	}
	return laReturn;
}
}
