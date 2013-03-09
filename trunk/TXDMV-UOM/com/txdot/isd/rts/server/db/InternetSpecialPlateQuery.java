package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.webapps.util.constants.ServiceConstants;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.webapps.order.common.data.Fees;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.business.SpecialPlateInfoBusiness;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlateDesign;
import com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse;

/*
 * InternetSpecialPlateQuery.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob B.		04/04/2007	Initial write.
 * 							defect 9119 Ver Special Plates
 * Bob B.		01/23/2008	Add query for getting SP images
 * 							add qrySPImages(), qrySPImageNames
 * 							defect 9473 Ver Tres Amigos Prep
 * B Hargrove	07/11/2008	Add pilot for RTS_PLT_TYPE.  
 * 							modify qryCompletePltInfo()
 * 							defect 9689 Ver MyPlates_POS
 * Bob B.		08/05/2009	Check new TOL display indi column before
 *  						returning the row back to IVTRS for
 * 							determining which images to send to TxO.
 * 							defect 10149 Ver 6.0.1 	
 * K Harrell	09/15/2009	Update for SQL Logging 
 * 							defect 10164 Ver Defect_POS_F   
 * B Brown		01/31/2011	Add new rts_org_no.crossoverindi and 
 * 							rts_itrnt_spapp_grp_plt.vendorplturl to the 
 * 							query.
 * 							modify qryCompletePltInfo()
 * 							defect 10693 Ver POS_670.  
 * K Harrell	09/30/2011	Remove reference to pilot qualifier
 * 							modify qryCompletePltInfo()
 * 							defect 11048 Ver 6.9.0 
 * K Harrell	10/28/2011	Restore reference to pilot qualifier
 * 							modify qryCompletePltInfo()
 * 							defect 11061 Ver 6.9.0             
 * ---------------------------------------------------------------------
 */

/**
 * This is the data access class for plate details
 * 
 * @version 6.9.0		10/28/2011
 * @author Robert Brown
 * @since 				04/04/2007 13:55:00
 */

public class InternetSpecialPlateQuery
{

	String csMethod = new String();

	DatabaseAccess caDA;

	/**
	 * 
	 * InternetSpecialPlateQuery.java Constructor
	 * 
	 * @param aaDA
	 * @throws RTSException
	 */
	public InternetSpecialPlateQuery(DatabaseAccess aaDA)
			throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Query to find an individual plate
	 * 
	 * @param aiGrpID
	 *            int
	 * @param aiPltID
	 *            int
	 * @param asPltDesign
	 *            String
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryCompletePltInfo(int aiGrpID, int aiPltID,
			String asPltDesign) throws RTSException
	{
		csMethod = "qryCompletePltInfo()";
		Log.write(Log.METHOD, this, csMethod
				+ CommonConstant.SQL_METHOD_BEGIN);

		String lsWhere = CommonConstant.STR_SPACE_EMPTY;
		if (aiGrpID != 0)
		{
			lsWhere += " AND C.grpid = " + aiGrpID + " ";
		}

		if (aiPltID != 0)
		{
			lsWhere += " AND C.grppltid = " + aiPltID + " ";
		}

		if (asPltDesign != null && asPltDesign.length() > 0)
		{
			lsWhere += " AND A.regpltdesign = '" + asPltDesign + "' ";
		}

		String lsOrderBy = CommonConstant.STR_SPACE_EMPTY;
		// Returning multiple records we must order.
		if (aiGrpID == 0 || aiPltID == 0)
		{
			lsOrderBy = " ORDER BY grpdisporder, grppltdisporder";
		}

		// Reference PILOT qualifier for RTS_PLT_TYPE
		String lsTableCreator = CommonConstant.RTS_TBCREATOR;

		// defect 11061
		 if (SystemProperty.isStaticTablePilot())
		 {
		 lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		 }
		 // end defect 11061

		// defect 10693
		int liCurrentDate = new RTSDate().getYYYYMMDDDate();
		// end defect 10693

		String lsSqlQry = "SELECT distinct A.regpltcd, C.plpregpltcd, "
				+ "C.addlsetregpltcd, C.addlsetplpregpltcd, "
				+ "A.pltsetimprtncecd, A.ISAAllowdCd, A.firstsetapplfee, "
				+ "Coalesce(E.addlsetapplfee, 0.00) AS ADDLSETAPPLFEE, "
				+ "A.firstsetrenwlfee, "
				+ "Coalesce(E.addlsetrenwlfee, 0.00) AS ADDLSETRENWLFEE, "
				+ "A.locinetauthcd, A.replfee, A.regpltdesign, "
				+ "Coalesce(B.maxbytecount, 0) AS MAXBYTECOUNT, "
				+ "A.loccntyauthcd, Coalesce(B.plpfee, 0.00) AS PLPFEE, "
				+ "Coalesce(B.userpltnoindi, 0) AS USERPLTNOINDI, "
				+ "Coalesce(B.plpacctitmcd, '') AS PLPACCTITMCD, "
				+ "Coalesce(O.acctitmcd, '') AS ACCTITMCD, "
				// defect 10693
				+ "Coalesce(O.crossoverindi, 0) AS CROSSOVERINDI, "
				+ "Coalesce(C.vendorplturl, '') AS VENDORPLTURL,"
				// end defect 10693
				+ "C.grppltdispdesc, C.grppltdispname, C.grpid, C.grppltid, "
				+ "C.orgno, C.pltimage, C.pltformid, C.pltfaxform, "
				+ "C.grppltdisporder, D.grpdispdesc, D.grpdispname, "
				+ "D.grpdisporder FROM rts.rts_itrnt_spapp_grp D, "
				+ "rts.rts_itrnt_spapp_grp_plt C "
				// + "LEFT OUTER JOIN rts.rts_plt_type B ON C.plpregpltcd =
				// B.regpltcd "
				// + "LEFT OUTER JOIN rts.rts_plt_type E ON C.addlsetregpltcd =
				// E.regpltcd, "
				// + "rts.rts_plt_type A, rts.rts_org_no O WHERE "
				+ "LEFT OUTER JOIN "
				+ lsTableCreator
				+ ".rts_plt_type B ON C.plpregpltcd = B.regpltcd "
				+ "LEFT OUTER JOIN "
				+ lsTableCreator
				+ ".rts_plt_type E ON C.addlsetregpltcd = E.regpltcd, "
				+ lsTableCreator
				+ ".rts_plt_type A, rts.rts_org_no O WHERE "
				+ "A.baseregpltcd = O.baseregpltcd AND C.orgno = O.orgno "
				+ "AND C.regpltcd = A.regpltcd and C.grpid = D.grpid "
				// defect 10693
				+ " AND "
				+ liCurrentDate
				+ " BETWEEN A.RTSEffDate and A.RTSEffEndDate "
				+ " AND "
				+ liCurrentDate
				+ " BETWEEN B.RTSEffDate and B.RTSEffEndDate "
				+ " AND "
				+ liCurrentDate
				+ " BETWEEN E.RTSEffDate and E.RTSEffEndDate "
				// end defect 10693
				+ lsWhere
				+ "UNION ALL SELECT distinct A.regpltcd, "
				+ "C.plpregpltcd, C.addlsetregpltcd, C.addlsetplpregpltcd, "
				+ "A.pltsetimprtncecd, A.ISAAllowdCd, A.firstsetapplfee, "
				+ "Coalesce(E.addlsetapplfee, 0.00) AS ADDLSETAPPLFEE, "
				+ "A.firstsetrenwlfee, "
				+ "Coalesce(E.addlsetrenwlfee, 0.00) AS ADDLSETRENWLFEE, "
				+ "A.locinetauthcd, A.replfee, A.regpltdesign, "
				+ "Coalesce(B.maxbytecount, 0) AS MAXBYTECOUNT, "
				+ "A.loccntyauthcd, Coalesce(B.plpfee, 0.00) AS PLPFEE, "
				+ "Coalesce(B.userpltnoindi, 0) AS USERPLTNOINDI, "
				+ "Coalesce(B.plpacctitmcd, '') AS PLPACCTITMCD, "
				// defect 10693
				// + "'' AS ACCTITMCD, C.grppltdispdesc, C.grppltdispname, "
				+ "'' AS ACCTITMCD, "
				+ "0 as CROSSOVERINDI, '' AS VENDORPLTURL, C.grppltdispdesc, C.grppltdispname, "
				// end defect 10693
				+ "C.grpid, C.grppltid, C.orgno, C.pltimage, C.pltformid, "
				+ "C.pltfaxform, C.grppltdisporder, D.grpdispdesc, D.grpdispname, "
				// + "D.grpdisporder FROM rts.rts_plt_type A,
				// rts.rts_itrnt_spapp_grp D, "
				+ "D.grpdisporder FROM "
				+ lsTableCreator
				+ ".rts_plt_type A, rts.rts_itrnt_spapp_grp D, "
				+ "rts.rts_itrnt_spapp_grp_plt C "
				// + "LEFT OUTER JOIN rts.rts_plt_type B ON C.plpregpltcd =
				// B.regpltcd "
				// + "LEFT OUTER JOIN rts.rts_plt_type E ON C.addlsetregpltcd =
				// E.regpltcd "
				+ "LEFT OUTER JOIN "
				+ lsTableCreator
				+ ".rts_plt_type B ON C.plpregpltcd = B.regpltcd "
				+ "LEFT OUTER JOIN "
				+ lsTableCreator
				+ ".rts_plt_type E ON C.addlsetregpltcd = E.regpltcd "
				+ "WHERE NOT EXISTS("
				+ "SELECT * FROM rts.rts_org_no O WHERE "
				+ "A.baseregpltcd = O.baseregpltcd and C.orgno = O.orgno) "
				+ "AND C.regpltcd = A.regpltcd and C.grpid = D.grpid "
				// defect 10693
				+ " AND " + liCurrentDate
				+ " BETWEEN A.RTSEffDate and A.RTSEffEndDate "
				+ " AND " + liCurrentDate
				+ " BETWEEN B.RTSEffDate and B.RTSEffEndDate "
				+ " AND " + liCurrentDate
				+ " BETWEEN E.RTSEffDate and E.RTSEffEndDate "
				// end defect 10693
				+ lsWhere + lsOrderBy;

		Vector lvData = new Vector();

		try
		{
			Log.write(Log.SQL, this, csMethod
					+ CommonConstant.SQL_BEGIN);

			ResultSet laResultSet = caDA.executeDBQuery(lsSqlQry);

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			int i = 0;
			while (laResultSet.next())
			{
				SpecialPlateInfoBusiness laSPInfoBus = new SpecialPlateInfoBusiness();

				SpecialPlatesInfoResponse laSPInfoResp = new SpecialPlatesInfoResponse();
				laSPInfoResp.setGrpId(aiGrpID);
				laSPInfoResp.setPltId(aiPltID);

				// regpltcd and plpregpltcd
				String lsRegPltCd = caDA.getStringFromDB(laResultSet,
						"REGPLTCD");
				laSPInfoResp.setRegPltCd(lsRegPltCd);
				laSPInfoResp.setPlpRegPltCd(caDA.getStringFromDB(
						laResultSet, "PLPREGPLTCD"));
				laSPInfoResp.setAddlSetRegPltCd(caDA.getStringFromDB(
						laResultSet, "ADDLSETREGPLTCD"));
				laSPInfoResp.setAddlSetPlpRegPltCd(caDA
						.getStringFromDB(laResultSet,
								"ADDLSETPLPREGPLTCD"));

				// pltsetimprtncecd
				int liPltSetImpCD = caDA.getIntFromDB(laResultSet,
						"PLTSETIMPRTNCECD");

				if (liPltSetImpCD > 0)
				{
					laSPInfoResp.setAddlSetIndi(true);
				}
				else
				{
					laSPInfoResp.setAddlSetIndi(false);
				}

				// setOrderOnline - add to sql above
				String lsLocInetAuthCD = caDA.getStringFromDB(
						laResultSet, "LOCINETAUTHCD");
				laSPInfoResp
						.setOrderOnline(lsLocInetAuthCD != null
								&& (lsLocInetAuthCD.equals("B") || lsLocInetAuthCD
										.equals("O")));

				String lsLocCntyAuthCD = caDA.getStringFromDB(
						laResultSet, "LOCCNTYAUTHCD");
				laSPInfoResp
						.setOrderableAtCnty(lsLocCntyAuthCD != null
								&& (lsLocCntyAuthCD.equals("B") || lsLocCntyAuthCD
										.equals("O")));
				// Souvenir
				if (aiGrpID == SpecialPlateInfoBusiness.GRP_SOUVENIR)
				{
					laSPInfoResp.setOrderableAtCnty(false);
					laSPInfoResp.setAddlSetIndi(false);
					laSPInfoResp.setOrderOnline(false);
				}

				// ISAAllowdCd
				String lsISAAllowed = caDA.getStringFromDB(laResultSet,
						"ISAALLOWDCD");

				if (lsISAAllowed.equals("B"))
				{
					laSPInfoResp.setIsaAllowIndi(true);
				}

				else
				{
					laSPInfoResp.setIsaAllowIndi(false);
				}

				// Begin Fees
				String lsAcctItmCd = caDA.getStringFromDB(laResultSet,
						"ACCTITMCD");
				// Used for the key to lookup
				// Registration Fee Exceptions
				String lsFirst = "F";
				String lsAdditional = "A";
				// There are 6 fees 2 single, 2 additional,
				// 1 personalization, 1 replacement
				Fees[] larrFees = new Fees[6];
				// Fee 1 - Single
				Fees laSingleSetFee = new Fees();
				laSingleSetFee.setItmQty(1);
				laSingleSetFee
						.setDesc(ServiceConstants.SPI_FEE_SINGLE_SET);
				laSingleSetFee.setAcctItmCd(lsAcctItmCd);
				// Souvenir
				if (aiGrpID == SpecialPlateInfoBusiness.GRP_SOUVENIR)
				{
					laSingleSetFee.setItemPrice(Double
							.parseDouble("20.00"));
				}
				else
				{
					laSingleSetFee
							.setItemPrice(Double
									.parseDouble(laSPInfoBus
											.handleApplFeeExceptions(
													lsRegPltCd
															+ lsFirst,
													caDA
															.getStringFromDB(
																	laResultSet,
																	"FIRSTSETAPPLFEE"))));
				}
				larrFees[0] = laSingleSetFee;
				// Fee 2 - Single
				Fees laSingleSetRenewFee = new Fees();
				laSingleSetRenewFee.setItmQty(1);
				laSingleSetRenewFee
						.setDesc(ServiceConstants.SPI_FEE_SINGLE_SET_RENEW);
				laSingleSetRenewFee.setAcctItmCd(lsAcctItmCd);
				// Souvenir
				if (aiGrpID == SpecialPlateInfoBusiness.GRP_SOUVENIR)
				{
					laSingleSetRenewFee.setItemPrice(Double
							.parseDouble("0.00"));
				}
				else
				{
					laSingleSetRenewFee
							.setItemPrice(Double
									.parseDouble(laSPInfoBus
											.handleRegFeeExceptions(
													lsRegPltCd
															+ lsFirst,
													caDA
															.getStringFromDB(
																	laResultSet,
																	"FIRSTSETRENWLFEE"))));
				}
				larrFees[1] = laSingleSetRenewFee;
				// Fee 3 - Addtional
				Fees laAddSetFee = new Fees();
				laAddSetFee.setItmQty(1);
				laAddSetFee.setDesc(ServiceConstants.SPI_FEE_ADD_SET);
				laAddSetFee.setAcctItmCd(lsAcctItmCd);
				laAddSetFee.setItemPrice(Double.parseDouble(laSPInfoBus
						.handleApplFeeExceptions(lsRegPltCd
								+ lsAdditional, caDA.getStringFromDB(
								laResultSet, "ADDLSETAPPLFEE"))));
				larrFees[2] = laAddSetFee;
				// Fee 4 - Addtional
				Fees laAddSetRenewFee = new Fees();
				laAddSetRenewFee.setItmQty(1);
				laAddSetRenewFee
						.setDesc(ServiceConstants.SPI_FEE_ADD_SET_RENEW);
				laAddSetRenewFee.setAcctItmCd(lsAcctItmCd);
				laAddSetRenewFee.setItemPrice(Double
						.parseDouble(laSPInfoBus
								.handleRegFeeExceptions(lsRegPltCd
										+ lsAdditional, caDA
										.getStringFromDB(laResultSet,
												"ADDLSETRENWLFEE"))));
				larrFees[3] = laAddSetRenewFee;
				// Fee 5 - Personalization
				Fees laPlpFee = new Fees();
				laPlpFee.setItmQty(1);
				laPlpFee
						.setDesc(ServiceConstants.SPI_FEE_PERSONALIZATION);
				laPlpFee.setAcctItmCd(caDA.getStringFromDB(laResultSet,
						"PLPACCTITMCD"));
				laPlpFee.setItemPrice(Double.parseDouble(caDA
						.getStringFromDB(laResultSet, "PLPFEE")));
				larrFees[4] = laPlpFee;
				// Fee 6 - Replacement
				Fees laRepFee = new Fees();
				laRepFee.setItmQty(1);
				laRepFee.setDesc(ServiceConstants.SPI_FEE_REPLACEMENT);
				laRepFee.setAcctItmCd(CommonConstant.STR_SPACE_EMPTY);
				// Souvenir
				if (aiGrpID == SpecialPlateInfoBusiness.GRP_SOUVENIR)
				{
					laRepFee.setItemPrice(Double.parseDouble("0.00"));
				}
				else
				{
					laRepFee.setItemPrice(Double.parseDouble(caDA
							.getStringFromDB(laResultSet, "REPLFEE")));
				}
				larrFees[5] = laRepFee;

				laSPInfoResp.setFees(larrFees);
				// End Fees

				// userpltnoindi
				laSPInfoResp
						.setUserPltNoAllowedIndi(caDA.getBooleanFromDB(
								laResultSet, "USERPLTNOINDI"));

				// regpltdesign
				String lsSpecPltDesign = caDA.getStringFromDB(
						laResultSet, "REGPLTDESIGN");
				SpecialPlateDesign laSpecPltDesign = laSPInfoBus
						.getPlateDesignData(lsSpecPltDesign);

				// Max Byte Count
				laSpecPltDesign.setPltPLPMaxChar(caDA.getIntFromDB(
						laResultSet, "MAXBYTECOUNT"));
				laSPInfoResp.setPltDesign(laSpecPltDesign);

				// group and plate descriptions and names
				laSPInfoResp.setGrpDesc(caDA.getStringFromDB(
						laResultSet, "GRPDISPDESC"));
				laSPInfoResp.setPltDesc(caDA.getStringFromDB(
						laResultSet, "GRPPLTDISPDESC"));

				laSPInfoResp.setGrpName(caDA.getStringFromDB(
						laResultSet, "GRPDISPNAME"));
				laSPInfoResp.setPlateName(caDA.getStringFromDB(
						laResultSet, "GRPPLTDISPNAME"));

				// grpid and pltid
				laSPInfoResp.setGrpId(caDA.getIntFromDB(laResultSet,
						"GRPID"));
				laSPInfoResp.setPltId(caDA.getIntFromDB(laResultSet,
						"GRPPLTID"));

				// orgno
				laSPInfoResp.setOrgNo(caDA.getStringFromDB(laResultSet,
						"ORGNO"));

				// pltimage
				laSPInfoResp.setPltImage(caDA.getStringFromDB(
						laResultSet, "PLTIMAGE"));
				// pltformid
				laSPInfoResp.setPltFormId(caDA.getStringFromDB(
						laResultSet, "PLTFORMID"));

				// pltfaxform
				laSPInfoResp.setPltFaxForm(caDA.getStringFromDB(
						laResultSet, "PLTFAXFORM"));
				// defect 10693
				laSPInfoResp.setCrossoverIndi(caDA.getIntFromDB(
						laResultSet, "CROSSOVERINDI"));
				laSPInfoResp.setVendorPlateURL(caDA.getStringFromDB(
						laResultSet, "VENDORPLTURL"));
				// end defect 10693

				lvData.add(i, laSPInfoResp);

				i++;

			} // End of While

			laResultSet.close();
			return lvData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		catch (Exception aeEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qrySPAppGrpPlt - Exception "
							+ aeEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeEx);
		}
		finally
		{
			Log.write(Log.METHOD, this, csMethod
					+ CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Determines whether or not a Special Pleas has a regpltcd
	 * 
	 * @param aiGrpID
	 *            String
	 * @param aiPltID
	 *            String
	 * @return boolean
	 */
	public boolean doesRegPltCdExist(int aiGrpID, int aiPltID)
			throws RTSException
	{
		csMethod = "doesRegPltCdExist()";
		Log.write(Log.METHOD, this, csMethod
				+ CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlQry = "select regpltcd from "
				+ " rts.rts_itrnt_spapp_grp_plt " + " where grpid = "
				+ aiGrpID + " and grppltid = " + aiPltID;

		boolean lbDoesRegPltCdExist = false;

		try
		{
			Log.write(Log.SQL, this, csMethod
					+ CommonConstant.SQL_BEGIN);

			ResultSet laResultSet = caDA.executeDBQuery(lsSqlQry
					.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			while (laResultSet.next())
			{
				if (caDA.getStringFromDB(laResultSet, "REGPLTCD")
						.equals("")
						|| caDA
								.getStringFromDB(laResultSet,
										"REGPLTCD") == null)
				{
					lbDoesRegPltCdExist = false;
				}
				else
				{
					lbDoesRegPltCdExist = true;
				}
			} // End of While
			laResultSet.close();
			return lbDoesRegPltCdExist;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			Log.write(Log.METHOD, this, csMethod
					+ CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * Determines whether or not a Special Pleas has a regpltcd
	 * 
	 * @param aiGrpID
	 *            String
	 * @param aiPltID
	 *            String
	 * @return Vector
	 */
	public Vector qrySPAppGrpPlt(int aiGrpID, int aiPltID)
			throws RTSException
	{
		csMethod = "qrySPAppGrpPlt()";

		Log.write(Log.METHOD, this, csMethod
				+ CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlQry = "select a.grpdispname, a.grpdispdesc, "
				+ "b.grppltdispname, b.grppltdispdesc, "
				+ "b.pltformid, b.pltimage "
				+ "from rts.rts_itrnt_spapp_grp a, "
				+ "rts.rts_itrnt_spapp_grp_plt b "
				+ "where grppltid = " + aiPltID
				+ " and b.grpid = a.grpid";

		Vector lvData = new Vector();

		try
		{

			Log.write(Log.SQL, this, csMethod
					+ CommonConstant.SQL_BEGIN);

			ResultSet laResultSet = caDA.executeDBQuery(lsSqlQry
					.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			int i = 0;
			while (laResultSet.next())
			{
				SpecialPlatesInfoResponse laSPInfoResp = new SpecialPlatesInfoResponse();
				laSPInfoResp.setGrpId(aiGrpID);
				laSPInfoResp.setPltId(aiPltID);
				laSPInfoResp.setGrpName(caDA.getStringFromDB(
						laResultSet, "GRPDISPNAME"));
				laSPInfoResp.setGrpDesc(caDA.getStringFromDB(
						laResultSet, "GRPDISPDESC"));
				laSPInfoResp.setPlateName(caDA.getStringFromDB(
						laResultSet, "GRPPLTDISPNAME"));
				laSPInfoResp.setPltDesc(caDA.getStringFromDB(
						laResultSet, "GRPPLTDISPDESC"));
				laSPInfoResp.setOrderOnline(false);
				laSPInfoResp.setOrderableAtCnty(true);
				laSPInfoResp.setPltFormId(caDA.getStringFromDB(
						laResultSet, "PLTFORMID"));
				laSPInfoResp.setAddlSetIndi(false);
				laSPInfoResp.setAllowAddSetOnline(false);
				laSPInfoResp.setAllowISAOnline(false);
				laSPInfoResp.setIsaAllowIndi(false);
				laSPInfoResp.setPltImage(caDA.getStringFromDB(
						laResultSet, "PLTIMAGE"));
				laSPInfoResp.setUserPltNoAllowedIndi(false);

				SpecialPlateInfoBusiness laSPPltInfoBus = new SpecialPlateInfoBusiness();
				laSPInfoResp.setPltDesign(laSPPltInfoBus
						.getPlateDesignData(null));

				lvData.add(i, laSPInfoResp);
				i++;
			} // End of While

			laResultSet.close();
			return lvData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			Log.write(Log.METHOD, this, csMethod
					+ CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * qrySPImages
	 * 
	 * @param asPltImageName
	 * @return Vector
	 * @throws RTSException
	 */

	public Vector qrySPImages(String asPltImageName)
			throws RTSException
	{
		csMethod = "qrySPImages()";
		Log.write(Log.METHOD, this, csMethod
				+ CommonConstant.SQL_METHOD_BEGIN);

		String lsSqlRecsQry = "select pltimage, pltimagedata from "
				+ "rts.rts_itrnt_spapp_grp_plt " + "where pltimage = '"
				+ asPltImageName + "'";

		Vector lvData = new Vector();

		try
		{

			Log.write(Log.SQL, this, csMethod
					+ CommonConstant.SQL_BEGIN);

			ResultSet laResultSet = caDA.executeDBQuery(lsSqlRecsQry
					.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			laResultSet.next();
			// just grab the first result in case there is nore that 1.
			SpecialPlatesInfoResponse laSPInfoResp = new SpecialPlatesInfoResponse();
			laSPInfoResp.setPltImage(caDA.getStringFromDB(laResultSet,
					"PLTIMAGE"));
			laSPInfoResp.setPlateImageObject((Object) caDA
					.getByteArrayFromDB(laResultSet, "PLTIMAGEDATA"));

			if (laSPInfoResp.getPlateImageObject() == null)
			{
				System.err.println("No plate image available in "
						+ "pltimagedata for plate image = "
						+ asPltImageName);
			}

			lvData.add(0, laSPInfoResp);
			laResultSet.close();
			return lvData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			Log.write(Log.METHOD, this, csMethod
					+ CommonConstant.SQL_METHOD_END);
		}
	}

	/**
	 * 
	 * qrySPImageNames
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qrySPImageNames() throws RTSException
	{
		csMethod = "qrySPImageNames()";
		Log.write(Log.METHOD, this, csMethod
				+ CommonConstant.SQL_METHOD_BEGIN);

		// defect 10149
		// String lsSqlRecsQry =
		// "select distinct pltimage from " +
		// "rts.rts_itrnt_spapp_grp_plt where pltimage is not null " +
		// "order by pltimage";
		String lsSqlRecsQry = "select distinct pltimage from "
				+ "rts.rts_itrnt_spapp_grp_plt where pltimage is not null "
				+ "and toldispindi = 1 " + "order by pltimage";
		// end defect 10149
		Vector lvData = new Vector();
		try
		{
			Log.write(Log.SQL, this, csMethod
					+ CommonConstant.SQL_BEGIN);

			ResultSet laResultSet = caDA.executeDBQuery(lsSqlRecsQry
					.toString());

			Log.write(Log.SQL, this, csMethod + CommonConstant.SQL_END);

			int i = 0;
			while (laResultSet.next())
			{
				SpecialPlatesInfoResponse laSPInfoResp = new SpecialPlatesInfoResponse();
				laSPInfoResp.setPltImage(caDA.getStringFromDB(
						laResultSet, "PLTIMAGE"));
				lvData.add(i, laSPInfoResp);
				i++;
			} // End of While

			if (i == 0)
			{
				System.err.println("0 records returned for query "
						+ lsSqlRecsQry);
			}

			laResultSet.close();
			return lvData;
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod
					+ CommonConstant.SQL_EXCEPTION
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
		finally
		{
			Log.write(Log.METHOD, this, csMethod
					+ CommonConstant.SQL_METHOD_END);
		}
	}
}
