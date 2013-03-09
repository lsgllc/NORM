package com.txdot.isd.rts.client.common.ui;

import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMINQ004REG.java
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/27/2005	Modify code for move to Java 1.4. 
 *							Code clean-up, etc.
 *							defect 7885 Ver 5.2.3
 * K Harrell	06/28/2009	Implement new OwnerData. Additional Cleanup.
 * 							modify getValueAt()
 * 							defect 10112 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for INQ004REG
 * 
 * @version	Defect_POS_F	06/28/2009
 * @author	Joseph Peters
 * <br>Creation Date:		09/20/2001 10:06:44
 */

public class TMINQ004REG extends javax.swing.table.AbstractTableModel
{
	private VehicleInquiryData caVehInqData;
	
	private final static String[] carrColumn_Name =
		{
			"Year",
			"Make",
			"VIN",
			"Plate No",
			"Exp Mo",
			"Exp Yr ",
			"Owner" };

	/**
	 * TMINQ004REG constructor comment.
	 */
	public TMINQ004REG()
	{
		super();
		caVehInqData = new VehicleInquiryData();
	}

	/**
	 * Adds data to the INQ004REG table.
	 * 
	 * @param VehicleInquiryData aaVehInqData
	 */
	public void add(VehicleInquiryData aaVehInqData)
	{
		caVehInqData = aaVehInqData;
		fireTableDataChanged();
	}

	/**
	 * Returns the column count.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		//return 7;
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
		return caVehInqData.getPartialDataList().size();
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
		// defect 10112 
		switch (aiColumn)
		{
			case CommonConstant.INQ004REG_COL_MODYR :
				{
					return Integer.toString(
						caVehInqData
							.getMfVehicleData()
							.getVehicleData()
							.getVehModlYr());
				}
			case CommonConstant.INQ004REG_COL_MAKE :
				{
					return caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVehMk();
				}
			case CommonConstant.INQ004REG_COL_VIN :
				{
					return caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVin();
				}
			case CommonConstant.INQ004REG_COL_PLTNO :
				{
					return (
						(RegistrationData) caVehInqData
							.getPartialDataList()
							.elementAt(
							aiRow))
						.getRegPltNo();
				}
			case CommonConstant.INQ004REG_COL_EXPMO :
				{
					return Integer.toString(
						((RegistrationData) caVehInqData
							.getPartialDataList()
							.elementAt(aiRow))
							.getRegExpMo());
				}
			case CommonConstant.INQ004REG_COL_EXPYR :
				{
					return Integer.toString(
						((RegistrationData) caVehInqData
							.getPartialDataList()
							.elementAt(aiRow))
							.getRegExpYr());
				}
			case CommonConstant.INQ004REG_COL_NAME1 :
				{
					return caVehInqData
						.getMfVehicleData()
						.getOwnerData()
						.getName1();
				}
			default :
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
		}
		// end defect 10112 
	}
}
