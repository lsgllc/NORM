package com.txdot.isd.rts.client.common.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.data.InProcessTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMINQ002.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/17/2010	Created
 * 							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for INQ002
 *
 * @version	6.6.0		09/17/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	09/17/2010	19:51:17
 */
public class TMINQ002 extends javax.swing.table.AbstractTableModel
{
	private Vector cvDatalist;

	private final static String[] carrColumn_Name =
		{
			"TransId",
			"Date",
			"Transaction Type",
			"Plate No",
			"EmpId",
			"Office Name                       " };

	/**
	 * TMINQ002 constructor comment.
	 */
	public TMINQ002()
	{
		super();
		cvDatalist = new Vector();
	}

	/**
	 * Adds data to the INQ002 table.
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
		InProcessTransactionData laData =
			(InProcessTransactionData) cvDatalist.get(aiRow);

		Object laReturn = CommonConstant.STR_SPACE_EMPTY;

		if (cvDatalist != null)
		{
			switch (aiColumn)
			{
				case CommonConstant.INQ002_COL_TRANSID :
					{
						laReturn = laData.getTransId();
						break;
					}
				case CommonConstant.INQ002_COL_DATE :
					{
						int liAMDate = laData.getTransAMDate();

						laReturn =
							new RTSDate(RTSDate.AMDATE, liAMDate)
								.toString();
						break;
					}
				case CommonConstant.INQ002_COL_TRANSCDDESC :
					{
						String lsReturn = new String();
						try
						{
							lsReturn =
								TransactionCodesCache.getTransCdDesc(
									laData.getTransCd());
							int liReceiptPos =
								lsReturn.indexOf("RECEIPT");
							if (liReceiptPos >= 0)
							{
								lsReturn =
									lsReturn.substring(0, liReceiptPos);
							}
							liReceiptPos =
								lsReturn.indexOf("TRANSACTION");

							if (liReceiptPos >= 0)
							{
								lsReturn =
									lsReturn.substring(0, liReceiptPos);
							}
							laReturn = lsReturn.trim();
						}
						catch (RTSException aeRTSEx)
						{

						}
						break;
					}
				case CommonConstant.INQ002_COL_REGPLTNO :
					{
						laReturn = " ";
						if (laData.getRegPltNo() != null)
						{
							laReturn = laReturn + laData.getRegPltNo();
						}
						break;
					}
				case CommonConstant.INQ002_COL_EMPID :
					{
						laReturn = " ";
						if (laData.getTransEmpId() != null)
						{
							laReturn =
								laReturn + laData.getTransEmpId();
						}
						break;
					}
				case CommonConstant.INQ002_COL_OFCNAME :
					{
						laReturn =
							OfficeIdsCache.getOfcName(
								laData.getOfcIssuanceNo());

						break;
					}
			}
		}
		return laReturn;
	}
}
