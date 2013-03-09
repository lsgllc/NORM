package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.CertifiedLienholderData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * CertifiedLienholder.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Zwiener	02/25/2009	Created 
 * 							defect 9968 Ver Defect_POS_E
 * K Harrell	07/02/2009	Modified to return by PermLienhldrId 
 * 							modify qryCertifiedLienholder() 
 * 							defect 10117 Ver Defect_POS_F
 * K Harrell	07/12/2009	Implement new CertifiedLienholderData
 * 							modify qryCertifiedLienholder()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/13/2009  Implement Local Options Report Sort Options	
 * 							add qryCerfifiedLienholder
 * 							  (CertifiedLienholderData,boolean)
 * 							modify qryCertifiedLienholder(CertifiedLienholderData)
 * 							defect 10250 Ver Defect_POS_G
 * K Harrell	10/20/2009	Implement ReportConstant 
 * 							modify qryCertifiedLienholder(CertifiedLienholderData)
 * 							defect 10250 Ver Defect_POS_G  
 * ---------------------------------------------------------------------
 */

/**
 * This class contains methods to access RTS_CERTFD_LIENHLDR
 *
 * @version	Defect_POS_G	10/20/2009
 * @author	Jim Zwiener
 * <br>Creation Date:		02/25/2009
 */
public class CertifiedLienholder extends CertifiedLienholderData
{

	DatabaseAccess caDA;

	/**
	 * CertifiedLienholder constructor comment.
	 *
	 * @param  aaDA  DatabaseAccess 
	 * @throws RTSException
	 */
	public CertifiedLienholder(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_CERTFD_LIENHLDR
	 *
	 * @param aaCertLienData CertifiedLienholderData
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryCertifiedLienholder(CertifiedLienholderData aaCertLienData)
		throws RTSException
	{
		// defect 10250 
		// Sort by Id 
		return qryCertifiedLienholder(
			aaCertLienData,
			ReportConstant.SORT_BY_ID);
		// end defect 10250 
	}

	/**
	 * Method to Query RTS.RTS_CERTFD_LIENHLDR
	 *
	 * @param  aaCertLienData CertifiedLienholderData
	 * @param  abIdSort boolean 
	 * @return Vector
	 * @throws RTSException 
	 */
	public Vector qryCertifiedLienholder(
		CertifiedLienholderData aaCertLienData,
		boolean abIdSort)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryCertifiedLienholder - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();

		ResultSet lrsQry;

		lsQry.append(
			"SELECT "
				+ "PermLienHldrId,"
				+ "CertfdLienhldrName1,"
				+ "CertfdLienhldrName2,"
				+ "CertfdLienHldrSt1,"
				+ "CertfdLienHldrSt2,"
				+ "CertfdLienHldrCity,"
				+ "CertfdLienHldrState,"
				+ "CertfdLienHldrZpCd,"
				+ "CertfdLienHldrZpCdP4,"
				+ "CertfdLienHldrCntry,"
				+ "RTSEffDate,"
				+ "RTSEffEndDate,"
				+ "ELienRdyIndi "
				+ "FROM RTS.RTS_CERTFD_LIENHLDR ");

		boolean lbElienRdyIndi = aaCertLienData.isElienRdy();

		boolean lbPermLienhldrId =
			!UtilityMethods.isEmpty(aaCertLienData.getPermLienHldrId());

		if (lbPermLienhldrId || lbElienRdyIndi)
		{
			lsQry.append(" WHERE ");

			if (lbElienRdyIndi)
			{
				String lsSuffix = lbPermLienhldrId ? " and " : " ";
				lsQry.append(" ELienRdyIndi = 1 " + lsSuffix);
			}
			if (lbPermLienhldrId)
			{
				lsQry.append(" PermLienHldrId = ? ");
				lvValues.addElement(
					new DBValue(
						Types.CHAR,
						DatabaseAccess.convertToString(
							aaCertLienData.getPermLienHldrId())));
			}
		}

		String lsSortBy =
			abIdSort
				? "PermLienHldrId"
				: "CertfdLienhldrName1,CertfdLienhldrName2";

		lsQry.append(" ORDER BY " + lsSortBy + ", RTSEffDate Desc");

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryCertifiedLienholder - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryCertifiedLienholder - SQL - End");
			while (lrsQry.next())
			{
				CertifiedLienholderData laCertfdLienhldrData =
					new CertifiedLienholderData();
				laCertfdLienhldrData.setPermLienHldrId(
					caDA.getStringFromDB(lrsQry, "PermLienhldrId"));

				laCertfdLienhldrData.setName1(
					caDA.getStringFromDB(
						lrsQry,
						"CertfdLienhldrName1"));
				laCertfdLienhldrData.setName2(
					caDA.getStringFromDB(
						lrsQry,
						"CertfdLienhldrName2"));
 
				laCertfdLienhldrData.setRTSEffDate(
					caDA.getIntFromDB(lrsQry, "RTSEffDate"));
				laCertfdLienhldrData.setRTSEffEndDate(
					caDA.getIntFromDB(lrsQry, "RTSEffEndDate"));
				laCertfdLienhldrData.setElienRdyIndi(
					caDA.getIntFromDB(lrsQry, "ElienRdyIndi"));

				AddressData laAddrData = new AddressData();
				laAddrData.setSt1(
					caDA.getStringFromDB(lrsQry, "CertfdLienHldrSt1"));
				laAddrData.setSt2(
					caDA.getStringFromDB(lrsQry, "CertfdLienHldrSt2"));
				laAddrData.setCity(
					caDA.getStringFromDB(lrsQry, "CertfdLienHldrCity"));
				laAddrData.setState(
					caDA.getStringFromDB(
						lrsQry,
						"CertfdLienHldrState"));
				laAddrData.setZpcd(
					caDA.getStringFromDB(lrsQry, "CertfdLienHldrZpCd"));
				laAddrData.setZpcdp4(
					caDA.getStringFromDB(
						lrsQry,
						"CertfdLienHldrZpCdP4"));
				laAddrData.setCntry(
					caDA.getStringFromDB(
						lrsQry,
						"CertfdLienHldrCntry"));
				laCertfdLienhldrData.setAddressData(laAddrData);

				// Add element to the Vector 
				lvRslt.addElement(laCertfdLienhldrData);
			} //End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryCertifiedLienholder - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryCertifiedLienholder - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"qryCertifiedLienholder - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
	} //END OF QUERY METHOD

}
