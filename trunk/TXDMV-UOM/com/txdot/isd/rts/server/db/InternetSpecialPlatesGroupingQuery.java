package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.business.SpecialPlateInfoBusiness;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse;

/*
 * InternetSpecialPlatesQuery.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob B.		04/03/2007	Initial write.
 * 							defect 9119 Ver Special Plates
 * Bob B.		08/06/2009	Check new TOL display indi column before
 *  						returning the row back to IVTRS for the 
 * 							menu build.
 * 							modify qrySPAppGrpPlt() 
 * 							defect 10149 Ver 6.0.1
 * K Harrell	09/15/2009	Update for SQL Logging 
 * 							defect 10164 Ver Defect_POS_F  	  
 * ---------------------------------------------------------------------
 */

/**
 * This is the data access class for special plate groups
 *
 * @version	Defect_POS_F	09/15/2009
 * @author	rlbrown
 * <br>Creation Date:		04/03/2007 11:12:00
 */
public class InternetSpecialPlatesGroupingQuery
{
	String csMethod = new String();
	
	DatabaseAccess caDA;

	/**
	 * InternetSpecialPlatesGroupingQuery constructor comment.
	 * 
	 * @param  aaDA 	DatabaseAccess
	 * @throws RTSException 
	 */
	public InternetSpecialPlatesGroupingQuery(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Query Special Plate Groups
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qrySPAppGrpPlt() throws RTSException
	{
		csMethod = "qrySPAppGrpPlt()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlQry =
			"select a.grpdispname, b.grppltdispname, b.grpid, "
				+ " b.grppltid, b.regpltcd, b.pltformid  from "
				+ " rts.rts_itrnt_spapp_grp a, "
				+ " rts.rts_itrnt_spapp_grp_plt b "
				// defect 10149 
				// + " where a.grpid = b.grpid"				
				+" where b.toldispindi = 1 and " + " a.grpid = b.grpid"
				// end defect 10149
				+" order by b.grppltdisporder, b.grppltdispname ";

		Vector lvData = new Vector();

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet =
				caDA.executeDBQuery(lsSqlQry.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			int i = 0;
			while (laResultSet.next())
			{
				SpecialPlatesInfoResponse laSPInfoResp =
					new SpecialPlatesInfoResponse();
				laSPInfoResp.setGrpName(
					caDA.getStringFromDB(laResultSet, "GRPDISPNAME"));
				laSPInfoResp.setGrpId(
					caDA.getIntFromDB(laResultSet, "GRPID"));
				laSPInfoResp.setPlateName(
					caDA.getStringFromDB(
						laResultSet,
						"GRPPLTDISPNAME"));
				laSPInfoResp.setPltId(
					caDA.getIntFromDB(laResultSet, "GRPPLTID"));
				laSPInfoResp.setRegPltCd(
					caDA.getStringFromDB(laResultSet, "REGPLTCD"));
				laSPInfoResp.setPltFormId(
					caDA.getStringFromDB(laResultSet, "PLTFORMID"));
				lvData.add(i, laSPInfoResp);
				i++;
			} //End of While

			laResultSet.close();
			return lvData;
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
	 * Query Special Plate Group description and Group name.
	 * 
	 * @param asGrpID String
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qrySPGrpInfo(String asGrpID) throws RTSException
	{
		csMethod = "qrySPGrpInfo()";

		Log.write(
			Log.METHOD,
			this,
			csMethod + CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlQry =
			"select grpid, grpdispdesc, grpdispname from "
				+ " rts.rts_itrnt_spapp_grp "
				+ " where grpid = "
				+ asGrpID;

		Vector lvData = new Vector();

		try
		{
			Log.write(
				Log.SQL,
				this,
				csMethod + CommonConstant.SQL_BEGIN);

			ResultSet laResultSet =
				caDA.executeDBQuery(lsSqlQry.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			int i = 0;
			while (laResultSet.next())
			{
				SpecialPlatesInfoResponse laSPInfoResp =
					new SpecialPlatesInfoResponse();
				laSPInfoResp.setGrpDesc(
					caDA.getStringFromDB(laResultSet, "GRPDISPDESC"));
				laSPInfoResp.setGrpId(
					caDA.getIntFromDB(laResultSet, "GRPID"));
				if (laSPInfoResp.getGrpId()
					== SpecialPlateInfoBusiness.GRP_PLP)
				{
					laSPInfoResp.setUserPltNoAllowedIndi(true);
				}
				laSPInfoResp.setGrpName(
					caDA.getStringFromDB(laResultSet, "GRPDISPNAME"));
				lvData.add(i, laSPInfoResp);
				i++;
			} //End of While

			laResultSet.close();
			return lvData;
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
