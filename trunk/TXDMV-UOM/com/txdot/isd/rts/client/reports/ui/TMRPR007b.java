package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.data.CertifiedLienholderData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMRPR007B.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/26/2009	Created 
 * 							defect 9972 Ver Defect_POS_E 
 * K Harrell	07/12/2009	Implement new LienholderData
 * 							modify getValueAt())
 * 							defect 10112 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for Transaction Code selection on RPR007.
 *
 * @version	Defect_POS_F	07/12/2009 
 * @author	Kathy Harrell
 * <br>Creation Date:		02/26/2009 
 */
public class TMRPR007b extends javax.swing.table.AbstractTableModel
{
	private Vector lvData;
	
	private final static String[] carrColumn_Name =
		{ "Perm Lienholder Id", "Lienholder Name" };

	private final static int PERMLIENHLDR_ID = 0;
	private final static int LIENHLDR_NAME = 1;
	

	/**
	 * TableModel constructor comment.
	 */
	public TMRPR007b()
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
		CertifiedLienholderData laData =
			(CertifiedLienholderData) lvData.get(aiRow);

		switch (aiColumn)
		{
			case PERMLIENHLDR_ID :
				{
					return laData.getPermLienHldrId() + "";
				}
			case LIENHLDR_NAME :
				{
					// defect 10112 
					return laData.getName1();
					// end defect 10112 
				}
			default :
				{
					return null;
				}
		}
	}
}
