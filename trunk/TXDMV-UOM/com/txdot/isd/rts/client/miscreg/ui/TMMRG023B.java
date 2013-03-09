package com.txdot.isd.rts.client.miscreg.ui;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.DisabledPlacardDeleteReasonCache;
import com.txdot.isd.rts.services.data.AccountCodesData;
import com.txdot.isd.rts.services.data.DisabledPlacardCustomerData;
import com.txdot.isd.rts.services.data.DisabledPlacardData;
import com.txdot.isd.rts.services.data.DisabledPlacardDeleteReasonData;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;

/*
 * TMMRG023B.java
 *
 * (c) Texas Department of Motor Vehicles 2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/11/2012	Created
 * 							defect 11214 Ver 6.10.0 
 * K Harrell	02/23/2012	Set "EXPIRED" for Delete Reason on Expired 
 * 							  Placards if not actually deleted  
 * 							modify getValueAt() 
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Table Model for FrmAssignedDisabledPlacardsMRG023 (Deleted/Expired) 
 *
 * @version	6.10.0		02/23/2012
 * @author	Kathy Harrell
 * @since				01/11/2012 
 */

public class TMMRG023B extends javax.swing.table.AbstractTableModel
{
	private Vector cvTableMdlVector;

	private final static String[] carrColumn_Name =
		{ "Placard", "Description", "Issue Date", "Exp Date", "Type",
		  "Delete Reason                                  " };

	private static final String SPACE = " ";

	/**
	 * TMMRG023B constructor comment.
	 */
	public TMMRG023B()
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
				case (MiscellaneousRegConstant.MRG023_COL_DELETE_REASON) :
				{
					DisabledPlacardDeleteReasonData laData  = DisabledPlacardDeleteReasonCache.getDsabldPlcrdDelReasn(laPlcrdData.getDelReasnCd());
					if (laData != null)
					{
						lsResult = laData.getDelReasnDesc();
					}
					else
					{
						lsResult = laPlcrdData .isExpired() ? "EXPIRED" : "VOLUNTARILY SURRENDERED"; 
					}
					break;
				}
			}
		}
		return lsResult;
	}
}

