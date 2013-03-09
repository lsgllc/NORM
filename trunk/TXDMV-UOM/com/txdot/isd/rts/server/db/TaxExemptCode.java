package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.TaxExemptCodeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 *
 * TaxExemptCode.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryTaxExemptCode() 
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3
 * K Harrell	10/10/2011	modify qryTaxExempt()
 * 							defect 11048 Ver 6.9.0   
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_TAX_EXMPT_CD
 * 
 * @version	6.9.0		10/10/2011
 * @author 	Kathy Harrell
 * @since 				08/31/2001 15:30:34 
 */

public class TaxExemptCode extends TaxExemptCodeData
{
	DatabaseAccess caDA;
	
	/**
	 * TaxExemptCode constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public TaxExemptCode(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_TAX_EXMPT_CD
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTaxExemptCode()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTaxExemptCode - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;
		
		// defect 11048 
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		
		if (SystemProperty.isStaticTablePilot())
		{
			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		}

		lsQry.append(
			"SELECT "
				+ "SalesTaxExmptCd,"
				+ "SalesTaxExmptDesc,"
				+ "IMCNoReqd "
				+ "FROM "
				+ lsTableCreator
				+ ".RTS_TAX_EXMPT_CD");
		// end defect 11048 
		
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTaxExemptCode - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryTaxExemptCode - SQL - End");

			while (lrsQry.next())
			{
				TaxExemptCodeData laTaxExemptCodeData =
					new TaxExemptCodeData();
				laTaxExemptCodeData.setSalesTaxExmptCd(
					caDA.getIntFromDB(lrsQry, "SalesTaxExmptCd"));
				laTaxExemptCodeData.setSalesTaxExmptDesc(
					caDA.getStringFromDB(lrsQry, "SalesTaxExmptDesc"));
				laTaxExemptCodeData.setIMCNoReqd(
					caDA.getIntFromDB(lrsQry, "IMCNoReqd"));
				// Add element to the Vector
				lvRslt.addElement(laTaxExemptCodeData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryTaxExemptCode - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTaxExemptCode - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTaxExemptCode - Exception "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
	} //END OF QUERY METHOD
} //END OF CLASS