package com.txdot.isd.rts.client.common.ui;

import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMREG002.java
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/28/2005	Modify code for move to Java 1.4
 *							defect 7885 Ver 5.2.3
 * B Hargrove	06/11/2009	Remove all 'Cancelled Sticker' references.
 * 							Add column headers.
 * 							Use CommonConstants for column numbers.
 * 							Add carrColumn_Name[]
 * 							modify getColumnCount(), getColumnName(),
 * 							getValueAt()
 * 							defect 9953 Ver Defect_POS_F
 * K Harrell	06/28/2009	Implement new OwnerData 
 * 							modify getValueAt()
 * 							defect 10112 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for REG002
 * 
 * @version	Defect_POS_F	06/11/2009
 * @author	Joseph Peters
 * <br>Creation Date:		10/30/2001 10:06:44
 */

public class TMREG002 extends javax.swing.table.AbstractTableModel
{
	private VehicleInquiryData caInquiryData;
	// defect 9953
	private final static String[] carrColumn_Name = 
		{"Owner Name", "Plate Number"};
	// end defect 9953
		
	/**
	 * TMREG002 constructor.
	 */
	public TMREG002()
	{
		super();
	}
	
	/**
	 * Put the data in the table model
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	public void add(VehicleInquiryData aaVehInqData)
	{
		caInquiryData = aaVehInqData;
	}
	
	/**
	 * get the number of columns in the table model
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		// defect 9953
		//return 2;
		return carrColumn_Name.length;
		// end defect 9953
	}
	
	/**
	 * Get the individual column names by column number.
	 * 
	 * @param aiCol int
	 * @return java.lang.String
	 */
	public String getColumnName(int aiCol)
	{
		// defect 9953
		//if (aiCol == 0)
		//{
		//	return CommonConstant.STR_SPACE_ONE;
		//}
		//else if (aiCol == 1)
		//{
		//	return CommonConstant.STR_SPACE_ONE;
		//}
		//else
		//{
		//	return CommonConstant.STR_SPACE_EMPTY;
		//}
		if (aiCol >= 0 && aiCol < carrColumn_Name.length)
		{
			return carrColumn_Name[aiCol];
		}
		else
		{
			return CommonConstant.STR_SPACE_EMPTY;
		}
		// end defect 9953
	}
	
	/**
	 * Get the number of row
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return 1;
	}
	
	/**
	 * Get the value at a specific row/column
	 * 
	 * @param int aiRow
	 * @param int aiColumn
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{

		if (caInquiryData != null)
		{
			// defect 9953
			// Use CommonConstants for all column numbers below.
			//if (aiColumn == 0)
			if (aiColumn == CommonConstant.REG002_COL_OWNRNAME)
			{
				// defect 10112 
				return caInquiryData
					.getMfVehicleData()
					.getOwnerData()
					.getName1();
				// end defect 10112 
			}
			//else if (aiColumn == 1)
			else if (aiColumn == CommonConstant.REG002_COL_PLTNO)
			// end defect 9953
			{
				if (caInquiryData
					.getMfVehicleData()
					.getRegData()
					.getCancPltIndi()
					== 1)
				{
					return caInquiryData
						.getMfVehicleData()
						.getRegData()
						.getRegPltNo();
				}
				// defect 9953
//				else if (
//					caInquiryData
//						.getMfVehicleData()
//						.getRegData()
//						.getCancStkrIndi()
//						== 1)
//				{
//					return caInquiryData
//						.getMfVehicleData()
//						.getRegData()
//						.getRegStkrNo();
//				}
				// end defect 9953
				else
				{
					return caInquiryData
						.getMfVehicleData()
						.getRegData()
						.getRegPltNo();
				}
			}
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
