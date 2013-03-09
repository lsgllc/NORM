package com.txdot.isd.rts.client.common.ui;

import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * TMCTL005.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	09/27/2005	Modify code for move to Java 1.4. 
 *							Code clean-up, etc.
 *							defect 7885 Ver 5.2.3
 * B Hargrove	06/11/2009	Remove all 'Cancelled Sticker' references.
 * 							Use CommonConstants for column numbers.
 * 							modify getValueAt()
 * 							defect 9953 Ver Defect_POS_F
 * K Harrell	06/28/2009	Implement new OwnerData
 * 							modify getValueAt()
 * 							defect 10112 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */
/**
 * Table Model for CTL005
 * 
 * @version	Ver Defect_POS_F	06/28/2009
 * @author	Joseph Peters
 * <br>Creation Date:			09/20/2001 10:06:44
 */

public class TMCTL005 extends javax.swing.table.AbstractTableModel
{
	private VehicleInquiryData caInquiryData;
	private final static String[] carrColumn_Name = 
		{"Mod Yr", "Make", "VIN", "Plt No", "Exp Mo", "Exp Yr", 
		 "Owner Name"};
	
	/**
	 * TMCTL005 constructor comment.
	 */
	public TMCTL005()
	{
		super();
	}
	
	/**
	 * Adds data to the CTL005 table.
	 * 
	 * @param VehicleInquiryData aaVehInqData
	 */
	public void add(VehicleInquiryData aaVehInqData)
	{
		caInquiryData = aaVehInqData;
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
		return 1;
	}
	
	/**
	 * Returns the value.
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return java.lang.Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{

		if (caInquiryData != null)
		{

			MFVehicleData laMFVehData = 
				caInquiryData.getMfVehicleData();

			// defect 9953
			// Use CommonConstants for all column numbers below
			//if (aiColumn == 0)
			if (aiColumn == CommonConstant.CTL005_COL_MODYR)
			{
				if (laMFVehData.getVehicleData().getVehModlYr() != 0)
				{
					return Integer.toString(
						laMFVehData.getVehicleData().getVehModlYr());
				}
				else
				{
					return CommonConstant.STR_SPACE_EMPTY;
				}
			}
			//else if (aiColumn == 1)
			else if (aiColumn == CommonConstant.CTL005_COL_MAKE)
			{
				return (laMFVehData.getVehicleData().getVehMk());
			}
			//else if (aiColumn == 2)
			else if (aiColumn == CommonConstant.CTL005_COL_VIN)
			{
				if (laMFVehData.getRegData().getCancPltIndi() == 1)
				{
					return laMFVehData.getRegData().getCancPltVin();
				}
				else
				{
					return (laMFVehData.getVehicleData().getVin());
				}
			}
			//else if (aiColumn == 3)
			else if (aiColumn == CommonConstant.CTL005_COL_PLTNO)
			{
				return (laMFVehData.getRegData().getRegPltNo());
			}
			//else if (aiColumn == 4)
			else if (aiColumn == CommonConstant.CTL005_COL_EXPMO)
			{
				if (laMFVehData.getRegData().getCancPltIndi() == 1)
				{
					if (laMFVehData.getRegData().getCancRegExpMo() != 0)
					{
						return Integer.toString(
							laMFVehData.getRegData().getCancRegExpMo());
					}
					else
					{
						return CommonConstant.STR_SPACE_EMPTY;
					}
				}
				else
				{
					if (laMFVehData.getRegData().getRegExpMo() != 0)
					{
						return Integer.toString(
							laMFVehData.getRegData().getRegExpMo());
					}
					else
					{
						return CommonConstant.STR_SPACE_EMPTY;
					}
				}
			}
			//else if (aiColumn == 5)
			else if (aiColumn == CommonConstant.CTL005_COL_EXPYR)
			{
				if (laMFVehData.getRegData().getCancPltIndi() == 1)
				{
					// defect 9953
					return CommonConstant.STR_SPACE_EMPTY;
					//if (laMFVehData.getRegData().
					//	getCancStkrExpYr() != 0)
					//{
					//	return Integer.toString(
					//		laMFVehData.getRegData().
					//			getCancStkrExpYr());
					//}
					//else
					//{
					//	return CommonConstant.STR_SPACE_EMPTY;
					//}
					// end defect 9953
				}
				else
				{
					if (laMFVehData.getRegData().getRegExpYr() != 0)
					{
						return Integer.toString(
							laMFVehData.getRegData().getRegExpYr());
					}
					else
					{
						return CommonConstant.STR_SPACE_EMPTY;
					}
				}
			}
			//else if (aiColumn == 6)
			else if (aiColumn == CommonConstant.CTL005_COL_OWNRNAME)
			{
				// defect 10112
				return (laMFVehData.getOwnerData().getName1());
				// end defect 10112 
			}
			// end defect 9953
		}
		return CommonConstant.STR_SPACE_EMPTY;
	}
}
