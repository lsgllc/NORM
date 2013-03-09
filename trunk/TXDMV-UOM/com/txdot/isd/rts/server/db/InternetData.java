package com.txdot.isd.rts.server.db;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.data.CompleteRegRenData;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.VehicleBaseData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.util.UtilityMethods;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.webapps.util.BatchLog;

/*
 * InternetData.java  
 * 
 * (c) Texas Department of Transportation  2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell 	09/29/2005	Created
 * 							add insItrntTransData() 
 * 							defect 7889 Ver 5.2.3  
 * K Harrell	11/13/2005	Streamline calls for Internet Purge
 * 							add purgeItrntData()
 * 							defect 8385 Ver 5.2.3
 * K Harrell	01/19/2009	Return number of rows purged
 * 							modify purgeItrntData()
 * 							defect 9825 Ver Defect_POS_D
 * K Harrell	02/06/2009	Corrected call so that would not purge 
 * 							data for record with same plate no where 
 * 							criteria was not met.  
 * 							modify purgeItrntData() 
 * 							defect 9931 Ver Defect_POS_D
 * K Harrell	09/15/2009	Update for SQL Logging 
 * 							defect 10164 Ver Defect_POS_F 
 * B Brown		01/26/2011	Add an inquiry to the bolb column 
 * 							CmpltTransData used later for
 * 							upataing fees in the blob
 * 							add qryCompleteTransactionData(),
 * 							updateFees()
 * 							defect 10714 Ver POS_670
 * B Brown		09/07/2011	Update major and minor color in  
 * 							CompleteTransactionData object for 
 * 							internet renewals.
 * 							modify updateFees()
 * 							defect 10985 Ver POS_681      
 *----------------------------------------------------------------------
 */

/**
 * This method contains methods to interact with database 
 *  
 * @version	POS_670 		01/26/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		09/29/2003 19:20:43 
 */
public class InternetData
{
	String csMethod = new String();
	
	DatabaseAccess caDA;

	/**
	 * InternetData constructor comment.
	 */
	public InternetData() throws RTSException
	{
		caDA = new DatabaseAccess();
	}
	/**
	 * InternetData constructor comment.
	 * 
	 * @param DatabaseAccess aaDA 
	 * @throws RTSException
	 */
	public InternetData(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * 
	 * Delete Data associated with IRENEW 
	 * 
	 * @param aaVehBaseData
	 * @throws RTSException
	 */
	public void delItrntData(VehicleBaseData aaVehBaseData)
		throws RTSException
	{
		csMethod = "delItrntData()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		String lsSqlDelData =
			"delete from rts.rts_itrnt_data "
				+ "where REGPLTNO=? AND "
				+ "VIN=? AND "
				+ "DOCNO=?";

		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getPlateNo()));
		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getVin()));
		lvValues.add(
			new DBValue(Types.VARCHAR, aaVehBaseData.getDocNo()));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
				
			caDA.executeDBInsertUpdateDelete(lsSqlDelData, lvValues);
			
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

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
	 * Insert into Itrnt Trans Data 
	 * 
	 * @param aaCompTransData CompleteTransactionData
	 * @param aaCompRegRenData CompleteRegRenData
	 * @throws RTSException
	 */
	public void insItrntData(
		CompleteTransactionData aaCompTransData,
		CompleteRegRenData aaCompRegRenData)
		throws RTSException
	{
		csMethod = "insItrntData()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		Vector lvValues = new Vector();

		String sqlIns =
			"INSERT INTO RTS.RTS_ITRNT_DATA ("
				+ "TRANSCD,"
				+ "REGPLTNO,"
				+ "VIN,"
				+ "DOCNO, "
				+ "CmpltTransData"
				+ ") VALUES ("
				+ "'IRENEW',"
				+ "?,"
				+ "?,"
				+ "?,"
				+ "?"
				+ ")";

		try
		{
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getPlateNo()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getVin()));
			lvValues.addElement(
				new DBValue(
					Types.VARCHAR,
					aaCompRegRenData.getVehBaseData().getDocNo()));

			byte[] laByteCompTransData =
				UtilityMethods.objToByteArray(aaCompTransData);
			lvValues.addElement(
				new DBValue(Types.BLOB, laByteCompTransData));

			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
				
			caDA.executeDBInsertUpdateDelete(sqlIns, lvValues);
			
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP,this,csMethod+ CommonConstant.SQL_EXCEPTION + aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
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
	 * Query Complete Transaction Data.
	 * 
	 * @param ahtData Hashtable
	 * @return CompleteTransactionData
	 * @throws RTSException
	 */
	public CompleteTransactionData qryItrntDataComplTransData(Hashtable ahtData)
		throws RTSException
	{
		csMethod = "qryItrntDataComplTransData()";
		
		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);
			
		CompleteTransactionData laCompTransData = null;

		//obtain keys from function call
		String lsPlateNo = (String) ahtData.get("RegPltNo");
		String lsTransCd = (String) ahtData.get("TransCd");
		String lsDocNo = (String) ahtData.get("DocNo");

		ResultSet laResultSet;
		StringBuffer lsSqlQry =
			new StringBuffer("SELECT CmpltTransData FROM RTS.RTS_ITRNT_DATA ");
		String lsWhereClause = "WHERE ";

		//build lsWhereClause		
		if (lsTransCd != null && lsTransCd.length() > 0)
		{
			lsWhereClause += "TransCd = '" + lsTransCd + "' ";
		}
		if (lsPlateNo != null && lsPlateNo.length() > 0)
		{
			if (lsWhereClause.length() > 7)
			{
				lsWhereClause += "AND RegPltNo = '" + lsPlateNo + "' ";
			}
			else
			{
				lsWhereClause += "RegPltNo = '" + lsPlateNo + "' ";
			}
		}
		if (lsDocNo != null && lsDocNo.length() > 0)
		{
			if (lsWhereClause.length() > 7)
			{
				lsWhereClause += "AND DocNo = '" + lsDocNo + "' ";
			}
			else
			{
				lsWhereClause += "DocNo = '" + lsDocNo + "' ";
			}
		}

		lsSqlQry.append(lsWhereClause);
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
				
			laResultSet = caDA.executeDBQuery(lsSqlQry.toString());
			
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			
			if (laResultSet.next())
			{
				///////////////////////////////////////////////////
				Object laBlobObj =
					caDA.getBlobFromDB(laResultSet, "CmpltTransData");
				if (laBlobObj == null)
				{
					return null;
				}
				else
				{
					if (laBlobObj instanceof CompleteTransactionData)
					{
						laCompTransData =
							(CompleteTransactionData) laBlobObj;
					}
					else
					{
						Log.write(
							Log.SQL,
							this,
							"qryItrntDataComplTransData: error, getBlobFromDB() "
								+ "NOT graph of CompleteTransactionData");
					}
				}
			}
			//cleanup
			laResultSet.close();
			laResultSet = null;
			return laCompTransData;
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
	
	/**
	 * update fees obj in the blob.
	 * 
	 * @param aaCompleteRegRenData CompleteRegRenData
	 * @return CompleteTransactionData
	 * @throws RTSException
	 */
	public CompleteTransactionData qryCompleteTransactionData(CompleteRegRenData aaCompleteRegRenData)
		throws RTSException
	{
		csMethod = "qryCompleteTransactionData()";
		
		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);
			
		CompleteTransactionData laCompTransData = null;	

		ResultSet laResultSet;
		StringBuffer lsSqlQry =
			new StringBuffer(
				"SELECT CmpltTransData FROM RTS.RTS_ITRNT_DATA WHERE "
					+ "RegPltNo  = '"
					+ aaCompleteRegRenData.getVehBaseData().getPlateNo()
					+ "' "
					+ "AND DocNo = '"
					+ aaCompleteRegRenData.getVehBaseData().getDocNo()
					+ "' "
					+ "AND VIN   = '"
					+ aaCompleteRegRenData.getVehBaseData().getVin()
					+ "' ");

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
				
			laResultSet = caDA.executeDBQuery(lsSqlQry.toString());
			
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			
			if (laResultSet.next()) 
			{
				Object laBlobObj =
					caDA.getBlobFromDB(laResultSet, "CmpltTransData");
				if (laBlobObj != null
					&& laBlobObj instanceof CompleteTransactionData) 
				{
					laCompTransData = (CompleteTransactionData) laBlobObj;
				} 
				else 
				{
					Log.write(
						Log.SQL,
						this,
						"qryCompleteTransactionData: error, getBlobFromDB() "
							+ "NOT graph of CompleteTransactionData");
				}
			}
			laResultSet.close();
			laResultSet = null;
			return laCompTransData;
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
	
	/**
	 * update fees obj in the blob.
	 * 
	 * @param aaCompleteRegRenData CompleteRegRenData
	 * @return boolean
	 * @throws RTSException
	 */
	public boolean updateFees(CompleteRegRenData aaCompleteRegRenData, 
	CompleteTransactionData aaCompleteTransactionData)
		throws RTSException
	{
		csMethod = "updateFees()";
		boolean lbGoodUpdate = false;
		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);
		Vector lvValues = new Vector();	

		StringBuffer lsSqlQry =
			new StringBuffer(
				"update RTS.RTS_ITRNT_DATA set CmpltTransData = ? WHERE "
					+ "RegPltNo  = '"
					+ aaCompleteRegRenData.getVehBaseData().getPlateNo()
					+ "' "
					+ "AND DocNo = '"
					+ aaCompleteRegRenData.getVehBaseData().getDocNo()
					+ "' "
					+ "AND VIN   = '"
					+ aaCompleteRegRenData.getVehBaseData().getVin()
					+ "' ");
		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
			// defect 10985
			aaCompleteTransactionData.getVehicleInfo().getVehicleData()
					.setVehMjrColorCd(
							aaCompleteRegRenData.getVehDescData()
									.getMajorColorCd());
			aaCompleteTransactionData.getVehicleInfo().getVehicleData()
					.setVehMnrColorCd(
							aaCompleteRegRenData.getVehDescData()
									.getMinorColorCd());
			// end defect 10985
			byte[] laByteCompTransData =
				UtilityMethods.objToByteArray(aaCompleteTransactionData);
			lvValues.addElement(
				new DBValue(Types.BLOB, laByteCompTransData));				
			int lsUpadteRow = caDA.executeDBInsertUpdateDelete(lsSqlQry.toString(),lvValues);
			
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			lbGoodUpdate =  true;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());

			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}

		finally
		{
			Log.write(
				Log.METHOD,
				this,
				csMethod + CommonConstant.SQL_METHOD_END);
		}		
		return lbGoodUpdate;
	}
	
	/**
	 * Purge rts_itrnt_data where it is older than the amount of
	 * days that are passed in.
	 * 
	 * @param aiCntyStatusCd int
	 * @param aiDaysOld int
	 * @return int
	 * @throws RTSException
	 */
	public int purgeItrntData(int aiCntyStatusCd, int aiDaysOld)
		throws RTSException
	{
		csMethod = "purgeItrntData()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		// defect 9931  
		// The replace SQL would delete all records from RTS_ITRNT_DATA  
		//  with the same plate no if any one of the associated records  
		//  in RTS_ITRNT_TRANS met the criteria 

		//		String lsSqlDelData =
		//			"delete from rts.rts_itrnt_data "
		//				+ "where regpltno in "
		//				+ "(select t.regpltno "
		//				+ "from rts.rts_itrnt_data d, "
		//				+ "rts.rts_itrnt_trans t "
		//				+ "where CntyStatusCd = ? and "
		//				+ "d.transcd = t.transcd and "
		//				+ "d.regpltno = t.regpltno and "
		//				+ "d.vin = t.vin and "
		//				+ "d.docno = t.docno "
		//				+ "and t.CntyPrcsdTimeStmp "
		//				+ "< (CURRENT TIMESTAMP - ? DAYS)";

		String lsSqlDelData =
			"delete from rts.rts_itrnt_data a "
				+ " where exists "
				+ " (select * from rts.rts_itrnt_trans b  "
				+ " where CntyStatusCd = ? and "
				+ " a.transcd = b.transcd and "
				+ " a.regpltno = b.regpltno and "
				+ " a.vin = b.vin and "
				+ " a.docno = b.docno "
				+ " and b.CntyPrcsdTimeStmp "
				+ " < (CURRENT TIMESTAMP - ? DAYS)";
		// end defect 9931

		// defect 8134
		if (aiCntyStatusCd == CommonConstants.UNPAID)
		{
			lsSqlDelData += "and itrntpymntstatuscd is null";
		}

		lsSqlDelData += ")";
		// end defect 8134

		Vector lvValues = new Vector();

		lvValues.add(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiCntyStatusCd)));
		lvValues.add(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(aiDaysOld)));

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);
				
			// defect 9825 
			// Return number of rows purged
			int liNumRows =
				caDA.executeDBInsertUpdateDelete(
					lsSqlDelData,
					lvValues);
					
			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);
			
			return liNumRows;
			// end defect 9825 

		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeRTSEx.getMessage());
			BatchLog.error(aeRTSEx);
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
}
