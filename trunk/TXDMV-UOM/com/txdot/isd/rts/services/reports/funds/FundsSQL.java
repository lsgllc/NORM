package com.txdot.isd.rts.services.reports.funds;

import java.util.Vector;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.data.EmployeeData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * FundsSQL.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * S Johnston	05/10/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify
 *							defect 7896 Ver 5.2.3
 * K Harrell	08/07/2008	Only include cashdrawer in SQL where
 * 							timestamps are null, else null pointer.
 * 							modify genFundsSQL() 
 * 							defect 8913 Ver MyPlates_POS 
 * K Harrell	10/27/2008	Add qualifier to TransTimestmp as SQL 
 * 							 components to be used w/  
 * 							 RTS_DSABLD_PLCRD_TRANS 
 * 							defect 9831 Ver Defect_POS_B 
 * ---------------------------------------------------------------------
 */
/**
 * Class is used to generate SQL for the Funds Reports.  Takes
 * the FundsData information, and creates dynamic SQL based
 * on the different permutations.
 *  
 * @version		Defect_POS_B	10/27/2008  
 * @author		Bobby Tulsiani
 * <br>Creation Date:			10/04/2001 10:49:13
 */
public class FundsSQL
{
	/**
	 * Util constructor
	 */
	public FundsSQL()
	{
		super();
	}

	/**
	 * Method uses user inputted selection criteria from FundsData to
	 * build SQL statements
	 * 
	 * @param aaFundsData FundsData
	 * @return FundsSQLData
	 */
	public FundsSQLData genFundsSQL(FundsData aaFundsData)
	{
		FundsSQLData aaFundsSQLData = new FundsSQLData();
		String lsSelect = "";
		String lsList = "";
		boolean lbFirstElement = true;

		//Build SQL based on if employee or cashdrawer
		if (aaFundsData.getFundsReportData().getEntity()
			== FundsConstant.EMPLOYEE)
		{
			lsSelect = "H.TransEmpId";
			if (aaFundsData.getFundsReportData().getPrimarySplit()
				== FundsConstant.CASH_DRAWER)
			{
				lsSelect = lsSelect + ", H.CashWsId";
			}
		}
		else
		{
			lsSelect = "H.CashWsId";
			if (aaFundsData.getFundsReportData().getPrimarySplit()
				== FundsConstant.EMPLOYEE)
			{
				lsSelect = lsSelect + ", H.TransEmpId";
			}
		}

		if (aaFundsData.getFundsReportData().getEntity()
			== FundsConstant.EMPLOYEE)
		{
			lsList = "H.TRANSEMPID in (";
			for (int i = 0;
				i < aaFundsData.getSelectedEmployees().size();
				i++)
			{
				if (i != 0)
				{
					lsList = lsList + ",";
				}
				lsList =
					lsList
						+ "'"
						+ ((EmployeeData) aaFundsData
							.getSelectedEmployees()
							.get(i))
							.getEmployeeId()
						+ "'";
			}
			lsList = lsList + ") AND ";
		}

		//Build SQL based on date range for each entity
		if (aaFundsData.getFundsReportData().getRange()
			!= FundsConstant.DATE_RANGE)
		{
			lsList = lsList + " (";
			for (int i = 0;
				i < aaFundsData.getSelectedCashDrawers().size();
				i++)
			{
				Vector lvCashDrawers =
					aaFundsData.getSelectedCashDrawers();
				CashWorkstationCloseOutData laFRData =
					(CashWorkstationCloseOutData) lvCashDrawers.get(i);

				// defect 8913 	
				// Only run where timestamps are assigned 
				if ((laFRData.getRptStatus() != null
					&& (laFRData.getRptStatus().equals("")
						|| laFRData.getRptStatus().equals(
							FundsConstant.CLOSE_COMPLETE)))
					&& laFRData.getCloseOutBegTstmp() != null
					&& laFRData.getCloseOutEndTstmp() != null)
				{
					if (i != 0 && !lbFirstElement)
					{
						lsList = lsList + " OR ";
					}
					lsList =
						lsList
							+ "(H.CASHWSID = "
							+ laFRData.getCashWsId()
							+ " AND H.TRANSTIMESTMP BETWEEN '"
							+ laFRData.getCloseOutBegTstmp().getDB2Date()
							+ "' AND '"
							+ laFRData.getCloseOutEndTstmp().getDB2Date()
							+ "') ";
					lbFirstElement = false;
				}
				// end defect 8913 
			}
			lsList = lsList + ") AND ";
		}

		//Else build SQL for same date range on each entity
		else
		{
			// defect 9831 
			// add qualifier "H" for Transtimestmp 
			lsList =
				lsList
					+ "H.TRANSTIMESTMP BETWEEN '"
					+ aaFundsData
						.getFundsReportData()
						.getFromRange()
						.getDB2Date()
					+ "' AND '"
					+ aaFundsData
						.getFundsReportData()
						.getToRange()
						.getDB2Date()
					+ "' AND ";
			// end defect 9831 

			if (aaFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER
				&& aaFundsData.isAllCashDrawers() == false)
			{
				lsList = lsList + " H.CASHWSID IN ( ";
				for (int i = 0;
					i < aaFundsData.getSelectedCashDrawers().size();
					i++)
				{
					Vector lvCashDrawers =
						aaFundsData.getSelectedCashDrawers();
					CashWorkstationCloseOutData laFRData =
						(
							CashWorkstationCloseOutData) lvCashDrawers
								.get(
							i);
					if (laFRData.getRptStatus().equals("")
						|| laFRData.getRptStatus().equals(
							"Close Out Complete"))
					{
						if (i != 0 && !lbFirstElement)
						{
							lsList = lsList + ", ";
						}
						lsList = lsList + laFRData.getCashWsId();
						lbFirstElement = false;
					}
				}
				lsList = lsList + ") AND ";
			}
		}
		aaFundsSQLData.setList(lsList);
		aaFundsSQLData.setSelect(lsSelect);
		return aaFundsSQLData;
	}
}
