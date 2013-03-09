package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * InventoryVirtual.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/12/2007	New class.	
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/22/2007	Update processing of updating the status code.
 * 							update updInventoryVirtual()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/23/2007	Add Inquiry Report SQL.
 * 							add qryInventoryInquiryCurrentBalanceReport()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	03/01/2007	Work on Update for split.  Note that a 
 * 							check for return rows had to be added to 
 * 							get the error checking to work correctly!
 * 							update updInventoryVirtual()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	03/30/2007	Rename InetReqIndi to ItrntReqIndi.
 * 							Add ReqIpAddr.
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	04/03/2007	Fix item list in the report query.
 * 							It was not populating correctly.
 * 							modify qryInventoryInquiryCurrentBalanceReport()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	04/24/2007	Allow pilot to be used when looking up item 
 * 							codes.
 * 							modify qryInventoryInquiryCurrentBalanceReport()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/24/2007	Add CustSuppliedIndi to the SQL.
 * 							modify insInventoryVirtual(), 
 * 								qryInventoryRange(),
 * 								qryInventoryForSpecificItem(),
 * 								qryInventoryInquiryCurrentBalanceReport(),
 * 								updInventoryVirtual()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/01/2007	Add a method to handle deleting all 
 * 							Customer Supplied items that have been left
 * 							on hold.
 * 							add delInventoryVirtualInvStatusCdCS()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/06/2007	Allow update to effect the TransTime.
 * 							modify delInventoryVirtualInvStatusCdCS(),
 * 								purgeInventoryVirtual(),
 * 								purgeInventoryVirtualStrandedPersonal(),
 * 								updInventoryVirtual(),
 * 								updInventoryVirtualInvStatusCd()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/21/2007	Add handling for HoopsRegPltNo.
 * 							add insInventoryVirtual(), 
 * 								qryInventoryForSpecificItem()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	06/25/2007	Modify Intersection check to now handle 
 * 							User Defined plates as well.
 * 							modify qryInventoryVirtualIntersection()
 * 							defect 9116 Ver Special Plates
 * K Harrell	06/26/2007	Complete "Incomplete Print Immediate" 
 * 							VI requests for batch.
 * 							add updInvVirtualForBatch()
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	07/09/2007	Order the query for Inquiry Report by desc
 * 							instead of item code.
 * 							update qryInventoryInquiryCurrentBalanceReport()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/17/2007	Purge of completes should not check for a 
 * 							transtime of 0.
 * 							modify purgeInventoryVirtual()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	07/18/2007	Modify the system hold release to ignore
 * 							the customer supplied plates.  We do not
 * 							want to make them available in VI.
 * 							Marked purgeInventoryVirtualStrandedPersonal()
 * 							for delete.
 * 							modify purgeInventoryVirtualStrandedPersonal(),
 * 								updInventoryVirtualInvStatusCd()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	08/14/2007	Set the Virtual boolean on inv inq data so
 * 							the report can act on that type of data.
 * 							Pass a qty of 0 if the item is on some kind
 * 							of hold.
 * 							modify qryInventoryInquiryCurrentBalanceReport()
 * 							defect 9254 Ver Special Plates
 * Ray Rowehl	08/15/2007	Set Exclusive lock on row when getting next
 * 							available item.  This prevents possible
 * 							other users selecting the same row.
 * 							Causing exclusive table locks in d1rtsts2!
 * 							Improve some logging on method entries
 * 							modify insInventoryVirtual(),
 * 								qryInventoryRange(),
 * 								qryNextAvailableItem(), 
 * 								updInventoryVirtual()
 * 							defect 9249 Ver Special Plates
 * Ray Rowehl	09/07/2007	Add query method to support ReCalc
 * 							add qryGetAllVirtualInv()
 * 							defect 9116 Ver Special Plates
 * K Harrell	10/16/2007	do not reference pilot table data
 * 							modify qryInventoryInquiryCurrentBalanceReport() 
 *							defect 9354 Ver Special Plates 2 
 * K Harrell	10/30/2007	add logging,restore reference to pilot table
 * 							add logWriteInvalloc()
 * 							modify delInventoryVirtual(), 
 * 							 insInventoryVirtual(),qryInventoryForSpecificItem(),
 * 							 qryInventoryInquiryCurrentBalanceReport(),
 * 							 qryInventoryRange(), 
 * 							 qryInventoryInventoryVirtualIntersection(),
 * 							 qryNextAvailableItem(), updInventoryvirtual(),
 * 							 updInventoryVirtualForLogicalLock(),
 * 							 updInventoryVirtualStatusCd()
 * 							defect 9404 Ver Special Plates
 * K Harrell	10/31/2007	Remove reference to pilot table (again)
 *  						 qryInventoryInquiryCurrentBalanceReport()
 * 							defect 9404 Ver Special Plates 2
 * K Harrell	11/06/2007	removing logging from 10/30/2007	
 *							deprecate logWriteInvalloc()
 * 							modify delInventoryVirtual(), 
 * 							 insInventoryVirtual(),qryInventoryForSpecificItem(),
 * 							 qryInventoryInquiryCurrentBalanceReport(),
 * 							 qryInventoryRange(), 
 * 							 qryInventoryInventoryVirtualIntersection(),
 * 							 qryNextAvailableItem(), updInventoryvirtual(),
 * 							 updInventoryVirtualForLogicalLock(),
 * 							 updInventoryVirtualStatusCd()
 * 							defect 9404 Ver Special Plates
 * Min Wang		01/28/2008	Add ItrntReqIndi to status update.	
 * 							modify updInventoryVirtual() 
 * 							defect 9467 Ver 3_Amigos Prep
 * Ray Rowehl	04/22/2008	We want to keep pattern plates on hold 
 * 							longer for IVTRS.
 * 							add updInventoryVirtualInvStatusCdItrnt(),
 * 								updInventoryVirtualInvStatusCdNonItrnt()
 * 							delete updInventoryVirtualInvStatusCd()
 * 							defect 9606 Ver FRVP
 * Ray Rowehl	04/22/2008	Modified the IVTRS sql to just subtract 
 * 							days.
 * 							modify updInventoryVirtualInvStatusCdItrnt()
 * 							defect 9606 Ver FRVP
 * Ray Rowehl	06/01/2008	Needed a space in the sql to separate
 * 							words.
 * 							modify updInventoryVirtualInvStatusCdItrnt()
 * 							defect 9681 Ver FRVP
 * K Harrell	09/04/2008	Implement isAllItmsSelected() 
 * 							modify qryInventoryInquiryCurrentBalance()
 * 							defect 9706 Ver Defect_POS_B 
 * K Harrell	08/27/2009	Consider PrintImmediate 2 
 * 							modify updInventoryVirtualForBatch()
 * 							defect 10184 Ver Defect_POS_F 
 * K Harrell	03/01/2010	Modify from Types.CHAR to Types.INTEGER 
 * 							where appropriate. 
 * 							modify assignSelectVariables(),
 * 							  qryInventoryRange(), updInventoryVirtual(), 
 * 							  updInventoryVirtualForBatch()
 * 							defect 10164 Ver POS_640 
 * K Harrell	03/19/2010	Remove unused methods.
 * 							delete purgeInventoryVirtualStrandedPersonal(), 
 * 							 logWriteInvAlloc()
 * 							defect 10239 Ver POS_640
 * K Harrell	06/14/2010	Add TransId to RTS_INV_VIRTUAL
 * 							modify updInventoryVirtual(InventoryAllocationData,
 *  						 String)
 *							defect 10505 Ver 6.5.0 
 * K Harrell	06/26/2010	add csMethod 
 * 							add qryInventoryVirtualForTransId(TransactionKey) 
 * 							defect 10491 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class defines the SQL statements to interface with Inventory 
 * Virtual
 *
 * @version	6.5.0  				06/26/2010   
 * @author	Ray Rowehl
 * <br>Creation Date:			02/12/2007 13:46:01
 */

public class InventoryVirtual extends InventoryAllocationData
{
	DatabaseAccess caDA;

	// defect 10491 
	private String csMethod = new String();
	// end defect 10491  

	/**
	 * InventoryAllocation constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public InventoryVirtual(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	// defect 10239 
	//	/** 
	//	 * Log Write Inventory Allocation values Method description
	//	 * 
	//	 * @param aaInvAllocData
	//	 * @param asMethodName
	//	 * @deprecated  
	//	 */
	//	private void logWriteInvAlloc(
	//		InventoryAllocationData aaInvAllocData,
	//		String asMethodName)
	//	{
	//		String lsString =
	//			"VI_METHOD: "
	//				+ asMethodName
	//				+ " VIItmCd: "
	//				+ aaInvAllocData.getViItmCd()
	//				+ " ItmCd: "
	//				+ aaInvAllocData.getItmCd()
	//				+ " InvItmYr: "
	//				+ aaInvAllocData.getInvItmYr()
	//				+ " PatrnSeqCd: "
	//				+ aaInvAllocData.getPatrnSeqCd()
	//				+ " PatrnSeqNo: "
	//				+ aaInvAllocData.getPatrnSeqNo()
	//				+ " InvItmNo: "
	//				+ aaInvAllocData.getInvItmNo()
	//				+ " HoopsRegPltNo: "
	//				+ aaInvAllocData.getHoopsRegPltNo()
	//				+ " IPAddress: "
	//				+ aaInvAllocData.getRequestorIpAddress()
	//				+ " CustSuppliedIndi: "
	//				+ aaInvAllocData.isCustSupplied()
	//				+ " OfcIssuanceNo: "
	//				+ aaInvAllocData.getOfcIssuanceNo()
	//				+ " TransAMDate: "
	//				+ aaInvAllocData.getTransAmDate()
	//				+ " TransWsId: "
	//				+ aaInvAllocData.getTransWsId()
	//				+ " TransTime: "
	//				+ aaInvAllocData.getTransTime();
	//
	//		Log.write(Log.SQL_EXCP, this, lsString);
	//	}
	// end defect 10239 

	/**
	 * Assignment of selected Variables
	 *
	 * @param aaProcInvData ProcessInventoryData
	 */
	private void assignSelectVariables(
		InventoryAllocationData aaProcInvData,
		Vector avValues)
	{
		avValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaProcInvData.getItmCd())));

		avValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaProcInvData.getInvItmYr())));

		// defect 10164 
		avValues.addElement(new DBValue(
		//Types.CHAR,
		Types.INTEGER,
			DatabaseAccess.convertToString(
				aaProcInvData.getPatrnSeqCd())));
		// end defect 10164 
	}

	/**
	 * Method to Delete from RTS.RTS_INV_VIRTUAL
	 *
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @throws RTSException 
	 */
	public void delInventoryVirtual(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delInventoryVirtual - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_INV_VIRTUAL "
				+ "WHERE "
				+ "VIItmCd = ? AND "
				+ "InvItmYr = ? AND "
				+ "InvStatusCd = ? AND "
				+ "InvItmNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmYr())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvStatusCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmNo())));

			Log.write(
				Log.SQL,
				this,
				"delInventoryVirtual - SQL - Begin");

			// defect 9404
			//			logWriteInvAlloc(
			//				aaInventoryAllocationData,
			//				"delInventoryVirtual");
			// end defect 9404

			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delInventoryVirtual - SQL - End");
			Log.write(Log.METHOD, this, "delInventoryVirtual - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delInventoryVirtual - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Purge all system locks on RTS.RTS_INV_VIRTUAL that 
	 * have expired and are Customer Supplied.
	 * 
	 * @throws RTSException
	 */
	public int purgeInventoryVirtualInvStatusCdCS() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"purgeInventoryVirtualInvStatusCdCS - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_INV_VIRTUAL "
				+ "WHERE InvStatusCd = 2 "
				+ "AND CustSuppliedIndi = 1 "
				+ "AND TransTime = 0 "
				+ "AND InvHoldTimeStmp < Current Timestamp - 15 minutes";

		try
		{
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryVirtualInvStatusCdCS - SQL - Begin");

			int liReleaseCount =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryVirtualInvStatusCdCS - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"purgeInventoryVirtualInvStatusCdCS - End");
			return liReleaseCount;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeInventoryVirtualInvStatusCdCS - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Insert into RTS.RTS_INV_VIRTUAL
	 *
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @throws RTSException 
	 */
	public void insInventoryVirtual(
		InventoryAllocationData aaInventoryAllocationData,
		String asClientHost)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insInventoryVirtual - Begin " + asClientHost);

		// by specifying the right number of fields, this has better 
		// performance.
		Vector lvValues = new Vector(22);

		String lsIns =
			"INSERT into RTS.RTS_INV_VIRTUAL ("
				+ "ViItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvItmEndNo,"
				+ "IsaIndi,"
				+ "UserPltNoIndi,"
				+ "InvStatusCd,"
				+ "PatrnSeqNo,"
				+ "PatrnSeqCd,"
				+ "InvQty,"
				+ "InvcNo,"
				+ "InvcDate,"
				+ "MfgPltNo,"
				+ "ItrntReqIndi,"
				+ "OfcIssuanceNo,"
				+ "TransWsId,"
				+ "TransAmDate,"
				+ "TransTime,"
				+ "TransEmpId,"
				+ "ReqIpAddr,"
				+ "CustSuppliedIndi,"
				+ "InvHoldTimeStmp,"
				+ "HoopsRegPltNo "
				+ ") VALUES ( "
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " CURRENT TIMESTAMP,"
				+ " ?)";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmYr())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmEndNo())));
			lvValues.addElement(
				new DBValue(
					Types.BOOLEAN,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.isISA())));
			lvValues.addElement(
				new DBValue(
					Types.BOOLEAN,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.isUserPltNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvStatusCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getPatrnSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getPatrnSeqCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvQty())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvcNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvcDate())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getMfgPltNo())));
			lvValues.addElement(
				new DBValue(
					Types.BOOLEAN,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.isItrntReq())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getTransAmDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getTransTime())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getTransEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData
							.getRequestorIpAddress())));
			lvValues.addElement(
				new DBValue(
					Types.BOOLEAN,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.isCustSupplied())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getHoopsRegPltNo())));

			Log.write(
				Log.SQL,
				this,
				"insInventoryVirtual - SQL - Begin " + asClientHost);

			// defect 9404 
			//			logWriteInvAlloc(
			//				aaInventoryAllocationData,
			//				"insInventoryVirtual " + asClientHost);
			// end defect 9404 

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insInventoryVirtual - SQL - End " + asClientHost);
			Log.write(
				Log.METHOD,
				this,
				"insInventoryVirtual - End " + asClientHost);
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insInventoryVirtual - "
					+ asClientHost
					+ " Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	}

	/**
	 * Method to Query RTS.RTS_INV_VIRTUAL to get all Inventory
	 *  given ItmCd
	 *
	 * @param  asItmCd			String
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryGetAllVirtualInv(String asItmCd)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryGetAllVirtualInv - Begin");

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "VIITMCD, "
				+ "INVITMYR, "
				+ "INVITMNO, "
				+ "INVITMENDNO, "
				+ "ISAINDI, "
				+ "USERPLTNOINDI, "
				+ "INVSTATUSCD, "
				+ "PATRNSEQNO,"
				+ "PATRNSEQCD, "
				+ "INVQTY, "
				+ "INVCNO, "
				+ "INVCDATE, "
				+ "MFGPLTNO, "
				+ "ITRNTREQINDI, "
				+ "OFCISSUANCENO, "
				+ "TRANSWSID, "
				+ "TRANSAMDATE, "
				+ "TRANSTIME, "
				+ "TRANSEMPID, "
				+ "REQIPADDR, "
				+ "CUSTSUPPLIEDINDI, "
				+ "INVHOLDTIMESTMP, "
				+ "HOOPSREGPLTNO "
				+ "FROM RTS.RTS_INV_VIRTUAL ");
		try
		{
			if (asItmCd != null && asItmCd.length() > 0)
			{
				//only for particular item
				lsQry.append("WHERE ViItmCd = ? ");

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(asItmCd)));
			}
			lsQry.append("order by ViItmCd, InvItmYr");

			Log.write(
				Log.SQL,
				this,
				" - qryGetAllVirtualInv - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryGetAllVirtualInv - SQL - End");

			while (lrsQry.next())
			{
				InventoryAllocationData laIAData =
					new InventoryAllocationData();
				laIAData.setViItmCd(
					caDA.getStringFromDB(lrsQry, "ViItmCd"));
				laIAData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laIAData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laIAData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laIAData.setISA(
					caDA.getBooleanFromDB(lrsQry, "IsaIndi"));
				laIAData.setUserPltNo(
					caDA.getBooleanFromDB(lrsQry, "UserPltNoIndi"));
				laIAData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laIAData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laIAData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				laIAData.setInvQty(caDA.getIntFromDB(lrsQry, "InvQty"));
				laIAData.setInvcNo(
					caDA.getStringFromDB(lrsQry, "InvcNo"));
				laIAData.setInvcDate(
					caDA.getIntFromDB(lrsQry, "InvcDate"));
				laIAData.setMfgPltNo(
					caDA.getStringFromDB(lrsQry, "MfgPltNo"));
				laIAData.setItrntReq(
					caDA.getBooleanFromDB(lrsQry, "ItrntReqIndi"));
				laIAData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laIAData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laIAData.setTransAmDate(
					caDA.getIntFromDB(lrsQry, "TransAmDate"));
				laIAData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laIAData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laIAData.setRequestorIpAddress(
					caDA.getStringFromDB(lrsQry, "ReqIpAddr"));
				laIAData.setCustSupplied(
					caDA.getBooleanFromDB(lrsQry, "CustSuppliedIndi"));
				laIAData.setInvHoldTimeStmp(
					caDA.getRTSDateFromDB(lrsQry, "InvHoldTimeStmp"));
				laIAData.setHoopsRegPltNo(
					caDA.getStringFromDB(lrsQry, "HoopsRegPltNo"));

				laIAData.setVirtual(true);

				// Add element to the Vector
				lvRslt.addElement(laIAData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryGetAllVirtualInv - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryGetAllVirtualInv - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}

	/**
	 * Method to Query RTS.RTS_INV_VIRTUAL for Inventory Ranges
	 * 
	 * @param  aaInvAllocData  InventoryAllocationData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryRange(
		InventoryAllocationData aaInvAllocData,
		String asClientHost)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryRange - Begin " + asClientHost);

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		//String lsStringById = "";

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		// defect 10164 
		DBValue liPatrnSeqNo = new DBValue(
			//Types.CHAR,
	Types.INTEGER,
		DatabaseAccess.convertToString(aaInvAllocData.getPatrnSeqNo()));

		DBValue liEndPatrnSeqNo = new DBValue(
			//Types.CHAR,
	Types.INTEGER,
		DatabaseAccess.convertToString(
			aaInvAllocData.getEndPatrnSeqNo()));
		// end defect 10164 

		String lsSelectStuff =
			" as RangeCd, VIITMCD, INVITMYR, INVITMNO,"
				+ " INVITMENDNO, ISAINDI, USERPLTNOINDI, INVSTATUSCD,"
				+ " PATRNSEQNO,"
				+ " PATRNSEQNO + INVQTY -1 as PATRNSEQENDNO,"
				+ " PATRNSEQCD, INVQTY, INVCNO, INVCDATE, MFGPLTNO,"
				+ " ITRNTREQINDI, OFCISSUANCENO, TRANSWSID,"
				+ " TRANSAMDATE, TRANSTIME, TRANSEMPID,"
				+ " ReqIpAddr, CustSuppliedIndi, INVHOLDTIMESTMP,"
				+ " HoopsRegPltNo"
				+ " FROM RTS.RTS_INV_VIRTUAL WHERE "
				+ " VIITMCD= ? AND "
				+ " INVITMYR= ? AND "
				+ " PATRNSEQCD= ? AND ";

		// 'W' (WITHIN)   >beg,  <end
			String Condition =
				" PATRNSEQNO> ? AND " //:BEGPATRNSEQNO (1)
		+" PATRNSEQNO< ?  AND " //:ENDPATRNSEQNO  (2)
		+" (PATRNSEQNO + INVQTY - 1)> ?  AND " //:BEGPATRNSEQNO (3)
	+" (PATRNSEQNO + INVQTY - 1) < ?  "; //:ENDPATRNSEQNO (4)

		String SelectString = "Select 'W' " + lsSelectStuff + Condition;
		//+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);
		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liEndPatrnSeqNo); //2
		lvValues.addElement(liPatrnSeqNo); //3
		lvValues.addElement(liEndPatrnSeqNo); //4

		// 'R' (RIGHT) >beg, <=end    "
			Condition = " PATRNSEQNO> ? AND " //:BEG (1)
		+" PATRNSEQNO<= ? AND " //:END (2)
	+" (PATRNSEQNO + INVQTY - 1) > ? "; //:END (3)

		SelectString = "UNION Select 'R' " + lsSelectStuff + Condition;

		lsQry.append(SelectString);
		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);

		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liEndPatrnSeqNo); //2
		lvValues.addElement(liEndPatrnSeqNo); //3

		// 'L'  (LEFT) beg<BEG, beg+qty>= BEG, beg+qty <END  "
			Condition = " PATRNSEQNO< ? AND " //:BEG  (1) 
		+" (PATRNSEQNO + INVQTY - 1)>= ?  AND " //:BEG  (2)
	+" (PATRNSEQNO + INVQTY - 1) < ? "; //:END  (3)

		SelectString = "UNION Select 'L' " + lsSelectStuff + Condition;
		//+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);

		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liPatrnSeqNo); //2
		lvValues.addElement(liEndPatrnSeqNo); //3

		//  'BE ' (BeginEnd)
			Condition = " PATRNSEQNO = ? AND " //:BEG (1)
	+" (PATRNSEQNO + INVQTY - 1) = ? "; //:END (2)

		SelectString =
			" UNION Select 'BE' " + lsSelectStuff + Condition;
		//+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);
		lvValues.addElement(liPatrnSeqNo);
		lvValues.addElement(liEndPatrnSeqNo);

		// 'BW' (BeginWithin)
			Condition = " PATRNSEQNO= ? AND " //:BEG (1)
	+" (PATRNSEQNO + INVQTY - 1)< ? "; //:END (2)

		SelectString =
			" UNION Select 'BW' " + lsSelectStuff + Condition;
		//+ lsStringById;

		lsQry.append(SelectString);
		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);

		lvValues.addElement(liPatrnSeqNo);
		lvValues.addElement(liEndPatrnSeqNo);

		// 'BO' (BeginOutSide)
			Condition = " PATRNSEQNO= ?  AND " //:BEG (1) 
	+" (PATRNSEQNO + INVQTY - 1) > ? "; //:END (2)

		SelectString =
			" UNION Select 'BO' " + lsSelectStuff + Condition;
		//+ lsStringById;
		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);
		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liEndPatrnSeqNo); //2

		// 'EW'  (EndWithin) 
			Condition =
				" (PATRNSEQNO + INVQTY - 1) = ?  AND " //:END (1)
	+" PATRNSEQNO> ? "; //:BEG (2)

		SelectString =
			" UNION Select 'EW' " + lsSelectStuff + Condition;
		//+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);
		lvValues.addElement(liEndPatrnSeqNo); //1	
		lvValues.addElement(liPatrnSeqNo); //2

		// 'EO'  (EndOutside)
			Condition =
				" (PATRNSEQNO + INVQTY - 1)= ? AND " //:END  (1)
	+" PATRNSEQNO< ? "; //:BEG  (2)

		SelectString =
			" UNION Select 'EO' " + lsSelectStuff + Condition;
		//+ lsStringById;

		lsQry.append(SelectString);

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);
		lvValues.addElement(liEndPatrnSeqNo); //1	
		lvValues.addElement(liPatrnSeqNo); //2

		// 'O'  (Outside)
			Condition = " PATRNSEQNO< ? AND " //:BEG (1)
	+" (PATRNSEQNO + INVQTY - 1)> ? "; //:END (2)

		SelectString = " UNION Select 'O' " + lsSelectStuff + Condition
			//+ lsStringById
	+" ORDER BY PATRNSEQCD, PATRNSEQNO";

		// ASSIGN OfcIssuanceNo,Substaid,ItmCd,InvItmYr,PatrnSeqCd
		assignSelectVariables(aaInvAllocData, lvValues);
		lvValues.addElement(liPatrnSeqNo); //1
		lvValues.addElement(liEndPatrnSeqNo); //2

		lsQry.append(SelectString);

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryRange - SQL - Begin " + asClientHost);

			Log.write(
				Log.DEBUG,
				this,
				" - qryInventoryRange - SQL - Begin " + asClientHost);

			// defect 9404
			//			logWriteInvAlloc(
			//				aaInvAllocData,
			//				"qryInventoryRange " + asClientHost);
			// end defect 9404

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.DEBUG,
				this,
				" - qryInventoryRange - SQL - End " + asClientHost);

			Log.write(
				Log.SQL,
				this,
				" - qryInventoryRange - SQL - End " + asClientHost);

			while (lrsQry.next())
			{
				InventoryAllocationData laNewInvAllocData =
					new InventoryAllocationData();
				laNewInvAllocData.setRangeCd(
					caDA.getStringFromDB(lrsQry, "RangeCd"));
				laNewInvAllocData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ViItmCd"));
				laNewInvAllocData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laNewInvAllocData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laNewInvAllocData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laNewInvAllocData.setISA(
					caDA.getBooleanFromDB(lrsQry, "ISAIndi"));
				laNewInvAllocData.setUserPltNo(
					caDA.getBooleanFromDB(lrsQry, "UserPltNoIndi"));
				laNewInvAllocData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laNewInvAllocData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laNewInvAllocData.setEndPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PATRNSEQENDNO"));
				laNewInvAllocData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				laNewInvAllocData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laNewInvAllocData.setInvcNo(
					caDA.getStringFromDB(lrsQry, "InvcNo"));
				laNewInvAllocData.setInvcDate(
					caDA.getIntFromDB(lrsQry, "InvcDate"));
				laNewInvAllocData.setMfgPltNo(
					caDA.getStringFromDB(lrsQry, "MfgPltNo"));
				laNewInvAllocData.setItrntReq(
					caDA.getBooleanFromDB(lrsQry, "ItrntReqIndi"));
				laNewInvAllocData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laNewInvAllocData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laNewInvAllocData.setTransAmDate(
					caDA.getIntFromDB(lrsQry, "TransAmDate"));
				laNewInvAllocData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laNewInvAllocData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laNewInvAllocData.setRequestorIpAddress(
					caDA.getStringFromDB(lrsQry, "ReqIpAddr"));
				laNewInvAllocData.setCustSupplied(
					caDA.getBooleanFromDB(lrsQry, "CustSuppliedIndi"));
				laNewInvAllocData.setInvHoldTimeStmp(
					caDA.getRTSDateFromDB(lrsQry, "InvHoldTimeStmp"));
				laNewInvAllocData.setHoopsRegPltNo(
					caDA.getStringFromDB(lrsQry, "HoopsRegPltNo"));
				laNewInvAllocData.setEndPatrnSeqNo(
					laNewInvAllocData.getPatrnSeqNo()
						+ laNewInvAllocData.getInvQty()
						- 1);
				laNewInvAllocData.setCalcInv(true);
				laNewInvAllocData.setViItmCd(
					laNewInvAllocData.getItmCd());
				// Add element to the Vector
				lvRslt.addElement(laNewInvAllocData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryRange - End " + asClientHost);
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryRange - Exception "
					+ asClientHost
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}

	/**
	 * Check for any Intersections of Virtual Inventory Data.
	 * 
	 * <p>This is used primarily for Invoice Receive to ensure there
	 * is no overlap in the ranges.
	 *
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryVirtualIntersection(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryVirtualIntersection - Begin");

		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet lrsQry;

		try
		{
			// basic part of query
			lsQry.append(
				"SELECT VIITMCD, INVITMYR, INVITMNO, "
					+ "INVITMENDNO, INVQTY, PATRNSEQNO, "
					+ "INVSTATUSCD, PATRNSEQCD "
					+ "FROM RTS.RTS_INV_VIRTUAL ");

			// if no hoops, query based on pattern seq no
			// Otherwise, use the hoops search.
			if (aaInventoryAllocationData.getHoopsRegPltNo() == null)
			{
				lsQry.append(
					"WHERE VIITMCD = ? AND "
						+ "INVITMYR = ? AND "
						+ "PATRNSEQCD = ? AND "
						+ "((? BETWEEN PATRNSEQNO AND "
						+ "(PATRNSEQNO+INVQTY-1)) OR "
						+ "(? BETWEEN PATRNSEQNO AND "
						+ "(PATRNSEQNO+INVQTY-1)) OR "
						+ "(PATRNSEQNO BETWEEN ? AND "
						+ "? )) ");

				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData.getItmCd())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData.getInvItmYr())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getPatrnSeqCd())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getPatrnSeqNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getEndPatrnSeqNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getPatrnSeqNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getEndPatrnSeqNo())));
			}
			else
			{
				lsQry.append("WHERE HOOPSREGPLTNO = ? ");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getHoopsRegPltNo())));

			}

			// add the order by 
			lsQry.append("ORDER BY PATRNSEQCD,PATRNSEQNO");

			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocationIntersection - SQL - Begin");

			// defect 9404
			//			logWriteInvAlloc(
			//				aaInventoryAllocationData,
			//				"qryInventoryAllocationIntersection");
			// end defect 9404

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocationIntersection - SQL - End");

			while (lrsQry.next())
			{
				InventoryAllocationData laInventoryAllocationData =
					new InventoryAllocationData();
				laInventoryAllocationData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ViItmCd"));
				laInventoryAllocationData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventoryAllocationData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laInventoryAllocationData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laInventoryAllocationData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laInventoryAllocationData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laInventoryAllocationData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laInventoryAllocationData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				laInventoryAllocationData.setViItmCd(
					laInventoryAllocationData.getItmCd());
				// Add element to the Vector
				lvRslt.addElement(laInventoryAllocationData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryVirtualIntersection - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryVirtualIntersection - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}

	/**
	 * Query for a Specific Item Number.  
	 *  
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryForSpecificItem(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryForSpecificItem - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "ViItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvItmEndNo,"
				+ "IsaIndi,"
				+ "UserPltNoIndi,"
				+ "InvStatusCd,"
				+ "PatrnSeqNo,"
				+ "PatrnSeqCd,"
				+ "InvQty,"
				+ "InvcNo,"
				+ "InvcDate,"
				+ "MfgPltNo,"
				+ "ItrntReqIndi,"
				+ "OfcIssuanceNo,"
				+ "TransWsId,"
				+ "TransAmDate,"
				+ "TransTime,"
				+ "TransEmpId,"
				+ "ReqIpAddr,"
				+ "CustSuppliedIndi, "
				+ "InvHoldTimeStmp, "
				+ "HoopsRegPltNo "
				+ "FROM RTS.RTS_INV_VIRTUAL "
				+ "where ");

		// only add ViItmCd to the where clause if it is specified
		if (aaInventoryAllocationData.getViItmCd() != null)
		{
			lsQry.append("ViItmcd = ? and InvItmYr = ? and ");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getViItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmYr())));
		}

		// If the PatrnSeqNo is greater than 0, use it.
		// Otherwise, if HoopsRegPltNo is not null and User Defined
		// Plate No, use that.
		// Otherwise, use InvItmNo.
		if (aaInventoryAllocationData.getPatrnSeqNo() > 0)
		{
			lsQry.append(
				" ? between PatrnSeqNo and (PatrnSeqNo+InvQty -1) ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getPatrnSeqNo())));
		}
		else if (
			aaInventoryAllocationData.getHoopsRegPltNo() != null
				&& aaInventoryAllocationData.isUserPltNo())
		{
			lsQry.append(" HoopsRegPltNo = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getHoopsRegPltNo())));
		}
		else
		{
			lsQry.append(" InvItmNo = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmNo())));
		}

		try
		{

			Log.write(
				Log.SQL,
				this,
				" - qryInventoryForSpecificItem - SQL - Begin");

			// defect 9404
			//			logWriteInvAlloc(
			//				aaInventoryAllocationData,
			//				"qryInventoryForSpecificItem");
			// end defect 9404

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.DEBUG,
				this,
				" - qryInventoryForSpecificItem - SQL - End");
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryForSpecificItem - SQL - End");

			while (lrsQry.next())
			{
				InventoryAllocationData laInventoryAllocationData =
					new InventoryAllocationData();

				laInventoryAllocationData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ViItmCd"));
				laInventoryAllocationData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventoryAllocationData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laInventoryAllocationData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laInventoryAllocationData.setISA(
					caDA.getBooleanFromDB(lrsQry, "IsaIndi"));
				laInventoryAllocationData.setUserPltNo(
					caDA.getBooleanFromDB(lrsQry, "UserPltNoIndi"));
				laInventoryAllocationData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laInventoryAllocationData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laInventoryAllocationData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				laInventoryAllocationData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laInventoryAllocationData.setInvcNo(
					caDA.getStringFromDB(lrsQry, "InvcNo"));
				laInventoryAllocationData.setInvcDate(
					caDA.getIntFromDB(lrsQry, "InvcDate"));
				laInventoryAllocationData.setMfgPltNo(
					caDA.getStringFromDB(lrsQry, "MfgPltNo"));
				laInventoryAllocationData.setItrntReq(
					caDA.getBooleanFromDB(lrsQry, "ItrntReqIndi"));
				laInventoryAllocationData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryAllocationData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laInventoryAllocationData.setTransAmDate(
					caDA.getIntFromDB(lrsQry, "TransAmDate"));
				laInventoryAllocationData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laInventoryAllocationData.setTransEmpId(
					caDA.getStringFromDB(lrsQry, "TransEmpId"));
				laInventoryAllocationData.setRequestorIpAddress(
					caDA.getStringFromDB(lrsQry, "ReqIpAddr"));
				laInventoryAllocationData.setCustSupplied(
					caDA.getBooleanFromDB(lrsQry, "CustSuppliedIndi"));
				laInventoryAllocationData.setInvHoldTimeStmp(
					caDA.getRTSDateFromDB(lrsQry, "InvHoldTimeStmp"));
				laInventoryAllocationData.setHoopsRegPltNo(
					caDA.getStringFromDB(lrsQry, "HoopsRegPltNo"));
				laInventoryAllocationData.setViItmCd(
					laInventoryAllocationData.getItmCd());
				// Add element to the Vector
				lvRslt.addElement(laInventoryAllocationData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryForSpecificItem - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryForSpecificItem - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}
	/** 
	 * Query Inventory Virtual For TransId 
	 * 
	 * @param aaTransKey
	 * @return InventoryAllocationData 
	 * @throws RTSException
	 */
	public InventoryAllocationData qryInventoryVirtualForTransId(TransactionKey aaTransKey)
		throws RTSException
	{
		csMethod = "getInventoryVirtualForTransId";
		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;
		lsQry.append(
			"SELECT "
				+ "VIItmCd,"
				+ "InvItmNo,"
				+ "InvItmEndNo,"
				+ "InvQty,"
				+ "OfcIssuanceNo,"
				+ "TransWsId,"
				+ "TransAMDate,"
				+ "TransTime "
				+ "FROM RTS.RTS_INV_VIRTUAL "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "TransAMDate = ? AND "
				+ "TransWsId = ? AND "
				+ "TransTime = ? ");

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransKey.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransKey.getTransAMDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransKey.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaTransKey.getTransTime())));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			InventoryAllocationData laInvAllocData = null;

			while (lrsQry.next())
			{
				laInvAllocData = new InventoryAllocationData();
				laInvAllocData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInvAllocData.setTransAmDate(
					caDA.getIntFromDB(lrsQry, "TransAMDate"));
				laInvAllocData.setTransWsId(
					caDA.getIntFromDB(lrsQry, "TransWsId"));
				laInvAllocData.setTransTime(
					caDA.getIntFromDB(lrsQry, "TransTime"));
				laInvAllocData.setViItmCd(
					caDA.getStringFromDB(lrsQry, "ViItmCd"));
				laInvAllocData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laInvAllocData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laInvAllocData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));

				break;
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return laInvAllocData;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Get the next available Virtual Item Number.
	 * 
	 * @param  aaInvAllocData
	 * @return InventoryAllocationData
	 * @throws RTSException 
	 */
	public InventoryAllocationData qryNextAvailableItem(
		InventoryAllocationData aaInvAllocData,
		String asClientHost)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryNextAvailableItem - Begin " + asClientHost);

		StringBuffer lsQry = new StringBuffer();

		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();

		ResultSet lrsQry;
		Vector lvValues = new Vector();

		// defect 9249
		// add exclusive clause at end
		// Also only use patrnseqcd = 0 because there are not any
		// others for virtual items.
		String lsSelect1 =
			"SELECT VIITMCD, INVITMYR, "
				+ "INVITMNO, "
				+ "INVITMENDNO, PATRNSEQNO, "
				+ "PATRNSEQCD FROM RTS.RTS_INV_VIRTUAL A WHERE "
				+ "A.VIITMCD = ? AND "
				+ "A.INVITMYR = ? AND "
				+ "A.INVSTATUSCD = 0 AND "
				+ "A.PATRNSEQNO = "
				+ "(SELECT MIN(PATRNSEQNO) FROM "
				+ "RTS.RTS_INV_VIRTUAL B "
				+ "WHERE "
				+ "A.VIITMCD = B.VIITMCD AND "
				+ "A.INVITMYR = B.INVITMYR AND "
				+ "A.INVSTATUSCD = B.INVSTATUSCD AND "
				+ "B.PATRNSEQCD = 0 "
			//+ "B.PATRNSEQCD = "
		//+ "(SELECT "
		//+ "MIN(PATRNSEQCD) "
		//+ "FROM RTS.RTS_INV_VIRTUAL C WHERE "
		//+ "A.VIITMCD = C.VIITMCD AND "
		//+ "A.INVITMYR = C.INVITMYR AND "
		//+ "A.INVSTATUSCD = C.INVSTATUSCD "
		//+ ") " 				
	+") " + "WITH RS USE AND KEEP EXCLUSIVE LOCKS";
		// end defect 9249

		lsQry.append(lsSelect1);

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInvAllocData.getItmCd())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInvAllocData.getInvItmYr())));

			Log.write(
				Log.SQL,
				this,
				" - qryNextAvailableItem - SQL - Begin "
					+ asClientHost);

			//	defect 9404
			//			logWriteInvAlloc(
			//				aaInvAllocData,
			//				"qryNextAvailableItem " + asClientHost);
			// end defect 9404

			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryNextAvailableItem - SQL - End " + asClientHost);

			while (lrsQry.next())
			{
				laInvAllocData.setItmCd(
					caDA.getStringFromDB(lrsQry, "VIItmCd"));
				laInvAllocData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInvAllocData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laInvAllocData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laInvAllocData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				laInvAllocData.setEndPatrnSeqNo(
					laInvAllocData.getPatrnSeqNo());
				laInvAllocData.setInvItmEndNo(
					laInvAllocData.getInvItmNo());
				laInvAllocData.setInvQty(1);
				laInvAllocData.setInvStatusCd(0);
				laInvAllocData.setVirtual(true);
				laInvAllocData.setCalcInv(true);
				laInvAllocData.setViItmCd(laInvAllocData.getItmCd());
				laInvAllocData.setVirtual(true);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryNextAvailableItem - End " + asClientHost);
			return (laInvAllocData);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryNextAvailableItem - "
					+ asClientHost
					+ " Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}

	/**
	 * Method to Query Inventory Current Balance from Virtual Inventory.
	 * 
	 * @param aaInvInqData InventoryInquiryUIData
	 * @return Vector
	 * @throws RTSException 	
	 */
	public Vector qryInventoryInquiryCurrentBalanceReport(InventoryInquiryUIData aaInvInqData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryInquiryCurrentBalanceReport - Begin ");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		String lsInvLocIdCd = aaInvInqData.getInvLocIdCd();
		Vector lvInvId = aaInvInqData.getInvIds();
		String lsInvIds = new String(CommonConstant.STR_SPACE_EMPTY);
		String lsInvId;
		// defect 9706 
		String lsList = CommonConstant.STR_SPACE_EMPTY;
		// end defect 9706 

		lsQry.append(
			"SELECT A.VIITMCD, A.INVITMYR, A.INVITMNO, "
				+ "A.INVITMENDNO, A.INVQTY, A.INVSTATUSCD, "
				+ "B.ITMCDDESC, A.PATRNSEQNO, "
				+ "A.OFCISSUANCENO, A.TRANSWSID, A.TRANSEMPID, "
				+ "A.TRANSAMDATE, A.ReqIpAddr, A.CustSuppliedIndi, "
				+ "A.INVHOLDTIMESTMP, A.HoopsRegPltNo "
				+ "FROM RTS.RTS_INV_VIRTUAL A, "
				+ "RTS.RTS_ITEM_CODES B WHERE "
				+ "A.VIITMCD = B.ITMCD ");

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
				{
					lsList = lsList + " OR ";
				}
				lsList =
					lsList
						+ "( A.ViItmCd = '"
						+ lvItmCds.get(i)
						+ "' and A.InvItmYr = "
						+ lvInvItmYrs.get(i)
						+ CommonConstant.STR_CLOSE_PARENTHESES
						+ CommonConstant.STR_SPACE_ONE;
			}
			lsQry.append(
				" AND "
					+ lsList
					+ CommonConstant.STR_SPACE_ONE
					+ CommonConstant.STR_CLOSE_PARENTHESES);
		}

		// handled w/in if (!aaInvInqData.isAllItmsSelected())
		//	// if there is a list of item codes, add that where clause section
		//	 if (lvItmCds.size() > 0)
		//	 {
		//		 lsQry.append(" AND " + lsList);
		//	 }
		// end defect 9706 

		// This will have to change once we have a better idea of  
		//what to do here.
		if (lvInvId != null)
		{
			if (lsInvLocIdCd.equals(InventoryConstant.STR_E))
			{
				lsQry.append(" AND TRANSEMPID IN (" + lsInvIds + ") ");
			}
			else if (lsInvLocIdCd.equals(InventoryConstant.STR_W))
			{
				lsQry.append(" AND TRANWSID IN (" + lsInvIds + ") ");
			}
		}

		// add paternSeqNo to the where clause
		if (aaInvInqData
			.getInvInqBy()
			.equals(InventoryConstant.SPECIFIC_ITM))
		{
			lsQry.append(
				" AND ? between PatrnSeqNo and (PatrnSeqNo+InvQty -1) and "
					+ "PatrnSeqCd = ? ");

			// made convertToString call to be statically referenced
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

		// If Office Issuance No is passed in, make it part of the 
		// where clause.
		//		if (aaInvInqData.getOfcIssuanceNo() != Integer.MIN_VALUE)
		//		{
		//			lsQry.append(" AND OFCISSUANCENO = ?");
		//			lvValues.addElement(
		//				new DBValue(
		//					Types.INTEGER,
		//					DatabaseAccess.convertToString(
		//						aaInvInqData.getOfcIssuanceNo())));
		//		}

		lsQry.append(
			" ORDER BY B.ITMCDDESC, A.InvItmYr, A.PatrnSeqNo ");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryInquiryCurrentBalanceReport - SQL Begin ");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryInquiryCurrentBalanceReport - SQL End   ");
			while (laResultSet.next())
			{
				InventoryInquiryReportData laInvInqRptData =
					new InventoryInquiryReportData();
				laInvInqRptData.setItmCd(
					caDA.getStringFromDB(laResultSet, "VIItmCd"));
				laInvInqRptData.setItmCdDesc(
					caDA.getStringFromDB(laResultSet, "ItmCdDesc"));
				laInvInqRptData.setInvItmYr(
					caDA.getIntFromDB(laResultSet, "InvItmYr"));
				laInvInqRptData.setInvItmNo(
					caDA.getStringFromDB(laResultSet, "InvItmNo"));
				laInvInqRptData.setInvItmEndNo(
					caDA.getStringFromDB(laResultSet, "InvItmEndNo"));

				laInvInqRptData.setPatrnSeqNo(
					caDA.getIntFromDB(laResultSet, "PatrnSeqNo"));
				laInvInqRptData.setInvStatusCd(
					caDA.getIntFromDB(laResultSet, "InvStatusCd"));

				// defect 9254
				// only pass a quantity if the item is not on hold!
				if (laInvInqRptData.getInvStatusCd() < 1)
				{
					laInvInqRptData.setInvQty(
						caDA.getIntFromDB(laResultSet, "InvQty"));
				}
				else
				{
					laInvInqRptData.setInvQty(0);
				}
				// end defect 9254

				laInvInqRptData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, "OfcIssuanceNo"));
				laInvInqRptData.setTransWsId(
					caDA.getIntFromDB(laResultSet, "TransWsId"));
				laInvInqRptData.setTransAmDate(
					caDA.getIntFromDB(laResultSet, "TransAmDate"));
				laInvInqRptData.setTransEmpId(
					caDA.getStringFromDB(laResultSet, "TransEmpId"));
				laInvInqRptData.setRequestorIpAddress(
					caDA.getStringFromDB(laResultSet, "ReqIpAddr"));
				laInvInqRptData.setCustSupplied(
					caDA.getBooleanFromDB(
						laResultSet,
						"CustSuppliedIndi"));
				laInvInqRptData.setInvHoldTimeStmp(
					caDA.getRTSDateFromDB(
						laResultSet,
						"InvHoldTimeStmp"));
				laInvInqRptData.setHoopsRegPltNo(
					caDA.getStringFromDB(laResultSet, "HoopsRegPltNo"));
				// This field is used only in the InventoryInquiryReportData.compareTo 
				// method for reports sorting purposes
				laInvInqRptData.setInvInqBy(aaInvInqData.getInvInqBy());

				laInvInqRptData.setInvLocIdCd("");
				laInvInqRptData.setInvId("");

				laInvInqRptData.setViItmCd(laInvInqRptData.getItmCd());

				// defect 9254
				// Set the Virtual Indicator
				laInvInqRptData.setVirtual(true);
				// end defect 9254

				// Add element to the Vector
				lvRslt.addElement(laInvInqRptData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryInquiryCurrentBalanceReport - End   ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryInquiryCurrentBalanceReport - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	}

	/**
	 * Method to Delete from RTS.RTS_INV_VIRTUAL for Purge.
	 * StatusCd is used to decide what group of records will be deleted.
	 * 
	 * <p>Returns the count of records purged.
	 * 
	 * <p>Only used for Purge.
	 * 
	 * @param  aiPurgeAMDate int
	 * @param  aiPurgeInvStatusCd int
	 * @return int	
	 * @throws RTSException
	 */
	public int purgeInventoryVirtual(
		int aiPurgeAMDate,
		int aiPurgeInvStatusCd)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeInventoryVirtual - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_INV_VIRTUAL WHERE TransAMDate <= ? "
				+ "AND InvStatusCd = ? ";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeAMDate)));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeInvStatusCd)));

		try
		{
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryVirtual - SQL - Begin");

			int liReturnCount =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryVirtual - SQL - End");
			Log.write(Log.METHOD, this, "purgeInventoryVirtual - End");
			return liReturnCount;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeInventoryVirtual - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	// defect 10239 
	//	/**
	//	 * Method to Delete from RTS.RTS_INV_VIRTUAL for Purge for 
	//	 * Incomplete Applications.
	//	 * 
	//	 * <p>Delete only for Purge
	//	 * 
	//	 * @param  aiPurgeAMDate int	
	//	 * @throws RTSException
	//	 * @deprecated
	//	 */
	//	public void purgeInventoryVirtualStrandedPersonal(
	//		int aiPurgeAMDate,
	//		int aiPurgeInvStatusCd)
	//		throws RTSException
	//	{
	//		// TODO Do we need this one?
	//		Log.write(
	//			Log.METHOD,
	//			this,
	//			"purgeInventoryHistoryStrandedPersonal - Begin");
	//
	//		Vector lvValues = new Vector();
	//
	//		String lsDel =
	//			"DELETE FROM RTS.RTS_INV_VIRTUAL WHERE TransAMDate <= ? "
	//				+ "AND InvStatusCd = ? "
	//				+ "AND UserPltNoIndi = 1 "
	//				+ "AND TransTime = 0 "
	//				+ "AND InvHoldTimeStmp < Current Timestamp - 15 minutes";
	//
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(aiPurgeAMDate)));
	//
	//		lvValues.addElement(
	//			new DBValue(
	//				Types.INTEGER,
	//				DatabaseAccess.convertToString(aiPurgeInvStatusCd)));
	//
	//		try
	//		{
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				"purgeInventoryVirtualStrandedPersonal - SQL - Begin");
	//
	//			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				"purgeInventoryVirtualStrandedPersonal - SQL - End");
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				"purgeInventoryVirtualStrandedPersonal - End");
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				"purgeInventoryVirtualStrandedPersonal - Exception - "
	//					+ aeRTSEx.getMessage());
	//			throw aeRTSEx;
	//		}
	//	}
	// end defect 10239 

	/**
	 * Method to update RTS.RTS_INV_VIRTUAL
	 * 
	 * <p>Used to set the InvStatusCd.
	 * Also used to update the row on range splits.
	 *
	 * @param  aaInventoryAllocationData InventoryAllocationData	
	 * @throws RTSException 
	 */
	public void updInventoryVirtual(
		InventoryAllocationData aaInventoryAllocationData,
		String asClientHost)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updInventoryVirtual - Begin " + asClientHost);

		Vector lvValues = new Vector();

		// defect 10505 
		String lsUpd =
			"UPDATE RTS.RTS_INV_VIRTUAL SET InvItmNo = ?, "
				+ "InvItmEndNo = ?, "
				+ "InvQty = ?, "
				+ "PatrnSeqNo = ?, "
				+ "PatrnSeqCd = ?, "
				+ "InvStatusCd = ?,"
				+ "TransId = ? ";
		// end defect 10505 

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvItmNo())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvItmEndNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvQty())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getPatrnSeqNo())));

		// defect 10164 
		lvValues.addElement(new DBValue(
		//Types.CHAR,
		Types.INTEGER,
			DatabaseAccess.convertToString(
				aaInventoryAllocationData.getPatrnSeqCd())));
		// end defect 10164 

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvStatusCd())));

		// defect 10505 
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getTransId())));
		// end defect 10505

		// add trans hold markers as needed
		if (aaInventoryAllocationData.getInvStatusCd() != 0)
		{
			// defect 9467
			lsUpd =
				lsUpd
					+ ","
					+ "MfgPltNo = ?, "
					+ "InvHoldTimeStmp = current timestamp, "
					+ "OfcIssuanceNo = ?, "
					+ "TransWsId = ?, "
					+ "TransAmDate = ?, "
					+ "TransTime = ?, "
					+ "TransEmpId = ?, "
					+ "ReqIpAddr = ?, "
					+ "CustSuppliedIndi = ?, "
					+ "ItrntReqIndi = ? ";
			// end defect 9467

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getMfgPltNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getTransWsId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getTransAmDate())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getTransTime())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getTransEmpId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData
							.getRequestorIpAddress())));
			lvValues.addElement(
				new DBValue(
					Types.BOOLEAN,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.isCustSupplied())));
			// defect 9467
			lvValues.addElement(
				new DBValue(
					Types.BOOLEAN,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.isItrntReq())));
			// end defect 9467

			// do not update the hoops if the passed value is null
			if (aaInventoryAllocationData.getHoopsRegPltNo() != null)
			{
				lsUpd = lsUpd + "," + "HoopsRegPltNo = ? ";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getHoopsRegPltNo())));
			}
		}
		else
		{
			// the hold is being removed
			// defect 9467
			lsUpd =
				lsUpd
					+ ","
					+ "MfgPltNo = '', "
					+ "OfcIssuanceNo = 0, "
					+ "TransWsId = 0, "
					+ "TransAmDate = 0, "
					+ "TransTime = 0, "
					+ "TransEmpId = '', "
					+ "ReqIpAddr = '', "
					+ "CustSuppliedIndi = 0, "
					+ "ItrntReqIndi = 0, "
					+ "HoopsRegPltNo = '' ";
			// end defect 9467
		}
		// end hold

		lsUpd =
			lsUpd
				+ " WHERE VIItmCd = ? AND "
				+ "InvItmYr = ? AND "
				+ "InvItmNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmYr())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmNo())));

			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtual - SQL - Begin " + asClientHost);

			// defect 9404
			//			logWriteInvAlloc(
			//				aaInventoryAllocationData,
			//				"updInventoryVirtual " + asClientHost);
			// end defect 9404

			int liReturnCount =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			if (liReturnCount < 1)
			{
				// count is off!!!
				System.out.println("Count is wrong " + asClientHost);
				throw new RTSException();
			}
			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtual - SQL - End " + asClientHost);
			Log.write(
				Log.METHOD,
				this,
				"updInventoryVirtual - End " + asClientHost);
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryVirtual - "
					+ asClientHost
					+ " Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	}

	/**  
	 * This has the effect of putting the row(s) in exclusive lock status.
	 * 
	 * @param  aaInventoryAllocationData InventoryAllocationData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updInventoryVirtualForLogicalLock(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updInventoryVirtualForLogicalLock - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			" UPDATE RTS.RTS_INV_VIRTUAL SET VIITMCD = ? "
				+ " WHERE VIITMCD = ? "
				+ " AND INVITMYR = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getViItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getViItmCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmYr())));

			// If the PatrnSeqNo is greater than 0, use it.
			// Otherwise, if HoopsRegPltNo is not null and User Defined
			// Plate No, use that.
			// Otherwise, use InvItmNo.
			if (aaInventoryAllocationData.getPatrnSeqNo() > 0)
			{
				lsUpd =
					lsUpd
						+ " AND PATRNSEQCD = ? "
						+ " AND ((? between PatrnSeqNo and (PatrnSeqNo+InvQty -1)) ";

				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getPatrnSeqCd())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getPatrnSeqNo())));

				// if there is an EndPatrnSeqNo, use it here
				if (aaInventoryAllocationData.getEndPatrnSeqNo() > 0)
				{
					lsUpd = lsUpd + " OR (PATRNSEQNO BETWEEN ? AND ?)";
					//8
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaInventoryAllocationData
									.getPatrnSeqNo())));
					//9
					lvValues.addElement(
						new DBValue(
							Types.INTEGER,
							DatabaseAccess.convertToString(
								aaInventoryAllocationData
									.getEndPatrnSeqNo())));
				}

				// close out the statement		
				lsUpd = lsUpd + ")";

			}
			else if (
				aaInventoryAllocationData.getHoopsRegPltNo() != null
					&& aaInventoryAllocationData.isUserPltNo())
			{
				lsUpd = lsUpd + " HoopsRegPltNo = ? ";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData
								.getHoopsRegPltNo())));
			}
			else
			{
				lsUpd = lsUpd + " InvItmNo = ? ";
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData.getInvItmNo())));
			}

			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualForLogicalLock - SQL - Begin");

			// defect 9404
			//			logWriteInvAlloc(
			//				aaInventoryAllocationData,
			//				"updInventoryVirtualForLogicalLock ");
			// end defect 9404

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualForLogicalLock - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updInventoryVirtualForLogicalLock - End");

			return (liNumRows);
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryVirtualForLogicalLock - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	}

	/**
	 * Method to Complete all Virtual Inventory on Hold for 
	 * Print Immediate transactions which will be completed by 
	 * Batch    
	 */
	public void updInventoryVirtualForBatch(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updInventoryVirtualForBatch - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_INV_VIRTUAL A SET INVSTATUSCD = 6 "
				+ " WHERE INVSTATUSCD = 2 AND EXISTS (SELECT REGPLTNO FROM "
				+ " RTS.RTS_SR_FUNC_TRANS B, RTS.RTS_TRANS_HDR C WHERE "
				+ " B.OFCISSUANCENO = ? AND "
				+ " B.SUBSTAID = ? AND "
				+ " A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ " A.TRANSWSID = B.TRANSWSID AND "
				+ " A.TRANSAMDATE = B.TRANSAMDATE AND "
				+ " A.TRANSTIME = B.TRANSTIME AND "
				+ " A.INVITMNO = B.REGPLTNO AND "
				+ " B.OFCISSUANCENO = C.OFCISSUANCENO AND "
				+ " B.TRANSWSID = C.TRANSWSID AND "
				+ " B.TRANSAMDATE = C.TRANSAMDATE AND "
				+ " B.CUSTSEQNO = C.CUSTSEQNO AND "
				+ " C.TRANSTIMESTMP IS NULL AND"
				+ " C.TRANSAMDATE < ? AND "
			// defect 10184 
		// + " C.PRINTIMMEDIATE = 1 )";
	+" C.PRINTIMMEDIATE in (1,2))";
		// end defect 10184 

		try
		{
			// defect 10164 
			// 1
			lvValues.addElement(new DBValue(
			//Types.CHAR,
			Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getOfcIssuanceNo())));
			//2
			lvValues.addElement(new DBValue(
			//Types.CHAR,
			Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getSubstaId())));

			// 3
			lvValues.addElement(new DBValue(
			//Types.CHAR,
			Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getTransAmDate())));
			// end defect 10164 

			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualForBatch - SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);

			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualForBatch - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updInventoryVirtualInvStatusCd - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryVirtualInvStatusCd - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Release all system locks on RTS.RTS_INV_VIRTUAL that 
	 * have expired.
	 * 
	 * <p>This method does handle IVTRS VI.
	 * 
	 * <p>Do not release the Customer Supplied Plates.
	 * 
	 * @throws RTSException
	 */
	public int updInventoryVirtualInvStatusCdItrnt()
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updInventoryVirtualInvStatusCdItrnt - Begin");

		Vector lvValues = new Vector();

		// defect 9606
		// modified to just subtract "days" from the timestamp.
		// defect 9681
		// needed a space in string
		String lsDel =
			"UPDATE RTS.RTS_INV_VIRTUAL "
				+ "SET InvStatusCd = 0 "
				+ "WHERE InvStatusCd = 2 "
				+ "AND CustSuppliedIndi = 0 "
				+ "AND TransTime = 0 "
				+ "AND ItrntReqIndi = 1 "
				+ "AND InvHoldTimeStmp < "
				+ "CURRENT TIMESTAMP - "
				+ SystemProperty.getPurgeViIvtrsReleaseDays()
				+ " Days";
		// end defect 9681
		// end defect 9606

		try
		{
			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualInvStatusCdItrnt - SQL - Begin");

			int liReleaseCount =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualInvStatusCdItrnt - SQL - "
					+ "Rows Released "
					+ liReleaseCount);
			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualInvStatusCdItrnt - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updInventoryVirtualInvStatusCdItrnt - End");
			return liReleaseCount;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryVirtualInvStatusCdItrnt - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}

	/**
	 * Method to Release all system locks on RTS.RTS_INV_VIRTUAL that 
	 * have expired.
	 * 
	 * <p>This method does not handle IVTRS VI.
	 * 
	 * <p>Do not release the Customer Supplied Plates.
	 * 
	 * @throws RTSException
	 */
	public int updInventoryVirtualInvStatusCdNonItrnt()
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updInventoryVirtualInvStatusCdNonItrnt - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"UPDATE RTS.RTS_INV_VIRTUAL "
				+ "SET InvStatusCd = 0 "
				+ "WHERE InvStatusCd = 2 "
				+ "AND CustSuppliedIndi = 0 "
				+ "AND TransTime = 0 "
				+ "AND ItrntReqIndi <> 1 "
				+ "AND InvHoldTimeStmp < Current Timestamp - 15 minutes";

		try
		{
			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualInvStatusCdNonItrnt - SQL - Begin");

			int liReleaseCount =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualInvStatusCdNonItrnt - SQL - "
					+ "Rows Released "
					+ liReleaseCount);
			Log.write(
				Log.SQL,
				this,
				"updInventoryVirtualInvStatusCdNonItrnt - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updInventoryVirtualInvStatusCdNonItrnt - End");
			return liReleaseCount;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryVirtualInvStatusCdNonItrnt - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
}
