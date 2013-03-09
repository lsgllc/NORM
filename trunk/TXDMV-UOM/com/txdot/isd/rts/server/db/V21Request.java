package com.txdot.isd.rts.server.db;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * V21Request.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/03/2008	Created
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/20/2008	Add ReqIPAddr to SQL 
 * 							defect 9546 Ver 3 Amigos PH A 
 * Ray Rowehl	02/22/2008	Add another "?" for the new ip addr
 * 							modify insV21Request()
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	01/21/2009	Purge V21 Requests
 * 							add purgeV21Request()
 * 							defect 9803 Ver Defect_POS_D   
 * K Harrell	07/23/2010	add V21IntrfcLogId to insert stmt
 * 							modify insV21Request()
 * 							defect 10482 Ver 6.5.0   
 * K Harrell	08/22/2011	remove reference to REQIPADDR 
 * 							modify insV21Request() 
 * 							defect 10979 Ver 6.8.1    								
 * ---------------------------------------------------------------------
 */
/**
 * This class provides access to RTS.RTS_V21_REQUEST 
 *
 * @version	6.8.1 		 	08/22/2011
 * @author	Kathy Harrell 
 * @since 					02/03/2007  11:57:00 
 */

public class V21Request extends V21RequestData
{
	DatabaseAccess caDA;

	/**
	 * V21Request constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public V21Request(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Insert into RTS.RTS_V21_REQUEST
	 * 
	 * @param  aaV21RequestData	V21RequestData
	 * @throws RTSException 
	 */
	public V21RequestData insV21Request(V21RequestData aaV21RequestData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insV21Request - Begin");

		Vector lvValues = new Vector();
		ResultSet lrsQry;
		
		// defect 10979
		String lsIns =
			"INSERT into RTS.RTS_V21_REQUEST("
				+ "V21ReqTypeCd,"
				//+ "ReqIPAddr,"
				+ "V21IntrfcLogId,"
				+ "ReqTimestmp) VALUES ( "
				+ " ?,"
				//+ " ?,"
				+ " ?,"
				+ " CURRENT TIMESTAMP)";
		// end defect 10979 

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getV21ReqTypeCd())));

			// defect 10979
//			lvValues.addElement(
//				new DBValue(
//					Types.CHAR,
//					DatabaseAccess.convertToString(
//						aaV21RequestData.getReqIPAddr())));
			// end defect 10979 

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getV21IntrfcLogId())));

			Log.write(Log.SQL, this, "insV21Request - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			String lsSel =
				"Select IDENTITY_VAL_LOCAL() as V21ReqId from RTS.RTS_V21_REQUEST";

			lrsQry = caDA.executeDBQuery(lsSel, null);
			int liV21ReqId = 0;

			while (lrsQry.next())
			{
				liV21ReqId = caDA.getIntFromDB(lrsQry, "V21ReqId");
				aaV21RequestData.setV21ReqId(liV21ReqId);
				break;
			} //End of While

			Log.write(Log.SQL, this, "insV21Request - SQL - End");
			Log.write(Log.METHOD, this, "insV21Request - End");

			return aaV21RequestData;
		}

		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - insV21Request - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insV21Request - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_V21_REQUEST for Purge
	 * 
	 * @param  aaPurgeRTSDate int	
	 * @return int  
	 * @throws RTSException
	 */
	public int purgeV21Request(RTSDate aaPurgeDate) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeV21Request- Begin");

		String lsDel =
			"DELETE FROM RTS.RTS_V21_REQUEST WHERE ReqTimestmp <= '"
				+ aaPurgeDate.getTimestamp()
				+ "'";
		try
		{
			Log.write(Log.SQL, this, "purgeV21Request- SQL - Begin");

			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, new Vector());

			Log.write(Log.SQL, this, "purgeV21Request- SQL - End");
			Log.write(Log.METHOD, this, "purgeV21Request- End");
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeV21Request- Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to update a RTS.RTS_V21_REQUEST
	 *
	 * @param  aaV21RequestData		
	 * @return int 
	 * @throws RTSException 	
	 */

	public int updV21Request(V21RequestData aaV21RequestData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updV21Request - Begin");

		Vector lvValues = new Vector();

		String lsUpd =
			"UPDATE RTS.RTS_V21_REQUEST set "
				+ "Resptimestmp = Current Timestamp, "
				+ "SuccessfulIndi = ?, "
				+ "ErrMsgNo= ? "
				+ "where V21ReqId = ?";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getSuccessfulIndi())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getErrMsgNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getV21ReqId())));

			Log.write(Log.SQL, this, "updV21Request - SQL - Begin");
			int liReturnCount =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updV21Request - SQL - End");

			Log.write(Log.METHOD, this, "updV21Request - End");
			return liReturnCount;
		}
		catch (RTSException leRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updV21Request - Exception - " + leRTSEx.getMessage());
			throw leRTSEx;
		}
	}
}
