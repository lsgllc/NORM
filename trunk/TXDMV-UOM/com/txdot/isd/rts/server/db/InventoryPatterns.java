package com.txdot.isd.rts.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.InventoryPatternsData;
import com.txdot.isd.rts.services.data.InventoryPatternsDescriptionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 *
 * InventoryPatterns.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   11/15/2001  Added qryInventoryPatternsDescription
 * R Hicks    	07/12/2002  Add call to closeLastStatement() after a
 * 						 	query
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modified qryInventoryPatternsDescription.
 * 							Ver 5.2.0
 * K Harrell	03/03/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/04/2005	Remove parameter from qryInventoryPatterns()
 * 							defect 7846 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	11/14/2006	Use functionality for piloting item codes
 * 							table data
 * 							modify qryInventoryPatternsDescription()
 * 							defect 9013 Ver Exempts
 * K Harrell	02/05/2007	Commented out test for pilot
 * 							modify qryInventoryPatternsDescription()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/06/2007	Added new columns; Modified SQL to omit 
 * 							Virtual Inventory 
 *							modify qryInventoryPatterns(),
 *							 	qryInventoryPatternsDescription()
 * 							defect 9085 Ver SpecialPlates  
 * Ray Rowehl	04/02/2007	Enhance to allow Virtual Item Codes to 
 * 							be shown at HQ.
 * 							modify qryInventoryPatternsDescription()
 * 							defect 9116 Ver SpecialPlates
 * K Harrell	04/09/2007  Reinstated PilotCreator
 * 							modify qryInventoryPatternsDescription()
 * 							defect 9085 Ver Special Plates  
 * Ray Rowehl	06/18/2007	Order the patterns query.
 * 							modify qryInventoryPatterns()
 * 							defect 9116 Ver SpecialPlates
 * K Harrell	07/08/2007	Add query to present VI item descriptions
 * 							upon request on INV022.
 * 							add qryVIInventoryPatternsDescription()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/07/2007	SQL was omitting patterned plates, omitting
 * 							year on PLP plates (if applicable). 
 * 							Augmented to add year for PLP Annual Plates
 * 							Omit OLDPLT/OLDPLT2/ROP. Added LP Plates. 
 * 							modify qryVIInventoryPatternsDescription()
 * 							defect 9215 Ver Special Plates 
 * K Harrell	11/12/2007	Do not reference pilot table data for 
 * 							 RTS_ITEM_CODES. Add for RTS_PLT_TYPE. 
 * 							modify qryInventoryPatternsDescription(),
 * 							 qryVIInventoryPatternsDescription()
 *							defect 9354 Ver Special Plates 2
 * K Harrell	01/17/2008	Remove reference to PILOT qualifier
 * 							modify qryVIInventoryPatternsDescription()
 * 							defect 9525 Ver 3 Amigos Prep
 * Ray Rowehl	06/26/2008	Add pilot for RTS_PLT_TYPE.  
 * 							modify qryVIInventoryPatternsDescription()
 * 							defect 9689 Ver MyPlates_POS	
 * K Harrell	09/30/2011	Remove reference to pilot qualifier
 * 							modify qryVIInventoryPatternsDescription()  
 * 							defect 11048 Ver 6.9.0  
 * K Harrell	10/28/2011	Restore reference to pilot qualifier
 * 							modify qryVIInventoryPatternsDescription(), 
 * 							 qryInventoryPatternsDescription()  
 * 							defect 11061 Ver 6.9.0                
 * ---------------------------------------------------------------------
 */

/**
 * This class allows user to access RTS_INV_PATTERNS a) Select * from
 * RTS.RTS_INV_PATTERNS b) Select w/ join of RTS_INV_PATTERNS & RTS_ITEM_CODES
 * to derive descriptions
 * 
 * @version 6.9.0 	10/28/20111
 * @author 	Kathy Harrell
 * @since 			08/31/2001
 */

public class InventoryPatterns extends InventoryPatternsData
{
	DatabaseAccess caDA;

	/**
	 * InventoryPatterns constructor comment.
	 * 
	 * @param aaDA
	 *            DatabaseAccess
	 * @throws RTSException
	 */
	public InventoryPatterns(DatabaseAccess aaDA) throws RTSException
	{
		caDA = aaDA;
	}

	/**
	 * Method to Query RTS.RTS_INV_PATTERNS
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryInventoryPatterns() throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryInventoryPatterns - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;

		// defect 9085
		// Added columns VIItmCd, ISAIndi
		lsQry.append("SELECT " + "ItmCd," + "InvItmYr,"
				+ "InvItmPatrnCd," + "InvItmNo," + "InvItmEndNo,"
				+ "ValidInvLtrs," + "PatrnSeqCd, " + "VIItmCd,"
				+ "ISAIndi " + "FROM RTS.RTS_INV_PATTERNS "
				+ "ORDER by 1, 2, 3");
		try
		{
			Log.write(Log.SQL, this,
					" - qryInventoryPatterns - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this,
					" - qryInventoryPatterns - SQL - End");
			while (lrsQry.next())
			{
				InventoryPatternsData laInventoryPatternsData = new InventoryPatternsData();
				laInventoryPatternsData.setItmCd(caDA.getStringFromDB(
						lrsQry, "ItmCd"));
				laInventoryPatternsData.setInvItmYr(caDA.getIntFromDB(
						lrsQry, "InvItmYr"));
				laInventoryPatternsData.setInvItmPatrnCd(caDA
						.getIntFromDB(lrsQry, "InvItmPatrnCd"));
				laInventoryPatternsData.setInvItmNo(caDA
						.getStringFromDB(lrsQry, "InvItmNo"));
				laInventoryPatternsData.setInvItmEndNo(caDA
						.getStringFromDB(lrsQry, "InvItmEndNo"));
				laInventoryPatternsData.setValidInvLtrs(caDA
						.getStringFromDB(lrsQry, "ValidInvLtrs"));
				laInventoryPatternsData.setPatrnSeqCd(caDA
						.getIntFromDB(lrsQry, "PatrnSeqCd"));
				// defect 9085
				laInventoryPatternsData.setISAIndi(caDA.getIntFromDB(
						lrsQry, "ISAIndi"));
				laInventoryPatternsData.setVIItmCd(caDA
						.getStringFromDB(lrsQry, "VIItmCd"));
				// end defect 9085
				// Add element to the Vector
				lvRslt.addElement(laInventoryPatternsData);
			} // End of While
			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this,
					" - qryInventoryPatterns - End ");
			return (lvRslt);
		}
		catch (SQLException leSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qryInventoryPatterns - SQL Exception "
							+ leSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, leSQLEx);
		}
	} // END OF QUERY METHOD

	/**
	 * Method to Query RTS_INV_PATTERNS joined to RTS_ITEM_CODES for those
	 * Descriptions available for Virtual Inventory
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryVIInventoryPatternsDescription()
			throws RTSException
	{
		Log.write(Log.METHOD, this,
				" - qryVIInventoryPatternsDescription - Begin");

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

		// defect 9215
		// Virtual Item Codes - Join on VIItmCd
		lsQry.append("SELECT DISTINCT " + " A.VIItmCd as ItmCd,"
				+ " ItmCdDesc, " + " 0 as InvItmYr "
				+ " FROM RTS.RTS_INV_PATTERNS A,"
				+ " RTS.RTS_ITEM_CODES B WHERE "
				+ " A.VIItmCd = B.ITMCD AND "
				+ " A.VIITMCD <> A.ITMCD ");

		// Special Plates - Patterned - Join on ItmCd, RegPltCd
		lsQry.append(" UNION  " + " SELECT DISTINCT " + " A.ItmCd,"
				+ " ItmCdDesc, " + " InvItmYr "
				+ " FROM RTS.RTS_INV_PATTERNS A,"
				+ " RTS.RTS_ITEM_CODES B, " + lsTableCreator
				+ ".RTS_PLT_TYPE C WHERE " + " A.ITMCD = B.ITMCD AND "
				+ " A.ITMCD = C.REGPLTCD AND "
				+ " C.PLTOWNRSHPCD NOT IN ('V') AND "
				+ " A.VIITMCD IS NOT NULL AND A.VIITMCD <> '' AND "
				+ " A.VIITMCD <> A.ITMCD ");

		// PLP Plates - Not Annual
		// "UserPltNoIndi = 1 and AnnualPltIndi = 0 and
		// RegPltCd not in ('OLDPLT','ROP') "
		lsQry.append(" UNION  " + " SELECT  " + " A.RegPltCd as ItmCd,"
				+ " B.ItmCdDesc," + 0 + " as InvItmYr " + " FROM "
				+ lsTableCreator + ".RTS_PLT_TYPE A, "
				+ " RTS.RTS_ITEM_CODES B WHERE "
				+ " A.REGPLTCD = B.ITMCD AND "
				+ " USERPLTNOINDI = 1 AND " + " ANNUALPLTINDI = 0 AND "
				+ " NEEDSPROGRAMCD NOT IN ('O') AND "
				+ " REGPLTCD NOT IN ('ROP') AND "
				+ " PLTOWNRSHPCD NOT IN ('V') ");

		// Annual PLP Plates
		// "UserPltNoIndi = 1 and AnnualPltIndi = 1 "
		// Current Year plus next two
		int liYear = new RTSDate().getYear();

		for (int i = 0; i < 3; i++)
		{
			lsQry.append(" UNION  " + " SELECT  "
					+ " A.RegPltCd as ItmCd," + " B.ItmCdDesc,"
					+ liYear + " as InvItmYr " + " FROM "
					+ lsTableCreator + ".RTS_PLT_TYPE A, "
					+ " RTS.RTS_ITEM_CODES B WHERE "
					+ " A.REGPLTCD = B.ITMCD AND "
					+ " USERPLTNOINDI = 1 AND "
					+ " ANNUALPLTINDI = 1 AND "
					+ " PLTOWNRSHPCD NOT IN ('V') ");
			liYear = liYear + 1;
		}

		// LP Plates - which happen to be Annual
		// Cannot Join to INV_PATTERNS
		// Current Year plus next two

		liYear = new RTSDate().getYear();
		for (int i = 0; i < 3; i++)
		{
			lsQry.append(" UNION  " + " SELECT "
					+ " A.RegPltCd as ItmCd," + " B.ItmCdDesc,"
					+ liYear + " as InvItmYr " + " FROM "
					+ lsTableCreator + ".RTS_PLT_TYPE A, "
					+ " RTS.RTS_ITEM_CODES B WHERE "
					+ " A.REGPLTCD = B.ITMCD AND "
					+ " NEEDSPROGRAMCD = 'L' AND "
					+ " ANNUALPLTINDI = 1 AND "
					+ " PLTOWNRSHPCD NOT IN ('V') ");

			liYear = liYear + 1;
		}
		// end defect 9525

		lsQry.append(" ORDER BY 1,3 ");

		try
		{
			Log
					.write(Log.SQL, this,
							" - qryVIInventoryPatternsDescription - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this,
					" - qryVIInventoryPatternsDescription - SQL - End");

			while (lrsQry.next())
			{
				InventoryPatternsDescriptionData laInventoryPatternsDescriptionData = new InventoryPatternsDescriptionData();
				laInventoryPatternsDescriptionData.setItmCd(caDA
						.getStringFromDB(lrsQry, "ItmCd"));
				laInventoryPatternsDescriptionData.setItmCdDesc(caDA
						.getStringFromDB(lrsQry, "ItmCdDesc"));
				laInventoryPatternsDescriptionData.setInvItmYr(caDA
						.getIntFromDB(lrsQry, "InvItmYr"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryPatternsDescriptionData);
			} // End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this,
					" - qryVIInventoryPatternsDescription - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qryVIInventoryPatternsDescription - SQL Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} // END OF QUERY METHOD

	/**
	 * Method to Query RTS_INV_PATTERNS joined to RTS_ITEM_CODES for
	 * Descriptions
	 * 
	 * @param aaGeneralSearchData
	 *            GeneralSearchData
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryInventoryPatternsDescription(
			GeneralSearchData aaGeneralSearchData) throws RTSException
	{
		Log.write(Log.METHOD, this,
				" - qryInventoryPatternsDescription - Begin");

		StringBuffer lsQry = new StringBuffer();

		Vector lvRslt = new Vector();

		ResultSet lrsQry;
		
		// defect 11061 
		//		String lsTableCreator = CommonConstant.RTS_TBCREATOR;
		//		if (SystemProperty.isStaticTablePilot())
		//		{
		//			lsTableCreator = CommonConstant.PILOT_TBCREATOR;
		//		}
		// end defect 11061 

		lsQry.append("SELECT DISTINCT " + " A.ItmCd," + " B.ItmCdDesc,"
				+ " A.InvItmYr " + " FROM RTS.RTS_INV_PATTERNS A, "
				+ " RTS.RTS_ITEM_CODES B WHERE "
				+ " A.ITMCD = B.ITMCD " + " AND PRINTABLEINDI <> 1");

		// If OfcIssuanceCd = 2 (Region) or 1 (HeadQuarters),
		// then display all inventory
		// Otherwise not.
		if (aaGeneralSearchData.getIntKey1() != 2
				&& aaGeneralSearchData.getIntKey1() != 1)
		{
			lsQry.append(" AND B.INVPROCSNGCD in (1,2)");
		}

		// TODO consider using PltOwnrShpCd of "O" instead.
		// What do we use "E" for?
		// HQ does not use normal Inventory.

		// if HeadQuarters, allow Virtual as well.
		// Otherwise do not.
		if (aaGeneralSearchData.getIntKey1() != 1)
		{
			lsQry.append(" AND B.ITMTRCKNGTYPE <> 'V'");
		}

		lsQry.append(" UNION ALL "
				+ " SELECT A.ItmCd, A.ItmCdDesc, 0 as InvItmYr "
				+ " FROM " + " RTS.RTS_ITEM_CODES A WHERE "
				+ " PRINTABLEINDI <> 1 ");
		// end defect 9354

		// If OfcIssuanceCd = 2 (Region) or 1 (HeadQuarters),
		// then display all inventory
		// Otherwise not.
		if (aaGeneralSearchData.getIntKey1() != 2
				&& aaGeneralSearchData.getIntKey1() != 1)
		{
			lsQry.append(" AND A.InvProcsngCd in (1,2) ");
		}

		lsQry
				.append(" AND NOT EXISTS (SELECT * FROM RTS.RTS_INV_PATTERNS B "
						+ " WHERE A.ITMCD = B.ITMCD) "
						+ " ORDER BY 1,3 ");
		try
		{
			Log.write(Log.SQL, this,
					" - qryInventoryPatternsDescription - SQL - Begin");
			lrsQry = caDA.executeDBQuery(lsQry.toString(), null);
			Log.write(Log.SQL, this,
					" - qryInventoryPatternsDescription - SQL - End");

			while (lrsQry.next())
			{
				InventoryPatternsDescriptionData laInventoryPatternsDescriptionData = new InventoryPatternsDescriptionData();
				laInventoryPatternsDescriptionData.setItmCd(caDA
						.getStringFromDB(lrsQry, "ItmCd"));
				laInventoryPatternsDescriptionData.setItmCdDesc(caDA
						.getStringFromDB(lrsQry, "ItmCdDesc"));
				laInventoryPatternsDescriptionData.setInvItmYr(caDA
						.getIntFromDB(lrsQry, "InvItmYr"));
				// Add element to the Vector
				lvRslt.addElement(laInventoryPatternsDescriptionData);
			} // End of While

			lrsQry.close();
			caDA.closeLastDBStatement();
			lrsQry = null;
			Log.write(Log.METHOD, this,
					" - qryInventoryPatternsDescription - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(Log.SQL_EXCP, this,
					" - qryInventoryPatternsDescription - SQL Exception "
							+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} // END OF QUERY METHOD
} // END OF CLASS
