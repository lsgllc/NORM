package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TransactionCodesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * TransactionCodes.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryTransactionCodes()	
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3   
 * K Harrell	04/03/2008	add TtlTrnsfrPnltyExmptCd to query
 * 							modify qryTransactionCodes()
 * 							defect 9583 Ver Defect_POS_A 
 * K Harrell	05/21/2008  removed TtlTrnsfrPnltyExmptCd from query
 * 							modify qrTransactionCodes() 
 * 							defect 9583 Ver Defect_POS_A 
 * K Harrell	06/20/2008	restore TtlTrnsfrPnltyExmptCd to query
 * 							modify qryTransactionCodes()
 * 							defect 9724 Ver Defect_POS_A 
 * K Harrell	03/11/2009	add ETTlTransIndi 
 * 							modify qryTransactionCodes()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	09/17/2010 	add PndngTransLookupIndi,
							 ElgblPndngTransLookupIndi 
							modify qryTransactionCodes() 
							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_TRANS_CDS.
 * 
 * @version	6.6.0			09/17/2010
 * @author 	Kathy Harrell
 * <br> Creation Date:		08/31/2001 15:34:10 
 */

public class TransactionCodes extends TransactionCodesData
{
	DatabaseAccess caDA;

	/**
	 * TransactionCodes constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public TransactionCodes(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_TRANS_CDS
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTransactionCodes() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTransactionCodes - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 10598 
		// Add PndngTransLookupIndi, ElgblePndngTransLookupIndi
		lsQry.append(
			"SELECT "
				+ "TransCd,"
				+ "TransCdDesc,"
				+ "RcptMsgCd,"
				+ "VoidableTransIndi,"
				+ "InvTransCdType,"
				+ "CumulativeTransCd,"
				+ "FeeSourceCd,"
				+ "CashDrawerIndi, "
				+ "TtlTrnsfrPnltyExmptCd, "
				+ "ETtlTransIndi, "
				+ "PndngTransLookupIndi,"
				+ "ElgblePndngTransLookupIndi "
				+ "FROM RTS.RTS_TRANS_CDS");
		// end defect 10598 

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionCodes - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryTransactionCodes - SQL - End");

			while (lrsQry.next())
			{
				TransactionCodesData laTransactionCodesData =
					new TransactionCodesData();
				laTransactionCodesData.setTransCd(
					caDA.getStringFromDB(lrsQry, "TransCd"));
				laTransactionCodesData.setTransCdDesc(
					caDA.getStringFromDB(lrsQry, "TransCdDesc"));
				laTransactionCodesData.setRcptMsgCd(
					caDA.getIntFromDB(lrsQry, "RcptMsgCd"));
				laTransactionCodesData.setVoidableTransIndi(
					caDA.getIntFromDB(lrsQry, "VoidableTransIndi"));
				laTransactionCodesData.setInvTransCdType(
					caDA.getIntFromDB(lrsQry, "InvTransCdType"));
				laTransactionCodesData.setCumulativeTransCd(
					caDA.getIntFromDB(lrsQry, "CumulativeTransCd"));
				laTransactionCodesData.setFeeSourceCd(
					caDA.getIntFromDB(lrsQry, "FeeSourceCd"));
				laTransactionCodesData.setCashDrawerIndi(
					caDA.getIntFromDB(lrsQry, "CashDrawerIndi"));
				laTransactionCodesData.setTtlTrnsfrPnltyExmptCd(
					caDA.getStringFromDB(
						lrsQry,
						"TtlTrnsfrPnltyExmptCd"));
				laTransactionCodesData.setETtlTransIndi(
					caDA.getIntFromDB(lrsQry, "ETtlTransIndi"));

				// defect 10598
				laTransactionCodesData.setPndngTransLookupIndi(
					caDA.getIntFromDB(lrsQry, "PndngTransLookupIndi"));
					
				laTransactionCodesData.setElgblePndngTransLookupIndi(
					caDA.getIntFromDB(
						lrsQry,
						"ElgblePndngTransLookupIndi"));
				// end defect 10598 

				// Add element to the Vector
				lvRslt.addElement(laTransactionCodesData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryTransactionCodes - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTransactionCodes - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS
