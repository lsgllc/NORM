package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * InventoryAllocation.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/18/2001 	Altered Query on InventoryAllocation for 
 *							InvId,InvLocIdCd
 * K Harrell	11/14/2001  Added qryInventoryAllocationIntersection
 * K Harrell	11/28/2001  Added updInventoryAllocationInvStatusCd
 * K Harrell	11/30/2001  Renamed above to ...ForLogicalLock
 *            	            Added where InvStatusCd = 0
 *                          Reduced number of parameters on 
 *							qryInventoryAllocation
 * K Harrell	12/01/2001  Altered qryInventoryAllocation for InvStatusCd
 * K Harrell	12/13/2001  Added updInventoryAllocationInvStatusCd
 * K Harrell	01/10/2002  Added qryMaxSubstaIdForInventoryAllocation
 * K Harrell	03/25/2002  Added IsSubscribing
 * R Hicks		07/12/2002  Add call to closeLastStatement() after a 
 *							query
 * Min Wang		08/25/2002	Fixed defect(CQU100004668) Added qryGetAllInv()
 * Ray Rowehl	01/09/2003  Modified updInventoryAllocationForLogicalLock().
 *							Defect CQU100005201
 * Min Wang		04/11/2003  Modified qryInventoryAllocation(). defect 5968.
 * Min Wang		12/15/2003  Pass parameter OfcIssuanceNo and ItmCd from 
 *							InvReCalc (optional).
 *                     		Added to overloading method qryGetAllInv() 
 *							to pass	ofcissuanceno and item code.  
 *							This will allow the query to return less data
 *							when required.
 *							Modified qryInventoryAllocationIntersection()
 * 							to allow check for all substations.
 *							added qryGetAllInv(int, string)
 *							modified qryInventoryAllocationIntersection()
 * 							Defect 6745  Version 5.1.5  Fix2
 * K Harrell	10/04/2004	Add order clause for retrieving subcon inv
 *							modify qryInventoryAllocationForSubcon()
 *							defect 7586 Ver 5.2.1
 * Min Wang		06/24/2005	Allow Pattern Seq. Cd. to be updated.
 * 							modify updInventoryAllocation()
 * 							defect 8244 Ver 5.2.2 Fix 5
 * K Harrell	11/03/2005	Java 1.4 Work
 *							Ver 5.2.3 
 * Ray Rowehl	07/18/2007	Sort the query.  Looking to order
 * 							the data for hold / release.
 *							modify qryInventoryAllocation()
 *							defect 9116 Ver Special Plates
 * Ray Rowehl 	09/07/2007	Set the Virtual boolean to false 
 *							since this is actual inventory.
 *							modify qryGetAllInv(), 
 *								qryGetAllInv(int, String)
 *							defect 9116 Ver Special Plates
 * K Harrell	06/26/2009	Corrected typos in various log.write stmts 
 * 							delete qryGetAllInv() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/13/2011	add qryInventoryAllocationSubstaIdForSubcon()
 * 							add csMethod 
 * 							defect 11178 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with database. 
 *
 * @version	6.9.0 			12/13/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2001 
 */

public class InventoryAllocation extends InventoryAllocationData
{
	DatabaseAccess caDA;
	
	// defect 11178 
	String csMethod = new String();
	// end defect 11178 
	
	/**
	 * InventoryAllocation constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public InventoryAllocation(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	
	/**
	 * Method to Delete from RTS.RTS_INV_ALLOCATION
	 *
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @throws RTSException 
	 */
	public void delInventoryAllocation(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "delInventoryAllocation - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_INV_ALLOCATION "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubStaId = ? AND "
				+ "ItmCd = ? AND "
				+ "InvItmYr = ? AND "
				+ "InvItmNo = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getSubstaId())));
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
				"delInventoryAllocation - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"delInventoryAllocation - SQL - End");
			Log.write(Log.METHOD, this, "delInventoryAllocation - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delInventoryAllocation - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Delete METHOD
	
	/**
	 * Method to Insert into RTS.RTS_INV_ALLOCATION
	 *
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @throws RTSException 
	 */
	public void insInventoryAllocation(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insInventoryAllocation - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_INV_ALLOCATION ("
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvId,"
				+ "InvLocIdCd,"
				+ "InvItmEndNo,"
				+ "InvQty,"
				+ "PatrnSeqNo,"
				+ "InvStatusCd,"
				+ "PatrnSeqCd ) VALUES ( "
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
				+ " ?)";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getSubstaId())));
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
						aaInventoryAllocationData.getInvId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvLocIdCd())));
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
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvStatusCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getPatrnSeqCd())));
			Log.write(
				Log.SQL,
				this,
				"insInventoryAllocation - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(
				Log.SQL,
				this,
				"insInventoryAllocation - SQL - End");
			Log.write(Log.METHOD, this, "insInventoryAllocation - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insInventoryAllocation - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF INSERT METHOD
	
//	/**
//	 * Method to Query RTS.RTS_INV_ALLOCATION to get all Inventory
//	 *
//	 * @return  Vector
//	 * @throws  RTSException 
//	 */
//	public Vector qryGetAllInv() throws RTSException
//	{
//		Log.write(Log.METHOD, this, " - qryGetAllInv - Begin");
//
//		StringBuffer lsQry = new StringBuffer();
//
//		Vector lvRslt = new Vector();
//
//		Vector lvValues = new Vector();
//
//		ResultSet lrsQry;
//
//		lsQry.append(
//			"SELECT "
//				+ "OfcIssuanceNo,"
//				+ "SubstaId,"
//				+ "ItmCd,"
//				+ "InvItmYr,"
//				+ "InvItmNo,"
//				+ "InvId,"
//				+ "InvLocIdCd,"
//				+ "InvItmEndNo,"
//				+ "InvQty,"
//				+ "PatrnSeqNo,"
//				+ "InvStatusCd,"
//				+ "PatrnSeqCd "
//				+ "FROM RTS.RTS_INV_ALLOCATION ");
//		try
//		{
//			Log.write(Log.SQL, this, " - qryGetAllInv - SQL - Begin");
//			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
//			Log.write(Log.SQL, this, " - qryGetAllInv - SQL - End");
//
//			while (lrsQry.next())
//			{
//				InventoryAllocationData laIAData =
//					new InventoryAllocationData();
//				laIAData.setOfcIssuanceNo(
//					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
//				laIAData.setSubstaId(
//					caDA.getIntFromDB(lrsQry, "SubstaId"));
//				laIAData.setItmCd(
//					caDA.getStringFromDB(lrsQry, "ItmCd"));
//				laIAData.setInvItmYr(
//					caDA.getIntFromDB(lrsQry, "InvItmYr"));
//				laIAData.setInvItmNo(
//					caDA.getStringFromDB(lrsQry, "InvItmNo"));
//				laIAData.setInvId(
//					caDA.getStringFromDB(lrsQry, "InvId"));
//				laIAData.setInvLocIdCd(
//					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
//				laIAData.setInvItmEndNo(
//					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
//				laIAData.setInvQty(
//					caDA.getIntFromDB(lrsQry, "InvQty"));
//				laIAData.setPatrnSeqNo(
//					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
//				laIAData.setInvStatusCd(
//					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
//				laIAData.setPatrnSeqCd(
//					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
//					
//				// defect 9116
//				// set the virtual boolean to be false
//				laIAData.setVirtual(false);
//				// end defect 9116
//				
//				// Add element to the Vector
//				lvRslt.addElement(laIAData);
//			} //End of While
//			lrsQry.close();
//			caDA.closeLastDBStatement();
//			lrsQry = null;
//			Log.write(Log.METHOD, this, " - qryGetAllInv - End ");
//			return (lvRslt);
//		}
//		catch (SQLException leSQLEx)
//		{
//			Log.write(
//				Log.SQL_EXCP,
//				this,
//				" - qryGetAllInv - Exception " + leSQLEx.getMessage());
//			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
//		}
//	} //END OF QUERY METHOD
	
	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION to get all Inventory
	 *  given OfcIssuanceNo, ItmCd
	 *
	 * @param  aiOfcissuanceNo	int
	 * @param  asItmCd			String
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryGetAllInv(int aiOfcissuanceNo, String asItmCd)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryGetAllInv - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvId,"
				+ "InvLocIdCd,"
				+ "InvItmEndNo,"
				+ "InvQty,"
				+ "PatrnSeqNo,"
				+ "InvStatusCd,"
				+ "PatrnSeqCd "
				+ "FROM RTS.RTS_INV_ALLOCATION ");
		try
		{
			if (aiOfcissuanceNo > 0
				|| (asItmCd != null && asItmCd.length() > 0))
			{
				lsQry.append("where ");
			}
			if (aiOfcissuanceNo > 0)
			{
				lsQry.append("OfcIssuanceNo = ? ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aiOfcissuanceNo)));
			}
			if (aiOfcissuanceNo > 0
				&& (asItmCd != null && asItmCd.length() > 0))
			{
				lsQry.append("and ");
			}
			if (asItmCd != null && asItmCd.length() > 0)
			{
				//only for particular item
				lsQry.append("ItmCd = ? ");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(asItmCd)));
			}
			lsQry.append(
				"order by OfcIssuanceNo, SubstaId, ItmCd, InvItmYr");

			Log.write(Log.SQL, this, " - qryGetAllInv - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryGetAllInv - SQL - End");

			while (lrsQry.next())
			{
				InventoryAllocationData laIAData =
					new InventoryAllocationData();
				laIAData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laIAData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laIAData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laIAData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laIAData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laIAData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laIAData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
				laIAData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laIAData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laIAData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laIAData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laIAData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
					
				// defect 9116
				// set the virtual boolean to be false
				laIAData.setVirtual(false);
				// end defect 9116
				
				// Add element to the Vector
				lvRslt.addElement(laIAData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryGetAllInv - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryGetAllInv - Exception " + leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION
	 * 
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryAllocation(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryAllocation - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvId,"
				+ "InvLocIdCd,"
				+ "InvItmEndNo,"
				+ "InvQty,"
				+ "PatrnSeqNo,"
				+ "InvStatusCd,"
				+ "PatrnSeqCd "
				+ "FROM RTS.RTS_INV_ALLOCATION "
				+ "where "
				+ "OfcIssuanceno = ? and "
				+ "SubstaId = ? and ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getSubstaId())));

		if (aaInventoryAllocationData.getInvId() != null)
		{
			lsQry.append("InvId = ? and InvLocIdCd = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvId())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvLocIdCd())));
		}
		else
		{
			lsQry.append("InvStatusCd = ? ");
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvStatusCd())));
		}
		if (aaInventoryAllocationData.getItmCd() != null)
		{
			lsQry.append(" and ItmCd = ? ");
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getItmCd())));

			if (aaInventoryAllocationData.getInvItmYr() > 0)
			{
				lsQry.append(" and InvItmYr = ? ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData.getInvItmYr())));
			}
			lsQry.append(" ORDER BY ItmCd, InvItmYr, InvItmNo ");
		}

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocation - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocation - SQL - End");

			while (lrsQry.next())
			{
				InventoryAllocationData laInventoryAllocationData =
					new InventoryAllocationData();
				laInventoryAllocationData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryAllocationData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventoryAllocationData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInventoryAllocationData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventoryAllocationData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laInventoryAllocationData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laInventoryAllocationData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
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
				// Add element to the Vector
				lvRslt.addElement(laInventoryAllocationData);
			} //End of While 
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryAllocation - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryAllocation - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION
	 * RTS I - INVALLS1,S2 (S447763.LST) INVALLS4 (Dynamic - S447700)
	 *  
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryAllocationForSpecificItem(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryAllocationForSpecificItem - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvId,"
				+ "InvLocIdCd,"
				+ "InvItmEndNo,"
				+ "InvQty,"
				+ "PatrnSeqNo,"
				+ "InvStatusCd,"
				+ "PatrnSeqCd "
				+ "FROM RTS.RTS_INV_ALLOCATION "
				+ "where "
				+ "OfcIssuanceno = ? and "
				+ "SubstaId = ? and "
				+ "Itmcd = ? and InvItmYr = ? and ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getSubstaId())));
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

		if (aaInventoryAllocationData.getPatrnSeqNo()
			!= Integer.MIN_VALUE)
		{
			lsQry.append(
				" ? between PatrnSeqNo and (PatrnSeqNo+InvQty -1) and  PatrnSeqCd = ? ");
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
				" - qryInventoryAllocationForSpecificItem - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocationForSpecificItem - SQL - End");

			while (lrsQry.next())
			{
				InventoryAllocationData laInventoryAllocationData =
					new InventoryAllocationData();
				laInventoryAllocationData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryAllocationData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventoryAllocationData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInventoryAllocationData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventoryAllocationData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laInventoryAllocationData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laInventoryAllocationData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
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
				// Add element to the Vector
				lvRslt.addElement(laInventoryAllocationData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryAllocationForSpecificItem - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryAllocationForSpecificItem - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION
	 * 		
	 * @param  aaInventoryAllocationData  InventoryAllocationData 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryAllocationForSubcon(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryAllocationForSubcon - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		// defect 7586
		// Order by itmcd,patrnseqcd,patrnseqno 
		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "InvId,"
				+ "InvLocIdCd,"
				+ "InvItmEndNo,"
				+ "InvQty,"
				+ "PatrnSeqNo,"
				+ "InvStatusCd,"
				+ "PatrnSeqCd "
				+ "FROM RTS.RTS_INV_ALLOCATION "
				+ "where "
				+ "OfcIssuanceno = ? and "
				+ "SubstaId = ? and "
				+ "InvStatusCd = ? and "
				+ "Invid = ? and "
				+ "Invlocidcd = ? order by itmcd,patrnseqcd,patrnseqno");
		// end defect 7586

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvStatusCd())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvLocIdCd())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocationForSubcon - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocationForSubcon - SQL - End");

			while (lrsQry.next())
			{
				InventoryAllocationData laInventoryAllocationData =
					new InventoryAllocationData();
				laInventoryAllocationData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryAllocationData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventoryAllocationData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInventoryAllocationData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventoryAllocationData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laInventoryAllocationData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laInventoryAllocationData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
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
				// Add element to the Vector
				lvRslt.addElement(laInventoryAllocationData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryAllocationForSubcon - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryAllocationForSubcon - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION
	 * 		
	 * @param  aiOfcIssuanceNo
	 * @param  aiSubstaId
	 * @param  asItmCd  
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryAllocationSubstaIdForSubcon(int aiOfcIssuanceNo,int aiSubstaId,String asItmCd)
		throws RTSException
	{
		csMethod = "qryInventoryAllocationSubstaIdForSubcon"; 
		Log.write(
			Log.METHOD,
			this,
			csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "Distinct "
				+ "SubstaId "
				+ "FROM RTS.RTS_INV_ALLOCATION "
				+ "where "
				+ "OfcIssuanceno = ? and "
				+ "Invid = ? and "
				+ "Invlocidcd = 'S' and "
				+ "ItmCd = ? order by 1");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aiOfcIssuanceNo)));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aiSubstaId)));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
						asItmCd)));
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				int liSubstaId = 
					caDA.getIntFromDB(lrsQry, "SubstaId");
				lvRslt.addElement(new Integer(liSubstaId)); 
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				csMethod + " - End ");
			return lvRslt;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Query RTS.RTS_INV_ALLOCATION
	 *
	 * @param  aaInventoryAllocationData  InventoryAllocationData	
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryInventoryAllocationIntersection(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryInventoryAllocationIntersection - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		try
		{
			lsQry.append(
				" SELECT OFCISSUANCENO,SUBSTAID,ITMCD, INVITMYR, INVITMNO, INVID, INVLOCIDCD, "
					+ " INVITMENDNO, INVQTY, PATRNSEQNO, INVSTATUSCD, "
					+ " PATRNSEQCD FROM RTS.RTS_INV_ALLOCATION "
					+ " WHERE "
					+ " OFCISSUANCENO = ? AND ");

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));

			// if substaid is min value, do not include it in query
			if (aaInventoryAllocationData.getSubstaId()
				!= Integer.MIN_VALUE)
			{
				lsQry.append(" SUBSTAID = ? AND ");
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaInventoryAllocationData.getSubstaId())));
			}
			lsQry.append(
				" ITMCD = ?  AND INVITMYR = ? AND "
					+ " PATRNSEQCD = ?  AND ((? BETWEEN "
					+ " PATRNSEQNO AND (PATRNSEQNO+INVQTY-1)) OR ( "
					+ " ? BETWEEN PATRNSEQNO AND "
					+ " (PATRNSEQNO+INVQTY-1)) OR (PATRNSEQNO BETWEEN  "
					+ " ?  AND ? )) "
					+ " ORDER BY PATRNSEQCD,PATRNSEQNO");
 
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
						aaInventoryAllocationData.getPatrnSeqCd())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getPatrnSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getEndPatrnSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getPatrnSeqNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getEndPatrnSeqNo())));
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocationIntersection - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryInventoryAllocationIntersection - SQL - End");

			while (lrsQry.next())
			{
				InventoryAllocationData laInventoryAllocationData =
					new InventoryAllocationData();
				laInventoryAllocationData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laInventoryAllocationData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laInventoryAllocationData.setItmCd(
					caDA.getStringFromDB(lrsQry, "ItmCd"));
				laInventoryAllocationData.setInvItmYr(
					caDA.getIntFromDB(lrsQry, "InvItmYr"));
				laInventoryAllocationData.setInvItmNo(
					caDA.getStringFromDB(lrsQry, "InvItmNo"));
				laInventoryAllocationData.setInvItmEndNo(
					caDA.getStringFromDB(lrsQry, "InvItmEndNo"));
				laInventoryAllocationData.setInvId(
					caDA.getStringFromDB(lrsQry, "InvId"));
				laInventoryAllocationData.setInvLocIdCd(
					caDA.getStringFromDB(lrsQry, "InvLocIdCd"));
				laInventoryAllocationData.setInvQty(
					caDA.getIntFromDB(lrsQry, "InvQty"));
				laInventoryAllocationData.setPatrnSeqNo(
					caDA.getIntFromDB(lrsQry, "PatrnSeqNo"));
				laInventoryAllocationData.setInvStatusCd(
					caDA.getIntFromDB(lrsQry, "InvStatusCd"));
				laInventoryAllocationData.setPatrnSeqCd(
					caDA.getIntFromDB(lrsQry, "PatrnSeqCd"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryAllocationData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryInventoryAllocationIntersection - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryInventoryAllocationIntersection - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Determine if subscribing
	 * 
	 * @param  aaInventoryAllocationData  InventoryAllocationData
	 * @return Vector
	 * @throws RTSException 
	 */
	public int qryIsSubscribing(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryIsSubscribing - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		int liIsSubscribing = 0;

		String lsTblName = "";

		if (aaInventoryAllocationData.getInvLocIdCd() == "D")
		{
			lsTblName = "RTS_DEALERS";
		}
		if (aaInventoryAllocationData.getInvLocIdCd() == "S")
		{
			lsTblName = "RTS_SUBCON";
		}
		if (aaInventoryAllocationData.getInvLocIdCd() == "E")
		{
			lsTblName = "RTS_SECURITY";
		}

		lsQry.append(
			"Select distinct 1 as IsSubscribing from RTS.RTS_SUBSTA_SUBSCR where "
				+ " tblname = '"
				+ lsTblName
				+ "' and "
				+ " ofcissuanceno = ? and "
				+ " (substaid = ? or "
				+ " (? = 0 and "
				+ " exists "
				+ " (select * from RTS.RTS_SUBSTA_SUBSCR where ofcissuanceno = ? and tblname = "
				+ "'"
				+ lsTblName
				+ "'"
				+ " )"
				+ " )"
				+ " )");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getSubstaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getOfcIssuanceNo())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryIsSubscribing - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryIsSubscribing - SQL - End");

			while (lrsQry.next())
			{
				liIsSubscribing =
					caDA.getIntFromDB(lrsQry, "IsSubscribing");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryIsSubscribing - End ");
			return (liIsSubscribing);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryIsSubscribing - Exception " + leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
	
	/**
	 * Method to Determine Max Substaid for Inventory 
	 * 
	 * @param  aaInventoryAllocationData  InventoryAllocationData
	 * @return Vector
	 * @throws RTSException 
	 */
	public int qryMaxSubstaIdForInventoryAllocation(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" -  qryMaxSubstaIdForInventoryAllocation1 - Begin");

		StringBuffer lsQry = new StringBuffer();

		ResultSet lrsQry;

		Vector lvValues = new Vector();

		String lsTblName = "";

		int liMaxSubstaId = Integer.MIN_VALUE;

		if (aaInventoryAllocationData.getInvLocIdCd() == "D")
		{
			lsTblName = "RTS_DEALERS";
		}
		if (aaInventoryAllocationData.getInvLocIdCd() == "S")
		{
			lsTblName = "RTS_SUBCON";
		}
		if (aaInventoryAllocationData.getInvLocIdCd() == "E")
		{
			lsTblName = "RTS_SECURITY";
		}

		int liIsSubscribing =
			qryIsSubscribing(aaInventoryAllocationData);

		if (liIsSubscribing == 1)
		{
			lsQry.append(
				"Select DISTINCT SubstaId from RTS.RTS_INV_ALLOCATION a where "
					+ " InvLocIdCd = ? and "
					+ " InvId = ? and "
					+ " ofcissuanceno = ? and "
					+ " (substaid = 0 or substaid = "
					+ " (select max(substaid) from RTS.RTS_INV_ALLOCATION B "
					+ " where a.ofcissuanceno = b.ofcissuanceno and a.substaid = b.substaid and B.InvLocIdCd = ? "
					+ " and B.InvId = ? and "
					+ " substaid in "
					+ " (select SubstaId from RTS.RTS_SUBSTA_SUBSCR where ofcissuanceno = ? and tblname = '"
					+ lsTblName
					+ "')))");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvLocIdCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvLocIdCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
		}
		else
		{
			lsQry.append(
				"Select distinct SubstaId from RTS.RTS_INV_ALLOCATION where "
					+ " InvLocIdCd = ? and "
					+ " InvId = ? and "
					+ " OfcIssuanceNo = ? and "
					+ " substaid = ? ");

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvLocIdCd())));
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvId())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getSubstaId())));
		}
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryMaxSubstaIdForInventoryAllocation1 - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryMaxSubstaIdForInventoryAllocation1 - SQL - End");

			while (lrsQry.next())
			{
				liMaxSubstaId = caDA.getIntFromDB(lrsQry, "SubstaId");
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" -  qryMaxSubstaIdForInventoryAllocation1 - End ");
			return (liMaxSubstaId);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryMaxSubStaIdForInventoryAllocation - Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}
	
	/**
	 * Method to update RTS.RTS_INV_ALLOCATION
	 *
	 * @param  aaInventoryAllocationData InventoryAllocationData	
	 * @throws RTSException 
	 */
	public void updInventoryAllocation(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updInventoryAllocation - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_INV_ALLOCATION SET InvItmNo = ?, "
				+ "InvId = ?, "
				+ "InvLocIdCd = ?, "
				+ "InvItmEndNo = ?, "
				+ "InvQty = ?, "
				+ "PatrnSeqNo = ?, "
			// defect 8244
	+"PatrnSeqCd = ?, "
			// end defect 8244
	+"InvStatusCd = ? ";
		//1
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvItmNo())));
		//2
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvId())));
		//3
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvLocIdCd())));
		//4
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvItmEndNo())));
		//5
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvQty())));
		//6
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getPatrnSeqNo())));
		// defect 8244
		//7
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getPatrnSeqCd())));
		// end defect 8244
		//8
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInventoryAllocationData.getInvStatusCd())));
		if (aaInventoryAllocationData.getNewSubstaId()
			!= Integer.MIN_VALUE)
		{
			lsUpd = lsUpd + ", SubstaId = ? ";
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getNewSubstaId())));
		}
		lsUpd =
			lsUpd
				+ " WHERE OfcIssuanceNo = ? AND "
				+ "SubStaId = ? AND "
				+ "ItmCd = ? AND "
				+ "InvItmYr = ? AND "
				+ "InvItmNo = ? ";
		try
		{
			//9
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			//10
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getSubstaId())));
			//11
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getItmCd())));
			//12
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmYr())));
			//13
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmNo())));

			Log.write(
				Log.SQL,
				this,
				"updInventoryAllocation - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updInventoryAllocation - SQL - End");
			Log.write(Log.METHOD, this, "updInventoryAllocation - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryAllocation - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Update METHOD
	
	/**
	 * Method to "update" RTS.RTS_INV_ALLOCATION.  
	 * This has the effect of putting the row(s) in exclusive lock status.
	 * 
	 * @param  aaInventoryAllocationData InventoryAllocationData	
	 * @return int
	 * @throws RTSException 
	 */
	public int updInventoryAllocationForLogicalLock(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updInventoryAllocationForLogicalLock - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			" UPDATE RTS.RTS_INV_ALLOCATION SET ITMCD = ? "
				+ " WHERE OFCISSUANCENO = ? "
				+ " AND INVSTATUSCD = 0 "
				+ " AND SUBSTAID = ? "
				+ " AND ITMCD = ? "
				+ " AND INVITMYR = ? "
				+ " AND PATRNSEQCD = ? "
				+ " AND ("
				+ "(? BETWEEN PATRNSEQNO AND (PATRNSEQNO+INVQTY-1)) ";
		try
		{
			//1
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getItmCd())));
			//2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			//3
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getSubstaId())));
			//4
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getItmCd())));
			//5
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getInvItmYr())));
			//6
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getPatrnSeqCd())));
			//7
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getPatrnSeqNo())));

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

			Log.write(
				Log.SQL,
				this,
				"updInventoryAllocationForLogicalLock - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updInventoryAllocationForLogicalLock - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updInventoryAllocationForLogicalLock - End");

			return (liNumRows);
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryAllocationForLogicalLock - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Update METHOD
	
	/**
	 * Method to update RTS.RTS_INV_ALLOCATION
	 * RTSI - INVALLUK
	 * 
	 * @param  aaInventoryAllocationData InventoryAllocationData
	 * @throws RTSException 
	 */
	public void updInventoryAllocationInvStatusCd(InventoryAllocationData aaInventoryAllocationData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"updInventoryAllocationInvStatusCd - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			" UPDATE RTS.RTS_INV_ALLOCATION SET INVSTATUSCD = 0 "
				+ " WHERE OFCISSUANCENO = ? AND SUBSTAID = ? AND INVSTATUSCD = 2 ";
		try
		{
			//1
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getOfcIssuanceNo())));
			//2
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaInventoryAllocationData.getSubstaId())));
			Log.write(
				Log.SQL,
				this,
				"updInventoryAllocationInvStatusCd - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(
				Log.SQL,
				this,
				"updInventoryAllocationInvStatusCd - SQL - End");
			Log.write(
				Log.METHOD,
				this,
				"updInventoryAllocationInvStatusCd - End");
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updInventoryAllocationInvStatusCd - Exception - "
					+ leRTSEx.getMessage());
			throw leRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
