package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.data.InventoryHistoryUIData;
import com.txdot.isd.rts.services.data.InventoryInquiryUIData;
import com.txdot.isd.rts.services.data.InventoryPatternsDescriptionData;
import com.txdot.isd.rts.services.data.TransactionInventoryDetailData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 * InventoryReportsSQL.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/17/2002	Change SQL to handle Specific Items
 * Ray Rowehl				on Inventory Inquiry
 * K Harrell    05/29/2002  Defect 4147 - Alter "INVVD" to "VOID"
 * K Harrell    01/27/2003  Do not exclude INVOFC, etc. in Inquiry
 * 							History Report 
 *							modify qryInventoryInquiryHistoryReport()
 *							defect 5195 
 * K Harrell	05/07/2004	call Utilitymethods.quote to replace single
 *							quote with 2 single quotes when passing
 *							empid
 *							modify qryInventoryInquiryCurrentBalanceR~()
 *							modify qryInventoryInquiryHistoryReport()
 *							formatted, added heading info for all
 *							methods
 *							defect 7000 Ver 5.2.0 
 * S Johnston	03/14/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify qryInventoryDeleteHistoryReport()
 * 							qryInventoryInquiryHistoryReport()
 * 							qryInventoryProfileReport()
 *							defect 7896 Ver 5.2.3
 * K Harrell	05/19/2005	Renaming of InventoryHistoryUIData elements
 * 							defect 7899 Ver 5.2.3 
 * Ray Rowehl	09/30/2005	Moved to server.db since this is a SQL class.
 * 							Note that this is first SQL class to make
 * 							heavy use of constants.  Lets review this.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	01/12/2006	Removed space from SQL for n!=1 INVID
 * 							modify qryInventoryInquiryCurrentBalanceReport()
 * 							defect 7890 Ver 5.2.3 
 * K Harrell	07/12/2007	Modified order by for SQL
 * 							renamed and referenced constants accordingly
 * 							modify qryInventoryInquiryCurrentBalanceReport(),
 * 									 qryInventoryInquiryHistoryReport()
 * 							defect 9085 Ver Special Plates
 * Min Wang		05/21/2008	on the current balance, some of the plates 
 * 							were not listed in order.
 * 							add SQL_ORDER_BY_INVID_ITMCDDESC_ITMYR_ITMNO
 * 							    SQL_ORDER_BY_ITMCDDESC_ITMYR_ITMNO
 * 							modify qryInventoryInquiryCurrentBalanceReport()
 * 							defect 9494 Ver Defect_POS_A
 * K Harrell	08/29/2008	Modify to build SQL based upon 
 * 							 isAllItmsSelected()
 * 							modify qryInventoryDeleteHistoryReport(),
 * 							 qryInventoryInquiryCurrentBalanceReport(),
 * 							 qryInventoryInquiryHistoryReport(),
 *						 	 qryInventoryReceiveHistoryReport()
 *							defect 9706 Ver Defect_POS_B  
 * K Harrell	10/12/2009	Select for all Selected Offices
 * 							add getOffices() 
 * 							add qryInventoryReceiveHistoryReport(InventoryHistoryUIData), 
 * 							 qryInventoryDeleteHistoryReport(InventoryHistoryUIData) 
 * 							delete qryInventoryReceiveHistoryReport(Vector), 
 * 							 qryInventoryDeleteHistoryReport(Vector)
 * 							delete TX_INVCNO, TXT_ITMYR, 
 * 							 TXT_TRANSAMDATE_GT_EQUAL, 
 * 							 TXT_TRANSAMDATE_LESS_EQUAL
 * 							modify qryInventoryHistoryLog()
 * 							modify SQL_INV_HIST, SQL_INV_HSTRY_1, 
 * 							 SQL_INV_HIST_2, SQL_INV_HSTRY_2 
 * 							defect 10207 Ver Defect_POS_G
 * K Harrell	03/19/2010	Remove unused method, associated constants.
 * 							delete qryInventoryProfileReport()
 * 							defect 10239 Ver POS_640    
 * ---------------------------------------------------------------------
 */

/**
 * SQL for Inventory Reports
 *
 * @version	POS_640		 	03/19/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/25/2001 09:49:48
 */

public class InventoryReportsSQL
{
	// defect 10239 
	//	private static final String COL_CHNGTIMESTMP = "ChngTimestmp";
	//	private static final String COL_DELETEINDI = "DeleteIndi";
	//	private static final String COL_ENTITY = "Entity";
	//	private static final String COL_ID = "Id";
	//	private static final String COL_NEXTAVAILINDI = "NextAvailIndi";
	//		private static final String COL_MAXQTY = "MaxQty";
	//		private static final String COL_MINQTY = "MinQty";
	//	private static final String SQL_AND_ENTITY = "and Entity = ?";
	//	private static final String SQL_AND_ID = "and Id = ?";
	//	private static final String SQL_AND_INVITMYR = "and InvItmYr = ?";
	//		private static final String SQL_AND_ITMCD = "and A.ItmCd = ?";
	//	private static final String SQL_INV_PROFILE =
	//			"SELECT OfcIssuanceNo,SubstaId,Entity,Id,A.ItmCd,"
	//				+ "B.ItmCdDesc,InvItmYr,MaxQty,MinQty,BranchOfcId,"
	//				+ "NextAvailIndi,DeleteIndi,ChngTimestmp "
	//				+ "FROM RTS.RTS_INV_PROFILE A, RTS.RTS_ITEM_CODES B "
	//				+ "where A.ITMCD = B.ITMCD "
	//				+ "AND DeleteIndi = 0 and OfcIssuanceNo = ? and SubstaId = ? ";
	//	private static final String SQL_ORDER_BY_ENTITY_ID_ITMCDDESC_INVITMYR =
	//			" order by Entity,Id,ItmCdDesc,InvitmYr ";
	//	private static final String MSG_QRY_IPR_BEGIN =
	//		" - qryInventoryProfileReport - Begin";
	//	private static final String MSG_QRY_IPR_END =
	//		" - qryInventoryProfileReport - End ";
	//	private static final String MSG_QRY_IPR_EXCEPTION =
	//		" - qryInventoryProfileReport - Exception ";
	//	private static final String MSG_QRY_IPR_SQL_BEGIN =
	//		" - qryInventoryProfileReport - SQL - Begin";
	//	private static final String MSG_QRY_IPR_SQL_END =
	//		" - qryInventoryProfileReport - SQL - End";
	// end defect 10239 

	private static final String COL_DELINVREASNTXT = "DelInvReasnTxt";
	private static final String COL_DELINVDEASN = "DelInvReasn";
	private static final String COL_INVENDNO = "InvEndNo";
	private static final String COL_INVCCORRINDI = "InvcCorrIndi";
	private static final String COL_INVCNO = "InvcNo";
	private static final String COL_INVID = "InvId";
	private static final String COL_INVITMNO = "InvItmNo";
	private static final String COL_INVITMYR = "InvItmYr";
	private static final String COL_ITMCD = "ItmCd";
	private static final String COL_ITMCDDESC = "ItmCdDesc";
	private static final String COL_INVLOCIDCD = "InvLocIdCd";
	private static final String COL_INVSTATUSCD = "InvStatusCd";
	private static final String COL_INVITMENDNO = "InvItmEndNo";
	private static final String COL_OFCISSUANCENO = "OfcIssuanceNo";
	private static final String COL_INVQTY = "InvQty";

	private static final String COL_PATRNSEQNO = "PatrnSeqNo";
	private static final String COL_SUBSTAID = "SubStaId";
	private static final String COL_TRANSAMDATE = "TransAMDate";
	private static final String COL_TRANSCD = "TransCd";
	private static final String COL_TRANSEMPID = "TransEmpId";
	private static final String COL_TRANSTIME = "TransTime";
	private static final String COL_TRANSWSID = "TransWsId";

	private static final String SQL_AND_INVID_IN = " AND A.InvId IN ";
	private static final String SQL_AND_INVITMNO_AND_INVENDNO =
		"AND INVITMNO = ? AND INVENDNO = ?";
	private static final String SQL_AND_TRANSAMDATE_BETWEEN =
		" AND A.TRANSAMDATE BETWEEN ? AND ? ";

	private static final String SQL_INVALLOC_1 =
		" SELECT A.ITMCD, A.INVITMYR, A.INVITMNO, A.INVID, "
			+ "A.INVLOCIDCD, A.INVITMENDNO, A.INVQTY, A.INVSTATUSCD, "
			+ "B.ITMCDDESC,A.PATRNSEQNO FROM RTS.RTS_INV_ALLOCATION A, "
			+ "RTS.RTS_ITEM_CODES B WHERE a.OfcIssuanceNo = ? and "
			+ "a.SubstaId = ? and A.ITMCD = B.ITMCD ";

	private static final String SQL_AND_INVLOCIDCD_EQUAL =
		" AND A.InvLocIdCd = '";

	// defect 9085 
	// Modified Order by statements to use ItmCdDesc vs. ItmCd 
	private static final String SQL_AND_TRANSCD_NOT =
		" AND A.TRANSCD NOT IN ('INVRCV','INVOFC', 'INVALL') ORDER BY"
			+ " A.InvId, ItmCdDesc, A.InvItmYr ";

	private static final String SQL_INV_HIST =
		"SELECT OfcIssuanceNo,SubStaId,TransAMDate,TransWsId,"
			+ "TransTime,TransEmpId,TransCd,A.ItmCd,ItmCdDesc,InvItmYr,"
			+ "InvItmNo,InvEndNo,InvQty,InvId, C.DelInvReasn,A.DelInvReasnTxt"
			+ " FROM RTS.RTS_INV_HSTRY A, RTS.RTS_ITEM_CODES B, "
			+ "RTS.RTS_DELETE_REASONS C  "
			+ "WHERE  A.DelInvReasnCd = C.DelInvReasnCd and ";
	private static final String SQL_INV_HSTRY_1 =
		"SELECT OfcIssuanceNo,SubStaId,TransAMDate,TransWsId,"
			+ "TransTime,TransEmpId,TransCd,A.ItmCd,ItmCdDesc,InvItmYr,"
			+ "InvItmNo,InvEndNo,InvQty,InvcNo,InvcCorrIndi,InvId "
			+ "FROM RTS.RTS_INV_HSTRY A, RTS.RTS_ITEM_CODES B "
			+ "WHERE ";

	private static final String SQL_INV_HIST_2 =
		" TransCd = 'INVDEL' and  A.Itmcd = B.ItmCd "
			+ "ORDER BY OfcIssuanceNo, TransAMDate, SubStaId, ItmCdDesc, InvItmYr, InvItmNo ";

	private static final String SQL_INV_HSTRY_2 =
		" A.Itmcd = B.ItmCd AND TRANSCD = 'INVRCV' "
			+ "ORDER BY OfcIssuanceNo, TransAMDate, SubStaId, ItmCdDesc, InvItmYr, InvItmNo ";

	private static final String SQL_ORDER_BY_ITMCDDESC_ITMYR =
		" ORDER BY ItmCdDesc, A.InvItmYr ";

	private static final String SQL_ORDER_BY_INVID_ITMCDDESC_ITMYR_ITMNO =
		" ORDER BY A.InvId,ItmCdDesc, A.InvItmYr, A.InvItmNo ";

	private static final String SQL_ORDER_BY_ITMCDDESC_ITMYR_ITMNO =
		" ORDER BY ItmCdDesc, A.InvItmYr, A.InvItmNo ";

	private static final String SQL_PATRNSEQNO =
		" AND ? between PatrnSeqNo and (PatrnSeqNo+InvQty -1) and "
			+ "PatrnSeqCd = ? ";

	private static final String SQL_QUOTE_AND_ITMYR_EQUAL =
		"' and A.InvItmYr = ";

	private static final String SQL_TRINVDETAIL_1 =
		" SELECT A.TRANSCD, A.OFCISSUANCENO, A.TRANSWSID, "
			+ "A.TRANSAMDATE, A.TRANSTIME, A.ITMCD, A.INVITMYR, "
			+ "A.INVENDNO, A.INVQTY, A.INVITMNO, A.INVID, A.INVLOCIDCD, "
			+ "B.ITMCDDESC FROM RTS.RTS_TR_INV_DETAIL A, "
			+ "RTS.RTS_ITEM_CODES B, RTS.RTS_TRANS_HDR C "
			+ "WHERE A.OFCISSUANCENO = ? "
			+ "AND A.SUBSTAID = ? AND C.TRANSTIMESTMP IS NOT NULL "
			+ "AND A.OFCISSUANCENO = C.OFCISSUANCENO "
			+ "AND A.SUBSTAID = C.SUBSTAID AND A.TRANSAMDATE = C.TRANSAMDATE"
			+ " AND A.TRANSWSID = C.TRANSWSID AND A.CUSTSEQNO = C.CUSTSEQNO"
			+ " AND A.ITMCD = B.ITMCD ";

	private static final String MSG_QRY_IDHR_BEGIN =
		" - qryInventoryDeleteHistoryReport - Begin";
	private static final String MSG_QRY_IDHR_END =
		" - qryInventoryDeleteHistoryReport - End ";
	private static final String MSG_QRY_IDHR_EXCEPTION =
		" - qryInventoryDeleteHistoryReport - Exception ";
	private static final String MSG_QRY_IDHR_SQL_BEGIN =
		" - qryInventoryDeleteHistoryReport - SQL - Begin";
	private static final String MSG_QRY_IDHR_SQL_END =
		" - qryInventoryDeleteHistoryReport - SQL - End";
	private static final String MSG_QRY_IICBR_BEGIN =
		" - qryInventoryInquiryCurrentBalanceReport - Begin";
	private static final String MSG_QRY_IICBR_SQL_BEGIN =
		" - qryInventoryInquiryCurrentBalanceReport - SQL - Begin";
	private static final String MSG_QRY_IICBR_SQL_END =
		" - qryInventoryInquiryCurrentBalanceReport - SQL - End";
	private static final String MSG_QRY_IICBR_EXCEPTION =
		" - qryInventoryInquiryCurrentBalanceReport - Exception ";
	private static final String MSG_QRY_IICBR_END =
		" - qryInventoryInquiryCurrentBalanceReport - End ";
	private static final String MSG_QRY_IIHR_BEGIN =
		" - qryInventoryInquiryHistoryReport - Begin";
	private static final String MSG_QRY_IIHR_END =
		" - qryInventoryInquiryHistoryReport - End ";
	private static final String MSG_QRY_IIHR_EXCEPTION =
		" - qryInventoryInquiryHistoryReport - Exception ";
	private static final String MSG_QRY_IIHR_SQL_BEGIN =
		" - qryInventoryInquiryHistoryReport - SQL - Begin";
	private static final String MSG_QRY_IIHR_SQL_END =
		" - qryInventoryInquiryHistoryReport - SQL - End";

	private static final String MSG_QRY_IRHR_BEGIN =
		" - qryInventoryReceiveHistoryReport - Begin";
	private static final String MSG_QRY_IRHR_END =
		" - qryInventoryReceiveHistoryReport - End ";
	private static final String MSG_QRY_IRHR_EXCEPTION =
		" - qryInventoryReceiveHistoryReport - Exception ";
	private static final String MSG_QRY_IRHR_SQL_END =
		" - qryInventoryReceiveHistoryReport - SQL - End";
	private static final String MSG_QRY_IRHR_SQL_BEGIN =
		" - qryInventoryReceiveHistoryReport - SQL - Begin";

	private static final String TRANSCD_INVVD = "INVVD";
	private static final String TRANSCD_VOID = "VOID";

	private static final String TXT_AND = "AND";
	private static final String TXT_SPACE_AND_SPACE =
		CommonConstant.STR_SPACE_ONE
			+ TXT_AND
			+ CommonConstant.STR_SPACE_ONE;

	private static final String TXT_ITMCD = "( A.ItmCd = '";
	private static final String TXT_SPACE_OR_SPACE = " OR ";

	DatabaseAccess caDA;

	/**
	 * InventoryReportsSQL Constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public InventoryReportsSQL(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to build list of offices for "IN" clause
	 *
	 * @return String
	 * @throws RTSException 
	 */
	public String getOffices(Vector avOffices)
	{
		String lsOffices = "(" + avOffices.get(0);

		for (int i = 1; i < avOffices.size(); i++)
		{
			lsOffices =
				lsOffices + CommonConstant.STR_COMMA + avOffices.get(i);

		}
		return lsOffices + ") ";
	}

	// defect 10239 
	//	/**
	//	 * qryInventoryDeleteHistoryReport
	//	 * 
	//	 * @param avInputVector Vector
	//	 * @return Vector
	//	 * @throws RTSException
	//	 */
	//	public Vector qryInventoryDeleteHistoryReport(Vector avInputVector)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, MSG_QRY_IDHR_BEGIN);
	//		StringBuffer lsQry = new StringBuffer();
	//		Vector lvResult = new Vector();
	//		String lsList = CommonConstant.STR_SPACE_EMPTY;
	//		String lsOfcIssuanceNo =
	//			(String) avInputVector.get(CommonConstant.ELEMENT_0);
	//		InventoryHistoryUIData laInvHistoryUIData =
	//			(InventoryHistoryUIData) avInputVector.get(
	//				CommonConstant.ELEMENT_1);
	//
	//		// defect 9706 
	//		// use isAllItmsSelected()(boolean) vs. getAllItmSelected() (int)
	//		if (!laInvHistoryUIData.isAllItmsSelected())
	//		{
	//			// end defect 9706 
	//			lsList =
	//				CommonConstant.STR_OPEN_PARENTHESES
	//					+ CommonConstant.STR_SPACE_ONE;
	//
	//			Vector lvInvItms =
	//				(Vector) laInvHistoryUIData.getInvItems();
	//
	//			for (int i = 0; i < lvInvItms.size(); i++)
	//			{
	//				if (i != 0)
	//				{
	//					lsList = lsList + TXT_SPACE_OR_SPACE;
	//				}
	//				InventoryPatternsDescriptionData laInvData =
	//					(InventoryPatternsDescriptionData) lvInvItms.get(i);
	//				lsList =
	//					lsList
	//						+ TXT_ITMCD
	//						+ laInvData.getItmCd()
	//						+ TXT_ITMYR
	//						+ laInvData.getInvItmYr()
	//						+ CommonConstant.STR_CLOSE_PARENTHESES
	//						+ CommonConstant.STR_SPACE_ONE;
	//			}
	//			lsList =
	//				lsList
	//					+ CommonConstant.STR_CLOSE_PARENTHESES
	//					+ CommonConstant.STR_SPACE_ONE
	//					+ TXT_AND
	//					+ CommonConstant.STR_SPACE_ONE;
	//		}
	//		lsList =
	//			lsList
	//				+ TXT_TRANSAMDATE_GT_EQUAL
	//				+ laInvHistoryUIData.getBeginDate().getAMDate()
	//				+ CommonConstant.STR_SPACE_ONE
	//				+ TXT_AND
	//				+ CommonConstant.STR_SPACE_ONE
	//				+ TXT_TRANSAMDATE_LESS_EQUAL
	//				+ laInvHistoryUIData.getEndDate().getAMDate()
	//				+ CommonConstant.STR_SPACE_ONE
	//				+ CommonConstant.STR_CLOSE_PARENTHESES
	//				+ CommonConstant.STR_SPACE_ONE
	//				+ TXT_AND
	//				+ CommonConstant.STR_SPACE_ONE;
	//		ResultSet laResultSet;
	//		lsQry.append(
	//			SQL_INV_HIST
	//				+ lsOfcIssuanceNo
	//				+ CommonConstant.STR_SPACE_ONE
	//				+ TXT_AND
	//				+ CommonConstant.STR_SPACE_ONE
	//				+ lsList
	//				+ SQL_INV_HIST_2);
	//		try
	//		{
	//			Log.write(Log.SQL, this, MSG_QRY_IDHR_SQL_BEGIN);
	//			laResultSet = caDA.executeDBQuery(lsQry.toString(), null);
	//			Log.write(Log.SQL, this, MSG_QRY_IDHR_SQL_END);
	//			while (laResultSet.next())
	//			{
	//				InventoryDeleteHistoryReportData laInvDeleteHistoryRptData =
	//					new InventoryDeleteHistoryReportData();
	//				laInvDeleteHistoryRptData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(laResultSet, COL_OFCISSUANCENO));
	//				laInvDeleteHistoryRptData.setSubStaId(
	//					caDA.getIntFromDB(laResultSet, COL_SUBSTAID));
	//				laInvDeleteHistoryRptData.setTransAMDate(
	//					caDA.getIntFromDB(laResultSet, COL_TRANSAMDATE));
	//				laInvDeleteHistoryRptData.setTransWsId(
	//					caDA.getIntFromDB(laResultSet, COL_TRANSWSID));
	//				laInvDeleteHistoryRptData.setTransTime(
	//					caDA.getIntFromDB(laResultSet, COL_TRANSTIME));
	//				laInvDeleteHistoryRptData.setTransEmpId(
	//					caDA.getStringFromDB(laResultSet, COL_TRANSEMPID));
	//				laInvDeleteHistoryRptData.setTransCd(
	//					caDA.getStringFromDB(laResultSet, COL_TRANSCD));
	//				laInvDeleteHistoryRptData.setItmCd(
	//					caDA.getStringFromDB(laResultSet, COL_ITMCD));
	//				laInvDeleteHistoryRptData.setItmCdDesc(
	//					caDA.getStringFromDB(laResultSet, COL_ITMCDDESC));
	//				laInvDeleteHistoryRptData.setInvItmYr(
	//					caDA.getIntFromDB(laResultSet, COL_INVITMYR));
	//				laInvDeleteHistoryRptData.setInvItmNo(
	//					caDA.getStringFromDB(laResultSet, COL_INVITMNO));
	//				laInvDeleteHistoryRptData.setInvEndNo(
	//					caDA.getStringFromDB(laResultSet, COL_INVENDNO));
	//				laInvDeleteHistoryRptData.setInvQty(
	//					caDA.getIntFromDB(laResultSet, COL_INVQTY));
	//				laInvDeleteHistoryRptData.setInvId(
	//					caDA.getStringFromDB(laResultSet, COL_INVID));
	//				laInvDeleteHistoryRptData.setDelInvReasn(
	//					caDA.getStringFromDB(laResultSet, COL_DELINVDEASN));
	//				laInvDeleteHistoryRptData.setDelInvReasnTxt(
	//					caDA.getStringFromDB(
	//						laResultSet,
	//						COL_DELINVREASNTXT));
	//				// Add element to the Vector
	//				lvResult.addElement(laInvDeleteHistoryRptData);
	//			} //End of While 
	//			laResultSet.close();
	//			laResultSet = null;
	//			Log.write(Log.METHOD, this, MSG_QRY_IDHR_END);
	//			return (lvResult);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				MSG_QRY_IDHR_EXCEPTION + aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 10239 

	/**
	 * qryInventoryDeleteHistoryReport
	 * 
	 * @param avInputVector Vector
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryInventoryDeleteHistoryReport(InventoryHistoryUIData aaInvHstryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_QRY_IDHR_BEGIN);
		StringBuffer lsQry = new StringBuffer();
		Vector lvResult = new Vector();
		String lsList = CommonConstant.STR_SPACE_EMPTY;
		String lsOffices =
			getOffices(aaInvHstryData.getSelectedCounties());

		// defect 9706 
		// use isAllItmsSelected()(boolean) vs. getAllItmSelected() (int)
		if (!aaInvHstryData.isAllItmsSelected())
		{
			// end defect 9706 

			lsList = " ( ";

			Vector lvInvItms = (Vector) aaInvHstryData.getInvItems();

			for (int i = 0; i < lvInvItms.size(); i++)
			{
				if (i != 0)
				{
					lsList = lsList + " or ";
				}
				InventoryPatternsDescriptionData laInvData =
					(InventoryPatternsDescriptionData) lvInvItms.get(i);
				lsList =
					lsList
						+ "( A.ItmCd = '"
						+ laInvData.getItmCd()
						+ "' and A.InvItmYr = "
						+ laInvData.getInvItmYr()
						+ " ) ";
			}
			lsList = lsList + ") and ";
		}

		lsList =
			lsList
				+ " TransAMDate >= "
				+ aaInvHstryData.getBeginDate().getAMDate()
				+ " and "
				+ " TransAMDate <= "
				+ aaInvHstryData.getEndDate().getAMDate()
				+ " and ";

		ResultSet laResultSet;
		lsQry.append(
			SQL_INV_HIST
				+ lsList
				+ " OfcIssuanceNo in "
				+ lsOffices
				+ " and "
				+ SQL_INV_HIST_2);
		try
		{
			Log.write(Log.SQL, this, MSG_QRY_IDHR_SQL_BEGIN);
			laResultSet = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, MSG_QRY_IDHR_SQL_END);
			while (laResultSet.next())
			{
				InventoryDeleteHistoryReportData laInvDeleteHistoryRptData =
					new InventoryDeleteHistoryReportData();
				laInvDeleteHistoryRptData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, COL_OFCISSUANCENO));
				laInvDeleteHistoryRptData.setSubStaId(
					caDA.getIntFromDB(laResultSet, COL_SUBSTAID));
				laInvDeleteHistoryRptData.setTransAMDate(
					caDA.getIntFromDB(laResultSet, COL_TRANSAMDATE));
				laInvDeleteHistoryRptData.setTransWsId(
					caDA.getIntFromDB(laResultSet, COL_TRANSWSID));
				laInvDeleteHistoryRptData.setTransTime(
					caDA.getIntFromDB(laResultSet, COL_TRANSTIME));
				laInvDeleteHistoryRptData.setTransEmpId(
					caDA.getStringFromDB(laResultSet, COL_TRANSEMPID));
				laInvDeleteHistoryRptData.setTransCd(
					caDA.getStringFromDB(laResultSet, COL_TRANSCD));
				laInvDeleteHistoryRptData.setItmCd(
					caDA.getStringFromDB(laResultSet, COL_ITMCD));
				laInvDeleteHistoryRptData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, COL_ITMCDDESC));
				laInvDeleteHistoryRptData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, COL_INVITMYR));
				laInvDeleteHistoryRptData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, COL_INVITMNO));
				laInvDeleteHistoryRptData.setInvEndNo(
					caDA.getStringFromDB(laResultSet, COL_INVENDNO));
				laInvDeleteHistoryRptData.setInvQty(
					caDA.getIntFromDB(laResultSet, COL_INVQTY));
				laInvDeleteHistoryRptData.setInvId(
					caDA.getStringFromDB(laResultSet, COL_INVID));
				laInvDeleteHistoryRptData.setDelInvReasn(
					caDA.getStringFromDB(laResultSet, COL_DELINVDEASN));
				laInvDeleteHistoryRptData.setDelInvReasnTxt(
					caDA.getStringFromDB(
						laResultSet,
						COL_DELINVREASNTXT));
				// Add element to the Vector
				lvResult.addElement(laInvDeleteHistoryRptData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, MSG_QRY_IDHR_END);
			return (lvResult);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				MSG_QRY_IDHR_EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				MSG_QRY_IDHR_EXCEPTION + aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query Inventory Current Balance
	 * 
	 * @param aaInvInqData InventoryInquiryUIData
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qryInventoryInquiryCurrentBalanceReport(InventoryInquiryUIData aaInvInqData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_QRY_IICBR_BEGIN);
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		String lsInvLocIdCd = aaInvInqData.getInvLocIdCd();
		Vector lvInvId = aaInvInqData.getInvIds();
		String lsInvIds = new String(CommonConstant.STR_SPACE_EMPTY);
		// Convert single quote to 2 single quotes
		String lsInvId;
		if (lvInvId != null)
		{
			lsInvIds =
				CommonConstant.STR_OPEN_PARENTHESES
					+ CommonConstant.STR_SPACE_ONE;
			for (int i = 0; i < lvInvId.size(); i++)
			{
				lsInvId = (String) lvInvId.get(i);
				if (lsInvLocIdCd.equals(InventoryConstant.STR_E))
				{
					lsInvId = UtilityMethods.quote(lsInvId);
				}

				if (i == 0)
				{
					lsInvIds =
						lsInvIds
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SINGLE_QUOTE
							+ lsInvId
							+ CommonConstant.STR_SINGLE_QUOTE
							+ CommonConstant.STR_SPACE_ONE;
				}
				else
				{
					lsInvIds =
						lsInvIds
							+ CommonConstant.STR_COMMA
							+ CommonConstant.STR_SINGLE_QUOTE
							+ lsInvId
							+ CommonConstant.STR_SINGLE_QUOTE
							+ CommonConstant.STR_SPACE_ONE;
				}
			}
			lsInvIds =
				lsInvIds
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_CLOSE_PARENTHESES
					+ CommonConstant.STR_SPACE_ONE;
		}

		// defect 9706
		// Do not pass Inventory ItmCd and Year if Select All  
		String lsList = "";
		if (!aaInvInqData.isAllItmsSelected())
		{
			Vector lvItmCds = aaInvInqData.getItmCds();
			Vector lvInvItmYrs = aaInvInqData.getInvItmYrs();

			lsList =
				CommonConstant.STR_OPEN_PARENTHESES
					+ CommonConstant.STR_SPACE_ONE;
			for (int i = 0; i < lvItmCds.size(); i++)
			{
				if (i != 0)
					lsList = lsList + TXT_SPACE_OR_SPACE;
				lsList =
					lsList
						+ TXT_ITMCD
						+ lvItmCds.get(i)
						+ SQL_QUOTE_AND_ITMYR_EQUAL
						+ lvInvItmYrs.get(i)
						+ CommonConstant.STR_CLOSE_PARENTHESES
						+ CommonConstant.STR_SPACE_ONE;
			}
			lsList =
				TXT_SPACE_AND_SPACE
					+ lsList
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_CLOSE_PARENTHESES;
		}
		// end defect 9706

		ResultSet laResultSet;
		lsQry.append(SQL_INVALLOC_1);
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvInqData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvInqData.getSubstaId())));

		if (lvInvId != null)
		{
			lsQry.append(
				SQL_AND_INVLOCIDCD_EQUAL
					+ lsInvLocIdCd
					+ CommonConstant.STR_SINGLE_QUOTE
					+ CommonConstant.STR_SPACE_ONE);

			if (!aaInvInqData
				.getInvLocIdCd()
				.equals(InventoryConstant.CHAR_C))
				lsQry.append(SQL_AND_INVID_IN + lsInvIds);
		}
		if (aaInvInqData
			.getInvInqBy()
			.equals(InventoryConstant.SPECIFIC_ITM))
		{
			lsQry.append(SQL_PATRNSEQNO);
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInvInqData.getPatrnSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInvInqData.getPatrnSeqCd())));
		}
		// defect 9706 
		lsQry.append(lsList);
		// end defect 9706 

		if (aaInvInqData.getInvInqBy().equals(InventoryConstant.EMP)
			|| aaInvInqData.getInvInqBy().equals(InventoryConstant.WS)
			|| aaInvInqData.getInvInqBy().equals(InventoryConstant.DLR)
			|| aaInvInqData.getInvInqBy().equals(
				InventoryConstant.SUBCON))
		{
			// defect 9494
			//lsQry.append(SQL_ORDER_BY_INVID_ITMCDDESC_ITMYR_PATRNSEQNO);
			lsQry.append(SQL_ORDER_BY_INVID_ITMCDDESC_ITMYR_ITMNO);
			// end defect 9494
		}
		else
		{
			// defect 9494
			//lsQry.append(SQL_ORDER_BY_ITMCDDESC_ITMYR_PATRNSEQNO);
			lsQry.append(SQL_ORDER_BY_ITMCDDESC_ITMYR_ITMNO);
			// end defect 9494
		}

		try
		{
			Log.write(Log.SQL, this, MSG_QRY_IICBR_SQL_BEGIN);
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, MSG_QRY_IICBR_SQL_END);
			while (laResultSet.next())
			{
				InventoryInquiryReportData laInvInqRptData =
					new InventoryInquiryReportData();
				laInvInqRptData.setItmCd(
					caDA.getStringFromDB(laResultSet, COL_ITMCD));
				laInvInqRptData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, COL_ITMCDDESC));
				laInvInqRptData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, COL_INVITMYR));
				laInvInqRptData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, COL_INVITMNO));
				laInvInqRptData.setInvItmEndNo(
					caDA.getStringFromDB(laResultSet, COL_INVITMENDNO));
				laInvInqRptData.setInvQty(
					caDA.getIntFromDB(laResultSet, COL_INVQTY));
				laInvInqRptData.setInvId(
					caDA.getStringFromDB(laResultSet, COL_INVID));
				laInvInqRptData.setInvLocIdCd(
					caDA.getStringFromDB(laResultSet, COL_INVLOCIDCD));
				laInvInqRptData.setPatrnSeqNo(
					caDA.getIntFromDB(laResultSet, COL_PATRNSEQNO));
				laInvInqRptData.setInvStatusCd(
					caDA.getIntFromDB(laResultSet, COL_INVSTATUSCD));
				// This field is used only in the InventoryInquiryReportData.compareTo 
				// method for reports sorting purposes
				laInvInqRptData.setInvInqBy(aaInvInqData.getInvInqBy());
				// Add element to the Vector
				lvRslt.addElement(laInvInqRptData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, MSG_QRY_IICBR_END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				MSG_QRY_IICBR_EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Query Inventory History Data 
	 * 
	 * @param aaInvInqData InventoryInquiryUIData
	 * @return Vector 	
	 * @throws RTSException
	 */
	public Vector qryInventoryInquiryHistoryReport(InventoryInquiryUIData aaInvInqData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_QRY_IIHR_BEGIN);

		StringBuffer lsQry = new StringBuffer();
		String lsInvIds = new String(CommonConstant.STR_SPACE_EMPTY);
		String lsInvLocIdCd = aaInvInqData.getInvLocIdCd();
		Vector lvInvId = aaInvInqData.getInvIds();
		String lsList = CommonConstant.STR_SPACE_EMPTY;
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		// Convert single quote to 2 single quotes
		String lsInvId;
		if (lvInvId != null)
		{
			lsInvIds =
				CommonConstant.STR_OPEN_PARENTHESES
					+ CommonConstant.STR_SPACE_ONE;
			for (int i = 0; i < lvInvId.size(); i++)
			{
				lsInvId = (String) lvInvId.get(i);
				if (lsInvLocIdCd.equals(InventoryConstant.STR_E))
				{
					lsInvId = UtilityMethods.quote(lsInvId);
				}

				if (i == 0)
				{
					lsInvIds =
						lsInvIds
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SINGLE_QUOTE
							+ lsInvId
							+ CommonConstant.STR_SINGLE_QUOTE
							+ CommonConstant.STR_SPACE_ONE;
				}
				else
				{
					lsInvIds =
						lsInvIds
							+ CommonConstant.STR_COMMA
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_SINGLE_QUOTE
							+ lsInvId
							+ CommonConstant.STR_SINGLE_QUOTE
							+ CommonConstant.STR_SPACE_ONE;
				}
			}
			lsInvIds =
				lsInvIds
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_CLOSE_PARENTHESES
					+ CommonConstant.STR_SPACE_ONE;
		}

		// defect 9706 
		// use isAllItmsSelected()(boolean) vs. getAllItmSelected() (int) 
		if (!aaInvInqData.isAllItmsSelected())
		{
			// end defect 9706 

			Vector lvItmCds = aaInvInqData.getItmCds();
			Vector lvInvItmYrs = aaInvInqData.getInvItmYrs();
			lsList =
				CommonConstant.STR_OPEN_PARENTHESES
					+ CommonConstant.STR_SPACE_ONE;
			for (int i = 0; i < lvItmCds.size(); i++)
			{
				if (i != 0)
				{
					lsList = lsList + TXT_SPACE_OR_SPACE;
				}
				lsList =
					lsList
						+ TXT_ITMCD
						+ lvItmCds.get(i)
						+ SQL_QUOTE_AND_ITMYR_EQUAL
						+ lvInvItmYrs.get(i)
						+ CommonConstant.STR_CLOSE_PARENTHESES
						+ CommonConstant.STR_SPACE_ONE;
			}
			lsList =
				TXT_SPACE_AND_SPACE
					+ lsList
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_CLOSE_PARENTHESES;
		}
		// end defect 9706

		ResultSet laResultSet;
		lsQry.append(SQL_TRINVDETAIL_1);
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvInqData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvInqData.getSubstaId())));
		if (!aaInvInqData
			.getInvInqBy()
			.equals(InventoryConstant.SPECIFIC_ITM))
		{
			lsQry.append(SQL_AND_TRANSAMDATE_BETWEEN);
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInvInqData.getBeginDt().getAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInvInqData.getEndDt().getAMDate())));
		}
		else
		{
			lsQry.append(SQL_AND_INVITMNO_AND_INVENDNO);
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInvInqData.getInvItmNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInvInqData.getInvItmNo())));
		}
		if (lvInvId != null)
		{
			lsQry.append(
				SQL_AND_INVLOCIDCD_EQUAL
					+ lsInvLocIdCd
					+ CommonConstant.STR_SINGLE_QUOTE
					+ CommonConstant.STR_SPACE_ONE);
			if (!aaInvInqData
				.getInvLocIdCd()
				.equals(InventoryConstant.CHAR_C))
				lsQry.append(SQL_AND_INVID_IN + lsInvIds);
		}

		// defect 9706 
		lsQry.append(lsList);
		// end defect 9706 

		if (lsInvLocIdCd.equals(InventoryConstant.CHAR_D)
			|| lsInvLocIdCd.equals(InventoryConstant.CHAR_W)
			|| lsInvLocIdCd.equals(InventoryConstant.CHAR_S)
			|| lsInvLocIdCd.equals(InventoryConstant.CHAR_E))
		{
			lsQry.append(SQL_AND_TRANSCD_NOT);
		}
		else
		{
			lsQry.append(SQL_ORDER_BY_ITMCDDESC_ITMYR);
		}
		try
		{
			Log.write(Log.SQL, this, MSG_QRY_IIHR_SQL_BEGIN);
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, MSG_QRY_IIHR_SQL_END);
			while (laResultSet.next())
			{
				TransactionInventoryDetailData laTransInvDetailData =
					new TransactionInventoryDetailData();
				laTransInvDetailData.setItmCd(
					caDA.getStringFromDB(laResultSet, COL_ITMCD));
				laTransInvDetailData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, COL_ITMCDDESC));
				laTransInvDetailData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, COL_INVITMYR));
				laTransInvDetailData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, COL_INVITMNO));
				laTransInvDetailData.setInvEndNo(
					caDA.getStringFromDB(laResultSet, COL_INVENDNO));
				laTransInvDetailData.setInvQty(
					caDA.getIntFromDB(laResultSet, COL_INVQTY));
				laTransInvDetailData.setInvId(
					caDA.getStringFromDB(laResultSet, COL_INVID));
				laTransInvDetailData.setInvLocIdCd(
					caDA.getStringFromDB(laResultSet, COL_INVLOCIDCD));
				laTransInvDetailData.setTransAMDate(
					caDA.getIntFromDB(laResultSet, COL_TRANSAMDATE));
				laTransInvDetailData.setTransCd(
					caDA.getStringFromDB(laResultSet, COL_TRANSCD));
				if (laTransInvDetailData
					.getTransCd()
					.equals(TRANSCD_INVVD))
				{
					laTransInvDetailData.setTransCd(TRANSCD_VOID);
				}
				laTransInvDetailData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, COL_OFCISSUANCENO));
				laTransInvDetailData.setTransWsId(
					caDA.getIntFromDB(laResultSet, COL_TRANSWSID));
				laTransInvDetailData.setTransTime(
					caDA.getIntFromDB(laResultSet, COL_TRANSTIME));
				// This field is used only in the TransactionInventoryDetailData.compareTo 
				// method for reports sorting purposes
				laTransInvDetailData.setSortingFlag(
					aaInvInqData.getInvInqBy());
				// Add element to the Vector
				lvRslt.addElement(laTransInvDetailData);
			}
			//End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, MSG_QRY_IIHR_END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				MSG_QRY_IIHR_EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	// defect 10239 
	//	/**
	//	 * Method to Query Inventory Profile Data
	//	 * 
	//	 * @param aaInvProfileRptData InventoryProfileReportData
	//	 * @return Vector
	//	 * @throws RTSException
	//	 */
	//	public Vector qryInventoryProfileReport(InventoryProfileReportData aaInvProfileRptData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, MSG_QRY_IPR_BEGIN);
	//		StringBuffer lsQry = new StringBuffer();
	//		Vector lvRslt = new Vector();
	//		Vector lvValues = new Vector();
	//		ResultSet laResultSet;
	//
	//		lsQry.append(SQL_INV_PROFILE);
	//		try
	//		{
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaInvProfileRptData.getOfcIssuanceNo())));
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaInvProfileRptData.getSubstaId())));
	//			// If specified Entity
	//			if (aaInvProfileRptData.getEntity() != null)
	//			{
	//				lsQry.append(SQL_AND_ENTITY);
	//				lvValues.addElement(
	//					new DBValue(
	//						Types.CHAR,
	//						DatabaseAccess.convertToString(
	//							aaInvProfileRptData.getEntity())));
	//			}
	//			// If specified Id
	//			if (aaInvProfileRptData.getId() != null)
	//			{
	//				lsQry.append(SQL_AND_ID);
	//				lvValues.addElement(
	//					new DBValue(
	//						Types.CHAR,
	//						DatabaseAccess.convertToString(
	//							aaInvProfileRptData.getId())));
	//			}
	//			// If specified ItmCd
	//			if (aaInvProfileRptData.getItmCd() != null)
	//			{
	//				lsQry.append(SQL_AND_ITMCD);
	//				lvValues.addElement(
	//					new DBValue(
	//						Types.CHAR,
	//						DatabaseAccess.convertToString(
	//							aaInvProfileRptData.getItmCd())));
	//			}
	//			// If specified InvItmYr
	//			if (aaInvProfileRptData.getInvItmYr() != Integer.MIN_VALUE)
	//			{
	//				lsQry.append(SQL_AND_INVITMYR);
	//				lvValues.addElement(
	//					new DBValue(
	//						Types.CHAR,
	//						DatabaseAccess.convertToString(
	//							aaInvProfileRptData.getInvItmYr())));
	//			}
	//			lsQry.append(SQL_ORDER_BY_ENTITY_ID_ITMCDDESC_INVITMYR);
	//			Log.write(Log.SQL, this, MSG_QRY_IPR_SQL_BEGIN);
	//			laResultSet =
	//				caDA.executeDBQuery(lsQry.toString(), lvValues);
	//			Log.write(Log.SQL, this, MSG_QRY_IPR_SQL_END);
	//			while (laResultSet.next())
	//			{
	//				InventoryProfileReportData laInvProfileRptData =
	//					new InventoryProfileReportData();
	//				laInvProfileRptData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(laResultSet, COL_OFCISSUANCENO));
	//				laInvProfileRptData.setSubstaId(
	//					caDA.getIntFromDB(laResultSet, COL_SUBSTAID));
	//				laInvProfileRptData.setEntity(
	//					caDA.getStringFromDB(laResultSet, COL_ENTITY));
	//				laInvProfileRptData.setId(
	//					caDA.getStringFromDB(laResultSet, COL_ID));
	//				laInvProfileRptData.setItmCd(
	//					caDA.getStringFromDB(laResultSet, COL_ITMCD));
	//				laInvProfileRptData.setInvItmYr(
	//					caDA.getIntFromDB(laResultSet, COL_INVITMYR));
	//				laInvProfileRptData.setMaxQty(
	//					caDA.getIntFromDB(laResultSet, COL_MAXQTY));
	//				laInvProfileRptData.setMinQty(
	//					caDA.getIntFromDB(laResultSet, COL_MINQTY));
	//				laInvProfileRptData.setNextAvailIndi(
	//					caDA.getIntFromDB(laResultSet, COL_NEXTAVAILINDI));
	//				laInvProfileRptData.setDeleteIndi(
	//					caDA.getIntFromDB(laResultSet, COL_DELETEINDI));
	//				laInvProfileRptData.setChngTimestmp(
	//					caDA.getRTSDateFromDB(
	//						laResultSet,
	//						COL_CHNGTIMESTMP));
	//				// Add element to the Vector
	//				lvRslt.addElement(laInvProfileRptData);
	//			}
	//			//End of While 
	//			laResultSet.close();
	//			laResultSet = null;
	//			Log.write(Log.METHOD, this, MSG_QRY_IPR_END);
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				MSG_QRY_IPR_EXCEPTION + aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 10239 

	// defect 10207 
	//	/**
	//	 * Method to Query Inventory Receive History
	//	 * 
	//	 * @param avInputVector	Vector
	//	 * @return Vector 
	//	 * @throws RTSException	
	//	 */
	//	public Vector qryInventoryReceiveHistoryReport(Vector avInputVector)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, MSG_QRY_IRHR_BEGIN);
	//		StringBuffer lsQry = new StringBuffer();
	//		Vector lvRslt = new Vector();
	//		String lsList = CommonConstant.STR_SPACE_EMPTY;
	//		String lsOfcIssuanceNo =
	//			(String) avInputVector.get(CommonConstant.ELEMENT_0);
	//		InventoryHistoryUIData laInvHistoryUIData =
	//			(InventoryHistoryUIData) avInputVector.get(
	//				CommonConstant.ELEMENT_1);
	//		Vector lvInvItms = (Vector) laInvHistoryUIData.getInvItems();
	//		// efect 9706 
	//		// use isAllItmsSelected()(boolean) vs. getAllItmSelected() (int)
	//		if (!laInvHistoryUIData.isAllItmsSelected())
	//		{
	//			// end defect 9706
	//
	//			lsList = "( ";
	//
	//			for (int i = 0; i < lvInvItms.size(); i++)
	//			{
	//				if (i != 0)
	//				{
	//					lsList = lsList + " or ";
	//				}
	//				InventoryPatternsDescriptionData laInvData =
	//					(InventoryPatternsDescriptionData) lvInvItms.get(i);
	//
	//				lsList =
	//					lsList
	//						+ " ItmCd = "
	//						+ laInvData.getItmCd()
	//						+ " and ItmYr = "
	//						+ laInvData.getInvItmYr()
	//						+ ") ";
	//
	//			}
	//			lsList = lsList + ") and ";
	//
	//		}
	//		if (laInvHistoryUIData.getInvoiceIndi() == 1)
	//		{
	//			lsList =
	//				lsList
	//					+ " InvCoNo = '"
	//					+ laInvHistoryUIData.getInvoiceNo()
	//					+ "' and ";
	//		}
	//		else
	//			lsList =
	//				lsList
	//					+ " TransAMDate >= "
	//					+ laInvHistoryUIData.getBeginDate().getAMDate()
	//					+ " and "
	//					+ " TransAMDate <= "
	//					+ laInvHistoryUIData.getEndDate().getAMDate()
	//					+ " ) and ";
	//
	//		ResultSet laResultSet;
	//		lsQry.append(
	//			SQL_INV_HSTRY_1
	//				+ lsOfcIssuanceNo
	//				+ TXT_SPACE_AND_SPACE
	//				+ lsList
	//				+ SQL_INV_HSTRY_2);
	//		try
	//		{
	//			Log.write(Log.SQL, this, MSG_QRY_IRHR_SQL_BEGIN);
	//			laResultSet = caDA.executeDBQuery(lsQry.toString(), null);
	//			Log.write(Log.SQL, this, MSG_QRY_IRHR_SQL_END);
	//			while (laResultSet.next())
	//			{
	//				InventoryReceiveHistoryReportData laInvReceiveHistoryRptData =
	//					new InventoryReceiveHistoryReportData();
	//				laInvReceiveHistoryRptData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(laResultSet, COL_OFCISSUANCENO));
	//				laInvReceiveHistoryRptData.setSubStaId(
	//					caDA.getIntFromDB(laResultSet, COL_SUBSTAID));
	//				laInvReceiveHistoryRptData.setTransAMDate(
	//					caDA.getIntFromDB(laResultSet, COL_TRANSAMDATE));
	//				laInvReceiveHistoryRptData.setTransWsId(
	//					caDA.getIntFromDB(laResultSet, COL_TRANSWSID));
	//				laInvReceiveHistoryRptData.setTransTime(
	//					caDA.getIntFromDB(laResultSet, COL_TRANSTIME));
	//				laInvReceiveHistoryRptData.setTransEmpId(
	//					caDA.getStringFromDB(laResultSet, COL_TRANSEMPID));
	//				laInvReceiveHistoryRptData.setTransCd(
	//					caDA.getStringFromDB(laResultSet, COL_TRANSCD));
	//				laInvReceiveHistoryRptData.setItmCd(
	//					caDA.getStringFromDB(laResultSet, COL_ITMCD));
	//				laInvReceiveHistoryRptData.setItmCdDesc(
	//					caDA.getStringFromDB(laResultSet, COL_ITMCDDESC));
	//				laInvReceiveHistoryRptData.setInvItmYr(
	//					caDA.getIntFromDB(laResultSet, COL_INVITMYR));
	//				laInvReceiveHistoryRptData.setInvItmNo(
	//					caDA.getStringFromDB(laResultSet, COL_INVITMNO));
	//				laInvReceiveHistoryRptData.setInvEndNo(
	//					caDA.getStringFromDB(laResultSet, COL_INVENDNO));
	//				laInvReceiveHistoryRptData.setInvQty(
	//					caDA.getIntFromDB(laResultSet, COL_INVQTY));
	//				laInvReceiveHistoryRptData.setInvcNo(
	//					caDA.getStringFromDB(laResultSet, COL_INVCNO));
	//				laInvReceiveHistoryRptData.setInvcCorrIndi(
	//					caDA.getIntFromDB(laResultSet, COL_INVCCORRINDI));
	//				laInvReceiveHistoryRptData.setInvId(
	//					caDA.getStringFromDB(laResultSet, COL_INVID));
	//				// Add element to the Vector
	//				lvRslt.addElement(laInvReceiveHistoryRptData);
	//			}
	//			//End of While 
	//			laResultSet.close();
	//			laResultSet = null;
	//			Log.write(Log.METHOD, this, MSG_QRY_IRHR_END);
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				MSG_QRY_IRHR_EXCEPTION + aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 10207 

	/**
	 * Method to Query Inventory Receive History
	 * 
	 * @param avInputVector	Vector
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qryInventoryReceiveHistoryReport(InventoryHistoryUIData aaInvHstryData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, MSG_QRY_IRHR_BEGIN);
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		String lsList = CommonConstant.STR_SPACE_EMPTY;
		String lsOffices =
			getOffices(aaInvHstryData.getSelectedCounties());
		Vector lvInvItms = (Vector) aaInvHstryData.getInvItems();

		// defect 9706 
		// use isAllItmsSelected()(boolean) vs. getAllItmSelected() (int)
		if (!aaInvHstryData.isAllItmsSelected())
		{
			// end defect 9706

			lsList = "( ";

			for (int i = 0; i < lvInvItms.size(); i++)
			{
				if (i != 0)
				{
					lsList = lsList + "  or  ";
				}
				InventoryPatternsDescriptionData laInvData =
					(InventoryPatternsDescriptionData) lvInvItms.get(i);

				lsList =
					lsList
						+ "( A.ItmCd = '"
						+ laInvData.getItmCd()
						+ "' and A.InvItmYr = "
						+ laInvData.getInvItmYr()
						+ " ) ";

			}
			lsList = lsList + ") and ";

		}
		if (aaInvHstryData.getInvoiceIndi() == 1)
		{
			lsList =
				lsList
					+ " InvcNo = '"
					+ aaInvHstryData.getInvoiceNo()
					+ "' and ";
		}
		else
		{
			lsList =
				lsList
					+ " TransAMDate >= "
					+ aaInvHstryData.getBeginDate().getAMDate()
					+ " and "
					+ " TransAMDate <= "
					+ aaInvHstryData.getEndDate().getAMDate()
					+ " and ";
		}

		ResultSet laResultSet;

		lsQry.append(
			SQL_INV_HSTRY_1
				+ " "
				+ lsList
				+ " OfcIssuanceNo in "
				+ lsOffices
				+ " and "
				+ SQL_INV_HSTRY_2);

		try
		{
			Log.write(Log.SQL, this, MSG_QRY_IRHR_SQL_BEGIN);
			laResultSet = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, MSG_QRY_IRHR_SQL_END);
			while (laResultSet.next())
			{
				InventoryReceiveHistoryReportData laInvReceiveHistoryRptData =
					new InventoryReceiveHistoryReportData();
				laInvReceiveHistoryRptData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, COL_OFCISSUANCENO));
				laInvReceiveHistoryRptData.setSubStaId(
					caDA.getIntFromDB(laResultSet, COL_SUBSTAID));
				laInvReceiveHistoryRptData.setTransAMDate(
					caDA.getIntFromDB(laResultSet, COL_TRANSAMDATE));
				laInvReceiveHistoryRptData.setTransWsId(
					caDA.getIntFromDB(laResultSet, COL_TRANSWSID));
				laInvReceiveHistoryRptData.setTransTime(
					caDA.getIntFromDB(laResultSet, COL_TRANSTIME));
				laInvReceiveHistoryRptData.setTransEmpId(
					caDA.getStringFromDB(laResultSet, COL_TRANSEMPID));
				laInvReceiveHistoryRptData.setTransCd(
					caDA.getStringFromDB(laResultSet, COL_TRANSCD));
				laInvReceiveHistoryRptData.setItmCd(
					caDA.getStringFromDB(laResultSet, COL_ITMCD));
				laInvReceiveHistoryRptData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, COL_ITMCDDESC));
				laInvReceiveHistoryRptData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, COL_INVITMYR));
				laInvReceiveHistoryRptData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, COL_INVITMNO));
				laInvReceiveHistoryRptData.setInvEndNo(
					caDA.getStringFromDB(laResultSet, COL_INVENDNO));
				laInvReceiveHistoryRptData.setInvQty(
					caDA.getIntFromDB(laResultSet, COL_INVQTY));
				laInvReceiveHistoryRptData.setInvcNo(
					caDA.getStringFromDB(laResultSet, COL_INVCNO));
				laInvReceiveHistoryRptData.setInvcCorrIndi(
					caDA.getIntFromDB(laResultSet, COL_INVCCORRINDI));
				laInvReceiveHistoryRptData.setInvId(
					caDA.getStringFromDB(laResultSet, COL_INVID));
				// Add element to the Vector
				lvRslt.addElement(laInvReceiveHistoryRptData);
			}
			//End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(Log.METHOD, this, MSG_QRY_IRHR_END);
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				MSG_QRY_IRHR_EXCEPTION + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				MSG_QRY_IRHR_EXCEPTION + aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	} //END OF QUERY METHOD
}
