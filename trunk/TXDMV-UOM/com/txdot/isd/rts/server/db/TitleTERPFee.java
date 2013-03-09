package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.TitleTERPFeeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 *
 * TitleTERPFee.java
 *
 * (c) Texas Department of Transportation 2003
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		07/12/2002	Add call to closeLastStatement() after a 
 *							query
 * K Harrell	03/18/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryTitleTERPFee()	
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3   
 * ---------------------------------------------------------------------
 */

/**
 * This class allows the user to access RTS_TTL_TERP_FEE
 * 
 * @version	5.2.3		06/19/2005
 * @author 	Kathy Harrell
 * <br>Creation Date:	07/30/2003 22:24:19  
 */
public class TitleTERPFee extends TitleTERPFeeData
{
	DatabaseAccess caDA;
	/**
	 * TitleTERPFee constructor comment.
	 *
	 * @param  aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public TitleTERPFee(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}
	/**
	 * Method to Query RTS.RTS_TTL_TERP_FEE
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryTitleTERPFee()
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryTitleTERPFee - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT CntyTERPStatusCd,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "TtlTERPFlatFee,"
				+ "TERPAcctItmCd "
				+ "FROM RTS.RTS_TTL_TERP_FEE");
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryTitleTERPFee - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, " - qryTitleTERPFee - SQL - End");

			while (lrsQry.next())
			{
				TitleTERPFeeData laTitleTERPFeeData =
					new TitleTERPFeeData();
				laTitleTERPFeeData.setCntyTERPStatusCd(
					caDA.getStringFromDB(lrsQry, "CntyTERPStatusCd"));
				laTitleTERPFeeData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laTitleTERPFeeData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				laTitleTERPFeeData.setTtlTERPFlatFee(
					caDA.getDollarFromDB(lrsQry, "TtlTERPFlatFee"));
				laTitleTERPFeeData.setTERPAcctItmCd(
					caDA.getStringFromDB(lrsQry, "TERPAcctItmCd"));
				// Add element to the Vector
				lvRslt.addElement(laTitleTERPFeeData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, " - qryTitleTERPFee - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryTitleTERPFee - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} //END OF QUERY METHOD
}
