package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com
	.txdot
	.isd
	.rts
	.services
	.data
	.WebServicesServiceActionVersionData;
import com.txdot.isd.rts.services.data.WebServicesServiceActionVersionListData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;

/*
 * WebServicesServiceActionVersionSql.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/02/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * SQL class for RTS_SRVC_ACTN_VERSION table.
 *
 * @version	MyPlates_POS	07/02/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		07/02/2008 14:53:36
 */

public class WebServicesServiceActionVersionSql
{
	DatabaseAccess caDA;
	
	/**
	 * WebServicesServiceActionVersionSql.java Constructor
	 * 
	 * <p>Sets the DatabaseAccess object.
	 * 
	 * @param aaDA
	 * @throws RTSException
	 */
	public WebServicesServiceActionVersionSql(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_SRVC_ACTN_VERSION
	 *
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qryRtsSrvcActnVersion() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryRtsSrvcActnVersion - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "SavId, "
				+ "SavDesc, "
				+ "SrvId, "
				+ "ActnId, "
				+ "Version, "
				+ "DeleteIndi "
				+ "FROM RTS.RTS_SRVC_ACTN_VERSION "
				+ "ORDER BY SavId");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryRtsSrvcActnVersion - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryRtsSrvcActnVersion - SQL - End");

			while (lrsQry.next())
			{
				WebServicesServiceActionVersionData laWebServiceActionVersionData =
					new WebServicesServiceActionVersionData();
				laWebServiceActionVersionData.setSavId(
					caDA.getIntFromDB(lrsQry, "SavId"));
				laWebServiceActionVersionData.setSavDesc(
					caDA.getStringFromDB(lrsQry, "SavDesc"));
				laWebServiceActionVersionData.setSrvcId(
					caDA.getIntFromDB(lrsQry, "SrvId"));
				laWebServiceActionVersionData.setActnId(
					caDA.getIntFromDB(lrsQry, "ActnId"));
				laWebServiceActionVersionData.setVersion(
					caDA.getIntFromDB(lrsQry, "Version"));
				laWebServiceActionVersionData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				lvRslt.addElement(laWebServiceActionVersionData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryRtsSrvcActnVersion - End ");
			return (lvRslt);
		}

		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRtsSrvcActnVersion - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}

	/**
	 * Method to Query RTS.RTS_SRVC_ACTN_VERSION with RTS.RTS_SRVC name.
	 *
	 * @return Vector 
	 * @throws RTSException	
	 */
	public Vector qryRtsSrvcActnVersionList() throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryRtsSrvcActnVersionList - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "SavId, "
				+ "SavDesc, "
				+ "A.SrvcId, "
				+ "SrvcName, "
				+ "ActnId, "
				+ "Version, "
				+ "DeleteIndi "
				+ "FROM RTS.RTS_SRVC_ACTN_VERSION A, "
				+ "RTS.RTS_SRVC B "
				+ "ORDER BY A.SrvcId, ActnId, Version");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryRtsSrvcActnVersionList - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(
				Log.SQL,
				this,
				" - qryRtsSrvcActnVersionList - SQL - End");

			while (lrsQry.next())
			{
				WebServicesServiceActionVersionListData laWebServiceActionVersionListData =
					new WebServicesServiceActionVersionListData();
				laWebServiceActionVersionListData.setSavId(
					caDA.getIntFromDB(lrsQry, "SavId"));
				laWebServiceActionVersionListData.setSavDesc(
					caDA.getStringFromDB(lrsQry, "SavDesc"));
				laWebServiceActionVersionListData.setSrvcId(
					caDA.getIntFromDB(lrsQry, "SrvcId"));
				laWebServiceActionVersionListData.setSrvcName(
					caDA.getStringFromDB(lrsQry, "SrvcName"));
				laWebServiceActionVersionListData.setActnId(
					caDA.getIntFromDB(lrsQry, "ActnId"));
				laWebServiceActionVersionListData.setVersion(
					caDA.getIntFromDB(lrsQry, "Version"));
				laWebServiceActionVersionListData.setDeleteIndi(
					caDA.getIntFromDB(lrsQry, "DeleteIndi"));

				lvRslt.addElement(laWebServiceActionVersionListData);
			}

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryRtsSrvcActnVersionList - End ");
			return (lvRslt);
		}

		catch (SQLException leSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryRtsSrvcActnVersionList - SQL Exception "
					+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	}

}
