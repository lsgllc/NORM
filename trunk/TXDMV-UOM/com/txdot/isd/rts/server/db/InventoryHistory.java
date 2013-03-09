package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.InventoryHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * InventoryHistory.java
 *
 * (c) Texas Department of Transportation 2001 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/18/2001	Added purgeInventoryHistory
 * K Harrell	11/07/2001	Added insInventoryHistory
 * K Harrell	06/06/2002	Added call to update RTS_Inv_Hstry_Log 
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	09/23/2002	Redundant ref to Trans_Hdr 
 *							modify insInventoryHistory()
 *							defect 4781
 * M Wang		01/17/2003	Added insInventoryHistory(InventoryHistoryData)
 *							to insert rows directly when processing 
 *							Receives and Deletes.
 *							defect 5111.
 * K Harrell	06/30/2005	Java 1.4 Work
 *							deprecate insInventoryHistory() 
 *							defect 7899 Ver 5.2.3
 * K Harrell	11/11/2005	Accept passed int vs. 
 * 							InventoryHistoryData Object
 *							delete insInventoryHistory() 
 * 							modify purgeInventoryHistory() 
 * 							defect 8423 Ver 5.2.3  
 * K Harrell	01/05/2009	Assign TransEmpId to "LGNERR" if null || 
 * 							TransEmpId.trim().length() == 0 on insert. 
 * 							modify insInventoryHistory()    
 *	 						defect 9847 Ver Defect_POS_D 
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeInventoryHistory()
 * 							defect 9825 Ver Defect_POS_D    
 * K Harrell	03/19/2010	Remove unused method.
 * 							delete qryInventoryHistory()
 * 							defect 10239 Ver POS_640  
 * K Harrell	06/15/2010  add TransId to insert
 * 							modify insInventoryHistory()
 * 							defect 10505 Ver 6.5.0       
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to interact with the database.
 * (RTS_INV_HSTRY) 
 *
 * @version	6.5.0  			06/15/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2001 13:35:39
 */

public class InventoryHistory extends InventoryHistoryData
{
	DatabaseAccess caDA;

	/**
	 * InventoryHistory constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public InventoryHistory(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert object into RTS.RTS_INV_HSTRY
	 * 
	 * @param  aaInvHistData  InventoryHistoryData	
	 * @throws RTSException 
	 */
	public void insInventoryHistory(InventoryHistoryData aaInvHistData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insInventoryHistory - Begin");

		Vector lvValues = new Vector();

		// defect 9847 
		if (aaInvHistData.getTransEmpId() == null
			|| aaInvHistData.getTransEmpId().trim().length() == 0)
		{
			aaInvHistData.setTransEmpId(
				CommonConstant.DEFAULT_TRANSEMPID);
		}
		// end defect 9847 

		// defect 10505 
		// Add TransId 
		String lsIns =
			" INSERT INTO RTS.RTS_INV_HSTRY   "
				+ " ( "
				+ "  OFCISSUANCENO     , "
				+ "  SUBSTAID          , "
				+ "  TRANSAMDATE       , "
				+ "  TRANSWSID         , "
				+ "  TRANSTIME         , "
				+ "  TRANSEMPID        , "
				+ "  TRANSCD           , "
				+ "  ITMCD             , "
				+ "  INVITMYR          , "
				+ "  INVITMNO          , "
				+ "  INVENDNO          , "
				+ "  INVQTY            , "
				+ "  DELINVREASNCD     , "
				+ "  INVCNO            , "
				+ "  INVCCORRINDI      , "
				+ "  DELINVREASNTXT    , "
				+ "  TRANSID           , "
				+ "  INVID ) VALUES ("
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
				+ " ?)";
		// end defect 10505 

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getOfcIssuanceNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getSubStaId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getTransAMDate())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getTransWsId())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getTransTime())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getTransEmpId())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getTransCd())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getItmCd())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getInvItmYr())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getInvItmNo())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getInvEndNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getInvQty())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getDelInvReasnCd())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getInvcNo())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaInvHistData.getInvcCorrIndi())));
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getDelInvReasnTxt())));

		// defect 10505 
		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getTransId())));
		// end defect 10505 

		lvValues.addElement(
			new DBValue(
				Types.CHAR,
				DatabaseAccess.convertToString(
					aaInvHistData.getInvId())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				"insInventoryHistory - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, "insInventoryHistory - SQL - End");
			Log.write(Log.METHOD, this, "insInventoryHistory - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insInventoryHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD
	/**
	 * Method to Delete from RTS.RTS_INV_HSTRY for Purge
	 * Delete only for Purge
	 * 
	 * @param  aiPurgeAMDate int
	 * @return int	
	 * @throws RTSException
	 */
	public int purgeInventoryHistory(int aiPurgeAMDate)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeInventoryHistory - Begin");

		Vector lvValues = new Vector();

		// defect 8423
		String lsDel =
			"DELETE FROM RTS.RTS_INV_HSTRY WHERE TransAMDate <= ?  ";

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiPurgeAMDate)));
		// end defect 8423

		try
		{
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryHistory - SQL - Begin");

			// defect 9825
			// Return number of rows purged  
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(
				Log.SQL,
				this,
				"purgeInventoryHistory - SQL - End");
			Log.write(Log.METHOD, this, "purgeInventoryHistory - End");
			return liNumRows;
			// end defect 9825  
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeInventoryHistory - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	// defect 10239 
	//	/**
	//	 * Method to Query RTS.RTS_INV_HSTRY
	//	 * 
	//	 * @param  aaInventoryHistoryData InventoryHistoryData	
	//	 * @return Vector
	//	 * @throws RTSException
	//	 */
	//	public Vector qryInventoryHistory(InventoryHistoryData aaInventoryHistoryData)
	//		throws RTSException
	//	{
	//		Log.write(Log.METHOD, this, " - qryInventoryHistory - Begin");
	//
	//		StringBuffer lsQry = new StringBuffer();
	//
	//		Vector lvValues = new Vector();
	//
	//		Vector lvRslt = new Vector();
	//
	//		ResultSet lrsQry;
	//
	//		lsQry.append(
	//			"SELECT "
	//				+ "OfcIssuanceNo,"
	//				+ "SubStaId,"
	//				+ "TransAMDate,"
	//				+ "TransWsId,"
	//				+ "TransTime,"
	//				+ "TransEmpId,"
	//				+ "TransCd,"
	//				+ "ItmCd,"
	//				+ "InvItmYr,"
	//				+ "InvItmNo,"
	//				+ "InvEndNo,"
	//				+ "InvQty,"
	//				+ "DelInvReasnCd,"
	//				+ "InvcNo,"
	//				+ "InvcCorrIndi,"
	//				+ "DelInvReasnTxt,"
	//				+ "InvId "
	//				+ "FROM RTS.RTS_INV_HSTRY WHERE OfcIssuanceNo = ? ");
	//		try
	//		{
	//			lvValues.addElement(
	//				new DBValue(
	//					Types.INTEGER,
	//					DatabaseAccess.convertToString(
	//						aaInventoryHistoryData.getOfcIssuanceNo())));
	//
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryInventoryHistory - SQL - Begin");
	//			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
	//			Log.write(
	//				Log.SQL,
	//				this,
	//				" - qryInventoryHistory - SQL - End");
	//
	//			while (lrsQry.next())
	//			{
	//				InventoryHistoryData laInventoryHistoryData =
	//					new InventoryHistoryData();
	//				laInventoryHistoryData.setOfcIssuanceNo(
	//					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
	//				laInventoryHistoryData.setSubStaId(
	//					caDA.getIntFromDB(lrsQry, "SubStaId"));
	//				laInventoryHistoryData.setTransAMDate(
	//					caDA.getIntFromDB(lrsQry, "TransAMDate"));
	//				laInventoryHistoryData.setTransWsId(
	//					caDA.getIntFromDB(lrsQry, "TransWsId"));
	//				laInventoryHistoryData.setTransTime(
	//					caDA.getIntFromDB(lrsQry, "TransTime"));
	//				laInventoryHistoryData.setTransEmpId(
	//					caDA.getStringFromDB(lrsQry, "TransEmpId"));
	//				laInventoryHistoryData.setTransCd(
	//					caDA.getStringFromDB(lrsQry, "TransCd"));
	//				laInventoryHistoryData.setItmCd(
	//					caDA.getStringFromDB(lrsQry, "ItmCd"));
	//				laInventoryHistoryData.setInvItmYr(
	//					caDA.getIntFromDB(lrsQry, "InvItmYr"));
	//				laInventoryHistoryData.setInvItmNo(
	//					caDA.getStringFromDB(lrsQry, "InvItmNo"));
	//				laInventoryHistoryData.setInvEndNo(
	//					caDA.getStringFromDB(lrsQry, "InvEndNo"));
	//				laInventoryHistoryData.setInvQty(
	//					caDA.getIntFromDB(lrsQry, "InvQty"));
	//				laInventoryHistoryData.setDelInvReasnCd(
	//					caDA.getIntFromDB(lrsQry, "DelInvReasnCd"));
	//				laInventoryHistoryData.setInvcNo(
	//					caDA.getStringFromDB(lrsQry, "InvcNo"));
	//				laInventoryHistoryData.setInvcCorrIndi(
	//					caDA.getIntFromDB(lrsQry, "InvcCorrIndi"));
	//				laInventoryHistoryData.setDelInvReasnTxt(
	//					caDA.getStringFromDB(lrsQry, "DelInvReasnTxt"));
	//				laInventoryHistoryData.setInvId(
	//					caDA.getStringFromDB(lrsQry, "InvId"));
	//				// Add element to the Vector
	//				lvRslt.addElement(laInventoryHistoryData);
	//			} //End of While
	//
	//			lrsQry.close();
	//			caDA.closeLastDBStatement();
	//			lrsQry = null;
	//			Log.write(
	//				Log.METHOD,
	//				this,
	//				" - qryInventoryHistory - End ");
	//			return (lvRslt);
	//		}
	//		catch (SQLException aeSQLEx)
	//		{
	//			Log.write(
	//				Log.SQL_EXCP,
	//				this,
	//				" - qryInventoryHistory - Exception "
	//					+ aeSQLEx.getMessage());
	//			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
	//		}
	//	} //END OF QUERY METHOD
	// end defect 10239 
} //END OF CLASS
