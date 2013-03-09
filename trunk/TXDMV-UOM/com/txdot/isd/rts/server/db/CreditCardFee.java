package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CreditCardFeeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * CreditCardFee.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks   	07/12/2002  Add call to closeLastStatement() after a query
 * MAbs			08/27/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * K Harrell	11/24/2004	Add cleanup of RTS_CRDT_CARD_FEE to purge
 *							add purgeCreditCardFee()
 *							defect 7689  Ver 5.2.2
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	08/28/2008	No longer update Admin Cache from this class
 * 							delete updAdminCache()
 * 							modify delCreditCardFee(),
 * 							 insCreditCardFee(),updCreditCardFee()
 * 							defect 8721 Ver Defect_POS_B
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeCreditCardFee()
 * 							defect 9825 Ver Defect_POS_D   
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_CRDT_CARD_FEE
 *
 * @version	Defect_POS_D	01/19/2009  
 * @author	Kathy Harrell
 * <br>Creation Date:		04/30/2002	14:50:09 
 */

public class CreditCardFee extends CreditCardFeeData
{
	DatabaseAccess caDA;

	/**
	 * CreditCardFee constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public CreditCardFee(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Delete from RTS.RTS_CRDT_CARD_FEE
	 *
	 * @param  aaCreditCardFeeData  CreditCardFeeData	
	 * @throws RTSException 
	 */
	public void delCreditCardFee(CreditCardFeeData aaCreditCardFeeData)
		throws RTSException
	{

		Log.write(Log.METHOD, this, "delCreditCardFee - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"UPDATE RTS.RTS_CRDT_CARD_FEE SET DeleteIndi = 1, "
				+ " ChngTimestmp = CURRENT TIMESTAMP "
				+ " WHERE "
				+ " OfcIssuanceNo = ? AND "
				+ " SubstaId = 0 AND "
				+ " RTSEffDate = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCreditCardFeeData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCreditCardFeeData
							.getRTSEffDate()
							.getYYYYMMDDDate())));
			Log.write(Log.SQL, this, "delCreditCardFee - SQL - Begin");
			caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "delCreditCardFee - SQL - End");
			Log.write(Log.METHOD, this, "delCreditCardFee - End");

			// defect 8721
			// updAdminCache(aaCreditCardFeeData);
			// end defect 8721 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"delCreditCardFee - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Insert into RTS.RTS_CRDT_CARD_FEE
	 * 
	 * @param  aaCreditCardFeeData  CreditCardFeeData		
	 * @throws RTSException 	
	 */
	public void insCreditCardFee(CreditCardFeeData aaCreditCardFeeData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "insCreditCardFee - Begin");

		Vector lvValues = new Vector();

		int liNumRows = updCreditCardFee(aaCreditCardFeeData);

		int liFeeTypeCd = 0;

		if (aaCreditCardFeeData.isPercentage())
		{
			liFeeTypeCd = 1;
		}

		if (liNumRows == 0)
		{
			String lsIns =
				"INSERT into RTS.RTS_CRDT_CARD_FEE("
					+ "OfcIssuanceNo,"
					+ "SubstaId,"
					+ "RTSEffDate,"
					+ "RTSEffEndDate,"
					+ "ItmPrice,"
					+ "FeeTypeCd,"
					+ "DeleteIndi,"
					+ "ChngTimestmp ) VALUES ( "
					+ " ?,"
					+ " 0,"
					+ " ?,"
					+ " ?,"
					+ " ?,"
					+ liFeeTypeCd
					+ " ,0,"
					+ " Current Timestamp)";
			try
			{
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCreditCardFeeData.getOfcIssuanceNo())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCreditCardFeeData
								.getRTSEffDate()
								.getYYYYMMDDDate())));
				lvValues.addElement(
					new DBValue(
						Types.INTEGER,
						DatabaseAccess.convertToString(
							aaCreditCardFeeData
								.getRTSEffEndDate()
								.getYYYYMMDDDate())));
				lvValues.addElement(
					new DBValue(
						Types.DECIMAL,
						DatabaseAccess.convertToString(
							aaCreditCardFeeData.getItmPrice())));

				Log.write(
					Log.SQL,
					this,
					"insCreditCardFee - SQL - Begin");
				liNumRows =
					caDA.executeDBInsertUpdateDelete(lsIns, lvValues);
				Log.write(
					Log.SQL,
					this,
					"insCreditCardFee - SQL - End");

				// defect 8721 
				// updAdminCache(aaCreditCardFeeData);
				// end defect 8721 

				Log.write(Log.METHOD, this, "insCreditCardFee - End");
			}

			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"insCreditCardFee - Exception - "
						+ aeRTSEx.getMessage());
				throw aeRTSEx;
			}
		} // If liNumRows = 0 
	} //END OF INSERT METHOD

	/**
	 * Method to Delete from RTS.RTS_CRDT_CARD_FEE for Purge
	 *
	 * @param  aiNumDays int
	 * @return int 
	 * @throws RTSException 
	 */
	public int purgeCreditCardFee(int aiNumDays) throws RTSException
	{
		Log.write(Log.METHOD, this, "purgeCreditCardFee - Begin");

		Vector lvValues = new Vector();

		String lsDel =
			"DELETE FROM RTS.RTS_CRDT_CARD_FEE WHERE DELETEINDI = 1 "
				+ " AND days(Current Date) - days(ChngTimestmp) > ? ";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiNumDays)));

			Log.write(
				Log.SQL,
				this,
				"purgeCreditCardFee - SQL - Begin");
			// defect 9825 
			// return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsDel, lvValues);
			Log.write(Log.SQL, this, "purgeCreditCardFee - SQL - End");
			Log.write(Log.METHOD, this, "purgeCreditCardFee - End");
			return liNumRows;
			// end defect 9825 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"purgeCreditCardFee - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Delete METHOD

	/**
	 * Method to Query RTS.RTS_CRDT_CARD_FEE
	 *
	 * @param  aaCreditCardFeeData CreditCardFeeData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryCreditCardFee(CreditCardFeeData aaCreditCardFeeData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryCreditCardFeeData - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "OfcIssuanceNo,"
				+ "SubstaId,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "ItmPrice, "
				+ "FeeTypeCd,"
				+ "DeleteIndi, "
				+ "ChngTimestmp "
				+ "FROM RTS.RTS_CRDT_CARD_FEE "
				+ " where OfcIssuanceNo = ? AND SubstaId = 0 ");

		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaCreditCardFeeData.getOfcIssuanceNo())));

		if (aaCreditCardFeeData.getChngTimestmp() == null)
		{
			lsQry.append(" AND DeleteIndi = 0");
		}
		else
		{
			lsQry.append(" AND ChngTimestmp > ?");
			lvValues.addElement(
				new DBValue(
					Types.TIMESTAMP,
					DatabaseAccess.convertToString(
						aaCreditCardFeeData.getChngTimestmp())));
		}
		lsQry.append(" order by 1,2,3");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryCreditCardFee - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(Log.SQL, this, " - qryCreditCardFee - SQL - End");

			while (lrsQry.next())
			{
				CreditCardFeeData laCreditCardFeeData =
					new CreditCardFeeData();
				laCreditCardFeeData.setOfcIssuanceNo(
					caDA.getIntFromDB(lrsQry, "OfcIssuanceNo"));
				laCreditCardFeeData.setSubstaId(
					caDA.getIntFromDB(lrsQry, "SubstaId"));
				laCreditCardFeeData.setRTSEffDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						caDA.getIntFromDB(lrsQry, "RTSEffDate")));
				laCreditCardFeeData.setRTSEffEndDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						caDA.getIntFromDB(lrsQry, "RTSEffEndDate")));
				laCreditCardFeeData.setPercentage(
					caDA.getIntFromDB(lrsQry, "FeeTypeCd") == 1);
				laCreditCardFeeData.setItmPrice(
					caDA.getDollarFromDB(lrsQry, "ItmPrice"));
				laCreditCardFeeData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));
				laCreditCardFeeData.setChngTimestmp(
					caDA.getRTSDateFromDB(lrsQry, "ChngTimestmp"));
				// Add element to the Vector
				lvRslt.addElement(laCreditCardFeeData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryCreditCardFee - End ");
			return (lvRslt);

		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCreditCardFee - SQL Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to update RTS.RTS_CRDT_CARD_FEE
	 *
	 * @param  aaCreditCardFeeData CreditCardFeeData
	 * @return int 
	 * @throws RTSException
	 */
	public int updCreditCardFee(CreditCardFeeData aaCreditCardFeeData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, "updCreditCardFee - Begin");

		Vector lvValues = new Vector();

		int liFeeTypeCd = 0;

		if (aaCreditCardFeeData.isPercentage())
		{
			liFeeTypeCd = 1;
		}

		String lsUpd =
			"UPDATE RTS.RTS_CRDT_CARD_FEE SET "
				+ "FeeTypeCd = "
				+ liFeeTypeCd
				+ ", RTSEffEndDate = ?, "
				+ "ItmPrice = ?, "
				+ "DeleteIndi = 0, "
				+ "ChngTimestmp  = Current Timestamp "
				+ "WHERE "
				+ "OfcIssuanceNo = ? AND "
				+ "SubstaId = 0 "
				+ "AND RTSEffDate = ? ";
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCreditCardFeeData
							.getRTSEffEndDate()
							.getYYYYMMDDDate())));
			lvValues.addElement(
				new DBValue(
					Types.DECIMAL,
					DatabaseAccess.convertToString(
						aaCreditCardFeeData.getItmPrice())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCreditCardFeeData.getOfcIssuanceNo())));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaCreditCardFeeData
							.getRTSEffDate()
							.getYYYYMMDDDate())));

			Log.write(Log.SQL, this, "updCreditCardFee - SQL - Begin");
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(lsUpd, lvValues);
			Log.write(Log.SQL, this, "updCreditCardFee - SQL - End");
			Log.write(Log.METHOD, this, "updCreditCardFee - End");

			// defect 8721 
			// updAdminCache(aaCreditCardFeeData);
			// end defect 8721 
			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"updCreditCardFee - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF Update METHOD
} //END OF CLASS
