package com.txdot.isd.rts.server.specialplates;

import java.util.Vector;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.data.SpecialPlateUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.reports.GenSpclPltApplicationReport;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.AdministrationLog;
import com.txdot.isd.rts.server.db.SpecialPlatePermit;
import com.txdot.isd.rts.server.db.SpecialPlateTransactionHistory;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;

/*
 * SpecialPlatesServerBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/15/2007  Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/17/2007	Update for Special Plate Report
 * 							add genSpclPltApplReport()
 * 							defect 9085 Ver Special Plates
 * T Pederson	03/26/2007	make special plate appl report landscape 
 * 							defect 9123 Ver Special Plates
 * K Harrell	05/09/2007	Update for modified constants
 * 							modify genSpclPltApplReport()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/17/2009	Implement ReportSearchData constructor.
 * 							Implement common call to initialize 
 * 							Report Properties. 
 * 							modify genSpclPltApplReport()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	12/26/2010	modify processData()
 * 							defect 10700 Ver 6..70 
 * ---------------------------------------------------------------------
 */

/**
 * The SpecialPlatesServerBusiness dispatch the incoming request to
 * the function request on the server side for Special Plates events.  
 * It also returns the result back to the caller
 *
 * @version	6.7.0		 	12/26/2010
 * @author 	Kathy Harrell
 * <br>Creation Date:		02/15/2007	17:51:00 
 */
public class SpecialPlatesServerBusiness
{
	/**
	 * SpecialPlatesServerBusiness constructor
	 */
	public SpecialPlatesServerBusiness()
	{
		super();
	}

	/**
	 * Used to call the private methods of the Special Plates 
	 * server business.
	 *
	 * @param aiModule int 
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			// defect 10700 
			case SpecialPlatesConstant.GET_DUPL_INSIG :
				{
					return getSpclPltDuplPrmt(aaData);
				}
				// end defect 10700 

			case SpecialPlatesConstant.GENERATE_SPCL_PLT_APPL_REPORT :
				{
					return genSpclPltApplReport(aaData);
				}
		}
		return null;
	}

	/**
	 * Return Special Plate Duplicate Insignia (Permit)  
	 * 
	 * @param aaData
	 * @return Vector 
	 * @throws RTSException
	 */
	private Object getSpclPltDuplPrmt(Object aaData)
		throws RTSException
	{
		String lsPltNo = (String) aaData;

		Vector lvReturn = new Vector();
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN 
			SpecialPlatePermit laSpclPltPrmt =
				new SpecialPlatePermit(laDBAccess);

			laDBAccess.beginTransaction();
			lvReturn = laSpclPltPrmt.qrySpecialPlatePermit(lsPltNo);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		return lvReturn;

	}

	/**
	 * genSpclPltApplReport
	 * 
	 * @param aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object genSpclPltApplReport(Object aaData)
		throws RTSException
	{
		// defect 8628 
		Vector lvSpclPltData = (Vector) aaData;

		SpecialPlateUIData laSpclPltUIData =
			(SpecialPlateUIData) lvSpclPltData.get(0);

		AdministrationLogData laLogData =
			(AdministrationLogData) lvSpclPltData.get(1);

		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			// UOW #1 BEGIN 
			AdministrationLog laAdminLog =
				new AdministrationLog(laDBAccess);
			laDBAccess.beginTransaction();
			laAdminLog.insAdministrationLog(laLogData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// UOW #2 BEGIN 
			ReportsServerBusiness laRptSrvrBus =
				new ReportsServerBusiness();
			ReportProperties laRptProps =
				laRptSrvrBus.initReportProperties(
					laLogData.getReportSearchData(),
					laDBAccess,
					ReportConstant.RPT_6001_REPORT_ID);
			// UOW #2 END
			laRptProps.setPageOrientation(ReportConstant.LANDSCAPE);

			// UOW #3 BEGIN
			SpecialPlateTransactionHistory laSpclPltTransHstry =
				new SpecialPlateTransactionHistory(laDBAccess);
			laDBAccess.beginTransaction();
			Vector lvRptData =
				laSpclPltTransHstry.qrySpecialPlateApplicationReport(
					laSpclPltUIData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END 

			GenSpclPltApplicationReport laGrp =
				new GenSpclPltApplicationReport(
					ReportConstant.RPT_6001_ONLN_REPORT_TITLE,
					laRptProps,
					laSpclPltUIData);

			laGrp.formatReport(lvRptData);

			// RETURN REPORT DATA/ATTRIBUTES 
			return new ReportSearchData(
				laGrp.getFormattedReport().toString(),
				ReportConstant.RPT_6001_ONLN_FILENAME,
				ReportConstant.RPT_6001_ONLN_REPORT_TITLE,
				ReportConstant.RPT_1_COPY,
				ReportConstant.LANDSCAPE);
			// end defect 8628  
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}
}
