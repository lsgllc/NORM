package com.txdot.isd.rts.client.reports.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * TMRPR002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	05/31/2005	Added parameters to JavaDoc, <p> now <br>
 * 							modify getValueAt()
 *							defect 7896 Ver 5.2.3
 * B Hargrove	09/28/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify  
 *							defect 7896 Ver 5.2.3
 * K Harrell	01/06/2009	Pad WsId. Report Number w/ leading "0"
 * 							add RPT_DESC,RPT_NO,WSID,DATE,TIME,SAMEOBJ,
 * 							 MAX_RPT_NO_LNGTH, MAX_WSID_LNGTH
 * 							delete DFLT_WSID0, addRevisedObj()
 * 							modify getValueAt()
 * 							defect 7124	Ver Defect_POS_D
 * K Harrell	06/15/2009	Implement ReportConstant 
 * 							delete RPT_DESC,RPT_NO,WSID,DATE,TIME,SAMEOBJ,
 * 							 MAX_RPT_NO_LNGTH, MAX_WSID_LNGTH
 * 							modify getValueAt()
 * 							defect 10086 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */
/**
 * Table model for screen RPR002.  Sets the column count and names
 * as well as inserting rows into the table.
 * 
 * @version	Defect_POS_F	06/15/2009
 * @author	Sunil Govindappa
 * <br>Creation Date:		06/22/2001 13:47:40
 */
public class TMRPR002 extends AbstractTableModel
{
	private Vector cvRevisedObjects = null;
	private Vector cvVector;
	private final static String[] carrColumn_Name =
		{ "Report Description", "Rpt#", "WsId", "Date", "Time" };

	// defect 10086
	//	private static final int RPT_DESC = 0;
	//	private static final int RPT_NO = 1;
	//	private static final int WSID = 2;
	//	private static final int DATE = 3;
	//	private static final int TIME = 4;
	//	private static final int SAMEOBJ = 99;
	// end defect 10086
	
	private static final int MAX_RPT_NO_LNGTH = 4;
	private static final int MAX_WSID_LNGTH = 3;

	/**
	 * TMRPR002 constructor
	 */
	public TMRPR002()
	{
		super();
		cvVector = new Vector();
		cvRevisedObjects = new Vector();
	}

	/**
	 * Add a vector to the table to post rows.
	 * 
	 * @param avVector Vector
	 */
	public void add(Vector avVector)
	{
		cvVector = new Vector(avVector);
		fireTableDataChanged();
	}

	/**
	 * Specify the number of columns in table.
	 * 
	 * @return int
	 */
	public int getColumnCount()
	{
		return carrColumn_Name.length;
	}

	/**
	 * getColumnName
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
	 * Return the number of rows in the table
	 * 
	 * @return int
	 */
	public int getRowCount()
	{
		return cvVector.size();
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
		// defect 7124 
		// Reorganize code;  Pad WsId and RptNo w/ leading "0" 
		ReprintReportsData laDataObj =
			(ReprintReportsData) cvVector.get(aiRow);

		Object laReturn = CommonConstant.STR_SPACE_EMPTY;

		switch (aiColumn)
		{
			// defect 10086
			// Use ReportConstant 
			case (ReportConstant.RPR002_COL_RPT_DESC) :
				{
					laReturn =
						CommonConstant.STR_SPACE_TWO
							+ laDataObj.getRptDesc();
					break;
				}
			case (ReportConstant.RPR002_COL_RPT_NO) :
				{
					if (laDataObj.getRptNo() == -1
						|| laDataObj.getRptNo() == 0)
					{
						laReturn = CommonConstant.STR_SPACE_EMPTY;
					}
					else
					{
						String lsRptNo = "" + laDataObj.getRptNo();
						laReturn =
							UtilityMethods.addPadding(
								lsRptNo,
								MAX_RPT_NO_LNGTH,
								"0");
					}
					break;
				}
			case (ReportConstant.RPR002_COL_RPT_WSID) :
				{
					//if WsId not set
					if (laDataObj.getWsId() == -1)
					{
						laReturn = CommonConstant.STR_SPACE_EMPTY;
					}
					else
					{
						String lsWsId = "" + laDataObj.getWsId();
						laReturn =
							UtilityMethods.addPadding(
								lsWsId,
								MAX_WSID_LNGTH,
								"0");
					}
					break;
				}
			case (ReportConstant.RPR002_COL_RPT_DATE) :
				{
					laReturn = laDataObj.getDate();
					break;
				}
			case (ReportConstant.RPR002_COL_RPT_TIME) :
				{
					laReturn = laDataObj.getTime();
					break;
				}
			case (ReportConstant.RPR002_COL_SAME_OBJECT) :
				{
					laReturn = laDataObj;
					break;
				}
				// end defect 10086
		}
		return laReturn;
		// end defect 7124 
	}
}