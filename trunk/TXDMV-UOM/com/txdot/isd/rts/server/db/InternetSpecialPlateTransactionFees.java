package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FeesData;
import com.txdot.isd.rts.services.data.SpecialPlateItrntTransData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * InternetSpecialPlateTransactionFees.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		05/11/2007	Created Class.
 * Jeff S.		06/29/2007	Added purge methods.
 * 							defect 9121 Ver Special Plates
 * K Harrell	07/17/2007  add purgeOrphanTransFees()	
 * 							delete purgeCompleteTransFees(),
 * 							 purgeIncompleteTransFees()
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/24/2007	pass new Vector() 
 * 							modify purgeOrphanTransFees()
 * 							defect 9085 Ver Special Plates 2
 * K Harrell	11/15/2007	Corrected title in prologue  
 * 							defect 9085 Ver Special Plates 2
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeOrphanTransFees() 
 * 							defect 9825 Ver Defect_POS_D 
 * K Harrell	09/15/2009	Update for SQL Logging 
 * 							defect 10164 Ver Defect_POS_F      
 * ---------------------------------------------------------------------
 */

/**
 * Class used to handle communucation with the DB table 
 * RTS_ITRNT_SPAPP_TRANS_FEES.
 *
 * @version	Defect_POS_F  	09/15/2009
 * @author	Jeff Seifert
 * <br>Creation Date:		05/11/2007 09:55:00
 */
public class InternetSpecialPlateTransactionFees
{
	String csMethod = new String();

	DatabaseAccess caDA;

	/**
	 * InternetSpecialPlateTransactionFees Constructor
	 * 
	 * @throws RTSException
	 */
	public InternetSpecialPlateTransactionFees() throws RTSException
	{
		caDA = new DatabaseAccess();
	}

	/**
	 * InternetSpecialPlateTransactionFees Constructor
	 * 
	 * @param aaDateAccess
	 * @throws RTSException
	 */
	public InternetSpecialPlateTransactionFees(DatabaseAccess aaDateAccess)
		throws RTSException
	{
		caDA = aaDateAccess;
	}

	/**
	 * Insert Internet Special Plate Transaction Fees Table.
	 * 
	 * @param aaSPTransData SpecialPlateItrntTransData
	 * @param aiReqPltNoReqId int
	 * @throws RTSException
	 */
	public void insSPItrntTransFees(
		int aiReqPltNoReqId,
		SpecialPlateItrntTransData aaSPTransData)
		throws RTSException
	{
		csMethod = "insSPItrntTransFees()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsFeesIns =
			"INSERT INTO RTS.RTS_ITRNT_SPAPP_TRANS_FEES("
				+ "REGPLTNO, "
				+ "REGPLTNOREQID, "
				+ "ACCTITMCD, "
				+ "PLTSURCHARGEFEE) VALUES (?, ?, ?, ?)";
		try
		{
			if (aaSPTransData.getFeesData() != null)
			{
				for (int i = 0;
					i < aaSPTransData.getFeesData().size();
					i++)
				{
					Vector lvValues = new Vector();
					FeesData laFees =
						(FeesData) aaSPTransData.getFeesData().get(i);
					lvValues.addElement(
						new DBValue(
							Types.CHAR,
							aaSPTransData.getRegPltNo()));
					lvValues.addElement(
						new DBValue(
							Types.SMALLINT,
							String.valueOf(aiReqPltNoReqId)));
					lvValues.addElement(
						new DBValue(Types.CHAR, laFees.getAcctItmCd()));
					lvValues.addElement(
						new DBValue(
							Types.DECIMAL,
							DatabaseAccess.convertToString(
								laFees.getItemPrice())));

					Log.write(
						Log.SQL,
						this,
						csMethod + CommonConstant.SQL_BEGIN);

					caDA.executeDBInsertUpdateDelete(
						lsFeesIns,
						lvValues);

					Log.write(
						Log.SQL,
						this,
						csMethod + CommonConstant.SQL_END);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Purge Orphan RTS_ITRNT_SPAPP_TRANS_FEES 
	 * 
	 * @return int
	 * @throws RTSException
	 */
	public int purgeOrphanTransFees() throws RTSException
	{
		csMethod = "purgeOrphanTransFees()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		try
		{
			String lsSqlDelData =
				"DELETE FROM RTS.RTS_ITRNT_SPAPP_TRANS_FEES A "
					+ "WHERE NOT EXISTS (SELECT * FROM "
					+ "RTS.RTS_ITRNT_SPAPP_TRANS B  "
					+ "WHERE A.REGPLTNO = B.REGPLTNO AND "
					+ "A.REGPLTNOREQID = B.REGPLTNOREQID )";

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			// Return number of rows purged 
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelData,
					new Vector());
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			return liNumRows;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Select Internet Special Plate Transaction Fees Table.
	 * 
	 * @param aiReqPltNoReqId int
	 * @param asRegpltNo String
	 * @throws RTSException
	 */
	public Vector qrySPItrntTransFees(
		int aiReqPltNoReqId,
		String asRegpltNo)
		throws RTSException
	{
		csMethod = "qrySPItrntTransFees()";
		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvFeesData = new Vector();

		String lsFeesQry =
			"SELECT ACCTITMCD, PLTSURCHARGEFEE FROM "
				+ "RTS.RTS_ITRNT_SPAPP_TRANS_FEES WHERE "
				+ "REGPLTNO = ? AND REGPLTNOREQID = ?";
		try
		{
			Vector lvValues = new Vector();
			lvValues.addElement(new DBValue(Types.CHAR, asRegpltNo));
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					String.valueOf(aiReqPltNoReqId)));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet =
				caDA.executeDBQuery(lsFeesQry, lvValues);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSet.next())
			{
				FeesData laFees = new FeesData();
				laFees.setItmQty(1);
				laFees.setItemPrice(
					caDA.getDollarFromDB(
						laResultSet,
						"PLTSURCHARGEFEE"));
				laFees.setAcctItmCd(
					caDA.getStringFromDB(laResultSet, "ACCTITMCD"));
				lvFeesData.add(laFees);
			}
			return lvFeesData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());

			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}
	}
}
