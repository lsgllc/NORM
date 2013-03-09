package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.PlateTypeData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * PlateType.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/31/2007	Created
 * 							defect 9085 Ver Special Plates 
 * K Harrell	02/07/2007	Joined to RTS_ITEM_CODES for Plate 
 * 							description
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/17/2007	Added check for MFGPNDINGINDI
 * 							modify qryPlateType() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/10/2007	Use Pilot.RTS_ITEM_CODES
 * 							modify qryPlateType() 
 * 							defect 9085 Ver Special Plates 
 * K Harrell	10/14/2007	Remove use of Pilot.RTS_ITEM_CODES
 * 							Add use of Pilot.RTS_PLT_TYPE 
 * 							modify qryPlateType()
 * 							defect 9354 Ver Special Plates 2
 * K Harrell	01/17/2008	Remove reference to PILOT qualifier
 * 							modify qryPlateType()
 * 							defect 9525 Ver 3 Amigos Prep 
 * B Hargrove	07/15/2008	Again, use reference to PILOT qualifier
 * 							for RTS_PLT_TYPE.
 * 							Add new field for Vendor Plate Indi.
 * 							modify qryPlateType()
 * 							defect 9689 Ver MyPlates_POS 
 * K Harrell	03/01/2010	add DROPFIRSTCHARINDI, 
 * 							 VENDORELGBLEINDI; delete  
 * 								ExpSynchCd, MfgDateOffsetCd
 * 							modify qryPlateType()
 * 							defect 10366 Ver POS_640 
 * K Harrell	12/08/2010	add CROSSOVERINDI, RESTYLEACCTITMCD,
 * 						     REPLACCTITMCD to SQL 
 * 							add csMethod 
 * 							modify qryPlateType()
 * 							defect 10695 Ver 6.7.0
 * Ray Rowehl	01/05/2011	Remove RESTYLEACCTITMCD,CROSSOVERINDI
 * 							as was dropped from the table.
 * 							defect 10695 Ver 6.7.0
 * K Harrell	08/22/2011	add PltSizeCd 
 * 							modify qryPlateType()
 * 							defect 10804 Ver 6.8.1
 * K Harrell	09/30/2011	Remove reference to Pilot qualifier
 * 							modify qryPlateType() 
 * 							defect 11048 Ver 6.9.0  
 * K Harrell	10/28/2011	Restore reference to Pilot qualifier
 * 							modify qryPlateType() 
 * 							defect 11061 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_PLT_TYPE
 * 
 * @version 6.9.0  		10/28/2011
 * @author 	Kathy Harrell
 * @since 				01/31/2007 16:01:00
 */

public class PlateType extends PlateTypeData
{
	// defect 10695
	private String csMethod = new String();
	// end defect 10695

	DatabaseAccess caDA;

	/**
	 * PaymentType constructor comment.
	 * 
	 * @param aaDA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public PlateType(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_PLT_TYPE
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryPlateType() throws RTSException
	{
		csMethod = "qryPlateType";

		Log.write(Log.METHOD, this, csMethod + " - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		
		// defect 11061
		 if (SystemProperty.isStaticTablePilot())
		 {
			 lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		 }
		 // end defect 11061 
		 
  	    // defect 11061
		//  add PLPDVIndi  
		// defect 10804
		// add PltSizeCd
		// defect 10695
		// add REPLACCTITMCD
		// defect 10366
		// add VendorElgbleIndi, DropFirstCharIndi
		// delete ExpSynchCd, MfgDateOffsetCd
		lsQry
				.append("SELECT "
						+ " REGPLTCD,"
						+ " ITMCDDESC AS REGPLTCDDESC,"
						+ " RTSEFFDATE,"
						+ " RTSEFFENDDATE,"
						+ " MANDPLTREPLAGE,"
						+ " OPTPLTREPLAGE,"
						+ " ANNUALPLTINDI,"
						+ " REPLFEE,"
						+ " REGRENWLCD,"
						+ " PLTSURCHARGEINDI,"
						+ " USERPLTNOINDI,"
						+ " DUPLSALLOWDCD,"
						+ " REGPLTDESIGN,"
						+ " SPCLPLTTYPE,"
						+ " PLTSETIMPRTNCECD,"
						+ " SPCLPRORTNINCRMNT,"
						+ " PLTOWNRSHPCD,"
						+ " LOCCNTYAUTHCD,"
						+ " LOCREGIONAUTHCD,"
						+ " LOCHQAUTHCD,"
						+ " LOCINETAUTHCD,"
						+ " RENWLRTRNADDRCD,"
						+ " SHPNGADDRCD,"
						+ " TRNSFRCD ,"
						+ " PLPACCTITMCD,"
						+ " FIRSTSETAPPLFEE ,"
						+ " FIRSTSETRENWLFEE ,"
						+ " ADDLSETAPPLFEE,"
						+ " ADDLSETRENWLFEE,"
						+ " PLPFEE ,"
						+ " DISPPLTGRPID,"
						+ " LIMITEDPLTGRPID,"
						+ " BASEREGPLTCD,"
						+ " NEEDSPROGRAMCD,"
						+ " ISAALLOWDCD,"
						+ " MAXBYTECOUNT,"
						+ " MFGPROCSCD,"
						+ " REMAKEFEE,"
						+ " VENDORPLTINDI,"
						+ " VENDORELGBLEINDI,"
						+ " DROPFIRSTCHARINDI, "
						+ " REPLACCTITMCD, "
						+ " PLTSIZECD, "
						+ " PLPDVINDI "
						+ " FROM "
						+ lsTableCreator
						+ ".RTS_PLT_TYPE A, "
						+ "RTS.RTS_ITEM_CODES B"
						+ " WHERE A.REGPLTCD = B.ITMCD AND PLTOWNRSHPCD NOT IN ('V') "
						+ " AND EXISTS (SELECT * FROM RTS.RTS_ORG_NO M "
						+ " WHERE A.BASEREGPLTCD = M.BASEREGPLTCD AND "
						+ " M.MFGPNDINGINDI = 0) "
						+ " UNION ALL  "
						+ " SELECT "
						+ " REGPLTCD,"
						+ " ITMCDDESC AS REGPLTCDDESC,"
						+ " RTSEFFDATE,"
						+ " RTSEFFENDDATE,"
						+ " MANDPLTREPLAGE,"
						+ " OPTPLTREPLAGE,"
						+ " ANNUALPLTINDI,"
						+ " REPLFEE,"
						+ " REGRENWLCD,"
						+ " PLTSURCHARGEINDI,"
						+ " USERPLTNOINDI,"
						+ " DUPLSALLOWDCD,"
						+ " REGPLTDESIGN,"
						+ " SPCLPLTTYPE,"
						+ " PLTSETIMPRTNCECD,"
						+ " SPCLPRORTNINCRMNT,"
						+ " PLTOWNRSHPCD,"
						+ " LOCCNTYAUTHCD,"
						+ " LOCREGIONAUTHCD,"
						+ " LOCHQAUTHCD,"
						+ " LOCINETAUTHCD,"
						+ " RENWLRTRNADDRCD,"
						+ " SHPNGADDRCD,"
						+ " TRNSFRCD ,"
						+ " PLPACCTITMCD,"
						+ " FIRSTSETAPPLFEE ,"
						+ " FIRSTSETRENWLFEE ,"
						+ " ADDLSETAPPLFEE,"
						+ " ADDLSETRENWLFEE,"
						+ " PLPFEE ,"
						+ " DISPPLTGRPID,"
						+ " LIMITEDPLTGRPID,"
						+ " BASEREGPLTCD,"
						+ " NEEDSPROGRAMCD,"
						+ " ISAALLOWDCD,"
						+ " MAXBYTECOUNT,"
						+ " MFGPROCSCD,"
						+ " REMAKEFEE,"
						+ " VENDORPLTINDI,"
						+ " VENDORELGBLEINDI,"
						+ " DROPFIRSTCHARINDI, "
						+ " REPLACCTITMCD, "
						+ " PLTSIZECD, "
						+ " PLPDVINDI "
						+ " FROM "
						+ lsTableCreator
						+ ".RTS_PLT_TYPE A, "
						+ " RTS.RTS_ITEM_CODES B"
						+ " WHERE A.REGPLTCD = B.ITMCD AND PLTOWNRSHPCD = 'V' "
						+ "	ORDER BY REGPLTCD");
		// end defect 10366
		// end defect 10695
		// end defect 10804

		try
		{
			Log.write(Log.SQL, this, csMethod + " - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this, csMethod + " - SQL - End");

			while (lrsQry.next())
			{
				PlateTypeData laPlateTypeData = new PlateTypeData();
				laPlateTypeData.setRegPltCd(caDA.getStringFromDB(
						lrsQry, "RegPltCd"));

				laPlateTypeData.setRegPltCdDesc(caDA.getStringFromDB(
						lrsQry, "RegPltCdDesc"));

				laPlateTypeData.setRTSEffDate(caDA.getIntFromDB(lrsQry,
						"RTSEffDate"));
				laPlateTypeData.setRTSEffEndDate(caDA.getIntFromDB(
						lrsQry, "RTSEffEndDate"));
				laPlateTypeData.setMandPltReplAge(caDA.getIntFromDB(
						lrsQry, "MandPltReplAge"));
				laPlateTypeData.setOptPltReplAge(caDA.getIntFromDB(
						lrsQry, "OptPltReplAge"));
				laPlateTypeData.setAnnualPltIndi(caDA.getIntFromDB(
						lrsQry, "AnnualPltIndi"));
				laPlateTypeData.setReplFee(caDA.getDollarFromDB(lrsQry,
						"ReplFee"));
				laPlateTypeData.setRegRenwlCd(caDA.getIntFromDB(lrsQry,
						"RegRenwlCd"));
				laPlateTypeData.setPltSurchargeIndi(caDA.getIntFromDB(
						lrsQry, "PLTSURCHARGEINDI"));
				laPlateTypeData.setUserPltNoIndi(caDA.getIntFromDB(
						lrsQry, "USERPLTNOINDI"));
				laPlateTypeData.setDuplsAllowdCd(caDA.getIntFromDB(
						lrsQry, "DUPLSALLOWDCD"));
				laPlateTypeData.setRegPltDesign(caDA.getStringFromDB(
						lrsQry, "REGPLTDESIGN"));
				laPlateTypeData.setSpclPltType(caDA.getStringFromDB(
						lrsQry, "SPCLPLTTYPE"));
				laPlateTypeData.setPltSetImprtnceCd(caDA.getIntFromDB(
						lrsQry, "PltSetImprtnceCd"));
				laPlateTypeData.setSpclPrortnIncrmnt(caDA.getIntFromDB(
						lrsQry, "SpclPrortnIncrmnt"));
				laPlateTypeData.setPltOwnrshpCd(caDA.getStringFromDB(
						lrsQry, "PLTOWNRSHPCD"));
				laPlateTypeData.setLocCntyAuthCd(caDA.getStringFromDB(
						lrsQry, "LOCCNTYAUTHCD"));
				laPlateTypeData.setLocRegionAuthCd(caDA
						.getStringFromDB(lrsQry, "LOCREGIONAUTHCD"));
				laPlateTypeData.setLocHQAuthCd(caDA.getStringFromDB(
						lrsQry, "LOCHQAUTHCD"));
				laPlateTypeData.setLocInetAuthCd(caDA.getStringFromDB(
						lrsQry, "LOCINETAUTHCD"));
				laPlateTypeData.setRenwlRtrnAddrCd(caDA
						.getStringFromDB(lrsQry, "RENWLRTRNADDRCD"));
				laPlateTypeData.setShpngAddrCd(caDA.getStringFromDB(
						lrsQry, "SHPNGADDRCD"));
				laPlateTypeData.setTrnsfrCd(caDA.getStringFromDB(
						lrsQry, "TRNSFRCD"));
				laPlateTypeData.setPLPAcctItmCd(caDA.getStringFromDB(
						lrsQry, "PLPACCTITMCD"));
				laPlateTypeData.setFirstSetApplFee(caDA
						.getDollarFromDB(lrsQry, "FIRSTSETAPPLFEE"));
				laPlateTypeData.setFirstSetRenwlFee(caDA
						.getDollarFromDB(lrsQry, "FIRSTSETRENWLFEE"));
				laPlateTypeData.setAddlSetApplFee(caDA.getDollarFromDB(
						lrsQry, "ADDLSETAPPLFEE"));
				laPlateTypeData.setAddlSetRenwlFee(caDA
						.getDollarFromDB(lrsQry, "ADDLSETRENWLFEE"));
				laPlateTypeData.setPLPFee(caDA.getDollarFromDB(lrsQry,
						"PLPFEE"));
				laPlateTypeData.setDispPltGrpId(caDA.getStringFromDB(
						lrsQry, "DISPPLTGRPID"));
				laPlateTypeData.setLimitedPltGrpId(caDA
						.getStringFromDB(lrsQry, "LIMITEDPLTGRPID"));
				laPlateTypeData.setBaseRegPltCd(caDA.getStringFromDB(
						lrsQry, "BASEREGPLTCD"));
				laPlateTypeData.setNeedsProgramCd(caDA.getStringFromDB(
						lrsQry, "NEEDSPROGRAMCD"));
				laPlateTypeData.setISAAllowdCd(caDA.getStringFromDB(
						lrsQry, "ISAALLOWDCD"));
				laPlateTypeData.setMaxByteCount(caDA.getIntFromDB(
						lrsQry, "MAXBYTECOUNT"));
				laPlateTypeData.setMfgProcsCd(caDA.getIntFromDB(lrsQry,
						"MFGPROCSCD"));
				laPlateTypeData.setRemakeFee(caDA.getDollarFromDB(
						lrsQry, "REMAKEFEE"));
				// defect 9689
				laPlateTypeData.setVendorPltIndi(caDA.getIntFromDB(
						lrsQry, "VENDORPLTINDI"));
				// end defect 9689

				// defect 10366
				laPlateTypeData.setVendorElgbleIndi(caDA.getIntFromDB(
						lrsQry, "VENDORELGBLEINDI"));
				laPlateTypeData.setDropFirstCharIndi(caDA.getIntFromDB(
						lrsQry, "DROPFIRSTCHARINDI"));
				// end defect 10366

				// defect 10695
				laPlateTypeData.setReplAcctItmCd(caDA.getStringFromDB(
						lrsQry, "REPLACCTITMCD"));
				// end defect 10695

				// defect 10804
				laPlateTypeData.setPltSizeCd(caDA.getStringFromDB(
						lrsQry, "PLTSIZECD"));
				// end defect 10804
				
				// defect 11061 
				laPlateTypeData.setPLPDVIndi(caDA.getIntFromDB(
						lrsQry, "PLPDVINDI"));
				// end defect 11061 
				
				// Add element to the Vector
				lvRslt.addElement(laPlateTypeData);
			} // End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this, csMethod + " - End ");
			return (lvRslt);
		}
		// defect 10366
		catch (RTSException aeRTSEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod + " - Exception - "
					+ aeRTSEx.getMessage());
			throw aeRTSEx;
		}
		// end defect 10366
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this, csMethod
					+ " - SQL Exception " + aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} // END OF QUERY METHOD

} // END OF CLASS
