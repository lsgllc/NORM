package com.txdot.isd.rts.server.db;

import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.webservices.inv.data.RtsInvRequest;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * WebServicesInventoryHistory.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/05/2010	Created
 * 							defect 10480 Ver POS_640 
 * K Harrell	05/06/2010	ReserveIndi to ResrvIndi 
 * 							modify insWebServicesInventoryHistory()
 * 							defect 10480 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class provides access to RTS.RTS_SRVC_INV_HSTRY 
 *
 * @version	POS_640			05/06/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		05/05/2010 09:34:17 
 */
public class WebServicesInventoryHistory
{
	DatabaseAccess caDA;

	String csMethod = new String();

	/**
	 * WebServicesInventoryHistory.java  Constructor
	 * 
	 */
	public WebServicesInventoryHistory(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Insert into RTS.RTS_SRVC_INV_HSTRY
	 *
	 * @param  aaRequest	
	 * @throws RTSException 
	 */
	public void insWebServicesInventoryHistory(
		int aiSavReqId,
		RtsInvRequest aaRequest)
		throws RTSException
	{
		csMethod = "insWebServicesInventoryHistory";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		Vector lvValues = new Vector();

		String lsIns =
			"INSERT into RTS.RTS_SRVC_INV_HSTRY("
				+ "SavReqId,"
				+ "ItmCd,"
				+ "InvItmYr,"
				+ "InvItmNo,"
				+ "MfgPltNo,"
				+ "RegPltNo,"
				+ "ResComptCntyNo,"
				+ "ResrvIndi,"
				+ "PlpIndi,"
				+ "IsaIndi "
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
				+ " ? ) ";

		try
		{
			// 1  SAVREQID - INTEGER 
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiSavReqId)));

			// 2  ITMCD - CHAR 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					DatabaseAccess.convertToString(
						aaRequest.getItmCd())));

			// 3  INVITMYR - SMALLINT 
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaRequest.getItmYr())));

			// 4  INVITMNO - CHAR 
			lvValues.addElement(
				new DBValue(Types.CHAR, aaRequest.getItmNo()));

			// 5  MFGPLTNO - CHAR 
			lvValues.addElement(
				new DBValue(
					Types.CHAR,
					aaRequest.getManufacturingPltNo()));

			// 6 REGPLTNO - CHAR 
			lvValues.addElement(
				new DBValue(Types.CHAR, aaRequest.getRegPltNo()));
				
			// 7 RESCOMPCNTYNO - SMALLINT 
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaRequest.getRequestingOfcIssuanceNo())));
						
			// 8 RESRVINDI - SMALLINT 
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaRequest.isFromReserveFlag() ? 1 : 0)));

			// 9 PLPINDI - SMALLINT 
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaRequest.isPlpFlag() ? 1 : 0)));
						
			// 10 ISAINDI - SMALLINT 
			lvValues.addElement(
				new DBValue(
					Types.SMALLINT,
					DatabaseAccess.convertToString(
						aaRequest.isIsaFlg() ? 1 : 0)));

			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");
			Log.write(Log.METHOD, this, csMethod + " - End");
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod + " - Exception - " + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF INSERT METHOD

}
