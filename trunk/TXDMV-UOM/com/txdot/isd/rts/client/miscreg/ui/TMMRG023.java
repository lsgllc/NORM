package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.data.AccountCodesData;
import com.txdot.isd.rts.services.data.DisabledPlacardCustomerData;
import com.txdot.isd.rts.services.data.DisabledPlacardData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;

/*
 * TMMRG023.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/04/2008	Present Term and Type according to Placard
 * 							vs. Customer
 * 							modify getValueAt()
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	07/25/2009	Refactored from TMMRG023A
 * 							Implement HB 3095 - Delete "Type", Retitle
 * 							"Term" as "Type" 
 * 							delete PLACARD, DESCRIPTION, ISSUE_DATE,
 *							 EXP_DATE, TERM, TYPE
 * 							modify carrColumn_Name
 * 							modify getValueAt() 
 * 							defect 10133 Ver Defect_POS_F      
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for FrmAssignedDisabledPlacardsMRG023
 *
 * @version	POS_Defect_B	07/25/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */

public class TMMRG023 extends javax.swing.table.AbstractTableModel
{
	private Vector cvTableMdlVector;

	// defect 10133 
	private final static String[] carrColumn_Name =
		{ "Placard", "Description", "Issue Date", "Exp Date", "Type" };
	// end defect 10133 

	private static final String SPACE = " ";

	/**
	 * TMMRG023 constructor comment.
	 */
	public TMMRG023()
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
		DisabledPlacardCustomerData laDataObj =
			(DisabledPlacardCustomerData) cvTableMdlVector.get(aiRow);

		Vector lvVector = laDataObj.getDsabldPlcrd();
		String lsResult = "";

		if (lvVector != null && lvVector.size() > 0)
		{
			DisabledPlacardData laPlcrdData =
				(DisabledPlacardData) lvVector.elementAt(0);

			// defect 10133 
			//	boolean lbMobility =
			//		laPlcrdData.getInvItmCd().equals(
			//			MiscellaneousRegConstant.MOBLTY_PLCRD_ITMCD);
			//
			// 	boolean lbPerm =		
			//	laPlcrdData.getAcctItmCd().equals(AcctCdConstant.BPM)
			//		|| laPlcrdData.getAcctItmCd().equals(
			//			AcctCdConstant.RPNM);

			switch (aiColumn)
			{
				case (MiscellaneousRegConstant.MRG023_COL_PLACARD) :
					{

						lsResult = SPACE + laPlcrdData.getInvItmNo();
						break;
					}
				case (
					MiscellaneousRegConstant.MRG023_COL_DESCRIPTION) :
					{
						AccountCodesData laAcctCdData =
							AccountCodesCache.getAcctCd(
								laPlcrdData.getAcctItmCd(),
								new RTSDate().getYYYYMMDDDate());
						if (laAcctCdData != null)
						{
							lsResult =
								SPACE + laAcctCdData.getAcctItmCdDesc();
						}
						break;
					}
				case (MiscellaneousRegConstant.MRG023_COL_ISSUE_DATE) :
					{
						String lsIssueDate =
							(new RTSDate(RTSDate.YYYYMMDD,
								laPlcrdData.getRTSEffDate()))
								.toString();
						lsResult = lsIssueDate;
						break;
					}
				case (MiscellaneousRegConstant.MRG023_COL_EXP_DATE) :
					{
						lsResult =
							UtilityMethods.addPadding(
								new Integer(laPlcrdData.getRTSExpMo())
									.toString(),
								2,
								"0")
								+ laPlcrdData.getRTSExpYr();
						lsResult =
							lsResult.substring(0, 2)
								+ "/"
								+ lsResult.substring(2);
						break;
					}
				case (MiscellaneousRegConstant.MRG023_COL_TYPE) :
					{
						lsResult =
							laPlcrdData.isPermanent()
								? MiscellaneousRegConstant
									.PERMANENT_MNEMONIC
								: MiscellaneousRegConstant
									.TEMPORARY_MNEMONIC;
						break;
					}
					// case (TYPE) :
					//		{
					//			lsResult =
					//				lbMobility
					//					? MiscellaneousRegConstant
					//						.MOBILITY_MNEMONIC
					//					: MiscellaneousRegConstant
					//						.NONMOBILITY_MNEMONIC;
					//			break;
					//		}
			}
			// end defect 10133
		}
		return lsResult;
	}
}