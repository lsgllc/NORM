package com.txdot.isd.rts.client.miscreg.ui;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.DisabledPlacardCustomerData;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;

/*
 * TMMRG021.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
  * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created.
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/04/2008	Due to changed size, reduce portion of 
 * 							address presented.
 * 							modify getValueAt()
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	07/26/2009	Implement HB 3095 - Delete "Type", Retitle
 * 							"Term" as "Type"
 * 							delete CUSTID, NAME, ADDRESS, TERM, TYPE,
 * 							 PLACARDS
 * 							modify carrColumn_Name
 * 							modify getValueAt() 
 * 							defect 10133 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for FrmDisabledPlacardSearchResultsMRG021 
 *
 * @version	Defect_POS_F  	07/26/2009
 * @author	Kathy Harrell	
 * <br>Creation Date:		10/21/2008
 */
public class TMMRG021 extends javax.swing.table.AbstractTableModel
{
	private Vector cvTableMdlVector;

	// defect 10133 
	// Remove "Term",
	private final static String[] carrColumn_Name =
		{
			"Id",
			"Disabled PersonOld / Institution Name",
			"Address",
			"Type",
			"Placards" };
	// end defect 10133 

	private static final String SPACE = " ";

	/**
	 * TMMRG021 constructor comment.
	 */
	public TMMRG021()
	{
		super();
		cvTableMdlVector = new Vector();
	}

	/**
	 * Add a cvTableMdlVector to the table to post rows.
	 * 
	 * @param avAddVector Vector
	 */
	public void add(Vector avAddVector)
	{
		cvTableMdlVector = new Vector(avAddVector);
		fireTableDataChanged();
	}

	/**
	 * Specify the number of columns in table.
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}

	/**
	 * Returns the column name.
	 * 
	 * @param  aiCol	int
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
	 * Return the number of rows in the Table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvTableMdlVector.size();
	}

	/**
	 * Return values from the table
	 * 
	 * @param aiRow int
	 * @param aiColumn int
	 * @return Object
	 */
	public Object getValueAt(int aiRow, int aiColumn)
	{
		DisabledPlacardCustomerData aaDPCustData =
			(DisabledPlacardCustomerData) cvTableMdlVector.get(aiRow);
		String lsReturn = SPACE;

		switch (aiColumn)
		{
			// defect 10133 
			// Implement constants, remove assignment of Mobility 
			//  column 
			case (MiscellaneousRegConstant.MRG021_COL_CUSTID) :
				{
					lsReturn = SPACE + aaDPCustData.getCustId();
					break;
				}
			case (MiscellaneousRegConstant.MRG021_COL_NAME) :
				{
					lsReturn = SPACE + aaDPCustData.getOwnerName();
					break;
				}
			case (MiscellaneousRegConstant.MRG021_COL_ADDRESS) :
				{
					AddressData laAddressData =
						aaDPCustData.getAddressData();

					String lsAddress =
						laAddressData.getSt1().trim()
							+ ", "
							+ laAddressData.getCity()
							+ ", "
							+ laAddressData.getStateCntry();
					lsReturn = SPACE + lsAddress;
					break;
				}
				// Use "Type" to reference Permanent vs. Temporary 
				//  vs. "Term" in 6.0.0 and prior 
			case (MiscellaneousRegConstant.MRG021_COL_TYPE) :
				{
					lsReturn = 
						aaDPCustData.isPermDsabld()
							? MiscellaneousRegConstant.PERMANENT_MNEMONIC
							: MiscellaneousRegConstant.TEMPORARY_MNEMONIC;
					break;
				}
				
				//	case (TYPE) :
				//		{
				//			String lsMobility =
				//				aaDPCustData.getMobltyDsabldIndi() == 1
				//					? MiscellaneousRegConstant.MOBILITY_MNEMONIC
				//					: MiscellaneousRegConstant
				//						.NONMOBILITY_MNEMONIC;
				//			lsReturn = lsMobility;
				//			break;
				//		}
				
			case (MiscellaneousRegConstant.MRG021_COL_PLACARDS) :
				{
					int liCount = 0;
					if (aaDPCustData.getDsabldPlcrd() != null)
					{
						liCount =
							((Vector) aaDPCustData.getDsabldPlcrd())
								.size();
					}
					lsReturn = liCount + "";
					break;
				}
				// end defect 10133 
		}
		return lsReturn;
	}
}