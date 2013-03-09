package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * V21PlateDispositionRequest.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/03/2008	Created
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/07/2008	Rename table DSPN vs. DISP 
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/15/2008	DisassociateCd to int  
 * 							defect 9546 Ver 3 Amigos PH A
 * K Harrell	02/15/2008	DisassociateCd to DissociateCd  
 * 							defect 9546 Ver 3 Amigos PH A 
 * K Harrell	02/20/2008	Modified column name V21UniqueId to V21PltId 
 * 							defect 9546 Ver 3 Amigos PH A
 * Ray Rowehl	02/22/2008	Changed out ")" for "," following VTNSource.
 * 							Remove duplicate VALUES stmt.
 * 							modify insV21PlateDispositionRequest() 
 * 							defect 9546 Ver 3 Amigos PH A
 * ---------------------------------------------------------------------
 */
/**
 * This class provides access to RTS.RTS_V21_PLT_DSPN_REQ  
 *
 * @version	3 Amigos PH A	02/22/2008
 * @author	Kathy Harrell 
 * <br>Creation Date:		02/03/2007  13:45:00 
 */
public class V21PlateDispositionRequest
{
	DatabaseAccess caDA;

	/**
	 * V21PlateDispositionRequest constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public V21PlateDispositionRequest(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Insert into RTS.RTS_V21_PLT_DSPN_REQ  
	 * 
	 * @param  aaV21RequestData
	 * @throws RTSException 
	 */
	public void insV21PlateDispositionRequest(V21RequestData aaV21RequestData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			"insV21PlateDispositionRequest - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_V21_PLT_DSPN_REQ("
				+ "V21ReqId,"
				+ "V21PltId,"
				+ "DocNo,"
				+ "RegExpMo,"
				+ "RegExpYr,"
				+ "RegPltNo,"
				+ "SpclRegId,"
				+ "VTNSource,"
				+ "DissociateCd)"
				+ " VALUES ("
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
						aaV21RequestData.getV21ReqId())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getV21IntrfcLogId())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getDocNo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getRegExpMo())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getRegExpYr())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getRegPltNo())));

			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaV21RequestData.getSpclRegId())));

			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaV21RequestData.getVTNSource())));

			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaV21RequestData.getDissociateCd())));

			Log.write(
				Log.SQL,
				this,
				"insV21PlateDispositionRequest - SQL - Begin");

			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);

			Log.write(
				Log.SQL,
				this,
				"insV21PlateDispositionRequest - SQL - End");

			Log.write(
				Log.METHOD,
				this,
				"insV21PlateDispositionRequest - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"insV21PlateDispositionRequest - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	}
}
